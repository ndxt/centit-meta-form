package com.centit.metaform.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.hibernate.service.BaseEntityManager;
import com.centit.metaform.po.ModelDataField;

/**
 * ModelDataField  Service.
 * create by scaffold 2016-06-02 
 
 * 数据模板字段null   
*/

public interface ModelDataFieldManager extends BaseEntityManager<ModelDataField,com.centit.metaform.po.ModelDataFieldId> 
{
	
	public JSONArray listModelDataFieldsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc);
}
