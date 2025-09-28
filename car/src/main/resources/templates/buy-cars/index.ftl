<link href="/css/buy-cars.css" rel="stylesheet">

<div class="buy-cars-page" id="app">
    <!-- 搜索框 -->
    <div class="search-header">
        <div class="container">
            <div class="search-box">
                <div class="search-input-group">
                    <input type="text" class="form-control search-input" placeholder="搜尋車型、品牌或關鍵詞..." v-model="searchForm.keyword" @keyup.enter="searchCars">
                    <button class="btn search-btn" @click="searchCars">
                        <i class="bi bi-search"></i>
                    </button>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="row">
            <!-- 左侧筛选条件 -->
            <div class="col-lg-3 col-md-4">
                <div class="filter-sidebar">
                    <h4 class="filter-title">篩選條件</h4>
                    
                    <!-- 品牌筛选 -->
                    <div class="filter-section">
                        <h5 class="filter-section-title">品牌</h5>
                        <div class="filter-options">
                            <label class="filter-option" v-for="brandItem in visibleBrands" :key="brandItem.brand">
                                <input type="checkbox" :value="brandItem.brand" v-model="searchForm.brand">
                                <span class="checkmark"></span>
                                <span v-text="brandItem.brand"></span>
                                <span class="brand-count">({{ brandItem.saleCount }})</span>
                            </label>
                        </div>
                        <div class="brand-toggle" v-if="brandSaleCountList.length > 8">
                            <button type="button" class="btn btn-link btn-sm" @click="toggleBrandExpanded">
                                {{ brandExpanded ? '收起' : '展开全部' }}
                                <i :class="brandExpanded ? 'bi bi-chevron-up' : 'bi bi-chevron-down'"></i>
                            </button>
                        </div>
                    </div>

                    <!-- 价格范围 -->
                    <div class="filter-section">
                        <h5 class="filter-section-title">價格範圍</h5>
                        <div class="price-range">
                            <input type="number" class="form-control" placeholder="最低價格" v-model="searchForm.priceMin">
                            <span class="price-separator">-</span>
                            <input type="number" class="form-control" placeholder="最高價格" v-model="searchForm.priceMax">
                        </div>
                    </div>

                    <!-- 年份 -->
                    <div class="filter-section">
                        <h5 class="filter-section-title">年份</h5>
                        <select class="form-control" v-model="searchForm.year">
                            <option value="">所有年份</option>
                            <option v-for="year in filterOptions.years" :key="year" :value="year" v-text="year"></option>
                        </select>
                    </div>


                    <!-- 变速系统 -->
                    <div class="filter-section">
                        <h5 class="filter-section-title">變速系統</h5>
                        <div class="filter-options">
                            <label class="filter-option" v-for="transmission in filterOptions.transmissions" :key="transmission">
                                <input type="checkbox" :value="transmission" v-model="searchForm.transmission">
                                <span class="checkmark"></span>
                                <span v-text="transmission"></span>
                            </label>
                        </div>
                    </div>

                    <!-- 驱动方式 -->
                    <div class="filter-section">
                        <h5 class="filter-section-title">驅動方式</h5>
                        <div class="filter-options">
                            <label class="filter-option" v-for="drivetrain in filterOptions.drivetrains" :key="drivetrain">
                                <input type="checkbox" :value="drivetrain" v-model="searchForm.drivetrain">
                                <span class="checkmark"></span>
                                <span v-text="drivetrain"></span>
                            </label>
                        </div>
                    </div>

                    <!-- 燃料系统 -->
                    <div class="filter-section">
                        <h5 class="filter-section-title">燃料系統</h5>
                        <div class="filter-options">
                            <label class="filter-option" v-for="fuelSystem in filterOptions.fuelSystems" :key="fuelSystem">
                                <input type="checkbox" :value="fuelSystem" v-model="searchForm.fuelSystem">
                                <span class="checkmark"></span>
                                <span v-text="fuelSystem"></span>
                            </label>
                        </div>
                    </div>

                    <!-- 应用筛选按钮 -->
                    <button class="btn btn-primary apply-filters-btn" @click="searchCars">
                        <i class="bi bi-funnel"></i> 應用篩選
                    </button>
                </div>
            </div>

            <!-- 右侧车辆列表 -->
            <div class="col-lg-9 col-md-8">
                <div class="cars-content">
                    <!-- 结果统计 -->
                    <div class="results-header">
                        <div class="results-count">
                            找到 <strong v-text="totalCars"></strong> 輛車
                        </div>
                        <div class="sort-options">
                            <select class="form-control sort-select" v-model="searchForm.sortBy" @change="searchCars">
                                <option value="newest">最新發布</option>
                                <option value="price-low">價格從低到高</option>
                                <option value="price-high">價格從高到低</option>
                                <option value="year-new">年份最新</option>
                            </select>
                        </div>
                    </div>

                    <!-- 加载状态 -->
                    <div v-if="loading" class="loading-container">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">加载中...</span>
                        </div>
                        <p>正在加载车辆数据...</p>
                    </div>

                    <!-- 车辆网格 -->
                    <div v-else class="cars-grid">
                        <div v-for="car in cars" :key="car.id" class="car-card" @click="goToCarDetail(car.id)">
                            <div class="car-image-container">
                                <img :src="car.coverImage || '/img/car/car6.jpg'" :alt="car.saleTitle" class="car-image" @error="handleImageError">
                                <div class="car-badge" v-text="car.manufactureYear"></div>
                            </div>
                            <div class="car-info">
                                <h3 class="car-title" v-text="(car.brand || '') + ' ' + (car.model || '')"></h3>
                                <div class="car-price-section">
                                    <span class="price-amount" v-text="'$' + formatPrice(car.salePrice)"></span>
                                </div>
                                <div class="car-specs">
                                    <div class="spec-item" v-if="car.fuelSystem">
                                        <div class="spec-content">
                                            <div class="spec-label">燃料类型</div>
                                            <div class="spec-value" v-text="car.fuelSystem"></div>
                                        </div>
                                    </div>
                                    <div class="spec-item" v-if="car.mileage">
                                        <div class="spec-content">
                                            <div class="spec-label">里程</div>
                                            <div class="spec-value" v-text="formatMileage(car.mileage) + ' km'"></div>
                                        </div>
                                    </div>
                                    <div class="spec-item" v-if="car.transmission">
                                        <div class="spec-content">
                                            <div class="spec-label">变速箱</div>
                                            <div class="spec-value" v-text="car.transmission"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 无数据提示 -->
                    <div v-if="!loading && cars.length === 0" class="no-data-container">
                        <div class="no-data-icon">
                            <i class="bi bi-car-front"></i>
                        </div>
                        <h3>暂无车辆数据</h3>
                        <p>请尝试调整筛选条件或稍后再试</p>
                    </div>

                    <!-- 分页 -->
                    <div v-if="totalPages > 1" class="pagination-container">
                        <nav aria-label="車輛列表分頁">
                            <ul class="pagination justify-content-center">
                                <li class="page-item" :class="{ disabled: currentPage <= 1 }">
                                    <a class="page-link" href="#" @click.prevent="changePage(currentPage - 1)">上一頁</a>
                                </li>
                                
                                <li v-for="pageNum in visiblePages" :key="pageNum" class="page-item" :class="{ active: pageNum === currentPage }">
                                    <a class="page-link" href="#" @click.prevent="changePage(pageNum)" v-text="pageNum"></a>
                                </li>
                                
                                <li class="page-item" :class="{ disabled: currentPage >= totalPages }">
                                    <a class="page-link" href="#" @click.prevent="changePage(currentPage + 1)">下一頁</a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
new Vue({
    el: '#app',
    data: {
        loading: false,
        cars: [],
        totalCars: 0,
        currentPage: 1,
        totalPages: 0,
        pageSize: 24,
        searchForm: {
            keyword: '',
            brand: [],
            model: '',
            year: '',
            priceMin: '',
            priceMax: '',
            sortBy: 'newest',
            pageNum: 1,
            pageSize: 24,
            // 新增筛选字段
            transmission: [],
            drivetrain: [],
            fuelSystem: []
        },
        filterOptions: {
            years: [2024, 2023, 2022, 2021, 2020, 2019, 2018, 2017, 2016],
            transmissions: [],
            drivetrains: [],
            fuelSystems: []
        },
        brandSaleCountList: [],
        brandExpanded: false
    },
    computed: {
        visiblePages() {
            const pages = [];
            const start = Math.max(1, this.currentPage - 2);
            const end = Math.min(this.totalPages, this.currentPage + 2);
            
            for (let i = start; i <= end; i++) {
                pages.push(i);
            }
            return pages;
        },
        visibleBrands() {
            if (this.brandExpanded) {
                return this.brandSaleCountList;
            } else {
                return this.brandSaleCountList.slice(0, 8);
            }
        }
    },
    mounted() {
        this.loadCars();
        this.loadBrandSaleCount();
        this.loadCarFilterOptions();
    },
    methods: {
        async loadCars() {
            try {
                this.loading = true;
                this.searchForm.pageNum = this.currentPage;
                this.searchForm.pageSize = this.pageSize;
                
                const response = await axios.post('/api/car-sales/list', this.searchForm);
                
                if (response.data.success) {
                    this.cars = response.data.data.list || [];
                    this.totalCars = response.data.data.total || 0;
                    this.totalPages = response.data.data.pages || 0;
                } else {
                    console.error('加载车辆数据失败:', response.data.message);
                    this.cars = [];
                    this.totalCars = 0;
                    this.totalPages = 0;
                }
            } catch (error) {
                console.error('加载车辆数据失败:', error);
                this.cars = [];
                this.totalCars = 0;
                this.totalPages = 0;
            } finally {
                this.loading = false;
            }
        },
        
        searchCars() {
            this.currentPage = 1;
            this.loadCars();
        },
        
        changePage(page) {
            if (page >= 1 && page <= this.totalPages && page !== this.currentPage) {
                this.currentPage = page;
                this.loadCars();
            }
        },
        
        formatPrice(price) {
            if (!price) return '0';
            return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        },
        
        formatMileage(mileage) {
            if (!mileage) return '0';
            return mileage.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        },
        
        handleImageError(event) {
            event.target.src = '/img/car/car6.jpg';
        },
        
        goToCarDetail(saleId) {
            window.location.href = '/detail/' + saleId;
        },
        
        async loadBrandSaleCount() {
            try {
                const response = await axios.get('/api/car-filter/brand-sale-count');
                if (response.data.success) {
                    this.brandSaleCountList = response.data.data || [];
                } else {
                    console.error('加载品牌数据失败:', response.data.message);
                }
            } catch (error) {
                console.error('加载品牌数据失败:', error);
            }
        },
        
        async loadCarFilterOptions() {
            try {
                const response = await axios.get('/api/car-filter/options');
                if (response.data.success) {
                    const options = response.data.data || {};
                    this.filterOptions.transmissions = options.transmissions || [];
                    this.filterOptions.drivetrains = options.drivetrains || [];
                    this.filterOptions.fuelSystems = options.fuelSystems || [];
                } else {
                    console.error('加载筛选选项失败:', response.data.message);
                }
            } catch (error) {
                console.error('加载筛选选项失败:', error);
            }
        },
        
        toggleBrandExpanded() {
            this.brandExpanded = !this.brandExpanded;
        }
    }
});
</script>