-- 新增是否启用字段
ALTER TABLE service ADD i_is_enabled BIT DEFAULT 1 NOT NULL;
ALTER TABLE service_type ADD i_is_enabled BIT DEFAULT 1 NOT NULL;

-- 2015-7-25 18:30:02 add
-- 新增备注
ALTER TABLE service ADD s_remark VARCHAR(500) NULL;

-- 2015-7-27 11:43:25 add
ALTER TABLE service ADD s_code VARCHAR(100) NULL;

-- 会诊表增加流程guid字段
alter table consultation add guid bigint(20) default null;
-- 会诊表月份字段名拼写错误
alter table consultation change mouth month int(11) default null;

ALTER TABLE profile_patient 
ADD COLUMN head_path VARCHAR(128) NULL AFTER postcode,
ADD COLUMN full_name VARCHAR(64) NULL AFTER head_path;

ALTER TABLE `wecardio_plus`.`profile_patient`
CHANGE COLUMN `head_path` `HeadPath` VARCHAR(128) NULL DEFAULT NULL ;

INSERT INTO `wecardio_plus`.`id_maker` (`maker_name`, `maker_value`, `updated`) VALUES ('patient_activity', '1', '1437035963');
INSERT INTO `wecardio_plus`.`id_maker` (`maker_name`, `maker_value`, `updated`) VALUES ('doctor_opinion', '1', '1437035963');

ALTER TABLE `wecardio_plus`.`patient_activity`
ADD COLUMN `create_name` VARCHAR(50) NULL AFTER `start_time`;

-- 出账信息表增加字段“支付方式”、“银行类型”
alter table `wecardio_plus`.t_out_info add `type` int(11) default null;
alter table `wecardio_plus`.t_out_info add `bank_type` int(11) default null;
-- 结算月份信息表增加字段“提现单据ID”
alter table `wecardio_plus`.t_month_clear add `order_id` bigint(20) not null;
alter table `wecardio_plus`.t_month_clear modify `out_money` decimal(10,0) default null;
-- 机构钱包增加字段“累计分成金额”
alter table `wecardio_plus`.organization_wallet add `grand_total` decimal(10,0) default null;

-- =================================2015-8-17版本======================================
-- 2015-8-18 11:35:00 (确认唯一标识)
ALTER TABLE service_type ADD s_uuid VARCHAR(128) NOT NULL;

ALTER TABLE `patient_activity`
ADD COLUMN `create_name` VARCHAR(50) NULL AFTER `start_time`;

-- =================================2015-9-21版本======================================
INSERT INTO t_pub_enum (ID, S_TBL_NAME, S_COL_ENAME, S_COL_CNAME, I_ENUM_VALUE, S_ENUM_NAME, S_ENUM_ENAME, I_IS_ENABLED, I_CASCADE, I_REL_ENUMID, S_REL_ENMUNAME) VALUES (27, 'evaluate', 'score', '评分', 1, 'common.enum.evaluate.one', null, 1, 2, null, null);
INSERT INTO t_pub_enum (ID, S_TBL_NAME, S_COL_ENAME, S_COL_CNAME, I_ENUM_VALUE, S_ENUM_NAME, S_ENUM_ENAME, I_IS_ENABLED, I_CASCADE, I_REL_ENUMID, S_REL_ENMUNAME) VALUES (28, 'evaluate', 'score', '评分', 2, 'common.enum.evaluate.two', null, 1, 2, null, null);
INSERT INTO t_pub_enum (ID, S_TBL_NAME, S_COL_ENAME, S_COL_CNAME, I_ENUM_VALUE, S_ENUM_NAME, S_ENUM_ENAME, I_IS_ENABLED, I_CASCADE, I_REL_ENUMID, S_REL_ENMUNAME) VALUES (29, 'evaluate', 'score', '评分', 3, 'common.enum.evaluate.three', null, 1, 2, null, null);
INSERT INTO t_pub_enum (ID, S_TBL_NAME, S_COL_ENAME, S_COL_CNAME, I_ENUM_VALUE, S_ENUM_NAME, S_ENUM_ENAME, I_IS_ENABLED, I_CASCADE, I_REL_ENUMID, S_REL_ENMUNAME) VALUES (30, 'evaluate', 'score', '评分', 4, 'common.enum.evaluate.four', null, 1, 2, null, null);
INSERT INTO t_pub_enum (ID, S_TBL_NAME, S_COL_ENAME, S_COL_CNAME, I_ENUM_VALUE, S_ENUM_NAME, S_ENUM_ENAME, I_IS_ENABLED, I_CASCADE, I_REL_ENUMID, S_REL_ENMUNAME) VALUES (31, 'evaluate', 'score', '评分', 5, 'common.enum.evaluate.five', null, 1, 2, null, null);
INSERT INTO t_pub_enum (ID, S_TBL_NAME, S_COL_ENAME, S_COL_CNAME, I_ENUM_VALUE, S_ENUM_NAME, S_ENUM_ENAME, I_IS_ENABLED, I_CASCADE, I_REL_ENUMID, S_REL_ENMUNAME) VALUES (32, 'doctor', 'auditState', '医生状态', 0, 'common.enum.auditState.edit', null, 1, 2, null, null);
INSERT INTO t_pub_enum (ID, S_TBL_NAME, S_COL_ENAME, S_COL_CNAME, I_ENUM_VALUE, S_ENUM_NAME, S_ENUM_ENAME, I_IS_ENABLED, I_CASCADE, I_REL_ENUMID, S_REL_ENMUNAME) VALUES (33, 'doctor', 'auditState', '医生状态', 1, 'common.enum.auditState.audit', null, 1, 2, null, null);
INSERT INTO t_pub_enum (ID, S_TBL_NAME, S_COL_ENAME, S_COL_CNAME, I_ENUM_VALUE, S_ENUM_NAME, S_ENUM_ENAME, I_IS_ENABLED, I_CASCADE, I_REL_ENUMID, S_REL_ENMUNAME) VALUES (34, 'doctor', 'auditState', '医生状态', 2, 'common.enum.auditState.finished', null, 1, 2, null, null);

-- =================================2015-9-28版本======================================
INSERT INTO `t_pub_enum` (`ID`, `S_TBL_NAME`, `S_COL_ENAME`, `S_COL_CNAME`, `I_ENUM_VALUE`, `S_ENUM_NAME`, `I_IS_ENABLED`, `I_CASCADE`) VALUES ('27', 'message', 'UserType', '用户类型', '1', 'admin.main.role.org', '1', '2');
INSERT INTO `t_pub_enum` (`ID`, `S_TBL_NAME`, `S_COL_ENAME`, `S_COL_CNAME`, `I_ENUM_VALUE`, `S_ENUM_NAME`, `I_IS_ENABLED`, `I_CASCADE`) VALUES ('28', 'message', 'UserType', '用户类型', '2', 'admin.main.role.doctor', '1', '2');
INSERT INTO `t_pub_enum` (`ID`, `S_TBL_NAME`, `S_COL_ENAME`, `S_COL_CNAME`, `I_ENUM_VALUE`, `S_ENUM_NAME`, `I_IS_ENABLED`, `I_CASCADE`) VALUES ('29', 'message', 'UserType', '用户类型', '3', 'admin.main.role.patient', '1', '2');

UPDATE `t_pub_enum` SET `I_ENUM_VALUE`='0' WHERE `ID`='20';
UPDATE `t_pub_enum` SET `I_ENUM_VALUE`='1' WHERE `ID`='21';
UPDATE `t_pub_enum` SET `I_ENUM_VALUE`='2' WHERE `ID`='22';
UPDATE `t_pub_enum` SET `I_ENUM_VALUE`='3' WHERE `ID`='23';
UPDATE `t_pub_enum` SET `I_ENUM_VALUE`='4' WHERE `ID`='24';
UPDATE `t_pub_enum` SET `I_ENUM_VALUE`='5' WHERE `ID`='25';
UPDATE `t_pub_enum` SET `I_ENUM_VALUE`='6' WHERE `ID`='26';
INSERT INTO `t_pub_enum` (`ID`, `S_TBL_NAME`, `S_COL_ENAME`, `S_COL_CNAME`, `I_ENUM_VALUE`, `S_ENUM_NAME`, `I_IS_ENABLED`, `I_CASCADE`) VALUES ('30', 'message', 'type', '消息类型', '7', 'admin.message.MessageType.doctorOpinionMessage', '1', '2');
INSERT INTO `t_pub_enum` (`ID`, `S_TBL_NAME`, `S_COL_ENAME`, `S_COL_CNAME`, `I_ENUM_VALUE`, `S_ENUM_NAME`, `I_IS_ENABLED`, `I_CASCADE`) VALUES ('31', 'message', 'type', '消息类型', '8', 'admin.message.MessageType.doctorRelievePatientMessage', '1', '2');
INSERT INTO `t_pub_enum` (`ID`, `S_TBL_NAME`, `S_COL_ENAME`, `S_COL_CNAME`, `I_ENUM_VALUE`, `S_ENUM_NAME`, `I_IS_ENABLED`, `I_CASCADE`) VALUES ('32', 'message', 'type', '消息类型', '9', 'admin.message.MessageType.patientRelievePatientMessage', '1', '2');

-- =================================2015-9-30版本======================================
ALTER TABLE `id_maker` CHANGE COLUMN `updated` `updated` INT(11) NOT NULL DEFAULT 0 ;