DROP PROCEDURE IF EXISTS init_project_account;
CREATE PROCEDURE init_project_account()
  BEGIN
    DECLARE done int DEFAULT 0 ;
    DECLARE temp_code VARCHAR(36);
    DECLARE temp_name VARCHAR(50);
    DECLARE temp_project_id VARCHAR(36);
    DECLARE temp_prestore_id_1 VARCHAR(36);
    DECLARE temp_prestore_id_2 VARCHAR(36);
    DECLARE temp_prestore_id_3 VARCHAR(36);
    DECLARE temp_prestore_id_4 VARCHAR(36);
    DECLARE temp_prestore_total DECIMAL(16,2);
    DECLARE project_count INT DEFAULT 0;
    DECLARE cur_project cursor for SELECT code,name FROM t_sys_project;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
    OPEN cur_project;
    projectLoop:LOOP
      FETCH cur_project INTO temp_code,temp_name;
      IF done=1 THEN
        LEAVE projectLoop;
      END IF;
      if(temp_code IS NOT NULL) THEN
        SELECT count(1) INTO project_count FROM t_project_account where project_id=temp_code;
        if(project_count=0) THEN
          SELECT uuid() INTO temp_project_id;
          SELECT uuid() INTO temp_prestore_id_1;
          SELECT uuid() INTO temp_prestore_id_2;
          SELECT uuid() INTO temp_prestore_id_3;
          SELECT uuid() INTO temp_prestore_id_4;
          -- 初始化项目预存账户
          INSERT INTO t_project_prestore_account(id, amount, prestore_type, project_account_id, create_time, update_time, signature)
          VALUES (temp_prestore_id_1,0,1,temp_project_id,now(),now(),null);-- 通用
          INSERT INTO t_project_prestore_account(id, amount, prestore_type, project_account_id, create_time, update_time, signature)
          VALUES (temp_prestore_id_2,0,2,temp_project_id,now(),now(),null);-- 专项
          INSERT INTO t_project_prestore_account(id, amount, prestore_type, project_account_id, create_time, update_time, signature)
          VALUES (temp_prestore_id_3,0,3,temp_project_id,now(),now(),null);-- 违约金
          INSERT INTO t_project_prestore_account(id, amount, prestore_type, project_account_id, create_time, update_time, signature)
          VALUES (temp_prestore_id_4,0,4,temp_project_id,now(),now(),null);-- 押金
          INSERT INTO t_project_account(id, project_id, company_id, project_name, company_name, create_time, update_time, version, signature, total_amount, cycle_amount, product_amount, late_amount, fine_amount, predeposit_amount, refund_amount)
          VALUES (temp_project_id,temp_code,'09841dc0-204a-41f2-a175-20a6dcee0187',temp_name,NULL ,now(),now(),0,NULL,0,0,0,0,0,0,0);
          -- 查询数据
          -- 通用预存
          INSERT INTO t_project_prestore_detail(id, prestore_account, amount, create_by, type, business_type, order_id, house_code_new, business_opera_detail_id)
            SELECT uuid(),temp_prestore_id_1,ifnull(a.after_amount,0),a.create_id,0,1,null,a.house_code_new,a.opera_id from t_ac_common_account_detail a
              LEFT JOIN tc_building b ON  a.house_code_new=b.house_code
            WHERE business_type=1 AND b.project_id=temp_code;
          -- 专项预存
          INSERT INTO t_project_prestore_detail(id, prestore_account, amount, create_by, type, business_type, order_id, house_code_new, business_opera_detail_id)
            SELECT uuid(),temp_prestore_id_2,ifnull(a.special_amount,0),a.create_id,a.account_type,1,null,a.house_code_new,null from t_ac_special_account a
            WHERE  a.project_id=temp_code;
          -- 押金暂不初始化
          -- 汇总通用预存
          UPDATE t_project_prestore_account
          SET  amount=ifnull((SELECT sum(a.amount) from t_project_prestore_detail a where a.type=0 AND a.business_type=1 AND a.prestore_account=temp_prestore_id_1),0)
          WHERE id=temp_prestore_id_1;
          -- 汇总专项预存
          UPDATE t_project_prestore_account
          SET amount= ifnull((SELECT sum(a.amount) from t_project_prestore_detail a  where a.type<>0 AND a.business_type=1 AND a.prestore_account=temp_prestore_id_2),0)
          WHERE id=temp_prestore_id_2;
          -- 汇总项目账户
          SELECT sum(amount) INTO temp_prestore_total FROM t_project_prestore_account WHERE project_account_id=temp_project_id;
          UPDATE t_project_account
          SET predeposit_amount=temp_prestore_total,
            total_amount=temp_prestore_total
          WHERE id=temp_project_id;
        END IF;
      END IF;
    END LOOP;
    close cur_project;
  END;