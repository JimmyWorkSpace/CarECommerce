<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">

    <title>${title}</title>

    <meta name="title" content="${ogDescription}" />
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
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="canonical" href="${ogUrl!''}">
    <link rel="alternate" hreflang="zh-TW" href="${ogUrl!''}">
    
    <link rel="shortcut icon" href="/img/favicon.ico" type="image/x-icon">
    <!-- Bootstrap CSS -->
    <link href="/assets/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="/assets/bootstrap-icons/bootstrap-icons.min.css" rel="stylesheet">
    <link href="/css/common.css" rel="stylesheet">
    <link rel="stylesheet" href="/assets/slick/slick.css">
    <link rel="stylesheet" href="/assets/slick/slick-theme.css">
    <link rel="stylesheet" href="/assets/viewer/viewer.min.css">
    
            <style>
        /* 購物車懸浮按鈕樣式 */
        .cart-float-btn {
            position: fixed;
            right: 30px;
            top: 50%;
            transform: translateY(-50%);
            z-index: 1000;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .cart-float-btn:hover {
            transform: translateY(-50%) scale(1.1);
        }
        
        .cart-icon {
            background: linear-gradient(135deg, #5ACFC9 0%, #4AB8B2 100%);
            color: white;
            width: 60px;
            height: 60px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            box-shadow: 0 4px 12px rgba(90, 207, 201, 0.3);
            position: relative;
            font-size: 1.5rem;
        }
        
        .cart-count {
            position: absolute;
            top: -5px;
            right: -5px;
            background: #ff4757;
            color: white;
            border-radius: 50%;
            width: 24px;
            height: 24px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 0.8rem;
            font-weight: bold;
            border: 2px solid white;
        }
        
        /* Toast通知样式 */
        .toast-notification {
            position: fixed;
            top: 20px;
            right: 20px;
            background: #5ACFC9;
            color: white;
            padding: 12px 20px;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
            z-index: 1001;
            animation: slideIn 0.3s ease;
        }
        
        @keyframes slideIn {
            from {
                transform: translateX(100%);
                opacity: 0;
            }
            to {
                transform: translateX(0);
                opacity: 1;
            }
        }
        
        /* 已登錄狀態樣式 */
        .user-status {
            background: transparent;
            color: white !important;
            border-radius: 25px;
            padding: 10px 20px !important;
            font-weight: 600;
            font-size: 0.95rem;
            transition: all 0.3s ease;
            border: none;
        }
        
        .user-status:hover {
            background: rgba(255, 255, 255, 0.1);
            color: #f8f9fa !important;
            transform: translateY(-1px);
        }
        
        .user-status i {
            margin-right: 8px;
            font-size: 1.1rem;
        }
        
        /* 登錄按鈕樣式 */
        .login-btn {
            background: linear-gradient(135deg, #5ACFC9 0%, #4AB8B2 100%);
            color: white !important;
            border-radius: 20px;
            padding: 8px 16px !important;
            font-weight: 600;
            font-size: 0.9rem;
            box-shadow: 0 2px 8px rgba(90, 207, 201, 0.3);
            transition: all 0.3s ease;
        }
        
        .login-btn:hover {
            background: linear-gradient(135deg, #4AB8B2 0%, #3AA7A1 100%);
            transform: translateY(-1px);
            box-shadow: 0 4px 12px rgba(90, 207, 201, 0.4);
            color: white !important;
        }
        
        /* 下拉菜單樣式 */
        .dropdown-menu {
            border: none;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
            margin-top: 8px;
        }
        
        .dropdown-item {
            padding: 10px 16px;
            color: #333;
            transition: all 0.3s ease;
        }
        
        .dropdown-item:hover {
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
            color: #5ACFC9;
        }
        
        .dropdown-item i {
            color: #6c757d;
        }
        
        /* 導航欄品牌文字樣式 */
        body .navbar .navbar-brand .brand-text {
            display: flex;
            flex-direction: row;
            align-items: center;
            color: white !important;
            text-decoration: none;
        }
        
        .brand-main {
            font-size: 1.5rem;
            font-weight: bold;
            margin-right: 8px;
            white-space: nowrap;
        }
        
        .brand-sub {
            font-size: 0.9rem;
            font-weight: normal;
            opacity: 0.9;
            white-space: nowrap;
        }
        
        /* 導航欄字體樣式 */
        body .navbar .navbar-nav .nav-link {
            color: white !important;
            font-weight: 600;
            font-size: 1rem;
            transition: all 0.3s ease;
        }
        
        .navbar-nav .nav-link:hover {
            color: #f8f9fa !important;
            transform: translateY(-1px);
        }
        
        body .navbar .navbar-nav .nav-item.active .nav-link {
            color: white !important;
            font-weight: 700;
        }
        
        /* 響應式設計 */
        @media (max-width: 768px) {
            .brand-text {
                flex-direction: column;
                align-items: flex-start;
            }
            
            .brand-main {
                font-size: 1.2rem;
                margin-right: 0;
                margin-bottom: 2px;
            }
            
            .brand-sub {
                font-size: 0.8rem;
                line-height: 1.2;
            }
            
            .cart-float-btn {
                right: 20px;
            }
            
            .cart-icon {
                width: 50px;
                height: 50px;
                font-size: 1.2rem;
            }
            
            .cart-count {
                width: 20px;
                height: 20px;
                font-size: 0.7rem;
            }
            
            .user-status,
            .login-btn {
                padding: 8px 16px !important;
                font-size: 0.85rem;
            }
            
            .user-status i {
                font-size: 1rem;
            }
        }
        </style>
        
    <script src="/assets/viewer/viewer.min.js"></script>
    <!-- Vue2 -->
    <script src="/js/vue.min.js"></script>
    <!-- Axios -->
    <script src="/js/axios.min.js"></script>
    <!-- jQuery -->
    <script src="/js/jquery.min.js"></script>
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
        });
    </script>
</head>
<body>
        <!-- 頭部導航 -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
            <div class="container">
                <a class="navbar-brand" href="/">
                    <div class="brand-text">
                        <span class="brand-main">車勢汽車交易網</span>
                        <span class="brand-sub">最保障消費者的一站式買賣二手車平台</span>
                    </div>
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav ms-auto">
                        <#if menus?? && menus?has_content>
                            <#list menus as menu>
                                <li class="nav-item">
                                    <a class="nav-link" href="${menu.linkUrl!''}">${menu.title!''}</a>
                                </li>
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
                                    <i class="bi bi-person-circle me-1"></i>
                                    ${user.name!'已登錄'}
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
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="/logout">
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
        

        
        <script>
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
            });
            
            // 暴露給全局使用
            window.updateCartCount = function() {
                window.cartManager.updateCartCount();
            };
            
            window.refreshCartCount = function() {
                window.cartManager.refreshCartCount();
            };
        </script>
        

</body>
</html>
