package com.centit.metaform.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.model.basedata.IOptInfo;
import com.centit.metaform.dao.MetaFormModelDao;
import com.centit.metaform.adapter.service.MetaFormModelManager;
import com.centit.metaform.adapter.po.MetaFormModel;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.jsonmaptable.GeneralJsonObjectDao;
import com.centit.support.database.orm.JpaMetadata;
import com.centit.support.database.orm.TableMapInfo;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MetaFormModel  Service.
 * create by scaffold 2016-06-02
 * <p>
 * 通用模块管理null
 *
 * @author zhf
 */
@Service("metaFormModelManagerImpl")
public class MetaFormModelManagerImpl
        implements MetaFormModelManager {

    public static final Log log = LogFactory.getLog(MetaFormModelManager.class);

    @Autowired
    private MetaFormModelDao metaFormModelDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveNewMetaFormModel(MetaFormModel metaFormModel) {
        metaFormModelDao.saveNewObject(metaFormModel);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateMetaFormModel(MetaFormModel metaFormModel) {
        metaFormModelDao.updateObject(metaFormModel);
        metaFormModelDao.saveObjectReferences(metaFormModel);
    }

    @Override
    @Transactional
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

        JSONArray listTables = DatabaseOptUtils.listObjectsByParamsDriverSqlAsJson(metaFormModelDao, sql, filterMap, pageDesc);
        return listTables;
    }


    @Override
    public int[] batchUpdateOptId(String optId, List<String> modleIds) {
        String sql = "UPDATE M_META_FORM_MODEL SET OPT_ID=?,IS_VALID ='F' WHERE MODEL_ID = ? ";
        int[] metaFormArr = metaFormModelDao.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
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
    public List listModelByOptId(String optId) {
        optId = getOptIdWithCommon(optId);
        List<MetaFormModel> metaFormModelList = metaFormModelDao.listObjectsByProperties(CollectionsOpt.createHashMap("optids", optId));
        return metaFormModelList.stream().map(metaFormModel -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("modelId", metaFormModel.getModelId());
            map.put("modelName", metaFormModel.getModelName());
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public MetaFormModel getObjectById(String modelId) {
        return metaFormModelDao.getObjectById(modelId);
    }

    @Override
    public void deleteObjectById(String modelId) {
        metaFormModelDao.deleteObjectById(modelId);
    }

    @Override
    public int countMetaFormModels(Map<String, Object> params) {
        return metaFormModelDao.countObjectByProperties(params);
    }

    @Override
    public void updateValidStatus(String modelId, String validType) {
        String sql = "UPDATE m_meta_form_model SET IS_VALID =? WHERE MODEL_ID =? ";
        metaFormModelDao.getJdbcTemplate().update(sql, new Object[]{validType, modelId});
    }

    @Override
    public void batchDeleteByIds(String[] modleIds) {
        String delSql = "DELETE FROM m_meta_form_model WHERE MODEL_ID = ? ";
        metaFormModelDao.getJdbcTemplate().batchUpdate(delSql, new BatchPreparedStatementSetter() {
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
        int delCount = DatabaseOptUtils.doExecuteSql(metaFormModelDao, delSql, new Object[]{osId});
        return delCount;
    }

    /**
     * 根据optId获取包含通用模板的optId字符串
     *
     * @param optId
     * @return
     */
    private String getOptIdWithCommon(String optId) {
        String topUnit = WebOptUtils.getCurrentTopUnit(RequestThreadLocal.getLocalThreadWrapperRequest());
        IOptInfo commonOptInfo = CodeRepositoryUtil.getCommonOptId(topUnit, optId);
        if (commonOptInfo != null) {
            String commonOptId = commonOptInfo.getOptId();
            return StringBaseOpt.concat(optId, ",", commonOptId);
        }
        return optId;
    }
}

