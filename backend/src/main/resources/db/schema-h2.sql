create table account
(
    account_id          bigint primary key comment '账户 ID',
    account_name        char(16)  not null comment '账户名称',
    account_describe    char(64) comment '账户描述',
    account_balance     bigint             default 0 comment '账户余额',
    account_income      bigint             default 0 comment '账户收入',
    account_expenditure bigint             default 0 comment '账户支出',
    create_time         timestamp not null comment '创建时间',
    modify_time         timestamp not null comment '修改时间',
    invalid             tinyint   not null default 0 comment '删除标识位，0：未删除，1：删除'
);