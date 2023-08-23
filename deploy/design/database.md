# 表结构信息

## account

账户表信息

|       字段名称        |   字段类型    |  默认值   |            备注            |
| -------------------- | ------------ | -------- | -------------------------- |
| account_id           | bigint       | not null | 账户ID                     |
| account_name         | varchar(32)  | not null | 账户名称                    |
| account_describe     | varchar(128) | not null | 账户描述                    |
| account_ownership_id | bigint       | not null | 账户归属人ID                |
| account_creator_id   | bigint       | not null | 账户创建人ID                |
| account_balance      | bigint       | not null | 账户余额                    |
| account_income       | bigint       | null     | 账户收入                    |
| account_expenditure  | bigint       | null     | 账户支出                    |
| create_time          | timestamp    | not null | 创建时间                    |
| modify_time          | timestamp    | not null | 修改时间                    |
| invalid              | tinyint      | 0        | 删除标识位，0：未删除，1：删除 |


## funds_record

资金记录信息表


|          字段名称          |    字段类型    |  默认值   |            备注            |
| ------------------------ | ------------ | -------- | -------------------------- |
| funds_record_id          | bigint       | not null | 资金记录ID                  |
| funds_record_balance     | bigint       | not null | 资金记录金额                 |
| funds_record_time        | timestamp    | not null | 资金记录时间                 |
| funds_record_describe    | varchar(128) | not null | 资金记录描述                 |
| funds_record_remark      | varchar(128) |  null        | 资金记录备注                 |
| funds_record_classify_id | bigint       | not null | 资金记录分类ID               |
| funds_account_id         | bigint       | null     | 资金来源账户ID               |
| funds_user_id            | bigint       | not null | 资金使用人                  |
| create_time              | timestamp    | not null | 创建时间                    |
| modify_time              | timestamp    | not null | 修改时间                    |
| invalid                  | tinyint      | 0        | 删除标识位，0：未删除，1：删除 |


## user

用户信息表

|    字段名称     |   字段类型    |  默认值   |            备注            |
| ------------- | ----------- | -------- | -------------------------- |
| user_id       | bigint      | not null | 用户ID                     |
| user_name     | varchar(8)  | not null | 用户名称                    |
| user_remark   | varchar(32) |  null | 用户备注                    |
| user_password | varchar(32) |  null | 用户密码                    |
| create_time   | timestamp   | not null | 创建时间                    |
| modify_time   | timestamp   | not null | 修改时间                    |
| invalid       | tinyint     | 0        | 删除标识位，0：未删除，1：删除 |


## funds_record_classify

资金分类表

|      字段名称       |    字段类型    |  默认值   |            备注            |
| ------------------ | ------------ | -------- | -------------------------- |
| classify_id        | bigint       | not null | 分类ID                     |
| classify_name      | varchar(8)   | not null | 分类名称                    |
| classify_type      | tinyint      | not null | 分类类型，0:支出，1:收入      |
| classify_describe  | varchar(128) | null     | 分类描述                    |
| parent_classify_id | bigint       | null     | 父分类ID                    |
| create_time        | timestamp    | not null | 创建时间                    |
| modify_time        | timestamp    | not null | 修改时间                    |
| invalid            | tinyint      | 0        | 删除标识位，0：未删除，1：删除 |

