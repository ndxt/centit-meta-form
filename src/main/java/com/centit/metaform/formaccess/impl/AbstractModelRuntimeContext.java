package com.centit.metaform.formaccess.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.centit.metaform.formaccess.ModelRuntimeContext;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.po.MetaTable;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.metadata.TableField;

public abstract class AbstractModelRuntimeContext implements ModelRuntimeContext{
	private String  modelCode;
	private MetaTable tableInfo;
	private MetaFormModel metaFormModel;
	
	public AbstractModelRuntimeContext(){
		
	}
	
	public AbstractModelRuntimeContext(String  modelCode){
		this.modelCode = modelCode;
	}
	
	@Override
	public MetaTable getTableInfo() {
		return tableInfo;
	}

	public void setTableInfo(MetaTable tableinfo) {
		this.tableInfo = tableinfo;
	}

	@Override
	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	@Override
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
	@Override
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

	@Override
	public MetaFormModel getMetaFormModel() {
		return metaFormModel;
	}

	public void setMetaFormModel(MetaFormModel metaFormModel) {
		this.metaFormModel = metaFormModel;
	}


}
