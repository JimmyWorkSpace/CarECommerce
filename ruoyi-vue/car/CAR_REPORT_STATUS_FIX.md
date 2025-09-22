# 车辆检举模块处理状态修复

## 问题描述
车辆检举模块在新增或修改时，处理状态下拉框和实际值不匹配。接口返回的状态值是 `"cancelled"`，但字典中定义的是 `"rejected"`。

## 问题原因
- 数据库字典数据中定义的状态值是 `"rejected"`（已駁回）
- 实际业务数据中存储的状态值是 `"cancelled"`（已取消）
- 前端下拉框基于字典数据渲染，导致显示不匹配

## 解决方案
统一使用 `"cancelled"` 作为取消状态的值，因为更符合业务逻辑。

### 1. 更新字典数据
执行以下SQL更新字典数据：

```sql
-- 更新字典數據
UPDATE sys_dict_data 
SET dict_label = '已取消', dict_value = 'cancelled', remark = '已取消'
WHERE dict_type = 'car_report_status' AND dict_value = 'rejected';

-- 如果沒有找到 'rejected' 記錄，則插入新的 'cancelled' 記錄
INSERT INTO sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
SELECT 
    (SELECT MAX(dict_code) + 1 FROM sys_dict_data WHERE dict_type = 'car_report_status'),
    4,
    '已取消',
    'cancelled',
    'car_report_status',
    '',
    'danger',
    'N',
    '0',
    'admin',
    NOW(),
    '',
    NULL,
    '已取消'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_data 
    WHERE dict_type = 'car_report_status' AND dict_value = 'cancelled'
);
```

### 2. 更新代码注释
已更新 `CarReportEntity.java` 中的注释，将 `rejected` 改为 `cancelled`。

### 3. 状态值对应关系
| 状态值 | 显示名称 | 说明 |
|--------|----------|------|
| submitted | 已提交 | 检举刚提交时的状态 |
| processing | 处理中 | 正在处理检举 |
| processed | 已处理 | 检举处理完成 |
| cancelled | 已取消 | 检举被取消 |

## 验证步骤
1. 执行SQL脚本更新字典数据
2. 重启应用使字典缓存生效
3. 访问车辆检举管理页面
4. 检查下拉框是否正确显示所有状态选项
5. 测试新增和修改功能，确认状态值正确匹配

## 注意事项
- 如果数据库中已有 `"rejected"` 状态的数据，需要根据业务需求决定是否要迁移为 `"cancelled"`
- 建议在测试环境先验证修改效果
- 修改后需要清理Redis缓存中的字典数据
