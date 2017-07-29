package com.centit.metaform.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.metaform.po.PendingMetaTable;



/**
 * PendingMdTableDao  Repository.
 * create by scaffold 2016-06-01 
 
 * 未落实表元数据表null   
*/

@Repository
public class PendingMetaTableDao extends BaseDaoImpl<PendingMetaTable,java.lang.Long>
	{

	public static final Log log = LogFactory.getLog(PendingMetaTableDao.class);
	
	@Override
	public Map<String, String> getFilterField() {
		if( filterField == null){
			filterField = new HashMap<String, String>();

			filterField.put("tableId" , CodeBook.EQUAL_HQL_ID);


			filterField.put("databaseCode" , CodeBook.EQUAL_HQL_ID);

			filterField.put("tableName" , CodeBook.EQUAL_HQL_ID);

			filterField.put("tableLabelName" , CodeBook.EQUAL_HQL_ID);

			filterField.put("tableType" , CodeBook.EQUAL_HQL_ID);

			filterField.put("tableState" , CodeBook.EQUAL_HQL_ID);

			filterField.put("tableComment" , CodeBook.EQUAL_HQL_ID);

			filterField.put("isInWorkflow" , CodeBook.EQUAL_HQL_ID);

			filterField.put("lastModifyDate" , CodeBook.EQUAL_HQL_ID);

			filterField.put("recorder" , CodeBook.EQUAL_HQL_ID);

		}
		return filterField;
	} 
}
