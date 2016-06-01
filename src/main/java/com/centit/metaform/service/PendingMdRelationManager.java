package com.centit.metaform.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.core.service.BaseEntityManager;
import com.centit.metaform.po.PendingMdRelation;

/**
 * PendingMdRelation  Service.
 * create by scaffold 2016-06-01 
 * @author codefan@sina.com
 * 未落实表关联关系表null   
*/

public interface PendingMdRelationManager extends BaseEntityManager<PendingMdRelation,java.lang.Long> 
{
	
	public JSONArray listPendingMdRelationsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc);
}
