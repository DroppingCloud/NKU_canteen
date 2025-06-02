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

 Date: 02/06/2025 17:03:30
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
  `campus` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`canteen_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of Canteen
-- ----------------------------
INSERT INTO `Canteen` VALUES (1, '学一食堂', '主楼东侧', '7:00-21:00', 'https://cdn.pixabay.com/photo/2016/08/21/14/49/cafe-1609795_1280.jpg', '八里台');
INSERT INTO `Canteen` VALUES (2, '学二食堂', '体育馆北侧', '6:30-20:30', 'https://cdn.pixabay.com/photo/2019/10/26/10/18/warsaw-4579114_1280.jpg', '八里台');
INSERT INTO `Canteen` VALUES (3, '学三食堂', '文科组团', '6:30-22:00', 'https://cdn.pixabay.com/photo/2021/01/28/01/27/school-5956715_1280.jpg', '津南');
INSERT INTO `Canteen` VALUES (4, '学四食堂', '西门', '7:00-20:00', 'https://cdn.pixabay.com/photo/2018/11/11/15/38/food-3808953_1280.jpg', '泰达');
INSERT INTO `Canteen` VALUES (6, '学五食堂', '北门', '5:00-20:00', 'https://cdn.pixabay.com/photo/2023/07/31/03/07/chick-8160008_1280.jpg', '泰达');

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
-- Records of Comment
-- ----------------------------
INSERT INTO `Comment` VALUES (1, 1, '番茄炒蛋超级好吃！', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (2, 2, '牛肉拉面味道一般。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (3, 3, '麻辣烫太辣了。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (4, 4, '学一食堂的环境不错。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (5, 5, '面包不新鲜。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (6, 6, '蒸菜很健康，推荐！', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (7, 7, '价格有点贵。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (8, 8, '素什锦很清淡，适合夏天。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (9, 1, '水饺份量足，蘸料好吃。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (10, 2, '档口服务态度很好。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (11, 3, '希望多些活动优惠。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (12, 4, '巧克力面包不错，甜而不腻。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (13, 5, '能不能早点营业？', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (14, 6, '希望饭菜更丰富点。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (15, 7, '拉面汤底好喝！', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (17, 1, '蒸菜略咸，建议少盐。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (18, 2, '米饭档口排队太久了。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (19, 3, '水饺皮太厚。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (20, 4, '菜品种类太少了。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (21, 5, '辣味十足，爽！', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (22, 6, '汤太油了。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (23, 7, '排骨蒸得不错。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (24, 8, '菜品温度不够热。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (26, 2, '环境不如以前了。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (27, 3, '能不能支持刷校园卡？', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (28, 4, '服务员态度不太好。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (29, 5, '点赞拉面！', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (30, 6, '饭硬，口感差。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (31, 7, '小菜不错，便宜实惠。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (32, 8, '饮料选择太少。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (33, 1, '餐具有点脏。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (34, 2, '建议增加糖醋类菜。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (35, 3, '价格合理，份量大。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (36, 4, '等位区太挤了。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (37, 5, '吃完好撑。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (38, 6, '蛋炒饭有点糊。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (39, 7, '味精太多了。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (40, 8, '很方便，推荐给同学。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (41, 1, '档口装饰挺有特色。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (42, 2, '排骨味道刚刚好。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (43, 3, '应该有无糖饮料。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (44, 4, '麻辣烫配菜新鲜。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (45, 5, '不建议来晚饭高峰。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (46, 6, '素菜组合很合理。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (47, 7, '中午座位紧张。', '2025-05-11 12:14:50');
INSERT INTO `Comment` VALUES (48, 8, '档口换老板了，味道变了。', '2025-05-11 12:14:50');

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
-- Records of Comment_Target
-- ----------------------------
INSERT INTO `Comment_Target` VALUES (1, 1, 'dish', 1);
INSERT INTO `Comment_Target` VALUES (2, 2, 'dish', 2);
INSERT INTO `Comment_Target` VALUES (3, 3, 'dish', 3);
INSERT INTO `Comment_Target` VALUES (4, 4, 'canteen', 1);
INSERT INTO `Comment_Target` VALUES (5, 5, 'dish', 5);
INSERT INTO `Comment_Target` VALUES (6, 6, 'dish', 7);
INSERT INTO `Comment_Target` VALUES (7, 7, 'dish', 8);
INSERT INTO `Comment_Target` VALUES (8, 8, 'dish', 8);
INSERT INTO `Comment_Target` VALUES (9, 9, 'dish', 4);
INSERT INTO `Comment_Target` VALUES (10, 10, 'stall', 4);
INSERT INTO `Comment_Target` VALUES (11, 11, 'canteen', 3);
INSERT INTO `Comment_Target` VALUES (12, 12, 'dish', 5);
INSERT INTO `Comment_Target` VALUES (13, 13, 'canteen', 1);
INSERT INTO `Comment_Target` VALUES (14, 14, 'canteen', 2);
INSERT INTO `Comment_Target` VALUES (15, 15, 'dish', 2);
INSERT INTO `Comment_Target` VALUES (17, 17, 'dish', 7);
INSERT INTO `Comment_Target` VALUES (18, 18, 'stall', 1);
INSERT INTO `Comment_Target` VALUES (19, 19, 'dish', 4);
INSERT INTO `Comment_Target` VALUES (20, 20, 'canteen', 3);
INSERT INTO `Comment_Target` VALUES (21, 21, 'dish', 3);
INSERT INTO `Comment_Target` VALUES (22, 22, 'stall', 3);
INSERT INTO `Comment_Target` VALUES (23, 23, 'dish', 7);
INSERT INTO `Comment_Target` VALUES (24, 24, 'stall', 6);
INSERT INTO `Comment_Target` VALUES (26, 26, 'canteen', 4);
INSERT INTO `Comment_Target` VALUES (27, 27, 'canteen', 4);
INSERT INTO `Comment_Target` VALUES (28, 28, 'stall', 2);
INSERT INTO `Comment_Target` VALUES (29, 29, 'dish', 2);
INSERT INTO `Comment_Target` VALUES (30, 30, 'dish', 1);
INSERT INTO `Comment_Target` VALUES (31, 31, 'dish', 5);
INSERT INTO `Comment_Target` VALUES (32, 32, 'stall', 6);
INSERT INTO `Comment_Target` VALUES (33, 33, 'stall', 4);
INSERT INTO `Comment_Target` VALUES (34, 34, 'dish', 6);
INSERT INTO `Comment_Target` VALUES (35, 35, 'stall', 7);
INSERT INTO `Comment_Target` VALUES (36, 36, 'dish', 1);
INSERT INTO `Comment_Target` VALUES (37, 37, 'dish', 1);
INSERT INTO `Comment_Target` VALUES (38, 38, 'stall', 1);
INSERT INTO `Comment_Target` VALUES (39, 39, 'dish', 1);
INSERT INTO `Comment_Target` VALUES (40, 40, 'stall', 2);
INSERT INTO `Comment_Target` VALUES (41, 41, 'stall', 1);
INSERT INTO `Comment_Target` VALUES (42, 42, 'dish', 7);
INSERT INTO `Comment_Target` VALUES (43, 43, 'dish', 5);
INSERT INTO `Comment_Target` VALUES (44, 44, 'stall', 3);
INSERT INTO `Comment_Target` VALUES (45, 45, 'canteen', 2);
INSERT INTO `Comment_Target` VALUES (46, 46, 'dish', 8);
INSERT INTO `Comment_Target` VALUES (47, 47, 'canteen', 1);
INSERT INTO `Comment_Target` VALUES (48, 48, 'stall', 2);
INSERT INTO `Comment_Target` VALUES (49, 49, 'comment', 2);
INSERT INTO `Comment_Target` VALUES (50, 50, 'comment', 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of Dish
-- ----------------------------
INSERT INTO `Dish` VALUES (1, '番茄炒蛋盖饭', '经典家常菜', 1.00, NULL, 1);
INSERT INTO `Dish` VALUES (2, '牛肉拉面', '精选牛肉', 15.00, NULL, 2);
INSERT INTO `Dish` VALUES (3, '招牌麻辣烫', '自选酱料', 18.50, NULL, 3);
INSERT INTO `Dish` VALUES (4, '鲜肉水饺（更新）', '现包水饺', 12.00, NULL, 4);
INSERT INTO `Dish` VALUES (6, '鸭脖拼盘', '香辣诱人', 13.00, NULL, 6);
INSERT INTO `Dish` VALUES (7, '豆豉蒸排骨', '蒸汽健康', 14.00, NULL, 7);
INSERT INTO `Dish` VALUES (8, '素什锦', '多种蔬菜组合', 9.00, NULL, 8);
INSERT INTO `Dish` VALUES (9, '测试', '测试', 15.00, 'https://example.com/测试.jpg', 3);
INSERT INTO `Dish` VALUES (11, '面包', NULL, 10.00, NULL, 5);
INSERT INTO `Dish` VALUES (22, '测试', '', 2.00, '', 1);

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
-- Records of Like
-- ----------------------------
INSERT INTO `Like` VALUES (1, 1);
INSERT INTO `Like` VALUES (2, 2);
INSERT INTO `Like` VALUES (3, 3);
INSERT INTO `Like` VALUES (4, 4);
INSERT INTO `Like` VALUES (5, 5);
INSERT INTO `Like` VALUES (6, 6);
INSERT INTO `Like` VALUES (7, 7);
INSERT INTO `Like` VALUES (8, 8);

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
-- Records of Like_Target
-- ----------------------------
INSERT INTO `Like_Target` VALUES (1, 1, 'dish', 1);
INSERT INTO `Like_Target` VALUES (2, 2, 'dish', 2);
INSERT INTO `Like_Target` VALUES (3, 3, 'stall', 3);
INSERT INTO `Like_Target` VALUES (4, 4, 'comment', 1);
INSERT INTO `Like_Target` VALUES (5, 5, 'comment', 2);
INSERT INTO `Like_Target` VALUES (6, 6, 'canteen', 1);
INSERT INTO `Like_Target` VALUES (7, 7, 'dish', 6);
INSERT INTO `Like_Target` VALUES (8, 8, 'dish', 3);

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
-- Records of Rating
-- ----------------------------
INSERT INTO `Rating` VALUES (1, 1, 'dish', 1, 4.5);
INSERT INTO `Rating` VALUES (2, 1, 'stall', 2, 3.5);
INSERT INTO `Rating` VALUES (3, 2, 'dish', 2, 5.0);
INSERT INTO `Rating` VALUES (4, 2, 'stall', 3, 4.0);
INSERT INTO `Rating` VALUES (5, 3, 'dish', 3, 3.0);
INSERT INTO `Rating` VALUES (6, 3, 'stall', 1, 4.2);
INSERT INTO `Rating` VALUES (7, 4, 'dish', 4, 3.8);
INSERT INTO `Rating` VALUES (8, 4, 'stall', 4, 4.0);
INSERT INTO `Rating` VALUES (9, 5, 'dish', 5, 4.6);
INSERT INTO `Rating` VALUES (10, 6, 'stall', 5, 4.7);

-- ----------------------------
-- Table structure for Stall
-- ----------------------------
DROP TABLE IF EXISTS `Stall`;
CREATE TABLE `Stall`  (
  `stall_id` int NOT NULL AUTO_INCREMENT COMMENT '档口编号，主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '档口名称',
  `canteen_id` int NOT NULL COMMENT '所属食堂编号（逻辑外键）',
  `floor` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '楼层位置，如“1F”、“2F”等',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '档口样图',
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`stall_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of Stall
-- ----------------------------
INSERT INTO `Stall` VALUES (1, '米饭档（更新3）', 1, '2F', 'https://cdn.pixabay.com/photo/2025/02/25/14/40/horse-9430636_1280.jpg', '主营米饭');
INSERT INTO `Stall` VALUES (2, '拉面档', 2, '2F', NULL, '各类拉面');
INSERT INTO `Stall` VALUES (3, '麻辣烫档', 3, '1F', NULL, '自选锅底');
INSERT INTO `Stall` VALUES (4, '水饺档', 4, '1F', NULL, '手工水饺');
INSERT INTO `Stall` VALUES (6, '卤味档', 1, '2F', NULL, '冷卤熟食');
INSERT INTO `Stall` VALUES (7, '蒸菜馆', 2, '1F', NULL, '健康蒸菜');
INSERT INTO `Stall` VALUES (8, '素食堂', 3, '2F', NULL, '纯素料理');

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
-- Records of User
-- ----------------------------
INSERT INTO `User` VALUES (1, 'Alice', 2, 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/notion_1.png', 'user', '7abdccbea8473767e91378e37850d296', '2025-05-11 08:06:40', 'alice@test.com');
INSERT INTO `User` VALUES (2, 'Bob', 1, 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/notion_2.png', 'user', '2acba7f51acfd4fd5102ad090fc612ee', '2025-05-11 08:06:40', 'bob@test.com');
INSERT INTO `User` VALUES (3, 'Charlie', 1, 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/notion_3.png', 'user', 'f1940d88490bce94c4ecfcd743b5c3aa', '2025-05-11 08:06:40', 'charlie@test.com');
INSERT INTO `User` VALUES (4, 'Diana', 2, 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/notion_4.png', 'user', 'ca7669cfc26196d72f7d5297cf1bc606', '2025-05-11 08:06:40', 'diana@test.com');
INSERT INTO `User` VALUES (5, 'Evan', 1, 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/notion_5.png', 'user', 'a821cd35ae9e617f483590c4a471df71', '2025-05-11 08:06:40', 'evan@test.com');
INSERT INTO `User` VALUES (6, 'Fay', 2, 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/notion_6.png', 'user', '228d388761d48a657f612d5a18abd8da', '2025-05-11 08:06:40', 'fay@test.com');
INSERT INTO `User` VALUES (7, 'Grace', 2, 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/notion_7.png', 'user', '8ff861bcfd87bd85e9b207ea74cb6596', '2025-05-11 08:06:40', 'grace@test.com');
INSERT INTO `User` VALUES (8, 'Henry', 1, 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/notion_10.png', 'user', '9f876785ec5425a0511339bed7230c2a', '2025-05-11 08:06:40', 'henry@test.com');
INSERT INTO `User` VALUES (9, 'AdminTest', 1, 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/memo_24.png', 'admin', '0192023a7bbd73250516f069df18b500', '2025-05-11 08:06:40', 'admin@test.com');
INSERT INTO `User` VALUES (10, '测试用户1', 2, 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/upstream_22.png', 'user', 'e10adc3949ba59abbe56e057f20f883e', '2025-05-11 08:52:09', 'test3@example.com');
INSERT INTO `User` VALUES (14, '测试用户3', 1, 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/upstream_21.png', 'user', 'e10adc3949ba59abbe56e057f20f883e', '2025-05-11 09:36:13', 'test2@test.com');
INSERT INTO `User` VALUES (15, '桃气大钧', 0, 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/upstream_21.png', 'user', '1b9197078712eb35762dd97206a0d4bd', '2025-05-11 11:15:07', 'zjh050211@qq.com');
INSERT INTO `User` VALUES (16, '测试用户4', 2, 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/upstream_21.png', 'user', 'e10adc3949ba59abbe56e057f20f883e', '2025-05-11 11:38:48', 'test4@test.com');
INSERT INTO `User` VALUES (17, '测试用户5', 0, 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/upstream_21.png', 'user', 'e10adc3949ba59abbe56e057f20f883e', '2025-05-11 11:41:38', 'test5@test.com');
INSERT INTO `User` VALUES (18, '芝士马铃薯旋风卷', 2, 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/upstream_21.png', 'user', '727d2691d6e65086dea043efaf897e79', '2025-05-11 11:46:01', '1225536651@qq.com');
INSERT INTO `User` VALUES (19, '测试用户4', 2, 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/upstream_22.png', 'user', 'e10adc3949ba59abbe56e057f20f883e', '2025-05-11 11:50:46', 'test4@test4.com');
INSERT INTO `User` VALUES (20, 'Tina', 2, 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/upstream_22.png', 'user', 'afdd0b4ad2ec172c586e2150770fbf9e', '2025-05-11 14:27:39', 'cuplsandy@sina.com');

-- ----------------------------
-- View structure for Stall_Dish_View
-- ----------------------------
DROP VIEW IF EXISTS `Stall_Dish_View`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `Stall_Dish_View` AS select `d`.`dish_id` AS `dish_id`,`d`.`name` AS `name`,`d`.`intro` AS `intro`,`d`.`price` AS `price`,`d`.`image_url` AS `image_url`,`d`.`stall_id` AS `stall_id`,`s`.`name` AS `stall_name`,`s`.`canteen_id` AS `canteen_id`,`c`.`name` AS `canteen_name`,(select count(0) from (`Like` `l` join `Like_Target` `lt` on((`l`.`like_id` = `lt`.`like_id`))) where ((`lt`.`target_type` = 'dish') and (`lt`.`target_id` = `d`.`dish_id`))) AS `like_count`,(select count(0) from (`Comment` `cm` join `Comment_Target` `ct` on((`cm`.`comment_id` = `ct`.`comment_id`))) where ((`ct`.`target_type` = 'dish') and (`ct`.`target_id` = `d`.`dish_id`))) AS `comment_count` from ((`Dish` `d` join `Stall` `s` on((`d`.`stall_id` = `s`.`stall_id`))) join `Canteen` `c` on((`s`.`canteen_id` = `c`.`canteen_id`))) order by `d`.`stall_id`,`d`.`dish_id`;

-- ----------------------------
-- Procedure structure for update_dish
-- ----------------------------
DROP PROCEDURE IF EXISTS `update_dish`;
delimiter ;;
CREATE PROCEDURE `update_dish`(IN p_dish_id INT,
    IN p_name VARCHAR(100),
    IN p_intro TEXT,
    IN p_price DECIMAL(6, 2),
    IN p_image_url VARCHAR(255),
    IN p_stall_id INT,
    OUT p_result INT)
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
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for update_stall
-- ----------------------------
DROP PROCEDURE IF EXISTS `update_stall`;
delimiter ;;
CREATE PROCEDURE `update_stall`(IN p_stall_id INT,
    IN p_name VARCHAR(100),
    IN p_canteen_id INT,
    IN p_floor VARCHAR(20),
    IN p_img VARCHAR(255),
    IN p_intro VARCHAR(255),
    OUT p_result INT)
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
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table Dish
-- ----------------------------
DROP TRIGGER IF EXISTS `before_dish_insert`;
delimiter ;;
CREATE TRIGGER `before_dish_insert` BEFORE INSERT ON `Dish` FOR EACH ROW BEGIN
    IF NEW.price < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = '菜品价格不能为负数';
    END IF;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table Dish
-- ----------------------------
DROP TRIGGER IF EXISTS `before_dish_update`;
delimiter ;;
CREATE TRIGGER `before_dish_update` BEFORE UPDATE ON `Dish` FOR EACH ROW BEGIN
    IF NEW.price < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = '菜品价格不能为负数';
    END IF;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
