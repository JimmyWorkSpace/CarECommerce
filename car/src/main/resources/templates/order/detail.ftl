<link href="/css/my-pages.css" rel="stylesheet">
<style>
    /* 票券券碼：手機端卡片列表 */
    @media (max-width: 767.98px) {
        .card-ticket-list .card-body { font-size: 0.9rem; }
        .card-ticket-list .small { font-size: 0.75rem; }
        .card-ticket-list .font-monospace { word-break: break-all; }
    }
</style>

<div class="my-page" id="app">
    <div class="container">
        <div class="row">
            <div class="col-12">
                
                <!-- 訂單詳情 -->
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
                        <!-- 訂單基本信息 -->
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
                        
                        <!-- 收件人信息（卡券訂單 orderBizType=2 不顯示） -->
                        <div v-if="order.orderBizType !== 2" class="mt-4">
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
                        
                        <!-- 訂單商品詳情 -->
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
                        
                        <!-- 票券券碼（已支付且 orderBizType=2 時顯示） -->
                        <div class="mt-4" v-if="order.orderStatus === 2 && order.orderBizType === 2">
                            <h6 class="mb-3 text-start fw-bold text-primary border-bottom pb-2">
                                <i class="bi bi-ticket-perforated me-2"></i>票券券碼
                            </h6>
                            <div class="item-info text-start bg-light rounded p-3">
                                <div v-if="!cardDetails || cardDetails.length === 0" class="text-muted">暫無券碼，請稍後刷新頁面。</div>
                                <template v-else>
                                    <!-- 桌面：表格 -->
                                    <div class="table-responsive d-none d-md-block">
                                        <table class="table table-sm table-hover mb-0">
                                            <thead>
                                                <tr>
                                                    <th>票券名稱</th>
                                                    <th>券碼</th>
                                                    <th>購買時間</th>
                                                    <th>核銷狀態</th>
                                                    <th>核銷時間</th>
                                                    <th>過期時間</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr v-for="item in cardDetails" :key="'t-' + item.id">
                                                    <td>{{ item.cardName || '-' }}</td>
                                                    <td class="fw-bold font-monospace">{{ item.ticketCode || '-' }}</td>
                                                    <td>{{ formatDateTime(item.createTime) }}</td>
                                                    <td>
                                                        <span class="badge" :class="item.redeemed === 1 ? 'bg-success' : 'bg-secondary'">
                                                            {{ item.redeemed === 1 ? '已核銷' : '未核銷' }}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <template v-if="item.redeemed === 1 && item.redeemedTime">
                                                            {{ formatDateTime(item.redeemedTime) }}
                                                        </template>
                                                        <span v-else class="text-muted">-</span>
                                                    </td>
                                                    <td>
                                                        <template v-if="item.redeemed !== 1 && item.validityEndTime">
                                                            {{ formatDateTime(item.validityEndTime) }}
                                                        </template>
                                                        <span v-else-if="item.redeemed !== 1" class="text-muted">-</span>
                                                        <span v-else class="text-muted">-</span>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <!-- 手機：卡片列表 -->
                                    <div class="d-md-none card-ticket-list">
                                        <div class="card border mb-2" v-for="item in cardDetails" :key="'c-' + item.id">
                                            <div class="card-body py-3 px-3">
                                                <div class="d-flex justify-content-between align-items-start mb-2">
                                                    <span class="text-muted small">票券名稱</span>
                                                    <span class="fw-bold text-break text-end">{{ item.cardName || '-' }}</span>
                                                </div>
                                                <div class="d-flex justify-content-between align-items-center mb-2">
                                                    <span class="text-muted small">券碼</span>
                                                    <span class="fw-bold font-monospace text-break text-end">{{ item.ticketCode || '-' }}</span>
                                                </div>
                                                <div class="d-flex justify-content-between align-items-center mb-2">
                                                    <span class="text-muted small">購買時間</span>
                                                    <span class="text-end">{{ formatDateTime(item.createTime) }}</span>
                                                </div>
                                                <div class="d-flex justify-content-between align-items-center mb-2">
                                                    <span class="text-muted small">核銷狀態</span>
                                                    <span class="badge" :class="item.redeemed === 1 ? 'bg-success' : 'bg-secondary'">
                                                        {{ item.redeemed === 1 ? '已核銷' : '未核銷' }}
                                                    </span>
                                                </div>
                                                <div class="d-flex justify-content-between align-items-center mb-2">
                                                    <span class="text-muted small">核銷時間</span>
                                                    <span class="text-end">
                                                        <template v-if="item.redeemed === 1 && item.redeemedTime">{{ formatDateTime(item.redeemedTime) }}</template>
                                                        <span v-else class="text-muted">-</span>
                                                    </span>
                                                </div>
                                                <div class="d-flex justify-content-between align-items-center">
                                                    <span class="text-muted small">過期時間</span>
                                                    <span class="text-end">
                                                        <template v-if="item.redeemed !== 1 && item.validityEndTime">{{ formatDateTime(item.validityEndTime) }}</template>
                                                        <span v-else class="text-muted">-</span>
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </template>
                            </div>
                        </div>
                        
                        <!-- 物流信息（卡券訂單 orderBizType=2 不顯示） -->
                        <div class="mt-4" v-if="order.orderBizType !== 2 && logisticsInfo.logisticsStatus">
                            <h6 class="mb-3 text-start fw-bold text-primary border-bottom pb-2">
                                <i class="bi bi-truck-fill me-2"></i>物流資訊
                                <button type="button" class="btn btn-outline-primary btn-sm ms-2" 
                                        @click="loadLogisticsInfo" :disabled="isLoadingLogistics">
                                    <i class="bi bi-arrow-clockwise me-1"></i>刷新
                                </button>
                                <span class="badge bg-secondary ms-2" v-if="isLoadingLogistics">加載中...</span>
                            </h6>
                            
                            <!-- 物流詳細信息 -->
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
                                
                                <!-- 簡單物流信息（當沒有詳細物流信息時顯示，卡券訂單不顯示） -->
                            <div class="item-info bg-light rounded p-3" v-else-if="order.orderBizType !== 2 && order.logicNumber && !logisticsInfo">
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
                            
                            <!-- 無物流信息時的提示（卡券訂單不顯示） -->
                            <div class="alert alert-info" v-if="order.orderBizType !== 2 && !order.logicNumber && order.orderStatus >= 5">
                                <i class="bi bi-info-circle me-2"></i>
                                訂單已發貨，但暫無物流信息，請稍後刷新或聯繫客服。
                            </div>
                        </div>
                        
                        <!-- 綠界支付信息 -->
                        <div class="mt-4">
                            <h6 class="mb-3 text-start fw-bold text-primary border-bottom pb-2">
                                <i class="bi bi-credit-card-fill me-2"></i>綠界支付資訊
                                <button type="button" class="btn btn-outline-primary btn-sm ms-2" 
                                        @click="loadECPayInfo" :disabled="isLoadingECPay">
                                    <i class="bi bi-arrow-clockwise me-1"></i>刷新
                                </button>
                                <span class="badge bg-secondary ms-2" v-if="isLoadingECPay">加載中...</span>
                            </h6>
                            
                            <!-- 調試信息 -->
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
                            
                            <!-- 無數據時的提示 -->
                            <div class="alert alert-warning" v-if="!ecpayInfo && !isLoadingECPay">
                                <i class="bi bi-exclamation-triangle me-2"></i>
                                暫無綠界支付信息，請點擊刷新按鈕獲取最新信息。
                            </div>
                        </div>
                        
                        <!-- 訂單操作 -->
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
                
                <!-- 加載狀態 -->
                <div v-else-if="isLoading" class="text-center py-5">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">加載中...</span>
                    </div>
                    <p class="mt-3">正在加載訂單詳情...</p>
                </div>
                
                <!-- 錯誤狀態 -->
                <div v-else class="empty-state">
                    <i class="bi bi-exclamation-triangle"></i>
                    <h4>訂單不存在</h4>
                    <p>您訪問的訂單不存在或已被刪除</p>
                    <a href="/my-order" class="btn btn-primary">返回訂單列表</a>
                </div>
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
            cardDetails: [],
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
            console.log('訂單詳情頁面已加載');
            this.loadOrderDetail();
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
            
            // 獲取訂單類型文本
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
            // 格式化日期時間為 yyyy-MM-dd HH:mm:ss（票券購買/過期時間等）
            formatDateTime(dateStr) {
                if (!dateStr) return '-';
                const d = new Date(dateStr);
                if (isNaN(d.getTime())) return '-';
                const y = d.getFullYear();
                const m = String(d.getMonth() + 1).padStart(2, '0');
                const day = String(d.getDate()).padStart(2, '0');
                const h = String(d.getHours()).padStart(2, '0');
                const min = String(d.getMinutes()).padStart(2, '0');
                const s = String(d.getSeconds()).padStart(2, '0');
                return y + '-' + m + '-' + day + ' ' + h + ':' + min + ':' + s;
            },
            
            // 加載訂單詳情
            loadOrderDetail() {
                this.isLoading = true;
                this.clearMessages();
                
                // 從URL獲取訂單ID
                const urlParams = new URLSearchParams(window.location.search);
                const orderId = urlParams.get('orderId');
                
                if (!orderId) {
                    this.showError('訂單ID不能為空');
                    this.isLoading = false;
                    return;
                }
                
                // 加載訂單基本信息
                axios.get('/my-order/order-info?orderId=' + orderId)
                    .then(response => {
                        if (response.data.code === 1) {
                            this.order = response.data.data;
                            this.originalOrderStatus = this.order.orderStatus;
                            this.loadOrderDetails(orderId);
                            this.loadECPayInfo(); // 加載綠界支付信息
                            if (this.order.orderBizType !== 2) {
                                this.loadLogisticsInfo(); // 卡券訂單不查詢物流
                            }
                            if (this.order.orderStatus === 2 && this.order.orderBizType === 2) {
                                this.loadCardDetails(orderId); // 已支付卡券訂單載入券碼
                            }
                            // 重新啟動狀態檢查
                            //this.stopStatusCheck();
                            //this.startStatusCheck();
                        } else {
                            this.showError(response.data.msg || '獲取訂單資訊失敗');
                        }
                    })
                    .catch(error => {
                        console.error('獲取訂單信息失敗:', error);
                        this.showError('獲取訂單資訊失敗，請稍後重試');
                    })
                    .finally(() => {
                        this.isLoading = false;
                    });
            },
            
            // 加載訂單商品詳情
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
            
            // 加載綠界支付信息
            loadECPayInfo() {
                let _this = this;
                const urlParams = new URLSearchParams(window.location.search);
                const orderId = urlParams.get('orderId');
                
                if (!orderId) {
                    console.warn('訂單ID為空，無法加載綠界支付信息');
                    return;
                }
                
                console.log('開始加載綠界支付信息，訂單ID:', orderId);
                this.isLoadingECPay = true;
                this.ecpayInfo = null; // 清空之前的數據
                axios.get('/my-order/ecpay-info?orderId=' + orderId)
                    .then(response => {
                        console.log('綠界支付信息API響應:', response.data);
                        if (response.data && response.data.code === 1) {
                            _this.ecpayInfo = response.data.data;
                            console.log('綠界支付信息設置成功:', this.ecpayInfo);
                            console.log('ecpayInfo類型:', typeof this.ecpayInfo);
                            console.log('ecpayInfo是否為null:', this.ecpayInfo === null);
                            console.log('ecpayInfo是否為undefined:', this.ecpayInfo === undefined);
                        } else {
                            console.warn('獲取綠界支付信息失敗:', response.data ? response.data.msg : '未知錯誤');
                            _this.ecpayInfo = null;
                        }
                    })
                    .catch(error => {
                        console.error('獲取綠界支付信息失敗:', error);
                        _this.ecpayInfo = null;
                    })
                    .finally(() => {
                        this.isLoadingECPay = false;
                        console.log('綠界支付信息加載完成，最終狀態:', this.ecpayInfo);
                    });
            },
            
            // 加載物流信息（卡券訂單 orderBizType=2 不查詢）
            loadLogisticsInfo() {
                let _this = this;
                if (!this.order || !this.order.orderNo) {
                    console.warn('訂單號為空，無法加載物流信息');
                    return;
                }
                if (this.order.orderBizType === 2) {
                    console.log('卡券訂單，跳過物流查詢');
                    return;
                }
                
                console.log('開始加載物流信息，訂單號:', this.order.orderNo);
                this.isLoadingLogistics = true;
                this.logisticsInfo = {}; // 清空之前的數據
                
                axios.get('/api/logistics/query?orderNo=' + this.order.orderNo)
                    .then(response => {
                        console.log('物流信息API響應:', response.data);
                        if (response.data && response.data.code === 1) {
                            _this.logisticsInfo = response.data.data;
                            console.log('物流信息設置成功:', _this.logisticsInfo);
                        } else {
                            _this.logisticsInfo = {};
                        }
                    })
                    .catch(error => {
                        console.error('獲取物流信息失敗:', error);
                        this.logisticsInfo = {};
                    })
                    .finally(() => {
                        this.isLoadingLogistics = false;
                        console.log('物流信息加載完成，最終狀態:', this.logisticsInfo);
                    });
            },
            
            // 獲取物流狀態文本
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
            
            // 獲取物流狀態樣式類
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
            
            // 獲取物流類型文本
            getLogisticsTypeText(type) {
                const typeMap = {
                    // 宅配類型
                    'HOME_TCAT': '宅配-黑貓宅急便',
                    'HOME_ECAN': '宅配-宅配通',
                    'HOME_FAMI': '宅配-全家宅配通',
                    'HOME_POST': '宅配-中華郵政',
                    // 超商類型
                    'CVS_FAMI': '超商-全家',
                    'CVS_OK': '超商-OK超商',
                    'CVS_HILIFE': '超商-萊爾富',
                    'CVS_IBON': '超商-7-11',
                    'CVS_UNIMART': '超商-統一超商',
                    // 其他類型
                    'HOME': '宅配',
                    'CVS': '超商'
                };
                return typeMap[type] || type || '未知類型';
            },
            
            // 判斷是否為宅配類型
            isHomeDelivery(type) {
                if (!type) return false;
                return type.startsWith('HOME') || type === 'HOME';
            },
            
            // 判斷是否為超商類型
            isConvenienceStore(type) {
                if (!type) return false;
                return type.startsWith('CVS') || type === 'CVS';
            },
            
            // 獲取綠界支付狀態文本
            getECPayStatusText(status) {
                const statusMap = {
                    '0': '未付款',
                    '1': '已付款',
                    '10200095': '交易失敗',
                    '10200163': '申請失敗'
                };
                return statusMap[status] || '未知狀態';
            },
            
            // 獲取綠界支付狀態樣式類
            getECPayStatusClass(status) {
                const classMap = {
                    '0': 'text-warning',
                    '1': 'text-success',
                    '10200095': 'text-danger',
                    '10200163': 'text-danger'
                };
                return classMap[status] || 'text-muted';
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
                                // 刷新訂單狀態
                                setTimeout(() => {
                                    this.loadOrderDetail();
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
            
            // 刷新訂單狀態
            refreshOrder() {
                this.stopStatusCheck();
                this.loadOrderDetail();
                this.showSuccess('訂單狀態已刷新');
            },
            
            // 開始狀態檢查
            startStatusCheck() {
                // 只有未支付或支付中的訂單才需要檢查狀態
                if (this.order && (this.order.orderStatus === 0 || this.order.orderStatus === 1)) {
                    console.log('開始定時檢查訂單狀態');
                    this.statusCheckInterval = setInterval(() => {
                        this.checkOrderStatus();
                    }, 20000); // 每20秒檢查一次
                }
            },
            
            // 停止狀態檢查
            stopStatusCheck() {
                if (this.statusCheckInterval) {
                    clearInterval(this.statusCheckInterval);
                    this.statusCheckInterval = null;
                    console.log('停止訂單狀態檢查');
                }
            },
            
            // 檢查訂單狀態
            checkOrderStatus() {
                if (!this.order || !this.order.orderNo) {
                    console.log('訂單資訊不完整，無法檢查狀態');
                    return;
                }
                
                console.log('從綠界API檢查訂單狀態:', this.order.orderNo);
                
                axios.post('/ecpay/refresh-status/' + this.order.orderNo)
                .then(response => {
                    if (response.data && (response.data.code === 1 || response.data.code === 200)) {
                        const result = response.data.data;
                        const newOrderInfo = result ? result.orderInfo : null;
                        
                        if (newOrderInfo && newOrderInfo.orderStatus !== this.originalOrderStatus) {
                            console.log('訂單狀態發生變化，從', this.originalOrderStatus, '變為', newOrderInfo.orderStatus);
                            this.originalOrderStatus = newOrderInfo.orderStatus;
                            if (newOrderInfo.orderStatus === 2) {
                                this.stopStatusCheck();
                                this.showSuccess('訂單已支付！正在刷新頁面…');
                                window.location.reload();
                                return;
                            }
                            if (newOrderInfo.orderStatus === 3 || newOrderInfo.orderStatus === 4) {
                                this.stopStatusCheck();
                                this.showSuccess('訂單狀態已更新！');
                                this.order = newOrderInfo;
                            }
                        }
                    } else {
                        console.error('從綠界API查詢訂單狀態失敗:', response.data ? response.data.msg : '未知錯誤');
                    }
                }).catch(error => {
                    console.error('從綠界API檢查訂單狀態失敗:', error);
                });
            },
            
            // 加載票券券碼（已支付且 orderBizType=2 時使用）
            loadCardDetails(orderId) {
                if (!orderId) return;
                axios.get('/my-order/card-details?orderId=' + orderId)
                    .then(response => {
                        if (response.data && response.data.code === 1 && response.data.data) {
                            this.cardDetails = response.data.data;
                        } else {
                            this.cardDetails = [];
                        }
                    })
                    .catch(() => { this.cardDetails = []; });
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
            
            // 清除所有消息
            clearMessages() {
                this.errorMessage = '';
                this.successMessage = '';
            },
            
            // 獲取完整的收件人地址
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

