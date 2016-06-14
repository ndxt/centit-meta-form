package com.centit.metaform.formaccess.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.centit.framework.core.controller.BaseController;
import com.centit.metaform.formaccess.FieldTemplateOptions;
import com.centit.metaform.formaccess.FormField;
import com.centit.metaform.formaccess.ListColumn;
import com.centit.metaform.formaccess.ListViewModel;
import com.centit.metaform.formaccess.MateFormModel;
import com.centit.metaform.formaccess.ModelOperation;
import com.centit.metaform.formaccess.ModelRuntimeContext;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.metadata.TableField;
import com.centit.support.database.metadata.TableInfo;

public abstract class AbstractModelRuntimeContext implements ModelRuntimeContext{
	private String  modelCode;
	private TableInfo tableInfo;
	private List<FormField> formFields; 
	
	public AbstractModelRuntimeContext(){
		
	}
	
	public AbstractModelRuntimeContext(String  modelCode){
		this.modelCode = modelCode;
	}
	

	public TableInfo getTableInfo() {
		return tableInfo;
	}

	public void setTableInfo(TableInfo tableinfo) {
		this.tableInfo = tableinfo;
	}
		
	public ListViewModel getListViewModel(){
		ListViewModel lv = new ListViewModel();
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
	
	@Override
	public MateFormModel getFormModle(String operation){
		return new MateFormModel(formFields);
	}

	public void addFormField(FormField ff){
		if(formFields==null)
			formFields = new ArrayList<>();
		formFields.add(ff);
	}

	public void setFormFields(List<FormField> formFields) {
		this.formFields = formFields;
	}
	
	public Map<String,Object> fetchPkFromRequest(HttpServletRequest request){
		Map<String,Object> jo = new HashMap<>();
		for(String pk: getTableInfo().getPkColumns()){
    		TableField pkp = getTableInfo().findFieldByColumn(pk);
    		Object pv = request.getParameter(pkp.getPropertyName());
    		switch(pkp.getJavaType()){
			case "Date":
			case "Timestamp":
    			jo.put(pkp.getPropertyName(), DatetimeOpt.castObjectToDate(pv));
    			break;
			case "Long":
    			jo.put(pkp.getPropertyName(), NumberBaseOpt.castObjectToLong(pv));
    			break;
			case "Double":
    			jo.put(pkp.getPropertyName(), NumberBaseOpt.castObjectToDouble(pv));
    			break;
			default:
    			jo.put(pkp.getPropertyName(), StringBaseOpt.objectToString(pv));
    			break;
    		}
    	}
		return jo;
	}
	
	public Map<String,Object> fetchObjectFromRequest( HttpServletRequest request){
		Map<String,Object> jo = new HashMap<>();
		for(TableField field: getTableInfo().getColumns()){
    		Object pv = request.getParameter(field.getPropertyName());
    		switch(field.getJavaType()){
			case "Date":
			case "Timestamp":
    			jo.put(field.getPropertyName(), DatetimeOpt.castObjectToDate(pv));
    			break;
			case "Long":
    			jo.put(field.getPropertyName(), NumberBaseOpt.castObjectToLong(pv));
    			break;
			case "Double":
    			jo.put(field.getPropertyName(), NumberBaseOpt.castObjectToDouble(pv));
    			break;
			default:
    			jo.put(field.getPropertyName(), StringBaseOpt.objectToString(pv));
    			break;
    		}
    	}
		return jo;
	}
}
