package com.centit.metaform.po;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.NotBlank;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * PendingMdRelDetialId  entity.
 * create by scaffold 2016-06-01 
 
 * 未落实表关联细节表null   
*/
//未落实表关联细节表 的主键
@Embeddable
public class PendingMetaRelDetailId implements java.io.Serializable {
	private static final long serialVersionUID =  1L;

	/**
	 * 关联代码 null 
	 */
	@JoinColumn(name = "RELATION_ID",nullable=false)
	@ManyToOne
	@JSONField(serialize=false)
	private PendingMetaRelation relation;

	/**
	 * p字段代码 null 
	 */
	@Column(name = "PARENT_COLUMN_NAME")
	@NotBlank(message = "字段不能为空")
	private String parentColumnName;

	// Constructors
	/** default constructor */
	public PendingMetaRelDetailId() {
	}
	/** full constructor */
	public PendingMetaRelDetailId(Long relationId, String parentColumnName) {

		this.setRelationId(relationId);
		this.parentColumnName = parentColumnName;	
	}

	
	
  
	public PendingMetaRelation getRelation() {
		return relation;
	}
	public void setRelation(PendingMetaRelation relation) {
		this.relation = relation;
	}
	public Long getRelationId() {
		if(null==this.relation)
			return null;
		return this.relation.getRelationId();
	}

	public void setRelationId(Long relationId) {
			this.relation=new PendingMetaRelation();
			this.relation.setRelationId(relationId);
	}
  
	public String getParentColumnName() {
		return this.parentColumnName;
	}

	public void setParentColumnName(String parentColumnName) {
		this.parentColumnName = parentColumnName;
	}

}
