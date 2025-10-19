<link href="/css/my-pages.css" rel="stylesheet">

<div class="my-page" id="app">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <h2 class="page-title">
                    <i class="bi bi-list-ul me-3"></i>
                    我的订单
                </h2>
                
                <!-- 订单列表 -->
                <div v-if="orders && orders.length > 0">
                    <div v-for="order in orders" :key="order.id" class="card item-card">
                        <div class="card-header">
                            <div class="d-flex justify-content-between align-items-center">
                                <h5 class="card-title mb-0">
                                    <i class="bi bi-receipt me-2"></i>订单号：{{ order.orderNo }}
                                </h5>
                                <span :class="getStatusClass(order.orderStatus)" class="status-badge">
                                    {{ getStatusText(order.orderStatus) }}
                                </span>
                            </div>
                        </div>
                        <div class="card-body">
                            <div class="item-info">
                                <div class="info-row">
                                    <span class="info-label">订单金额：</span>
                                    <span class="info-value amount">${CurrencyUnit} {{ order.totalPrice }}</span>
                                </div>
                                <div class="info-row">
                                    <span class="info-label">下单时间：</span>
                                    <span class="info-value">{{ formatDate(order.createTime) }}</span>
                                </div>
                                <div class="info-row">
                                    <span class="info-label">收件人：</span>
                                    <span class="info-value">{{ order.receiverName || '-' }}</span>
                                </div>
                                <div class="info-row">
                                    <span class="info-label">联系电话：</span>
                                    <span class="info-value">{{ order.receiverMobile || '-' }}</span>
                                </div>
                                <div class="info-row">
                                    <span class="info-label">收货地址：</span>
                                    <span class="info-value">{{ order.receiverAddress || '-' }}</span>
                                </div>
                                
                                <!-- 物流状态显示（仅已支付订单显示） -->
                                <div v-if="order.orderStatus === 2" class="info-row">
                                    <span class="info-label">物流状态：</span>
                                    <span class="info-value logistics-status" :class="getLogisticsStatusClass(order)">
                                        <i class="bi bi-truck me-1"></i>{{ getLogisticsStatusText(order) }}
                                    </span>
                                </div>
                            </div>
                            
                            <!-- 订单详情（已弃用，现在跳转到详情页面） -->
                            <div class="order-details" :id="'order-details-' + order.id" style="display: none;">
                                <h6 class="mb-3">订单商品：</h6>
                                <div v-for="detail in getOrderDetails(order.id)" :key="detail.id" class="order-detail-item">
                                    <div class="detail-item-info">
                                        <div class="detail-item-name">{{ detail.productName }}</div>
                                        <div class="detail-item-details">
                                            数量: {{ detail.productAmount }} × ${CurrencyUnit} {{ detail.productPrice }}
                                        </div>
                                    </div>
                                    <div class="detail-item-price">
                                                ${CurrencyUnit} {{ detail.totalPrice }}
                                    </div>
                                </div>
                            </div>
                            
                            <!-- 订单操作 -->
                            <div class="item-actions">
                                <button type="button" class="btn btn-outline-primary btn-sm" 
                                        @click="viewOrderDetail(order.id)">
                                    <i class="bi bi-eye me-1"></i>查看详情
                                </button>
                                
                                <button v-if="order.orderStatus === 0" type="button" 
                                        class="btn btn-primary btn-sm" 
                                        @click="repayOrder(order.id)">
                                    <i class="bi bi-credit-card me-1"></i>立即支付
                                </button>
                                
                                <button v-if="order.orderStatus === 0" type="button" 
                                        class="btn btn-outline-danger btn-sm" 
                                        @click="cancelOrder(order.id)">
                                    <i class="bi bi-x-circle me-1"></i>取消订单
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- 空状态 -->
                <div v-else class="empty-state">
                    <i class="bi bi-inbox"></i>
                    <h4>暂无订单</h4>
                    <p>您还没有任何订单</p>
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
                console.log('我的订单页面已加载');
                this.initializeOrderStatusTracking();
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
                        4: '支付失败',
                        5: '已发货',
                        6: '已完成',
                        7: '退货中',
                        8: '已退货'
                    };
                    return statusMap[status] || '未知状态';
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
                
                // 获取物流状态文本
                getLogisticsStatusText(order) {
                    try {
                        // 安全地获取属性值
                        const statusCode = order && order.logicStatusCode ? order.logicStatusCode : null;
                        const msg = order && order.logicMsg ? order.logicMsg : null;
                        
                        // 如果有物流状态码，根据状态码显示
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
                            return statusMap[statusCode] || '未知状态(' + statusCode + ')';
                        }
                        // 如果没有状态码但有物流消息，显示物流消息
                        else if (msg && msg !== null && msg !== '') {
                            return msg;
                        }
                        // 都没有则显示默认状态
                        else {
                            return '物流信息待更新';
                        }
                    } catch (error) {
                        console.error('获取物流状态文本时出错:', error);
                        return '物流信息待更新';
                    }
                },
                
                // 获取物流状态样式类
                getLogisticsStatusClass(order) {
                    try {
                        // 安全地获取属性值
                        const statusCode = order && order.logicStatusCode ? order.logicStatusCode : null;
                        const msg = order && order.logicMsg ? order.logicMsg : null;
                        
                        // 如果有物流状态码，根据状态码设置样式
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
                        // 如果有物流消息，可能是错误信息
                        else if (msg && msg !== null && msg !== '') {
                            return 'logistics-message';
                        }
                        // 默认状态
                        else {
                            return 'logistics-default';
                        }
                    } catch (error) {
                        console.error('获取物流状态样式类时出错:', error);
                        return 'logistics-default';
                    }
                },
                
                // 格式化日期
                formatDate(dateStr) {
                    if (!dateStr) return '-';
                    const date = new Date(dateStr);
                    return date.toLocaleString('zh-CN');
                },
                
                // 查看订单详情页面
                viewOrderDetail(orderId) {
                    // 跳转到订单详情页面
                    window.location.href = '/my-order/detail-page?orderId=' + orderId;
                },
                
                // 加载订单详情
                loadOrderDetails(orderId) {
                    axios.get('/my-order/detail?orderId=' + orderId)
                        .then(response => {
                            if (response.data.code === 1) {
                                this.$set(this.orderDetails, orderId, response.data.data);
                            } else {
                                this.showError(response.data.msg || '获取订单详情失败');
                            }
                        })
                        .catch(error => {
                            console.error('获取订单详情失败:', error);
                            this.showError('获取订单详情失败，请稍后重试');
                        });
                },
                
                // 获取订单详情
                getOrderDetails(orderId) {
                    return this.orderDetails[orderId] || [];
                },
                
                // 重新支付订单
                repayOrder(orderId) {
                    if (confirm('确定要重新支付这个订单吗？')) {
                        axios.post('/my-order/repay/' + orderId)
                            .then(response => {
                                if (response.data.code === 1) {
                                    // 跳转到支付页面
                                    window.location.href = response.data.data;
                                } else {
                                    this.showError(response.data.msg || '重新支付失败');
                                }
                            })
                            .catch(error => {
                                console.error('重新支付失败:', error);
                                this.showError('重新支付失败，请稍后重试');
                            });
                    }
                },
                
                // 取消订单
                cancelOrder(orderId) {
                    if (confirm('确定要取消这个订单吗？取消后商品将放回购物车。')) {
                        axios.post('/my-order/cancel/' + orderId)
                            .then(response => {
                                if (response.data.code === 1) {
                                    this.showSuccess(response.data.msg || '订单取消成功');
                                    // 刷新页面
                                    setTimeout(() => {
                                        window.location.reload();
                                    }, 1500);
                                } else {
                                    this.showError(response.data.msg || '取消订单失败');
                                }
                            })
                            .catch(error => {
                                console.error('取消订单失败:', error);
                                this.showError('取消订单失败，请稍后重试');
                            });
                    }
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
                
                // 初始化订单状态跟踪
                initializeOrderStatusTracking() {
                    this.originalOrderStatuses = {};
                    this.orders.forEach(order => {
                        this.originalOrderStatuses[order.id] = order.orderStatus;
                    });
                },
                
            }
        });
</script>
