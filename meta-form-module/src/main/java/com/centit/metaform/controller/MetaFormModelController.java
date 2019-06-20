package com.centit.metaform.controller;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.service.MetaFormModelManager;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * MetaFormModel  Controller.
 * create by scaffold 2016-06-02

 * 通用模块管理null
*/


@Controller
@RequestMapping("/metaformmodel")
@Api(value = "自定义表单管理", tags = "自定义表单管理")
public class MetaFormModelController extends BaseController{
    //private static final Log log = LogFactory.getLog(MetaFormModelController.class);

    @Resource
    private MetaFormModelManager metaFormModelMag;


    /**
     * 查询所有   通用模块管理  列表
     *
     * @param field    json中只保存需要的属性名
     * @param request  {@link HttpServletRequest}
     * @return {data:[]}
     */
    @ApiOperation(value = "查询所有通用模块")
    @RequestMapping(method = RequestMethod.GET)
    @WrapUpResponseBody
    public PageQueryResult list(String[] field, PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
        JSONArray listObjects = metaFormModelMag.listObjectsAsJson(searchColumn, pageDesc);
        if (ArrayUtils.isNotEmpty(field)) {
           return PageQueryResult.createJSONArrayResult(listObjects, pageDesc, field, MetaFormModel.class);
        }
        else{
            return PageQueryResult.createJSONArrayResult(listObjects,pageDesc,MetaFormModel.class);
        }
    }
    /**
     * 查询单个  通用模块管理

     * @param modelId  Model_Id
     * @return {data:{}}
     */
    @ApiOperation(value = "查询单个通用模块")
    @RequestMapping(value = "/{modelId}", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public MetaFormModel getMetaFormModel(@PathVariable String modelId) {
        MetaFormModel metaFormModel = metaFormModelMag.getObjectById(modelId);
        return metaFormModel;
    }

    /**
     * 新增 通用模块管理
     *
     * @param metaFormModel  {@link MetaFormModel}
     * @return
     */
    @ApiOperation(value = "新增通用模块")
    @RequestMapping(method = {RequestMethod.POST})
    @WrapUpResponseBody
    public void createMetaFormModel(MetaFormModel metaFormModel,
        HttpServletRequest request, HttpServletResponse response) {
        MetaFormModel model=new MetaFormModel();
        String usercode = WebOptUtils.getCurrentUnitCode(request);
        model.copyNotNullProperty(metaFormModel);
        model.setRecorder(usercode);
        model.setFormTemplate(StringEscapeUtils.unescapeHtml4(model.getFormTemplate()));
        model.setExtendOptJs(StringEscapeUtils.unescapeHtml4(model.getExtendOptJs()));
        metaFormModelMag.saveNewObject(model);
        JsonResultUtils.writeSingleDataJson(model.getModelId(),response);
    }

    /**
     * 删除单个  通用模块管理

     * @param modelId  Model_Id
     */
    @ApiOperation(value = "删除单个通用模块")
    @RequestMapping(value = "/{modelId}", method = {RequestMethod.DELETE})
    @WrapUpResponseBody
    public void deleteMetaFormModel(@PathVariable String modelId) {
        metaFormModelMag.deleteObjectById(modelId);
    }

    /**
     * 新增或保存 通用模块管理

     * @param modelId  Model_Id
     * @param metaFormModel  {@link MetaFormModel}
     */
    @ApiOperation(value = "编辑通用模块")
    @RequestMapping(value = "/{modelId}", method = {RequestMethod.PUT})
    @WrapUpResponseBody
    public void updateMetaFormModel(@PathVariable String modelId, @RequestBody MetaFormModel metaFormModel) {
        metaFormModel.setModelId(modelId);
        metaFormModel.setFormTemplate(StringEscapeUtils.unescapeHtml4(metaFormModel.getFormTemplate()));
        metaFormModel.setExtendOptJs(StringEscapeUtils.unescapeHtml4(metaFormModel.getExtendOptJs()));
        metaFormModelMag.updateMetaFormModel(metaFormModel);
    }

    @ApiOperation(value = "修改模板内容")
    @RequestMapping(value = "/{modelId}/template", method = {RequestMethod.PUT})
    @WrapUpResponseBody
    public void updateFormTemplate(@PathVariable String modelId,
                                    @RequestBody String formTemplate) {
        MetaFormModel metaFormModel = metaFormModelMag.getObjectById(modelId);
        metaFormModel.setFormTemplate(StringEscapeUtils.unescapeHtml4(formTemplate));
        metaFormModelMag.updateMetaFormModel(metaFormModel);
    }

    @ApiOperation(value = "修改模板事件操作")
    @RequestMapping(value = "/{modelId}/optjs", method = {RequestMethod.PUT})
    @WrapUpResponseBody
    public void updateFormOptJs(@PathVariable String modelId,
                                   @RequestBody String formOptjs) {
        MetaFormModel metaFormModel = metaFormModelMag.getObjectById(modelId);
        metaFormModel.setExtendOptJs(StringEscapeUtils.unescapeHtml4(formOptjs));
        metaFormModelMag.updateMetaFormModel(metaFormModel);
    }

    @ApiOperation(value = "删除模板事件操作")
    @RequestMapping(value = "/{modelId}/optjs", method = {RequestMethod.DELETE})
    @WrapUpResponseBody
    public void deleteFormOptJs(@PathVariable String modelId) {
        metaFormModelMag.deleteFormOptJs(modelId);
    }
}
