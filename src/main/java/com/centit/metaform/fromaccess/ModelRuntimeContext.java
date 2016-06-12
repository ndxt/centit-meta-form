package com.centit.metaform.fromaccess;

import java.util.List;

import com.centit.support.database.jsonmaptable.JsonObjectDao;
import com.centit.support.database.metadata.TableInfo;

public interface ModelRuntimeContext {
	
	public JsonObjectDao getJsonObjectDao();
	
	public String getModelCode();
	
	public List<FormField> getFormFields();
	
	public ListViewDescription getListViewDesc();
	
	public TableInfo getTableinfo();
	
}
