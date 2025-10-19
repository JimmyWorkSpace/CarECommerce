-- 测试物流状态显示功能的SQL脚本
-- 为已支付的订单添加不同的物流状态测试数据

-- 更新订单1：有物流状态码的情况
UPDATE car_order_info 
SET logicStatusCode = '303', 
    logicMsg = NULL,
    logicMsgJson = '{"logisticsStatus":"303","shipmentNo":"TEST123456"}'
WHERE order_no = '202510181403399891' AND order_status = 2;

-- 更新订单2：只有物流消息的情况（模拟alert消息）
UPDATE car_order_info 
SET logicStatusCode = NULL, 
    logicMsg = '廠商訂單編號重覆，請重新設定，請回到原廠商頁面重新操作，本視窗即將關閉。',
    logicMsgJson = NULL
WHERE order_no = '202510181403399892' AND order_status = 2;

-- 更新订单3：已送达状态
UPDATE car_order_info 
SET logicStatusCode = '305', 
    logicMsg = NULL,
    logicMsgJson = '{"logisticsStatus":"305","shipmentNo":"TEST789012"}'
WHERE order_no = '202510181403399893' AND order_status = 2;

-- 更新订单4：配送异常状态
UPDATE car_order_info 
SET logicStatusCode = '308', 
    logicMsg = NULL,
    logicMsgJson = '{"logisticsStatus":"308","shipmentNo":"TEST345678"}'
WHERE order_no = '202510181403399894' AND order_status = 2;

-- 更新订单5：无物流信息（保持默认状态）
UPDATE car_order_info 
SET logicStatusCode = NULL, 
    logicMsg = NULL,
    logicMsgJson = NULL
WHERE order_no = '202510181403399895' AND order_status = 2;

-- 更新订单6：空字符串状态（测试边界情况）
UPDATE car_order_info 
SET logicStatusCode = '', 
    logicMsg = '',
    logicMsgJson = NULL
WHERE order_no = '202510181403399896' AND order_status = 2;

-- 查询结果验证
SELECT 
    order_no,
    order_status,
    logicStatusCode,
    logicMsg,
    CASE 
        WHEN logicStatusCode IS NOT NULL THEN '有状态码'
        WHEN logicMsg IS NOT NULL THEN '有消息'
        ELSE '无物流信息'
    END as logistics_info_type
FROM car_order_info 
WHERE order_status = 2 
ORDER BY order_no;
