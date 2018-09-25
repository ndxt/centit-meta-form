package com.centit.metaform.service;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.metaform.po.ModelDataField;
import com.centit.support.database.utils.PageDesc;

import java.io.Serializable;
import java.util.Map;

/**
 * ModelDataField  Service.
 * create by scaffold 2016-06-02 
 
 * 数据模板字段null   
*/

public interface ModelDataFieldManager extends BaseEntityManager<ModelDataField, Serializable>
{

    public JSONArray listModelDataFieldsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc);
}
