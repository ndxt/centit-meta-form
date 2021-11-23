package com.centit.metaform.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.metaform.dao.MetaFormModelDao;
import com.centit.metaform.dao.MetaFormModelDraftDao;
import com.centit.metaform.dubbo.adapter.MetaFormModelDraftManager;
import com.centit.metaform.dubbo.adapter.po.MetaFormModel;
import com.centit.metaform.dubbo.adapter.po.MetaFormModelDraft;
import com.centit.support.database.jsonmaptable.GeneralJsonObjectDao;
import com.centit.support.database.orm.JpaMetadata;
import com.centit.support.database.orm.TableMapInfo;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service("metaFormModelManagerDraftImpl")
public class MetaFormModelManagerDraftImpl implements MetaFormModelDraftManager {

    @Autowired
    private MetaFormModelDraftDao metaFormModelDraftDao;

    @Autowired
    private MetaFormModelDao metaFormModelDao;

    @Override
    @Transactional
    public JSONArray listFormModeAsJson(String[] fields, Map<String, Object> filterMap, PageDesc pageDesc) {
        TableMapInfo mapInfo = JpaMetadata.fetchTableMapInfo(MetaFormModelDraft.class);
        List<String> c = new ArrayList<>();
        if (fields != null) {
            c.addAll(Arrays.asList(fields));
        }
        String sql = "select " +
                ((c != null && c.size() > 0)
                        ? GeneralJsonObjectDao.buildPartFieldSql(mapInfo, c, "a", true)
                        : GeneralJsonObjectDao.buildFieldSql(mapInfo, "a", 1)) +
                ",b.TABLE_NAME,b.TABLE_LABEL_NAME " +
                " from M_META_FORM_MODEL_DRAFT a left join F_MD_TABLE b on a.table_id=b.table_id " +
                " where 1=1 [:dataBaseCode| and b.DATABASE_CODE = :dataBaseCode ] " +
                " [:tableId | and a.table_id = :tableId] " +
                " [:modelType | and a.MODEL_TYPE = :modelType] " +
                " [:flowOptType | and b.WORKFLOW_OPT_TYPE = :flowOptType ] " +
                " [allFlowOpt | and b.WORKFLOW_OPT_TYPE <> '0' ] " +
                " [:modelId | and a.MODEL_ID = :modelId ] " +
                " [:(like)modelName | and a.model_name like :modelName]" +
                " [:applicationId | and a.APPLICATION_ID = :applicationId ] " +
                " [:optId | and a.OPT_ID = :optId ] ";
        String orderBy = GeneralJsonObjectDao.fetchSelfOrderSql(sql, filterMap);
        if (StringUtils.isNotBlank(orderBy)) {
            sql = sql + " order by "
                    + QueryUtils.cleanSqlStatement(orderBy);
        }

        JSONArray listTables = DatabaseOptUtils.listObjectsByParamsDriverSqlAsJson(metaFormModelDraftDao, sql, filterMap, pageDesc);
        return listTables;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateMetaFormModelDraft(MetaFormModelDraft metaFormModel) {
        metaFormModelDraftDao.mergeObject(metaFormModel);
        metaFormModelDraftDao.saveObjectReferences(metaFormModel);
    }

    @Override
    public void publishMetaFormModel(MetaFormModelDraft metaFormModelDraft) {
        MetaFormModel metaFormModel = new MetaFormModel();
        BeanUtils.copyProperties(metaFormModelDraft, metaFormModel);
        metaFormModelDao.mergeObject(metaFormModel);
    }

    @Override
    public MetaFormModelDraft getMetaFormModelDraftById(String modelId) {
        return metaFormModelDraftDao.getObjectById(modelId);
    }

    @Override
    public void saveMetaFormModelDraft(MetaFormModelDraft metaFormModel) {
        metaFormModelDraftDao.mergeObject(metaFormModel);
    }

    @Override
    public void deleteMetaFormModelDraftById(String modelId) {
        metaFormModelDraftDao.deleteObjectById(modelId);
    }

    @Override
    public int[] batchUpdateOptId(String optId, List<String> modleIds) {
        String sql="UPDATE M_META_FORM_MODEL_DRAFT SET OPT_ID=? WHERE MODEL_ID = ? ";
        int[] metaFormArr = metaFormModelDraftDao.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
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


}

