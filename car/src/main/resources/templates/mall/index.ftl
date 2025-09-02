<!-- 商城頁面 -->
<div class="mall-container" id="app">
    <!-- 商品分類過濾 -->
    <div class="filter-section">
        <div class="filter-buttons">
            <button class="filter-btn" 
                    :class="{ active: selectedCategory === 'all' }"
                    @click="filterByCategory('all')">
                <i class="bi bi-grid-3x3-gap"></i>
                全部商品
            </button>
            <button class="filter-btn" 
                    :class="{ active: selectedCategory === 'engine' }"
                    @click="filterByCategory('engine')">
                <i class="bi bi-gear"></i>
                發動機配件
            </button>
            <button class="filter-btn" 
                    :class="{ active: selectedCategory === 'brake' }"
                    @click="filterByCategory('brake')">
                <i class="bi bi-disc"></i>
                制動系統
            </button>
            <button class="filter-btn" 
                    :class="{ active: selectedCategory === 'suspension' }"
                    @click="filterByCategory('suspension')">
                <i class="bi bi-arrow-up-down"></i>
                懸挂系統
            </button>
            <button class="filter-btn" 
                    :class="{ active: selectedCategory === 'electrical' }"
                    @click="filterByCategory('electrical')">
                <i class="bi bi-lightning"></i>
                電氣系統
            </button>
            <button class="filter-btn" 
                    :class="{ active: selectedCategory === 'exterior' }"
                    @click="filterByCategory('exterior')">
                <i class="bi bi-car-front"></i>
                外觀配件
            </button>
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
             :data-category="product.category || 'all'">
            <div class="product-image">
                <img :src="product.image" :alt="product.name">
                <div class="product-overlay">
                    <button class="btn btn-primary add-to-cart-btn" 
                            @click="addToCart(product)"
                            :disabled="addingToCart === product.id">
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
                    <span v-if="product.year" class="badge bg-warning me-2">{{ product.year }}年</span>
                </div>
                <div class="product-price">
                    <span class="price-symbol">¥</span>
                    <span class="price-amount">{{ formatPrice(product.price) }}</span>
                </div>
            </div>
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

.filter-buttons {
    display: flex;
    flex-wrap: wrap;
    gap: 15px;
    justify-content: center;
}

.filter-btn {
    background: white;
    border: 2px solid #e9ecef;
    border-radius: 25px;
    padding: 12px 20px;
    font-weight: 600;
    color: #666;
    transition: all 0.3s ease;
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 8px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.filter-btn:hover {
    border-color: #5ACFC9;
    color: #5ACFC9;
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(90, 207, 201, 0.2);
}

.filter-btn.active {
    background: linear-gradient(135deg, #5ACFC9 0%, #4AB8B2 100%);
    border-color: #5ACFC9;
    color: white;
    box-shadow: 0 4px 12px rgba(90, 207, 201, 0.3);
}

.filter-btn i {
    font-size: 1.1rem;
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

.product-price {
    display: flex;
    align-items: baseline;
    gap: 2px;
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
    
    .filter-buttons {
        gap: 10px;
    }
    
    .filter-btn {
        padding: 10px 15px;
        font-size: 0.9rem;
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
        totalPages: 1
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
        this.loadProducts();
    },
    methods: {
        // 加载商品数据
        async loadProducts() {
            try {
                this.loading = true;
                this.error = '';
                
                // 这里可以调用后端API获取商品数据
                // 暂时使用模拟数据
                await this.loadMockProducts();
                
            } catch (error) {
                console.error('加载商品失败:', error);
                this.error = '加载商品失败，请稍后重试';
            } finally {
                this.loading = false;
            }
        },
        
        // 加载模拟商品数据
        async loadMockProducts() {
            // 模拟API调用延迟
            await new Promise(resolve => setTimeout(resolve, 500));
            
            this.products = [
                {
                    id: 1,
                    name: '丰田凯美瑞 2.0L 豪华版',
                    description: '2019年款，行驶里程5万公里，车况良好，无重大事故',
                    price: 150000,
                    image: '/img/car/car4.jpg',
                    category: 'exterior',
                    brand: '丰田',
                    model: '凯美瑞',
                    year: 2019
                },
                {
                    id: 2,
                    name: '本田雅阁 2.4L 尊贵版',
                    description: '2020年款，行驶里程3万公里，保养记录完整',
                    price: 200000,
                    image: '/img/car/car6.jpg',
                    category: 'engine',
                    brand: '本田',
                    model: '雅阁',
                    year: 2020
                },
                {
                    id: 3,
                    name: '日产天籁 2.0L 舒适版',
                    description: '2018年款，行驶里程7万公里，车况稳定',
                    price: 120000,
                    image: '/img/car/car8.jpg',
                    category: 'suspension',
                    brand: '日产',
                    model: '天籁',
                    year: 2018
                },
                {
                    id: 4,
                    name: '大众帕萨特 1.8T 豪华版',
                    description: '2019年款，行驶里程4万公里，动力强劲',
                    price: 180000,
                    image: '/img/car/car9.jpg',
                    category: 'brake',
                    brand: '大众',
                    model: '帕萨特',
                    year: 2019
                },
                {
                    id: 5,
                    name: '别克君威 2.0T 精英版',
                    description: '2020年款，行驶里程2万公里，配置丰富',
                    price: 160000,
                    image: '/img/car/car10.jpg',
                    category: 'electrical',
                    brand: '别克',
                    model: '君威',
                    year: 2020
                },
                {
                    id: 6,
                    name: '马自达阿特兹 2.5L 运动版',
                    description: '2019年款，行驶里程6万公里，操控出色',
                    price: 140000,
                    image: '/img/car/car11.jpg',
                    category: 'suspension',
                    brand: '马自达',
                    model: '阿特兹',
                    year: 2019
                }
            ];
            
            this.filteredProducts = [...this.products];
            this.calculatePagination();
        },
        
        // 按分类过滤商品
        filterByCategory(category) {
            this.selectedCategory = category;
            this.currentPage = 1;
            
            if (category === 'all') {
                this.filteredProducts = [...this.products];
            } else {
                this.filteredProducts = this.products.filter(product => 
                    product.category === category
                );
            }
            
            this.calculatePagination();
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
        async addToCart(product) {
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
        }
    }
});
</script> 