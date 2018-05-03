package com.centit.metaform.service;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.metaform.po.MetaChangLog;
import com.centit.support.database.utils.PageDesc;

import java.util.Map;

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
