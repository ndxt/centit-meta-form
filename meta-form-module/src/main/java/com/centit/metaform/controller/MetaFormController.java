package com.centit.metaform.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.service.MetaFormModelManager;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.compiler.Lexer;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/formaccess")
@Api(value = "自定义表单数据处理", tags = "自定义表单数据处理")
public class MetaFormController extends BaseController {
    protected static final Logger logger = LoggerFactory.getLogger(MetaFormController.class);

    @Autowired
    private MetaFormModelManager metaFormModelManager;

    @Autowired
    private MetaObjectService metaObjectService;

    private int runJSEvent(String js, Map<String, Object> object, String event){
        if(StringUtils.isBlank(js)){
            return 0;
        }
        JSMateObjectEvent jsMateObjectEvent = new JSMateObjectEvent(metaObjectService, js);
        return jsMateObjectEvent.runEvent(event,object);
    }


    @ApiOperation(value = "分页查询表单数据列表")
    @RequestMapping(value = "/{modeId}/list", method = RequestMethod.GET)
    @WrapUpResponseBody
    public PageQueryResult<Object> listObjects(@PathVariable String modeId, PageDesc pageDesc,
                                               String [] fields, HttpServletRequest request) {
        Map<String, Object> params = collectRequestParameters(request);//convertSearchColumn(request);
        MetaFormModel model = metaFormModelManager.getObjectById(modeId);
        String sql = model.getDataFilterSql();

        if(StringUtils.isBlank(sql)) {
            JSONArray ja = metaObjectService.pageQueryObjects(
                    model.getTableId(), params, pageDesc);
            return PageQueryResult.createJSONArrayResult(ja, pageDesc, fields);
        }
        if(StringUtils.equalsIgnoreCase("select",Lexer.getFirstWord(sql))) {
            JSONArray ja = metaObjectService.pageQueryObjects(
                    model.getTableId(), sql, params, pageDesc);
            return PageQueryResult.createJSONArrayResult(ja, pageDesc);
        }
        JSONArray ja = metaObjectService.pageQueryObjects(
                model.getTableId(), params, sql.split(","), pageDesc);
        return PageQueryResult.createJSONArrayResult(ja, pageDesc);
    }

    @ApiOperation(value = "获取一个数据，主键作为参数以key-value形式提交")
    @RequestMapping(value = "/{modeId}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public Map<String, Object> getObject(@PathVariable String modeId,
                                               HttpServletRequest request) {
        Map<String, Object> parameters = collectRequestParameters(request);
        MetaFormModel model = metaFormModelManager.getObjectById(modeId);
        return metaObjectService.getObjectById(model.getTableId(), parameters);
    }

    @ApiOperation(value = "修改表单数据")
    @RequestMapping(value = "/{modeId}", method = RequestMethod.PUT)
    @WrapUpResponseBody
    public ResponseData updateObject(@PathVariable String modeId,
                                            @RequestBody String jsonString) {
        MetaFormModel model = metaFormModelManager.getObjectById(modeId);
        JSONObject object = JSON.parseObject(jsonString);
        if(runJSEvent(model.getExtendOptJs(), object, "beforeUpdate")==0) {
            metaObjectService.updateObject(model.getTableId(), object);
        }
        return ResponseData.makeSuccessResponse();
    }

    @ApiOperation(value = "新增表单数据")
    @RequestMapping(value = "/{modeId}", method = RequestMethod.POST)
    @WrapUpResponseBody
    public ResponseData saveObject(@PathVariable String modeId,
                                          @RequestBody String jsonString) {
        MetaFormModel model = metaFormModelManager.getObjectById(modeId);
        JSONObject object = JSON.parseObject(jsonString);
        if(runJSEvent(model.getExtendOptJs(), object, "beforeSave")==0) {
            metaObjectService.saveObject(model.getTableId(), object);
        }
        return ResponseData.makeSuccessResponse();
    }

    @ApiOperation(value = "删除表单数据")
    @RequestMapping(value = "/{modeId}", method = RequestMethod.DELETE)
    @WrapUpResponseBody
    public ResponseData deleteObject(@PathVariable String modeId,
                                     HttpServletRequest request) {
        Map<String, Object> parameters = collectRequestParameters(request);
        MetaFormModel model = metaFormModelManager.getObjectById(modeId);
        if(runJSEvent(model.getExtendOptJs(), parameters, "beforeDelete")==0) {
            metaObjectService.deleteObject(model.getTableId(), parameters);
        }
        return ResponseData.makeSuccessResponse();
    }

    @ApiOperation(value = "获取一个数据带子表，主键作为参数以key-value形式提交")
    @RequestMapping(value = "/{modeId}/withChildren", method = RequestMethod.GET)
    @WrapUpResponseBody
    public Map<String, Object> getObjectWithChildren(@PathVariable String modeId,
                                         HttpServletRequest request) {
        Map<String, Object> parameters = collectRequestParameters(request);
        MetaFormModel model = metaFormModelManager.getObjectById(modeId);
        return metaObjectService.getObjectWithChildren(model.getTableId(), parameters, 1);
    }

    @ApiOperation(value = "修改表单数据带子表")
    @RequestMapping(value = "/{modeId}/withChildren", method = RequestMethod.PUT)
    @WrapUpResponseBody
    public ResponseData updateObjectWithChildren(@PathVariable String modeId,
                                     @RequestBody String jsonString) {
        MetaFormModel model = metaFormModelManager.getObjectById(modeId);
        JSONObject object = JSON.parseObject(jsonString);
        if(runJSEvent(model.getExtendOptJs(), object, "beforeUpdate")==0) {
            metaObjectService.updateObjectWithChildren(model.getTableId(), object);
        }
        return ResponseData.makeSuccessResponse();
    }

    @ApiOperation(value = "新增表单数据带子表")
    @RequestMapping(value = "/{modeId}/withChildren", method = RequestMethod.POST)
    @WrapUpResponseBody
    public ResponseData saveObjectWithChildren(@PathVariable String modeId,
                                   @RequestBody String jsonString) {
        MetaFormModel model = metaFormModelManager.getObjectById(modeId);
        JSONObject object = JSON.parseObject(jsonString);
        if(runJSEvent(model.getExtendOptJs(), object, "beforeSave")==0) {
            metaObjectService.saveObjectWithChildren(model.getTableId(), object);
        }
        return ResponseData.makeSuccessResponse();
    }

    @ApiOperation(value = "删除表单数据带子表")
    @RequestMapping(value = "/{modeId}/withChildren", method = RequestMethod.DELETE)
    @WrapUpResponseBody
    public ResponseData deleteObjectWithChildren(@PathVariable String modeId,
                                     HttpServletRequest request) {
        Map<String, Object> parameters = collectRequestParameters(request);
        MetaFormModel model = metaFormModelManager.getObjectById(modeId);
        if(runJSEvent(model.getExtendOptJs(), parameters, "beforeDelete")==0) {
            metaObjectService.deleteObjectWithChildren(model.getTableId(), parameters);
        }
        return ResponseData.makeSuccessResponse();
    }

}
