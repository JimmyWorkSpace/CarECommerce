<link href="/css/home.css" rel="stylesheet">
<div class="home-page" id="app">
    <!-- 第一行：轮播图 -->
    <div class="hero-section">
        <div class="hero-slider">
            <#list heroSlides as slide>
            <div class="hero-slide">
                <img src="${slide.image}" alt="${slide.title}" class="hero-image">
                <div class="hero-content">
                    <h2 class="hero-title">${slide.title}</h2>
                    <p class="hero-subtitle">${slide.subtitle}</p>
                    <a href="${slide.link}" class="hero-btn">了解更多</a>
                </div>
            </div>
            </#list>
        </div>
    </div>

    <!-- 第二行：功能区域 -->
    <div class="features-section">
        <div class="container">
            <div class="row">
                <div class="col-12 col-md-4 mb-4" v-for="(feature, index) in features" :key="index">
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
                <h2 class="search-title">寻找您的理想座驾</h2>
                <div class="search-box">
                    <div class="search-input-group">
                        <input type="text" class="form-control search-input" placeholder="输入车型、品牌或关键词..." v-model="searchKeyword">
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
                <div class="col-12 col-md-6 mb-4">
                    <div class="ad-card">
                        <img src="/img/ad1.jpg" alt="广告1" class="ad-image">
                        <div class="ad-content">
                            <h3>专业检测</h3>
                            <p>所有车辆均经过专业检测，确保车况良好</p>
                        </div>
                    </div>
                </div>
                <div class="col-12 col-md-6 mb-4">
                    <div class="ad-card">
                        <iframe src="/ad-content.html" class="ad-iframe" frameborder="0"></iframe>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 第五行：精选店家 -->
    <div class="dealers-section">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title">精选店家</h2>
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
                <h2 class="section-title">精选好车</h2>
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
                    <h4>联系我们</h4>
                    <p><i class="bi bi-telephone"></i> 客服热线：400-123-4567</p>
                    <p><i class="bi bi-envelope"></i> 邮箱：service@carce.cc</p>
                    <p><i class="bi bi-geo-alt"></i> 地址：台北市信义区信义路五段7号</p>
                </div>
                <div class="col-12 col-md-4 mb-4">
                    <h4>服务时间</h4>
                    <p>周一至周五：9:00-18:00</p>
                    <p>周六至周日：10:00-17:00</p>
                    <p>节假日：10:00-16:00</p>
                </div>
                <div class="col-12 col-md-4 mb-4">
                    <h4>关注我们</h4>
                    <div class="social-links">
                        <a href="#" class="social-link"><i class="bi bi-facebook"></i></a>
                        <a href="#" class="social-link"><i class="bi bi-instagram"></i></a>
                        <a href="#" class="social-link"><i class="bi bi-twitter"></i></a>
                        <a href="#" class="social-link"><i class="bi bi-youtube"></i></a>
                    </div>
                </div>
            </div>
            <div class="footer-bottom">
                <p>&copy; 2024 二手车销售平台. 保留所有权利.</p>
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
                title: '品质保证',
                subtitle: '所有车辆均经过专业检测，确保车况良好'
            },
            {
                icon: 'bi bi-currency-dollar',
                title: '价格透明',
                subtitle: '明码标价，无隐藏费用，让您购车更放心'
            },
            {
                icon: 'bi bi-headset',
                title: '专业服务',
                subtitle: '专业团队为您提供全程购车服务'
            }
        ],


    },
    mounted() {
        this.initHeroSlider();
    },
    methods: {
        performSearch() {
            if (this.searchKeyword.trim()) {
                // 执行搜索逻辑
                console.log('搜索关键词:', this.searchKeyword);
            }
        },
        initHeroSlider() {
            // 初始化轮播图
            $('.hero-slider').slick({
                dots: true,
                infinite: true,
                speed: 500,
                fade: true,
                cssEase: 'linear',
                autoplay: true,
                autoplaySpeed: 5000,
                arrows: true
            });
        },

    }
});
</script> 