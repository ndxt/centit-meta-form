package com.centit.metaform.formaccess.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.metaform.formaccess.ModelRuntimeContext;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.po.MetaTable;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.QueryAndNamedParams;
import com.centit.support.database.QueryUtils;
import com.centit.support.database.QueryUtils.SimpleFilterTranslater;
import com.centit.support.database.metadata.TableField;

public abstract class AbstractModelRuntimeContext implements ModelRuntimeContext{
	private String  modelCode;
	private MetaTable tableInfo;
	private MetaFormModel metaFormModel;
	private Map<String,Object> userEvniData;
	
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
	
	@Override
	public void setCurrentUserDetails(
			CentitUserDetails userDetails) {
		if(userDetails==null)
			return;
		if(userEvniData==null)
			userEvniData = new HashMap<>();
		
		userEvniData.put("currentUser", userDetails);
		//当前用户主机构信息
		userEvniData.put("primaryUnit", CodeRepositoryUtil
				.getUnitInfoByCode(userDetails.getPrimaryUnit()));
		//当前用户所有机构关联关系信息
		userEvniData.put("userUnits",
				CodeRepositoryUtil.getUserUnits(userDetails.getUserCode()));
		//当前用户的角色信息
		userEvniData.put("userRoles", userDetails.getUserRoleCodes());
	}
	
	@Override
	public void setUserEvniData(String key,Object value) {
		if(userEvniData==null)
			userEvniData = new HashMap<>();
		
		userEvniData.put(key, value);
	}
	
	@Override
	public QueryAndNamedParams getMetaFormFilter(){
		if(metaFormModel==null)
			return null;
		if(StringUtils.isBlank(metaFormModel.getDataFilterSql()))
			return null;
		
		return QueryUtils.translateQueryFilter(
				metaFormModel.getDataFilterSql(), 
				new SimpleFilterTranslater(userEvniData));
	}
}
