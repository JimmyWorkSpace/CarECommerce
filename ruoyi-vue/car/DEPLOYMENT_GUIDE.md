# 广告位维护功能 - 部署指南

## 部署步骤

### 1. 数据库配置

#### 1.1 执行数据库表创建脚本
```sql
-- 执行 car/src/main/resources/sql/car_advertisement.sql
-- 创建广告位表
CREATE TABLE `car_advertisement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imageUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `linkUrl` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转地址',
  `isLink` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否跳转 1 是 0 否',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '显示内容，isLink为0时设置，富文本',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题，鼠标放入时的提示信息',
  `showOrder` int(11) NULL DEFAULT NULL COMMENT '排序',
  `delFlag` bit(1) NULL DEFAULT b'0' COMMENT '删除标记 1 是 0 否',
  `createTime` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '广告位' ROW_FORMAT = Compact;
```

#### 1.2 执行菜单权限脚本
```sql
-- 执行 car/src/main/resources/sql/advertisement_menu.sql
-- 创建菜单和权限
```

### 2. 后端配置

#### 2.1 确保FTP服务配置正确
在 `application.yml` 中添加或确认以下配置：

```yaml
carce:
  prefix: https://your-domain.com  # 图片访问前缀
  ftp:
    server: your-ftp-server
    port: 21
    user: your-username
    password: your-password
```

#### 2.2 确保Mapper XML文件被正确加载
在 `application.yml` 中确认MyBatis配置：

```yaml
mybatis:
  mapper-locations: classpath:mapper/**/*.xml
```

### 3. 前端配置

#### 3.1 确保API文件正确引入
检查 `ruoyi-ui/src/api/car/advertisement.js` 文件是否存在且内容正确。

#### 3.2 确保页面组件正确引入
检查 `ruoyi-ui/src/views/car/advertisement/index.vue` 文件是否存在且内容正确。

#### 3.3 确保Editor组件可用
检查 `ruoyi-ui/src/components/Editor/index.vue` 组件是否存在。

### 4. 编译部署

#### 4.1 后端编译
```bash
# 在项目根目录执行
mvn clean package -DskipTests
```

#### 4.2 前端编译
```bash
# 进入前端目录
cd ruoyi-ui

# 安装依赖
npm install

# 编译
npm run build
```

#### 4.3 部署到服务器
将编译后的文件部署到相应的服务器目录。

### 5. 功能测试

#### 5.1 基础功能测试
1. 登录系统，检查菜单是否正确显示
2. 进入广告位维护页面
3. 测试新增广告功能
4. 测试修改广告功能
5. 测试删除广告功能
6. 测试图片上传功能
7. 测试排序功能

#### 5.2 权限测试
1. 使用不同权限的用户登录
2. 测试各种操作权限是否正确控制

#### 5.3 图片上传测试
1. 测试图片上传到FTP服务器
2. 验证图片URL是否正确生成
3. 测试图片预览功能

## 常见问题

### 1. 菜单不显示
- 检查菜单SQL是否执行成功
- 检查用户是否有相应权限
- 检查菜单配置是否正确

### 2. 图片上传失败
- 检查FTP服务器配置是否正确
- 检查网络连接是否正常
- 检查FTP服务器目录权限

### 3. 富文本编辑器不显示
- 检查Editor组件是否正确引入
- 检查相关依赖是否安装

### 4. 排序功能不工作
- 检查数据库表结构是否正确
- 检查Mapper XML文件是否正确配置
- 检查前端API调用是否正确

## 配置检查清单

- [ ] 数据库表创建成功
- [ ] 菜单权限配置成功
- [ ] FTP服务器配置正确
- [ ] 后端编译成功
- [ ] 前端编译成功
- [ ] 功能测试通过
- [ ] 权限测试通过
- [ ] 图片上传测试通过

## 联系支持

如果遇到问题，请检查：
1. 日志文件中的错误信息
2. 数据库连接是否正常
3. FTP服务器连接是否正常
4. 网络连接是否正常 