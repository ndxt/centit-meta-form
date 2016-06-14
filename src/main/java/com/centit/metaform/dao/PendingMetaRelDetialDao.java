package com.centit.metaform.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.metaform.po.PendingMetaRelDetial;



/**
 * PendingMdRelDetialDao  Repository.
 * create by scaffold 2016-06-01 
 
 * 未落实表关联细节表null   
*/

@Repository
public class PendingMetaRelDetialDao extends BaseDaoImpl<PendingMetaRelDetial,com.centit.metaform.po.PendingMetaRelDetialId>
	{

	public static final Log log = LogFactory.getLog(PendingMetaRelDetialDao.class);
	
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
