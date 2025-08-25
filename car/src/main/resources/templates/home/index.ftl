<link href="/css/home.css" rel="stylesheet">
<link href="/assets/swiper/css/swiper.min.css" rel="stylesheet">
<script src="/assets/swiper/js/swiper.min.js"></script>
<div class="home-page" id="app">
    <!-- 第一行：轮播图 -->
    <div class="hero-section">
        <div class="swiper hero-swiper">
            <div class="swiper-wrapper">
                <#list heroSlides as slide>
                <div class="swiper-slide">
                    <#if slide.isLink == "true">
                    <a href="${slide.link}" class="hero-slide-link">
                        <img src="${slide.image}" alt="${slide.title}" class="hero-image">
                        <div class="hero-content">
                            <h2 class="hero-title">${slide.title}</h2>
                            <p class="hero-subtitle">${slide.subtitle}</p>
                            <span class="hero-btn">了解更多</span>
                        </div>
                    </a>
                    <#else>
                    <img src="${slide.image}" alt="${slide.title}" class="hero-image">
                    <div class="hero-content">
                        <h2 class="hero-title">${slide.title}</h2>
                        <p class="hero-subtitle">${slide.subtitle}</p>
                    </div>
                    </#if>
                </div>
                </#list>
            </div>
            <!-- 分页器 -->
            <div class="swiper-pagination"></div>
        </div>
    </div>

    <!-- 第二行：功能区域 -->
    <div class="features-section">
        <div class="container">
            <div class="row">
                <div class="col-12 col-md-4 mb-3" v-for="(feature, index) in features" :key="index">
                    <div class="feature-card">
                        <div class="feature-icon">
                            <i :class="feature.icon"></i>
                        </div>
                        <h3 class="feature-title" v-text="feature.title"></h3>
                        <p class="feature-subtitle" v-text="feature.subtitle"></p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 第三行：搜索框 -->
    <div class="search-section">
        <div class="container">
            <div class="search-container">
                <h2 class="search-title">尋找您的理想座駕</h2>
                <div class="search-box">
                    <div class="search-input-group">
                        <input type="text" class="form-control search-input" placeholder="輸入車型、品牌或關鍵詞..." v-model="searchKeyword">
                        <button class="btn search-btn" @click="performSearch">
                            <i class="bi bi-search"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 第四行：广告位 -->
    <div class="ad-section">
        <div class="container">
            <div class="row">
                <#if advertisements?? && advertisements?size gt 0>
                    <#list advertisements as ad>
                    <div class="col-12 col-md-6 mb-4">
                        <div class="ad-card" data-ad-id="${ad.id}">
                            <#if ad.isLink == 1>
                            <a href="${ad.linkUrl}" class="ad-link" target="_blank">
                                <img src="${ad.imageUrl}" alt="${ad.title}" class="ad-image" 
                                     onerror="this.style.display='none'; this.nextElementSibling.style.display='block';">
                                <#if ad.title?? && ad.title != ''>
                                <div class="ad-title-overlay" style="display: none;">${ad.title}</div>
                                </#if>
                            </a>
                            <#else>
                            <div class="ad-content-link" onclick="showAdContent('${ad.id?string}', '${ad.title?js_string}', '${ad.content?js_string}')">
                                <img src="${ad.imageUrl}" alt="${ad.title}" class="ad-image"
                                     onerror="this.style.display='none'; this.nextElementSibling.style.display='block';">
                                <#if ad.title?? && ad.title != ''>
                                <div class="ad-title-overlay" style="display: none;">${ad.title}</div>
                                </#if>
                            </div>
                            </#if>
                        </div>
                    </div>
                    </#list>
                <#else>
                    <!-- 默认广告内容 -->
                    <div class="col-12 col-md-6 mb-4">
                        <div class="ad-card">
                            <img src="/img/ad1.jpg" alt="专业检测" class="ad-image" 
                                 onerror="this.style.display='none'; this.nextElementSibling.style.display='block';">
                            <div class="ad-title-overlay" style="display: none;">專業檢測</div>
                        </div>
                    </div>
                    <div class="col-12 col-md-6 mb-4">
                        <div class="ad-card">
                            <iframe src="/ad-content.html" class="ad-iframe" frameborder="0"></iframe>
                        </div>
                    </div>
                </#if>
            </div>
        </div>
    </div>

    <!-- 第五行：精选店家 -->
    <div class="dealers-section">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title">精選店家</h2>
                <a href="/dealers" class="view-more-link">查看更多 <i class="bi bi-arrow-right"></i></a>
            </div>
            <div class="dealers-container">
                <#list dealers as dealer>
                <div class="dealer-card">
                    <img src="${dealer.image}" alt="${dealer.name}" class="dealer-image">
                    <h3 class="dealer-name">${dealer.name}</h3>
                </div>
                </#list>
            </div>
        </div>
    </div>

    <!-- 第六行：精选好车 -->
    <div class="cars-section">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title">精選好車</h2>
                <a href="/buy-cars" class="view-more-link">查看更多 <i class="bi bi-arrow-right"></i></a>
            </div>
            <div class="cars-container">
                <#list cars as car>
                <div class="car-card">
                    <img src="${car.image}" alt="${car.model}" class="car-image">
                    <h3 class="car-model">${car.model}</h3>
                    <p class="car-price">$${car.price}</p>
                </div>
                </#list>
            </div>
        </div>
    </div>

    <!-- 第七行：页脚 -->
    <footer class="footer">
        <div class="container">
            <div class="row">
                <div class="col-12 col-md-4 mb-4">
                    <h4>聯繫我們</h4>
                    <p><i class="bi bi-telephone"></i> 客服熱線：400-123-4567</p>
                    <p><i class="bi bi-envelope"></i> 郵箱：service@carce.cc</p>
                    <p><i class="bi bi-geo-alt"></i> 地址：台北市信義區信義路五段7號</p>
                </div>
                <div class="col-12 col-md-4 mb-4">
                    <h4>服務時間</h4>
                    <p>週一至週五：9:00-18:00</p>
                    <p>週六至週日：10:00-17:00</p>
                    <p>節假日：10:00-16:00</p>
                </div>
                <div class="col-12 col-md-4 mb-4">
                    <h4>關注我們</h4>
                    <div class="social-links">
                        <a href="#" class="social-link"><i class="bi bi-facebook"></i></a>
                        <a href="#" class="social-link"><i class="bi bi-instagram"></i></a>
                        <a href="#" class="social-link"><i class="bi bi-twitter"></i></a>
                        <a href="#" class="social-link"><i class="bi bi-youtube"></i></a>
                    </div>
                </div>
            </div>
            <div class="footer-bottom">
                <p>&copy; 2024 二手車銷售平台. 保留所有權利.</p>
            </div>
        </div>
    </footer>
</div>

<script>
new Vue({
    el: '#app',
    data: {
        searchKeyword: '',
        features: [
            {
                icon: 'bi bi-shield-check',
                title: '品質保證',
                subtitle: '所有車輛均經過專業檢測，確保車況良好'
            },
            {
                icon: 'bi bi-currency-dollar',
                title: '價格透明',
                subtitle: '明碼標價，無隱藏費用，讓您購車更放心'
            },
            {
                icon: 'bi bi-headset',
                title: '專業服務',
                subtitle: '專業團隊為您提供全程購車服務'
            }
        ],


    },
    mounted() {
        this.initHeroSlider();
    },
    methods: {
        performSearch() {
            if (this.searchKeyword.trim()) {
                // 執行搜尋邏輯
                console.log('搜尋關鍵詞:', this.searchKeyword);
            }
        },
        initHeroSlider() {
            // 初始化Swiper輪播圖
            new Swiper('.hero-swiper', {
                // 循環模式
                loop: true,
                // 自動播放
                autoplay: {
                    delay: 5000,
                    disableOnInteraction: false,
                },
                // 淡入淡出效果
                effect: 'fade',
                fadeEffect: {
                    crossFade: true
                },
                // 分頁器
                pagination: {
                    el: '.swiper-pagination',
                    clickable: true,
                },

                // 速度
                speed: 500,
            });
        },

        // 顯示廣告內容
        showAdContent(adId, title, content) {
            // 檢查adId是否有效
            if (!adId || adId === 'null' || adId === '') {
                console.error('廣告ID無效:', adId);
                return;
            }
            // 打開新窗口顯示廣告內容頁面
            window.open('/ad-content/' + adId, '_blank', 'width=800,height=600,scrollbars=yes,resizable=yes');
        },

    }
});
</script> 