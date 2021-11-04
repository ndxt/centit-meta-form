package com.centit.metaform.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.metaform.dubbo.api.po.MetaFormModel;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.FieldType;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MetaFormModelDao  Repository.
 * create by scaffold 2016-06-02
 * <p>
 * 通用模块管理null
 */

@Repository
public class MetaFormModelDao extends BaseDaoImpl<MetaFormModel, java.lang.String> {

    public static final Log log = LogFactory.getLog(MetaFormModelDao.class);

    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();

        filterField.put("modelId", CodeBook.EQUAL_HQL_ID);
        filterField.put("tableId", CodeBook.EQUAL_HQL_ID);
        filterField.put("modelComment", CodeBook.EQUAL_HQL_ID);
        filterField.put("modelName", CodeBook.LIKE_HQL_ID);
        filterField.put("modelType", CodeBook.EQUAL_HQL_ID);
        filterField.put("relationId", CodeBook.EQUAL_HQL_ID);
        filterField.put("parentModelCode", CodeBook.EQUAL_HQL_ID);
        filterField.put("displayOrder", CodeBook.EQUAL_HQL_ID);
        filterField.put("lastModifyDate", CodeBook.EQUAL_HQL_ID);
        filterField.put("recorder", CodeBook.EQUAL_HQL_ID);
        filterField.put("databaseCode", CodeBook.EQUAL_HQL_ID);
        return filterField;
    }

    public List<Pair<String, String>> getSubModelPropertiesMap(Long parentTableId, Long childTableId) {
        @SuppressWarnings("unchecked")
        JSONArray columnsMap = DatabaseOptUtils.listObjectsBySqlAsJson(this,
                "select b.parent_column_name as parent ,b.child_column_name as child" +
                        " from F_META_RELATION a join F_META_REL_DETIAL b on (a.relation_id=b.relation_id)" +
                        " where a.parent_table_id = ? and a.child_table_id = ? ",
                new Object[]{parentTableId, childTableId});
        if (columnsMap == null || columnsMap.size() == 0)
            return null;
        List<Pair<String, String>> columnList = new ArrayList<>();
        for (Object columnPair : columnsMap) {
            columnList.add(new ImmutablePair<>(
                    FieldType.mapPropName(StringBaseOpt.objectToString(((JSONObject) columnPair).get("parent"))),
                    FieldType.mapPropName(StringBaseOpt.objectToString(((JSONObject) columnPair).get("child")))));
        }
        return columnList;
    }


    /*public List<MetaFormModel> listFormByTable(String tableId) {
        return listObjects("From MetaFormModel where mdTable.tableId = ?", tableId);
        //return listObjects("From MetaFormModel where tableId = ?", tableId);
    }*/
}
