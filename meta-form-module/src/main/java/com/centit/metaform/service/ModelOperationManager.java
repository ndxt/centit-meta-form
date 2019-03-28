package com.centit.metaform.service;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.metaform.po.ModelOperation;
import com.centit.support.database.utils.PageDesc;

import java.util.HashMap;
import java.util.Map;

/**
 * ModelOperation  Service.
 * create by scaffold 2016-06-21

 * 模块操作定义null
*/

public interface ModelOperationManager extends BaseEntityManager<ModelOperation, HashMap<String,Object>>
{

    public JSONArray listModelOperationsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc);
}
