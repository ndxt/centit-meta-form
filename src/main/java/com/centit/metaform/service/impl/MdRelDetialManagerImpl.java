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
import com.centit.metaform.dao.MdRelDetialDao;
import com.centit.metaform.po.MdRelDetail;
import com.centit.metaform.service.MdRelDetialManager;

/**
 * MdRelDetial  Service.
 * create by scaffold 2016-06-02 
 
 * 表关联细节表null   
*/
@Service
public class MdRelDetialManagerImpl 
		extends BaseEntityManagerImpl<MdRelDetail,com.centit.metaform.po.MdRelDetiaild,MdRelDetialDao>
	implements MdRelDetialManager{

	public static final Log log = LogFactory.getLog(MdRelDetialManager.class);

	
	private MdRelDetialDao mdRelDetialDao ;
	
	@Resource(name = "mdRelDetialDao")
    @NotNull
	public void setMdRelDetialDao(MdRelDetialDao baseDao)
	{
		this.mdRelDetialDao = baseDao;
		setBaseDao(this.mdRelDetialDao);
	}
	
/*
 	@PostConstruct
    public void init() {
        
    }
 	
 */
	@Override
    @Transactional(propagation=Propagation.REQUIRED) 
	public JSONArray listMdRelDetialsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc){
			
		return SysDaoOptUtils.listObjectsAsJson(baseDao, fields, MdRelDetail.class,
    			filterMap, pageDesc);
	}
	
}

