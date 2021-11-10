package com.centit.metaform.dubbo.adapter;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.metaform.dubbo.adapter.po.MetaFormModel;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

/**
 * MetaFormModel  Service.
 * create by scaffold 2016-06-02
 * <p>
 * 通用模块管理null
 */

public interface MetaFormModelManager extends BaseEntityManager<MetaFormModel, String> {

    JSONArray listMetaFormModelsAsJson(String[] fields, Map<String, Object> filterMap, PageDesc pageDesc);

    void saveNewMetaFormModel(MetaFormModel mtaFormModel);

    void updateMetaFormModel(MetaFormModel mtaFormModel);

    JSONArray addTableNameToList(JSONArray listObjects);

    void deleteFormOptJs(String modelId);

    JSONArray listFormModeAsJson(String[] fields, Map<String, Object> filterMap, PageDesc pageDesc);

    MetaFormModel getObjectByIdAndFile(String filePath, String modelId);

    int[] batchUpdateOptId(String optId, List<String> modleIds);
}
