<link href="/css/about.css" rel="stylesheet">

<div class="about-page">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <!-- iframe容器 -->
                <div class="iframe-container">
                    <iframe id="menu-content-iframe" 
                            src="/menu-content/${menu.id}/content" 
                            frameborder="0" 
                            scrolling="no"
                            style="width: 100%; height: 600px; border: none; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                    </iframe>
                </div>
                
                <!-- QA模块 - 放在iframe外面 -->
                <#if qaList?? && qaList?has_content>
                    <div class="qa-section">
                        <h3 class="qa-title">
                            <i class="bi bi-question-circle"></i>
                            常見問題
                        </h3>
                        <div class="accordion" id="qaAccordion-${menu.id}">
                            <#list qaList as qa>
                                <div class="accordion-item">
                                    <h2 class="accordion-header" id="heading-${qa.id}">
                                        <button class="accordion-button collapsed" 
                                                type="button" 
                                                data-bs-toggle="collapse" 
                                                data-bs-target="#collapse-${qa.id}" 
                                                aria-expanded="false" 
                                                aria-controls="collapse-${qa.id}">
                                            <i class="bi bi-question-circle-fill me-2"></i>
                                            ${qa.question}
                                        </button>
                                    </h2>
                                    <div id="collapse-${qa.id}" 
                                         class="accordion-collapse collapse" 
                                         aria-labelledby="heading-${qa.id}" 
                                         data-bs-parent="#qaAccordion-${menu.id}">
                                        <div class="accordion-body">
                                            ${qa.answer}
                                        </div>
                                    </div>
                                </div>
                            </#list>
                        </div>
                    </div>
                </#if>
            </div>
        </div>
    </div>
</div>

<style>
    /* iframe容器样式 */
    .iframe-container {
        position: relative;
        min-height: 400px;
        margin-bottom: 40px;
    }
    
    .iframe-container iframe {
        transition: height 0.3s ease;
    }
    
    /* QA模块样式 */
    .qa-section {
        margin-top: 40px;
        padding-top: 30px;
        border-top: 2px solid #f0f0f0;
    }

    .qa-title {
        color: #5ACFC9;
        font-weight: 600;
        margin-bottom: 25px;
        font-size: 1.4rem;
        display: flex;
        align-items: center;
    }

    .qa-title i {
        margin-right: 10px;
        font-size: 1.2rem;
    }

    .accordion {
        border-radius: 10px;
        overflow: hidden;
        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    }

    .accordion-item {
        border: none;
        border-bottom: 1px solid #e9ecef;
    }

    .accordion-item:last-child {
        border-bottom: none;
    }

    .accordion-button {
        background-color: #f8f9fa;
        border: none;
        padding: 20px 25px;
        font-weight: 500;
        color: #333;
        font-size: 1rem;
        transition: all 0.3s ease;
        display: flex;
        align-items: center;
    }

    .accordion-button:not(.collapsed) {
        background-color: #5ACFC9;
        color: white;
        box-shadow: none;
    }

    .accordion-button:focus {
        box-shadow: none;
        border: none;
    }

    .accordion-button::after {
        background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16' fill='%23333'%3e%3cpath fill-rule='evenodd' d='M1.646 4.646a.5.5 0 0 1 .708 0L8 10.293l5.646-5.647a.5.5 0 0 1 .708.708l-6 6a.5.5 0 0 1-.708 0l-6-6a.5.5 0 0 1 0-.708z'/%3e%3c/svg%3e");
        transition: transform 0.3s ease;
    }

    .accordion-button:not(.collapsed)::after {
        background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16' fill='%23ffffff'%3e%3cpath fill-rule='evenodd' d='M1.646 4.646a.5.5 0 0 1 .708 0L8 10.293l5.646-5.647a.5.5 0 0 1 .708.708l-6 6a.5.5 0 0 1-.708 0l-6-6a.5.5 0 0 1 0-.708z'/%3e%3c/svg%3e");
        transform: rotate(180deg);
    }

    .accordion-button i {
        margin-right: 10px;
        font-size: 1.1rem;
    }

    .accordion-body {
        padding: 25px;
        background-color: white;
        line-height: 1.8;
        color: #666;
    }

    .accordion-body h4 {
        color: #5ACFC9;
        font-weight: 600;
        margin: 20px 0 15px 0;
        font-size: 1.1rem;
    }

    .accordion-body h5 {
        color: #333;
        font-weight: 600;
        margin: 15px 0 10px 0;
        font-size: 1rem;
    }

    .accordion-body p {
        margin-bottom: 15px;
        color: #666;
    }

    .accordion-body ul, .accordion-body ol {
        margin-bottom: 15px;
        padding-left: 20px;
    }

    .accordion-body li {
        margin-bottom: 8px;
        color: #666;
    }

    .accordion-body strong {
        color: #333;
        font-weight: 600;
    }
    
    /* 响应式设计 */
    @media (max-width: 768px) {
        .qa-title {
            font-size: 1.2rem;
        }
        
        .accordion-button {
            padding: 15px 20px;
            font-size: 0.95rem;
        }
        
        .accordion-body {
            padding: 20px;
        }
    }
</style>

<script>
// iframe高度自适应功能
document.addEventListener('DOMContentLoaded', function() {
    
    window.addEventListener('message', function(event) {
        if (event.data && event.data.type === 'iframe-height-change') {
            const contentId = event.data.contentId;
            const height = event.data.height;
            const iframe = document.getElementById('menu-content-iframe');
            
            if (iframe && contentId === '${menu.id}') {
                // 设置最小高度为400px，最大高度为2000px
                const minHeight = 400;
                const maxHeight = 2000;
                const finalHeight = Math.max(minHeight, Math.min(maxHeight, height)); // 不使用额外缓冲
                
                iframe.style.height = finalHeight + 'px';
                console.log('调整菜单富文本内容iframe高度:', '原始高度:', height, '最终高度:', finalHeight);
            }
        }
    });
    
    // 页面加载完成后，为iframe请求高度
    setTimeout(function() {
        const iframe = document.getElementById('menu-content-iframe');
        if (iframe) {
            try {
                iframe.contentWindow.postMessage({type: 'request-height'}, '*');
            } catch (e) {
                console.log('无法访问菜单富文本内容iframe内容:', e);
            }
        }
    }, 500);
});
</script>
