package com.centit.metaform.service;

import com.alibaba.fastjson.JSONArray;
import com.centit.metaform.dubbo.api.po.MetaFormTemplate;
import com.centit.support.database.utils.PageDesc;

import java.util.Map;

/**
 * MetaFormTemplate  Service.
 *
 * @Date 2021/1/25
 * 页面模板管理
 */
public interface MetaFormTemplateService {

    /**
     * 获取所有的页面模板
     *
     * @param filterMap 过滤条件
     * @param pageDesc  分页描述
     * @return
     */
    JSONArray listFormTemplate(Map<String, Object> filterMap, PageDesc pageDesc);

    /**
     * 根据id获取页面模板
     *
     * @param id
     * @return
     */
    MetaFormTemplate getMetaFormTemplate(String id);

    void saveMetaFormTemplate(MetaFormTemplate metaFormTemplate);

    void deleteMetaFormTemplateById(String id);
}
