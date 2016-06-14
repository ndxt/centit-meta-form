package com.centit.metaform.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.centit.dde.po.DatabaseInfo;
import com.centit.support.database.metadata.TableField;
import com.centit.support.database.metadata.TableInfo;
import com.centit.support.database.metadata.TableReference;

/**
 * create by scaffold 2016-06-02 
 
 
  表元数据表状态分为 系统/查询/更新
系统，不可以做任何操作
查询，仅用于通用查询模块，不可以更新
更新，可以更新
   
*/
@Entity
@Table(name = "F_MD_TABLE")
public class MetaTable implements TableInfo,java.io.Serializable {
	private static final long serialVersionUID =  1L;
	/**
	 * 表ID 表编号 
	 */
	@Id
	@Column(name = "TABLE_ID")
	@GeneratedValue(generator = "assignedGenerator")
	@GenericGenerator(name = "assignedGenerator", strategy = "assigned")
	private Long tableId;

	/**
	 * 所属数据库ID null 
	 */
	@JoinColumn(name="DATABASE_CODE", nullable = true)  
	@ManyToOne
	private DatabaseInfo  databaseInfo;
	
	
	
	public DatabaseInfo getDatabaseInfo() {
		if(null==this.databaseInfo)
			this.databaseInfo=new DatabaseInfo();
		return databaseInfo;
	}
	public void setDatabaseInfo(DatabaseInfo databaseInfo) {
		this.databaseInfo = databaseInfo;
	}
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
	
	
	@OneToMany(mappedBy="mdTable",orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<MetaColumn> mdColumns;
	
	@Transient
	@OneToMany(mappedBy="mdTable",orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<MetaRelation> mdRelations;
	
	
	@OneToMany(mappedBy="mdTable",orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<MetaFormModel> metaFormModels;

	// Constructors
	/** default constructor */
	public MetaTable() {
	}
	
	
	public MetaTable(PendingMetaTable ptable) {
		this.tableId = ptable.getTableId();		
		this.databaseInfo=ptable.getDatabaseInfo();
		this.tableName= ptable.getTableName();
		this.tableLabelName= ptable.getTableLabelName();
		this.tableType=ptable.getTableType();
		this.tableState= ptable.getTableState();
		this.tableComment= ptable.getTableComment();
		this.isInWorkflow= ptable.getIsInWorkflow();
		this.lastModifyDate= ptable.getLastModifyDate();
		this.recorder= ptable.getRecorder();
	}
	
	
	/** minimal constructor */
	public MetaTable(
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
	public MetaTable(
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
		this.setDatabaseCode(databaseCode);
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


	public Set<MetaColumn> getMdColumns(){
		if(this.mdColumns==null)
			this.mdColumns = new HashSet<MetaColumn>();
		return this.mdColumns;
	}

	public void setMdColumns(Set<MetaColumn> mdColumns1) {
		this.getMdColumns().clear();
		Iterator<MetaColumn> itr=mdColumns1.iterator();
		while(itr.hasNext()){
			itr.next().getCid().setMdTable(this);
		}
		this.getMdColumns().addAll(mdColumns1);
	}	

	public void addMdColumn(MetaColumn mdColumn ){
		if (this.mdColumns==null)
			this.mdColumns = new HashSet<MetaColumn>();
		this.mdColumns.add(mdColumn);
	}
	
	public void removeMdColumn(MetaColumn mdColumn ){
		if (this.mdColumns==null)
			return;
		this.mdColumns.remove(mdColumn);
	}
	
	public MetaColumn newMdColumn(){
		MetaColumn res = new MetaColumn();
  
		res.setTableId(this.getTableId());

		return res;
	}
	/**
	 * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
	 * 
	 */
	public void replaceMdColumns(Set<MetaColumn> set) {
		List<MetaColumn> newObjs = new ArrayList<MetaColumn>();
		for(MetaColumn p :set){
			if(p==null)
				continue;
			MetaColumn newdt = newMdColumn();
			newdt.copyNotNullProperty(p);
			newObjs.add(newdt);
		}
		//delete
		boolean found = false;
		Set<MetaColumn> oldObjs = new HashSet<MetaColumn>();
		oldObjs.addAll(getMdColumns());
		
		for(Iterator<MetaColumn> it=oldObjs.iterator(); it.hasNext();){
			MetaColumn odt = it.next();
			found = false;
			for(MetaColumn newdt :newObjs){
				if(odt.getCid().equals( newdt.getCid())){
					found = true;
					break;
				}
			}
			if(! found)
				removeMdColumn(odt);
		}
		oldObjs.clear();
		//insert or update
		for(MetaColumn newdt :newObjs){
			found = false;
			for(Iterator<MetaColumn> it=getMdColumns().iterator();
			 it.hasNext();){
				MetaColumn odt = it.next();
				if(odt.getCid().equals( newdt.getCid())){
					odt.copy(newdt);
					found = true;
					break;
				}
			}
			if(! found)
				addMdColumn(newdt);
		} 	
	}	


	
	

	public Set<MetaRelation> getMdRelations(){
		if(this.mdRelations==null)
			this.mdRelations = new HashSet<MetaRelation>();
		return this.mdRelations;
	}

	public void setMdRelations(Set<MetaRelation> mdRelations) {
		this.mdRelations = mdRelations;
	}	

	public void addMdRelation(MetaRelation mdRelation ){
		if (this.mdRelations==null)
			this.mdRelations = new HashSet<MetaRelation>();
		this.mdRelations.add(mdRelation);
	}
	
	public void removeMdRelation(MetaRelation mdRelation ){
		if (this.mdRelations==null)
			return;
		this.mdRelations.remove(mdRelation);
	}
	
	public MetaRelation newMdRelation(){
		MetaRelation res = new MetaRelation();
  
		res.setChildTableId(this.getTableId());

		return res;
	}
	/**
	 * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
	 * 
	 */
	public void replaceMdRelations(Set<MetaRelation> set) {
		List<MetaRelation> newObjs = new ArrayList<MetaRelation>();
		for(MetaRelation p :set){
			if(p==null)
				continue;
			MetaRelation newdt = newMdRelation();
			newdt.copyNotNullProperty(p);
			newObjs.add(newdt);
		}
		//delete
		boolean found = false;
		Set<MetaRelation> oldObjs = new HashSet<MetaRelation>();
		oldObjs.addAll(getMdRelations());
		
		for(Iterator<MetaRelation> it=oldObjs.iterator(); it.hasNext();){
			MetaRelation odt = it.next();
			found = false;
			for(MetaRelation newdt :newObjs){
				if(odt.getRelationId().equals( newdt.getRelationId())){
					found = true;
					break;
				}
			}
			if(! found)
				removeMdRelation(odt);
		}
		oldObjs.clear();
		//insert or update
		for(MetaRelation newdt :newObjs){
			found = false;
			for(Iterator<MetaRelation> it=getMdRelations().iterator();
			 it.hasNext();){
				MetaRelation odt = it.next();
				if(odt.getRelationId().equals( newdt.getRelationId())){
					odt.copy(newdt);
					found = true;
					break;
				}
			}
			if(! found)
				addMdRelation(newdt);
		} 	
	}	

	public Set<MetaFormModel> getMetaFormModels(){
		if(this.metaFormModels==null)
			this.metaFormModels = new HashSet<MetaFormModel>();
		return this.metaFormModels;
	}

	public void setMetaFormModels(Set<MetaFormModel> metaFormModels) {
		this.metaFormModels = metaFormModels;
	}	

	public void addMetaFormModel(MetaFormModel metaFormModel ){
		if (this.metaFormModels==null)
			this.metaFormModels = new HashSet<MetaFormModel>();
		this.metaFormModels.add(metaFormModel);
	}
	
	public void removeMetaFormModel(MetaFormModel metaFormModel ){
		if (this.metaFormModels==null)
			return;
		this.metaFormModels.remove(metaFormModel);
	}
	
	public MetaFormModel newMetaFormModel(){
		MetaFormModel res = new MetaFormModel();
  
		res.setTableId(this.getTableId());

		return res;
	}
	/**
	 * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
	 * 
	 */
	public void replaceMetaFormModels(Set<MetaFormModel> set) {
		List<MetaFormModel> newObjs = new ArrayList<MetaFormModel>();
		for(MetaFormModel p :set){
			if(p==null)
				continue;
			MetaFormModel newdt = newMetaFormModel();
			newdt.copyNotNullProperty(p);
			newObjs.add(newdt);
		}
		//delete
		boolean found = false;
		Set<MetaFormModel> oldObjs = new HashSet<MetaFormModel>();
		oldObjs.addAll(getMetaFormModels());
		
		for(Iterator<MetaFormModel> it=oldObjs.iterator(); it.hasNext();){
			MetaFormModel odt = it.next();
			found = false;
			for(MetaFormModel newdt :newObjs){
				if(odt.getModelCode().equals( newdt.getModelCode())){
					found = true;
					break;
				}
			}
			if(! found)
				removeMetaFormModel(odt);
		}
		oldObjs.clear();
		//insert or update
		for(MetaFormModel newdt :newObjs){
			found = false;
			for(Iterator<MetaFormModel> it=getMetaFormModels().iterator();
			 it.hasNext();){
				MetaFormModel odt = it.next();
				if(odt.getModelCode().equals( newdt.getModelCode())){
					odt.copy(newdt);
					found = true;
					break;
				}
			}
			if(! found)
				addMetaFormModel(newdt);
		} 	
	}	


	public MetaTable copy(MetaTable other){
		this.mdColumns=other.getMdColumns();
		this.setTableId(other.getTableId());
		this.setDatabaseCode(other.getDatabaseCode());
		this.tableName= other.getTableName();  
		this.tableLabelName= other.getTableLabelName();  
		this.tableType= other.getTableType();  
		this.tableState= other.getTableState();  
		this.tableComment= other.getTableComment();  
		this.isInWorkflow= other.getIsInWorkflow();  
		this.lastModifyDate= other.getLastModifyDate();  
		this.recorder= other.getRecorder();
		this.mdColumns = other.getMdColumns();	
		this.mdRelations = other.getMdRelations();	
		this.mdRelations = other.getMdRelations();	
		this.metaFormModels = other.getMetaFormModels();
		return this;
	}
	
	public MetaTable copyNotNullProperty(MetaTable other){
  
		if(null!=other.getMdColumns())
			this.setMdColumns(other.getMdColumns());
		if( other.getTableId() != null)
			this.setTableId(other.getTableId());
		if( other.getDatabaseCode() != null)
			this.setDatabaseCode(other.getDatabaseCode());
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
	
		//this.mdColumns = other.getMdColumns();
		//replaceMdColumns(other.getMdColumns());
			
		//this.mdRelations = other.getMdRelations();
        //replaceMdRelations(other.getMdRelations());
			
		//this.mdRelations = other.getMdRelations();
        //replaceMdRelations(other.getMdRelations());
			
		//this.metaFormModels = other.getMetaFormModels();
        //replaceMetaFormModels(other.getMetaFormModels());
		
		return this;
	}

	public MetaTable clearProperties(){
  
		this.setDatabaseCode(null);
		this.tableName= null;  
		this.tableLabelName= null;  
		this.tableType= null;  
		this.tableState= null;  
		this.tableComment= null;  
		this.isInWorkflow= null;  
		this.lastModifyDate= null;  
		this.recorder= null;
	
		this.mdColumns = new HashSet<MetaColumn>();	
		this.mdRelations = new HashSet<MetaRelation>();	
		this.mdRelations = new HashSet<MetaRelation>();	
		this.metaFormModels = new HashSet<MetaFormModel>();
		return this;
	}

	@Override
	public String getPkName() {
		return "PK_"+ this.tableName;
	}
	@Override
	public String getSchema() {
		return null;
	}
	@Override
	public MetaColumn findFieldByName(String name) {
		if(mdColumns==null)
			return null;
		for(MetaColumn c: mdColumns){
			if(c.getPropertyName().equals(name))
				return c;
		}
		return null;
	}
	@Override
	public MetaColumn findFieldByColumn(String name) {
		if(mdColumns==null)
			return null;
		for(MetaColumn c: mdColumns){
			if(c.getColumnName().equals(name))
				return c;
		}
		return null;
	}
	@Override
	public boolean isParmaryKey(String colname) {
		if(mdColumns==null)
			return false;
		for(MetaColumn c: mdColumns){
			if(c.getColumnName().equals(colname)){
				return c.isPrimaryKey();
			}
		}
		return false;
	}
	@Override
	public List<MetaColumn> getColumns() {
		return new ArrayList<MetaColumn>(mdColumns);
	}
	@Override
	public List<String> getPkColumns() {
		if(mdColumns==null)
			return null;
		
		List<String> pks =  new ArrayList<>();
		for(MetaColumn c: mdColumns){
			if(c.isPrimaryKey()){
				pks.add(c.getColumnName());
			}
		}
		return pks;
	}
	@Override
	public List<? extends TableReference> getReferences() {
		// TODO Auto-generated method stub
		return null;
	}
}
