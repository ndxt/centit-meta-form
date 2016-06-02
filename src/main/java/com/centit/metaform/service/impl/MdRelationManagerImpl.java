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
import com.centit.metaform.dao.MdRelationDao;
import com.centit.metaform.po.MdRelation;
import com.centit.metaform.service.MdRelationManager;

/**
 * MdRelation  Service.
 * create by scaffold 2016-06-02 
 
 * 表关联关系表null   
*/
@Service
public class MdRelationManagerImpl 
		extends BaseEntityManagerImpl<MdRelation,java.lang.Long,MdRelationDao>
	implements MdRelationManager{

	public static final Log log = LogFactory.getLog(MdRelationManager.class);

	
	private MdRelationDao mdRelationDao ;
	
	@Resource(name = "mdRelationDao")
    @NotNull
	public void setMdRelationDao(MdRelationDao baseDao)
	{
		this.mdRelationDao = baseDao;
		setBaseDao(this.mdRelationDao);
	}
	
/*
 	@PostConstruct
    public void init() {
        
    }
 	
 */
	@Override
    @Transactional(propagation=Propagation.REQUIRED) 
	public JSONArray listMdRelationsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc){
			
		return SysDaoOptUtils.listObjectsAsJson(baseDao, fields, MdRelation.class,
    			filterMap, pageDesc);
	}
	
}

