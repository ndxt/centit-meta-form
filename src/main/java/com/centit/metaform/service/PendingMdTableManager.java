package com.centit.metaform.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.core.service.BaseEntityManager;
import com.centit.metaform.po.PendingMdTable;

/**
 * PendingMdTable  Service.
 * create by scaffold 2016-06-01 
 * @author codefan@sina.com
 * 未落实表元数据表null   
*/

public interface PendingMdTableManager extends BaseEntityManager<PendingMdTable,java.lang.Long> 
{
	
	public JSONArray listPendingMdTablesAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc);
}
