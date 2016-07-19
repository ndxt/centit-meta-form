package com.centit.metaform.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.core.service.BaseEntityManager;
import com.centit.metaform.po.MetaColumn;
import com.centit.metaform.po.MetaTable;
import com.centit.metaform.po.PendingMetaTable;

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
	public JSONArray listMdTablesAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc);
	public Serializable saveNewPendingMetaTable(PendingMetaTable pmt);
	public void deletePendingMetaTable(long tableId);
	public PendingMetaTable getPendingMetaTable(long tableId);
	public void savePendingMetaTable(PendingMetaTable pmt);
	public List<String> makeAlterTableSqls(Long tableId);
	public Pair<Integer, String> publishMetaTable(Long tableId,String currentUser);
	public List<PendingMetaTable> listDrafts(Map<String, Object> searchColumn,
			PageDesc pageDesc);
	
	public List<Pair<String, String>> listTablesInPdm(String pdmFilePath);
	public boolean importTableFromPdm(String pdmFilePath, String tableCode, String databaseCode);
	public List<MetaColumn> getNotInFormFields(Long tableId);
	public List<MetaColumn> listFields(Long tableId);
}
