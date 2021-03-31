package com.centit.metaform.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import com.centit.metaform.dao.MetaFormModelDao;
import com.centit.metaform.dao.MetaFormModelEditDao;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.po.MetaFormModelEdit;
import com.centit.metaform.service.MetaFormModelEditManager;
import com.centit.support.database.jsonmaptable.GeneralJsonObjectDao;
import com.centit.support.database.orm.JpaMetadata;
import com.centit.support.database.orm.TableMapInfo;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class MetaFormModelManagerEditImpl extends BaseEntityManagerImpl<MetaFormModelEdit, String, MetaFormModelEditDao>
        implements MetaFormModelEditManager {

    @Autowired
    private MetaFormModelEditDao metaFormModelEditDao;

    @Autowired
    private MetaFormModelDao metaFormModelDao;

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
                ",b.TABLE_NAME,b.TABLE_LABEL_NAME, form_state " +
                " from M_META_FORM_MODEL_EDIT a left join F_MD_TABLE b on a.table_id=b.table_id " +
                " where 1=1 [:dataBaseCode| and b.DATABASE_CODE = :dataBaseCode ] " +
                " [:tableId | and a.table_id = :tableId] " +
                " [:modelType | and a.MODEL_TYPE = :modelType] " +
                " [:flowOptType | and b.WORKFLOW_OPT_TYPE = :flowOptType ] " +
                " [allFlowOpt | and b.WORKFLOW_OPT_TYPE <> '0' ] " +
                " [:modelId | and a.MODEL_ID = :modelId ] " +
                " [:(like)modelName | and a.model_name like :modelName]" +
                " [:applicationId | and a.APPLICATION_ID = :applicationId ] " +
                " [:ownGroup | and a.own_group = :ownGroup ] ";
        String orderBy = GeneralJsonObjectDao.fetchSelfOrderSql(sql, filterMap);
        if (StringUtils.isNotBlank(orderBy)) {
            sql = sql + " order by "
                    + QueryUtils.cleanSqlStatement(orderBy);
        }

        JSONArray listTables = DatabaseOptUtils.listObjectsByParamsDriverSqlAsJson(metaFormModelEditDao, sql, filterMap, pageDesc);
        return listTables;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateMetaFormModelEdit(MetaFormModelEdit metaFormModel) {
        metaFormModelEditDao.mergeObject(metaFormModel);
        metaFormModelEditDao.saveObjectReferences(metaFormModel);
    }

    @Override
    public void publishMetaFormModel(MetaFormModelEdit metaFormModelEdit) {
        MetaFormModel metaFormModel = new MetaFormModel();
        BeanUtils.copyProperties(metaFormModelEdit, metaFormModel);
        metaFormModelDao.mergeObject(metaFormModel);
    }


}

