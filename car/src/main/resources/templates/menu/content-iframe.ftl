<style>
    .menu-content-text {
        line-height: 1.8;
        color: #333;
    }
    
    .menu-content-text h1 {
        color: #333;
        font-weight: 600;
        margin-bottom: 30px;
        font-size: 2rem;
        text-align: center;
    }
    
    .menu-content-text h2 {
        color: #5ACFC9;
        font-weight: 600;
        margin: 30px 0 15px 0;
        font-size: 1.5rem;
        border-bottom: 2px solid #5ACFC9;
        padding-bottom: 10px;
    }
    
    .menu-content-text h3 {
        color: #333;
        font-weight: 600;
        margin: 25px 0 10px 0;
        font-size: 1.3rem;
    }
    
    .menu-content-text h4 {
        color: #5ACFC9;
        font-weight: 600;
        margin: 20px 0 10px 0;
        font-size: 1.1rem;
    }
    
    .menu-content-text p {
        color: #666;
        line-height: 1.8;
        margin-bottom: 15px;
        font-size: 1rem;
    }
    
    .menu-content-text ul, .menu-content-text ol {
        margin-bottom: 15px;
        padding-left: 20px;
    }
    
    .menu-content-text li {
        margin-bottom: 8px;
        color: #666;
        line-height: 1.6;
    }
    
    .menu-content-text strong {
        color: #333;
        font-weight: 600;
    }
    
    .menu-content-text em {
        color: #5ACFC9;
        font-style: normal;
        font-weight: 500;
    }
    
    .highlight-box {
        background-color: #f8f9fa;
        border-left: 4px solid #5ACFC9;
        padding: 20px;
        margin: 20px 0;
        border-radius: 0 8px 8px 0;
    }
    
    .content-section {
        margin: 30px 0;
    }
    
    .content-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 20px;
        margin: 20px 0;
    }
    
    .content-item {
        background-color: #f8f9fa;
        padding: 20px;
        border-radius: 8px;
        border: 1px solid #e9ecef;
    }
    
    .content-item h4 {
        color: #5ACFC9;
        margin-bottom: 10px;
    }
    
    .content-item p {
        margin-bottom: 0;
        color: #666;
    }
    
    .menu-content-text img {
        max-width: 100%;
        height: auto;
        border-radius: 8px;
        margin: 15px 0;
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }
    
    .menu-content-text blockquote {
        border-left: 4px solid #5ACFC9;
        padding-left: 20px;
        margin: 20px 0;
        font-style: italic;
        color: #666;
        background-color: #f8f9fa;
        padding: 15px 20px;
        border-radius: 0 8px 8px 0;
    }
    
    .menu-content-text table {
        width: 100%;
        border-collapse: collapse;
        margin: 20px 0;
        background-color: #fff;
        border-radius: 8px;
        overflow: hidden;
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }
    
    .menu-content-text th,
    .menu-content-text td {
        padding: 12px 15px;
        text-align: left;
        border-bottom: 1px solid #e9ecef;
    }
    
    .menu-content-text th {
        background-color: #5ACFC9;
        color: #fff;
        font-weight: 600;
    }
    
    .menu-content-text tr:hover {
        background-color: #f8f9fa;
    }
    
    .menu-content-text code {
        background-color: #f8f9fa;
        padding: 2px 6px;
        border-radius: 4px;
        font-family: 'Courier New', monospace;
        color: #e83e8c;
    }
    
    .menu-content-text pre {
        background-color: #f8f9fa;
        padding: 15px;
        border-radius: 8px;
        overflow-x: auto;
        margin: 15px 0;
        border: 1px solid #e9ecef;
    }
    
    .menu-content-text pre code {
        background-color: transparent;
        padding: 0;
        color: #333;
    }
    
    /* 响应式设计 */
    @media (max-width: 768px) {
        .menu-content-text h1 {
            font-size: 1.5rem;
        }
        
        .menu-content-text h2 {
            font-size: 1.3rem;
        }
        
        .menu-content-text h3 {
            font-size: 1.1rem;
        }
        
        .content-grid {
            grid-template-columns: 1fr;
        }
        
        .menu-content-text table {
            font-size: 0.9rem;
        }
        
        .menu-content-text th,
        .menu-content-text td {
            padding: 8px 10px;
        }
    }
</style>

<div class="container">
    <div class="row">
        <div class="col-12">
            <div class="card">
                <div class="card-body">
                    <#if menu??>
                        <!-- 菜单富文本内容 -->
                        <div class="menu-content-text">
                            ${menu.content!''}
                        </div>
                    <#else>
                        <div class="alert alert-info" role="alert">
                            <h4 class="alert-heading">内容不存在</h4>
                            <p>抱歉，您要查看的内容不存在或已被删除。</p>
                        </div>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
// 页面加载完成后通知父页面调整高度
document.addEventListener('DOMContentLoaded', function() {
    // 监听来自父页面的高度请求
    window.addEventListener('message', function(event) {
        if (event.data && event.data.type === 'request-height') {
            // 计算页面内容高度
            const bodyHeight = document.body.scrollHeight;
            const documentHeight = document.documentElement.scrollHeight;
            const contentHeight = Math.max(bodyHeight, documentHeight);
            
            // 发送高度信息给父页面
            window.parent.postMessage({
                type: 'iframe-height-change',
                contentId: '${menu.id}',
                height: contentHeight
            }, '*');
        }
    });
    
    // 页面加载完成后自动请求高度调整
    setTimeout(function() {
        const bodyHeight = document.body.scrollHeight;
        const documentHeight = document.documentElement.scrollHeight;
        const contentHeight = Math.max(bodyHeight, documentHeight);
        
        window.parent.postMessage({
            type: 'iframe-height-change',
            contentId: '${menu.id}',
            height: contentHeight
        }, '*');
    }, 100);
});
</script>
