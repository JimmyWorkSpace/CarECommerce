# 商城商品功能实现说明

## 功能概述
商城页面已经从原来的汽车展示改为汽车配件商品展示，商品数据来源于`products`表，通过AJAX请求动态加载。

## 数据库结构

### 主要表结构
1. **products表** - 商品主表
   - `id`: 商品ID
   - `source`: 商品来源
   - `name`: 商品名称
   - `alias`: 商品别名
   - `model`: 商品型号
   - `market_price`: 商品市价
   - `price`: 商品售价
   - `id_supplier`: 供应商ID
   - `brand`: 商品品牌
   - `tag`: 商品类别标签
   - `is_public`: 是否发布
   - `memo`: 备注
   - `c_dt`: 创建时间
   - `u_dt`: 更新时间

2. **商品图片关联** (通过SQL查询实现)
   - `products_pimages`表: 商品与图片的关联表
   - `pimages`表: 图片信息表
   - 图片文件存储在: `/img/product/{图片ID}_{尺寸}.jpg`
   - 列表页使用90x90尺寸: `{图片ID}_90x90.jpg`

## 代码结构

### 后端代码
1. **CarProductsEntity.java** - 商品实体类
2. **CarProductsService.java** - 商品服务类
   - `getPublicProducts()`: 获取已发布的商品列表
   - `getProductsByCategory()`: 根据分类获取商品
   - `getProductImageUrl()`: 获取商品图片URL
   - `mapTagToCategory()`: 将商品标签映射到前端分类
3. **CarProductsApiController.java** - 商品API控制器
   - `GET /api/products/list`: 获取所有商品
   - `GET /api/products/category?category={category}`: 根据分类获取商品

### 前端代码
1. **商城页面模板**: `src/main/resources/templates/mall/index.ftl`
2. **Vue.js功能**:
   - 动态加载商品数据
   - 分类过滤
   - 分页显示
   - 购物车功能
   - 图片错误处理

## 商品分类映射

| 数据库标签 | 前端分类 | 说明 |
|-----------|----------|------|
| 發動機/引擎 | engine | 发动机配件 |
| 制動/煞車 | brake | 制动系统 |
| 懸挂/懸架 | suspension | 悬挂系统 |
| 電氣/電器 | electrical | 电气系统 |
| 外觀/外飾 | exterior | 外观配件 |
| 工具 | tools | 工具设备 |
| 設備 | equipment | 其他设备 |

## 使用方法

### 1. 数据库准备
执行测试数据脚本：
```sql
-- 运行 test_data_products.sql 文件
```

### 2. 启动应用
确保应用正常启动，数据库连接正常。

### 3. 访问商城页面
访问 `/mall` 路径即可看到商品列表。

### 4. API测试
- 获取所有商品: `GET /api/products/list`
- 获取分类商品: `GET /api/products/category?category=engine`

## 图片处理

### 图片命名规则
- 列表图片: `{商品ID}_90x90.jpg`
- 详情图片: `{商品ID}_1200x1200.jpg`

### 图片存储路径
- 配置文件中的`carce.prefix` + `/img/product/` + 图片文件名
- 例如: `https://testcloud.carce.cc/img/product/1_90x90.jpg`

### 图片错误处理
当商品图片加载失败时，会自动显示默认图片 `/img/car/car6.jpg`

## 注意事项

1. **商品发布状态**: 只有`is_public=1`的商品才会显示
2. **价格显示**: 如果`market_price > price`，会显示原价和现价
3. **分类过滤**: 支持按商品标签进行前端分类过滤
4. **购物车集成**: 商品可以添加到购物车，使用现有的购物车系统
5. **响应式设计**: 页面支持移动端和桌面端显示

## 扩展功能

### 可以添加的功能
1. 商品搜索功能
2. 商品详情页面
3. 商品评价系统
4. 商品收藏功能
5. 商品推荐算法
6. 库存管理
7. 价格历史记录

### 性能优化
1. 商品数据缓存
2. 图片CDN加速
3. 分页查询优化
4. 数据库索引优化

## 故障排除

### 常见问题
1. **商品不显示**: 检查`is_public`字段是否为1
2. **图片不显示**: 检查图片文件是否存在，路径是否正确
3. **分类过滤不工作**: 检查商品标签映射是否正确
4. **API调用失败**: 检查后端服务是否正常，数据库连接是否正常

### 调试方法
1. 查看浏览器控制台错误信息
2. 检查后端日志
3. 验证数据库数据
4. 测试API接口响应
