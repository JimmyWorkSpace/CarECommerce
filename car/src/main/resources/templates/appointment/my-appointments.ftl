<link href="/css/appointment.css" rel="stylesheet">

<div class="appointment-page" id="appointmentApp">
    <div class="page-header">
        <h2 class="page-title">
            <i class="bi bi-calendar-check me-2"></i>我的預約
        </h2>
        <p class="page-subtitle">查看和管理您的車輛預約記錄</p>
    </div>

    <!-- 加载状态 -->
    <div <#noparse>v-if="loading"</#noparse> class="loading-container">
        <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">加載中...</span>
        </div>
        <p class="mt-3 text-muted">正在加載預約數據...</p>
    </div>

    <!-- 无数据状态 -->
    <div <#noparse>v-else-if="appointments.length === 0"</#noparse> class="no-data-container">
        <div class="no-data-icon">
            <i class="bi bi-calendar-x"></i>
        </div>
        <h3>暫無預約記錄</h3>
        <p>您還沒有預約過任何車輛，快去挑選心儀的車輛吧！</p>
        <a href="/buy-cars" class="btn btn-primary">
            <i class="bi bi-search me-2"></i>去買車
        </a>
    </div>

    <!-- 预约列表 -->
    <div <#noparse>v-else</#noparse> class="appointments-container">
        <div class="appointment-card" <#noparse>v-for="appointment in appointments" :key="appointment.id"</#noparse>>
            <div class="appointment-header">
                <div class="appointment-info">
                    <h5 class="appointment-title"><#noparse>{{ appointment.carTitle }}</#noparse></h5>
                    <div class="appointment-meta">
                        <span class="appointment-date">
                            <i class="bi bi-calendar me-1"></i>
                            <#noparse>{{ formatDate(appointment.appointmentTime) }}</#noparse>
                        </span>
                        <span class="appointment-phone">
                            <i class="bi bi-telephone me-1"></i>
                            <#noparse>{{ appointment.appointmentPhone }}</#noparse>
                        </span>
                    </div>
                </div>
                <div class="appointment-status">
                    <span class="status-badge" <#noparse>:class="getStatusClass(appointment.appointmentStatus)"</#noparse>>
                        <#noparse>{{ appointment.appointmentStatusDesc }}</#noparse>
                    </span>
                </div>
            </div>

            <div class="appointment-body">
                <div class="car-info">
                    <div class="car-image">
                        <img <#noparse>:src="appointment.carCoverImage || '/img/car/car6.jpg'" 
                             :alt="appointment.carTitle" 
                             @error="handleImageError"</#noparse>>
                    </div>
                    <div class="car-details">
                        <div class="car-specs">
                            <span class="car-brand"><#noparse>{{ appointment.carBrand }}</#noparse></span>
                            <span class="car-model"><#noparse>{{ appointment.carModel }}</#noparse></span>
                        </div>
                        <div class="car-price">
                            $<#noparse>{{ formatPrice(appointment.carPrice) }}</#noparse>
                        </div>
                    </div>
                </div>

                <div class="appointment-actions" <#noparse>v-if="appointment.appointmentStatus === 1"</#noparse>>
                    <button class="btn btn-outline-primary btn-sm" <#noparse>@click="confirmViewing(appointment.id)"</#noparse>>
                        <i class="bi bi-check-circle me-1"></i>確認看車
                    </button>
                    <button class="btn btn-outline-danger btn-sm" <#noparse>@click="cancelAppointment(appointment.id)"</#noparse>>
                        <i class="bi bi-x-circle me-1"></i>取消預約
                    </button>
                </div>

                <div class="appointment-note" <#noparse>v-if="appointment.appointmentNote"</#noparse>>
                    <strong>備註：</strong><#noparse>{{ appointment.appointmentNote }}</#noparse>
                </div>
            </div>

            <div class="appointment-footer">
                <div class="appointment-time">
                    <small class="text-muted">
                        <i class="bi bi-clock me-1"></i>
                        預約時間：<#noparse>{{ formatDateTime(appointment.appointmentTime) }}</#noparse>
                    </small>
                </div>
                <div class="appointment-created">
                    <small class="text-muted">
                        <i class="bi bi-calendar-plus me-1"></i>
                        創建時間：<#noparse>{{ formatDateTime(appointment.createTime) }}</#noparse>
                    </small>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
new Vue({
    el: '#appointmentApp',
    data: {
        loading: true,
        appointments: []
    },
    mounted() {
        this.loadAppointments();
    },
    methods: {
        async loadAppointments() {
            try {
                this.loading = true;
                const response = await axios.get('/appointment/api/my-appointments');
                
                if (response.data.success) {
                    this.appointments = response.data.data || [];
                } else {
                    console.error('加载预约列表失败:', response.data.message);
                    this.appointments = [];
                }
            } catch (error) {
                console.error('加载预约列表失败:', error);
                this.appointments = [];
                
                // 处理未登录状态
                if (window.handleUnauthorized && window.handleUnauthorized(error.response)) {
                    return;
                }
                
                // 显示错误提示
                const errorMessage = window.handleApiError ? 
                    window.handleApiError(error, '加载预约列表失败') : 
                    '加载预约列表失败，请稍后重试';
                alert(errorMessage);
            } finally {
                this.loading = false;
            }
        },

        async confirmViewing(appointmentId) {
            if (!confirm('確認您已經看過這輛車了嗎？')) {
                return;
            }

            try {
                <#noparse>const response = await axios.post(`/appointment/api/confirm/${appointmentId}`);</#noparse>
                
                if (response.data.success) {
                    alert('確認成功！');
                    this.loadAppointments(); // 重新加载列表
                } else {
                    alert('確認失败：' + response.data.message);
                }
            } catch (error) {
                console.error('確認看車失败:', error);
                const errorMessage = window.handleApiError ? 
                    window.handleApiError(error, '確認看車失败') : 
                    '確認看車失败，请稍后重试';
                alert(errorMessage);
            }
        },

        async cancelAppointment(appointmentId) {
            if (!confirm('確定要取消這個預約嗎？')) {
                return;
            }

            try {
                <#noparse>const response = await axios.post(`/appointment/api/cancel/${appointmentId}`);</#noparse>
                
                if (response.data.success) {
                    alert('取消成功！');
                    this.loadAppointments(); // 重新加载列表
                } else {
                    alert('取消失败：' + response.data.message);
                }
            } catch (error) {
                console.error('取消預約失败:', error);
                const errorMessage = window.handleApiError ? 
                    window.handleApiError(error, '取消預約失败') : 
                    '取消預約失败，请稍后重试';
                alert(errorMessage);
            }
        },

        formatPrice(price) {
            if (!price) return '0';
            return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        },

        formatDate(dateString) {
            if (!dateString) return '';
            const date = new Date(dateString);
            return date.toLocaleDateString('zh-TW', {
                year: 'numeric',
                month: 'long',
                day: 'numeric'
            });
        },

        formatDateTime(dateString) {
            if (!dateString) return '';
            const date = new Date(dateString);
            return date.toLocaleString('zh-TW', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit'
            });
        },

        getStatusClass(status) {
            switch (status) {
                case 1: return 'status-appointed';
                case 2: return 'status-viewed';
                case 3: return 'status-cancelled';
                default: return 'status-unknown';
            }
        },

        handleImageError(event) {
            event.target.src = '/img/car/car6.jpg';
        }
    }
});
</script>

