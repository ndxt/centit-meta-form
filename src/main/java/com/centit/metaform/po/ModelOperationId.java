package com.centit.metaform.po;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.validator.constraints.NotBlank;

/**
 * ModelOperationId  entity.
 * create by scaffold 2016-06-21 
 
 * 模块操作定义null   
*/
//模块操作定义 的主键
@Embeddable
public class ModelOperationId implements java.io.Serializable {
	private static final long serialVersionUID =  1L;

	/**
	 * 模块代码 所属（关联） 
	 */
	@Column(name = "Model_Code")
	@NotBlank(message = "字段不能为空")
	private String modelCode;

	/**
	 * 操作 null 
	 */
	@Column(name = "OPERATION")
	@NotBlank(message = "字段不能为空")
	private String operation;

	// Constructors
	/** default constructor */
	public ModelOperationId() {
	}
	/** full constructor */
	public ModelOperationId(String modelCode, String operation) {

		this.modelCode = modelCode;
		this.operation = operation;	
	}

  
	public String getModelCode() {
		return this.modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
  
	public String getOperation() {
		return this.operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}


	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ModelOperationId))
			return false;
		
		ModelOperationId castOther = (ModelOperationId) other;
		boolean ret = true;
  
		ret = ret && ( this.getModelCode() == castOther.getModelCode() ||
					   (this.getModelCode() != null && castOther.getModelCode() != null
							   && this.getModelCode().equals(castOther.getModelCode())));
  
		ret = ret && ( this.getOperation() == castOther.getOperation() ||
					   (this.getOperation() != null && castOther.getOperation() != null
							   && this.getOperation().equals(castOther.getOperation())));

		return ret;
	}
	
	public int hashCode() {
		int result = 17;
  
		result = 37 * result +
		 	(this.getModelCode() == null ? 0 :this.getModelCode().hashCode());
  
		result = 37 * result +
		 	(this.getOperation() == null ? 0 :this.getOperation().hashCode());
	
		return result;
	}
}
