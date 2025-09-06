<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>绿界支付签名验证</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .verify-section {
            margin-bottom: 30px;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .result-box {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 5px;
            margin-top: 10px;
        }
        .error-box {
            background-color: #f8d7da;
            color: #721c24;
            padding: 15px;
            border-radius: 5px;
            margin-top: 10px;
        }
        .success-box {
            background-color: #d4edda;
            color: #155724;
            padding: 15px;
            border-radius: 5px;
            margin-top: 10px;
        }
        .step-box {
            background-color: #e9ecef;
            padding: 10px;
            margin: 5px 0;
            border-radius: 3px;
            font-family: monospace;
            font-size: 12px;
        }
    </style>
</head>
<body>
    <div class="container mt-4">
        <h1>绿界支付签名验证工具</h1>
        <p class="text-muted">根据绿界支付官方文档验证签名生成是否正确</p>
        
        <!-- 配置验证 -->
        <div class="verify-section">
            <h3>1. 配置验证</h3>
            <p>验证HashKey和HashIV是否正确配置</p>
            <button class="btn btn-primary" onclick="verifyConfig()">验证配置</button>
            <div id="configResult"></div>
        </div>
        
        <!-- 签名步骤验证 -->
        <div class="verify-section">
            <h3>2. 签名生成步骤验证</h3>
            <p>按照绿界支付官方文档验证每个步骤</p>
            <button class="btn btn-success" onclick="verifySignatureSteps()">验证签名步骤</button>
            <div id="signatureStepsResult"></div>
        </div>
        
        <!-- 全方位金流支付验证 -->
        <div class="verify-section">
            <h3>3. 全方位金流支付验证</h3>
            <p>验证全方位金流支付参数构建是否符合官方API文档要求</p>
            <button class="btn btn-info" onclick="verifyAioPayment()">验证全方位金流支付</button>
            <div id="aioPaymentResult"></div>
        </div>
        
        <!-- 实际支付测试 -->
        <div class="verify-section">
            <h3>4. 实际支付测试</h3>
            <p>创建真实的支付订单并跳转到绿界支付</p>
            <form id="paymentTestForm">
                <div class="form-group">
                    <label for="testItemName">商品名称:</label>
                    <input type="text" class="form-control" id="testItemName" value="测试商品" required>
                </div>
                <div class="form-group">
                    <label for="testAmount">金额:</label>
                    <input type="number" class="form-control" id="testAmount" value="1" step="0.01" required>
                </div>
                <div class="form-group">
                    <label for="testDescription">描述:</label>
                    <input type="text" class="form-control" id="testDescription" value="测试订单" required>
                </div>
                <button type="button" class="btn btn-warning" onclick="testRealPayment()">创建真实支付订单</button>
            </form>
            <div id="paymentTestResult"></div>
        </div>
        
        <!-- 官方测试信息 -->
        <div class="verify-section">
            <h3>5. 官方测试信息</h3>
            <div class="alert alert-info">
                <h5>测试环境配置：</h5>
                <ul>
                    <li><strong>商户编号:</strong> 2000132</li>
                    <li><strong>HashKey:</strong> 5294y06JbISpM5x9</li>
                    <li><strong>HashIV:</strong> v77hoKGq4kWxNNIS</li>
                    <li><strong>测试服务器:</strong> https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5</li>
                </ul>
                <h5>测试卡号：</h5>
                <ul>
                    <li><strong>信用卡:</strong> 4000-0000-0000-0002</li>
                    <li><strong>有效期限:</strong> 任意未来日期</li>
                    <li><strong>安全码:</strong> 任意3位数字</li>
                </ul>
            </div>
        </div>
    </div>

    <script src="/js/jquery.min.js"></script>
    <script src="/js/bootstrap.bundle.min.js"></script>
    <script>
        function verifyConfig() {
            $('#configResult').html('<div class="text-info">正在验证配置...</div>');
            
            $.get('/api/test/ecpay/config')
                .done(function(data) {
                    if (data.success) {
                        let html = '<div class="success-box">';
                        html += '<h5>✅ 配置验证成功</h5>';
                        html += '<div class="row">';
                        html += '<div class="col-md-6">';
                        html += '<p><strong>商户编号:</strong> ' + data.data.merchantId + '</p>';
                        html += '<p><strong>HashKey:</strong> ' + data.data.hashKey + '</p>';
                        html += '<p><strong>HashIV:</strong> ' + data.data.hashIv + '</p>';
                        html += '</div>';
                        html += '<div class="col-md-6">';
                        html += '<p><strong>环境:</strong> ' + (data.data.production ? '正式环境' : '测试环境') + '</p>';
                        html += '<p><strong>服务器:</strong> ' + data.data.serverUrl + '</p>';
                        html += '</div>';
                        html += '</div>';
                        
                        // 验证是否为官方测试配置
                        if (data.data.merchantId === '2000132' && 
                            data.data.hashKey === '5294y06JbISpM5x9' && 
                            data.data.hashIv === 'v77hoKGq4kWxNNIS') {
                            html += '<div class="alert alert-success mt-2">✅ 使用官方测试配置，配置正确！</div>';
                        } else {
                            html += '<div class="alert alert-warning mt-2">⚠️ 配置与官方测试配置不符，请检查！</div>';
                        }
                        
                        html += '</div>';
                        $('#configResult').html(html);
                    } else {
                        $('#configResult').html('<div class="error-box">❌ 配置验证失败: ' + data.message + '</div>');
                    }
                })
                .fail(function(xhr) {
                    $('#configResult').html('<div class="error-box">❌ 请求失败: ' + xhr.responseText + '</div>');
                });
        }
        
        function verifySignatureSteps() {
            $('#signatureStepsResult').html('<div class="text-info">正在验证签名生成步骤...</div>');
            
            $.get('/api/test/ecpay/signature/detail')
                .done(function(data) {
                    if (data.success) {
                        let html = '<div class="success-box">';
                        html += '<h5>✅ 签名生成步骤验证成功</h5>';
                        
                        // 步骤1: 过滤参数
                        html += '<div class="step-box">';
                        html += '<strong>步骤1: 过滤参数（按字母顺序排序）</strong><br>';
                        html += JSON.stringify(data.data.step1_filteredParams, null, 2);
                        html += '</div>';
                        
                        // 步骤2: 查询字符串
                        html += '<div class="step-box">';
                        html += '<strong>步骤2: 构建查询字符串</strong><br>';
                        html += data.data.step2_queryString;
                        html += '</div>';
                        
                        // 步骤3: 待签名字符串
                        html += '<div class="step-box">';
                        html += '<strong>步骤3: 构建待签名字符串（HashKey=xxx&参数串&HashIV=xxx）</strong><br>';
                        html += data.data.step3_signString;
                        html += '</div>';
                        
                        // 步骤4: URL编码
                        html += '<div class="step-box">';
                        html += '<strong>步骤4: URL编码（UTF-8）</strong><br>';
                        html += data.data.step4_encodedString;
                        html += '</div>';
                        
                        // 步骤5: 小写转换
                        html += '<div class="step-box">';
                        html += '<strong>步骤5: 转换为小写</strong><br>';
                        html += data.data.step5_lowerString;
                        html += '</div>';
                        
                        // 步骤6: 最终签名
                        html += '<div class="step-box" style="background-color: #d1ecf1; color: #0c5460;">';
                        html += '<strong>步骤6: SHA256加密并转换为大写</strong><br>';
                        html += '<span style="font-size: 16px; font-weight: bold;">' + data.data.step6_signature + '</span>';
                        html += '</div>';
                        
                        html += '</div>';
                        $('#signatureStepsResult').html(html);
                    } else {
                        $('#signatureStepsResult').html('<div class="error-box">❌ 签名步骤验证失败: ' + data.message + '</div>');
                    }
                })
                .fail(function(xhr) {
                    $('#signatureStepsResult').html('<div class="error-box">❌ 请求失败: ' + xhr.responseText + '</div>');
                });
        }
        
        function verifyAioPayment() {
            $('#aioPaymentResult').html('<div class="text-info">正在验证全方位金流支付参数...</div>');
            
            $.get('/api/test/ecpay/aio')
                .done(function(data) {
                    if (data.success) {
                        let html = '<div class="success-box">';
                        html += '<h5>✅ 全方位金流支付参数验证成功</h5>';
                        
                        html += '<div class="row">';
                        html += '<div class="col-md-6">';
                        html += '<h6>基本信息</h6>';
                        html += '<p><strong>参数数量:</strong> ' + data.data.paramCount + '</p>';
                        html += '<p><strong>API地址:</strong> ' + data.data.apiUrl + '</p>';
                        html += '<p><strong>环境:</strong> ' + (data.data.isProduction ? '正式环境' : '测试环境') + '</p>';
                        html += '</div>';
                        html += '<div class="col-md-6">';
                        html += '<h6>必填参数检查</h6>';
                        let requiredParams = data.data.requiredParams;
                        let paymentParams = data.data.paymentParams;
                        let missingParams = [];
                        requiredParams.forEach(function(param) {
                            if (!paymentParams[param]) {
                                missingParams.push(param);
                            }
                        });
                        
                        if (missingParams.length === 0) {
                            html += '<p style="color: green;">✅ 所有必填参数都已包含</p>';
                        } else {
                            html += '<p style="color: red;">❌ 缺少必填参数: ' + missingParams.join(', ') + '</p>';
                        }
                        html += '</div>';
                        html += '</div>';
                        
                        html += '<h6>支付参数详情</h6>';
                        html += '<div class="step-box">';
                        html += '<pre>' + JSON.stringify(data.data.paymentParams, null, 2) + '</pre>';
                        html += '</div>';
                        
                        html += '</div>';
                        $('#aioPaymentResult').html(html);
                    } else {
                        $('#aioPaymentResult').html('<div class="error-box">❌ 全方位金流支付参数验证失败: ' + data.message + '</div>');
                    }
                })
                .fail(function(xhr) {
                    $('#aioPaymentResult').html('<div class="error-box">❌ 请求失败: ' + xhr.responseText + '</div>');
                });
        }
        
        function testRealPayment() {
            $('#paymentTestResult').html('<div class="text-info">正在创建真实支付订单...</div>');
            
            const paymentData = {
                amount: parseFloat($('#testAmount').val()),
                itemName: $('#testItemName').val(),
                description: $('#testDescription').val()
            };
            
            $.post('/api/payment/create', paymentData)
                .done(function(data) {
                    if (data.code === 1) {
                        let html = '<div class="success-box">';
                        html += '<h5>✅ 支付订单创建成功</h5>';
                        html += '<p><strong>商户订单号:</strong> ' + data.data.MerchantTradeNo + '</p>';
                        html += '<p><strong>金额:</strong> ' + data.data.TotalAmount + '</p>';
                        html += '<p><strong>商品名称:</strong> ' + data.data.ItemName + '</p>';
                        html += '<p><strong>签名:</strong> ' + data.data.CheckMacValue + '</p>';
                        html += '<div class="alert alert-warning mt-2">';
                        html += '<strong>⚠️ 注意:</strong> 点击下面的按钮将跳转到绿界支付测试页面，请使用测试卡号进行支付。';
                        html += '</div>';
                        html += '<button class="btn btn-primary mt-2" onclick="submitToECPay(' + JSON.stringify(data.data).replace(/"/g, '&quot;') + ')">跳转到绿界支付测试页面</button>';
                        html += '</div>';
                        $('#paymentTestResult').html(html);
                    } else {
                        $('#paymentTestResult').html('<div class="error-box">❌ 支付订单创建失败: ' + data.msg + '</div>');
                    }
                })
                .fail(function(xhr) {
                    $('#paymentTestResult').html('<div class="error-box">❌ 请求失败: ' + xhr.responseText + '</div>');
                });
        }
        
        function submitToECPay(paymentParams) {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = 'https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5';
            
            for (const [key, value] of Object.entries(paymentParams)) {
                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = key;
                input.value = value;
                form.appendChild(input);
            }
            
            document.body.appendChild(form);
            form.submit();
        }
    </script>
</body>
</html>
