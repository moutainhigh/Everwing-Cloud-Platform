/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/5/15 10:54:46                           */
/*==============================================================*/


drop table if exists t_ac_account;

drop table if exists t_ac_bill_detail;

drop table if exists t_ac_common_account_detail;

drop table if exists t_ac_current_charge;

drop table if exists t_ac_current_charge_detail;

drop table if exists t_ac_delay_account;

drop table if exists t_ac_last_bill_fee_info;

drop table if exists t_ac_late_fee_bill_info;

drop table if exists t_ac_late_fee_stream;

drop table if exists t_ac_special_account;

drop table if exists t_ac_special_detail;

drop table if exists t_ac_wx_binding;

drop table if exists t_ac_business_opera_detail;

DROP TABLE IF EXISTS sequence;

/*==============================================================*/
/* Table: t_ac_account                                          */
/*==============================================================*/
create table t_ac_account
(
   id                   varchar(36) not null,
   house_code_new        varchar(128) not null comment '关联tc_building的house_code_new字段',
   common_deposit_amount decimal(16,2) comment '通用账户金额',
   special_deposit_amount decimal(16,2) comment '专项抵扣金额（例：物业100 本体20  水费200 电费  0）',
   late_fee_amount      decimal(16,2) comment '此资产所产生的滞纳金金额',
   current_charging_amount decimal(16,2) comment '当月计费总金额',
   last_arrears_amount  decimal(16,2) comment '上月欠费总金额',
   current_bill_amount  decimal(16,2) comment '当月账单总金额',
   project_id           varchar(36) not null comment '项目id，关联t_sys_project的code字段',
   project_name         varchar(128) comment '项目名称',
   create_time          timestamp not null comment '创建时间',
   create_id            varchar(36) comment '创建人，如果是系统则使用system，如果是具体员工，关联t_sys_user表的user_id',
   modify_time          timestamp comment '修改时间',
   modify_id            varchar(36) comment '修改人，如果是系统则使用system，如果是具体员工，关联t_sys_user表的user_id',
   signature            varchar(128) comment '对涉及到的金额+时间字段进行签名，任何写入更新之前都需要校验此字段，校验成功后才能更新/写入',
   charging_month       varchar(10),
   primary key (id)
);

alter table t_ac_account comment '资产账户，一处资产信息对应一个账户（总账户）';

/*==============================================================*/
/* Table: t_ac_bill_detail                                      */
/*==============================================================*/
create table t_ac_bill_detail
(
   id                   varchar(36) not null,
   create_time          timestamp not null,
   bill_month           varchar(10) not null comment '例如 2014-04',
   bill_state           int not null comment '0 有效 1作废',
   bill_detail          varchar(1000) not null comment 'json格式包含输出账单的所有内容',
   house_code_new       varchar(36) not null comment '房屋编码',
   account_id      varchar(36) not null,
   bill_amount          decimal(16,2) not null,
   bill_payer           varchar(36) not null,
   bill_address         varchar(128) not null,
   bill_invalid         datetime default null,
   project_id           varchar(36),
   signature            varchar(128),
   pay_state            int COMMENT '缴费状态0 未缴费 1已缴费 2 部分缴费',
   primary key (id)
);

alter table t_ac_bill_detail comment '每月产生一条账单数据，包含账单里面所有的内容';

/*==============================================================*/
/* Table: t_ac_common_account_detail                            */
/*==============================================================*/
create table t_ac_common_account_detail
(
   id                   varchar(36) not null comment '表id唯一',
   account_id           varchar(36) not null comment '关联各个资产通用账户的id',
   house_code_new        varchar(128) not null,
   before_amount        decimal(16,2) not null comment '变动前的账户余额',
   after_amount         decimal(16,2) not null comment '账户变动后的月',
   change_amount        decimal(16,2) not null comment '本次发生变动的金额',
   business_type        int not null comment '1：预存   2：抵扣  3：退款',
   dikou_type           int comment '抵扣类型，记录通用账户进行的抵扣类型 0：非抵扣操作   1：物业  2：本体  3：水费  4:电费 （后续有其他可以递增）',
   bill_detail_id       varchar(128) comment '关联计费的明细id',
   description          varchar(256) not null,
   project_id           varchar(36) not null comment '项目id',
   project_name         varchar(128) comment '项目名称',
   create_time          timestamp not null comment '创建时间',
   create_id            varchar(36) not null comment '创建人',
   opera_id             varchar(36),
   primary key (id)
);

alter table t_ac_common_account_detail comment '记录每个资产通用账户的金额变动流水信息';

/*==============================================================*/
/* Table: t_ac_current_charge                                   */
/*==============================================================*/
create table t_ac_current_charge
(
   id                   varchar(36) not null,
   house_code_new        varchar(128) not null,
   current_bill_fee     decimal(16,2) not null,
   account_id           varchar(36) not null,
   account_type         int comment '计费项目也就是区分计费类型（1：物业管理费 2：本体基金  3：水费  4：电费  。。。后面有新的递增累加）',
   charging_month       varchar(10),
   audit_time           timestamp,
   project_id           varchar(36) not null,
   project_name         varchar(128) ,
   create_id            varchar(36) not null,
   create_time          timestamp not null,
   signature            varchar(128),
   primary key (id)
);

alter table t_ac_current_charge comment '各个资产关联信息当月计费总金额';

/*==============================================================*/
/* Table: t_ac_current_charge_detail                            */
/*==============================================================*/
create table t_ac_current_charge_detail
(
   id                   varchar(36) not null,
   house_code_new        varchar(128) not null,
   charge_amount        decimal(16,2) not null,
   account_id           varchar(36) not null comment '关联当月计费总金额表的id',
   account_type         int comment '计费项目也就是区分计费类型（1：物业管理费 2：本体基金  3：水费  4：电费  。。。后面有新的递增累加）',
   charge_time          timestamp,
   audit_time           datetime default null,
   last_charge_id       varchar(36),
   charge_detail        varchar(1000) comment '尽可能详细的存放账单那边需要的数据信息',
   common_dikou         decimal(16,2) comment '通用账户发生的抵扣金额',
   special_dikou        decimal(16,2) comment '专项账户发生的抵扣金额',
   project_id           varchar(36) not null,
   project_name         varchar(128) ,
   create_id            varchar(128) not null,
   create_time          timestamp not null,
   payed_amount         decimal(16,2) comment '已付款金额，存放本次账单的已付款金额，如果全部结清，则修改是否已付款状态为2',
   current_payment		decimal(16,2) not null DEFAULT 0.00 COMMENT '本次支付金额字段',
   assign_amount        decimal(16,2),
   update_time          timestamp,
   payable_amount       decimal(16,2) comment '本期计费应缴=本期计费+分摊',
   currenct_arreas		decimal(16,2) comment '本期欠费',
   business_type        int comment '业务类型：1 计费   2 专项账户抵扣  3 通用账户抵扣   4 交费  等等',
   opera_id             VARCHAR(36) comment '老账户关联的是操作明细表的id，新账户关联订单id',
   primary key (id)
);
-- ALTER TABLE t_ac_current_charge_detail ADD COLUMN current_payment decimal(16,2) not null DEFAULT 0.00 COMMENT '本次支付金额字段';
alter table t_ac_current_charge_detail comment '各个资产每月的计费明细记录，也作为账单获取数据的依据';

/*==============================================================*/
/* Table: t_ac_delay_account                                    */
/*==============================================================*/
create table t_ac_delay_account
(
   id                   varchar(36) not null,
   amount               decimal(16,2) not null comment '单个资产滞纳金总额',
   account_id           varchar(36) not null,
   project_id           varchar(36) not null,
   project_name         varchar(128) ,
   create_id            varchar(36) not null,
   create_time          timestamp not null,
   modify_id            varchar(36),
   modify_time          timestamp,
   account_type         int comment '1水费,2电费.3物业,4本体。。。。。',
   house_code_new        varchar(128) not null,
   primary key (id)
);

alter table t_ac_delay_account comment '存放各个资产滞纳金账户的信息汇总表';

/*==============================================================*/
/* Table: t_ac_last_bill_fee_info                               */
/*==============================================================*/
create table t_ac_last_bill_fee_info
(
   id                   varchar(36) not null,
   house_code_new        varchar(128) not null,
   last_bill_fee        decimal(16,2) not null comment '上月欠费总金额=计费结果表中上月本期应付',
   account_id           varchar(36) not null,
   project_id           varchar(36) not null,
   project_name         varchar(128) ,
   create_id            varchar(36) not null,
   create_time          timestamp not null,
   modify_id            varchar(36),
   modify_time          timestamp,
   account_type         int comment '水费,电费,物业,本体,等',
   signature            varchar(128),
   primary key (id)
);

alter table t_ac_last_bill_fee_info comment '存放各个资产欠费总金额信息';

/*==============================================================*/
/* Table: t_ac_late_fee_bill_info                               */
/*==============================================================*/
create table t_ac_late_fee_bill_info
(
   id                   varchar(36) not null,
   project_id           varchar(36) ,
   project_name         varchar(128),
   description          varchar(256),
   is_singleinterest    int,
   rate                 decimal(3,2),
   overdue_days         int,
   is_used				int comment '是否使用 0 使用中  1已废弃', 
   account_type         int,
   create_time          timestamp,
   create_by            varchar(36),
   primary key (id)
);

alter table t_ac_late_fee_bill_info comment '存放各个账户需要开始计违约金的信息表';

/*==============================================================*/
/* Table: t_ac_late_fee_stream                                  */
/*==============================================================*/
create table t_ac_late_fee_stream
(
   id                   varchar(36) not null,
   delay_account_id     varchar(36) not null comment '滞纳金账户id，应用late_fee_detail的id字段',
   house_code_new        varchar(128) not null comment '建筑表code',
   before_amount        decimal(16,2) not null comment '账户变动前金额',
   after_amount         decimal(16,2) not null comment '账户变动后金额',
   change_amount        decimal(16,2) not null comment '变动金额',
   business_type        int not null comment '业务类型  1：产生滞纳金  2 减免滞纳金 3 抵扣滞纳金 4 交费滞纳金',
   project_id           varchar(36) not null comment '项目code',
   project_name         varchar(128) comment '项目名称',
   create_time          timestamp not null comment '创建时间',
   create_id            varchar(36) not null comment '创建人',
   description          varchar(256),
   opera_id             varchar(36) comment '前台做滞纳金减免的时候写入',
   principal_account    decimal(16,2),
   rate                 decimal(3,2),
   is_singleinterest    int,
   overdue_days         int,
   primary key (id)
);

alter table t_ac_late_fee_stream comment '记录各个资产产生违约金的流水详情信息';

/*==============================================================*/
/* Table: t_ac_special_account                                  */
/*==============================================================*/
create table t_ac_special_account
(
   id                   varchar(36) not null,
   house_code_new        varchar(128) not null,
   special_amount       decimal(16,2) not null,
   account_type          int not null comment '水费,电费,物业,本体,等',
   account_id           varchar(36) not null,
   project_id           varchar(36) not null,
   project_name         varchar(128) ,
   create_id            varchar(36) not null,
   create_time          timestamp not null,
   modify_id            varchar(36),
   modify_time          timestamp,
   signature            varchar(128),
   primary key (id)
);

alter table t_ac_special_account comment '存放各个资产专项抵扣账户的信息汇总表';

/*==============================================================*/
/* Table: t_ac_special_detail                                   */
/*==============================================================*/
create table t_ac_special_detail
(
   id                   varchar(36) not null,
   special_id           varchar(36) not null comment '引用专项抵扣账户id',
   house_code_new        varchar(128) not null comment '建筑表code',
   before_amount        decimal(16,2) not null comment '账户变动前金额',
   after_amount         decimal(16,2) not null comment '账户变动后金额',
   change_amount        decimal(16,2) not null comment '变动金额',
   business_type        int not null comment '业务类型  1:预存   2：抵扣   3：退款',
   project_id           varchar(36) not null comment '项目code',
   project_name         varchar(128) comment '项目名称',
   create_time          timestamp not null comment '创建时间',
   create_id            varchar(36) not null comment '创建人',
   description          varchar(256),
   bill_detail_id       varchar(36) comment '关联抵扣明细表id',
   opera_id             varchar(36),
   primary key (id)
);

alter table t_ac_special_detail comment '记录各个资产专项抵扣账户的流水详情信息';


drop table if exists t_ac_cycle_order_detail;

drop table if exists t_ac_order;

drop table if exists t_ac_abnormal_charge;

/*==============================================================*/
/* Table: t_ac_abnormal_charge                                  */
/*==============================================================*/
create table t_ac_abnormal_charge
(
   id                   varchar(36) not null,
   house_code           varchar(36) comment '房号',
   building_name        varchar(200),
   Owner_name           varchar(100) comment '这里冗余一个业主姓名，免得关联起来麻烦',
   account_type         int comment '1水，2电，3物业，4，本体   5.。。。。。按照计费的项目来一一对应',
   abnormal_type        decimal(16,2),
   before_amount        decimal(16,2),
   amount               decimal(16,2) comment '存在正负数，正数为少收费，负数为多收费。
            其中正数则放到单月的计费结果里面，具体字段是加在收费结果明细表中的历史欠费总额里面。
            负数放到对应的专项预存庄户里面
            ',
   opera_id             varchar(36),
   project_id           varchar(36),
   project_name         varchar(200),
   create_time          datetime,
   description          text,
   primary key (id)
);

alter table t_ac_abnormal_charge comment '用于处理计费审核后发现了抄表错误，单价错误，等等情况。财务可以在这时候作出处理操作';

/*==============================================================*/
/* Table: t_ac_cycle_order_detail                               */
/*==============================================================*/
create table t_ac_cycle_order_detail
(
   detail_id                   varchar(36) not null,
   order_id             varchar(64),
   business_type        int comment '只存在2种类型,缴费 和 预存',
   deposit_type         int,
   account_type         int comment '1 水费 2电费 3物业 4本体.....',
   detail_amount               decimal(16,2),
   house_code_new       VARCHAR(36),
   late_amount          decimal(16,2),
   create_time          datetime DEFAULT CURRENT_TIMESTAMP,
   primary key (detail_id)
);

/*==============================================================*/
/* Table: t_ac_order                                            */
/*==============================================================*/
create table t_ac_order
(
   id                   varchar(36) not null,
   order_no             varchar(64) not null,
   amount               decimal(16,2) not null,
   payer                varchar(36),
   payer_mobile         varchar(16),
   order_state          int not null comment '1 已生成，2已完成，已作废',
   pay_state            int not null comment '1，未支付，2支付中,，3部分支付，4已支付',
   order_type           int not null comment '1 周期性收费订单 2产品类收费订单,通过该字段判断读取周期性订单明细表还是产品类订单明细表',
   update_time          timestamp,
   opera_id             varchar(36),
   signature            varchar(128),
   payment_time         timestamp,
   house_code_new       VARCHAR(36),
   payment_channel      int not null comment '1 微信，2支付宝，3网银，4拉卡拉',
   is_rcorded           int not null comment '1 已入帐，2未入账',
   pay_channel_trade_no varchar(36)  comment '1 渠道交易订单号',
   primary key (id)
);

/*==============================================================*/
/* Table: t_ac_wx_binding                                       */
/*==============================================================*/
create table t_ac_wx_binding
(
   user_id              varchar(32) not null comment '手动生成用户ID，通过sequence生成',
   open_id              varchar(62) not null comment '小程序专用，当前微信用户在小程序中的唯一标示',
   mobile               varchar(16) not null,
   real_name            varchar(32),
   identity             varchar(20),
   primary key (user_id)
);


create index idx_house_code_new on t_ac_account(house_code_new);
create index idx_house_code_new on t_ac_last_bill_fee_info(house_code_new);
create index idx_house_code_new on t_ac_delay_account(house_code_new);
create index idx_house_code_new on t_ac_common_account_detail(house_code_new);
create index idx_house_code_new on t_ac_current_charge(house_code_new);
create index idx_house_code_new on t_ac_current_charge_detail(house_code_new);
create index idx_house_code_new on t_ac_special_account(house_code_new);
create index idx_house_code_new on t_ac_special_detail(house_code_new);
create index idx_house_code_new on t_ac_bill_detail(house_code_new);
create index idx_house_code_new on t_ac_late_fee_stream(house_code_new);

create index idx_account_type on t_ac_delay_account(account_type);
create index idx_account_type on t_ac_special_account(account_type);
create index idx_account_type on t_ac_last_bill_fee_info(account_type);
create index idx_account_type on t_ac_current_charge_detail(account_type);

create index idx_business_type on t_ac_late_fee_stream(business_type);
create index idx_business_type on t_ac_common_account_detail(business_type);
create index idx_business_type on t_ac_special_detail(business_type);
create index idx_business_type on t_ac_current_charge_detail(business_type);


drop table if exists t_ac_business_opera_detail;

/*==============================================================*/
/* Table: t_ac_business_opera_detail                            */
/*==============================================================*/
create table t_ac_business_opera_detail
(
   id                   varchar(36) not null,
   operation_id         varchar(36) not null comment '通过当前登录用户来获取',
   operation_time       timestamp not null,
   business_type        int not null comment '1缴费，.2预存, 3退费，4减免',
   amount               decimal(16,2) not null,
   project_id           varchar(36) not null,
   project_name         varchar(128) not null,
   remark               VARCHAR(512),
   signature            varchar(128),
   person_type          int comment '1物业前台工作人员，2业主业主本人',
   client_type          int comment '1 pc(前台现金缴费)，2 pos(前台pos缴费)，3小程序缴费(业主在小程序内进行缴费)',
   primary key (id)
);

alter table t_ac_business_opera_detail comment '前台每操作一笔资金相关的业务,记录一条流水明细';

CREATE TABLE `sequence` (
  `current_value` int(11) NOT NULL,
  `increment_value` int(11) NOT NULL,
  `sequence_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sequence
-- ----------------------------
INSERT INTO `sequence` VALUES ('10013', '1', 'wxxcx');
INSERT INTO `sequence` VALUES ('120027', '1', 'D');




CREATE DEFINER=`root`@`%` FUNCTION `func_currval`(seq_name varchar(50)) RETURNS int(11)
  begin
    declare value integer;
    set value = 0;
    select current_value into value from sequence where sequence_name = seq_name;
    if ifnull(value,0)=0 then
      return false;
    elseif ifnull(value,0)='' then
      return false;
    else
      return value;
    end if;
  end;


CREATE FUNCTION `func_nextval`(seq_name varchar(50)) RETURNS varchar(50) CHARSET utf8
  begin
    declare seq int;
    set seq = func_currval(seq_name);
    if seq=0 then
      return false;
    else
      update sequence set current_value = current_value + increment_value
      where sequence_name = seq_name;
      return CONCAT(seq_name, seq);
    end if;
  end;


CREATE  FUNCTION `func_setval`(seq_name varchar(50),value integer) RETURNS int(11)
  begin
    update sequence
    set current_value = value
    where sequence_name = sequence_name;
    return func_currval(sequence_name);
  end;

