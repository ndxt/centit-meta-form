package com.centit.metaform.po;

import javax.persistence.Column;
import javax.persistence.Embeddable;
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
public class MetaColumnId implements java.io.Serializable {
    private static final long serialVersionUID =  1L;

    /**
     * 表ID 表单主键
     */
    @JoinColumn(name = "TABLE_ID")
    @ManyToOne
    @JSONField(serialize=false)
    private MetaTable mdTable;

    /**
     * 字段代码 null
     */
    @Column(name = "COLUMN_NAME")
    @NotBlank(message = "字段不能为空")
    private String columnName;





    // Constructors
    /** default constructor */
    public MetaColumnId() {
    }
    /** full constructor */
    public MetaColumnId(MetaTable mdTable1, String columnName) {

        this.mdTable = mdTable1;
        this.columnName = columnName;
    }


    public MetaTable getMdTable() {
        if(null==mdTable)
            mdTable=new MetaTable();
        return mdTable;
    }
    public void setMdTable(MetaTable mdTable) {
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
        if (!(other instanceof MetaColumnId))
            return false;

        MetaColumnId castOther = (MetaColumnId) other;
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
