drop table if exists t_jf_assets_charging_detail_info;

drop table if exists t_jf_charging_item_base_info;

drop table if exists t_jf_charging_item_total_info;

drop table if exists t_jf_charging_scheme;

drop table if exists t_jg_charging_rule_info;

drop table if exists t_jg_charging_rule_detail_info;

drop table if exists t_jf_rule_building_relation_info;


/*==============================================================*/
/* Table: t_jf_assets_charging_detail_info                      */
/*==============================================================*/
create table t_jf_assets_charging_detail_info
(
   id                   varchar(36) not null,
   charging_total_id    varchar(36) not null comment '计费项详情id',
   charging_type        int not null comment '计费类型  同 计费项类型',
   house_code           varchar(36) not null comment '房屋房号',
   building_name        varchar(200) not null comment '房屋名称',
   current_charging_fee decimal(16,2) not null comment '本期计费金额',
   last_charging_id     varchar(36) not null comment '上期计费id，关联上次的计费详情id',
   charging_status      int not null comment ' 计费状态  1  未计费  2 计费中  3 已计费  4 计费失败',
   charging_detail      varchar(1000) comment '计费详情（json串格式存储计费的详细明细，单价，规则，面积，各阶梯的金额等等）',
   audit_status         int comment '审核状态   1 待审核   2审核中  3 审核完成   4 审核失败',
   deductible_status    int comment '抵扣状态 1 未抵扣  2 抵扣中  3 抵扣成功  4 抵扣失败',
   charging_time        datetime comment '计费时间',
   is_create_bill       int comment '是否生成账单',
   current_share_fee    decimal(16,2) comment '本期分摊金额',
   create_time          datetime not null comment '创建时间',
   create_od            varchar(36) not null comment '创建人',
   primary key (id)
);

alter table t_jf_assets_charging_detail_info comment '单个资产，单个计费项的，单个周期的计费详情表';


/*==============================================================*/
/* Table: t_jf_charging_item_base_info                          */
/*==============================================================*/
create table t_jf_charging_item_base_info
(
   id                   varchar(36) not null,
   charging_item_name   varchar(200) not null comment '计费项名称',
   charging_type        int not null comment '计费项编号，这里对应账户的1 物业，2本体，3水等，方便的关联',
   is_used              int not null comment '描述计费项是否使用中，0 使用  1 停用',
   create_time          datetime not null comment '创建时间',
   start_used_time      datetime comment '开启时间，做一个记录',
   stop_time            datetime comment '停止时间，有可能中间会停用，做个记录',
   create_id            varchar(36) not null comment '创建人',
   project_id           varchar(36) not null comment '项目id',
   project_name         varchar(200) not null comment '项目名称',
   primary key (id)
);

alter table t_jf_charging_item_base_info comment '用于记录每个管理处所拥有的计费项信息，每个管理处是可以不同的';

/*==============================================================*/
/* Table: t_jf_charging_item_total_info                         */
/*==============================================================*/
create table t_jf_charging_item_total_info
(
   id                   varchar(36) not null,
   charging_item_id     varchar(36) not null comment '计费项id  关联计费项信息表',
   charging_item_name   varchar(200) not null comment '计费项名称',
   charging_type        int not null comment '计费类型  1 物业   2 本体 3 水费  4 电费  5 污水处理费   6 垃圾处理费等等，后面有新增继续递增就好',
   start_charging_time  datetime not null comment '开始计费时间是对项目可开始计费时间的描述，实际计费时间不一定是这一天',
   charging_status      int comment '计费状态  1  未计费  2 计费中  3 已计费  4 计费失败',
   audit_status         int comment '审核状态   1 待审核   2审核中  3 审核完成   4 审核失败',
   deduct_status        int comment '抵扣状态 1 未抵扣  2 抵扣中  3 抵扣成功  4 抵扣失败',
   charging_time        datetime comment '计费时间',
   audit_time           datetime comment '审核时间',
   deduct_time          datetime not null comment '抵扣时间',
   current_charging_total decimal(16,2) not null comment '本期计费总额',
   current_deduct_total decimal(16,2) not null comment '本期抵扣总额',
   last_arrears_total   decimal(16,2) not null comment '上期欠费总额',
   charging_times       int not null comment '计费次数',
   last_charging_id     varchar(36) comment '上期计费id',
   charging_num         int comment '本期总计费数',
   project_id           varchar(36) not null comment '项目id',
   project_name         varchar(200) not null comment '项目名称',
   create_time          datetime not null comment '创建时间',
   create_id            varchar(64) not null comment '创建人',
   primary key (id)
);

alter table t_jf_charging_item_total_info comment '单个计费项当月的计费，审核，抵扣，托收，情况信息，单个计费项单月的一个汇总情况';



/*==============================================================*/
/* Table: t_jf_charging_scheme                                  */
/*==============================================================*/
create table t_jf_charging_scheme
(
   id                   varchar(36) not null,
   scheme_name          varchar(200) not null comment '方案名称',
   scheme_type          int not null comment '方案类型（1:物业管理费，2：本体基金 3：水费 4：电费  5 垃圾处理费
            6 污水处理费',
   is_used              int not null comment '启用状态（0：启用，1停用）',
   charging_type        int not null comment '计费方式（0：自动，1：手动）',
   proportion           decimal(16,2) not null comment '违约金计算方式（0：单利，1：复利）',
   start_using_date     datetime comment '启用日期 ',
   end_using_date       datetime comment '失效日期',
   overdue_start_dates  int not null comment '逾期天数',
   calculation_type     int not null comment '违约金计算方式（0：单利，1：复利）',
   frequency            int not null comment '计算频率(以月为单位)',
   charge_data          int not null comment '自动计费的会用到的，手动的不影响',
   project_id           varchar(36) not null,
   project_name         varchar(200) not null,
   create_id            varchar(36) not null,
   create_time          datetime not null,
   primary key (id)
);

alter table t_jf_charging_scheme comment '同一个计费项下使用中的方案只有一个';

/*==============================================================*/
/* Table: t_jf_rule_building_relation_info                      */
/*==============================================================*/
create table t_jf_rule_building_relation_info
(
   id                   varchar(36) not null,
   house_code           varchar(36) not null comment '房号',
   rule_id              varchar(36) not null comment '计费规则id',
   create_id            varchar(36) not null comment '创建人',
   create_time          datetime not null comment '创建时间',
   primary key (id)
);

alter table t_jf_rule_building_relation_info comment '这里记录单个房屋和计费规则的关联关系信息，用在单个房屋的计费';


/*==============================================================*/
/* Table: t_jg_charging_rule_detail_info                        */
/*==============================================================*/
create table t_jg_charging_rule_detail_info
(
   id                   varchar(36) not null,
   scheme_id            varchar(36) not null comment '方案id，关联计费方案表',
   rule_name            varchar(200) not null comment '规则名称',
   rule_type            int not null comment '同方案类型',
   is_ladder            int not null comment '这个主要针对的是电存在峰谷值也有阶梯计费的情况
             1 标准   2 峰值  3  谷值   4 平 值  。标准的就给水费用，正常的各阶梯公式
            峰值就是峰值的各阶梯',
   unit_price           decimal(16,2) comment '标准单价，使用与所有计费项',
   cricitcal_value      decimal(16,2) comment '标准临界值 （如水30吨以内不收费，此值为0）  水电相关才有值',
   standard_formula     varchar(200) comment '标准公式  如  unit_price   *   用量',
   formula_description  varchar(1000),
   coefficient1         decimal(16,2) comment '用于一些固定的系数值  如  0.27 * 0.9 * 1.08 * 用量  0.9这种固定的系数',
   coefficient2         decimal(16,2) comment '用于一些固定的系数值  如  0.27 * 0.9 * 1.08 * 用量  0.9这种固定的系数',
   coefficient3         decimal(16,2),
   create_id            varchar(36) not null comment '创建人',
   create_time          datetime not null comment '创建时间',
   update_by            varchar(36) not null comment '修改人',
   update_time          datetime not null comment '修改时间',
   primary key (id)
);

alter table t_jg_charging_rule_detail_info comment '同一个计费方案可能存在多个规则，如：物业，商铺和住宅单价不同';


/*==============================================================*/
/* Table: t_jg_charging_rule_info                               */
/*==============================================================*/
create table t_jg_charging_rule_info
(
   id                   varchar(36) not null,
   billing_item_id      varchar(36) not null,
   rule_description     varchar(200) not null,
   project_id           varchar(36) not null,
   project_name         varchar(100) not null,
   create_time          datetime not null,
   create_id            varchar(36) not null,
   primary key (id)
);

alter table t_jg_charging_rule_info comment '记录各个项目各种计费项的不同计费规则，是一个汇总形式的计费规则，用于给每个房做关联';

--
--
-- drop index IF EXISTS jdx_charging_item_id on t_jf_charging_item_total_info;
--
-- drop index idx_house_charge_id on t_jf_rule_building_relation_info;
--
-- drop index idx_house_code on t_jf_assets_charging_detail_info;
--
-- drop index idx_charge_total_id on t_jf_assets_charging_detail_info;
--
-- drop index idx_rule_id on t_jg_charging_rule_detail_info;
--
-- drop index idx_charging_type on t_jf_charging_item_total_info;
--
-- drop index idx_charge_item_id on t_jg_charging_rule_info;
--

create index idx_charge_total_id on t_jf_assets_charging_detail_info
(
   charging_total_id
);

create index idx_rule_id on t_jg_charging_rule_detail_info
(
   scheme_id
);


create index idx_house_charge_id on t_jf_rule_building_relation_info
(
   house_code,
   rule_id
);

create index idx_charging_type on t_jf_charging_item_total_info
(
   charging_type
);


create index idx_house_code on t_jf_assets_charging_detail_info
(
   house_code
);


create index jdx_charging_item_id on t_jf_charging_item_total_info
(
   charging_item_id
);


create index idx_charge_item_id on t_jg_charging_rule_info
(
   billing_item_id
);
