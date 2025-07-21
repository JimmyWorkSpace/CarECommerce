<!-- 商城页面 -->
<div class="mall-container">
    <!-- 商品分类过滤 -->
    <div class="filter-section">
        <div class="filter-buttons">
            <button class="filter-btn active" data-category="all">
                <i class="bi bi-grid-3x3-gap"></i>
                全部商品
            </button>
            <button class="filter-btn" data-category="engine">
                <i class="bi bi-gear"></i>
                发动机配件
            </button>
            <button class="filter-btn" data-category="brake">
                <i class="bi bi-disc"></i>
                制动系统
            </button>
            <button class="filter-btn" data-category="suspension">
                <i class="bi bi-arrow-up-down"></i>
                悬挂系统
            </button>
            <button class="filter-btn" data-category="electrical">
                <i class="bi bi-lightning"></i>
                电气系统
            </button>
            <button class="filter-btn" data-category="exterior">
                <i class="bi bi-car-front"></i>
                外观配件
            </button>
        </div>
    </div>
    
    <!-- 商品网格 -->
    <div class="products-grid" id="productsGrid">
        <#list products as product>
        <div class="product-card" data-category="${product.category!'all'}">
            <div class="product-image">
                <img src="${product.image}" alt="${product.name}">
                <div class="product-overlay">
                    <button class="btn btn-primary add-to-cart-btn" 
                            onclick="addToCart({
                                id: '${product.id}',
                                name: '${product.name}',
                                price: ${product.price},
                                image: '${product.image}'
                            })">
                        <i class="bi bi-cart-plus"></i>
                        加入购物车
                    </button>
                </div>
            </div>
            <div class="product-info">
                <h5 class="product-name">${product.name}</h5>
                <p class="product-description">${product.description}</p>
                <div class="product-price">
                    <span class="price-symbol">¥</span>
                    <span class="price-amount">${product.price}</span>
                </div>
            </div>
        </div>
        </#list>
    </div>
    
    <!-- 分页 -->
    <div class="pagination-container">
        <nav aria-label="商品分页">
            <ul class="pagination justify-content-center">
                <li class="page-item disabled">
                    <a class="page-link" href="#" tabindex="-1" aria-disabled="true">上一页</a>
                </li>
                <li class="page-item active"><a class="page-link" href="#">1</a></li>
                <li class="page-item"><a class="page-link" href="#">2</a></li>
                <li class="page-item"><a class="page-link" href="#">3</a></li>
                <li class="page-item">
                    <a class="page-link" href="#">下一页</a>
                </li>
            </ul>
        </nav>
    </div>
</div>

<script>
// 商品分类过滤功能
document.addEventListener('DOMContentLoaded', function() {
    const filterButtons = document.querySelectorAll('.filter-btn');
    const productCards = document.querySelectorAll('.product-card');
    
    filterButtons.forEach(button => {
        button.addEventListener('click', function() {
            // 移除所有按钮的active类
            filterButtons.forEach(btn => btn.classList.remove('active'));
            // 添加当前按钮的active类
            this.classList.add('active');
            
            const selectedCategory = this.getAttribute('data-category');
            
            // 过滤商品
            productCards.forEach(card => {
                const cardCategory = card.getAttribute('data-category');
                
                if (selectedCategory === 'all' || cardCategory === selectedCategory) {
                    card.style.display = 'block';
                    card.style.animation = 'fadeIn 0.5s ease-in-out';
                } else {
                    card.style.display = 'none';
                }
            });
        });
    });
});
</script>

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

.add-to-cart-btn:hover {
    background: linear-gradient(135deg, #4AB8B2 0%, #3AA7A1 100%);
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(90, 207, 201, 0.4);
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