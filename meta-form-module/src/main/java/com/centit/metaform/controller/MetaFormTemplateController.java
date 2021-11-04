package com.centit.metaform.controller;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.metaform.dubbo.api.po.MetaFormTemplate;
import com.centit.metaform.service.MetaFormTemplateService;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * MetaFormTemplate  Controller.
 *
 * @Date 2021/1/25
 * 页面模板管理
 */
@Controller
@RequestMapping("/metaFormTemplate")
@Api(value = "页面模板管理", tags = "页面模板管理")
public class MetaFormTemplateController extends BaseController {

    @Autowired
    private MetaFormTemplateService formTemplateService;

    @ApiOperation(value = "查询所有页面模板列表")
    @GetMapping(value = "/listFormTemplate")
    @WrapUpResponseBody
    public PageQueryResult<Object> listFormTemplate(PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> searchColumn = collectRequestParameters(request);
        JSONArray listObjects = formTemplateService.listFormTemplate(searchColumn, pageDesc);
        return PageQueryResult.createJSONArrayResult(listObjects, pageDesc, MetaFormTemplate.class);
    }

    @ApiOperation(value = "根据id获取页面模板", notes = "根据id获取页面模板")
    @WrapUpResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MetaFormTemplate getMetaFormTemplate(@PathVariable String id) {
        MetaFormTemplate metaFormTemplate = formTemplateService.getMetaFormTemplate(id);
        return metaFormTemplate;
    }

    @ApiOperation(value = "保存页面模板", notes = "保存页面模板")
    @WrapUpResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public MetaFormTemplate saveMetaFormTemplate(@RequestBody MetaFormTemplate metaFormTemplate) {
        formTemplateService.saveMetaFormTemplate(metaFormTemplate);
        return metaFormTemplate;
    }

    @ApiOperation(value = "删除页面模板", notes = "删除页面模板")
    @WrapUpResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteMetaFormTemplateById(@PathVariable String id) {
        formTemplateService.deleteMetaFormTemplateById(id);
    }

}
