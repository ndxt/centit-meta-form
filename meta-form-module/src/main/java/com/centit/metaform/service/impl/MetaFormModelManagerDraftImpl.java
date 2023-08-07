package com.centit.metaform.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.centit.metaform.dao.MetaFormModelDao;
import com.centit.metaform.dao.MetaFormModelDraftDao;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.po.MetaFormModelDraft;
import com.centit.metaform.service.MetaFormModelDraftManager;
import com.centit.support.database.utils.PageDesc;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("metaFormModelManagerDraftImpl")
public class MetaFormModelManagerDraftImpl implements MetaFormModelDraftManager {

    @Autowired
    private MetaFormModelDraftDao metaFormModelDraftDao;

    @Autowired
    private MetaFormModelDao metaFormModelDao;

    @Override
    @Transactional
    public JSONArray listFormModeAsJson(String[] fields, Map<String, Object> filterMap, PageDesc pageDesc) {
        return metaFormModelDraftDao.listFormModeAsJson(fields, filterMap, pageDesc);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateMetaFormModelDraft(MetaFormModelDraft metaFormModel) {
        metaFormModelDraftDao.mergeObject(metaFormModel);
        metaFormModelDraftDao.saveObjectReferences(metaFormModel);
    }

    @Override
    @Transactional
    public void publishMetaFormModel(MetaFormModelDraft metaFormModelDraft) {
        MetaFormModel metaFormModel = new MetaFormModel();
        BeanUtils.copyProperties(metaFormModelDraft, metaFormModel);
        metaFormModelDao.mergeObject(metaFormModel);
    }

    @Override
    @Transactional
    public MetaFormModelDraft getMetaFormModelDraftById(String modelId) {
        return metaFormModelDraftDao.getObjectById(modelId);
    }

    @Override
    @Transactional
    public void saveMetaFormModelDraft(MetaFormModelDraft metaFormModel) {
        metaFormModelDraftDao.mergeObject(metaFormModel);
    }

    @Override
    @Transactional
    public void deleteMetaFormModelDraftById(String modelId) {
        metaFormModelDraftDao.deleteObjectById(modelId);
    }

    @Override
    @Transactional
    public void deleteMetaFormModelDraftByIdWithMetaFormModel(String modelId) {
        metaFormModelDraftDao.deleteObjectById(modelId);
        metaFormModelDao.deleteObjectById(modelId);
    }

    @Override
    @Transactional
    public int[] batchUpdateOptId(String optId, List<String> modleIds) {
        return metaFormModelDraftDao.batchUpdateOptId(optId, modleIds);
    }

    @Override
    @Transactional
    public void updateValidStatus(String modelId,String validType) {
        metaFormModelDraftDao.updateValidStatus(modelId, validType);
    }

    @Override
    @Transactional
    public void batchDeleteByIds(String[] modleIds) {
        metaFormModelDraftDao.batchDeleteByIds(modleIds);
    }

    @Override
    @Transactional
    public int clearTrashStand(String osId) {
        return  metaFormModelDraftDao.clearTrashStand(osId);
    }
}

