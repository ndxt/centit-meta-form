package com.centit.metaform.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.centit.metaform.dao.MetaFormTemplateDao;
import com.centit.metaform.po.MetaFormTemplate;
import com.centit.metaform.service.MetaFormTemplateService;
import com.centit.support.database.utils.PageDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * MetaFormTemplate  Service.
 *
 * @Date 2021/1/25
 * 页面模板管理
 */
@Service
public class MetaFormTemplateServiceImpl implements MetaFormTemplateService {

    @Autowired
    MetaFormTemplateDao formTemplateDao;

    @Override
    public JSONArray listFormTemplate(Map<String, Object> filterMap, PageDesc pageDesc) {
        return formTemplateDao.listObjectsAsJson(filterMap, pageDesc);
    }

    @Override
    public MetaFormTemplate getMetaFormTemplate(String id) {
        return formTemplateDao.getObjectById(id);
    }

    @Override
    public void saveMetaFormTemplate(MetaFormTemplate metaFormTemplate) {
        formTemplateDao.mergeObject(metaFormTemplate);
    }

    @Override
    public void deleteMetaFormTemplateById(String id) {
        formTemplateDao.deleteObjectById(id);
    }
}

