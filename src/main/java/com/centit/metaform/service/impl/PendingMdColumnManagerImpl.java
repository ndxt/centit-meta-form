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
import com.centit.metaform.dao.PendingMdColumnDao;
import com.centit.metaform.po.PendingMdColumn;
import com.centit.metaform.service.PendingMdColumnManager;

/**
 * PendingMdColumn  Service.
 * create by scaffold 2016-06-01 
 * @author codefan@sina.com
 * 未落实字段元数据表null   
*/
@Service
public class PendingMdColumnManagerImpl 
		extends BaseEntityManagerImpl<PendingMdColumn,java.lang.Long,PendingMdColumnDao>
	implements PendingMdColumnManager{

	public static final Log log = LogFactory.getLog(PendingMdColumnManager.class);

	
	private PendingMdColumnDao pendingMdColumnDao ;
	
	@Resource(name = "pendingMdColumnDao")
    @NotNull
	public void setPendingMdColumnDao(PendingMdColumnDao baseDao)
	{
		this.pendingMdColumnDao = baseDao;
		setBaseDao(this.pendingMdColumnDao);
	}
	
/*
 	@PostConstruct
    public void init() {
        
    }
 	
 */
	@Override
    @Transactional(propagation=Propagation.REQUIRED) 
	public JSONArray listPendingMdColumnsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc){
			
		return SysDaoOptUtils.listObjectsAsJson(baseDao, fields, PendingMdColumn.class,
    			filterMap, pageDesc);
	}
	
}

