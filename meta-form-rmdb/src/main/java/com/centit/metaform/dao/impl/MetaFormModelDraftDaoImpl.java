package com.centit.metaform.dao.impl;

import com.alibaba.fastjson2.JSONArray;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.metaform.dao.MetaFormModelDraftDao;
import com.centit.metaform.po.MetaFormModelDraft;
import com.centit.support.database.jsonmaptable.GeneralJsonObjectDao;
import com.centit.support.database.orm.JpaMetadata;
import com.centit.support.database.orm.TableMapInfo;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;


@Repository("metaFormModelDraftDao")
public class MetaFormModelDraftDaoImpl extends BaseDaoImpl<MetaFormModelDraft, String> implements MetaFormModelDraftDao {

    //public static final Logger logger = LoggerFactory.getLogger(MetaFormModelDraftDaoImpl.class);

    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("(like)modelName", "(model_name like :modelName or model_id like :modelName)");
        return  filterField;
    }

    @Override
    public JSONArray listFormModeAsJson(String[] fields, Map<String, Object> filterMap, PageDesc pageDesc) {
        TableMapInfo mapInfo = JpaMetadata.fetchTableMapInfo(MetaFormModelDraft.class);
        List<String> c = new ArrayList<>();
        if (fields != null) {
            c.addAll(Arrays.asList(fields));
        }
        String sql = "select " +
                (c.size() > 0
                        ? GeneralJsonObjectDao.buildPartFieldSql(mapInfo, c, "a", true)
                        : GeneralJsonObjectDao.buildFieldSql(mapInfo, "a", 1)) +
                " from M_META_FORM_MODEL_DRAFT a " +
                " where 1=1 " +
                " [:modelType | and a.MODEL_TYPE = :modelType] " +
                " [:modelId | and a.MODEL_ID = :modelId ] " +
                " [:(like)modelName | and a.model_name like :modelName]" +
                " [ :osId | and a.os_id = :osId ] " +
                " [ :recorder | and a.RECORDER = :recorder ] " +
                " [ :isValid | and a.IS_VALID = :isValid ] " +
                " [:optId | and a.OPT_ID = :optId ] ";
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
        String sql = "UPDATE M_META_FORM_MODEL_DRAFT SET OPT_ID=? ,IS_VALID ='F' WHERE MODEL_ID = ? ";
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
        String sql ="UPDATE m_meta_form_model_draft SET IS_VALID =? WHERE MODEL_ID =? ";
        this.getJdbcTemplate().update(sql, new Object[]{validType, modelId});
    }

    @Override
    public void batchDeleteByIds(String[] modleIds) {
        String delSql ="DELETE FROM m_meta_form_model_draft WHERE MODEL_ID = ? ";
        this.getJdbcTemplate().batchUpdate(delSql, new BatchPreparedStatementSetter(){
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
        String delSql ="DELETE FROM m_meta_form_model_draft WHERE IS_VALID = 'T' AND OS_ID=? ";
        int delCount = DatabaseOptUtils.doExecuteSql(this, delSql, new Object[]{osId});
        return  delCount;
    }
}
