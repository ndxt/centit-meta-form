package com.centit.metaform.dubbo.adapter;

import com.alibaba.fastjson.JSONArray;
import com.centit.metaform.dubbo.adapter.po.MetaFormModel;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

/**
 * MetaFormModel  Service.
 * create by scaffold 2016-06-02
 * <p>
 * 通用模块管理null
 * @author zhf
 */

public interface MetaFormModelManager {

    void saveNewMetaFormModel(MetaFormModel mtaFormModel);

    void updateMetaFormModel(MetaFormModel mtaFormModel);

    JSONArray listFormModeAsJson(String[] fields, Map<String, Object> filterMap, PageDesc pageDesc);

    int[] batchUpdateOptId(String optId, List<String> modleIds);

    List listModelByOptId(String optId);

    MetaFormModel getObjectById(String modelId);

    void deleteObjectById(String modelId);

    int countMetaFormModels(Map<String,Object> params);

    void updateValidStatus(String modelId,String validType);
}
