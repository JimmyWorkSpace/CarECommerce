<div class="container mt-4">
    <div class="row">
        <div class="col-12">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2><i class="bi bi-flag me-2"></i>我的檢舉</h2>
                <a href="/buy-cars" class="btn btn-outline-primary">
                    <i class="bi bi-arrow-left me-2"></i>返回車輛列表
                </a>
            </div>
            
            <#if reports?? && reports?size gt 0>
                <div class="row">
                    <#list reports as report>
                        <div class="col-12 mb-3">
                            <div class="card">
                                <div class="card-body">
                                    <div class="row">
                                        <div class="col-md-8">
                                            <h5 class="card-title">
                                                ${report.carTitle!''}
                                            </h5>
                                            <p class="card-text text-muted mb-2">
                                                <strong>
                                                    <a href="/detail/${report.saleId!''}" class="text-decoration-none text-dark">
                                                        ${report.carBrand!''} ${report.carModel!''} 
                                                        <#if report.carYear??>${report.carYear}年</#if>
                                                    </a>
                                                </strong>
                                            </p>
                                            <p class="card-text">
                                                <strong>檢舉原因：</strong>${report.reasonDisplayName!''}
                                            </p>
                                            <p class="card-text">
                                                <strong>詳細說明：</strong>${report.description!''}
                                            </p>
                                        </div>
                                        <div class="col-md-4 text-end">
                                            <div class="mb-2">
                                                <span class="badge 
                                                    <#if report.status == 'submitted'>bg-warning
                                                    <#elseif report.status == 'processing'>bg-info
                                                    <#elseif report.status == 'processed'>bg-success
                                                    <#elseif report.status == 'rejected'>bg-danger
                                                    <#elseif report.status == 'cancelled'>bg-secondary
                                                    <#else>bg-secondary</#if>">
                                                    ${report.statusDisplayName!''}
                                                </span>
                                                <#if report.status == 'submitted'>
                                                    <button class="btn btn-outline-danger btn-sm ms-2" 
                                                            onclick="cancelReport(${report.id!''})"
                                                            title="撤銷此檢舉">
                                                        <i class="bi bi-x-circle me-1"></i>撤銷
                                                    </button>
                                                </#if>
                                            </div>
                                            <p class="text-muted small mb-1">
                                                <i class="bi bi-calendar me-1"></i>
                                                提交時間：${report.createdAt!''}
                                            </p>
                                            <#if report.processedAt?? && report.processedAt?has_content>
                                                <p class="text-muted small mb-1">
                                                    <i class="bi bi-check-circle me-1"></i>
                                                    處理時間：${report.processedAt!''}
                                                </p>
                                            </#if>
                                            <#if report.processNote??>
                                                <p class="text-muted small">
                                                    <strong>處理備註：</strong>${report.processNote}
                                                </p>
                                            </#if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </#list>
                </div>
            <#else>
                <div class="text-center py-5">
                    <i class="bi bi-flag display-1 text-muted"></i>
                    <h4 class="mt-3 text-muted">暫無檢舉記錄</h4>
                    <p class="text-muted">您還沒有提交過任何檢舉</p>
                    <a href="/buy-cars" class="btn btn-primary">
                        <i class="bi bi-search me-2"></i>瀏覽車輛
                    </a>
                </div>
            </#if>
        </div>
    </div>
</div>

<style>
.card {
    border: 1px solid #dee2e6;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.card:hover {
    box-shadow: 0 4px 8px rgba(0,0,0,0.15);
    transition: box-shadow 0.3s ease;
}

.badge {
    font-size: 0.875rem;
    padding: 0.5rem 0.75rem;
}

/* 车辆型号链接样式 */
.card-text a {
    transition: color 0.3s ease;
}

.card-text a:hover {
    color: #0d6efd !important;
    text-decoration: underline !important;
}

/* 撤銷按钮样式 */
.btn-outline-danger {
    border-color: #dc3545;
    color: #dc3545;
}

.btn-outline-danger:hover {
    background-color: #dc3545;
    border-color: #dc3545;
    color: white;
}
</style>

<script>
// 撤銷檢舉
function cancelReport(reportId) {
    if (!reportId) {
        alert('檢舉ID無效');
        return;
    }
    
    if (!confirm('確定要撤銷此檢舉嗎？撤銷後將無法恢復。')) {
        return;
    }
    
    // 显示加载状态
    const button = event.target.closest('button');
    const originalText = button.innerHTML;
    button.innerHTML = '<i class="bi bi-hourglass-split me-1"></i>撤銷中...';
    button.disabled = true;
    
    // 发送撤銷请求
    fetch('/api/report/cancel/' + reportId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('檢舉已成功撤銷');
            // 刷新页面
            location.reload();
        } else {
            alert('撤銷失敗：' + (data.message || '未知錯誤'));
            // 恢复按钮状态
            button.innerHTML = originalText;
            button.disabled = false;
        }
    })
    .catch(error => {
        console.error('撤銷檢舉錯誤:', error);
        alert('撤銷失敗，請稍後再試');
        // 恢复按钮状态
        button.innerHTML = originalText;
        button.disabled = false;
    });
}
</script>
