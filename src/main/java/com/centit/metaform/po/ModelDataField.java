package com.centit.metaform.po;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;


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
	 * 更改类别 只读/更改/隐藏 
	 */
	@Column(name = "Access_Type")
	@Length(min = 0,  message = "字段长度不能小于{min}大于{max}")
	private String  accessType;
	/**
	 * 显示顺序 null 
	 */
	@Column(name = "Display_Order")
	private Long  displayOrder;
	/**
	 * 输入说明 与系统关联，比如自动与登录用户代码关联，选择系统用户，选择系统机构，等等 
	 */
	@Column(name = "input_hint")
	@Length(min = 0, max = 32, message = "字段长度不能小于{min}大于{max}")
	private String  inputHint;
	/**
	 * 输入约束描述 null 
	 */
	@Column(name = "Validate_hint")
	@Length(min = 0, max = 256, message = "字段长度不能小于{min}大于{max}")
	private String  validateHint;
	/**
	 * 字段高度 null 
	 */
	@Column(name = "Field_Height")
	private Long  fieldHeight;
	/**
	 * 标签长度 null 
	 */
	@Column(name = "Label_Length")
	private Long  labelLength;
	/**
	 * 字段长度 null 
	 */
	@Column(name = "Field_Length")
	private Long  fieldLength;

	// Constructors
	/** default constructor */
	public ModelDataField() {
	}
	/** minimal constructor */
	public ModelDataField(com.centit.metaform.po.ModelDataFieldId id 
				
		) {
		this.cid = id; 
			
			
	}

/** full constructor */
	public ModelDataField(com.centit.metaform.po.ModelDataFieldId id
			
	,String  accessType,Long  displayOrder,String  inputHint,String  validateHint,Long  fieldHeight,Long  labelLength,Long  fieldLength) {
		this.cid = id; 
			
	
		this.accessType= accessType;
		this.displayOrder= displayOrder;
		this.inputHint= inputHint;
		this.validateHint= validateHint;
		this.fieldHeight= fieldHeight;
		this.labelLength= labelLength;
		this.fieldLength= fieldLength;		
	}

	public com.centit.metaform.po.ModelDataFieldId getCid() {
		return this.cid;
	}
	
	public void setCid(com.centit.metaform.po.ModelDataFieldId id) {
		this.cid = id;
	}
  
	public String getModelCode() {
		if(this.cid==null)
			this.cid = new com.centit.metaform.po.ModelDataFieldId();
		return this.cid.getModelCode();
	}
	
	public void setModelCode(String modelCode) {
		if(this.cid==null)
			this.cid = new com.centit.metaform.po.ModelDataFieldId();
		this.cid.setModelCode(modelCode);
	}
  
	public String getColumnName() {
		if(this.cid==null)
			this.cid = new com.centit.metaform.po.ModelDataFieldId();
		return this.cid.getColumnName();
	}
	
	public void setColumnName(String columnName) {
		if(this.cid==null)
			this.cid = new com.centit.metaform.po.ModelDataFieldId();
		this.cid.setColumnName(columnName);
	}
	
	

	// Property accessors
  
	public String getAccessType() {
		return this.accessType;
	}
	
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
  
	public Long getLabelLength() {
		return this.labelLength;
	}
	
	public void setLabelLength(Long labelLength) {
		this.labelLength = labelLength;
	}
  
	public Long getFieldLength() {
		return this.fieldLength;
	}
	
	public void setFieldLength(Long fieldLength) {
		this.fieldLength = fieldLength;
	}



	public ModelDataField copy(ModelDataField other){
  
		this.setModelCode(other.getModelCode());  
		this.setColumnName(other.getColumnName());
  
		this.accessType= other.getAccessType();  
		this.displayOrder= other.getDisplayOrder();  
		this.inputHint= other.getInputHint();  
		this.validateHint= other.getValidateHint();  
		this.fieldHeight= other.getFieldHeight();  
		this.labelLength= other.getLabelLength();  
		this.fieldLength= other.getFieldLength();

		return this;
	}
	
	public ModelDataField copyNotNullProperty(ModelDataField other){
  
	if( other.getModelCode() != null)
		this.setModelCode(other.getModelCode());  
	if( other.getColumnName() != null)
		this.setColumnName(other.getColumnName());
  
		if( other.getAccessType() != null)
			this.accessType= other.getAccessType();  
		if( other.getDisplayOrder() != null)
			this.displayOrder= other.getDisplayOrder();  
		if( other.getInputHint() != null)
			this.inputHint= other.getInputHint();  
		if( other.getValidateHint() != null)
			this.validateHint= other.getValidateHint();  
		if( other.getFieldHeight() != null)
			this.fieldHeight= other.getFieldHeight();  
		if( other.getLabelLength() != null)
			this.labelLength= other.getLabelLength();  
		if( other.getFieldLength() != null)
			this.fieldLength= other.getFieldLength();		

		return this;
	}

	public ModelDataField clearProperties(){
  
		this.accessType= null;  
		this.displayOrder= null;  
		this.inputHint= null;  
		this.validateHint= null;  
		this.fieldHeight= null;  
		this.labelLength= null;  
		this.fieldLength= null;

		return this;
	}
}
