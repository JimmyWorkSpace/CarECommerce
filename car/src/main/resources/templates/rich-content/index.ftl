<link href="/css/about.css" rel="stylesheet">

<div class="about-page">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <!-- iframe容器 -->
                <div class="iframe-container">
                    <iframe id="rich-content-iframe" 
                            src="/rich-content/${richContent.id}/content" 
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
    const iframe = document.getElementById('rich-content-iframe');
    const contentId = '${richContent.id}';
    
    // 调整iframe高度的函数
    function adjustIframeHeight(height) {
        if (!iframe) return;
        
        // 设置最小高度为400px，最大高度为2000px
        const minHeight = 400;
        const maxHeight = 2000;
        const finalHeight = Math.max(minHeight, Math.min(maxHeight, height));
        
        iframe.style.height = finalHeight + 'px';
        console.log('调整富文本内容iframe高度:', '原始高度:', height, '最终高度:', finalHeight);
    }
    
    // 请求iframe重新计算高度
    function requestIframeHeight() {
        if (!iframe) return;
        
        try {
            iframe.contentWindow.postMessage({type: 'request-height'}, '*');
        } catch (e) {
            console.log('无法访问富文本内容iframe内容:', e);
        }
    }
    
    // 监听来自iframe的消息
    window.addEventListener('message', function(event) {
        if (event.data && event.data.type === 'iframe-height-change') {
            const msgContentId = event.data.contentId;
            const height = event.data.height;
            
            if (msgContentId === contentId) {
                adjustIframeHeight(height);
            }
        }
    });
    
    // 页面加载完成后，为iframe请求高度
    setTimeout(requestIframeHeight, 500);
    
    // 监听窗口大小变化
    let resizeTimer;
    window.addEventListener('resize', function() {
        clearTimeout(resizeTimer);
        resizeTimer = setTimeout(function() {
            console.log('窗口大小变化，重新请求iframe高度');
            requestIframeHeight();
        }, 300); // 防抖，300ms后执行
    });
    
    // 监听设备方向变化（移动设备）
    window.addEventListener('orientationchange', function() {
        console.log('设备方向变化，重新请求iframe高度');
        // 方向变化后延迟执行，等待布局完成
        setTimeout(function() {
            requestIframeHeight();
        }, 500);
    });
    
    // 监听媒体查询变化（PC转手机版）- 切换时刷新页面
    if (window.matchMedia) {
        const mediaQuery = window.matchMedia('(max-width: 768px)');
        let lastIsMobile = mediaQuery.matches;
        
        console.log('[富文本详情页] 媒体查询监听初始化');
        console.log('[富文本详情页] 当前窗口宽度:', window.innerWidth);
        console.log('[富文本详情页] 初始是否为手机版:', lastIsMobile);
        console.log('[富文本详情页] 媒体查询匹配:', mediaQuery.matches);
        
        mediaQuery.addEventListener('change', function(e) {
            console.log('[富文本详情页] 媒体查询change事件触发');
            console.log('[富文本详情页] 事件对象:', e);
            console.log('[富文本详情页] 当前窗口宽度:', window.innerWidth);
            console.log('[富文本详情页] 媒体查询匹配结果:', e.matches);
            console.log('[富文本详情页] 上次状态 (lastIsMobile):', lastIsMobile);
            console.log('[富文本详情页] 当前状态 (isMobile):', e.matches);
            
            const isMobile = e.matches;
            if (isMobile !== lastIsMobile) {
                console.log('[富文本详情页] 检测到PC/手机版切换！');
                console.log('[富文本详情页] 从', lastIsMobile ? '手机版' : 'PC版', '切换到', isMobile ? '手机版' : 'PC版');
                console.log('[富文本详情页] 准备刷新页面...');
                lastIsMobile = isMobile;
                // 刷新当前页面
                window.location.reload();
            } else {
                console.log('[富文本详情页] 状态未变化，不刷新页面');
            }
        });
        
        // 也监听窗口大小变化，用于调试
        window.addEventListener('resize', function() {
            console.log('[富文本详情页] 窗口大小变化 - 宽度:', window.innerWidth, '媒体查询匹配:', mediaQuery.matches);
        });
    } else {
        console.warn('[富文本详情页] window.matchMedia 不支持，无法监听媒体查询变化');
    }
});
</script>
