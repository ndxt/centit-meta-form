package com.centit.metaform.fromaccess;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.centit.support.database.jsonmaptable.JsonObjectDao;
import com.centit.support.database.metadata.TableInfo;

public interface ModelRuntimeContext {
	
	public JsonObjectDao getJsonObjectDao();
	
	public String getModelCode();
	
	public MateFormModel getFormModle(String operation);
	
	public ListViewModel getListViewModel();
	
	public TableInfo getTableInfo();
	
	public Map<String,Object> fetchPkFromRequest( HttpServletRequest request);
	
	public Map<String,Object> fetchObjectFromRequest( HttpServletRequest request);
}
