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
@Table(name = "F_META_REL_DETIAL")
public class MetaRelDetail implements java.io.Serializable {
    private static final long serialVersionUID =  1L;

//    @EmbeddedId
//    private com.centit.metaform.po.MetaRelDetailId cid;

    /**
     * 关联代码 null
     */
    @Column(name = "RELATION_ID")
    private Long relationId;

    /**
     * p字段代码 null
     */
    @Column(name = "PARENT_COLUMN_NAME")
    @NotBlank(message = "字段不能为空")
    private String parentColumnName;

    /**
     * C字段代码 null
     */
    @Column(name = "CHILD_COLUMN_NAME")
    @NotBlank(message = "字段不能为空")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String  childColumnName;

    // Constructors
    /** default constructor */
    public MetaRelDetail() {
    }
    /** minimal constructor */
    public MetaRelDetail(Long relationId, String parentColumnName, String  childColumnName) {
        this.relationId = relationId;
        this.parentColumnName = parentColumnName;

        this.childColumnName= childColumnName;
    }


//    public com.centit.metaform.po.MetaRelDetailId getCid() {
//        return this.cid;
//    }

//    public void setCid(com.centit.metaform.po.MetaRelDetailId id) {
//        this.cid = id;
//    }
  
  
    public String getParentColumnName() {
//        if(this.cid==null)
//            this.cid = new com.centit.metaform.po.MetaRelDetailId();
//        return this.cid.getParentColumnName();
        return this.parentColumnName;
    }

    public void setParentColumnName(String parentColumnName) {
//        if(this.cid==null)
//            this.cid = new com.centit.metaform.po.MetaRelDetailId();
//        this.cid.setParentColumnName(parentColumnName);
        this.parentColumnName = parentColumnName;
    }

    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relation) {
        this.relationId = relation;
    }

    // Property accessors
  
    public String getChildColumnName() {
        return this.childColumnName;
    }

    public void setChildColumnName(String childColumnName) {
        this.childColumnName = childColumnName;
    }



    public MetaRelDetail copy(MetaRelDetail other){
  
//        this.setCid(other.getCid());
        this.parentColumnName = other.getParentColumnName();
  
        this.childColumnName= other.getChildColumnName();

        return this;
    }

    public MetaRelDetail copyNotNullProperty(MetaRelDetail other){
  
//    if( other.getCid() != null)
//        this.setCid(other.getCid());
//    if( other.getParentColumnName() != null)
//        this.setParentColumnName(other.getParentColumnName());
        if (other.getParentColumnName() != null)
            this.parentColumnName = other.getParentColumnName();
        if (other.getRelationId() != null)
            this.relationId = other.getRelationId();
  
        if( other.getChildColumnName() != null)
            this.childColumnName= other.getChildColumnName();

        return this;
    }

    public MetaRelDetail clearProperties(){
  
        this.childColumnName= null;

        return this;
    }

}
