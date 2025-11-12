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
                            <input type="tel" class="form-control" id="phoneNumber" name="phoneNumber" 
                                   placeholder="請輸入手機號碼" required <#noparse>v-model.trim="phoneNumber" @input="onPhoneInput"</#noparse>>
                        </div>
                        
                        <div class="mb-3">
                            <label for="smsCode" class="form-label">
                                <i class="bi bi-shield-check me-2"></i>短信驗證碼
                            </label>
                            <div class="input-group">
                                <input type="text" class="form-control" id="smsCode" name="smsCode" 
                                       placeholder="請輸入驗證碼" required maxlength="6" <#noparse>v-model.trim="smsCode" @input="onCodeInput"</#noparse>>
                                <button type="button" class="btn btn-outline-primary" id="sendSmsBtn" <#noparse>:disabled="countdown>0" @click="sendSms"</#noparse>>
                                    <span id="sendSmsText"><#noparse>{{ countdown>0 ? `${countdown}秒後重發` : '發送驗證碼' }}</#noparse></span>
                                </button>
                            </div>
                            <div class="form-text">驗證碼將發送到您的手機，有效期5分鐘</div>
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
                            還沒有帳號？ 
                            <a href="#" class="text-decoration-none">立即註冊</a>
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
        smsCode: '',
        countdown: 0,
        countdownTimer: null,
        returnUrl: '${returnUrl!''}'
    },
    methods: {
        isValidPhone(phone) {
            return /^1[3-9]\d{9}$/.test(phone);
        },
        isValidCode(code) {
            return /^\d{6}$/.test(code);
        },
        onPhoneInput(e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length > 11) {
                value = value.substring(0, 11);
            }
            this.phoneNumber = value;
        },
        onCodeInput(e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length > 6) {
                value = value.substring(0, 6);
            }
            this.smsCode = value;
        },
        startCountdown() {
            this.countdown = 30; // 改为30秒倒计时
            if (this.countdownTimer) {
                clearInterval(this.countdownTimer);
            }
            this.countdownTimer = setInterval(() => {
                this.countdown--;
                if (this.countdown <= 0) {
                    clearInterval(this.countdownTimer);
                    this.countdownTimer = null;
                    this.countdown = 0;
                }
            }, 1000);
        },
        async sendSms() {
            const phone = this.phoneNumber.trim();
            
            // 验证手机号格式
            if (!this.isValidPhone(phone)) {
                alert('請輸入手機號碼');
                return;
            }
            
            try {
                const res = await axios.post('/api/sms/send', { phoneNumber: phone });
                const data = res.data;
                if (data.success) {
                    this.startCountdown();
                    alert('驗證碼已發送，請查看控制台輸出');
                } else {
                    // 如果服务器返回剩余等待时间，显示更友好的提示
                    if (data.remainingTime) {
                        alert('請等待 ' + data.remainingTime + ' 秒後再發送驗證碼');
                        // 如果服务器返回剩余时间，设置前端倒计时
                        this.countdown = data.remainingTime;
                        this.startCountdown();
                    } else {
                        alert(data.message || '發送失敗，請稍後重試');
                    }
                }
            } catch (err) {
                console.error(err);
                alert('發送失敗，請稍後重試');
            }
        },
        handleSubmit() {
            const phone = this.phoneNumber.trim();
            const code = this.smsCode.trim();
            if (!this.isValidCode(code)) {
                alert('請輸入6位數字驗證碼');
                return;
            }
            // 手动提交表单，保留原有後端處理
            const form = document.getElementById('loginForm');
            form.submit();
        }
    }
});
</script>