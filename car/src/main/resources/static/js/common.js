/**
 * 通用JavaScript工具函数
 * 用于处理未登录状态和API错误
 */

// 通用未登录处理函数
window.handleUnauthorized = function(response) {
    if (response && response.data && response.data.message) {
        const message = response.data.message;
        if (message.includes('未登录') || message.includes('用户未登录')) {
            console.log('检测到未登录状态，跳转到登录页面');
            // 保存当前页面URL，登录后可以跳转回来
            const currentUrl = window.location.pathname + window.location.search;
            localStorage.setItem('redirectAfterLogin', currentUrl);
            // 跳转到登录页面
            window.location.href = '/login';
            return true; // 表示已处理未登录状态
        }
    }
    return false; // 表示不是未登录状态
};

// 通用API错误处理函数
window.handleApiError = function(error, defaultMessage = '请求失败') {
    if (error.response) {
        const response = error.response;
        // 检查是否是未登录错误
        if (window.handleUnauthorized(response)) {
            return; // 已处理未登录状态，直接返回
        }
        
        // 处理其他HTTP错误
        if (response.status === 401) {
            console.log('401未授权，跳转到登录页面');
            window.handleUnauthorized(response);
            return;
        }
        
        if (response.status === 403) {
            console.log('403禁止访问');
            return '权限不足，无法访问此资源';
        }
        
        if (response.status === 404) {
            console.log('404资源不存在');
            return '请求的资源不存在';
        }
        
        if (response.status >= 500) {
            console.log('服务器错误');
            return '服务器内部错误，请稍后重试';
        }
        
        // 返回服务器返回的错误信息
        if (response.data && response.data.message) {
            return response.data.message;
        }
    }
    
    // 网络错误或其他错误
    if (error.code === 'NETWORK_ERROR' || error.message.includes('Network Error')) {
        return '网络连接失败，请检查网络设置';
    }
    
    return defaultMessage;
};

// 通用成功消息显示函数
window.showSuccessMessage = function(message, duration = 3000) {
    // 创建成功提示元素
    const successDiv = document.createElement('div');
    successDiv.className = 'alert alert-success alert-dismissible fade show position-fixed';
    successDiv.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
    successDiv.innerHTML = `
        <i class="bi bi-check-circle me-2"></i>
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    // 添加到页面
    document.body.appendChild(successDiv);
    
    // 自动移除
    setTimeout(() => {
        if (successDiv.parentNode) {
            successDiv.remove();
        }
    }, duration);
};

// 通用错误消息显示函数
window.showErrorMessage = function(message, duration = 5000) {
    // 创建错误提示元素
    const errorDiv = document.createElement('div');
    errorDiv.className = 'alert alert-danger alert-dismissible fade show position-fixed';
    errorDiv.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
    errorDiv.innerHTML = `
        <i class="bi bi-exclamation-triangle me-2"></i>
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    // 添加到页面
    document.body.appendChild(errorDiv);
    
    // 自动移除
    setTimeout(() => {
        if (errorDiv.parentNode) {
            errorDiv.remove();
        }
    }, duration);
};

// 通用确认对话框函数
window.showConfirmDialog = function(message, onConfirm, onCancel) {
    if (confirm(message)) {
        if (typeof onConfirm === 'function') {
            onConfirm();
        }
    } else {
        if (typeof onCancel === 'function') {
            onCancel();
        }
    }
};

// 检查用户登录状态
window.checkLoginStatus = function() {
    // 这里可以根据实际需求检查登录状态
    // 例如检查localStorage中的token，或者调用API检查
    const token = localStorage.getItem('userToken');
    return !!token;
};

// 获取重定向URL（登录后跳转回来）
window.getRedirectAfterLogin = function() {
    return localStorage.getItem('redirectAfterLogin') || '/';
};

// 清除重定向URL
window.clearRedirectAfterLogin = function() {
    localStorage.removeItem('redirectAfterLogin');
};

// 页面加载完成后自动检查登录状态
document.addEventListener('DOMContentLoaded', function() {
    // 如果当前页面需要登录，但用户未登录，则跳转到登录页面
    const requiresAuth = document.body.hasAttribute('data-requires-auth');
    if (requiresAuth && !window.checkLoginStatus()) {
        console.log('页面需要登录，但用户未登录，跳转到登录页面');
        window.location.href = '/login';
    }
});

/**
 * 加载HTML内容到iframe并自动调整高度
 * @param {HTMLIFrameElement} iframe - iframe元素对象
 * @param {string} htmlContent - 要加载的HTML文本内容
 * @param {Object} options - 可选配置参数
 * @param {number} options.minHeight - 最小高度（默认200px）
 * @param {number} options.maxHeight - 最大高度（默认2000px）
 * @param {number} options.adjustDelay - 调整延迟时间（默认100ms）
 * @param {boolean} options.removeScrollbars - 是否移除滚动条（默认true）
 */
window.loadHtmlToIframe = function(iframe, htmlContent, options = {}) {
    // 参数验证
    if (!iframe || !(iframe instanceof HTMLIFrameElement)) {
        console.error('loadHtmlToIframe: 第一个参数必须是iframe元素');
        return;
    }
    
    if (!htmlContent || typeof htmlContent !== 'string') {
        console.error('loadHtmlToIframe: 第二个参数必须是HTML文本字符串');
        return;
    }
    
    // 默认配置
    const config = {
        minHeight: 200,
        maxHeight: 2000,
        adjustDelay: 100,
        removeScrollbars: true,
        ...options
    };
    
    // 设置iframe样式
    iframe.style.border = 'none';
    iframe.style.width = '100%';
    iframe.style.minHeight = config.minHeight + 'px';
    
    if (config.removeScrollbars) {
        iframe.style.overflow = 'hidden';
    }
    
    // 创建完整的HTML文档
    const fullHtml = `
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    margin: 0;
                    padding: 10px;
                    font-family: Arial, sans-serif;
                    line-height: 1.6;
                    word-wrap: break-word;
                }
                img {
                    max-width: 100%;
                    height: auto;
                }
                table {
                    max-width: 100%;
                    overflow-x: auto;
                }
                /* Video和YouTube视频样式 */
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
                iframe[src*="youtube.com"],
                iframe[src*="youtu.be"],
                iframe[src*="youtube-nocookie.com"] {
                    aspect-ratio: 16 / 9;
                    width: 99%;
                    max-width: 99%;
                }
            </style>
        </head>
        <body>
            ${htmlContent}
        </body>
        </html>
    `;
    
    // 调整高度的函数
    const adjustHeight = function() {
        setTimeout(() => {
            adjustIframeHeight(iframe, config);
        }, config.adjustDelay);
    };
    
    // 加载HTML内容
    try {
        // 使用srcdoc属性加载HTML内容
        iframe.srcdoc = fullHtml;
        
        // 监听iframe加载完成事件
        iframe.onload = function() {
            adjustHeight();
        };
        
        // 如果iframe已经加载完成，直接调整高度
        if (iframe.contentDocument && iframe.contentDocument.readyState === 'complete') {
            adjustHeight();
        }
        
    } catch (error) {
        console.error('loadHtmlToIframe: 加载HTML内容失败', error);
        // 降级方案：使用src属性加载data URL
        try {
            const dataUrl = 'data:text/html;charset=utf-8,' + encodeURIComponent(fullHtml);
            iframe.src = dataUrl;
            
            iframe.onload = function() {
                adjustHeight();
            };
        } catch (fallbackError) {
            console.error('loadHtmlToIframe: 降级方案也失败', fallbackError);
        }
    }
    
    // 监听窗口大小变化，重新调整iframe高度
    let resizeTimer;
    const handleResize = function() {
        clearTimeout(resizeTimer);
        resizeTimer = setTimeout(function() {
            adjustHeight();
        }, 300); // 防抖，300ms后执行
    };
    
    window.addEventListener('resize', handleResize);
    
    // 监听设备方向变化（移动设备）
    window.addEventListener('orientationchange', function() {
        setTimeout(function() {
            adjustHeight();
        }, 500);
    });
    
    // 监听媒体查询变化（PC转手机版）
    if (window.matchMedia) {
        const mediaQuery = window.matchMedia('(max-width: 768px)');
        let lastIsMobile = mediaQuery.matches;
        
        mediaQuery.addEventListener('change', function(e) {
            const isMobile = e.matches;
            if (isMobile !== lastIsMobile) {
                lastIsMobile = isMobile;
                setTimeout(function() {
                    adjustHeight();
                }, 300);
            }
        });
    }
    
    // 将调整函数保存到iframe元素上，以便外部可以调用
    iframe._adjustHeight = adjustHeight;
};

/**
 * 调整iframe高度
 * @param {HTMLIFrameElement} iframe - iframe元素
 * @param {Object} config - 配置参数
 */
function adjustIframeHeight(iframe, config) {
    try {
        // 获取iframe内容的高度
        const iframeDoc = iframe.contentDocument || iframe.contentWindow.document;
        const body = iframeDoc.body;
        const html = iframeDoc.documentElement;
        
        if (!body || !html) {
            console.warn('adjustIframeHeight: 无法获取iframe内容');
            return;
        }
        
        // 计算内容高度
        const contentHeight = Math.max(
            body.scrollHeight,
            body.offsetHeight,
            html.clientHeight,
            html.scrollHeight,
            html.offsetHeight
        );
        
        // 添加一些边距
        const finalHeight = Math.min(
            Math.max(contentHeight + 20, config.minHeight),
            config.maxHeight
        );
        
        // 设置iframe高度
        iframe.style.height = finalHeight + 'px';
        
        console.log(`iframe高度已调整为: ${finalHeight}px`);
        
        // 如果内容高度超过最大高度，显示滚动条
        if (contentHeight > config.maxHeight - 20) {
            iframe.style.overflow = 'auto';
            console.warn('内容高度超过最大限制，显示滚动条');
        }
        
    } catch (error) {
        console.error('adjustIframeHeight: 调整高度失败', error);
        // 设置默认高度
        iframe.style.height = config.minHeight + 'px';
    }
}