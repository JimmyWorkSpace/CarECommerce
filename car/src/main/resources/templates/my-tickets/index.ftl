<link href="/css/my-pages.css" rel="stylesheet">

<div class="my-page" id="app">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <h2 class="page-title">
                    <i class="bi bi-ticket-perforated me-3"></i>
                    我的票券
                </h2>
                
                <!-- 載入中 -->
                <div v-if="isLoading" class="text-center py-5 text-muted">
                    <span class="spinner-border spinner-border-sm me-2"></span>載入中...
                </div>
                <!-- 票券列表 -->
                <div v-else-if="tickets && tickets.length > 0">
                    <div v-for="t in tickets" :key="t.id" class="card item-card mb-3">
                        <div class="card-body">
                            <div class="item-info">
                                <div class="info-row">
                                    <span class="info-label">票券名稱：</span>
                                    <span class="info-value">{{ t.cardName || '-' }}</span>
                                </div>
                                <div class="info-row">
                                    <span class="info-label">票券序號：</span>
                                    <span class="info-value font-monospace fw-bold text-break">{{ t.ticketCode || '-' }}</span>
                                </div>
                                <div class="info-row">
                                    <span class="info-label">目前狀態：</span>
                                    <span class="badge" :class="getStatusClass(t.status)">{{ t.status }}</span>
                                </div>
                                <div class="info-row">
                                    <span class="info-label">購買時間：</span>
                                    <span class="info-value">{{ formatDateTime(t.createTime) }}</span>
                                </div>
                                <div class="info-row" v-if="t.status === '已核銷' && t.redeemedTime">
                                    <span class="info-label">核銷時間：</span>
                                    <span class="info-value">{{ formatDateTime(t.redeemedTime) }}</span>
                                </div>
                            </div>
                            <div class="item-actions mt-3" v-if="t.orderId">
                                <a :href="'/my-order/detail-page?orderId=' + t.orderId" class="btn btn-outline-primary btn-sm">
                                    <i class="bi bi-receipt me-1"></i>查看訂單
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- 空狀態 -->
                <div v-else class="empty-state">
                    <i class="bi bi-ticket-perforated"></i>
                    <h4>暫無票券</h4>
                    <p>您尚未購買任何票券</p>
                    <a href="/" class="btn btn-primary mt-2">前往選購</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    new Vue({
        el: '#app',
        data: {
            tickets: [],
            isLoading: true
        },
        mounted: function() {
            var vm = this;
            axios.get('/my-tickets/list').then(function(r) {
                vm.isLoading = false;
                if (r.data && r.data.code === 1 && r.data.data) {
                    vm.tickets = r.data.data;
                }
            }).catch(function() {
                vm.isLoading = false;
            });
        },
        methods: {
            formatDateTime(dateStr) {
                if (!dateStr) return '-';
                var d = new Date(dateStr);
                if (isNaN(d.getTime())) return '-';
                var y = d.getFullYear();
                var m = String(d.getMonth() + 1).padStart(2, '0');
                var day = String(d.getDate()).padStart(2, '0');
                var h = String(d.getHours()).padStart(2, '0');
                var min = String(d.getMinutes()).padStart(2, '0');
                var s = String(d.getSeconds()).padStart(2, '0');
                return y + '-' + m + '-' + day + ' ' + h + ':' + min + ':' + s;
            },
            getStatusClass(status) {
                if (status === '已核銷') return 'bg-success';
                if (status === '已過期') return 'bg-secondary';
                return 'bg-primary';
            }
        }
    });
</script>
