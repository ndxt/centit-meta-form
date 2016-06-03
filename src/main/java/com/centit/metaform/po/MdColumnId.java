package com.centit.metaform.po;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.validator.constraints.NotBlank;

/**
 * MdColumnId  entity.
 * create by scaffold 2016-06-02 
 
 * 字段元数据表null   
*/
//字段元数据表 的主键
@Embeddable
public class MdColumnId implements java.io.Serializable {
	private static final long serialVersionUID =  1L;

	/**
	 * 表ID 表单主键 
	 */
	@Column(name = "TABLE_ID")
	@NotBlank(message = "字段不能为空")
	private Long tableId;

	/**
	 * 字段代码 null 
	 */
	@Column(name = "COLUMN_NAME")
	@NotBlank(message = "字段不能为空")
	private String columnName;

	// Constructors
	/** default constructor */
	public MdColumnId() {
	}
	/** full constructor */
	public MdColumnId(Long tableId, String columnName) {

		this.tableId = tableId;
		this.columnName = columnName;	
	}

  
	public Long getTableId() {
		return this.tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
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
		if (!(other instanceof MdColumnId))
			return false;
		
		MdColumnId castOther = (MdColumnId) other;
		boolean ret = true;
  
		ret = ret && ( this.getTableId() == castOther.getTableId() ||
					   (this.getTableId() != null && castOther.getTableId() != null
							   && this.getTableId().equals(castOther.getTableId())));
  
		ret = ret && ( this.getColumnName() == castOther.getColumnName() ||
					   (this.getColumnName() != null && castOther.getColumnName() != null
							   && this.getColumnName().equals(castOther.getColumnName())));

		return ret;
	}
	
	public int hashCode() {
		int result = 17;
  
		result = 37 * result +
		 	(this.getTableId() == null ? 0 :this.getTableId().hashCode());
  
		result = 37 * result +
		 	(this.getColumnName() == null ? 0 :this.getColumnName().hashCode());
	
		return result;
	}
}
