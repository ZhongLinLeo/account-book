create
database if not exists account_book;
use
account_book;

drop table if exists account;
create table account
(
    account_id           bigint primary key comment '账户 ID',
    account_name         varchar(32) not null comment '账户名称',
    account_describe     varchar(128) comment '账户描述',
    account_ownership_id bigint      not null comment '账户归属人ID',
    account_balance      bigint               default 0 comment '账户余额',
    account_income       bigint               default 0 comment '账户收入',
    account_expenditure  bigint               default 0 comment '账户支出',
    account_type         tinyint              default 0 comment '账户类型，0:储蓄账户，1:信用账户',
    repay_date           date null comment '还款时间，只有信用卡有',
    create_time          timestamp   not null default current_timestamp comment '创建时间',
    modify_time          timestamp   not null ON UPDATE CURRENT_TIMESTAMP default current_timestamp comment '修改时间',
    invalid              tinyint     not null default 0 comment '删除标识位，0：未删除，1：删除'
) engine = INNODB,
  DEFAULT CHARSET = utf8,comment ='账户信息表';

drop table if exists funds_record;
create table funds_record
(
    funds_record_id          bigint primary key comment '资金记录ID',
    funds_record_balance     bigint       not null comment '资金记录金额',
    funds_record_time        timestamp    not null comment '资金记录时间',
    funds_record_describe    varchar(128) not null comment '资金记录描述',
    funds_record_remark      varchar(128) null comment '资金记录备注',
    funds_record_classify_id bigint       not null comment '资金记录分类ID',
    funds_account_id         bigint comment '资金来源账户ID',
    funds_user_id            bigint       not null comment '资金使用人',
    create_time              timestamp    not null default current_timestamp comment '创建时间',
    modify_time              timestamp    not null ON UPDATE CURRENT_TIMESTAMP default current_timestamp comment '修改时间',
    invalid                  tinyint      not null default 0 comment '删除标识位，0：未删除，1：删除'
) engine = INNODB,
  DEFAULT CHARSET = utf8,comment ='资金记录表';

drop table if exists user;
create table user
(
    user_id       bigint primary key comment '用户ID',
    user_name     varchar(8) not null comment '用户名称',
    user_remark   varchar(32) null comment '用户备注',
    user_password varchar(32) null comment '用户密码',
    create_time   timestamp  not null default current_timestamp comment '创建时间',
    modify_time   timestamp  not null ON UPDATE CURRENT_TIMESTAMP default current_timestamp comment '修改时间',
    invalid       tinyint    not null default 0 comment '删除标识位，0：未删除，1：删除'
) engine = INNODB,
  DEFAULT CHARSET = utf8,comment ='用户信息表';

drop table if exists funds_record_classify;
create table funds_record_classify
(
    classify_id       bigint primary key comment '分类ID',
    classify_name     varchar(8) not null comment '分类名称',
    classify_type     tinyint    not null comment '分类类型，0:支出，1:收入',
    classify_describe varchar(128) null comment '分类描述',
    classify_icon     varchar(32) null comment '分类图标',
    default_classify  tinyint             default 0 comment '是否为默认分类，0:否，1:是',
    include_analyze   tinyint             default 0 comment '是否计入收支分析，0:否，1:是',
    create_time       timestamp  not null default current_timestamp comment '创建时间',
    modify_time       timestamp  not null ON UPDATE CURRENT_TIMESTAMP default current_timestamp comment '修改时间',
    invalid           tinyint    not null default 0 comment '删除标识位，0：未删除，1：删除'
) engine = INNODB,
  DEFAULT CHARSET = utf8,comment ='资金分类表';


drop table if exists loan;
create table loan
(
    loan_id      bigint primary key comment '贷款id',
    loan_type    tinyint      null comment '贷款类型，1：房贷，2：消费贷',
    loan_desc    varchar(32)  not null comment '贷款描述',
    loan_amount  bigint       not null comment '贷款金额',
    loan_period  int          not null comment '贷款周期',
    repay_amount bigint       not null comment '还款金额',
    repay_date   date         not null comment '还款时间',
    loan_remark  varchar(128) null comment '贷款备注',
    create_time  timestamp    not null                             default current_timestamp comment '创建时间',
    modify_time  timestamp    not null ON UPDATE CURRENT_TIMESTAMP default current_timestamp comment '修改时间',
    invalid      tinyint      not null                             default 0 comment '删除标识位，0：未删除，1：删除'
) engine = INNODB,
  DEFAULT CHARSET = utf8,comment ='贷款信息表';


drop table if exists loan_rate;
create table loan_rate
(
    id          bigint primary key comment 'id',
    loan_id     bigint       not null comment '贷款id',
    rate        bigint       not null comment '利率',
    rate_desc   varchar(32)  not null comment '利率描述',
    rate_remark varchar(128) null comment '利率备注',
    create_time timestamp    not null                             default current_timestamp comment '创建时间',
    modify_time timestamp    not null ON UPDATE CURRENT_TIMESTAMP default current_timestamp comment '修改时间',
    invalid     tinyint      not null                             default 0 comment '删除标识位，0：未删除，1：删除'
) engine = INNODB,
  DEFAULT CHARSET = utf8,comment ='贷款利率表';

drop table if exists loan_prepay;
create table loan_prepay
(
    id            bigint primary key comment 'id',
    loan_id       bigint       not null comment '贷款id',
    prepay_amount bigint       not null comment '提前还款金额',
    prepay_desc   varchar(32)  not null comment '提前还款描述',
    prepay_remark varchar(128) null comment '提前还款备注',
    prepay_time   timestamp    not null comment '提前还款时间',
    create_time   timestamp    not null                             default current_timestamp comment '创建时间',
    modify_time   timestamp    not null ON UPDATE CURRENT_TIMESTAMP default current_timestamp comment '修改时间',
    invalid       tinyint      not null                             default 0 comment '删除标识位，0：未删除，1：删除'
) engine = INNODB,
  DEFAULT CHARSET = utf8,comment ='贷款提前还款信息表';

drop table if exists loan_repay_change;
create table loan_repay_change
(
    id                  bigint primary key comment 'id',
    loan_id             bigint       not null comment '贷款id',
    repay_change_time   timestamp    not null comment '还款变更时间',
    repay_amount        bigint       not null comment '变更后金额',
    reduce_time         int          not null comment '变更后缩短时间',
    repay_change_desc   varchar(32)  not null comment '提前还款描述',
    repay_change_remark varchar(128) null comment '提前还款备注',
    create_time         timestamp    not null                             default current_timestamp comment '创建时间',
    modify_time         timestamp    not null ON UPDATE CURRENT_TIMESTAMP default current_timestamp comment '修改时间',
    invalid             tinyint      not null                             default 0 comment '删除标识位，0：未删除，1：删除'
) engine = INNODB,
  DEFAULT CHARSET = utf8,comment ='贷款提前还款信息表';



