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
import com.centit.metaform.dao.PendingMdTableDao;
import com.centit.metaform.po.PendingMdTable;
import com.centit.metaform.service.PendingMdTableManager;

/**
 * PendingMdTable  Service.
 * create by scaffold 2016-06-01 
 * @author codefan@sina.com
 * 未落实表元数据表null   
*/
@Service
public class PendingMdTableManagerImpl 
		extends BaseEntityManagerImpl<PendingMdTable,java.lang.Long,PendingMdTableDao>
	implements PendingMdTableManager{

	public static final Log log = LogFactory.getLog(PendingMdTableManager.class);

	
	private PendingMdTableDao pendingMdTableDao ;
	
	@Resource(name = "pendingMdTableDao")
    @NotNull
	public void setPendingMdTableDao(PendingMdTableDao baseDao)
	{
		this.pendingMdTableDao = baseDao;
		setBaseDao(this.pendingMdTableDao);
	}
	
/*
 	@PostConstruct
    public void init() {
        
    }
 	
 */
	@Override
    @Transactional(propagation=Propagation.REQUIRED) 
	public JSONArray listPendingMdTablesAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc){
			
		return SysDaoOptUtils.listObjectsAsJson(baseDao, fields, PendingMdTable.class,
    			filterMap, pageDesc);
	}
	
}

