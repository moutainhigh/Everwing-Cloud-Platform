
-- ----------------------------
-- Function structure for fun_find_group_childrens_by_id
-- ----------------------------
DROP FUNCTION IF EXISTS `fun_find_group_childrens_by_id`;
delimiter ;;
CREATE FUNCTION `fun_find_group_childrens_by_id`(vPid varchar(50))
 RETURNS varchar(4000)
  DETERMINISTIC
BEGIN
	DECLARE sTemp VARCHAR(4000);
	DECLARE sTempChd VARCHAR(4000);
	SET sTemp='';
	SET sTempChd = CAST(vPid AS CHAR);
	WHILE sTempChd IS NOT NULL DO
		SET sTemp= CONCAT(sTemp,',',sTempChd);
		SELECT GROUP_CONCAT(id) INTO sTempChd FROM t_jg_staff_grop WHERE FIND_IN_SET(pid,sTempChd)>0;
	END WHILE;
	RETURN sTemp;
    END
;;
delimiter ;

-- ----------------------------
-- Function structure for fun_find_organization_by_loginname
-- ----------------------------
DROP FUNCTION IF EXISTS `fun_find_organization_by_loginname`;
delimiter ;;
CREATE FUNCTION `fun_find_organization_by_loginname`(loginname VARCHAR(50))
 RETURNS varchar(4000)
  DETERMINISTIC
BEGIN
	DECLARE sTemp VARCHAR(4000);
	DECLARE sTempChd VARCHAR(4000);

	SET sTemp='$';
	SET sTempChd = CAST(loginname AS CHAR);

	SELECT GROUP_CONCAT(organization_id) INTO sTempChd FROM t_sys_organization WHERE CODE = (SELECT u.staff_number FROM t_sys_user u WHERE u.login_name = sTempChd);

	WHILE sTempChd IS NOT NULL DO
		SET sTemp= CONCAT(sTemp,',',sTempChd);
		SELECT GROUP_CONCAT(pid) INTO sTempChd FROM t_sys_organization WHERE FIND_IN_SET(organization_id,sTempChd)>0;
	END WHILE;
	RETURN sTemp;
END
;;
delimiter ;

-- ----------------------------
-- Function structure for INTE_ARRAY
-- ----------------------------
DROP FUNCTION IF EXISTS `INTE_ARRAY`;
delimiter ;;
CREATE FUNCTION `INTE_ARRAY`(setA varchar(255),setB varchar(255))
 RETURNS int(1)
  DETERMINISTIC
BEGIN
    DECLARE idx INT DEFAULT 0 ;
    DECLARE len INT DEFAULT 0;
    DECLARE llen INT DEFAULT 0;
    DECLARE clen INT DEFAULT 0;
    DECLARE tmpStr varchar(255);
    DECLARE curt varchar(255);
    SET len = LENGTH(setB);
    WHILE idx < len DO
        SET idx = idx + 1;
        SET tmpStr = SUBSTRING_INDEX(setB,",",idx);
        SET clen = LENGTH(tmpStr);

        IF idx = 1 THEN SET curt = tmpStr;
        ELSE SET curt = SUBSTRING(setB,llen+2,clen-llen-1);
        END IF;

        IF FIND_IN_SET(curt,setA) > 0 THEN RETURN 1;
        END IF;

        IF clen <= llen THEN RETURN 0;
        END IF;

        SET llen = clen;
    END WHILE;
    RETURN 0;
END
;;
delimiter ;

-- ----------------------------
-- Function structure for SPLIT_STR
-- ----------------------------
DROP FUNCTION IF EXISTS `SPLIT_STR`;
delimiter ;;
CREATE FUNCTION `SPLIT_STR`(x VARCHAR(255),
  delim VARCHAR(12),
  pos INT)
 RETURNS varchar(255)
  DETERMINISTIC
RETURN REPLACE(SUBSTRING(SUBSTRING_INDEX(x, delim, pos),
       LENGTH(SUBSTRING_INDEX(x, delim, pos -1)) + 1),
       delim, '')
;;
delimiter ;

