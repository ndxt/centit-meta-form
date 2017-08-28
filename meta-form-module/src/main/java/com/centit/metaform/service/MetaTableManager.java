package com.centit.metaform.service;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.hibernate.service.BaseEntityManager;
import com.centit.metaform.po.MetaColumn;
import com.centit.metaform.po.MetaTable;
import com.centit.metaform.po.PendingMetaTable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

/**
 * MdTable  Service.
 * create by scaffold 2016-06-02 
 
 * 表元数据表状态分为 系统/查询/更新
系统，不可以做任何操作
查询，仅用于通用查询模块，不可以更新
更新，可以更新
   
*/

public interface MetaTableManager extends BaseEntityManager<MetaTable,java.lang.Long> 
{
	JSONArray listMdTablesAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc);
	void saveNewPendingMetaTable(PendingMetaTable pmt);
	void deletePendingMetaTable(long tableId);
	PendingMetaTable getPendingMetaTable(long tableId);
	void savePendingMetaTable(PendingMetaTable pmt);
	List<String> makeAlterTableSqls(Long tableId);
	Pair<Integer, String> publishMetaTable(Long tableId,String currentUser);
	JSONArray listDrafts(String[] fields, Map<String, Object> searchColumn,
			PageDesc pageDesc);
	
	List<Pair<String, String>> listTablesInPdm(String pdmFilePath);
	boolean importTableFromPdm(String pdmFilePath, String tableCode, String databaseCode);
	List<MetaColumn> getNotInFormFields(Long tableId);
	List<MetaColumn> listFields(Long tableId);
}
