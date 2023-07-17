package com.centit.metaform.dao.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.metaform.dao.MetaFormModelDao;
import com.centit.metaform.po.MetaFormModel;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.jsonmaptable.GeneralJsonObjectDao;
import com.centit.support.database.orm.JpaMetadata;
import com.centit.support.database.orm.TableMapInfo;
import com.centit.support.database.utils.FieldType;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * MetaFormModelDao  Repository.
 * create by scaffold 2016-06-02
 * <p>
 * 通用模块管理null
 */

@Repository("metaFormModelDao")
public class MetaFormModelDaoImpl extends BaseDaoImpl<MetaFormModel, java.lang.String> implements MetaFormModelDao {

    //public static final Logger logger = LoggerFactory.getLogger(MetaFormModelDaoImpl.class);

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
        filterField.put("(splitforin)optids", "opt_id in (:optids)");
        return filterField;
    }

    @Override
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

    @Override
    public JSONArray listFormModeAsJson(String[] fields, Map<String, Object> filterMap, PageDesc pageDesc) {
        TableMapInfo mapInfo = JpaMetadata.fetchTableMapInfo(MetaFormModel.class);
        List<String> c = new ArrayList<>();
        if (fields != null) {
            c.addAll(Arrays.asList(fields));
        }
        String sql = "select " +
                ((c != null && c.size() > 0)
                        ? GeneralJsonObjectDao.buildPartFieldSql(mapInfo, c, "a", true)
                        : GeneralJsonObjectDao.buildFieldSql(mapInfo, "a", 1)) +
                " from M_META_FORM_MODEL a  " +
                " where 1=1  " +
                " [:modelType | and a.MODEL_TYPE = :modelType] " +
                " [:modelId | and a.MODEL_ID = :modelId ] " +
                " [:(like)modelName | and a.model_name like :modelName]" +
                " [:applicationId | and a.APPLICATION_ID = :applicationId ] " +
                " [ :isValid | and a.IS_VALID = :isValid ] " +
                " [:optId | and a.OPT_ID = :optId ]  [:osId | and a.os_id = :osId ] " +
                " [:(like)apiId | and a.form_template like :apiId ] ";
        String orderBy = GeneralJsonObjectDao.fetchSelfOrderSql(sql, filterMap);
        if (StringUtils.isNotBlank(orderBy)) {
            sql = sql + " order by "
                    + QueryUtils.cleanSqlStatement(orderBy);
        }

        JSONArray listTables = DatabaseOptUtils.listObjectsByParamsDriverSqlAsJson(this, sql, filterMap, pageDesc);
        return listTables;
    }

    @Override
    public int[] batchUpdateOptId(String optId, List<String> modleIds) {
        String sql = "UPDATE M_META_FORM_MODEL SET OPT_ID=?,IS_VALID ='F' WHERE MODEL_ID = ? ";
        int[] metaFormArr = this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, optId);
                ps.setString(2, modleIds.get(i));
            }

            @Override
            public int getBatchSize() {
                return modleIds.size();
            }
        });
        return metaFormArr;
    }


    @Override
    public void updateValidStatus(String modelId, String validType) {
        String sql = "UPDATE m_meta_form_model SET IS_VALID =? WHERE MODEL_ID =? ";
        this.getJdbcTemplate().update(sql, new Object[]{validType, modelId});
    }

    @Override
    public void batchDeleteByIds(String[] modleIds) {
        String delSql = "DELETE FROM m_meta_form_model WHERE MODEL_ID = ? ";
        this.getJdbcTemplate().batchUpdate(delSql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i)
                    throws SQLException {
                ps.setString(1, modleIds[i]);
            }

            public int getBatchSize() {
                return modleIds.length;
            }
        });
    }

    @Override
    public int clearTrashStand(String osId) {
        String delSql = "DELETE FROM m_meta_form_model WHERE IS_VALID = 'T' AND OS_ID = ? ";
        int delCount = DatabaseOptUtils.doExecuteSql(this, delSql, new Object[]{osId});
        return delCount;
    }
}
