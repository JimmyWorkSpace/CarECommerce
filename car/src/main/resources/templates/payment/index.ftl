    <style>
    /* 支付页面样式 */
    .payment-page {
        padding: 20px 0;
        background-color: #f8f9fa;
        min-height: 100vh;
    }
    
    .payment-title {
        color: #333;
            margin-bottom: 30px;
        font-weight: 600;
    }
    
    .payment-form-card, .payment-summary-card {
            margin-bottom: 20px;
        }
    
    .payment-page .card {
        border: none;
        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        border-radius: 10px;
    }
    
    .payment-page .card-header {
        background: linear-gradient(135deg, #5ACFC9 0%, #4AB8B2 100%);
        color: white;
        border-radius: 10px 10px 0 0 !important;
        border: none;
    }
    
    .payment-page .card-title {
        color: white;
        font-weight: 600;
    }
    
    .order-info {
            background: #f8f9fa;
        padding: 15px;
            border-radius: 8px;
    }
    
    .info-row {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 8px 0;
        border-bottom: 1px solid #e9ecef;
    }
    
    .info-row:last-child {
        border-bottom: none;
    }
    
    .info-label {
        font-weight: 500;
        color: #666;
    }
    
    .info-value {
        color: #333;
        font-weight: 500;
    }
    
    .info-value.amount {
        color: #e74c3c;
        font-weight: 600;
        font-size: 1.1em;
        }
        
        .cart-items-list {
        max-height: 200px;
            overflow-y: auto;
            border: 1px solid #dee2e6;
            border-radius: 8px;
        padding: 10px;
            background: #f8f9fa;
        }
        
        .cart-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px;
        margin-bottom: 8px;
            background: white;
            border-radius: 6px;
            border: 1px solid #e9ecef;
        }
        
        .cart-item:last-child {
            margin-bottom: 0;
        }
        
        .cart-item-info {
            flex: 1;
        }
        
        .cart-item-name {
            font-weight: 600;
        margin-bottom: 4px;
        color: #333;
        }
        
        .cart-item-details {
        font-size: 0.85rem;
            color: #6c757d;
        }
        
        .cart-item-price {
            font-weight: 600;
            color: #e74c3c;
        }
    
    .payment-page .btn-primary {
        background: linear-gradient(135deg, #5ACFC9 0%, #4AB8B2 100%);
        border: none;
        border-radius: 8px;
        padding: 12px 24px;
        font-weight: 600;
    }
    
    .payment-page .btn-primary:hover {
        background: linear-gradient(135deg, #4AB8B2 0%, #3a9a94 100%);
        transform: translateY(-1px);
        box-shadow: 0 4px 12px rgba(90, 207, 201, 0.3);
    }
    
    .payment-page .btn-outline-secondary, .payment-page .btn-outline-primary {
        border-radius: 8px;
        padding: 8px 16px;
        font-weight: 500;
    }
    
    .payment-page .btn-outline-primary {
        border-color: #5ACFC9;
        color: #5ACFC9;
    }
    
    .payment-page .btn-outline-primary:hover {
        background-color: #5ACFC9;
        border-color: #5ACFC9;
    }
    
    .payment-page .form-control {
        border-radius: 8px;
        border: 1px solid #dee2e6;
        padding: 10px 12px;
    }
    
    .payment-page .form-control:focus {
        border-color: #5ACFC9;
        box-shadow: 0 0 0 0.2rem rgba(90, 207, 201, 0.25);
    }
    
    .payment-page .form-label {
        font-weight: 500;
        color: #333;
        margin-bottom: 6px;
    }
    
    .spin {
        animation: spin 1s linear infinite;
    }
    
    @keyframes spin {
        from { transform: rotate(0deg); }
        to { transform: rotate(360deg); }
    }
    
    /* 响应式设计 */
    @media (max-width: 768px) {
        .payment-page {
            padding: 10px 0;
        }
        
        .payment-title {
            font-size: 1.5rem;
            margin-bottom: 20px;
        }
        
        .payment-page .card-body {
            padding: 15px;
        }
        
        .info-row {
            flex-direction: column;
            align-items: flex-start;
            gap: 4px;
        }
        }
    </style>

<div class="payment-page" id="app">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <h2 class="payment-title">
                    <i class="bi bi-credit-card me-3"></i>
                    支付订单
                </h2>
                
                <!-- 环境提示 -->
                <div v-if="paymentConfig && !paymentConfig.isProduction" class="alert alert-warning alert-dismissible fade show" role="alert">
                    <i class="bi bi-exclamation-triangle me-2"></i>
                    <strong>测试环境提示：</strong>当前为{{ paymentConfig.environment }}环境，支付金额固定为0.01元
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                
                <!-- 错误提示 -->
                <div v-if="errorMessage" class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="bi bi-exclamation-triangle me-2"></i>
                    {{ errorMessage }}
                    <button type="button" class="btn-close" @click="errorMessage = ''"></button>
                </div>
                
                <!-- 成功提示 -->
                <div v-if="successMessage" class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="bi bi-check-circle me-2"></i>
                    {{ successMessage }}
                    <button type="button" class="btn-close" @click="successMessage = ''"></button>
            </div>
            
                <div class="row">
                    <div class="col-lg-8">
                        <div class="payment-form-card">
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="card-title mb-0">
                                        <i class="bi bi-person me-2"></i>收件人信息
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <form @submit.prevent="submitPayment">
                                        <div class="row">
                                            <div class="col-md-6 mb-3">
                                                <label for="receiverName" class="form-label">收件人姓名 <span class="text-danger">*</span></label>
                                                <input type="text" class="form-control" id="receiverName" 
                                                       placeholder="请输入收件人姓名" 
                                                       v-model="formData.receiverName" required>
                                            </div>
                                            <div class="col-md-6 mb-3">
                                                <label for="receiverMobile" class="form-label">收件人手机号 <span class="text-danger">*</span></label>
                                                <input type="tel" class="form-control" id="receiverMobile" 
                                                       placeholder="请输入收件人手机号" 
                                                       v-model="formData.receiverMobile" required>
                                            </div>
                                        </div>
                                        <div class="mb-3">
                                            <label for="receiverAddress" class="form-label">收件人地址 <span class="text-danger">*</span></label>
                                            <textarea class="form-control" id="receiverAddress" 
                                                      rows="3" placeholder="请输入详细的收件人地址" 
                                                      v-model="formData.receiverAddress" required></textarea>
                                        </div>
                                        <div class="mb-3">
                                            <label for="description" class="form-label">订单描述</label>
                                            <textarea class="form-control" id="description" 
                                                      rows="2" placeholder="请输入订单描述（可选）"
                                                      v-model="formData.description"></textarea>
                    </div>
                    
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-lg-4">
                        <div class="payment-summary-card">
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="card-title mb-0">
                                        <i class="bi bi-receipt me-2"></i>订单摘要
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <div class="order-info">
                                        <div class="info-row">
                                            <span class="info-label">商品名称：</span>
                                            <span class="info-value" v-text="formData.itemName"></span>
                                        </div>
                                        <div class="info-row">
                                            <span class="info-label">支付金额：</span>
                                            <span class="info-value amount">¥ {{ formData.amount }}</span>
                                        </div>
                                        <div class="info-row">
                                            <span class="info-label">收件人：</span>
                                            <span class="info-value" v-text="formData.receiverName || '-'"></span>
                    </div>
                                        <div class="info-row">
                                            <span class="info-label">联系电话：</span>
                                            <span class="info-value" v-text="formData.receiverMobile || '-'"></span>
                    </div>
                                        <div class="info-row">
                                            <span class="info-label">收货地址：</span>
                                            <span class="info-value" v-text="formData.receiverAddress || '-'"></span>
                    </div>
                    </div>
                    
                    <!-- 购物车商品列表 -->
                                    <div v-if="cartItems && cartItems.length > 0" class="mt-3">
                                        <h6 class="mb-3">
                                            <i class="bi bi-cart3 me-2"></i>购物车商品
                                        </h6>
                                        <div class="cart-items-list">
                                            <div class="cart-item" v-for="item in cartItems" :key="item.id">
                                                <div class="cart-item-info">
                                                    <div class="cart-item-name" v-text="item.productName"></div>
                                                    <div class="cart-item-details">
                                                        数量: {{ item.productAmount }} × ¥{{ parseFloat(item.productPrice).toFixed(2) }}
                                                    </div>
                                                </div>
                                                <div class="cart-item-price">
                                                    ¥{{ parseFloat(item.subtotal).toFixed(2) }}
                                                </div>
                                            </div>
                        </div>
                    </div>
                        
                        <!-- 立即支付按钮 -->
                        <div class="d-grid gap-2 mt-4">
                            <button type="button" class="btn btn-primary btn-lg" @click="submitPayment" :disabled="isLoading">
                                <span v-if="!isLoading">
                                    <i class="bi bi-credit-card me-2"></i>立即支付
                                </span>
                                <span v-else>
                                    <i class="bi bi-arrow-clockwise spin me-2"></i>处理中...
                                </span>
                            </button>
                        </div>
                        </div>
                        </div>
                        </div>
                    </div>
            </div>
            
                <div class="row mt-4">
                    <div class="col-12">
                        <div class="d-flex justify-content-center gap-3">
                            <a href="/cart" class="btn btn-outline-secondary">
                                <i class="bi bi-arrow-left me-2"></i>返回购物车
                </a>
                <a href="/mall" class="btn btn-outline-primary">
                                <i class="bi bi-shop me-2"></i>继续购物
                </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            new Vue({
            el: '#app',
            data: {
                formData: {
                    itemName: '${itemName!''}',
                    amount: ${amount!0},
                    description: '${description!''}',
                    receiverName: '',
                    receiverMobile: '',
                    receiverAddress: ''
                },
                cartItems: [],
                isLoading: false,
                errorMessage: '',
                successMessage: '',
                paymentConfig: null
            },
            mounted() {
                console.log('Vue实例已挂载');
                this.initCartItems();
                this.loadPaymentConfig();
            },
            methods: {
                // 加载支付配置
                loadPaymentConfig() {
                    axios.get('/api/payment/config')
                        .then(response => {
                            if (response.data.code === 1) {
                                this.paymentConfig = response.data.data;
                            } else {
                                console.error('获取支付配置失败:', response.data.msg);
                            }
                        })
                        .catch(error => {
                            console.error('获取支付配置异常:', error);
                        });
                },
                
                // 初始化购物车商品列表
                initCartItems() {
                    const cartDataStr = '${cartData!''}';
                    if (cartDataStr && cartDataStr.trim() !== '') {
                        try {
                            const cartData = JSON.parse(cartDataStr);
                            this.cartItems = cartData.items || [];
                        } catch (error) {
                            console.error('解析购物车数据失败:', error);
                            console.error('购物车数据内容:', cartDataStr);
                        }
                    }
                },
                
                // 提交支付
                submitPayment() {
                    console.log('submitPayment方法被调用');
                    // 表单验证
                    if (!this.validateForm()) {
                        console.log('表单验证失败');
                    return;
                }
                
                    this.isLoading = true;
                    this.clearMessages();
                    
                    // 创建支付订单
                    axios.post('/api/payment/create', {
                        amount: this.formData.amount,
                        itemName: this.formData.itemName,
                        description: this.formData.description || '购买商品：' + this.formData.itemName,
                        receiverName: this.formData.receiverName,
                        receiverMobile: this.formData.receiverMobile,
                        receiverAddress: this.formData.receiverAddress,
                        cartData: this.cartItems
                    })
                    .then(response => {
                        if (response.data.code === 1) {
                            // 支付订单创建成功，跳转到绿界支付页面
                            this.submitToECPay(response.data.data);
                        } else {
                            this.showError(response.data.msg || '創建支付訂單失敗');
                        }
                    })
                    .catch(error => {
                        console.error('创建支付订单失败:', error);
                        this.showError('創建支付訂單失敗，請稍後重試');
                    })
                    .finally(() => {
                        this.isLoading = false;
                    });
                },
                
                // 表单验证
                validateForm() {
                    if (!this.formData.amount || this.formData.amount <= 0) {
                        this.showError('支付金額必須大於0');
                        return false;
                    }
                    
                    if (!this.formData.itemName || this.formData.itemName.trim() === '') {
                        this.showError('商品名稱不能為空');
                        return false;
                    }
                    
                    if (!this.formData.receiverName || this.formData.receiverName.trim() === '') {
                        this.showError('收件人姓名不能為空');
                        return false;
                    }
                    
                    if (!this.formData.receiverMobile || this.formData.receiverMobile.trim() === '') {
                        this.showError('收件人手機號不能為空');
                        return false;
                    }
                    
                    if (!this.formData.receiverAddress || this.formData.receiverAddress.trim() === '') {
                        this.showError('收件人地址不能為空');
                        return false;
                    }
                    
                    return true;
                },
            
            // 提交到绿界支付
                submitToECPay(paymentParams) {
                    // 获取支付服务器地址
                    const serverUrl = this.paymentConfig ? this.paymentConfig.serverUrl : 'https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5';
                    
                // 创建表单并提交到绿界支付
                const form = document.createElement('form');
                form.method = 'POST';
                    form.action = serverUrl;
                
                // 添加所有支付参数
                for (const [key, value] of Object.entries(paymentParams)) {
                    const input = document.createElement('input');
                    input.type = 'hidden';
                    input.name = key;
                    input.value = value;
                    form.appendChild(input);
                }
                
                // 提交表单
                document.body.appendChild(form);
                form.submit();
                },
            
            // 显示错误信息
                showError(message) {
                    this.errorMessage = message;
                    this.successMessage = '';
                },
            
            // 显示成功信息
                showSuccess(message) {
                    this.successMessage = message;
                    this.errorMessage = '';
                },
                
                // 清除所有消息
                clearMessages() {
                    this.errorMessage = '';
                    this.successMessage = '';
                }
            }
        });
        });
    </script>