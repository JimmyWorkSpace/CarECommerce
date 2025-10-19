<link href="/css/my-pages.css" rel="stylesheet">

<div class="my-page" id="app">
    <div class="container">
        <div class="row">
            <div class="col-12">
                
                <!-- 订单详情 -->
                <div v-if="order" class="card item-card">
                    <div class="card-header">
                        <div class="d-flex justify-content-between align-items-center">
                            <h5 class="card-title mb-0">
                                <i class="bi bi-receipt me-2"></i>訂單號：{{ order.orderNo }}
                            </h5>
                            <span :class="getStatusClass(order.orderStatus)" class="status-badge">
                                {{ getStatusText(order.orderStatus) }}
                            </span>
                        </div>
                    </div>
                    <div class="card-body">
                        <!-- 订单基本信息 -->
                        <div class="item-info">
                            <div class="info-row">
                                <span class="info-label">訂單金額：</span>
                                <span class="info-value amount">${CurrencyUnit} {{ order.totalPrice }}</span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">下單時間：</span>
                                <span class="info-value">{{ formatDate(order.createTime) }}</span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">訂單狀態：</span>
                                <span class="info-value">{{ getStatusText(order.orderStatus) }}</span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">配送方式：</span>
                                <span class="info-value">{{ getOrderTypeText(order.orderType) }}</span>
                            </div>
                        </div>
                        
                        <!-- 收件人信息 -->
                        <div class="mt-4">
                            <h6 class="mb-3 text-start fw-bold text-primary border-bottom pb-2">
                                <i class="bi bi-person-fill me-2"></i>收件人資訊
                            </h6>
                            <div class="item-info text-start bg-light rounded p-3">
                                <div class="info-row mb-2">
                                    <span class="info-label fw-semibold text-muted">
                                        <i class="bi bi-person me-1"></i>收件人：
                                    </span>
                                    <span class="info-value fw-bold">{{ order.receiverName || '-' }}</span>
                                </div>
                                <div class="info-row mb-2">
                                    <span class="info-label fw-semibold text-muted">
                                        <i class="bi bi-telephone me-1"></i>聯繫電話：
                                    </span>
                                    <span class="info-value fw-bold">{{ order.receiverMobile || '-' }}</span>
                                </div>
                                <div class="info-row mb-2" v-if="order.orderType === 1">
                                    <span class="info-label fw-semibold text-muted">
                                        <i class="bi bi-geo-alt me-1"></i>收件地址：
                                    </span>
                                    <span class="info-value fw-bold">{{ getFullReceiverAddress() }}</span>
                                </div>
                                <div class="info-row mb-2" v-if="order.orderType === 2">
                                    <span class="info-label fw-semibold text-muted">
                                        <i class="bi bi-shop me-1"></i>取貨門店：
                                    </span>
                                    <span class="info-value fw-bold">{{ order.cvsStoreName || '-' }}</span>
                                </div>
                                <div class="info-row mb-2" v-if="order.orderType === 2">
                                    <span class="info-label fw-semibold text-muted">
                                        <i class="bi bi-geo-alt me-1"></i>門店地址：
                                    </span>
                                    <span class="info-value fw-bold">{{ order.cvsAddress || '-' }}</span>
                                </div>
                                <div class="info-row mb-2" v-if="order.orderType === 2">
                                    <span class="info-label fw-semibold text-muted">
                                        <i class="bi bi-telephone me-1"></i>門店電話：
                                    </span>
                                    <span class="info-value fw-bold">{{ order.cvsTelephone || '-' }}</span>
                                </div>
                            </div>
                        </div>
                        
                        <!-- 订单商品详情 -->
                        <div class="mt-4">
                            <h6 class="mb-3 text-start fw-bold text-primary border-bottom pb-2">
                                <i class="bi bi-box-seam-fill me-2"></i>商品詳情
                            </h6>
                            <div class="order-details text-start">
                                <div v-for="detail in orderDetails" :key="detail.id" class="order-detail-item bg-light rounded p-3 mb-3 border">
                                    <div class="row align-items-center">
                                        <div class="col-md-8">
                                            <div class="detail-item-info">
                                                <div class="detail-item-name fw-bold text-dark mb-2">{{ detail.productName }}</div>
                                                <div class="detail-item-details text-muted">
                                                    <i class="bi bi-hash me-1"></i>數量: <span class="fw-semibold">{{ detail.productAmount }}</span>
                                                    <i class="bi bi-x me-2"></i>
                                                    <i class="bi bi-currency-dollar me-1"></i>單價: <span class="fw-semibold">{{ detail.productPrice }}</span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-4 text-end">
                                            <div class="detail-item-price">
                                                <span class="h5 text-success fw-bold">
                                                    <i class="bi bi-currency-dollar me-1"></i>{{ detail.totalPrice }}
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- 物流信息 -->
                        <div class="mt-4" v-if="logisticsInfo.logisticsStatus">
                            <h6 class="mb-3 text-start fw-bold text-primary border-bottom pb-2">
                                <i class="bi bi-truck-fill me-2"></i>物流資訊
                                <button type="button" class="btn btn-outline-primary btn-sm ms-2" 
                                        @click="loadLogisticsInfo" :disabled="isLoadingLogistics">
                                    <i class="bi bi-arrow-clockwise me-1"></i>刷新
                                </button>
                                <span class="badge bg-secondary ms-2" v-if="isLoadingLogistics">加载中...</span>
                            </h6>
                            
                            <!-- 物流详细信息 -->
                            <div class="item-info bg-light rounded p-3">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="info-row mb-2" v-if="isConvenienceStore(logisticsInfo.logisticsType)">
                                            <span class="info-label fw-semibold text-muted">
                                                <i class="bi bi-truck me-1"></i>物流單號：
                                            </span>
                                            <span class="info-value fw-bold text-primary">{{ logisticsInfo.shipmentNo || order.logicNumber || '-' }}</span>
                                        </div>
                                        <div class="info-row mb-2" v-if="isHomeDelivery(logisticsInfo.logisticsType)">
                                            <span class="info-label fw-semibold text-muted">
                                                <i class="bi bi-box-seam me-1"></i>托運單號：
                                            </span>
                                            <span class="info-value fw-bold">{{ logisticsInfo.bookingNote || '-' }}</span>
                                        </div>
                                        <div class="info-row mb-2">
                                            <span class="info-label fw-semibold text-muted">
                                                <i class="bi bi-truck me-1"></i>物流類型：
                                            </span>
                                            <span class="info-value fw-bold">{{ getLogisticsTypeText(logisticsInfo.logisticsType) }}</span>
                                        </div>
                                        <div class="info-row mb-2" v-if="isConvenienceStore(logisticsInfo.logisticsType) && logisticsInfo.cvspaymentNo">
                                            <span class="info-label fw-semibold text-muted">
                                                <i class="bi bi-shop me-1"></i>寄貨編號：
                                            </span>
                                            <span class="info-value fw-bold text-primary">{{ logisticsInfo.cvspaymentNo }}</span>
                                        </div>
                                        <div class="info-row mb-2" v-if="isConvenienceStore(logisticsInfo.logisticsType) && logisticsInfo.cvsvalidationNo">
                                            <span class="info-label fw-semibold text-muted">
                                                <i class="bi bi-key me-1"></i>驗證碼：
                                            </span>
                                            <span class="info-value fw-bold text-warning">{{ logisticsInfo.cvsvalidationNo }}</span>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="info-row mb-2">
                                            <span class="info-label fw-semibold text-muted">
                                                <i class="bi bi-check-circle me-1"></i>物流狀態：
                                            </span>
                                            <span class="info-value fw-bold" :class="getLogisticsStatusClass(logisticsInfo.logisticsStatus)">
                                                {{ getLogisticsStatusText(logisticsInfo.logisticsStatus) }}
                                            </span>
                                        </div>
                                        <div class="info-row mb-2">
                                            <span class="info-label fw-semibold text-muted">
                                                <i class="bi bi-calendar-event me-1"></i>交易日期：
                                            </span>
                                            <span class="info-value fw-bold">{{ logisticsInfo.tradeDate || '-' }}</span>
                                        </div>
                                        <div class="info-row mb-2">
                                            <span class="info-label fw-semibold text-muted">
                                                <i class="bi bi-person me-1"></i>寄件人：
                                            </span>
                                            <span class="info-value fw-bold">{{ logisticsInfo.senderName || '-' }}</span>
                                        </div>
                                        <div class="info-row mb-2">
                                            <span class="info-label fw-semibold text-muted">
                                                <i class="bi bi-telephone me-1"></i>寄件人電話：
                                            </span>
                                            <span class="info-value fw-bold">{{ logisticsInfo.senderPhone || '-' }}</span>
                                        </div>
                                        <div class="info-row mb-2" v-if="logisticsInfo.senderCellPhone">
                                            <span class="info-label fw-semibold text-muted">
                                                <i class="bi bi-phone me-1"></i>寄件人手機：
                                            </span>
                                            <span class="info-value fw-bold">{{ logisticsInfo.senderCellPhone }}</span>
                                        </div>
                                        <div class="info-row mb-2" v-if="logisticsInfo.shipChargeDate">
                                            <span class="info-label fw-semibold text-muted">
                                                <i class="bi bi-calendar-check me-1"></i>運費扣款日期：
                                            </span>
                                            <span class="info-value fw-bold">{{ logisticsInfo.shipChargeDate }}</span>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- 商品信息 -->
                                <div class="row mt-3" v-if="logisticsInfo.goodsName">
                                    <div class="col-12">
                                        <div class="border-top pt-3">
                                            <h6 class="fw-bold text-muted mb-2">
                                                <i class="bi bi-box-seam me-1"></i>商品信息
                                            </h6>
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="info-row mb-2">
                                                        <span class="info-label fw-semibold text-muted">商品名稱：</span>
                                                        <span class="info-value fw-bold">{{ logisticsInfo.goodsName || '-' }}</span>
                                                    </div>
                                                    <div class="info-row mb-2">
                                                        <span class="info-label fw-semibold text-muted">商品金額：</span>
                                                        <span class="info-value fw-bold text-success">${CurrencyUnit} {{ logisticsInfo.goodsAmount || '-' }}</span>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="info-row mb-2">
                                                        <span class="info-label fw-semibold text-muted">處理費：</span>
                                                        <span class="info-value fw-bold">${CurrencyUnit} {{ logisticsInfo.handlingCharge || '-' }}</span>
                                                    </div>
                                                    <div class="info-row mb-2">
                                                        <span class="info-label fw-semibold text-muted">商品重量：</span>
                                                        <span class="info-value fw-bold">{{ logisticsInfo.goodsWeight || '-' }}</span>
                                                    </div>
                                                    <div class="info-row mb-2" v-if="logisticsInfo.actualWeight">
                                                        <span class="info-label fw-semibold text-muted">實際重量：</span>
                                                        <span class="info-value fw-bold">{{ logisticsInfo.actualWeight }} 公斤</span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- 代收信息 -->
                                <div class="row mt-3" v-if="logisticsInfo.collectionAmount && logisticsInfo.collectionAmount !== '0'">
                                    <div class="col-12">
                                        <div class="border-top pt-3">
                                            <h6 class="fw-bold text-muted mb-2">
                                                <i class="bi bi-cash-coin me-1"></i>代收信息
                                            </h6>
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="info-row mb-2">
                                                        <span class="info-label fw-semibold text-muted">代收金額：</span>
                                                        <span class="info-value fw-bold text-success">${CurrencyUnit} {{ logisticsInfo.collectionAmount }}</span>
                                                    </div>
                                                    <div class="info-row mb-2">
                                                        <span class="info-label fw-semibold text-muted">代收手續費：</span>
                                                        <span class="info-value fw-bold">${CurrencyUnit} {{ logisticsInfo.collectionChargeFee || '-' }}</span>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="info-row mb-2">
                                                        <span class="info-label fw-semibold text-muted">撥款金額：</span>
                                                        <span class="info-value fw-bold text-primary">${CurrencyUnit} {{ logisticsInfo.collectionAllocateAmount || '-' }}</span>
                                                    </div>
                                                    <div class="info-row mb-2" v-if="logisticsInfo.collectionAllocateDate">
                                                        <span class="info-label fw-semibold text-muted">撥款日期：</span>
                                                        <span class="info-value fw-bold">{{ logisticsInfo.collectionAllocateDate }}</span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- 简单物流信息（当没有详细物流信息时显示） -->
                            <div class="item-info bg-light rounded p-3" v-else-if="order.logicNumber && !logisticsInfo">
                                <div class="info-row">
                                    <span class="info-label fw-semibold text-muted">
                                        <i class="bi bi-truck me-1"></i>物流單號：
                                    </span>
                                    <span class="info-value fw-bold text-primary">{{ order.logicNumber }}</span>
                                </div>
                                <div class="info-row mt-2" v-if="order.logicStatusCode">
                                    <span class="info-label fw-semibold text-muted">
                                        <i class="bi bi-check-circle me-1"></i>物流狀態：
                                    </span>
                                    <span class="info-value fw-bold" :class="getLogisticsStatusClass(order.logicStatusCode)">
                                        {{ getLogisticsStatusText(order.logicStatusCode) }}
                                    </span>
                                </div>
                            </div>
                            
                            <!-- 无物流信息时的提示 -->
                            <div class="alert alert-info" v-if="!order.logicNumber && order.orderStatus >= 5">
                                <i class="bi bi-info-circle me-2"></i>
                                订单已发货，但暂无物流信息，请稍后刷新或联系客服。
                            </div>
                        </div>
                        
                        <!-- 绿界支付信息 -->
                        <div class="mt-4">
                            <h6 class="mb-3 text-start fw-bold text-primary border-bottom pb-2">
                                <i class="bi bi-credit-card-fill me-2"></i>綠界支付資訊
                                <button type="button" class="btn btn-outline-primary btn-sm ms-2" 
                                        @click="loadECPayInfo" :disabled="isLoadingECPay">
                                    <i class="bi bi-arrow-clockwise me-1"></i>刷新
                                </button>
                                <span class="badge bg-secondary ms-2" v-if="isLoadingECPay">加载中...</span>
                            </h6>
                            
                            <!-- 调试信息 -->
                            <div class="item-info bg-light rounded p-3" v-if="ecpayInfo">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="info-row mb-2">
                                            <span class="info-label fw-semibold text-muted">
                                                <i class="bi bi-receipt me-1"></i>特店交易編號：
                                            </span>
                                            <span class="info-value fw-bold">{{ ecpayInfo.MerchantTradeNo || '-' }}</span>
                                        </div>
                                        <div class="info-row mb-2">
                                            <span class="info-label fw-semibold text-muted">
                                                <i class="bi bi-currency-dollar me-1"></i>交易金額：
                                            </span>
                                            <span class="info-value fw-bold text-success">${CurrencyUnit} {{ ecpayInfo.TradeAmt || '-' }}</span>
                                        </div>
                                        <div class="info-row mb-2">
                                            <span class="info-label fw-semibold text-muted">
                                                <i class="bi bi-calendar-check me-1"></i>付款時間：
                                            </span>
                                            <span class="info-value fw-bold">{{ ecpayInfo.PaymentDate || '-' }}</span>
                                        </div>
                                        <div class="info-row mb-2">
                                            <span class="info-label fw-semibold text-muted">
                                                <i class="bi bi-credit-card me-1"></i>付款方式：
                                            </span>
                                            <span class="info-value fw-bold">{{ ecpayInfo.PaymentType || '-' }}</span>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="info-row mb-2">
                                            <span class="info-label fw-semibold text-muted">
                                                <i class="bi bi-check-circle me-1"></i>交易狀態：
                                            </span>
                                            <span class="info-value fw-bold" :class="getECPayStatusClass(ecpayInfo.TradeStatus)">
                                                {{ getECPayStatusText(ecpayInfo.TradeStatus) }}
                                            </span>
                                        </div>
                                        <div class="info-row mb-2">
                                            <span class="info-label fw-semibold text-muted">
                                                <i class="bi bi-percent me-1"></i>手續費合計：
                                            </span>
                                            <span class="info-value fw-bold">${CurrencyUnit} {{ ecpayInfo.HandlingCharge }}</span>
                                        </div>
                                        <div class="info-row mb-2">
                                            <span class="info-label fw-semibold text-muted">
                                                <i class="bi bi-calculator me-1"></i>交易手續費：
                                            </span>
                                            <span class="info-value fw-bold">${CurrencyUnit} {{ ecpayInfo.PaymentTypeChargeFee }}</span>
                                        </div>
                                        <div class="info-row mb-2">
                                            <span class="info-label fw-semibold text-muted">
                                                <i class="bi bi-calendar-event me-1"></i>訂單成立時間：
                                            </span>
                                            <span class="info-value fw-bold">{{ ecpayInfo.TradeDate || '-' }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- 无数据时的提示 -->
                            <div class="alert alert-warning" v-if="!ecpayInfo && !isLoadingECPay">
                                <i class="bi bi-exclamation-triangle me-2"></i>
                                暂无绿界支付信息，请点击刷新按钮获取最新信息。
                            </div>
                        </div>
                        
                        <!-- 订单操作 -->
                        <div class="mt-4">
                            <div class="item-actions">
                                <button v-if="order.orderStatus === 0" type="button" 
                                        class="btn btn-primary btn-sm" 
                                        @click="repayOrder(order.id)">
                                    <i class="bi bi-credit-card me-1"></i>立即支付
                                </button>
                                
                                <button v-if="order.orderStatus === 0" type="button" 
                                        class="btn btn-outline-danger btn-sm" 
                                        @click="cancelOrder(order.id)">
                                    <i class="bi bi-x-circle me-1"></i>取消訂單
                                </button>
                                
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- 加载状态 -->
                <div v-else-if="isLoading" class="text-center py-5">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">加载中...</span>
                    </div>
                    <p class="mt-3">正在加载订单详情...</p>
                </div>
                
                <!-- 错误状态 -->
                <div v-else class="empty-state">
                    <i class="bi bi-exclamation-triangle"></i>
                    <h4>订单不存在</h4>
                    <p>您访问的订单不存在或已被删除</p>
                    <a href="/my-order" class="btn btn-primary">返回订单列表</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    new Vue({
        el: '#app',
        data: {
            order: null,
            orderDetails: [],
            ecpayInfo: {},
            logisticsInfo: {},
            isLoading: true,
            isLoadingECPay: false,
            isLoadingLogistics: false,
            errorMessage: '',
            successMessage: '',
            statusCheckInterval: null,
            originalOrderStatus: null
        },
        mounted() {
            console.log('订单详情页面已加载');
            this.loadOrderDetail();
        },
        beforeDestroy() {
            this.stopStatusCheck();
        },
        methods: {
            // 获取订单状态文本
            getStatusText(status) {
                const statusMap = {
                    0: '未支付',
                    1: '支付中',
                    2: '已支付',
                    3: '已取消',
                    4: '支付失敗',
                    5: '已發貨',
                    6: '已完成',
                    7: '退貨中',
                    8: '已退貨'
                };
                return statusMap[status] || '未知狀態';
            },
            
            // 获取订单状态样式类
            getStatusClass(status) {
                const classMap = {
                    0: 'status-unpaid',
                    1: 'status-unpaid',
                    2: 'status-paid',
                    3: 'status-cancelled',
                    4: 'status-cancelled',
                    5: 'status-shipped',
                    6: 'status-completed',
                    7: 'status-unpaid',
                    8: 'status-cancelled'
                };
                return classMap[status] || 'status-unpaid';
            },
            
            // 获取订单类型文本
            getOrderTypeText(orderType) {
                const typeMap = {
                    1: '宅配到府',
                    2: '超商取貨'
                };
                return typeMap[orderType] || '未知類型';
            },
            
            // 格式化日期
            formatDate(dateStr) {
                if (!dateStr) return '-';
                const date = new Date(dateStr);
                return date.toLocaleString('zh-CN');
            },
            
            // 加载订单详情
            loadOrderDetail() {
                this.isLoading = true;
                this.clearMessages();
                
                // 从URL获取订单ID
                const urlParams = new URLSearchParams(window.location.search);
                const orderId = urlParams.get('orderId');
                
                if (!orderId) {
                    this.showError('訂單ID不能為空');
                    this.isLoading = false;
                    return;
                }
                
                // 加载订单基本信息
                axios.get('/my-order/order-info?orderId=' + orderId)
                    .then(response => {
                        if (response.data.code === 1) {
                            this.order = response.data.data;
                            this.originalOrderStatus = this.order.orderStatus;
                            this.loadOrderDetails(orderId);
                            this.loadECPayInfo(); // 加载绿界支付信息
                            this.loadLogisticsInfo(); // 加载物流信息
                            // 重新启动状态检查
                            //this.stopStatusCheck();
                            //this.startStatusCheck();
                        } else {
                            this.showError(response.data.msg || '獲取訂單資訊失敗');
                        }
                    })
                    .catch(error => {
                        console.error('获取订单信息失败:', error);
                        this.showError('獲取訂單資訊失敗，請稍後重試');
                    })
                    .finally(() => {
                        this.isLoading = false;
                    });
            },
            
            // 加载订单商品详情
            loadOrderDetails(orderId) {
                axios.get('/my-order/detail?orderId=' + orderId)
                    .then(response => {
                        if (response.data.code === 1) {
                            this.orderDetails = response.data.data || [];
                        } else {
                            console.error('獲取訂單詳情失敗:', response.data.msg);
                        }
                    })
                    .catch(error => {
                        console.error('獲取訂單詳情失敗:', error);
                    });
            },
            
            // 加载绿界支付信息
            loadECPayInfo() {
                const urlParams = new URLSearchParams(window.location.search);
                const orderId = urlParams.get('orderId');
                
                if (!orderId) {
                    console.warn('订单ID为空，无法加载绿界支付信息');
                    return;
                }
                
                console.log('开始加载绿界支付信息，订单ID:', orderId);
                this.isLoadingECPay = true;
                this.ecpayInfo = null; // 清空之前的数据
                axios.get('/my-order/ecpay-info?orderId=' + orderId)
                    .then(response => {
                        console.log('绿界支付信息API响应:', response.data);
                        if (response.data && response.data.code === 1) {
                            this.ecpayInfo = response.data.data;
                            console.log('绿界支付信息设置成功:', this.ecpayInfo);
                            console.log('ecpayInfo类型:', typeof this.ecpayInfo);
                            console.log('ecpayInfo是否为null:', this.ecpayInfo === null);
                            console.log('ecpayInfo是否为undefined:', this.ecpayInfo === undefined);
                        } else {
                            console.warn('获取绿界支付信息失败:', response.data ? response.data.msg : '未知错误');
                            this.ecpayInfo = null;
                        }
                    })
                    .catch(error => {
                        console.error('获取绿界支付信息失败:', error);
                        this.ecpayInfo = null;
                    })
                    .finally(() => {
                        this.isLoadingECPay = false;
                        console.log('绿界支付信息加载完成，最终状态:', this.ecpayInfo);
                    });
            },
            
            // 加载物流信息
            loadLogisticsInfo() {
                let _this = this;
                if (!this.order || !this.order.orderNo) {
                    console.warn('订单号为空，无法加载物流信息');
                    return;
                }
                
                console.log('开始加载物流信息，订单号:', this.order.orderNo);
                this.isLoadingLogistics = true;
                this.logisticsInfo = {}; // 清空之前的数据
                
                axios.get('/api/logistics/query?orderNo=' + this.order.orderNo)
                    .then(response => {
                        console.log('物流信息API响应:', response.data);
                        if (response.data && response.data.code === 1) {
                            _this.logisticsInfo = response.data.data;
                            console.log('物流信息设置成功:', _this.logisticsInfo);
                        } else {
                            console.warn('获取物流信息失败:', response.data ? response.data.msg : '未知错误');
                            _this.logisticsInfo = {};
                        }
                    })
                    .catch(error => {
                        console.error('获取物流信息失败:', error);
                        this.logisticsInfo = {};
                    })
                    .finally(() => {
                        this.isLoadingLogistics = false;
                        console.log('物流信息加载完成，最终状态:', this.logisticsInfo);
                    });
            },
            
            // 获取物流状态文本
            getLogisticsStatusText(status) {
                const statusMap = {
                    '300': '訂單建立',
                    '301': '待收件',
                    '302': '已收件',
                    '303': '配送中',
                    '304': '已取件',
                    '305': '已送達',
                    '306': '退貨中',
                    '307': '已退貨',
                    '308': '配送異常',
                    '309': '已取消'
                };
                return statusMap[status] || '未知狀態';
            },
            
            // 获取物流状态样式类
            getLogisticsStatusClass(status) {
                const classMap = {
                    '300': 'text-info',
                    '301': 'text-warning',
                    '302': 'text-primary',
                    '303': 'text-primary',
                    '304': 'text-success',
                    '305': 'text-success',
                    '306': 'text-warning',
                    '307': 'text-danger',
                    '308': 'text-danger',
                    '309': 'text-danger'
                };
                return classMap[status] || 'text-muted';
            },
            
            // 获取物流类型文本
            getLogisticsTypeText(type) {
                const typeMap = {
                    // 宅配类型
                    'HOME_TCAT': '宅配-黑貓宅急便',
                    'HOME_ECAN': '宅配-宅配通',
                    'HOME_FAMI': '宅配-全家宅配通',
                    'HOME_POST': '宅配-中華郵政',
                    // 超商类型
                    'CVS_FAMI': '超商-全家',
                    'CVS_OK': '超商-OK超商',
                    'CVS_HILIFE': '超商-萊爾富',
                    'CVS_IBON': '超商-7-11',
                    'CVS_UNIMART': '超商-統一超商',
                    // 其他类型
                    'HOME': '宅配',
                    'CVS': '超商'
                };
                return typeMap[type] || type || '未知類型';
            },
            
            // 判断是否为宅配类型
            isHomeDelivery(type) {
                if (!type) return false;
                return type.startsWith('HOME') || type === 'HOME';
            },
            
            // 判断是否为超商类型
            isConvenienceStore(type) {
                if (!type) return false;
                return type.startsWith('CVS') || type === 'CVS';
            },
            
            // 获取绿界支付状态文本
            getECPayStatusText(status) {
                const statusMap = {
                    '0': '未付款',
                    '1': '已付款',
                    '10200095': '交易失敗',
                    '10200163': '申請失敗'
                };
                return statusMap[status] || '未知狀態';
            },
            
            // 获取绿界支付状态样式类
            getECPayStatusClass(status) {
                const classMap = {
                    '0': 'text-warning',
                    '1': 'text-success',
                    '10200095': 'text-danger',
                    '10200163': 'text-danger'
                };
                return classMap[status] || 'text-muted';
            },
            
            // 重新支付订单
            repayOrder(orderId) {
                if (confirm('確定要重新支付這個訂單嗎？')) {
                    axios.post('/my-order/repay/' + orderId)
                        .then(response => {
                            if (response.data.code === 1) {
                                // 跳转到支付页面
                                window.location.href = response.data.data;
                            } else {
                                this.showError(response.data.msg || '重新支付失敗');
                            }
                        })
                        .catch(error => {
                            console.error('重新支付失败:', error);
                            this.showError('重新支付失敗，請稍後重試');
                        });
                }
            },
            
            // 取消订单
            cancelOrder(orderId) {
                if (confirm('確定要取消這個訂單嗎？取消後商品將放回購物車。')) {
                    axios.post('/my-order/cancel/' + orderId)
                        .then(response => {
                            if (response.data.code === 1) {
                                this.showSuccess(response.data.msg || '訂單取消成功');
                                // 刷新订单状态
                                setTimeout(() => {
                                    this.loadOrderDetail();
                                }, 1500);
                            } else {
                                this.showError(response.data.msg || '取消訂單失敗');
                            }
                        })
                        .catch(error => {
                            console.error('取消订单失败:', error);
                            this.showError('取消訂單失敗，請稍後重試');
                        });
                }
            },
            
            // 刷新订单状态
            refreshOrder() {
                this.stopStatusCheck();
                this.loadOrderDetail();
                this.showSuccess('訂單狀態已刷新');
            },
            
            // 开始状态检查
            startStatusCheck() {
                // 只有未支付或支付中的订单才需要检查状态
                if (this.order && (this.order.orderStatus === 0 || this.order.orderStatus === 1)) {
                    console.log('開始定時檢查訂單狀態');
                    this.statusCheckInterval = setInterval(() => {
                        this.checkOrderStatus();
                    }, 20000); // 每20秒检查一次
                }
            },
            
            // 停止状态检查
            stopStatusCheck() {
                if (this.statusCheckInterval) {
                    clearInterval(this.statusCheckInterval);
                    this.statusCheckInterval = null;
                    console.log('停止訂單狀態檢查');
                }
            },
            
            // 检查订单状态
            checkOrderStatus() {
                if (!this.order || !this.order.orderNo) {
                    console.log('訂單資訊不完整，無法檢查狀態');
                    return;
                }
                
                console.log('從綠界API檢查訂單狀態:', this.order.orderNo);
                
                // 从绿界API查询订单状态
                axios.post('/ecpay/refresh-status/' + this.order.orderNo)
                .then(response => {
                    if (response.data && response.data.code === 200) {
                        const result = response.data.data;
                        const newOrderInfo = result.orderInfo;
                        const updated = result.updated;
                        
                        console.log('從綠界API獲取到最新訂單狀態:', newOrderInfo ? newOrderInfo.orderStatus : 'unknown');
                        console.log('訂單狀態是否更新:', updated);
                        
                        if (newOrderInfo) {
                            // 如果订单状态发生变化，更新页面数据
                            if (newOrderInfo.orderStatus !== this.originalOrderStatus) {
                                console.log('訂單狀態發生變化，從', this.originalOrderStatus, '變為', newOrderInfo.orderStatus);
                                this.order = newOrderInfo;
                                this.originalOrderStatus = newOrderInfo.orderStatus;
                                
                                // 如果订单已支付、已取消或已失败，停止状态检查
                                if (newOrderInfo.orderStatus === 2) {
                                    this.stopStatusCheck();
                                    this.showSuccess('訂單狀態已更新為已支付！');
                                } else if (newOrderInfo.orderStatus === 3 || newOrderInfo.orderStatus === 4) {
                                    this.stopStatusCheck();
                                    this.showSuccess('訂單狀態已更新！');
                                }
                            }
                        }
                    } else {
                        console.error('從綠界API查詢訂單狀態失敗:', response.data ? response.data.msg : '未知錯誤');
                    }
                }).catch(error => {
                    console.error('從綠界API檢查訂單狀態失敗:', error);
                });
            },
            
            // 显示错误信息
            showError(message) {
                this.errorMessage = message;
                this.successMessage = '';
                setTimeout(() => {
                    this.errorMessage = '';
                }, 5000);
            },
            
            // 显示成功信息
            showSuccess(message) {
                this.successMessage = message;
                this.errorMessage = '';
                setTimeout(() => {
                    this.successMessage = '';
                }, 5000);
            },
            
            // 清除所有消息
            clearMessages() {
                this.errorMessage = '';
                this.successMessage = '';
            },
            
            // 获取完整的收件人地址
            getFullReceiverAddress() {
                if (!this.order) return '-';
                
                let address = '';
                if (this.order.receiverCity) {
                    address += this.order.receiverCity;
                }
                if (this.order.receiverDistrict) {
                    address += this.order.receiverDistrict;
                }
                if (this.order.receiverAddress) {
                    address += this.order.receiverAddress;
                }
                return address || '-';
            }
        }
    });
</script>

