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
/* 频道页面专用样式 - 现代化设计 */
.channel-tabs {
    border: none;
    margin-bottom: 40px;
    background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
    border-radius: 15px;
    padding: 8px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    position: relative;
    overflow: hidden;
}

.channel-tabs::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(45deg, rgba(90, 207, 201, 0.1) 0%, rgba(74, 184, 178, 0.1) 100%);
    border-radius: 15px;
    z-index: 0;
}

.channel-tabs .nav-item {
    position: relative;
    z-index: 1;
    margin: 0 4px;
}

.channel-tabs .nav-link {
    border: none;
    color: #666;
    font-weight: 600;
    padding: 16px 28px;
    border-radius: 12px;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    background: transparent;
    font-size: 1rem;
    letter-spacing: 0.5px;
    text-transform: none;
    box-shadow: none;
    overflow: hidden;
}

.channel-tabs .nav-link::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg, #5ACFC9 0%, #4AB8B2 100%);
    opacity: 0;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    border-radius: 12px;
    z-index: -1;
}

.channel-tabs .nav-link::after {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    width: 0;
    height: 0;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
    transform: translate(-50%, -50%);
    transition: all 0.6s cubic-bezier(0.4, 0, 0.2, 1);
    z-index: -1;
}

.channel-tabs .nav-link:hover {
    color: white;
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(90, 207, 201, 0.3);
}

.channel-tabs .nav-link:hover::before {
    opacity: 1;
}

.channel-tabs .nav-link:hover::after {
    width: 300px;
    height: 300px;
}

.channel-tabs .nav-link.active {
    color: white;
    background: linear-gradient(135deg, #5ACFC9 0%, #4AB8B2 100%);
    transform: translateY(-3px);
    box-shadow: 0 12px 30px rgba(90, 207, 201, 0.4);
    font-weight: 700;
    letter-spacing: 0.8px;
}

.channel-tabs .nav-link.active::before {
    opacity: 1;
}

.channel-tabs .nav-link.active::after {
    width: 300px;
    height: 300px;
    background: rgba(255, 255, 255, 0.1);
}

/* 焦点状态和无障碍访问 */
.channel-tabs .nav-link:focus {
    outline: none;
    box-shadow: 0 0 0 3px rgba(90, 207, 201, 0.3);
    transform: translateY(-1px);
}

.channel-tabs .nav-link:focus:not(.active) {
    background: rgba(90, 207, 201, 0.1);
    color: #5ACFC9;
}

/* 加载动画效果 */
.channel-tabs .nav-link.loading {
    position: relative;
    pointer-events: none;
}

.channel-tabs .nav-link.loading::before {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    width: 20px;
    height: 20px;
    margin: -10px 0 0 -10px;
    border: 2px solid transparent;
    border-top: 2px solid currentColor;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    z-index: 1;
}

.channel-tabs .nav-link.loading {
    color: transparent;
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}

/* 平滑的内容切换动画 */
.channel-content {
    min-height: 400px;
    position: relative;
}

.tab-pane {
    animation: fadeInUp 0.5s ease-out;
}

@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

/* 增强的视觉层次 */
.channel-tabs .nav-link {
    position: relative;
    z-index: 2;
}

.channel-tabs .nav-link::before {
    z-index: -1;
}

.channel-tabs .nav-link::after {
    z-index: -2;
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

/* 响应式设计 - 现代化移动端适配 */
@media (max-width: 768px) {
    .channel-tabs {
        margin-bottom: 30px;
        padding: 6px;
        border-radius: 12px;
    }
    
    .channel-tabs .nav-item {
        margin: 0 2px;
    }
    
    .channel-tabs .nav-link {
        padding: 12px 18px;
        font-size: 0.9rem;
        border-radius: 10px;
        letter-spacing: 0.3px;
    }
    
    .channel-tabs .nav-link:hover {
        transform: translateY(-1px);
        box-shadow: 0 6px 20px rgba(90, 207, 201, 0.25);
    }
    
    .channel-tabs .nav-link.active {
        transform: translateY(-2px);
        box-shadow: 0 8px 25px rgba(90, 207, 201, 0.35);
        letter-spacing: 0.5px;
    }
    
    .channel-tabs .nav-link:hover::after,
    .channel-tabs .nav-link.active::after {
        width: 200px;
        height: 200px;
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

/* 超小屏幕优化 */
@media (max-width: 480px) {
    .channel-tabs {
        padding: 4px;
        border-radius: 10px;
    }
    
    .channel-tabs .nav-link {
        padding: 10px 14px;
        font-size: 0.85rem;
        border-radius: 8px;
    }
    
    .channel-tabs .nav-link:hover::after,
    .channel-tabs .nav-link.active::after {
        width: 150px;
        height: 150px;
    }
}
</style>
