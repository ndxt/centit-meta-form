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
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.centit.dde.po.DatabaseInfo;


/**
 * create by scaffold 2016-06-01 
 
 
  未落实表元数据表null   
*/
@Entity
@Table(name = "F_PENDING_META_TABLE")
public class PendingMetaTable implements java.io.Serializable {
	private static final long serialVersionUID =  1L;



	/**
	 * 表ID 表单主键 
	 */
	@Id
	@Column(name = "TABLE_ID")
	//1.用一张hibernate_sequences表管理主键,需要建hibernate_sequences表
	/*@GeneratedValue(strategy=GenerationType.TABLE,generator="table_generator")
	@TableGenerator(name = "table_generator",table="hibernate_sequences",initialValue=200000001,
	pkColumnName="SEQ_NAME",pkColumnValue="pendingtableId",allocationSize=1,valueColumnName="SEQ_VALUE")*/
	//2.用序列
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seqgen")
	@SequenceGenerator(sequenceName="SEQ_PENDINGTABLEID",name="seqgen",allocationSize=1,initialValue=1)
	private Long tableId;

	/**
	 * 所属数据库ID null 
	 */
	@JoinColumn(name="DATABASE_CODE", nullable = true)  
	@ManyToOne
	@NotFound(action=NotFoundAction.IGNORE)
	private DatabaseInfo  databaseInfo;
	/**
	 * 表代码 null 
	 */
	@Column(name = "TABLE_NAME")
	@NotBlank(message = "字段不能为空")
	@Length(min = 0, max = 32, message = "字段长度不能小于{min}大于{max}")
	private String  tableName;
	/**
	 * 表名称 null 
	 */
	@Column(name = "TABLE_LABEL_NAME")
	@NotBlank(message = "字段不能为空")
	@Length(min = 0, max = 64, message = "字段长度不能小于{min}大于{max}")
	private String  tableLabelName;
	/**
	 * 类别 表/视图 目前只能是表 
	 */
	@Column(name = "TABLE_TYPE")
	@NotBlank(message = "字段不能为空")
	@Length(min = 0,  message = "字段长度不能小于{min}大于{max}")
	private String  tableType;
	/**
	 * 状态 系统 S / R 查询(只读)/ N 新建(读写) 
	 */
	@Column(name = "TABLE_STATE")
	@NotBlank(message = "字段不能为空")
	@Length(min = 0,  message = "字段长度不能小于{min}大于{max}")
	private String  tableState;
	/**
	 * 描述 null 
	 */
	@Column(name = "TABLE_COMMENT")
	@Length(min = 0, max = 256, message = "字段长度不能小于{min}大于{max}")
	private String  tableComment;
	/**
	 * 与流程中业务关联关系
	 * 0: 不关联工作流 1：和流程业务关联 2： 和流程过程关联
	 */
	@Column(name = "Workflow_OPT_TYPE")
	@NotBlank(message = "字段不能为空")
	@Length(min = 0, max=1, message = "字段长度不能小于{min}大于{max}")
	private String  workFlowOptType;
	
	//Y/N 更新时是否校验时间戳 添加 Last_modify_time datetime
	@Column(name = "update_check_timestamp")
	@NotBlank(message = "字段不能为空")
	@Length(min = 0, max=1, message = "字段长度不能小于{min}大于{max}")
	private String  updateCheckTimeStamp;
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

	
	@OneToMany(mappedBy="cid.mdTable",orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	//@JoinColumn(name="TABLE_ID")
	/*@JoinColumns({
	       @JoinColumn(name="wfcode", referencedColumnName="wfcode"),
	       @JoinColumn(name="version", referencedColumnName="version")
	    })*/
	private Set<PendingMetaColumn> mdColumns;
	
	@Transient
	@OneToMany(mappedBy="mdTable",orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<PendingMetaRelation> mdRelations;
	
	
	
	
	// Constructors
	/** default constructor */
	public PendingMetaTable() {
	}
	/** minimal constructor 
	 * @param workFlowOptType 
	 * @param updateCheckTimeStamp */
	public PendingMetaTable(
		Long tableId		
		,String  tableName,String  tableLabelName,String  tableType,String  tableState,String  isInWorkflow, String workFlowOptType, String updateCheckTimeStamp) {
	
	
		this.tableId = tableId;		
		
		this.tableName= tableName; 
		this.tableLabelName= tableLabelName; 
		this.tableType= tableType; 
		this.tableState= tableState; 
		this.workFlowOptType=workFlowOptType;
		this.updateCheckTimeStamp=updateCheckTimeStamp;
	}

	
	/** full constructor 
	 * @param updateCheckTimeStamp 
	 * @param workFlowOptType */
	public PendingMetaTable(
	 Long tableId		
	,String  databaseCode,String  tableName,String  tableLabelName,String  tableType,String  tableState,String  tableComment,String  isInWorkflow,Date  lastModifyDate,String  recorder, String updateCheckTimeStamp, String workFlowOptType) {
	
	
		this.tableId = tableId;		
		
		this.setDatabaseCode(databaseCode);
		this.tableName= tableName;
		this.tableLabelName= tableLabelName;
		this.tableType= tableType;
		this.tableState= tableState;
		this.tableComment= tableComment;
		this.workFlowOptType=workFlowOptType;
		this.updateCheckTimeStamp=updateCheckTimeStamp;
		this.lastModifyDate= lastModifyDate;
		this.recorder= recorder;		
	}
	
	
	public Set<PendingMetaColumn> getMdColumns() {
		if(null==this.mdColumns)
			this.mdColumns=new HashSet<PendingMetaColumn>();
		return mdColumns;
	}
	public void setMdColumns(Set<PendingMetaColumn> mdColumns1) {
		this.getMdColumns().clear();
		Iterator<PendingMetaColumn> itr=mdColumns1.iterator();
		while(itr.hasNext()){
			itr.next().getCid().setMdTable(this);
		}
		this.getMdColumns().addAll(mdColumns1);
	}
	public Set<PendingMetaRelation> getMdRelations() {
		return mdRelations;
	}
	public void setMdRelations(Set<PendingMetaRelation> mdRelations) {
		this.mdRelations = mdRelations;
	}
	
	
	
	public DatabaseInfo getDatabaseInfo(){
		if(null==this.databaseInfo)
			this.databaseInfo=new DatabaseInfo();
		return this.databaseInfo;
	}
  
	public void setDatabaseInfo(DatabaseInfo dbInfo){
		this.databaseInfo=dbInfo;
	}
	public Long getTableId() {
		return this.tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}
	// Property accessors
  
	public String getDatabaseCode() {
		return this.getDatabaseInfo().getDatabaseCode();
	}
	
	public void setDatabaseCode(String databaseCode) {
		this.databaseInfo=new DatabaseInfo();
		this.databaseInfo.setDatabaseCode(databaseCode);
	}
  
	public String getTableName() {
		return this.tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
  
	public String getTableLabelName() {
		return this.tableLabelName;
	}
	
	public void setTableLabelName(String tableLabelName) {
		this.tableLabelName = tableLabelName;
	}
  
	public String getWorkFlowOptType() {
		return workFlowOptType;
	}
	public void setWorkFlowOptType(String workFlowOptType) {
		this.workFlowOptType = workFlowOptType;
	}
	public String getUpdateCheckTimeStamp() {
		return updateCheckTimeStamp;
	}
	public void setUpdateCheckTimeStamp(String updateCheckTimeStamp) {
		this.updateCheckTimeStamp = updateCheckTimeStamp;
	}
	
	public String getTableType() {
		return this.tableType;
	}
	
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
  
	public String getTableState() {
		return this.tableState;
	}
	
	public void setTableState(String tableState) {
		this.tableState = tableState;
	}
  
	public String getTableComment() {
		return this.tableComment;
	}
	
	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
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

	public String getDatabaseName(){
		return getDatabaseInfo().getDatabaseName();
	}


	public PendingMetaTable copy(PendingMetaTable other){
		this.setMdColumns(other.getMdColumns());
		this.setTableId(other.getTableId());
		this.setDatabaseInfo(other.getDatabaseInfo());
		this.tableName= other.getTableName();  
		this.tableLabelName= other.getTableLabelName();  
		this.tableType= other.getTableType();  
		this.tableState= other.getTableState();  
		this.tableComment= other.getTableComment();  
		this.workFlowOptType=other.getWorkFlowOptType();
		this.updateCheckTimeStamp=other.getUpdateCheckTimeStamp();
		this.lastModifyDate= other.getLastModifyDate();  
		this.recorder= other.getRecorder();
		return this;
	}
	
	public PendingMetaTable copyNotNullProperty(PendingMetaTable other){
  
		if( other.getTableId() != null)
			this.setTableId(other.getTableId());
		if(other.getMdColumns()!=null)
			this.setMdColumns(other.getMdColumns());
		if( other.getDatabaseCode() != null)
			this.databaseInfo=other.getDatabaseInfo(); 
		if( other.getTableName() != null)
			this.tableName= other.getTableName();  
		if( other.getTableLabelName() != null)
			this.tableLabelName= other.getTableLabelName();  
		if( other.getTableType() != null)
			this.tableType= other.getTableType();  
		if( other.getTableState() != null)
			this.tableState= other.getTableState();  
		if( other.getTableComment() != null)
			this.tableComment= other.getTableComment();  
		if( other.getWorkFlowOptType() != null)
			this.workFlowOptType= other.getWorkFlowOptType();  
		if(other.getUpdateCheckTimeStamp()!=null)
			this.updateCheckTimeStamp=other.getUpdateCheckTimeStamp();
		if( other.getLastModifyDate() != null)
			this.lastModifyDate= other.getLastModifyDate();  
		if( other.getRecorder() != null)
			this.recorder= other.getRecorder();		
		return this;
	}

	public PendingMetaTable clearProperties(){
  
		this.databaseInfo=null;
		this.tableName= null;  
		this.tableLabelName= null;  
		this.tableType= null;  
		this.tableState= null;  
		this.tableComment= null;  
		this.workFlowOptType=null;
		this.updateCheckTimeStamp=null;
		this.lastModifyDate= null;  
		this.recorder= null;

		return this;
	}
}
