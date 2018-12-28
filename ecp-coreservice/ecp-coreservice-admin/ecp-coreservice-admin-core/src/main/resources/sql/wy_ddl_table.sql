SET collation_connection = utf8mb4_general_ci;
SET default_collation_for_utf8mb4 = utf8mb4_general_ci;


DROP TABLE IF EXISTS `t_synchrodata`;
CREATE TABLE `t_synchrodata`  (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `code` varchar(20) NOT NULL COMMENT '编码',
  `table_name` varchar(50) NOT NULL COMMENT '表名字，用于查询',
  `table_field_name` varchar(50) NOT NULL COMMENT '表字段，一定要用主键，用于查询',
  `table_field_value` varchar(100) NOT NULL COMMENT '表字段值',
  `destination_queue` varchar(255) NULL DEFAULT NULL COMMENT '目的队列',
  `operation` varchar(25) NULL DEFAULT NULL COMMENT '操作类型：create,modify,delete',
  `priority_level` tinyint(1) NOT NULL COMMENT '同步优先级',
  `state` varchar(10) NOT NULL COMMENT '同步状态：draft,done,error',
  `description` varchar(225) NULL DEFAULT NULL COMMENT '描述',
  `error_message` varchar(550) NULL DEFAULT NULL COMMENT '错误消息',
  `synchro_time` datetime(0) NULL DEFAULT NULL COMMENT '同步时间',
  `creater_id` varchar(36) NULL DEFAULT NULL COMMENT '创建用户id',
  `creater_name` varchar(200) NULL DEFAULT NULL COMMENT '创建用户名称',
  `modify_id` varchar(36) NULL DEFAULT NULL COMMENT '最后更新用户id',
  `modify_name` varchar(200) NULL DEFAULT NULL COMMENT '最后更新用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '数据同步记录表';



-- ----------------------------
-- Table structure for t_quill_editor
-- ----------------------------
DROP TABLE IF EXISTS `t_quill_editor`;
CREATE TABLE `t_quill_editor`  (
  `id` varchar(36) NOT NULL,
  `table_id` varchar(100) NULL DEFAULT NULL COMMENT '表名字',
  `relation_id` varchar(100)  NULL DEFAULT NULL COMMENT '表的关联字段',
  `html_content` longtext NULL COMMENT 'html内容',
  `creater_id` varchar(36)  NULL DEFAULT NULL,
  `creater_name` varchar(108) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '富文本表，用于存储富文本数据';




-- ----------------------------
-- Table structure for t_attachment
-- ----------------------------
DROP TABLE IF EXISTS `t_attachment`;
CREATE TABLE `t_attachment`  (
  `id` varchar(36) NOT NULL DEFAULT '',
  `table_id` varchar(100) NULL DEFAULT NULL COMMENT '关联表',
  `relation_id` varchar(100)  NULL DEFAULT NULL COMMENT '关联表的字段',
  `attachment_type` varchar(200)  NULL DEFAULT NULL COMMENT '附件类型',
  `attachment_key` varchar(500)  NULL DEFAULT NULL COMMENT '附件key',
  `attachment_name` varchar(200)  NULL DEFAULT NULL COMMENT '附件名称',
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建用户id',
  `creater_name` varchar(200)  NULL DEFAULT NULL COMMENT '创建用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_bank_info
-- ----------------------------
DROP TABLE IF EXISTS `t_bank_info`;
CREATE TABLE `t_bank_info`  (
  `id` int(2) NOT NULL COMMENT '主键id',
  `is_union` tinyint(1) NULL DEFAULT 0 COMMENT '是否为银联银行: 0: 是 , 1: 不是',
  `is_local` tinyint(1) NULL DEFAULT 0 COMMENT '是否为本地行: 0: 本地: ,1: 外地',
  `bank_type` varchar(10)  NULL DEFAULT NULL COMMENT '行别',
  `bank_no` varchar(15)  NULL DEFAULT NULL COMMENT '行号',
  `bank_name` varchar(100)  NULL DEFAULT NULL COMMENT '银行名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_bank_return_info
-- ----------------------------
DROP TABLE IF EXISTS `t_bank_return_info`;
CREATE TABLE `t_bank_return_info`  (
  `bank_code` varchar(100)  NOT NULL,
  `return_info` varchar(100)  NULL DEFAULT NULL,
  PRIMARY KEY (`bank_code`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_bc_collection
-- ----------------------------
DROP TABLE IF EXISTS `t_bc_collection`;
CREATE TABLE `t_bc_collection`  (
  `id` varchar(36)  NOT NULL,
  `cust_id` varchar(36)  NULL DEFAULT NULL,
  `cust_name` varchar(20)  NULL DEFAULT NULL COMMENT '冗余客户姓名',
  `create_bank` int(11) NULL DEFAULT NULL COMMENT '开户行',
  `card_num` varchar(30)  NULL DEFAULT NULL COMMENT '银行卡号',
  `province` varchar(30)  NULL DEFAULT NULL COMMENT '开户省',
  `city` varchar(30)  NULL DEFAULT NULL COMMENT '开户市',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '启用时间',
  `contract_no` varchar(50)  NULL DEFAULT NULL COMMENT '合同号',
  `relate_building_code` varchar(200)  NULL DEFAULT NULL COMMENT '关联资产',
  `charging_items` varchar(30)  NULL DEFAULT NULL COMMENT '收费项',
  `attachment` varchar(200)  NULL DEFAULT NULL COMMENT '附件fileId',
  `relate_building_full_name` varchar(100)  NULL DEFAULT NULL COMMENT '关联资产名称',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态1生效0失效',
  `project_id` varchar(36)  NULL DEFAULT NULL,
  `card_type` int(2) NULL DEFAULT NULL COMMENT '证件类型',
  `card_no` varchar(25)  NULL DEFAULT NULL COMMENT '证件号码',
  `create_by` varchar(36)  NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '银行托收';

-- ----------------------------
-- Table structure for t_bc_collection_total
-- ----------------------------
DROP TABLE IF EXISTS `t_bc_collection_total`;
CREATE TABLE `t_bc_collection_total`  (
  `id` varchar(36)  NOT NULL,
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `collection_type` tinyint(1) NULL DEFAULT 0 COMMENT '托收类型, 0: 银联 , 1: 金融联',
  `family_count` int(10) NULL DEFAULT NULL COMMENT '托收户数',
  `total_amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '总额',
  `complete_count` int(10) NULL DEFAULT NULL COMMENT '成功托收户数',
  `complete_amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '成功托收金额',
  `collection_status` tinyint(1) NULL DEFAULT 0 COMMENT '托收状态: 0: 待托收, 1: 回盘 , 2: 完成',
  `collection_time` datetime(0) NULL DEFAULT NULL COMMENT '托收时间',
  `back_time` datetime(0) NULL DEFAULT NULL COMMENT '回盘时间',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `modify_id` varchar(36)  NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL,
  `is_wait_back` varchar(2)  NULL DEFAULT 'N' COMMENT '是否待回盘, Y:是, N:已回盘',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_bc_jrl_body
-- ----------------------------
DROP TABLE IF EXISTS `t_bc_jrl_body`;
CREATE TABLE `t_bc_jrl_body`  (
  `id` varchar(36)  NOT NULL,
  `head_id` varchar(36)  NULL DEFAULT NULL,
  `bill_id` varchar(36)  NULL DEFAULT NULL COMMENT '账单id',
  `building_code` varchar(200)  NULL DEFAULT NULL COMMENT '建筑code',
  `building_full_name` varchar(200)  NULL DEFAULT NULL COMMENT '建筑全名',
  `cust_id` varchar(36)  NULL DEFAULT NULL COMMENT '客户id',
  `cust_name` varchar(100)  NULL DEFAULT NULL COMMENT '客户名',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '文件类型: 0:托收 , 1: 回盘',
  `detail_no` varchar(8)  NULL DEFAULT NULL COMMENT '明细序号,同一批次不重复',
  `agreement_no` varchar(32)  NULL DEFAULT NULL COMMENT '协议编号 , 回盘用',
  `amount` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '收款金额',
  `bank_type` varchar(2)  NULL DEFAULT '00' COMMENT '银行行别, 本地业务用 , 异地业务默认00',
  `bank_code` varchar(14)  NULL DEFAULT NULL COMMENT '银行行号 , 本地业务为空格 , 异地业务使用',
  `account` varchar(32)  NULL DEFAULT NULL COMMENT '收款户名',
  `account_name` varchar(120)  NULL DEFAULT NULL COMMENT '收款账户户主名称',
  `PS` varchar(120)  NULL DEFAULT NULL COMMENT '附言',
  `bank_resp_code` varchar(8)  NULL DEFAULT '--------' COMMENT '银行返回码, 返回用 , 提出时为 --------',
  `remark` varchar(100)  NULL DEFAULT NULL COMMENT '附加信息',
  `bank_resp_ps` varchar(120)  NULL DEFAULT NULL COMMENT '银行回执附言 , 提出时可不写',
  `create_id` varchar(36)  NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `modify_id` varchar(36)  NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL,
  `wy_amount` DECIMAL(8,2) NULL DEFAULT NULL,
  `bt_amount` DECIMAL(8,2) NULL DEFAULT NULL,
  `water_amount` DECIMAL(8,2) NULL DEFAULT NULL,
  `elect_amount` DECIMAL(8,2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_building_code`(`building_code`) USING BTREE,
  INDEX `idx_detail_no`(`detail_no`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_bc_jrl_head
-- ----------------------------
DROP TABLE IF EXISTS `t_bc_jrl_head`;
CREATE TABLE `t_bc_jrl_head`  (
  `id` varchar(36)  NOT NULL,
  `total_id` varchar(36)  NULL DEFAULT NULL COMMENT '头文件id',
  `coll_id` varchar(36)  NULL DEFAULT NULL COMMENT '托收文件id',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '类型: 0: 托收文件 , 1: 回盘文件',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `project_name` varchar(36)  NULL DEFAULT NULL COMMENT '项目名称',
  `company_id` varchar(36)  NULL DEFAULT NULL COMMENT '公司id',
  `business_type` varchar(3)  NULL DEFAULT NULL COMMENT '业务类型: 批量代付: 303 , 批量代收: 304',
  `enterprise_no` varchar(14)  NULL DEFAULT NULL COMMENT '企业代码',
  `bank_no` varchar(100)  NULL DEFAULT NULL COMMENT '代办银行号',
  `fee_no` varchar(5)  NULL DEFAULT NULL COMMENT '费项代码',
  `file_name` varchar(8)  NULL DEFAULT NULL COMMENT '交易文件名',
  `fee_type` varchar(3)  NULL DEFAULT NULL COMMENT '费种 , 人民币: CNY , 港币: HKD ,美元: USD',
  `total_count` int(8) NULL DEFAULT NULL COMMENT '总笔数',
  `total_amount` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '总金额',
  `complete_count` int(8) NULL DEFAULT NULL COMMENT '成功笔数',
  `complete_amount` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '总金额',
  `business_no` varchar(5)  NULL DEFAULT NULL COMMENT '业务编码: 30301: 人民币批量代付 , 30401: 人民币批量代收',
  `request_date` datetime(0) NULL DEFAULT NULL COMMENT '委托日期',
  `pay_date` datetime(0) NULL DEFAULT NULL COMMENT '应划款日期',
  `response_date` datetime(0) NULL DEFAULT NULL COMMENT '回执日期',
  `charge_account` varchar(32)  NULL DEFAULT NULL COMMENT '代办账号',
  `charge_account_name` varchar(120)  NULL DEFAULT NULL COMMENT '代办账户户名',
  `create_id` varchar(36)  NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `modify_id` varchar(36)  NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_bc_project
-- ----------------------------
DROP TABLE IF EXISTS `t_bc_project`;
CREATE TABLE `t_bc_project`  (
  `id` varchar(36)  NOT NULL COMMENT '主键',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `project_name` varchar(36)  NULL DEFAULT NULL COMMENT '项目名称',
  `wy_type` tinyint(1) NULL DEFAULT 0 COMMENT '物业托收方式: 2: 未启动, 0: 银联托收 , 1: 金融联托收',
  `bt_type` tinyint(1) NULL DEFAULT 0 COMMENT '本体基金托收方式: 2: 未启动, 0: 银联托收 , 1: 金融联托收',
  `water_type` tinyint(1) NULL DEFAULT 0 COMMENT '水费托收方式: 2: 未启动, 0: 银联托收 , 1: 金融联托收',
  `elect_type` tinyint(1) NULL DEFAULT 0 COMMENT '电费托收方式: 2: 未启动, 0: 银联托收 , 1: 金融联托收',
  `wy_status` tinyint(1) NULL DEFAULT 0 COMMENT '物业托收状态: 0: 开启 , 1: 关闭',
  `bt_status` tinyint(1) NULL DEFAULT 0 COMMENT '本体基金托收状态: 0: 开启 , 1: 关闭',
  `water_status` tinyint(1) NULL DEFAULT 0 COMMENT '水费托收状态: 0: 开启, 1: 关闭',
  `elect_status` tinyint(1) NULL DEFAULT 0 COMMENT '电费托收状态: 0: 开启, 1: 关闭',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '项目托收状态: 0: 开启, 1: 关闭',
  `shop_num` varchar(36)  NULL DEFAULT '0' COMMENT '商户号, 银联使用',
  `union_private_zone` varchar(11)  NULL DEFAULT NULL COMMENT '银联私有域',
  `enterprise_no` varchar(36)  NULL DEFAULT NULL COMMENT '企业编码 , 金融联使用',
  `jrl_wait_bank_no` varchar(30)  NULL DEFAULT NULL COMMENT '代办银行号, 金融联使用',
  `union_count` int(10) NULL DEFAULT 0 COMMENT '银联托收户数',
  `jrl_count` int(10) NULL DEFAULT 0 COMMENT '金融联托收户数',
  `create_id` varchar(36)  NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `modify_id` varchar(36)  NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_bc_union_back_body
-- ----------------------------
DROP TABLE IF EXISTS `t_bc_union_back_body`;
CREATE TABLE `t_bc_union_back_body`  (
  `id` varchar(36)  NOT NULL COMMENT '主键',
  `head_id` varchar(36)  NULL DEFAULT NULL COMMENT '头表id',
  `bank_no` varchar(8)  NULL DEFAULT NULL COMMENT '银行代号',
  `trade_date` datetime(0) NULL DEFAULT NULL COMMENT '交易日期',
  `order_no` varchar(16)  NULL DEFAULT NULL COMMENT '订单号',
  `trade_status` varchar(4)  NULL DEFAULT NULL COMMENT '交易状态: 1001 : 代扣成功 , 其余都为代扣失败/未执行',
  `resp_code` varchar(2)  NULL DEFAULT NULL COMMENT '响应码: 00成功 , 其余都为失败',
  `is_bank_card` tinyint(1) NULL DEFAULT NULL COMMENT '卡折类型: 0: 卡 , 1: 折',
  `bank_card_no` varchar(25)  NULL DEFAULT NULL COMMENT '卡号/折号',
  `amount` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '金额',
  `use` varchar(25)  NULL DEFAULT NULL COMMENT '用途',
  `private_zone` varchar(60)  NULL DEFAULT NULL COMMENT '私有域',
  `building_code` varchar(200)  NULL DEFAULT NULL COMMENT '建筑code',
  `building_full_name` varchar(200)  NULL DEFAULT NULL COMMENT '建筑全名',
  `bill_id` varchar(36)  NULL DEFAULT NULL COMMENT '账单id',
  `cust_id` varchar(36)  NULL DEFAULT NULL COMMENT '客户id',
  `cust_name` varchar(36)  NULL DEFAULT NULL COMMENT '客户名称',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `create_id` varchar(36)  NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `modify_id` varchar(36)  NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL,
  `wy_amount` DECIMAL(8,2) NULL DEFAULT NULL,
  `bt_amount` DECIMAL(8,2) NULL DEFAULT NULL,
  `water_amount` DECIMAL(8,2) NULL DEFAULT NULL,
  `elect_amount` DECIMAL(8,2) NULL DEFAULT NULL,
  `service_amount` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '手续费',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_building_code`(`building_code`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_bc_union_back_head
-- ----------------------------
DROP TABLE IF EXISTS `t_bc_union_back_head`;
CREATE TABLE `t_bc_union_back_head`  (
  `id` varchar(36)  NOT NULL,
  `file_name` varchar(100)  NULL DEFAULT NULL COMMENT '文件名',
  `total_id` varchar(36)  NULL DEFAULT NULL COMMENT '托收总单id',
  `total_count` int(10) NULL DEFAULT 0 COMMENT '总笔数',
  `total_amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '总金额',
  `complete_count` int(10) NULL DEFAULT 0 COMMENT '成功笔数',
  `complete_amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '成功金额',
  `type` tinyint(1) NULL DEFAULT 0 COMMENT '类型: 0: 银联, 1: 金融联',
  `create_id` varchar(36)  NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `modify_id` varchar(36)  NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_bc_union_collection_body
-- ----------------------------
DROP TABLE IF EXISTS `t_bc_union_collection_body`;
CREATE TABLE `t_bc_union_collection_body`  (
  `id` varchar(36)  NOT NULL,
  `head_id` varchar(36)  NULL DEFAULT NULL COMMENT '头表id',
  `bill_id` varchar(36)  NULL DEFAULT NULL COMMENT '账单ID',
  `building_code` varchar(200)  NULL DEFAULT NULL COMMENT '建筑code',
  `building_full_name` varchar(200)  NULL DEFAULT NULL COMMENT '建筑全名',
  `cust_id` varchar(36)  NULL DEFAULT NULL COMMENT '客户id',
  `cust_name` varchar(100)  NULL DEFAULT NULL COMMENT '客户名称',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `order_no` varchar(16)  NULL DEFAULT NULL COMMENT '订单号 定长16位',
  `bank_no` varchar(4)  NULL DEFAULT NULL COMMENT '开户行号',
  `is_card` tinyint(1) NULL DEFAULT NULL COMMENT '卡折标记 : 0: 卡 , 1: 折',
  `bank_card_no` varchar(25)  NULL DEFAULT NULL COMMENT '卡号',
  `id_card_master_name` varchar(20)  NULL DEFAULT NULL COMMENT '持卡人姓名',
  `id_card_type` varchar(2)  NULL DEFAULT NULL COMMENT '卡类型: 01:身份证, 02:军官证 , 03:护照, 04: 户口簿 , 05: 回乡证, 06: 其他',
  `id_card_no` varchar(25)  NULL DEFAULT NULL COMMENT '证件号码',
  `amount` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '金额',
  `use` varchar(25)  NULL DEFAULT NULL COMMENT '用途',
  `private_zone` varchar(60)  NULL DEFAULT NULL COMMENT '私有域',
  `create_id` varchar(36)  NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `modify_id` varchar(36)  NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL,
  `wy_amount` DECIMAL(8,2) NULL DEFAULT NULL,
  `bt_amount` DECIMAL(8,2) NULL DEFAULT NULL,
  `water_amount` DECIMAL(8,2) NULL DEFAULT NULL,
  `elect_amount` DECIMAL(8,2) NULL DEFAULT NULL,
  `service_amount` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '手续费',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_building_code`(`building_code`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_bc_union_collection_head
-- ----------------------------
DROP TABLE IF EXISTS `t_bc_union_collection_head`;
CREATE TABLE `t_bc_union_collection_head`  (
  `id` varchar(36)  NOT NULL COMMENT '主键',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `project_name` varchar(36)  NULL DEFAULT NULL COMMENT '项目名',
  `total_id` varchar(36)  NULL DEFAULT NULL COMMENT '总单id',
  `type` tinyint(1) NULL DEFAULT 0 COMMENT '托收类型: 0: 银联, 1: 金融联',
  `shop_num` varchar(36)  NULL DEFAULT NULL COMMENT '商户号',
  `batch_no` varchar(36)  NULL DEFAULT NULL COMMENT '批次号',
  `total_count` int(10) NULL DEFAULT 0 COMMENT '总笔数',
  `total_amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '总金额',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '生成时间',
  `create_id` varchar(36)  NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL,
  `modify_id` varchar(36)  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_bs_asset_account
-- ----------------------------
DROP TABLE IF EXISTS `t_bs_asset_account`;
CREATE TABLE `t_bs_asset_account`  (
  `id` varchar(36)  NOT NULL COMMENT '主键',
  `building_code` varchar(300)  NULL DEFAULT NULL COMMENT '建筑编码',
  `type` int(11) NULL DEFAULT NULL COMMENT '账户类型  0:通用账户  1:物业管理费账户 2:本体基金  3:水费账户  4:电费账户  ',
  `account_balance` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '余额',
  `use_status` int(11) NULL DEFAULT NULL COMMENT '使用状态',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目编码',
  `project_name` varchar(100)  NULL DEFAULT NULL COMMENT '项目名称',
  `create_time` date NULL DEFAULT NULL COMMENT '创建时间',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人编码',
  `create_name` varchar(50)  NULL DEFAULT NULL COMMENT '创建人名称',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改人编码',
  `modify_name` varchar(50)  NULL DEFAULT NULL COMMENT '修改人名称',
  `full_name` varchar(200)  NULL DEFAULT NULL COMMENT '建筑全称',
  `modify_time` date NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_building_code`(`building_code`(255)) USING BTREE
) ENGINE = InnoDB COMMENT = '计费账户表(和建筑资产关联，每个建筑分别有通用账户，物业管理账户，水费账户，电费账户，本体基金账户五个账户)';

-- ----------------------------
-- Table structure for t_bs_asset_account_stream
-- ----------------------------
DROP TABLE IF EXISTS `t_bs_asset_account_stream`;
CREATE TABLE `t_bs_asset_account_stream`  (
  `id` varchar(36)  NOT NULL COMMENT '主键',
  `parent_id` varchar(36)  NULL DEFAULT NULL COMMENT '父ID',
  `chang_money` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '变化金额',
  `occurrence_time` datetime(0) NULL DEFAULT NULL COMMENT '业务发生时间',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人编码',
  `create_name` varchar(50)  NULL DEFAULT NULL COMMENT '创建人名称',
  `purpose` varchar(100)  NULL DEFAULT NULL COMMENT '流水用途',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '计费账户流水表';

-- ----------------------------
-- Table structure for t_bs_asset_account_stream_delete_info
-- ----------------------------
DROP TABLE IF EXISTS `t_bs_asset_account_stream_delete_info`;
CREATE TABLE `t_bs_asset_account_stream_delete_info`  (
  `id` varchar(36)  NOT NULL COMMENT '主键',
  `parent_id` varchar(36)  NULL DEFAULT NULL COMMENT '父ID',
  `chang_money` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '变化金额',
  `occurrence_time` datetime(0) NULL DEFAULT NULL COMMENT '业务发生时间',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人编码',
  `create_name` varchar(50)  NULL DEFAULT NULL COMMENT '创建人名称'
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_bs_charge_bill_history
-- ----------------------------
DROP TABLE IF EXISTS `t_bs_charge_bill_history`;
CREATE TABLE `t_bs_charge_bill_history`  (
  `id` varchar(36)  NOT NULL COMMENT '主键',
  `charge_total_id` varchar(36)  NULL DEFAULT NULL COMMENT '本期总记录id',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `building_code` varchar(100)  NULL DEFAULT NULL COMMENT '建筑code',
  `full_name` varchar(150)  NULL DEFAULT NULL COMMENT '建筑全名',
  `last_bill_fee` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '上期账单金额',
  `last_payed` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '上期已付',
  `current_fee` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '本期产生金额',
  `late_fee` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '违约金',
  `current_bill_fee` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '本清应付(本期账单)',
  `account_balance` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '账户余额',
  `billing_time` datetime(0) NULL DEFAULT NULL COMMENT '计费时间',
  `last_bill_id` varchar(36)  NULL DEFAULT NULL COMMENT '上期账单id',
  `is_used` tinyint(1) NULL DEFAULT 0 COMMENT '是否有效',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `share_fee` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '分摊费用',
  `common_desummoney` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '通用账户抵扣金额',
  `no_common_desummoney` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '非通用账户抵扣金额',
  `tax` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '税金',
  `fee_item_detail` text  NULL COMMENT '记录各个费用项计算值拼凑成json字符串',
  `last_owed_info` text  NULL COMMENT '记录上个周期的欠费json数据,以便于后期进行重新计费',
  `current_kq_amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '本期扣取额,不含通用账户扣取',
  `is_zip_complete` tinyint(1) NULL DEFAULT 0 COMMENT '本分单是否已经打包完成',
  `aduit_status` tinyint(1) NULL DEFAULT 0 COMMENT '0未审核，1审核成功，2，审核失败',
  `temporary_bill` text  NULL COMMENT '记录临时计费的数据',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_building_code`(`building_code`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_bs_charge_bill_history_init_data
-- ----------------------------
DROP TABLE IF EXISTS `t_bs_charge_bill_history_init_data`;
CREATE TABLE `t_bs_charge_bill_history_init_data`  (
  `id` varchar(36)  NOT NULL COMMENT '主键',
  `charge_total_id` varchar(36)  NULL DEFAULT NULL COMMENT '本期总记录id',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `building_code` varchar(100)  NULL DEFAULT NULL COMMENT '建筑code',
  `full_name` varchar(150)  NULL DEFAULT NULL COMMENT '建筑全名',
  `last_bill_fee` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '上期账单金额',
  `last_payed` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '上期已付',
  `current_fee` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '本期产生金额',
  `late_fee` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '违约金',
  `current_bill_fee` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '本清应付(本期账单)',
  `account_balance` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '账户余额',
  `billing_time` datetime(0) NULL DEFAULT NULL COMMENT '计费时间',
  `last_bill_id` varchar(36)  NULL DEFAULT NULL COMMENT '上期账单id',
  `is_used` tinyint(1) NULL DEFAULT 0 COMMENT '是否有效',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `share_fee` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '分摊费用',
  `common_desummoney` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '通用账户抵扣金额',
  `no_common_desummoney` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '非通用账户抵扣金额',
  `tax` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '税金',
  `fee_item_detail` text  NULL COMMENT '记录各个费用项计算值拼凑成json字符串',
  `last_owed_info` text  NULL COMMENT '记录上个周期的欠费json数据,以便于后期进行重新计费',
  `current_kq_amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '本期扣取额,不含通用账户扣取',
  `is_zip_complete` tinyint(1) NULL DEFAULT 0 COMMENT '本分单是否已经打包完成',
  `aduit_status` tinyint(1) NULL DEFAULT 0 COMMENT '0未审核，1审核成功，2，审核失败',
  `temporary_bill` text  NULL COMMENT '记录临时计费的数据',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_bs_charge_bill_total
-- ----------------------------
DROP TABLE IF EXISTS `t_bs_charge_bill_total`;
CREATE TABLE `t_bs_charge_bill_total`  (
  `id` varchar(36)  NOT NULL COMMENT '主键',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `last_total_id` varchar(36)  NULL DEFAULT NULL COMMENT '上次总账单id',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '账户类型,0:通用账户,1:物业管理费,2:本体基金,3:水表,4:电表',
  `scheme_id` varchar(36)  NULL DEFAULT NULL COMMENT '本次计费关联的计费schemeid',
  `billing_time` datetime(0) NULL DEFAULT NULL COMMENT '计费时间',
  `current_fee` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '本期金额',
  `last_owed_fee` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '上期欠费',
  `total_fee` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '本期总计',
  `charging_type` tinyint(1) NULL DEFAULT 0 COMMENT '计费方式, 0:自动 , 1:手动',
  `is_rebilling` tinyint(1) NULL DEFAULT 1 COMMENT '是否重新计费, 0:是,1:不是',
  `audit_status` tinyint(1) NULL DEFAULT 0 COMMENT '是否审核: 0:待审核,1审核完成,2不通过,3部分审核完成',
  `cmac_is_billing` tinyint(1) NULL DEFAULT 0 COMMENT '是否从通用账户扣费过, 0:未扣费, 1:扣费',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改人',
  `bill_status` tinyint(1) NULL DEFAULT NULL COMMENT '计费状态 0 未计费；1 全部计费  2，部分计费',
  `sun_status` tinyint(1) NULL DEFAULT 0 COMMENT '虚拟表对用的账单汇总状态，0，为汇总，1，已经汇总',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '每期计费总计表(含审批,重新计费状态)';

-- ----------------------------
-- Table structure for t_bs_charge_type
-- ----------------------------
DROP TABLE IF EXISTS `t_bs_charge_type`;
CREATE TABLE `t_bs_charge_type`  (
  `id` varchar(36)  NOT NULL COMMENT '主键id',
  `unit` varchar(36)  NULL DEFAULT NULL COMMENT '单位',
  `formula_info_value` varchar(800)  NULL DEFAULT NULL COMMENT '公式内容值',
  `formula_info` varchar(800)  NULL DEFAULT NULL COMMENT '公式内容名',
  `charging_rule_id` varchar(36)  NOT NULL COMMENT '关联规则id',
  `charging_id` varchar(36)  NULL DEFAULT NULL COMMENT '细项收费项编号',
  `charging_name` varchar(200)  NULL DEFAULT NULL COMMENT '收费项名称',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `project_name` varchar(36)  NULL DEFAULT NULL COMMENT '项目名',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(36)  NULL DEFAULT NULL COMMENT '更新人编码',
  `min_criticalpoint` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '临界点一',
  `max_criticalpoint` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '临界点二',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '方案下的收费类型表';

-- ----------------------------
-- Table structure for t_bs_charging_rules
-- ----------------------------
DROP TABLE IF EXISTS `t_bs_charging_rules`;
CREATE TABLE `t_bs_charging_rules`  (
  `id` varchar(36)  NOT NULL COMMENT '主键id',
  `rule_name` varchar(36)  NULL DEFAULT NULL COMMENT '规则名称',
  `charging_scheme_id` varchar(36)  NOT NULL COMMENT '关联方案id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(36)  NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '方案下的收费规则表（一个方案可以多个规则并存）';

-- ----------------------------
-- Table structure for t_bs_charging_scheme
-- ----------------------------
DROP TABLE IF EXISTS `t_bs_charging_scheme`;
CREATE TABLE `t_bs_charging_scheme`  (
  `id` varchar(36)  NOT NULL COMMENT '主键id',
  `scheme_name` varchar(36)  NOT NULL COMMENT '方案名称',
  `is_used` tinyint(1) NULL DEFAULT NULL COMMENT '启用状态（0：启用，1停用）',
  `start_using_date` datetime(0) NULL DEFAULT NULL COMMENT '启用日期',
  `end_using_date` datetime(0) NULL DEFAULT NULL COMMENT '失效日期',
  `charging_type` tinyint(1) NULL DEFAULT 0 COMMENT '计费方式（0：自动，1：手动）',
  `proportion` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '违约金比例  %',
  `overdue_start_dates` int(11) NULL DEFAULT 0 COMMENT '逾期开始天数',
  `calculation_type` tinyint(1) NULL DEFAULT 0 COMMENT '违约金计算方式（0：单利，1：复利）',
  `scheme_type` tinyint(1) NULL DEFAULT NULL COMMENT '方案类型（1:物业管理费，2：本体基金 3：水费 4：电费）',
  `frequency` tinyint(2) NULL DEFAULT NULL COMMENT '计算频率(以月为单位)',
  `charging_area` tinyint(2) NULL DEFAULT NULL COMMENT '计算面积（针对物业管理费 0：建筑面积 1：套内面积,没有置空）',
  `charge_data` int(11) NULL DEFAULT NULL COMMENT '计费时间（某天--存储天）',
  `is_effective` tinyint(1) NULL DEFAULT 0 COMMENT '是否生效（0：有效，1：失效）',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `project_name` varchar(36)  NULL DEFAULT NULL COMMENT '项目名',
  `tax_rate` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '税率',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `create_name` varchar(36)  NULL DEFAULT NULL COMMENT '创建人姓名',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `update_by` varchar(36)  NULL DEFAULT NULL COMMENT '最后修改人',
  `update_name` varchar(36)  NULL DEFAULT NULL COMMENT '创建人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '物业，本体，水，电相关方案表';

-- ----------------------------
-- Table structure for t_bs_constant
-- ----------------------------
DROP TABLE IF EXISTS `t_bs_constant`;
CREATE TABLE `t_bs_constant`  (
  `id` varchar(36)  NOT NULL COMMENT '主键ID',
  `bill_constant_name` varchar(100)  NULL DEFAULT NULL COMMENT '常量名',
  `bill_constant_value` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '常量值',
  `constant_type` int(4) NULL DEFAULT NULL COMMENT '0:水费常量  1电费计费常量',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目编号',
  `project_name` varchar(100)  NULL DEFAULT NULL COMMENT '项目名称',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人编码',
  `create_name` varchar(100)  NULL DEFAULT NULL COMMENT '创建人名称',
  `create_time` date NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_id` varchar(36)  NULL DEFAULT NULL COMMENT '最后修改人编码',
  `last_update_name` varchar(100)  NULL DEFAULT NULL COMMENT '最后修改人名称',
  `last_update_time` date NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '计费常量表';

-- ----------------------------
-- Table structure for t_bs_owed_history
-- ----------------------------
DROP TABLE IF EXISTS `t_bs_owed_history`;
CREATE TABLE `t_bs_owed_history`  (
  `id` varchar(36)  NOT NULL COMMENT '主键id',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `owed_amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '欠费金额',
  `account_id` varchar(36)  NULL DEFAULT NULL COMMENT '关联账户id',
  `owed_time` datetime(0) NULL DEFAULT NULL COMMENT '欠费时间',
  `owed_end_time` datetime(0) NULL DEFAULT NULL COMMENT '费用交清时间',
  `is_used` tinyint(1) NULL DEFAULT 0 COMMENT '是否生效: 0:生效 , 1:失效',
  `total_late_fee` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '累计产生总违约费',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_account_id`(`account_id`) USING BTREE
) ENGINE = InnoDB COMMENT = '欠费历史表';

-- ----------------------------
-- Table structure for t_bs_pay_info
-- ----------------------------
DROP TABLE IF EXISTS `t_bs_pay_info`;
CREATE TABLE `t_bs_pay_info`  (
  `id` varchar(36)  NOT NULL,
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `building_code` varchar(200)  NULL DEFAULT NULL COMMENT '建筑code',
  `building_full_name` varchar(200)  NULL DEFAULT NULL COMMENT '建筑名',
  `batch_no` varchar(50)  NULL DEFAULT NULL COMMENT '流水号',
  `pay_type` tinyint(1) NULL DEFAULT 1 COMMENT '支付类型: 1 现金 2 微信 3 银联 4 混合支付',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态: 0: 收款, 1: 退款 , 2: 减免',
  `asset_no` varchar(200)  NULL DEFAULT NULL COMMENT '资产编号',
  `cust_name` varchar(100)  NULL DEFAULT NULL COMMENT '客户名称',
  `wy_amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '物业金额',
  `bt_amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '本体金额',
  `water_amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '水费金额',
  `elect_amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '电费金额',
  `common_amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '通用账户金额',
  `jm_amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '减免金额',
  `jm_remark` varchar(200)  NULL DEFAULT NULL COMMENT '减免备注',
  `wy_tax` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '物业税金',
  `bt_tax` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '本体税金',
  `water_tax` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '水费税金',
  `elect_tax` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '电费税金',
  `pay_cash` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '现金支付额',
  `pay_wx` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '微信支付额',
  `pay_union` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '银联刷卡支付额',
  `pay_zfb` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '支付宝支付额',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `create_id` varchar(36)  NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL,
  `modify_id` varchar(36)  NULL DEFAULT NULL,
  `payer_name` varchar(50)  NULL DEFAULT NULL COMMENT '付款人名称',
  `is_give_invoice` tinyint(1) NULL DEFAULT 1 COMMENT '是否开具发票(0: 是, 1: 未)',
  `relation_id` varchar(36)  NULL DEFAULT NULL COMMENT '关联银账交割id ',
  `pay_coll` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '托收支付额',
  `pay_bank` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '银行支付额',
  `wy_late_fee` DECIMAL(8,2) NULL DEFAULT 0.00,
  `bt_late_fee` DECIMAL(8,2) NULL DEFAULT 0.00,
  `water_late_fee` DECIMAL(8,2) NULL DEFAULT 0.00,
  `elect_late_fee` DECIMAL(8,2) NULL DEFAULT 0.00,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_bs_project
-- ----------------------------
DROP TABLE IF EXISTS `t_bs_project`;
CREATE TABLE `t_bs_project`  (
  `id` varchar(36)  NOT NULL,
  `project_id` varchar(36)  NOT NULL,
  `project_name` varchar(36)  NOT NULL,
  `billing_time` datetime(0) NULL DEFAULT NULL COMMENT '计费时间',
  `wy_status` tinyint(1) NULL DEFAULT 0 COMMENT '物业管理费计费状态 : 0:未启动 ,1:未计费 , 2:计费,3:计费中',
  `bt_status` tinyint(1) NULL DEFAULT 0 COMMENT '本体基金费计费状态: 0:未启动, 1:未计费 , 2:计费,3:计费中',
  `water_status` tinyint(1) NULL DEFAULT 0 COMMENT '水费计费状态: 0:未启动, 1:未计费 , 2:计费,3:计费中,4.部分计费',
  `elect_status` tinyint(1) NULL DEFAULT 0 COMMENT '电费计费状态: 0:未启动, 1:未计费 , 2:计费,3:计费中,4.部分计费',
  `common_status` tinyint(1) NULL DEFAULT 0 COMMENT '通用账户开关: 0:开 , 1:关闭',
  `current_fee` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '本期金额',
  `last_owed_fee` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '上期欠费金额',
  `total_fee` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '合计总金额',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '当前项目启动状态: 0 : 开启, 1: 关闭',
  `wy_order` tinyint(1) NULL DEFAULT NULL COMMENT '物业账户优先级',
  `bt_order` tinyint(1) NULL DEFAULT NULL COMMENT '本体基金账户优先级',
  `water_order` tinyint(1) NULL DEFAULT NULL COMMENT '水费账户优先级',
  `elect_order` tinyint(1) NULL DEFAULT NULL COMMENT '电费账户优先级',
  `is_gen_bill` tinyint(1) NULL DEFAULT 0 COMMENT '账单是否已生成, 0: 未生成, 1: 已生成',
  `create_id` varchar(36)  NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_id` varchar(36)  NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '项目收费计费状态表';

-- ----------------------------
-- Table structure for t_bs_push_finance_data
-- ----------------------------
DROP TABLE IF EXISTS `t_bs_push_finance_data`;
CREATE TABLE `t_bs_push_finance_data`  (
  `id` varchar(64)  NOT NULL,
  `company` varchar(64)  NULL DEFAULT NULL COMMENT '公司名称',
  `project_name` varchar(64)  NULL DEFAULT NULL COMMENT '项目名称',
  `project_id` varchar(64)  NULL DEFAULT NULL COMMENT '项目id',
  `toll_date` datetime(0) NULL DEFAULT NULL COMMENT '收费日期',
  `toll_project` varchar(64)  NULL DEFAULT NULL COMMENT '收费项目,（物业 wy，水费 water，电费 elect，本体 bt，交费 pay，产品购买  product , 退费  Refund （还没有做后需添加））',
  `income_money` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '收入金额',
  `tax_money` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '税金额',
  `tariff` varchar(16)  NULL DEFAULT '0.0' COMMENT '税率',
  `currency` varchar(16)  NULL DEFAULT 'RMB' COMMENT '币种',
  `toll_mode` varchar(16)  NULL DEFAULT NULL COMMENT '收费方式(现金  cash，刷卡 charge，支付宝  alipay 微信支付  weixinpay)',
  `toll_type` varchar(16)  NULL DEFAULT NULL COMMENT '收费类型	(1.收费，2.退费，3.应收，4.押金(暂时还没做，后续添加))',
  `houses_code` varchar(128)  NULL DEFAULT NULL COMMENT '房屋编号',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `result` varchar(32)  NULL DEFAULT NULL COMMENT '推送结果 success 成功   erro  失败',
  `message` varchar(128)  NULL DEFAULT NULL COMMENT '接口返回的信息描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '推送财务相关数据接口的本地记录表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_bs_rebilling_info
-- ----------------------------
DROP TABLE IF EXISTS `t_bs_rebilling_info`;
CREATE TABLE `t_bs_rebilling_info`  (
  `id` varchar(36)  NOT NULL COMMENT '主键',
  `account_id` varchar(36)  NULL DEFAULT NULL COMMENT '关联账户id',
  `building_code` varchar(200)  NULL DEFAULT NULL COMMENT '建筑code',
  `full_name` varchar(200)  NULL DEFAULT NULL COMMENT '建筑全名',
  `type` tinyint(1) NULL DEFAULT 0 COMMENT '重计费项: 0:通用账户 , 1:物业管理费, 2:本体基金 , 3:水费 , 4:电费',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '当前状态: 0:扣取, 1:退回, 2:正常',
  `change_amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '到账结果: 扣取为负值, 退回为正值',
  `rebilling_start_time` datetime(0) NULL DEFAULT NULL COMMENT '重计开始月份',
  `billing_time` datetime(0) NULL DEFAULT NULL COMMENT '计费时间',
  `rebilling_reason` varchar(200)  NULL DEFAULT NULL COMMENT '重计原因',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `create_id` varchar(36)  NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL,
  `modify_id` varchar(36)  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_bs_rule_building_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_bs_rule_building_relation`;
CREATE TABLE `t_bs_rule_building_relation`  (
  `id` varchar(36)  NOT NULL COMMENT '主键id',
  `charging_rule_id` varchar(36)  NOT NULL COMMENT '关联规则id(一个规则选择多个建筑就有多条数据)',
  `relation_building_code` varchar(512)  NULL DEFAULT NULL COMMENT '关联建筑code',
  `relation_building_name` varchar(512)  NULL DEFAULT NULL COMMENT '关联建筑全名称',
  `relation_building_pid` varchar(500)  NULL DEFAULT NULL COMMENT '父ID 构成树',
  `relation_building_full_name` varchar(512)  NULL DEFAULT NULL COMMENT '关联建筑全称',
  `relation_building_type` varchar(30)  NULL DEFAULT NULL COMMENT '关联建筑房屋类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '方案关联建筑信息表（用于水电表的方案存在关联部分建筑而非全部项目）';

-- ----------------------------
-- Table structure for t_bs_share_basics_info
-- ----------------------------
DROP TABLE IF EXISTS `t_bs_share_basics_info`;
CREATE TABLE `t_bs_share_basics_info`  (
  `id` varchar(36)  NOT NULL COMMENT '主键id',
  `share_name` varchar(64)  NULL DEFAULT '0.00' COMMENT '分摊名',
  `share_frequency` int(11) NULL DEFAULT NULL COMMENT '分摊频率(0:无频率一次)',
  `is_used` tinyint(2) NOT NULL COMMENT '开/关(0 开,1 关)',
  `start_mode` tinyint(2) NULL DEFAULT 0 COMMENT '启动方式(0:自动  1：手动)',
  `effect_time` datetime(0) NULL DEFAULT NULL COMMENT '生效时间',
  `invalid_time` datetime(0) NULL DEFAULT NULL COMMENT '失效时间',
  `last_execute_time` datetime(0) NULL DEFAULT NULL COMMENT '上次执行时间',
  `share_amount` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '分摊金额',
  `share_type` tinyint(2) NULL DEFAULT NULL COMMENT '分摊类型(0:物业管理费分摊 1：水费分摊  2：电费分摊)',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `project_name` varchar(36)  NULL DEFAULT NULL COMMENT '项目名称',
  `create_by` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '分摊基础信息表';

-- ----------------------------
-- Table structure for t_bs_share_building_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_bs_share_building_relation`;
CREATE TABLE `t_bs_share_building_relation`  (
  `id` varchar(36)  NOT NULL COMMENT '主键id',
  `share_task_id` varchar(36)  NOT NULL COMMENT '分摊关联任务表id',
  `relation_building_code` varchar(512)  NULL DEFAULT NULL COMMENT '关联建筑code',
  `relation_building_name` varchar(512)  NULL DEFAULT NULL COMMENT '关联建筑全名称',
  `relation_building_pid` varchar(512)  NULL DEFAULT NULL COMMENT '关联建筑父id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '分摊任务建筑关联表（用于水电表的分摊中存在多个分摊任务）';

-- ----------------------------
-- Table structure for t_bs_share_related_task
-- ----------------------------
DROP TABLE IF EXISTS `t_bs_share_related_task`;
CREATE TABLE `t_bs_share_related_task`  (
  `id` varchar(36)  NOT NULL COMMENT '主键id',
  `share_basics_id` varchar(36)  NULL DEFAULT NULL COMMENT '分摊id',
  `share_task_name` varchar(36)  NULL DEFAULT NULL COMMENT '分摊任务名称',
  `share_amount_formula` varchar(128)  NULL DEFAULT NULL COMMENT '分摊总量（公式）',
  `share_amount` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '实际分摊量',
  `share_money` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '分摊金额',
  `share_type` tinyint(2) NULL DEFAULT NULL COMMENT '分摊方式(0:按户，1：按用量)',
  `share_price` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '分摊单价',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '分摊关联任务表（针对水电表--一个分摊可以多个分摊任务）';

-- ----------------------------
-- Table structure for t_deposit
-- ----------------------------
DROP TABLE IF EXISTS `t_deposit`;
CREATE TABLE `t_deposit`  (
  `id` varchar(36)  NOT NULL COMMENT '主键',
  `project_id` varchar(50)  NULL DEFAULT NULL COMMENT '项目编码',
  `name` varchar(100)  NULL DEFAULT NULL COMMENT '押金名',
  `pay_deposit_datetime` datetime(0) NULL DEFAULT NULL COMMENT '押金收取时间',
  `total_amount` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '押金总额',
  `deposit_person` varchar(100)  NULL DEFAULT NULL COMMENT '押金人',
  `order_batch_no` varchar(50)  NULL DEFAULT NULL COMMENT '订单批次号',
  `status` varchar(20)  NULL DEFAULT NULL COMMENT '状态：待退，完成',
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建用户id',
  `creater_name` varchar(200)  NULL DEFAULT NULL COMMENT '创建用户名称',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '最后更新用户id',
  `modify_name` varchar(200)  NULL DEFAULT NULL COMMENT '最后更新用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '押金管理';

-- ----------------------------
-- Table structure for t_deposit_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_deposit_detail`;
CREATE TABLE `t_deposit_detail`  (
  `id` varchar(36)  NOT NULL COMMENT '主键',
  `deposit_id` varchar(36)  NULL DEFAULT NULL COMMENT '主表id',
  `order_number` varchar(100)  NULL DEFAULT NULL COMMENT '退押单号',
  `redeem_deposi_amount` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '退押金额',
  `deduct_amount` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '扣除金额',
  `redeem_deposit_datetime` datetime(0) NULL DEFAULT NULL COMMENT '退押时间',
  `description` varchar(250)  NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '退押明细';

-- ----------------------------
-- Table structure for t_division
-- ----------------------------
DROP TABLE IF EXISTS `t_division`;
CREATE TABLE `t_division`  (
  `id` int(30) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `division_name` varchar(30)  NULL DEFAULT NULL COMMENT '部门',
  `isinitialize` int(11) NULL DEFAULT 0 COMMENT '是否初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_elect_meter_asset_no
-- ----------------------------
DROP TABLE IF EXISTS `t_elect_meter_asset_no`;
CREATE TABLE `t_elect_meter_asset_no`  (
  `asset_no` varchar(50)  NULL DEFAULT NULL
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_event_test
-- ----------------------------
DROP TABLE IF EXISTS `t_event_test`;
CREATE TABLE `t_event_test`  (
  `id` varchar(36)  NULL DEFAULT NULL,
  `name` varchar(36)  NULL DEFAULT NULL
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_fields
-- ----------------------------
DROP TABLE IF EXISTS `t_fields`;
CREATE TABLE `t_fields`  (
  `id` varchar(200)  NOT NULL DEFAULT '' COMMENT '主键',
  `table_id` varchar(50)  NULL DEFAULT NULL COMMENT '表标识',
  `field_name` varchar(100)  NULL DEFAULT NULL COMMENT '字段名',
  `field_type` varchar(100)  NULL DEFAULT NULL COMMENT '字段类型',
  `field_length` tinyint(1) NULL DEFAULT NULL COMMENT '字段长度',
  `display_name` varchar(100)  NULL DEFAULT NULL COMMENT '字段显示名称',
  `display_flag` varchar(10)  NULL DEFAULT 'No' COMMENT '是否展示',
  `required` varchar(10)  NULL DEFAULT 'No' COMMENT '是否必填',
  `order_index` tinyint(1) NULL DEFAULT NULL COMMENT '排序索引',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '垂直表结构，用于存储字段';

-- ----------------------------
-- Table structure for t_jg_account_receivable
-- ----------------------------
DROP TABLE IF EXISTS `t_jg_account_receivable`;
CREATE TABLE `t_jg_account_receivable`  (
  `id` varchar(36)  NOT NULL COMMENT '主键id',
  `trad_no` varchar(64)  NULL DEFAULT NULL COMMENT '交易编号',
  `payer_name` varchar(256)  NULL DEFAULT NULL COMMENT '付款人姓名',
  `payed_type` tinyint(2) NULL DEFAULT NULL COMMENT '付款方式 1 现金 2 微信 3 银联 4 混合支付　5托收　6银行收款',
  `payed_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '付款时间',
  `pay_wx` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '微信支付金额',
  `alipay` DECIMAL(8,2) NULL DEFAULT NULL,
  `pay_cash` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '现金支付金额',
  `pay_union` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '银联支付金额',
  `bank_receipts` DECIMAL(8,2) NULL DEFAULT NULL,
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '状态 1 已结算 2 未结算  3 已退款',
  `opr_id` varchar(36)  NULL DEFAULT NULL COMMENT '操作人id',
  `opr_name` varchar(36)  NULL DEFAULT NULL COMMENT '操作人姓名',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目编号',
  `project_name` varchar(128)  NULL DEFAULT NULL COMMENT '项目名称',
  `total_id` varchar(128)  NULL DEFAULT NULL COMMENT '总结算单id',
  `relation_id` varchar(36)  NULL DEFAULT NULL COMMENT '关联id',
  `business_type` int(11) NULL DEFAULT 1 COMMENT '1,交费 2，产品 3，押金',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_trad_no`(`trad_no`) USING BTREE
) ENGINE = InnoDB COMMENT = '收账明细表';

-- ----------------------------
-- Table structure for t_jg_deposit_receipt
-- ----------------------------
DROP TABLE IF EXISTS `t_jg_deposit_receipt`;
CREATE TABLE `t_jg_deposit_receipt`  (
  `id` varchar(36)  NOT NULL COMMENT '主键id',
  `total_id` varchar(36)  NULL DEFAULT NULL COMMENT '总账单id',
  `deposit_num` varchar(256)  NULL DEFAULT NULL COMMENT '存单号',
  `amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '存款金额',
  `opr_id` varchar(64)  NULL DEFAULT NULL COMMENT '操作人id',
  `opr_name` varchar(256)  NULL DEFAULT NULL COMMENT '操作人姓名',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `project_id` varchar(128)  NULL DEFAULT NULL COMMENT '项目名称',
  `project_name` varchar(128)  NULL DEFAULT NULL COMMENT '项目名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '存单表';

-- ----------------------------
-- Table structure for t_jg_give_account_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_jg_give_account_detail`;
CREATE TABLE `t_jg_give_account_detail`  (
  `id` varchar(36)  NOT NULL COMMENT '主键id',
  `opr_num` varchar(128)  NULL DEFAULT NULL COMMENT '批次号',
  `opr_amount` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '交账金额',
  `type` tinyint(2) NULL DEFAULT NULL COMMENT '交账来源 1 系统  2 其他',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '1 待确认 2 退回 3 已确认',
  `opr_id` varchar(128)  NULL DEFAULT NULL COMMENT '交账人id',
  `opr_name` varchar(128)  NULL DEFAULT NULL COMMENT '交账人姓名',
  `receive_id` varchar(128)  NULL DEFAULT NULL COMMENT '接收人id',
  `receive_name` varchar(256)  NULL DEFAULT NULL COMMENT '接收人姓名',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目编号',
  `project_name` varchar(256)  NULL DEFAULT NULL COMMENT '项目名称',
  `total_id` varchar(128)  NULL DEFAULT NULL COMMENT '总账单id',
  `remark` varchar(512)  NULL DEFAULT NULL COMMENT '备注',
  `opr_type` tinyint(2) NULL DEFAULT NULL COMMENT '交账方式  1 现金  2 其他',
  `opr_start_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '交账时间',
  `opr_end_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '交账确认时间',
  `next_total_id` varchar(128)  NULL DEFAULT NULL COMMENT '总结算表id 关联总结算表id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '交账明细表';

-- ----------------------------
-- Table structure for t_jg_staff_grop
-- ----------------------------
DROP TABLE IF EXISTS `t_jg_staff_grop`;
CREATE TABLE `t_jg_staff_grop`  (
  `id` varchar(36)  NOT NULL COMMENT '主键id',
  `user_id` varchar(36)  NOT NULL COMMENT '用户id，关联t_sys_user',
  `staff_name` varchar(128)  NULL DEFAULT NULL COMMENT '员工姓名',
  `role_name` varchar(128)  NULL DEFAULT NULL COMMENT '角色名',
  `role_level` tinyint(1) NULL DEFAULT 0 COMMENT '角色等级',
  `pid` varchar(36)  NULL DEFAULT NULL COMMENT '上级id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目编号',
  `project_name` varchar(256)  NULL DEFAULT NULL COMMENT '项目名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '交割员工组表';

-- ----------------------------
-- Table structure for t_jg_total_calculation
-- ----------------------------
DROP TABLE IF EXISTS `t_jg_total_calculation`;
CREATE TABLE `t_jg_total_calculation`  (
  `id` varchar(36)  NOT NULL COMMENT '主键id',
  `opr_id` varchar(36)  NULL DEFAULT NULL COMMENT '操作人id',
  `opr_name` varchar(256)  NULL DEFAULT NULL COMMENT '操作人姓名',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '状态 1 待确认 2 已退回 3已交账',
  `cash_total` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '现金总金额',
  `cash_gaven` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '现金已交金额',
  `cash_not_give` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '现金未交金额',
  `wx_total` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '微信总金额',
  `wx_gaven` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '微信已交金额',
  `wx_not_give` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '微信未交金额',
  `alipay_total` DECIMAL(8,2) NULL DEFAULT NULL,
  `alipay_gaven` DECIMAL(8,2) NULL DEFAULT NULL,
  `alipay_not_give` DECIMAL(8,2) NULL DEFAULT NULL,
  `union_total` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '银联总金额',
  `union_gaven` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '银联已交金额',
  `union_not_give` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '银联未交金额',
  `bank_receipts_total` DECIMAL(8,2) NULL DEFAULT NULL,
  `bank_receipts_gaven` DECIMAL(8,2) NULL DEFAULT NULL,
  `bank_receipts_not_give` DECIMAL(8,2) NULL DEFAULT NULL,
  `project_id` varchar(128)  NULL DEFAULT NULL COMMENT '项目名称',
  `project_name` varchar(128)  NULL DEFAULT NULL COMMENT '项目名称',
  `receive_id` varchar(128)  NULL DEFAULT NULL COMMENT '接收人id',
  `receive_name` varchar(256)  NULL DEFAULT NULL COMMENT '接收人姓名',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `total_id` varchar(128)  NULL DEFAULT NULL COMMENT '结算总单id',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `total_num` varchar(64)  NULL DEFAULT NULL COMMENT '总单号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '总结算表';

-- ----------------------------
-- Table structure for t_product_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_product_detail`;
CREATE TABLE `t_product_detail`  (
  `id` varchar(36)  NOT NULL,
  `project_id` varchar(36)  NULL DEFAULT '' COMMENT '项目编码',
  `batch_no` varchar(100)  NULL DEFAULT NULL COMMENT '批次号',
  `code` varchar(100)  NULL DEFAULT NULL COMMENT '产品编码',
  `field_id` varchar(100)  NULL DEFAULT NULL COMMENT '字段名',
  `field_value` varchar(4000)  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '产品子表';

-- ----------------------------
-- Table structure for t_product_detail_history
-- ----------------------------
DROP TABLE IF EXISTS `t_product_detail_history`;
CREATE TABLE `t_product_detail_history`  (
  `id` varchar(36)  NOT NULL,
  `project_id` varchar(36)  NULL DEFAULT '' COMMENT '项目编码',
  `batch_no` varchar(100)  NULL DEFAULT NULL COMMENT '批次号',
  `code` varchar(100)  NULL DEFAULT NULL COMMENT '产品编码',
  `field_id` varchar(100)  NULL DEFAULT NULL COMMENT '字段名',
  `field_value` varchar(4000)  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '产品子表';

-- ----------------------------
-- Table structure for t_product_order
-- ----------------------------
DROP TABLE IF EXISTS `t_product_order`;
CREATE TABLE `t_product_order`  (
  `id` varchar(36)  NOT NULL,
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目编码',
  `batch_no` varchar(100)  NULL DEFAULT NULL COMMENT '批次号',
  `product_type` varchar(100)  NULL DEFAULT NULL COMMENT '产品类型',
  `product_name` varchar(100)  NULL DEFAULT NULL COMMENT '产品名称',
  `total_price` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '总金额',
  `discount_price` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '折扣金额',
  `status` varchar(20)  NULL DEFAULT NULL COMMENT '订单状态',
  `buyer_id` varchar(36)  NULL DEFAULT NULL COMMENT '客户id，对应个人客户或者企业客户表的id',
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建用户id',
  `creater_name` varchar(50)  NULL DEFAULT NULL COMMENT '创建用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改人id',
  `modify_name` varchar(50)  NULL DEFAULT NULL COMMENT '修改人名称',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_batch_no`(`batch_no`) USING BTREE,
  INDEX `idx_buyer_id`(`buyer_id`) USING BTREE
) ENGINE = InnoDB COMMENT = '产品历史交易表';

-- ----------------------------
-- Table structure for t_product_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_product_order_detail`;
CREATE TABLE `t_product_order_detail`  (
  `id` varchar(36)  NOT NULL,
  `order_id` varchar(36)  NULL DEFAULT NULL COMMENT '订单编号',
  `order_batch_no` varchar(36)  NULL DEFAULT NULL COMMENT '订单号',
  `product_common` varchar(2000)  NULL DEFAULT NULL COMMENT '产品公共字段部分',
  `quantity` tinyint(4) NULL DEFAULT NULL COMMENT '购买数量',
  `order_amount` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '订单金额',
  `buy_begin_date` datetime(0) NULL DEFAULT NULL COMMENT '订单开始时间',
  `buy_end_date` datetime(0) NULL DEFAULT NULL,
  `description` varchar(200)  NULL DEFAULT NULL COMMENT '描述',
  `house_code_new` varchar(35)  NULL DEFAULT NULL COMMENT '资产新编码',
  `building_code` varchar(100)  NULL DEFAULT NULL COMMENT '建筑编码',
  `building_full_name` varchar(200)  NULL DEFAULT NULL COMMENT '建筑全名称',
  `product_code` varchar(35)  NULL DEFAULT NULL COMMENT '产品编码',
  `product_name` varchar(100)  NULL DEFAULT NULL COMMENT '产品名称',
  `product_description` varchar(200)  NULL DEFAULT NULL COMMENT '产品描述',
  `product_average_price` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '产品单价',
  `deposit` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '产品押金',
  `taxrate` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '产品税率',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_product_payment_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_product_payment_detail`;
CREATE TABLE `t_product_payment_detail`  (
  `id` varchar(36)  NOT NULL,
  `order_id` varchar(36)  NOT NULL COMMENT '订单编号',
  `order_batch_no` varchar(35)  NULL DEFAULT NULL COMMENT '订单批次号',
  `business_serial_number` varchar(50)  NULL DEFAULT NULL COMMENT '银行交易流水号',
  `pay_type` varchar(10)  NULL DEFAULT NULL COMMENT '支付类型：现金、刷卡、微信支付宝',
  `price` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '当次支付金额金额',
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '操作人、收款人id',
  `creater_name` varchar(20)  NULL DEFAULT NULL COMMENT '操作人、收款人名称',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '产品支付明细表';

-- ----------------------------
-- Table structure for t_property_changing_history
-- ----------------------------
DROP TABLE IF EXISTS `t_property_changing_history`;
CREATE TABLE `t_property_changing_history`  (
  `id` varchar(36)  NOT NULL DEFAULT '' COMMENT '主键',
  `building_code` varchar(100)  NULL DEFAULT NULL COMMENT '建筑编号',
  `building_type` varchar(100)  NULL DEFAULT NULL COMMENT '资产类型',
  `old_holder` varchar(500)  NULL DEFAULT NULL COMMENT '旧产权人',
  `new_holder` varchar(500)  NULL DEFAULT NULL COMMENT '新产权人',
  `old_holder_name` varchar(1000)  NULL DEFAULT NULL COMMENT '旧产权人名称',
  `new_holder_name` varchar(1000)  NULL DEFAULT NULL COMMENT '新产权人名称',
  `building_certificate` varchar(100)  NULL DEFAULT NULL COMMENT '房产证',
  `subscribed_date` date NULL DEFAULT NULL COMMENT '认购日期',
  `project_id` varchar(100)  NULL DEFAULT NULL COMMENT '项目编码',
  `cust_from` varchar(10)  NULL DEFAULT NULL COMMENT '客户来源：个人客户和企业客户',
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建用户id',
  `creater_name` varchar(200)  NULL DEFAULT NULL COMMENT '创建用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '产权变更记录';

-- ----------------------------
-- Table structure for t_quill_editor
-- ----------------------------
DROP TABLE IF EXISTS `t_quill_editor`;
CREATE TABLE `t_quill_editor`  (
  `id` varchar(36)  NOT NULL,
  `table_id` varchar(100)  NULL DEFAULT NULL COMMENT '表名字',
  `relation_id` varchar(100)  NULL DEFAULT NULL COMMENT '表的关联字段',
  `html_content` longtext  NULL COMMENT 'html内容',
  `creater_id` varchar(36)  NULL DEFAULT NULL,
  `creater_name` varchar(108)  NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '富文本表，用于存储富文本数据';

-- ----------------------------
-- Table structure for t_sys_code
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_code`;
CREATE TABLE `t_sys_code`  (
  `type` varchar(50)  NOT NULL,
  `prefix` varchar(10)  NULL DEFAULT NULL COMMENT '前缀',
  `suffix` varchar(10)  NULL DEFAULT NULL COMMENT '后缀',
  `timestamp_str` varchar(25)  NULL DEFAULT NULL COMMENT '日期',
  `code` varchar(36)  NULL DEFAULT NULL,
  PRIMARY KEY (`type`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_sys_company
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_company`;
CREATE TABLE `t_sys_company`  (
  `company_id` varchar(36)  NOT NULL DEFAULT '' COMMENT '主键',
  `code` varchar(100)  NULL DEFAULT NULL COMMENT '编码',
  `name` varchar(250)  NULL DEFAULT NULL COMMENT '名称',
  `description` varchar(500)  NULL DEFAULT NULL COMMENT '描述',
  `leader` varchar(100)  NULL DEFAULT NULL COMMENT '公司负责人',
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建用户id',
  `creater_name` varchar(200)  NULL DEFAULT NULL COMMENT '创建用户名称',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '最后更新用户id',
  `modify_name` varchar(200)  NULL DEFAULT NULL COMMENT '最后更新用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`company_id`) USING BTREE
) ENGINE = InnoDB COMMENT = '子公司表';

-- ----------------------------
-- Table structure for t_sys_data_privilege
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_data_privilege`;
CREATE TABLE `t_sys_data_privilege`  (
  `data_privilege_id` varchar(36)  NOT NULL DEFAULT '' COMMENT '主键',
  `table_name` varchar(100)  NULL DEFAULT NULL COMMENT '表名',
  `rule` varchar(2000)  NULL DEFAULT NULL COMMENT '名称',
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建用户id',
  `creater_name` varchar(200)  NULL DEFAULT NULL COMMENT '创建用户名称',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '最后更新用户id',
  `modify_name` varchar(200)  NULL DEFAULT NULL COMMENT '最后更新用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`data_privilege_id`) USING BTREE
) ENGINE = InnoDB COMMENT = '数据权限表';

-- ----------------------------
-- Table structure for t_sys_department
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_department`;
CREATE TABLE `t_sys_department`  (
  `department_id` varchar(36)  NOT NULL DEFAULT '' COMMENT '主键',
  `code` varchar(100)  NULL DEFAULT NULL COMMENT '编码',
  `name` varchar(250)  NULL DEFAULT NULL COMMENT '名称',
  `description` varchar(500)  NULL DEFAULT NULL COMMENT '描述',
  `leader` varchar(100)  NULL DEFAULT NULL COMMENT '部门负责人',
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建用户id',
  `creater_name` varchar(200)  NULL DEFAULT NULL COMMENT '创建用户名称',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '最后更新用户id',
  `modify_name` varchar(200)  NULL DEFAULT NULL COMMENT '最后更新用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`department_id`) USING BTREE
) ENGINE = InnoDB COMMENT = '部门表';

-- ----------------------------
-- Table structure for t_sys_import_export
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_import_export`;
CREATE TABLE `t_sys_import_export`  (
  `id` varchar(36)  NOT NULL DEFAULT '',
  `module_description` varchar(100)  NULL DEFAULT NULL COMMENT '模块名称',
  `description` varchar(200)  NULL DEFAULT NULL COMMENT '描述',
  `file_name` varchar(100)  NULL DEFAULT NULL COMMENT '文件名',
  `file_size` varchar(50)  NULL DEFAULT NULL COMMENT '文件大小，KB',
  `file_type` varchar(50)  NULL DEFAULT NULL COMMENT '文件类型',
  `file_dir` varchar(300)  NULL DEFAULT NULL COMMENT '文件所在目录',
  `operation_type` varchar(255)  NULL DEFAULT NULL COMMENT '操作类型：1是导入，2是导出',
  `status` varchar(100)  NULL DEFAULT NULL COMMENT '当前状态：参考t_sys_lookup表的importExportState属性',
  `batch_no` varchar(100)  NULL DEFAULT NULL COMMENT '批次号',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `creater_id` varchar(36)  NULL DEFAULT NULL,
  `creater_name` varchar(200)  NULL DEFAULT NULL,
  `upload_file_id` varchar(36)  NULL DEFAULT NULL,
  `upload_message_id` varchar(36)  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_batch_no`(`batch_no`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_sys_lookup
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_lookup`;
CREATE TABLE `t_sys_lookup`  (
  `lookup_id` varchar(36)  NOT NULL DEFAULT '' COMMENT '主键',
  `code` varchar(100)  NULL DEFAULT NULL COMMENT '属性编码',
  `name` varchar(200)  NULL DEFAULT NULL COMMENT '属性名称',
  `parent_code` varchar(100)  NULL DEFAULT NULL COMMENT '父级编码',
  `lan` varchar(20)  NULL DEFAULT 'zh_CN' COMMENT '语言，默认为zh_CN',
  `description` varchar(500)  NULL DEFAULT NULL COMMENT '属性描述',
  `status` varchar(10)  NULL DEFAULT '0' COMMENT '状态，enable是启用，disable是禁用',
  `item_order` tinyint(4) NULL DEFAULT 0,
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建用户id',
  `project_code` varchar(36)  NULL DEFAULT NULL COMMENT '项目编码',
  `project_name` varchar(200)  NULL DEFAULT NULL COMMENT '项目名称',
  `creater_name` varchar(200)  NULL DEFAULT NULL COMMENT '创建用户名称',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '最后更新用户id',
  `modify_name` varchar(200)  NULL DEFAULT NULL COMMENT '最后更新用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`lookup_id`) USING BTREE,
  UNIQUE INDEX `idx_code`(`code`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_sys_lookup_item
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_lookup_item`;
CREATE TABLE `t_sys_lookup_item`  (
  `lookup_item_id` varchar(36)  NOT NULL DEFAULT '' COMMENT '主键',
  `lookup_id` varchar(36)  NOT NULL DEFAULT '' COMMENT '主表主键',
  `code` varchar(100)  NULL DEFAULT NULL COMMENT '属性编码',
  `name` varchar(200)  NULL DEFAULT NULL COMMENT '属性名称',
  `parent_code` varchar(100)  NULL DEFAULT NULL COMMENT '父级编码',
  `lan` varchar(20)  NULL DEFAULT 'zh_CN' COMMENT '语言，默认为zh_CN',
  `description` varchar(500)  NULL DEFAULT NULL COMMENT '属性描述',
  `status` varchar(10)  NULL DEFAULT '0' COMMENT '状态，enable是启用，disable是禁用',
  `item_order` tinyint(4) NULL DEFAULT 0,
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建用户id',
  `creater_name` varchar(200)  NULL DEFAULT NULL COMMENT '创建用户名称',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '最后更新用户id',
  `modify_name` varchar(200)  NULL DEFAULT NULL COMMENT '最后更新用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`lookup_item_id`) USING BTREE,
  UNIQUE INDEX `idx_code`(`code`, `parent_code`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_menu`;
CREATE TABLE `t_sys_menu`  (
  `menu_id` varchar(36)  NOT NULL COMMENT '菜单id',
  `menu_name` varchar(100)  NULL DEFAULT NULL COMMENT '菜单名',
  `menu_desc` varchar(100)  NULL DEFAULT NULL COMMENT '菜单描述',
  `menu_url` varchar(500)  NULL DEFAULT NULL COMMENT '菜单url , 指定菜单跳转的url',
  `menu_permission` varchar(200)  NULL DEFAULT NULL COMMENT '菜单权限 , 该菜单的访问权限',
  `menu_level` tinyint(1) NULL DEFAULT NULL COMMENT '菜单层级 , 在同一个pid下 , 值越小层级越高',
  `menu_order` tinyint(1) NULL DEFAULT NULL COMMENT '菜单排序 , 在同一个pid下可重新排序',
  `menu_img` varchar(100)  NULL DEFAULT NULL COMMENT '菜单对应的图片',
  `pid` varchar(36)  NULL DEFAULT NULL COMMENT '父级菜单id , 顶级菜单父id为空',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改者id',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`menu_id`) USING BTREE,
  INDEX `idx_pid`(`pid`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_operation_log`;
CREATE TABLE `t_sys_operation_log`  (
  `operation_log_id` varchar(250)  NOT NULL COMMENT '必填',
  `company_id` varchar(45)  NULL DEFAULT NULL COMMENT '公司ID，非必填',
  `project_id` varchar(45)  NULL DEFAULT NULL COMMENT '非必填',
  `module_name` varchar(45)  NOT NULL COMMENT '必填',
  `business_name` varchar(45)  NOT NULL COMMENT '必填',
  `create_time` datetime(0) NOT NULL COMMENT '必填',
  `operation_role` varchar(45)  NULL DEFAULT NULL COMMENT '操作角色，必填',
  `operation_user` varchar(45)  NOT NULL COMMENT '必填',
  `operation_data_id` varchar(45)  NULL DEFAULT NULL COMMENT '操作数据ID，非必填',
  `operation_date` datetime(0) NULL DEFAULT NULL COMMENT '非必填',
  `operation_type` varchar(45)  NOT NULL COMMENT '必填',
  `operation_ip` varchar(45)  NULL DEFAULT NULL COMMENT '非必填',
  `log_source_type` varchar(45)  NULL DEFAULT NULL COMMENT '日志来源，必填',
  PRIMARY KEY (`operation_log_id`) USING BTREE,
  INDEX `idx_company_id`(`company_id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_sys_organization
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_organization`;
CREATE TABLE `t_sys_organization`  (
  `organization_id` varchar(36)  NOT NULL DEFAULT '' COMMENT '主键',
  `type` varchar(100)  NULL DEFAULT NULL COMMENT '组织类型，有公司，部门，项目，岗位，员工',
  `code` varchar(100)  NULL DEFAULT NULL COMMENT '公司，部门，项目，岗位，员工的编码',
  `pid` varchar(250)  NULL DEFAULT NULL COMMENT '公司，部门，项目，岗位，员工的父编码',
  `description` varchar(500)  NULL DEFAULT NULL COMMENT '描述',
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建用户id',
  `creater_name` varchar(200)  NULL DEFAULT NULL COMMENT '创建用户名称',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '最后更新用户id',
  `modify_name` varchar(200)  NULL DEFAULT NULL COMMENT '最后更新用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`organization_id`) USING BTREE,
  UNIQUE INDEX `uk_code_type_pid`(`type`, `code`, `pid`) USING BTREE
) ENGINE = InnoDB COMMENT = '组织表，是子公司，部门，项目，岗位和员工的关系表';

-- ----------------------------
-- Table structure for t_sys_project
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_project`;
CREATE TABLE `t_sys_project`  (
  `project_id` varchar(36)  NOT NULL COMMENT '主键id',
  `code` varchar(10)  NULL DEFAULT NULL COMMENT '编码',
  `name` varchar(100)  NULL DEFAULT NULL COMMENT '名称',
  `description` varchar(200)  NULL DEFAULT NULL COMMENT '描述',
  `address` varchar(200)  NULL DEFAULT NULL COMMENT '项目地址',
  `status` varchar(100)  NULL DEFAULT NULL COMMENT '状态；参考t_sys_lookup表的enableDisable属性',
  `leader` varchar(100)  NULL DEFAULT NULL COMMENT '项目经理',
  `bill_status` tinyint(1) NULL DEFAULT 0 COMMENT '账单自动生成开关: 0: 开启 , 1: 关闭',
  `zip_code` varchar(10)  NULL DEFAULT NULL COMMENT '邮编号码',
  `province` varchar(50)  NULL DEFAULT NULL COMMENT '省',
  `city` varchar(50)  NULL DEFAULT NULL COMMENT '市',
  `area` varchar(50)  NULL DEFAULT NULL COMMENT '区/县',
  `streets` varchar(100)  NULL DEFAULT NULL COMMENT '街道',
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人id',
  `creater_name` varchar(200)  NULL DEFAULT NULL COMMENT '创建用户名称',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '最后更新用户id',
  `modify_name` varchar(200)  NULL DEFAULT NULL COMMENT '最后更新用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`project_id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_resource`;
CREATE TABLE `t_sys_resource`  (
  `src_id` varchar(36)  NOT NULL COMMENT '资源id',
  `menu_id` varchar(36)  NULL DEFAULT NULL COMMENT '菜单id',
  `src_name` varchar(36)  NULL DEFAULT NULL COMMENT '资源名,按钮名',
  `src_desc` varchar(100)  NULL DEFAULT NULL COMMENT '资源描述',
  `src_permission` varchar(200)  NULL DEFAULT NULL COMMENT '权限标识',
  `src_url` varchar(500)  NULL DEFAULT NULL COMMENT '权限url',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改者id',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`src_id`) USING BTREE,
  INDEX `idx_menu_id`(`menu_id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role`;
CREATE TABLE `t_sys_role`  (
  `role_id` varchar(36)  NOT NULL COMMENT '角色ID',
  `code` varchar(100)  NULL DEFAULT NULL COMMENT '编码',
  `role_name` varchar(36)  NULL DEFAULT NULL COMMENT '角色名',
  `role_desc` varchar(100)  NULL DEFAULT NULL COMMENT '角色描述',
  `status` varchar(100)  NULL DEFAULT NULL COMMENT '状态；参考t_sys_lookup表的enableDisable属性',
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建者id',
  `creater_name` varchar(200)  NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改者id',
  `modify_name` varchar(200)  NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_sys_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role_resource`;
CREATE TABLE `t_sys_role_resource`  (
  `id` varchar(36)  NOT NULL COMMENT '主键id',
  `role_id` varchar(36)  NULL DEFAULT NULL COMMENT '角色id',
  `src_id` varchar(36)  NULL DEFAULT NULL COMMENT '菜单资源id , 当菜单资源类型为m时 , 指向菜单id当菜单资源类型为r时 , 指向资源id',
  `src_type` char(1)  NULL DEFAULT NULL COMMENT '菜单资源类型 , 当值为m时, 指向菜单表当值为r时 , 指向按钮资源表',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user`;
CREATE TABLE `t_sys_user`  (
  `user_id` varchar(36)  NOT NULL COMMENT '主键id',
  `staff_number` varchar(36)  NULL DEFAULT NULL COMMENT '工号',
  `staff_name` varchar(36)  NULL DEFAULT NULL COMMENT '员工姓名',
  `document_type` varchar(100)  NULL DEFAULT NULL COMMENT '证件类型',
  `document_number` varchar(100)  NULL DEFAULT NULL COMMENT '证件号码',
  `sex` varchar(10)  NULL DEFAULT NULL COMMENT '性别',
  `email` varchar(50)  NULL DEFAULT NULL COMMENT '邮箱',
  `image` varchar(100)  NULL DEFAULT NULL COMMENT '头像key',
  `mobile_telephone` varchar(20)  NULL DEFAULT NULL COMMENT '移动电话',
  `join_date` datetime(0) NULL DEFAULT NULL COMMENT '入职时间',
  `login_name` varchar(64)  NULL DEFAULT NULL COMMENT '员工登录名',
  `password` varchar(36)  NULL DEFAULT NULL COMMENT '用户密码',
  `staff_state` varchar(100)  NULL DEFAULT NULL COMMENT '员工状态',
  `status` varchar(100)  NULL DEFAULT NULL COMMENT '状态；参考t_sys_lookup表的enableDisable属性',
  `company_id` varchar(36)  NULL DEFAULT NULL COMMENT '默认公司',
  `type` varchar(100)  NULL DEFAULT NULL COMMENT '员工类型，超级管理员，普通用户',
  `file_ids` varchar(500)  NULL DEFAULT NULL COMMENT '附件id集合',
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人id',
  `creater_name` varchar(200)  NULL DEFAULT NULL COMMENT '创建用户名称',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '最后更新用户id',
  `modify_name` varchar(200)  NULL DEFAULT NULL COMMENT '最后更新用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `uk_staff_number`(`staff_number`) USING BTREE,
  UNIQUE INDEX `uk_login_name`(`login_name`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_vehicle
-- ----------------------------
DROP TABLE IF EXISTS `t_vehicle`;
CREATE TABLE `t_vehicle`  (
  `id` varchar(36)  NOT NULL COMMENT '主键',
  `project_id` varchar(50)  NULL DEFAULT NULL COMMENT '项目编码',
  `building_code` varchar(100)  NULL DEFAULT NULL COMMENT '建筑编号',
  `vehicle_number` varchar(50)  NULL DEFAULT NULL COMMENT '车牌号',
  `vehicle_license` varchar(50)  NULL DEFAULT NULL COMMENT '行驶证号',
  `attained_vehicle_license_date` date NULL DEFAULT NULL COMMENT '行驶证发放日期',
  `holder` varchar(100)  NULL DEFAULT NULL COMMENT '所有人，来自客户',
  `holder_type` varchar(36)  NULL DEFAULT NULL COMMENT '所有人类型：租客、业主',
  `brand` varchar(100)  NULL DEFAULT NULL COMMENT '车辆品牌',
  `vehicle_type` varchar(100)  NULL DEFAULT NULL COMMENT '车辆类型',
  `engine_no` varchar(36)  NULL DEFAULT NULL COMMENT '发动机号码',
  `vehicle_register_date` date NULL DEFAULT NULL COMMENT '车辆登记日期',
  `vehicle_register_person` varchar(50)  NULL DEFAULT NULL COMMENT '车辆登记人',
  `vehicle_color` varchar(100)  NULL DEFAULT NULL COMMENT '车辆颜色',
  `can_carry_passengers_number` tinyint(1) NULL DEFAULT NULL COMMENT '核定载客量',
  `model_number` varchar(100)  NULL DEFAULT NULL COMMENT '车辆型号',
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建用户id',
  `creater_name` varchar(200)  NULL DEFAULT NULL COMMENT '创建用户名称',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '最后更新用户id',
  `modify_name` varchar(200)  NULL DEFAULT NULL COMMENT '最后更新用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '车辆管理';

-- ----------------------------
-- Table structure for t_whitelist
-- ----------------------------
DROP TABLE IF EXISTS `t_whitelist`;
CREATE TABLE `t_whitelist`  (
  `id` varchar(36)  NOT NULL,
  `user_id` varchar(36)  NOT NULL COMMENT '白名单人员id',
  `gating_code` varchar(36)  NOT NULL COMMENT '白名单人员对应门控机code',
  `company_id` varchar(36)  NOT NULL COMMENT '门控机所在公司',
  `project_id` varchar(36)  NOT NULL COMMENT '门控机所在项目',
  `create_by` varchar(36)  NOT NULL COMMENT '新建白名单人员id',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tb_building_gate
-- ----------------------------
DROP TABLE IF EXISTS `tb_building_gate`;
CREATE TABLE `tb_building_gate`  (
  `id` varchar(36) NOT NULL,
  `building_id` varchar(36) NULL DEFAULT NULL COMMENT '房屋id 关联Builidng_id',
  `gate_id` varchar(36) NULL DEFAULT NULL COMMENT '门控机id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_building_id`(`building_id`) USING BTREE,
  INDEX `idx_gate_id`(`gate_id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tb_gating
-- ----------------------------
DROP TABLE IF EXISTS `tb_gating`;
CREATE TABLE `tb_gating`  (
  `id` varchar(36)  NOT NULL COMMENT 'id',
  `equipment_num` varchar(100)  NULL DEFAULT NULL COMMENT '设备id',
  `gating_code` varchar(36) NULL DEFAULT NULL COMMENT '门控机code',
  `account_name` varchar(36)  NULL DEFAULT NULL COMMENT '门控机账号',
  `equipment_name` varchar(50)  NULL DEFAULT NULL COMMENT '设备名称',
  `equipment_model` varchar(50)  NULL DEFAULT NULL COMMENT '设备型号',
  `equipment_sn` varchar(50)  NULL DEFAULT NULL COMMENT '设备sn',
  `batch_nummer` varchar(50)  NULL DEFAULT NULL COMMENT '批次编号',
  `Manufacture_date` date NULL DEFAULT NULL COMMENT '出厂日期',
  `quality_time_start` date NULL DEFAULT NULL COMMENT '质保开始日期',
  `scrap_time` date NULL DEFAULT NULL COMMENT '报废日期',
  `scrap_cause` varchar(200)  NULL DEFAULT NULL COMMENT '报废原因',
  `quality_term` int(11) NULL DEFAULT NULL COMMENT '质保年限',
  `employ_term` int(11) NULL DEFAULT NULL COMMENT '使用年限',
  `facility_state` varchar(20) NULL DEFAULT NULL COMMENT '当前门控机的状态（未销售，已销售，正在使用，维修中，报废）',
  `market_id` varchar(36)  NULL DEFAULT NULL COMMENT '销售合同ID',
  `purchase_id` varchar(36)  NULL DEFAULT NULL COMMENT '采购订单ID',
  `production_firm` varchar(50)  NULL DEFAULT NULL COMMENT '生产厂商',
  `production_site` varchar(100)  NULL DEFAULT NULL COMMENT '生产地址',
  `district` varchar(100)  NULL DEFAULT NULL COMMENT '位置',
  `two_dimension_code` varchar(2000)  NULL DEFAULT NULL COMMENT '二维码',
  `open_gating_state` tinyint(1) NULL DEFAULT 0 COMMENT '门控机开门状态（1开门2关门）',
  `is_wall_gating` tinyint(4) NULL DEFAULT 0 COMMENT '是否为围墙机(0不是1是)',
  `employ_project` varchar(30)  NULL DEFAULT NULL COMMENT '所属项目名',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `company_id` varchar(36)  NULL DEFAULT NULL COMMENT '公司id',
  `videos_state` tinyint(1) NULL DEFAULT 0 COMMENT '0',
  `online_state` tinyint(1) NULL DEFAULT 0,
  `version` varchar(16)  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tb_gating_copy
-- ----------------------------
DROP TABLE IF EXISTS `tb_gating_copy`;
CREATE TABLE `tb_gating_copy`  (
  `id` varchar(36)  NOT NULL COMMENT 'id',
  `equipment_num` varchar(100)  NULL DEFAULT NULL COMMENT '设备id',
  `gating_code` varchar(36)  NULL DEFAULT NULL COMMENT '门控机code',
  `account_name` varchar(36)  NULL DEFAULT NULL COMMENT '门控机账号',
  `equipment_name` varchar(50)  NULL DEFAULT NULL COMMENT '设备名称',
  `equipment_model` varchar(50)  NULL DEFAULT NULL COMMENT '设备型号',
  `equipment_sn` varchar(50)  NULL DEFAULT NULL COMMENT '设备sn',
  `batch_nummer` varchar(50)  NULL DEFAULT NULL COMMENT '批次编号',
  `Manufacture_date` date NULL DEFAULT NULL COMMENT '出厂日期',
  `quality_time_start` date NULL DEFAULT NULL COMMENT '质保开始日期',
  `scrap_time` date NULL DEFAULT NULL COMMENT '报废日期',
  `scrap_cause` varchar(200)  NULL DEFAULT NULL COMMENT '报废原因',
  `quality_term` int(11) NULL DEFAULT NULL COMMENT '质保年限',
  `employ_term` int(11) NULL DEFAULT NULL COMMENT '使用年限',
  `facility_state` varchar(20)  NULL DEFAULT NULL COMMENT '当前门控机的状态（未销售，已销售，正在使用，维修中，报废）',
  `market_id` varchar(36)  NULL DEFAULT NULL COMMENT '销售合同ID',
  `purchase_id` varchar(36)  NULL DEFAULT NULL COMMENT '采购订单ID',
  `production_firm` varchar(50)  NULL DEFAULT NULL COMMENT '生产厂商',
  `production_site` varchar(100)  NULL DEFAULT NULL COMMENT '生产地址',
  `district` varchar(100)  NULL DEFAULT NULL COMMENT '位置',
  `two_dimension_code` varchar(2000)  NULL DEFAULT NULL COMMENT '二维码',
  `open_gating_state` tinyint(1) NULL DEFAULT 0 COMMENT '门控机开门状态（1开门2关门）',
  `is_wall_gating` tinyint(4) NULL DEFAULT 0 COMMENT '是否为围墙机(0不是1是)',
  `employ_project` varchar(30)  NULL DEFAULT NULL COMMENT '所属项目名',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `company_id` varchar(36)  NULL DEFAULT NULL COMMENT '公司id',
  `videos_state` tinyint(1) NULL DEFAULT 0 COMMENT '0',
  `online_state` tinyint(1) NULL DEFAULT 0,
  `version` varchar(16)  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_agent_code
-- ----------------------------
DROP TABLE IF EXISTS `tc_agent_code`;
CREATE TABLE `tc_agent_code`  (
  `agent_code_id` varchar(36)  NOT NULL,
  `agent_code` varchar(50)  NULL DEFAULT NULL COMMENT '坐席号',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`agent_code_id`) USING BTREE
) ENGINE = InnoDB ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for tc_agent_code_and_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `tc_agent_code_and_sys_user`;
CREATE TABLE `tc_agent_code_and_sys_user`  (
  `id` varchar(36)  NOT NULL,
  `agent_code_id` varchar(36)  NULL DEFAULT NULL,
  `sys_user_id` varchar(36)  NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for tc_building
-- ----------------------------
DROP TABLE IF EXISTS `tc_building`;
CREATE TABLE `tc_building`  (
  `id` varchar(36)  NOT NULL COMMENT '主键',
  `building_code` varchar(200)  NULL DEFAULT NULL COMMENT '建筑编号',
  `pid` varchar(200)  NULL DEFAULT NULL COMMENT '父ID',
  `building_name` varchar(50)  NULL DEFAULT NULL COMMENT '节点名称',
  `building_full_name` varchar(200)  NULL DEFAULT NULL COMMENT '节点全名称',
  `project_id` varchar(36)  NULL DEFAULT NULL,
  `building_type` varchar(36)  NULL DEFAULT NULL COMMENT '节点类型',
  `is_charge_obj` varchar(100)  NULL DEFAULT NULL COMMENT '是否计费',
  `password` varchar(15)  NULL DEFAULT NULL COMMENT '开门密码',
  `building_area` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '建筑总面积',
  `usable_area` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '套内面积',
  `share_area` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '公摊面积',
  `finish_date` date NULL DEFAULT NULL COMMENT '竣工日期',
  `floor_area` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '用地总面积',
  `house_code` varchar(50)  NULL DEFAULT NULL COMMENT '房屋编码',
  `house_code_new` varchar(25)  NULL DEFAULT NULL COMMENT '新房屋编码',
  `property_name` varchar(100)  NULL DEFAULT NULL COMMENT '资产名称，如商铺名称等',
  `property_addr` varchar(225)  NULL DEFAULT NULL COMMENT '资产地址',
  `company_id` varchar(36)  NULL DEFAULT NULL COMMENT '公司编码',
  `join_flag` varchar(10)  NULL DEFAULT NULL COMMENT '如何标识：已入伙，未入伙',
  `join_date` date NULL DEFAULT NULL COMMENT '入伙日期',
  `unit_wy_price` DECIMAL(8,2) NULL DEFAULT 0 COMMENT '物业费单价',
  `unit_bt_price` DECIMAL(8,2) NULL DEFAULT 0 COMMENT '本体基金单价',
  `property_right_type` varchar(20)  NULL DEFAULT NULL COMMENT '产权类型：如红本，蓝本，小产权',
  `property_attributes` varchar(100)  NULL DEFAULT NULL COMMENT '产权属性',
  `property_right_flag` varchar(4)  NULL DEFAULT 'No' COMMENT '是否拥有产权标识:Yes,No',
  `building_certificate` varchar(100)  NULL DEFAULT NULL COMMENT '房产证号',
  `asset_attributes` varchar(100)  NULL DEFAULT NULL COMMENT '资产属性，类型',
  `market_state` varchar(100)  NULL DEFAULT NULL COMMENT '销售状态',
  `building_height` varchar(100)  NULL DEFAULT NULL COMMENT '建筑高度',
  `discounts` varchar(20)  NULL DEFAULT NULL COMMENT '折扣，未入住，按百分百',
  `parking_space_type` varchar(100)  NULL DEFAULT NULL COMMENT '车位类型',
  `is_standard_building` varchar(5)  NULL DEFAULT 'Yes' COMMENT '是否是标准建筑结构',
  `asset_usage` varchar(100)  NULL DEFAULT NULL COMMENT '使用方式',
  `place_attribute` varchar(100)  NULL DEFAULT NULL COMMENT '位置属性',
  `bill_address` varchar(100)  NULL DEFAULT NULL COMMENT '账单地址',
  `associated_parking_spaces` varchar(100)  NULL DEFAULT NULL COMMENT '关联车位',
  `is_fixed_parking_spaces` varchar(8)  NULL DEFAULT '是否固定车位',
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建用户id',
  `creater_name` varchar(200)  NULL DEFAULT NULL COMMENT '创建用户名称',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '最后更新用户id',
  `modify_name` varchar(200)  NULL DEFAULT NULL COMMENT '最后更新用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_pid`(`pid`) USING BTREE,
  UNIQUE INDEX `idx_building_code`(`building_code`) USING BTREE,
  UNIQUE INDEX `idx_building_full_name`(`building_full_name`) USING BTREE,
  UNIQUE INDEX `idx_house_code_new`(`house_code_new`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_car
-- ----------------------------
DROP TABLE IF EXISTS `tc_car`;
CREATE TABLE `tc_car`  (
  `housecode` varchar(255)  NULL DEFAULT NULL,
  `name` varchar(255)  NULL DEFAULT NULL
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_construction
-- ----------------------------
DROP TABLE IF EXISTS `tc_construction`;
CREATE TABLE `tc_construction`  (
  `id` varchar(36)  NOT NULL,
  `construction_code` varchar(200)  NULL DEFAULT NULL COMMENT '施工编码',
  `construction_addr` varchar(225)  NULL DEFAULT NULL COMMENT '施工地址',
  `start_date` date NULL DEFAULT NULL COMMENT '开工日期',
  `engineering_name` varchar(36)  NULL DEFAULT NULL COMMENT '工程名称',
  `engineering_cycle` varchar(36)  NULL DEFAULT '周' COMMENT '工程周期:年,月,周',
  `completion_date` date NULL DEFAULT NULL COMMENT '竣工日期',
  `engineering_unit` varchar(36)  NULL DEFAULT NULL COMMENT '工程单位',
  `owner_of_property` varchar(36)  NULL DEFAULT NULL COMMENT '物业负责人',
  `engineering_director` varchar(36)  NULL DEFAULT NULL COMMENT '工程负责人',
  `water_use` varchar(5)  NULL DEFAULT '否' COMMENT '是否用水:是,否',
  `electricity_use` varchar(5)  NULL DEFAULT '否' COMMENT '是否用电:是,否',
  `electric_power` varchar(5)  NULL DEFAULT '否' COMMENT '是否已供电:是,否',
  `water_power` varchar(5)  NULL DEFAULT '否' COMMENT '是否已供水:是,否',
  `billing_status` varchar(5)  NULL DEFAULT '开始' COMMENT '计费状态:开始,结束',
  `construction_state` varchar(5)  NULL DEFAULT NULL COMMENT '状态:施工中,未施工,暂停,完工',
  `state` varchar(1)  NULL DEFAULT NULL,
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目ID',
  `company_id` varchar(36)  NULL DEFAULT NULL COMMENT '公司ID',
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `creater_name` varchar(36)  NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改人ID',
  `modify_name` varchar(36)  NULL DEFAULT NULL COMMENT '修改人姓名',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_electlog
-- ----------------------------
DROP TABLE IF EXISTS `tc_electlog`;
CREATE TABLE `tc_electlog`  (
  `id` varchar(36)  NOT NULL,
  `elect_id` varchar(36)  NULL DEFAULT NULL,
  `operator` varchar(36)  NULL DEFAULT NULL,
  `modifytime` datetime(0) NULL DEFAULT NULL,
  `modifytype` varchar(36)  NULL DEFAULT NULL,
  `modifymatter` varchar(200)  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_electricity_meter
-- ----------------------------
DROP TABLE IF EXISTS `tc_electricity_meter`;
CREATE TABLE `tc_electricity_meter`  (
  `id` varchar(36)  NOT NULL COMMENT '主键id',
  `code` varchar(36)  NOT NULL COMMENT '电表编号',
  `position` varchar(200)  NOT NULL COMMENT '安装位置编码',
  `relation_building` varchar(200)  NULL DEFAULT NULL COMMENT '关联收费对象编码',
  `company_code` varchar(50)  NULL DEFAULT NULL COMMENT '公司编码',
  `init_amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '初始读数',
  `min_amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '最小读数',
  `max_amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '最大读数',
  `electricitymeter_type` tinyint(1) NULL DEFAULT 0 COMMENT '电表类型 , 0 普通表 , 1分时表',
  `is_circle` tinyint(1) NULL DEFAULT 0 COMMENT '是否循环使用 , 0 是 , 1不是',
  `state` tinyint(1) NULL DEFAULT 0 COMMENT '电表表状态, 0 启用 , 1停用',
  `type` tinyint(1) NULL DEFAULT 0 COMMENT '抄表方式: 0 室内 , 1 室外 , 2 远程',
  `parent_code` varchar(36)  NULL DEFAULT NULL COMMENT '父表编号',
  `master_code` varchar(36)  NULL DEFAULT NULL COMMENT '主表编号',
  `user_type` tinyint(1) NULL DEFAULT 0 COMMENT '使用性质: 0 商用, 1 民用 , 2 管理处',
  `is_billing` tinyint(1) NULL DEFAULT 0 COMMENT '是否计费: 0 是, 1 不是',
  `is_public` tinyint(1) NULL DEFAULT 0 COMMENT '是否公用: 0 是, 1 不是',
  `asset_no` varchar(36)  NULL DEFAULT NULL COMMENT '资产编号',
  `brand` varchar(36)  NULL DEFAULT NULL COMMENT '品牌',
  `specs` varchar(50)  NULL DEFAULT NULL COMMENT '规格',
  `provider` varchar(100)  NULL DEFAULT NULL COMMENT '供应商',
  `provider_phone` varchar(15)  NULL DEFAULT NULL COMMENT '供应商联系电话',
  `assemble_person` varchar(36)  NULL DEFAULT NULL COMMENT '安装人',
  `assemble_time` date NULL DEFAULT NULL COMMENT '安装时间',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `init_peak_value` float(16, 2) NULL DEFAULT 0.00 COMMENT '初始峰值读数',
  `max_peak_value` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '峰值最大读数',
  `init_valley_value` float(16, 2) NULL DEFAULT 0.00 COMMENT '初始谷值读数',
  `max_valley_value` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '谷值最大读数',
  `init_average_value` float(16, 2) NULL DEFAULT 0.00 COMMENT '初始平均值读数',
  `max_average_value` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '平值最大读数',
  `electricitymeter_name` varchar(100)  NULL DEFAULT NULL COMMENT '电表名称',
  `rate` float(10, 2) NULL DEFAULT 1.00 COMMENT '倍率',
  `relation_building_name` varchar(200)  NULL DEFAULT NULL COMMENT '收费对象名称',
  `position_name` varchar(200)  NULL DEFAULT NULL COMMENT '电表位置名称',
  `assemble_person_id` varchar(60)  NULL DEFAULT NULL COMMENT '安装人编码',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目编码',
  `project_name` varchar(255)  NULL DEFAULT NULL COMMENT '项目名称',
  `meter_level` tinyint(4) NULL DEFAULT NULL COMMENT '电表级别（1，一级表，2，二级表...）',
  `new_coding` varchar(30)  NULL DEFAULT NULL COMMENT '新编码',
  `external_coding` varchar(30)  NULL DEFAULT NULL COMMENT '外部码 ',
  `describe_num` varchar(30)  NULL DEFAULT NULL COMMENT '描述 ',
  `group_table_coding` varchar(30)  NULL DEFAULT NULL COMMENT '组表编码',
  `group_table_name` varchar(30)  NULL DEFAULT NULL COMMENT '组表名称 ',
  `is_share` varchar(30)  NULL DEFAULT NULL COMMENT '是否分摊',
  `check_table` varchar(30)  NULL DEFAULT NULL COMMENT '是否校验表',
  `elect_state` varchar(30)  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_position`(`position`) USING BTREE,
  INDEX `idx_relation_building`(`relation_building`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_engineeringlog
-- ----------------------------
DROP TABLE IF EXISTS `tc_engineeringlog`;
CREATE TABLE `tc_engineeringlog`  (
  `id` varchar(36)  NOT NULL,
  `building_id` varchar(36)  NULL DEFAULT NULL,
  `operator` varchar(36)  NULL DEFAULT NULL,
  `modifytime` datetime(0) NULL DEFAULT NULL,
  `modifytype` varchar(36)  NULL DEFAULT NULL,
  `modifymatter` varchar(200)  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_enterprise_cust
-- ----------------------------
DROP TABLE IF EXISTS `tc_enterprise_cust`;
CREATE TABLE `tc_enterprise_cust`  (
  `enterprise_id` varchar(36)  NOT NULL COMMENT '企业id',
  `enterprise_name` varchar(30)  NULL DEFAULT NULL COMMENT '企业名称',
  `address` varchar(80)  NULL DEFAULT NULL COMMENT '注册地址',
  `representative` varchar(50)  NULL DEFAULT NULL COMMENT '法定代表人',
  `office_phone` varchar(30)  NULL DEFAULT NULL COMMENT '办公电话',
  `fax_number` varchar(50)  NULL DEFAULT NULL COMMENT '传真',
  `enterprise_url` varchar(80)  NULL DEFAULT NULL COMMENT '企业网址',
  `tax_certificate` varchar(80)  NULL DEFAULT NULL COMMENT '税务资质',
  `enterprise_property` varchar(80)  NULL DEFAULT NULL COMMENT '单位属性',
  `trading_date` date NULL DEFAULT NULL COMMENT '营业证书有效期',
  `trading_number` varchar(80)  NULL DEFAULT NULL COMMENT '营业执照号',
  `tax_number` varchar(80)  NULL DEFAULT NULL COMMENT '税务登记证号',
  `tax_date` date NULL DEFAULT NULL COMMENT '税务证书有效期',
  `manage_type` varchar(50)  NULL DEFAULT NULL COMMENT '经营类型',
  `unit_number` varchar(50)  NULL DEFAULT NULL COMMENT '单位编号',
  `e_mail` varchar(60)  NULL DEFAULT NULL COMMENT '电子邮件',
  `business_address` varchar(60)  NULL DEFAULT NULL COMMENT '办公地址',
  `principal` varchar(36)  NULL DEFAULT NULL COMMENT '单位委托人',
  `emergency_contact` varchar(30)  NULL DEFAULT NULL COMMENT '单位紧急联系人',
  `emergency_contact_phone` varchar(20)  NULL DEFAULT NULL COMMENT '紧急联系人电话',
  `organization_code` varchar(80)  NULL DEFAULT NULL COMMENT '组织机构代码',
  `register_date` date NULL DEFAULT NULL COMMENT '注册时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `company_id` varchar(36)  NULL DEFAULT NULL COMMENT '公司id',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`enterprise_id`) USING BTREE,
  INDEX `temp_index_enterprise_id`(`enterprise_id`) USING BTREE,
  INDEX `project_id`(`project_id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_enterprise_cust_bak
-- ----------------------------
DROP TABLE IF EXISTS `tc_enterprise_cust_bak`;
CREATE TABLE `tc_enterprise_cust_bak`  (
  `enterprise_id` varchar(36)  NOT NULL COMMENT '企业id',
  `enterprise_name` varchar(30)  NULL DEFAULT NULL COMMENT '企业名称',
  `address` varchar(80)  NULL DEFAULT NULL COMMENT '注册地址',
  `representative` varchar(50)  NULL DEFAULT NULL COMMENT '法定代表人',
  `office_phone` varchar(30)  NULL DEFAULT NULL COMMENT '办公电话',
  `fax_number` varchar(50)  NULL DEFAULT NULL COMMENT '传真',
  `enterprise_url` varchar(80)  NULL DEFAULT NULL COMMENT '企业网址',
  `tax_certificate` varchar(80)  NULL DEFAULT NULL COMMENT '税务资质',
  `enterprise_property` varchar(80)  NULL DEFAULT NULL COMMENT '单位属性',
  `trading_date` date NULL DEFAULT NULL COMMENT '营业证书有效期',
  `trading_number` varchar(80)  NULL DEFAULT NULL COMMENT '营业执照号',
  `tax_number` varchar(80)  NULL DEFAULT NULL COMMENT '税务登记证号',
  `tax_date` date NULL DEFAULT NULL COMMENT '税务证书有效期',
  `manage_type` varchar(50)  NULL DEFAULT NULL COMMENT '经营类型',
  `unit_number` varchar(50)  NULL DEFAULT NULL COMMENT '单位编号',
  `e_mail` varchar(60)  NULL DEFAULT NULL COMMENT '电子邮件',
  `business_address` varchar(60)  NULL DEFAULT NULL COMMENT '办公地址',
  `principal` varchar(30)  NULL DEFAULT NULL COMMENT '单位委托人',
  `emergency_contact` varchar(30)  NULL DEFAULT NULL COMMENT '单位紧急联系人',
  `emergency_contact_phone` varchar(20)  NULL DEFAULT NULL COMMENT '紧急联系人电话',
  `organization_code` varchar(80)  NULL DEFAULT NULL COMMENT '组织机构代码',
  `register_date` date NULL DEFAULT NULL COMMENT '注册时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `company_id` varchar(36)  NULL DEFAULT NULL COMMENT '公司id',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间'
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_enterprise_cust_staff
-- ----------------------------
DROP TABLE IF EXISTS `tc_enterprise_cust_staff`;
CREATE TABLE `tc_enterprise_cust_staff`  (
  `id` varchar(36)  NOT NULL COMMENT 'id',
  `name` varchar(36)  NULL DEFAULT NULL COMMENT '员工姓名',
  `cert_type` varchar(2)  NULL DEFAULT NULL COMMENT '证件类型',
  `cert_num` varchar(30)  NULL DEFAULT NULL COMMENT '证件号码',
  `phone_num` varchar(18)  NULL DEFAULT NULL COMMENT '电话号码',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `enterprise_id` varchar(36)  NULL DEFAULT NULL COMMENT '企业客户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_house
-- ----------------------------
DROP TABLE IF EXISTS `tc_house`;
CREATE TABLE `tc_house`  (
  `id` varchar(36)  NOT NULL COMMENT '主键(uuid)',
  `building_code` varchar(200)  NULL DEFAULT NULL COMMENT '建筑code',
  `name` varchar(100)  NULL DEFAULT NULL,
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建用户id',
  `creater_name` varchar(200)  NULL DEFAULT NULL COMMENT '创建用户名称',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '最后更新用户id',
  `modify_name` varchar(200)  NULL DEFAULT NULL COMMENT '最后更新用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_building_code`(`building_code`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_hydro_meter_operation_record
-- ----------------------------
DROP TABLE IF EXISTS `tc_hydro_meter_operation_record`;
CREATE TABLE `tc_hydro_meter_operation_record`  (
  `id` varchar(36)  NOT NULL COMMENT '主键id',
  `replace_before_code` varchar(36)  NOT NULL COMMENT '操作前编号',
  `replace_after_code` varchar(36)  NULL DEFAULT NULL COMMENT '操作后编号(如果是启停用则可为null)',
  `replace_before_use` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '操作前用量,如更换时,该表用量',
  `meter_type` tinyint(1) NULL DEFAULT NULL COMMENT '表类型(0:水表,1:电表)',
  `reading_before` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '操作前读数',
  `used_fair_amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '换表前的平值用量',
  `used_valley_amount` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '换表前的谷值用量',
  `used_peak_amount` DECIMAL(8,0) NULL DEFAULT 0 COMMENT '换表前的峰值用量',
  `used_amount` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '更换水电表时，旧表已经使用的水电量',
  `reading_after` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '操作后读数（如果为启停用则可为null）',
  `operation_type` tinyint(1) NULL DEFAULT NULL COMMENT '操作类型（0:更换,1:启用,2:停用）',
  `operation_reason` varchar(500)  NULL DEFAULT NULL COMMENT '操作原因',
  `operation_user` varchar(36)  NULL DEFAULT NULL COMMENT '操作人',
  `operation_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `fair_value_before` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '变更前平直读数',
  `peak_value_before` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '变更前峰值读数',
  `valley_value_before` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '变更前谷值读数',
  `fair_value_after` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '变更后平值读数',
  `peak_value_after` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '变更后峰值读数',
  `valley_value_after` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '变更后谷值读数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '水电表操作记录表';

-- ----------------------------
-- Table structure for tc_meter_data
-- ----------------------------
DROP TABLE IF EXISTS `tc_meter_data`;
CREATE TABLE `tc_meter_data`  (
  `id` varchar(36)  NOT NULL COMMENT '主键id',
  `peak_reading` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '峰值读数',
  `last_peak_reading` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '上次峰值读数',
  `vally_reading` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '谷值读数',
  `last_vally_reading` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '上次谷值读数',
  `common_reading` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '平值读数',
  `last_common_reading` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '上次平值读数',
  `total_reading` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '总读数',
  `last_total_reading` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '上月读数',
  `use_count` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '本次总用量',
  `peak_count` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '峰值用量',
  `vally_count` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '谷值用量',
  `common_count` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '平值用量',
  `meter_type` tinyint(1) NULL DEFAULT 0 COMMENT '表类型: 0为水表,1为电表',
  `meter_code` varchar(36)  NULL DEFAULT '0' COMMENT '关联到电表或者水表编号code',
  `error_reading` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '错误读数',
  `circle_correction` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '循环修正量',
  `peak_circle_correction` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '峰值循环修正量',
  `valley_circle_correction` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '谷值循环修正量',
  `average_circle_correction` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '平值循环修正量',
  `correction` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '修正量',
  `peak_correction` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '峰值修正量',
  `valley_correction` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '谷值修正量',
  `average_correction` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '平值修正量',
  `correction_person_id` varchar(36)  NULL DEFAULT '0.00' COMMENT '修正人',
  `correction_time` datetime(0) NULL DEFAULT NULL COMMENT '修正时间',
  `audit_status` tinyint(1) NULL DEFAULT 0 COMMENT '审核状态,0为待审核,1为已审核',
  `reading_person_name` varchar(36)  NULL DEFAULT NULL COMMENT '抄表人姓名',
  `reading_person_id` varchar(36)  NULL DEFAULT NULL COMMENT '抄表人',
  `reading_time` datetime(0) NULL DEFAULT NULL COMMENT '抄表时间',
  `remark` varchar(500)  NULL DEFAULT NULL COMMENT '备注',
  `task_id` varchar(36)  NULL DEFAULT NULL COMMENT '该任务所属任务编号',
  `is_used` tinyint(1) NULL DEFAULT 0 COMMENT '该数据是否有效,0:有效,1:无效',
  `before_task_id` varchar(36)  NULL DEFAULT NULL COMMENT '重读时,之前的任务id',
  `is_replaced` tinyint(1) NULL DEFAULT 0 COMMENT '该记录当前周期是否换表,0:未换,1:已还过',
  `create_id` varchar(36)  NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `modify_id` varchar(36)  NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL,
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目编码',
  `project_name` varchar(100)  NULL DEFAULT NULL COMMENT '项目名称',
  `ex_status` tinyint(1) NULL DEFAULT 1 COMMENT '异常状态: 1: 正常 , 2: 警告, 3: 错误',
  `file_id` varchar(64)  NULL DEFAULT NULL COMMENT '文件ID',
  `meter_name` varchar(200)  NULL DEFAULT NULL,
  `img_file_id` varchar(64)  NULL DEFAULT NULL,
  `temp_use_count` DECIMAL(8,2) NULL DEFAULT 0.00 COMMENT '备用用量',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_task_id`(`task_id`) USING BTREE,
  INDEX `idx_meter_code`(`meter_code`) USING BTREE
) ENGINE = InnoDB COMMENT = '抄表元数据表';

-- ----------------------------
-- Table structure for tc_meterfeeobj_relation
-- ----------------------------
DROP TABLE IF EXISTS `tc_meterfeeobj_relation`;
CREATE TABLE `tc_meterfeeobj_relation`  (
  `id` varchar(36)  NOT NULL COMMENT '主键',
  `building_code` varchar(100)  NULL DEFAULT NULL COMMENT '收费对象编码',
  `relation_id` varchar(36)  NULL DEFAULT NULL COMMENT '关联水电表以及抄表任务的 主键id',
  `type` int(2) NULL DEFAULT NULL COMMENT '表类型(0-水表；1-电表;3-任务)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_building_code`(`building_code`) USING BTREE,
  INDEX `idx_relation_id`(`relation_id`) USING BTREE
) ENGINE = InnoDB COMMENT = '水电表业务中专门记录水电表和关联收费对象的关联关系 ';

-- ----------------------------
-- Table structure for tc_operationlog
-- ----------------------------
DROP TABLE IF EXISTS `tc_operationlog`;
CREATE TABLE `tc_operationlog`  (
  `id` varchar(36)  NOT NULL,
  `building_id` varchar(36)  NULL DEFAULT NULL,
  `house_code_new` varchar(36)  NULL DEFAULT NULL,
  `building_full_name` varchar(200)  NULL DEFAULT NULL,
  `address` varchar(200)  NULL DEFAULT NULL,
  `operator` varchar(36)  NULL DEFAULT NULL,
  `modifytime` datetime(0) NULL DEFAULT NULL,
  `modifytype` varchar(36)  NULL DEFAULT NULL,
  `modifymatter` varchar(200)  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_order
-- ----------------------------
DROP TABLE IF EXISTS `tc_order`;
CREATE TABLE `tc_order`  (
  `order_id` varchar(36)  NOT NULL,
  `order_code` varchar(30)  NULL DEFAULT NULL COMMENT '工单编号',
  `cust_id` varchar(36)  NULL DEFAULT NULL COMMENT '提出申请的个人客户',
  `cust_type` varchar(36)  NULL DEFAULT NULL COMMENT '提出申请的企业客户',
  `building_code` varchar(200)  NULL DEFAULT NULL,
  `order_type_id_1` varchar(36)  NULL DEFAULT NULL COMMENT '一级工单类型',
  `order_type_id_2` varchar(36)  NULL DEFAULT NULL COMMENT '二级工单类型',
  `order_type_id_3` varchar(36)  NULL DEFAULT NULL COMMENT '三级工单类型',
  `description` text  NULL COMMENT '任务描述',
  `is_urgent` tinyint(1) NULL DEFAULT NULL COMMENT '是否加急 0=false 1=true',
  `principal_sys_user_id` varchar(36)  NULL DEFAULT NULL COMMENT '负责人',
  `need_callback` tinyint(1) NULL DEFAULT NULL COMMENT '是否回访 0=false 1=true',
  `source_type` int(10) NULL DEFAULT NULL COMMENT '1=前台 2=CC',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `status` int(5) NULL DEFAULT NULL COMMENT '1=处理中 2=完成',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `procesInstructions` text  NULL,
  `callback_content` varchar(300)  NULL DEFAULT '' COMMENT '回访信息',
  `create_by` varchar(36)  NULL DEFAULT NULL,
  `update_by` varchar(36)  NULL DEFAULT NULL,
  `finish_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_order_complaint
-- ----------------------------
DROP TABLE IF EXISTS `tc_order_complaint`;
CREATE TABLE `tc_order_complaint`  (
  `id` varchar(36)  NOT NULL COMMENT '投诉工单id',
  `order_code` varchar(10)  NULL DEFAULT NULL COMMENT '工单编号',
  `cust_name` varchar(500)  NULL DEFAULT NULL COMMENT '客户名',
  `building_code` varchar(200)  NULL DEFAULT NULL COMMENT '关联资产',
  `describer` varchar(200)  NULL DEFAULT NULL COMMENT '工单描述',
  `order_type` tinyint(2) NULL DEFAULT NULL COMMENT '工单类型,0:水电抄表  1:资产变更抄表',
  `order_type_one` tinyint(2) NULL DEFAULT NULL COMMENT '预留字段:分类一:0:水表,1:电表',
  `order_type_two` tinyint(2) NULL DEFAULT NULL COMMENT '预留字段:分类二:0:计费错误,1:抄表',
  `order_source` tinyint(2) NULL DEFAULT NULL COMMENT '工单来源: 0:客服,1:CC',
  `is_visit` tinyint(1) NULL DEFAULT NULL COMMENT '是否回访: 0:是,1:不是',
  `order_status` tinyint(1) NULL DEFAULT NULL COMMENT '处理状态: 0:未接受,1:处理中,2:已完成,3作废',
  `is_urgent` tinyint(1) NULL DEFAULT NULL COMMENT '是否紧急: 0:加急,1:不是',
  `charge_person_id` varchar(36)  NULL DEFAULT NULL COMMENT '负责人id',
  `complete_order_id` varchar(36)  NULL DEFAULT NULL COMMENT '完成工单id',
  `relation_id` varchar(36)  NULL DEFAULT NULL COMMENT '投诉工单关联外部id,如关联抄表任务id',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目编号',
  `project_name` varchar(100)  NULL DEFAULT NULL COMMENT '项目名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_building_code`(`building_code`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_order_complete
-- ----------------------------
DROP TABLE IF EXISTS `tc_order_complete`;
CREATE TABLE `tc_order_complete`  (
  `id` varchar(36)  NOT NULL COMMENT '完成工单主键id',
  `order_code` varchar(36)  NULL DEFAULT NULL COMMENT '完成工单单号',
  `complete_content` varchar(30)  NULL DEFAULT NULL COMMENT '完成内容',
  `complaint_order_id` varchar(36)  NULL DEFAULT NULL COMMENT '投诉工单id',
  `relation_id` varchar(36)  NULL DEFAULT NULL COMMENT '预留字段:完成工单需要关联其他的表主键id',
  `exception_reason` tinyint(2) NULL DEFAULT NULL COMMENT '异常原因,从数据字典中获取',
  `remark` varchar(200)  NULL DEFAULT NULL COMMENT '备注',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `complete_peak_reading` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '现场峰值读数',
  `complete_vally_reading` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '现场谷值读数',
  `complete_common_reading` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '现场平值读数',
  `is_already_billing` tinyint(2) NULL DEFAULT NULL COMMENT '主要针对产权变更的单独抄表的计费状态： 0未计费，1已经计费',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_order_type
-- ----------------------------
DROP TABLE IF EXISTS `tc_order_type`;
CREATE TABLE `tc_order_type`  (
  `tc_order_type_id` varchar(36)  NOT NULL,
  `name` varchar(20)  NULL DEFAULT NULL COMMENT '显示名称',
  `level` int(3) NULL DEFAULT NULL COMMENT '最多三级联动',
  `parent_id` varchar(36)  NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`tc_order_type_id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_person_asset
-- ----------------------------
DROP TABLE IF EXISTS `tc_person_asset`;
CREATE TABLE `tc_person_asset`  (
  `person_building_id` varchar(36)  NOT NULL,
  `cust_id` varchar(36)  NULL DEFAULT NULL,
  `enterprise_id` varchar(36)  NULL DEFAULT NULL,
  `building_id` varchar(36)  NULL DEFAULT NULL,
  `state` varchar(1)  NULL DEFAULT NULL,
  `creater_id` varchar(36)  NULL DEFAULT NULL,
  `creater_name` varchar(36)  NULL DEFAULT NULL,
  `modify_id` varchar(36)  NULL DEFAULT NULL,
  `modify_name` varchar(36)  NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`person_building_id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_person_building
-- ----------------------------
DROP TABLE IF EXISTS `tc_person_building`;
CREATE TABLE `tc_person_building`  (
  `person_building_id` varchar(36)  NOT NULL DEFAULT '',
  `cust_id` varchar(36)  NULL DEFAULT NULL COMMENT '个人客户id',
  `enterprise_id` varchar(36)  NULL DEFAULT NULL COMMENT '企业id',
  `building_id` varchar(36)  NULL DEFAULT NULL,
  `building_code` varchar(200)  NULL DEFAULT NULL COMMENT '建筑code',
  `state` tinyint(4) NULL DEFAULT 0 COMMENT '关联状态(0开启，1禁用)',
  `cust_type` varchar(30)  NULL DEFAULT NULL COMMENT '客户类型',
  `enterprise_call_type` tinyint(4) NULL DEFAULT NULL COMMENT '企业呼叫人状态',
  `accessory` varchar(36)  NULL DEFAULT NULL COMMENT '附件',
  `relation_date` date NULL DEFAULT NULL COMMENT '关联时间',
  PRIMARY KEY (`person_building_id`) USING BTREE,
  INDEX `idx_cust_id`(`cust_id`) USING BTREE,
  INDEX `idx_enterprise_id`(`enterprise_id`) USING BTREE,
  INDEX `idx_building_id`(`building_id`) USING BTREE
) ENGINE = InnoDB COMMENT = '客户资产绑定关系表';

-- ----------------------------
-- Table structure for tc_person_cust
-- ----------------------------
DROP TABLE IF EXISTS `tc_person_cust`;
CREATE TABLE `tc_person_cust`  (
  `cust_id` varchar(36)  NOT NULL COMMENT '客户id',
  `cust_code` varchar(30)  NULL DEFAULT NULL COMMENT '客户编号',
  `name` varchar(30)  NULL DEFAULT NULL COMMENT '姓名',
  `sex` varchar(4)  NULL DEFAULT NULL COMMENT '性别',
  `native_place` varchar(100)  NULL DEFAULT NULL COMMENT '籍贯',
  `census` varchar(100)  NULL DEFAULT NULL COMMENT '户口所在地',
  `birthday` date NULL DEFAULT NULL COMMENT '出生日期',
  `card_type` tinyint(4) NULL DEFAULT NULL COMMENT '证件类型',
  `card_num` varchar(36)  NULL DEFAULT NULL COMMENT '证件号码',
  `jiajia_num` varchar(30)  NULL DEFAULT NULL COMMENT '家家客户端id',
  `weixin_num` varchar(30)  NULL DEFAULT NULL COMMENT '微信帐号',
  `work_units` varchar(100)  NULL DEFAULT NULL COMMENT '工作单位',
  `marrie_state` tinyint(4) NULL DEFAULT NULL COMMENT '婚否',
  `is_enterprise` tinyint(4) NULL DEFAULT NULL COMMENT '是否有企业联系',
  `urgent_contact_person` varchar(30)  NULL DEFAULT NULL COMMENT '紧急联系人',
  `urgent_contact_phone` varchar(30)  NULL DEFAULT NULL COMMENT '紧急联系电话',
  `upload_image` varchar(10000)  NULL DEFAULT NULL COMMENT '上传图片',
  `phone_num` varchar(30)  NULL DEFAULT NULL COMMENT '电话号码',
  `register_phone` varchar(30)  NULL DEFAULT NULL COMMENT '注册电话',
  `email` varchar(30)  NULL DEFAULT NULL COMMENT '电子邮件',
  `nation` varchar(10)  NULL DEFAULT NULL COMMENT '民族',
  `remark` varchar(500)  NULL DEFAULT NULL,
  `national` varchar(200)  NULL DEFAULT NULL,
  `company_id` varchar(36)  NULL DEFAULT NULL COMMENT '公司id',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `create_id` varchar(36)  NULL DEFAULT NULL,
  `create_name` varchar(299)  NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `modify_id` varchar(36)  NULL DEFAULT NULL,
  `modify_name` varchar(200)  NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`cust_id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_person_cust_bak
-- ----------------------------
DROP TABLE IF EXISTS `tc_person_cust_bak`;
CREATE TABLE `tc_person_cust_bak`  (
  `cust_id` varchar(36)  NOT NULL COMMENT '客户id',
  `cust_code` varchar(30)  NULL DEFAULT NULL COMMENT '客户编号',
  `name` varchar(30)  NULL DEFAULT NULL COMMENT '姓名',
  `sex` varchar(4)  NULL DEFAULT NULL COMMENT '性别',
  `native_place` varchar(100)  NULL DEFAULT NULL COMMENT '籍贯',
  `census` varchar(100)  NULL DEFAULT NULL COMMENT '户口所在地',
  `birthday` date NULL DEFAULT NULL COMMENT '出生日期',
  `card_type` tinyint(4) NULL DEFAULT NULL COMMENT '证件类型',
  `card_num` varchar(36)  NULL DEFAULT NULL COMMENT '证件号码',
  `jiajia_num` varchar(30)  NULL DEFAULT NULL COMMENT '家家客户端id',
  `weixin_num` varchar(30)  NULL DEFAULT NULL COMMENT '微信帐号',
  `work_units` varchar(100)  NULL DEFAULT NULL COMMENT '工作单位',
  `marrie_state` tinyint(4) NULL DEFAULT NULL COMMENT '婚否',
  `is_enterprise` tinyint(4) NULL DEFAULT NULL COMMENT '是否有企业联系',
  `urgent_contact_person` varchar(30)  NULL DEFAULT NULL COMMENT '紧急联系人',
  `urgent_contact_phone` varchar(30)  NULL DEFAULT NULL COMMENT '紧急联系电话',
  `upload_image` varchar(10000)  NULL DEFAULT NULL COMMENT '上传图片',
  `phone_num` varchar(30)  NULL DEFAULT NULL COMMENT '电话号码',
  `register_phone` varchar(30)  NULL DEFAULT NULL COMMENT '注册电话',
  `email` varchar(30)  NULL DEFAULT NULL COMMENT '电子邮件',
  `nation` varchar(10)  NULL DEFAULT NULL COMMENT '民族',
  `remark` varchar(500)  NULL DEFAULT NULL,
  `national` varchar(200)  NULL DEFAULT NULL,
  `company_id` varchar(36)  NULL DEFAULT NULL COMMENT '公司id',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `create_id` varchar(36)  NULL DEFAULT NULL,
  `create_name` varchar(299)  NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `modify_id` varchar(36)  NULL DEFAULT NULL,
  `modify_name` varchar(200)  NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_person_cust_cert
-- ----------------------------
DROP TABLE IF EXISTS `tc_person_cust_cert`;
CREATE TABLE `tc_person_cust_cert`  (
  `id` varchar(36)  NOT NULL COMMENT 'id',
  `cust_id` varchar(36)  NULL DEFAULT NULL COMMENT '客户id',
  `cert_type` varchar(2)  NULL DEFAULT NULL COMMENT '证件类型',
  `cert_num` varchar(30)  NULL DEFAULT NULL COMMENT '证件号码',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改者',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_person_cust_phone
-- ----------------------------
DROP TABLE IF EXISTS `tc_person_cust_phone`;
CREATE TABLE `tc_person_cust_phone`  (
  `id` varchar(36)  NOT NULL COMMENT 'id',
  `cust_id` varchar(36)  NULL DEFAULT NULL COMMENT '客户id',
  `phone_num` varchar(20)  NULL DEFAULT NULL COMMENT '电话号码',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改者',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_person_emphasis
-- ----------------------------
DROP TABLE IF EXISTS `tc_person_emphasis`;
CREATE TABLE `tc_person_emphasis`  (
  `emphasis_id` varchar(36)  NOT NULL COMMENT '重点客户id',
  `username` varchar(36)  NULL DEFAULT NULL COMMENT '客户姓名username',
  `applyreason` varchar(500)  NULL DEFAULT NULL COMMENT '申请理由 applyreason',
  `applyfilepath` varchar(400)  NULL DEFAULT NULL COMMENT '上传申请的资料路径applyfilepath',
  `applydate` datetime(0) NULL DEFAULT NULL,
  `applystatus` tinyint(4) NULL DEFAULT NULL COMMENT '审核状态applystatus--0:待审核；1：审核通过；2：审核不通过',
  `idcard` varchar(36)  NULL DEFAULT NULL COMMENT '身份证号码idcard',
  `failreason` varchar(500)  NULL DEFAULT NULL COMMENT '未通过原因failreason',
  `phonenum` varchar(36)  NULL DEFAULT NULL COMMENT '联系电话phonenum',
  `enterprise_id` varchar(36)  NULL DEFAULT NULL COMMENT '企业客户id',
  `cust_id` varchar(36)  NULL DEFAULT NULL COMMENT '个人客户id',
  `applyeddate` datetime(0) NULL DEFAULT NULL,
  `approval_id` varchar(36)  NULL DEFAULT NULL COMMENT '审批代办id',
  `theme` varchar(100)  NULL DEFAULT NULL COMMENT '主题',
  `applystype` tinyint(4) NULL DEFAULT NULL COMMENT '申请类型(0申请1失效)',
  PRIMARY KEY (`emphasis_id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_person_relation
-- ----------------------------
DROP TABLE IF EXISTS `tc_person_relation`;
CREATE TABLE `tc_person_relation`  (
  `person_relationid` varchar(36)  NOT NULL,
  `cust_id` varchar(36)  NULL DEFAULT NULL,
  `cust_code` varchar(36)  NULL DEFAULT NULL,
  `name` varchar(200)  NULL DEFAULT NULL,
  `register_phone` varchar(200)  NULL DEFAULT NULL,
  `relatetion_id` varchar(36)  NULL DEFAULT NULL,
  `create_id` varchar(36)  NULL DEFAULT NULL,
  `create_name` varchar(200)  NULL DEFAULT NULL,
  `modify_id` varchar(200)  NULL DEFAULT NULL,
  `modify_name` varchar(200)  NULL DEFAULT NULL,
  `crate_time` datetime(0) NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`person_relationid`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_person_vip
-- ----------------------------
DROP TABLE IF EXISTS `tc_person_vip`;
CREATE TABLE `tc_person_vip`  (
  `vip_id` varchar(36)  NOT NULL COMMENT '主键',
  `cust_id` varchar(36)  NULL DEFAULT NULL COMMENT '个人客户id',
  `enterprise_id` varchar(36)  NULL DEFAULT NULL COMMENT '企业客户id',
  `vip_grade` tinyint(4) NULL DEFAULT NULL COMMENT 'vip客户等级',
  `vip_num` varchar(30)  NULL DEFAULT NULL COMMENT 'vip编号',
  `vip_start` datetime(0) NULL DEFAULT NULL,
  `vip_end` datetime(0) NULL DEFAULT NULL,
  `apply_reason` varchar(100)  NULL DEFAULT NULL COMMENT '申请理由',
  `refuse_reason` varchar(100)  NULL DEFAULT NULL COMMENT '拒绝理由',
  `review_state` tinyint(4) NULL DEFAULT NULL COMMENT '审核状态',
  `staff_id` varchar(36)  NULL DEFAULT NULL COMMENT 'VIP专员id',
  `applyfile_path` varchar(400)  NULL DEFAULT NULL COMMENT '上传申请的资料路径',
  `approval_id` varchar(36)  NULL DEFAULT NULL COMMENT '审批代办id',
  `theme` varchar(100)  NULL DEFAULT NULL COMMENT '主题',
  `applystype` tinyint(4) NULL DEFAULT NULL COMMENT '申请类型（0申请1失效）',
  `name` varchar(40)  NULL DEFAULT NULL COMMENT '申请人姓名',
  `idcard` varchar(36)  NULL DEFAULT NULL COMMENT '身份证号码',
  `phonenum` varchar(36)  NULL DEFAULT NULL COMMENT '联系电话phonenum',
  PRIMARY KEY (`vip_id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_public_asset
-- ----------------------------
DROP TABLE IF EXISTS `tc_public_asset`;
CREATE TABLE `tc_public_asset`  (
  `id` varchar(36)  NOT NULL COMMENT 'uuid',
  `building_code` varchar(200)  NULL DEFAULT NULL,
  `house_code_new` varchar(9)  NULL DEFAULT NULL COMMENT '资产编码',
  `building_full_name` varchar(36)  NULL DEFAULT NULL COMMENT '资产名称',
  `description` varchar(36)  NULL DEFAULT NULL COMMENT '描述',
  `address` varchar(36)  NULL DEFAULT NULL,
  `location` varchar(36)  NULL DEFAULT NULL COMMENT '位置',
  `amount` int(2) NULL DEFAULT NULL COMMENT '数量',
  `unit` varchar(6)  NULL DEFAULT NULL COMMENT '单位',
  `is_hold` varchar(1)  NULL DEFAULT NULL COMMENT '是否自持',
  `purpose` varchar(36)  NULL DEFAULT NULL COMMENT '用途',
  `is_manage` varchar(1)  NULL DEFAULT NULL COMMENT '是否经营',
  `is_billing` varchar(1)  NULL DEFAULT NULL,
  `is_water` varchar(1)  NULL DEFAULT NULL COMMENT '是否供电',
  `is_electricity` varchar(1)  NULL DEFAULT NULL COMMENT '是否供水',
  `unit_wy_price` decimal(10, 2) NULL DEFAULT NULL,
  `unit_bt_price` decimal(10, 2) NULL DEFAULT NULL,
  `water_amount` int(10) NULL DEFAULT NULL,
  `electricity_amount` int(10) NULL DEFAULT NULL,
  `project_id` varchar(4)  NULL DEFAULT NULL,
  `creater_id` varchar(36)  NULL DEFAULT NULL,
  `creater_name` varchar(200)  NULL DEFAULT NULL,
  `modify_id` varchar(36)  NULL DEFAULT NULL,
  `modify_name` varchar(200)  NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_public_building
-- ----------------------------
DROP TABLE IF EXISTS `tc_public_building`;
CREATE TABLE `tc_public_building`  (
  `id` varchar(36)  NOT NULL COMMENT '主键(uuid)',
  `building_code` varchar(200)  NULL DEFAULT NULL COMMENT '建筑code',
  `name` varchar(100)  NULL DEFAULT NULL COMMENT '名称',
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建用户id',
  `creater_name` varchar(100)  NULL DEFAULT NULL COMMENT '创建用户名称',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '最后更新用户id',
  `modify_name` varchar(100)  NULL DEFAULT NULL COMMENT '最后更新用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_building_code`(`building_code`) USING BTREE
) ENGINE = InnoDB COMMENT = '公建';

-- ----------------------------
-- Table structure for tc_public_place
-- ----------------------------
DROP TABLE IF EXISTS `tc_public_place`;
CREATE TABLE `tc_public_place`  (
  `id` char(36)  NOT NULL COMMENT '主键',
  `product_code` varchar(50)  NULL DEFAULT NULL COMMENT '产品编码',
  `type` char(20)  NULL DEFAULT NULL COMMENT '类型',
  `name` varchar(100)  NULL DEFAULT NULL COMMENT '名称',
  `address` varchar(100)  NULL DEFAULT NULL COMMENT '地址',
  `area` DECIMAL(8,2) NULL DEFAULT NULL,
  `unit` varchar(10)  NULL DEFAULT NULL COMMENT '单位',
  `state` varchar(20)  NULL DEFAULT NULL COMMENT '状态',
  `place_attributes` varchar(100)  NULL DEFAULT NULL COMMENT '面积属性',
  `property_attributes` varchar(100)  NULL DEFAULT NULL COMMENT '产权属性',
  `start_date` date NULL DEFAULT NULL COMMENT '开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '结束日期',
  `property_leader` varchar(50)  NULL DEFAULT NULL COMMENT '物业负责人',
  `use_water_flag` char(3)  NULL DEFAULT NULL COMMENT '是否用水标识：是，否',
  `lessee` varchar(100)  NULL DEFAULT NULL COMMENT '承租客户',
  `water_delivery_flag` char(3)  NULL DEFAULT NULL COMMENT '是否已供水标识：是，否',
  `power_supply_flag` char(3)  NULL DEFAULT NULL COMMENT '是否已供电标识：是，否',
  `field1` char(3)  NULL DEFAULT NULL COMMENT '是否用用',
  `bill_status` varchar(20)  NULL DEFAULT NULL COMMENT '计费状态',
  `creater_id` char(36)  NULL DEFAULT NULL COMMENT '创建用户id',
  `creater_name` varchar(50)  NULL DEFAULT NULL COMMENT '创建用户名称',
  `modify_id` char(36)  NULL DEFAULT NULL COMMENT '最后更新用户id',
  `modify_name` varchar(50)  NULL DEFAULT NULL COMMENT '最后更新用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '公共场所场地出租';

-- ----------------------------
-- Table structure for tc_public_rental
-- ----------------------------
DROP TABLE IF EXISTS `tc_public_rental`;
CREATE TABLE `tc_public_rental`  (
  `id` varchar(36)  NOT NULL,
  `asset_no` varchar(200)  NULL DEFAULT NULL COMMENT '资产编码',
  `public_name` varchar(200)  NULL DEFAULT NULL COMMENT '名称',
  `public_number` varchar(200)  NULL DEFAULT NULL COMMENT '数量',
  `position_attribute` varchar(200)  NULL DEFAULT '道路' COMMENT '位置属性:架空层,地下室,人防,道路,广场,空地,绿地',
  `owner_of_property` varchar(200)  NULL DEFAULT NULL COMMENT '物业负责人',
  `public_type` varchar(200)  NULL DEFAULT NULL COMMENT '类型',
  `rented_address` varchar(200)  NULL DEFAULT NULL COMMENT '出租地址',
  `company` varchar(200)  NULL DEFAULT '平方米' COMMENT '单位:平方米,个',
  `property_property` varchar(200)  NULL DEFAULT '企业' COMMENT '产权属性:私人,企业,政府,公共权益',
  `start_time` date NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` date NULL DEFAULT NULL COMMENT '结束时间',
  `tenant_customer` varchar(200)  NULL DEFAULT NULL COMMENT '承租客户',
  `water_use` varchar(5)  NULL DEFAULT '否' COMMENT '是否用水:是,否',
  `electricity_use` varchar(5)  NULL DEFAULT '否' COMMENT '是否用电:是,否',
  `electric_power` varchar(5)  NULL DEFAULT '否' COMMENT '是否已供电:是,否',
  `water_power` varchar(5)  NULL DEFAULT '否' COMMENT '是否已供水:是,否',
  `billing_status` varchar(5)  NULL DEFAULT '关闭' COMMENT '计费状态:  开始, 关闭',
  `public_state` varchar(5)  NULL DEFAULT '从未出租' COMMENT '状态: 出租中, 从未出租,已停租空置',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目ID',
  `company_id` varchar(36)  NULL DEFAULT NULL COMMENT '公司ID',
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `creater_name` varchar(36)  NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改人ID',
  `modify_name` varchar(36)  NULL DEFAULT NULL COMMENT '修改人姓名',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_reading_schedule
-- ----------------------------
DROP TABLE IF EXISTS `tc_reading_schedule`;
CREATE TABLE `tc_reading_schedule`  (
  `id` varchar(36)  NOT NULL COMMENT '计划id',
  `schedule_code` varchar(36)  NULL DEFAULT NULL COMMENT '计划编号',
  `schedule_name` varchar(100)  NULL DEFAULT NULL COMMENT '计划名',
  `reading_start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `reading_end_time` datetime(0) NULL DEFAULT NULL COMMENT '计划完成时间',
  `reading_days` tinyint(2) NULL DEFAULT 0 COMMENT '抄表时长',
  `audit_start_time` datetime(0) NULL DEFAULT NULL COMMENT '审核开始时间',
  `audit_days` tinyint(2) NULL DEFAULT 0 COMMENT '审核时长',
  `is_circle` tinyint(1) NULL DEFAULT 0 COMMENT '是否开启自动循环:0开启,1关闭',
  `exec_freq` tinyint(2) NULL DEFAULT 0 COMMENT '执行频率',
  `meter_type` tinyint(1) NULL DEFAULT 0 COMMENT '抄表类型:0水表,1电表',
  `company_code` varchar(36)  NULL DEFAULT NULL COMMENT '公司编码',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '计划当前状态:0新建,1执行中,2完成,3终止',
  `schedule_desc` varchar(300)  NULL DEFAULT NULL COMMENT '描述',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目id',
  `total_meter_count` int(36) NULL DEFAULT 0 COMMENT '总表数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '抄表计划表';

-- ----------------------------
-- Table structure for tc_reading_task
-- ----------------------------
DROP TABLE IF EXISTS `tc_reading_task`;
CREATE TABLE `tc_reading_task`  (
  `id` varchar(36)  NOT NULL COMMENT '任务id',
  `task_code` varchar(36)  NOT NULL COMMENT '任务编号',
  `schedule_code` varchar(36)  NULL DEFAULT NULL COMMENT '计划编号',
  `schedule_id` varchar(36)  NOT NULL COMMENT '计划id,关联到计划表',
  `task_content` varchar(200)  NULL DEFAULT NULL COMMENT '任务内容',
  `schedule_content` varchar(200)  NULL DEFAULT NULL COMMENT '计划内容',
  `reading_position` varchar(300)  NULL DEFAULT NULL COMMENT '抄表地点,关联到tc_building的building_code',
  `reading_person_id` varchar(36)  NULL DEFAULT NULL COMMENT '抄表人',
  `total_meter_count` int(6) NULL DEFAULT 0 COMMENT '抄表总数',
  `complete_meter_count` int(6) NULL DEFAULT 0 COMMENT '已抄表数',
  `remain_meter_count` int(6) NULL DEFAULT 0 COMMENT '未抄表数',
  `meter_type` tinyint(1) NULL DEFAULT 0 COMMENT '表类型:0水表,1电表',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态:0新建,1执行中,2执行完成,3终止,4:重抄任务执行中,5:重抄任务完成',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '抄表开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '抄表结束时间',
  `audit_status` tinyint(4) NULL DEFAULT 0 COMMENT '审核状态,0,待审核,1审核完成,2审核不通过',
  `audit_start_time` datetime(0) NULL DEFAULT NULL COMMENT '审核开始时间',
  `audit_end_time` datetime(0) NULL DEFAULT NULL COMMENT '审核结束时间',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `last_task_id` varchar(36)  NULL DEFAULT NULL COMMENT '关联的上一次任务的id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '抄表任务';

-- ----------------------------
-- Table structure for tc_stall
-- ----------------------------
DROP TABLE IF EXISTS `tc_stall`;
CREATE TABLE `tc_stall`  (
  `id` varchar(36)  NOT NULL COMMENT '车位id',
  `building_code` varchar(200)  NULL DEFAULT NULL COMMENT '建筑code',
  `name` varchar(100)  NULL DEFAULT NULL,
  `is_mechanical_stall` varchar(100)  NULL DEFAULT NULL COMMENT '是否机械车位',
  `stall_type` varchar(100)  NULL DEFAULT NULL COMMENT '车位类型',
  `administrative_expenese` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '如果是私家车位，产生的管理费用，单位(元/月)',
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建用户id',
  `creater_name` varchar(200)  NULL DEFAULT NULL COMMENT '创建用户名称',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '最后更新用户id',
  `modify_name` varchar(200)  NULL DEFAULT NULL COMMENT '最后更新用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_building_code`(`building_code`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_store
-- ----------------------------
DROP TABLE IF EXISTS `tc_store`;
CREATE TABLE `tc_store`  (
  `id` varchar(36)  NOT NULL COMMENT '商铺id',
  `building_code` varchar(200)  NULL DEFAULT NULL COMMENT '建筑code',
  `store_name` varchar(100)  NULL DEFAULT NULL COMMENT '商铺号',
  `store_type` varchar(30)  NULL DEFAULT NULL COMMENT '商铺类型',
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建用户id',
  `creater_name` varchar(200)  NULL DEFAULT NULL COMMENT '创建用户名称',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '最后更新用户id',
  `modify_name` varchar(200)  NULL DEFAULT NULL COMMENT '最后更新用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_building_code`(`building_code`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_tp_asset
-- ----------------------------
DROP TABLE IF EXISTS `tc_tp_asset`;
CREATE TABLE `tc_tp_asset`  (
  `id` varchar(36)  NOT NULL,
  `building_code` varchar(200)  NULL DEFAULT NULL COMMENT '建筑编号',
  `building_name` varchar(50)  NULL DEFAULT NULL COMMENT '建筑名称',
  `house_code` varchar(50)  NULL DEFAULT NULL COMMENT '房屋编码',
  `house_code_new` varchar(25)  NULL DEFAULT NULL COMMENT '新房屋编码',
  `type` varchar(20)  NULL DEFAULT NULL COMMENT '资产属性',
  `property_owner` varchar(36)  NULL DEFAULT NULL COMMENT '产权人',
  `property_type` varchar(20)  NULL DEFAULT NULL COMMENT '产权属性',
  `property_addr` varchar(225)  NULL DEFAULT NULL COMMENT '资产地址',
  `building_area` varchar(50)  NULL DEFAULT NULL COMMENT '建筑面积',
  `qi` varchar(20)  NULL DEFAULT NULL COMMENT '期',
  `qu` varchar(20)  NULL DEFAULT NULL COMMENT '区',
  `unit_wy_price` decimal(16, 2) NULL DEFAULT NULL COMMENT '物业费单价',
  `unit_bt_price` decimal(16, 2) NULL DEFAULT NULL COMMENT '本体基金单价',
  `addr_type` varchar(20)  NULL DEFAULT NULL COMMENT '位置属性',
  `is_charge_obj` varchar(5)  NULL DEFAULT NULL COMMENT '是否计费',
  `bill_addr` varchar(36)  NULL DEFAULT NULL COMMENT '账单地址',
  `password` varchar(30)  NULL DEFAULT NULL COMMENT '房屋密码',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '项目ID',
  `company_id` varchar(36)  NULL DEFAULT NULL COMMENT '公司ID',
  `state` varchar(1)  NULL DEFAULT NULL,
  `creater_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `creater_name` varchar(36)  NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改人ID',
  `modify_name` varchar(36)  NULL DEFAULT NULL COMMENT '修改人姓名',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_water_meter
-- ----------------------------
DROP TABLE IF EXISTS `tc_water_meter`;
CREATE TABLE `tc_water_meter`  (
  `id` varchar(36)  NOT NULL COMMENT '主键id',
  `code` varchar(36)  NOT NULL COMMENT '水表编号',
  `water_meter_name` varchar(200)  NULL DEFAULT NULL COMMENT '水电表名称',
  `position` varchar(200)  NOT NULL COMMENT '安装位置',
  `relation_building` varchar(200)  NULL DEFAULT NULL COMMENT '关联收费对象',
  `company_code` varchar(50)  NULL DEFAULT NULL COMMENT '公司编码',
  `project_id` varchar(36)  NULL DEFAULT NULL COMMENT '所在的项目id信息',
  `rate` DECIMAL(8, 2) NULL DEFAULT NULL COMMENT '倍率',
  `init_amount` DECIMAL(16, 2) NULL DEFAULT 0.00 COMMENT '初始读数',
  `min_amount` DECIMAL(16, 2) NULL DEFAULT 0.00 COMMENT '最小读数',
  `max_amount` DECIMAL(16, 2) NULL DEFAULT 0.00 COMMENT '最大读数',
  `is_circle` tinyint(1) NULL DEFAULT 0 COMMENT '是否循环使用 , 0 是 , 1不是',
  `state` tinyint(1) NULL DEFAULT 0 COMMENT '水表状态, 0 启用 , 1停用',
  `type` tinyint(1) NULL DEFAULT 0 COMMENT '抄表方式: 0 室内 , 1 室外 , 2 远程',
  `parent_code` varchar(36)  NULL DEFAULT NULL COMMENT '父表编号',
  `master_code` varchar(36)  NULL DEFAULT NULL COMMENT '主表编号',
  `user_type` tinyint(1) NULL DEFAULT 0 COMMENT '使用性质: 0 商用, 1 民用 , 2 管理处',
  `is_billing` tinyint(1) NULL DEFAULT 0 COMMENT '是否计费: 0 是, 1 不是',
  `is_public` tinyint(1) NULL DEFAULT 0 COMMENT '是否公用: 0 是, 1 不是',
  `asset_no` varchar(36)  NULL DEFAULT NULL COMMENT '资产编号',
  `brand` varchar(36)  NULL DEFAULT NULL COMMENT '品牌',
  `specs` varchar(50)  NULL DEFAULT NULL COMMENT '规格',
  `provider` varchar(36)  NULL DEFAULT NULL COMMENT '供应商',
  `provider_phone` varchar(15)  NULL DEFAULT NULL COMMENT '供应商联系电话',
  `assemble_person` varchar(36)  NULL DEFAULT NULL COMMENT '安装人',
  `assemble_time` date NULL DEFAULT NULL COMMENT '安装时间',
  `create_id` varchar(36)  NULL DEFAULT NULL COMMENT '创建人',
  `create_time` date NULL DEFAULT NULL COMMENT '创建时间',
  `modify_id` varchar(36)  NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` date NULL DEFAULT NULL COMMENT '修改时间',
  `meter_level` tinyint(4) NULL DEFAULT NULL COMMENT '水电表级别（1，一级表，2，二级表...）',
  `modify_name` varchar(20)  NULL DEFAULT NULL COMMENT '修改人姓名',
  `check_table` varchar(20)  NULL DEFAULT NULL COMMENT '是否校验表',
  `describe_num` varchar(20)  NULL DEFAULT NULL COMMENT '描述',
  `group_table_coding` varchar(20)  NULL DEFAULT NULL COMMENT '组表编码',
  `group_table_name` varchar(20)  NULL DEFAULT NULL COMMENT '组表名称',
  `is_share` varchar(20)  NULL DEFAULT NULL COMMENT '是否分摊',
  `new_coding` char(200)  NULL DEFAULT NULL COMMENT '新编码',
  `payment_object` char(30)  NULL DEFAULT NULL COMMENT '缴费对象',
  `external_coding` char(30)  NULL DEFAULT NULL COMMENT '外部编码',
  `water_state` varchar(30)  NULL DEFAULT NULL,
  INDEX `idx_position`(`position`) USING BTREE,
  INDEX `idx_code`(`code`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for tc_waterlog
-- ----------------------------
DROP TABLE IF EXISTS `tc_waterlog`;
CREATE TABLE `tc_waterlog`  (
  `id` varchar(36)  NOT NULL,
  `water_id` varchar(36)  NULL DEFAULT NULL,
  `operator` varchar(36)  NULL DEFAULT NULL,
  `modifytime` datetime(0) NULL DEFAULT NULL,
  `modifytype` varchar(36)  NULL DEFAULT NULL,
  `modifymatter` varchar(200)  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for ts_annex
-- ----------------------------
DROP TABLE IF EXISTS `ts_annex`;
CREATE TABLE `ts_annex`  (
  `annex_id` varchar(36)  NOT NULL COMMENT '附件id',
  `relation_id` varchar(36)  NULL DEFAULT NULL COMMENT '关联id（可以是服务请求、任务、工单）',
  `annex_type` tinyint(4) NULL DEFAULT NULL COMMENT '附件类型（业主签名、图片、录音、文档等）1, 图片, 2:zip',
  `annex_address` varchar(300)  NULL DEFAULT NULL COMMENT '附件长度',
  `annex_name` varchar(50)  NULL DEFAULT NULL COMMENT '附件名称',
  `annex_time` datetime(0) NULL DEFAULT NULL COMMENT '附件时间',
  `pact_id` varchar(36)  NULL DEFAULT NULL COMMENT '合同id',
  `is_main` varchar(10)  NULL DEFAULT NULL COMMENT '主辅图（0主图，1辅图）关联产品表',
  `file_type` varchar(30)  NULL DEFAULT NULL COMMENT '文件格式',
  `is_used` tinyint(4) NULL DEFAULT NULL COMMENT '是否有效(0有效,1无效)',
  `md5` varchar(60)  NULL DEFAULT NULL COMMENT '文件md5码',
  `upload_file_id` varchar(64)  NULL DEFAULT NULL COMMENT '对接fastDFS用于从fastDFS取文件的file_id，具有唯一性',
  PRIMARY KEY (`annex_id`) USING BTREE
) ENGINE = InnoDB;
