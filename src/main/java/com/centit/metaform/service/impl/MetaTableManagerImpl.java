package com.centit.metaform.service.impl;

import java.io.Serializable;
import java.util.List;
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
import com.centit.metaform.dao.MetaTableDao;
import com.centit.metaform.dao.PendingMetaTableDao;
import com.centit.metaform.po.MetaTable;
import com.centit.metaform.po.PendingMetaTable;
import com.centit.metaform.service.MetaTableManager;

/**
 * MdTable  Service.
 * create by scaffold 2016-06-02 
 
 * 表元数据表状态分为 系统/查询/更新
系统，不可以做任何操作
查询，仅用于通用查询模块，不可以更新
更新，可以更新
   
*/
@Service
public class MetaTableManagerImpl 
		extends BaseEntityManagerImpl<MetaTable,java.lang.Long,MetaTableDao>
	implements MetaTableManager{

	public static final Log log = LogFactory.getLog(MetaTableManager.class);

	
	private MetaTableDao metaTableDao ;
	
	@Resource(name = "metaTableDao")
    @NotNull
	public void setMetaTableDao(MetaTableDao baseDao)
	{
		this.metaTableDao = baseDao;
		setBaseDao(this.metaTableDao);
	}
	
	@Resource
	private PendingMetaTableDao pendingMdTableDao;
/*
 	@PostConstruct
    public void init() {
        
    }
 	
 */
	@Override
    @Transactional(propagation=Propagation.REQUIRED) 
	public JSONArray listMdTablesAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc){
			
		return SysDaoOptUtils.listObjectsAsJson(baseDao, fields, MetaTable.class,
    			filterMap, pageDesc);
	}

	@Override
	@Transactional
	public Serializable saveNewPendingMetaTable(PendingMetaTable pmt) {
		return pendingMdTableDao.saveNewObject(pmt);
	}

	@Override
	@Transactional
	public void deletePendingMetaTable(long tableId) {
		pendingMdTableDao.deleteObjectById(tableId);
	}

	@Override
	@Transactional
	public PendingMetaTable getPendingMetaTable(long tableId) {
		return pendingMdTableDao.getObjectById(tableId);
	}

	@Override
	@Transactional
	public void savePendingMetaTable(PendingMetaTable pmt) {
		pendingMdTableDao.mergeObject(pmt);
	}

	@Override
	@Transactional
	public String publishMetaTable(Long tableId) {
		try{
		PendingMetaTable ptable=pendingMdTableDao.getObjectById(tableId);
		MetaTable table= new MetaTable(ptable);
		metaTableDao.mergeObject(table);
		}catch(Exception e){
			return "failed to publish!";
		}
		return "finished!";
	}

	@Override
	@Transactional(readOnly=true)
	public List<PendingMetaTable> listDrafts(Map<String, Object> searchColumn,
			PageDesc pageDesc) {
		return pendingMdTableDao.listObjects(searchColumn, pageDesc);
	}
	
}

