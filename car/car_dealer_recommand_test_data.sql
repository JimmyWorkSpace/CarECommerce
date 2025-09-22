-- 插入测试推荐店家数据
-- 首先需要确保car_dealers表中有一些测试数据，然后创建推荐记录

-- 插入推荐记录（假设car_dealers表中已有ID为1-6的店家数据）
INSERT INTO car_recommand (recommandType, recommandId, showOrder, delFlag, createTime) VALUES
(0, 1, 1, 0, NOW()),
(0, 2, 2, 0, NOW()),
(0, 3, 3, 0, NOW()),
(0, 4, 4, 0, NOW()),
(0, 5, 5, 0, NOW()),
(0, 6, 6, 0, NOW());

-- 查询推荐店家数据验证
SELECT 
    cr.id as recommand_id,
    cr.recommandType,
    cr.recommandId,
    cr.showOrder,
    cd.dealer_name,
    cd.public_address,
    cd.company_phone,
    cd.description
FROM car_recommand cr
LEFT JOIN car_dealers cd ON cr.recommandId = cd.id
WHERE cr.recommandType = 0 
  AND cr.delFlag = 0 
ORDER BY cr.showOrder;
