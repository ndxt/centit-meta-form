package com.centit.metaform.po;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.centit.support.database.metadata.SimpleTableField;


/**
 * create by scaffold 2016-06-02 
 
 
  数据模板字段null   
*/
@Entity
@Table(name = "M_MODEL_DATA_FIELD")
public class ModelDataField implements java.io.Serializable {
	private static final long serialVersionUID =  1L;

	@EmbeddedId
	private com.centit.metaform.po.ModelDataFieldId cid;
	
	/**
	 * 字段类别 ，关联只读字段（reference_Data 中为关联SQL语句）
	 */
    @Column(name = "COLUMN_TYPE")
	@Length(  message = "字段长度不能大于{max}")
	private String  columnType;
	
	
	/**
	 * H 隐藏  R 只读 C 新建是可以编辑 F 非空时可以编辑 N 正常编辑
	 */
    @Column(name = "ACCESS_TYPE")
	@Length(  message = "字段长度不能大于{max}")
	private String  accessType;
	/**
	 * 显示顺序 null 
	 */
	@Column(name = "Display_Order")
	private Long  displayOrder;
	/**
	 * 输入说明 与系统关联，比如自动与登录用户代码关联，选择系统用户，选择系统机构，等等 
	 */
	@Column(name = "INPUT_HINT")
	@Length(max = 256, message = "字段长度不能大于{max}")
	private String  inputHint;
	
	@Column(name = "INPUT_TYPE")
	@Length(max = 32, message = "字段长度不能大于{max}")
	private String  inputType;
	
	
	/**
	 * 引用类型 
	 * 0：没有：1： 数据字典(列表)   2： 数据字典(树型)   3：JSON表达式 4：sql语句   5：SQL（树）
	   9 :框架内置字典（用户、机构、角色等等）  Y：年份 M：月份   F:文件（column_Type 必须为 varchar（64））
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
	 * 输入约束描述
	 */
	@Column(name = "VALIDATE_HINT")
	@Length(max = 256, message = "字段长度不能大于{max}")
	private String  validateHint;
	
	/**
	 * 约束提示
	 */
	@Column(name = "VALIDATE_INFO")
	@Length(max = 256, message = "字段长度不能大于{max}")
	private String  validateInfo;
	
	/**
	 * 约束表达式
	 */
	@Column(name = "VALIDATE_REGEX")
	@Length(max = 256, message = "字段长度不能大于{max}")
	private String  validateRegex;
	
	/**
	 * 默认值
	 */
	@Column(name = "DEFAULT_VALUE")
	private String defaultValue;
	
	
	/**
	 * 过滤类别
	 */
	@Column(name = "FILTER_TYPE")
	private String filterType;
	
	/**
	 * 过滤类别
	 */
	@Column(name = "MANDATORY")
	private String mandatory;
	
	/**
	 * 是否是焦点
	 */
	@Column(name = "FOCUS")
	private String focus;
	
	/**
	 * 连接url
	 */
	@Column(name = "URL")
	private String url;
	
	/**
	 * 其他扩展属性
	 */
	@Column(name = "EXTEND_OPTIONS")
	private String extendOptions;
	
	
	@Column(name = "VIEW_FORMAT")
	@Length(max = 50, message = "字段长度不能大于{max}")
	private String viewFormat;
	

	/**
	 * 字段高度 null 
	 */
	@Column(name = "FIELD_HEIGHT")
	private Long  fieldHeight;
	
	/**
	 * 字段长度 null 
	 */
	@Column(name = "FIELD_WIDTH")
	private Long  fieldWidth;

	// Constructors
	/** default constructor */
	public ModelDataField() {
	}
	/** minimal constructor */
	public ModelDataField(ModelDataFieldId cid, String columnType) {
		super();
		this.cid = cid;
		this.columnType = columnType;
	}

	/** full constructor */
	public ModelDataField(ModelDataFieldId cid, String columnType,
			String accessType, Long displayOrder, String inputHint,
			String referenceType, String referenceData, String validateHint,
			String validateInfo, String validateRegex, String defaultValue,
			String filterType, String mandatory, String focus, String url,
			String extendOptions, Long fieldHeight, Long fieldWidth) {
		super();
		this.cid = cid;
		this.columnType = columnType;
		this.accessType = accessType;
		this.displayOrder = displayOrder;
		this.inputHint = inputHint;
		this.referenceType = referenceType;
		this.referenceData = referenceData;
		this.validateHint = validateHint;
		this.validateInfo = validateInfo;
		this.validateRegex = validateRegex;
		this.defaultValue = defaultValue;
		this.filterType = filterType;
		this.mandatory = mandatory;
		this.focus = focus;
		this.url = url;
		this.extendOptions = extendOptions;
		this.fieldHeight = fieldHeight;
		this.fieldWidth = fieldWidth;
	}

	
	public ModelDataFieldId getCid() {
		return this.cid;
	}
	
	public void setCid(ModelDataFieldId id) {
		this.cid = id;
	}
  
	public String getModelCode() {
		if(this.cid==null)
			return null;
		return this.cid.getModelCode();
	}
	
	public void setModelCode(String modelCode) {
		if(this.cid==null)
			this.cid = new ModelDataFieldId();
		this.cid.setModelCode(modelCode);
	}
  
	public String getColumnName() {
		if(this.cid==null)
			return null;//this.cid = new ModelDataFieldId();
		return this.cid.getColumnName();
	}
	
	
	public String getPropertyName(){
		if(this.cid==null)
			return null;
		return SimpleTableField.mapPropName(
				 this.cid.getColumnName());
	}
	
	public void setColumnName(String columnName) {
		if(this.cid==null)
			this.cid = new ModelDataFieldId();
		this.cid.setColumnName(columnName);
	}
	
	

	// Property accessors
  
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	/**
	 * H 隐藏  R 只读 C 新建是可以编辑 F 非空时可以编辑 N 正常编辑
	 */
	public String getAccessType() {
		return this.accessType;
	}
	
	/**
	 * H 隐藏  R 只读 C 新建是可以编辑 F 非空时可以编辑 N 正常编辑
	 */
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
  
	public Long getDisplayOrder() {
		return this.displayOrder;
	}
	
	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
	}
  
	public String getInputHint() {
		return this.inputHint;
	}
	
	public void setInputHint(String inputHint) {
		this.inputHint = inputHint;
	}
  
	public String getValidateHint() {
		return this.validateHint;
	}
	
	public void setValidateHint(String validateHint) {
		this.validateHint = validateHint;
	}
  
	public Long getFieldHeight() {
		return this.fieldHeight;
	}
	
	public void setFieldHeight(Long fieldHeight) {
		this.fieldHeight = fieldHeight;
	}
  
	/**
	 * R 为引用 T 为表字段
	 * @return
	 */	
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	/**
		0：没有：1： 数据字典(列表)   2： 数据字典(树型)   3：JSON表达式 4：SQL（列表）5：SQL（树）
		9 :框架内置字典（用户、机构、角色等等）  Y：年份 M：月份   F:文件（column_Type 必须为 varchar（64））
	 * @return
	 */
	public String getReferenceType() {
		return referenceType;
	}
	/**
	 * 0：没有：1： 数据字典(列表)   2： 数据字典(树型)   3：JSON表达式 4：SQL（列表）5：SQL（树）
	 9 :框架内置字典（用户、机构、角色等等）  Y：年份 M：月份   F:文件（column_Type 必须为 varchar（64））
	 * @param referenceType
	 */
	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}
	public String getReferenceData() {
		return referenceData;
	}
	public void setReferenceData(String referenceData) {
		this.referenceData = referenceData;
	}
	public String getValidateInfo() {
		return validateInfo;
	}
	public void setValidateInfo(String validateInfo) {
		this.validateInfo = validateInfo;
	}
	public String getValidateRegex() {
		return validateRegex;
	}
	public void setValidateRegex(String validateRegex) {
		this.validateRegex = validateRegex;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getFilterType() {
		return filterType;
	}
	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}
	public String getMandatory() {
		return mandatory;
	}
	
//	public boolean isMandatory() {
//		return "T".equals(mandatory) ||  "Y".equals(mandatory) || "1".equals(mandatory);
//	}
	
	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}
	public String getFocus() {
		return focus;
	}
//	public boolean isFocus() {
//		return "T".equals(focus) ||  "Y".equals(focus) || "1".equals(focus);
//	}
	
	public void setFocus(String focus) {
		this.focus = focus;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getExtendOptions() {
		return extendOptions;
	}
	public void setExtendOptions(String extendOptions) {
		this.extendOptions = extendOptions;
	}
	public Long getFieldWidth() {
		return fieldWidth;
	}
	public void setFieldWidth(Long fieldWidth) {
		this.fieldWidth = fieldWidth;
	}
	

	
	public String getViewFormat() {
		return viewFormat;
	}
	public void setViewFormat(String viewFormat) {
		this.viewFormat = viewFormat;
	}

	public ModelDataField copy(ModelDataField other){
  
		this.setModelCode(other.getModelCode());  
		this.setColumnName(other.getColumnName());
		this.accessType= other.getAccessType();  
		this.columnType=other.getColumnType();
		this.defaultValue=other.getDefaultValue();
		this.displayOrder=other.getDisplayOrder();
		this.extendOptions=other.getExtendOptions();
		this.fieldHeight=other.getFieldHeight();
		this.fieldWidth=other.getFieldWidth();
		this.filterType=other.getFilterType();
		this.inputHint=other.getInputHint();
		this.inputType=other.getInputType();
		this.mandatory=other.getMandatory();
		this.referenceData=other.getReferenceData();
		this.referenceType=other.getReferenceType();
		this.url=other.getUrl();
		this.validateHint=other.getValidateHint();
		this.validateInfo=other.getValidateInfo();
		this.validateRegex=other.getValidateRegex();
		this.viewFormat=other.getViewFormat();
		return this;
	}
	
	public ModelDataField copyNotNullProperty(ModelDataField other){
		if( other.getModelCode() != null)
			this.setModelCode(other.getModelCode());  
		if( other.getColumnName() != null)
			this.setColumnName(other.getColumnName());
		if(other.getAccessType()!= null)
			this.accessType= other.getAccessType();  
		if(other.getColumnType()!= null)
			this.columnType=other.getColumnType();
		if(other.getDefaultValue()!= null)
			this.defaultValue=other.getDefaultValue();
		if(other.getDisplayOrder()!= null)
			this.displayOrder=other.getDisplayOrder();
		if(other.getExtendOptions()!= null)
			this.extendOptions=other.getExtendOptions();
		if(other.getFieldHeight()!= null)
			this.fieldHeight=other.getFieldHeight();
		if(other.getFieldWidth()!= null)
			this.fieldWidth=other.getFieldWidth();
		if(other.getFilterType()!= null)
			this.filterType=other.getFilterType();
		if(other.getInputHint()!= null)
			this.inputHint=other.getInputHint();
		if(other.getMandatory()!= null)
			this.mandatory=other.getMandatory();
		if(other.getReferenceData()!= null)
			this.referenceData=other.getReferenceData();
		if(other.getReferenceType()!= null)
			this.referenceType=other.getReferenceType();
		if(other.getUrl()!= null)
			this.url=other.getUrl();
		if(other.getValidateHint()!= null)
			this.validateHint=other.getValidateHint();
		if(other.getInputType()!= null)
			this.inputType=other.getInputType();
		if(other.getValidateInfo()!= null)
			this.validateInfo=other.getValidateInfo();
		if(other.getValidateRegex()!= null)
			this.validateRegex=other.getValidateRegex();
		if(other.getViewFormat()!= null)
			this.viewFormat=other.getViewFormat();
		return this;
	}

	public ModelDataField clearProperties(){
		this.cid=null;
		this.accessType= null;  
		this.columnType=null; 
		this.defaultValue=null; 
		this.displayOrder=null; 
		this.extendOptions=null; 
		this.fieldHeight=null; 
		this.fieldWidth=null; 
		this.filterType=null; 
		this.inputHint=null; 
		this.mandatory=null; 
		this.referenceData=null; 
		this.referenceType=null; 
		this.url=null; 
		this.validateHint=null; 
		this.validateInfo=null; 
		this.viewFormat=null;
		this.validateRegex=null; 
		return this;
	}
	
}
