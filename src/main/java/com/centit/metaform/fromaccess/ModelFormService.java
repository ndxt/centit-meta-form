package com.centit.metaform.fromaccess;

import java.sql.SQLException;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.core.dao.PageDesc;

public interface ModelFormService {
	
	public JdbcModelRuntimeContext createRuntimeContext(String modelCode);
	
	public JSONArray listObjectsByFilter(JdbcModelRuntimeContext rc,Map<String, Object> filters);
	
	public JSONArray listObjectsByFilter(JdbcModelRuntimeContext rc,Map<String, Object> filters, PageDesc pageDesc );	
	
	public JSONObject getObjectByProperties(JdbcModelRuntimeContext rc,Map<String, Object> properties);
	
	
	public void saveNewObject(JdbcModelRuntimeContext rc, Map<String, Object> object) throws SQLException;
	
	
	public void updateObject(JdbcModelRuntimeContext rc, Map<String, Object> object) throws SQLException;
	
	public void deleteObjectById(JdbcModelRuntimeContext rc,Map<String,Object> keyValue) throws SQLException;
}
