    <style>
    /* 支付頁面樣式 */
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
    
    /* 響應式設計 */
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
                    支付訂單
                </h2>
                
                <!-- 環境提示 -->
                <div v-if="paymentConfig && !paymentConfig.isProduction" class="alert alert-warning alert-dismissible fade show" role="alert">
                    <i class="bi bi-exclamation-triangle me-2"></i>
                    <strong>測試環境提示：</strong>當前為{{ paymentConfig.environment }}環境，支付金額固定為0.01元
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                
                <!-- 訂單重新支付提示 -->
                <div v-if="orderId" class="alert alert-info alert-dismissible fade show" role="alert">
                    <i class="bi bi-info-circle me-2"></i>
                    <strong>重新支付訂單：</strong>訂單號 {{ orderNo }}，請完成支付操作
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                
                <!-- 錯誤提示 -->
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
                                        <i class="bi bi-truck me-2"></i>配送方式
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <!-- Tab導航 -->
                                    <ul class="nav nav-tabs mb-4" id="deliveryTabs" role="tablist">
                                        <li class="nav-item" role="presentation">
                                            <button class="nav-link active" id="home-delivery-tab" data-bs-toggle="tab" 
                                                    data-bs-target="#home-delivery" type="button" role="tab" 
                                                    aria-controls="home-delivery" aria-selected="true"
                                                    @click="switchDeliveryType(1)">
                                                <i class="bi bi-house me-2"></i>宅配到府
                                            </button>
                                        </li>
                                        <li class="nav-item" role="presentation">
                                            <button class="nav-link" id="store-pickup-tab" data-bs-toggle="tab" 
                                                    data-bs-target="#store-pickup" type="button" role="tab" 
                                                    aria-controls="store-pickup" aria-selected="false"
                                                    @click="switchDeliveryType(2)">
                                                <i class="bi bi-shop me-2"></i>超商取貨
                                            </button>
                                        </li>
                                    </ul>
                                    
                                    <!-- Tab內容 -->
                                    <div class="tab-content" id="deliveryTabContent">
                                        <!-- 宅配到府 -->
                                        <div class="tab-pane fade show active" id="home-delivery" role="tabpanel" 
                                             aria-labelledby="home-delivery-tab">
                                            <form @submit.prevent="submitPayment">
                                                <div class="row">
                                                    <div class="col-md-6 mb-3">
                                                        <label for="receiverName" class="form-label">收件人姓名 <span class="text-danger">*</span></label>
                                                        <input type="text" class="form-control" id="receiverName" 
                                                               placeholder="請輸入收件人姓名" 
                                                               v-model="formData.receiverName" required>
                                                    </div>
                                                    <div class="col-md-6 mb-3">
                                                        <label for="receiverMobile" class="form-label">收件人手機號 <span class="text-danger">*</span></label>
                                                        <input type="text" class="form-control" id="receiverMobile" 
                                                               placeholder="請輸入收件人手機號" 
                                                               v-model="formData.receiverMobile" required>
                                                    </div>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="receiverAddress" class="form-label">收件人地址 <span class="text-danger">*</span></label>
                                                    <textarea class="form-control" id="receiverAddress" 
                                                              rows="3" placeholder="請輸入詳細的收件人地址" 
                                                              v-model="formData.receiverAddress" required></textarea>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="description" class="form-label">訂單描述</label>
                                                    <textarea class="form-control" id="description" 
                                                              rows="2" placeholder="請輸入訂單描述（可選）"
                                                              v-model="formData.description"></textarea>
                                                </div>
                                            </form>
                                        </div>
                                        
                                        <!-- 超商取貨 -->
                                        <div class="tab-pane fade" id="store-pickup" role="tabpanel" 
                                             aria-labelledby="store-pickup-tab">
                                            <form @submit.prevent="submitPayment">
                                                <div class="row">
                                                    <div class="col-md-6 mb-3">
                                                        <label for="pickupName" class="form-label">取貨人姓名 <span class="text-danger">*</span></label>
                                                        <input type="text" class="form-control" id="pickupName" 
                                                               placeholder="請輸入取貨人姓名" 
                                                               v-model="formData.receiverName" required>
                                                    </div>
                                                    <div class="col-md-6 mb-3">
                                                        <label for="pickupMobile" class="form-label">取貨人手機號 <span class="text-danger">*</span></label>
                                                        <input type="text" class="form-control" id="pickupMobile" 
                                                               placeholder="請輸入取貨人手機號" 
                                                               v-model="formData.receiverMobile" required>
                                                    </div>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="storeSelect" class="form-label">選擇取貨門店 <span class="text-danger">*</span></label>
                                                    <select class="form-control" id="storeSelect" v-model="formData.selectedStore" required>
                                                        <option value="">請選擇取貨門店</option>
                                                        <option v-for="store in storeList" :key="store.storeId" :value="store">
                                                            {{ store.storeName }} - {{ store.storeAddress }}
                                                        </option>
                                                    </select>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="description" class="form-label">訂單描述</label>
                                                    <textarea class="form-control" id="description" 
                                                              rows="2" placeholder="請輸入訂單描述（可選）"
                                                              v-model="formData.description"></textarea>
                                                </div>
                                                
                                                <!-- 門店資訊顯示 -->
                                                <div v-if="formData.selectedStore" class="alert alert-info">
                                                    <h6><i class="bi bi-info-circle me-2"></i>門店資訊</h6>
                                                    <p class="mb-1"><strong>門店名稱：</strong>{{ formData.selectedStore.storeName }}</p>
                                                    <p class="mb-1"><strong>門店地址：</strong>{{ formData.selectedStore.storeAddress }}</p>
                                                    <p class="mb-0"><strong>聯繫電話：</strong>{{ formData.selectedStore.storeTelephone }}</p>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-lg-4">
                        <div class="payment-summary-card">
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="card-title mb-0">
                                        <i class="bi bi-receipt me-2"></i>訂單摘要
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <div class="order-info">
                                        <div class="info-row">
                                            <span class="info-label">商品名稱：</span>
                                            <span class="info-value" v-text="formData.itemName"></span>
                                        </div>
                                        <div class="info-row">
                                            <span class="info-label">支付金額：</span>
                                            <span class="info-value amount">${CurrencyUnit} {{ formData.amount }}</span>
                                        </div>
                                        <div class="info-row">
                                            <span class="info-label">收件人：</span>
                                            <span class="info-value" v-text="formData.receiverName || '-'"></span>
                    </div>
                                        <div class="info-row">
                                            <span class="info-label">聯繫電話：</span>
                                            <span class="info-value" v-text="formData.receiverMobile || '-'"></span>
                    </div>
                                        <div class="info-row">
                                            <span class="info-label">配送方式：</span>
                                            <span class="info-value" v-text="formData.orderType === 1 ? '宅配到府' : '超商取貨'"></span>
                                        </div>
                                        <div class="info-row" v-if="formData.orderType === 1">
                                            <span class="info-label">收貨地址：</span>
                                            <span class="info-value" v-text="formData.receiverAddress || '-'"></span>
                                        </div>
                                        <div class="info-row" v-if="formData.orderType === 2 && formData.selectedStore">
                                            <span class="info-label">取貨門店：</span>
                                            <span class="info-value" v-text="formData.selectedStore.storeName"></span>
                                        </div>
                                        <div class="info-row" v-if="formData.orderType === 2 && formData.selectedStore">
                                            <span class="info-label">門店地址：</span>
                                            <span class="info-value" v-text="formData.selectedStore.storeAddress"></span>
                                        </div>
                    </div>
                    
                    <!-- 購物車商品列表 -->
                                    <div v-if="cartItems && cartItems.length > 0" class="mt-3">
                                        <h6 class="mb-3">
                                            <i class="bi bi-cart3 me-2"></i>購物車商品
                                        </h6>
                                        <div class="cart-items-list">
                                            <div class="cart-item" v-for="item in cartItems" :key="item.id">
                                                <div class="cart-item-info">
                                                    <div class="cart-item-name" v-text="item.productName"></div>
                                                    <div class="cart-item-details">
                                                        數量: {{ item.productAmount }} × ${CurrencyUnit} {{ parseFloat(item.productPrice).toFixed(2) }}
                                                    </div>
                                                </div>
                                                <div class="cart-item-price">
                                                    ${CurrencyUnit} {{ parseFloat(item.subtotal).toFixed(2) }}
                                                </div>
                                            </div>
                        </div>
                    </div>
                        
                        <!-- 立即支付按鈕 -->
                        <div class="d-grid gap-2 mt-4">
                            <button type="button" class="btn btn-primary btn-lg" @click="submitPayment" :disabled="isLoading">
                                <span v-if="!isLoading">
                                    <i class="bi bi-credit-card me-2"></i>立即支付
                                </span>
                                <span v-else>
                                    <i class="bi bi-arrow-clockwise spin me-2"></i>處理中...
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
                                <i class="bi bi-arrow-left me-2"></i>返回購物車
                </a>
                <a href="/mall" class="btn btn-outline-primary">
                                <i class="bi bi-shop me-2"></i>繼續購物
                </a>
                        </div>
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
                    receiverAddress: '',
                    orderType: 1, // 1: 宅配到府, 2: 超商取貨
                    selectedStore: null
                },
                orderId: null,
                orderNo: null,
                orderInfo: ${orderInfoJson},
                cartItems: [],
                storeList: [], // 超商門店列表
                isLoading: false,
                errorMessage: '',
                successMessage: '',
                paymentConfig: null,
                currentOrderNo: null // 當前支付訂單號
            },
            mounted() {
                console.log('Vue實例已掛載');
                this.initCartItems();
                this.loadPaymentConfig();
                this.loadStoreList();
                this.initData();
            },
            methods: {
            	initData(){
            		if(this.orderInfo && this.orderInfo.id){
                        this.formData.receiverName = this.orderInfo.receiverName;
                        this.formData.receiverMobile = this.orderInfo.receiverMobile;
                        this.formData.receiverAddress = this.orderInfo.receiverAddress;
                        this.formData.orderType = this.orderInfo.orderType;
                        this.formData.orderNo = this.orderInfo.orderNo;
                        this.formData.orderId = this.orderInfo.id;
                    }
            	},
                // 載入支付配置
                loadPaymentConfig() {
                    axios.get('/api/payment/config')
                        .then(response => {
                            if (response.data.code === 1) {
                                this.paymentConfig = response.data.data;
                            } else {
                                console.error('獲取支付配置失敗:', response.data.msg);
                            }
                        })
                        .catch(error => {
                            console.error('獲取支付配置異常:', error);
                        });
                },
                
                // 初始化購物車商品列表
                initCartItems() {
                    const cartDataStr = '${cartData!''}';
                    if (cartDataStr && cartDataStr.trim() !== '') {
                        try {
                            const cartData = JSON.parse(cartDataStr);
                            this.cartItems = cartData.items || [];
                        } catch (error) {
                            console.error('解析購物車數據失敗:', error);
                            console.error('購物車數據內容:', cartDataStr);
                        }
                    }
                },
                
                // 提交支付
                submitPayment() {
                    console.log('submitPayment方法被調用');
                    // 表單驗證
                    if (!this.validateForm()) {
                        console.log('表單驗證失敗');
                    return;
                }
                
                    // 同時顯示支付確認對話框和創建支付訂單
                    this.showPaymentConfirmDialog();
                    this.proceedToPayment();
                },
                
                // 顯示支付確認對話框
                showPaymentConfirmDialog() {
                    // 創建模態框HTML
                    const modalHtml = `
                        <div class="modal fade" id="paymentConfirmModal" tabindex="-1" aria-labelledby="paymentConfirmModalLabel" aria-hidden="true">
                            <div class="modal-dialog modal-dialog-centered">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="paymentConfirmModalLabel">
                                            <i class="bi bi-info-circle me-2"></i>支付提示
                                        </h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body text-center">
                                        <div class="mb-4">
                                            <i class="bi bi-credit-card" style="font-size: 3rem; color: #5ACFC9;"></i>
                                        </div>
                                        <h6 class="mb-3">支付頁面已在新窗口打開</h6>
                                        <p class="text-muted">請在新窗口中完成支付操作，支付完成後請點擊下方按鈕確認</p>
                                        <div class="alert alert-info">
                                            <small><i class="bi bi-lightbulb me-1"></i>提示：如果新窗口沒有自動打開，請檢查瀏覽器彈窗設置</small>
                                        </div>
                                    </div>
                                    <div class="modal-footer justify-content-center">
                                        <button type="button" class="btn btn-outline-secondary me-3" id="cancelPaymentBtn">
                                            <i class="bi bi-x-circle me-2"></i>取消支付
                                        </button>
                                        <button type="button" class="btn btn-primary" id="confirmPaidBtn">
                                            <i class="bi bi-check-circle me-2"></i>已完成支付
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    `;
                    
                    // 移除已存在的模態框
                    const existingModal = document.getElementById('paymentConfirmModal');
                    if (existingModal) {
                        existingModal.remove();
                    }
                    
                    // 添加模態框到頁面
                    document.body.insertAdjacentHTML('beforeend', modalHtml);
                    
                    // 顯示模態框
                    const modal = new bootstrap.Modal(document.getElementById('paymentConfirmModal'));
                    modal.show();
                    
                    // 綁定事件
                    document.getElementById('confirmPaidBtn').addEventListener('click', () => {
                        modal.hide();
                        this.checkPaymentStatus();
                    });
                    
                    document.getElementById('cancelPaymentBtn').addEventListener('click', () => {
                        modal.hide();
                        this.goToOrderDetail();
                    });
                    
                    // 模態框關閉時清理
                    document.getElementById('paymentConfirmModal').addEventListener('hidden.bs.modal', () => {
                        document.getElementById('paymentConfirmModal').remove();
                    });
                },
                
                // 繼續支付流程
                proceedToPayment() {
                    this.isLoading = true;
                    this.clearMessages();
                    
                    // 創建支付訂單
                    const paymentData = {
                        amount: this.formData.amount,
                        itemName: this.formData.itemName,
                        description: this.formData.description || '購買商品：' + this.formData.itemName,
                        receiverName: this.formData.receiverName,
                        receiverMobile: this.formData.receiverMobile,
                        orderType: this.formData.orderType,
                        cartData: this.cartItems
                    };
                    
                    // 如果是重新支付訂單，添加訂單ID
                    if (this.orderId) {
                        paymentData.orderId = this.orderId;
                    }
                    
                    // 根據配送方式添加相應字段
                    if (this.formData.orderType === 1) {
                        // 宅配到府
                        paymentData.receiverAddress = this.formData.receiverAddress;
                    } else {
                        // 超商取貨
                        if (this.formData.selectedStore) {
                            paymentData.cvsStoreID = this.formData.selectedStore.storeId;
                            paymentData.cvsStoreName = this.formData.selectedStore.storeName;
                            paymentData.cvsAddress = this.formData.selectedStore.storeAddress;
                            paymentData.cvsTelephone = this.formData.selectedStore.storeTelephone;
                            paymentData.cvsOutSide = this.formData.selectedStore.outSide || 0;
                        }
                    }
                    
                    axios.post('/api/payment/create', paymentData)
                    .then(response => {
                        debugger;
                        if (response.data.code === 1) {
                            // 支付訂單創建成功，在新窗口打開綠界支付頁面
                            this.currentOrderNo = response.data.data.MerchantTradeNo;
                            this.openPaymentInNewWindow(response.data.data);
                        } else {
                            this.showError(response.data.msg || '創建支付訂單失敗');
                        }
                    })
                    .catch(error => {
                        console.error('創建支付訂單失敗:', error);
                        this.showError('創建支付訂單失敗，請稍後重試');
                    })
                    .finally(() => {
                        this.isLoading = false;
                    });
                },
                
                // 表單驗證
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
                    
                    // 根據配送方式驗證相應字段
                    if (this.formData.orderType === 1) {
                        // 宅配到府，驗證地址
                        if (!this.formData.receiverAddress || this.formData.receiverAddress.trim() === '') {
                            this.showError('收件人地址不能為空');
                            return false;
                        }
                    } else {
                        // 超商取貨，驗證門店選擇
                        if (!this.formData.selectedStore) {
                            this.showError('請選擇取貨門店');
                            return false;
                        }
                    }
                    
                    return true;
                },
            
                // 在新窗口打開支付頁面
                openPaymentInNewWindow(paymentParams) {
                    // 獲取支付服務器地址
                    const serverUrl = this.paymentConfig ? this.paymentConfig.serverUrl : 'https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5';
                    
                    // 創建表單並提交到綠界支付
                    const form = document.createElement('form');
                    form.method = 'POST';
                    form.action = serverUrl;
                    form.target = '_blank'; // 在新窗口打開
                    
                    // 添加所有支付參數
                    for (const [key, value] of Object.entries(paymentParams)) {
                        const input = document.createElement('input');
                        input.type = 'hidden';
                        input.name = key;
                        input.value = value;
                        form.appendChild(input);
                    }
                    
                    // 提交表單
                    document.body.appendChild(form);
                    form.submit();
                },
                
                // 檢查支付狀態
                checkPaymentStatus() {
                    debugger;
                    if (!this.currentOrderNo) {
                        this.showError('無法獲取訂單信息，請重新支付');
                        return;
                    }
                    
                    this.checkOrderStatus(this.currentOrderNo);
                },
                
                // 跳转到订单详情页面
                goToOrderDetail() {
                    if (this.orderId) {
                        window.location.href = '/my-order/detail-page?orderId=' + this.orderId;
                    } else {
                        window.location.href = '/my-order';
                    }
                },
                
                // 檢查訂單狀態
                checkOrderStatus(orderNo) {
                    this.isLoading = true;
                    this.clearMessages();
                    
                    axios.get('/api/payment/status/' + orderNo)
                        .then(response => {
                            if (response.data.code === 1) {
                                this.orderId = response.data.data.orderId;
                                const orderStatus = response.data.data;
                                //if (orderStatus.paymentStatus === 1) {
                                    // 支付成功
                                    debugger;
                                    this.showSuccess('支付成功！正在跳轉到訂單詳情頁面...');
                                    setTimeout(() => {
                                        // 跳转到订单详情页面
                                        if (this.orderId) {
                                            window.location.href = '/my-order/detail-page?orderId=' + this.orderId;
                                        } else {
                                            window.location.href = '/my-order';
                                        }
                                    }, 2000);
                                //} else {
                                    // 支付未完成
                                //    this.showError('支付尚未完成，請完成支付後再次確認');
                                //}
                            } else {
                                this.showError(response.data.msg || '查詢訂單狀態失敗');
                            }
                        })
                        .catch(error => {
                            console.error('查詢訂單狀態失敗:', error);
                            this.showError('查詢訂單狀態失敗，請稍後重試');
                        })
                        .finally(() => {
                            this.isLoading = false;
                        });
                },
            
            // 顯示錯誤資訊
                showError(message) {
                    this.errorMessage = message;
                    this.successMessage = '';
                },
            
            // 顯示成功資訊
                showSuccess(message) {
                    this.successMessage = message;
                    this.errorMessage = '';
                },
                
                // 清除所有訊息
                clearMessages() {
                    this.errorMessage = '';
                    this.successMessage = '';
                },
                
                // 切換配送方式
                switchDeliveryType(orderType) {
                    this.formData.orderType = orderType;
                    // 清空相關字段
                    if (orderType === 1) {
                        // 宅配到府，清空門店選擇
                        this.formData.selectedStore = null;
                    } else {
                        // 超商取貨，清空地址
                        this.formData.receiverAddress = '';
                    }
                },
                
                // 載入超商門店列表
                loadStoreList() {
                    axios.get('/api/logistics/stores')
                        .then(response => {
                            if (response.data.code === 1) {
                                this.storeList = response.data.data || [];
                                console.log('門店列表載入成功:', this.storeList);
                            } else {
                                console.error('獲取門店列表失敗:', response.data.msg);
                                this.showError('獲取門店列表失敗');
                            }
                        })
                        .catch(error => {
                            console.error('獲取門店列表異常:', error);
                            this.showError('獲取門店列表異常');
                        });
                }
            }
        });
        });
    </script>