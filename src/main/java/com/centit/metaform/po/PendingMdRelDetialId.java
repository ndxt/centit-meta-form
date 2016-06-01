package com.centit.metaform.po;

import java.util.Date;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.hibernate.validator.constraints.NotBlank;

/**
 * PendingMdRelDetialId  entity.
 * create by scaffold 2016-06-01 
 * @author codefan@sina.com
 * 未落实表关联细节表null   
*/
//未落实表关联细节表 的主键
@Embeddable
public class PendingMdRelDetialId implements java.io.Serializable {
	private static final long serialVersionUID =  1L;

	/**
	 * 关联代码 null 
	 */
	@Column(name = "relation_ID")
	@NotBlank(message = "字段不能为空")
	private String relationId;

	/**
	 * p字段代码 null 
	 */
	@Column(name = "parent_column_Name")
	@NotBlank(message = "字段不能为空")
	private String parentColumnName;

	// Constructors
	/** default constructor */
	public PendingMdRelDetialId() {
	}
	/** full constructor */
	public PendingMdRelDetialId(String relationId, String parentColumnName) {

		this.relationId = relationId;
		this.parentColumnName = parentColumnName;	
	}

  
	public String getRelationId() {
		return this.relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
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
		if (!(other instanceof PendingMdRelDetialId))
			return false;
		
		PendingMdRelDetialId castOther = (PendingMdRelDetialId) other;
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
