drop table if exists D_DataBase_Info;

drop table if exists D_OS_INFO;

drop table if exists F_META_CHANG_LOG;

drop table if exists F_MD_COLUMN;

drop table if exists F_MD_RELATION;

drop table if exists F_MD_REL_DETIAL;

drop table if exists F_MD_TABLE;

drop table if exists F_PENDING_META_COLUMN;

drop table if exists F_PENDING_META_RELATION;

drop table if exists F_PENDING_META_REL_DETIAL;

drop table if exists F_PENDING_META_TABLE;

drop table if exists M_Meta_Form_Model;

drop table if exists M_Model_Data_Field;

drop table if exists M_Model_OPERATION;

create table D_DataBase_Info
(
  Database_Code        varchar(32) not null,
  database_name        varchar(100),
  OS_ID                varchar(64),
  database_url         varchar(1000),
  username             varchar(100),
  password             varchar(100) comment '加密',
  database_desc        varchar(500),
  last_Modify_DATE     datetime,
  create_time          datetime,
  created              varchar(8),
  primary key (Database_Code)
);

create table D_OS_INFO
(
  OS_ID                varchar(64) not null,
  OS_NAME              varchar(200) not null,
  OS_URL               char(10),
  DDE_SYNC_URL         char(10) comment '这个仅供DDE使用',
  SYS_DATA_PUSH_OPTION char(10),
  last_Modify_DATE     datetime,
  create_time          datetime,
  created              varchar(8),
  primary key (OS_ID)
);

create table F_META_CHANG_LOG
(
  change_ID            varchar(64) not null,
  DATABASE_CODE        varchar(32),
  Table_ID             varchar(32) comment '表单主键',
  change_Date          datetime not null default NOW(),
  changer              varchar(6) not null,
  change_Script        longtext,
  change_comment       varchar(2048),
  primary key (change_ID)
);

CREATE TABLE `f_md_column` (
  `TABLE_ID` varchar(64) NOT NULL,
  `COLUMN_NAME` varchar(32) NOT NULL,
  `FIELD_LABEL_NAME` varchar(64) DEFAULT NULL,
  `COLUMN_LENGTH` decimal(6,0) DEFAULT NULL,
  `SCALE` decimal(3,0) DEFAULT NULL,
  `ACCESS_TYPE` char(1) NOT NULL,
  `COLUMN_TYPE` varchar(32) DEFAULT NULL,
  `FIELD_TYPE` varchar(32) DEFAULT NULL,
  `PRIMARY_KEY` char(1) DEFAULT NULL,
  `MANDATORY` char(1) DEFAULT NULL,
  `COLUMN_COMMENT` varchar(1024) DEFAULT NULL,
  `COLUMN_ORDER` decimal(3,0) DEFAULT '99',
  `LAST_MODIFY_DATE` datetime DEFAULT NULL,
  `RECORDER` varchar(32) DEFAULT NULL,
  `REFERENCE_TYPE` varchar(1) DEFAULT NULL,
  `REFERENCE_DATA` varchar(256) DEFAULT NULL,
  `VALIDATE_REGEX` varchar(32) DEFAULT NULL,
  `VALIDATE_INFO` varchar(32) DEFAULT NULL,
  `AUTO_CREATE_RULE` varchar(1) DEFAULT NULL,
  `AUTO_CREATE_PARAM` varchar(16) DEFAULT NULL,
  `WORKFLOW_VARIABLE_TYPE` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`TABLE_ID`,`COLUMN_NAME`)
);

CREATE TABLE `f_md_relation` (
  `RELATION_ID` varchar(64) NOT NULL,
  `PARENT_TABLE_ID` varchar(64) NOT NULL,
  `CHILD_TABLE_ID` varchar(64) NOT NULL,
  `RELATION_NAME` varchar(64) NOT NULL,
  `RELATION_STATE` char(1) NOT NULL,
  `RELATION_COMMENT` varchar(256) DEFAULT NULL,
  `last_modify_Date` datetime DEFAULT NULL,
  `RECORDER` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`RELATION_ID`)
);

CREATE TABLE `f_md_rel_detail` (
  `RELATION_ID` varchar(64) NOT NULL,
  `PARENT_COLUMN_CODE` varchar(32) NOT NULL,
  `CHILD_COLUMN_CODE` varchar(32) NOT NULL,
  PRIMARY KEY (`RELATION_ID`,`PARENT_COLUMN_CODE`)
);

CREATE TABLE `f_md_table` (
  `TABLE_ID` varchar(64) NOT NULL,
  `TABLE_LABEL_NAME` varchar(500) NOT NULL,
  `DATABASE_CODE` varchar(32) NOT NULL COMMENT '数据库代码',
  `TABLE_NAME` varchar(500) DEFAULT NULL,
  `TABLE_TYPE` char(1) NOT NULL COMMENT '表/视图 目前只能是表',
  `ACCESS_TYPE` char(1) NOT NULL COMMENT '系统 S / R 查询(只读)/ N 新建(读写)',
  `TABLE_COMMENT` varchar(256) DEFAULT NULL,
  `WORKFLOW_OPT_TYPE` char(1) NOT NULL DEFAULT '0',
  `Record_Date` datetime DEFAULT NULL,
  `Recorder` varchar(32) DEFAULT NULL,
  `UPDATE_CHECK_TIMESTAMP` char(1) DEFAULT NULL,
  `FULLTEXT_SEARCH` char(1) DEFAULT NULL,
  `WRITE_OPT_LOG` char(1) DEFAULT NULL,
  PRIMARY KEY (`TABLE_ID`)
);


CREATE TABLE `f_pending_meta_column` (
  `Table_ID` varchar(64) NOT NULL COMMENT '表单主键',
  `column_Name` varchar(32) NOT NULL,
  `field_Label_Name` varchar(64) NOT NULL,
  `column_Comment` varchar(1000) DEFAULT NULL,
  `column_Order` decimal(3,0) DEFAULT '99',
  `FIELD_TYPE` varchar(32) NOT NULL,
  `max_Length` decimal(12,0) DEFAULT NULL COMMENT 'precision',
  `scale` decimal(3,0) DEFAULT NULL,
  `mandatory` char(1) DEFAULT NULL,
  `primary_key` char(1) DEFAULT NULL,
  `last_modify_Date` datetime DEFAULT NULL,
  `Recorder` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`Table_ID`,`column_Name`)
);

CREATE TABLE `f_pending_meta_table` (
  `Table_ID` varchar(64) NOT NULL COMMENT '表单主键',
  `PRIMARY_KEY` varchar(64) DEFAULT NULL,
  `Database_Code` varchar(32) DEFAULT NULL,
  `Table_Name` varchar(64) NOT NULL,
  `Table_Label_Name` varchar(100) NOT NULL,
  `table_Comment` varchar(256) DEFAULT NULL,
  `table_state` char(1) NOT NULL COMMENT '系统 S / R 查询(只读)/ N 新建(读写)',
  `Workflow_OPT_TYPE` char(1) NOT NULL DEFAULT '0' COMMENT '0: 不关联工作流 1：和流程业务关联 2： 和流程过程关联',
  `update_check_timestamp` char(1) DEFAULT NULL COMMENT 'Y/N 更新时是否校验时间戳',
  `last_modify_Date` datetime DEFAULT NULL,
  `Recorder` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`Table_ID`)
);

CREATE TABLE `m_meta_form_model` (
  `MODEL_ID` varchar(64) NOT NULL,
  `Table_ID` varchar(64) DEFAULT NULL COMMENT '表单主表',
  `Model_Name` varchar(64) NOT NULL,
  `APPLICATION_ID` varchar(64) DEFAULT NULL,
  `Model_Type` char(1) DEFAULT NULL COMMENT ' N 正常表单 S 子模块表单 L 列表表单',
  `RELATION_ID` varchar(64) DEFAULT NULL,
  `form_template` longtext,
  `REL_FLOW_CODE` varchar(32) DEFAULT NULL,
  `last_modify_Date` datetime DEFAULT NULL,
  `Recorder` varchar(8) DEFAULT NULL,
  `Model_Comment` varchar(256) DEFAULT NULL,
  `extend_opt_js` longtext,
  `Data_filter_Sql` varchar(2000) DEFAULT NULL COMMENT '条件语句',
  `mode_Opt_Url` varchar(800) DEFAULT NULL COMMENT 'json String 格式的参数',
  `FLOW_OPT_TITLE` varchar(500) DEFAULT NULL,
  `DATABASE_CODE` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '数据库代码',
  PRIMARY KEY (`MODEL_ID`)
);
/*
create table M_Model_Data_Field
(
  Model_Code           varchar(32) not null,
  column_Name          varchar(32) not null,
  column_type          char(1) not null comment '表字段，关联只读字段（reference_Data 中为关联SQL语句）',
  Access_Type          char(1) default 'W' comment 'H 隐藏  R 只读 C 新建是可以编辑 F 非空时可以编辑 N 正常编辑',
  Display_Order        numeric(4,0),
  input_TYPE           varchar(32),
  input_hint           varchar(256) comment '与系统关联，比如自动与登录用户代码关联，选择系统用户，选择系统机构，等等',
  reference_Type       char(1) comment ' 0：没有：1： 数据字典(列表)   2： 数据字典(树型)   3：JSON表达式 4：sql语句   5：SQL（树）
            	   9 :框架内置字典（用户、机构、角色等等）  Y：年份 M：月份   F:文件（column_Type 必须为 varchar（64））',
  reference_Data       varchar(500) comment '根据paramReferenceType类型（1,2,3）填写对应值',
  Validate_Regex       varchar(200) comment 'regex表达式',
  Validate_Info        varchar(200) comment '约束不通过提示信息',
  default_Value        varchar(200) comment '参数默认值',
  Validate_hint        varchar(256),
  filter_type          char(2) comment 'HI 查询时这个字段隐藏 NO 没有， MC （match)  LT 小于 GT 大于 EQ 等于 BT 介于  LE 小于等于 GE 大于等于 ',
  mandatory            char(1),
  focus                char(1),
  url                  varchar(256),
  extend_Options       varchar(1000),
  field_height         numeric(4,0) default 1 comment '网格布局默认为 1',
  field_width          numeric(4,0) default 1 comment '网格布局默认为 1',
  view_format          varchar(50) comment '显示时格式化，针对number和datetime类型，目前仅实现日期类型',
  primary key (Model_Code, column_Name)
);

create table M_Model_OPERATION
(
  Model_Code           varchar(32) not null comment '所属（关联）',
  OPERATION            varchar(32) not null,
  OPT_Model_Code       varchar(16) comment '一个模块中的操作可能是针对其他模块的',
  method               varchar(16),
  label                varchar(32),
  DATA_RELATION_TYPE   varchar(1) comment 'L: list 列表   N ：不关联数据   S：单选择  M多选',
  Display_Order        numeric(4,0),
  open_type            varchar(1) comment '0：没有：1： 提示信息  2：只读表单  3：读写表单  ',
  return_operation     varchar(1) comment '0：不操作 1： 刷新页面  2：删除当前行 3：更新当前行',
  opt_hint_title       varchar(100),
  opt_hint_info        varchar(500) comment '操作前提示信息',
  extend_Options       varchar(1000),
  OPT_MESSAGE          varchar(500),
  primary key (Model_Code, OPERATION)
);
*/
create table M_APPLICATION_INFO
(
    APPLICATION_ID varchar(32) not null primary key,
    APPLICATION_ID_NAME varchar(200),
    PAGE_FLOW longtext
);
