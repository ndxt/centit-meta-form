package com.centit.metaform.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.metaform.po.MetaFormModel;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.metadata.SimpleTableField;



/**
 * MetaFormModelDao  Repository.
 * create by scaffold 2016-06-02 
 
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
	
	public List<Pair<String,String>> getSubModelPropertiesMap(Long parentTableId,Long childTableId){
		@SuppressWarnings("unchecked")
		List<Object[]> columnsMap = (List<Object[]> )DatabaseOptUtils.findObjectsBySql(this, 
			"select b.parent_column_name,b.child_column_name "+
			" from F_META_RELATION a join F_META_REL_DETIAL b on (a.relation_id=b.relation_id)"+
			" where a.parent_table_id = ? and a.child_table_id = ? ",
			new Object[]{parentTableId,childTableId});
		if(columnsMap==null || columnsMap.size()==0)
			return null;
		List<Pair<String,String>> columnList = new ArrayList<>();
		for(Object[] columnPair:columnsMap){
			columnList.add(new ImmutablePair<String,String>(
					SimpleTableField.mapPropName(StringBaseOpt.objectToString(columnPair[0])),
					SimpleTableField.mapPropName(StringBaseOpt.objectToString(columnPair[1]))));
		}
		return columnList;
	}
	
}
