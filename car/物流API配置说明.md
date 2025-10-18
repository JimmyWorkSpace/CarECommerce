# 物流API配置说明

## 概述

已将绿界科技物流API地址配置到各个环境的application.yml文件中，不再在程序中硬编码判断环境。通过配置文件统一管理不同环境的API地址。

## 配置结构

### ECPayConfig 配置类

```java
@Data
@Component
@ConfigurationProperties(prefix = "ecpay")
public class ECPayConfig {
    
    // 物流API地址配置
    private String logisticsApiUrl = "https://logistics-stage.ecpay.com.tw";        // 测试环境
    private String logisticsProductionUrl = "https://logistics.ecpay.com.tw";      // 正式环境
    
    // 获取当前环境的物流API地址
    public String getCurrentLogisticsApiUrl() {
        return production ? logisticsProductionUrl : logisticsApiUrl;
    }
}
```

### 配置文件

#### application-dev.yml (开发环境)
```yaml
ecpay:
  production: false
  # 物流API地址配置
  logistics-api-url: "https://logistics-stage.ecpay.com.tw"
  logistics-production-url: "https://logistics.ecpay.com.tw"
```

#### application-test.yml (测试环境)
```yaml
ecpay:
  production: false
  # 物流API地址配置
  logistics-api-url: "https://logistics-stage.ecpay.com.tw"
  logistics-production-url: "https://logistics.ecpay.com.tw"
```

#### application-prod.yml (生产环境)
```yaml
ecpay:
  production: true
  # 物流API地址配置
  logistics-api-url: "https://logistics-stage.ecpay.com.tw"
  logistics-production-url: "https://logistics.ecpay.com.tw"
```

## 使用方式

### LogisticsService 中的使用

```java
/**
 * 获取物流API地址
 */
private String getLogisticsApiUrl() {
    return ecPayConfig.getCurrentLogisticsApiUrl();
}
```

### 调用示例

```java
// 调用绿界宅配API
String apiUrl = getLogisticsApiUrl() + "/Express/Create";
log.info("调用绿界物流API，URL：{}", apiUrl);

// 发送POST请求
String response = restTemplate.postForObject(apiUrl, requestBody, String.class);
```

## 环境切换

### 开发环境
- `spring.profiles.active=dev`
- 使用测试环境API：`https://logistics-stage.ecpay.com.tw`

### 测试环境
- `spring.profiles.active=test`
- 使用测试环境API：`https://logistics-stage.ecpay.com.tw`

### 生产环境
- `spring.profiles.active=prod`
- 使用正式环境API：`https://logistics.ecpay.com.tw`

## 配置优势

1. **集中管理**：所有API地址在配置文件中统一管理
2. **环境隔离**：不同环境使用不同的API地址
3. **易于维护**：修改API地址只需更新配置文件
4. **避免硬编码**：程序代码中不再包含环境判断逻辑
5. **配置灵活**：可以根据需要为不同环境配置不同的API地址

## 注意事项

1. **生产环境配置**：请确保生产环境的 `merchant-id`、`hash-key`、`hash-iv` 等敏感信息使用真实的生产环境值
2. **URL配置**：确保各个环境的回调URL和通知URL配置正确
3. **环境切换**：通过 `spring.profiles.active` 参数控制使用哪个环境的配置
4. **API地址**：测试环境和正式环境的API地址不同，请确保配置正确

## 相关API地址

### 绿界科技物流API

#### 测试环境
- 宅配到府：`https://logistics-stage.ecpay.com.tw/Express/Create`
- 超商取货：`https://logistics-stage.ecpay.com.tw/Express/Create`
- 门店查询：`https://logistics-stage.ecpay.com.tw/Express/map`

#### 正式环境
- 宅配到府：`https://logistics.ecpay.com.tw/Express/Create`
- 超商取货：`https://logistics.ecpay.com.tw/Express/Create`
- 门店查询：`https://logistics.ecpay.com.tw/Express/map`

## 配置验证

可以通过以下方式验证配置是否正确：

```java
@Resource
private ECPayConfig ecPayConfig;

public void validateConfig() {
    log.info("当前环境：{}", ecPayConfig.isProduction() ? "生产环境" : "测试环境");
    log.info("物流API地址：{}", ecPayConfig.getCurrentLogisticsApiUrl());
    log.info("商户编号：{}", ecPayConfig.getMerchantId());
}
```
