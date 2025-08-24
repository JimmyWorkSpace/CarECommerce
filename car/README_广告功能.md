# 广告功能实现说明

## 功能概述
实现了从数据库查询广告数据并在首页显示的功能。广告数据从`car_advertisement`表中查询，只显示前两个广告。

### 广告显示特性：
- **只显示图片**：广告位只显示图片，不显示文字内容
- **鼠标悬停显示标题**：当title有值时，鼠标移入时显示title
- **图片加载失败处理**：如果图片加载失败，自动显示title
- **链接广告**：如果isLink=1，点击时跳转到指定链接
- **内容广告**：如果isLink=0，点击后在新页面显示content中的富文本内容

## 实现内容

### 1. 数据库实体类
- `CarAdvertisementEntity.java` - 广告实体类，包含以下字段：
  - `id`: 主键ID
  - `imageUrl`: 广告图片URL
  - `linkUrl`: 广告链接URL
  - `isLink`: 是否为链接广告（1=是，0=否）
  - `content`: 广告内容
  - `title`: 广告标题
  - `showOrder`: 显示顺序
  - `delFlag`: 删除标志（0=未删除，1=已删除）
  - `createTime`: 创建时间

### 2. Mapper接口
- `CarAdvertisementMapper.java` - 使用tk.mybatis通用Mapper，提供基本的CRUD操作

### 3. Service层
- `CarAdvertisementService.java` - 广告服务类，提供以下方法：
  - `getHomeAdvertisements()`: 获取首页广告列表（前两个，按排序字段升序）
  - `getById()`: 根据ID获取广告
  - `save()`: 保存广告
  - `update()`: 更新广告
  - `deleteById()`: 删除广告（逻辑删除）

### 4. Controller层
- 在`CarViewController.java`中添加了广告数据查询
- 在首页方法中调用`carAdvertisementService.getHomeAdvertisements()`获取广告数据
- 添加了`/api/advertisements`接口用于测试

### 5. 模板修改
- 修改了`home/index.ftl`模板，支持从数据库查询的广告数据
- 支持广告链接功能（当`isLink=1`时显示为链接）
- 支持内容广告功能（当`isLink=0`时点击显示富文本内容）
- 当没有广告数据时显示默认广告内容
- 创建了`ad-content.ftl`模板用于显示广告内容页面

### 6. CSS样式
- 在`home.css`中添加了广告链接和内容链接的样式
- 支持广告图片的悬停效果
- 添加了标题覆盖层样式，支持鼠标悬停显示标题
- 支持图片加载失败时的标题显示

## 数据库表结构
```sql
CREATE TABLE car_advertisement (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    imageUrl VARCHAR(255) COMMENT '广告图片URL',
    linkUrl VARCHAR(255) COMMENT '广告链接URL',
    isLink INT DEFAULT 0 COMMENT '是否为链接广告（1=是，0=否）',
    content TEXT COMMENT '广告内容',
    title VARCHAR(255) COMMENT '广告标题',
    showOrder INT DEFAULT 0 COMMENT '显示顺序',
    delFlag INT DEFAULT 0 COMMENT '删除标志（0=未删除，1=已删除）',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);
```

## 测试数据
在`test_data_car_advertisement.sql`文件中提供了测试数据，包含4条广告记录。

## 使用方法

### 1. 创建数据库表
```sql
CREATE TABLE car_advertisement (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    imageUrl VARCHAR(255) COMMENT '广告图片URL',
    linkUrl VARCHAR(255) COMMENT '广告链接URL',
    isLink INT DEFAULT 0 COMMENT '是否为链接广告（1=是，0=否）',
    content TEXT COMMENT '广告内容',
    title VARCHAR(255) COMMENT '广告标题',
    showOrder INT DEFAULT 0 COMMENT '显示顺序',
    delFlag INT DEFAULT 0 COMMENT '删除标志（0=未删除，1=已删除）',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);
```

### 2. 插入测试数据
执行`test_data_car_advertisement.sql`文件中的SQL语句。

### 3. 访问页面
- 首页：`http://localhost:8082/`
- API接口：`http://localhost:8082/api/advertisements`

## 注意事项
1. 广告数据从manager数据源查询
2. 只显示前两个广告（按showOrder排序）
3. 只显示未删除的广告（delFlag=0）
4. 支持广告链接功能，当isLink=1时广告可点击跳转到指定链接
5. 支持内容广告功能，当isLink=0时点击在新页面显示富文本内容
6. 当没有广告数据时显示默认广告内容
7. 图片加载失败时自动显示标题
8. 鼠标悬停时显示标题（如果title有值） 