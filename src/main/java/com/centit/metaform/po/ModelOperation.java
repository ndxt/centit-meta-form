package com.centit.metaform.po;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;


/**
 * create by scaffold 2016-06-21 
 
 
  模块操作定义null   
*/
@Entity
@Table(name = "M_MODEL_OPERATION")
public class ModelOperation implements java.io.Serializable {
	private static final long serialVersionUID =  1L;

	@EmbeddedId
	private com.centit.metaform.po.ModelOperationId cid;


	/**
	 * 操作模块代码 一个模块中的操作可能是针对其他模块的 
	 */
	@Column(name = "OPT_MODEL_CODE")
	@Length(min = 0, max = 16, message = "字段长度不能小于{min}大于{max}")
	private String  optModelCode;
	/**
	 * 操作方法 null 
	 */
	@Column(name = "METHOD")
	@Length(min = 0, max = 16, message = "字段长度不能小于{min}大于{max}")
	private String  method;
	/**
	 * 操作名称 null 
	 */
	@Column(name = "LABEL")
	@Length(min = 0, max = 32, message = "字段长度不能小于{min}大于{max}")
	private String  label;
	/**
	 * 显示顺序 null 
	 */
	@Column(name = "DISPLAY_ORDER")
	private Long  displayOrder;
	/**
	 * 打开方式 0：没有：1： 提示信息  2：只读表单  3：读写表单   
	 */
	@Column(name = "OPEN_TYPE")
	@Length(min = 0, max = 1, message = "字段长度不能小于{min}大于{max}")
	private String  openType;
	/**
	 * 返回后操作 0：不操作 1： 刷新页面  2：删除当前行 3：更新当前行 
	 */
	@Column(name = "RETURN_OPERATION")
	@Length(min = 0, max = 1, message = "字段长度不能小于{min}大于{max}")
	private String  returnOperation;
	/**
	 * 操作前提示类别 0： 没有  1 yes or no 2 cancel ok 3 cancel retry ok 
	 */
	@Column(name = "OPT_HINT_TYPE")
	@Length(min = 0, max = 1, message = "字段长度不能小于{min}大于{max}")
	private String  optHintType;
	/**
	 * 操作提示信息 操作前提示信息 
	 */
	@Column(name = "OPT_HINT_INFO")
	@Length(min = 0, max = 500, message = "字段长度不能小于{min}大于{max}")
	private String  optHintInfo;
	/**
	 * 其他扩展属性 null 
	 */
	@Column(name = "EXTEND_OPTIONS")
	@Length(min = 0, max = 1000, message = "字段长度不能小于{min}大于{max}")
	private String  extendOptions;

	// Constructors
	/** default constructor */
	public ModelOperation() {
	}
	/** minimal constructor */
	public ModelOperation(ModelOperationId id 
				
		) {
		this.cid = id;
	}

	public ModelOperation(String modelCode, String operation, String method,String label){
		
		this.cid =  new ModelOperationId( modelCode,operation);
		this.method = method;
		this.label = label;
	}
	
/** full constructor */
	public ModelOperation(com.centit.metaform.po.ModelOperationId id
			
	,String  optModelCode,String  method,String  label,Long  displayOrder,String  openType,String  returnOperation,String  optHintType,String  optHintInfo,String  extendOptions) {
		this.cid = id; 
			
	
		this.optModelCode= optModelCode;
		this.method= method;
		this.label= label;
		this.displayOrder= displayOrder;
		this.openType= openType;
		this.returnOperation= returnOperation;
		this.optHintType= optHintType;
		this.optHintInfo= optHintInfo;
		this.extendOptions= extendOptions;		
	}

	public com.centit.metaform.po.ModelOperationId getCid() {
		return this.cid;
	}
	
	public void setCid(com.centit.metaform.po.ModelOperationId id) {
		this.cid = id;
	}
  
	public String getModelCode() {
		if(this.cid==null)
			this.cid = new com.centit.metaform.po.ModelOperationId();
		return this.cid.getModelCode();
	}
	
	public void setModelCode(String modelCode) {
		if(this.cid==null)
			this.cid = new com.centit.metaform.po.ModelOperationId();
		this.cid.setModelCode(modelCode);
	}
  
	public String getOperation() {
		if(this.cid==null)
			this.cid = new com.centit.metaform.po.ModelOperationId();
		return this.cid.getOperation();
	}
	
	public void setOperation(String operation) {
		if(this.cid==null)
			this.cid = new com.centit.metaform.po.ModelOperationId();
		this.cid.setOperation(operation);
	}
	
	

	// Property accessors
  
	public String getOptModelCode() {
		return this.optModelCode;
	}
	
	public void setOptModelCode(String optModelCode) {
		this.optModelCode = optModelCode;
	}
  
	public String getMethod() {
		return this.method;
	}
	
	public void setMethod(String method) {
		this.method = method;
	}
  
	public String getLabel() {
		return this.label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
  
	public Long getDisplayOrder() {
		return this.displayOrder;
	}
	
	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
	}
  
	public String getOpenType() {
		return this.openType;
	}
	
	public void setOpenType(String openType) {
		this.openType = openType;
	}
  
	public String getReturnOperation() {
		return this.returnOperation;
	}
	
	public void setReturnOperation(String returnOperation) {
		this.returnOperation = returnOperation;
	}
  
	public String getOptHintType() {
		return this.optHintType;
	}
	
	public void setOptHintType(String optHintType) {
		this.optHintType = optHintType;
	}
  
	public String getOptHintInfo() {
		return this.optHintInfo;
	}
	
	public void setOptHintInfo(String optHintInfo) {
		this.optHintInfo = optHintInfo;
	}
  
	public String getExtendOptions() {
		return this.extendOptions;
	}
	
	public void setExtendOptions(String extendOptions) {
		this.extendOptions = extendOptions;
	}



	public ModelOperation copy(ModelOperation other){
  
		this.setModelCode(other.getModelCode());  
		this.setOperation(other.getOperation());
  
		this.optModelCode= other.getOptModelCode();  
		this.method= other.getMethod();  
		this.label= other.getLabel();  
		this.displayOrder= other.getDisplayOrder();  
		this.openType= other.getOpenType();  
		this.returnOperation= other.getReturnOperation();  
		this.optHintType= other.getOptHintType();  
		this.optHintInfo= other.getOptHintInfo();  
		this.extendOptions= other.getExtendOptions();

		return this;
	}
	
	public ModelOperation copyNotNullProperty(ModelOperation other){
  
	if( other.getModelCode() != null)
		this.setModelCode(other.getModelCode());  
	if( other.getOperation() != null)
		this.setOperation(other.getOperation());
  
		if( other.getOptModelCode() != null)
			this.optModelCode= other.getOptModelCode();  
		if( other.getMethod() != null)
			this.method= other.getMethod();  
		if( other.getLabel() != null)
			this.label= other.getLabel();  
		if( other.getDisplayOrder() != null)
			this.displayOrder= other.getDisplayOrder();  
		if( other.getOpenType() != null)
			this.openType= other.getOpenType();  
		if( other.getReturnOperation() != null)
			this.returnOperation= other.getReturnOperation();  
		if( other.getOptHintType() != null)
			this.optHintType= other.getOptHintType();  
		if( other.getOptHintInfo() != null)
			this.optHintInfo= other.getOptHintInfo();  
		if( other.getExtendOptions() != null)
			this.extendOptions= other.getExtendOptions();		

		return this;
	}

	public ModelOperation clearProperties(){
  
		this.optModelCode= null;  
		this.method= null;  
		this.label= null;  
		this.displayOrder= null;  
		this.openType= null;  
		this.returnOperation= null;  
		this.optHintType= null;  
		this.optHintInfo= null;  
		this.extendOptions= null;

		return this;
	}
}
