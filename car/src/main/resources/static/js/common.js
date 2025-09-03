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
