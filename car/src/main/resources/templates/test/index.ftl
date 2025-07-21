<div class="test-container">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow-lg">
                <div class="card-header bg-info text-white">
                    <h4 class="mb-0">
                        <i class="bi bi-gear me-2"></i>
                        登录状态测试页面
                    </h4>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <h5>当前登录状态</h5>
                            <#if user?? && user?has_content>
                                <div class="alert alert-success">
                                    <i class="bi bi-check-circle me-2"></i>
                                    <strong>已登录</strong>
                                </div>
                                
                                <div class="user-info">
                                    <h6>用户信息：</h6>
                                    <ul class="list-group list-group-flush">
                                        <li class="list-group-item">
                                            <strong>用户ID：</strong> ${user.id}
                                        </li>
                                        <li class="list-group-item">
                                            <strong>用户名：</strong> ${user.username}
                                        </li>
                                        <li class="list-group-item">
                                            <strong>昵称：</strong> ${user.nickname}
                                        </li>
                                        <li class="list-group-item">
                                            <strong>邮箱：</strong> ${user.email}
                                        </li>
                                        <li class="list-group-item">
                                            <strong>电话：</strong> ${user.phone}
                                        </li>
                                    </ul>
                                </div>
                            <#else>
                                <div class="alert alert-warning">
                                    <i class="bi bi-exclamation-triangle me-2"></i>
                                    <strong>未登录</strong>
                                </div>
                                <p class="text-muted">当前没有用户登录</p>
                            </#if>
                        </div>
                        
                        <div class="col-md-6">
                            <h5>测试操作</h5>
                            <div class="d-grid gap-2">
                                <#if user?? && user?has_content>
                                    <a href="/test/logout" class="btn btn-danger">
                                        <i class="bi bi-box-arrow-right me-2"></i>
                                        清除登录状态
                                    </a>
                                <#else>
                                    <a href="/test/login" class="btn btn-success">
                                        <i class="bi bi-box-arrow-in-right me-2"></i>
                                        手动设置登录状态
                                    </a>
                                </#if>
                                
                                <a href="/login" class="btn btn-primary">
                                    <i class="bi bi-person me-2"></i>
                                    访问登录页面
                                </a>
                                
                                <a href="/" class="btn btn-secondary">
                                    <i class="bi bi-house me-2"></i>
                                    返回首页
                                </a>
                            </div>
                            
                            <div class="mt-3">
                                <h6>说明：</h6>
                                <ul class="list-unstyled">
                                    <li><i class="bi bi-info-circle me-2"></i>在开发环境中，系统会自动设置用户为已登录状态</li>
                                    <li><i class="bi bi-info-circle me-2"></i>可以通过上方按钮手动测试登录/登出功能</li>
                                    <li><i class="bi bi-info-circle me-2"></i>登录状态会显示在页面顶部导航栏中</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
.test-container {
    padding: 2rem 0;
}

.card {
    border: none;
    border-radius: 15px;
    overflow: hidden;
}

.card-header {
    background: linear-gradient(135deg, #17a2b8 0%, #138496 100%) !important;
    border: none;
    padding: 1.5rem;
}

.user-info {
    margin-top: 1rem;
}

.list-group-item {
    border: none;
    padding: 0.5rem 0;
    background: transparent;
}

.alert {
    border-radius: 10px;
    border: none;
}

.btn {
    border-radius: 10px;
    padding: 0.75rem 1.5rem;
    font-weight: 600;
    transition: all 0.3s ease;
}

.btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}
</style> 