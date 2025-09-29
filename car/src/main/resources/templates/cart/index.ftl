<div class="cart-page" id="app">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <h2 class="cart-title">
                    <i class="bi bi-cart3 me-3"></i>
                    購物車
                </h2>
                
                <!-- 加載狀態 -->
                <div v-if="loading" class="text-center py-5">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">載入中...</span>
                    </div>
                    <p class="mt-3 text-muted">正在載入購物車...</p>
                </div>
                
                <!-- 錯誤提示 -->
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
                
                <!-- 購物車商品容器 -->
                <div v-if="!loading && cartItems.length > 0" id="cartItemsContainer">
                    <!-- 全选/取消全选 -->
                    <div class="cart-select-all mb-3">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" 
                                   :checked="isAllSelected" 
                                   @change="toggleSelectAll" 
                                   id="selectAll">
                            <label class="form-check-label" for="selectAll">
                                <strong>全选</strong>
                            </label>
                        </div>
                    </div>
                    
                    <div v-for="item in cartItems" :key="item.id" class="cart-item-card">
                        <div class="row align-items-center">
                            <div class="col-md-1">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" 
                                           :checked="item.selected" 
                                           @change="toggleItemSelection(item.id)"
                                           :id="'item-' + item.id">
                                    <label class="form-check-label" :for="'item-' + item.id">
                                        <!-- 隐藏的label，只用于复选框功能 -->
                                    </label>
                                </div>
                            </div>
                            <div class="col-md-2">
                                <img :src="getProductImage(item)" :alt="item.productName" class="cart-item-image">
                            </div>
                            <div class="col-md-3">
                                <h5 class="cart-item-name">{{ item.productName }}</h5>
                                <p class="cart-item-details">
                                    <span class="badge bg-secondary me-2">{{ item.brand }} {{ item.model }}</span>
                                    <span class="badge bg-info me-2">{{ item.tag }}</span>
                                    <span class="badge bg-warning me-2">{{ item.source }}</span>
                                    <span class="badge bg-success me-2">市价: ${CurrencyUnit} {{ formatPrice(item.marketPrice) }}</span>
                                </p>
                                <p class="cart-item-price">售价: ${CurrencyUnit} {{ formatPrice(item.productPrice) }}</p>
                            </div>
                            <div class="col-md-3">
                                <div class="quantity-controls">
                                    <button class="btn btn-sm btn-outline-secondary quantity-btn" 
                                            @click="decreaseQuantity(item.id)"
                                            :disabled="item.productAmount <= 1">
                                        <strong>-</strong>
                                    </button>
                                    <span class="quantity-display">{{ item.productAmount }}</span>
                                    <button class="btn btn-sm btn-outline-secondary quantity-btn" 
                                            @click="increaseQuantity(item.id)">
                                        <strong>+</strong>
                                    </button>
                                </div>
                            </div>
                            <div class="col-md-2">
                                <div class="item-total">
                                    <strong>${CurrencyUnit} {{ formatPrice(item.subtotal) }}</strong>
                                </div>
                            </div>
                            <div class="col-md-1">
                                <button class="btn btn-sm btn-danger remove-btn" @click="removeItem(item.id)">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- 購物車統計和操作 -->
                <div v-if="!loading && cartItems.length > 0" class="cart-summary">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="cart-actions">
                                <button class="btn btn-outline-secondary" @click="clearCart">
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
                                    <span><strong>{{ cartItems.length }}</strong></span>
                                </div>
                                <div class="total-row">
                                    <span>已選商品：</span>
                                    <span><strong>{{ selectedItemsCount }}</strong></span>
                                </div>
                                <div class="total-row">
                                    <span>已選數量：</span>
                                    <span><strong>{{ selectedQuantity }}</strong></span>
                                </div>
                                <div class="total-row total-price">
                                    <span>已選總計：</span>
                                    <span><strong>${CurrencyUnit} {{ formatPrice(selectedTotalPrice) }}</strong></span>
                                </div>
                                <button class="btn btn-success btn-lg checkout-btn" 
                                        @click="checkout" 
                                        :disabled="selectedItemsCount === 0">
                                    <i class="bi bi-credit-card me-2"></i>
                                    結算選中商品 ({{ selectedItemsCount }}件)
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- 空購物車提示 -->
                <div v-if="!loading && cartItems.length === 0" class="empty-cart">
                    <div class="empty-cart-icon">
                        <i class="bi bi-cart-x"></i>
                    </div>
                    <h4>購物車是空的</h4>
                    <p class="text-muted">您還沒有添加任何商品到購物車</p>
                    <a href="/mall" class="btn btn-primary">
                        <i class="bi bi-shop me-2"></i>
                        去商城購物
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

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

.cart-item-details {
    margin-bottom: 0.5rem;
}

.cart-item-details .badge {
    font-size: 0.75rem;
}

.cart-item-price {
    font-size: 1.1rem;
    font-weight: 600;
    color: #e74c3c;
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
    font-weight: bold;
}

.quantity-display {
    min-width: 40px;
    text-align: center;
    font-weight: 600;
    font-size: 1.1rem;
}

.item-total {
    text-align: center;
    font-size: 1.1rem;
    color: #e74c3c;
}

.remove-btn {
    width: 35px;
    height: 35px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
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
    flex-wrap: wrap;
}

.cart-total-section {
    text-align: right;
}

.total-row {
    display: flex;
    justify-content: space-between;
    margin-bottom: 0.5rem;
    font-size: 1rem;
}

.total-price {
    font-size: 1.2rem;
    font-weight: 600;
    color: #e74c3c;
    border-top: 1px solid #e9ecef;
    padding-top: 0.5rem;
    margin-top: 1rem;
}

.checkout-btn {
    margin-top: 1rem;
    width: 100%;
}

.empty-cart {
    text-align: center;
    padding: 4rem 2rem;
    background: white;
    border: 1px solid #e9ecef;
    border-radius: 10px;
    margin-top: 2rem;
}

.empty-cart-icon {
    font-size: 4rem;
    color: #6c757d;
    margin-bottom: 1rem;
}

.empty-cart h4 {
    color: #333;
    margin-bottom: 0.5rem;
}

.empty-cart p {
    color: #6c757d;
    margin-bottom: 2rem;
}

@media (max-width: 768px) {
    .cart-item-card .row > div {
        margin-bottom: 1rem;
    }
    
    .cart-actions {
        justify-content: center;
    }
    
    .cart-total-section {
        text-align: center;
        margin-top: 1rem;
    }
}
</style>

<script>
<#noparse>
new Vue({
    el: '#app',
    data: {
        cartItems: [],
        loading: false,
        error: '',
        message: ''
    },
    computed: {
        totalQuantity() {
            return this.cartItems.reduce((total, item) => total + (item.productAmount || 0), 0);
        },
        totalPrice() {
            return this.cartItems.reduce((total, item) => {
                const price = item.productPrice || 0;
                const amount = item.productAmount || 0;
                return total + (price * amount);
            }, 0);
        },
        // 选中的商品数量
        selectedItemsCount() {
            return this.cartItems.filter(item => item.selected).length;
        },
        // 选中商品的总数量
        selectedQuantity() {
            return this.cartItems
                .filter(item => item.selected)
                .reduce((total, item) => total + (item.productAmount || 0), 0);
        },
        // 选中商品的总价格
        selectedTotalPrice() {
            return this.cartItems
                .filter(item => item.selected)
                .reduce((total, item) => {
                    const price = item.productPrice || 0;
                    const amount = item.productAmount || 0;
                    return total + (price * amount);
                }, 0);
        },
        // 是否全选
        isAllSelected() {
            return this.cartItems.length > 0 && this.cartItems.every(item => item.selected);
        }
    },
    mounted() {
        console.log('購物車页面Vue实例已挂载');
        // 延迟加载，避免与主布局的初始化冲突
        setTimeout(() => {
            this.loadCart();
        }, 100);
    },
    methods: {
        // 加载購物車数据
        async loadCart() {
            // 防止重复调用
            if (this.loading) {
                console.log('購物車正在加载中，跳过重复调用');
                return;
            }
            
            try {
                console.log('开始加载購物車数据...');
                this.loading = true;
                this.error = '';
                
                const response = await axios.get('/api/shopping-cart/list');
                console.log('購物車API响应:', response);
                const data = response.data;
                console.log('購物車数据:', data);
                
                if (data.success) {
                    this.cartItems = (data.data || []).map(item => ({
                        ...item,
                        selected: true // 默认选中所有商品
                    }));
                    console.log('購物車数据加载成功，商品数量:', this.cartItems.length);
                    console.log('購物車商品详情:', this.cartItems);
                    // 更新全局購物車数量
                    this.updateGlobalCartCount();
                } else {
                    // 检查是否是未登录错误
                    if (data.message && (data.message.includes('未登录') || data.message.includes('用户未登录'))) {
                        console.log('用户未登录，跳转到登录页面');
                        // 保存当前页面URL，登录后可以跳转回来
                        const currentUrl = window.location.pathname + window.location.search;
                        localStorage.setItem('redirectAfterLogin', currentUrl);
                        // 跳转到登录页面
                        window.location.href = '/login';
                        return; // 已处理未登录状态，不显示错误信息
                    }
                    this.error = data.message || '获取購物車失败';
                    console.error('購物車数据加载失败:', data.message);
                }
            } catch (error) {
                console.error('加载購物車失败:', error);
                console.error('错误详情:', error.response?.data || error.message);
                console.error('错误状态码:', error.response?.status);
                // 使用通用错误处理函数
                const errorMessage = window.handleApiError ? window.handleApiError(error, '网络错误，请稍后重试') : '网络错误，请稍后重试';
                this.error = errorMessage;
            } finally {
                console.log('購物車加载完成，loading状态设置为false');
                this.loading = false;
                console.log('当前購物車状态 - loading:', this.loading, 'cartItems数量:', this.cartItems.length, 'error:', this.error);
            }
        },
        
        // 减少商品数量
        async decreaseQuantity(id) {
            const item = this.cartItems.find(item => item.id === id);
            if (item && item.productAmount > 1) {
                await this.updateQuantity(id, item.productAmount - 1);
            }
        },
        
        // 增加商品数量
        async increaseQuantity(id) {
            const item = this.cartItems.find(item => item.id === id);
            if (item) {
                await this.updateQuantity(id, item.productAmount + 1);
            }
        },
        
        // 更新商品数量
        async updateQuantity(id, newQuantity) {
            console.log('准备更新商品数量，ID: ' + id + ', 新数量: ' + newQuantity);
            
            if (newQuantity <= 0) {
                console.log('数量为0或负数，移除商品');
                await this.removeItem(id);
                return;
            }
            
            try {
                const url = '/api/shopping-cart/update/' + id;
                console.log('发送更新请求到: ' + url + ', 数量: ' + newQuantity);
                
                const response = await axios.put(url, {
                    quantity: newQuantity
                });
                
                const data = response.data;
                console.log('更新响应:', data);
                
                if (data.success) {
                    // 使用通用成功消息显示函数
                    if (window.showSuccessMessage) {
                        window.showSuccessMessage('更新数量成功');
                    } else {
                        this.message = '更新数量成功';
                    }
                    // 更新本地数据
                    const item = this.cartItems.find(item => item.id === id);
                    if (item) {
                        item.productAmount = newQuantity;
                        console.log('商品 ' + id + ' 数量已更新为: ' + newQuantity);
                    }
                    // 更新全局購物車数量
                    this.updateGlobalCartCount();
                } else {
                    // 使用通用错误消息显示函数
                    if (window.showErrorMessage) {
                        window.showErrorMessage(data.message || '更新数量失败');
                    } else {
                        this.error = data.message || '更新数量失败';
                    }
                    console.error('更新数量失败:', data.message);
                }
            } catch (error) {
                console.error('更新数量失败:', error);
                console.error('错误详情:', error.response?.data || error.message);
                // 使用通用错误处理函数
                const errorMessage = window.handleApiError ? window.handleApiError(error, '网络错误，请稍后重试') : '网络错误，请稍后重试';
                this.error = errorMessage;
            }
        },
        
        // 移除商品
        async removeItem(id) {
            if (!confirm('確定要移除這個商品嗎？')) {
                return;
            }
            
            try {
                const response = await axios.delete('/api/shopping-cart/remove/' + id);
                
                const data = response.data;
                if (data.success) {
                    // 从本地数组中移除
                    this.cartItems = this.cartItems.filter(item => item.id !== id);
                    // 更新全局購物車数量
                    this.updateGlobalCartCount();
                    // 显示成功消息
                    if (window.showSuccessMessage) {
                        window.showSuccessMessage('商品已移除');
                    } else {
                        this.message = '商品已移除';
                    }
                } else {
                    // 显示错误消息
                    if (window.showErrorMessage) {
                        window.showErrorMessage(data.message || '移除商品失败');
                    } else {
                        this.error = data.message || '移除商品失败';
                    }
                }
            } catch (error) {
                console.error('移除商品失败:', error);
                const errorMessage = window.handleApiError ? window.handleApiError(error, '网络错误，请稍后重试') : '网络错误，请稍后重试';
                this.error = errorMessage;
            }
        },
        
        // 清空購物車
        async clearCart() {
            if (!confirm('確定要清空整個購物車嗎？')) {
                return;
            }
            
            try {
                const response = await axios.delete('/api/shopping-cart/clear');
                
                const data = response.data;
                if (data.success) {
                    this.cartItems = [];
                    // 更新全局購物車数量
                    this.updateGlobalCartCount();
                    // 显示成功消息
                    if (window.showSuccessMessage) {
                        window.showSuccessMessage('購物車已清空');
                    } else {
                        this.message = '購物車已清空';
                    }
                } else {
                    // 显示错误消息
                    if (window.showErrorMessage) {
                        window.showErrorMessage(data.message || '清空購物車失敗');
                    } else {
                        this.error = data.message || '清空購物車失敗';
                    }
                }
            } catch (error) {
                console.error('清空購物車失敗:', error);
                const errorMessage = window.handleApiError ? window.handleApiError(error, '网络错误，请稍后重试') : '网络错误，请稍后重试';
                this.error = errorMessage;
            }
        },
        
        // 切换全选状态
        toggleSelectAll() {
            const isAllSelected = this.isAllSelected;
            this.cartItems.forEach(item => {
                item.selected = !isAllSelected;
            });
        },
        
        // 切换单个商品选择状态
        toggleItemSelection(itemId) {
            const item = this.cartItems.find(item => item.id === itemId);
            if (item) {
                item.selected = !item.selected;
            }
        },
        
        // 结算
        async checkout() {
            const selectedItems = this.cartItems.filter(item => item.selected);
            
            if (selectedItems.length === 0) {
                if (window.showErrorMessage) {
                    window.showErrorMessage('請選擇要結算的商品');
                } else {
                    this.error = '請選擇要結算的商品';
                }
                return;
            }
            
            try {
                // 构建结算数据，只包含选中的商品
                const checkoutData = {
                    items: selectedItems.map(item => ({
                        id: item.id,
                        productId: item.productId,
                        productName: item.productName,
                        productAmount: item.productAmount,
                        productPrice: item.productPrice,
                        subtotal: item.subtotal
                    })),
                    totalAmount: this.selectedTotalPrice,
                    totalQuantity: this.selectedQuantity
                };
                
                // 创建表单并提交到支付页面
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = '/payment/index';
                
                // 添加支付参数
                const formData = {
                    itemName: `購物車商品 (${selectedItems.length}件)`,
                    amount: this.selectedTotalPrice,
                    description: `購買${selectedItems.length}件商品，總計${this.selectedTotalPrice}元`,
                    cartData: JSON.stringify(checkoutData)
                };
                
                // 创建隐藏的input字段
                Object.entries(formData).forEach(([key, value]) => {
                    const input = document.createElement('input');
                    input.type = 'hidden';
                    input.name = key;
                    input.value = value;
                    form.appendChild(input);
                });
                
                // 提交表单
                document.body.appendChild(form);
                form.submit();
                
            } catch (error) {
                console.error('結算失敗:', error);
                const errorMessage = window.handleApiError ? window.handleApiError(error, '結算失敗，請稍後重試') : '結算失敗，請稍後重試';
                this.error = errorMessage;
            }
        },
        
        // 格式化价格
        formatPrice(price) {
            if (!price) return '0';
            return parseFloat(price).toLocaleString('zh-CN', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            });
        },
        
        
        // 跳转到登录页面
        redirectToLogin() {
            console.log('跳转到登录页面...');
            // 保存当前页面URL，登录后可以跳转回来
            const currentUrl = window.location.pathname + window.location.search;
            localStorage.setItem('redirectAfterLogin', currentUrl);
            // 跳转到登录页面
            window.location.href = '/login';
        },
        
        // 更新全局購物車数量
        updateGlobalCartCount() {
            if (window.cartManager && window.cartManager.updateCartCount) {
                window.cartManager.updateCartCount();
            }
        },
        
        // 获取商品图片
        getProductImage(item) {
            // 直接使用DTO中的商品图片路径
            return item.productImage || '/img/product/default_90x90.jpg';
        }
    }
});
</#noparse>
</script>

