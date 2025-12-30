<style>
    /* iframe容器样式 */
    #richContentIframe {
        transition: height 0.3s ease;
    }
    
    /* 确保iframe内容样式 */
    .rich-content-text {
        line-height: 1.8;
        color: #333;
    }
    
    .rich-content-text h1 {
        color: #333;
        font-weight: 600;
        margin-bottom: 30px;
        font-size: 2rem;
        text-align: center;
    }
    
    .rich-content-text h2 {
        color: #5ACFC9;
        font-weight: 600;
        margin: 30px 0 15px 0;
        font-size: 1.5rem;
        border-bottom: 2px solid #5ACFC9;
        padding-bottom: 10px;
    }
    
    .rich-content-text h3 {
        color: #333;
        font-weight: 600;
        margin: 25px 0 10px 0;
        font-size: 1.3rem;
    }
    
    .rich-content-text h4 {
        color: #5ACFC9;
        font-weight: 600;
        margin: 20px 0 10px 0;
        font-size: 1.1rem;
    }
    
    .rich-content-text p {
        color: #666;
        line-height: 1.8;
        margin-bottom: 15px;
        font-size: 1rem;
    }
    
    .rich-content-text ul, .rich-content-text ol {
        margin-bottom: 15px;
        padding-left: 20px;
    }
    
    .rich-content-text li {
        margin-bottom: 8px;
        color: #666;
        line-height: 1.6;
    }
    
    .rich-content-text strong {
        color: #333;
        font-weight: 600;
    }
    
    .rich-content-text em {
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
        border-radius: 10px;
        text-align: center;
        border: 1px solid #e9ecef;
    }
    
    .content-item h4 {
        color: #5ACFC9;
        margin-bottom: 10px;
    }
    
    .info-box {
        background-color: #e8f8f7;
        padding: 20px;
        border-radius: 10px;
        margin: 30px 0;
        border: 1px solid #5ACFC9;
    }
    
    .info-box h3 {
        color: #5ACFC9;
        margin-bottom: 15px;
    }
    
    .info-box p {
        margin: 5px 0;
        color: #333;
    }
    
    /* Video和YouTube视频样式 */
    .rich-content-text video,
    .rich-content-text iframe[src*="youtube.com"],
    .rich-content-text iframe[src*="youtu.be"],
    .rich-content-text iframe[src*="youtube-nocookie.com"],
    video,
    iframe[src*="youtube.com"],
    iframe[src*="youtu.be"],
    iframe[src*="youtube-nocookie.com"] {
        max-width: 99%;
        height: auto;
        display: block;
        margin: 15px auto;
        border-radius: 8px;
    }
    
    /* 确保YouTube iframe保持16:9比例 */
    .rich-content-text iframe[src*="youtube.com"],
    .rich-content-text iframe[src*="youtu.be"],
    .rich-content-text iframe[src*="youtube-nocookie.com"],
    iframe[src*="youtube.com"],
    iframe[src*="youtu.be"],
    iframe[src*="youtube-nocookie.com"] {
        aspect-ratio: 16 / 9;
        width: 99%;
        max-width: 99%;
    }
    
    /* 响应式设计 */
    @media (max-width: 768px) {
        .rich-content-text h1 {
            font-size: 1.5rem;
        }
        
        .rich-content-text h2 {
            font-size: 1.3rem;
        }
        
        .rich-content-text h3 {
            font-size: 1.1rem;
        }
        
        .rich-content-text h4 {
            font-size: 1rem;
        }
        
        .rich-content-text p {
            font-size: 0.95rem;
        }
        
        .content-grid {
            grid-template-columns: 1fr;
        }
        
        .content-item {
            padding: 15px;
        }
        
        #richContentIframe {
            margin: 0 -15px;
        }
    }
</style>

<div class="container">
    <div class="row">
        <div class="col-12">
            <div class="card">
                <div class="card-body">
                    <#if richContent??>
                        <!-- 富文本内容iframe -->
                        <iframe id="richContentIframe" 
                                style="width: 100%; border: none; min-height: 200px;">
                        </iframe>
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
                        <script>
                            document.addEventListener('DOMContentLoaded', function() {
                                const iframe = document.getElementById('richContentIframe');
                                const htmlContent = `${richContent.content!''}`;
                                
                                // 使用loadHtmlToIframe方法加载内容并自动调整高度
                                if (typeof window.loadHtmlToIframe === 'function') {
                                    window.loadHtmlToIframe(iframe, htmlContent, {
                                        minHeight: 200,
                                        maxHeight: Infinity,
                                        adjustDelay: 150,
                                        removeScrollbars: true
                                    });
                                } else {
                                    console.error('loadHtmlToIframe方法未找到，请确保common.js已加载');
                                    // 降级方案：直接设置内容
                                    iframe.srcdoc = htmlContent;
                                }
                            });
                        </script>
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
