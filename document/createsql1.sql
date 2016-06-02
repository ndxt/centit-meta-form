CREATE TABLE F_MD_REL_DETIAL  (
   "RELATION_ID"        VARCHAR2(32)                    NOT NULL,
   "PARENT_COLUMN_NAME" VARCHAR2(32)                    NOT NULL,
   "CHILD_COLUMN_NAME"  VARCHAR2(32)                    NOT NULL,
   CONSTRAINT PK_F_MD_REL_DETIAL PRIMARY KEY ("RELATION_ID", "PARENT_COLUMN_NAME")
);
CREATE TABLE F_MD_RELATION  (
   "RELATION_ID"        NUMBER(12)                      NOT NULL,
   "PARENT_TABLE_ID"    NUMBER(12),
   "CHILD_TABLE_ID"     NUMBER(12),
   "RELATION_NAME"      VARCHAR2(64)                    NOT NULL,
   "RELATION_STATE"     CHAR                            NOT NULL,
   "RELATION_COMMENT"   VARCHAR2(256),
   "LAST_MODIFY_DATE"   DATE,
   "RECORDER"           VARCHAR2(8),
   CONSTRAINT PK_F_MD_RELATION PRIMARY KEY ("RELATION_ID")
);
CREATE TABLE F_MD_COLUMN  (
   "TABLE_ID"           NUMBER(12)                      NOT NULL,
   "COLUMN_NAME"        VARCHAR2(32)                    NOT NULL,
   "FIELD_LABEL_NAME"   VARCHAR2(64)                    NOT NULL,
   "COLUMN_COMMENT"     VARCHAR2(256),
   "COLUMN_ORDER"       NUMBER(3)                      DEFAULT 99,
   "COLUMN_TYPE"        VARCHAR2(32)                    NOT NULL,
   "MAX_LENGTH"         NUMBER(6),
   "SCALE"              NUMBER(3),
   "ACCESS_TYPE"        CHAR                            NOT NULL,
   "MANDATORY"          CHAR,
   "PRIMARYKEY"         CHAR,
   "COLUMN_STATE"       CHAR                            NOT NULL,
   "REFERENCE_TYPE"     CHAR,
   "REFERENCE_DATA"     VARCHAR2(1000),
   "VALIDATE_REGEX"     VARCHAR2(200),
   "VALIDATE_INFO"      VARCHAR2(200),
   "DEFAULT_VALUE"      VARCHAR2(200),
   "LAST_MODIFY_DATE"   DATE,
   "RECORDER"           VARCHAR2(8),
   CONSTRAINT PK_F_MD_COLUMN PRIMARY KEY ("TABLE_ID", "COLUMN_NAME")
);

CREATE TABLE F_MD_TABLE  (
   "TABLE_ID"           NUMBER(12)                      NOT NULL,
   "DATABASE_CODE"      VARCHAR2(32),
   "TABLE_NAME"         VARCHAR2(32)                    NOT NULL,
   "TABLE_LABEL_NAME"   VARCHAR2(64)                    NOT NULL,
   "TABLE_TYPE"         CHAR                            NOT NULL,
   "TABLE_STATE"        CHAR                            NOT NULL,
   "TABLE_COMMENT"      VARCHAR2(256),
   "IS_IN_WORKFLOW"     CHAR                           DEFAULT '0' NOT NULL,
   "LAST_MODIFY_DATE"   DATE,
   "RECORDER"           VARCHAR2(8),
   CONSTRAINT PK_F_MD_TABLE PRIMARY KEY ("TABLE_ID")
);
CREATE TABLE "M_META_FORM_MODEL"  (
   "MODEL_CODE"         VARCHAR2(16)                    NOT NULL,
   "TABLE_ID"           NUMBER(12),
   "MODEL_COMMENT"      VARCHAR2(256),
   "MODEL_NAME"         VARCHAR2(64)                    NOT NULL,
   "ACCESS_TYPE"        CHAR,
   "RELATION_TYPE"      CHAR,
   "PARENT_MODEL_CODE"  VARCHAR2(16),
   "DISPLAY_ORDER"      NUMBER(4),
   "LAST_MODIFY_DATE"   DATE,
   "RECORDER"           VARCHAR2(8),
   CONSTRAINT PK_M_META_FORM_MODEL PRIMARY KEY ("MODEL_CODE")
);
CREATE TABLE "M_MODEL_DATA_FIELD"  (
   "MODEL_CODE"         VARCHAR2(16)                    NOT NULL,
   "COLUMN_NAME"        VARCHAR2(32)                    NOT NULL,
   "ACCESS_TYPE"        CHAR,
   "DISPLAY_ORDER"      NUMBER(4),
   "INPUT_HINT"         VARCHAR2(32),
   "VALIDATE_HINT"      VARCHAR2(256),
   "FIELD_HEIGHT"       NUMBER(4),
   "LABEL_LENGTH"       NUMBER(4),
   "FIELD_LENGTH"       NUMBER(4),
   CONSTRAINT PK_M_MODEL_DATA_FIELD PRIMARY KEY ("MODEL_CODE", "COLUMN_NAME")
);