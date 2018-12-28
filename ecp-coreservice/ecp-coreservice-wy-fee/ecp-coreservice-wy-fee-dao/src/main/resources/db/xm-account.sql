/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/5/15 11:01:01                           */
/*==============================================================*/


drop table if exists t_project_account;

drop table if exists t_project_cycle_account;

drop table if exists t_project_cycle_detail;

drop table if exists t_project_delay_account;

drop table if exists t_project_delay_detail;

drop table if exists t_project_prestore_account;

drop table if exists t_project_prestore_detail;

drop table if exists t_project_fine_detail;

drop table if exists t_project_product_detail;

drop table if exists t_project_refund_detail;

/*==============================================================*/
/* Table: t_project_account                                     */
/*==============================================================*/
create table t_project_account
(
   id                   varchar(36) not null,
   project_id           varchar(36) not null,
   company_id           varchar(36) not null,
   project_name         varchar(128),
   company_name         varchar(128),
   create_time          timestamp not null,
   update_time          timestamp not null,
   version              int not null,
   signature            varchar(128),
   total_amount         decimal(16,2),
   cycle_amount         decimal(16,2),
   product_amount       decimal(16,2),
   late_amount          decimal(16,2),
   fine_amount          decimal(16,2),
   predeposit_amount    decimal(16,2),
   refund_amount        decimal(16,2),
   primary key (id)
);

/*==============================================================*/
/* Table: t_project_cycle_account                               */
/*==============================================================*/
create table t_project_cycle_account
(
   id                   varchar(36) not null,
   amount               decimal(16,2) not null,
   account_type         int not null comment '水费 电费. 物业 等等
            ',
   project_account_id   varchar(36) not null,
   create_time          timestamp not null,
   update_time          timestamp not null,
   signature            varchar(128),
   primary key (id)
);

/*==============================================================*/
/* Table: t_project_cycle_detail                                */
/*==============================================================*/
create table t_project_cycle_detail
(
   id                   varchar(36) not null,
   cycle_id             varchar(128) not null,
   amount               decimal(16,2) not null,
   create_time          timestamp not null,
   create_by            varchar(36) not null comment '托收的话值为:system.普通则为前台操作人ID',
   description          varchar(30),
   account_type         int not null comment '收费项目类型,水,电,物业,本体',
   charge_type          int comment '账户类型',
   pay_channel          int COMMENT '支付渠道',
   business_type        int COMMENT '业务类型',
   house_code_new           varchar(128),
   business_opera_detail varchar(36) comment '如果是前台收费,需要把资金业务操作明细ID写入,否则写null',
   rate_after           decimal(16,2),
   rate                 decimal(5,4),
   rate_fee             decimal(16,2),
   primary key (id)
);

/*==============================================================*/
/* Table: t_project_delay_account                               */
/*==============================================================*/
create table t_project_delay_account
(
   id                   varchar(36) not null,
   amount               decimal(16,2) not null,
   account_type         int not null comment '水费,电费,物业',
   project_account_id   varchar(36) not null,
   create_time          timestamp not null,
   update_time          timestamp not null,
   signature            varchar(128),
   primary key (id)
);

/*==============================================================*/
/* Table: t_project_delay_detail                                */
/*==============================================================*/
create table t_project_delay_detail
(
   id                   varchar(36) not null,
   delay_account_id     varchar(36) not null,
   amount               decimal(16,2) not null,
   create_time          timestamp not null,
   create_by            varchar(36) not null,
   account_type         int not null comment '1水电,2物业,3物业,4本体',
   house_code_new           varchar(128),
   charge_type          int comment '1前台收费,2托收收费,3线上收费',
   business_opera_detail_id varchar(36) comment '如果是前台收费,需要把资金业务操作明细ID写入,否则写null',
   primary key (id)
);

/*==============================================================*/
/* Table: t_project_prestore_account                             */
/*==============================================================*/
create table t_project_prestore_account
(
   id                   varchar(36) not null,
   amount               decimal(16,2) not null,
   prestore_type          int not null comment '1 通用预存,2专项预存,3押金',
   project_account_id   varchar(36) not null,
   create_time          timestamp not null,
   update_time          timestamp not null,
   signature            varchar(128),
   primary key (id)
);

alter table t_project_prestore_account comment '预存账户';

/*==============================================================*/
/* Table: t_project_prestore_detail                              */
/*==============================================================*/
create table t_project_prestore_detail
(
   id                   varchar(36) not null,
   prestore_account      varchar(36) not null,
   amount               decimal(16,2) not null,
   create_time          timestamp not null,
   create_by            varchar(36) not null,
   type                 int  comment '0:通用预存,1押金,2水费,3电费,3,物业,5本体,.......',
   business_type            int not null comment '1:预存   2：抵扣   3：退款',
   order_id                 VARCHAR(36) comment '订单id',
   house_code_new           varchar(128) comment '通用预存和专项预存时写入数据',
   business_opera_detail_id varchar(36) comment '如果是前台收费,需要把资金业务操作明细ID写入,否则写null',
   primary key (id)
);

alter table t_project_prestore_detail comment '预存账户明细表';

/*==============================================================*/
/* Table: t_project_fine_detail                                 */
/*==============================================================*/
create table t_project_fine_detail
(
   id                   varchar(36) not null,
   project_account_id   varchar(36) not null,
   amount               decimal(16,2) not null,
   create_time          timestamp not null,
   create_by            varchar(36) not null,
   order_id             varchar(36) comment '如果是押金类退款时写入订单ID',
   business_opera_detail_id varchar(36) comment '如果是前台收费,需要把资金业务操作明细ID写入,否则写null',
   primary key (id)
);

alter table t_project_fine_detail comment '罚金账户明细表';

/*==============================================================*/
/* Table: t_project_product_detail                              */
/*==============================================================*/
create table t_project_product_detail
(
   id                   varchar(36) not null,
   project_account_id    varchar(36) not null,
   money                decimal(16,2) not null,
   create_by            varchar(36) not null,
   create_time          timestamp not null,
   description          varchar(128),
   order_id             varchar(36),
   order_detail         text,
   is_asset             int comment '0 关联资产 1未关联资产',
   house_code_new           varchar(128),
   rate_after           decimal(16,2),
   rate                 decimal(5,4),
   rate_fee             decimal(16,2),
   primary key (id)
);

/*==============================================================*/
/* Table: t_project_refund_detail                               */
/*==============================================================*/
create table t_project_refund_detail
(
   id                   varchar(36) not null,
   project_account_id   varchar(36) not null,
   refund_type          int not null comment '1,周期性收费退款,2押金退款',
   amount               decimal(16,2) not null,
   create_time          timestamp not null,
   create_by            varchar(36) not null,
   account_type         int comment '0押金,1水费，2电费，3物业，4本体......',
   order_id             varchar(36) comment '如果是押金类退款时写入订单ID',
   business_opera_detail_id varchar(36) comment '如果是前台收费,需要把资金业务操作明细ID写入,否则写null',
   primary key (id)
);

alter table t_project_refund_detail comment '退费账户明细表';

create index idx_business_opera_detail_id on t_project_prestore_detail(business_opera_detail_id);
create index idx_batch_no on t_bs_pay_info(batch_no);
create index idx_house_code_new on t_project_prestore_detail(house_code_new);

