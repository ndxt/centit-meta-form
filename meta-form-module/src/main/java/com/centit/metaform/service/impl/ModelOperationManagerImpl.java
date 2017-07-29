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
import com.centit.metaform.dao.ModelOperationDao;
import com.centit.metaform.po.ModelOperation;

/**
 * ModelOperation  Service.
 * create by scaffold 2016-06-21 
 
 * 模块操作定义null   
*/
@Service
public class ModelOperationManagerImpl 
		extends BaseEntityManagerImpl<ModelOperation,com.centit.metaform.po.ModelOperationId,ModelOperationDao>
	{

	public static final Log log = LogFactory.getLog(ModelOperationManagerImpl.class);

	
	private ModelOperationDao modelOperationDao ;
	
	@Resource(name = "modelOperationDao")
    @NotNull
	public void setModelOperationDao(ModelOperationDao baseDao)
	{
		this.modelOperationDao = baseDao;
		setBaseDao(this.modelOperationDao);
	}
	
/* 	@PostConstruct
    public void init() {
        
    }
 */
	@Transactional(propagation=Propagation.REQUIRED) 
	public JSONArray listModelOperationsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc){
			
		return SysDaoOptUtils.listObjectsAsJson(baseDao, fields, ModelOperation.class,
    			filterMap, pageDesc);
	}
	
}

