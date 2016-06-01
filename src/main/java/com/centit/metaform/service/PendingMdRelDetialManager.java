package com.centit.metaform.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.core.service.BaseEntityManager;
import com.centit.metaform.po.PendingMdRelDetial;

/**
 * PendingMdRelDetial  Service.
 * create by scaffold 2016-06-01 
 * @author codefan@sina.com
 * 未落实表关联细节表null   
*/

public interface PendingMdRelDetialManager extends BaseEntityManager<PendingMdRelDetial,com.centit.metaform.po.PendingMdRelDetialId> 
{
	
	public JSONArray listPendingMdRelDetialsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc);
}
