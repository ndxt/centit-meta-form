package com.centit.metaform.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.metaform.po.ModelDataField;



/**
 * ModelDataFieldDao  Repository.
 * create by scaffold 2016-06-02 
 * @author codefan@sina.com
 * 数据模板字段null   
*/

@Repository
public class ModelDataFieldDao extends BaseDaoImpl<ModelDataField,com.centit.metaform.po.ModelDataFieldId>
	{

	public static final Log log = LogFactory.getLog(ModelDataFieldDao.class);
	
	@Override
	public Map<String, String> getFilterField() {
		if( filterField == null){
			filterField = new HashMap<String, String>();

			filterField.put("modelCode" , CodeBook.EQUAL_HQL_ID);

			filterField.put("columnName" , CodeBook.EQUAL_HQL_ID);


			filterField.put("accessType" , CodeBook.EQUAL_HQL_ID);

			filterField.put("displayOrder" , CodeBook.EQUAL_HQL_ID);

			filterField.put("inputHint" , CodeBook.EQUAL_HQL_ID);

			filterField.put("validateHint" , CodeBook.EQUAL_HQL_ID);

			filterField.put("fieldHeight" , CodeBook.EQUAL_HQL_ID);

			filterField.put("labelLength" , CodeBook.EQUAL_HQL_ID);

			filterField.put("fieldLength" , CodeBook.EQUAL_HQL_ID);

		}
		return filterField;
	} 
}
