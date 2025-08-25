<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${title!'二手車銷售平台'}</title>
    <meta property="og:title" content="${title!'二手車銷售平台'}" />
    <meta property="og:type" content="website" />
    <meta property="og:description" content="${description!'專業的二手車銷售平台'}" />
    <meta property="og:url" content="${url!''}" />
    <meta property="og:image" content="${image!''}" />
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
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
            color: white !important;
            border-radius: 20px;
            padding: 8px 16px !important;
            font-weight: 600;
            font-size: 0.9rem;
            box-shadow: 0 2px 8px rgba(40, 167, 69, 0.3);
            transition: all 0.3s ease;
        }
        
        .user-status:hover {
            background: linear-gradient(135deg, #20c997 0%, #17a2b8 100%);
            transform: translateY(-1px);
            box-shadow: 0 4px 12px rgba(40, 167, 69, 0.4);
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
        
        /* 導航欄品牌圖片樣式 */
        .navbar-brand-img {
            height: 40px;
            width: auto;
            max-width: 200px;
            object-fit: contain;
        }
        
        /* 響應式設計 */
        @media (max-width: 768px) {
            .navbar-brand-img {
                height: 35px;
                max-width: 150px;
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
                padding: 6px 12px !important;
                font-size: 0.8rem;
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
        <!-- 头部导航 -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
            <div class="container">
                <a class="navbar-brand" href="/">
                    <img src="/img/title.png" alt="二手車銷售平台" class="navbar-brand-img">
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav ms-auto">
                        <li class="nav-item">
                            <a class="nav-link" href="/">首頁</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/buy-cars">買車</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/mall">商城</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/about">關於</a>
                        </li>
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
            // 購物車功能
            let cart = JSON.parse(localStorage.getItem('cart') || '[]');
            
            function updateCartCount() {
                const count = cart.reduce((total, item) => total + item.quantity, 0);
                const cartCountElement = document.getElementById('cartCount');
                if (cartCountElement) {
                    cartCountElement.textContent = count;
                }
            }
            
            function addToCart(product) {
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
                updateCartCount();
                showToast('商品已添加到購物車');
            }
            
            function showToast(message) {
                // 創建toast提示
                const toast = document.createElement('div');
                toast.className = 'toast-notification';
                toast.textContent = message;
                document.body.appendChild(toast);
                
                setTimeout(() => {
                    toast.remove();
                }, 2000);
            }
            
            // 頁面加載完成後的初始化
            document.addEventListener('DOMContentLoaded', function() {
                updateCartCount();
                
                // 購物車按鈕點擊事件
                const cartFloatBtn = document.getElementById('cartFloatBtn');
                if (cartFloatBtn) {
                    cartFloatBtn.addEventListener('click', function() {
                        window.location.href = '/cart';
                    });
                }
            });
        </script>
        

</body>
</html>
