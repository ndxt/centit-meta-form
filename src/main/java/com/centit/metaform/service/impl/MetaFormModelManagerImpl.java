package com.centit.metaform.service.impl;

import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.hibernate.dao.SysDaoOptUtils;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
import com.centit.metaform.dao.MetaFormModelDao;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.service.MetaFormModelManager;

/**
 * MetaFormModel  Service.
 * create by scaffold 2016-06-02 
 * @author codefan@sina.com
 * 通用模块管理null   
*/
@Service
public class MetaFormModelManagerImpl 
		extends BaseEntityManagerImpl<MetaFormModel,java.lang.String,MetaFormModelDao>
	implements MetaFormModelManager{

	public static final Log log = LogFactory.getLog(MetaFormModelManager.class);

	
	private MetaFormModelDao metaFormModelDao ;
	
	@Resource(name = "metaFormModelDao")
    @NotNull
	public void setMetaFormModelDao(MetaFormModelDao baseDao)
	{
		this.metaFormModelDao = baseDao;
		setBaseDao(this.metaFormModelDao);
	}
	
/*
 	@PostConstruct
    public void init() {
        
    }
 	
 */
	@Override
    @Transactional(propagation=Propagation.REQUIRED) 
	public JSONArray listMetaFormModelsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc){
			
		return SysDaoOptUtils.listObjectsAsJson(baseDao, fields, MetaFormModel.class,
    			filterMap, pageDesc);
	}
	
}

