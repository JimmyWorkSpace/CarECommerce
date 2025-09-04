<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>支付页面 - 汽车电商平台</title>
    <link href="/assets/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="/assets/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
    <link href="/css/common.css" rel="stylesheet">
    <style>
        .payment-container {
            max-width: 600px;
            margin: 50px auto;
            padding: 30px;
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
        }
        .payment-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .payment-form {
            margin-bottom: 30px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .payment-methods {
            margin: 20px 0;
        }
        .payment-method-item {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 10px;
            cursor: pointer;
            transition: all 0.3s;
        }
        .payment-method-item:hover {
            border-color: #007bff;
            background-color: #f8f9fa;
        }
        .payment-method-item.selected {
            border-color: #007bff;
            background-color: #e3f2fd;
        }
        .payment-summary {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin: 20px 0;
        }
        .btn-pay {
            width: 100%;
            padding: 15px;
            font-size: 18px;
            font-weight: bold;
        }
        .loading {
            display: none;
        }
        .error-message {
            color: #dc3545;
            margin-top: 10px;
        }
        .success-message {
            color: #28a745;
            margin-top: 10px;
        }
        
        .cart-items-list {
            max-height: 300px;
            overflow-y: auto;
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 15px;
            background: #f8f9fa;
        }
        
        .cart-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px;
            margin-bottom: 10px;
            background: white;
            border-radius: 6px;
            border: 1px solid #e9ecef;
        }
        
        .cart-item:last-child {
            margin-bottom: 0;
        }
        
        .cart-item-info {
            flex: 1;
        }
        
        .cart-item-name {
            font-weight: 600;
            margin-bottom: 5px;
        }
        
        .cart-item-details {
            font-size: 0.9rem;
            color: #6c757d;
        }
        
        .cart-item-price {
            font-weight: 600;
            color: #e74c3c;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="payment-container">
            <div class="payment-header">
                <h2><i class="bi bi-credit-card"></i> 支付订单</h2>
                <p class="text-muted">请选择支付方式并完成支付</p>
                
                <!-- 环境提示 -->
                <#if isDevOrTest?? && isDevOrTest>
                <div class="alert alert-warning" role="alert">
                    <i class="bi bi-exclamation-triangle me-2"></i>
                    <strong>测试环境提示：</strong>当前为开发/测试环境，支付金额固定为0.01元
                </div>
                </#if>
            </div>
            
            <div class="payment-form">
                <form id="paymentForm">
                    <div class="form-group">
                        <label for="itemName">商品名称</label>
                        <input type="text" class="form-control" id="itemName" name="itemName" 
                               value="${itemName!''}" readonly>
                    </div>
                    
                    <div class="form-group">
                        <label for="amount">支付金额</label>
                        <div class="input-group">
                            <span class="input-group-text">NT$</span>
                            <input type="number" class="form-control" id="amount" name="amount" 
                                   value="${amount!0}" readonly>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="description">订单描述</label>
                        <textarea class="form-control" id="description" name="description" 
                                  rows="3" placeholder="请输入订单描述（可选）">${description!''}</textarea>
                    </div>
                    
                    <!-- 收件人信息 -->
                    <div class="form-group">
                        <label for="receiverName">收件人姓名 <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="receiverName" name="receiverName" 
                               placeholder="请输入收件人姓名" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="receiverMobile">收件人手机号 <span class="text-danger">*</span></label>
                        <input type="tel" class="form-control" id="receiverMobile" name="receiverMobile" 
                               placeholder="请输入收件人手机号" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="receiverAddress">收件人地址 <span class="text-danger">*</span></label>
                        <textarea class="form-control" id="receiverAddress" name="receiverAddress" 
                                  rows="3" placeholder="请输入详细的收件人地址" required></textarea>
                    </div>
                    
                    <!-- 隐藏的购物车数据字段 -->
                    <input type="hidden" id="cartData" name="cartData" value="${cartData!''}">
                    
                    <!-- 购物车商品列表 -->
                    <#if cartData?? && cartData != ''>
                    <div class="form-group">
                        <label class="form-label">购物车商品</label>
                        <div class="cart-items-list" id="cartItemsList">
                            <!-- 商品列表将通过JavaScript动态生成 -->
                        </div>
                    </div>
                    </#if>
                    
                    
                    <div class="payment-summary">
                        <h5>订单摘要</h5>
                        <div class="row">
                            <div class="col-6">商品名称：</div>
                            <div class="col-6" id="summaryItemName">${itemName!''}</div>
                        </div>
                        <div class="row">
                            <div class="col-6">支付金额：</div>
                            <div class="col-6">¥ <span id="summaryAmount">${amount!0}</span></div>
                        </div>
                        <div class="row">
                            <div class="col-6">收件人：</div>
                            <div class="col-6" id="summaryReceiverName">-</div>
                        </div>
                        <div class="row">
                            <div class="col-6">联系电话：</div>
                            <div class="col-6" id="summaryReceiverMobile">-</div>
                        </div>
                        <div class="row">
                            <div class="col-6">收货地址：</div>
                            <div class="col-6" id="summaryReceiverAddress">-</div>
                        </div>
                    </div>
                    
                    <button type="submit" class="btn btn-primary btn-pay" id="payButton">
                        <span class="btn-text">立即支付</span>
                        <span class="loading">
                            <i class="bi bi-arrow-clockwise spin"></i> 处理中...
                        </span>
                    </button>
                    
                    <div class="error-message" id="errorMessage"></div>
                    <div class="success-message" id="successMessage"></div>
                </form>
            </div>
            
            <div class="text-center">
                <a href="/cart" class="btn btn-outline-secondary me-2">
                    <i class="bi bi-arrow-left"></i> 返回购物车
                </a>
                <a href="/mall" class="btn btn-outline-primary">
                    <i class="bi bi-shop"></i> 继续购物
                </a>
            </div>
        </div>
    </div>
    
    <script src="/js/jquery.min.js"></script>
    <script src="/js/bootstrap.bundle.min.js"></script>
    <script src="/js/axios.min.js"></script>
    <script>
        <#noparse>
        $(document).ready(function() {
            // 初始化购物车商品列表
            initCartItemsList();
            
            // 收件人信息输入监听
            $('#receiverName, #receiverMobile, #receiverAddress').on('input', function() {
                updateSummary();
            });
            
            // 更新摘要
            function updateSummary() {
                const receiverName = $('#receiverName').val() || '-';
                const receiverMobile = $('#receiverMobile').val() || '-';
                const receiverAddress = $('#receiverAddress').val() || '-';
                
                $('#summaryReceiverName').text(receiverName);
                $('#summaryReceiverMobile').text(receiverMobile);
                $('#summaryReceiverAddress').text(receiverAddress);
            }
            
            // 表单提交
            $('#paymentForm').submit(function(e) {
                e.preventDefault();
                
                const amount = parseFloat($('#amount').val());
                const itemName = $('#itemName').val();
                const description = $('#description').val();
                const receiverName = $('#receiverName').val();
                const receiverMobile = $('#receiverMobile').val();
                const receiverAddress = $('#receiverAddress').val();
                
                if (!amount || amount <= 0) {
                    showError('支付金額必須大於0');
                    return;
                }
                
                if (!itemName || itemName.trim() === '') {
                    showError('商品名稱不能為空');
                    return;
                }
                
                if (!receiverName || receiverName.trim() === '') {
                    showError('收件人姓名不能為空');
                    return;
                }
                
                if (!receiverMobile || receiverMobile.trim() === '') {
                    showError('收件人手機號不能為空');
                    return;
                }
                
                if (!receiverAddress || receiverAddress.trim() === '') {
                    showError('收件人地址不能為空');
                    return;
                }
                
                // 显示加载状态
                showLoading(true);
                hideMessages();
                
                // 创建支付订单
                axios.post('/api/payment/create', {
                    amount: amount,
                    itemName: itemName,
                    description: description || '购买商品：' + itemName,
                    receiverName: receiverName,
                    receiverMobile: receiverMobile,
                    receiverAddress: receiverAddress,
                    cartData: $('#cartData').val()
                })
                .then(function(response) {
                    if (response.data.code === 1) {
                        // 支付订单创建成功，跳转到绿界支付页面
                        const paymentParams = response.data.data;
                        submitToECPay(paymentParams);
                    } else {
                        showError(response.data.msg || '創建支付訂單失敗');
                    }
                })
                .catch(function(error) {
                    console.error('创建支付订单失败:', error);
                    showError('創建支付訂單失敗，請稍後重試');
                })
                .finally(function() {
                    showLoading(false);
                });
            });
            
            // 提交到绿界支付
            function submitToECPay(paymentParams) {
                // 创建表单并提交到绿界支付
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = 'https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5'; // 测试环境
                
                // 添加所有支付参数
                for (const [key, value] of Object.entries(paymentParams)) {
                    const input = document.createElement('input');
                    input.type = 'hidden';
                    input.name = key;
                    input.value = value;
                    form.appendChild(input);
                }
                
                // 提交表单
                document.body.appendChild(form);
                form.submit();
            }
            
            // 显示加载状态
            function showLoading(show) {
                if (show) {
                    $('.btn-text').hide();
                    $('.loading').show();
                    $('#payButton').prop('disabled', true);
                } else {
                    $('.btn-text').show();
                    $('.loading').hide();
                    $('#payButton').prop('disabled', false);
                }
            }
            
            // 显示错误信息
            function showError(message) {
                $('#errorMessage').text(message).show();
                $('#successMessage').hide();
            }
            
            // 显示成功信息
            function showSuccess(message) {
                $('#successMessage').text(message).show();
                $('#errorMessage').hide();
            }
            
            // 隐藏所有消息
            function hideMessages() {
                $('#errorMessage').hide();
                $('#successMessage').hide();
            }
            
            // 初始化购物车商品列表
            function initCartItemsList() {
                const cartData = $('#cartData').val();
                if (cartData && cartData.trim() !== '') {
                    try {
                        const cartItems = JSON.parse(cartData);
                        if (cartItems && cartItems.items && cartItems.items.length > 0) {
                            const cartListContainer = $('#cartItemsList');
                            cartListContainer.empty();
                            
                            cartItems.items.forEach(item => {
                                const cartItemHtml = `
                                    <div class="cart-item">
                                        <div class="cart-item-info">
                                            <div class="cart-item-name">${item.productName}</div>
                                            <div class="cart-item-details">
                                                数量: ${item.productAmount} × ¥${parseFloat(item.productPrice).toFixed(2)}
                                            </div>
                                        </div>
                                        <div class="cart-item-price">
                                            ¥${parseFloat(item.subtotal).toFixed(2)}
                                        </div>
                        </div>
                                `;
                                cartListContainer.append(cartItemHtml);
                            });
                        }
                    } catch (error) {
                        console.error('解析购物车数据失败:', error);
                        console.error('购物车数据内容:', cartData);
                    }
                }
            }
            
            // 初始化
            updateSummary();
        });
        </#noparse>
    </script>
</body>
</html>
