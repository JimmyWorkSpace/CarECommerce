<link href="/css/my-pages.css" rel="stylesheet">

<div class="my-page" id="app">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <h2 class="page-title">
                    <i class="bi bi-list-ul me-3"></i>
                    我的訂單
                </h2>
                
                <!-- 訂單列表 -->
                <div v-if="orders && orders.length > 0">
                    <div v-for="order in orders" :key="order.id" class="card item-card">
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
                            <div class="item-info">
                                <div class="info-row">
                                    <span class="info-label">訂單金額：</span>
                                    <span class="info-value amount">${CurrencyUnit} {{ order.totalPrice }}</span>
                                </div>
                                <div class="info-row">
                                    <span class="info-label">下單時間：</span>
                                    <span class="info-value">{{ formatDate(order.createTime) }}</span>
                                </div>
                                <div v-if="order.orderBizType !== 2" class="info-row">
                                    <span class="info-label">收件人：</span>
                                    <span class="info-value">{{ order.receiverName || '-' }}</span>
                                </div>
                                <div v-if="order.orderBizType !== 2" class="info-row">
                                    <span class="info-label">聯繫電話：</span>
                                    <span class="info-value">{{ order.receiverMobile || '-' }}</span>
                                </div>
                                <div v-if="order.orderBizType !== 2" class="info-row">
                                    <span class="info-label">收貨地址：</span>
                                    <span class="info-value">{{ order.receiverAddress || '-' }}</span>
                                </div>
                                
                                <!-- 物流狀態顯示（僅已支付的一般訂單顯示，卡券訂單不顯示） -->
                                <div v-if="order.orderStatus === 2 && order.orderBizType !== 2" class="info-row">
                                    <span class="info-label">物流狀態：</span>
                                    <span class="info-value logistics-status" :class="getLogisticsStatusClass(order)">
                                        <i class="bi bi-truck me-1"></i>{{ getLogisticsStatusText(order) }}
                                    </span>
                                </div>
                            </div>
                            
                            <!-- 訂單詳情（已棄用，現在跳轉到詳情頁面） -->
                            <div class="order-details" :id="'order-details-' + order.id" style="display: none;">
                                <h6 class="mb-3">訂單商品：</h6>
                                <div v-for="detail in getOrderDetails(order.id)" :key="detail.id" class="order-detail-item">
                                    <div class="detail-item-info">
                                        <div class="detail-item-name">{{ detail.productName }}</div>
                                        <div class="detail-item-details">
                                            數量: {{ detail.productAmount }} × ${CurrencyUnit} {{ detail.productPrice }}
                                        </div>
                                    </div>
                                    <div class="detail-item-price">
                                                ${CurrencyUnit} {{ detail.totalPrice }}
                                    </div>
                                </div>
                            </div>
                            
                            <!-- 訂單操作 -->
                            <div class="item-actions">
                                <button type="button" class="btn btn-outline-primary btn-sm" 
                                        @click="viewOrderDetail(order.id)">
                                    <i class="bi bi-eye me-1"></i>查看詳情
                                </button>
                                
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
                
                <!-- 空狀態 -->
                <div v-else class="empty-state">
                    <i class="bi bi-inbox"></i>
                    <h4>暫無訂單</h4>
                    <p>您還沒有任何訂單</p>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
        new Vue({
            el: '#app',
            data: {
                orders: ${ordersJson},
                orderDetails: {},
                isLoading: false,
                errorMessage: '',
                successMessage: '',
                statusCheckInterval: null,
                originalOrderStatuses: {}
            },
            mounted() {
                console.log('我的訂單頁面已加載');
                this.initializeOrderStatusTracking();
            },
            beforeDestroy() {
                this.stopStatusCheck();
            },
            methods: {
                // 獲取訂單狀態文本
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
                
                // 獲取訂單狀態樣式類
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
                
                // 獲取物流狀態文本
                getLogisticsStatusText(order) {
                    try {
                        // 安全地獲取屬性值
                        const statusCode = order && order.logicStatusCode ? order.logicStatusCode : null;
                        const msg = order && order.logicMsg ? order.logicMsg : null;
                        
                        // 如果有物流狀態碼，根據狀態碼顯示
                        if (statusCode && statusCode !== null && statusCode !== '') {
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
                            return statusMap[statusCode] || '未知狀態(' + statusCode + ')';
                        }
                        // 如果沒有狀態碼但有物流消息，顯示物流消息
                        else if (msg && msg !== null && msg !== '') {
                            return msg;
                        }
                        // 都沒有則顯示默認狀態
                        else {
                            return '物流信息待更新';
                        }
                    } catch (error) {
                        console.error('獲取物流狀態文本時出錯:', error);
                        return '物流信息待更新';
                    }
                },
                
                // 獲取物流狀態樣式類
                getLogisticsStatusClass(order) {
                    try {
                        // 安全地獲取屬性值
                        const statusCode = order && order.logicStatusCode ? order.logicStatusCode : null;
                        const msg = order && order.logicMsg ? order.logicMsg : null;
                        
                        // 如果有物流狀態碼，根據狀態碼設置樣式
                        if (statusCode && statusCode !== null && statusCode !== '') {
                            const classMap = {
                                '300': 'logistics-created',
                                '301': 'logistics-pending',
                                '302': 'logistics-received',
                                '303': 'logistics-shipping',
                                '304': 'logistics-picked',
                                '305': 'logistics-delivered',
                                '306': 'logistics-returning',
                                '307': 'logistics-returned',
                                '308': 'logistics-error',
                                '309': 'logistics-cancelled'
                            };
                            return classMap[statusCode] || 'logistics-unknown';
                        }
                        // 如果有物流消息，可能是錯誤信息
                        else if (msg && msg !== null && msg !== '') {
                            return 'logistics-message';
                        }
                        // 默認狀態
                        else {
                            return 'logistics-default';
                        }
                    } catch (error) {
                        console.error('獲取物流狀態樣式類時出錯:', error);
                        return 'logistics-default';
                    }
                },
                
                // 格式化日期
                formatDate(dateStr) {
                    if (!dateStr) return '-';
                    const date = new Date(dateStr);
                    return date.toLocaleString('zh-CN');
                },
                
                // 查看訂單詳情頁面
                viewOrderDetail(orderId) {
                    // 跳轉到訂單詳情頁面
                    window.location.href = '/my-order/detail-page?orderId=' + orderId;
                },
                
                // 加載訂單詳情
                loadOrderDetails(orderId) {
                    axios.get('/my-order/detail?orderId=' + orderId)
                        .then(response => {
                            if (response.data.code === 1) {
                                this.$set(this.orderDetails, orderId, response.data.data);
                            } else {
                                this.showError(response.data.msg || '獲取訂單詳情失敗');
                            }
                        })
                        .catch(error => {
                            console.error('獲取訂單詳情失敗:', error);
                            this.showError('獲取訂單詳情失敗，請稍後重試');
                        });
                },
                
                // 獲取訂單詳情
                getOrderDetails(orderId) {
                    return this.orderDetails[orderId] || [];
                },
                
                // 重新支付訂單
                repayOrder(orderId) {
                    if (confirm('確定要重新支付這個訂單嗎？')) {
                        axios.post('/my-order/repay/' + orderId)
                            .then(response => {
                                if (response.data.code === 1) {
                                    // 跳轉到支付頁面
                                    window.location.href = response.data.data;
                                } else {
                                    this.showError(response.data.msg || '重新支付失敗');
                                }
                            })
                            .catch(error => {
                                console.error('重新支付失敗:', error);
                                this.showError('重新支付失敗，請稍後重試');
                            });
                    }
                },
                
                // 取消訂單
                cancelOrder(orderId) {
                    if (confirm('確定要取消這個訂單嗎？取消後商品將放回購物車。')) {
                        axios.post('/my-order/cancel/' + orderId)
                            .then(response => {
                                if (response.data.code === 1) {
                                    this.showSuccess(response.data.msg || '訂單取消成功');
                                    // 刷新頁面
                                    setTimeout(() => {
                                        window.location.reload();
                                    }, 1500);
                                } else {
                                    this.showError(response.data.msg || '取消訂單失敗');
                                }
                            })
                            .catch(error => {
                                console.error('取消訂單失敗:', error);
                                this.showError('取消訂單失敗，請稍後重試');
                            });
                    }
                },
                
                // 顯示錯誤信息
                showError(message) {
                    this.errorMessage = message;
                    this.successMessage = '';
                    setTimeout(() => {
                        this.errorMessage = '';
                    }, 5000);
                },
                
                // 顯示成功信息
                showSuccess(message) {
                    this.successMessage = message;
                    this.errorMessage = '';
                    setTimeout(() => {
                        this.successMessage = '';
                    }, 5000);
                },
                
                // 初始化訂單狀態跟蹤
                initializeOrderStatusTracking() {
                    this.originalOrderStatuses = {};
                    this.orders.forEach(order => {
                        this.originalOrderStatuses[order.id] = order.orderStatus;
                    });
                },
                
            }
        });
</script>
