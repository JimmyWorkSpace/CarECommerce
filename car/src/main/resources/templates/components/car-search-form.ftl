<!-- 通用车辆搜索表单组件 -->
<div class="car-search-form-container" id="carSearchFormContainer">
    <div class="container">
        <form class="car-search-form" id="carSearchForm" @submit.prevent="performSearch">
            <!-- 第一行 -->
            <div class="search-form-row">
                <div class="search-form-group">
                    <label class="search-form-label">汽車品牌</label>
                    <select class="search-form-select" id="carBrandSelect" v-model="searchForm.brand">
                        <option value="">全部</option>
                        <option v-for="brand in brands" :key="brand.id" :value="brand.brand" v-text="brand.brand"></option>
                    </select>
                </div>
                <div class="search-form-group">
                    <label class="search-form-label">汽車型號</label>
                    <select class="search-form-select" v-model="searchForm.model" :disabled="!searchForm.brand">
                        <option value="">全部</option>
                        <option v-for="model in models" :key="model" :value="model" v-text="model"></option>
                    </select>
                </div>
                <div class="search-form-group">
                    <label class="search-form-label">出廠年份起</label>
                    <select class="search-form-select" v-model="searchForm.yearFrom">
                        <option value="">全部</option>
                        <option v-for="year in years" :key="year" :value="year" v-text="year"></option>
                    </select>
                </div>
                <div class="search-form-group">
                    <label class="search-form-label">出廠年份至</label>
                    <select class="search-form-select" v-model="searchForm.yearTo">
                        <option value="">全部</option>
                        <option v-for="year in years" :key="year" :value="year" v-text="year"></option>
                    </select>
                </div>
            </div>
            
            <!-- 第二行 -->
            <div class="search-form-row">
                <div class="search-form-group">
                    <label class="search-form-label">排氣量cc起</label>
                    <input type="number" class="search-form-input" placeholder="自填" v-model.number="searchForm.displacementFrom" min="0" step="1">
                </div>
                <div class="search-form-group">
                    <label class="search-form-label">排氣量cc迄</label>
                    <input type="number" class="search-form-input" placeholder="自填" v-model.number="searchForm.displacementTo" min="0" step="1">
                </div>
                <div class="search-form-group">
                    <label class="search-form-label">車色</label>
                    <select class="search-form-select" v-model="searchForm.color">
                        <option value="">全部</option>
                        <option v-for="color in colors" :key="color" :value="color" v-text="color"></option>
                    </select>
                </div>
                <div class="search-form-group">
                    <label class="search-form-label">燃料系統</label>
                    <select class="search-form-select" v-model="searchForm.fuelSystem">
                        <option value="">全部</option>
                        <option v-for="fuel in fuelSystems" :key="fuel" :value="fuel" v-text="fuel"></option>
                    </select>
                </div>
            </div>
            
            <!-- 第三行：車輛所在地 -->
            <div class="search-form-row">
                <div class="search-form-group full-width">
                    <label class="search-form-label">車輛所在地 (可複選)</label>
                    <div class="location-checkboxes">
                        <label class="location-checkbox" v-for="location in locations" :key="location">
                            <input type="checkbox" :value="location" v-model="searchForm.locations">
                            <span v-text="location"></span>
                        </label>
                    </div>
                </div>
            </div>
            
            <!-- 第四行：關鍵字和搜尋按鈕 -->
            <div class="search-form-row">
                <div class="search-form-group search-keyword-group">
                    <label class="search-form-label">關鍵字</label>
                    <input type="text" class="search-form-input keyword-input" placeholder="" v-model="searchForm.keyword" @keyup.enter="performSearch">
                </div>
                <div class="search-form-group search-button-group">
                    <button type="button" class="search-submit-btn" @click.prevent="performSearch">搜尋</button>
                </div>
            </div>
        </form>
    </div>
</div>

<script>
console.log('=== 搜索表单脚本文件已加载 ===');
// 搜索表单Vue实例
(function() {
    console.log('搜索表单脚本开始执行');
    
    // 确保DOM加载完成后再初始化
    function initSearchForm() {
        console.log('开始初始化搜索表单Vue实例');
        
        // 检查Vue是否可用
        if (typeof Vue === 'undefined') {
            console.error('Vue未加载，请检查vue.min.js是否正确加载');
            return;
        }
        console.log('Vue已加载，版本:', Vue.version);
        
        // 检查DOM元素是否存在
        const container = document.getElementById('carSearchFormContainer');
        if (!container) {
            console.error('找不到搜索表单容器元素 #carSearchFormContainer');
            // 延迟重试
            setTimeout(function() {
                const retryContainer = document.getElementById('carSearchFormContainer');
                if (retryContainer) {
                    console.log('延迟后找到搜索表单容器元素，重新初始化');
                    initSearchForm();
                } else {
                    console.error('延迟后仍然找不到搜索表单容器元素');
                }
            }, 500);
            return;
        }
        console.log('找到搜索表单容器元素');
        
        // 从页面获取过滤条件JSON数据
        const searchFilterJson = <#if searchFilterJson??>'${searchFilterJson?js_string}'<#else>'{}'</#if>;
        let searchFilter = {};
        try {
            searchFilter = JSON.parse(searchFilterJson);
            console.log('解析搜索过滤条件成功');
        } catch (e) {
            console.error('解析搜索过滤条件JSON失败:', e);
            searchFilter = {};
        }
        
        // 如果已经存在Vue实例，先销毁
        if (window.carSearchFormVue) {
            try {
                window.carSearchFormVue.$destroy();
                console.log('已销毁旧的Vue实例');
            } catch (e) {
                console.warn('销毁旧的Vue实例失败:', e);
            }
        }
        
        // 创建搜索表单的Vue实例
        try {
            window.carSearchFormVue = new Vue({
        el: '#carSearchFormContainer',
        data: {
            brands: searchFilter.brands || [],
            years: [],
            models: [],
            minYear: searchFilter.minYear || 1990,
            maxYear: searchFilter.maxYear || new Date().getFullYear(),
            fuelSystems: searchFilter.fuelSystems || [],
            colors: searchFilter.colors || [],
            locations: searchFilter.locations || [],
            searchForm: {
                brand: '',
                model: '',
                yearFrom: '',
                yearTo: '',
                displacementFrom: '',
                displacementTo: '',
                color: '',
                fuelSystem: '',
                locations: [],
                keyword: ''
            }
        },
        watch: {
            // 监听品牌选择变化
            'searchForm.brand': function(newBrand, oldBrand) {
                if (newBrand && newBrand !== oldBrand) {
                    this.loadModelsByBrand(newBrand);
                } else if (!newBrand) {
                    // 清空品牌时，清空型号列表
                    this.models = [];
                    this.searchForm.model = '';
                }
            }
        },
        mounted() {
            this.initYears();
            console.log('搜索表单初始化完成，品牌数量:', this.brands.length, '年份范围:', this.minYear, '-', this.maxYear);
            console.log('Vue实例已创建:', window.carSearchFormVue);
            
            // 如果URL中有品牌参数，加载对应的型号列表
            const urlParams = new URLSearchParams(window.location.search);
            const brandFromUrl = urlParams.get('brand');
            if (brandFromUrl) {
                this.searchForm.brand = brandFromUrl;
                this.loadModelsByBrand(brandFromUrl);
            }
            
            // 测试按钮点击
            const searchBtn = document.querySelector('.search-submit-btn');
            if (searchBtn) {
                console.log('搜索按钮已找到');
            } else {
                console.error('搜索按钮未找到');
            }
        },
        methods: {
            // 初始化年份列表
            initYears() {
                const years = [];
                // 从最大值到最小值，降序排列
                for (let i = this.maxYear; i >= this.minYear; i--) {
                    years.push(i);
                }
                this.years = years;
            },
            
            // 根据品牌加载型号列表
            async loadModelsByBrand(brand) {
                if (!brand) {
                    this.models = [];
                    this.searchForm.model = '';
                    return;
                }
                
                try {
                    console.log('加载型号列表，品牌:', brand);
                    const response = await axios.get('/api/car-filter/models', {
                        params: { brand: brand }
                    });
                    
                    if (response.data && response.data.success) {
                        this.models = response.data.data || [];
                        console.log('型号列表加载成功，数量:', this.models.length);
                        // 如果当前选择的型号不在新列表中，清空选择
                        if (this.searchForm.model && !this.models.includes(this.searchForm.model)) {
                            this.searchForm.model = '';
                        }
                    } else {
                        console.error('加载型号列表失败:', response.data ? response.data.message : '未知错误');
                        this.models = [];
                    }
                } catch (error) {
                    console.error('加载型号列表异常:', error);
                    this.models = [];
                }
            },
            
            // 执行搜索
            performSearch() {
                console.log('=== performSearch 方法被调用 ===');
                console.log('执行搜索，当前路径:', window.location.pathname);
                console.log('搜索条件:', JSON.stringify(this.searchForm, null, 2));
                
                // 如果在车辆列表页，触发事件让页面处理
                if (window.location.pathname === '/buy-cars' || window.location.pathname.indexOf('/buy-cars') === 0) {
                    console.log('在车辆列表页，触发carSearch事件');
                    window.dispatchEvent(new CustomEvent('carSearch', { detail: this.searchForm }));
                    return;
                }
                
                // 在首页，构建URL参数并跳转
                console.log('在首页，准备跳转到车辆列表页');
                const params = new URLSearchParams();
                
                // 品牌
                if (this.searchForm.brand) {
                    params.append('brand', this.searchForm.brand);
                }
                
                // 型号
                if (this.searchForm.model) {
                    params.append('model', this.searchForm.model);
                }
                
                // 出厂年份
                if (this.searchForm.yearFrom) {
                    params.append('yearFrom', this.searchForm.yearFrom);
                }
                if (this.searchForm.yearTo) {
                    params.append('yearTo', this.searchForm.yearTo);
                }
                
                // 排量
                if (this.searchForm.displacementFrom) {
                    params.append('displacementFrom', this.searchForm.displacementFrom);
                }
                if (this.searchForm.displacementTo) {
                    params.append('displacementTo', this.searchForm.displacementTo);
                }
                
                // 车色
                if (this.searchForm.color) {
                    params.append('color', this.searchForm.color);
                }
                
                // 燃料系统
                if (this.searchForm.fuelSystem) {
                    params.append('fuelSystem', this.searchForm.fuelSystem);
                }
                
                // 车辆所在地（多个）
                if (this.searchForm.locations && this.searchForm.locations.length > 0) {
                    this.searchForm.locations.forEach(location => {
                        params.append('locations', location);
                    });
                }
                
                // 关键字
                if (this.searchForm.keyword) {
                    params.append('keyword', this.searchForm.keyword);
                }
                
                // 跳转到车辆列表页
                const url = '/buy-cars' + (params.toString() ? '?' + params.toString() : '');
                console.log('准备跳转，URL:', url);
                window.location.href = url;
            }
        }
            });
            console.log('Vue实例创建成功');
        } catch (e) {
            console.error('创建Vue实例失败:', e);
            console.error('错误详情:', e.stack);
        }
    }
    
    // 初始化函数
    function doInit() {
        // 使用setTimeout确保DOM完全渲染
        setTimeout(function() {
            initSearchForm();
        }, 50);
    }
    
    // 如果DOM已经加载完成，直接初始化
    if (document.readyState === 'loading') {
        console.log('DOM正在加载，等待DOMContentLoaded事件...');
        document.addEventListener('DOMContentLoaded', function() {
            console.log('DOMContentLoaded事件触发，开始初始化');
            doInit();
        });
    } else {
        // DOM已经加载完成，直接初始化
        console.log('DOM已加载完成，立即初始化');
        doInit();
    }
})();
console.log('=== 搜索表单脚本执行完成 ===');
</script>

