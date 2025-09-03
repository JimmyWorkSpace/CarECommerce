<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>支付结果 - 汽车电商平台</title>
    <link href="/assets/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="/assets/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
    <link href="/css/common.css" rel="stylesheet">
    <style>
        .result-container {
            max-width: 600px;
            margin: 50px auto;
            padding: 40px;
            background: #fff;
            border-radius: 15px;
            box-shadow: 0 0 30px rgba(0,0,0,0.1);
            text-align: center;
        }
        
        .result-icon {
            font-size: 5rem;
            margin-bottom: 20px;
        }
        
        .result-icon.success {
            color: #28a745;
        }
        
        .result-icon.failed {
            color: #dc3545;
        }
        
        .result-title {
            font-size: 2rem;
            font-weight: bold;
            margin-bottom: 15px;
        }
        
        .result-message {
            font-size: 1.1rem;
            color: #6c757d;
            margin-bottom: 30px;
        }
        
        .order-details {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            margin: 20px 0;
            text-align: left;
        }
        
        .order-detail-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
            padding: 5px 0;
        }
        
        .order-detail-row:last-child {
            margin-bottom: 0;
            border-top: 1px solid #dee2e6;
            padding-top: 15px;
            font-weight: bold;
        }
        
        .action-buttons {
            margin-top: 30px;
        }
        
        .btn {
            margin: 0 10px;
            padding: 12px 25px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="result-container">
            <!-- 支付成功 -->
            <#if paymentStatus?? && paymentStatus == 1>
            <div class="result-icon success">
                <i class="bi bi-check-circle-fill"></i>
            </div>
            <h1 class="result-title">支付成功！</h1>
            <p class="result-message">您的订单已支付成功，感谢您的购买！</p>
            
            <!-- 支付失败 -->
            <#elseif paymentStatus?? && paymentStatus == 2>
            <div class="result-icon failed">
                <i class="bi bi-x-circle-fill"></i>
            </div>
            <h1 class="result-title">支付失败</h1>
            <p class="result-message">很抱歉，支付过程中出现了问题，请稍后重试。</p>
            
            <!-- 支付取消 -->
            <#elseif paymentStatus?? && paymentStatus == 3>
            <div class="result-icon failed">
                <i class="bi bi-x-circle-fill"></i>
            </div>
            <h1 class="result-title">支付已取消</h1>
            <p class="result-message">您已取消本次支付，如需继续购买请重新下单。</p>
            
            <!-- 默认状态 -->
            <#else>
            <div class="result-icon">
                <i class="bi bi-question-circle-fill"></i>
            </div>
            <h1 class="result-title">支付状态未知</h1>
            <p class="result-message">无法确定支付状态，请联系客服确认。</p>
            </#if>
            
            <!-- 订单详情 -->
            <#if paymentOrder??>
            <div class="order-details">
                <h5>订单详情</h5>
                <div class="order-detail-row">
                    <span>订单号：</span>
                    <span>${paymentOrder.merchantTradeNo!''}</span>
                </div>
                <div class="order-detail-row">
                    <span>商品名称：</span>
                    <span>${paymentOrder.itemName!''}</span>
                </div>
                <div class="order-detail-row">
                    <span>支付金额：</span>
                    <span>NT$ ${paymentOrder.totalAmount!0}</span>
                </div>
                <div class="order-detail-row">
                    <span>支付时间：</span>
                    <span>${(paymentOrder.paymentTime?string("yyyy-MM-dd HH:mm:ss"))!''}</span>
                </div>
                <div class="order-detail-row">
                    <span>支付状态：</span>
                    <span>
                        <#if paymentOrder.paymentStatus??>
                            <#switch paymentOrder.paymentStatus>
                                <#case 0>待支付<#break>
                                <#case 1>支付成功<#break>
                                <#case 2>支付失败<#break>
                                <#case 3>已取消<#break>
                                <#default>未知状态
                            </#switch>
                        <#else>
                            未知状态
                        </#if>
                    </span>
                </div>
            </div>
            </#if>
            
            <!-- 操作按钮 -->
            <div class="action-buttons">
                <#if paymentStatus?? && paymentStatus == 1>
                <!-- 支付成功 -->
                <a href="/mall" class="btn btn-primary">
                    <i class="bi bi-shop me-2"></i>继续购物
                </a>
                <a href="/cart" class="btn btn-outline-secondary">
                    <i class="bi bi-cart me-2"></i>查看购物车
                </a>
                <#else>
                <!-- 支付失败或取消 -->
                <a href="/payment/index?itemName=${paymentOrder.itemName!''}&amount=${paymentOrder.totalAmount!0}" class="btn btn-primary">
                    <i class="bi bi-credit-card me-2"></i>重新支付
                </a>
                <a href="/cart" class="btn btn-outline-secondary">
                    <i class="bi bi-cart me-2"></i>返回购物车
                </a>
                </#if>
                
                <a href="/mall" class="btn btn-outline-primary">
                    <i class="bi bi-house me-2"></i>返回首页
                </a>
            </div>
        </div>
    </div>
    
    <script src="/js/jquery.min.js"></script>
    <script src="/js/bootstrap.bundle.min.js"></script>
    <script>
        $(document).ready(function() {
            // 自动跳转倒计时（支付成功时）
            <#if paymentStatus?? && paymentStatus == 1>
            let countdown = 10;
            const countdownElement = $('<div class="mt-3 text-muted">页面将在 <span id="countdown">' + countdown + '</span> 秒后自动跳转到商城</div>');
            $('.action-buttons').before(countdownElement);
            
            const timer = setInterval(function() {
                countdown--;
                $('#countdown').text(countdown);
                
                if (countdown <= 0) {
                    clearInterval(timer);
                    window.location.href = '/mall';
                }
            }, 1000);
            </#if>
        });
    </script>
</body>
</html>
