<link href="/css/car-detail.css" rel="stylesheet">
<link rel="canonical" href="${ogUrl!''}">
<div class="dealer-detail" id="app">
    <!-- 第一行：图片和信息 -->
    <div class="row mb-4">
        <!-- 左侧图片轮播 -->
        <div class="col-md-6">
            <div class="swiper-wrapper-container" @mouseenter="stopAutoPlay" @mouseleave="startAutoPlay">
                <div class="swiper-container">
                    <div v-for="(photo, index) in photos" 
                         :key="index"
                         class="swiper-slide">
                        <div class="image-container">
                            <img class="swiper_image" 
                                 :src="photo" 
                                 :alt="dealerInfo.dealerName || '車商圖片'"
                                 @click="openLightbox(index)"
                                 @error="handleImageError">
                        </div>
                    </div>
                    <!-- 如果没有图片，显示默认图片 -->
                    <div v-if="!photos || photos.length === 0" class="swiper-slide">
                        <div class="image-container">
                            <img class="swiper_image" 
                                 src="/img/car/car4.jpg" 
                                 alt="車商圖片"
                                 @error="handleImageError">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- 右侧信息 -->
        <div class="col-md-6">
            <div class="dealer-info-section">
                <div class="info-row" v-if="dealerInfo.registeredName">
                    <div class="info-label">
                        <i class="bi bi-building"></i> 登記名稱
                    </div>
                    <div class="info-value">{{ dealerInfo.registeredName }}</div>
                </div>
                <div class="info-row" v-if="dealerInfo.contactPerson">
                    <div class="info-label">
                        <i class="bi bi-person"></i> 聯絡人
                    </div>
                    <div class="info-value">{{ dealerInfo.contactPerson }}</div>
                </div>
                <div class="info-row" v-if="dealerInfo.companyPhone">
                    <div class="info-label">
                        <i class="bi bi-telephone"></i> 公司電話
                    </div>
                    <div class="info-value">
                        <a :href="'tel:' + dealerInfo.companyPhone" class="contact-link">{{ dealerInfo.companyPhone }}</a>
                    </div>
                </div>
                <div class="info-row" v-if="dealerInfo.companyMobile">
                    <div class="info-label">
                        <i class="bi bi-phone"></i> 行動電話
                    </div>
                    <div class="info-value">
                        <a :href="'tel:' + dealerInfo.companyMobile" class="contact-link">{{ dealerInfo.companyMobile }}</a>
                    </div>
                </div>
                <div class="info-row" v-if="dealerInfo.publicAddress">
                    <div class="info-label">
                        <i class="bi bi-geo-alt"></i> 賞車地址
                    </div>
                    <div class="info-value">
                        <a :href="'https://www.google.com/maps/search/?api=1&query=' + encodeURIComponent(dealerInfo.publicAddress)" 
                           target="_blank" class="map-icon-link" title="查看地圖">
                            <i class="bi bi-map"></i>
                        </a>
                        <span class="address-text">{{ dealerInfo.publicAddress }}</span>
                    </div>
                </div>
                <div class="info-row" v-if="dealerInfo.registeredAddress">
                    <div class="info-label">
                        <i class="bi bi-house"></i> 登記地址
                    </div>
                    <div class="info-value">{{ dealerInfo.registeredAddress }}</div>
                </div>
                <div class="info-row" v-if="dealerInfo.businessHours">
                    <div class="info-label">
                        <i class="bi bi-clock"></i> 營業時間
                    </div>
                    <div class="info-value">{{ dealerInfo.businessHours }}</div>
                </div>
                <div class="info-row" v-if="dealerInfo.website">
                    <div class="info-label">
                        <i class="bi bi-globe"></i> 官方網站
                    </div>
                    <div class="info-value">
                        <a :href="dealerInfo.website" target="_blank" class="website-link">{{ dealerInfo.website }}</a>
                    </div>
                </div>
            </div>
            <div class="action-buttons">
                <button class="btn btn-line" @click="openLineContact" v-if="dealerInfo.lineId">
                    <i class="bi bi-chat-dots"></i> 加LINE
                </button>
                <button class="btn btn-phone" @click="openPhoneContact" v-if="dealerInfo.companyPhone || dealerInfo.companyMobile">
                    <i class="bi bi-telephone"></i> 電話聯絡
                </button>
            </div>
        </div>
    </div>
    
    <!-- 第二行：Tab页 -->
    <div class="tabs">
        <ul class="nav nav-tabs">
            <li class="nav-item" v-for="(tab, index) in tabs" :key="index">
                <a class="nav-link" :class="{ active: activeTab.code === tab.code }" @click="activeTab = tab">
                    {{ tab.title }}
                </a>
            </li>
        </ul>
        <div class="tab-content p-3 border border-top-0 rounded-bottom">
            <!-- 車商介紹 Tab -->
            <div v-show="activeTab.code === 'dealer_intro'">
                <div v-if="dealerInfo.description" class="iframe-container">
                    <iframe ref="dealerContentFrame" 
                            class="content-frame" 
                            :srcdoc="getHtmlContent(dealerInfo.description)"
                            frameborder="0" 
                            width="100%">
                    </iframe>
                </div>
                <div v-else class="text-muted">暫無介紹內容</div>
            </div>
            
            <!-- 店家精選 Tab -->
            <div v-show="activeTab.code === 'dealer_cars'">
                <div v-if="cars && cars.length > 0" class="cars-list">
                    <div class="row">
                        <div class="col-12 col-sm-6 col-md-4 col-lg-3 mb-4" v-for="(car, index) in cars" :key="index">
                            <a :href="'/detail/' + car.id" class="car-card-link">
                                <div class="car-card">
                                    <img :src="car.image" :alt="car.model" class="car-image" @error="handleCarImageError">
                                    <h3 class="car-model">{{ car.model }}</h3>
                                    <p class="car-price">{{ '$' + car.price }}</p>
                                </div>
                            </a>
                        </div>
                    </div>
                </div>
                <div v-else class="text-muted text-center py-5">
                    暫無車輛信息
                </div>
            </div>
        </div>
    </div>
    
    <!-- 图片灯箱 -->
    <div v-if="visibleLightbox" class="lightbox" @click="closeLightbox">
        <div class="lightbox-content" @click.stop>
            <button class="lightbox-close" @click="closeLightbox">&times;</button>
            <button class="lightbox-prev" @click="prevLightboxImage" v-if="photos && photos.length > 1">&#10094;</button>
            <button class="lightbox-next" @click="nextLightboxImage" v-if="photos && photos.length > 1">&#10095;</button>
            <img :src="photos[lightboxIndex]" :alt="dealerInfo.dealerName" class="lightbox-image">
            <div class="lightbox-counter" v-if="photos && photos.length > 1">
                {{ lightboxIndex + 1 }} / {{ photos.length }}
            </div>
        </div>
    </div>
</div>

<script>
const dealerInfoData = ${dealerInfoJson};
const dealerId = ${dealerId};

new Vue({
    el: '#app',
    data: {
        dealerInfo: dealerInfoData || {},
        photos: dealerInfoData && dealerInfoData.photos ? dealerInfoData.photos : [],
        currentImageIndex: 0,
        visibleLightbox: false,
        lightboxIndex: 0,
        autoPlayTimer: null,
        slickInstance: null,
        activeTab: { code: 'dealer_intro', title: '車商介紹' },
        tabs: [
            { code: 'dealer_intro', title: '車商介紹' },
            { code: 'dealer_cars', title: '店家精選' }
        ],
        cars: ${carsJson} || []
    },
    mounted() {
        this.initSwiper();
        this.setupContentFrame();
    },
    beforeDestroy() {
        this.stopAutoPlay();
        if (this.slickInstance) {
            $(this.$el).find('.swiper-container').slick('unslick');
        }
    },
    methods: {
        initSwiper() {
            let _this = this;
            this.$nextTick(() => {
                const swiperContainer = $(this.$el).find('.swiper-container');
                if (swiperContainer.length) {
                    // 如果有图片，初始化轮播
                    if (this.photos && this.photos.length > 0) {
                        swiperContainer.slick({
                            infinite: true,
                            speed: 300,
                            slidesToShow: 1,
                            slidesToScroll: 1,
                            autoplay: this.photos.length > 1,
                            autoplaySpeed: 3000,
                            arrows: this.photos.length > 1,
                            dots: this.photos.length > 1,
                            prevArrow: '<div class="swiper-button-prev"></div>',
                            nextArrow: '<div class="swiper-button-next"></div>',
                            fade: true,
                            cssEase: 'linear'
                        }).on('afterChange', function(event, slick, currentSlide) {
                            _this.currentImageIndex = currentSlide;
                        });
                    } else {
                        // 没有图片时，也初始化但不显示导航
                        swiperContainer.slick({
                            infinite: false,
                            speed: 300,
                            slidesToShow: 1,
                            slidesToScroll: 1,
                            autoplay: false,
                            arrows: false,
                            dots: false,
                            fade: false
                        });
                    }
                    this.slickInstance = swiperContainer;
                }
            });
        },
        changeImage(index) {
            if (this.slickInstance) {
                this.slickInstance.slick('slickGoTo', index);
            } else {
                this.currentImageIndex = index;
            }
        },
        prevImage() {
            if (this.slickInstance) {
                this.slickInstance.slick('slickPrev');
            } else if (this.photos && this.photos.length > 0) {
                this.currentImageIndex = (this.currentImageIndex - 1 + this.photos.length) % this.photos.length;
            }
        },
        nextImage() {
            if (this.slickInstance) {
                this.slickInstance.slick('slickNext');
            } else if (this.photos && this.photos.length > 0) {
                this.currentImageIndex = (this.currentImageIndex + 1) % this.photos.length;
            }
        },
        openLightbox(index) {
            this.lightboxIndex = index;
            this.visibleLightbox = true;
            document.body.style.overflow = 'hidden';
        },
        closeLightbox() {
            this.visibleLightbox = false;
            document.body.style.overflow = '';
        },
        prevLightboxImage() {
            if (this.photos && this.photos.length > 0) {
                this.lightboxIndex = (this.lightboxIndex - 1 + this.photos.length) % this.photos.length;
            }
        },
        nextLightboxImage() {
            if (this.photos && this.photos.length > 0) {
                this.lightboxIndex = (this.lightboxIndex + 1) % this.photos.length;
            }
        },
        getThumbnailUrl(url) {
            if (!url) return '';
            // 如果URL包含图片处理参数，直接返回
            if (url.includes('?') || url.includes('imageView')) {
                return url;
            }
            // 否则添加缩略图参数（根据实际图片服务调整）
            return url;
        },
        handleImageError(event) {
            event.target.src = '/img/car/car4.jpg';
        },
        openLineContact() {
            if (this.dealerInfo.lineId) {
                window.open('https://line.me/ti/p/' + this.dealerInfo.lineId, '_blank');
            }
        },
        openPhoneContact() {
            const phone = this.dealerInfo.companyPhone || this.dealerInfo.companyMobile;
            if (phone) {
                window.location.href = 'tel:' + phone;
            }
        },
        startAutoPlay() {
            if (this.slickInstance && this.photos && this.photos.length > 1) {
                this.slickInstance.slick('slickPlay');
            }
        },
        stopAutoPlay() {
            if (this.slickInstance) {
                this.slickInstance.slick('slickPause');
            }
            if (this.autoPlayTimer) {
                clearInterval(this.autoPlayTimer);
                this.autoPlayTimer = null;
            }
        },
        handleCarImageError(event) {
            event.target.src = '/img/car/car4.jpg';
        },
        setupContentFrame() {
            this.$nextTick(() => {
                const frame = this.$refs.dealerContentFrame;
                if (frame) {
                    frame.onload = () => {
                        this.adjustIframeHeight(frame);
                    };
                    if (frame.contentDocument && frame.contentDocument.readyState === 'complete') {
                        frame.onload();
                    }
                }
            });
        },
        adjustIframeHeight(frame) {
            try {
                const frameDoc = frame.contentWindow.document;
                const frameBody = frameDoc.body;
                
                if (!frameBody) return;
                
                const contentHeight = Math.max(
                    frameBody.scrollHeight,
                    frameBody.offsetHeight,
                    frameDoc.documentElement.scrollHeight,
                    frameDoc.documentElement.offsetHeight
                );
                
                frame.style.height = (contentHeight + 20) + 'px';
            } catch (e) {
                console.error('调整iframe高度失败', e);
                frame.style.height = '300px';
            }
        },
        getHtmlContent(htmlContent) {
            if (!htmlContent) return '';
            return '<!DOCTYPE html><html><head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1"><style>body { margin: 0; padding: 20px; font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; line-height: 1.8; color: #333; } img { max-width: 100%; height: auto; }</style></head><body>' + htmlContent + '</body></html>';
        }
    }
});
</script>

<style>
.dealer-detail {
    padding: 20px;
    max-width: 1200px;
    margin: 0 auto;
    overflow-x: hidden;
}

.dealer-title {
    color: #5ACFC9;
    font-size: 2rem;
    font-weight: 560;
    margin-bottom: 25px;
    padding-bottom: 15px;
    border-bottom: 3px solid #5ACFC9;
}

.dealer-info-section {
    padding: 5px 25px;
    background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.08);
    margin-bottom: 25px;
    text-align: left;
}

.info-row {
    display: flex;
    align-items: flex-start;
    padding: 15px 0;
    border-bottom: 1px solid #e9ecef;
    transition: background-color 0.2s ease;
    text-align: left;
}

.info-row:last-child {
    border-bottom: none;
}

.info-row:hover {
    background-color: rgba(90, 207, 201, 0.05);
    margin: 0 -10px;
    padding-left: 10px;
    padding-right: 10px;
    border-radius: 6px;
}

.info-label {
    flex: 0 0 140px;
    font-size: 0.95rem;
    font-weight: 600;
    color: #5ACFC9;
    display: flex;
    align-items: center;
    gap: 8px;
    text-align: left;
    justify-content: flex-start;
}

.info-label i {
    font-size: 1.1rem;
}

.info-value {
    flex: 1;
    font-size: 1rem;
    font-weight: 500;
    color: #333;
    line-height: 1.6;
    text-align: left;
}

.contact-link,
.map-link,
.website-link,
.line-link {
    color: #007bff;
    text-decoration: none;
    transition: all 0.2s ease;
    display: inline-flex;
    align-items: center;
    gap: 5px;
}

.contact-link:hover,
.map-link:hover,
.website-link:hover,
.line-link:hover {
    color: #0056b3;
    text-decoration: underline;
}

.map-icon-link {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 28px;
    height: 28px;
    margin-right: 10px;
    color: #007bff;
    background: #f0f8ff;
    border-radius: 6px;
    text-decoration: none;
    transition: all 0.2s ease;
    vertical-align: middle;
}

.map-icon-link:hover {
    background: #e0f0ff;
    color: #0056b3;
    transform: scale(1.1);
    text-decoration: none;
}

.map-icon-link i {
    font-size: 1.1rem;
}

.address-text {
    display: inline-block;
    vertical-align: middle;
    text-align: left;
}

.line-link {
    color: #00C300;
    font-weight: 600;
}

.line-link:hover {
    color: #00A300;
}

.section-title {
    border-bottom: 3px solid #5ACFC9;
    padding-bottom: 12px;
    margin-bottom: 25px;
}

.section-title h3 {
    margin: 0;
    color: #333;
    font-size: 1.8rem;
    font-weight: 560;
}

.iframe-container {
    padding: 0;
}

.content-frame {
    width: 100%;
    min-height: 200px;
    border: none;
    display: block;
    background: transparent;
    margin: 0;
    padding: 0;
}

/* Tab样式 */
.tabs {
    margin-top: 30px;
}

.nav-tabs {
    border-bottom: 2px solid #5ACFC9;
}

.nav-tabs .nav-link {
    border-radius: 0;
    border-color: transparent;
    color: #666666;
    font-size: 1rem;
    padding: 0.75rem 1.5rem;
    transition: all 0.3s ease;
}

.nav-tabs .nav-link:hover {
    color: #5ACFC9;
    border-color: transparent;
    background-color: rgba(90, 207, 201, 0.1);
}

.nav-tabs .nav-link.active {
    color: white;
    background-color: #5ACFC9;
    border-color: #5ACFC9;
    cursor: pointer;
}

.tab-content {
    background-color: #fff;
    border-radius: 0 0 8px 8px;
    min-height: 200px;
    padding: 25px;
}

/* 车辆列表样式 */
.cars-list {
    padding: 0;
}

.car-card-link {
    text-decoration: none;
    color: inherit;
    display: block;
    height: 100%;
}

.car-card-link:hover {
    text-decoration: none;
    color: inherit;
}

.car-card {
    background: white;
    border-radius: 10px;
    overflow: hidden;
    box-shadow: 0 4px 6px rgba(0,0,0,0.1);
    transition: all 0.3s ease;
    height: 100%;
}

.car-card-link:hover .car-card {
    transform: translateY(-5px);
    box-shadow: 0 8px 15px rgba(0,0,0,0.2);
}

.car-image {
    width: 100%;
    height: 200px;
    object-fit: cover;
}

.car-model {
    padding: 15px 15px 5px;
    font-size: 1.2rem;
    font-weight: bold;
    color: #333;
    margin: 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.car-price {
    padding: 0 15px 15px;
    font-size: 1.1rem;
    color: #FA9F42;
    font-weight: bold;
    margin: 0;
}

/* 使用car-detail.css中的轮播图样式 */
.dealer-detail .swiper-wrapper-container {
    position: relative;
    width: 100%;
    overflow: hidden;
}

.dealer-detail .swiper-container {
    position: relative;
    width: 100%;
    overflow: hidden;
}

.dealer-detail .swiper-slide .image-container {
    position: relative;
    width: 100%;
    padding-bottom: 66.67%; /* 3:2 宽高比 */
    overflow: hidden;
    background-color: #f8f9fa;
}

.dealer-detail .swiper-slide .swiper_image {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
    object-position: center;
    cursor: pointer;
}

.dealer-detail .swiper-button-prev,
.dealer-detail .swiper-button-next {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    width: 40px;
    height: 40px;
    background: rgba(0, 0, 0, 0.5);
    color: white;
    border: none;
    border-radius: 50%;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24px;
    z-index: 10;
    transition: background 0.3s ease;
}

.dealer-detail .swiper-button-prev:hover,
.dealer-detail .swiper-button-next:hover {
    background: rgba(0, 0, 0, 0.7);
}

.dealer-detail .swiper-button-prev {
    left: 10px;
}

.dealer-detail .swiper-button-next {
    right: 10px;
}

.dealer-detail .swiper-button-prev::before {
    content: '‹';
}

.dealer-detail .swiper-button-next::before {
    content: '›';
}

/* Slick dots样式 */
.dealer-detail .slick-dots {
    position: absolute;
    bottom: 15px;
    left: 50%;
    transform: translateX(-50%);
    display: flex;
    gap: 8px;
    z-index: 10;
    list-style: none;
    padding: 0;
    margin: 0;
}

.dealer-detail .slick-dots li {
    width: 12px;
    height: 12px;
}

.dealer-detail .slick-dots li button {
    width: 12px;
    height: 12px;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.5);
    border: none;
    cursor: pointer;
    transition: background 0.3s ease;
    font-size: 0;
    padding: 0;
}

.dealer-detail .slick-dots li.slick-active button {
    background: #FA9F42;
}

.dealer-detail .slick-dots li button:hover {
    background: rgba(255, 255, 255, 0.8);
}

.action-buttons {
    display: flex;
    gap: 15px;
    margin-top: 25px;
    justify-content: flex-start;
}

.action-buttons .btn {
    flex: 1;
    padding: 12px 20px;
    border-radius: 8px;
    font-size: 1rem;
    font-weight: 600;
    border: none;
    cursor: pointer;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
}

.action-buttons .btn-line {
    background: linear-gradient(135deg, #00C300 0%, #00A300 100%);
    color: white;
    box-shadow: 0 4px 12px rgba(0, 195, 0, 0.3);
}

.action-buttons .btn-line:hover {
    background: linear-gradient(135deg, #00A300 0%, #008800 100%);
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(0, 195, 0, 0.4);
}

.action-buttons .btn-phone {
    background: linear-gradient(135deg, #007BFF 0%, #0056B3 100%);
    color: white;
    box-shadow: 0 4px 12px rgba(0, 123, 255, 0.3);
}

.action-buttons .btn-phone:hover {
    background: linear-gradient(135deg, #0056B3 0%, #004085 100%);
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(0, 123, 255, 0.4);
}

.lightbox {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.9);
    z-index: 9999;
    display: flex;
    align-items: center;
    justify-content: center;
}

.lightbox-content {
    position: relative;
    max-width: 90%;
    max-height: 90%;
}

.lightbox-image {
    max-width: 100%;
    max-height: 90vh;
    object-fit: contain;
}

.lightbox-close {
    position: absolute;
    top: -40px;
    right: 0;
    background: none;
    border: none;
    color: white;
    font-size: 40px;
    cursor: pointer;
    z-index: 10000;
    line-height: 1;
}

.lightbox-prev,
.lightbox-next {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    background: rgba(255, 255, 255, 0.3);
    border: none;
    color: white;
    font-size: 30px;
    padding: 10px 15px;
    cursor: pointer;
    z-index: 10000;
    border-radius: 4px;
}

.lightbox-prev:hover,
.lightbox-next:hover {
    background: rgba(255, 255, 255, 0.5);
}

.lightbox-prev {
    left: -50px;
}

.lightbox-next {
    right: -50px;
}

.lightbox-counter {
    position: absolute;
    bottom: -40px;
    left: 50%;
    transform: translateX(-50%);
    color: white;
    font-size: 16px;
}

@media (max-width: 768px) {
    .dealer-detail {
        padding: 10px;
    }
    
    .lightbox-prev {
        left: 10px;
    }
    
    .lightbox-next {
        right: 10px;
    }
    
    .lightbox-close {
        top: 10px;
        right: 10px;
    }
}
</style>


