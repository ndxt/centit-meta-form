package com.centit.metaform.service;

import com.alibaba.fastjson2.JSONArray;
import com.centit.metaform.po.MetaFormModelDraft;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

public interface MetaFormModelDraftManager {

    JSONArray listFormModeAsJson(String[] fields, Map<String, Object> filterMap, PageDesc pageDesc);

    void updateMetaFormModelDraft(MetaFormModelDraft metaFormModel);

    void publishMetaFormModel(MetaFormModelDraft metaFormModel);

    MetaFormModelDraft getMetaFormModelDraftById(String modelId);

    void saveMetaFormModelDraft(MetaFormModelDraft metaFormModel);

    void deleteMetaFormModelDraftById(String modelId);

    void deleteMetaFormModelDraftByIdWithMetaFormModel(String modelId);

    int[] batchUpdateOptId(String optId, List<String> modleIds);

    void updateValidStatus(String modelId,String validType);

    void batchDeleteByIds(String[] modleIds);

    int clearTrashStand(String osId);
}
