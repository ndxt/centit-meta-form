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

import oracle.jdbc.proxy.annotation.GetCreator;

import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.alibaba.fastjson.annotation.JSONField;

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
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "assigned") 
	private String modelCode;

	/**
	 * 表ID 表单主表 
	 */
	@JoinColumn(name="TABLE_ID")
	@ManyToOne
	@JSONField(serialize=false)
	private MetaTable mdTable;
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
	 * 表单模板
	 */
	@Column(name = "form_template")
	@Length(min = 0,  message = "字段长度不能小于{min}大于{max}")
	private String  formTemplate;
	
	/**
	 * 是否是树形结构
	 */
	@Column(name = "list_as_tree")
	@Length(min = 0,  message = "字段长度不能小于{min}大于{max}")
	private String  listAsTree;
	
	/**
	 * 与父模块关系 O 没有父模块  1  一对一，2 多对一 
	 */
	@Column(name = "RELATION_TYPE")
	@Length(min = 0,  message = "字段长度不能小于{min}大于{max}")
	private String  relationType;
	/**
	 * 父模块代码 子模块必需对应父模块对应的子表 
	 */
	@JoinColumn(name = "PARENT_MODEL_CODE")
	@ManyToOne
	@JSONField(serialize=false)
	private MetaFormModel  parentModel;
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
	@Length(max = 8, message = "字段长度不能大于{max}")
	private String  recorder;
	
	@Column(name = "EXTEND_OPTIONS")
	@Length(max = 800, message = "字段长度不能大于{max}")
	private String  extendOptions;
	
	@Column(name = "EXTEND_OPT_BEAN")
	@Length(max = 64, message = "字段长度不能大于{max}")
	private String  extendOptBean;
	
	@Column(name = "EXTEND_OPT_BEAN_PARAM")
	@Length(max = 800, message = "字段长度不能大于{max}")
	private String  extendOptBeanParam;

	
	
	@OneToMany(mappedBy="cid.metaFormModel",orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<ModelDataField> modelDataFields;
	
	
	@OneToMany(mappedBy="parentModel",fetch = FetchType.LAZY)
	private Set<MetaFormModel> childFormModels;

	
	
	@OneToMany(mappedBy="cid.metaFormModel",orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<ModelOperation> modelOperations;
	
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
	public MetaFormModel(String modelCode, MetaTable mdTable,
			String modelComment, String modelName, String accessType,
			String formTemplate, String listAsTree, String relationType,
			String parentModelCode, Long displayOrder, Date lastModifyDate,
			String recorder, String extendOptions, String extendOptBean,
			String extendOptBeanParam) {
		super();
		this.modelCode = modelCode;
		this.mdTable = mdTable;
		this.modelComment = modelComment;
		this.modelName = modelName;
		this.accessType = accessType;
		this.formTemplate = formTemplate;
		this.listAsTree = listAsTree;
		this.relationType = relationType;
		this.setParentModelCode(parentModelCode);
		this.displayOrder = displayOrder;
		this.lastModifyDate = lastModifyDate;
		this.recorder = recorder;
		this.extendOptions = extendOptions;
		this.extendOptBean = extendOptBean;
		this.extendOptBeanParam = extendOptBeanParam;
		this.modelDataFields = new HashSet<ModelDataField>();
		this.childFormModels = new HashSet<MetaFormModel>();
	}
	

	
	public String getTableLabelName(){
			if(null==this.mdTable)
				return null;
			return this.mdTable.getTableLabelName();
	}
	
	public Long getTableId(){
		if(null==this.mdTable)
			return null;
		return this.mdTable.getTableId();
	}
  
	public MetaFormModel getParentModel() {
		return parentModel;
	}
	public void setParentModel(MetaFormModel parentModel) {
		this.parentModel = parentModel;
	}
	public Set<MetaFormModel> getChildFormModels() {
		return childFormModels;
	}
	public void setChildFormModels(Set<MetaFormModel> childFormModels) {
		this.childFormModels = childFormModels;
	}
	public String getModelCode() {
		return this.modelCode;
	}

	
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
  
	public MetaTable getMdTable() {
		if(null==this.mdTable)
			this.mdTable=new MetaTable();
		return this.mdTable;
	}
	public void setMdTable(MetaTable mdTable) {
		this.mdTable = mdTable;
	}
	public void setTableId(Long tableId) {
		MetaTable tb=new MetaTable();
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
  
	public String getFormTemplate() {
		return formTemplate;
	}
	public void setFormTemplate(String formTemplate) {
		this.formTemplate = formTemplate;
	}
	public String getListAsTree() {
		return listAsTree;
	}
	public void setListAsTree(String listAsTree) {
		this.listAsTree = listAsTree;
	}
	public String getParentModelCode() {
		if(null==this.parentModel)
			return null;
		return this.parentModel.getModelCode();
	}
	
	public void setParentModelCode(String parentModelCode) {
		this.parentModel=new MetaFormModel();
		this.parentModel.setModelCode(parentModelCode);
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
		this.getModelDataFields().clear();
		Iterator<ModelDataField> it=modelDataFields.iterator();
		while(it.hasNext())
		{
			it.next().getCid().setMetaFormModel(this);
		}
		this.getModelDataFields().addAll(modelDataFields);
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
		if(this.childFormModels==null)
			this.childFormModels = new HashSet<MetaFormModel>();
		return this.childFormModels;
	}

	public void setMetaFormModels(Set<MetaFormModel> metaFormModels) {
		this.childFormModels = metaFormModels;
	}	

	public void addMetaFormModel(MetaFormModel metaFormModel ){
		if (this.childFormModels==null)
			this.childFormModels = new HashSet<MetaFormModel>();
		this.childFormModels.add(metaFormModel);
	}
	
	public void removeMetaFormModel(MetaFormModel metaFormModel ){
		if (this.childFormModels==null)
			return;
		this.childFormModels.remove(metaFormModel);
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
	
	public Set<ModelOperation> getModelOperations() {
		if(this.modelOperations==null)
			this.modelOperations = new HashSet<ModelOperation>();
		return modelOperations;
	}
	
	public void setModelOperations(Set<ModelOperation> modelOperations) {
		this.getModelOperations().clear();
		Iterator<ModelOperation> it=modelOperations.iterator();
		while(it.hasNext())
		{
			it.next().getCid().setMetaFormModel(this);
		}
		this.getModelOperations().addAll(modelOperations);
	}
	
	public void addModelOperation(ModelOperation modelOperation ){
		if(this.modelOperations==null)
			this.modelOperations = new HashSet<ModelOperation>();
		this.modelOperations.add(modelOperation);
	}
	
	public void removeModelOperation(ModelOperation modelOperation ){
		if (this.modelOperations==null)
			return;
		this.modelOperations.remove(modelOperation);
	}
	
	public ModelOperation newModelOperation(){
		ModelOperation res = new ModelOperation();  
		res.setModelCode(this.getModelCode());
		return res;
	}
	/**
	 * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
	 * 
	 */
	public void replaceModelOperations(Set<ModelOperation> set) {
		List<ModelOperation> newObjs = new ArrayList<ModelOperation>();
		for(ModelOperation p :set){
			if(p==null)
				continue;
			ModelOperation newdt = newModelOperation();
			newdt.copyNotNullProperty(p);
			newObjs.add(newdt);
		}
		//delete
		boolean found = false;
		Set<ModelOperation> oldObjs = new HashSet<ModelOperation>();
		oldObjs.addAll(getModelOperations());
		
		for(Iterator<ModelOperation> it=oldObjs.iterator(); it.hasNext();){
			ModelOperation odt = it.next();
			found = false;
			for(ModelOperation newdt :newObjs){
				if(odt.getCid().equals( newdt.getCid())){
					found = true;
					break;
				}
			}
			if(! found)
				removeModelOperation(odt);
		}
		oldObjs.clear();
		//insert or update
		for(ModelOperation newdt :newObjs){
			found = false;
			for(Iterator<ModelOperation> it=getModelOperations().iterator();
			 it.hasNext();){
				ModelOperation odt = it.next();
				if(odt.getCid().equals( newdt.getCid())){
					odt.copy(newdt);
					found = true;
					break;
				}
			}
			if(! found)
				addModelOperation(newdt);
		} 	
	}	
	
	public String getExtendOptions() {
		return extendOptions;
	}
	public void setExtendOptions(String extendOptions) {
		this.extendOptions = extendOptions;
	}

	public String getExtendOptBean() {
		return extendOptBean;
	}
	public void setExtendOptBean(String extendOptBean) {
		this.extendOptBean = extendOptBean;
	}
	
	public String getExtendOptBeanParam() {
		return extendOptBeanParam;
	}
	
	public void setExtendOptBeanParam(String extendOptBeanParam) {
		this.extendOptBeanParam = extendOptBeanParam;
	}

	public MetaFormModel copy(MetaFormModel other){
  
		this.setModelCode(other.getModelCode());
  
		this.setMdTable(other.getMdTable());
		this.modelComment= other.getModelComment();  
		this.modelName= other.getModelName();  
		this.accessType= other.getAccessType();  
		this.relationType= other.getRelationType();  
		this.setParentModel(other.getParentModel());
		this.displayOrder= other.getDisplayOrder();  
		this.formTemplate=other.getFormTemplate();
		this.listAsTree=other.getListAsTree();
		this.lastModifyDate= other.getLastModifyDate();  
		this.recorder= other.getRecorder();	
		this.modelDataFields = other.getModelDataFields();
		this.childFormModels = other.getMetaFormModels();		
		this.extendOptBean = other.getExtendOptBean();
		this.extendOptBeanParam = other.getExtendOptBeanParam();
		
		this.setModelOperations(other.getModelOperations());
		this.setModelDataFields(other.getModelDataFields());
		return this;
	}
	
	
	public MetaFormModel copyNotNullProperty(MetaFormModel other){
  
	if( other.getModelCode() != null)
		this.setModelCode(other.getModelCode());
  
		if( other.getMdTable()!= null)
			this.setMdTable(other.getMdTable());
		if( other.getModelComment() != null)
			this.modelComment= other.getModelComment();  
		if( other.getModelName() != null)
			this.modelName= other.getModelName();  
		if( other.getAccessType() != null)
			this.accessType= other.getAccessType();  
		if( other.getRelationType() != null)
			this.relationType= other.getRelationType();  
		if( other.getFormTemplate() != null)
			this.formTemplate=other.getFormTemplate();
		if( other.getListAsTree() != null)
			this.listAsTree=other.getListAsTree();
		if( other.getDisplayOrder() != null)
			this.displayOrder= other.getDisplayOrder();  
		if( other.getLastModifyDate() != null)
			this.lastModifyDate= other.getLastModifyDate();  
		if( other.getRecorder() != null)
			this.recorder= other.getRecorder();
		if(null!=other.getParentModel())
			this.setParentModel(other.getParentModel());
		if( other.getMetaFormModels() != null)
		this.childFormModels = other.getMetaFormModels();
		if( other.getExtendOptBean() != null)
			this.extendOptBean = other.getExtendOptBean();
		if( other.getExtendOptBeanParam() != null)
			this.extendOptBeanParam = other.getExtendOptBeanParam();
		
		if(null!=other.getModelDataFields())
			this.setModelDataFields(other.getModelDataFields());
		if(null!=other.getModelOperations())
			this.setModelOperations(other.getModelOperations());
		return this;
	}

	public MetaFormModel clearProperties(){
  
		this.setMdTable(null);
		this.modelComment= null;  
		this.modelName= null;  
		this.accessType= null;  
		this.relationType= null;  
		this.parentModel=null;
		this.displayOrder= null;  
		this.lastModifyDate= null;  
		this.recorder= null;
	
		this.modelDataFields = new HashSet<ModelDataField>();	
		this.childFormModels = new HashSet<MetaFormModel>();
		return this;
	}
}
