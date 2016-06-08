package com.centit.metaform.fromaccess.impl;

import java.util.ArrayList;
import java.util.List;

import com.centit.framework.core.controller.BaseController;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.JsonObjectWork;
import com.centit.metaform.fromaccess.FieldTemplateOptions;
import com.centit.metaform.fromaccess.FormField;
import com.centit.metaform.fromaccess.ListColumn;
import com.centit.metaform.fromaccess.ListViewDescription;
import com.centit.metaform.fromaccess.ModelOperation;
import com.centit.metaform.fromaccess.ModelRuntimeContext;
import com.centit.support.database.jsonmaptable.JsonObjectDao;
import com.centit.support.database.metadata.TableInfo;

public class HibernateModelRuntimeContext implements ModelRuntimeContext {
	private String  modelCode;
	public void setBaseObjectDao(BaseDaoImpl<?, ?> baseObjectDao) {
		this.baseObjectDao = baseObjectDao;
	}

	private BaseDaoImpl<?, ?> baseObjectDao;
	private TableInfo tableinfo;
	private List<FormField> formFields; 
	
	public HibernateModelRuntimeContext(){
		
	}
	
	public HibernateModelRuntimeContext(String  modelCode){
		this.modelCode = modelCode;
	}
	

	public TableInfo getTableinfo() {
		return tableinfo;
	}

	public void setTableinfo(TableInfo tableinfo) {
		this.tableinfo = tableinfo;
	}
	
	private JsonObjectDao dao = null;
	
	public JsonObjectDao getJsonObjectDao(){
		if(dao==null){
			dao = new JsonObjectWork(baseObjectDao,tableinfo);
		}
		return dao;
	}
	
	public void close(){
		
	}
	
	public void commitAndClose(){
		
	}
	
	public void rollbackAndClose(){
		
	}
	
	public ListViewDescription getListViewDesc(){
		ListViewDescription lv = new ListViewDescription();
		FormField ff = new FormField();
		ff.setKey(BaseController.SEARCH_STRING_PREFIX + "userName");
		ff.setType("input");
		FieldTemplateOptions templateOptions = new FieldTemplateOptions();
		templateOptions.setLabel("姓名：");
		templateOptions.setPlaceholder("请输入完整的姓名。");
		ff.setTemplateOptions(templateOptions);
		lv.addFilter(ff);
		ListColumn id = new ListColumn("id","编号");
		id.setPrimaryKey(true);
		lv.addColumn(id);
		lv.addColumn(new ListColumn("userName","用户姓名"));
		lv.addColumn(new ListColumn("userPhone","电话"));
		
		lv.addOperation(new ModelOperation(modelCode,"view","get","查看"));
		lv.addOperation(new ModelOperation(modelCode,"edit","get","编辑"));
		return lv;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	
	public List<FormField> getFormFields(){
		
		return this.formFields;
	}

	public void addFormField(FormField ff){
		if(formFields==null)
			formFields = new ArrayList<>();
		formFields.add(ff);
	}

	public void setFormFields(List<FormField> formFields) {
		this.formFields = formFields;
	}
}
