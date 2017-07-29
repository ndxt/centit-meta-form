package com.centit.metaform.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.core.service.BaseEntityManager;
import com.centit.metaform.po.MetaChangLog;

/**
 * MdChangLog  Service.
 * create by scaffold 2016-06-01 
 
 * 元数据更改记录null   
*/

public interface MetaChangLogManager extends BaseEntityManager<MetaChangLog,java.lang.Long> 
{
	
	JSONArray listMdChangLogsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc);
}
