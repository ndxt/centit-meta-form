package com.centit.metaform.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.metaform.po.MetaFormModel;



/**
 * MetaFormModelDao  Repository.
 * create by scaffold 2016-06-02 
 * @author codefan@sina.com
 * 通用模块管理null   
*/

@Repository
public class MetaFormModelDao extends BaseDaoImpl<MetaFormModel,java.lang.String>
	{

	public static final Log log = LogFactory.getLog(MetaFormModelDao.class);
	
	@Override
	public Map<String, String> getFilterField() {
		if( filterField == null){
			filterField = new HashMap<String, String>();

			filterField.put("modelCode" , CodeBook.EQUAL_HQL_ID);


			filterField.put("tableId" , CodeBook.EQUAL_HQL_ID);

			filterField.put("modelComment" , CodeBook.EQUAL_HQL_ID);

			filterField.put("modelName" , CodeBook.EQUAL_HQL_ID);

			filterField.put("accessType" , CodeBook.EQUAL_HQL_ID);

			filterField.put("relationType" , CodeBook.EQUAL_HQL_ID);

			filterField.put("parentModelCode" , CodeBook.EQUAL_HQL_ID);

			filterField.put("displayOrder" , CodeBook.EQUAL_HQL_ID);

			filterField.put("lastModifyDate" , CodeBook.EQUAL_HQL_ID);

			filterField.put("recorder" , CodeBook.EQUAL_HQL_ID);

		}
		return filterField;
	} 
}
