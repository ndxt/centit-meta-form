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
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * create by scaffold 2016-06-02 
 
 
  通用模块管理null   
*/
@Entity
@Table(name = "M_META_FORM_MODEL")
public class MetaFormModel implements java.io.Serializable {
	private static final long serialVersionUID =  1L;



	/**
	 * 模块代码 null 
	 */
	@Id
	@Column(name = "MODEL_CODE")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="table_generator")
	@TableGenerator(name = "table_generator",table="hibernate_sequences",initialValue=200000001,
	pkColumnName="SEQ_NAME",pkColumnValue="pendingtableId",allocationSize=1,valueColumnName="SEQ_VALUE")
	private String modelCode;

	/**
	 * 表ID 表单主表 
	 */
	@JoinColumn(name="TABLE_ID")
	@ManyToOne
	private MdTable mdTable;
	/**
	 * 模快描述 null 
	 */
	@Column(name = "MODEL_COMMENT")
	@Length(min = 0, max = 256, message = "字段长度不能小于{min}大于{max}")
	private String  modelComment;
	/**
	 * 模块名称 null 
	 */
	@Column(name = "MODEL_NAME")
	@NotBlank(message = "字段不能为空")
	@Length(min = 0, max = 64, message = "字段长度不能小于{min}大于{max}")
	private String  modelName;
	/**
	 * 存储类别 只读（视图、查询），新增（只能新增一条），修改，编辑列表（增删改） 
	 */
	@Column(name = "ACCESS_TYPE")
	@Length(min = 0,  message = "字段长度不能小于{min}大于{max}")
	private String  accessType;
	/**
	 * 与父模块关系 O 没有父模块  1  一对一，2 多对一 
	 */
	@Column(name = "RELATION_TYPE")
	@Length(min = 0,  message = "字段长度不能小于{min}大于{max}")
	private String  relationType;
	/**
	 * 父模块代码 子模块必需对应父模块对应的子表 
	 */
	@Column(name = "PARENT_MODEL_CODE")
	@Length(min = 0, max = 16, message = "字段长度不能小于{min}大于{max}")
	private String  parentModelCode;
	/**
	 * 显示顺序 null 
	 */
	@Column(name = "DISPLAY_ORDER")
	private Long  displayOrder;
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
	
	@Transient
	@OneToMany(mappedBy="",orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<ModelDataField> modelDataFields;
	
	@Transient
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<MetaFormModel> metaFormModels;

	// Constructors
	/** default constructor */
	public MetaFormModel() {
	}
	/** minimal constructor */
	public MetaFormModel(
		String modelCode		
		,String  modelName) {
	
	
		this.modelCode = modelCode;		
	
		this.modelName= modelName; 		
	}

/** full constructor */
	public MetaFormModel(
	 String modelCode		
	,Long  tableId,String  modelComment,String  modelName,String  accessType,String  relationType,String  parentModelCode,Long  displayOrder,Date  lastModifyDate,String  recorder) {
	
	
		this.modelCode = modelCode;		
	
		this.setTableId(tableId);
		this.modelComment= modelComment;
		this.modelName= modelName;
		this.accessType= accessType;
		this.relationType= relationType;
		this.parentModelCode= parentModelCode;
		this.displayOrder= displayOrder;
		this.lastModifyDate= lastModifyDate;
		this.recorder= recorder;		
	}
	

  
	public String getModelCode() {
		return this.modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	// Property accessors
  
	public Long getTableId() {
		return this.getMdTable().getTableId();
	}
	
	public MdTable getMdTable() {
		if(null==this.mdTable)
			this.mdTable=new MdTable();
		return this.mdTable;
	}
	public void setMdTable(MdTable mdTable) {
		this.mdTable = mdTable;
	}
	public void setTableId(Long tableId) {
		MdTable tb=new MdTable();
		tb.setTableId(tableId);
		this.setMdTable(tb);
	}
  
	public String getModelComment() {
		return this.modelComment;
	}
	
	public void setModelComment(String modelComment) {
		this.modelComment = modelComment;
	}
  
	public String getModelName() {
		return this.modelName;
	}
	
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
  
	public String getAccessType() {
		return this.accessType;
	}
	
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
  
	public String getRelationType() {
		return this.relationType;
	}
	
	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}
  
	public String getParentModelCode() {
		return this.parentModelCode;
	}
	
	public void setParentModelCode(String parentModelCode) {
		this.parentModelCode = parentModelCode;
	}
  
	public Long getDisplayOrder() {
		return this.displayOrder;
	}
	
	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
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


	public Set<ModelDataField> getModelDataFields(){
		if(this.modelDataFields==null)
			this.modelDataFields = new HashSet<ModelDataField>();
		return this.modelDataFields;
	}

	public void setModelDataFields(Set<ModelDataField> modelDataFields) {
		this.modelDataFields = modelDataFields;
	}	

	public void addModelDataField(ModelDataField modelDataField ){
		if (this.modelDataFields==null)
			this.modelDataFields = new HashSet<ModelDataField>();
		this.modelDataFields.add(modelDataField);
	}
	
	public void removeModelDataField(ModelDataField modelDataField ){
		if (this.modelDataFields==null)
			return;
		this.modelDataFields.remove(modelDataField);
	}
	
	public ModelDataField newModelDataField(){
		ModelDataField res = new ModelDataField();
  
		res.setModelCode(this.getModelCode());

		return res;
	}
	/**
	 * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
	 * 
	 */
	public void replaceModelDataFields(Set<ModelDataField> set) {
		List<ModelDataField> newObjs = new ArrayList<ModelDataField>();
		for(ModelDataField p :set){
			if(p==null)
				continue;
			ModelDataField newdt = newModelDataField();
			newdt.copyNotNullProperty(p);
			newObjs.add(newdt);
		}
		//delete
		boolean found = false;
		Set<ModelDataField> oldObjs = new HashSet<ModelDataField>();
		oldObjs.addAll(getModelDataFields());
		
		for(Iterator<ModelDataField> it=oldObjs.iterator(); it.hasNext();){
			ModelDataField odt = it.next();
			found = false;
			for(ModelDataField newdt :newObjs){
				if(odt.getCid().equals( newdt.getCid())){
					found = true;
					break;
				}
			}
			if(! found)
				removeModelDataField(odt);
		}
		oldObjs.clear();
		//insert or update
		for(ModelDataField newdt :newObjs){
			found = false;
			for(Iterator<ModelDataField> it=getModelDataFields().iterator();
			 it.hasNext();){
				ModelDataField odt = it.next();
				if(odt.getCid().equals( newdt.getCid())){
					odt.copy(newdt);
					found = true;
					break;
				}
			}
			if(! found)
				addModelDataField(newdt);
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
  
		res.setParentModelCode(this.getModelCode());

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


	public MetaFormModel copy(MetaFormModel other){
  
		this.setModelCode(other.getModelCode());
  
		this.setTableId(other.getTableId());
		this.modelComment= other.getModelComment();  
		this.modelName= other.getModelName();  
		this.accessType= other.getAccessType();  
		this.relationType= other.getRelationType();  
		this.parentModelCode= other.getParentModelCode();  
		this.displayOrder= other.getDisplayOrder();  
		this.lastModifyDate= other.getLastModifyDate();  
		this.recorder= other.getRecorder();
	
		this.modelDataFields = other.getModelDataFields();	
		this.metaFormModels = other.getMetaFormModels();
		return this;
	}
	
	public MetaFormModel copyNotNullProperty(MetaFormModel other){
  
	if( other.getModelCode() != null)
		this.setModelCode(other.getModelCode());
  
		if( other.getTableId() != null)
			this.setTableId(other.getTableId());
		if( other.getModelComment() != null)
			this.modelComment= other.getModelComment();  
		if( other.getModelName() != null)
			this.modelName= other.getModelName();  
		if( other.getAccessType() != null)
			this.accessType= other.getAccessType();  
		if( other.getRelationType() != null)
			this.relationType= other.getRelationType();  
		if( other.getParentModelCode() != null)
			this.parentModelCode= other.getParentModelCode();  
		if( other.getDisplayOrder() != null)
			this.displayOrder= other.getDisplayOrder();  
		if( other.getLastModifyDate() != null)
			this.lastModifyDate= other.getLastModifyDate();  
		if( other.getRecorder() != null)
			this.recorder= other.getRecorder();		
	
		//this.modelDataFields = other.getModelDataFields();
        replaceModelDataFields(other.getModelDataFields());
			
		//this.metaFormModels = other.getMetaFormModels();
        replaceMetaFormModels(other.getMetaFormModels());
		
		return this;
	}

	public MetaFormModel clearProperties(){
  
		this.setMdTable(null);;
		this.modelComment= null;  
		this.modelName= null;  
		this.accessType= null;  
		this.relationType= null;  
		this.parentModelCode= null;  
		this.displayOrder= null;  
		this.lastModifyDate= null;  
		this.recorder= null;
	
		this.modelDataFields = new HashSet<ModelDataField>();	
		this.metaFormModels = new HashSet<MetaFormModel>();
		return this;
	}
}
