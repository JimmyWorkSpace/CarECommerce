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
                    <div class="swiper-wrapper">
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
            <!-- 类别标签 -->
            <div v-if="product.tag" class="product-category mb-3">
                <span v-for="tag in getProductTags(product.tag)" 
                      :key="tag" 
                      class="badge bg-warning me-2 mb-2 tag-clickable"
                      @click="searchByTag(tag)">
                    {{ tag }}
                </span>
            </div>
            
            <!-- 价格 -->
            <div class="price-section mb-3">
                <div class="price-row" v-if="product.promotionalPrice">
                    <span class="price-label promotional-label">特惠價</span>
                    <span class="price-symbol promotional-symbol">${CurrencyUnit}</span>
                    <span class="price-amount promotional-amount">{{ formatPrice(product.price) }}</span>
                    <span v-if="product.originalPrice" class="price-market text-decoration-line-through text-muted ms-2">
                        售價 ${CurrencyUnit}{{ formatPrice(product.originalPrice) }}
                    </span>
                </div>
                <div class="price-row" v-if="!product.promotionalPrice">
                    <span class="price-label">售價</span>
                    <span class="price-symbol">${CurrencyUnit}</span>
                    <span class="price-amount">{{ formatPrice(product.price) }}</span>
                </div>
                <div class="market-price" v-if="product.marketPrice && product.marketPrice > product.price">
                    <span class="price-label text-muted">市價：</span>
                    <span class="price-symbol text-decoration-line-through text-muted">${CurrencyUnit}</span>
                    <span class="price-amount text-decoration-line-through text-muted">{{ formatPrice(product.marketPrice) }}</span>
                    <span class="discount-badge ms-2">省 ${CurrencyUnit}{{ formatPrice(product.marketPrice - product.price) }}</span>
                </div>
            </div>
            
            <!-- 简要描述 -->
            <div v-if="product.alias" class="product-short-description mb-3">
                <p class="text-muted mb-0" style="font-size: 14px; line-height: 1.8;">{{ product.alias }}</p>
            </div>
            
            <!-- 商品信息 -->
            <div class="product-info-section mb-3">
                <div class="info-item" v-if="product.brand">
                    <span class="info-label">
                        <i class="bi bi-award me-1"></i>品牌：
                    </span>
                    <span class="info-value">{{ product.brand }}</span>
                </div>
                <div class="info-item" v-if="product.model">
                    <span class="info-label">
                        <i class="bi bi-gear me-1"></i>型號：
                    </span>
                    <span class="info-value">{{ product.model }}</span>
                </div>
                <div class="info-item" v-if="product.amount !== null && product.amount !== undefined">
                    <span class="info-label">
                        <i class="bi bi-box me-1"></i>庫存：
                    </span>
                    <span class="info-value">{{ product.amount }} 件</span>
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
                        id="productContentFrame"
                                class="content-frame" 
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
                            <!-- 显示商品属性 -->
                            <tr v-for="attr in productAttrs" :key="attr.id" v-if="attr.attrName && attr.attrValue">
                                <td class="spec-name">{{ attr.attrName }}</td>
                                <td class="spec-value">{{ attr.attrValue }}</td>
                            </tr>
                            <!-- 如果没有属性，显示提示 -->
                            <tr v-if="!productAttrs || productAttrs.length === 0">
                                <td colspan="2" class="text-center text-muted">暫無規格信息</td>
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
const productAttrsData = ${productAttrsJson![]};
const CurrencyUnit = '${CurrencyUnit}';
const imagePrefix = '${imagePrefix}';

new Vue({
    el: '#app',
    data: {
        product: productData || {},
        images: imagesData || [],
        productAttrs: productAttrsData || [],
        currentImageIndex: 0,
        visibleLightbox: false,
        lightboxIndex: 0,
        autoPlayTimer: null,
        activeTab: { code: 'product_detail', title: '商品詳情' },
        tabs: [
            { code: 'product_detail', title: '商品詳情' },
            { code: 'product_spec', title: '商品規格' }
        ],
        addingToCart: false,
        message: '',
        error: '',
        autoPlayTimer: null,
        autoPlayInterval: 5000
    },
    mounted() {
        this.startAutoPlay();
        
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
        window.doFrameResize('productContentFrame', this.product.memo);
    },
    beforeDestroy() {
        this.stopAutoPlay();
    },
    methods: {
        changeImage(index) {
            this.currentImageIndex = index;
        },
        prevImage() {
            if (this.images && this.images.length > 0) {
                this.currentImageIndex = this.currentImageIndex > 0 ? this.currentImageIndex - 1 : this.images.length - 1;
            }
        },
        nextImage() {
            if (this.images && this.images.length > 0) {
                this.currentImageIndex = this.currentImageIndex < this.images.length - 1 ? this.currentImageIndex + 1 : 0;
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
            // 如果URL已经是完整URL，直接使用原图作为缩略图
            // 或者可以根据实际需求生成缩略图URL
            // 这里暂时直接返回原图URL，因为数据库中的imageUrl可能已经是完整路径
            return url;
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
            if (this.images && this.images.length > 1) {
                this.autoPlayTimer = setInterval(() => {
                    this.nextImage();
                }, this.autoPlayInterval);
            }
        },
        stopAutoPlay() {
            if (this.autoPlayTimer) {
                clearInterval(this.autoPlayTimer);
                this.autoPlayTimer = null;
            }
        },
        // 解析商品标签（按逗号分隔）
        getProductTags(tags) {
            if (!tags) return [];
            return tags.split(',').map(tag => tag.trim()).filter(tag => tag.length > 0);
        },
        // 根据标签搜索商品
        searchByTag(tag) {
            // 跳转到商城页面并搜索标签
            window.location.href = '/mall?tag=' + encodeURIComponent(tag);
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
    color: #3351A5;
    font-size: 2rem;
    font-weight: 600;
    margin-bottom: 15px;
}

.product-subtitle {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
}

/* 商品类别样式 */
.product-category {
    text-align: left;
}

.product-category .badge {
    font-size: 0.9rem;
    padding: 6px 12px;
    font-weight: 500;
    background-color: #668CFA !important;
    color: white;
}

.tag-clickable {
    cursor: pointer;
    transition: all 0.2s ease;
}

.tag-clickable:hover {
    background-color: #5578E0 !important;
    transform: translateY(-1px);
    box-shadow: 0 2px 4px rgba(102, 140, 250, 0.3);
}

/* 价格区域样式 - 去掉块样式 */
.price-section {
    text-align: left;
    padding: 0;
    background: transparent;
    border-radius: 0;
    box-shadow: none;
}

.current-price {
    display: flex;
    align-items: baseline;
    gap: 5px;
    margin-bottom: 8px;
}

.price-label {
    font-size: 1rem;
    color: #666;
}

.price-symbol {
    font-size: 1.8rem;
    font-weight: 600;
    color: #3351A5;
}

.price-amount {
    font-weight: 700;
    color: #3351A5;
    font-size: 1.8rem;
}

.promotional-label {
    color: #3351A5;
    font-size: 1.3rem;
}

.promotional-symbol {
    color: #3351A5;
    font-size: 2.2rem;
    font-weight: 600;
}

.promotional-amount {
    color: #3351A5;
    font-size: 2.2rem;
    font-weight: 700;
}

.price-row {
    display: flex;
    align-items: baseline;
    gap: 5px;
    margin-bottom: 8px;
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

/* 简要描述样式 */
.product-short-description {
    text-align: left;
}

/* 商品信息区域样式 - 去掉块样式 */
.product-info-section {
    text-align: left;
    padding: 0;
    background: transparent;
    border-radius: 0;
    box-shadow: none;
}

.info-item {
    display: flex;
    align-items: center;
    padding: 8px 0;
    font-size: 0.95rem;
}

.info-label {
    font-weight: 600;
    color: #5ACFC9;
    margin-right: 8px;
    display: inline-flex;
    align-items: center;
}

.info-label i {
    font-size: 1rem;
}

.info-value {
    color: #333;
    font-weight: 400;
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
    background-color: #3551A5;
    border: none;
    color: white;
    box-shadow: 0 4px 12px rgba(53, 81, 165, 0.3);
}

.action-buttons .btn-primary:hover:not(:disabled) {
    background-color: #2A4080;
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(53, 81, 165, 0.4);
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

.specs-table .table td {
    padding: 12px 15px;
}

.specs-table .spec-name {
    width: 200px;
    font-weight: 600;
    color: #5ACFC9;
    background-color: #f8f9fa;
}

.specs-table .spec-value {
    color: #333;
    background-color: #ffffff;
}

/* 使用car-detail.css中的轮播图样式 */
.product-detail .swiper-wrapper-container {
    position: relative;
    width: 100%;
    overflow: hidden;
    margin-bottom: 15px;
}

.product-detail .swiper-container {
    position: relative;
    width: 100%;
    overflow: hidden;
}

.product-detail .swiper-wrapper {
    display: flex;
    transition: transform 0.3s ease;
}

.product-detail .swiper-slide {
    min-width: 100%;
    flex-shrink: 0;
    display: none;
}

.product-detail .swiper-slide.active {
    display: block;
}

.product-detail .swiper-slide .image-container {
    position: relative;
    width: 100%;
    padding-bottom: 66.67%; /* 3:2 宽高比 (2/3 * 100%) */
    overflow: hidden;
    background-color: #f8f9fa;
}

.product-detail .swiper-slide img {
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
    max-width: 100%;
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

