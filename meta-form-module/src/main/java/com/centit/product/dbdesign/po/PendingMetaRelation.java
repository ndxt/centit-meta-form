package com.centit.product.dbdesign.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.centit.product.metadata.po.MetaRelDetail;
import com.centit.product.metadata.po.MetaRelation;
import com.centit.support.database.metadata.TableReference;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.*;

/**
 * create by scaffold 2016-06-01
 * <p>
 * <p>
 * 未落实表关联关系表null
 */
@Data
@Entity
@Table(name = "F_PENDING_META_RELATION")
public class PendingMetaRelation implements TableReference, java.io.Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 关联代码 关联关系，类似与外键，但不创建外键
     */
    @Id
    @Column(name = "RELATION_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqgen")
    @SequenceGenerator(sequenceName = "SEQ_PENDINGRELATIONID", name = "seqgen", allocationSize = 1, initialValue = 1)
    private String relationId;

//    /**
//     * 主表表ID 表单主键
//     */
//    @JoinColumn(name = "PARENT_TABLE_ID")
//    @ManyToOne
//    @JSONField(serialize=false)
//    //@NotFound(action=NotFoundAction.IGNORE)
//    private PendingMetaTable  parentTable;
//    /**
//     * 从表表ID 表单主键
//     */
//    @JoinColumn(name = "CHILD_TABLE_ID")
//    @ManyToOne
//    @JSONField(serialize=false)
//    //@NotFound(action=NotFoundAction.IGNORE)
//    private PendingMetaTable  childTable;

    @Column(name = "PARENT_TABLE_ID")
    private String parentTableId;

    @Column(name = "CHILD_TABLE_ID")
    private String childTableId;

    /**
     * 关联名称 null
     */
    @Column(name = "RELATION_NAME")
    @NotBlank(message = "字段不能为空")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String relationName;
    /**
     * 状态 null
     */
    @Column(name = "RELATION_STATE")
    @Length(message = "字段长度不能大于{max}")
    private String relationState;
    /**
     * 关联说明 null
     */
    @Column(name = "RELATION_COMMENT")
    @Length(max = 256, message = "字段长度不能大于{max}")
    private String relationComment;
    /**
     * 更改时间 null
     */
    @Column(name = "LAST_MODIFY_DATE")
    private Date lastModifyDate;
    /**
     * 更改人员 null
     */
    @Column(name = "RECORDER")
    @Length(max = 8, message = "字段长度不能大于{max}")
    private String recorder;

    @OneToMany(mappedBy="relationId",orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "RELATION_ID", referencedColumnName = "RELATION_ID")
    private Set<PendingMetaRelDetail> relationDetails;

    @ApiModelProperty(value = "主表信息")
    @OneToMany(targetEntity = PendingMetaTable.class)
    @JoinColumn(name = "parentTableId", referencedColumnName = "tableId")
    private PendingMetaTable parentTable;

    @ApiModelProperty(value = "从表信息")
    @OneToMany(targetEntity = PendingMetaTable.class)
    @JoinColumn(name = "childTableId", referencedColumnName = "tableId")
    private PendingMetaTable childTable;

    // Constructors

    /**
     * default constructor
     */
    public PendingMetaRelation() {
    }

    /**
     * minimal constructor
     */
    public PendingMetaRelation(String relationId, String relationName, String relationState) {
        this.relationId = relationId;
        this.relationName = relationName;
        this.relationState = relationState;
    }

    /**
     * full constructor
     */
    public PendingMetaRelation(String relationId, PendingMetaTable parentTable, PendingMetaTable childTable, String relationName, String relationState, String relationComment, Date lastModifyDate, String recorder) {
//        this.parentTable=parentTable;
//        this.childTable=childTable;
        this.relationId = relationId;
        this.relationName = relationName;
        this.relationState = relationState;
        this.relationComment = relationComment;
        this.lastModifyDate = lastModifyDate;
        this.recorder = recorder;
    }


    public PendingMetaRelation copy(PendingMetaRelation other) {

        this.setRelationId(other.getRelationId());

//        this.childTable=other.getChildTable();
//        this.parentTable=other.getParentTable();
        this.relationName = other.getRelationName();
        this.relationState = other.getRelationState();
        this.relationComment = other.getRelationComment();
        this.lastModifyDate = other.getLastModifyDate();
        this.recorder = other.getRecorder();

        return this;
    }

    public PendingMetaRelation copyNotNullProperty(PendingMetaRelation other) {

        if (other.getRelationId() != null)
            this.setRelationId(other.getRelationId());
        if (other.getRelationDetails() != null)
            this.setRelationDetails(other.getRelationDetails());
//        if( other.getParentTable() != null)
//            this.parentTable= other.getParentTable();
//        if( other.getChildTable() != null)
//            this.childTable= other.getChildTable();
        if (other.getRelationName() != null)
            this.relationName = other.getRelationName();
        if (other.getRelationState() != null)
            this.relationState = other.getRelationState();
        if (other.getRelationComment() != null)
            this.relationComment = other.getRelationComment();
        if (other.getLastModifyDate() != null)
            this.lastModifyDate = other.getLastModifyDate();
        if (other.getRecorder() != null)
            this.recorder = other.getRecorder();
        return this;
    }

    public PendingMetaRelation clearProperties() {
//        this.parentTable= null;
//        this.childTable= null;
        this.relationName = null;
        this.relationState = null;
        this.relationComment = null;
        this.lastModifyDate = null;
        this.recorder = null;
        return this;
    }

    public MetaRelation mapToMetaRelation(){
        MetaRelation mr = new MetaRelation();
        mr.setRelationId(this.getRelationId());
        mr.setRelationName(this.getRelationName());
        mr.setRelationState(this.getRelationState());
        mr.setRelationComment(this.getRelationComment());
        mr.setParentTableId(this.getParentTableId());
        mr.setChildTableId(this.getChildTableId());

        Set<PendingMetaRelDetail> pRelationDetails=this.getRelationDetails();
        mr.setRelationDetails(new ArrayList<>());
        Iterator<PendingMetaRelDetail> itr=pRelationDetails.iterator();
        while(itr.hasNext()){
            PendingMetaRelDetail pdetail=itr.next();
            MetaRelDetail  detail=new MetaRelDetail();
            detail.setRelationId(relationId);
            detail.setParentColumnName(pdetail.getParentColumnName());
            detail.setChildColumnName(pdetail.getChildColumnName());
            mr.getRelationDetails().add(detail);
        }
        return mr;

    }

    @Override
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    public String getReferenceCode() {
        return String.valueOf(this.relationId);
    }

    @Override
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    public String getReferenceName() {
        return this.relationName;
    }

    @Override
    @ApiModelProperty(value = "子表名称")
    public String getTableName() {
        return this.childTable==null?null:this.childTable.getTableName();
    }

    @Override
    @ApiModelProperty(value = "父表名称")
    public String getParentTableName() {
        return this.parentTable==null?null:this.parentTable.getTableName();
    }

    @Override
    @ApiModelProperty(hidden = true)
    public Map<String, String> getReferenceColumns() {
        if(relationDetails == null || relationDetails.size()<1) {
            return null;
        }
        Map<String, String> colMap = new HashMap<>(relationDetails.size()+1);
        for(PendingMetaRelDetail mrd : relationDetails){
            colMap.put(mrd.getParentColumnName(), mrd.getChildColumnName());
        }
        return colMap;
    }

    @Override
    @ApiModelProperty(hidden = true)
    public boolean containColumn(String sCol) {
        if(relationDetails == null || relationDetails.size()<1) {
            return false;
        }

        for(PendingMetaRelDetail mrd : relationDetails){
            if(StringUtils.equals(sCol, mrd.getChildColumnName())){
                return true;
            }
        }
        return false;
    }
}
