package com.centit.metaform.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.core.service.BaseEntityManager;
import com.centit.metaform.po.MdColumn;

/**
 * MdColumn  Service.
 * create by scaffold 2016-06-02 
 * @author codefan@sina.com
 * 字段元数据表null   
*/

public interface MdColumnManager extends BaseEntityManager<MdColumn,com.centit.metaform.po.MdColumnId> 
{
	
	public JSONArray listMdColumnsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc);
}
