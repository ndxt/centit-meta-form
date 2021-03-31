package com.centit.metaform.service;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.metaform.po.MetaFormModelEdit;
import com.centit.support.database.utils.PageDesc;

import java.util.Map;

public interface MetaFormModelEditManager extends BaseEntityManager<MetaFormModelEdit, String> {

    JSONArray listFormModeAsJson(String[] fields, Map<String, Object> filterMap, PageDesc pageDesc);

    void updateMetaFormModelEdit(MetaFormModelEdit metaFormModel);

    void publishMetaFormModel(MetaFormModelEdit metaFormModel);
}
