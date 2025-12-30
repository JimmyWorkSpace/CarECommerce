<div class="login-container" id="app">
    <div class="row justify-content-center">
        <div class="col-md-10 col-lg-8 col-xl-6">
            <div class="card shadow-lg">
                <div class="card-header bg-primary text-white text-center">
                    <h4 class="mb-0">
                        <i class="bi bi-person-circle me-2"></i>
                        用戶登錄
                    </h4>
                </div>
                <div class="card-body p-4">
                    <#if error??>
                        <div class="alert alert-danger" role="alert">
                            <i class="bi bi-exclamation-triangle me-2"></i>
                            ${error}
                        </div>
                    </#if>
                    
                    <form action="/login" method="post" id="loginForm" <#noparse>@submit.prevent="handleSubmit"</#noparse>>
                        <!-- 隱藏的返回 URL 字段 -->
                        <input type="hidden" name="returnUrl" value="${returnUrl!''}">
                        
                        <div class="mb-3">
                            <label for="phoneNumber" class="form-label">
                                <i class="bi bi-phone me-2"></i>手機號碼
                            </label>
                            <div class="form-text">必須為10碼數字，以09開頭</div>
                            <input type="tel" class="form-control" id="phoneNumber" name="phoneNumber" 
                                   placeholder="請輸入手機號碼（09開頭）" required maxlength="10" <#noparse>:value="phoneNumber" @input="onPhoneInput"</#noparse>>
                        </div>
                        
                        <div class="mb-3">
                            <label for="password" class="form-label">
                                <i class="bi bi-lock me-2"></i>密碼
                            </label>
                            <input type="password" class="form-control" id="password" name="password" 
                                   placeholder="請輸入密碼" required <#noparse>v-model.trim="password"</#noparse>>
                        </div>
                        
                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary btn-lg">
                                <i class="bi bi-box-arrow-in-right me-2"></i>
                                登錄
                            </button>
                        </div>
                    </form>
                    
                    <div class="text-center mt-3">
                        <small class="text-muted">
                            <#--  還沒有帳號？   -->
                            <a href="/register" class="text-decoration-none">立即註冊/忘記密碼</a>
                        </small>
                    </div>
                </div>
            </div>
            
            <div class="text-center mt-3">
                <a href="/" class="btn btn-outline-secondary">
                    <i class="bi bi-arrow-left me-2"></i>
                    返回首頁
                </a>
            </div>
        </div>
    </div>
</div>

<style>
.login-container {
    min-height: 70vh;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 2rem 1rem;
    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.login-container .row {
    width: 100%;
    max-width: 800px;
}

.card {
    border: none;
    border-radius: 20px;
    overflow: hidden;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
    backdrop-filter: blur(10px);
    background: rgba(255, 255, 255, 0.95);
}

.card-header {
    background: linear-gradient(135deg, #5ACFC9 0%, #4AB8B2 100%) !important;
    border: none;
    padding: 1.5rem;
}

.form-control {
    border-radius: 10px;
    border: 2px solid #e9ecef;
    padding: 0.75rem 1rem;
    transition: all 0.3s ease;
}

.form-control:focus {
    border-color: #5ACFC9;
    box-shadow: 0 0 0 0.2rem rgba(90, 207, 201, 0.25);
}

.btn-primary {
    background: linear-gradient(135deg, #5ACFC9 0%, #4AB8B2 100%);
    border: none;
    border-radius: 10px;
    padding: 0.75rem 1.5rem;
    font-weight: 600;
    transition: all 0.3s ease;
}

.btn-primary:hover {
    background: linear-gradient(135deg, #4AB8B2 0%, #3AA7A1 100%);
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(90, 207, 201, 0.4);
}

.alert {
    border-radius: 10px;
    border: none;
}

.form-check-input:checked {
    background-color: #5ACFC9;
    border-color: #5ACFC9;
}

/* 大屏幕优化 */
@media (min-width: 1200px) {
    .login-container .row {
        max-width: 900px;
    }
    
    .card-body {
        padding: 2.5rem !important;
    }
    
    .card-header {
        padding: 2rem !important;
    }
    
    .form-control {
        padding: 1rem 1.25rem;
        font-size: 1.1rem;
    }
    
    .btn-lg {
        padding: 1rem 2rem;
        font-size: 1.1rem;
    }
}

/* 中等屏幕优化 */
@media (min-width: 992px) and (max-width: 1199px) {
    .login-container .row {
        max-width: 850px;
    }
    
    .card-body {
        padding: 2rem !important;
    }
    
    .form-control {
        padding: 0.875rem 1.125rem;
    }
}

/* 响应式设计优化 */
@media (max-width: 768px) {
    .login-container {
        min-height: 80vh;
        padding: 1rem;
    }
    
    .login-container .row {
        max-width: 100%;
    }
    
    .card-body {
        padding: 1.5rem !important;
    }
    
    .btn-lg {
        padding: 0.875rem 1.5rem;
        font-size: 1rem;
    }
}

@media (max-width: 576px) {
    .login-container {
        min-height: 90vh;
        padding: 0.5rem;
    }
    
    .card-body {
        padding: 1rem !important;
    }
    
    .card-header {
        padding: 1rem !important;
    }
    
    .card-header h4 {
        font-size: 1.25rem;
    }
}
</style>

<script>
new Vue({
    el: '#app',
    data: {
        phoneNumber: '',
        password: '',
        returnUrl: '${returnUrl!''}'
    },
    methods: {
        isValidPhone(phone) {
            // 台湾手机号格式：10码数字，以09开头
            return /^09\d{8}$/.test(phone);
        },
        onPhoneInput(e) {
            let value = e.target.value.replace(/\D/g, '');
            // 台湾手机号：限制为10码
            if (value.length > 10) {
                value = value.substring(0, 10);
            }
            // 更新Vue数据
            this.phoneNumber = value;
            // 确保输入框显示的值也是过滤后的值
            e.target.value = value;
        },
        handleSubmit() {
            const phone = this.phoneNumber.trim();
            const password = this.password.trim();
            
            if (!phone) {
                this.showToast('請輸入手機號碼', 'warning');
                return;
            }
            
            if (!this.isValidPhone(phone)) {
                this.showToast('請輸入正確的手機號碼格式（10碼數字，以09開頭）', 'warning');
                return;
            }
            
            if (!password) {
                this.showToast('請輸入密碼', 'warning');
                return;
            }
            
            // 手动提交表单
            const form = document.getElementById('loginForm');
            form.submit();
        },
        showToast(message, type) {
            type = type || 'info';
            // 创建toast容器（如果不存在）
            let toastContainer = document.getElementById('toastContainer');
            if (!toastContainer) {
                toastContainer = document.createElement('div');
                toastContainer.id = 'toastContainer';
                toastContainer.className = 'toast-container position-fixed top-0 end-0 p-3';
                toastContainer.style.zIndex = '9999';
                document.body.appendChild(toastContainer);
            }
            
            // 创建toast元素
            const toastId = 'toast-' + Date.now();
            const toast = document.createElement('div');
            toast.id = toastId;
            toast.className = 'toast';
            toast.setAttribute('role', 'alert');
            toast.setAttribute('aria-live', 'assertive');
            toast.setAttribute('aria-atomic', 'true');
            
            // 根据类型设置样式
            const bgClass = type === 'success' ? 'bg-success' : type === 'danger' ? 'bg-danger' : type === 'warning' ? 'bg-warning' : 'bg-info';
            const textClass = type === 'warning' ? 'text-dark' : 'text-white';
            
            // 根据类型设置图标
            let iconClass = 'info-circle';
            if (type === 'success') {
                iconClass = 'check-circle';
            } else if (type === 'danger' || type === 'warning') {
                iconClass = 'exclamation-triangle';
            }
            
            toast.innerHTML = '<div class="toast-header ' + bgClass + ' ' + textClass + '">' +
                '<i class="bi bi-' + iconClass + ' me-2"></i>' +
                '<strong class="me-auto">提示</strong>' +
                '<button type="button" class="btn-close btn-close-white" data-bs-dismiss="toast" aria-label="Close"></button>' +
                '</div>' +
                '<div class="toast-body">' +
                message +
                '</div>';
            
            toastContainer.appendChild(toast);
            
            // 初始化并显示toast
            const bsToast = new bootstrap.Toast(toast, {
                autohide: true,
                delay: 3000
            });
            bsToast.show();
            
            // toast关闭后移除元素
            toast.addEventListener('hidden.bs.toast', function() {
                toast.remove();
            });
        }
    },
    mounted() {
        // 页面加载时，如果已有手机号输入
        const phoneInput = document.getElementById('phoneNumber');
        if (phoneInput && phoneInput.value) {
            this.phoneNumber = phoneInput.value;
        }
    }
});
</script>