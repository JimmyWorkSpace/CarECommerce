# 绿界API请求配置说明

## 概述

已按照绿界科技API文档要求，正确配置了HTTP请求头和传输协议，确保与绿界API的兼容性。

## 绿界API文档要求

根据绿界科技官方文档，物流API的HTTP传输协议要求：

- **Accept**: `text/html`
- **Content Type**: `application/x-www-form-urlencoded`
- **HTTP Method**: `POST`

## 代码实现

### 1. 导入必要的类

```java
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
```

### 2. HTTP请求配置

```java
// 构建请求体
MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
for (Map.Entry<String, String> entry : params.entrySet()) {
    requestBody.add(entry.getKey(), entry.getValue());
}

// 设置请求头，按照绿界API文档要求
HttpHeaders headers = new HttpHeaders();
headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);  // Content-Type: application/x-www-form-urlencoded
headers.set("Accept", "text/html");                             // Accept: text/html

// 创建HTTP实体
HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

// 发送POST请求
String response = restTemplate.postForObject(apiUrl, requestEntity, String.class);
```

## 修改的方法

### 1. 宅配到府物流单创建

**方法**: `createHomeDeliveryLogistics()`

**修改位置**: 第277-286行

```java
// 设置请求头，按照绿界API文档要求
HttpHeaders headers = new HttpHeaders();
headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
headers.set("Accept", "text/html");

// 创建HTTP实体
HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

// 发送POST请求
String response = restTemplate.postForObject(apiUrl, requestEntity, String.class);
```

### 2. 超商取货物流单创建

**方法**: `createConvenienceStoreLogistics()`

**修改位置**: 第587-596行

```java
// 设置请求头，按照绿界API文档要求
HttpHeaders headers = new HttpHeaders();
headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
headers.set("Accept", "text/html");

// 创建HTTP实体
HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

// 发送POST请求
String response = restTemplate.postForObject(apiUrl, requestEntity, String.class);
```

## 请求头详解

### Content-Type: application/x-www-form-urlencoded

- **作用**: 指定请求体的数据格式为表单数据
- **格式**: `key1=value1&key2=value2&key3=value3`
- **Spring实现**: `MediaType.APPLICATION_FORM_URLENCODED`

### Accept: text/html

- **作用**: 告诉服务器客户端期望接收的响应格式
- **绿界要求**: 必须设置为 `text/html`
- **Spring实现**: `headers.set("Accept", "text/html")`

## 请求流程

1. **构建参数**: 将业务参数转换为 `MultiValueMap<String, String>`
2. **设置请求头**: 按照绿界API文档要求设置Content-Type和Accept
3. **创建HTTP实体**: 将请求体和请求头封装为 `HttpEntity`
4. **发送请求**: 使用 `RestTemplate.postForObject()` 发送POST请求
5. **处理响应**: 解析绿界API返回的响应数据

## 响应格式

绿界API返回的响应格式：

### 成功响应
```
1|MerchantID=XXX&MerchantTradeNo=XXX&RtnCode=1&RtnMsg=成功&AllPayLogisticsID=XXX&...
```

### 失败响应
```
0|ErrorMessage
```

## 注意事项

1. **请求头必须正确**: 严格按照绿界API文档要求设置请求头
2. **数据格式**: 请求体必须是 `application/x-www-form-urlencoded` 格式
3. **字符编码**: 确保中文字符正确编码
4. **签名验证**: 请求参数必须包含正确的 `CheckMacValue`
5. **HTTPS协议**: 所有请求必须使用HTTPS协议

## 测试验证

可以通过以下方式验证请求配置是否正确：

```java
@PostConstruct
public void testLogisticsAPI() {
    // 测试宅配到府
    // 测试超商取货
    // 检查请求头和响应格式
}
```

## 相关API地址

- **测试环境**: `https://logistics-stage.ecpay.com.tw/Express/Create`
- **正式环境**: `https://logistics.ecpay.com.tw/Express/Create`

## 错误排查

如果遇到请求失败，请检查：

1. 请求头是否正确设置
2. Content-Type是否为 `application/x-www-form-urlencoded`
3. Accept是否为 `text/html`
4. 请求体格式是否正确
5. 签名是否正确生成
6. API地址是否正确
