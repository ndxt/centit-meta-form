package com.centit.metaform.po;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.alibaba.fastjson.annotation.JSONField;
import com.centit.support.database.metadata.TableReference;


/**
 * create by scaffold 2016-06-02 
 
 
  表关联关系表null   
*/
@Entity
@Table(name = "F_META_RELATION")
public class MetaRelation implements TableReference, java.io.Serializable {
	private static final long serialVersionUID =  1L;



	/**
	 * 关联代码 关联关系，类似与外键，但不创建外键 
	 */
	@Id
	@Column(name = "RELATION_ID")
	@GeneratedValue(generator = "assignedGenerator")
	@GenericGenerator(name = "assignedGenerator", strategy = "assigned")
	private Long relationId;

	
	
	/*@JoinColumn(name="TABLE_ID")
	@ManyToOne
	private MdTable mdTable;*/
	/**
	 * 主表表ID 表单主键 
	 */
	@JoinColumn(name = "PARENT_TABLE_ID")
	@ManyToOne
	@JSONField(serialize=false)
	private MetaTable  parentTable;
	/**
	 * 从表表ID 表单主键 
	 */
	@JoinColumn(name = "CHILD_TABLE_ID")
	@ManyToOne
	@JSONField(serialize=false)
	private MetaTable  childTable;
	/**
	 * 关联名称 null 
	 */
	@Column(name = "RELATION_NAME")
	@NotBlank(message = "字段不能为空")
	@Length(max = 64, message = "字段长度不能大于{max}")
	private String  relationName;
	/**
	 * 状态 null 
	 */
	@Column(name = "RELATION_STATE")
	private String  relationState;
	/**
	 * 关联说明 null 
	 */
	@Column(name = "RELATION_COMMENT")
	@Length(max = 256, message = "字段长度不能大于{max}")
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
	@Length(max = 8, message = "字段长度不能大于{max}")
	private String  recorder;

	
	@OneToMany(mappedBy="cid.relation",orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<MetaRelDetail> relationDetails;
	
	
	// Constructors
	/** default constructor */
	public MetaRelation() {
	}
	/** minimal constructor */
	public MetaRelation(
		Long relationId		
		,String  relationName,String  relationState) {
	
	
		this.relationId = relationId;		
	
		this.relationName= relationName; 
		this.relationState= relationState; 		
	}

	/** full constructor */
	public MetaRelation(
	 Long relationId		
	,MetaTable  parentTableId,MetaTable  childTableId,String  relationName,String  relationState,String  relationComment,Date  lastModifyDate,String  recorder) {
	
	
		this.relationId = relationId;		
		this.parentTable= parentTableId;
		this.childTable= childTableId;
		this.relationName= relationName;
		this.relationState= relationState;
		this.relationComment= relationComment;
		this.lastModifyDate= lastModifyDate;
		this.recorder= recorder;		
	}
	

  
	public MetaRelation(PendingMetaRelation next) {
		this.setRelationId(next.getRelationId());
		this.relationName= next.getRelationName();  
		this.relationState= next.getRelationState();  
		this.relationComment= next.getRelationComment();  
		this.lastModifyDate= next.getLastModifyDate();  
		this.recorder= next.getRecorder();
		
		Set<PendingMetaRelDetail> pRelationDetails=next.getRelationDetails();
		this.relationDetails=new HashSet<MetaRelDetail>();
		Iterator<PendingMetaRelDetail> itr=pRelationDetails.iterator();
		while(itr.hasNext()){
			PendingMetaRelDetail pdetail=itr.next();
			MetaRelDetail  detail=new MetaRelDetail(new MetaRelDetailId(this,pdetail.getParentColumnName()),pdetail.getChildColumnName());
			this.relationDetails.add(detail);
		}
	}
	
	
	
	public Set<MetaRelDetail> getRelationDetails() {
		return relationDetails;
	}
	
	public void setRelationDetails(Set<MetaRelDetail> relationDetails) {
		this.relationDetails = relationDetails;
	}
	
	public Long getRelationId() {
		return this.relationId;
	}

	public void setRelationId(Long relationId) {
		this.relationId = relationId;
	}
	// Property accessors
  
	public Long getParentTableId() {
		if(null==this.parentTable)
			return null;
		return this.parentTable.getTableId();
	}
	
	public void setParentTableId(Long parentTableId) {
		if(null==this.parentTable)
			this.parentTable=new MetaTable();
		this.parentTable.setTableId(parentTableId);
	}
  
	public Long getChildTableId() {
		if(null==this.childTable)
			return null;
		return this.childTable.getTableId();
	}
	
	public void setChildTableId(Long childTableId) {
		if(null==this.childTable)
			this.childTable=new MetaTable();
		this.parentTable.setTableId(childTableId);
	}
  
	public String getRelationName() {
		return this.relationName;
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



	public MetaRelation copy(MetaRelation other){
  
		this.setRelationId(other.getRelationId());
  
		
		this.relationName= other.getRelationName();  
		this.relationState= other.getRelationState();  
		this.relationComment= other.getRelationComment();  
		this.lastModifyDate= other.getLastModifyDate();  
		this.recorder= other.getRecorder();

		return this;
	}
	
	public MetaRelation copyNotNullProperty(MetaRelation other){
  
	if( other.getRelationId() != null)
		this.setRelationId(other.getRelationId());
  
		if( other.getParentTableId() != null)
			this.parentTable=other.getParentTable();  
		if( other.getChildTableId() != null)
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

	public MetaTable getParentTable() {
		return parentTable;
	}
	public void setParentTable(MetaTable parentTable) {
		this.parentTable = parentTable;
	}
	public MetaTable getChildTable() {
		return childTable;
	}
	public void setChildTable(MetaTable childTable) {
		this.childTable = childTable;
	}
	public MetaRelation clearProperties(){
  
		this.parentTable= null;  
		this.childTable= null;  
		this.relationName= null;  
		this.relationState= null;  
		this.relationComment= null;  
		this.lastModifyDate= null;  
		this.recorder= null;

		return this;
	}
	
	@Override
	public String getReferenceCode() {
		return String.valueOf( this.relationId);
	}
	@Override
	public String getReferenceName() {
		return this.relationName;
	}
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return String.valueOf( this.childTable); 
	}
	@Override
	public String getParentTableName() {
		// TODO Auto-generated method stub
		return String.valueOf( this.parentTable); 
	}
	@Override
	public Map<String, String> getReferenceColumns() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean containColumn(String sCol) {
		// TODO Auto-generated method stub
		return false;
	}
}
