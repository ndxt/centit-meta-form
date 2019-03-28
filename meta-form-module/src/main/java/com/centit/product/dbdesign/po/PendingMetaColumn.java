package com.centit.product.dbdesign.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.centit.product.metadata.po.MetaColumn;
import com.centit.support.database.metadata.TableField;
import com.centit.support.database.utils.DBType;
import com.centit.support.database.utils.FieldType;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.Date;


/**
 * create by scaffold 2016-06-01


  未落实字段元数据表null
*/
@Data
@Entity
@Table(name = "F_PENDING_META_COLUMN")
public class PendingMetaColumn implements TableField, java.io.Serializable {
    private static final long serialVersionUID =  1L;

//    @EmbeddedId
//    private PendingMetaColumnId cid;

    /**
     * 字段名称 null
     */
    @Id
    @Column(name = "TABLE_ID")
    @NotBlank(message = "字段不能为空")
    private String  tableId;

    /**
     * 字段名称 null
     */
    @Id
    @Column(name = "COLUMN_NAME")
    @NotBlank(message = "字段不能为空")
    private String  columnName;

    /**
     * 字段名称 null
     */
    @Column(name = "FIELD_LABEL_NAME")
    @NotBlank(message = "字段不能为空")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String  fieldLabelName;
    /**
     * 字段描述 null
     */
    @Column(name = "COLUMN_COMMENT")
    @Length(max = 256, message = "字段长度不能大于{max}")
    private String  columnComment;
    /**
     * 显示次序 null
     */
    @Column(name = "COLUMN_ORDER")
    private Long  columnOrder;
    /**
     * 字段类型 null
     */
    @Column(name = "COLUMN_TYPE")
    @NotBlank(message = "字段不能为空")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String  columnFieldType;
    /**
     * 字段长度 precision
     */
    @Column(name = "MAX_LENGTH")
    private Integer  maxLengthM;
    /**
     * 字段精度 null
     */
    @Column(name = "SCALE")
    private Integer  scaleM;
    /**
     * 字段类别 null
     */
    @Column(name = "ACCESS_TYPE")
    @NotBlank(message = "字段不能为空")
    @Length(message = "字段长度不能大于{max}")
    private String  accessType;
    /**
     * 是否必填 null
     */
    @Column(name = "MANDATORY")
    @Length( message = "字段长度不能大于{max}")
    private String  mandatory;
    /**
     * 是否为主键 null
     */
    @Column(name = "PRIMARYKEY")
    @Length( message = "字段长度不能大于{max}")
    private String  primarykey;
    /**
     * 状态 null
     */
    @Column(name = "COLUMN_STATE")
    //@NotBlank(message = "字段不能为空")
    @Length( message = "字段长度不能大于{max}")
    private String  columnState;
    /**
     * 引用类型 0：没有：1： 数据字典 2：JSON表达式 3：sql语句  Y：年份 M：月份
     */
    @Column(name = "REFERENCE_TYPE")
    @Length(message = "字段长度不能大于{max}")
    private String  referenceType;
    /**
     * 引用数据 根据paramReferenceType类型（1,2,3）填写对应值
     */
    @Column(name = "REFERENCE_DATA")
    @Length(max = 1000, message = "字段长度不能大于{max}")
    private String  referenceData;
    /**
     * 约束表达式 regex表达式
     */
    @Column(name = "VALIDATE_REGEX")
    @Length(max = 200, message = "字段长度不能大于{max}")
    private String  validateRegex;
    /**
     * 约束提示 约束不通过提示信息
     */
    @Column(name = "VALIDATE_INFO")
    @Length(max = 200, message = "字段长度不能大于{max}")
    private String  validateInfo;

    /**
     * 自动生成规则   C 常量  U uuid S sequence
     */
    @Column(name = "AUTO_CREATE_RULE")
    @Length(max = 200, message = "字段长度不能大于{max}")
    private String  autoCreateRule;

    /**
     * 自动生成参数
     */
    @Column(name = "AUTO_CREATE_PARAM")
    @Length(max = 200, message = "字段长度不能大于{max}")
    private String  autoCreateParam;

    /**
     * 更改时间 null
     */
    @Column(name = "LAST_MODIFY_DATE")
    private Date  lastModifyDate;
    /**
     * 更改人员 null
     */
    @Column(name = "RECORDER")
    @Length(max = 8, message = "字段长度不能大于{max}")
    private String  recorder;

    @Transient
    private DBType databaseType;

    // Constructors
    /** default constructor */
    public PendingMetaColumn() {
        this.columnState="0";
    }

    public PendingMetaColumn(PendingMetaTable mdTable, String columnName) {
//        this.cid= new PendingMetaColumnId(mdTable,columnName);
        this.tableId = mdTable.getTableId();
        this.columnName = columnName;
        this.columnState="0";
    }

    /** minimal constructor */
//    public PendingMetaColumn(
//        PendingMetaColumnId cid,String  fieldLabelName,String  columnType,String  accessType,String  columnState) {
//        this.cid=cid;
//        this.fieldLabelName= fieldLabelName;
//        this.columnFieldType= columnType;
//        this.accessType= accessType;
//        this.columnState= columnState;
//    }

/** full constructor */
    public PendingMetaColumn(String  fieldLabelName,String  columnComment,Long  columnOrder,String  columnType,
            Integer  maxLength,Integer  scale,String  accessType,String  mandatory,String  primarykey,String  columnState,String  referenceType,String  referenceData,String  validateRegex,String  validateInfo,String  defaultValue,Date  lastModifyDate,String  recorder) {

//        this.cid=cid;
        this.fieldLabelName= fieldLabelName;
        this.columnComment= columnComment;
        this.columnOrder= columnOrder;
        this.columnFieldType= columnType;
        this.maxLengthM= maxLength;
        this.scaleM= scale;
        this.accessType= accessType;
        this.mandatory= mandatory;
        this.primarykey= primarykey;
        this.columnState= columnState;
        this.referenceType= referenceType;
        this.referenceData= referenceData;
        this.validateRegex= validateRegex;
        this.validateInfo= validateInfo;
        this.lastModifyDate= lastModifyDate;
        this.recorder= recorder;
    }

    public PendingMetaColumn copy(PendingMetaColumn other){

//        this.setCid(other.getCid());
        this.fieldLabelName= other.getFieldLabelName();
        this.columnComment= other.getColumnComment();
        this.columnOrder= other.getColumnOrder();
        this.columnFieldType= other.getColumnFieldType();
        this.maxLengthM= other.getMaxLengthM();
        this.scaleM= other.getScaleM();
        this.accessType= other.getAccessType();
        this.mandatory= other.isMandatory()?"T":"F";
        this.primarykey= other.getPrimarykey();
        this.columnState= other.getColumnState();
        this.referenceType= other.getReferenceType();
        this.referenceData= other.getReferenceData();
        this.validateRegex= other.getValidateRegex();
        this.validateInfo= other.getValidateInfo();
        this.lastModifyDate= other.getLastModifyDate();
        this.recorder= other.getRecorder();

        return this;
    }

    public PendingMetaColumn copyNotNullProperty(PendingMetaColumn other){


//        if( other.getCid() != null)
//            this.setCid(other.getCid());
        if( other.getFieldLabelName() != null)
            this.fieldLabelName= other.getFieldLabelName();
        if( other.getColumnComment() != null)
            this.columnComment= other.getColumnComment();
        if( other.getColumnOrder() != null)
            this.columnOrder= other.getColumnOrder();
        if( other.getColumnType() != null)
            this.columnFieldType= other.getColumnFieldType();
        if( other.getMaxLengthM() != null)
            this.maxLengthM= other.getMaxLengthM();
        if( other.getScaleM() != null)
            this.scaleM= other.getScaleM();
        if( other.getAccessType() != null)
            this.accessType= other.getAccessType();
        if( other.getMandatory() != null)
            this.mandatory= other.isMandatory()?"T":"F";
        if( other.getPrimarykey() != null)
            this.primarykey= other.getPrimarykey();
        if( other.getColumnState() != null)
            this.columnState= other.getColumnState();
        if( other.getReferenceType() != null)
            this.referenceType= other.getReferenceType();
        if( other.getReferenceData() != null)
            this.referenceData= other.getReferenceData();
        if( other.getValidateRegex() != null)
            this.validateRegex= other.getValidateRegex();
        if( other.getValidateInfo() != null)
            this.validateInfo= other.getValidateInfo();
        if( other.getLastModifyDate() != null)
            this.lastModifyDate= other.getLastModifyDate();
        if( other.getRecorder() != null)
            this.recorder= other.getRecorder();

        return this;
    }

    public PendingMetaColumn clearProperties(){
//        this.setCid(null);
        this.fieldLabelName= null;
        this.columnComment= null;
        this.columnOrder= null;
        this.columnFieldType= null;
        this.accessType= null;
        this.mandatory= null;
        this.primarykey= null;
        this.columnState= null;
        this.referenceType= null;
        this.referenceData= null;
        this.validateRegex= null;
        this.validateInfo= null;
        this.lastModifyDate= null;
        this.recorder= null;

        return this;
    }
    @Override
    public String getPropertyName() {
        return FieldType.mapPropName(getColumnName());
    }
    @Override
    public String getJavaType() {
        return FieldType.mapToJavaType(this.columnFieldType, this.scaleM==null?0:this.scaleM);
    }
    @Override
    public boolean isMandatory() {
        return "T".equals(mandatory) ||  "Y".equals(mandatory) || "1".equals(mandatory);
    }

    public boolean isPrimaryKey() {
        return "T".equals(primarykey) ||  "Y".equals(primarykey) || "1".equals(primarykey);
    }


    @Override
    public int getMaxLength() {
        if("string".equalsIgnoreCase(this.columnFieldType) ||
                "integer".equalsIgnoreCase(this.columnFieldType)||
                "float".equalsIgnoreCase(this.columnFieldType) ||
                "varchar".equalsIgnoreCase(this.columnFieldType)||
                "number".equalsIgnoreCase(this.columnFieldType))
            return maxLengthM==null?0:maxLengthM.intValue();
        return 0;
    }
    @Override
    public int getPrecision() {
        return getMaxLength();
    }
    @Override
    public int getScale() {
        if("float".equalsIgnoreCase(this.columnFieldType) ||
                "number".equalsIgnoreCase(this.columnFieldType))
            return scaleM==null?0:scaleM.intValue();
        return 0;
    }

    @Override
    public String getDefaultValue() {
        return "C".equals(autoCreateRule)?autoCreateParam:null;
    }
    @Override
    @JSONField(serialize=false)
    public String getColumnType() {
        return FieldType.mapToDatabaseType(this.columnFieldType, this.databaseType);
    }

    public MetaColumn mapToMetaColumn(){
        MetaColumn mc = new MetaColumn();
        mc.setTableId(this.getTableId());
        mc.setColumnName(this.getColumnName());
        mc.setColumnName(this.getFieldLabelName());
        mc.setColumnComment(this.getColumnComment());
        mc.setColumnOrder(this.getColumnOrder());
        mc.setColumnType(getColumnFieldType());
        mc.setColumnLength(this.getMaxLength());
        mc.setColumnPrecision(this.getScale());
        mc.setAccessType(this.getAccessType());
        mc.setMandatory(this.isMandatory()?"T":"F");
        mc.setPrimaryKey(this.getPrimarykey());
        mc.setColumnState(this.getColumnState());
        mc.setReferenceType(this.getReferenceType());
        mc.setReferenceData(this.getReferenceData());
        mc.setValidateRegex(this.getValidateRegex());
        mc.setValidateInfo(this.getValidateInfo());
        mc.setAutoCreateRule(this.getAutoCreateRule());
        mc.setAutoCreateParam(this.getAutoCreateParam());
        mc.setLastModifyDate(this.getLastModifyDate());
        mc.setRecorder(this.getRecorder());
        return mc;
    }
}
