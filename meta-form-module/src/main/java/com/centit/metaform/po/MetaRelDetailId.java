package com.centit.metaform.po;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.NotBlank;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * MdRelDetialId  entity.
 * create by scaffold 2016-06-02 
 
 * 表关联细节表null   
*/
//表关联细节表 的主键
@Embeddable
public class MetaRelDetailId implements java.io.Serializable {
	private static final long serialVersionUID =  1L;

	/**
	 * 关联代码 null 
	 */
	@JoinColumn(name = "RELATION_ID",nullable=false)
	@ManyToOne(fetch=FetchType.LAZY)
	@JSONField(serialize=false)
	private MetaRelation relation;

	/**
	 * p字段代码 null 
	 */
	@Column(name = "PARENT_COLUMN_NAME")
	@NotBlank(message = "字段不能为空")
	private String parentColumnName;

	// Constructors
	/** default constructor */
	public MetaRelDetailId() {
	}
	/** full constructor */
	public MetaRelDetailId(MetaRelation relation, String parentColumnName) {

		this.relation = relation;
		this.parentColumnName = parentColumnName;	
	}

  
	public MetaRelation getRelation() {
		return relation;
	}
	public void setRelation(MetaRelation relation) {
		this.relation = relation;
	}
	public Long getRelationId() {
		if(null==this.relation)
			return null;
		return this.relation.getRelationId();
	}

	public void setRelationId(Long relationId) {
			this.relation=new MetaRelation();
			this.relation.setRelationId(relationId);
	}
  
	public String getParentColumnName() {
		return this.parentColumnName;
	}

	public void setParentColumnName(String parentColumnName) {
		this.parentColumnName = parentColumnName;
	}


	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof MetaRelDetailId))
			return false;
		
		MetaRelDetailId castOther = (MetaRelDetailId) other;
		boolean ret = true;
  
		ret = ret && ( this.getRelationId() == castOther.getRelationId() ||
					   (this.getRelationId() != null && castOther.getRelationId() != null
							   && this.getRelationId().equals(castOther.getRelationId())));
  
		ret = ret && ( this.getParentColumnName() == castOther.getParentColumnName() ||
					   (this.getParentColumnName() != null && castOther.getParentColumnName() != null
							   && this.getParentColumnName().equals(castOther.getParentColumnName())));

		return ret;
	}
	
	public int hashCode() {
		int result = 17;
  
		result = 37 * result +
		 	(this.getRelationId() == null ? 0 :this.getRelationId().hashCode());
  
		result = 37 * result +
		 	(this.getParentColumnName() == null ? 0 :this.getParentColumnName().hashCode());
	
		return result;
	}
}
