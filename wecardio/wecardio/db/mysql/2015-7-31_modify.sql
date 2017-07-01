--主键数据初始化
insert into id_maker(maker_name, maker_value, updated) values('t_pub_wf_inst', 10000, 0);
insert into id_maker(maker_name, maker_value, updated) values('t_pub_wf_workitem', 10000, 0);
insert into id_maker(maker_name, maker_value, updated) values('organization', 10000, 0);
insert into id_maker(maker_name, maker_value, updated) values('account_doctor', 10010, 0);
INSERT INTO id_maker (maker_name, maker_value, updated) VALUES ('service_package', 13, 1437035963);
INSERT INTO id_maker (maker_name, maker_value, updated) VALUES ('service_type', 10011, 1437035963);
INSERT INTO id_maker (maker_name, maker_value, updated) VALUES ('profile_patient', 10025, 1437035963);
INSERT INTO id_maker (maker_name, maker_value, updated) VALUES ('account_patient', 10025, 1437035963);


--
-- 增加机构医生资质证书表
--
DROP TABLE IF EXISTS `organization_doctor_pic`;
CREATE TABLE `organization_doctor_pic` (
  `oid` bigint(20) NOT NULL,
  `s_large` varchar(255) DEFAULT NULL,
  `s_medium` varchar(255) DEFAULT NULL,
  `s_source` varchar(255) DEFAULT NULL,
  `s_thumbnail` varchar(255) DEFAULT NULL,
  KEY `FK_ogxed1osdq42dc4iwoa0dwjrn` (`oid`),
  CONSTRAINT `FK_ogxed1osdq42dc4iwoa0dwjrn` FOREIGN KEY (`oid`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 修改医生信息表`profile_doctor`字段`oid`为NULL
--
alter table `profile_doctor` modify `oid` bigint(20) NULL;


--
-- 医生信息表`profile_doctor`增加流程`guid`字段
--
alter table `profile_doctor` add `guid` bigint(20) DEFAULT NULL;


--
-- 增加医生资格证书图片表`profile_doctor_pic`
--
DROP TABLE IF EXISTS `profile_doctor_pic`;
CREATE TABLE `profile_doctor_pic` (
  `did` bigint(20) NOT NULL,
  `s_source` varchar(255) DEFAULT NULL,
  `s_thumbnail` varchar(255) DEFAULT NULL,
  KEY `FK_mtsl4hrparjuekfplw87v4ced` (`did`),
  CONSTRAINT `FK_mtsl4hrparjuekfplw87v4ced` FOREIGN KEY (`did`) REFERENCES `profile_doctor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- `id_maker`触发器，自动更新时间
--
delimiter |
create trigger id_maker_insert_trigger before insert on id_maker for each row 
begin
    set NEW.updated = unix_timestamp(now());
end; 
|

delimiter |
create trigger id_maker_update_trigger before update on id_maker for each row 
begin
    set NEW.updated = unix_timestamp(now());
end; 
|
delimiter ;

-- 机构及平台统计表添加 同步完成字段
ALTER TABLE t_platform_settlement ADD is_synch TINYINT(4) DEFAULT 0 NULL COMMENT '0-否  1-是';
ALTER TABLE orgainization_settlement ADD is_synch TINYINT(4) DEFAULT 0 NULL COMMENT '0-否  1-是';

--2015-8-15 16:08:39
ALTER TABLE t_platform_settlement ADD rate_amount DECIMAL(10,2) NOT NULL COMMENT '平台所得金额';

--2015-8-16 21:45:13
INSERT INTO id_maker (maker_name, maker_value, updated) VALUES ('orgainization_settlement', 10000, 1437035963);
INSERT INTO id_maker (maker_name, maker_value, updated) VALUES ('t_platform_settlement', 10000, 1437035963);


-- 20150817 11:53   消息管理功能脚本
ALTER TABLE `wecardio_plus`.`message`
ADD COLUMN `from_name` VARCHAR(45) NULL AFTER `recevied_time`,
ADD COLUMN `to_name` VARCHAR(45) NULL AFTER `from_name`,
ADD COLUMN `to_type` TINYINT(4) NULL AFTER `to_name`,
ADD COLUMN `from_type` TINYINT(4) NULL AFTER `to_type`;


INSERT INTO `wecardio_plus`.`t_pub_enum` (`ID`, `S_TBL_NAME`, `S_COL_ENAME`, `S_COL_CNAME`, `I_ENUM_VALUE`, `S_ENUM_NAME`, `I_IS_ENABLED`, `I_CASCADE`) VALUES ('12', 'message', 'type', '消息类型', '0', 'admin.message.MessageType.consultationResult', '1', '2');
INSERT INTO `wecardio_plus`.`t_pub_enum` (`ID`, `S_TBL_NAME`, `S_COL_ENAME`, `S_COL_CNAME`, `I_ENUM_VALUE`, `S_ENUM_NAME`, `I_IS_ENABLED`, `I_CASCADE`) VALUES ('13', 'message', 'type', '消息类型', '1', 'admin.message.MessageType.healthMessage', '1', '2');
INSERT INTO `wecardio_plus`.`t_pub_enum` (`ID`, `S_TBL_NAME`, `S_COL_ENAME`, `S_COL_CNAME`, `I_ENUM_VALUE`, `S_ENUM_NAME`, `I_IS_ENABLED`, `I_CASCADE`) VALUES ('14', 'message', 'type', '消息类型', '2', 'admin.message.MessageType.systemMesage', '1', '2');
INSERT INTO `wecardio_plus`.`t_pub_enum` (`ID`, `S_TBL_NAME`, `S_COL_ENAME`, `S_COL_CNAME`, `I_ENUM_VALUE`, `S_ENUM_NAME`, `I_IS_ENABLED`, `I_CASCADE`) VALUES ('15', 'message', 'type', '消息类型', '3', 'admin.message.MessageType.invitePatient', '1', '2');
INSERT INTO `wecardio_plus`.`t_pub_enum` (`ID`, `S_TBL_NAME`, `S_COL_ENAME`, `S_COL_CNAME`, `I_ENUM_VALUE`, `S_ENUM_NAME`, `I_IS_ENABLED`, `I_CASCADE`) VALUES ('16', 'message', 'type', '消息类型', '4', 'admin.message.MessageType.patientComfireAdd', '1', '2');
INSERT INTO `wecardio_plus`.`t_pub_enum` (`ID`, `S_TBL_NAME`, `S_COL_ENAME`, `S_COL_CNAME`, `I_ENUM_VALUE`, `S_ENUM_NAME`, `I_IS_ENABLED`, `I_CASCADE`) VALUES ('17', 'message', 'type', '消息类型', '5', 'admin.message.MessageType.patientRefuse', '1', '2');

UPDATE `wecardio_plus`.`t_pub_enum` SET `I_ENUM_VALUE`='1' WHERE `ID`='12';
UPDATE `wecardio_plus`.`t_pub_enum` SET `I_ENUM_VALUE`='2' WHERE `ID`='13';
UPDATE `wecardio_plus`.`t_pub_enum` SET `I_ENUM_VALUE`='3' WHERE `ID`='14';
UPDATE `wecardio_plus`.`t_pub_enum` SET `I_ENUM_VALUE`='4' WHERE `ID`='15';
UPDATE `wecardio_plus`.`t_pub_enum` SET `I_ENUM_VALUE`='5' WHERE `ID`='16';
UPDATE `wecardio_plus`.`t_pub_enum` SET `I_ENUM_VALUE`='6' WHERE `ID`='17';
INSERT INTO `wecardio_plus`.`t_pub_enum` (`ID`, `S_TBL_NAME`, `S_COL_ENAME`, `S_COL_CNAME`, `I_ENUM_VALUE`, `S_ENUM_NAME`, `I_IS_ENABLED`, `I_CASCADE`) VALUES ('18', 'message', 'type', '消息类型', '7', 'admin.message.MessageType.commonMessage', '1', '2');

ALTER TABLE `wecardio_plus`.`message`
ADD COLUMN `iDeal` TINYINT(4) NULL DEFAULT 0 AFTER `from_type`;

ALTER TABLE `wecardio_plus`.`patient_activity`
ADD COLUMN `create_name` VARCHAR(50) NULL AFTER `start_time`;
