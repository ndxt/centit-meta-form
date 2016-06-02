package com.centit.metaform.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.core.service.BaseEntityManager;
import com.centit.metaform.po.MdRelation;

/**
 * MdRelation  Service.
 * create by scaffold 2016-06-02 
 
 * 表关联关系表null   
*/

public interface MdRelationManager extends BaseEntityManager<MdRelation,java.lang.Long> 
{
	
	public JSONArray listMdRelationsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc);
}
