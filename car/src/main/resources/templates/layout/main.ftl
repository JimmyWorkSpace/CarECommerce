<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">

    <title>${title}</title>

    <meta name="title" content="${ogTitle}" />
    <meta name="description" content="${ogDescription}" />
    <meta name="keywords" content="${ogDescription}" />

    <meta name="robots" content="index, follow">
    
    <meta property="og:title" content="${ogTitle}" />
    <meta property="og:description" content="${ogDescription}" />
    <meta property="og:url" content="${ogUrl!''}" />
    <meta property="og:image" content="${ogImage!''}" />
    <meta property="og:type" content="website" />
    <meta property="og:locate" content="zh_TW" />
    <meta property="og:site_name" content="車勢汽車交易網 CarBuy" />

    <link rel="canonical" href="${ogUrl!''}">
    <link rel="alternate" hreflang="zh-TW" href="${ogUrl!''}">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <link rel="shortcut icon" href="/img/favicon.ico" type="image/x-icon">
    <!-- Bootstrap CSS -->
    <link href="/assets/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="/assets/bootstrap-icons/bootstrap-icons.min.css" rel="stylesheet">
    <link href="/css/common.css" rel="stylesheet">
    <link rel="stylesheet" href="/assets/slick/slick.css">
    <link rel="stylesheet" href="/assets/slick/slick-theme.css">
    <link rel="stylesheet" href="/assets/viewer/viewer.min.css">
        
    <script src="/assets/viewer/viewer.min.js"></script>
    <!-- Vue2 -->
    <script src="/js/vue.min.js"></script>
    <!-- Axios -->
    <script src="/js/axios.min.js"></script>
    <!-- jQuery -->
    <script src="/js/jquery.min.js"></script>
    <!-- iFrameResize -->
    <script src="/assets/iframe-resizer/iframe-resizer.parent.js"></script>
    <!-- Common JavaScript -->
    <script src="/js/common.js"></script>
    <!-- Bootstrap JS -->
    <script src="/assets/bootstrap/js/bootstrap.bundle.min.js"></script>
    <!-- Custom CSS -->
    <script src="/assets/slick/slick.min.js"> </script>
    <script>
        // 高亮當前頁面的導航項
        document.addEventListener('DOMContentLoaded', function() {
            const currentPath = window.location.pathname;
            const navLinks = document.querySelectorAll('.navbar-nav .nav-link');
            
            navLinks.forEach(link => {
                if (link.getAttribute('href') === currentPath) {
                    link.parentElement.classList.add('active');
                }
            });
            
            // 所有有子菜单的主菜单点击处理（PC和移动端都适用）
            const dropdownToggles = document.querySelectorAll('.navbar-nav .dropdown-toggle');
            dropdownToggles.forEach(toggle => {
                toggle.addEventListener('click', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    
                    // 如果是移动端，使用自定义处理
                    if (window.innerWidth <= 991.98) {
                        const dropdown = this.closest('.dropdown');
                        const isOpen = dropdown.classList.contains('show');
                        
                        // 关闭所有其他下拉菜单
                        document.querySelectorAll('.navbar-nav .dropdown').forEach(d => {
                            if (d !== dropdown) {
                                d.classList.remove('show');
                                const menu = d.querySelector('.dropdown-menu');
                                if (menu) {
                                    menu.classList.remove('show');
                                }
                            }
                        });
                        
                        // 切换当前下拉菜单
                        if (isOpen) {
                            dropdown.classList.remove('show');
                            const menu = dropdown.querySelector('.dropdown-menu');
                            if (menu) {
                                menu.classList.remove('show');
                            }
                            this.setAttribute('aria-expanded', 'false');
                        } else {
                            dropdown.classList.add('show');
                            const menu = dropdown.querySelector('.dropdown-menu');
                            if (menu) {
                                menu.classList.add('show');
                            }
                            this.setAttribute('aria-expanded', 'true');
                        }
                    }
                    // PC端由Bootstrap的data-bs-toggle="dropdown"处理，但确保不跳转
                });
            });
            
            // 点击其他地方时关闭下拉菜单（移动端）
            if (window.innerWidth <= 991.98) {
                document.addEventListener('click', function(e) {
                    if (!e.target.closest('.dropdown')) {
                        document.querySelectorAll('.navbar-nav .dropdown').forEach(dropdown => {
                            dropdown.classList.remove('show');
                            const menu = dropdown.querySelector('.dropdown-menu');
                            if (menu) {
                                menu.classList.remove('show');
                            }
                            const toggle = dropdown.querySelector('.dropdown-toggle');
                            if (toggle) {
                                toggle.setAttribute('aria-expanded', 'false');
                            }
                        });
                    }
                });
            }
        });
            window.doFrameResize = function(frameId, htmlContent) {
                const iframe = document.getElementById(frameId);
                
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

                iframeResize({
                    license: "GPLv3",
                    log: true      // 开启调试日志
                }, '#' + frameId);
            }
    </script>
</head>
<body>
        <!-- 頭部導航 -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
            <div class="container">
                <a class="navbar-brand" href="/">
                    <div class="brand-text">
                        <span class="brand-main">車勢汽車交易網</span>
                        <span class="brand-sub">最保障消費者的二手車平台</span>
                    </div>
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav ms-auto">
                        <#if menus?? && menus?has_content>
                            <#list menus as menu>
                                <#if menu.children?? && menu.children?has_content>
                                    <!-- 有子菜单的主菜单，显示下拉菜单 -->
                                    <li class="nav-item dropdown">
                                        <a class="nav-link dropdown-toggle" href="javascript:void(0);" role="button" data-bs-toggle="dropdown" aria-expanded="false" onclick="event.preventDefault();">
                                            ${menu.title!''}
                                        </a>
                                        <ul class="dropdown-menu">
                                            <#list menu.children as child>
                                                <li>
                                                    <#if child.linkType?? && child.linkType == 1>
                                                        <!-- 富文本类型子菜单，跳转到富文本显示页面 -->
                                                        <a class="dropdown-item" href="/menu-content/${child.id}">${child.title!''}</a>
                                                    <#else>
                                                        <!-- 普通链接类型子菜单，跳转到指定URL -->
                                                        <a class="dropdown-item" href="${child.linkUrl!''}">${child.title!''}</a>
                                                    </#if>
                                                </li>
                                            </#list>
                                        </ul>
                                    </li>
                                <#else>
                                    <!-- 没有子菜单的普通菜单 -->
                                    <li class="nav-item">
                                        <#if menu.linkType?? && menu.linkType == 1>
                                            <!-- 富文本类型菜单，跳转到富文本显示页面 -->
                                            <a class="nav-link" href="/menu-content/${menu.id}">${menu.title!''}</a>
                                        <#else>
                                            <!-- 普通链接类型菜单，跳转到指定URL -->
                                            <a class="nav-link" href="${menu.linkUrl!''}">${menu.title!''}</a>
                                        </#if>
                                    </li>
                                </#if>
                            </#list>
                        </#if>
                        <#if user?? && user?has_content>
                            <li class="nav-item">
                                <a class="nav-link" href="/cart">
                                    <i class="bi bi-cart3"></i>
                                    購物車
                                </a>
                            </li>
                            <li class="nav-item dropdown">
                                <a class="nav-link user-status dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                    <i class="bi bi-person-circle"></i>
                                </a>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item" href="/my-order/index">
                                        <i class="bi bi-list-ul me-2"></i>我的訂單
                                    </a></li>
                                    <li><a class="dropdown-item" href="/appointment/my-appointments">
                                        <i class="bi bi-calendar-check me-2"></i>我的預約
                                    </a></li>
                                    <li><a class="dropdown-item" href="/my-reports">
                                        <i class="bi bi-flag me-2"></i>我的檢舉
                                    </a></li>
                                    <li><a class="dropdown-item" href="/register">
                                        <i class="bi bi-key me-2"></i>修改密碼
                                    </a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="#" id="logoutBtn">
                                        <i class="bi bi-box-arrow-right me-2"></i>退出登錄
                                    </a></li>
                                </ul>
                            </li>
                        <#else>
                            <li class="nav-item">
                                <a class="nav-link login-btn" href="/login">登錄/註冊</a>
                            </li>
                        </#if>
                    </ul>
                </div>
            </div>
        </nav>
        
        <!-- 主要內容區域 -->
        <div class="container mt-4 main-container">
            <#if content?? && content?has_content>
                <#include "${content}">
            <#else>
                <div class="error-container">
                    <h2>頁面內容加載失敗</h2>
                    <p>抱歉，頁面內容無法正常顯示。</p>
                    <a href="/" class="btn btn-primary">返回首頁</a>
                </div>
            </#if>
        </div>
        
        <!-- 通用页脚 -->
        <#include "/layout/footer.ftl">
        
        <!-- 購物車懸浮按鈕 -->
        <#if user?? && user?has_content>
        <div class="cart-float-btn" id="cartFloatBtn">
            <div class="cart-icon">
                <i class="bi bi-cart3"></i>
                <span class="cart-count" id="cartCount">0</span>
            </div>
        </div>
        </#if>
        
        <!-- 通用登录弹窗 -->
        <div class="modal fade" id="loginModal" tabindex="-1" aria-labelledby="loginModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header" style="background: linear-gradient(135deg, #5ACFC9 0%, #4AB8B2 100%); color: white; border-radius: 15px 15px 0 0;">
                        <h5 class="modal-title text-start" id="loginModalLabel">
                            <i class="bi bi-person-circle me-2"></i>用戶登錄
                        </h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body p-4">
                        <div id="loginError" class="alert alert-danger" role="alert" style="display: none;">
                            <i class="bi bi-exclamation-triangle me-2"></i>
                            <span id="loginErrorText"></span>
                        </div>
                        
                        <form id="loginModalForm">
                            <div class="mb-3">
                                <label for="loginPhoneNumber" class="form-label">
                                    <i class="bi bi-phone me-2"></i>手機號碼
                                </label>
                                <div class="form-text">必須為10碼數字，以09開頭</div>
                                <input type="tel" class="form-control" id="loginPhoneNumber" 
                                       placeholder="請輸入手機號碼（09開頭）" required maxlength="10">
                            </div>
                            
                            <div class="mb-3">
                                <label for="loginPassword" class="form-label">
                                    <i class="bi bi-lock me-2"></i>密碼
                                </label>
                                <input type="password" class="form-control" id="loginPassword" 
                                       placeholder="請輸入密碼" required>
                            </div>
                        </form>
                        
                        <div class="text-center mt-3">
                            <small class="text-muted">
                                <a href="/register" class="text-decoration-none">立即註冊/忘記密碼</a>
                            </small>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                        <button type="button" class="btn btn-primary" id="loginSubmitBtn">
                            <i class="bi bi-box-arrow-in-right me-2"></i>登錄
                        </button>
                    </div>
                </div>
            </div>
        </div>

        
        <script>
            window.doFrameResize = function(frameId, htmlContent) {
                const iframe = document.getElementById(frameId);
                
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

                iframeResize({
                    license: "GPLv3",
                    log: true      // 开启调试日志
                }, '#' + frameId);
            }
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
            
            // 購物車功能
            let cart = JSON.parse(localStorage.getItem('cart') || '[]');
            
            // 全局購物車狀態管理
            window.cartManager = {
                // 更新購物車數量顯示
                updateCartCount: function() {
                    const count = cart.reduce((total, item) => total + (item.quantity || 0), 0);
                    const cartCountElement = document.getElementById('cartCount');
                    if (cartCountElement) {
                        cartCountElement.textContent = count;
                    }
                },
                
                // 從服務器同步購物車數據
                syncCartFromServer: async function() {
                    // 防止重复调用
                    if (this.isSyncing) {
                        console.log('正在同步中，跳过重复调用');
                        return;
                    }
                    
                    try {
                        this.isSyncing = true;
                        console.log('开始同步购物车数据...');
                        
                        const response = await fetch('/api/shopping-cart/list');
                        if (response.ok) {
                            const data = await response.json();
                            if (data.success && data.data) {
                                // 轉換服務器數據格式為本地格式
                                cart = data.data.map(item => ({
                                    id: item.id,
                                    name: item.productName,
                                    price: item.productPrice,
                                    image: item.productImage,
                                    quantity: item.productAmount
                                }));
                                localStorage.setItem('cart', JSON.stringify(cart));
                                this.updateCartCount();
                                console.log('购物车数据同步成功，商品数量:', cart.length);
                            }
                        }
                    } catch (error) {
                        console.log('同步購物車數據失敗:', error);
                    } finally {
                        this.isSyncing = false;
                    }
                },
                
                // 添加商品到購物車
                addToCart: function(product) {
                    const existingItem = cart.find(item => item.id === product.id);
                    if (existingItem) {
                        existingItem.quantity += 1;
                    } else {
                        cart.push({
                            id: product.id,
                            name: product.name,
                            price: product.price,
                            image: product.image,
                            quantity: 1
                        });
                    }
                    localStorage.setItem('cart', JSON.stringify(cart));
                    this.updateCartCount();
                    this.showToast('商品已添加到購物車');
                },
                
                // 顯示Toast提示
                showToast: function(message) {
                    const toast = document.createElement('div');
                    toast.className = 'toast-notification';
                    toast.textContent = message;
                    document.body.appendChild(toast);
                    
                    setTimeout(() => {
                        toast.remove();
                    }, 2000);
                },
                
                // 刷新購物車數量
                refreshCartCount: function() {
                    // 避免重复调用，只在需要时同步
                    if (!this.isSyncing) {
                        this.syncCartFromServer();
                    }
                }
            };
            
            // 頁面加載完成後的初始化
            document.addEventListener('DOMContentLoaded', function() {
                // 初始化購物車數量
                window.cartManager.updateCartCount();
                
                // 從服務器同步購物車數據
                window.cartManager.syncCartFromServer();
                
                // 購物車按鈕點擊事件
                const cartFloatBtn = document.getElementById('cartFloatBtn');
                if (cartFloatBtn) {
                    cartFloatBtn.addEventListener('click', function() {
                        window.location.href = '/cart';
                    });
                }
                
                // 監聽購物車數據變化事件
                window.addEventListener('cartUpdated', function() {
                    window.cartManager.refreshCartCount();
                });
                
                // 退出登錄按鈕事件
                const logoutBtn = document.getElementById('logoutBtn');
                if (logoutBtn) {
                    logoutBtn.addEventListener('click', function(e) {
                        e.preventDefault();
                        
                        // 清除本地存储的購物車數據
                        localStorage.removeItem('cart');
                        localStorage.removeItem('redirectAfterLogin');
                        
                        // 跳轉到退出登錄接口
                        window.location.href = '/logout';
                    });
                }
            });
            
            // 暴露給全局使用
            window.updateCartCount = function() {
                window.cartManager.updateCartCount();
            };
            
            window.refreshCartCount = function() {
                window.cartManager.refreshCartCount();
            };
            
            // 通用登录管理器
            window.loginManager = {
                loginPhoneNumber: '',
                loginPassword: '',
                loginSubmitting: false,
                pendingAction: null,
                
                // 检查用户是否已登录
                isLoggedIn: function() {
                    // 通过检查页面元素判断是否登录
                    // 如果存在退出登录按钮或用户状态显示，说明已登录
                    const logoutBtn = document.getElementById('logoutBtn');
                    const userStatus = document.querySelector('.user-status');
                    return !!(logoutBtn || userStatus);
                },
                
                // 显示登录弹窗
                showLoginModal: function(pendingAction) {
                    this.pendingAction = pendingAction || null;
                    const loginModal = new bootstrap.Modal(document.getElementById('loginModal'));
                    loginModal.show();
                },
                
                // 初始化登录弹窗
                init: function() {
                    const self = this;
                    
                    // 手机号输入限制（台湾手机号：10码，以09开头）
                    const phoneInput = document.getElementById('loginPhoneNumber');
                    if (phoneInput) {
                        phoneInput.setAttribute('maxlength', '10');
                        phoneInput.addEventListener('input', function(e) {
                            let value = e.target.value.replace(/\D/g, '');
                            // 台湾手机号：限制为10码
                            if (value.length > 10) {
                                value = value.substring(0, 10);
                            }
                            self.loginPhoneNumber = value;
                            e.target.value = value;
                        });
                    }
                    
                    // 密码输入
                    const passwordInput = document.getElementById('loginPassword');
                    if (passwordInput) {
                        passwordInput.addEventListener('input', function(e) {
                            self.loginPassword = e.target.value;
                        });
                    }
                    
                    // 提交登录按钮
                    const submitBtn = document.getElementById('loginSubmitBtn');
                    if (submitBtn) {
                        submitBtn.addEventListener('click', function() {
                            self.submitLoginModal();
                        });
                    }
                    
                    // 监听弹窗关闭事件，清空表单
                    const loginModalElement = document.getElementById('loginModal');
                    if (loginModalElement) {
                        loginModalElement.addEventListener('hidden.bs.modal', function() {
                            self.resetLoginForm();
                        });
                    }
                },
                
                
                // 显示Toast提示
                showToast: function(message, type) {
                    type = type || 'info';
                    // 创建toast容器（如果不存在）
                    let toastContainer = document.getElementById('toastContainer');
                    if (!toastContainer) {
                        toastContainer = document.createElement('div');
                        toastContainer.id = 'toastContainer';
                        toastContainer.className = 'toast-container position-fixed top-0 end-0 p-3';
                        toastContainer.style.zIndex = '9999';
                        document.body.appendChild(toastContainer);
                    }
                    
                    // 创建toast元素
                    const toastId = 'toast-' + Date.now();
                    const toast = document.createElement('div');
                    toast.id = toastId;
                    toast.className = 'toast';
                    toast.setAttribute('role', 'alert');
                    toast.setAttribute('aria-live', 'assertive');
                    toast.setAttribute('aria-atomic', 'true');
                    
                    // 根据类型设置样式
                    const bgClass = type === 'success' ? 'bg-success' : type === 'danger' ? 'bg-danger' : type === 'warning' ? 'bg-warning' : 'bg-info';
                    const textClass = type === 'warning' ? 'text-dark' : 'text-white';
                    
                    // 根据类型设置图标
                    let iconClass = 'info-circle';
                    if (type === 'success') {
                        iconClass = 'check-circle';
                    } else if (type === 'danger' || type === 'warning') {
                        iconClass = 'exclamation-triangle';
                    }
                    
                    toast.innerHTML = '<div class="toast-header ' + bgClass + ' ' + textClass + '">' +
                        '<i class="bi bi-' + iconClass + ' me-2"></i>' +
                        '<strong class="me-auto">提示</strong>' +
                        '<button type="button" class="btn-close btn-close-white" data-bs-dismiss="toast" aria-label="Close"></button>' +
                        '</div>' +
                        '<div class="toast-body">' +
                        message +
                        '</div>';
                    
                    toastContainer.appendChild(toast);
                    
                    // 初始化并显示toast
                    const bsToast = new bootstrap.Toast(toast, {
                        autohide: true,
                        delay: 3000
                    });
                    bsToast.show();
                    
                    // toast关闭后移除元素
                    toast.addEventListener('hidden.bs.toast', function() {
                        toast.remove();
                    });
                },
                
                
                // 显示登录错误
                showLoginError: function(message) {
                    const errorDiv = document.getElementById('loginError');
                    const errorText = document.getElementById('loginErrorText');
                    if (errorDiv && errorText) {
                        errorText.textContent = message;
                        errorDiv.style.display = 'block';
                    }
                },
                
                // 隐藏登录错误
                hideLoginError: function() {
                    const errorDiv = document.getElementById('loginError');
                    if (errorDiv) {
                        errorDiv.style.display = 'none';
                    }
                },
                
                // 提交登录
                submitLoginModal: async function() {
                    const phone = this.loginPhoneNumber.trim();
                    const password = this.loginPassword.trim();
                    
                    if (!phone) {
                        this.showLoginError('請輸入手機號碼');
                        return;
                    }
                    
                    // 验证手机号格式（台湾手机号：10码，以09开头）
                    if (!/^09\d{8}$/.test(phone)) {
                        this.showLoginError('請輸入正確的手機號碼格式（10碼數字，以09開頭）');
                        return;
                    }
                    
                    if (!password) {
                        this.showLoginError('請輸入密碼');
                        return;
                    }
                    
                    this.loginSubmitting = true;
                    this.hideLoginError();
                    
                    const submitBtn = document.getElementById('loginSubmitBtn');
                    if (submitBtn) {
                        submitBtn.disabled = true;
                        submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>登錄中...';
                    }
                    
                    // 保存待执行的操作
                    if (this.pendingAction) {
                        sessionStorage.setItem('pendingAction', this.pendingAction);
                    }
                    
                    // 创建隐藏的表单并提交（因为后端返回重定向，axios无法处理）
                    const form = document.createElement('form');
                    form.method = 'POST';
                    form.action = '/login';
                    form.style.display = 'none';
                    
                    const phoneInput = document.createElement('input');
                    phoneInput.type = 'hidden';
                    phoneInput.name = 'phoneNumber';
                    phoneInput.value = phone;
                    form.appendChild(phoneInput);
                    
                    const passwordInput = document.createElement('input');
                    passwordInput.type = 'hidden';
                    passwordInput.name = 'password';
                    passwordInput.value = password;
                    form.appendChild(passwordInput);
                    
                    // 添加当前页面URL作为返回地址
                    const returnUrlInput = document.createElement('input');
                    returnUrlInput.type = 'hidden';
                    returnUrlInput.name = 'returnUrl';
                    returnUrlInput.value = window.location.pathname + window.location.search;
                    form.appendChild(returnUrlInput);
                    
                    document.body.appendChild(form);
                    form.submit();
                },
                
                // 重置登录表单
                resetLoginForm: function() {
                    this.loginPhoneNumber = '';
                    this.loginPassword = '';
                    this.loginSubmitting = false;
                    this.hideLoginError();
                    
                    const phoneInput = document.getElementById('loginPhoneNumber');
                    const passwordInput = document.getElementById('loginPassword');
                    const submitBtn = document.getElementById('loginSubmitBtn');
                    
                    if (phoneInput) phoneInput.value = '';
                    if (passwordInput) passwordInput.value = '';
                    if (submitBtn) {
                        submitBtn.disabled = false;
                        submitBtn.innerHTML = '<i class="bi bi-box-arrow-in-right me-2"></i>登錄';
                    }
                }
            };
            
            // 检查登录状态，如果未登录则显示登录弹窗
            window.checkLoginAndShowModal = function(pendingAction) {
                if (!window.loginManager.isLoggedIn()) {
                    window.loginManager.showLoginModal(pendingAction);
                    return false;
                }
                return true;
            };
            
            // 页面加载完成后初始化登录管理器
            document.addEventListener('DOMContentLoaded', function() {
                window.loginManager.init();
                
                // 检查是否有待执行的操作（登录后返回）
                const pendingAction = sessionStorage.getItem('pendingAction');
                if (pendingAction) {
                    sessionStorage.removeItem('pendingAction');
                    // 延迟执行，确保页面已完全加载
                    setTimeout(() => {
                        // 触发自定义事件，让页面自己处理待执行的操作
                        window.dispatchEvent(new CustomEvent('pendingAction', { detail: { action: pendingAction } }));
                    }, 500);
                }
            });
        </script>
        
        <style>
        /* 登录弹窗样式 */
        #loginModal .modal-content {
            border-radius: 15px;
            border: none;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
        }

        #loginModal .modal-header {
            border-radius: 15px 15px 0 0;
            border: none;
        }

        #loginModal .form-control {
            border-radius: 10px;
            border: 2px solid #e9ecef;
            padding: 0.75rem 1rem;
            transition: all 0.3s ease;
        }

        #loginModal .form-control:focus {
            border-color: #5ACFC9;
            box-shadow: 0 0 0 0.2rem rgba(90, 207, 201, 0.25);
        }

        #loginModal .btn-primary {
            background: linear-gradient(135deg, #5ACFC9 0%, #4AB8B2 100%);
            border: none;
            border-radius: 10px;
            padding: 0.75rem 1.5rem;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        #loginModal .btn-primary:hover:not(:disabled) {
            background: linear-gradient(135deg, #4AB8B2 0%, #3AA7A1 100%);
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(90, 207, 201, 0.4);
        }

        #loginModal .btn-primary:disabled {
            opacity: 0.6;
            cursor: not-allowed;
        }

        #loginModal .alert {
            border-radius: 10px;
            border: none;
        }

        #loginModal .input-group .btn {
            border-radius: 0 10px 10px 0;
        }

        #loginModal .input-group .form-control {
            border-radius: 10px 0 0 10px;
        }
        </style>

</body>
</html>
