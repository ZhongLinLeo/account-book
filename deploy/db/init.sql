INSERT INTO account_book.funds_record_classify (classify_id, classify_name, classify_type, classify_describe,
                                                classify_icon, default_classify, create_time, modify_time, invalid)
VALUES (1000000000000000001, '转出', 0, '转账使用--转出标识', 'classify-transfer-out', 1, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO account_book.funds_record_classify (classify_id, classify_name, classify_type, classify_describe,
                                                classify_icon, default_classify, create_time, modify_time, invalid)
VALUES (1000000000000000002, '转入', 1, '转账使用--转入标识', 'classify-transfer-in', 1, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO account_book.funds_record_classify (classify_id, classify_name, classify_type, classify_describe,
                                                classify_icon, default_classify, create_time, modify_time, invalid)
VALUES (1000000000000000003, '还款转出', 0, '还款使用--转出标识', 'classify-repayment-out', 1, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO account_book.funds_record_classify (classify_id, classify_name, classify_type, classify_describe,
                                                classify_icon, default_classify, create_time, modify_time, invalid)
VALUES (1000000000000000004, '还款转入', 1, '还款使用--转入标识', 'classify-repayment-in', 1, DEFAULT, DEFAULT, DEFAULT);