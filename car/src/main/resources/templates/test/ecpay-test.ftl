<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>绿界支付测试</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .test-section {
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
    </style>
</head>
<body>
    <div class="container mt-4">
        <h1>绿界支付测试页面</h1>
        
        <!-- 配置测试 -->
        <div class="test-section">
            <h3>1. 配置测试</h3>
            <button class="btn btn-primary" onclick="testConfig()">测试配置</button>
            <div id="configResult"></div>
        </div>
        
        <!-- 签名测试 -->
        <div class="test-section">
            <h3>2. 签名生成测试</h3>
            <button class="btn btn-success" onclick="testSignature()">测试签名生成</button>
            <button class="btn btn-info" onclick="testSignatureDetail()">详细签名测试</button>
            <div id="signatureResult"></div>
        </div>
        
        <!-- 支付测试 -->
        <div class="test-section">
            <h3>3. 支付测试</h3>
            <form id="paymentForm">
                <div class="form-group">
                    <label for="itemName">商品名称:</label>
                    <input type="text" class="form-control" id="itemName" value="测试商品" required>
                </div>
                <div class="form-group">
                    <label for="amount">金额:</label>
                    <input type="number" class="form-control" id="amount" value="1" step="0.01" required>
                </div>
                <div class="form-group">
                    <label for="description">描述:</label>
                    <input type="text" class="form-control" id="description" value="测试订单" required>
                </div>
                <button type="button" class="btn btn-warning" onclick="testPayment()">创建支付订单</button>
            </form>
            <div id="paymentResult"></div>
        </div>
    </div>

    <script src="/js/jquery.min.js"></script>
    <script src="/js/bootstrap.bundle.min.js"></script>
    <script>
        function testConfig() {
            $('#configResult').html('<div class="text-info">正在测试配置...</div>');
            
            $.get('/api/test/ecpay/config')
                .done(function(data) {
                    if (data.success) {
                        let html = '<div class="success-box">';
                        html += '<h5>配置测试成功</h5>';
                        html += '<pre>' + JSON.stringify(data.data, null, 2) + '</pre>';
                        html += '</div>';
                        $('#configResult').html(html);
                    } else {
                        $('#configResult').html('<div class="error-box">配置测试失败: ' + data.message + '</div>');
                    }
                })
                .fail(function(xhr) {
                    $('#configResult').html('<div class="error-box">请求失败: ' + xhr.responseText + '</div>');
                });
        }
        
        function testSignature() {
            $('#signatureResult').html('<div class="text-info">正在测试签名生成...</div>');
            
            $.get('/api/test/ecpay/signature')
                .done(function(data) {
                    if (data.success) {
                        let html = '<div class="success-box">';
                        html += '<h5>签名生成成功</h5>';
                        html += '<p><strong>签名:</strong> ' + data.data.signature + '</p>';
                        html += '<details><summary>详细信息</summary>';
                        html += '<pre>' + JSON.stringify(data.data, null, 2) + '</pre>';
                        html += '</details>';
                        html += '</div>';
                        $('#signatureResult').html(html);
                    } else {
                        $('#signatureResult').html('<div class="error-box">签名生成失败: ' + data.message + '</div>');
                    }
                })
                .fail(function(xhr) {
                    $('#signatureResult').html('<div class="error-box">请求失败: ' + xhr.responseText + '</div>');
                });
        }
        
        function testSignatureDetail() {
            $('#signatureResult').html('<div class="text-info">正在测试详细签名生成...</div>');
            
            $.get('/api/test/ecpay/signature/detail')
                .done(function(data) {
                    if (data.success) {
                        let html = '<div class="success-box">';
                        html += '<h5>详细签名生成成功</h5>';
                        html += '<div class="row">';
                        html += '<div class="col-md-6">';
                        html += '<h6>步骤1: 过滤参数</h6>';
                        html += '<pre style="font-size: 12px;">' + JSON.stringify(data.data.step1_filteredParams, null, 2) + '</pre>';
                        html += '</div>';
                        html += '<div class="col-md-6">';
                        html += '<h6>步骤2: 查询字符串</h6>';
                        html += '<pre style="font-size: 12px;">' + data.data.step2_queryString + '</pre>';
                        html += '</div>';
                        html += '</div>';
                        html += '<div class="row">';
                        html += '<div class="col-md-6">';
                        html += '<h6>步骤3: 签名字符串</h6>';
                        html += '<pre style="font-size: 12px;">' + data.data.step3_signString + '</pre>';
                        html += '</div>';
                        html += '<div class="col-md-6">';
                        html += '<h6>步骤4: URL编码</h6>';
                        html += '<pre style="font-size: 12px;">' + data.data.step4_encodedString + '</pre>';
                        html += '</div>';
                        html += '</div>';
                        html += '<div class="row">';
                        html += '<div class="col-md-6">';
                        html += '<h6>步骤5: 小写转换</h6>';
                        html += '<pre style="font-size: 12px;">' + data.data.step5_lowerString + '</pre>';
                        html += '</div>';
                        html += '<div class="col-md-6">';
                        html += '<h6>步骤6: 最终签名</h6>';
                        html += '<pre style="font-size: 12px; color: red; font-weight: bold;">' + data.data.step6_signature + '</pre>';
                        html += '</div>';
                        html += '</div>';
                        html += '<details><summary>完整数据</summary>';
                        html += '<pre>' + JSON.stringify(data.data, null, 2) + '</pre>';
                        html += '</details>';
                        html += '</div>';
                        $('#signatureResult').html(html);
                    } else {
                        $('#signatureResult').html('<div class="error-box">详细签名生成失败: ' + data.message + '</div>');
                    }
                })
                .fail(function(xhr) {
                    $('#signatureResult').html('<div class="error-box">请求失败: ' + xhr.responseText + '</div>');
                });
        }
        
        function testPayment() {
            $('#paymentResult').html('<div class="text-info">正在创建支付订单...</div>');
            
            const paymentData = {
                amount: parseFloat($('#amount').val()),
                itemName: $('#itemName').val(),
                description: $('#description').val()
            };
            
            $.post('/api/payment/create', paymentData)
                .done(function(data) {
                    if (data.code === 1) {
                        let html = '<div class="success-box">';
                        html += '<h5>支付订单创建成功</h5>';
                        html += '<p><strong>商户订单号:</strong> ' + data.data.MerchantTradeNo + '</p>';
                        html += '<p><strong>金额:</strong> ' + data.data.TotalAmount + '</p>';
                        html += '<details><summary>完整参数</summary>';
                        html += '<pre>' + JSON.stringify(data.data, null, 2) + '</pre>';
                        html += '</details>';
                        html += '<button class="btn btn-primary mt-2" onclick="submitToECPay(' + JSON.stringify(data.data).replace(/"/g, '&quot;') + ')">跳转到绿界支付</button>';
                        html += '</div>';
                        $('#paymentResult').html(html);
                    } else {
                        $('#paymentResult').html('<div class="error-box">支付订单创建失败: ' + data.msg + '</div>');
                    }
                })
                .fail(function(xhr) {
                    $('#paymentResult').html('<div class="error-box">请求失败: ' + xhr.responseText + '</div>');
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
