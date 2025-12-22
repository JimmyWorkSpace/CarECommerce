<link href="/css/buy-cars.css" rel="stylesheet">
<link href="/css/car-search-form.css" rel="stylesheet">
<link href="/css/car-detail.css" rel="stylesheet">
<link href="/css/home.css" rel="stylesheet">
<style>
/* 车辆列表页专用样式 - 图片3:2比例 */
.buy-cars-page .car-image-wrapper {
    position: relative;
    width: 100%;
    padding-top: 66.67%; /* 3:2 Aspect Ratio */
    overflow: hidden;
    border-radius: 6px;
}

.buy-cars-page .car-detail-image {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s ease;
}

.spec-name.text-muted{
    padding-left: 2px !important;
    margin-bottom: 7px !important;
    height: 1.2rem !important;
    border-left-width: 2px !important;
    font-size: 17px !important;
}
.spec-value{
    font-size: 17px !important;
    text-align: left !important;
    margin-bottom: 7px !important;
}
.car-item-detail{
}
.buy-cars-page .car-item-detail:hover .car-detail-image {
    transform: scale(1.03);
}
</style>

<!-- 搜索表单（在Vue实例外部） -->
<#include "../components/car-search-form.ftl">

<div class="buy-cars-page" id="app">
    <div class="container">
        <div class="row">
            <!-- 车辆列表 -->
            <div class="col-12 col-lg-9">
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

                    <!-- 车辆列表 -->
                    <div v-else class="cars-list-detail-style">
                        <div class="row">
                            <div v-for="car in cars" :key="car.id" class="col-12 mb-3">
                                <div class="car-item-detail" @click="goToCarDetail(car.id)">
                                    <div class="row">
                                        <!-- 左侧图片 -->
                                        <div class="col-md-4">
                                            <div class="car-image-wrapper">
                                                <img :src="car.coverImage || '/img/car/car6.jpg'" 
                                                     :alt="car.saleTitle" 
                                                     class="car-detail-image" 
                                                     @error="handleImageError">
                                            </div>
                                        </div>
                                        
                                        <!-- 右侧信息 -->
                                        <div class="col-md-8">
                                    <h2 class="car-title" style="padding-bottom: 0px;margin-bottom: 0px;">
                                        {{ car.saleTitleJoin }}
                                    </h2>
                                    <div class="price" style="margin-bottom: 3px;">
                                        <span>$</span><span>
                                            {{ car.salePrice ? formatPrice(car.salePrice) : '面議' }}
                                        </span>
                                    </div>
                                    <div class="specs">
                                        <div class="row">
                                            <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                                                <div class="spec-name text-muted">年份</div>
                                                <div class="spec-value">{{ car.manufactureYear || '--' }}</div>
                                            </div>
                                            <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                                                <div class="spec-name text-muted">里程</div>
                                                <div class="spec-value">{{ car.mileage ? formatMileage(car.mileage) + ' km' : '--' }}</div>
                                            </div>
                                            <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                                                <div class="spec-name text-muted">排量</div>
                                                <div class="spec-value">{{ car.displacement || '--' }}</div>
                                            </div>
                                            <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                                                <div class="spec-name text-muted">變速箱</div>
                                                <div class="spec-value">{{ car.transmission || '--' }}</div>
                                            </div>
                                            <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                                                <div class="spec-name text-muted">燃料</div>
                                                <div class="spec-value">{{ car.fuelSystem || '--' }}</div>
                                            </div>
                                            <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                                                <div class="spec-name text-muted">顏色</div>
                                                <div class="spec-value">{{ car.color || '--' }}</div>
                                            </div>
                                            <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                                                <div class="spec-name text-muted">車門</div>
                                                <div class="spec-value">{{ car.doorCount ? car.doorCount + '門' : '--' }}</div>
                                            </div>
                                            <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                                                <div class="spec-name text-muted">座位</div>
                                                <div class="spec-value">{{ car.passengerCount ? car.passengerCount + '座' : '--' }}</div>
                                            </div>
                                        </div>
                                    </div>
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
            
            <!-- 右侧广告栏（PC端） -->
            <div class="col-12 col-lg-3 d-none d-lg-block">
                <div class="cars-ads-sidebar">
                    <div v-for="(ad, index) in advertisements.slice(0, 2)" :key="ad.id" class="mb-3">
                        <div class="ad-card" :data-ad-id="ad.id">
                            <!-- 連結類型广告 -->
                            <a v-if="ad.isLink === 1" :href="ad.linkUrl" class="ad-link">
                                <img :src="ad.imageUrl" :alt="ad.title" class="ad-image" 
                                     @error="handleAdImageError($event, ad.title)">
                                <div v-if="ad.title" class="ad-title-overlay" style="display: none;" v-text="ad.title"></div>
                                <div v-if="ad.title" class="ad-title-bottom" v-text="ad.title"></div>
                            </a>
                            <!-- 內容類型广告 -->
                            <div v-else class="ad-content-link" @click="showAdContent(ad.id, ad.title, ad.content)">
                                <img :src="ad.imageUrl" :alt="ad.title" class="ad-image"
                                     @error="handleAdImageError($event, ad.title)">
                                <div v-if="ad.title" class="ad-title-overlay" style="display: none;" v-text="ad.title"></div>
                                <div v-if="ad.title" class="ad-title-bottom" v-text="ad.title"></div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- 當沒有廣告數據时顯示默認内容 -->
                    <template v-if="!advertisements || advertisements.length === 0">
                        <div class="mb-3">
                            <div class="ad-card">
                                <img src="/img/ad1.jpg" alt="专业检测" class="ad-image" 
                                     @error="handleAdImageError($event, '專業檢測')">
                                <div class="ad-title-overlay" style="display: none;">專業檢測</div>
                            </div>
                        </div>
                        <div class="mb-3">
                            <div class="ad-card">
                                <iframe src="/ad-content.html" class="ad-iframe" frameborder="0"></iframe>
                            </div>
                        </div>
                    </template>
                </div>
            </div>
        </div>
        
        <!-- 手机端广告（显示在页面下方） -->
        <div class="row d-lg-none mt-4">
            <div class="col-12">
                <div class="cars-ads-mobile">
                    <div v-for="(ad, index) in advertisements.slice(0, 2)" :key="ad.id" class="mb-3">
                        <div class="ad-card" :data-ad-id="ad.id">
                            <!-- 連結類型广告 -->
                            <a v-if="ad.isLink === 1" :href="ad.linkUrl" class="ad-link">
                                <img :src="ad.imageUrl" :alt="ad.title" class="ad-image" 
                                     @error="handleAdImageError($event, ad.title)">
                                <div v-if="ad.title" class="ad-title-overlay" style="display: none;" v-text="ad.title"></div>
                                <div v-if="ad.title" class="ad-title-bottom" v-text="ad.title"></div>
                            </a>
                            <!-- 內容類型广告 -->
                            <div v-else class="ad-content-link" @click="showAdContent(ad.id, ad.title, ad.content)">
                                <img :src="ad.imageUrl" :alt="ad.title" class="ad-image"
                                     @error="handleAdImageError($event, ad.title)">
                                <div v-if="ad.title" class="ad-title-overlay" style="display: none;" v-text="ad.title"></div>
                                <div v-if="ad.title" class="ad-title-bottom" v-text="ad.title"></div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- 當沒有廣告數據时顯示默認内容 -->
                    <template v-if="!advertisements || advertisements.length === 0">
                        <div class="mb-3">
                            <div class="ad-card">
                                <img src="/img/ad1.jpg" alt="专业检测" class="ad-image" 
                                     @error="handleAdImageError($event, '專業檢測')">
                                <div class="ad-title-overlay" style="display: none;">專業檢測</div>
                            </div>
                        </div>
                        <div class="mb-3">
                            <div class="ad-card">
                                <iframe src="/ad-content.html" class="ad-iframe" frameborder="0"></iframe>
                            </div>
                        </div>
                    </template>
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
        advertisements: [],
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
        this.loadCars();
        this.loadBrandSaleCount();
        this.loadCarFilterOptions();
        this.getAdvertisements();
        
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
        
        // 获取广告数据
        getAdvertisements() {
            let _this = this;
            $.ajax({
                url: '/api/advertisement/list',
                method: 'GET',
                success: (data) => {
                    // 过滤出 advType=0 的前两条数据
                    const filteredAds = data.data.filter(ad => ad.advType === 0).slice(0, 2);
                    _this.advertisements = filteredAds;
                },
                error: (error) => {
                    console.log('获取广告数据失败:', error);
                }
            });
        },
        
        // 顯示廣告內容
        showAdContent(adId, title, content) {
            // 檢查adId是否有效
            if (!adId || adId === 'null' || adId === '') {
                console.error('廣告ID無效:', adId);
                return;
            }
            // 在新頁面打開廣告內容頁面
            window.location.href = '/ad-content/' + adId;
        },
        
        handleAdImageError(event, title) {
            // 图片加载失败时的处理
            if (title) {
                event.target.style.display = 'none';
                const titleElement = event.target.nextElementSibling;
                if (titleElement && titleElement.classList.contains('ad-title-overlay')) {
                    titleElement.style.display = 'block';
                }
            }
        },
        
        // 应用搜索表单条件
        applySearchForm(formData) {
            // 更新searchForm
            this.searchForm.brand = formData.brand ? [formData.brand] : [];
            this.searchForm.model = formData.model || '';
            
            // 处理年份字段，确保正确解析
            if (formData.yearFrom) {
                const yearFrom = parseInt(formData.yearFrom);
                this.searchForm.yearFrom = (!isNaN(yearFrom) && yearFrom > 0) ? yearFrom : null;
            } else {
                this.searchForm.yearFrom = null;
            }
            
            if (formData.yearTo) {
                const yearTo = parseInt(formData.yearTo);
                this.searchForm.yearTo = (!isNaN(yearTo) && yearTo > 0) ? yearTo : null;
            } else {
                this.searchForm.yearTo = null;
            }
            
            // 处理排量字段
            if (formData.displacementFrom) {
                const displacementFrom = parseInt(formData.displacementFrom);
                this.searchForm.displacementFrom = (!isNaN(displacementFrom) && displacementFrom > 0) ? displacementFrom : null;
            } else {
                this.searchForm.displacementFrom = null;
            }
            
            if (formData.displacementTo) {
                const displacementTo = parseInt(formData.displacementTo);
                this.searchForm.displacementTo = (!isNaN(displacementTo) && displacementTo > 0) ? displacementTo : null;
            } else {
                this.searchForm.displacementTo = null;
            }
            
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