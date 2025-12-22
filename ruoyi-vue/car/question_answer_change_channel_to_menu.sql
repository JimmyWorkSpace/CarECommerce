-- 问答模块：将频道关联改为菜单关联
-- 执行此SQL前请备份数据库

-- 修改表结构：将channelId字段改为menuId
ALTER TABLE car_question_answer CHANGE COLUMN channelId menuId BIGINT(20) NOT NULL COMMENT '菜單ID';

-- 注意：如果表中已有数据，需要手动迁移数据
-- 例如：UPDATE car_question_answer SET menuId = (对应的菜单ID) WHERE menuId = (原来的频道ID);
-- 具体迁移逻辑需要根据业务实际情况调整

