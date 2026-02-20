<style>
    .card-list-page { padding: 24px 0; min-height: 60vh; }
    .card-list-page .page-title { margin-bottom: 28px; font-weight: 600; color: #333; }
    .card-item { border: 1px solid #e9ecef; border-radius: 12px; padding: 20px; margin-bottom: 20px; background: #fff; box-shadow: 0 1px 3px rgba(0,0,0,0.06); display: flex; align-items: center; flex-wrap: wrap; }
    .card-item .col-left { flex: 1; min-width: 200px; text-align: left; }
    .card-item .card-name { font-size: 1.15rem; font-weight: 600; color: #333; margin-bottom: 8px; }
    .card-item .card-desc { font-size: 0.9rem; color: #6c757d; margin-bottom: 8px; white-space: pre-wrap; }
    .card-item .validity { font-size: 0.85rem; color: #495057; }
    .card-item .col-price { width: 100px; text-align: center; margin: 0 16px; }
    .card-item .card-price { font-size: 1.25rem; color: #e74c3c; font-weight: 600; }
    .card-item .col-qty { display: flex; align-items: center; gap: 0; margin: 0 16px; }
    .card-item .qty-btn { width: 36px; height: 36px; padding: 0; font-size: 1.1rem; line-height: 1; border: 1px solid #dee2e6; background: #fff; cursor: pointer; border-radius: 6px; }
    .card-item .qty-btn:hover { background: #f8f9fa; }
    .card-item .qty-btn:disabled { opacity: 0.6; cursor: not-allowed; }
    .card-item .qty-value { width: 44px; height: 36px; text-align: center; border: 1px solid #dee2e6; border-left: none; border-right: none; font-size: 1rem; }
    .card-item .col-buy { margin-left: auto; }
    .card-item .btn-buy { background: linear-gradient(135deg, #5ACFC9 0%, #4AB8B2 100%); border: none; color: #fff; font-weight: 500; padding: 10px 24px; border-radius: 8px; white-space: nowrap; }
    .card-item .btn-buy:hover { opacity: 0.9; color: #fff; }
    .card-item .btn-buy:disabled { opacity: 0.7; }
    .card-empty { text-align: center; padding: 48px 20px; color: #6c757d; }
    @media (max-width: 768px) {
        .card-item .col-price, .card-item .col-qty { width: 100%; margin: 12px 0 0 0; justify-content: flex-start; }
        .card-item .col-buy { margin-left: 0; margin-top: 12px; width: 100%; }
        .card-item .col-buy .btn-buy { width: 100%; }
    }
</style>

<div class="card-list-page" id="app">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <h2 class="page-title">
                    <i class="bi bi-ticket-perforated me-3"></i>票券列表
                </h2>
                <p class="text-muted mb-4">可購買的優惠券與方案，選擇數量後點擊「購買」對接金流完成付款。後續將支援加入購物車。</p>

                <div v-if="error" class="alert alert-danger alert-dismissible fade show">
                    <i class="bi bi-exclamation-triangle me-2"></i>{{ error }}
                    <button type="button" class="btn-close" @click="error = ''"></button>
                </div>

                <div v-if="cardList.length === 0" class="card-empty">
                    <i class="bi bi-inbox" style="font-size: 3rem;"></i>
                    <p class="mt-3 mb-0">暫無可購買的票券</p>
                </div>

                <div v-for="card in cardList" :key="card.id" class="card-item">
                    <div class="col-left">
                        <div class="card-name">{{ card.cardName }}</div>
                        <div v-if="card.usageInstruction" class="card-desc">{{ card.usageInstruction }}</div>
                        <div class="validity">
                            <span v-if="card.validityType === 1">有效期限：指定日期至 {{ formatValidityDate(card.validityEndDate) }}</span>
                            <span v-else-if="card.validityType === 2">有效期限：購買後 {{ card.validityDays }} 天內有效</span>
                            <span v-else>有效期限：以方案說明為準</span>
                        </div>
                    </div>
                    <div class="col-price">
                        <div class="card-price">${CurrencyUnit!''}{{ formatPrice(card.salePrice) }}</div>
                    </div>
                    <div class="col-qty">
                        <button type="button" class="qty-btn" @click="decreaseQty(card)" :disabled="card._qty <= 1">−</button>
                        <input type="text" class="qty-value" :value="card._qty" readonly>
                        <button type="button" class="qty-btn" @click="increaseQty(card)" :disabled="card._qty >= 99">+</button>
                    </div>
                    <div class="col-buy">
                        <button type="button" class="btn btn-primary btn-buy" @click="buyCard(card)" :disabled="card._loading">
                            <span v-if="!card._loading"><i class="bi bi-credit-card me-2"></i>購買</span>
                            <span v-else><i class="bi bi-arrow-clockwise spin me-2"></i>處理中...</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
(function() {
    var cardList = ${cardListJson!'[]'};
    cardList.forEach(function(c) { c._qty = 1; c._loading = false; });

    document.addEventListener('DOMContentLoaded', function() {
        new Vue({
            el: '#app',
            data: {
                cardList: cardList,
                isLoading: false,
                error: '',
                paymentConfig: null
            },
            mounted: function() {
                this.loadPaymentConfig();
            },
            methods: {
                loadPaymentConfig: function() {
                    var vm = this;
                    axios.get('/api/payment/config').then(function(res) {
                        if (res.data && res.data.code === 1) vm.paymentConfig = res.data.data;
                    }).catch(function() {});
                },
                formatPrice: function(val) {
                    if (val == null) return '0';
                    return parseFloat(val).toFixed(2);
                },
                formatValidityDate: function(val) {
                    if (val == null || val === '') return '-';
                    var d;
                    if (typeof val === 'number') {
                        d = new Date(val);
                    } else if (typeof val === 'string' && /^\d+$/.test(val)) {
                        d = new Date(parseInt(val, 10));
                    } else {
                        d = new Date(val);
                    }
                    if (isNaN(d.getTime())) return '-';
                    var y = d.getFullYear();
                    var m = ('0' + (d.getMonth() + 1)).slice(-2);
                    var day = ('0' + d.getDate()).slice(-2);
                    return y + '-' + m + '-' + day;
                },
                decreaseQty: function(card) {
                    if (card._qty > 1) card._qty--;
                },
                increaseQty: function(card) {
                    if (card._qty < 99) card._qty++;
                },
                buyCard: function(card) {
                    var vm = this;
                    var qty = parseInt(card._qty, 10) || 1;
                    if (qty < 1) qty = 1;
                    card._loading = true;
                    vm.error = '';
                    axios.post('/api/payment/card/create', {
                        cardData: [ { cardId: card.id, quantity: qty } ]
                    }).then(function(res) {
                        card._loading = false;
                        if (res.data && res.data.code === 1 && res.data.data) {
                            vm.openPaymentInNewWindow(res.data.data);
                        } else {
                            vm.error = (res.data && res.data.msg) ? res.data.msg : '創建支付訂單失敗';
                        }
                    }).catch(function(err) {
                        card._loading = false;
                        vm.error = (err.response && err.response.data && err.response.data.msg) ? err.response.data.msg : '請先登錄或稍後再試';
                    });
                },
                openPaymentInNewWindow: function(paymentParams) {
                    var serverUrl = this.paymentConfig && this.paymentConfig.serverUrl
                        ? this.paymentConfig.serverUrl
                        : 'https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5';
                    var form = document.createElement('form');
                    form.method = 'POST';
                    form.action = serverUrl;
                    form.target = '_blank';
                    for (var k in paymentParams) {
                        if (paymentParams.hasOwnProperty(k)) {
                            var input = document.createElement('input');
                            input.type = 'hidden';
                            input.name = k;
                            input.value = paymentParams[k];
                            form.appendChild(input);
                        }
                    }
                    document.body.appendChild(form);
                    form.submit();
                }
            }
        });
    });
})();
</script>
