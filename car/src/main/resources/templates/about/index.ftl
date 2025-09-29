<link href="/css/about.css" rel="stylesheet">

<div class="about-page">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <!-- iframe容器 -->
                <div class="iframe-container">
                    <iframe id="about-iframe" 
                            src="/about/content" 
                            frameborder="0" 
                            scrolling="no"
                            style="width: 100%; height: 600px; border: none; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                    </iframe>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
    /* iframe容器样式 */
    .iframe-container {
        position: relative;
        min-height: 400px;
    }
    
    .iframe-container iframe {
        transition: height 0.3s ease;
    }
</style>

<script>
// iframe高度自适应功能
document.addEventListener('DOMContentLoaded', function() {
    // 监听来自iframe的消息
    window.addEventListener('message', function(event) {
        if (event.data && event.data.type === 'iframe-height-change') {
            const channelId = event.data.channelId;
            const height = event.data.height;
            const iframe = document.getElementById('about-iframe');
            
            if (iframe && channelId === 'about') {
                // 设置最小高度为400px，最大高度为2000px
                const minHeight = 400;
                const maxHeight = 2000;
                const finalHeight = Math.max(minHeight, Math.min(maxHeight, height)); // 不使用额外缓冲
                
                iframe.style.height = finalHeight + 'px';
                console.log('调整关于页面iframe高度:', '原始高度:', height, '最终高度:', finalHeight);
            }
        }
    });
    
    // 页面加载完成后，为iframe请求高度
    setTimeout(function() {
        const iframe = document.getElementById('about-iframe');
        if (iframe) {
            try {
                iframe.contentWindow.postMessage({type: 'request-height'}, '*');
            } catch (e) {
                console.log('无法访问关于页面iframe内容:', e);
            }
        }
    }, 500);
});
</script> 