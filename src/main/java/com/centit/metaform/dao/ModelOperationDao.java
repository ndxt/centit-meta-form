package com.centit.metaform.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.metaform.po.ModelOperation;



/**
 * ModelOperationDao  Repository.
 * create by scaffold 2016-06-21 
 
 * 模块操作定义null   
*/

@Repository
public class ModelOperationDao extends BaseDaoImpl<ModelOperation,com.centit.metaform.po.ModelOperationId>
	{

	public static final Log log = LogFactory.getLog(ModelOperationDao.class);
	
	@Override
	public Map<String, String> getFilterField() {
		if( filterField == null){
			filterField = new HashMap<String, String>();

			filterField.put("modelCode" , CodeBook.EQUAL_HQL_ID);

			filterField.put("operation" , CodeBook.EQUAL_HQL_ID);


			filterField.put("optModelCode" , CodeBook.EQUAL_HQL_ID);

			filterField.put("method" , CodeBook.EQUAL_HQL_ID);

			filterField.put("label" , CodeBook.EQUAL_HQL_ID);

			filterField.put("displayOrder" , CodeBook.EQUAL_HQL_ID);

			filterField.put("openType" , CodeBook.EQUAL_HQL_ID);

			filterField.put("returnOperation" , CodeBook.EQUAL_HQL_ID);

			filterField.put("optHintType" , CodeBook.EQUAL_HQL_ID);

			filterField.put("optHintInfo" , CodeBook.EQUAL_HQL_ID);

			filterField.put("extendOptions" , CodeBook.EQUAL_HQL_ID);

		}
		return filterField;
	} 
}
