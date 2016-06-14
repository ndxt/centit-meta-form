package com.centit.metaform.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.core.service.BaseEntityManager;
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
	public Serializable saveNewPendingMdTable(PendingMetaTable pmt);
	public void deletePendingMdTable(long tableId);
	public PendingMetaTable getPendingMdTable(long tableId);
	public void savePendingMdTable(PendingMetaTable pmt);
	public String publishMdTable(Long tableId);
	public List<PendingMetaTable> listDrafts(Map<String, Object> searchColumn,
			PageDesc pageDesc);
}
