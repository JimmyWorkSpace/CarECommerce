-- 测试QA模块的SQL脚本

-- 1. 检查表是否存在
SHOW TABLES LIKE 'car_question_answer';

-- 2. 检查表结构
DESCRIBE car_question_answer;

-- 3. 检查是否有数据
SELECT COUNT(*) as total_qa FROM car_question_answer;

-- 4. 查看所有QA数据
SELECT id, channelId, question, showOrder, delFlag, createTime 
FROM car_question_answer 
ORDER BY channelId, showOrder;

-- 5. 按频道分组查看QA数据
SELECT 
    channelId,
    COUNT(*) as qa_count,
    GROUP_CONCAT(question SEPARATOR ' | ') as questions
FROM car_question_answer 
WHERE delFlag = 0
GROUP BY channelId
ORDER BY channelId;
