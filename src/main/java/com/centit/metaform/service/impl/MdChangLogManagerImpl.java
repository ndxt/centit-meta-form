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
import com.centit.metaform.dao.MdChangLogDao;
import com.centit.metaform.po.MdChangLog;
import com.centit.metaform.service.MdChangLogManager;

/**
 * MdChangLog  Service.
 * create by scaffold 2016-06-01 
 
 * 元数据更改记录null   
*/
@Service
public class MdChangLogManagerImpl 
		extends BaseEntityManagerImpl<MdChangLog,java.lang.Long,MdChangLogDao>
	implements MdChangLogManager{

	public static final Log log = LogFactory.getLog(MdChangLogManager.class);

	
	private MdChangLogDao mdChangLogDao ;
	
	@Resource(name = "mdChangLogDao")
    @NotNull
	public void setMdChangLogDao(MdChangLogDao baseDao)
	{
		this.mdChangLogDao = baseDao;
		setBaseDao(this.mdChangLogDao);
	}
	
/*
 	@PostConstruct
    public void init() {
        
    }
 	
 */
	@Override
    @Transactional(propagation=Propagation.REQUIRED) 
	public JSONArray listMdChangLogsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc){
			
		return SysDaoOptUtils.listObjectsAsJson(baseDao, fields, MdChangLog.class,
    			filterMap, pageDesc);
	}
	
}

