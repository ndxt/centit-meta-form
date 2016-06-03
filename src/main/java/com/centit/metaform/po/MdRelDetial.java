package com.centit.metaform.po;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;


/**
 * create by scaffold 2016-06-02 
 
 
  表关联细节表null   
*/
@Entity
@Table(name = "F_MD_REL_DETIAL")
public class MdRelDetial implements java.io.Serializable {
	private static final long serialVersionUID =  1L;

	@EmbeddedId
	private com.centit.metaform.po.MdRelDetialId cid;


	/**
	 * C字段代码 null 
	 */
	@Column(name = "CHILD_COLUMN_NAME")
	@NotBlank(message = "字段不能为空")
	@Length(min = 0, max = 32, message = "字段长度不能小于{min}大于{max}")
	private String  childColumnName;

	// Constructors
	/** default constructor */
	public MdRelDetial() {
	}
	/** minimal constructor */
	public MdRelDetial(com.centit.metaform.po.MdRelDetialId id 
				
		,String  childColumnName) {
		this.cid = id; 
			
	
		this.childColumnName= childColumnName; 		
	}



	public com.centit.metaform.po.MdRelDetialId getCid() {
		return this.cid;
	}
	
	public void setCid(com.centit.metaform.po.MdRelDetialId id) {
		this.cid = id;
	}
  
	public String getRelationId() {
		if(this.cid==null)
			this.cid = new com.centit.metaform.po.MdRelDetialId();
		return this.cid.getRelationId();
	}
	
	public void setRelationId(String relationId) {
		if(this.cid==null)
			this.cid = new com.centit.metaform.po.MdRelDetialId();
		this.cid.setRelationId(relationId);
	}
  
	public String getParentColumnName() {
		if(this.cid==null)
			this.cid = new com.centit.metaform.po.MdRelDetialId();
		return this.cid.getParentColumnName();
	}
	
	public void setParentColumnName(String parentColumnName) {
		if(this.cid==null)
			this.cid = new com.centit.metaform.po.MdRelDetialId();
		this.cid.setParentColumnName(parentColumnName);
	}
	
	

	// Property accessors
  
	public String getChildColumnName() {
		return this.childColumnName;
	}
	
	public void setChildColumnName(String childColumnName) {
		this.childColumnName = childColumnName;
	}



	public MdRelDetial copy(MdRelDetial other){
  
		this.setRelationId(other.getRelationId());  
		this.setParentColumnName(other.getParentColumnName());
  
		this.childColumnName= other.getChildColumnName();

		return this;
	}
	
	public MdRelDetial copyNotNullProperty(MdRelDetial other){
  
	if( other.getRelationId() != null)
		this.setRelationId(other.getRelationId());  
	if( other.getParentColumnName() != null)
		this.setParentColumnName(other.getParentColumnName());
  
		if( other.getChildColumnName() != null)
			this.childColumnName= other.getChildColumnName();		

		return this;
	}

	public MdRelDetial clearProperties(){
  
		this.childColumnName= null;

		return this;
	}	
	
}
