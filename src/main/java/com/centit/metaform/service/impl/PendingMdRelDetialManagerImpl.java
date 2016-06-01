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
import com.centit.metaform.dao.PendingMdRelDetialDao;
import com.centit.metaform.po.PendingMdRelDetial;
import com.centit.metaform.service.PendingMdRelDetialManager;

/**
 * PendingMdRelDetial  Service.
 * create by scaffold 2016-06-01 
 * @author codefan@sina.com
 * 未落实表关联细节表null   
*/
@Service
public class PendingMdRelDetialManagerImpl 
		extends BaseEntityManagerImpl<PendingMdRelDetial,com.centit.metaform.po.PendingMdRelDetialId,PendingMdRelDetialDao>
	implements PendingMdRelDetialManager{

	public static final Log log = LogFactory.getLog(PendingMdRelDetialManager.class);

	
	private PendingMdRelDetialDao pendingMdRelDetialDao ;
	
	@Resource(name = "pendingMdRelDetialDao")
    @NotNull
	public void setPendingMdRelDetialDao(PendingMdRelDetialDao baseDao)
	{
		this.pendingMdRelDetialDao = baseDao;
		setBaseDao(this.pendingMdRelDetialDao);
	}
	
/*
 	@PostConstruct
    public void init() {
        
    }
 	
 */
	@Override
    @Transactional(propagation=Propagation.REQUIRED) 
	public JSONArray listPendingMdRelDetialsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc){
			
		return SysDaoOptUtils.listObjectsAsJson(baseDao, fields, PendingMdRelDetial.class,
    			filterMap, pageDesc);
	}
	
}

