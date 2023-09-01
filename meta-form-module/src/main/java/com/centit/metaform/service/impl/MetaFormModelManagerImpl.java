package com.centit.metaform.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.framework.model.basedata.OptInfo;
import com.centit.metaform.dao.MetaFormModelDao;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.service.MetaFormModelManager;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.PageDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MetaFormModel  Service.
 * create by scaffold 2016-06-02
 * <p>
 * 通用模块管理null
 *
 * @author zhf
 */
@Service("metaFormModelManagerImpl")
public class MetaFormModelManagerImpl
        implements MetaFormModelManager {

    @Autowired
    private MetaFormModelDao metaFormModelDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveNewMetaFormModel(MetaFormModel metaFormModel) {
        metaFormModelDao.saveNewObject(metaFormModel);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateMetaFormModel(MetaFormModel metaFormModel) {
        metaFormModelDao.updateObject(metaFormModel);
        metaFormModelDao.saveObjectReferences(metaFormModel);
    }

    @Override
    @Transactional
    public JSONArray listFormModeAsJson(String[] fields, Map<String, Object> filterMap, PageDesc pageDesc) {
        return metaFormModelDao.listFormModeAsJson(fields, filterMap, pageDesc);
    }


    @Override
    public int[] batchUpdateOptId(String optId, List<String> modleIds) {
        return metaFormModelDao.batchUpdateOptId(optId, modleIds);
    }


    @Override
    public List listModelByOptId(String optId) {
        optId = getOptIdWithCommon(optId);
        List<MetaFormModel> metaFormModelList = metaFormModelDao.listObjectsByProperties(CollectionsOpt.createHashMap("optids", optId));
        return metaFormModelList.stream().map(metaFormModel -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("modelId", metaFormModel.getModelId());
            map.put("modelName", metaFormModel.getModelName());
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public MetaFormModel getObjectById(String modelId) {
        return metaFormModelDao.getObjectById(modelId);
    }

    @Override
    public void deleteObjectById(String modelId) {
        metaFormModelDao.deleteObjectById(modelId);
    }

    @Override
    public int countMetaFormModels(Map<String, Object> params) {
        return metaFormModelDao.countObjectByProperties(params);
    }

    @Override
    public void updateValidStatus(String modelId, String validType) {
        metaFormModelDao.updateValidStatus(modelId, validType);
    }

    @Override
    public void batchDeleteByIds(String[] modleIds) {
        metaFormModelDao.batchDeleteByIds(modleIds);
    }

    @Override
    public int clearTrashStand(String osId) {
        return metaFormModelDao.clearTrashStand(osId);
    }

    /**
     * 根据optId获取包含通用模板的optId字符串
     *
     * @param optId
     * @return
     */
    private String getOptIdWithCommon(String optId) {
        String topUnit = WebOptUtils.getCurrentTopUnit(RequestThreadLocal.getLocalThreadWrapperRequest());
        OptInfo commonOptInfo = CodeRepositoryUtil.getCommonOptId(topUnit, optId);
        if (commonOptInfo != null) {
            String commonOptId = commonOptInfo.getOptId();
            return StringBaseOpt.concat(optId, ",", commonOptId);
        }
        return optId;
    }
}

