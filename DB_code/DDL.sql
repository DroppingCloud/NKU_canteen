-- 用户表：存储所有用户信息（包括普通用户与管理员）
CREATE TABLE User (
    user_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户编号，主键',
    nickname VARCHAR(50) NOT NULL COMMENT '用户昵称',
    gender TINYINT COMMENT '性别：1-男，2-女',
    avatar VARCHAR(255) COMMENT '用户头像 URL',
    user_type ENUM('user', 'admin') DEFAULT 'user' COMMENT '用户类型：user-普通用户，admin-管理员',
    password VARCHAR(255) NOT NULL COMMENT '用户密码，建议加密存储',
    register_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    email VARCHAR(100) UNIQUE COMMENT '电子邮箱，唯一'
);

-- 食堂表：记录各食堂基本信息
CREATE TABLE Canteen (
    canteen_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '食堂编号，主键',
    name VARCHAR(100) NOT NULL COMMENT '食堂名称',
    location VARCHAR(255) COMMENT '食堂地理位置描述',
    open_time VARCHAR(100) COMMENT '营业时间'
);

-- 档口表：表示每个食堂中若干个档口
CREATE TABLE Stall (
    stall_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '档口编号，主键',
    name VARCHAR(100) NOT NULL COMMENT '档口名称',
    canteen_id INT NOT NULL COMMENT '所属食堂编号（逻辑外键）',
    floor VARCHAR(20) COMMENT '楼层位置，如“1F”、“2F”等'
);

-- 菜品表：描述各档口提供的菜品
CREATE TABLE Dish (
    dish_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '菜品编号，主键',
    name VARCHAR(100) NOT NULL COMMENT '菜品名称',
    intro TEXT COMMENT '菜品简介',
    price DECIMAL(6,2) NOT NULL CHECK (price >= 0) COMMENT '菜品价格，单位元，保留两位小数',
    image_url VARCHAR(255) COMMENT '菜品图片地址',
    stall_id INT NOT NULL COMMENT '所属档口编号（逻辑外键）'
);

-- 评论表：记录所有用户的评论信息
CREATE TABLE Comment (
    comment_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '评论编号，主键',
    user_id INT NOT NULL COMMENT '评论者编号（逻辑外键）',
    content TEXT NOT NULL COMMENT '评论内容',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '评论创建时间'
);

-- 点赞表：记录用户发起的点赞操作（不区分类型）
CREATE TABLE `Like` (
    like_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '点赞编号，主键',
    user_id INT NOT NULL COMMENT '点赞者编号（逻辑外键）'
);

-- 评论目标表：表示一条评论的具体目标（食堂 / 档口 / 菜品 / 评论）
CREATE TABLE Comment_Target (
    comment_target_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '评论目标记录编号，主键',
    comment_id INT NOT NULL COMMENT '评论编号（逻辑外键）',
    target_type ENUM('canteen', 'stall', 'dish', 'comment') NOT NULL COMMENT '目标类型',
    target_id INT NOT NULL COMMENT '目标编号，对应目标实体主键'
);

-- 点赞目标表：表示一个点赞行为针对的对象
CREATE TABLE Like_Target (
    like_target_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '点赞目标记录编号，主键',
    like_id INT NOT NULL COMMENT '点赞编号（逻辑外键）',
    target_type ENUM('canteen', 'stall', 'dish', 'comment') NOT NULL COMMENT '目标类型',
    target_id INT NOT NULL COMMENT '目标编号，对应目标实体主键'
);