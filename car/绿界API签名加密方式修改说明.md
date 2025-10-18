# 绿界API签名加密方式修改说明

## 概述

已将绿界科技API的签名生成方式从SHA256加密修改为MD5加密，以符合绿界API的特定要求。

## 修改内容

### 1. ECPayUtils.java 修改

#### 修改前（SHA256加密）
```java
/**
 * 生成绿界支付签名
 * 按照绿界支付官方检查码机制文档：https://developers.ecpay.com.tw/?p=2902
 * 1. 将传递参数依照第一个英文字母，由A到Z的顺序来排序
 * 2. 参数最前面加上HashKey、最后面加上HashIV
 * 3. 将整串字串进行URL encode（按照.NET编码(ecpay)规则）
 * 4. 转为小写
 * 5. 以SHA256加密方式来产生杂凑值
 * 6. 再转大写产生CheckMacValue
 */

// 7. SHA256加密
String signature = DigestUtils.sha256Hex(lowerString);
log.info("绿界支付签名生成 - 步骤6 SHA256加密后: {}", signature);
```

#### 修改后（MD5加密）
```java
/**
 * 生成绿界支付签名
 * 按照绿界支付官方检查码机制文档：https://developers.ecpay.com.tw/?p=2902
 * 1. 将传递参数依照第一个英文字母，由A到Z的顺序来排序
 * 2. 参数最前面加上HashKey、最后面加上HashIV
 * 3. 将整串字串进行URL encode（按照.NET编码(ecpay)规则）
 * 4. 转为小写
 * 5. 以MD5加密方式来产生杂凑值
 * 6. 再转大写产生CheckMacValue
 */

// 7. MD5加密
String signature = DigestUtils.md5Hex(lowerString);
log.info("绿界支付签名生成 - 步骤6 MD5加密后: {}", signature);
```

### 2. 影响的方法

以下方法中的签名生成都已修改为MD5加密：

1. **`generateSignature(Map<String, String> params)`** - 支付签名生成
2. **`generateSignatureForLogistics(Map<String, Object> params)`** - 物流签名生成

## 签名生成流程

### 完整流程（MD5版本）

1. **参数过滤和排序**
   - 过滤空值参数和CheckMacValue
   - 按字母顺序排序（TreeMap自动排序）

2. **构建查询字符串**
   - 格式：`key1=value1&key2=value2&key3=value3`

3. **构建待签名字符串**
   - 格式：`HashKey=xxx&参数串&HashIV=xxx`

4. **URL编码**
   - 使用UTF-8编码
   - 按照.NET编码(ecpay)规则处理特殊字符

5. **转换为小写**
   - 将整个字符串转换为小写

6. **MD5加密**
   - 使用Apache Commons Codec的MD5加密
   - 生成32位十六进制字符串

7. **转换为大写**
   - 将MD5结果转换为大写
   - 生成最终的CheckMacValue

## 代码示例

### 签名生成示例

```java
@Resource
private ECPayUtils ecPayUtils;

public void generateSignatureExample() {
    Map<String, String> params = new HashMap<>();
    params.put("MerchantID", "3002607");
    params.put("MerchantTradeNo", "TEST20250116001");
    params.put("TotalAmount", "100");
    params.put("TradeDesc", "测试订单");
    
    // 生成MD5签名
    String signature = ecPayUtils.generateSignature(params);
    log.info("生成的签名: {}", signature);
}
```

### 物流API签名示例

```java
public void generateLogisticsSignatureExample() {
    Map<String, Object> params = new TreeMap<>();
    params.put("MerchantID", "3002607");
    params.put("MerchantTradeNo", "LOG20250116001");
    params.put("LogisticsType", "HOME");
    params.put("LogisticsSubType", "TCAT");
    
    // 生成物流MD5签名
    String signature = ecPayUtils.generateSignatureForLogistics(params);
    log.info("生成的物流签名: {}", signature);
}
```

## 技术细节

### MD5 vs SHA256 对比

| 特性 | MD5 | SHA256 |
|------|-----|--------|
| 输出长度 | 32位十六进制 | 64位十六进制 |
| 安全性 | 较低 | 较高 |
| 性能 | 较快 | 较慢 |
| 绿界要求 | ✅ 支持 | ❌ 不支持 |

### 依赖库

使用Apache Commons Codec库进行MD5加密：

```xml
<dependency>
    <groupId>commons-codec</groupId>
    <artifactId>commons-codec</artifactId>
    <version>1.15</version>
</dependency>
```

### 导入语句

```java
import org.apache.commons.codec.digest.DigestUtils;
```

## 验证方法

### 签名验证示例

```java
public boolean verifySignature(Map<String, String> params, String receivedSignature) {
    try {
        // 生成签名
        String calculatedSignature = ecPayUtils.generateSignature(params);
        
        // 比较签名（忽略大小写）
        boolean isValid = calculatedSignature.equalsIgnoreCase(receivedSignature);
        
        log.info("签名验证 - 计算签名: {}, 接收签名: {}, 验证结果: {}", 
                calculatedSignature, receivedSignature, isValid);
        
        return isValid;
    } catch (Exception e) {
        log.error("签名验证失败", e);
        return false;
    }
}
```

## 注意事项

1. **兼容性**：确保绿界API支持MD5签名方式
2. **安全性**：MD5相对SHA256安全性较低，但符合绿界API要求
3. **一致性**：所有API调用必须使用相同的签名方式
4. **测试**：建议在测试环境先验证MD5签名的正确性

## 测试建议

1. **单元测试**：为签名生成方法编写单元测试
2. **集成测试**：在测试环境验证API调用
3. **签名对比**：与绿界官方示例进行签名对比
4. **错误处理**：测试签名验证失败的情况

## 相关文档

- [绿界支付官方文档](https://developers.ecpay.com.tw/?p=2902)
- [Apache Commons Codec文档](https://commons.apache.org/proper/commons-codec/)
- [MD5算法说明](https://zh.wikipedia.org/wiki/MD5)
