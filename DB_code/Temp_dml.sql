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