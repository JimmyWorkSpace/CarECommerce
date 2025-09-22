# 富文本内容管理功能

## 功能概述
为ruoyi-vue项目的car模块增加了富文本内容管理功能，支持富文本内容的增删改查操作。

## 已完成的文件

### 后端文件
1. **Controller层**
   - `ruoyi-vue/car/src/main/java/com/ruoyi/car/controller/CarRichContentController.java`
   - 提供RESTful API接口，包括列表查询、详情查询、新增、修改、删除、导出功能

2. **Service层**
   - `ruoyi-vue/car/src/main/java/com/ruoyi/car/service/ICarRichContentService.java` - 服务接口
   - `ruoyi-vue/car/src/main/java/com/ruoyi/car/service/impl/CarRichContentServiceImpl.java` - 服务实现

3. **实体类和Mapper**
   - `CarRichContentEntity.java` - 已存在
   - `CarRichContentMapper.java` - 已存在

### 前端文件
1. **API接口**
   - `ruoyi-vue/ruoyi-ui/src/api/car/richContent.js` - 前端API调用接口

2. **Vue页面**
   - `ruoyi-vue/ruoyi-ui/src/views/car/richContent/index.vue` - 列表页面（包含弹窗式新增/修改功能）

### 数据库文件
- `ruoyi-vue/car/rich_content_menu.sql` - 菜单权限SQL脚本

## 功能特性

### 列表页面功能
- 支持按标题和内容类型进行搜索
- 支持批量删除
- 支持数据导出
- 表格显示：ID、标题、内容类型、排序、创建时间
- 操作按钮：修改、删除

### 新增/修改页面功能
- **弹窗式设计**：使用Element UI的Dialog组件
- **弹窗布局**：
  - 上方：基本信息区域（标题、排序）
  - 下方：富文本编辑区域
- 支持富文本编辑，最小高度500px
- 表单验证：标题为必填项
- 弹窗尺寸：屏幕宽度的95%，高度的70%，提供更大的编辑空间
- 默认设置：新增时内容类型默认为2（频道）

### 数据字段说明
- `id`: 主键ID
- `title`: 标题
- `content`: 富文本内容
- `contentType`: 内容类型（1-关于，2-频道）
- `showOrder`: 排序
- `delFlag`: 删除标记
- `createTime`: 创建时间

## 权限配置
菜单权限前缀：`car:richContent`
- `car:richContent:list` - 列表查询
- `car:richContent:query` - 详情查询
- `car:richContent:add` - 新增
- `car:richContent:edit` - 修改
- `car:richContent:remove` - 删除
- `car:richContent:export` - 导出

## 使用说明

1. **执行菜单SQL**：运行 `rich_content_menu.sql` 创建菜单和权限
2. **路由配置**：需要在路由配置中添加富文本内容相关路由
3. **访问路径**：
   - 列表页：`/car/richContent`
   - 新增/修改：通过弹窗操作，无需单独路由

## 技术特点
- 遵循ruoyi框架规范
- 使用Element UI组件库
- 支持富文本编辑器
- 响应式布局设计
- 完整的权限控制
- 符合RESTful API设计规范
