# Redis配置说明

## 概述

本项目已集成Redis支持，用于：
1. Sa-Token用户登录信息存储
2. 缓存数据存储
3. 会话管理

## 依赖配置

### Maven依赖

项目已添加以下Redis相关依赖：

```xml
<!-- Redis 依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

<!-- Sa-Token Redis 集成 -->
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-redis</artifactId>
    <version>1.37.0</version>
</dependency>

<!-- Apache Commons Pool2 连接池支持 -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
```

## 配置文件

### application.yml

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    database: 0
    password: 
    timeout: 10s

# Sa-Token配置
sa-token:
  token-name: satoken
  timeout: 2592000
  active-timeout: -1
  is-concurrent: true
  is-share: false
  token-style: uuid
  is-log: false
  token-prefix: 'satoken:'
  is-read-cookie: false
  is-read-header: true
  is-read-body: false
  is-read-session: false
  is-write-session: false
```

## 配置类

### RedisConfig.java

配置RedisTemplate，设置序列化器：

```java
@Configuration
@ConditionalOnClass(RedisConnectionFactory.class)
public class RedisConfig {
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        // 配置RedisTemplate
    }
}
```

### Sa-Token自动配置

Sa-Token会自动检测Redis配置并使用Redis存储，无需额外配置类。

### 公开路径配置

以下路径允许未登录用户访问：

```java
// 公开页面
"/", "/home", "/buy-cars", "/mall", "/about", "/cart",
"/login", "/register", "/logout"

// 静态资源
"/css/**", "/js/**", "/img/**", "/assets/**", "/lib/**"

// 公开API
"/api/banners", "/api/advertisements", "/api/cars/**", 
"/api/test/**", "/api/shopping-cart/**",
"/api/sms/**", "/api/verify/**", "/api/captcha/**"

// 其他
"/error", "/favicon.ico", "/swagger-ui/**", "/v2/api-docs/**"
```

**注意**: 其他所有路径都需要用户登录后才能访问。

### 验证码和短信API说明

以下API允许未登录用户访问，用于用户注册和登录：

- `/api/sms/**` - 短信验证码相关接口
- `/api/verify/**` - 验证码验证接口  
- `/api/captcha/**` - 图形验证码接口

这些接口通常用于：
- 用户注册时发送短信验证码
- 用户登录时发送短信验证码
- 验证用户输入的验证码
- 获取图形验证码

## 工具类

### RedisUtil.java

提供常用的Redis操作方法：

- 字符串操作：set, get, expire等
- Hash操作：hset, hget, hmset等
- List操作：lSet, lGet, lRemove等
- Set操作：sSet, sGet, sRemove等

## 使用方法

### 1. 启动Redis服务

确保本地Redis服务运行在6379端口：

```bash
# Windows
redis-server

# Linux/Mac
redis-server /etc/redis/redis.conf
```

### 2. 测试Redis连接

访问测试接口：

```
GET /api/test/redis
```

### 3. 测试Redis操作

```bash
# 测试Redis连接
GET /api/test/redis

# 测试Redis基本操作
GET /api/test/redis/operations
```

## 功能特性

### 1. 用户登录持久化

- 用户登录信息存储在Redis中
- 服务重启后用户无需重新登录
- 支持分布式部署

### 2. 自动回退机制

- 如果Redis不可用，自动回退到内存存储
- 确保系统稳定性

### 3. 条件化配置

- 使用`@ConditionalOnClass`注解
- 只有在Redis可用时才创建相关Bean

### 4. 权限控制配置

- 配置了公开路径，允许未登录用户访问
- 包括：首页、商城、购物车、静态资源、公开API等
- 其他路径需要登录后才能访问

## 注意事项

1. **Redis服务必须运行**：确保Redis服务在6379端口运行
2. **网络连接**：确保应用能连接到Redis服务
3. **内存配置**：根据实际需求调整Redis内存配置
4. **持久化**：建议配置Redis持久化，避免数据丢失

## 故障排除

### 常见错误

1. **连接失败**：检查Redis服务是否运行
2. **权限错误**：检查Redis配置的密码和权限
3. **内存不足**：检查Redis内存使用情况

### 日志查看

查看应用启动日志，确认Redis连接状态：

```bash
tail -f logs/application.log
```

## 扩展功能

### 1. 缓存管理

可以使用RedisUtil实现：
- 商品信息缓存
- 用户会话缓存
- 接口响应缓存

### 2. 分布式锁

利用Redis实现：
- 商品库存锁定
- 订单处理锁定
- 并发控制

### 3. 消息队列

使用Redis List实现简单的消息队列功能。
