package com.centit.metaform.dubbo.adapter;

import com.alibaba.fastjson.JSONArray;
import com.centit.metaform.dubbo.adapter.po.MetaFormModelDraft;
import com.centit.support.database.utils.PageDesc;

import java.util.Map;

public interface MetaFormModelDraftManager {

    JSONArray listFormModeAsJson(String[] fields, Map<String, Object> filterMap, PageDesc pageDesc);

    void updateMetaFormModelDraft(MetaFormModelDraft metaFormModel);

    void publishMetaFormModel(MetaFormModelDraft metaFormModel);

    MetaFormModelDraft getMetaFormModelDraftById(String modelId);

    void saveMetaFormModelDraft(MetaFormModelDraft metaFormModel);

    void deleteMetaFormModelDraftById(String modelId);
}
