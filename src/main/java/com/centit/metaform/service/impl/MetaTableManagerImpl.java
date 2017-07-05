package com.centit.metaform.service.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import com.centit.framework.staticsystem.service.IntegrationEnvironment;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.hibernate.dao.SysDaoOptUtils;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
import com.centit.framework.staticsystem.po.DatabaseInfo;
import com.centit.metaform.dao.MetaChangLogDao;
import com.centit.metaform.dao.MetaColumnDao;
import com.centit.metaform.dao.MetaTableDao;
import com.centit.metaform.dao.PendingMetaRelationDao;
import com.centit.metaform.dao.PendingMetaTableDao;
import com.centit.metaform.formaccess.FieldType;
import com.centit.metaform.formaccess.PdmTableInfo;
import com.centit.metaform.po.MetaChangLog;
import com.centit.metaform.po.MetaColumn;
import com.centit.metaform.po.MetaTable;
import com.centit.metaform.po.PendingMetaColumn;
import com.centit.metaform.po.PendingMetaRelation;
import com.centit.metaform.po.PendingMetaTable;
import com.centit.metaform.service.MetaTableManager;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.database.DBType;
import com.centit.support.database.DataSourceDescription;
import com.centit.support.database.DbcpConnect;
import com.centit.support.database.DbcpConnectPools;
import com.centit.support.database.ddl.DB2DDLOperations;
import com.centit.support.database.ddl.DDLOperations;
import com.centit.support.database.ddl.GeneralDDLOperations;
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
	private MetaColumnDao metaColumnDao;
	
	@Resource
	private MetaChangLogDao metaChangLogDao;
			
	@Resource
	private PendingMetaTableDao pendingMdTableDao;
	
	@Resource
    private PendingMetaRelationDao pendignRelationDao;
	
	@Resource
    protected IntegrationEnvironment integrationEnvironment;
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
		return  makeAlterTableSqls(ptable);
	}

	@Transactional
	public List<String> makeAlterTableSqls(PendingMetaTable ptable) {		
		MetaTable stable = metaTableDao.getObjectById(ptable.getTableId());
		
		DatabaseInfo mdb = integrationEnvironment.getDatabaseInfo(ptable.getDatabaseCode());
				//databaseInfoDao.getDatabaseInfoById(ptable.getDatabaseCode());
		
		DBType dbType = DBType.mapDBType(mdb.getDatabaseUrl());
		ptable.setDatabaseType(dbType);
		DDLOperations ddlOpt = null;
		switch(dbType){
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
			stable.setDatabaseType(dbType);
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
	
	public void checkPendingMetaTable(PendingMetaTable ptable,String currentUser){
		if("Y".equals(ptable.getUpdateCheckTimeStamp())){
			PendingMetaColumn col = ptable.findFieldByName("lastModifyDate");
			if(col==null){
				col = new PendingMetaColumn(ptable, "LAST_MODIFY_DATE");
				col.setFieldLabelName("最新更新时间");
				col.setColumnComment("最新更新时间");
				col.setColumnFieldType(FieldType.DATETIME);
				col.setLastModifyDate(DatetimeOpt.currentUtilDate());
				col.setRecorder(currentUser);
				ptable.getColumns().add(col);
			};
		}
		
		if("1".equals(ptable.getWorkFlowOptType())){
			PendingMetaColumn col = ptable.findFieldByName("wfInstId");
			if(col==null){
				col = new PendingMetaColumn(ptable, "WF_INST_ID");
				col.setFieldLabelName("流程实例ID");
				col.setColumnComment("业务对应的工作流程实例ID");
				col.setColumnFieldType(FieldType.INTEGER);
				col.setMaxLength(12);
				col.setLastModifyDate(DatetimeOpt.currentUtilDate());
				col.setRecorder(currentUser);
				ptable.getColumns().add(col);
			};
		}else if("2".equals(ptable.getWorkFlowOptType())){
			PendingMetaColumn col = ptable.findFieldByName("nodeInstId");
			if(col==null){
				col = new PendingMetaColumn(ptable, "NODE_INST_ID");
				col.setFieldLabelName("流程实例ID");
				col.setColumnComment("业务对应的工作流程实例ID");
				col.setColumnFieldType(FieldType.INTEGER);
				col.setMaxLength(12);
				col.setLastModifyDate(DatetimeOpt.currentUtilDate());
				col.setRecorder(currentUser);
				ptable.getColumns().add(col);
			};
		}		
	}
	/**
	 * 对比pendingMetaTable和MetaTable中的字段信息，并对数据库中的表进行重构，
	 * 重构成功后将对应的表结构信息同步到 MetaTable中，并在MetaChangeLog中记录信息
	 * @return 返回错误编号 和 错误说明， 编号为0表示成功
	 */
	@Override
	@Transactional
	public Pair<Integer, String> publishMetaTable(Long tableId,String currentUser) {
		//TODO 根据不同的表类别 做不同的重构
		try{
			PendingMetaTable ptable=pendingMdTableDao.getObjectById(tableId);		
			
			Pair<Integer, String> ret = GeneralDDLOperations.checkTableWellDefined(ptable);
			if(ret.getLeft().intValue() != 0)
				return ret;
			MetaChangLog chgLog = new MetaChangLog();
			List<String> errors = new ArrayList<>();
			if("T".equals(ptable.getTableType())) {
				DatabaseInfo mdb = integrationEnvironment.getDatabaseInfo(ptable.getDatabaseCode());
				//databaseInfoDao.getDatabaseInfoById(ptable.getDatabaseCode());

				DataSourceDescription dbc = new DataSourceDescription();
				dbc.setDatabaseCode(mdb.getDatabaseCode());
				dbc.setConnUrl(mdb.getDatabaseUrl());
				dbc.setUsername(mdb.getUsername());
				dbc.setPassword(mdb.getClearPassword());
				DbcpConnect conn = DbcpConnectPools.getDbcpConnect(dbc);
				JsonObjectDao jsonDao = null;
				ptable.setDatabaseType(conn.getDatabaseType());
				switch (conn.getDatabaseType()) {
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
				//检查字段定义一致性，包括：检查是否有时间戳、是否和工作流关联
				checkPendingMetaTable(ptable, currentUser);
				List<String> sqls = makeAlterTableSqls(ptable);

				for (String sql : sqls) {
					try {
						jsonDao.doExecuteSql(sql);
					} catch (SQLException se) {
						errors.add(se.getMessage());
					}
				}
				chgLog.setChangeScript(JSON.toJSONString(sqls));
				chgLog.setChangeComment(JSON.toJSONString(errors));
			}
			chgLog.setChangeId(metaChangLogDao.getNextKey());
			chgLog.setTableID(ptable.getTableId());
			chgLog.setChanger(currentUser);
			metaChangLogDao.saveNewObject(chgLog);
			if(errors.size()==0){
				ptable.setRecorder(currentUser);
				pendingMdTableDao.mergeObject(ptable);
				MetaTable table= new MetaTable(ptable);
				metaTableDao.mergeObject(table);
				return new ImmutablePair<>(0,"发布成功！");
			}else
				return new ImmutablePair<>(-10,JSON.toJSONString(errors));
		}catch(Exception e){
			return new ImmutablePair<>(0,"发布失败!" +  e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly=true)
	public JSONArray listDrafts(String[] fields, Map<String, Object> searchColumn,
			PageDesc pageDesc) {
		JSONArray listTables = SysDaoOptUtils.listObjectsAsJson( baseDao ,
	            fields,PendingMetaTable.class, 
	            searchColumn,  pageDesc);
		List<DatabaseInfo> databases = integrationEnvironment.listDatabaseInfo();
		for(Object obj:listTables){
			JSONObject table = (JSONObject)obj;
			String databaseCode = table.getString("databaseCode");
			if(databaseCode!=null){
				for(DatabaseInfo di:databases){
					if(databaseCode.equals(di.getDatabaseCode())){
						table.put("databaseName", di.getDatabaseName());
						break;
					}
				}
			}
		}
		return listTables;
	}

	@Override
	public List<Pair<String, String>> listTablesInPdm(String pdmFilePath) {
		return PdmTableInfo.listTablesInPdm(pdmFilePath);
	}

	@Override
	@Transactional
	public boolean importTableFromPdm(String pdmFilePath, String tableCode, String databaseCode) {
		PendingMetaTable metaTable = PdmTableInfo.importTableFromPdm(pdmFilePath, tableCode, databaseCode);
		if(metaTable==null)
			return false;
		pendingMdTableDao.saveNewObject(metaTable);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<MetaColumn> getNotInFormFields(Long tableId) {
		String sql = "select * from F_META_COLUMN  t where t.table_id=? " +
				"and t.column_name not in " +
				"(select f.column_name from m_model_data_field f join m_meta_form_model m" +
				" on f.model_code=m.model_code and m.table_id=? )";
		return
				(List<MetaColumn>) DatabaseOptUtils.findObjectsBySql(metaColumnDao, sql, new Object[]{tableId,tableId},MetaColumn.class);
	}

	@Override
	public List<MetaColumn> listFields(Long tableId) {
		Map<String,Object> filterMap = new HashMap<String,Object>();
       	filterMap.put("tableId", tableId);
       	
		return metaColumnDao.listObjects(filterMap);
	}

}

