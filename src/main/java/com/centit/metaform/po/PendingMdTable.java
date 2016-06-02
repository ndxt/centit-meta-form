package com.centit.metaform.po;

import java.util.Date;
import java.sql.Timestamp;

import javax.persistence.Column;


import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.persistence.GeneratedValue;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.centit.dde.po.DatabaseInfo;


/**
 * create by scaffold 2016-06-01 
 
 
  未落实表元数据表null   
*/
@Entity
@Table(name = "F_PENDING_MD_TABLE")
public class PendingMdTable implements java.io.Serializable {
	private static final long serialVersionUID =  1L;



	/**
	 * 表ID 表单主键 
	 */
	@Id
	@Column(name = "Table_ID")
	@GeneratedValue(generator = "assignedGenerator")
	@GenericGenerator(name = "assignedGenerator", strategy = "assigned")
	private Long tableId;

	/**
	 * 所属数据库ID null 
	 */
	@JoinColumn(name="Database_Code", nullable = true)  
	@ManyToOne
	private DatabaseInfo  databaseInfo;
	/**
	 * 表代码 null 
	 */
	@Column(name = "Table_Name")
	@NotBlank(message = "字段不能为空")
	@Length(min = 0, max = 32, message = "字段长度不能小于{min}大于{max}")
	private String  tableName;
	/**
	 * 表名称 null 
	 */
	@Column(name = "Table_Label_Name")
	@NotBlank(message = "字段不能为空")
	@Length(min = 0, max = 64, message = "字段长度不能小于{min}大于{max}")
	private String  tableLabelName;
	/**
	 * 类别 表/视图 目前只能是表 
	 */
	@Column(name = "table_type")
	@NotBlank(message = "字段不能为空")
	@Length(min = 0,  message = "字段长度不能小于{min}大于{max}")
	private String  tableType;
	/**
	 * 状态 系统 S / R 查询(只读)/ N 新建(读写) 
	 */
	@Column(name = "table_state")
	@NotBlank(message = "字段不能为空")
	@Length(min = 0,  message = "字段长度不能小于{min}大于{max}")
	private String  tableState;
	/**
	 * 描述 null 
	 */
	@Column(name = "table_Comment")
	@Length(min = 0, max = 256, message = "字段长度不能小于{min}大于{max}")
	private String  tableComment;
	/**
	 * 是否是流程中的业务表 null 
	 */
	@Column(name = "Is_In_Workflow")
	@NotBlank(message = "字段不能为空")
	@Length(min = 0,  message = "字段长度不能小于{min}大于{max}")
	private String  isInWorkflow;
	/**
	 * 更改时间 null 
	 */
	@Column(name = "last_modify_Date")
	private Date  lastModifyDate;
	/**
	 * 更改人员 null 
	 */
	@Column(name = "Recorder")
	@Length(min = 0, max = 8, message = "字段长度不能小于{min}大于{max}")
	private String  recorder;

	
	// Constructors
	/** default constructor */
	public PendingMdTable() {
	}
	/** minimal constructor */
	public PendingMdTable(
		Long tableId		
		,String  tableName,String  tableLabelName,String  tableType,String  tableState,String  isInWorkflow) {
	
	
		this.tableId = tableId;		
	
		this.tableName= tableName; 
		this.tableLabelName= tableLabelName; 
		this.tableType= tableType; 
		this.tableState= tableState; 
		this.isInWorkflow= isInWorkflow; 		
	}

/** full constructor */
	public PendingMdTable(
	 Long tableId		
	,String  databaseCode,String  tableName,String  tableLabelName,String  tableType,String  tableState,String  tableComment,String  isInWorkflow,Date  lastModifyDate,String  recorder) {
	
	
		this.tableId = tableId;		
	
		this.setDatabaseCode(databaseCode);
		this.tableName= tableName;
		this.tableLabelName= tableLabelName;
		this.tableType= tableType;
		this.tableState= tableState;
		this.tableComment= tableComment;
		this.isInWorkflow= isInWorkflow;
		this.lastModifyDate= lastModifyDate;
		this.recorder= recorder;		
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
		this.databaseInfo=new DatabaseInfo(databaseCode);
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
  
	public String getIsInWorkflow() {
		return this.isInWorkflow;
	}
	
	public void setIsInWorkflow(String isInWorkflow) {
		this.isInWorkflow = isInWorkflow;
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


	public PendingMdTable copy(PendingMdTable other){
  
		this.setTableId(other.getTableId());
  
		this.setDatabaseInfo(other.getDatabaseInfo());
		this.tableName= other.getTableName();  
		this.tableLabelName= other.getTableLabelName();  
		this.tableType= other.getTableType();  
		this.tableState= other.getTableState();  
		this.tableComment= other.getTableComment();  
		this.isInWorkflow= other.getIsInWorkflow();  
		this.lastModifyDate= other.getLastModifyDate();  
		this.recorder= other.getRecorder();

		return this;
	}
	
	public PendingMdTable copyNotNullProperty(PendingMdTable other){
  
	if( other.getTableId() != null)
		this.setTableId(other.getTableId());
  
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
		if( other.getIsInWorkflow() != null)
			this.isInWorkflow= other.getIsInWorkflow();  
		if( other.getLastModifyDate() != null)
			this.lastModifyDate= other.getLastModifyDate();  
		if( other.getRecorder() != null)
			this.recorder= other.getRecorder();		

		return this;
	}

	public PendingMdTable clearProperties(){
  
		this.databaseInfo=null;
		this.tableName= null;  
		this.tableLabelName= null;  
		this.tableType= null;  
		this.tableState= null;  
		this.tableComment= null;  
		this.isInWorkflow= null;  
		this.lastModifyDate= null;  
		this.recorder= null;

		return this;
	}
}
