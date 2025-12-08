<link href="/css/buy-cars.css" rel="stylesheet">
<link href="/css/car-search-form.css" rel="stylesheet">

<!-- 搜索表单（在Vue实例外部） -->
<#include "../components/car-search-form.ftl">

<div class="buy-cars-page" id="app">
    <div class="container">
        <div class="row">
            <!-- 车辆列表 -->
            <div class="col-12">
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
                                <h3 class="car-title" v-text="car.saleTitleJoin + ' ' + car.saleTitle"></h3>
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
            yearFrom: '',
            yearTo: '',
            priceMin: '',
            priceMax: '',
            displacementFrom: '',
            displacementTo: '',
            color: '',
            locations: [],
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
        // 从URL参数读取搜索条件
        this.loadSearchParamsFromUrl();
        // 同步搜索表单组件的值
        this.syncSearchFormToComponent();
        this.loadCars();
        this.loadBrandSaleCount();
        this.loadCarFilterOptions();
        
        // 监听搜索表单组件的搜索事件
        window.addEventListener('carSearch', (event) => {
            // 如果是在车辆列表页触发的搜索，直接执行搜索
            this.applySearchForm(event.detail);
        });
    },
    methods: {
        async loadCars() {
            try {
                this.loading = true;
                this.searchForm.pageNum = this.currentPage;
                this.searchForm.pageSize = this.pageSize;
                
                // 构建请求参数，处理字段类型
                const requestData = {
                    ...this.searchForm,
                    yearFrom: this.searchForm.yearFrom || null,
                    yearTo: this.searchForm.yearTo || null,
                    displacementFrom: this.searchForm.displacementFrom || null,
                    displacementTo: this.searchForm.displacementTo || null
                };
                
                const response = await axios.post('/api/car-sales/list', requestData);
                
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
        },
        
        // 从URL参数加载搜索条件
        loadSearchParamsFromUrl() {
            const urlParams = new URLSearchParams(window.location.search);
            
            // 品牌
            const brand = urlParams.get('brand');
            if (brand) {
                this.searchForm.brand = [brand];
            }
            
            // 型号
            const model = urlParams.get('model');
            if (model) {
                this.searchForm.model = model;
            }
            
            // 出厂年份
            const yearFrom = urlParams.get('yearFrom');
            if (yearFrom) {
                this.searchForm.yearFrom = parseInt(yearFrom);
            }
            const yearTo = urlParams.get('yearTo');
            if (yearTo) {
                this.searchForm.yearTo = parseInt(yearTo);
            }
            
            // 排量
            const displacementFrom = urlParams.get('displacementFrom');
            if (displacementFrom) {
                this.searchForm.displacementFrom = parseInt(displacementFrom);
            }
            const displacementTo = urlParams.get('displacementTo');
            if (displacementTo) {
                this.searchForm.displacementTo = parseInt(displacementTo);
            }
            
            // 车色
            const color = urlParams.get('color');
            if (color) {
                this.searchForm.color = color;
            }
            
            // 燃料系统
            const fuelSystem = urlParams.get('fuelSystem');
            if (fuelSystem) {
                this.searchForm.fuelSystem = [fuelSystem];
            }
            
            // 车辆所在地
            const locations = urlParams.getAll('locations');
            if (locations.length > 0) {
                this.searchForm.locations = locations;
            }
            
            // 关键字
            const keyword = urlParams.get('keyword');
            if (keyword) {
                this.searchForm.keyword = keyword;
            }
        },
        
        // 同步搜索条件到搜索表单组件
        syncSearchFormToComponent() {
            // 等待搜索表单组件初始化完成
            setTimeout(async () => {
                if (window.carSearchFormVue) {
                    // 先设置品牌，然后加载型号列表
                    if (this.searchForm.brand && this.searchForm.brand.length > 0) {
                        const brand = this.searchForm.brand[0];
                        window.carSearchFormVue.searchForm.brand = brand;
                        // 加载该品牌的型号列表
                        if (window.carSearchFormVue.loadModelsByBrand) {
                            await window.carSearchFormVue.loadModelsByBrand(brand);
                            // 型号列表加载完成后再设置型号
                            if (this.searchForm.model) {
                                window.carSearchFormVue.searchForm.model = this.searchForm.model;
                            }
                        }
                    } else if (this.searchForm.model) {
                        window.carSearchFormVue.searchForm.model = this.searchForm.model;
                    }
                    if (this.searchForm.yearFrom) {
                        window.carSearchFormVue.searchForm.yearFrom = this.searchForm.yearFrom.toString();
                    }
                    if (this.searchForm.yearTo) {
                        window.carSearchFormVue.searchForm.yearTo = this.searchForm.yearTo.toString();
                    }
                    if (this.searchForm.displacementFrom) {
                        window.carSearchFormVue.searchForm.displacementFrom = this.searchForm.displacementFrom;
                    }
                    if (this.searchForm.displacementTo) {
                        window.carSearchFormVue.searchForm.displacementTo = this.searchForm.displacementTo;
                    }
                    if (this.searchForm.color) {
                        window.carSearchFormVue.searchForm.color = this.searchForm.color;
                    }
                    if (this.searchForm.fuelSystem && this.searchForm.fuelSystem.length > 0) {
                        window.carSearchFormVue.searchForm.fuelSystem = this.searchForm.fuelSystem[0];
                    }
                    if (this.searchForm.locations && this.searchForm.locations.length > 0) {
                        window.carSearchFormVue.searchForm.locations = this.searchForm.locations;
                    }
                    if (this.searchForm.keyword) {
                        window.carSearchFormVue.searchForm.keyword = this.searchForm.keyword;
                    }
                }
            }, 100);
        },
        
        // 应用搜索表单条件
        applySearchForm(formData) {
            // 更新searchForm
            this.searchForm.brand = formData.brand ? [formData.brand] : [];
            this.searchForm.model = formData.model || '';
            this.searchForm.yearFrom = formData.yearFrom ? parseInt(formData.yearFrom) : null;
            this.searchForm.yearTo = formData.yearTo ? parseInt(formData.yearTo) : null;
            this.searchForm.displacementFrom = formData.displacementFrom || null;
            this.searchForm.displacementTo = formData.displacementTo || null;
            this.searchForm.color = formData.color || '';
            this.searchForm.fuelSystem = formData.fuelSystem ? [formData.fuelSystem] : [];
            this.searchForm.locations = formData.locations || [];
            this.searchForm.keyword = formData.keyword || '';
            
            // 重置到第一页并搜索
            this.currentPage = 1;
            this.loadCars();
        }
    }
});
</script>