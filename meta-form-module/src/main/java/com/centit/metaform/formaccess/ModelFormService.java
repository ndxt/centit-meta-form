package com.centit.metaform.formaccess;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.OptionItem;
import com.centit.framework.core.dao.PageDesc;

public interface ModelFormService {
	
	ModelRuntimeContext createRuntimeContext(String modelCode);
	
	JSONArray listObjectsByFilter(ModelRuntimeContext rc,Map<String, Object> filters) throws SQLException;
	
	JSONArray listObjectsByFilter(ModelRuntimeContext rc,Map<String, Object> filters, PageDesc pageDesc );	
	
	JSONObject getObjectByProperties(ModelRuntimeContext rc,Map<String, Object> properties);
	
	Map<String,Object> createNewPk(ModelRuntimeContext rc ) throws SQLException;
	
	Map<String,Object> getModelReferenceFields(ModelRuntimeContext rc, JSONObject object) throws SQLException;
	
	JSONObject createInitialObject(ModelRuntimeContext rc ) throws SQLException;
	
	MetaFormDefine createFormDefine(ModelRuntimeContext rc,String operation);
	
	ListViewDefine createListViewModel(ModelRuntimeContext rc);

	JSONObject getObjectById(ModelRuntimeContext rc,Map<String, Object> keyValue);

	int saveNewObject(ModelRuntimeContext rc, 
			Map<String, Object> object, HttpServletResponse response) throws Exception;
	
	int mergeObject(ModelRuntimeContext rc, 
			Map<String, Object> object, HttpServletResponse response) throws Exception;
		
	int updateObject(ModelRuntimeContext rc,
			Map<String, Object> object, HttpServletResponse response) throws Exception;
	
	int submitObject(ModelRuntimeContext rc,
			Map<String, Object> object, HttpServletResponse response) throws Exception;
	
	int deleteObjectById(ModelRuntimeContext rc,
			Map<String,Object> keyValue, HttpServletResponse response) throws Exception;
	
	
	/**
	 * 获取级联查询的数据选项
	 * @param rc
	 * @param propertyName
	 * @param startGroup
	 * @return
	 */
	List<OptionItem> getAsyncReferenceData(ModelRuntimeContext rc,
			String propertyName,String startGroup);
}
