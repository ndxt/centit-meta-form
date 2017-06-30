package com.centit.metaform.formaccess.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.OptionItem;
import com.centit.framework.core.dao.PageDesc;
import com.centit.metaform.formaccess.ListViewDefine;
import com.centit.metaform.formaccess.MetaFormDefine;
import com.centit.metaform.formaccess.ModelFormService;
import com.centit.metaform.formaccess.ModelRuntimeContext;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class JSONModelFormServiceImpl implements ModelFormService {

	@Override
	public ModelRuntimeContext createRuntimeContext(String modelCode) {
		return null;
	}

	@Override
	public JSONArray listObjectsByFilter(ModelRuntimeContext rc, Map<String, Object> filters) throws SQLException {
		return null;
	}

	@Override
	public JSONArray listObjectsByFilter(ModelRuntimeContext rc, Map<String, Object> filters, PageDesc pageDesc) {
		return null;
	}

	@Override
	public JSONObject getObjectByProperties(ModelRuntimeContext rc, Map<String, Object> properties) {
		return null;
	}

	@Override
	public Map<String, Object> createNewPk(ModelRuntimeContext rc) throws SQLException {
		return null;
	}

	@Override
	public Map<String, Object> getModelReferenceFields(ModelRuntimeContext rc, JSONObject object) throws SQLException {
		return null;
	}

	@Override
	public JSONObject createInitialObject(ModelRuntimeContext rc) throws SQLException {
		return null;
	}

	@Override
	public MetaFormDefine createFormDefine(ModelRuntimeContext rc, String operation) {
		return null;
	}

	@Override
	public ListViewDefine createListViewModel(ModelRuntimeContext rc) {
		return null;
	}

	@Override
	public int saveNewObject(ModelRuntimeContext rc, Map<String, Object> object, HttpServletResponse response) throws Exception {
		return 0;
	}

	@Override
	public int mergeObject(ModelRuntimeContext rc, Map<String, Object> object, HttpServletResponse response) throws Exception {
		return 0;
	}

	@Override
	public int updateObject(ModelRuntimeContext rc, Map<String, Object> object, HttpServletResponse response) throws Exception {
		return 0;
	}

	@Override
	public int submitObject(ModelRuntimeContext rc, Map<String, Object> object, HttpServletResponse response) throws Exception {
		return 0;
	}

	@Override
	public int deleteObjectById(ModelRuntimeContext rc, Map<String, Object> keyValue, HttpServletResponse response) throws Exception {
		return 0;
	}

	/**
	 * 获取级联查询的数据选项
	 *
	 * @param rc
	 * @param propertyName
	 * @param startGroup
	 * @return
	 */
	@Override
	public List<OptionItem> getAsyncReferenceData(ModelRuntimeContext rc, String propertyName, String startGroup) {
		return null;
	}
}
