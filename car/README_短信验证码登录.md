# 短信验证码登录功能

## 功能概述

本系统已实现短信验证码登录功能，用户可以通过手机号+短信验证码的方式进行登录。

## 主要特性

1. **短信验证码发送**：支持发送6位数字验证码
2. **验证码有效期**：验证码有效期为5分钟
3. **防重复发送**：60秒内不能重复发送验证码
4. **自动注册**：新用户首次登录时自动创建账户
5. **Sa-Token集成**：使用Sa-Token进行权限管理
6. **模拟发送**：开发环境下验证码打印到控制台
7. **环境适配**：dev/test环境固定验证码为123456，生产环境随机生成

## 技术实现

### 1. 依赖配置

在`pom.xml`中添加了Sa-Token依赖：
```xml
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-spring-boot-starter</artifactId>
    <version>1.37.0</version>
</dependency>
```

### 2. 核心组件

- **SmsService**：短信验证码服务，负责生成、验证和管理验证码
- **CarUserService**：用户服务，处理用户登录、注册和查询
- **SmsApiController**：短信API控制器，处理发送验证码请求
- **LoginController**：登录控制器，处理登录逻辑

### 3. 数据库表

用户表结构（`car_user`）：
```sql
CREATE TABLE `car_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `phoneNumber` varchar(20) NOT NULL COMMENT '手机号',
  `nickName` varchar(255) DEFAULT NULL COMMENT '昵称',
  `delFlag` bit(1) DEFAULT b'0' COMMENT '删除标记',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `lastLoginTime` datetime DEFAULT NULL COMMENT '上次登录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_phone_number` (`phoneNumber`)
);
```

## 使用方法

### 1. 启动应用

```bash
mvn spring-boot:run
```

### 2. 访问登录页面

- 正式登录页面：`http://localhost:8080/login`
- 测试页面：`http://localhost:8080/test/sms-test`

### 3. 登录流程

1. 输入手机号（格式：1[3-9]xxxxxxxxx）
2. 点击"发送验证码"按钮
3. 查看控制台输出获取验证码
4. 输入6位验证码
5. 点击"登录"按钮

### 4. 验证码查看

验证码会打印在控制台，格式如下：

**开发环境 (dev/test)：**
```
=== 开发环境：使用固定验证码 123456 ===
=== 短信验证码发送模拟 ===
手机号: 13800138000
验证码: 123456
有效期: 5 分钟
环境: 开发环境 (固定验证码)
========================
```

**生产环境：**
```
=== 短信验证码发送模拟 ===
手机号: 13800138000
验证码: 847392
有效期: 5 分钟
环境: 生产环境 (随机验证码)
========================
```

## 配置说明

### Sa-Token配置

在`application.yml`中配置：
```yaml
sa-token:
  token-name: satoken
  timeout: 2592000  # 30天
  active-timeout: -1
  is-concurrent: true
  is-share: false
  token-style: uuid
  is-log: false
```

### 验证码配置

在`SmsService`中可以调整：
- 验证码长度：默认6位
- 有效期：默认5分钟
- 重发间隔：默认60秒

### 环境配置

系统会根据当前激活的Spring Profile自动调整验证码策略：

- **dev/test环境**：验证码固定为 `123456`
- **生产环境**：验证码随机生成6位数字

在`application.yml`中设置激活的环境：
```yaml
spring:
  profiles:
    active: test  # 可选值：dev, test, prod
```

## 安全特性

1. **手机号格式验证**：只允许中国大陆手机号格式
2. **验证码格式验证**：只允许6位数字
3. **防重复发送**：60秒内不能重复发送
4. **验证码过期**：5分钟后自动失效
5. **一次性使用**：验证成功后立即删除验证码

## 扩展功能

### 1. 真实短信发送

要接入真实短信服务，需要：
1. 在`SmsService.sendSmsCode()`方法中集成短信服务商API
2. 移除开发环境的控制台打印
3. 配置短信服务商的相关参数

### 2. 图形验证码

可以添加图形验证码防止恶意发送：
1. 在发送短信前先验证图形验证码
2. 使用验证码生成库生成图形验证码

### 3. 登录日志

可以添加登录日志记录：
1. 记录登录时间、IP地址、设备信息
2. 记录登录成功/失败状态
3. 异常登录检测

## 故障排除

### 1. 验证码发送失败

- 检查手机号格式是否正确
- 检查是否在60秒内重复发送
- 查看控制台错误日志

### 2. 登录失败

- 检查验证码是否正确
- 检查验证码是否过期
- 检查数据库连接是否正常

### 3. 数据库问题

- 确保`car_user`表已创建
- 检查数据库连接配置
- 查看SQL执行日志

## 测试数据

系统提供了测试数据：
```sql
INSERT INTO `car_user` (`phoneNumber`, `nickName`, `delFlag`, `createTime`, `lastLoginTime`) VALUES
('13800138000', '测试用户1', b'0', NOW(), NOW()),
('13800138001', '测试用户2', b'0', NOW(), NOW()),
('13800138002', '测试用户3', b'0', NOW(), NOW());
```

可以使用这些手机号进行测试，或者使用任意手机号进行自动注册测试。
