package com.centit.metaform.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;


/**
 * create by scaffold 2016-06-02 
 
 
  字段元数据表null   
*/
@Entity
@Table(name = "F_MD_COLUMN")
public class MdColumn implements java.io.Serializable {
	private static final long serialVersionUID =  1L;

	@EmbeddedId
	private com.centit.metaform.po.MdColumnId cid;


	/**
	 * 字段名称 null 
	 */
	@Column(name = "field_Label_Name")
	@NotBlank(message = "字段不能为空")
	@Length(min = 0, max = 64, message = "字段长度不能小于{min}大于{max}")
	private String  fieldLabelName;
	
	@JoinColumn(name="TABLE_ID")
	@ManyToOne
	private MdTable mdTable;
	
	/**
	 * 字段描述 null 
	 */
	@Column(name = "column_Comment")
	@Length(min = 0, max = 256, message = "字段长度不能小于{min}大于{max}")
	private String  columnComment;
	/**
	 * 显示次序 null 
	 */
	@Column(name = "column_Order")
	private Long  columnOrder;
	/**
	 * 字段类型 null 
	 */
	@Column(name = "column_Type")
	@NotBlank(message = "字段不能为空")
	@Length(min = 0, max = 32, message = "字段长度不能小于{min}大于{max}")
	private String  columnType;
	/**
	 * 字段长度 precision 
	 */
	@Column(name = "max_Length")
	private Long  maxLength;
	/**
	 * 字段精度 null 
	 */
	@Column(name = "scale")
	private Long  scale;
	/**
	 * 字段类别 null 
	 */
	@Column(name = "access_type")
	@NotBlank(message = "字段不能为空")
	@Length(min = 0,  message = "字段长度不能小于{min}大于{max}")
	private String  accessType;
	/**
	 * 是否必填 null 
	 */
	@Column(name = "mandatory")
	@Length(min = 0,  message = "字段长度不能小于{min}大于{max}")
	private String  mandatory;
	/**
	 * 是否为主键 null 
	 */
	@Column(name = "primarykey")
	@Length(min = 0,  message = "字段长度不能小于{min}大于{max}")
	private String  primarykey;
	/**
	 * 状态 null 
	 */
	@Column(name = "column_state")
	@NotBlank(message = "字段不能为空")
	@Length(min = 0,  message = "字段长度不能小于{min}大于{max}")
	private String  columnState;
	/**
	 * 引用类型 0：没有：1： 数据字典 2：JSON表达式 3：sql语句  Y：年份 M：月份 
	 */
	@Column(name = "reference_Type")
	@Length(min = 0,  message = "字段长度不能小于{min}大于{max}")
	private String  referenceType;
	/**
	 * 引用数据 根据paramReferenceType类型（1,2,3）填写对应值 
	 */
	@Column(name = "reference_Data")
	@Length(min = 0, max = 1000, message = "字段长度不能小于{min}大于{max}")
	private String  referenceData;
	/**
	 * 约束表达式 regex表达式 
	 */
	@Column(name = "Validate_Regex")
	@Length(min = 0, max = 200, message = "字段长度不能小于{min}大于{max}")
	private String  validateRegex;
	/**
	 * 约束提示 约束不通过提示信息 
	 */
	@Column(name = "Validate_Info")
	@Length(min = 0, max = 200, message = "字段长度不能小于{min}大于{max}")
	private String  validateInfo;
	/**
	 * 默认值 参数默认值 
	 */
	@Column(name = "default_Value")
	@Length(min = 0, max = 200, message = "字段长度不能小于{min}大于{max}")
	private String  defaultValue;
	/**
	 * 更改时间 null 
	 */
	@Column(name = "last_modify_Date")
	private Date  lastModifyDate;
	/**
	 * 更改人员 null 
	 */
	@Column(name = "Recorder")
	@Length(min = 0, max = 8, message = "字段长度不能小于{min}大于{max}")
	private String  recorder;

	// Constructors
	/** default constructor */
	public MdColumn() {
	}
	/** minimal constructor */
	public MdColumn(com.centit.metaform.po.MdColumnId id 
				
		,String  fieldLabelName,String  columnType,String  accessType,String  columnState) {
		this.cid = id; 
			
	
		this.fieldLabelName= fieldLabelName; 
		this.columnType= columnType; 
		this.accessType= accessType; 
		this.columnState= columnState; 		
	}

/** full constructor */
	public MdColumn(com.centit.metaform.po.MdColumnId id
			
	,String  fieldLabelName,String  columnComment,Long  columnOrder,String  columnType,Long  maxLength,Long  scale,String  accessType,String  mandatory,String  primarykey,String  columnState,String  referenceType,String  referenceData,String  validateRegex,String  validateInfo,String  defaultValue,Date  lastModifyDate,String  recorder) {
		this.cid = id; 
			
	
		this.fieldLabelName= fieldLabelName;
		this.columnComment= columnComment;
		this.columnOrder= columnOrder;
		this.columnType= columnType;
		this.maxLength= maxLength;
		this.scale= scale;
		this.accessType= accessType;
		this.mandatory= mandatory;
		this.primarykey= primarykey;
		this.columnState= columnState;
		this.referenceType= referenceType;
		this.referenceData= referenceData;
		this.validateRegex= validateRegex;
		this.validateInfo= validateInfo;
		this.defaultValue= defaultValue;
		this.lastModifyDate= lastModifyDate;
		this.recorder= recorder;		
	}

	public com.centit.metaform.po.MdColumnId getCid() {
		return this.cid;
	}
	
	public void setCid(com.centit.metaform.po.MdColumnId id) {
		this.cid = id;
	}
  
	public Long getTableId() {
		if(this.cid==null)
			this.cid = new com.centit.metaform.po.MdColumnId();
		return this.cid.getTableId();
	}
	
	public void setTableId(Long tableId) {
		if(this.cid==null)
			this.cid = new com.centit.metaform.po.MdColumnId();
		this.cid.setTableId(tableId);
	}
  
	public String getColumnName() {
		if(this.cid==null)
			this.cid = new com.centit.metaform.po.MdColumnId();
		return this.cid.getColumnName();
	}
	
	public void setColumnName(String columnName) {
		if(this.cid==null)
			this.cid = new com.centit.metaform.po.MdColumnId();
		this.cid.setColumnName(columnName);
	}
	
	

	// Property accessors
  
	public String getFieldLabelName() {
		return this.fieldLabelName;
	}
	
	public void setFieldLabelName(String fieldLabelName) {
		this.fieldLabelName = fieldLabelName;
	}
  
	public String getColumnComment() {
		return this.columnComment;
	}
	
	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}
  
	public Long getColumnOrder() {
		return this.columnOrder;
	}
	
	public void setColumnOrder(Long columnOrder) {
		this.columnOrder = columnOrder;
	}
  
	public String getColumnType() {
		return this.columnType;
	}
	
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
  
	public Long getMaxLength() {
		return this.maxLength;
	}
	
	public void setMaxLength(Long maxLength) {
		this.maxLength = maxLength;
	}
  
	public Long getScale() {
		return this.scale;
	}
	
	public void setScale(Long scale) {
		this.scale = scale;
	}
  
	public String getAccessType() {
		return this.accessType;
	}
	
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
  
	public String getMandatory() {
		return this.mandatory;
	}
	
	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}
  
	public String getPrimarykey() {
		return this.primarykey;
	}
	
	public void setPrimarykey(String primarykey) {
		this.primarykey = primarykey;
	}
  
	public String getColumnState() {
		return this.columnState;
	}
	
	public void setColumnState(String columnState) {
		this.columnState = columnState;
	}
  
	public String getReferenceType() {
		return this.referenceType;
	}
	
	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}
  
	public String getReferenceData() {
		return this.referenceData;
	}
	
	public void setReferenceData(String referenceData) {
		this.referenceData = referenceData;
	}
  
	public String getValidateRegex() {
		return this.validateRegex;
	}
	
	public void setValidateRegex(String validateRegex) {
		this.validateRegex = validateRegex;
	}
  
	public String getValidateInfo() {
		return this.validateInfo;
	}
	
	public void setValidateInfo(String validateInfo) {
		this.validateInfo = validateInfo;
	}
  
	public String getDefaultValue() {
		return this.defaultValue;
	}
	
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
  
	public Date getLastModifyDate() {
		return this.lastModifyDate;
	}
	
	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
  
	public String getRecorder() {
		return this.recorder;
	}
	
	public void setRecorder(String recorder) {
		this.recorder = recorder;
	}



	public MdColumn copy(MdColumn other){
  
		this.setTableId(other.getTableId());  
		this.setColumnName(other.getColumnName());
  
		this.fieldLabelName= other.getFieldLabelName();  
		this.columnComment= other.getColumnComment();  
		this.columnOrder= other.getColumnOrder();  
		this.columnType= other.getColumnType();  
		this.maxLength= other.getMaxLength();  
		this.scale= other.getScale();  
		this.accessType= other.getAccessType();  
		this.mandatory= other.getMandatory();  
		this.primarykey= other.getPrimarykey();  
		this.columnState= other.getColumnState();  
		this.referenceType= other.getReferenceType();  
		this.referenceData= other.getReferenceData();  
		this.validateRegex= other.getValidateRegex();  
		this.validateInfo= other.getValidateInfo();  
		this.defaultValue= other.getDefaultValue();  
		this.lastModifyDate= other.getLastModifyDate();  
		this.recorder= other.getRecorder();

		return this;
	}
	
	public MdColumn copyNotNullProperty(MdColumn other){
  
	if( other.getTableId() != null)
		this.setTableId(other.getTableId());  
	if( other.getColumnName() != null)
		this.setColumnName(other.getColumnName());
  
		if( other.getFieldLabelName() != null)
			this.fieldLabelName= other.getFieldLabelName();  
		if( other.getColumnComment() != null)
			this.columnComment= other.getColumnComment();  
		if( other.getColumnOrder() != null)
			this.columnOrder= other.getColumnOrder();  
		if( other.getColumnType() != null)
			this.columnType= other.getColumnType();  
		if( other.getMaxLength() != null)
			this.maxLength= other.getMaxLength();  
		if( other.getScale() != null)
			this.scale= other.getScale();  
		if( other.getAccessType() != null)
			this.accessType= other.getAccessType();  
		if( other.getMandatory() != null)
			this.mandatory= other.getMandatory();  
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
		if( other.getDefaultValue() != null)
			this.defaultValue= other.getDefaultValue();  
		if( other.getLastModifyDate() != null)
			this.lastModifyDate= other.getLastModifyDate();  
		if( other.getRecorder() != null)
			this.recorder= other.getRecorder();		

		return this;
	}

	public MdColumn clearProperties(){
  
		this.fieldLabelName= null;  
		this.columnComment= null;  
		this.columnOrder= null;  
		this.columnType= null;  
		this.maxLength= null;  
		this.scale= null;  
		this.accessType= null;  
		this.mandatory= null;  
		this.primarykey= null;  
		this.columnState= null;  
		this.referenceType= null;  
		this.referenceData= null;  
		this.validateRegex= null;  
		this.validateInfo= null;  
		this.defaultValue= null;  
		this.lastModifyDate= null;  
		this.recorder= null;

		return this;
	}
}
