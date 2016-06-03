package com.centit.metaform.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.core.service.BaseEntityManager;
import com.centit.metaform.po.MdTable;
import com.centit.metaform.po.PendingMdTable;

/**
 * MdTable  Service.
 * create by scaffold 2016-06-02 
 
 * 表元数据表状态分为 系统/查询/更新
系统，不可以做任何操作
查询，仅用于通用查询模块，不可以更新
更新，可以更新
   
*/

public interface MdTableManager extends BaseEntityManager<MdTable,java.lang.Long> 
{
	public JSONArray listMdTablesAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc);
	public Serializable saveNewPendingMdTable(PendingMdTable pmt);
	public void deletePendingMdTable(long tableId);
	public PendingMdTable getPendingMdTable(long tableId);
	public void savePendingMdTable(PendingMdTable pmt);
	public String publishMdTable(Long tableId);
	public List<PendingMdTable> listDrafts(Map<String, Object> searchColumn,
			PageDesc pageDesc);
}
