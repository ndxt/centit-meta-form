package com.centit.metaform.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.metaform.po.MetaRelDetail;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;



/**
 * MdRelDetialDao  Repository.
 * create by scaffold 2016-06-02 
 
 * 表关联细节表null   
*/

@Repository
public class MetaRelDetialDao extends BaseDaoImpl<MetaRelDetail,com.centit.metaform.po.MetaRelDetailId>
    {

    public static final Log log = LogFactory.getLog(MetaRelDetialDao.class);

    @Override
    public Map<String, String> getFilterField() {
        if( filterField == null){
            filterField = new HashMap<String, String>();

            filterField.put("relationId" , CodeBook.EQUAL_HQL_ID);

            filterField.put("parentColumnName" , CodeBook.EQUAL_HQL_ID);


            filterField.put("childColumnName" , CodeBook.EQUAL_HQL_ID);

        }
        return filterField;
    }
}
