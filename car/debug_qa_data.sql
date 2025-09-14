-- 调试QA数据的SQL脚本

-- 1. 检查表是否存在
SELECT 'car_question_answer表存在' as status, COUNT(*) as count FROM information_schema.tables WHERE table_name = 'car_question_answer';

-- 2. 检查表结构
DESCRIBE car_question_answer;

-- 3. 检查所有QA数据
SELECT 
    '所有QA数据' as info,
    COUNT(*) as total_count,
    COUNT(CASE WHEN delFlag = 0 THEN 1 END) as active_count,
    COUNT(CASE WHEN delFlag = 1 THEN 1 END) as deleted_count
FROM car_question_answer;

-- 4. 查看所有QA数据详情
SELECT 
    id,
    channelId,
    question,
    LENGTH(answer) as answer_length,
    showOrder,
    delFlag,
    createTime
FROM car_question_answer 
ORDER BY channelId, showOrder;

-- 5. 按频道分组统计
SELECT 
    channelId,
    COUNT(*) as qa_count,
    GROUP_CONCAT(id ORDER BY showOrder SEPARATOR ', ') as qa_ids
FROM car_question_answer 
WHERE delFlag = 0
GROUP BY channelId
ORDER BY channelId;

-- 6. 检查频道数据
SELECT 
    '频道数据' as info,
    id,
    title,
    contentType,
    delFlag
FROM car_rich_content 
WHERE contentType = 2 
ORDER BY id;

-- 7. 检查频道和QA的匹配情况
SELECT 
    rc.id as channel_id,
    rc.title as channel_title,
    COUNT(qa.id) as qa_count
FROM car_rich_content rc
LEFT JOIN car_question_answer qa ON rc.id = qa.channelId AND qa.delFlag = 0
WHERE rc.contentType = 2 AND rc.delFlag = 0
GROUP BY rc.id, rc.title
ORDER BY rc.id;
