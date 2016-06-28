package com.centit.metaform.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
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
import com.centit.dde.dao.DatabaseInfoDao;
import com.centit.dde.po.DatabaseInfo;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.hibernate.dao.SysDaoOptUtils;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
import com.centit.metaform.dao.MetaChangLogDao;
import com.centit.metaform.dao.MetaTableDao;
import com.centit.metaform.dao.PendingMetaTableDao;
import com.centit.metaform.po.MetaChangLog;
import com.centit.metaform.po.MetaColumn;
import com.centit.metaform.po.MetaTable;
import com.centit.metaform.po.PendingMetaColumn;
import com.centit.metaform.po.PendingMetaTable;
import com.centit.metaform.service.MetaTableManager;
import com.centit.support.database.DataSourceDescription;
import com.centit.support.database.DbcpConnect;
import com.centit.support.database.DbcpConnectPools;
import com.centit.support.database.ddl.DB2DDLOperations;
import com.centit.support.database.ddl.DDLOperations;
import com.centit.support.database.ddl.MySqlDDLOperations;
import com.centit.support.database.ddl.OracleDDLOperations;
import com.centit.support.database.ddl.SqlSvrDDLOperations;
import com.centit.support.database.jsonmaptable.DB2JsonObjectDao;
import com.centit.support.database.jsonmaptable.JsonObjectDao;
import com.centit.support.database.jsonmaptable.MySqlJsonObjectDao;
import com.centit.support.database.jsonmaptable.OracleJsonObjectDao;
import com.centit.support.database.jsonmaptable.SqlSvrJsonObjectDao;

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
	
	@Resource
	private MetaChangLogDao metaChangLogDao;
	
	@Resource
    private DatabaseInfoDao databaseInfoDao;
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

	/**
	 * 对比pendingMetaTable和MetaTable中的字段信息，并对数据库中的表进行重构，
	 * 重构成功后将对应的表结构信息同步到 MetaTable中，并在MetaChangeLog中记录信息
	 */
	@Override
	@Transactional
	public String publishMetaTable(Long tableId) {
		try{
		PendingMetaTable ptable=pendingMdTableDao.getObjectById(tableId);
		MetaTable stable = metaTableDao.getObjectById(tableId);
		DatabaseInfo mdb = databaseInfoDao.getObjectById(stable.getDatabaseCode());		
		DataSourceDescription dbc = new DataSourceDescription();
		dbc.setDatabaseCode(mdb.getDatabaseCode());
		dbc.setConnUrl(mdb.getDatabaseUrl());
		dbc.setUsername(mdb.getUsername());
		dbc.setPassword(mdb.getPassword());		
		DbcpConnect conn = DbcpConnectPools.getDbcpConnect(dbc);
		JsonObjectDao jsonDao=null;
		DDLOperations ddlOpt = null;
		switch(conn.getDatabaseType()){
		case Oracle:
			jsonDao = new OracleJsonObjectDao(conn,stable);
			ddlOpt = new OracleDDLOperations();
			break;
	  	case DB2:
	  		jsonDao = new DB2JsonObjectDao(conn,stable);
	  		ddlOpt = new DB2DDLOperations();
	  		break;
	  	case SqlServer:
	  		jsonDao = new SqlSvrJsonObjectDao(conn,stable);
	  		ddlOpt = new SqlSvrDDLOperations();
	  		break;
	  	case MySql:
	  		jsonDao = new MySqlJsonObjectDao(conn,stable);
	  		ddlOpt = new MySqlDDLOperations();
	  		break;
	  	default:
	  		jsonDao = new OracleJsonObjectDao(conn,stable);
	  		ddlOpt = new OracleDDLOperations();
	  		break;
		}
		
		List<String> sqls = new ArrayList<>();
		for(PendingMetaColumn pcol : ptable.getMdColumns()){
			
		}
			
		for(MetaColumn pcol : stable.getMdColumns()){
			
		}
		
		
		
		MetaTable table= new MetaTable(ptable);
		metaTableDao.mergeObject(table);
		//MetaChangLog chgLog = new MetaChangLog();		
		//metaChangLogDao.saveNewObject(chgLog);
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

