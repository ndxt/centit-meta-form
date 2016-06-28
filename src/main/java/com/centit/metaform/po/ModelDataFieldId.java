package com.centit.metaform.po;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.NotBlank;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * ModelDataFieldId  entity.
 * create by scaffold 2016-06-02 
 
 * 数据模板字段null   
*/
//数据模板字段 的主键
@Embeddable
public class ModelDataFieldId implements java.io.Serializable {
	private static final long serialVersionUID =  1L;

	/**
	 * 模块代码 null 
	 */
	@JoinColumn(name = "MODEL_CODE")
	@ManyToOne
	@JSONField(serialize=false)
	private MetaFormModel metaFormModel;

	/**
	 * 字段代码 null 
	 */
	@Column(name = "COLUMN_NAME")
	@NotBlank(message = "字段不能为空")
	private String columnName;

	// Constructors
	/** default constructor */
	public ModelDataFieldId() {
	}
	/** full constructor 
	 * @param metaFormModel */
	public ModelDataFieldId(String columnName,MetaFormModel metaFormModel) {

		this.metaFormModel=metaFormModel;
		this.columnName = columnName;	
	}

  
	public ModelDataFieldId (String modelCode,
			String columnName) {
		// TODO Auto-generated method stub
		this.setModelCode(modelCode);
		this.columnName = columnName;	
	}
	
	public MetaFormModel getMetaFormModel() {
		return metaFormModel;
	}
	public void setMetaFormModel(MetaFormModel metaFormModel) {
		this.metaFormModel = metaFormModel;
	}
	
	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}


	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ModelDataFieldId))
			return false;
		
		ModelDataFieldId castOther = (ModelDataFieldId) other;
		boolean ret = true;
  
		ret = ret && ( this.getModelCode() == castOther.getModelCode() ||
					   (this.getModelCode() != null && castOther.getModelCode() != null
							   && this.getModelCode().equals(castOther.getModelCode())));
  
		ret = ret && ( this.getColumnName() == castOther.getColumnName() ||
					   (this.getColumnName() != null && castOther.getColumnName() != null
							   && this.getColumnName().equals(castOther.getColumnName())));

		return ret;
	}
	
	
	
	public String getModelCode() {
		if(null==this.metaFormModel)
			return null;
		return this.metaFormModel.getModelCode();
	}
	public int hashCode() {
		int result = 17;
  
		result = 37 * result +
		 	(this.getModelCode() == null ? 0 :this.getModelCode().hashCode());
  
		result = 37 * result +
		 	(this.getColumnName() == null ? 0 :this.getColumnName().hashCode());
	
		return result;
	}
	public void setModelCode(String modelCode) {
		if(null==this.metaFormModel)
			this.metaFormModel=new MetaFormModel();
		this.metaFormModel.setModelCode(modelCode);
		
	}
}
