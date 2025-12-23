<link href="/css/car-detail.css" rel="stylesheet">
<link href="/css/buy-cars.css" rel="stylesheet">
<link rel="canonical" href="${ogUrl!''}">
<div class="dealer-detail" id="app">
    <!-- 第一行：图片和信息 -->
    <div class="row mb-4">
        <!-- 左侧图片轮播 -->
        <div class="col-md-6">
            <div class="swiper-wrapper-container" @mouseenter="stopAutoPlay" @mouseleave="startAutoPlay">
                <div class="swiper-container">
                    <div class="swiper-wrapper">
                        <div v-for="(photo, index) in photos" 
                             :key="index"
                             class="swiper-slide"
                             :class="{ active: currentImageIndex === index }">
                            <div class="image-container">
                                <img class="swiper_image" 
                                     :src="photo" 
                                     :alt="dealerInfo.dealerName || '車商圖片'"
                                     @click="openLightbox(index)"
                                     @error="handleImageError">
                            </div>
                        </div>
                        <!-- 如果没有图片，显示默认图片 -->
                        <div v-if="!photos || photos.length === 0" class="swiper-slide active">
                            <div class="image-container">
                                <img class="swiper_image" 
                                     src="/img/car/car4.jpg" 
                                     alt="車商圖片"
                                     @error="handleImageError">
                            </div>
                        </div>
                    </div>
                    <!-- 輪播圖导航按钮 -->
                    <div class="swiper-button-prev" v-if="photos && photos.length > 1" @click="prevImage"></div>
                    <div class="swiper-button-next" v-if="photos && photos.length > 1" @click="nextImage"></div>
                    <!-- 輪播圖指示器 -->
                    <div class="swiper-pagination" v-if="photos && photos.length > 1">
                        <span v-for="(photo, index) in photos" 
                              :key="index"
                              class="swiper-pagination-bullet"
                              :class="{ active: currentImageIndex === index }"
                              @click="changeImage(index)"></span>
                    </div>
                </div>
                <div class="thumbnail-container" v-if="photos && photos.length > 1">
                    <img v-for="(photo, index) in photos" 
                         :key="index"
                         :src="getThumbnailUrl(photo)" 
                         class="thumbnail" 
                         :class="{ active: currentImageIndex === index }"
                         @click="changeImage(index)"
                         @error="handleThumbnailError"
                         alt="縮略圖">
                </div>
            </div>
        </div>
        
        <!-- 右侧信息 -->
        <div class="col-md-6">
            <div class="dealer-info-section">
                <div class="info-row">
                    <div class="info-label">
                        <i class="bi bi-building"></i> 登記名稱
                    </div>
                    <div class="info-value">{{ dealerInfo.dealerName }}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">
                        <i class="bi bi-building"></i> 對外營業名稱
                    </div>
                    <div class="info-value">{{ dealerInfo.registeredName }}</div>
                </div>
                <div class="info-row" >
                    <div class="info-label">
                        <i class="bi bi-building"></i> 統一編號
                    </div>
                    <div class="info-value">{{ dealerInfo.taxId }}</div>
                </div>
                <div class="info-row" >
                    <div class="info-label">
                        <i class="bi bi-person"></i> 聯絡人
                    </div>
                    <div class="info-value">{{ dealerInfo.contactPerson }}</div>
                </div>
                <div class="info-row" >
                    <div class="info-label">
                        <i class="bi bi-telephone"></i> 公司電話
                    </div>
                    <div class="info-value">
                        <a :href="'tel:' + dealerInfo.companyPhone" class="contact-link">{{ dealerInfo.companyPhone }}</a>
                    </div>
                </div>
                <div class="info-row">
                    <div class="info-label">
                        <i class="bi bi-phone"></i> 行動電話
                    </div>
                    <div class="info-value">
                        <a :href="'tel:' + dealerInfo.companyMobile" class="contact-link">{{ dealerInfo.companyMobile }}</a>
                    </div>
                </div>
                <div class="info-row" >
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
                <div v-if="cars && cars.length > 0" class="cars-list-detail-style">
                    <div class="row">
                        <div v-for="(car, index) in cars" :key="index" class="col-12 col-lg-6 mb-3">
                            <a :href="'/detail/' + car.id" class="car-item-detail-link">
                                <div class="car-item-detail">
                                    <div class="row">
                                        <!-- 左侧图片 -->
                                        <div class="col-md-4">
                                            <div class="car-image-wrapper">
                                                <img :src="car.coverImage || '/img/car/car6.jpg'" 
                                                     :alt="car.saleTitleJoin || ''" 
                                                     class="car-detail-image" 
                                                     @error="handleCarImageError">
                                            </div>
                                        </div>
                                        
                                        <!-- 右侧信息 -->
                                        <div class="col-md-8">
                                            <h2 class="car-title">
                                                {{ formatTitle(car.brand, car.model, car.manufactureYear) }}
                                            </h2>
                                            <div class="price">
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
                                                        <div class="spec-name text-muted">公里數</div>
                                                        <div class="spec-value">{{ car.mileage ? formatMileage(car.mileage) : '--' }}</div>
                                                    </div>
                                                    <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                                                        <div class="spec-name text-muted">排氣量(L)</div>
                                                        <div class="spec-value">{{ car.displacement ? formatDisplacement(car.displacement) : '--' }}</div>
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
    mounted: function() {
        this.setupContentFrame();
        // 启动自动播放
        this.startAutoPlay();
    },
    beforeDestroy: function() {
        this.stopAutoPlay();
    },
    methods: {
        changeImage: function(index) {
            this.currentImageIndex = index;
        },
        prevImage: function() {
            if (this.photos && this.photos.length > 0) {
                this.currentImageIndex = (this.currentImageIndex - 1 + this.photos.length) % this.photos.length;
            }
        },
        nextImage: function() {
            if (this.photos && this.photos.length > 0) {
                this.currentImageIndex = (this.currentImageIndex + 1) % this.photos.length;
            }
        },
        openLightbox: function(index) {
            this.lightboxIndex = index;
            this.visibleLightbox = true;
            document.body.style.overflow = 'hidden';
        },
        closeLightbox: function() {
            this.visibleLightbox = false;
            document.body.style.overflow = '';
        },
        prevLightboxImage: function() {
            if (this.photos && this.photos.length > 0) {
                this.lightboxIndex = (this.lightboxIndex - 1 + this.photos.length) % this.photos.length;
            }
        },
        nextLightboxImage: function() {
            if (this.photos && this.photos.length > 0) {
                this.lightboxIndex = (this.lightboxIndex + 1) % this.photos.length;
            }
        },
        getThumbnailUrl: function(url) {
            if (!url) return '';
            
            // 检查URL是否已经包含_90x90后缀
            if (url.includes('_90x90.')) {
                return url;
            }
            
            // 获取文件扩展名
            var lastDotIndex = url.lastIndexOf('.');
            if (lastDotIndex === -1) {
                return url;
            }
            
            // 在扩展名前插入_90x90
            var baseUrl = url.substring(0, lastDotIndex);
            var extension = url.substring(lastDotIndex);
            
            return baseUrl + '_90x90' + extension;
        },
        handleImageError: function(event) {
            event.target.src = '/img/car/car4.jpg';
        },
        handleThumbnailError: function(event) {
            event.target.src = '/img/car/car4.jpg';
        },
        openLineContact: function() {
            if (this.dealerInfo.lineId) {
                window.open('https://line.me/ti/p/' + this.dealerInfo.lineId, '_blank');
            }
        },
        openPhoneContact: function() {
            var phone = this.dealerInfo.companyPhone || this.dealerInfo.companyMobile;
            if (phone) {
                window.location.href = 'tel:' + phone;
            }
        },
        startAutoPlay: function() {
            var _this = this;
            if (this.photos && this.photos.length > 1) {
                this.autoPlayTimer = setInterval(function() {
                    _this.nextImage();
                }, 5000);
            }
        },
        stopAutoPlay: function() {
            if (this.autoPlayTimer) {
                clearInterval(this.autoPlayTimer);
                this.autoPlayTimer = null;
            }
        },
        handleCarImageError: function(event) {
            event.target.src = '/img/car/car6.jpg';
        },
        formatPrice: function(price) {
            if (!price) return '0';
            return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        },
        formatMileage: function(mileage) {
            if (!mileage) return '0';
            return mileage.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        },
        formatDisplacement: function(displacement) {
            if (!displacement) return '--';
            const displacementNum = parseFloat(displacement);
            if (isNaN(displacementNum) || displacementNum <= 0) return '--';
            return (displacementNum / 1000).toFixed(1);
        },
        formatTitle: function(brand, model, year) {
            var parts = [];
            if (brand) parts.push(brand);
            if (model) parts.push(model);
            if (year) parts.push(year);
            return parts.join(' ');
        },
        setupContentFrame: function() {
            var _this = this;
            this.$nextTick(function() {
                var frame = _this.$refs.dealerContentFrame;
                if (frame) {
                    frame.onload = function() {
                        _this.adjustIframeHeight(frame);
                    };
                    if (frame.contentDocument && frame.contentDocument.readyState === 'complete') {
                        frame.onload();
                    }
                }
            });
        },
        adjustIframeHeight: function(frame) {
            try {
                var frameDoc = frame.contentWindow.document;
                var frameBody = frameDoc.body;
                
                if (!frameBody) return;
                
                var contentHeight = Math.max(
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
        getHtmlContent: function(htmlContent) {
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

.car-title{
    margin: 0px;
    font-size: 1rem;
}

.price{
    margin: 0px !important;
}

.cars-list-detail-style .spec-name {
    height: 1.2rem;
}

.cars-list-detail-style .spec-value {
    margin-top: 3px;
    height: 1.5rem;
    border-left: 3px solid #F0F0F2;
    padding-left: 0.5rem;
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

/* 轮播图样式 */
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

.dealer-detail .swiper-wrapper {
    display: flex;
    transition: transform 0.3s ease;
}

.dealer-detail .swiper-slide {
    min-width: 100%;
    flex-shrink: 0;
    display: none;
}

.dealer-detail .swiper-slide.active {
    display: block;
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

/* 轮播图指示器 */
.dealer-detail .swiper-pagination {
    position: absolute;
    bottom: 15px;
    left: 50%;
    transform: translateX(-50%);
    display: flex;
    gap: 8px;
    z-index: 10;
}

.dealer-detail .swiper-pagination-bullet {
    width: 12px;
    height: 12px;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.5);
    cursor: pointer;
    transition: background 0.3s ease;
}

.dealer-detail .swiper-pagination-bullet.active {
    background: #FA9F42;
}

.dealer-detail .swiper-pagination-bullet:hover {
    background: rgba(255, 255, 255, 0.8);
}

/* 缩略图容器 */
.dealer-detail .thumbnail-container {
    display: flex;
    gap: 10px;
    overflow-x: auto;
    padding-bottom: 10px;
    margin-top: 10px;
}

.dealer-detail .thumbnail-container .thumbnail {
    width: 80px;
    height: 60px;
    object-fit: cover;
    cursor: pointer;
    border: 2px solid transparent;
    border-radius: 4px;
    transition: all 0.3s ease;
    flex-shrink: 0;
}

.dealer-detail .thumbnail-container .thumbnail:hover {
    border-color: #5ACFC9;
    transform: scale(1.05);
}

.dealer-detail .thumbnail-container .thumbnail.active {
    border-color: #5ACFC9;
    box-shadow: 0 2px 8px rgba(90, 207, 201, 0.4);
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

/* 店家详情页店家精选车辆图片样式 - 高度填满，居中裁剪 */
.dealer-detail .cars-list-detail-style .car-item-detail .row {
    display: flex;
    align-items: stretch;
}

.dealer-detail .cars-list-detail-style .car-image-wrapper {
    height: 100%;
    padding-top: 0;
    display: flex;
    align-items: stretch;
}

.dealer-detail .cars-list-detail-style .car-detail-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
    object-position: center;
    position: relative;
}
</style>


