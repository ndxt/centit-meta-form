package com.centit.metaform.fromaccess;

import com.centit.support.database.jsonmaptable.JsonObjectDao;
import com.centit.support.database.metadata.TableInfo;

public interface ModelRuntimeContext {
	
	public JsonObjectDao getJsonObjectDao();
	
	public String getModelCode();
	
	public MateFormModel getFormModle(String operation);
	
	public ListViewModel getListViewModel();
	
	public TableInfo getTableinfo();
}
