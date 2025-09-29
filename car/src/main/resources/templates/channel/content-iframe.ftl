<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title!''}</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/bootstrap-icons.css" rel="stylesheet">
    <link href="/css/my-pages.css" rel="stylesheet">
    <style>
        body {
            margin: 0;
            padding: 20px;
            background-color: #f8f9fa;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
        }
        
        .iframe-content {
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        
        .channel-content-text {
            line-height: 1.8;
            color: #333;
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
        
        
        /* 响应式设计 */
        @media (max-width: 768px) {
            body {
                padding: 10px;
            }
            
            .iframe-content {
                padding: 20px;
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
        }
    </style>
</head>
<body>
    <div class="iframe-content">
        <#if channelContent??>
            <!-- 频道内容 -->
            <div class="channel-content-text">
                ${channelContent.content!''}
            </div>
        <#else>
            <div class="alert alert-info" role="alert">
                <h4 class="alert-heading">频道内容</h4>
                <p>暂无频道内容，请联系管理员添加。</p>
            </div>
        </#if>
    </div>

    <script src="/js/bootstrap.bundle.min.js"></script>
    <script>
        // 通知父页面iframe高度变化
        function notifyParentHeight() {
            // 使用更精确的高度计算方法
            const contentElement = document.querySelector('.iframe-content');
            if (!contentElement) return;
            
            // 获取内容元素的实际高度，包括padding和border
            const contentHeight = contentElement.offsetHeight;
            
            // 获取body的实际高度
            const bodyHeight = document.body.offsetHeight;
            
            // 使用内容元素高度，如果body高度更大则使用body高度
            const height = Math.max(contentHeight, bodyHeight);
            
            if (window.parent && window.parent !== window) {
                window.parent.postMessage({
                    type: 'iframe-height-change',
                    height: height,
                    channelId: '${channelContent.id!''}'
                }, '*');
            }
        }
        
        // 页面加载完成后通知高度
        document.addEventListener('DOMContentLoaded', function() {
            // 多次尝试获取准确高度
            setTimeout(notifyParentHeight, 100);
            setTimeout(notifyParentHeight, 300);
            setTimeout(notifyParentHeight, 500);
        });
        
        // 图片加载完成后重新计算高度
        window.addEventListener('load', function() {
            setTimeout(notifyParentHeight, 100);
            setTimeout(notifyParentHeight, 300);
        });
        
        
        // 监听窗口大小变化
        window.addEventListener('resize', function() {
            setTimeout(notifyParentHeight, 100);
        });
        
        // 监听来自父页面的高度请求
        window.addEventListener('message', function(event) {
            if (event.data && event.data.type === 'request-height') {
                setTimeout(notifyParentHeight, 50);
            }
        });
    </script>
</body>
</html>
