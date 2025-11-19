<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        body {
            margin: 0;
            padding: 20px;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
            line-height: 1.8;
            color: #333;
        }
        .dealer-description {
            max-width: 100%;
        }
        .dealer-description img {
            max-width: 100%;
            height: auto;
        }
    </style>
</head>
<body>
    <div class="dealer-description">
        ${(dealerInfo.description)!''}
    </div>
</body>
</html>

