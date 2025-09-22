-- 插入测试推荐车辆数据
-- 首先需要确保car_sales表中有一些测试数据，然后创建推荐记录

-- 插入推荐记录（假设car_sales表中已有ID为1-9的车辆数据）
INSERT INTO car_recommand (recommandType, recommandId, showOrder, delFlag, createTime) VALUES
(1, 1, 1, 0, NOW()),
(1, 2, 2, 0, NOW()),
(1, 3, 3, 0, NOW()),
(1, 4, 4, 0, NOW()),
(1, 5, 5, 0, NOW()),
(1, 6, 6, 0, NOW()),
(1, 7, 7, 0, NOW()),
(1, 8, 8, 0, NOW()),
(1, 9, 9, 0, NOW());

-- 查询推荐车辆数据验证
SELECT 
    cr.id as recommand_id,
    cr.recommandType,
    cr.recommandId,
    cr.showOrder,
    cs.sale_title,
    cs.sale_price,
    cs.status,
    cs.is_visible
FROM car_recommand cr
LEFT JOIN car_sales cs ON cr.recommandId = cs.id
WHERE cr.recommandType = 1 
  AND cr.delFlag = 0 
  AND cs.status = '上架'
  AND cs.is_visible = 1
ORDER BY cr.showOrder;
