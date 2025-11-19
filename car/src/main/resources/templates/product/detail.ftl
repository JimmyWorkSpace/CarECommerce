<link href="/css/car-detail.css" rel="stylesheet">
<link rel="canonical" href="${ogUrl!''}">
<div class="product-detail" id="app">
    <!-- 错误提示 -->
    <div v-if="error" class="alert alert-danger alert-dismissible fade show" role="alert" style="margin-bottom: 20px;">
        <i class="bi bi-exclamation-triangle me-2"></i>
        {{ error }}
        <button type="button" class="btn-close" @click="error = ''"></button>
    </div>
    
    <!-- 成功提示 -->
    <div v-if="message" class="alert alert-success alert-dismissible fade show" role="alert" style="margin-bottom: 20px;">
        <i class="bi bi-check-circle me-2"></i>
        {{ message }}
        <button type="button" class="btn-close" @click="message = ''"></button>
    </div>
    
    <!-- 第一行：图片和信息 -->
    <div class="row mb-4">
        <!-- 左侧图片轮播 -->
        <div class="col-md-6">
            <div class="swiper-wrapper-container" @mouseenter="stopAutoPlay" @mouseleave="startAutoPlay">
                <div class="swiper-container">
                    <div v-for="(image, index) in images" 
                         :key="index"
                         class="swiper-slide"
                         :class="{ active: currentImageIndex === index }">
                        <div class="image-container">
                            <img class="swiper_image" 
                                 :src="image" 
                                 :alt="product.name || '商品圖片'"
                                 @click="openLightbox(index)"
                                 @error="handleImageError">
                        </div>
                    </div>
                    <!-- 如果没有图片，显示默认图片 -->
                    <div v-if="!images || images.length === 0" class="swiper-slide active">
                        <div class="image-container">
                            <img class="swiper_image" 
                                 :src="imagePrefix + '/img/product/default.jpg'" 
                                 alt="商品圖片"
                                 @error="handleImageError">
                        </div>
                    </div>
                    <!-- 輪播圖导航按钮 -->
                    <div class="swiper-button-prev" v-if="images && images.length > 1" @click="prevImage"></div>
                    <div class="swiper-button-next" v-if="images && images.length > 1" @click="nextImage"></div>
                    <!-- 輪播圖指示器 -->
                    <div class="swiper-pagination" v-if="images && images.length > 1">
                        <span v-for="(image, index) in images" 
                              :key="index"
                              class="swiper-pagination-bullet"
                              :class="{ active: currentImageIndex === index }"
                              @click="changeImage(index)"></span>
                    </div>
                </div>
                <div class="thumbnail-container" v-if="images && images.length > 1">
                    <img v-for="(image, index) in images" 
                         :key="index"
                         :src="getThumbnailUrl(image)" 
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
            <h2 class="product-title" style="text-align: left">
                {{ product.name || '商品名稱' }}
            </h2>
            <#--  <div class="product-subtitle mb-3">
                <span v-if="product.brand" class="badge bg-secondary me-2">{{ product.brand }}</span>
                <span v-if="product.model" class="badge bg-info me-2">{{ product.model }}</span>
                <span v-if="product.tag" class="badge bg-warning me-2">{{ product.tag }}</span>
            </div>  -->
            
            <div class="price-section mb-4">
                <div class="current-price mb-2">
                    <span class="price-label text-muted">售價：</span>
                    <span class="price-symbol">${CurrencyUnit}</span>
                    <span class="price-amount h3">{{ formatPrice(product.price) }}</span>
                </div>
                <div class="market-price" v-if="product.marketPrice && product.marketPrice > product.price">
                    <span class="price-label text-muted">市價：</span>
                    <span class="price-symbol text-decoration-line-through text-muted">${CurrencyUnit}</span>
                    <span class="price-amount text-decoration-line-through text-muted">{{ formatPrice(product.marketPrice) }}</span>
                    <span class="discount-badge ms-2">省 ${CurrencyUnit}{{ formatPrice(product.marketPrice - product.price) }}</span>
                </div>
            </div>
            
            <div class="product-info-section mb-4">
                <#--  <div class="info-row" v-if="product.alias">
                    <div class="info-label">
                        <i class="bi bi-tag"></i> 商品別名
                    </div>
                    <div class="info-value">{{ product.alias }}</div>
                </div>  -->
                <#--  <div class="info-row" v-if="product.source">
                    <div class="info-label">
                        <i class="bi bi-box-seam"></i> 商品來源
                    </div>
                    <div class="info-value">{{ product.source }}</div>
                </div>  -->
                <div class="info-row" v-if="product.brand">
                    <div class="info-label">
                        <i class="bi bi-award"></i> 品牌
                    </div>
                    <div class="info-value">{{ product.brand }}</div>
                </div>
                <div class="info-row" v-if="product.model">
                    <div class="info-label">
                        <i class="bi bi-gear"></i> 型號
                    </div>
                    <div class="info-value">{{ product.model }}</div>
                </div>
                <div class="info-row" v-if="product.tag">
                    <div class="info-label">
                        <i class="bi bi-grid"></i> 類別
                    </div>
                    <div class="info-value">{{ product.tag }}</div>
                </div>
            </div>
            
            <div class="action-buttons">
                <button class="btn btn-primary btn-lg w-100 mb-2" 
                        @click.stop.prevent="addToCart($event)" 
                        :disabled="addingToCart"
                        type="button">
                    <i v-if="!addingToCart" class="bi bi-cart-plus"></i>
                    <span v-else class="spinner-border spinner-border-sm me-2" role="status"></span>
                    {{ addingToCart ? '添加中...' : '加入購物車' }}
                </button>
                <button class="btn btn-outline-secondary btn-lg w-100" @click="goBack">
                    <i class="bi bi-arrow-left"></i> 返回商城
                </button>
            </div>
        </div>
    </div>
    
    <!-- 第二行：商品详情Tab页 -->
    <div class="tabs">
        <ul class="nav nav-tabs">
            <li class="nav-item" v-for="(tab, index) in tabs" :key="index">
                <a class="nav-link" :class="{ active: activeTab.code === tab.code }" @click="activeTab = tab">
                    {{ tab.title }}
                </a>
            </li>
        </ul>
        <div class="tab-content p-3 border border-top-0 rounded-bottom">
            <!-- 商品詳情 Tab -->
            <div v-show="activeTab.code === 'product_detail'">
                <div v-if="product.memo" class="product-description">
                    <div class="iframe-container">
                        <iframe ref="productContentFrame" 
                                class="content-frame" 
                                :srcdoc="getHtmlContent(product.memo)"
                                frameborder="0" 
                                width="100%">
                        </iframe>
                    </div>
                </div>
                <div v-else class="text-muted text-center py-5">
                    暫無商品詳情
                </div>
            </div>
            
            <!-- 商品規格 Tab -->
            <div v-show="activeTab.code === 'product_spec'">
                <div class="specs-table">
                    <table class="table table-bordered">
                        <tbody>
                            <tr v-if="product.name">
                                <td class="spec-name">商品名稱</td>
                                <td class="spec-value">{{ product.name }}</td>
                            </tr>
                            <tr v-if="product.alias">
                                <td class="spec-name">商品別名</td>
                                <td class="spec-value">{{ product.alias }}</td>
                            </tr>
                            <tr v-if="product.brand">
                                <td class="spec-name">品牌</td>
                                <td class="spec-value">{{ product.brand }}</td>
                            </tr>
                            <tr v-if="product.model">
                                <td class="spec-name">型號</td>
                                <td class="spec-value">{{ product.model }}</td>
                            </tr>
                            <tr v-if="product.tag">
                                <td class="spec-name">類別</td>
                                <td class="spec-value">{{ product.tag }}</td>
                            </tr>
                            <tr v-if="product.source">
                                <td class="spec-name">來源</td>
                                <td class="spec-value">{{ product.source }}</td>
                            </tr>
                            <tr v-if="product.price">
                                <td class="spec-name">售價</td>
                                <td class="spec-value">${CurrencyUnit}{{ formatPrice(product.price) }}</td>
                            </tr>
                            <tr v-if="product.marketPrice">
                                <td class="spec-name">市價</td>
                                <td class="spec-value">${CurrencyUnit}{{ formatPrice(product.marketPrice) }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 图片灯箱 -->
    <div v-if="visibleLightbox" class="lightbox" @click="closeLightbox">
        <div class="lightbox-content" @click.stop>
            <button class="lightbox-close" @click="closeLightbox">&times;</button>
            <button class="lightbox-prev" @click="prevLightboxImage" v-if="images && images.length > 1">&#10094;</button>
            <button class="lightbox-next" @click="nextLightboxImage" v-if="images && images.length > 1">&#10095;</button>
            <img :src="images[lightboxIndex]" :alt="product.name" class="lightbox-image" @error="handleLightboxImageError">
            <div class="lightbox-counter" v-if="images && images.length > 1">
                {{ lightboxIndex + 1 }} / {{ images.length }}
            </div>
        </div>
    </div>
</div>

<script>
const productData = ${productJson};
const imagesData = ${imagesJson};
const CurrencyUnit = '${CurrencyUnit}';
const imagePrefix = '${imagePrefix}';

new Vue({
    el: '#app',
    data: {
        product: productData || {},
        images: imagesData || [],
        currentImageIndex: 0,
        visibleLightbox: false,
        lightboxIndex: 0,
        autoPlayTimer: null,
        slickInstance: null,
        activeTab: { code: 'product_detail', title: '商品詳情' },
        tabs: [
            { code: 'product_detail', title: '商品詳情' },
            { code: 'product_spec', title: '商品規格' }
        ],
        addingToCart: false,
        message: '',
        error: ''
    },
    mounted() {
        this.initSwiper();
        this.setupContentFrame();
        
        // 监听登录后待执行的操作
        const self = this;
        window.addEventListener('pendingAction', function(event) {
            if (event.detail && event.detail.action === 'addToCart') {
                // 登录后自动执行加购操作
                const pendingCartItem = sessionStorage.getItem('pendingAddToCart');
                if (pendingCartItem) {
                    try {
                        const cartItem = JSON.parse(pendingCartItem);
                        // 检查是否是当前商品
                        if (cartItem.productId === self.product.id) {
                            sessionStorage.removeItem('pendingAddToCart');
                            // 延迟执行，确保页面已完全加载
                            setTimeout(() => {
                                self.addToCart();
                            }, 300);
                        }
                    } catch (e) {
                        console.error('解析待加购商品信息失败:', e);
                    }
                }
            }
        });
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
                    if (this.images && this.images.length > 0) {
                        swiperContainer.slick({
                            infinite: true,
                            speed: 300,
                            slidesToShow: 1,
                            slidesToScroll: 1,
                            autoplay: this.images.length > 1,
                            autoplaySpeed: 3000,
                            arrows: this.images.length > 1,
                            dots: this.images.length > 1,
                            prevArrow: '<div class="swiper-button-prev"></div>',
                            nextArrow: '<div class="swiper-button-next"></div>',
                            fade: true,
                            cssEase: 'linear'
                        }).on('afterChange', function(event, slick, currentSlide) {
                            _this.currentImageIndex = currentSlide;
                        });
                    } else {
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
            } else if (this.images && this.images.length > 0) {
                this.currentImageIndex = (this.currentImageIndex - 1 + this.images.length) % this.images.length;
            }
        },
        nextImage() {
            if (this.slickInstance) {
                this.slickInstance.slick('slickNext');
            } else if (this.images && this.images.length > 0) {
                this.currentImageIndex = (this.currentImageIndex + 1) % this.images.length;
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
            if (this.images && this.images.length > 0) {
                this.lightboxIndex = (this.lightboxIndex - 1 + this.images.length) % this.images.length;
            }
        },
        nextLightboxImage() {
            if (this.images && this.images.length > 0) {
                this.lightboxIndex = (this.lightboxIndex + 1) % this.images.length;
            }
        },
        getThumbnailUrl(url) {
            if (!url) return '';
            // 如果URL包含图片处理参数，直接返回
            if (url.includes('?') || url.includes('imageView')) {
                return url;
            }
            // 将原图URL转换为缩略图URL（假设原图是 .jpg，缩略图是 _90x90.jpg）
            return url.replace('.jpg', '_90x90.jpg');
        },
        handleImageError(event) {
            // 防止无限重试：立即移除错误监听器并隐藏图片
            const target = event.target;
            
            // 如果已经处理过，直接返回并阻止默认行为
            if (target.dataset.errorHandled === 'true') {
                event.preventDefault();
                event.stopPropagation();
                return false;
            }
            
            // 立即标记为已处理，防止重复处理
            target.dataset.errorHandled = 'true';
            
            // 立即设置onerror为一个简单的处理器，阻止任何后续的错误处理
            // 这个处理器会在默认图片也加载失败时被调用
            target.onerror = function() {
                this.style.display = 'none';
                this.onerror = null;
                return false;
            };
            
            // 检查当前URL是否包含默认图片路径
            const currentSrc = target.src || '';
            const defaultImagePath = '/img/product/default.jpg';
            
            // 如果已经是默认图片，直接隐藏并阻止
            if (currentSrc.includes(defaultImagePath)) {
                target.style.display = 'none';
                target.onerror = null; // 确保不再触发错误
                event.preventDefault();
                event.stopPropagation();
                return false;
            }
            
            // 尝试加载默认图片（只尝试一次）
            // 如果默认图片也失败，上面设置的onerror会被调用
            const defaultImage = imagePrefix + defaultImagePath;
            target.src = defaultImage;
            
            // 阻止事件传播
            event.preventDefault();
            event.stopPropagation();
            return false;
        },
        handleThumbnailError(event) {
            // 缩略图错误处理，防止无限重试
            const target = event.target;
            
            // 如果已经处理过，直接返回并阻止
            if (target.dataset.errorHandled === 'true') {
                event.preventDefault();
                event.stopPropagation();
                return false;
            }
            
            // 立即标记为已处理
            target.dataset.errorHandled = 'true';
            
            // 立即设置onerror为一个简单的处理器，阻止任何后续的错误处理
            target.onerror = function() {
                this.style.display = 'none';
                this.onerror = null;
                return false;
            };
            
            // 检查当前URL是否包含默认缩略图路径
            const currentSrc = target.src || '';
            const defaultThumbnailPath = '/img/product/default_90x90.jpg';
            
            // 如果已经是默认缩略图，直接隐藏并阻止
            if (currentSrc.includes(defaultThumbnailPath)) {
                target.style.display = 'none';
                target.onerror = null; // 确保不再触发错误
                event.preventDefault();
                event.stopPropagation();
                return false;
            }
            
            // 尝试加载默认缩略图（只尝试一次）
            const defaultThumbnail = imagePrefix + defaultThumbnailPath;
            target.src = defaultThumbnail;
            
            // 阻止事件传播
            event.preventDefault();
            event.stopPropagation();
            return false;
        },
        handleLightboxImageError(event) {
            // 灯箱图片错误处理，防止无限重试
            const target = event.target;
            
            // 如果已经处理过，直接返回并阻止
            if (target.dataset.errorHandled === 'true') {
                event.preventDefault();
                event.stopPropagation();
                return false;
            }
            
            // 立即标记为已处理
            target.dataset.errorHandled = 'true';
            
            // 立即设置onerror为一个简单的处理器，阻止任何后续的错误处理
            target.onerror = function() {
                this.style.display = 'none';
                this.onerror = null;
                return false;
            };
            
            // 检查当前URL是否包含默认图片路径
            const currentSrc = target.src || '';
            const defaultImagePath = '/img/product/default.jpg';
            
            // 如果已经是默认图片，直接隐藏并阻止
            if (currentSrc.includes(defaultImagePath)) {
                target.style.display = 'none';
                target.onerror = null; // 确保不再触发错误
                event.preventDefault();
                event.stopPropagation();
                return false;
            }
            
            // 尝试加载默认图片（只尝试一次）
            const defaultImage = imagePrefix + defaultImagePath;
            target.src = defaultImage;
            
            // 阻止事件传播
            event.preventDefault();
            event.stopPropagation();
            return false;
        },
        formatPrice(price) {
            if (!price) return '0';
            return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        },
        async addToCart(event) {
            // 阻止事件冒泡和默认行为
            if (event) {
                event.preventDefault();
                event.stopPropagation();
            }
            
            // 检查登录状态，如果未登录则显示登录弹窗
            if (!window.checkLoginAndShowModal('addToCart')) {
                // 保存商品信息，登录后自动加购
                sessionStorage.setItem('pendingAddToCart', JSON.stringify({
                    productId: this.product.id,
                    productAmount: 1,
                    productPrice: this.product.price,
                    productName: this.product.name
                }));
                return;
            }
            
            if (this.addingToCart) return;
            
            try {
                this.addingToCart = true;
                this.error = '';
                
                const cartItem = {
                    productId: this.product.id,
                    productAmount: 1,
                    productPrice: this.product.price,
                    productName: this.product.name
                };
                
                const response = await axios.post('/api/shopping-cart/add', cartItem);
                const data = response.data;
                
                if (data.success) {
                    this.message = '成功添加到購物車！';
                    // 3秒后自动清除消息
                    setTimeout(() => {
                        this.message = '';
                    }, 3000);
                    
                    // 更新全局购物车数量
                    this.updateGlobalCartCount();
                } else {
                    this.error = data.message || '添加到購物車失敗';
                }
                
            } catch (error) {
                console.error('添加到購物車失敗:', error);
                if (error.response && error.response.data) {
                    this.error = error.response.data.message || '添加到購物車失敗';
                } else {
                    this.error = '网络错误，请稍后重试';
                }
            } finally {
                this.addingToCart = false;
            }
        },
        // 更新全局购物车数量
        updateGlobalCartCount() {
            // 触发全局购物车数量更新
            if (window.cartManager) {
                window.cartManager.refreshCartCount();
            }
            // 同时触发自定义事件
            window.dispatchEvent(new CustomEvent('cartUpdated'));
        },
        goBack() {
            window.location.href = '/mall';
        },
        startAutoPlay() {
            if (this.slickInstance && this.images && this.images.length > 1) {
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
        setupContentFrame() {
            this.$nextTick(() => {
                const frame = this.$refs.productContentFrame;
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
.product-detail {
    padding: 20px;
    max-width: 1200px;
    margin: 0 auto;
    overflow-x: hidden;
}

.product-title {
    color: #333;
    font-size: 2rem;
    font-weight: 600;
    margin-bottom: 15px;
}

.product-subtitle {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
}

.price-section {
    padding: 20px;
    background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.current-price {
    display: flex;
    align-items: baseline;
    gap: 5px;
}

.price-label {
    font-size: 1rem;
}

.price-symbol {
    font-size: 1.5rem;
    font-weight: 600;
    color: #FA9F42;
}

.price-amount {
    font-weight: 700;
    color: #FA9F42;
}

.market-price {
    display: flex;
    align-items: baseline;
    gap: 5px;
    font-size: 0.9rem;
}

.discount-badge {
    background: #dc3545;
    color: white;
    padding: 2px 8px;
    border-radius: 4px;
    font-size: 0.85rem;
    font-weight: 600;
}

.product-info-section {
    padding: 15px 20px;
    background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.info-row {
    display: flex;
    align-items: flex-start;
    padding: 12px 0;
    border-bottom: 1px solid #e9ecef;
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
    flex: 0 0 120px;
    font-size: 0.95rem;
    font-weight: 600;
    color: #5ACFC9;
    display: flex;
    align-items: center;
    gap: 8px;
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
}

.action-buttons {
    margin-top: 20px;
}

.action-buttons .btn {
    padding: 12px 20px;
    border-radius: 8px;
    font-size: 1rem;
    font-weight: 600;
    transition: all 0.3s ease;
}

.action-buttons .btn-primary {
    background: linear-gradient(135deg, #FA9F42 0%, #FF8C00 100%);
    border: none;
    color: white;
    box-shadow: 0 4px 12px rgba(250, 159, 66, 0.3);
}

.action-buttons .btn-primary:hover:not(:disabled) {
    background: linear-gradient(135deg, #FF8C00 0%, #FF7F00 100%);
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(250, 159, 66, 0.4);
}

.action-buttons .btn-primary:disabled {
    opacity: 0.6;
    cursor: not-allowed;
}

.action-buttons .btn-outline-secondary:hover {
    background-color: #6c757d;
    color: white;
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

.specs-table {
    max-width: 800px;
}

.specs-table .table {
    margin-bottom: 0;
}

.specs-table .spec-name {
    width: 200px;
    font-weight: 600;
    color: #5ACFC9;
    background-color: #f8f9fa;
}

.specs-table .spec-value {
    color: #333;
}

/* 使用car-detail.css中的轮播图样式 */
.product-detail .swiper-wrapper-container {
    position: relative;
    width: 100%;
    overflow: hidden;
}

.product-detail .swiper-container {
    position: relative;
    width: 100%;
    overflow: hidden;
}

.product-detail .swiper-slide .image-container {
    position: relative;
    width: 100%;
    padding-bottom: 66.67%; /* 3:2 宽高比 */
    overflow: hidden;
    background-color: #f8f9fa;
}

.product-detail .swiper-slide .swiper_image {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
    object-position: center;
    cursor: pointer;
}

.product-detail .swiper-button-prev,
.product-detail .swiper-button-next {
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

.product-detail .swiper-button-prev:hover,
.product-detail .swiper-button-next:hover {
    background: rgba(0, 0, 0, 0.7);
}

.product-detail .swiper-button-prev {
    left: 10px;
}

.product-detail .swiper-button-next {
    right: 10px;
}

.product-detail .swiper-button-prev::before {
    content: '‹';
}

.product-detail .swiper-button-next::before {
    content: '›';
}

.thumbnail-container {
    display: flex;
    gap: 10px;
    margin-top: 15px;
    overflow-x: auto;
    padding: 5px 0;
}

.thumbnail {
    width: 80px;
    height: 80px;
    object-fit: cover;
    border: 2px solid transparent;
    border-radius: 6px;
    cursor: pointer;
    transition: all 0.3s ease;
}

.thumbnail:hover {
    border-color: #5ACFC9;
    transform: scale(1.05);
}

.thumbnail.active {
    border-color: #FA9F42;
}

/* Slick dots样式 */
.product-detail .slick-dots {
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

.product-detail .slick-dots li {
    width: 12px;
    height: 12px;
}

.product-detail .slick-dots li button {
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

.product-detail .slick-dots li.slick-active button {
    background: #FA9F42;
}

.product-detail .slick-dots li button:hover {
    background: rgba(255, 255, 255, 0.8);
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

@media (max-width: 768px) {
    .product-detail {
        padding: 10px;
    }
    
    .product-title {
        font-size: 1.5rem;
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
    
    .thumbnail-container {
        gap: 8px;
    }
    
    .thumbnail {
        width: 60px;
        height: 60px;
    }
}
</style>

