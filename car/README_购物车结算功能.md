# 购物车结算功能说明

## 功能概述

本项目已实现完整的购物车结算功能，用户可以在购物车页面点击"立即结算"按钮，跳转到支付页面完成支付流程。当配置文件为dev或test环境时，支付金额固定为1元，方便开发和测试。

## 功能特性

- ✅ 购物车商品结算
- ✅ 自动跳转支付页面
- ✅ 环境配置金额控制
- ✅ 购物车商品列表显示
- ✅ 支付结果页面
- ✅ 完整的支付流程

## 技术实现

### 1. 购物车结算流程

```
购物车页面 → 点击"立即结算" → 构建结算数据 → 跳转支付页面 → 完成支付 → 显示结果
```

### 2. 环境配置金额控制

- **开发环境 (dev)**: 支付金额固定为1元
- **测试环境 (test)**: 支付金额固定为1元  
- **生产环境 (prod)**: 使用实际购物车金额

### 3. 核心组件

1. **购物车页面** (`/cart/index.ftl`)
2. **支付页面** (`/payment/index.ftl`)
3. **支付结果页面** (`/payment/result.ftl`)
4. **支付页面控制器** (`PaymentPageController`)
5. **支付结果控制器** (`PaymentResultController`)
6. **绿界支付服务** (`ECPayService`)

## 使用说明

### 1. 购物车结算

1. 在购物车页面添加商品
2. 点击"立即结算"按钮
3. 系统自动跳转到支付页面
4. 显示购物车商品列表和支付信息

### 2. 支付流程

1. 确认商品信息和金额
2. 选择支付方式
3. 点击"立即支付"
4. 跳转到绿界支付页面
5. 完成支付后返回结果页面

### 3. 支付结果

- **支付成功**: 显示成功信息，自动跳转商城
- **支付失败**: 显示失败信息，可重新支付
- **支付取消**: 显示取消信息，可重新支付

## 代码结构

### 购物车结算按钮

```javascript
// 结算
async checkout() {
    if (this.cartItems.length === 0) {
        if (window.showErrorMessage) {
            window.showErrorMessage('購物車是空的，無法結算');
        } else {
            this.error = '購物車是空的，無法結算';
        }
        return;
    }
    
    try {
        // 构建结算数据
        const checkoutData = {
            items: this.cartItems.map(item => ({
                id: item.id,
                productId: item.productId,
                productName: item.productName,
                productAmount: item.productAmount,
                productPrice: item.productPrice,
                subtotal: item.subtotal
            })),
            totalAmount: this.totalPrice,
            totalQuantity: this.totalQuantity
        };
        
        // 跳转到支付页面
        const queryParams = new URLSearchParams({
            itemName: `購物車商品 (${this.cartItems.length}件)`,
            amount: this.totalPrice,
            description: `購買${this.cartItems.length}件商品，總計${this.totalPrice}元`,
            cartData: JSON.stringify(checkoutData)
        });
        
        window.location.href = `/payment/index?${queryParams.toString()}`;
        
    } catch (error) {
        console.error('結算失敗:', error);
        const errorMessage = window.handleApiError ? window.handleApiError(error, '結算失敗，請稍後重試') : '結算失敗，請稍後重試';
        this.error = errorMessage;
    }
}
```

### 环境配置金额控制

```java
// 获取当前环境配置
String activeProfile = System.getProperty("spring.profiles.active");
if (activeProfile == null) {
    activeProfile = "dev"; // 默认使用dev环境
}

// 如果是dev或test环境，金额固定为1
BigDecimal finalAmount = amount;
if ("dev".equals(activeProfile) || "test".equals(activeProfile)) {
    finalAmount = BigDecimal.ONE;
    log.info("开发/测试环境，支付金额固定为1元，原始金额: {}", amount);
}
```

### 购物车商品列表显示

```html
<!-- 购物车商品列表 -->
<#if cartData?? && cartData != ''>
<div class="form-group">
    <label class="form-label">购物车商品</label>
    <div class="cart-items-list" id="cartItemsList">
        <!-- 商品列表将通过JavaScript动态生成 -->
    </div>
</div>
</#if>
```

## 配置说明

### 1. 环境配置

#### 开发环境 (application-dev.yml)

```yaml
# 绿界支付配置
ecpay:
  production: false
  merchant-id: "2000132"
  hash-key: "5294y06JbISpM5x9"
  hash-iv: "v77hoKGq4kWxNNIS"
  return-url: "http://localhost:8080/api/payment/return"
  notify-url: "http://localhost:8080/api/payment/callback"
  client-back-url: "http://localhost:8080/payment/result"
  result-url: "http://localhost:8080/payment/result"
```

#### 生产环境 (application-prod.yml)

```yaml
# 绿界支付配置
ecpay:
  production: true
  merchant-id: "你的正式商户编号"
  hash-key: "你的正式HashKey"
  hash-iv: "你的正式HashIV"
  return-url: "https://yourdomain.com/api/payment/return"
  notify-url: "https://yourdomain.com/api/payment/callback"
  client-back-url: "https://yourdomain.com/payment/result"
  result-url: "https://yourdomain.com/payment/result"
```

### 2. 数据库配置

确保已执行 `payment_order_table.sql` 创建支付订单表。

## 测试说明

### 1. 开发环境测试

1. 启动应用，确保使用dev环境配置
2. 在购物车添加商品
3. 点击"立即结算"
4. 验证支付金额是否为1元
5. 完成支付流程测试

### 2. 测试环境测试

1. 修改配置为test环境
2. 重复开发环境测试步骤
3. 验证金额控制功能

### 3. 生产环境测试

1. 修改配置为prod环境
2. 验证使用实际购物车金额
3. 测试完整支付流程

## 注意事项

### 1. 环境配置

- 确保正确设置 `spring.profiles.active`
- dev和test环境金额固定为1元
- prod环境使用实际金额

### 2. 用户登录

- 结算功能需要用户登录
- 未登录用户自动跳转登录页面
- 登录后返回结算页面

### 3. 购物车数据

- 结算时传递完整的购物车数据
- 支持多商品结算
- 数据格式为JSON字符串

### 4. 支付安全

- 所有支付操作都需要验证用户身份
- 支付回调进行签名验证
- 订单状态实时更新

## 常见问题

### 1. 结算按钮无响应

**可能原因：**
- 购物车为空
- JavaScript错误
- 用户未登录

**解决方案：**
- 检查购物车是否有商品
- 查看浏览器控制台错误
- 确保用户已登录

### 2. 支付金额不正确

**可能原因：**
- 环境配置错误
- 金额计算逻辑问题

**解决方案：**
- 检查环境配置
- 验证金额计算逻辑
- 查看日志信息

### 3. 支付页面无法访问

**可能原因：**
- 用户未登录
- 权限配置错误
- 路由配置问题

**解决方案：**
- 确保用户已登录
- 检查权限配置
- 验证路由配置

## 扩展功能

### 1. 优惠券支持

可以在结算时添加优惠券功能，自动计算折扣金额。

### 2. 积分抵扣

支持用户积分抵扣部分支付金额。

### 3. 分期付款

集成分期付款功能，支持多种分期方案。

### 4. 发票管理

添加发票开具和管理功能。

## 联系支持

如有问题，请联系：

- **技术支持**: 查看项目日志和错误信息
- **功能建议**: 提交功能需求和建议
- **问题反馈**: 报告使用中遇到的问题

---

**注意**: 本功能已完全集成到绿界支付系统中，支持完整的支付流程和订单管理。
