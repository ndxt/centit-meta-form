drop table if exists D_DataBase_Info;

drop table if exists D_OS_INFO;

drop table if exists F_META_CHANG_LOG;

drop table if exists F_META_COLUMN;

drop table if exists F_META_RELATION;

drop table if exists F_META_REL_DETIAL;

drop table if exists F_META_TABLE;

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
  OS_ID                varchar(20),
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
  OS_ID                varchar(20) not null,
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
  change_ID            numeric(12,0) not null,
  Table_ID             numeric(12,0) comment '表单主键',
  change_Date          datetime not null default NOW(),
  changer              varchar(6) not null,
  change_Script        text,
  change_comment       varchar(2048),
  primary key (change_ID)
);

create table F_META_COLUMN
(
  Table_ID             numeric(12,0) not null comment '表单主键',
  column_Name          varchar(32) not null,
  field_Label_Name     varchar(64) not null,
  column_Comment       varchar(256),
  column_Order         numeric(3,0) default 99,
  column_Type          varchar(32) not null,
  max_Length           numeric(6,0) comment 'precision',
  scale                numeric(3,0),
  access_type          char(1) not null,
  mandatory            char(1),
  primarykey           char(1),
  column_state         char(1) not null,
  reference_Type       char(1) comment ' 0：没有：1： 数据字典(列表)   2： 数据字典(树型)   3：JSON表达式 4：sql语句   5：SQL（树）
            	   9 :框架内置字典（用户、机构、角色等等）  Y：年份 M：月份   F:文件（column_Type 必须为 varchar（64））',
  reference_Data       varchar(1000) comment '根据paramReferenceType类型（1,2,3）填写对应值',
  Validate_Regex       varchar(200) comment 'regex表达式',
  Validate_Info        varchar(200) comment '约束不通过提示信息',
  auto_create_Rule     char(1) comment 'C 常量  U uuid S sequence',
  auto_create_Param    varchar(1000) comment '常量（默认值）或则 sequence名字',
  last_modify_Date     datetime,
  Recorder             varchar(8),
  primary key (Table_ID, column_Name)
);

create table F_META_RELATION
(
  relation_ID          numeric(12,0) not null comment '关联关系，类似与外键，但不创建外键',
  Parent_Table_ID      numeric(12,0) comment '表单主键',
  Child_Table_ID       numeric(12,0) comment '表单主键',
  relation_name        varchar(64) not null,
  relation_state       char(1) not null,
  relation_comment     varchar(256),
  last_modify_Date     datetime,
  Recorder             varchar(8),
  primary key (relation_ID)
);

create table F_META_REL_DETIAL
(
  relation_ID          varchar(32) not null,
  parent_column_Name   varchar(32) not null,
  child_column_Name    varchar(32) not null,
  primary key (relation_ID, parent_column_Name)
);

create table F_META_TABLE
(
  Table_ID             numeric(12,0) not null comment '表编号',
  Database_Code        varchar(32),
  table_type           char(1) not null comment '表/视图/已存在表的扩展字段    目前只能是表',
  Table_Name           varchar(64) not null,
  Table_Label_Name     varchar(100) not null,
  EXT_COLUMN_NAME      varchar(64) comment '扩展字段名成(字段类型 必须是 CLOB 或者 TEXT )',
  EXT_COLUMN_FROMAT    varchar(10) comment 'XML\JSON',
  Recorder             varchar(8),
  table_state          char(1) not null comment '系统 S / R 查询(只读)/ N 新建(读写)',
  table_Comment        varchar(256),
  Workflow_OPT_TYPE    char(1) not null default '0' comment '0: 不关联工作流 1：和流程业务关联 2： 和流程过程关联
            1, 添加  WFINSTID 流程实例ID
            2, 添加 NODEINSTID WFINSTID	节点实例编号 流程实例ID


            Name	Code	Comment	Data Type	Length	Precision	Primary	Foreign Key	Mandatory
            节点实例编号	NODEINSTID		NUMBER(12)	12		TRUE	FALSE	TRUE
            流程实例ID	WFINSTID		NUMBER(12)	12		FALSE	TRUE	FALSE',
  update_check_timestamp char(1) comment 'Y/N 更新时是否校验时间戳 添加 Last_modify_time datetime',
  last_modify_Date     datetime,
  primary key (Table_ID)
);

alter table F_META_TABLE comment '状态分为 系统/查询/更新
系统，不可以做任何操作
查询，仅用于通用查询模块，不可以更新
                                 -&';

create table F_PENDING_META_COLUMN
(
  Table_ID             numeric(12,0) not null comment '表单主键',
  column_Name          varchar(32) not null,
  field_Label_Name     varchar(64) not null,
  column_Comment       varchar(256),
  column_Order         numeric(3,0) default 99,
  column_Type          varchar(32) not null,
  max_Length           numeric(6,0) comment 'precision',
  scale                numeric(3,0),
  access_type          char(1) not null,
  mandatory            char(1),
  primarykey           char(1),
  column_state         char(1) not null,
  reference_Type       char(1) comment ' 0：没有：1： 数据字典(列表)   2： 数据字典(树型)   3：JSON表达式 4：sql语句   5：SQL（树）
            	   9 :框架内置字典（用户、机构、角色等等）  Y：年份 M：月份   F:文件（column_Type 必须为 varchar（64））',
  reference_Data       varchar(1000) comment '根据paramReferenceType类型（1,2,3）填写对应值',
  Validate_Regex       varchar(200) comment 'regex表达式',
  Validate_Info        varchar(200) comment '约束不通过提示信息',
  auto_create_Rule     char(1),
  auto_create_Param    varchar(1000),
  last_modify_Date     datetime,
  Recorder             varchar(8),
  primary key (Table_ID)
);

create table F_PENDING_META_RELATION
(
  relation_ID          numeric(12,0) not null comment '关联关系，类似与外键，但不创建外键',
  Parent_Table_ID      numeric(12,0) comment '表单主键',
  Child_Table_ID       numeric(12,0) comment '表单主键',
  relation_name        varchar(64) not null,
  relation_state       char(1) not null,
  relation_comment     varchar(256),
  last_modify_Date     datetime,
  Recorder             varchar(8),
  primary key (relation_ID)
);

create table F_PENDING_META_REL_DETIAL
(
  relation_ID          varchar(32) not null,
  parent_column_Name   varchar(32) not null,
  child_column_Name    varchar(32) not null,
  primary key (relation_ID, parent_column_Name)
);

create table F_PENDING_META_TABLE
(
  Table_ID             numeric(12,0) not null comment '表单主键',
  Database_Code        varchar(32),
  table_type           char(1) not null comment '表/视图 目前只能是表',
  Table_Name           varchar(64) not null,
  Table_Label_Name     varchar(100) not null,
  EXT_COLUMN_NAME      varchar(64) comment '扩展字段名成(字段类型 必须是 CLOB 或者 TEXT )',
  EXT_COLUMN_FROMAT    varchar(10) comment 'XML\JSON',
  table_state          char(1) not null comment '系统 S / R 查询(只读)/ N 新建(读写)',
  table_Comment        varchar(256),
  Workflow_OPT_TYPE    char(1) not null default '0' comment '0: 不关联工作流 1：和流程业务关联 2： 和流程过程关联',
  update_check_timestamp char(1) comment 'Y/N 更新时是否校验时间戳',
  last_modify_Date     datetime,
  Recorder             varchar(8),
  primary key (Table_ID)
);

create table M_Meta_Form_Model
(
  Model_Code           varchar(16) not null,
  Table_ID             numeric(12,0) comment '表单主表',
  Model_Comment        varchar(256),
  Model_Name           varchar(64) not null,
  Access_Type          char(1) comment 'R 只读（视图、查询），A  新增（只能新增一条），W 修改 ，L 编辑列表（增删改）',
  form_template        varchar(128),
  list_as_tree         char(1),
  Relation_type        char(1) comment '0 没有父模块  1  一对一，2 多对一',
  Parent_Model_Code    varchar(16) comment '子模块必需对应父模块对应的子表',
  Display_Order        numeric(4,0),
  last_modify_Date     datetime,
  Recorder             varchar(8),
  extend_Options       varchar(800),
  extend_opt_bean      varchar(64) comment '实现特定接口的bean，这个可以在业务保存、提交、修改、删除的时候调用对应的业务处理方法',
  extend_opt_bean_param varchar(800) comment 'json String 格式的参数',
  Data_filter_Sql      varchar(800) comment '条件语句',
  REL_WFCODE           varchar(32),
  primary key (Model_Code)
);

create table M_Model_Data_Field
(
  Model_Code           varchar(16) not null,
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
  Model_Code           varchar(16) not null comment '所属（关联）',
  OPERATION            varchar(32) not null,
  OPT_Model_Code       varchar(16) comment '一个模块中的操作可能是针对其他模块的',
  method               varchar(16),
  label              varchar(32),
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
