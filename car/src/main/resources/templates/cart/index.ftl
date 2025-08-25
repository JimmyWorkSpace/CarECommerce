<div class="cart-page">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <h2 class="cart-title">
                    <i class="bi bi-cart3 me-3"></i>
                    購物車
                </h2>
                
                <#if message??>
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="bi bi-check-circle me-2"></i>
                        ${message}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </#if>
                
                <!-- 購物車商品容器 -->
                <div id="cartItemsContainer">
                    <!-- 購物車商品將通過JavaScript動態加載 -->
                </div>
                
                <!-- 購物車統計和操作 -->
                <div class="cart-summary" id="cartSummary" style="display: none;">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="cart-actions">
                                <button class="btn btn-outline-secondary" onclick="clearCart()">
                                    <i class="bi bi-trash me-2"></i>
                                    清空購物車
                                </button>
                                <a href="/mall" class="btn btn-outline-primary">
                                    <i class="bi bi-arrow-left me-2"></i>
                                    繼續購物
                                </a>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="cart-total-section">
                                <div class="total-row">
                                    <span>商品總數：</span>
                                    <span><strong id="totalItems">0</strong></span>
                                </div>
                                <div class="total-row">
                                    <span>總數量：</span>
                                    <span><strong id="totalQuantity">0</strong></span>
                                </div>
                                <div class="total-row total-price">
                                    <span>總計：</span>
                                    <span><strong id="totalPrice">¥0</strong></span>
                                </div>
                                <button class="btn btn-success btn-lg checkout-btn" onclick="checkout()">
                                    <i class="bi bi-credit-card me-2"></i>
                                    立即結算
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- 空購物車提示 -->
                <div class="empty-cart" id="emptyCart" style="display: none;">
                    <div class="empty-cart-icon">
                        <i class="bi bi-cart-x"></i>
                    </div>
                    <h4>購物車是空的</h4>
                    <p class="text-muted">您還沒有添加任何商品到購物車</p>
                    <a href="/mall" class="btn btn-primary">
                        <i class="bi bi-shop me-2"></i>
                        去商城逻逻
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
// 從localStorage讀取購物車數據
let cart = JSON.parse(localStorage.getItem('cart') || '[]');

function renderCartItems() {
    const container = document.getElementById('cartItemsContainer');
    const cartSummary = document.getElementById('cartSummary');
    const emptyCart = document.getElementById('emptyCart');
    
    if (cart.length === 0) {
        container.innerHTML = '';
        if (cartSummary) cartSummary.style.display = 'none';
        if (emptyCart) emptyCart.style.display = 'block';
        return;
    }
    
    if (emptyCart) emptyCart.style.display = 'none';
    if (cartSummary) cartSummary.style.display = 'block';
    
    let html = '';
    let totalQuantity = 0;
    let totalPrice = 0;
    
    cart.forEach(function(item) {
        totalQuantity += item.quantity;
        totalPrice += item.price * item.quantity;
        
        html += '<div class="cart-item-card">' +
            '<div class="row align-items-center">' +
            '<div class="col-md-2">' +
            '<img src="' + item.image + '" alt="' + item.name + '" class="cart-item-image">' +
            '</div>' +
            '<div class="col-md-4">' +
            '<h5 class="cart-item-name">' + item.name + '</h5>' +
            '<p class="cart-item-price">¥' + item.price + '</p>' +
            '</div>' +
            '<div class="col-md-3">' +
            '<div class="quantity-controls">' +
            '<button class="btn btn-sm btn-outline-secondary quantity-btn" onclick="updateQuantity(\'' + item.id + '\', ' + (item.quantity - 1) + ')">' +
            '<i class="bi bi-dash"></i>' +
            '</button>' +
            '<span class="quantity-display">' + item.quantity + '</span>' +
            '<button class="btn btn-sm btn-outline-secondary quantity-btn" onclick="updateQuantity(\'' + item.id + '\', ' + (item.quantity + 1) + ')">' +
            '<i class="bi bi-plus"></i>' +
            '</button>' +
            '</div>' +
            '</div>' +
            '<div class="col-md-2">' +
            '<div class="item-total">' +
            '<strong>¥' + (item.price * item.quantity) + '</strong>' +
            '</div>' +
            '</div>' +
            '<div class="col-md-1">' +
            '<button class="btn btn-sm btn-danger remove-btn" onclick="removeItem(\'' + item.id + '\')">' +
            '<i class="bi bi-trash"></i>' +
            '</button>' +
            '</div>' +
            '</div>' +
            '</div>';
    });
    
    container.innerHTML = html;
    
    // 更新統計信息
    document.getElementById('totalItems').textContent = cart.length;
    document.getElementById('totalQuantity').textContent = totalQuantity;
    document.getElementById('totalPrice').textContent = '¥' + totalPrice;
}

function updateQuantity(productId, quantity) {
    if (quantity <= 0) {
        removeItem(productId);
        return;
    }
    
    const item = cart.find(item => item.id === productId);
    if (item) {
        item.quantity = quantity;
        localStorage.setItem('cart', JSON.stringify(cart));
        renderCartItems();
    }
}

function removeItem(productId) {
    if (confirm('確定要移除這個商品嗎？')) {
        cart = cart.filter(item => item.id !== productId);
        localStorage.setItem('cart', JSON.stringify(cart));
        renderCartItems();
    }
}

function clearCart() {
    if (confirm('確定要清空購物車嗎？')) {
        cart = [];
        localStorage.setItem('cart', JSON.stringify(cart));
        renderCartItems();
    }
}

function checkout() {
    if (confirm('確定要提交訂單嗎？')) {
        cart = [];
        localStorage.setItem('cart', JSON.stringify(cart));
        renderCartItems();
        alert('訂單提交成功！');
    }
}

// 頁面加載時渲染購物車
document.addEventListener('DOMContentLoaded', function() {
    renderCartItems();
});
</script>

<style>
.cart-page {
    padding: 2rem 0;
}

.cart-title {
    color: #333;
    margin-bottom: 2rem;
    padding-bottom: 1rem;
    border-bottom: 2px solid #5ACFC9;
}

.cart-item-card {
    background: white;
    border: 1px solid #e9ecef;
    border-radius: 10px;
    padding: 1.5rem;
    margin-bottom: 1rem;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    transition: all 0.3s ease;
}

.cart-item-card:hover {
    box-shadow: 0 4px 8px rgba(0,0,0,0.15);
    transform: translateY(-2px);
}

.cart-item-image {
    width: 100%;
    height: 100px;
    object-fit: cover;
    border-radius: 8px;
}

.cart-item-name {
    margin-bottom: 0.5rem;
    color: #333;
    font-weight: 600;
}

.cart-item-price {
    color: #666;
    margin-bottom: 0;
}

.quantity-controls {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 0.5rem;
}

.quantity-btn {
    width: 35px;
    height: 35px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 2px solid #5ACFC9;
    color: #5ACFC9;
    background: white;
    transition: all 0.3s ease;
}

.quantity-btn:hover {
    background: #5ACFC9;
    color: white;
    transform: scale(1.1);
}

.quantity-display {
    font-weight: 600;
    font-size: 1.1rem;
    min-width: 40px;
    text-align: center;
}

.item-total {
    text-align: center;
    color: #5ACFC9;
    font-size: 1.1rem;
}

.remove-btn {
    border-radius: 50%;
    width: 35px;
    height: 35px;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
}

.remove-btn:hover {
    transform: scale(1.1);
}

.cart-summary {
    background: white;
    border: 1px solid #e9ecef;
    border-radius: 10px;
    padding: 2rem;
    margin-top: 2rem;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.cart-actions {
    display: flex;
    gap: 1rem;
    margin-bottom: 1rem;
}

.total-row {
    display: flex;
    justify-content: space-between;
    margin-bottom: 0.5rem;
    font-size: 1.1rem;
}

.total-price {
    font-size: 1.3rem;
    font-weight: bold;
    color: #5ACFC9;
    border-top: 1px solid #e9ecef;
    padding-top: 0.5rem;
    margin-top: 0.5rem;
}

.checkout-btn {
    width: 100%;
    margin-top: 1rem;
    background: linear-gradient(135deg, #5ACFC9 0%, #4AB8B2 100%);
    border: none;
    transition: all 0.3s ease;
}

.checkout-btn:hover {
    background: linear-gradient(135deg, #4AB8B2 0%, #3AA7A1 100%);
    transform: translateY(-2px);
}

.empty-cart {
    text-align: center;
    padding: 4rem 2rem;
    background: white;
    border-radius: 10px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.empty-cart-icon {
    font-size: 4rem;
    color: #ccc;
    margin-bottom: 1rem;
}

.empty-cart h4 {
    color: #666;
    margin-bottom: 0.5rem;
}

.empty-cart p {
    margin-bottom: 2rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .cart-item-card {
        padding: 1rem;
    }
    
    .quantity-controls {
        flex-direction: column;
        gap: 0.25rem;
    }
    
    .quantity-btn {
        width: 30px;
        height: 30px;
        font-size: 0.8rem;
    }
    
    .cart-actions {
        flex-direction: column;
    }
    
    .cart-summary {
        padding: 1.5rem;
    }
}
</style> 