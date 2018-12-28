DROP FUNCTION IF EXISTS  `INTE_ARRAY`;
delimiter //
-- 集合交集检查函数
-- @param varchar(255) setA A 集合 如 "1,3,5,9"
-- @param varchar(255) setB B 集合 如 "8,2,3,7"
-- @return int(1) B 集合内单元在 A集合 内存在则返回 1 否则返回 0
CREATE FUNCTION `INTE_ARRAY` (setA varchar(255),setB varchar(255)) RETURNS int(1)
BEGIN
    DECLARE idx INT DEFAULT 0 ; -- B 集合单元索引
    DECLARE len INT DEFAULT 0;-- B 集合表达式长度
    DECLARE llen INT DEFAULT 0;-- 最后检查位置
    DECLARE clen INT DEFAULT 0;-- 当前检查位置
    DECLARE tmpStr varchar(255);-- 临时检查数据集
    DECLARE curt varchar(255);-- B 当前检查的单元
    SET len = LENGTH(setB);
    WHILE idx < len DO
        SET idx = idx + 1;
        SET tmpStr = SUBSTRING_INDEX(setB,",",idx);
        SET clen = LENGTH(tmpStr);
-- 获取当前 setB 中的单元
        IF idx = 1 THEN SET curt = tmpStr;
        ELSE SET curt = SUBSTRING(setB,llen+2,clen-llen-1);
        END IF;
-- 检查是否存在于 setA 中
        IF FIND_IN_SET(curt,setA) > 0 THEN RETURN 1;
        END IF;
-- 当前检查终点与上次检查终点相同则跳出
        IF clen <= llen THEN RETURN 0;
        END IF;

        SET llen = clen;
    END WHILE;
    RETURN 0;
END;
//
delimiter ;