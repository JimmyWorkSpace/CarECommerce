<!-- 測試返回URL功能 -->
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">
                    <h4>返回URL測試頁面</h4>
                </div>
                <div class="card-body">
                    <p>這是一個測試頁面，用於驗證登錄後的返回URL功能。</p>
                    <p>當前URL: <code id="currentUrl"></code></p>
                    
                    <div class="mt-4">
                        <h5>測試鏈接：</h5>
                        <ul>
                            <li><a href="/login?returnUrl=/static-demo">跳轉到登錄頁（返回詳情頁）</a></li>
                            <li><a href="/login?returnUrl=/buy-cars">跳轉到登錄頁（返回列表頁）</a></li>
                            <li><a href="/login?returnUrl=/mall">跳轉到登錄頁（返回商城頁）</a></li>
                            <li><a href="/login?returnUrl=/about">跳轉到登錄頁（返回關於頁）</a></li>
                        </ul>
                    </div>
                    
                    <div class="mt-4">
                        <h5>直接測試：</h5>
                        <button class="btn btn-primary" onclick="testReturnUrl()">
                            測試當前頁面返回URL
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    // 顯示當前頁面URL
    document.getElementById('currentUrl').textContent = window.location.pathname + window.location.search;
});

function testReturnUrl() {
    const currentUrl = window.location.pathname + window.location.search;
    const loginUrl = '/login?returnUrl=' + encodeURIComponent(currentUrl);
    alert('將跳轉到: ' + loginUrl);
    window.location.href = loginUrl;
}
</script> 