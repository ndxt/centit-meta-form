package com.centit.metaform.dao.json;

import com.alibaba.fastjson2.JSONArray;
import com.centit.metaform.dao.MetaFormModelDraftDao;
import com.centit.metaform.po.MetaFormModelDraft;
import com.centit.support.database.utils.PageDesc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class MetaFormModelDraftDaoImpl implements MetaFormModelDraftDao {

    //public static final Logger logger = LoggerFactory.getLogger(MetaFormModelDraftDaoImpl.class);

    @Override
    public MetaFormModelDraft getObjectById(Object tableId) {
        return null;
    }

    @Override
    public int mergeObject(MetaFormModelDraft metaFormModel) {
        return 0;
    }

    @Override
    public int saveObjectReferences(MetaFormModelDraft metaFormModel) {
        return 0;
    }

    @Override
    public int deleteObjectById(Object tableId) {
        return 0;
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
    public void updateValidStatus(String modelId, String validType) {

    }

    @Override
    public void batchDeleteByIds(String[] modleIds) {

    }

    @Override
    public int clearTrashStand(String osId) {
        return 0;
    }
}
