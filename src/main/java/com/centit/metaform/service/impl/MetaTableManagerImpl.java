package com.centit.metaform.service.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.centit.dde.dao.DatabaseInfoDao;
import com.centit.dde.po.DatabaseInfo;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.hibernate.dao.SysDaoOptUtils;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
import com.centit.metaform.dao.MetaChangLogDao;
import com.centit.metaform.dao.MetaTableDao;
import com.centit.metaform.dao.PendingMetaRelationDao;
import com.centit.metaform.dao.PendingMetaTableDao;
import com.centit.metaform.po.MetaChangLog;
import com.centit.metaform.po.MetaColumn;
import com.centit.metaform.po.MetaTable;
import com.centit.metaform.po.PendingMetaColumn;
import com.centit.metaform.po.PendingMetaRelation;
import com.centit.metaform.po.PendingMetaTable;
import com.centit.metaform.service.MetaTableManager;
import com.centit.support.database.DBType;
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
	
	
	@Resource
    private PendingMetaRelationDao pendignRelationDao;
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
		Set<PendingMetaRelation> relations =pmt.getMdRelations();
		Iterator<PendingMetaRelation> itr=relations.iterator();
		while(itr.hasNext()){
			PendingMetaRelation relation =itr.next();
			relation.setParentTable(pmt);
			if(relation.getRelationId()==null)
				pendignRelationDao.saveNewObject(relation);
			else{
				pendignRelationDao.mergeObject(relation);
			}
		}
		pendingMdTableDao.mergeObject(pmt);
	}

	
	/**
	 * 对比pendingMetaTable和MetaTable中的字段信息，
	 * 获取表结构差异对应的Sql语句
	 */
	@Override
	@Transactional
	public List<String> makeAlterTableSqls(Long tableId) {
		PendingMetaTable ptable=pendingMdTableDao.getObjectById(tableId);
		MetaTable stable = metaTableDao.getObjectById(tableId);
		DatabaseInfo mdb = databaseInfoDao.getDatabaseInfoById(ptable.getDatabaseCode());		
	
		DDLOperations ddlOpt = null;
		switch(DBType.mapDBType(mdb.getDatabaseUrl())){
		case Oracle:
			ddlOpt = new OracleDDLOperations();
			break;
	  	case DB2:
	  		ddlOpt = new DB2DDLOperations();
	  		break;
	  	case SqlServer:
	  		ddlOpt = new SqlSvrDDLOperations();
	  		break;
	  	case MySql:
	  		ddlOpt = new MySqlDDLOperations();
	  		break;
	  	default:
	  		ddlOpt = new OracleDDLOperations();
	  		break;
		}
		
		List<String> sqls = new ArrayList<>();
		if(stable==null){
			sqls.add(ddlOpt.makeCreateTableSql(ptable));
		}else{
			for(PendingMetaColumn pcol : ptable.getMdColumns()){
				MetaColumn ocol = stable.findFieldByColumn(pcol.getColumnName());
				if(ocol==null){
					sqls.add(ddlOpt.makeAddColumnSql(
							ptable.getTableName(), pcol) );
				}else{
					if(pcol.getColumnType().equals(ocol.getColumnType())){
						if( pcol.getMaxLength() != ocol.getMaxLength() ||
								pcol.getScale() != ocol.getScale()){
							sqls.add(ddlOpt.makeModifyColumnSql(
									ptable.getTableName(), pcol) );
						}
					}else{
						sqls.addAll(ddlOpt.makeReconfigurationColumnSqls(
								ptable.getTableName(),ocol.getColumnName(), pcol));
					}
				}
			}
				
			for(MetaColumn ocol : stable.getMdColumns()){
				PendingMetaColumn pcol = ptable.findFieldByColumn(ocol.getColumnName());
				if(pcol==null){
					sqls.add(ddlOpt.makeDropColumnSql(stable.getTableName(),ocol.getColumnName()));
				}
			}
		}
		
		return sqls;
	}
	
	/**
	 * 对比pendingMetaTable和MetaTable中的字段信息，并对数据库中的表进行重构，
	 * 重构成功后将对应的表结构信息同步到 MetaTable中，并在MetaChangeLog中记录信息
	 */
	@Override
	@Transactional
	public String publishMetaTable(Long tableId,String currentUser) {
		try{
			PendingMetaTable ptable=pendingMdTableDao.getObjectById(tableId);
			DatabaseInfo mdb = databaseInfoDao.getDatabaseInfoById(ptable.getDatabaseCode());		
			DataSourceDescription dbc = new DataSourceDescription();
			dbc.setDatabaseCode(mdb.getDatabaseCode());
			dbc.setConnUrl(mdb.getDatabaseUrl());
			dbc.setUsername(mdb.getUsername());
			dbc.setPassword(mdb.getClearPassword());		
			DbcpConnect conn = DbcpConnectPools.getDbcpConnect(dbc);
			JsonObjectDao jsonDao=null;
			switch(conn.getDatabaseType()){
			case Oracle:
				jsonDao = new OracleJsonObjectDao(conn);
				break;
		  	case DB2:
		  		jsonDao = new DB2JsonObjectDao(conn);
		  		break;
		  	case SqlServer:
		  		jsonDao = new SqlSvrJsonObjectDao(conn);
		  		break;
		  	case MySql:
		  		jsonDao = new MySqlJsonObjectDao(conn);
		  		break;
		  	default:
		  		jsonDao = new OracleJsonObjectDao(conn);
		  		break;
			}
			
			List<String> sqls =  makeAlterTableSqls(tableId);
			
			List<String> errors = new ArrayList<>();
			for(String sql:sqls){
				try{
					jsonDao.doExecuteSql(sql);
				}catch(SQLException se){
					errors.add(se.getMessage());
				}
			}
			if(errors.size()==0)
				errors.add("表结构变更成功！");
			
			MetaChangLog chgLog = new MetaChangLog();
			chgLog.setChangeId(metaChangLogDao.getNextKey());
			chgLog.setTableID(ptable.getTableId());
			chgLog.setChangeScript(JSON.toJSONString(sqls));
			chgLog.setChangeComment(JSON.toJSONString(errors));
			chgLog.setChanger(currentUser);
			metaChangLogDao.saveNewObject(chgLog);
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

