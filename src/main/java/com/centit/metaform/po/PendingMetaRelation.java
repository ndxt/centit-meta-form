package com.centit.metaform.po;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.alibaba.fastjson.annotation.JSONField;
import com.centit.framework.core.po.EntityWithTimestamp;


/**
 * create by scaffold 2016-06-01 
 
 
  未落实表关联关系表null   
*/
@Entity
@Table(name = "F_PENDING_META_RELATION")
public class PendingMetaRelation implements EntityWithTimestamp,java.io.Serializable {
	private static final long serialVersionUID =  1L;



	/**
	 * 关联代码 关联关系，类似与外键，但不创建外键 
	 */
	@Id
	@Column(name = "RELATION_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seqgen")
	@SequenceGenerator(sequenceName="SEQ_PENDINGRELATIONID",name="seqgen",allocationSize=1,initialValue=1)
	private Long relationId;

	/**
	 * 主表表ID 表单主键 
	 */
	@JoinColumn(name = "PARENT_TABLE_ID")
	@ManyToOne
	@JSONField(serialize=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private PendingMetaTable  parentTable;
	/**
	 * 从表表ID 表单主键 
	 */
	@JoinColumn(name = "CHILD_TABLE_ID")
	@ManyToOne
	@JSONField(serialize=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private PendingMetaTable  childTable;
	/**
	 * 关联名称 null 
	 */
	@Column(name = "RELATION_NAME")
	@NotBlank(message = "字段不能为空")
	@Length(min = 0, max = 64, message = "字段长度不能小于{min}大于{max}")
	private String  relationName;
	/**
	 * 状态 null 
	 */
	@Column(name = "RELATION_STATE")
	@Length(min = 0,  message = "字段长度不能小于{min}大于{max}")
	private String  relationState;
	/**
	 * 关联说明 null 
	 */
	@Column(name = "RELATION_COMMENT")
	@Length(min = 0, max = 256, message = "字段长度不能小于{min}大于{max}")
	private String  relationComment;
	/**
	 * 更改时间 null 
	 */
	@Column(name = "LAST_MODIFY_DATE")
	private Date  lastModifyDate;
	/**
	 * 更改人员 null 
	 */
	@Column(name = "RECORDER")
	@Length(min = 0, max = 8, message = "字段长度不能小于{min}大于{max}")
	private String  recorder;
	
	
	
	@OneToMany(mappedBy="cid.relation",orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<PendingMetaRelDetail> relationDetails;

	// Constructors
	/** default constructor */
	public PendingMetaRelation() {
	}
	/** minimal constructor */
	public PendingMetaRelation(Long relationId,String  relationName,String  relationState) {
		this.relationId = relationId;		
		this.relationName= relationName; 
		this.relationState= relationState; 		
	}

	/** full constructor */
	public PendingMetaRelation(Long relationId,PendingMetaTable  parentTable,PendingMetaTable  childTable,String  relationName,String  relationState,String  relationComment,Date  lastModifyDate,String  recorder) {
		this.parentTable=parentTable;
		this.childTable=childTable;
		this.relationId = relationId;		
		this.relationName= relationName;
		this.relationState= relationState;
		this.relationComment= relationComment;
		this.lastModifyDate= lastModifyDate;
		this.recorder= recorder;		
	}
	
	public Long getChildTableId(){
    	if(null!=this.getChildTable())
    		return this.getChildTable().getTableId();
    	return null;
    }
	
    public String getChildTableName(){
    	if(null!=this.getChildTable())
    		return this.getChildTable().getTableLabelName();
    	return null;
    }
    
	public Set<PendingMetaRelDetail> getRelationDetails() {
		if(null==this.relationDetails)
			this.relationDetails=new HashSet<PendingMetaRelDetail>();
		return relationDetails;
	}
	
	public void setRelationDetails(Set<PendingMetaRelDetail> relationDetails) {
		if(null==relationDetails){
			this.relationDetails=null;
		}else{
			this.getRelationDetails().clear();
			Iterator<PendingMetaRelDetail> itr=relationDetails.iterator();
			while(itr.hasNext()){
				itr.next().getCid().setRelation(this);
			}
			this.getRelationDetails().addAll(relationDetails);
		}
	}
	
	public PendingMetaTable getParentTable() {
		return parentTable;
	}
	
	public void setParentTable(PendingMetaTable parentTable) {
		
		this.parentTable = parentTable;
	}
	
	public PendingMetaTable getChildTable() {
		return childTable;
	}
	
	public void setChildTable(PendingMetaTable childTable) {
		if(null==this.childTable)
			this.childTable=new PendingMetaTable();
		this.childTable = childTable;
	}
	
	public Long getRelationId() {
		return this.relationId;
	}

	public void setRelationId(Long relationId) {
		this.relationId = relationId;
	}
	// Property accessors
  
	public String getRelationName() {
		return this.relationName;
	}
	
	public void setChildTableId(Long childTableId){
		if(null==this.childTable)
			this.childTable=new PendingMetaTable();
		this.childTable.setTableId(childTableId);
	}
	
	public void setParentTableId(Long parentTableId){
		if(null==this.parentTable)
			this.parentTable=new PendingMetaTable();
		this.parentTable.setTableId(parentTableId);
	}
	
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
  
	public String getRelationState() {
		return this.relationState;
	}
	
	public void setRelationState(String relationState) {
		this.relationState = relationState;
	}
  
	public String getRelationComment() {
		return this.relationComment;
	}
	
	public void setRelationComment(String relationComment) {
		this.relationComment = relationComment;
	}
  
	public Date getLastModifyDate() {
		return this.lastModifyDate;
	}
	
	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
  
	public String getRecorder() {
		return this.recorder;
	}
	
	public void setRecorder(String recorder) {
		this.recorder = recorder;
	}



	public PendingMetaRelation copy(PendingMetaRelation other){
  
		this.setRelationId(other.getRelationId());
  
		this.childTable=other.getChildTable();
		this.parentTable=other.getParentTable();
		this.relationName= other.getRelationName();  
		this.relationState= other.getRelationState();  
		this.relationComment= other.getRelationComment();  
		this.lastModifyDate= other.getLastModifyDate();  
		this.recorder= other.getRecorder();

		return this;
	}
	
	public PendingMetaRelation copyNotNullProperty(PendingMetaRelation other){
  
		if( other.getRelationId() != null)
			this.setRelationId(other.getRelationId());
		if(other.getRelationDetails()!=null)
			this.setRelationDetails(other.getRelationDetails());
		if( other.getParentTable() != null)
			this.parentTable= other.getParentTable();  
		if( other.getChildTable() != null)
			this.childTable= other.getChildTable();  
		if( other.getRelationName() != null)
			this.relationName= other.getRelationName();  
		if( other.getRelationState() != null)
			this.relationState= other.getRelationState();  
		if( other.getRelationComment() != null)
			this.relationComment= other.getRelationComment();  
		if( other.getLastModifyDate() != null)
			this.lastModifyDate= other.getLastModifyDate();  
		if( other.getRecorder() != null)
			this.recorder= other.getRecorder();		
		return this;
	}

	public PendingMetaRelation clearProperties(){
		this.parentTable= null;  
		this.childTable= null;  
		this.relationName= null;  
		this.relationState= null;  
		this.relationComment= null;  
		this.lastModifyDate= null;  
		this.recorder= null;
		return this;
	}
	
}
