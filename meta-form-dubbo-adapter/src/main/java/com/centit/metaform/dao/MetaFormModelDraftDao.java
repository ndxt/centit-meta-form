package com.centit.metaform.dao;

import com.alibaba.fastjson2.JSONArray;
import com.centit.metaform.po.MetaFormModelDraft;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

public interface MetaFormModelDraftDao {

    MetaFormModelDraft getObjectById(Object tableId);

    int mergeObject(MetaFormModelDraft metaFormModel);

    int saveObjectReferences(MetaFormModelDraft metaFormModel);

    int deleteObjectById(Object tableId);

    JSONArray listFormModeAsJson(String[] fields, Map<String, Object> filterMap, PageDesc pageDesc);

    int[] batchUpdateOptId(String optId, List<String> modleIds);

    void updateValidStatus(String modelId,String validType);

    void batchDeleteByIds(String[] modleIds);

    int clearTrashStand(String osId);

    JSONArray searchFormModeAsJson(String keyWords, String applicationId, String formType, PageDesc pageDesc);
}
