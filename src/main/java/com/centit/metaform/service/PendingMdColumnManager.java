package com.centit.metaform.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.core.service.BaseEntityManager;
import com.centit.metaform.po.PendingMdColumn;

/**
 * PendingMdColumn  Service.
 * create by scaffold 2016-06-01 
 * @author codefan@sina.com
 * 未落实字段元数据表null   
*/

public interface PendingMdColumnManager extends BaseEntityManager<PendingMdColumn,java.lang.Long> 
{
	
	public JSONArray listPendingMdColumnsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc);
}
