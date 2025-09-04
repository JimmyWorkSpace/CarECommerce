# 绿界支付（ECPay）集成说明

## 概述

本项目已成功集成台湾绿界支付（ECPay），支持多种支付方式，包括信用卡、ATM、超商等。绿界支付是台湾地区最流行的第三方支付平台之一。

## 功能特性

- ✅ 支持多种支付方式（信用卡、ATM、超商等）
- ✅ 完整的支付流程（创建订单 → 跳转支付 → 回调处理）
- ✅ 安全的签名验证机制
- ✅ 支持测试环境和正式环境
- ✅ 完整的订单状态管理
- ✅ 用户友好的支付界面

## 技术架构

### 核心组件

1. **ECPayConfig** - 绿界支付配置类
2. **ECPayUtils** - 绿界支付工具类（签名生成、验证等）
3. **PaymentOrderEntity** - 支付订单实体
4. **PaymentOrderMapper** - 支付订单数据访问层
5. **ECPayService** - 绿界支付业务逻辑服务
6. **ECPayController** - 绿界支付API控制器
7. **PaymentPageController** - 支付页面控制器

### 支付流程

```
用户选择商品 → 创建支付订单 → 跳转绿界支付 → 用户完成支付 → 绿界回调通知 → 更新订单状态
```

## 配置说明

### 1. 依赖配置

在 `pom.xml` 中添加以下依赖：

```xml
<!-- 绿界支付相关依赖 -->
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.13</version>
</dependency>

<dependency>
    <groupId>commons-codec</groupId>
    <artifactId>commons-codec</artifactId>
    <version>1.15</version>
</dependency>

<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>

<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.12.0</version>
</dependency>
```

### 2. 环境配置

#### 开发环境 (application-dev.yml)

```yaml
# 绿界支付配置
ecpay:
  # 测试环境配置
  production: false
  merchant-id: "2000132"  # 官方测试商户编号
  hash-key: "5294y06JbISpM5x9"  # 官方测试HashKey
  hash-iv: "v77hoKGq4kWxNNIS"   # 官方测试HashIV
  return-url: "http://localhost:8080/api/payment/return"
  notify-url: "http://localhost:8080/api/payment/callback"
  client-back-url: "http://localhost:8080/payment/result"
  result-url: "http://localhost:8080/payment/result"
```

#### 测试环境 (application-test.yml)

```yaml
# 绿界支付配置
ecpay:
  # 测试环境配置
  production: false
  merchant-id: "2000132"  # 官方测试商户编号
  hash-key: "5294y06JbISpM5x9"  # 官方测试HashKey
  hash-iv: "v77hoKGq4kWxNNIS"   # 官方测试HashIV
  return-url: "https://sale.carce.cc/api/payment/return"
  notify-url: "https://sale.carce.cc/api/payment/callback"
  client-back-url: "https://sale.carce.cc/payment/result"
  result-url: "https://sale.carce.cc/payment/result"
```

#### 生产环境 (application-prod.yml)

```yaml
# 绿界支付配置
ecpay:
  # 正式环境配置
  production: true
  merchant-id: "你的正式商户编号"
  hash-key: "你的正式HashKey"
  hash-iv: "你的正式HashIV"
  return-url: "https://yourdomain.com/api/payment/return"
  notify-url: "https://yourdomain.com/api/payment/callback"
  client-back-url: "https://yourdomain.com/payment/result"
  result-url: "https://yourdomain.com/payment/result"
```

### 3. 数据库配置

执行 `payment_order_table.sql` 创建支付订单表：

```sql
CREATE TABLE `payment_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `merchant_trade_no` varchar(50) NOT NULL COMMENT '商户订单号',
  `ecpay_trade_no` varchar(50) DEFAULT NULL COMMENT '绿界支付交易号',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单金额',
  `item_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `trade_desc` text COMMENT '订单描述',
  `payment_type` varchar(50) DEFAULT NULL COMMENT '支付方式',
  `payment_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '支付状态',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `ecpay_status` varchar(50) DEFAULT NULL COMMENT '绿界支付状态',
  `ecpay_status_desc` varchar(255) DEFAULT NULL COMMENT '绿界支付状态描述',
  `error_message` text COMMENT '错误信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_merchant_trade_no` (`merchant_trade_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_payment_status` (`payment_status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付订单表';
```

## API 接口说明

### 1. 创建支付订单

**接口地址：** `POST /api/payment/create`

**请求参数：**
```json
{
  "amount": 150000.00,
  "itemName": "奔驰C200L",
  "description": "购买奔驰C200L轿车"
}
```

**响应示例：**
```json
{
  "code": 1,
  "msg": "成功",
  "data": {
    "MerchantID": "2000132",
    "MerchantTradeNo": "202412011200001",
    "MerchantTradeDate": "2024/12/01 12:00:00",
    "PaymentType": "aio",
    "TotalAmount": "150000",
    "TradeDesc": "购买奔驰C200L轿车",
    "ItemName": "奔驰C200L",
    "ReturnURL": "http://localhost:8080/api/payment/return",
    "ClientBackURL": "http://localhost:8080/payment/result",
    "OrderResultURL": "http://localhost:8080/api/payment/return",
    "ChoosePayment": "ALL",
    "Language": "ZH-TW",
    "CheckMacValue": "生成的签名"
  }
}
```

### 2. 查询支付订单状态

**接口地址：** `GET /api/payment/status/{merchantTradeNo}`

**响应示例：**
```json
{
  "code": 1,
  "msg": "成功",
  "data": {
    "id": 1,
    "merchantTradeNo": "202412011200001",
    "ecpayTradeNo": "EC202412011200001",
    "userId": 1,
    "totalAmount": 150000.00,
    "itemName": "奔驰C200L",
    "paymentStatus": 1,
    "paymentTime": "2024-12-01T12:05:00",
    "ecpayStatus": "SUCCESS",
    "ecpayStatusDesc": "支付成功"
  }
}
```

### 3. 取消支付订单

**接口地址：** `POST /api/payment/cancel/{merchantTradeNo}`

**响应示例：**
```json
{
  "code": 1,
  "msg": "成功",
  "data": true
}
```

### 4. 绿界支付回调接口

**接口地址：** `POST /api/payment/callback`

**说明：** 此接口由绿界支付调用，用于通知支付结果

### 5. 绿界支付同步返回接口

**接口地址：** `GET /api/payment/return`

**说明：** 用户支付完成后，绿界支付会重定向到此接口

## 前端集成

### 1. 支付页面

访问 `/payment/index` 显示支付页面，支持传入以下参数：

- `itemName`: 商品名称
- `amount`: 支付金额
- `description`: 订单描述

**示例：**
```
/payment/index?itemName=奔驰C200L&amount=150000&description=购买奔驰C200L轿车
```

### 2. JavaScript 调用示例

```javascript
// 创建支付订单
axios.post('/api/payment/create', {
    amount: 150000.00,
    itemName: '奔驰C200L',
    description: '购买奔驰C200L轿车'
})
.then(function(response) {
    if (response.data.code === 1) {
        // 跳转到绿界支付
        const paymentParams = response.data.data;
        submitToECPay(paymentParams);
    }
});

// 提交到绿界支付
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
```

## 测试说明

### 1. 测试环境

- **服务器地址：** `https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5`
- **商户编号：** `2000132` (官方测试商户编号)
- **HashKey：** `5294y06JbISpM5x9` (官方测试HashKey)
- **HashIV：** `v77hoKGq4kWxNNIS` (官方测试HashIV)

### 2. 测试卡号

绿界支付测试环境提供以下测试卡号：

- **信用卡：** 4000-0000-0000-0002
- **有效期限：** 任意未来日期
- **安全码：** 任意3位数字

**注意：** 在开发/测试环境下，系统会自动将支付金额固定为0.01元，无论用户输入多少金额。

### 3. 测试流程

1. 启动应用，确保配置正确
2. 访问支付页面 `/payment/index`
3. 填写商品信息和金额
4. 点击"立即支付"
5. 跳转到绿界支付测试页面
6. 使用测试卡号完成支付
7. 查看回调处理和订单状态更新

## 安全说明

### 1. 签名验证

- 所有支付回调都会进行签名验证
- 使用SHA256算法生成签名
- 验证失败的回调会被拒绝处理

### 2. 参数验证

- 支付金额必须大于0
- 商品名称不能为空
- 用户必须登录才能创建支付订单

### 3. 权限控制

- 用户只能查看和操作自己的支付订单
- 支付回调接口不验证用户登录状态（由绿界支付调用）

## 常见问题

### 1. 支付回调失败

**可能原因：**
- 签名验证失败
- 回调URL配置错误
- 网络连接问题

**解决方案：**
- 检查HashKey和HashIV配置
- 确认回调URL可访问
- 查看服务器日志

### 2. 支付页面无法访问

**可能原因：**
- 用户未登录
- 权限配置错误

**解决方案：**
- 确保用户已登录
- 检查AuthInterceptor配置

### 3. 订单状态未更新

**可能原因：**
- 回调处理异常
- 数据库连接问题

**解决方案：**
- 查看回调日志
- 检查数据库连接
- 手动查询绿界支付订单状态

## 部署说明

### 1. 开发环境

1. 执行数据库建表SQL
2. 配置 `application-dev.yml`
3. 启动应用
4. 测试支付流程

### 2. 生产环境

1. 修改 `application-prod.yml` 配置
2. 更新正式环境的商户信息
3. 配置HTTPS证书
4. 确保回调URL可访问
5. 测试支付流程

### 3. 监控建议

- 监控支付回调接口的响应时间
- 记录支付成功率和失败率
- 监控订单状态更新情况
- 设置支付异常告警

## 联系支持

如有问题，请联系：

- **绿界支付官方：** https://www.ecpay.com.tw/
- **技术支持：** 查看项目日志和错误信息
- **文档更新：** 定期查看绿界支付官方文档

---

**注意：** 本集成方案基于绿界支付官方文档开发，如有疑问请参考官方文档或联系绿界支付技术支持。
