<!-- 商城頁面 -->
<div class="mall-container" id="app">
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
                    <div class="product-overlay">
                        <button class="btn btn-primary add-to-cart-btn" 
                                @click.stop.prevent="addToCart(product, $event)"
                                :disabled="addingToCart === product.id"
                                type="button">
                            <i v-if="addingToCart !== product.id" class="bi bi-cart-plus"></i>
                            <span v-else class="spinner-border spinner-border-sm me-2" role="status"></span>
                            {{ addingToCart === product.id ? '添加中...' : '加入購物車' }}
                        </button>
                    </div>
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
                            <span v-if="product.promotionalPrice" class="price-amount promotional-amount">{{ formatPrice(product.promotionalPrice) }}</span>
                            <span v-if="product.promotionalPrice" class="price-market text-decoration-line-through text-muted ms-2">
                                售價 ${CurrencyUnit}{{ formatPrice(product.price) }}
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

<style>
.mall-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
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
    background: white;
    border: 2px solid #e9ecef;
    border-radius: 20px;
    padding: 6px 14px;
    font-weight: 500;
    color: #666;
    transition: all 0.3s ease;
    cursor: pointer;
    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
    font-size: 0.85rem;
}

.category-btn:hover {
    border-color: #5ACFC9;
    color: #5ACFC9;
    transform: translateY(-1px);
    box-shadow: 0 2px 6px rgba(90, 207, 201, 0.2);
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
    background: #f8f9fa;
    border-radius: 18px;
}

.category-btn-second:hover {
    background: white;
}

.category-btn-second.active {
    background: linear-gradient(135deg, #4AB8B2 0%, #3AA7A1 100%);
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

.product-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.7);
    display: flex;
    align-items: center;
    justify-content: center;
    opacity: 0;
    transition: opacity 0.3s ease;
}

.product-card:hover .product-overlay {
    opacity: 1;
}

.add-to-cart-btn {
    background: linear-gradient(135deg, #5ACFC9 0%, #4AB8B2 100%);
    border: none;
    padding: 12px 24px;
    border-radius: 25px;
    font-weight: 600;
    transition: all 0.3s ease;
    box-shadow: 0 4px 12px rgba(90, 207, 201, 0.3);
}

.add-to-cart-btn:hover:not(:disabled) {
    background: linear-gradient(135deg, #4AB8B2 0%, #3AA7A1 100%);
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(90, 207, 201, 0.4);
}

.add-to-cart-btn:disabled {
    opacity: 0.7;
    cursor: not-allowed;
}

.add-to-cart-btn i {
    margin-right: 8px;
}

.product-info {
    padding: 20px;
}

.product-name {
    color: #333;
    font-weight: 600;
    margin-bottom: 8px;
    font-size: 1.1rem;
}

.product-description {
    color: #666;
    font-size: 0.9rem;
    margin-bottom: 15px;
    line-height: 1.4;
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
}

.tag-clickable {
    cursor: pointer;
    transition: all 0.2s ease;
}

.tag-clickable:hover {
    background-color: #4AB8B2 !important;
    transform: translateY(-1px);
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
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
    color: #5ACFC9;
    font-size: 1rem;
    font-weight: 600;
}

.price-amount {
    color: #5ACFC9;
    font-size: 1.5rem;
    font-weight: 700;
}

.promotional-label {
    color: #ff6b6b;
    font-size: 1.3rem;
}

.promotional-symbol {
    color: #ff6b6b;
    font-size: 2.2rem;
    font-weight: 600;
}

.promotional-amount {
    color: #ff6b6b;
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
        addingToCart: null,
        currentPage: 1,
        pageSize: 12,
        totalPages: 1,
        // 分类相关
        categoryTree: [],
        firstLevelCategories: [],
        secondLevelCategories: [],
        selectedFirstCategory: '',
        selectedSecondCategory: ''
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
        
        // 检查URL参数中是否有标签搜索
        const urlParams = new URLSearchParams(window.location.search);
        const tagParam = urlParams.get('tag');
        if (tagParam) {
            // 如果有标签参数，延迟执行标签搜索
            setTimeout(() => {
                this.searchByTag(decodeURIComponent(tagParam));
            }, 500);
        }
        
        // 监听登录后待执行的操作
        const self = this;
        window.addEventListener('pendingAction', function(event) {
            if (event.detail && event.detail.action === 'addToCart') {
                // 登录后自动执行加购操作
                const pendingCartItem = sessionStorage.getItem('pendingAddToCart');
                if (pendingCartItem) {
                    try {
                        const cartItem = JSON.parse(pendingCartItem);
                        sessionStorage.removeItem('pendingAddToCart');
                        // 延迟执行，确保页面已完全加载
                        setTimeout(() => {
                            const product = self.products.find(p => p.id === cartItem.productId);
                            if (product) {
                                self.addToCart(product);
                            }
                        }, 300);
                    } catch (e) {
                        console.error('解析待加购商品信息失败:', e);
                    }
                }
            }
        });
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
                    console.error('加载分类树失败:', response.data.message);
                }
            } catch (error) {
                console.error('加载分类树失败:', error);
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
                    this.error = response.data.message || '加载商品失败';
                }
                
            } catch (error) {
                console.error('加载商品失败:', error);
                this.error = '加载商品失败，请稍后重试';
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
                console.error('过滤商品失败:', error);
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
                    this.message = '已找到 ' + this.filteredProducts.length + ' 个包含标签"' + tag + '"的商品';
                    // 3秒后自动清除消息
                    setTimeout(() => {
                        this.message = '';
                    }, 3000);
                } else {
                    this.error = response.data.message || '搜索失败';
                    this.filteredProducts = [];
                }
                
                this.calculatePagination();
                
            } catch (error) {
                console.error('根据标签搜索商品失败:', error);
                this.error = '搜索失败，请稍后重试';
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
        
        // 添加到购物车
        async addToCart(product, event) {
            // 阻止事件冒泡和默认行为，防止跳转到详情页
            if (event) {
                event.preventDefault();
                event.stopPropagation();
            }
            
            // 检查登录状态，如果未登录则显示登录弹窗
            if (!window.checkLoginAndShowModal('addToCart')) {
                // 保存商品信息，登录后自动加购
                sessionStorage.setItem('pendingAddToCart', JSON.stringify({
                    productId: product.id,
                    productAmount: 1,
                    productPrice: product.price,
                    productName: product.name
                }));
                return;
            }
            
            try {
                this.addingToCart = product.id;
                
                const cartItem = {
                    productId: product.id,
                    productAmount: 1,
                    productPrice: product.price,
                    productName: product.name
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
                this.addingToCart = null;
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
        }
    }
});
</script> 