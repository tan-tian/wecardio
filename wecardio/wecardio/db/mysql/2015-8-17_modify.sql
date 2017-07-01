-- 基于客户8月12日提供的数据库基础上调整

--主键
INSERT INTO id_maker (maker_name, maker_value, updated) VALUES ('profile_patient', 10025, 1437035963);
INSERT INTO id_maker (maker_name, maker_value, updated) VALUES ('account_patient', 10025, 1437035963);
INSERT INTO id_maker (maker_name, maker_value, updated) VALUES ('orgainization_settlement', 10000, 1437035963);
INSERT INTO id_maker (maker_name, maker_value, updated) VALUES ('t_platform_settlement', 10000, 1437035963);
UPDATE id_maker set maker_value = 10099 where maker_name = 'message';

--医生
alter table `profile_doctor` modify `oid` bigint(20) NULL;

--医生图像
DROP TABLE IF EXISTS `profile_doctor_pic`;
CREATE TABLE `profile_doctor_pic` (
  `did` bigint(20) NOT NULL,
  `s_source` varchar(255) DEFAULT NULL,
  `s_thumbnail` varchar(255) DEFAULT NULL,
  KEY `FK_mtsl4hrparjuekfplw87v4ced` (`did`),
  CONSTRAINT `FK_mtsl4hrparjuekfplw87v4ced` FOREIGN KEY (`did`) REFERENCES `profile_doctor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--销售统计表
ALTER TABLE t_platform_settlement ADD is_synch TINYINT(4) DEFAULT 0 NULL COMMENT '0-否  1-是';
ALTER TABLE orgainization_settlement ADD is_synch TINYINT(4) DEFAULT 0 NULL COMMENT '0-否  1-是';
ALTER TABLE t_platform_settlement ADD rate_amount DECIMAL(10,2) NOT NULL COMMENT '平台所得金额';


ALTER TABLE `message`
ADD COLUMN `from_name` VARCHAR(45) NULL AFTER `recevied_time`,
ADD COLUMN `to_name` VARCHAR(45) NULL AFTER `from_name`,
ADD COLUMN `to_type` TINYINT(4) NULL AFTER `to_name`,
ADD COLUMN `from_type` TINYINT(4) NULL AFTER `to_type`,
ADD COLUMN `iDeal` TINYINT(4) NULL DEFAULT 0 AFTER `from_type`;

INSERT INTO `t_pub_enum` (`ID`, `S_TBL_NAME`, `S_COL_ENAME`, `S_COL_CNAME`, `I_ENUM_VALUE`, `S_ENUM_NAME`, `I_IS_ENABLED`, `I_CASCADE`) VALUES ('12', 'message', 'type', '消息类型', '0', 'admin.message.MessageType.consultationResult', '1', '2');
INSERT INTO `t_pub_enum` (`ID`, `S_TBL_NAME`, `S_COL_ENAME`, `S_COL_CNAME`, `I_ENUM_VALUE`, `S_ENUM_NAME`, `I_IS_ENABLED`, `I_CASCADE`) VALUES ('13', 'message', 'type', '消息类型', '1', 'admin.message.MessageType.healthMessage', '1', '2');
INSERT INTO `t_pub_enum` (`ID`, `S_TBL_NAME`, `S_COL_ENAME`, `S_COL_CNAME`, `I_ENUM_VALUE`, `S_ENUM_NAME`, `I_IS_ENABLED`, `I_CASCADE`) VALUES ('14', 'message', 'type', '消息类型', '2', 'admin.message.MessageType.systemMesage', '1', '2');
INSERT INTO `t_pub_enum` (`ID`, `S_TBL_NAME`, `S_COL_ENAME`, `S_COL_CNAME`, `I_ENUM_VALUE`, `S_ENUM_NAME`, `I_IS_ENABLED`, `I_CASCADE`) VALUES ('15', 'message', 'type', '消息类型', '3', 'admin.message.MessageType.invitePatient', '1', '2');
INSERT INTO `t_pub_enum` (`ID`, `S_TBL_NAME`, `S_COL_ENAME`, `S_COL_CNAME`, `I_ENUM_VALUE`, `S_ENUM_NAME`, `I_IS_ENABLED`, `I_CASCADE`) VALUES ('16', 'message', 'type', '消息类型', '4', 'admin.message.MessageType.patientComfireAdd', '1', '2');
INSERT INTO `t_pub_enum` (`ID`, `S_TBL_NAME`, `S_COL_ENAME`, `S_COL_CNAME`, `I_ENUM_VALUE`, `S_ENUM_NAME`, `I_IS_ENABLED`, `I_CASCADE`) VALUES ('17', 'message', 'type', '消息类型', '5', 'admin.message.MessageType.patientRefuse', '1', '2');
INSERT INTO `t_pub_enum` (`ID`, `S_TBL_NAME`, `S_COL_ENAME`, `S_COL_CNAME`, `I_ENUM_VALUE`, `S_ENUM_NAME`, `I_IS_ENABLED`, `I_CASCADE`) VALUES ('18', 'message', 'type', '消息类型', '7', 'admin.message.MessageType.commonMessage', '1', '2');

UPDATE `t_pub_enum` SET `I_ENUM_VALUE`='1' WHERE `ID`='12';
UPDATE `t_pub_enum` SET `I_ENUM_VALUE`='2' WHERE `ID`='13';
UPDATE `t_pub_enum` SET `I_ENUM_VALUE`='3' WHERE `ID`='14';
UPDATE `t_pub_enum` SET `I_ENUM_VALUE`='4' WHERE `ID`='15';
UPDATE `t_pub_enum` SET `I_ENUM_VALUE`='5' WHERE `ID`='16';
UPDATE `t_pub_enum` SET `I_ENUM_VALUE`='6' WHERE `ID`='17';

ALTER TABLE `organization_doctor_pic` ADD COLUMN `id` bigint(20) NOT NULL;
ALTER TABLE `organization_doctor_pic` ADD PRIMARY KEY (`id`);

ALTER TABLE `patient_activity`
ADD COLUMN `create_name` VARCHAR(50) NULL AFTER `start_time`;

-- 出账信息表增加字段“支付方式”、“银行类型”
alter table t_out_info add `type` int(11) default null;
alter table t_out_info add `bank_type` int(11) default null;
-- 结算月份信息表增加字段“提现单据ID”
alter table t_month_clear add `order_id` bigint(20) not null;
alter table t_month_clear modify `out_money` decimal(10,0) default null;
-- 机构钱包增加字段“累计分成金额”
alter table organization_wallet add `grand_total` decimal(10,0) default null;
