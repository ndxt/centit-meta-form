package com.centit.metaform.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.centit.support.database.utils.PageDesc;
import com.centit.framework.hibernate.service.BaseEntityManager;
import com.centit.metaform.po.MetaFormModel;

/**
 * MetaFormModel  Service.
 * create by scaffold 2016-06-02 
 
 * 通用模块管理null   
*/

public interface MetaFormModelManager extends BaseEntityManager<MetaFormModel,java.lang.String> 
{
	
	JSONArray listMetaFormModelsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc);

	void updateMetaFormModel(MetaFormModel mtaFormModel);
}
