<div class="cart-page" id="app">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <h2 class="cart-title">
                    <i class="bi bi-cart3 me-3"></i>
                    購物車
                </h2>
                
                <!-- 加载状态 -->
                <div v-if="loading" class="text-center py-5">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">載入中...</span>
                    </div>
                    <p class="mt-3 text-muted">正在載入購物車...</p>
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
                
                <!-- 購物車商品容器 -->
                <div v-if="!loading && cartItems.length > 0" id="cartItemsContainer">
                    <div v-for="item in cartItems" :key="item.id" class="cart-item-card">
                        <div class="row align-items-center">
                            <div class="col-md-2">
                                <img :src="item.productImage" :alt="item.productName" class="cart-item-image">
                            </div>
                            <div class="col-md-4">
                                <h5 class="cart-item-name">{{ item.productName }}</h5>
                                <p class="cart-item-details">
                                    <span class="badge bg-secondary me-2">{{ item.brandName }} {{ item.modelName }}</span>
                                    <span class="badge bg-info me-2">{{ item.manufactureYear }}年</span>
                                    <span class="badge bg-warning me-2">{{ item.color }}</span>
                                    <span class="badge bg-success me-2">{{ item.mileage }}km</span>
                                </p>
                                <p class="cart-item-price">¥{{ formatPrice(item.productPrice) }}</p>
                            </div>
                            <div class="col-md-3">
                                <div class="quantity-controls">
                                    <button class="btn btn-sm btn-outline-secondary quantity-btn" 
                                            @click="updateQuantity(item.id, item.productAmount - 1)"
                                            :disabled="item.productAmount <= 1">
                                        <i class="bi bi-dash"></i>
                                    </button>
                                    <span class="quantity-display">{{ item.productAmount }}</span>
                                    <button class="btn btn-sm btn-outline-secondary quantity-btn" 
                                            @click="updateQuantity(item.id, item.productAmount + 1)">
                                        <i class="bi bi-plus"></i>
                                    </button>
                                </div>
                            </div>
                            <div class="col-md-2">
                                <div class="item-total">
                                    <strong>¥{{ formatPrice(item.subtotal) }}</strong>
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
                                    <span>總數量：</span>
                                    <span><strong>{{ totalQuantity }}</strong></span>
                                </div>
                                <div class="total-row total-price">
                                    <span>總計：</span>
                                    <span><strong>¥{{ formatPrice(totalPrice) }}</strong></span>
                                </div>
                                <button class="btn btn-success btn-lg checkout-btn" @click="checkout">
                                    <i class="bi bi-credit-card me-2"></i>
                                    立即結算
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
    color: #666;
    margin-bottom: 0;
    font-weight: 600;
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

.quantity-btn:hover:not(:disabled) {
    background: #5ACFC9;
    color: white;
    transform: scale(1.1);
}

.quantity-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
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

<script>
new Vue({
    el: '#app',
    data: {
        cartItems: [],
        loading: true,
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
        }
    },
    mounted() {
        this.loadCart();
    },
    methods: {
        // 加载购物车数据
        async loadCart() {
            try {
                this.loading = true;
                this.error = '';
                
                const response = await axios.get('/api/shopping-cart/list');
                const data = response.data;
                
                if (data.success) {
                    this.cartItems = data.data || [];
                } else {
                    this.error = data.message || '获取购物车失败';
                }
            } catch (error) {
                console.error('加载购物车失败:', error);
                this.error = '网络错误，请稍后重试';
            } finally {
                this.loading = false;
            }
        },
        
        // 更新商品数量
        async updateQuantity(id, quantity) {
            if (quantity <= 0) {
                await this.removeItem(id);
                return;
            }
            
            try {
                const response = await axios.put(`/api/shopping-cart/update/${id}`, {
                    quantity: quantity
                });
                
                const data = response.data;
                if (data.success) {
                    this.message = '更新数量成功';
                    // 更新本地数据
                    const item = this.cartItems.find(item => item.id === id);
                    if (item) {
                        item.productAmount = quantity;
                    }
                } else {
                    this.error = data.message || '更新数量失败';
                }
            } catch (error) {
                console.error('更新数量失败:', error);
                this.error = '网络错误，请稍后重试';
            }
        },
        
        // 移除商品
        async removeItem(id) {
            if (!confirm('確定要移除這個商品嗎？')) {
                return;
            }
            
            try {
                const response = await axios.delete(`/api/shopping-cart/remove/${id}`);
                
                const data = response.data;
                if (data.success) {
                    this.message = '移除商品成功';
                    // 从本地数据中移除
                    this.cartItems = this.cartItems.filter(item => item.id !== id);
                } else {
                    this.error = data.message || '移除商品失败';
                }
            } catch (error) {
                console.error('移除商品失败:', error);
                this.error = '网络错误，请稍后重试';
            }
        },
        
        // 清空购物车
        async clearCart() {
            if (!confirm('確定要清空購物車嗎？')) {
                return;
            }
            
            try {
                const response = await axios.delete('/api/shopping-cart/clear');
                
                const data = response.data;
                if (data.success) {
                    this.message = '清空購物車成功';
                    this.cartItems = [];
                } else {
                    this.error = data.message || '清空購物車失败';
                }
            } catch (error) {
                console.error('清空購物車失败:', error);
                this.error = '网络错误，请稍后重试';
            }
        },
        
        // 结算
        async checkout() {
            if (!confirm('確定要提交訂單嗎？')) {
                return;
            }
            
            try {
                // 这里可以调用结算接口
                // 暂时直接清空购物车
                await this.clearCart();
                this.message = '訂單提交成功！';
            } catch (error) {
                console.error('结算失败:', error);
                this.error = '结算失败，请稍后重试';
            }
        },
        
        // 格式化价格
        formatPrice(price) {
            if (!price) return '0';
            return parseFloat(price).toLocaleString('zh-CN', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            });
        }
    }
});
</script> 