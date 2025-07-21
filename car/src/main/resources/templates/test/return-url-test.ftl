<!-- 测试返回URL功能 -->
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">
                    <h4>返回URL测试页面</h4>
                </div>
                <div class="card-body">
                    <p>这是一个测试页面，用于验证登录后的返回URL功能。</p>
                    <p>当前页面URL: <code id="currentUrl"></code></p>
                    
                    <div class="mt-4">
                        <h5>测试链接：</h5>
                        <ul>
                            <li><a href="/login?returnUrl=/static-demo">跳转到登录页（返回详情页）</a></li>
                            <li><a href="/login?returnUrl=/buy-cars">跳转到登录页（返回列表页）</a></li>
                            <li><a href="/login?returnUrl=/mall">跳转到登录页（返回商城页）</a></li>
                            <li><a href="/login?returnUrl=/about">跳转到登录页（返回关于页）</a></li>
                        </ul>
                    </div>
                    
                    <div class="mt-4">
                        <h5>直接测试：</h5>
                        <button class="btn btn-primary" onclick="testReturnUrl()">
                            测试当前页面返回URL
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    // 显示当前页面URL
    document.getElementById('currentUrl').textContent = window.location.pathname + window.location.search;
});

function testReturnUrl() {
    const currentUrl = window.location.pathname + window.location.search;
    const loginUrl = '/login?returnUrl=' + encodeURIComponent(currentUrl);
    alert('将跳转到: ' + loginUrl);
    window.location.href = loginUrl;
}
</script> 