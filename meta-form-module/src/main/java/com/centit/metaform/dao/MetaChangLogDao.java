package com.centit.metaform.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.metaform.po.MetaChangLog;



/**
 * MdChangLogDao  Repository.
 * create by scaffold 2016-06-01 
 
 * 元数据更改记录null   
*/

@Repository
public class MetaChangLogDao extends BaseDaoImpl<MetaChangLog,java.lang.Long>
    {

    public static final Log log = LogFactory.getLog(MetaChangLogDao.class);

    @Override
    public Map<String, String> getFilterField() {
        if( filterField == null){
            filterField = new HashMap<String, String>();

            filterField.put("version" , CodeBook.EQUAL_HQL_ID);


            filterField.put("changeId" , CodeBook.EQUAL_HQL_ID);

            filterField.put("changeDate" , CodeBook.EQUAL_HQL_ID);

            filterField.put("changer" , CodeBook.EQUAL_HQL_ID);

            filterField.put("changeTableSum" , CodeBook.EQUAL_HQL_ID);

            filterField.put("changeRelationSum" , CodeBook.EQUAL_HQL_ID);

            filterField.put("changeScript" , CodeBook.EQUAL_HQL_ID);

            filterField.put("changeComment" , CodeBook.EQUAL_HQL_ID);

            filterField.put("auditor" , CodeBook.EQUAL_HQL_ID);

            filterField.put("auditDate" , CodeBook.EQUAL_HQL_ID);

        }
        return filterField;
    }

    public Long getNextKey(){
        return DatabaseOptUtils.getNextLongSequence(this, "S_META_CHANGLOG_ID");
    }
}
