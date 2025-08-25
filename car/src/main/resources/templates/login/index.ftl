<div class="login-container">
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
                    
                    <form action="/login" method="post">
                        <!-- 隱藏的返回 URL 字段 -->
                        <input type="hidden" name="returnUrl" value="${returnUrl!''}">
                        
                        <div class="mb-3">
                            <label for="username" class="form-label">
                                <i class="bi bi-person me-2"></i>用戶名
                            </label>
                            <input type="text" class="form-control" id="username" name="username" 
                                   placeholder="請輸入用戶名" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="password" class="form-label">
                                <i class="bi bi-lock me-2"></i>密碼
                            </label>
                            <input type="password" class="form-control" id="password" name="password" 
                                   placeholder="請輸入密碼" required>
                        </div>
                        
                        <div class="mb-3 form-check">
                            <input type="checkbox" class="form-check-input" id="rememberMe">
                            <label class="form-check-label" for="rememberMe">
                                記住我
                            </label>
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