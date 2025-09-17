# 问答模块管理功能

## 功能概述
为ruoyi-vue项目的car模块增加了问答模块管理功能，支持问答内容的增删改查操作，包括列表页、搜索和修改功能。**使用tkMyBatis现成方法，避免自定义SQL映射问题。**

## 已完成的文件

### 后端文件
1. **Controller层**
   - `ruoyi-vue/car/src/main/java/com/ruoyi/car/controller/CarQuestionAnswerController.java`
   - 提供RESTful API接口，包括列表查询、详情查询、新增、修改、删除、导出功能

2. **Service层**
   - `ruoyi-vue/car/src/main/java/com/ruoyi/car/service/ICarQuestionAnswerService.java` - 服务接口
   - `ruoyi-vue/car/src/main/java/com/ruoyi/car/service/impl/CarQuestionAnswerServiceImpl.java` - 服务实现

3. **实体类和Mapper**
   - `CarQuestionAnswerEntity.java` - 已存在
   - `CarQuestionAnswerMapper.java` - 已存在（继承tkMyBatis的Mapper接口）

### 前端文件
1. **API接口**
   - `ruoyi-vue/ruoyi-ui/src/api/car/questionAnswer.js` - 前端API调用接口

2. **Vue页面**
   - `ruoyi-vue/ruoyi-ui/src/views/car/questionAnswer/index.vue` - 列表页面（包含弹窗式新增/修改功能）

### 数据库文件
- `ruoyi-vue/car/question_answer_menu.sql` - 菜单权限SQL脚本

## 功能特性

### 列表页面功能
- 支持按频道和问题内容进行搜索
- 支持批量删除（逻辑删除）
- 支持数据导出
- 表格显示：频道、问题、回答、排序、创建时间
- 操作按钮：修改、删除

### 新增/修改页面功能
- **弹窗式设计**：使用Element UI的Dialog组件
- **弹窗布局**：
  - 上方：基本信息区域（频道选择、排序）
  - 中间：问题输入区域（文本域）
  - 下方：回答编辑区域（富文本编辑器）
- 支持富文本编辑回答内容，最小高度300px
- 表单验证：频道、问题、回答为必填项
- 弹窗尺寸：屏幕宽度的80%，高度的90%，提供合适的编辑空间
- 频道选择：自动加载CarRichContentEntity中contentType=2的频道数据

### 数据字段说明
- `id`: 主键ID
- `channelId`: 频道ID（对应CarRichContentEntity的id）
- `question`: 问题内容
- `answer`: 回答内容（富文本）
- `showOrder`: 排序
- `delFlag`: 删除标记
- `createTime`: 创建时间

## 技术实现

### 后端实现（使用tkMyBatis现成方法）
- **查询单个记录**：使用`selectOne()`方法
- **条件查询**：使用`Example`和`Criteria`构建查询条件
- **插入记录**：使用`insertSelective()`方法
- **更新记录**：使用`updateByPrimaryKeySelective()`方法
- **逻辑删除**：通过更新`delFlag`字段实现
- **分页查询**：结合ruoyi框架的分页功能

### 前端实现
- 使用Element UI组件库
- 富文本编辑器支持回答内容编辑
- 频道数据动态加载
- 响应式布局设计

## 权限配置
菜单权限包括：
- `car:questionAnswer:list` - 查询权限
- `car:questionAnswer:query` - 详情查询权限
- `car:questionAnswer:add` - 新增权限
- `car:questionAnswer:edit` - 修改权限
- `car:questionAnswer:remove` - 删除权限
- `car:questionAnswer:export` - 导出权限

## 使用说明
1. 执行 `question_answer_menu.sql` 脚本创建菜单权限
2. 重启后端服务
3. 在前端系统中即可看到"问答模块"菜单
4. 可以通过频道管理功能先创建频道，然后在问答模块中关联使用

## 关联关系
- 问答模块的channelId字段关联CarRichContentEntity的id字段
- 前端会自动加载频道列表供用户选择
- 支持按频道筛选问答内容

## 技术优势
- **避免SQL映射问题**：完全使用tkMyBatis现成方法，无需自定义XML映射
- **逻辑删除**：通过更新delFlag字段实现软删除，数据更安全
- **条件查询**：使用Example构建复杂查询条件，代码更简洁
- **自动填充**：创建时间、删除标记等字段自动设置