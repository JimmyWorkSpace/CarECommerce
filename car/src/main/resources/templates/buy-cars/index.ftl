<link href="/css/buy-cars.css" rel="stylesheet">

<div class="buy-cars-page">
    <!-- 搜索框 -->
    <div class="search-header">
        <div class="container">
            <div class="search-box">
                <div class="search-input-group">
                    <input type="text" class="form-control search-input" placeholder="搜索车型、品牌或关键词..." value="${searchKeyword!''}">
                    <button class="btn search-btn">
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
                    <h4 class="filter-title">筛选条件</h4>
                    
                    <!-- 品牌筛选 -->
                    <div class="filter-section">
                        <h5 class="filter-section-title">品牌</h5>
                        <div class="filter-options">
                            <#list brands as brandItem>
                            <label class="filter-option">
                                <input type="checkbox" name="brand" value="${brandItem}" <#if brandItem == (brand!'')>checked</#if>>
                                <span class="checkmark"></span>
                                ${brandItem}
                            </label>
                            </#list>
                        </div>
                    </div>

                    <!-- 价格范围 -->
                    <div class="filter-section">
                        <h5 class="filter-section-title">价格范围</h5>
                        <div class="price-range">
                            <input type="number" class="form-control" placeholder="最低价格" value="${priceMin!''}">
                            <span class="price-separator">-</span>
                            <input type="number" class="form-control" placeholder="最高价格" value="${priceMax!''}">
                        </div>
                    </div>

                    <!-- 年份 -->
                    <div class="filter-section">
                        <h5 class="filter-section-title">年份</h5>
                        <select class="form-control">
                            <option value="">所有年份</option>
                            <option value="2024">2024</option>
                            <option value="2023">2023</option>
                            <option value="2022">2022</option>
                            <option value="2021">2021</option>
                            <option value="2020">2020</option>
                            <option value="2019">2019</option>
                            <option value="2018">2018</option>
                        </select>
                    </div>

                    <!-- 燃料类型 -->
                    <div class="filter-section">
                        <h5 class="filter-section-title">燃料类型</h5>
                        <div class="filter-options">
                            <#list fuelTypes as fuelTypeItem>
                            <label class="filter-option">
                                <input type="checkbox" name="fuelType" value="${fuelTypeItem}" <#if fuelTypeItem == (fuelType!'')>checked</#if>>
                                <span class="checkmark"></span>
                                ${fuelTypeItem}
                            </label>
                            </#list>
                        </div>
                    </div>

                    <!-- 变速箱 -->
                    <div class="filter-section">
                        <h5 class="filter-section-title">变速箱</h5>
                        <div class="filter-options">
                            <#list transmissions as transmissionItem>
                            <label class="filter-option">
                                <input type="checkbox" name="transmission" value="${transmissionItem}" <#if transmissionItem == (transmission!'')>checked</#if>>
                                <span class="checkmark"></span>
                                ${transmissionItem}
                            </label>
                            </#list>
                        </div>
                    </div>

                    <!-- 车身类型 -->
                    <div class="filter-section">
                        <h5 class="filter-section-title">车身类型</h5>
                        <div class="filter-options">
                            <#list bodyTypes as bodyTypeItem>
                            <label class="filter-option">
                                <input type="checkbox" name="bodyType" value="${bodyTypeItem}" <#if bodyTypeItem == (bodyType!'')>checked</#if>>
                                <span class="checkmark"></span>
                                ${bodyTypeItem}
                            </label>
                            </#list>
                        </div>
                    </div>

                    <!-- 里程数 -->
                    <div class="filter-section">
                        <h5 class="filter-section-title">里程数</h5>
                        <select class="form-control">
                            <option value="">所有里程</option>
                            <option value="10000">10,000 km以下</option>
                            <option value="30000">30,000 km以下</option>
                            <option value="50000">50,000 km以下</option>
                            <option value="100000">100,000 km以下</option>
                        </select>
                    </div>

                    <!-- 应用筛选按钮 -->
                    <button class="btn btn-primary apply-filters-btn">
                        <i class="bi bi-funnel"></i> 应用筛选
                    </button>
                </div>
            </div>

            <!-- 右侧车辆列表 -->
            <div class="col-lg-9 col-md-8">
                <div class="cars-content">
                    <!-- 结果统计 -->
                    <div class="results-header">
                        <div class="results-count">
                            找到 <strong>${totalCars}</strong> 辆车
                        </div>
                        <div class="sort-options">
                            <select class="form-control sort-select">
                                <option value="newest">最新发布</option>
                                <option value="price-low">价格从低到高</option>
                                <option value="price-high">价格从高到低</option>
                                <option value="year-new">年份最新</option>
                            </select>
                        </div>
                    </div>

                    <!-- 车辆网格 -->
                    <div class="cars-grid">
                        <#list cars as car>
                        <div class="car-card">
                            <div class="car-image-container">
                                <img src="${car.image}" alt="${car.title}" class="car-image">
                                <div class="car-badge">${car.year}</div>
                            </div>
                            <div class="car-info">
                                <h3 class="car-title">${car.title}</h3>
                                <div class="car-details">
                                    <span class="car-mileage"><i class="bi bi-speedometer2"></i> ${car.mileage} km</span>
                                    <span class="car-fuel"><i class="bi bi-fuel-pump"></i> ${car.fuelType}</span>
                                    <span class="car-transmission"><i class="bi bi-gear"></i> ${car.transmission}</span>
                                </div>
                                <div class="car-price">
                                    <span class="price-amount">$${car.price?string("###,###")}</span>
                                </div>
                                <div class="car-actions">
                                    <a href="/static-demo" class="btn btn-outline-primary btn-sm">查看详情</a>
                                    <button class="btn btn-outline-secondary btn-sm">
                                        <i class="bi bi-heart"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                        </#list>
                    </div>

                    <!-- 分页 -->
                    <#if totalPages gt 1>
                    <div class="pagination-container">
                        <nav aria-label="车辆列表分页">
                            <ul class="pagination justify-content-center">
                                <#if currentPage gt 1>
                                <li class="page-item">
                                    <a class="page-link" href="?page=${currentPage - 1}">上一页</a>
                                </li>
                                </#if>
                                
                                <#list 1..totalPages as pageNum>
                                <li class="page-item <#if pageNum == currentPage>active</#if>">
                                    <a class="page-link" href="?page=${pageNum}">${pageNum}</a>
                                </li>
                                </#list>
                                
                                <#if currentPage lt totalPages>
                                <li class="page-item">
                                    <a class="page-link" href="?page=${currentPage + 1}">下一页</a>
                                </li>
                                </#if>
                            </ul>
                        </nav>
                    </div>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</div> 