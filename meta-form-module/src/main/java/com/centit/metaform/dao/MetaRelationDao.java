package com.centit.metaform.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.metaform.po.MetaRelation;



/**
 * MdRelationDao  Repository.
 * create by scaffold 2016-06-02 
 
 * 表关联关系表null   
*/

@Repository
public class MetaRelationDao extends BaseDaoImpl<MetaRelation,java.lang.Long>
    {

    public static final Log log = LogFactory.getLog(MetaRelationDao.class);

    @Override
    public Map<String, String> getFilterField() {
        if( filterField == null){
            filterField = new HashMap<String, String>();

            filterField.put("relationId" , CodeBook.EQUAL_HQL_ID);


            filterField.put("parentTableId" , CodeBook.EQUAL_HQL_ID);

            filterField.put("childTableId" , CodeBook.EQUAL_HQL_ID);

            filterField.put("relationName" , CodeBook.EQUAL_HQL_ID);

            filterField.put("relationState" , CodeBook.EQUAL_HQL_ID);

            filterField.put("relationComment" , CodeBook.EQUAL_HQL_ID);

            filterField.put("lastModifyDate" , CodeBook.EQUAL_HQL_ID);

            filterField.put("recorder" , CodeBook.EQUAL_HQL_ID);

        }
        return filterField;
    }
}
