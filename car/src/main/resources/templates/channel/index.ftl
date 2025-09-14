<link href="/css/my-pages.css" rel="stylesheet">

<div class="my-page">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <#if channelContentList?? && channelContentList?size gt 0>
                    <!-- 动态生成Tab导航 -->
                    <ul class="nav nav-tabs channel-tabs" id="channelTabs" role="tablist">
                        <#list channelContentList as content>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link <#if content_index == 0>active</#if>" 
                                        id="tab-${content.id}" 
                                        data-bs-toggle="tab" 
                                        data-bs-target="#content-${content.id}" 
                                        type="button" 
                                        role="tab" 
                                        aria-controls="content-${content.id}" 
                                        aria-selected="<#if content_index == 0>true<#else>false</#if>">
                                    ${content.title}
                                </button>
                            </li>
                        </#list>
                    </ul>
                    
                    <!-- 动态生成Tab内容 -->
                    <div class="tab-content channel-content" id="channelTabContent">
                        <#list channelContentList as content>
                            <div class="tab-pane fade <#if content_index == 0>show active</#if>" 
                                 id="content-${content.id}" 
                                 role="tabpanel" 
                                 aria-labelledby="tab-${content.id}">
                                <!-- 频道内容 -->
                                <div class="channel-content-text">
                                    ${content.content}
                                </div>
                                
                                <!-- QA模块 -->
                                <#if channelQAMap?? && channelQAMap[content.id?string]??>
                                    <div class="qa-section">
                                        <h3 class="qa-title">
                                            <i class="bi bi-question-circle"></i>
                                            常見問題
                                        </h3>
                                        <div class="accordion" id="qaAccordion-${content.id}">
                                            <#list channelQAMap[content.id?string] as qa>
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
                                                         data-bs-parent="#qaAccordion-${content.id}">
                                                        <div class="accordion-body">
                                                            ${qa.answer}
                                                        </div>
                                                    </div>
                                                </div>
                                            </#list>
                                        </div>
                                    </div>
                                <#else>
                                    <div class="no-qa-info" style="background: #fff3cd; padding: 15px; margin: 20px 0; border: 1px solid #ffeaa7; border-radius: 5px;">
                                        <strong>暂无问答数据</strong><br>
                                        频道ID: ${content.id}<br>
                                        请联系管理员添加该频道的问答内容。
                                    </div>
                                </#if>
                            </div>
                        </#list>
                    </div>
                <#else>
                    <!-- 默认内容 -->
                    <div class="alert alert-info" role="alert">
                        <h4 class="alert-heading">频道内容</h4>
                        <p>暂无频道内容，请联系管理员添加。</p>
                    </div>
                </#if>
            </div>
        </div>
    </div>
</div>

<style>
/* 频道页面专用样式 */
.channel-tabs {
    border-bottom: 2px solid #5ACFC9;
    margin-bottom: 30px;
}

.channel-tabs .nav-link {
    border: none;
    color: #666;
    font-weight: 500;
    padding: 12px 20px;
    margin-right: 5px;
    border-radius: 8px 8px 0 0;
    transition: all 0.3s ease;
}

.channel-tabs .nav-link:hover {
    color: #5ACFC9;
    background-color: #f8f9fa;
}

.channel-tabs .nav-link.active {
    color: white;
    background-color: #5ACFC9;
    border: none;
}

.channel-content {
    min-height: 400px;
}

.channel-content-text {
    padding: 20px 0;
}

.channel-content-text h3 {
    color: #333;
    font-weight: 600;
    margin-bottom: 20px;
    font-size: 1.5rem;
}

.channel-content-text h4 {
    color: #5ACFC9;
    font-weight: 600;
    margin: 25px 0 10px 0;
    font-size: 1.2rem;
}

.channel-content-text p {
    color: #666;
    line-height: 1.8;
    margin-bottom: 15px;
    font-size: 1rem;
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

/* QA内容特殊样式 */
.qa-answer {
    font-size: 1rem;
    line-height: 1.8;
}

.highlight-box {
    background-color: #f8f9fa;
    border-left: 4px solid #5ACFC9;
    padding: 15px 20px;
    margin: 15px 0;
    border-radius: 0 8px 8px 0;
}

.benefits, .service-features, .quality-assurance, .problem-solutions, .support-services, .after-sales {
    margin: 20px 0;
}

.benefit-grid, .feature-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 15px;
    margin: 15px 0;
}

.benefit-item, .feature-item {
    background-color: #f8f9fa;
    padding: 10px 15px;
    border-radius: 6px;
    text-align: center;
    font-weight: 500;
    color: #5ACFC9;
}

.contact-info, .contact-loan, .contact-support, .contact-join, .fee-contact {
    background-color: #e8f8f7;
    padding: 15px 20px;
    border-radius: 8px;
    margin: 20px 0;
    border: 1px solid #5ACFC9;
}

.contact-info p, .contact-loan p, .contact-support p, .contact-join p, .fee-contact p {
    margin: 0;
    color: #333;
    font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .channel-tabs .nav-link {
        padding: 10px 15px;
        font-size: 0.9rem;
    }
    
    .channel-content-text h3 {
        font-size: 1.3rem;
    }
    
    .channel-content-text h4 {
        font-size: 1.1rem;
    }
    
    .channel-content-text p {
        font-size: 0.95rem;
    }
    
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
    
    .benefit-grid, .feature-grid {
        grid-template-columns: 1fr;
    }
}
</style>
