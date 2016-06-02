package com.centit.metaform.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.core.service.BaseEntityManager;
import com.centit.metaform.po.MdRelDetial;

/**
 * MdRelDetial  Service.
 * create by scaffold 2016-06-02 
 * @author codefan@sina.com
 * 表关联细节表null   
*/

public interface MdRelDetialManager extends BaseEntityManager<MdRelDetial,com.centit.metaform.po.MdRelDetialId> 
{
	
	public JSONArray listMdRelDetialsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc);
}
