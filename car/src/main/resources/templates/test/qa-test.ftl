<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>QA模块测试</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h1>QA模块测试页面</h1>
        
        <div class="row">
            <div class="col-md-6">
                <h3>所有QA数据</h3>
                <#if allQA?? && allQA?size gt 0>
                    <div class="list-group">
                        <#list allQA as qa>
                            <div class="list-group-item">
                                <h5>ID: ${qa.id}</h5>
                                <p><strong>频道ID:</strong> ${qa.channelId}</p>
                                <p><strong>问题:</strong> ${qa.question}</p>
                                <p><strong>排序:</strong> ${qa.showOrder}</p>
                                <p><strong>删除标记:</strong> ${qa.delFlag?c}</p>
                            </div>
                        </#list>
                    </div>
                <#else>
                    <div class="alert alert-warning">没有找到QA数据</div>
                </#if>
            </div>
            
            <div class="col-md-6">
                <h3>频道10的QA数据</h3>
                <#if channel10QA?? && channel10QA?size gt 0>
                    <div class="list-group">
                        <#list channel10QA as qa>
                            <div class="list-group-item">
                                <h5>ID: ${qa.id}</h5>
                                <p><strong>问题:</strong> ${qa.question}</p>
                                <p><strong>排序:</strong> ${qa.showOrder}</p>
                            </div>
                        </#list>
                    </div>
                <#else>
                    <div class="alert alert-warning">频道10没有QA数据</div>
                </#if>
                
                <h3>频道11的QA数据</h3>
                <#if channel11QA?? && channel11QA?size gt 0>
                    <div class="list-group">
                        <#list channel11QA as qa>
                            <div class="list-group-item">
                                <h5>ID: ${qa.id}</h5>
                                <p><strong>问题:</strong> ${qa.question}</p>
                                <p><strong>排序:</strong> ${qa.showOrder}</p>
                            </div>
                        </#list>
                    </div>
                <#else>
                    <div class="alert alert-warning">频道11没有QA数据</div>
                </#if>
            </div>
        </div>
        
        <div class="mt-4">
            <a href="/channel" class="btn btn-primary">返回频道页面</a>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
