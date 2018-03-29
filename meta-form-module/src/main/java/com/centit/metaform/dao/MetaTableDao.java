package com.centit.metaform.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.metaform.po.MetaTable;



/**
 * MdTableDao  Repository.
 * create by scaffold 2016-06-02 
 
 * 表元数据表状态分为 系统/查询/更新
系统，不可以做任何操作
查询，仅用于通用查询模块，不可以更新
更新，可以更新
   
*/

@Repository
public class MetaTableDao extends BaseDaoImpl<MetaTable,java.lang.Long>
    {

    public static final Log log = LogFactory.getLog(MetaTableDao.class);

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
