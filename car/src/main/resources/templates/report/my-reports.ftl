<link href="/css/my-pages.css" rel="stylesheet">

<div class="my-page">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <h2 class="page-title text-center">
                    <i class="bi bi-flag me-3"></i>我的檢舉
                </h2>
                
                <#if reports?? && reports?size gt 0>
                    <#list reports as report>
                        <div class="card item-card">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <span class="status-badge 
                                        <#if report.status == 'submitted'>status-submitted
                                        <#elseif report.status == 'processing'>status-processing
                                        <#elseif report.status == 'processed'>status-processed
                                        <#elseif report.status == 'cancelled'>status-cancelled-report
                                        <#else>status-submitted</#if>">
                                        ${report.statusDisplayName!''}
                                    </span>
                                    <#if report.status == 'submitted'>
                                        <button class="btn btn-outline-danger btn-sm" 
                                                onclick="cancelReport(${report.id!''})"
                                                title="撤銷此檢舉">
                                            <i class="bi bi-x-circle me-1"></i>撤銷
                                        </button>
                                    </#if>
                                </div>
                                
                                <div class="item-info">
                                    <div class="info-row">
                                        <span class="info-label">檢舉原因：</span>
                                        <span class="info-value">${report.reasonDisplayName!''}</span>
                                    </div>
                                    <div class="info-row">
                                        <span class="info-label">提交時間：</span>
                                        <span class="info-value">${report.createdAt!''}</span>
                                    </div>
                                    <#if report.processedAt?? && report.processedAt?has_content>
                                        <div class="info-row">
                                            <span class="info-label">處理時間：</span>
                                            <span class="info-value">${report.processedAt!''}</span>
                                        </div>
                                    </#if>
                                </div>
                                
                                <div class="item-note">
                                    <strong>詳細說明：</strong>${report.description!''}
                                    <#if report.processNote??>
                                        <br><strong>處理備註：</strong>${report.processNote}
                                    </#if>
                                </div>
                                
                            </div>
                        </div>
                    </#list>
                <#else>
                    <div class="empty-state">
                        <i class="bi bi-flag"></i>
                        <h4>暫無檢舉記錄</h4>
                        <p>您還沒有提交過任何檢舉</p>
                        <a href="/buy-cars" class="btn btn-primary">
                            <i class="bi bi-search me-2"></i>瀏覽車輛
                        </a>
                    </div>
                </#if>
            </div>
        </div>
    </div>
</div>

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