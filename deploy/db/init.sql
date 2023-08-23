create
database account_book;
use
account_book;

drop table if exists account;
create table account
(
    account_id           bigint primary key comment '账户 ID',
    account_name         varchar(32) not null comment '账户名称',
    account_describe     varchar(128) comment '账户描述',
    account_ownership_id bigint      not null comment '账户归属人ID',
    account_creator_id   bigint      not null comment '账户创建人ID',
    account_balance      bigint               default 0 comment '账户余额',
    account_income       bigint               default 0 comment '账户收入',
    account_expenditure  bigint               default 0 comment '账户支出',
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
    classify_id        bigint primary key comment '分类ID',
    classify_name      varchar(8) not null comment '分类名称',
    classify_type      tinyint    not null comment '分类类型，0:支出，1:收入',
    classify_describe  varchar(128) null comment '分类描述',
    parent_classify_id bigint null comment '父分类ID',
    create_time        timestamp  not null default current_timestamp comment '创建时间',
    modify_time        timestamp  not null ON UPDATE CURRENT_TIMESTAMP default current_timestamp comment '修改时间',
    invalid            tinyint    not null default 0 comment '删除标识位，0：未删除，1：删除'
)engine = INNODB,
  DEFAULT CHARSET = utf8,comment ='资金分类表';