<!-- 引入 Swiper CSS -->
<link href="/assets/swiper/css/swiper.min.css" rel="stylesheet">
<script src="/assets/swiper/js/swiper.min.js"></script>
<link href="/css/home.css" rel="stylesheet">

<!-- 商城頁面 -->
<div class="mall-page" id="app">
    <!-- 第一行：輪播圖 -->
    <div class="hero-section">
        <div class="swiper hero-swiper">
            <div class="swiper-wrapper">
                <!-- 使用 Vue.js 動態展示輪播圖數據 -->
                <div class="swiper-slide" v-for="(banner, index) in banners" :key="banner.id">
                    <!-- 連結類型輪播圖 -->
                    <a v-if="banner.isLink && banner.linkUrl" :href="banner.linkUrl" class="hero-slide-link">
                        <img :src="banner.imageUrl" :alt="banner.title || '輪播圖' + (index + 1)" class="hero-image">
                        <div class="hero-content" v-if="banner.title">
                            <h2 class="hero-title" v-text="banner.title"></h2>
                            <p class="hero-subtitle" v-if="banner.subtitle" v-text="banner.subtitle"></p>
                            <span class="hero-btn">了解更多</span>
                        </div>
                    </a>
                    <!-- 非連結類型輪播圖 -->
                    <template v-else>
                        <img :src="banner.imageUrl" :alt="banner.title || '輪播圖' + (index + 1)" class="hero-image">
                        <div class="hero-content" v-if="banner.title">
                            <h2 class="hero-title" v-text="banner.title"></h2>
                            <p class="hero-subtitle" v-if="banner.subtitle" v-text="banner.subtitle"></p>
                        </div>
                    </template>
                </div>
                
                <!-- 當沒有輪播圖數據時顯示默認內容 -->
                <div class="swiper-slide" v-if="!banners || banners.length === 0">
                    <img src="/img/default-hero.jpg" alt="默認輪播圖" class="hero-image">
                    <div class="hero-content">
                        <h2 class="hero-title">歡迎來到線上購物</h2>
                        <p class="hero-subtitle">發現更多優質商品</p>
                    </div>
                </div>
            </div>
            <!-- 分页器 -->
            <div class="swiper-pagination"></div>
        </div>
    </div>

    <!-- 商品區域 -->
    <div class="mall-container">
    <!-- 精选商品区块 -->
    <div v-if="recommendedProducts && recommendedProducts.length > 0" class="recommended-section">
        <div class="section-header">
            <h3 class="section-title">
                精選商品
            </h3>
        </div>
        <div class="products-grid">
            <div v-for="product in recommendedProducts" 
                 :key="product.id" 
                 class="product-card" 
                 :data-category="product.category || 'other'">
                <a :href="'/product/' + product.id" class="product-card-link">
                    <div class="product-image">
                        <img :src="product.image" :alt="product.name" @error="handleImageError">
                    </div>
                    <div class="product-info">
                        <h5 class="product-name">{{ product.name }}</h5>
                        <p class="product-description">{{ product.description }}</p>
                        <div class="product-meta">
                            <span v-if="product.brand" class="badge bg-secondary me-2">{{ product.brand }}</span>
                            <span v-if="product.model" class="badge bg-info me-2">{{ product.model }}</span>
                            <span v-if="product.source" class="badge bg-warning me-2">{{ product.source }}</span>
                        </div>
                        <div class="product-tags" v-if="product.productTags">
                            <span v-for="tag in getProductTags(product.productTags)" 
                                  :key="tag" 
                                  class="badge bg-primary me-1 mb-1 tag-clickable"
                                  @click.stop.prevent="searchByTag(tag, $event)">
                                {{ tag }}
                            </span>
                        </div>
                        <div class="product-price">
                            <div class="price-row">
                                <span v-if="product.promotionalPrice" class="price-label promotional-label">特惠價</span>
                                <span v-if="product.promotionalPrice" class="price-symbol promotional-symbol">${CurrencyUnit}</span>
                                <span v-if="product.promotionalPrice" class="price-amount promotional-amount">{{ formatPrice(product.price) }}</span>
                                <span v-if="product.originalPrice" class="price-market text-decoration-line-through text-muted ms-2">
                                    售價 ${CurrencyUnit}{{ formatPrice(product.originalPrice) }}
                                </span>
                                <span v-if="!product.promotionalPrice" class="price-label">售價</span>
                                <span v-if="!product.promotionalPrice" class="price-symbol">${CurrencyUnit}</span>
                                <span v-if="!product.promotionalPrice" class="price-amount">{{ formatPrice(product.price) }}</span>
                            </div>
                        </div>
                    </div>
                </a>
            </div>
        </div>
    </div>
    
    <!-- 商品分類過濾 -->
    <div class="filter-section">
        <!-- 一級分類 -->
        <div class="category-level-first">
            <div class="category-buttons">
                <button class="category-btn" 
                        :class="{ active: !selectedFirstCategory }"
                        @click="selectFirstCategory(null)">
                    全部
                </button>
                <button v-for="category in firstLevelCategories" 
                        :key="category.id"
                        class="category-btn" 
                        :class="{ active: selectedFirstCategory == category.id }"
                        @click="selectFirstCategory(category.id)">
                    {{ category.name }}
                </button>
            </div>
        </div>
        
        <!-- 二級分類 -->
        <div class="category-level-second" v-if="selectedFirstCategory && secondLevelCategories.length > 0">
            <div class="category-buttons">
                <button class="category-btn category-btn-second" 
                        :class="{ active: !selectedSecondCategory }"
                        @click="selectSecondCategory(null)">
                    全部
                </button>
                <button v-for="category in secondLevelCategories" 
                        :key="category.id"
                        class="category-btn category-btn-second" 
                        :class="{ active: selectedSecondCategory == category.id }"
                        @click="selectSecondCategory(category.id)">
                    {{ category.name }}
                </button>
            </div>
        </div>
    </div>
    
    <!-- 加载状态 -->
    <div v-if="loading" class="text-center py-5">
        <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">載入中...</span>
        </div>
        <p class="mt-3 text-muted">正在載入商品...</p>
    </div>
    
    <!-- 错误提示 -->
    <div v-if="error" class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="bi bi-exclamation-triangle me-2"></i>
        {{ error }}
        <button type="button" class="btn-close" @click="error = ''"></button>
    </div>
    
    <!-- 成功提示 -->
    <div v-if="message" class="alert alert-success alert-dismissible fade show" role="alert">
        <i class="bi bi-check-circle me-2"></i>
        {{ message }}
        <button type="button" class="btn-close" @click="message = ''"></button>
    </div>
    
    <!-- 商品網格 -->
    <div v-if="!loading" class="products-grid">
        <div v-for="product in filteredProducts" 
             :key="product.id" 
             class="product-card" 
             :data-category="product.category || 'other'">
            <a :href="'/product/' + product.id" class="product-card-link">
                <div class="product-image">
                    <img :src="product.image" :alt="product.name" @error="handleImageError">
                </div>
                <div class="product-info">
                    <h5 class="product-name">{{ product.name }}</h5>
                    <p class="product-description">{{ product.description }}</p>
                    <div class="product-meta">
                        <span v-if="product.brand" class="badge bg-secondary me-2">{{ product.brand }}</span>
                        <span v-if="product.model" class="badge bg-info me-2">{{ product.model }}</span>
                        <span v-if="product.source" class="badge bg-warning me-2">{{ product.source }}</span>
                    </div>
                    <div class="product-tags" v-if="product.productTags">
                        <span v-for="tag in getProductTags(product.productTags)" 
                              :key="tag" 
                              class="badge bg-primary me-1 mb-1 tag-clickable"
                              @click.stop.prevent="searchByTag(tag, $event)">
                            {{ tag }}
                        </span>
                    </div>
                    <div class="product-price">
                        <div class="price-row">
                            <span v-if="product.promotionalPrice" class="price-label promotional-label">特惠價</span>
                            <span v-if="product.promotionalPrice" class="price-symbol promotional-symbol">${CurrencyUnit}</span>
                            <span v-if="product.promotionalPrice" class="price-amount promotional-amount">{{ formatPrice(product.price) }}</span>
                            <span v-if="product.originalPrice" class="price-market text-decoration-line-through text-muted ms-2">
                                售價 ${CurrencyUnit}{{ formatPrice(product.originalPrice) }}
                            </span>
                            <span v-if="!product.promotionalPrice" class="price-label">售價</span>
                            <span v-if="!product.promotionalPrice" class="price-symbol">${CurrencyUnit}</span>
                            <span v-if="!product.promotionalPrice" class="price-amount">{{ formatPrice(product.price) }}</span>
                        </div>
                    </div>
                </div>
            </a>
        </div>
    </div>
    
    <!-- 空状态 -->
    <div v-if="!loading && filteredProducts.length === 0" class="text-center py-5">
        <div class="empty-state">
            <i class="bi bi-search display-1 text-muted"></i>
            <h4 class="mt-3 text-muted">沒有找到相關商品</h4>
            <p class="text-muted">請嘗試其他分類或關鍵詞</p>
        </div>
    </div>
    
    <!-- 分頁 -->
    <div v-if="!loading && totalPages > 1" class="pagination-container">
        <nav aria-label="商品分頁">
            <ul class="pagination justify-content-center">
                <li class="page-item" :class="{ disabled: currentPage === 1 }">
                    <a class="page-link" href="#" @click.prevent="changePage(currentPage - 1)">上一頁</a>
                </li>
                <li v-for="page in visiblePages" 
                    :key="page" 
                    class="page-item" 
                    :class="{ active: page === currentPage }">
                    <a class="page-link" href="#" @click.prevent="changePage(page)">{{ page }}</a>
                </li>
                <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                    <a class="page-link" href="#" @click.prevent="changePage(currentPage + 1)">下一頁</a>
                </li>
            </ul>
        </nav>
    </div>
    </div>
</div>

<style>
/* 商城页面整体布局 */
.mall-page {
    font-family: 'Microsoft YaHei', Arial, sans-serif;
}

.mall-container {
    width: 100%;
    max-width: 100%;
    margin: 0;
    padding: 20px;
}

/* 轮播图样式覆盖 - 去掉底部margin */
.hero-section {
    margin-bottom: 0 !important;
}

/* 精选商品区块样式 */
.recommended-section {
    margin-bottom: 40px;
    padding: 30px 0;
}

.section-header {
    margin-bottom: 25px;
    text-align: center;
}

.section-title {
    color: #3351A5;
    font-weight: 700;
    font-size: 1.8rem;
    margin: 0;
}

.filter-section {
    margin-bottom: 30px;
}

.category-level-first {
    margin-bottom: 20px;
}

.category-level-second {
    margin-top: 15px;
    padding-top: 15px;
    border-top: 2px solid #e9ecef;
}

.category-buttons {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    justify-content: flex-start;
    align-items: center;
}

.category-btn {
    background: #6BE5DA;
    border: 2px solid #6BE5DA;
    border-radius: 20px;
    padding: 6px 14px;
    font-weight: 500;
    color: white;
    transition: all 0.3s ease;
    cursor: pointer;
    box-shadow: 0 1px 3px rgba(107, 229, 218, 0.2);
    font-size: 0.85rem;
}

.category-btn:hover {
    border-color: #5ACFC9;
    background-color: #5ACFC9;
    color: white;
    transform: translateY(-1px);
    box-shadow: 0 2px 6px rgba(107, 229, 218, 0.3);
}

.category-btn.active {
    background: linear-gradient(135deg, #5ACFC9 0%, #4AB8B2 100%);
    border-color: #5ACFC9;
    color: white;
    box-shadow: 0 2px 8px rgba(90, 207, 201, 0.3);
}

.category-btn-second {
    font-size: 0.8rem;
    padding: 5px 12px;
    background: #6BE5DA;
    border-radius: 18px;
    color: white;
}

.category-btn-second:hover {
    background: #5ACFC9;
    color: white;
}

.category-btn-second.active {
    background: linear-gradient(135deg, #5ACFC9 0%, #4AB8B2 100%);
    color: white;
}

.products-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
    gap: 30px;
    margin-bottom: 40px;
}

.product-card {
    background: white;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    overflow: hidden;
    transition: all 0.3s ease;
    position: relative;
}

.product-card-link {
    text-decoration: none;
    color: inherit;
    display: block;
    height: 100%;
}

.product-card-link:hover {
    text-decoration: none;
    color: inherit;
}

.product-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.product-image {
    position: relative;
    height: 200px;
    overflow: hidden;
}

.product-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s ease;
}

.product-card:hover .product-image img {
    transform: scale(1.05);
}


.product-info {
    padding: 20px;
    text-align: left;
}

.product-name {
    color: #3351A5;
    font-weight: 600;
    margin-bottom: 8px;
    font-size: 1.1rem;
    text-align: left;
}

.product-description {
    color: #666;
    font-size: 0.9rem;
    margin-bottom: 15px;
    line-height: 1.4;
    text-align: left;
}

.product-meta {
    margin-bottom: 15px;
}

.product-meta .badge {
    font-size: 0.75rem;
}

.product-tags {
    margin-bottom: 15px;
    display: flex;
    flex-wrap: wrap;
    gap: 5px;
}

.product-tags .badge {
    font-size: 0.75rem;
    padding: 5px 10px;
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

.product-price {
    display: flex;
    flex-direction: column;
    gap: 5px;
}

.price-row {
    display: flex;
    align-items: baseline;
    gap: 2px;
}

.price-label {
    color: #666;
    font-size: 0.85rem;
    margin-right: 5px;
}

.price-symbol {
    color: #3351A5;
    font-size: 1rem;
    font-weight: 600;
}

.price-amount {
    color: #3351A5;
    font-size: 1.5rem;
    font-weight: 700;
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

.price-market {
    color: #999;
    font-size: 0.9rem;
    font-weight: 400;
}

.market-price {
    text-decoration: line-through;
    color: #999;
    font-size: 0.9rem;
    margin-left: 10px;
    font-weight: 400;
}

.pagination-container {
    margin-top: 40px;
}

.pagination .page-link {
    color: #5ACFC9;
    border-color: #5ACFC9;
}

.pagination .page-item.active .page-link {
    background-color: #5ACFC9;
    border-color: #5ACFC9;
}

.pagination .page-link:hover {
    color: #4AB8B2;
    border-color: #4AB8B2;
}

.empty-state {
    padding: 2rem;
}

.empty-state i {
    color: #ccc;
}

/* 动画效果 */
@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

/* 响应式设计 */
@media (max-width: 768px) {
    .mall-container {
        padding: 15px;
    }
    
    .category-buttons {
        gap: 6px;
    }
    
    .category-btn {
        padding: 5px 12px;
        font-size: 0.8rem;
    }
    
    .category-btn-second {
        padding: 4px 10px;
        font-size: 0.75rem;
    }
    
    .products-grid {
        grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
        gap: 20px;
    }
    
    .product-info {
        padding: 15px;
    }
    
    .product-name {
        font-size: 1rem;
    }
    
    .price-amount {
        font-size: 1.3rem;
    }
}
</style>

<script>
new Vue({
    el: '#app',
    data: {
        products: [],
        filteredProducts: [],
        selectedCategory: 'all',
        loading: true,
        error: '',
        message: '',
        currentPage: 1,
        pageSize: 12,
        totalPages: 1,
        // 分类相关
        categoryTree: [],
        firstLevelCategories: [],
        secondLevelCategories: [],
        selectedFirstCategory: '',
        selectedSecondCategory: '',
        // 轮播图相关
        banners: [],
        heroSwiper: null,
        // 推荐商品
        recommendedProducts: []
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
        }
    },
    mounted() {
        this.loadCategoryTree();
        this.loadProducts();
        this.getBanner();
        this.loadRecommendedProducts();
        
        // 检查URL参数中是否有标签搜索
        const urlParams = new URLSearchParams(window.location.search);
        const tagParam = urlParams.get('tag');
        if (tagParam) {
            // 如果有标签参数，延迟执行标签搜索
            setTimeout(() => {
                this.searchByTag(decodeURIComponent(tagParam));
            }, 500);
        }
        
        // 列表页已移除加购功能，不再监听登录后自动加购
    },
    methods: {
        // 加载分类树
        async loadCategoryTree() {
            try {
                const response = await axios.get('/api/products/categories/tree');
                if (response.data.success) {
                    this.categoryTree = response.data.data || [];
                    this.firstLevelCategories = this.categoryTree;
                } else {
                    console.error('載入分類樹失敗:', response.data.message);
                }
            } catch (error) {
                console.error('載入分類樹失敗:', error);
            }
        },
        
        // 选择一级分类
        selectFirstCategory(categoryId) {
            this.selectedFirstCategory = categoryId;
            this.selectedSecondCategory = '';
            this.currentPage = 1;
            
            if (categoryId) {
                // 找到选中的一级分类，获取其二级分类
                const selectedCategory = this.firstLevelCategories.find(c => c.id == categoryId);
                if (selectedCategory && selectedCategory.children) {
                    this.secondLevelCategories = selectedCategory.children;
                } else {
                    this.secondLevelCategories = [];
                }
            } else {
                this.secondLevelCategories = [];
            }
            
            // 执行筛选
            this.filterByCategory();
        },
        
        // 选择二级分类
        selectSecondCategory(categoryId) {
            this.selectedSecondCategory = categoryId;
            this.currentPage = 1;
            this.filterByCategory();
        },
        
        // 加载商品数据
        async loadProducts() {
            try {
                this.loading = true;
                this.error = '';
                
                // 调用后端API获取商品数据
                const response = await axios.get('/api/products/list');
                
                if (response.data.success) {
                    this.products = response.data.data || [];
                    this.filteredProducts = [...this.products];
                    this.calculatePagination();
                } else {
                    this.error = response.data.message || '載入商品失敗';
                }
                
            } catch (error) {
                console.error('載入商品失敗:', error);
                this.error = '載入商品失敗，請稍後重試';
            } finally {
                this.loading = false;
            }
        },
        
        // 按分类过滤商品
        async filterByCategory() {
            try {
                this.loading = true;
                this.currentPage = 1;
                
                let categoryId = null;
                if (this.selectedSecondCategory) {
                    // 如果选择了二级分类，使用二级分类ID
                    categoryId = this.selectedSecondCategory;
                } else if (this.selectedFirstCategory) {
                    // 如果只选择了一级分类，使用一级分类ID（后端会返回该一级分类及其所有二级分类的商品）
                    categoryId = this.selectedFirstCategory;
                }
                
                if (categoryId) {
                    // 调用API获取分类商品
                    const response = await axios.get('/api/products/category?categoryId=' + categoryId);
                    if (response.data.success) {
                        this.filteredProducts = response.data.data || [];
                    } else {
                        // 如果API调用失败，使用本地过滤
                        this.filteredProducts = this.products.filter(product => {
                            if (this.selectedSecondCategory) {
                                return product.categoryId == this.selectedSecondCategory;
                            } else if (this.selectedFirstCategory) {
                                // 一级分类：需要匹配一级分类或其子分类
                                const productCategory = this.findCategoryById(product.categoryId);
                                if (productCategory) {
                                    return productCategory.id == this.selectedFirstCategory || 
                                           productCategory.parentId == this.selectedFirstCategory;
                                }
                                return false;
                            }
                            return true;
                        });
                    }
                } else {
                    // 没有选择分类，显示全部
                    this.filteredProducts = [...this.products];
                }
                
                this.calculatePagination();
                
            } catch (error) {
                console.error('過濾商品失敗:', error);
                // 使用本地过滤作为备选方案
                if (!this.selectedFirstCategory && !this.selectedSecondCategory) {
                    this.filteredProducts = [...this.products];
                } else {
                    this.filteredProducts = this.products.filter(product => {
                        if (this.selectedSecondCategory) {
                            return product.categoryId == this.selectedSecondCategory;
                        } else if (this.selectedFirstCategory) {
                            // 一级分类：需要匹配一级分类或其子分类
                            const productCategory = this.findCategoryById(product.categoryId);
                            if (productCategory) {
                                return productCategory.id == this.selectedFirstCategory || 
                                       productCategory.parentId == this.selectedFirstCategory;
                            }
                            return false;
                        }
                        return true;
                    });
                }
                this.calculatePagination();
            } finally {
                this.loading = false;
            }
        },
        
        // 根据ID查找分类（返回分类对象和其父分类ID）
        findCategoryById(categoryId) {
            if (!categoryId) return null;
            for (let firstLevel of this.firstLevelCategories) {
                if (firstLevel.id == categoryId) {
                    return { id: firstLevel.id, parentId: null };
                }
                if (firstLevel.children) {
                    for (let secondLevel of firstLevel.children) {
                        if (secondLevel.id == categoryId) {
                            return { id: secondLevel.id, parentId: firstLevel.id };
                        }
                    }
                }
            }
            return null;
        },
        
        // 解析商品标签（按逗号分隔）
        getProductTags(tags) {
            if (!tags) return [];
            return tags.split(',').map(tag => tag.trim()).filter(tag => tag.length > 0);
        },
        
        // 根据标签搜索商品
        async searchByTag(tag, event) {
            // 阻止事件冒泡和默认行为，防止跳转到详情页
            if (event) {
                event.preventDefault();
                event.stopPropagation();
            }
            
            try {
                this.loading = true;
                this.currentPage = 1;
                this.error = '';
                
                // 清除分类选择
                this.selectedFirstCategory = '';
                this.selectedSecondCategory = '';
                this.secondLevelCategories = [];
                
                // 调用API获取标签商品
                const response = await axios.get('/api/products/tag?tag=' + encodeURIComponent(tag));
                
                if (response.data.success) {
                    this.filteredProducts = response.data.data || [];
                    this.message = '已找到 ' + this.filteredProducts.length + ' 個包含標籤"' + tag + '"的商品';
                    // 3秒后自动清除消息
                    setTimeout(() => {
                        this.message = '';
                    }, 3000);
                } else {
                    this.error = response.data.message || '搜索失敗';
                    this.filteredProducts = [];
                }
                
                this.calculatePagination();
                
            } catch (error) {
                console.error('根據標籤搜索商品失敗:', error);
                this.error = '搜索失敗，請稍後重試';
                this.filteredProducts = [];
                this.calculatePagination();
            } finally {
                this.loading = false;
            }
        },
        
        // 计算分页
        calculatePagination() {
            this.totalPages = Math.ceil(this.filteredProducts.length / this.pageSize);
            this.currentPage = Math.min(this.currentPage, this.totalPages);
        },
        
        // 切换页面
        changePage(page) {
            if (page >= 1 && page <= this.totalPages) {
                this.currentPage = page;
            }
        },
        
        // 格式化价格
        formatPrice(price) {
            if (!price) return '0';
            return parseFloat(price).toLocaleString('zh-CN', {
                minimumFractionDigits: 0,
                maximumFractionDigits: 0
            });
        },
        
        // 处理图片加载失败
        handleImageError(event) {
            // 设置默认图片
            event.target.src = '/img/car/car6.jpg';
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
        
        // 获取推荐商品
        async loadRecommendedProducts() {
            try {
                const response = await axios.get('/api/products/recommended');
                if (response.data.success) {
                    this.recommendedProducts = response.data.data || [];
                } else {
                    console.error('載入推薦商品失敗:', response.data.message);
                    this.recommendedProducts = [];
                }
            } catch (error) {
                console.error('載入推薦商品失敗:', error);
                this.recommendedProducts = [];
            }
        },
        
        // 获取轮播图数据
        getBanner() {
            let _this = this;
            $.ajax({
                url: '/api/banner/list',
                method: 'GET',
                success: (data) => {
                    _this.banners = data.data;
                    // 数据加载完成后重新初始化 Swiper
                    _this.$nextTick(() => {
                        _this.initHeroSlider();
                    });
                },
                error: (error) => {
                    console.log(error);
                    // 加载失败时也初始化 Swiper，顯示默認内容
                    _this.$nextTick(() => {
                        _this.initHeroSlider();
                    });
                }
            });
        },
        
        // 初始化轮播图
        initHeroSlider() {
            // 銷毀之前的 Swiper 實例（如果存在）
            if (this.heroSwiper) {
                this.heroSwiper.destroy(true, true);
            }
            
            // 初始化Swiper輪播圖
            this.heroSwiper = new Swiper('.hero-swiper', {
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
        }
    }
});
</script> 