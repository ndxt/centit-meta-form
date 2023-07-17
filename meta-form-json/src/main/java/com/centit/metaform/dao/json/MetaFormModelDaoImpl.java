package com.centit.metaform.dao.json;

import com.alibaba.fastjson2.JSONArray;
import com.centit.metaform.dao.MetaFormModelDao;
import com.centit.metaform.po.MetaFormModel;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * MetaFormModelDao  Repository.
 * create by scaffold 2016-06-02
 * <p>
 * 通用模块管理null
 */

@Repository
public class MetaFormModelDaoImpl implements MetaFormModelDao {

    @Override
    public MetaFormModel getObjectById(Object tableId) {
        return null;
    }

    @Override
    public void saveNewObject(MetaFormModel metaFormModel) {

    }

    @Override
    public int mergeObject(MetaFormModel metaFormModel) {
        return 0;
    }

    @Override
    public int updateObject(MetaFormModel metaFormModel) {
        return 0;
    }

    @Override
    public int deleteObjectById(Object id) {
        return 0;
    }

    @Override
    public int saveObjectReferences(MetaFormModel metaFormModel) {
        return 0;
    }

    @Override
    public List<Pair<String, String>> getSubModelPropertiesMap(Long parentTableId, Long childTableId) {
        return null;
    }

    @Override
    public JSONArray listFormModeAsJson(String[] fields, Map<String, Object> filterMap, PageDesc pageDesc) {
        return null;
    }

    @Override
    public int[] batchUpdateOptId(String optId, List<String> modleIds) {
        return new int[0];
    }

    @Override
    public List<MetaFormModel> listObjectsByProperties(Map<String, Object> filterMap) {
        return null;
    }

    @Override
    public int countObjectByProperties(Map<String, Object> filterMap) {
        return 0;
    }

    @Override
    public int clearTrashStand(String osId) {
        return 0;
    }

    @Override
    public void batchDeleteByIds(String[] modleIds) {

    }

    @Override
    public void updateValidStatus(String modelId, String validType) {

    }
}
