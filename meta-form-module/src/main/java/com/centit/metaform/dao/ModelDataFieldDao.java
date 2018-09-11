package com.centit.metaform.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.metaform.po.ModelDataField;
import com.centit.support.algorithm.CollectionsOpt;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * ModelDataFieldDao  Repository.
 * create by scaffold 2016-06-02 
 
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

    public List<ModelDataField> listObjectFullFields(String modelCode){
        String sql = "select a.Model_Code,a.column_Name,a.column_type,a.Access_Type,a.Display_Order,a.input_TYPE," +
            "a.input_hint,a.reference_Type,a.reference_Data,a.Validate_Regex,a.Validate_Info,a.default_Value," +
            "a.Validate_hint,a.filter_type,a.mandatory,a.focus,a.url,a.extend_Options,a.field_height,a.field_width," +
            "a.view_format, b.field_label_name as columnLabel " +
            "from m_model_data_field a join f_meta_column b on a.column_name = b.column_name " +
            "where a.Model_Code = :modelCode";
        return super.listObjectsBySql(sql, CollectionsOpt.createHashMap("modelCode", modelCode));
    }
}
