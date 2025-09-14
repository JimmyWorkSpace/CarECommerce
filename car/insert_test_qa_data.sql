-- 插入测试QA数据
-- 确保有频道数据存在，然后插入对应的QA数据

-- 首先检查频道数据
SELECT id, title FROM car_rich_content WHERE contentType = 2 AND delFlag = 0;

-- 插入测试QA数据（假设频道ID为10, 11, 12, 13）
INSERT INTO car_question_answer (channelId, question, answer, showOrder, delFlag, createTime) VALUES
(10, '測試問題1 - 賣車頻道', '<div class="qa-answer"><p>這是賣車頻道的測試答案1。</p></div>', 1, 0, NOW()),
(10, '測試問題2 - 賣車頻道', '<div class="qa-answer"><p>這是賣車頻道的測試答案2。</p></div>', 2, 0, NOW()),
(11, '測試問題1 - 購車服務', '<div class="qa-answer"><p>這是購車服務的測試答案1。</p></div>', 1, 0, NOW()),
(11, '測試問題2 - 購車服務', '<div class="qa-answer"><p>這是購車服務的測試答案2。</p></div>', 2, 0, NOW()),
(12, '測試問題1 - 消費者保障', '<div class="qa-answer"><p>這是消費者保障的測試答案1。</p></div>', 1, 0, NOW()),
(12, '測試問題2 - 消費者保障', '<div class="qa-answer"><p>這是消費者保障的測試答案2。</p></div>', 2, 0, NOW()),
(13, '測試問題1 - 加盟專區', '<div class="qa-answer"><p>這是加盟專區的測試答案1。</p></div>', 1, 0, NOW()),
(13, '測試問題2 - 加盟專區', '<div class="qa-answer"><p>這是加盟專區的測試答案2。</p></div>', 2, 0, NOW());

-- 验证插入的数据
SELECT 
    qa.id,
    qa.channelId,
    rc.title as channel_title,
    qa.question,
    qa.showOrder,
    qa.delFlag
FROM car_question_answer qa
LEFT JOIN car_rich_content rc ON qa.channelId = rc.id
WHERE qa.delFlag = 0
ORDER BY qa.channelId, qa.showOrder;
