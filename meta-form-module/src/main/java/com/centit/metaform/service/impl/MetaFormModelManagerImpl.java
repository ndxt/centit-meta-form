package com.centit.metaform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import com.centit.framework.model.basedata.IOptInfo;
import com.centit.metaform.dao.MetaFormModelDao;
import com.centit.metaform.dubbo.adapter.MetaFormModelManager;
import com.centit.metaform.dubbo.adapter.po.MetaFormModel;
import com.centit.product.metadata.dao.MetaTableDao;
import com.centit.product.metadata.po.MetaTable;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.jsonmaptable.GeneralJsonObjectDao;
import com.centit.support.database.orm.JpaMetadata;
import com.centit.support.database.orm.TableMapInfo;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryUtils;
import com.centit.support.file.FileIOOpt;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MetaFormModel  Service.
 * create by scaffold 2016-06-02
 * <p>
 * 通用模块管理null
 */
@Service("metaFormModelManagerImpl")
public class MetaFormModelManagerImpl
        extends BaseEntityManagerImpl<MetaFormModel, java.lang.String, MetaFormModelDao>
        implements BaseEntityManager<MetaFormModel, String>, MetaFormModelManager {

    public static final Log log = LogFactory.getLog(MetaFormModelManager.class);

    @Autowired
    private MetaFormModelDao metaFormModelDao;

    @Autowired
    private MetaTableDao metaTableDao;


    @Autowired
    public void setMetaFormModelDao(MetaFormModelDao baseDao) {
        this.metaFormModelDao = baseDao;
        super.setBaseDao(this.metaFormModelDao);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public JSONArray listMetaFormModelsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc) {

        JSONArray resultArray =
                baseDao.listObjectsAsJson(filterMap, pageDesc);
        return resultArray;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveNewMetaFormModel(MetaFormModel metaFormModel) {
        super.saveNewObject(metaFormModel);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateMetaFormModel(MetaFormModel metaFormModel) {
        metaFormModelDao.updateObject(metaFormModel);
        metaFormModelDao.saveObjectReferences(metaFormModel);
    }

    @Override
    public JSONArray addTableNameToList(JSONArray listObjects) {
        List<MetaTable> tableObjects = metaTableDao.listObjects();
        if (listObjects != null && listObjects.size() > 0) {
            for (int i = 0; i < listObjects.size(); i++) {
                JSONObject tempObj = listObjects.getJSONObject(i);
                Long tableId = tempObj.getLong("tableId");
                if (tableObjects != null && tableObjects.size() > 0) {
                    for (int j = 0; j < tableObjects.size(); j++) {
                        if (tableId != null && tableId.equals(tableObjects.get(j).getTableId())) {
                            tempObj.put("tableLabelName", tableObjects.get(j).getTableLabelName());
                            break;
                        }
                    }
                }
            }
        }
        return listObjects;
    }

    @Override
    @Transactional
    public void deleteFormOptJs(String modelId) {
        DatabaseOptUtils.doExecuteSql(metaFormModelDao,
                "update M_META_FORM_MODEL set EXTEND_OPT_JS = null where MODEL_ID = ?",
                new Object[]{modelId});
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
                ",b.TABLE_NAME,b.TABLE_LABEL_NAME " +
                " from M_META_FORM_MODEL a left join F_MD_TABLE b on a.table_id=b.table_id " +
                " where 1=1 [:dataBaseCode| and b.DATABASE_CODE = :dataBaseCode ] " +
                " [:tableId | and a.table_id = :tableId] " +
                " [:modelType | and a.MODEL_TYPE = :modelType] " +
                " [:flowOptType | and b.WORKFLOW_OPT_TYPE = :flowOptType ] " +
                " [allFlowOpt | and b.WORKFLOW_OPT_TYPE <> '0' ] " +
                " [:modelId | and a.MODEL_ID = :modelId ] " +
                " [:(like)modelName | and a.model_name like :modelName]" +
                " [:applicationId | and a.APPLICATION_ID = :applicationId ] " +
                " [:optId | and a.OPT_ID = :optId ]  [:osId | and a.os_id = :osId ] ";
        String orderBy = GeneralJsonObjectDao.fetchSelfOrderSql(sql, filterMap);
        if (StringUtils.isNotBlank(orderBy)) {
            sql = sql + " order by "
                    + QueryUtils.cleanSqlStatement(orderBy);
        }

        JSONArray listTables = DatabaseOptUtils.listObjectsByParamsDriverSqlAsJson(metaFormModelDao, sql, filterMap, pageDesc);
        return listTables;
    }

    @Override
    public MetaFormModel getObjectByIdAndFile(String filePath, String modelId) {
        try {
            String metaModel = FileIOOpt.readStringFromFile(filePath + File.separator + modelId + ".json",
                    "UTF-8");
            return JSON.parseObject(metaModel, MetaFormModel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int[] batchUpdateOptId(String optId, List<String> modleIds) {
        String sql="UPDATE M_META_FORM_MODEL SET OPT_ID=? WHERE MODEL_ID = ? ";
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
        List<MetaFormModel> metaFormModelList = metaFormModelDao.listObjects(CollectionsOpt.createHashMap("optids", optId));
        return metaFormModelList.stream().map(metaFormModel -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("modelId", metaFormModel.getModelId());
            map.put("modelName", metaFormModel.getModelName());
            return map;
        }).collect(Collectors.toList());
    }

    /**
     * 根据optId获取包含通用模板的optId字符串
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

