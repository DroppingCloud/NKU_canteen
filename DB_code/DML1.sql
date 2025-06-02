-- 1. 清空所有数据
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM `Comment_Target`;
DELETE FROM `Comment`;
DELETE FROM `Like_Target`;
DELETE FROM `Like`;
DELETE FROM `Rating`;
DELETE FROM `Dish`;
DELETE FROM `Stall`;
DELETE FROM `Canteen`;
DELETE FROM `User`;

SET FOREIGN_KEY_CHECKS = 1;

-- 2. 重置主键计数器
ALTER TABLE `User` AUTO_INCREMENT = 1;
ALTER TABLE `Canteen` AUTO_INCREMENT = 1;
ALTER TABLE `Stall` AUTO_INCREMENT = 1;
ALTER TABLE `Dish` AUTO_INCREMENT = 1;
ALTER TABLE `Comment` AUTO_INCREMENT = 1;
ALTER TABLE `Comment_Target` AUTO_INCREMENT = 1;
ALTER TABLE `Like` AUTO_INCREMENT = 1;
ALTER TABLE `Like_Target` AUTO_INCREMENT = 1;
ALTER TABLE `Rating` AUTO_INCREMENT = 1;


-- 3. 再插入测试数据
-- 插入用户（含1名管理员）
INSERT INTO `User` (nickname, gender, avatar, user_type, password, email)
VALUES 
('Alice', 2, '/images/avatar1.png', 'user', 'alice123', 'alice@test.com'),
('Bob', 1, '/images/avatar2.png', 'user', 'bob123', 'bob@test.com'),
('Charlie', 1, '/images/avatar3.png', 'user', 'charlie123', 'charlie@test.com'),
('Diana', 2, '/images/avatar4.png', 'user', 'diana123', 'diana@test.com'),
('Evan', 1, '/images/avatar5.png', 'user', 'evan123', 'evan@test.com'),
('Fay', 2, '/images/avatar6.png', 'user', 'fay123', 'fay@test.com'),
('Grace', 2, '/images/avatar7.png', 'user', 'grace123', 'grace@test.com'),
('Henry', 1, '/images/avatar8.png', 'user', 'henry123', 'henry@test.com'),
('AdminTest', 1, '/images/admin.png', 'admin', 'admin123', 'admin@test.com');

-- 插入食堂
INSERT INTO `Canteen` (name, location, open_time, img, campus)
VALUES 
('学一食堂', '主楼东侧', '7:00-21:00', NULL, '八里台'),
('学二食堂', '体育馆北侧', '6:30-20:30', NULL, '八里台'),
('学三食堂', '文科组团', '6:30-22:00', NULL, '津南'),
('学四食堂', '西门', '7:00-20:00', NULL, '津南'),
('学五食堂', '东南角', '6:00-22:00', NULL, '八里台');

-- 插入档口
INSERT INTO `Stall` (name, canteen_id, floor, img, intro)
VALUES 
('米饭档', 1, '1F', NULL, '主营米饭'),
('拉面档', 2, '2F', NULL, '各类拉面'),
('麻辣烫档', 3, '1F', NULL, '自选锅底'),
('水饺档', 4, '1F', NULL, '手工水饺'),
('面包坊', 5, '1F', NULL, '现烤面包'),
('卤味档', 1, '2F', NULL, '冷卤熟食'),
('蒸菜馆', 2, '1F', NULL, '健康蒸菜'),
('素食堂', 3, '2F', NULL, '纯素料理');

-- 插入菜品
INSERT INTO `Dish` (name, intro, price, image_url, stall_id)
VALUES 
('番茄炒蛋盖饭', '经典家常菜', 10.5, NULL, 1),
('牛肉拉面', '精选牛肉', 15.0, NULL, 2),
('招牌麻辣烫', '自选酱料', 18.5, NULL, 3),
('鲜肉水饺', '现包水饺', 12.0, NULL, 4),
('巧克力面包', '新鲜出炉', 6.0, NULL, 5),
('鸭脖拼盘', '香辣诱人', 13.0, NULL, 6),
('豆豉蒸排骨', '蒸汽健康', 14.0, NULL, 7),
('素什锦', '多种蔬菜组合', 9.0, NULL, 8);

-- 插入评论
INSERT INTO `Comment` (user_id, content)
VALUES 
(1, '味道不错，下次再来！'),
(2, '服务一般，味道可以。'),
(3, '干净卫生，值得推荐。'),
(4, '太咸了，不喜欢。'),
(5, '份量足，性价比高。'),
(6, '没有辣味，不够劲！'),
(7, '饭有点硬，扣一分。'),
(8, '超出预期，赞一个！');

-- 插入评论目标
INSERT INTO `Comment_Target` (comment_id, target_type, target_id)
VALUES 
(1, 'dish', 1),
(2, 'dish', 2),
(3, 'stall', 3),
(4, 'stall', 4),
(5, 'canteen', 1),
(6, 'dish', 3),
(7, 'dish', 1),
(8, 'canteen', 3);

-- 插入点赞
INSERT INTO `Like` (user_id)
VALUES (1), (2), (3), (4), (5), (6), (7), (8);

-- 插入点赞目标
INSERT INTO `Like_Target` (like_id, target_type, target_id)
VALUES 
(1, 'dish', 1),
(2, 'dish', 2),
(3, 'stall', 3),
(4, 'comment', 1),
(5, 'comment', 2),
(6, 'canteen', 1),
(7, 'dish', 6),
(8, 'dish', 3);

-- 插入评分
INSERT INTO `Rating` (user_id, target_type, target_id, score)
VALUES 
(1, 'dish', 1, 4.5),
(1, 'stall', 2, 3.5),
(2, 'dish', 2, 5.0),
(2, 'stall', 3, 4.0),
(3, 'dish', 3, 3.0),
(3, 'stall', 1, 4.2),
(4, 'dish', 4, 3.8),
(4, 'stall', 4, 4.0),
(5, 'dish', 5, 4.6),
(6, 'stall', 5, 4.7);
