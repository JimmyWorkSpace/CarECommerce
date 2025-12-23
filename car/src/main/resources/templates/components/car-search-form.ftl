<!-- 通用车辆搜索表单组件 -->
<#assign searchFilter = searchFilter!{} />
<#assign brands = (searchFilter.brands)![] />
<#assign fuelSystems = (searchFilter.fuelSystems)![] />
<#assign colors = (searchFilter.colors)![] />
<#assign locations = (searchFilter.locations)![] />
<#assign minYear = (searchFilter.minYear)!1990 />
<#assign maxYear = (searchFilter.maxYear)!((.now?long?number_to_date?string("yyyy"))?number) />

<#-- 生成年份列表（降序） -->
<#assign years = [] />
<#if maxYear?? && minYear?? && maxYear gte minYear>
    <#list maxYear?int..minYear?int as year>
        <#assign years = years + [year] />
    </#list>
<#else>
    <#-- 如果年份数据有问题，生成默认年份列表 -->
    <#assign currentYear = (.now?long?number_to_date?string("yyyy"))?number />
    <#list currentYear..1990 as year>
        <#assign years = years + [year] />
    </#list>
</#if>

<#-- URL参数将通过JavaScript读取并填充表单 -->

<div class="car-search-form-container" id="carSearchFormContainer">
    <div class="container">
        <form class="car-search-form" id="carSearchForm">
            <!-- 第一行 -->
            <div class="search-form-row">
                <div class="search-form-group">
                    <label class="search-form-label">汽車品牌</label>
                    <select class="search-form-select" id="carBrandSelect" name="brand">
                        <option value="">全部</option>
                        <#list brands as brand>
                            <option value="${brand.id?c}">${brand.brand!''}</option>
                        </#list>
                    </select>
                </div>
                <div class="search-form-group">
                    <label class="search-form-label">汽車型號</label>
                    <select class="search-form-select" id="carModelSelect" name="model" disabled>
                        <option value="">全部</option>
                        <#-- 型号将通过JS动态加载 -->
                    </select>
                </div>
                <div class="search-form-group">
                    <label class="search-form-label">出廠年份起</label>
                    <select class="search-form-select" name="yearFrom" id="yearFromSelect">
                        <option value="">全部</option>
                        <#list years as year>
                            <option value="${year?c}">${year?c}</option>
                        </#list>
                    </select>
                </div>
                <div class="search-form-group">
                    <label class="search-form-label">出廠年份至</label>
                    <select class="search-form-select" name="yearTo" id="yearToSelect">
                        <option value="">全部</option>
                        <#list years as year>
                            <option value="${year?c}">${year?c}</option>
                        </#list>
                    </select>
                </div>
            </div>
            
            <!-- 第二行 -->
            <div class="search-form-row">
                <div class="search-form-group">
                    <label class="search-form-label">排氣量起</label>
                    <input type="number" class="search-form-input" name="displacementFrom" id="displacementFromInput" placeholder="自填範圍1.1至6.0" min="1.1" step="0.1">
                </div>
                <div class="search-form-group">
                    <label class="search-form-label">排氣量迄</label>
                    <input type="number" class="search-form-input" name="displacementTo" id="displacementToInput" placeholder="自填範圍1.1至6.0" min="6.0" step="0.1">
                </div>
                <div class="search-form-group">
                    <label class="search-form-label">車色</label>
                    <select class="search-form-select" name="color" id="colorSelect">
                        <option value="">全部</option>
                        <#list colors as color>
                            <option value="${color}">${color}</option>
                        </#list>
                    </select>
                </div>
                <div class="search-form-group">
                    <label class="search-form-label">燃料系統</label>
                    <select class="search-form-select" name="fuelSystem" id="fuelSystemSelect">
                        <option value="">全部</option>
                        <#list fuelSystems as fuel>
                            <option value="${fuel}">${fuel}</option>
                        </#list>
                    </select>
                </div>
            </div>
            
            <!-- 第三行：車輛所在地 -->
            <div class="search-form-row">
                <div class="search-form-group full-width">
                    <label class="search-form-label">車輛所在地 (可複選)</label>
                    <div class="location-checkboxes">
                        <#list locations as location>
                            <label class="location-checkbox">
                                <input type="checkbox" name="locations" value="${location}" class="location-checkbox-input">
                                <span>${location}</span>
                            </label>
                        </#list>
                    </div>
                </div>
            </div>
            
            <!-- 第四行：關鍵字和搜尋按鈕 -->
            <div class="search-form-row">
                <div class="search-form-group search-keyword-group">
                    <label class="search-form-label">關鍵字</label>
                    <input type="text" class="search-form-input keyword-input" name="keyword" id="keywordInput" placeholder="">
                </div>
                <div class="search-form-group search-button-group">
                    <button type="button" class="search-submit-btn" id="searchSubmitBtn">搜尋</button>
                </div>
            </div>
        </form>
    </div>
</div>

<script>
(function() {
    'use strict';
    
    // 全局变量
    let brandSelect, modelSelect, searchForm, searchBtn, keywordInput;
    
    /**
     * 从URL获取参数值
     */
    function getUrlParameter(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name) || '';
    }
    
    /**
     * 从URL获取多个参数值（用于多选）
     */
    function getUrlParameters(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.getAll(name) || [];
    }
    
    /**
     * 根据品牌ID加载型号列表
     * @param {string} brandId - 品牌ID
     * @param {string} selectedModel - 要选中的型号（可选）
     */
    function loadModelsByBrand(brandId, selectedModel) {
        if (!modelSelect) return;
        
        if (!brandId) {
            // 清空型号列表并禁用
            modelSelect.innerHTML = '<option value="">全部</option>';
            modelSelect.disabled = true;
            return;
        }
        
        // 启用型号选择框
        modelSelect.disabled = false;
        
        // 显示加载状态
        modelSelect.innerHTML = '<option value="">加载中...</option>';
        
        // 使用fetch API加载型号列表，传递品牌ID
        fetch('/api/car-filter/models?brandId=' + encodeURIComponent(brandId))
            .then(function(response) {
                return response.json();
            })
            .then(function(result) {
                if (result && result.success && result.data) {
                    // 清空并填充型号选项
                    modelSelect.innerHTML = '<option value="">全部</option>';
                    result.data.forEach(function(model) {
                        const option = document.createElement('option');
                        option.value = model;
                        option.textContent = model;
                        if (selectedModel && selectedModel === model) {
                            option.selected = true;
                        }
                        modelSelect.appendChild(option);
                    });
                } else {
                    modelSelect.innerHTML = '<option value="">全部</option>';
                    console.error('加载型号列表失败:', result ? result.message : '未知错误');
                }
            })
            .catch(function(error) {
                console.error('加载型号列表异常:', error);
                if (modelSelect) {
                    modelSelect.innerHTML = '<option value="">全部</option>';
                }
            });
    }
    
    /**
     * 填充表单初始值
     */
    function fillFormFromUrl() {
        const brand = getUrlParameter('brand');
        const model = getUrlParameter('model');
        const yearFrom = getUrlParameter('yearFrom');
        const yearTo = getUrlParameter('yearTo');
        const displacementFrom = getUrlParameter('displacementFrom');
        const displacementTo = getUrlParameter('displacementTo');
        const color = getUrlParameter('color');
        const fuelSystem = getUrlParameter('fuelSystem');
        const locations = getUrlParameters('locations');
        const keyword = getUrlParameter('keyword');
        
        // 填充品牌
        if (brand && brandSelect) {
            brandSelect.value = brand;
            // 如果选择了品牌，加载型号列表
            if (brand) {
                loadModelsByBrand(brand, model);
            }
        }
        
        // 填充年份
        if (yearFrom && document.getElementById('yearFromSelect')) {
            document.getElementById('yearFromSelect').value = yearFrom;
        }
        if (yearTo && document.getElementById('yearToSelect')) {
            document.getElementById('yearToSelect').value = yearTo;
        }
        
        // 填充排量（将URL参数中的毫升值转换为升值显示，如果值小于100则视为升值）
        if (displacementFrom && document.getElementById('displacementFromInput')) {
            const displacementFromValue = parseFloat(displacementFrom);
            if (!isNaN(displacementFromValue)) {
                // 如果值小于100，可能是升值，直接显示；否则是毫升值，除以1000显示为升值
                const displayValue = displacementFromValue < 100 ? displacementFromValue : displacementFromValue / 1000;
                document.getElementById('displacementFromInput').value = displayValue;
            }
        }
        if (displacementTo && document.getElementById('displacementToInput')) {
            const displacementToValue = parseFloat(displacementTo);
            if (!isNaN(displacementToValue)) {
                // 如果值小于100，可能是升值，直接显示；否则是毫升值，除以1000显示为升值
                const displayValue = displacementToValue < 100 ? displacementToValue : displacementToValue / 1000;
                document.getElementById('displacementToInput').value = displayValue;
            }
        }
        
        // 填充车色
        if (color && document.getElementById('colorSelect')) {
            document.getElementById('colorSelect').value = color;
        }
        
        // 填充燃料系统
        if (fuelSystem && document.getElementById('fuelSystemSelect')) {
            document.getElementById('fuelSystemSelect').value = fuelSystem;
        }
        
        // 填充车辆所在地（多选）
        if (locations && locations.length > 0) {
            const locationInputs = document.querySelectorAll('.location-checkbox-input');
            locationInputs.forEach(function(input) {
                if (locations.indexOf(input.value) !== -1) {
                    input.checked = true;
                }
            });
        }
        
        // 填充关键字
        if (keyword && keywordInput) {
            keywordInput.value = keyword;
        }
    }
    
    /**
     * 执行搜索
     */
    function performSearch() {
        console.log('performSearch 被调用');
        
        if (!searchForm) {
            console.error('搜索表单不存在');
            return;
        }
        
        const params = new URLSearchParams();
        const searchConditions = {};
        
        // 直接获取表单元素的值
        const brand = brandSelect ? brandSelect.value : '';
        const model = modelSelect ? modelSelect.value : '';
        const yearFrom = document.getElementById('yearFromSelect') ? document.getElementById('yearFromSelect').value : '';
        const yearTo = document.getElementById('yearToSelect') ? document.getElementById('yearToSelect').value : '';
        const displacementFrom = document.getElementById('displacementFromInput') ? document.getElementById('displacementFromInput').value : '';
        const displacementTo = document.getElementById('displacementToInput') ? document.getElementById('displacementToInput').value : '';
        const color = document.getElementById('colorSelect') ? document.getElementById('colorSelect').value : '';
        const fuelSystem = document.getElementById('fuelSystemSelect') ? document.getElementById('fuelSystemSelect').value : '';
        const keyword = keywordInput ? keywordInput.value : '';
        
        // 获取车辆所在地（多选）
        const locationInputs = document.querySelectorAll('.location-checkbox-input:checked');
        const locations = [];
        locationInputs.forEach(function(input) {
            locations.push(input.value);
        });
        
        // 构建搜索条件对象
        if (brand) {
            searchConditions.brand = brand;
            params.append('brand', brand);
        }
        if (model) {
            searchConditions.model = model;
            params.append('model', model);
        }
        if (yearFrom) {
            const yearFromValue = parseInt(yearFrom);
            if (!isNaN(yearFromValue) && yearFromValue > 0) {
                searchConditions.yearFrom = yearFromValue.toString();
                params.append('yearFrom', yearFromValue.toString());
            }
        }
        if (yearTo) {
            const yearToValue = parseInt(yearTo);
            if (!isNaN(yearToValue) && yearToValue > 0) {
                searchConditions.yearTo = yearToValue.toString();
                params.append('yearTo', yearToValue.toString());
            }
        }
        if (displacementFrom) {
            // 将排量从升转换为毫升（乘以1000）
            const displacementFromValue = parseFloat(displacementFrom);
            if (!isNaN(displacementFromValue) && displacementFromValue > 0) {
                const displacementFromMl = Math.round(displacementFromValue * 1000);
                searchConditions.displacementFrom = displacementFromMl.toString();
                params.append('displacementFrom', displacementFromMl.toString());
            }
        }
        if (displacementTo) {
            // 将排量从升转换为毫升（乘以1000）
            const displacementToValue = parseFloat(displacementTo);
            if (!isNaN(displacementToValue) && displacementToValue > 0) {
                const displacementToMl = Math.round(displacementToValue * 1000);
                searchConditions.displacementTo = displacementToMl.toString();
                params.append('displacementTo', displacementToMl.toString());
            }
        }
        if (color) {
            searchConditions.color = color;
            params.append('color', color);
        }
        if (fuelSystem) {
            searchConditions.fuelSystem = fuelSystem;
            params.append('fuelSystem', fuelSystem);
        }
        if (locations.length > 0) {
            searchConditions.locations = locations;
            locations.forEach(function(location) {
                params.append('locations', location);
            });
        }
        if (keyword) {
            searchConditions.keyword = keyword;
            params.append('keyword', keyword);
        }
        
        const currentPath = window.location.pathname;
        console.log('当前路径:', currentPath);
        console.log('搜索条件:', searchConditions);
        
        // 如果在车辆列表页，触发事件让页面处理
        if (currentPath === '/buy-cars' || currentPath.indexOf('/buy-cars') === 0) {
            console.log('在车辆列表页，触发carSearch事件');
            // 触发自定义事件
            window.dispatchEvent(new CustomEvent('carSearch', { 
                detail: searchConditions 
            }));
            return;
        }
        
        // 在首页或其他页面，构建URL参数并跳转到车辆列表页
        const url = '/buy-cars' + (params.toString() ? '?' + params.toString() : '');
        console.log('跳转到车辆列表页，URL:', url);
        window.location.href = url;
    }
    
    // 初始化函数
    function initSearchForm() {
        // 获取DOM元素
        brandSelect = document.getElementById('carBrandSelect');
        modelSelect = document.getElementById('carModelSelect');
        searchForm = document.getElementById('carSearchForm');
        searchBtn = document.getElementById('searchSubmitBtn');
        keywordInput = document.getElementById('keywordInput');
        
        // 检查必要的元素是否存在
        if (!searchForm) {
            console.error('搜索表单元素不存在: #carSearchForm');
            return;
        }
        if (!searchBtn) {
            console.error('搜索按钮元素不存在: #searchSubmitBtn');
            return;
        }
        
        console.log('搜索表单初始化，找到元素:', {
            searchForm: !!searchForm,
            searchBtn: !!searchBtn,
            brandSelect: !!brandSelect,
            modelSelect: !!modelSelect,
            keywordInput: !!keywordInput
        });
        
        // 绑定品牌选择变化事件
        if (brandSelect) {
            brandSelect.addEventListener('change', function() {
                const selectedBrand = this.value;
                loadModelsByBrand(selectedBrand);
                // 清空型号选择
                if (modelSelect) {
                    modelSelect.value = '';
                }
            });
        }
        
        // 绑定搜索按钮点击事件
        searchBtn.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            console.log('搜索按钮被点击');
            performSearch();
        });
        
        // 绑定关键字输入框回车事件
        if (keywordInput) {
            keywordInput.addEventListener('keyup', function(e) {
                if (e.key === 'Enter' || e.keyCode === 13) {
                    e.preventDefault();
                    console.log('回车键触发搜索');
                    performSearch();
                }
            });
        }
        
        // 绑定表单提交事件
        searchForm.addEventListener('submit', function(e) {
            e.preventDefault();
            e.stopPropagation();
            console.log('表单提交事件触发');
            performSearch();
        });
        
        // 从URL填充表单
        fillFormFromUrl();
    }
    
    // 页面加载完成后初始化
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initSearchForm);
    } else {
        // DOM已经加载完成，延迟一点确保所有元素都已渲染
        setTimeout(initSearchForm, 100);
    }
})();
</script>
