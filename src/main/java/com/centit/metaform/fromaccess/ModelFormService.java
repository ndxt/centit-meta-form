package com.centit.metaform.fromaccess;

import java.sql.SQLException;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.core.dao.PageDesc;

public interface ModelFormService {
	
	public ModelRuntimeContext createRuntimeContext(String modelCode);
	
	public JSONArray listObjectsByFilter(ModelRuntimeContext rc,Map<String, Object> filters);
	
	public JSONArray listObjectsByFilter(ModelRuntimeContext rc,Map<String, Object> filters, PageDesc pageDesc );	
	
	public JSONObject getObjectByProperties(ModelRuntimeContext rc,Map<String, Object> properties);
	
	
	public void saveNewObject(ModelRuntimeContext rc, Map<String, Object> object) throws SQLException;
	
	
	public void updateObject(ModelRuntimeContext rc, Map<String, Object> object) throws SQLException;
	
	public void deleteObjectById(ModelRuntimeContext rc,Map<String,Object> keyValue) throws SQLException;
}
