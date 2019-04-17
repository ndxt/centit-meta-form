package com.centit.metaform.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.BaseController;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.service.MetaFormModelManager;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;


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
     * @param response {@link HttpServletResponse}
     * @return {data:[]}
     */
    @ApiOperation(value = "查询所有通用模块")
    @RequestMapping(method = RequestMethod.GET)
    public void list(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
        JSONArray listObjects = metaFormModelMag.listObjectsAsJson(searchColumn, pageDesc);
        JSONArray jsonObjects = metaFormModelMag.addTableNameToList(listObjects);
        SimplePropertyPreFilter simplePropertyPreFilter = null;

        if (null == pageDesc) {
            JsonResultUtils.writeSingleDataJson(jsonObjects, response);
            return;
        }
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, jsonObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);
        if (ArrayUtils.isNotEmpty(field)) {
            simplePropertyPreFilter = new SimplePropertyPreFilter(MetaFormModel.class, field);
            JsonResultUtils.writeResponseDataAsJson(resData, response,simplePropertyPreFilter);
        }
        else{
            JsonResultUtils.writeResponseDataAsJson(resData, response);
        }
    }
    /**
     * 查询单个  通用模块管理

     * @param modelCode  Model_Code
     * @param response    {@link HttpServletResponse}
     * @return {data:{}}
     */
    @ApiOperation(value = "查询单个通用模块")
    @RequestMapping(value = "/{modelCode}", method = {RequestMethod.GET})
    public void getMetaFormModel(@PathVariable String modelCode, HttpServletResponse response) {

        MetaFormModel metaFormModel = metaFormModelMag.getObjectById( modelCode);


        JSONObject modelResult = JSONObject.parseObject(JSONObject.toJSONString(metaFormModel));
        modelResult.put("lastModifyDate",metaFormModel.getLastModifyDate().toString());

        JsonResultUtils.writeSingleDataJson(modelResult, response);
    }

    /**
     * 新增 通用模块管理
     *
     * @param metaFormModel  {@link MetaFormModel}
     * @return
     */
    @ApiOperation(value = "新增通用模块")
    @RequestMapping(method = {RequestMethod.POST})
    public void createMetaFormModel(@RequestBody @Valid MetaFormModel metaFormModel,
             HttpServletRequest request, HttpServletResponse response) {
        MetaFormModel model=new MetaFormModel();
        String usercode = getLoginUserCode(request);
        model.copyNotNullProperty(metaFormModel);
        model.setRecorder(usercode);
        model.setLastModifyDate(new Date());
        metaFormModelMag.saveNewObject(model);


        JsonResultUtils.writeSingleDataJson(model.getModeId(),response);
    }

    /**
     * 删除单个  通用模块管理

     * @param modelCode  Model_Code
     */
    @ApiOperation(value = "删除单个通用模块")
    @RequestMapping(value = "/{modelCode}", method = {RequestMethod.DELETE})
    public void deleteMetaFormModel(@PathVariable String modelCode, HttpServletResponse response) {

        metaFormModelMag.deleteObjectById( modelCode);

        JsonResultUtils.writeSuccessJson(response);
    }

    /**
     * 新增或保存 通用模块管理

     * @param modelCode  Model_Code
     * @param metaFormModel  {@link MetaFormModel}
     * @param response    {@link HttpServletResponse}
     */
    @ApiOperation(value = "新增或保存通用模块")
    @RequestMapping(value = "/{modelCode}", method = {RequestMethod.PUT})
    public void updateMetaFormModel(@PathVariable String modelCode,
            @RequestBody @Valid MetaFormModel metaFormModel, HttpServletResponse response) {
        MetaFormModel dbMetaFormModel = metaFormModelMag.getObjectById( modelCode);
        dbMetaFormModel.copyNotNullProperty(metaFormModel);
        dbMetaFormModel.setLastModifyDate(new Date());
        metaFormModelMag.updateMetaFormModel(dbMetaFormModel);

        JsonResultUtils.writeBlankJson(response);
    }
}
