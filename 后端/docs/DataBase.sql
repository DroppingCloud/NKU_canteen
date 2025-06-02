/*
 Navicat Premium Dump SQL

 Source Server         : NKU
 Source Server Type    : MySQL
 Source Server Version : 80030 (8.0.30)
 Source Host           : dbconn.sealoshzh.site:36140
 Source Schema         : canteen

 Target Server Type    : MySQL
 Target Server Version : 80030 (8.0.30)
 File Encoding         : 65001

 Date: 25/05/2025 00:18:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for Canteen
-- ----------------------------
DROP TABLE IF EXISTS `Canteen`;
CREATE TABLE `Canteen`  (
  `canteen_id` int NOT NULL AUTO_INCREMENT COMMENT '食堂编号，主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '食堂名称',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '食堂地理位置描述',
  `open_time` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '营业时间',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '食堂样图',
  `campus` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '食堂校区',
  PRIMARY KEY (`canteen_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for Comment
-- ----------------------------
DROP TABLE IF EXISTS `Comment`;
CREATE TABLE `Comment`  (
  `comment_id` int NOT NULL AUTO_INCREMENT COMMENT '评论编号，主键',
  `user_id` int NOT NULL COMMENT '评论者编号（逻辑外键）',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论创建时间',
  PRIMARY KEY (`comment_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for Comment_Target
-- ----------------------------
DROP TABLE IF EXISTS `Comment_Target`;
CREATE TABLE `Comment_Target`  (
  `comment_target_id` int NOT NULL AUTO_INCREMENT COMMENT '评论目标记录编号，主键',
  `comment_id` int NOT NULL COMMENT '评论编号（逻辑外键）',
  `target_type` enum('canteen','stall','dish','comment') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标类型',
  `target_id` int NOT NULL COMMENT '目标编号，对应目标实体主键',
  PRIMARY KEY (`comment_target_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for Dish
-- ----------------------------
DROP TABLE IF EXISTS `Dish`;
CREATE TABLE `Dish`  (
  `dish_id` int NOT NULL AUTO_INCREMENT COMMENT '菜品编号，主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜品名称',
  `intro` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '菜品简介',
  `price` decimal(6, 2) NOT NULL COMMENT '菜品价格，单位元，保留两位小数',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜品图片地址',
  `stall_id` int NOT NULL COMMENT '所属档口编号（逻辑外键）',
  PRIMARY KEY (`dish_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜品表，通过触发器确保价格非负' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Triggers for table Dish
-- ----------------------------
DELIMITER $$
CREATE TRIGGER `before_dish_insert` BEFORE INSERT ON `Dish` FOR EACH ROW
BEGIN
    IF NEW.price < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = '菜品价格不能为负数';
    END IF;
END
$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER `before_dish_update` BEFORE UPDATE ON `Dish` FOR EACH ROW
BEGIN
    IF NEW.price < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = '菜品价格不能为负数';
    END IF;
END
$$
DELIMITER ;

-- ----------------------------
-- Table structure for Like
-- ----------------------------
DROP TABLE IF EXISTS `Like`;
CREATE TABLE `Like`  (
  `like_id` int NOT NULL AUTO_INCREMENT COMMENT '点赞编号，主键',
  `user_id` int NOT NULL COMMENT '点赞者编号（逻辑外键）',
  PRIMARY KEY (`like_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for Like_Target
-- ----------------------------
DROP TABLE IF EXISTS `Like_Target`;
CREATE TABLE `Like_Target`  (
  `like_target_id` int NOT NULL AUTO_INCREMENT COMMENT '点赞目标记录编号，主键',
  `like_id` int NOT NULL COMMENT '点赞编号（逻辑外键）',
  `target_type` enum('canteen','stall','dish','comment') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标类型',
  `target_id` int NOT NULL COMMENT '目标编号，对应目标实体主键',
  PRIMARY KEY (`like_target_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for Rating
-- ----------------------------
DROP TABLE IF EXISTS `Rating`;
CREATE TABLE `Rating`  (
  `rating_id` int NOT NULL AUTO_INCREMENT COMMENT '评分记录编号，主键',
  `user_id` int NOT NULL COMMENT '评分者编号',
  `target_type` enum('stall','dish') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标类型',
  `target_id` int NOT NULL COMMENT '目标编号',
  `score` decimal(2, 1) NOT NULL COMMENT '评分分数，范围 0.0 到 5.0，保留 1 位小数',
  PRIMARY KEY (`rating_id`) USING BTREE,
  UNIQUE INDEX `uniq_user_target`(`user_id` ASC, `target_type` ASC, `target_id` ASC) USING BTREE,
  CONSTRAINT `Rating_chk_score` CHECK ((`score` >= 0.0) and (`score` <= 5.0))
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for Stall
-- ----------------------------
DROP TABLE IF EXISTS `Stall`;
CREATE TABLE `Stall`  (
  `stall_id` int NOT NULL AUTO_INCREMENT COMMENT '档口编号，主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '档口名称',
  `canteen_id` int NOT NULL COMMENT '所属食堂编号（逻辑外键）',
  `floor` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '楼层位置，如"1F"、"2F"等',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '档口样图',
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`stall_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for User
-- ----------------------------
DROP TABLE IF EXISTS `User`;
CREATE TABLE `User`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户编号，主键',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `gender` tinyint NULL DEFAULT NULL COMMENT '性别：1-男，2-女',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像 URL',
  `user_type` enum('user','admin') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'user' COMMENT '用户类型：user-普通用户，admin-管理员',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码，建议加密存储',
  `register_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电子邮箱，唯一',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Procedure for updating dish
-- ----------------------------
DELIMITER $$
CREATE PROCEDURE `update_dish`(
    IN p_dish_id INT,
    IN p_name VARCHAR(100),
    IN p_intro TEXT,
    IN p_price DECIMAL(6, 2),
    IN p_image_url VARCHAR(255),
    IN p_stall_id INT,
    OUT p_result INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        SET p_result = 0; -- 更新失败
        ROLLBACK;
    END;
    
    START TRANSACTION;
    
    -- 检查菜品是否存在
    IF NOT EXISTS (SELECT 1 FROM Dish WHERE dish_id = p_dish_id) THEN
        SET p_result = -1; -- 菜品不存在
        ROLLBACK;
    -- 检查价格是否为负
    ELSEIF p_price IS NOT NULL AND p_price < 0 THEN
        SET p_result = -2; -- 价格为负
        ROLLBACK;
    ELSE
        -- 更新菜品信息
        UPDATE Dish SET
            name = COALESCE(p_name, name),
            intro = COALESCE(p_intro, intro),
            price = COALESCE(p_price, price),
            image_url = COALESCE(p_image_url, image_url),
            stall_id = COALESCE(p_stall_id, stall_id)
        WHERE dish_id = p_dish_id;
        
        SET p_result = ROW_COUNT(); -- 成功更新的行数
        COMMIT;
    END IF;
END$$
DELIMITER ;

-- ----------------------------
-- Procedure for updating stall
-- ----------------------------
DELIMITER $$
CREATE PROCEDURE `update_stall`(
    IN p_stall_id INT,
    IN p_name VARCHAR(100),
    IN p_canteen_id INT,
    IN p_floor VARCHAR(20),
    IN p_img VARCHAR(255),
    IN p_intro VARCHAR(255),
    OUT p_result INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        SET p_result = 0; -- 更新失败
        ROLLBACK;
    END;
    
    START TRANSACTION;
    
    -- 检查档口是否存在
    IF NOT EXISTS (SELECT 1 FROM Stall WHERE stall_id = p_stall_id) THEN
        SET p_result = -1; -- 档口不存在
        ROLLBACK;
    -- 检查食堂是否存在(当食堂ID不为空时)
    ELSEIF p_canteen_id IS NOT NULL AND NOT EXISTS (SELECT 1 FROM Canteen WHERE canteen_id = p_canteen_id) THEN
        SET p_result = -2; -- 食堂不存在
        ROLLBACK;
    -- 检查名称是否为空（当名称参数不为NULL时）
    ELSEIF p_name IS NOT NULL AND TRIM(p_name) = '' THEN
        SET p_result = -3; -- 名称不能为空
        ROLLBACK;
    -- 检查食堂ID是否为空（当食堂ID参数不为NULL时）
    ELSEIF p_canteen_id IS NOT NULL AND p_canteen_id <= 0 THEN
        SET p_result = -4; -- 食堂ID无效
        ROLLBACK;
    -- 检查楼层是否为空（当楼层参数不为NULL时）
    ELSEIF p_floor IS NOT NULL AND TRIM(p_floor) = '' THEN
        SET p_result = -5; -- 楼层不能为空
        ROLLBACK;
    ELSE
        -- 更新档口信息
        UPDATE Stall SET
            name = COALESCE(p_name, name),
            canteen_id = COALESCE(p_canteen_id, canteen_id),
            floor = COALESCE(p_floor, floor),
            img = COALESCE(p_img, img),
            intro = COALESCE(p_intro, intro)
        WHERE stall_id = p_stall_id;
        
        SET p_result = ROW_COUNT(); -- 成功更新的行数
        COMMIT;
    END IF;
END$$
DELIMITER ;

-- ----------------------------
-- View for stall dishes with like and comment count
-- ----------------------------
DROP VIEW IF EXISTS `Stall_Dish_View`;
CREATE VIEW `Stall_Dish_View` AS
SELECT 
    d.dish_id,
    d.name,
    d.intro,
    d.price,
    d.image_url,
    d.stall_id,
    s.name AS stall_name,
    s.canteen_id,
    c.name AS canteen_name,
    (SELECT COUNT(*) FROM `Like` l JOIN `Like_Target` lt ON l.like_id = lt.like_id 
     WHERE lt.target_type = 'dish' AND lt.target_id = d.dish_id) AS like_count,
    (SELECT COUNT(*) FROM `Comment` cm JOIN `Comment_Target` ct ON cm.comment_id = ct.comment_id 
     WHERE ct.target_type = 'dish' AND ct.target_id = d.dish_id) AS comment_count
FROM 
    `Dish` d
JOIN 
    `Stall` s ON d.stall_id = s.stall_id
JOIN 
    `Canteen` c ON s.canteen_id = c.canteen_id
ORDER BY 
    d.stall_id, d.dish_id;

SET FOREIGN_KEY_CHECKS = 1;
