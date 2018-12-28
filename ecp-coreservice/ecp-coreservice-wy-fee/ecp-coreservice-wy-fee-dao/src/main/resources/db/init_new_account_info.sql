-- 处理504违约金不正确的情况(这里只是找出数据，供我们发现数据的问题)
DROP PROCEDURE IF EXISTS init_new_account_info;

DELIMITER // 
CREATE PROCEDURE init_new_account_info(IN projectId VARCHAR(20),IN projectName VARCHAR(20))
	BEGIN
		-- 定义所需要的变量	
  DECLARE commonAmountNow DECIMAL(16,2); -- 当前账户的通用账户总金额
	DECLARE specialTotalAmount DECIMAL(16,2); -- 当前系统单个资产专项抵扣账户的总和
 	DECLARE latefeeNow DECIMAL(16,2); -- 当前系统此资产欠费的违约金
	DECLARE currentChargeTotal DECIMAL(16,2);-- 此资产的当月计费总金额
	DECLARE lastTotalArrears DECIMAL(16,2); -- 此资产的上期欠费总金额
	DECLARE currentTotalBill DECIMAL(16,2); -- 当月账单总金额
	DECLARE accountId VARCHAR(128);    -- 账户id用于初始化

	DECLARE wyArreasAmount DECIMAL(16,2);  -- 当前物业欠费总金额
	DECLARE btArreasAmount DECIMAL(16,2);  -- 当前本体欠费总金额
	DECLARE waterArreasAmount DECIMAL(16,2);  -- 当前水费欠费总金额
	DECLARE electArreasAmount DECIMAL(16,2);  -- 当前电费欠费总金额

	DECLARE wyBillingFee DECIMAL(16,2);  -- 本月物业计费信息
	DECLARE btBillingFee DECIMAL(16,2);  -- 本月本体计费信息
	DECLARE waterBillingFee DECIMAL(16,2);  -- 本月水费计费信息
	DECLARE electBillingFee DECIMAL(16,2);  -- 本月电费计费信息
	
	DECLARE wySpecialAmount DECIMAL(16,2);  -- 现在物业专项账户余额
	DECLARE btSpecialAmount DECIMAL(16,2);  -- 现在本体专项账户余额
	DECLARE waterSpecialAmount DECIMAL(16,2);  -- 现在水费专项账户余额
	DECLARE electSpecialAmount DECIMAL(16,2);  -- 现在电费专项账户余额

	DECLARE speWyAccountId VARCHAR(128);   -- 物业专项账户的id，用于插入流水使用
	DECLARE speBtAccountId VARCHAR(128);   -- 本体专项账户的id，用于插入流水使用
	DECLARE speWaterAccountId VARCHAR(128);    -- 水费专项账户的id，用于插入流水使用
	DECLARE speelectAccountId VARCHAR(128);    -- 电费专项账户的id，用于插入流水使用

	DECLARE chargeWyAccountId VARCHAR(128);   -- 物业当月收费总额id，用于插入收费结果明细
	DECLARE chargeBtAccountId VARCHAR(128);   -- 本体当月收费总额id，用于插入收费结果明细
	DECLARE chargeWaterAccountId VARCHAR(128);    -- 水费当月收费总额id，用于插入收费结果明细
	DECLARE chargeelectAccountId VARCHAR(128);    -- 电费当月收费总额id，用于插入收费结果明细

	DECLARE wyLatefee DECIMAL(16,2);  -- 物业违约金金额
	DECLARE btLatefee DECIMAL(16,2);  -- 本体违约金金额
	DECLARE waterLatefee DECIMAL(16,2);  -- 水费违约金金额
	DECLARE electLatefee DECIMAL(16,2);  -- 电费违约金金额

	DECLARE wypayedamount DECIMAL(16,2) DEFAULT 0.00;  -- 物业已付
	DECLARE btpayedamount DECIMAL(16,2) DEFAULT 0.00;  -- 本体已付
	DECLARE waterpayedamount DECIMAL(16,2) DEFAULT 0.00;  -- 水费已付
	DECLARE electpayedamount DECIMAL(16,2) DEFAULT 0.00;  -- 电费已付

	DECLARE wyLatefeeBj DECIMAL(16,2);  -- 物业违约金本金金额
	DECLARE btLatefeeBj DECIMAL(16,2);  -- 本体违约金本金金额
	DECLARE waterLatefeeBj DECIMAL(16,2);  -- 水费违约金本金金额
	DECLARE electLatefeeBj DECIMAL(16,2);  -- 电费违约金本金金额

	DECLARE wylatefeeACID VARCHAR(128);   -- 物业违约金账户id
	DECLARE btlatefeeACID VARCHAR(128);   -- 本体违约金账户id
	DECLARE waterlatefeeACID VARCHAR(128);    -- 水费违约金账户id
	DECLARE electlatefeeACID VARCHAR(128);    -- 电费违约金账户id

	DECLARE chargeTime DATETIME;  -- 计费时间往前调一个月，初始化数据相当于上个月的计费

	DECLARE iloop INT DEFAULT 1;   -- 循环插入违约金账户流水使用

	DECLARE isFuli INT;
	DECLARE overDays INT;
	DECLARE proRate INT;

	DECLARE buildCode VARCHAR(128); -- 房屋编码
	DECLARE houseCode VARCHAR(128);	-- 流水违约金

	

	DECLARE done INT DEFAULT 0 ; -- 游标执行标识
	
	DECLARE init_account_list CURSOR FOR
					-- 所有存在账户的建筑暂时全部初始化（后期building_code切换了house_code）
					SELECT t2.building_code,t1.house_code FROM tc_building t1,t_bs_asset_account t2 WHERE t1.building_code = t2.building_code AND t2.project_id = 1019 
					AND t1.house_code IS NOT NULL
				-- 	AND t1.building_code = '09841dc0-204a-41f2-a175-20a6dcee0187.2e0f42d7-d6ae-4f47-85bc-b0e77c9bfb4f.ceng.1532571961649_10085'
					GROUP BY t2.building_code;
	DECLARE EXIT HANDLER FOR NOT FOUND SET done = 1 ; 

	OPEN init_account_list;

		read_loop: LOOP

			IF done = 1 
			THEN
				SELECT '退出游标' back;
				LEAVE read_loop;  -- 循环结算
			END IF;

			IF done = 0 
			THEN
				FETCH init_account_list INTO buildCode,houseCode;
				-- 得到房屋编号
				-- 1.查询出通用账户总金额
				SET commonAmountNow = (
					SELECT IFNULL( SUM(account_balance) ,0.0) FROM t_bs_asset_account WHERE TYPE = 0 AND building_code = buildCode
	);
				-- 2 查询此资产当前的欠违约金金额
				SET latefeeNow = (
						SELECT IFNULL(SUM(total_late_fee),0) FROM t_bs_owed_history WHERE account_id IN (
							SELECT id FROM t_bs_asset_account WHERE building_code  = buildCode
		)
	);
			
				-- 3 查询此资产当前所有可用专项账户的总和
				SET specialTotalAmount = (
		SELECT IFNULL(SUM(account_balance),0) FROM t_bs_asset_account WHERE building_code = buildCode AND TYPE != 0 AND account_balance >  0
	);
			
				-- 4.查询出此资产的当月计费总金额
				SET currentChargeTotal = (
					SELECT IFNULL(SUM(current_fee),0.0)  FROM t_bs_charge_bill_history WHERE building_code = buildCode AND charge_total_id IN (
						SELECT id FROM t_bs_charge_bill_total WHERE project_id = projectId AND DATE_FORMAT(billing_time,'%Y-%m') = DATE_FORMAT(NOW(),'%Y-%m')
					)
	);

			-- 5 查询此资产的额上月欠费总金额(直接从账户取)
			SET lastTotalArrears = (
				SELECT IFNULL(SUM(account_balance),0)  FROM t_bs_asset_account  WHERE building_code = buildCode AND TYPE != 0 AND account_balance <=  0
);
			
			-- 6 查询当月账单总金额(这里当月账单总金额暂时和账户的欠费相等)
			SET currentTotalBill = lastTotalArrears;
			SET accountId =  REPLACE(UPPER(UUID()),'-','');
			-- 插入资产账户信息
			INSERT INTO t_ac_account VALUES (accountId,
			houseCode,ABS(commonAmountNow) ,ABS(specialTotalAmount) ,ABS(latefeeNow),ABS(lastTotalArrears) ,ABS(lastTotalArrears) ,0.00 ,
			projectId,projectName,SYSDATE(),'system',SYSDATE(),'system','','' );

			-- 插入一条通用账户初始化流水值
			INSERT INTO t_ac_common_account_detail VALUES (
			REPLACE(UPPER(UUID()),'-',''),accountId,houseCode,0.0,commonAmountNow,commonAmountNow,1,0,'',
			'初始化通用账户流水',projectId,projectName,SYSDATE(),'system',''
			);
				
			-- 上月欠费总金额（四个账户）
			SET wyArreasAmount = (
				SELECT (CASE WHEN IFNULL(SUM(account_balance),0)  >= 0 THEN 0 ELSE IFNULL(SUM(account_balance),0)  END) FROM t_bs_asset_account WHERE building_code = buildCode AND TYPE =1
			);
				
			SET btArreasAmount = (
				SELECT (CASE WHEN IFNULL(SUM(account_balance),0)  >= 0 THEN 0 ELSE IFNULL(SUM(account_balance),0)  END) FROM t_bs_asset_account WHERE building_code = buildCode AND TYPE =2
			);


			SET waterArreasAmount = (
				SELECT (CASE WHEN IFNULL(SUM(account_balance),0)  >= 0 THEN 0 ELSE IFNULL(SUM(account_balance),0)  END) FROM t_bs_asset_account WHERE building_code = buildCode AND TYPE =3
			);

			SET electArreasAmount = (
				SELECT (CASE WHEN IFNULL(SUM(account_balance),0)  >= 0 THEN 0 ELSE IFNULL(SUM(account_balance),0)  END) FROM t_bs_asset_account WHERE building_code = buildCode AND TYPE =4
			);


			INSERT INTO t_ac_last_bill_fee_info VALUES (
			REPLACE(UPPER(UUID()),'-',''),houseCode,ABS(wyArreasAmount),accountId,projectId,projectName,'system',SYSDATE(),'',SYSDATE(),1,''
			)	;

			INSERT INTO t_ac_last_bill_fee_info VALUES (
			REPLACE(UPPER(UUID()),'-',''),houseCode,ABS(btArreasAmount),accountId,projectId,projectName,'system',SYSDATE(),'',SYSDATE(),2,''
			)	;

			INSERT INTO t_ac_last_bill_fee_info VALUES (
			REPLACE(UPPER(UUID()),'-',''),houseCode,ABS(waterArreasAmount),accountId,projectId,projectName,'system',SYSDATE(),'',SYSDATE(),3,''
			)	;

			INSERT INTO t_ac_last_bill_fee_info VALUES (
			REPLACE(UPPER(UUID()),'-',''),houseCode,ABS(electArreasAmount),accountId,projectId,projectName,'system',SYSDATE(),'',SYSDATE(),4,''
			)	;


			-- 初始化当月收费总金额信息
			SET wyBillingFee = (
					SELECT IFNULL(SUM(current_fee),0)  FROM t_bs_charge_bill_history WHERE building_code = buildCode AND charge_total_id IN 
				(
				SELECT id FROM t_bs_charge_bill_total WHERE TYPE =1 AND project_id = projectId AND DATE_FORMAT(billing_time,'%Y-%m') = DATE_FORMAT(NOW(),'%Y-%m')
				)
);
	
			

						SET btBillingFee = (
			SELECT IFNULL(SUM(current_fee),0)  FROM t_bs_charge_bill_history WHERE building_code = buildCode AND charge_total_id IN 
				(
				SELECT id FROM t_bs_charge_bill_total WHERE TYPE =2 AND project_id = projectId AND DATE_FORMAT(billing_time,'%Y-%m') = DATE_FORMAT(NOW(),'%Y-%m')
				)
);


						SET waterBillingFee = (
			SELECT IFNULL(SUM(current_fee),0)  FROM t_bs_charge_bill_history WHERE building_code = buildCode AND charge_total_id IN 
				(
				SELECT id FROM t_bs_charge_bill_total WHERE TYPE =3 AND project_id = projectId AND DATE_FORMAT(billing_time,'%Y-%m') = DATE_FORMAT(NOW(),'%Y-%m')
				)
);


						SET electBillingFee = (
			SELECT IFNULL(SUM(current_fee),0)  FROM t_bs_charge_bill_history WHERE building_code = buildCode AND charge_total_id IN 
				(
				SELECT id FROM t_bs_charge_bill_total WHERE TYPE =4 AND project_id = projectId AND DATE_FORMAT(billing_time,'%Y-%m') = DATE_FORMAT(NOW(),'%Y-%m')
				)
);



			
			SET chargeWyAccountId = REPLACE(UPPER(UUID()),'-','');
			INSERT INTO t_ac_current_charge VALUES (
				chargeWyAccountId,houseCode,ABS(wyArreasAmount),accountId,1,DATE_FORMAT(NOW(),'%Y-%m'),NULL,projectId,projectName,'system',SYSDATE(),''
			);
			
			SET chargeBtAccountId = REPLACE(UPPER(UUID()),'-','');
			INSERT INTO t_ac_current_charge VALUES (
					chargeBtAccountId,houseCode,ABS(btArreasAmount),accountId,2,DATE_FORMAT(NOW(),'%Y-%m'),NULL,projectId,projectName,'system',SYSDATE(),''
				);

			SET chargeWaterAccountId = REPLACE(UPPER(UUID()),'-','');
			INSERT INTO t_ac_current_charge VALUES (
				chargeWaterAccountId,houseCode,ABS(waterArreasAmount),accountId,3,DATE_FORMAT(NOW(),'%Y-%m'),NULL,projectId,projectName,'system',SYSDATE(),''
			);

			SET chargeelectAccountId = REPLACE(UPPER(UUID()),'-','');
			INSERT INTO t_ac_current_charge VALUES (
				chargeelectAccountId,houseCode,ABS(electArreasAmount),accountId,4,DATE_FORMAT(NOW(),'%Y-%m'),NULL,projectId,projectName,'system',SYSDATE(),''
			);


		-- 插入当月计费总金额后，根据计费信息插入当月收费结果明细表

	INSERT INTO t_ac_current_charge_detail VALUES (
	REPLACE(UPPER(UUID()),'-',''),houseCode,ABS(wyArreasAmount),   chargeWyAccountId    ,1,NOW(),NOW(),'','',0,0,projectId,projectName,'system',SYSDATE(),
		0,0,0,NULL,ABS(wyArreasAmount),ABS(wyArreasAmount),1,NULL
);

	INSERT INTO t_ac_current_charge_detail VALUES (
	REPLACE(UPPER(UUID()),'-',''),houseCode,ABS(btArreasAmount),   chargeBtAccountId    ,2,NOW(),NOW(),'','',0,0,projectId,projectName,'system',SYSDATE(),
		0,0,0,NULL,ABS(btArreasAmount),ABS(btArreasAmount),1,NULL
);

	INSERT INTO t_ac_current_charge_detail VALUES (
	REPLACE(UPPER(UUID()),'-',''),houseCode,ABS(waterArreasAmount),   chargeWaterAccountId    ,3,NOW(),NOW(),'','',0,0,projectId,projectName,'system',SYSDATE(),
		0,0,0,NULL,ABS(waterArreasAmount),ABS(waterArreasAmount),1,NULL
);

	INSERT INTO t_ac_current_charge_detail VALUES (
	REPLACE(UPPER(UUID()),'-',''),houseCode,ABS(electArreasAmount),   chargeelectAccountId   ,4,NOW(),NOW(),'','',0,0,projectId,projectName,'system',SYSDATE(),
		0,0,0,NULL,ABS(electArreasAmount),ABS(electArreasAmount),1,NULL
);


		-- 专项抵扣账户以及对应的初始化流水
		SET wySpecialAmount = (
			SELECT IFNULL(SUM(account_balance),0)  FROM t_bs_asset_account WHERE TYPE = 1 AND building_code = buildCode AND account_balance > 0
);

		SET btSpecialAmount = (
			SELECT IFNULL(SUM(account_balance),0)  FROM t_bs_asset_account WHERE TYPE = 2 AND building_code = buildCode AND account_balance > 0
);

		SET waterSpecialAmount = (
			SELECT IFNULL(SUM(account_balance),0)  FROM t_bs_asset_account WHERE TYPE = 3 AND building_code = buildCode AND account_balance > 0
);

		SET electSpecialAmount = (
			SELECT IFNULL(SUM(account_balance),0)  FROM t_bs_asset_account WHERE TYPE = 4 AND building_code = buildCode AND account_balance > 0
);


	 SET speWyAccountId = REPLACE(UPPER(UUID()),'-','');
		INSERT INTO t_ac_special_account VALUES (
		speWyAccountId,houseCode,wySpecialAmount,1,accountId,projectId,projectName,'system',SYSDATE(),'',NULL,NULL
);



	SET speBtAccountId = REPLACE(UPPER(UUID()),'-','');
				INSERT INTO t_ac_special_account VALUES (
		speBtAccountId,houseCode,btSpecialAmount,2,accountId,projectId,projectName,'system',SYSDATE(),'',NULL,NULL
);

	SET speWaterAccountId = REPLACE(UPPER(UUID()),'-','');
			INSERT INTO t_ac_special_account VALUES (
		speWaterAccountId,houseCode,waterSpecialAmount,3,accountId,projectId,projectName,'system',SYSDATE(),'',NULL,NULL
);

	SET speelectAccountId = REPLACE(UPPER(UUID()),'-','');
			INSERT INTO t_ac_special_account VALUES (
		speelectAccountId,houseCode,electSpecialAmount,4,accountId,projectId,projectName,'system',SYSDATE(),'',NULL,NULL
);


INSERT INTO t_ac_special_detail VALUES (
REPLACE(UPPER(UUID()),'-',''),speWyAccountId,houseCode,0.0,wySpecialAmount,wySpecialAmount,1,projectId,projectName,SYSDATE(),'system','新版账户初始化专项抵扣账户',NULL,NULL
);

INSERT INTO t_ac_special_detail VALUES (
REPLACE(UPPER(UUID()),'-',''),speBtAccountId,houseCode,0.0,btSpecialAmount,btSpecialAmount,1,projectId,projectName,SYSDATE(),'system','新版账户初始化专项抵扣账户',NULL,NULL
);


INSERT INTO t_ac_special_detail VALUES (
REPLACE(UPPER(UUID()),'-',''),speWaterAccountId,houseCode,0.0,waterSpecialAmount,waterSpecialAmount,1,projectId,projectName,SYSDATE(),'system','新版账户初始化专项抵扣账户',NULL,NULL
);

INSERT INTO t_ac_special_detail VALUES (
REPLACE(UPPER(UUID()),'-',''),speelectAccountId,houseCode,0.0,electSpecialAmount,electSpecialAmount,1,projectId,projectName,SYSDATE(),'system','新版账户初始化专项抵扣账户',NULL,NULL
);


	
			-- 初始化违约金账户以及对应的流水信息

				latefee_loop : LOOP
					    -- 这里同时初始化一下违约金的计费规则信息
					IF iloop <5 
						THEN
							
									SET wyLatefee = (
												SELECT IFNULL(SUM(total_late_fee),0)  FROM t_bs_owed_history WHERE account_id = (
													SELECT id FROM t_bs_asset_account WHERE building_code = buildCode AND TYPE = iloop 
												) AND is_used = 0 
									);

									SET wyLatefeeBj = (
												SELECT IFNULL(SUM(owed_amount),0)  FROM t_bs_owed_history WHERE account_id = (
													SELECT id FROM t_bs_asset_account WHERE building_code = buildCode AND TYPE = iloop
												) AND is_used = 0 
									);
				
									SET wylatefeeACID = REPLACE(UPPER(UUID()),'-','');
											INSERT INTO t_ac_delay_account VALUES (
										wylatefeeACID,wyLatefee,accountId,projectId,projectName,'system',SYSDATE(),NULL,NULL,iloop,houseCode
									);

							
							SET isFuli = (
								SELECT calculation_type FROM t_bs_charging_scheme WHERE project_id = projectId AND scheme_type = iloop AND is_used = 0
							);
									
									SET overDays = (
										SELECT overdue_start_dates FROM t_bs_charging_scheme WHERE project_id = projectId AND scheme_type = iloop AND is_used = 0
							);

									SET proRate = (
										SELECT proportion FROM t_bs_charging_scheme WHERE project_id = projectId AND scheme_type = iloop AND is_used = 0
							);
							SELECT isFuli,overDays,proRate;

							IF (isFuli IS NOT NULL) AND (overDays IS NOT NULL) AND (proRate IS NOT NULL) AND (wyLatefee > 0)
							THEN
								-- 只有存在违约金计费规则的项目才进行违约金的计算的
								INSERT INTO t_ac_late_fee_stream VALUES (
									REPLACE(UPPER(UUID()),'-',''),wylatefeeACID,houseCode,0,(wyLatefee),(wyLatefee),4,projectId,
									projectName,SYSDATE(),'system','违约金账户初始化对应的流水',NULL,wyLatefeeBj,proRate,isFuli,overDays
								);
									
-- 										-- 因为之前的账户余额是包含了违约金的，所以前面取账户余额的是有问题的
							UPDATE t_ac_current_charge_detail SET currenct_arreas = currenct_arreas - wyLatefee,charge_amount = charge_amount - wyLatefee,
											payable_amount = payable_amount - wyLatefee WHERE house_code_new = houseCode AND account_type = iloop;
							
							UPDATE t_ac_current_charge SET current_bill_fee = current_bill_fee - wyLatefee WHERE account_type = iloop AND house_code_new = houseCode ;

							UPDATE t_ac_last_bill_fee_info SET last_bill_fee = last_bill_fee - wyLatefee WHERE  account_type = iloop AND house_code_new = houseCode ;

							UPDATE t_ac_account SET last_arrears_amount = last_arrears_amount - wyLatefee ,current_charging_amount = current_charging_amount - wyLatefee 
											WHERE house_code_new = houseCode;

							END IF;
					END IF;
								SET iloop = iloop+1;
								IF iloop >= 5 
									THEN
										LEAVE latefee_loop;
								END IF;
				END LOOP latefee_loop; 
			SET iloop = 1;

			END IF;
			
			END LOOP read_loop;
	-- 释放游标
	CLOSE init_account_list;

	END
//
DELIMITER;




 -- CALL init_new_account_info('1013','桃源峰景园');
 --  CALL init_new_account_info('1019','桃源居管理处');
