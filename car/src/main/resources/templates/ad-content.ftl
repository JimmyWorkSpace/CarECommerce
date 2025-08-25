<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title!'廣告詳情'}</title>
    <style>
        body {
            font-family: 'Microsoft YaHei', Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f8f9fa;
            line-height: 1.6;
        }
        .ad-content-container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        .ad-header {
            background: linear-gradient(135deg, #5ACFC9 0%, #4AB8B2 100%);
            color: white;
            padding: 30px;
            text-align: center;
        }
        .ad-title {
            font-size: 2rem;
            font-weight: bold;
            margin: 0;
        }
        .ad-body {
            padding: 30px;
            color: #333;
        }
        .ad-body h1, .ad-body h2, .ad-body h3 {
            color: #5ACFC9;
            margin-top: 20px;
            margin-bottom: 10px;
        }
        .ad-body p {
            margin-bottom: 15px;
        }
        .ad-body img {
            max-width: 100%;
            height: auto;
            border-radius: 5px;
            margin: 10px 0;
        }
        .error-message {
            text-align: center;
            color: #dc3545;
            padding: 50px 20px;
            font-size: 1.2rem;
        }
        .close-btn {
            position: fixed;
            top: 20px;
            right: 20px;
            background: #5ACFC9;
            color: white;
            border: none;
            border-radius: 50%;
            width: 40px;
            height: 40px;
            font-size: 20px;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        .close-btn:hover {
            background: #4AB8B2;
            transform: scale(1.1);
        }
    </style>
</head>
<body>
    <button class="close-btn" onclick="window.close()">×</button>
    
    <#if error??>
        <div class="error-message">
            <h2>錯誤</h2>
            <p>${error}</p>
        </div>
    <#else>
        <div class="ad-content-container">
            <div class="ad-header">
                <h1 class="ad-title">${title!'廣告詳情'}</h1>
            </div>
            <div class="ad-body">
                <#if content??>
                    ${content}
                <#else>
                    <p>暫無內容</p>
                </#if>
            </div>
        </div>
    </#if>
</body>
</html> 