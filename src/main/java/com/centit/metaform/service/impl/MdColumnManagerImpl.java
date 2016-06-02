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
import com.centit.metaform.dao.MdColumnDao;
import com.centit.metaform.po.MdColumn;
import com.centit.metaform.service.MdColumnManager;

/**
 * MdColumn  Service.
 * create by scaffold 2016-06-02 
 * @author codefan@sina.com
 * 字段元数据表null   
*/
@Service
public class MdColumnManagerImpl 
		extends BaseEntityManagerImpl<MdColumn,com.centit.metaform.po.MdColumnId,MdColumnDao>
	implements MdColumnManager{

	public static final Log log = LogFactory.getLog(MdColumnManager.class);

	
	private MdColumnDao mdColumnDao ;
	
	@Resource(name = "mdColumnDao")
    @NotNull
	public void setMdColumnDao(MdColumnDao baseDao)
	{
		this.mdColumnDao = baseDao;
		setBaseDao(this.mdColumnDao);
	}
	
/*
 	@PostConstruct
    public void init() {
        
    }
 	
 */
	@Override
    @Transactional(propagation=Propagation.REQUIRED) 
	public JSONArray listMdColumnsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc){
			
		return SysDaoOptUtils.listObjectsAsJson(baseDao, fields, MdColumn.class,
    			filterMap, pageDesc);
	}
	
}

