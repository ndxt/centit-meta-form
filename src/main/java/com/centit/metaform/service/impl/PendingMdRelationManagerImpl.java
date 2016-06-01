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
import com.centit.metaform.dao.PendingMdRelationDao;
import com.centit.metaform.po.PendingMdRelation;
import com.centit.metaform.service.PendingMdRelationManager;

/**
 * PendingMdRelation  Service.
 * create by scaffold 2016-06-01 
 * @author codefan@sina.com
 * 未落实表关联关系表null   
*/
@Service
public class PendingMdRelationManagerImpl 
		extends BaseEntityManagerImpl<PendingMdRelation,java.lang.Long,PendingMdRelationDao>
	implements PendingMdRelationManager{

	public static final Log log = LogFactory.getLog(PendingMdRelationManager.class);

	
	private PendingMdRelationDao pendingMdRelationDao ;
	
	@Resource(name = "pendingMdRelationDao")
    @NotNull
	public void setPendingMdRelationDao(PendingMdRelationDao baseDao)
	{
		this.pendingMdRelationDao = baseDao;
		setBaseDao(this.pendingMdRelationDao);
	}
	
/*
 	@PostConstruct
    public void init() {
        
    }
 	
 */
	@Override
    @Transactional(propagation=Propagation.REQUIRED) 
	public JSONArray listPendingMdRelationsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc){
			
		return SysDaoOptUtils.listObjectsAsJson(baseDao, fields, PendingMdRelation.class,
    			filterMap, pageDesc);
	}
	
}

