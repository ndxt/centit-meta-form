package com.centit.metaform.dao;

import com.alibaba.fastjson2.JSONArray;
import com.centit.metaform.po.MetaFormModel;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

/**
 * MetaFormModelDao  Repository.
 * create by scaffold 2016-06-02
 * <p>
 * 通用模块管理null
 */

public interface MetaFormModelDao {

    MetaFormModel getObjectById(Object modelId);

    void saveNewObject(MetaFormModel metaFormModel);

    int mergeObject(MetaFormModel metaFormModel);

    int updateObject(MetaFormModel metaFormModel);

    int deleteObjectById(Object id);

    int saveObjectReferences(MetaFormModel metaFormModel);

    List<Pair<String, String>> getSubModelPropertiesMap(Long parentTableId, Long childTableId);

    JSONArray listFormModeAsJson(String[] fields, Map<String, Object> filterMap, PageDesc pageDesc);

    int[] batchUpdateOptId(String optId, List<String> modleIds);

    List<MetaFormModel> listObjectsByProperties(Map<String, Object> filterMap);

    int countObjectByProperties(Map<String, Object> filterMap);

    int clearTrashStand(String osId);

    void batchDeleteByIds(String[] modleIds);

    void updateValidStatus(String modelId, String validType);
}
