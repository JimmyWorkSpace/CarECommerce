# 推荐好车页面增加"是否可见"过滤功能

## 功能概述
在推荐好车页面增加了"是否可见"的过滤条件，用户可以根据车辆的可见性状态进行筛选。

## 实现内容

### 1. 前端页面更新
**文件**: `ruoyi-ui/src/views/car/salesRecommend/index.vue`

**变更内容**:
- 在搜索表单中添加"是否可见"下拉选择框
- 添加两个选项：
  - 可见 (值为1)
  - 不可见 (值为0)
- 更新查询参数，添加`isVisible`字段

**代码变更**:
```vue
<el-form-item label="是否可见" prop="isVisible">
  <el-select v-model="queryParams.isVisible" placeholder="请选择是否可见" clearable size="small">
    <el-option label="可见" :value="1" />
    <el-option label="不可见" :value="0" />
  </el-select>
</el-form-item>
```

### 2. 后端SQL查询更新
**文件**: `car/src/main/resources/mapper/carcecloud/CarSalesMapper.xml`

**变更内容**:
- 在`selectCarSalesList`查询中添加`is_visible`字段的过滤条件
- 使用动态SQL，当`isVisible`参数不为空时进行过滤

**代码变更**:
```xml
<if test="entity.isVisible != null">
    AND cs.is_visible = #{entity.isVisible}
</if>
```

### 3. 数据模型支持
**现有支持**:
- `CarSalesEntity`实体类已包含`isVisible`字段
- `CarSalesDto`数据传输对象已包含`isVisible`字段
- 数据库表`car_sales`已包含`is_visible`字段，默认值为1

## 功能特性

### 1. 过滤选项
- **可见**: 显示`is_visible = 1`的车辆
- **不可见**: 显示`is_visible = 0`的车辆
- **全部**: 不选择时显示所有车辆

### 2. 用户体验
- 下拉框支持清空选择
- 与其他过滤条件可以组合使用
- 支持重置功能

### 3. 数据完整性
- 默认值：新车辆默认为可见状态(`is_visible = 1`)
- 数据类型：使用Integer类型，1表示可见，0表示不可见

## 使用说明

### 1. 管理员操作
1. 进入"推荐好车"页面
2. 在搜索条件中选择"是否可见"状态
3. 点击"搜索"按钮进行筛选
4. 可以与其他条件（销售员、状态等）组合使用

### 2. 过滤逻辑
- 选择"可见"：只显示可见的车辆
- 选择"不可见"：只显示不可见的车辆
- 不选择：显示所有车辆

## 技术实现

### 1. 前端技术
- Vue.js 2.x
- Element UI组件库
- 响应式数据绑定

### 2. 后端技术
- MyBatis动态SQL
- Spring Boot
- 参数化查询防止SQL注入

### 3. 数据库
- 字段类型：INTEGER
- 默认值：1（可见）
- 索引：建议在`is_visible`字段上创建索引以提高查询性能

## 注意事项

1. **权限控制**: 确保只有有权限的用户才能修改车辆的可见性状态
2. **数据一致性**: 修改车辆可见性时，需要同步更新相关缓存
3. **性能优化**: 如果数据量大，建议在`is_visible`字段上创建索引
4. **业务逻辑**: 不可见的车辆通常不应该在前端展示给普通用户

## 扩展建议

1. **批量操作**: 可以添加批量设置车辆可见性的功能
2. **自动规则**: 可以设置自动规则，如已售车辆自动设为不可见
3. **审核流程**: 可以添加审核流程，新车辆需要审核后才能设为可见
4. **统计功能**: 可以添加可见/不可见车辆的统计报表

