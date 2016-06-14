package com.centit.metaform.po;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.NotBlank;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * MdColumnId  entity.
 * create by scaffold 2016-06-02 
 
 * 字段元数据表null   
*/
//字段元数据表 的主键
@Embeddable
public class PendingMetaColumnId implements java.io.Serializable {
	private static final long serialVersionUID =  1L;

	/**
	 * 表ID 表单主键 
	 */
	@JoinColumn(name = "TABLE_ID",nullable=false)
	@ManyToOne(fetch=FetchType.LAZY)
	@JSONField(serialize=false)
	private PendingMetaTable mdTable;

	/**
	 * 字段代码 null 
	 */
	@Column(name = "COLUMN_NAME")
	@NotBlank(message = "字段不能为空")
	private String columnName;

	// Constructors
	/** default constructor */
	public PendingMetaColumnId() {
	}
	/** full constructor */
	public PendingMetaColumnId(PendingMetaTable mdTable, String columnName) {
		this.setMdTable(mdTable);
		this.columnName = columnName;	
	}

	public PendingMetaTable getMdTable() {
		if(null==this.mdTable)
			this.mdTable=new PendingMetaTable();
		return mdTable;
	}
	public void setMdTable(PendingMetaTable mdTable) {
		this.mdTable = mdTable;
	}
	public Long getTableId() {
		return this.getMdTable().getTableId();
	}

	public void setTableId(Long tableId) {
		this.getMdTable().setTableId(tableId);
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
		if (!(other instanceof PendingMetaColumnId))
			return false;
		
		PendingMetaColumnId castOther = (PendingMetaColumnId) other;
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
