package com.centit.metaform.formaccess;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.core.dao.PageDesc;

public interface ModelFormService {
	
	public ModelRuntimeContext createRuntimeContext(String modelCode);
	
	public JSONArray listObjectsByFilter(ModelRuntimeContext rc,Map<String, Object> filters);
	
	public JSONArray listObjectsByFilter(ModelRuntimeContext rc,Map<String, Object> filters, PageDesc pageDesc );	
	
	public JSONObject getObjectByProperties(ModelRuntimeContext rc,Map<String, Object> properties);
	
	public Map<String,Object> createNewPk(ModelRuntimeContext rc ) throws SQLException;
	
	public Map<String,Object> getModelReferenceFields(ModelRuntimeContext rc, JSONObject object) throws SQLException;
	
	public JSONObject createInitialObject(ModelRuntimeContext rc ) throws SQLException;
	
	public MetaFormDefine createFormDefine(ModelRuntimeContext rc,String operation);
	
	public ListViewDefine createListViewModel(ModelRuntimeContext rc);
	
	public int saveNewObject(ModelRuntimeContext rc, 
			Map<String, Object> object, HttpServletResponse response) throws Exception;
	
	public int mergeObject(ModelRuntimeContext rc, 
			Map<String, Object> object, HttpServletResponse response) throws Exception;
		
	public int updateObject(ModelRuntimeContext rc,
			Map<String, Object> object, HttpServletResponse response) throws Exception;
	
	public int deleteObjectById(ModelRuntimeContext rc,
			Map<String,Object> keyValue, HttpServletResponse response) throws Exception;

}
