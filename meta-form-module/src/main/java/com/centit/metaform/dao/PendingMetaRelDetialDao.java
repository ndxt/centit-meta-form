package com.centit.metaform.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.metaform.po.PendingMetaRelDetail;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;



/**
 * PendingMdRelDetialDao  Repository.
 * create by scaffold 2016-06-01

 * 未落实表关联细节表null
*/

@Repository
public class PendingMetaRelDetialDao extends BaseDaoImpl<PendingMetaRelDetail, HashMap<String,Object>>
    {

    public static final Log log = LogFactory.getLog(PendingMetaRelDetialDao.class);

    @Override
    public Map<String, String> getFilterField() {
        if( filterField == null){
            filterField = new HashMap<>();

            filterField.put("relationId" , CodeBook.EQUAL_HQL_ID);

            filterField.put("parentColumnName" , CodeBook.EQUAL_HQL_ID);


            filterField.put("childColumnName" , CodeBook.EQUAL_HQL_ID);

        }
        return filterField;
    }
}
