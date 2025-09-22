# CarSalesMapper 重构说明

## 重构概述
将 CarSalesMapper 从注解形式的SQL改为XML形式的MyBatis SQL语句，提高代码的可维护性和可读性。

## 主要变更

### 1. 创建XML映射文件
**文件位置**: `car/src/main/resources/mapper/carcecloud/CarSalesMapper.xml`

**包含的SQL语句**:
- `getByUid` - 根据uid查询车辆销售信息
- `getById` - 根据id查询车辆销售信息  
- `updateUidById` - 根据id更新uid
- `selectCarSalesList` - 根据条件查询车辆销售列表
- `selectCarSalesByIds` - 根据ID列表批量查询车辆销售信息
- `selectCarBaseInfoBySaleId` - 根据车辆销售ID查询车辆基本信息
- `selectCarBaseInfoByUid` - 根据uid查询车辆基本信息

### 2. 更新Mapper接口
**文件位置**: `car/src/main/java/com/ruoyi/car/mapper/carcecloud/CarSalesMapper.java`

**变更内容**:
- 移除 `@Select` 和 `@Update` 注解
- 添加完整的方法注释
- 新增批量查询和车辆基本信息查询方法

### 3. 简化Service实现
**文件位置**: `car/src/main/java/com/ruoyi/car/service/impl/CarSalesServiceImpl.java`

**变更内容**:
- 移除复杂的Example查询逻辑
- 直接调用Mapper方法
- 移除不再需要的导入

## 技术优势

### 1. 可维护性提升
- XML格式的SQL更易读和维护
- 复杂的动态SQL更容易实现
- 支持SQL语法高亮和格式化

### 2. 性能优化
- 避免了Example查询的额外开销
- 直接使用原生SQL，性能更好
- 支持更复杂的查询优化

### 3. 功能增强
- 支持动态条件查询
- 支持批量操作
- 支持复杂的关联查询

## XML SQL特性

### 1. 动态SQL
```xml
<where>
    <if test="saleTitle != null and saleTitle != ''">
        AND sale_title LIKE CONCAT('%', #{saleTitle}, '%')
    </if>
    <if test="status != null and status != ''">
        AND status = #{status}
    </if>
</where>
```

### 2. 批量查询
```xml
<foreach collection="list" item="id" open="(" separator="," close=")">
    #{id}
</foreach>
```

### 3. 复杂关联查询
```xml
SELECT
    cb.brand,                    -- 品牌
    c.manufacture_year,          -- 出厂年
    cs.sale_price,               -- 售价
    cl.name AS location_name     -- 地点
FROM cars c
LEFT JOIN car_brand cb ON cb.id = c.id_brand
LEFT JOIN car_sales cs ON c.id = cs.car_id
LEFT JOIN car_location cl ON cs.car_location_id = cl.id
```

## 兼容性说明

- 保持了原有的方法签名不变
- 保持了原有的业务逻辑不变
- 对外接口完全兼容

## 测试建议

1. 测试所有查询方法的正确性
2. 测试动态条件查询功能
3. 测试批量查询功能
4. 测试车辆基本信息查询功能
5. 验证性能是否有提升

## 注意事项

- 确保XML文件路径正确
- 确保MyBatis配置正确扫描XML文件
- 建议在测试环境先验证功能正确性
