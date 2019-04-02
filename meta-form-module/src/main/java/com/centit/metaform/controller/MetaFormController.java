package com.centit.metaform.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.OptionItem;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.BaseController;
import com.centit.metaform.dao.MetaFormModelDao;
import com.centit.metaform.formaccess.MetaFormDefine;
import com.centit.metaform.formaccess.ModelFormService;
import com.centit.metaform.formaccess.ModelRuntimeContext;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.po.ModelDataField;
import com.centit.metaform.service.MetaFormModelManager;
import com.centit.metaform.service.ModelDataFieldManager;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/formaccess")
@Api(value = "自定义表单", tags = "自定义表单")
public class MetaFormController extends BaseController {
    protected static final Logger logger = LoggerFactory.getLogger(MetaFormController.class);

    @Resource(name = "modelFormService")
    private ModelFormService modelFormService;

    @Resource
    private MetaFormModelManager modelManager;

    @Resource
    private ModelDataFieldManager modelDataFieldManager;

    /**
     * 作为主表的查看列表
     */
    @ApiOperation(value = "查询主表列表")
    @RequestMapping(value = "/{modelCode}/list", method = RequestMethod.GET)
    public void list(@PathVariable String modelCode, boolean noMeta, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = collectRequestParameters(request);//convertSearchColumn(request);

        ModelRuntimeContext rc = modelFormService.createRuntimeContext(modelCode);
        rc.setCurrentUserDetails(this.getLoginUser(request));

        //默认值作为默认搜索条件
        Set<ModelDataField> fields = rc.getMetaFormModel().getModelDataFields();
        for(ModelDataField field : fields){
            if(StringUtils.isNotBlank(field.getDefaultValue())){
                searchColumn.put(field.getPropertyName(), field.getDefaultValue());
            }
        }

        ResponseMapData resData = new ResponseMapData();
        //ListViewDefine metaData = modelFormService.createListViewModel(rc);
        MetaFormDefine metaData = modelFormService.createFormDefine(rc, "view");
        JSONArray objs = modelFormService.listObjectsByFilter(rc, searchColumn, pageDesc);
        resData.addResponseData(OBJLIST, metaData.transObjectsRefranceData(objs));
        resData.addResponseData(PAGE_DESC, pageDesc);
        rc.close();
        if (!noMeta) {
            resData.addResponseData("formModel", modelFormService.createListViewModel(rc));
        }
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 查看模块主表的详细信息
     */
    @ApiOperation(value = "查看模块主表的详细信息")
    @RequestMapping(value = "/{modelCode}/view", method = RequestMethod.GET)
    public void view(@PathVariable String modelCode, boolean noMeta, HttpServletRequest request, HttpServletResponse response) {
        ModelRuntimeContext rc = modelFormService.createRuntimeContext(modelCode);
        Map<String, Object> jo = rc.fetchPkFromRequest(request);
        ResponseMapData resData = new ResponseMapData();

        JSONObject obj = modelFormService.getObjectByProperties(rc, jo);
        try {
            if (obj != null) {
                Map<String, Object> refField = modelFormService.getModelReferenceFields(rc, obj);
                obj.putAll(refField);
            }
        } catch (SQLException e) {
        }

        MetaFormDefine metaData = modelFormService.createFormDefine(rc, "view");
        resData.addResponseData("obj", metaData.transObjectRefranceData(obj));
        rc.close();
        if (!noMeta) {
            metaData.updateReadOnlyRefrenceField();
            resData.addResponseData("formModel", metaData);
        }

        JSONArray subModels = modelFormService.listSubModelCode(modelCode);
        resData.addResponseData("subModelCode", subModels);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @ApiOperation(value = "查看信息列表")
    @RequestMapping(value = "/{modelCode}/viewList", method = RequestMethod.GET)
    public void viewList(@PathVariable String modelCode, boolean noMeta, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);

        ModelRuntimeContext subRc = modelFormService.createRuntimeContext(modelCode);
        JSONArray subObjs = null;
        try {
            subObjs = modelFormService.listSubModelObjectsByFilter(subRc, searchColumn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        MetaFormDefine subMetaData = modelFormService.createFormDefine(subRc, "list");
        MetaFormDefine metaData = modelFormService.createListViewModel(subRc);

        metaData.setFields(subMetaData.getFields());

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, subObjs);
        resData.addResponseData("formModel", metaData);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 根据父模块code查询所有子模块code
     */
    @ApiOperation(value = "根据父模块code查询所有子模块code")
    @RequestMapping(value = "/{modelCode}/listSubModel", method = RequestMethod.GET)
    public void listSubModelCode(@PathVariable String modelCode, HttpServletRequest request, HttpServletResponse response) {
        JSONArray subModels = modelFormService.listSubModelCode(modelCode);
        ResponseMapData resData = new ResponseMapData();

        resData.addResponseData("subModelCode", subModels);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 获取主模块和所有子模块的数据
     */
    @ApiOperation(value = "获取主模块和所有子模块的数据")
    @RequestMapping(value = "/{modelCode}/viewAll", method = RequestMethod.GET)
    public void viewAll(@PathVariable String modelCode, boolean noMeta, HttpServletRequest request, HttpServletResponse response) {
        ModelRuntimeContext rc = modelFormService.createRuntimeContext(modelCode);

        Map<String, Object> searchColumn = convertSearchColumn(request);

        Map<String, Object> jo = new HashMap<>();
        jo.put(((String[]) searchColumn.get("primaryKey"))[0], ((String[]) (searchColumn.get("primaryValue")))[0]);

        ResponseMapData resData = new ResponseMapData();

        JSONObject obj = modelFormService.getObjectByProperties(rc, jo);
        try {
            if (obj != null) {
                Map<String, Object> refField = modelFormService.getModelReferenceFields(rc, obj);
                obj.putAll(refField);
            }
        } catch (SQLException e) {
        }

        MetaFormDefine metaData = modelFormService.createFormDefine(rc, "view");
        resData.addResponseData("obj", metaData.transObjectRefranceData(obj));
        rc.close();
        if (!noMeta) {
            metaData.updateReadOnlyRefrenceField();
            resData.addResponseData("formModel", metaData);
        }

        List<MetaFormModel> subModel = modelFormService.listSubModel(modelCode);
        if (subModel != null && subModel.size() > 0) {
            JSONArray allSubObjs = new JSONArray();
            JSONArray allSubFields = new JSONArray();
            for (int i = 0; i < subModel.size(); i++) {
                ModelRuntimeContext subRc = modelFormService.createRuntimeContext(subModel.get(i).getModelCode());
                JSONArray subObjs = null;
                try {
                    subObjs = modelFormService.listSubModelObjectsByFilter(subRc, obj);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                allSubObjs.add(subObjs);

                MetaFormDefine subMetaData = modelFormService.createFormDefine(subRc, "list");
                allSubFields.add(subMetaData);
            }
            resData.addResponseData("subObjs", allSubObjs);
            resData.addResponseData("subFields", allSubFields);
        }

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @ApiOperation(value = "meta")
    @RequestMapping(value = "/{modelCode}/meta/{metaType}", method = RequestMethod.GET)
    public void meta(@PathVariable String modelCode, @PathVariable String metaType,
                     HttpServletRequest request, HttpServletResponse response) {
        ModelRuntimeContext rc = modelFormService.createRuntimeContext(modelCode);
        if ("list".equals(metaType)) {
            JsonResultUtils.writeSingleDataJson(modelFormService.createListViewModel(rc), response);
        } else {
            MetaFormDefine metaData = modelFormService.createFormDefine(rc, metaType);
            metaData.updateReadOnlyRefrenceField();
            JsonResultUtils.writeSingleDataJson(metaData, response);
        }
        rc.close();
    }

    /**
     * 创建表单
     *
     * @param modelCode 模块代码
     * @param noMeta 不需要表单元数据 开关
     * @param request 请求信息
     * @param response 返回信息
     */
    @ApiOperation(value = "创建表单")
    @RequestMapping(value = "/{modelCode}/create", method = RequestMethod.GET)
    public void create(@PathVariable String modelCode,
                       boolean noMeta, HttpServletRequest request, HttpServletResponse response) {
        ModelRuntimeContext rc = modelFormService.createRuntimeContext(modelCode);
        try {
            ResponseMapData resData = new ResponseMapData();
            JSONObject obj = modelFormService.createInitialObject(rc);
            if (obj != null) {
                Map<String, Object> refField = modelFormService.getModelReferenceFields(rc, obj);
                obj.putAll(refField);
            }

            MetaFormDefine metaData = modelFormService.createFormDefine(rc, "create");
            resData.addResponseData("obj", metaData.transObjectRefranceData(obj));

            rc.close();
            if (!noMeta) {
                //这个是判断是否为只读表单，如果是只读表单初始化表单内容
                metaData.updateReadOnlyRefrenceField();

                JSONObject metaJson = JSONObject.parseObject(JSONObject.toJSONString(metaData));
                JSONArray metaFieldsJson = metaJson.getJSONArray("fields");
                if (metaFieldsJson != null && metaFieldsJson.size() > 0) {
                    for (int i = 0; i < metaFieldsJson.size(); i++) {
                        JSONObject metaObjJson = metaFieldsJson.getJSONObject(i);
                        JSONObject templateOptions = metaObjJson.getJSONObject("templateOptions");

                        String inputType = metaObjJson.getString("type");
                        if (StringUtils.isNotBlank(inputType) && inputType.equals("select")) {
                            templateOptions.put("type", "select");
                        } else {
                            templateOptions.put("type", inputType);
                            metaObjJson.put("type", "input");
                        }
                    }
                }

                resData.addResponseData("formModel", metaJson);
            }
            JsonResultUtils.writeResponseDataAsJson(resData, response);
        } catch (SQLException e) {
            JsonResultUtils.writeErrorMessageJson(e.getMessage(), response);
            rc.rollbackAndClose();
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "createPk")
    @RequestMapping(value = "/{modelCode}/createpk", method = RequestMethod.GET)
    public void createPk(@PathVariable String modelCode,
                         HttpServletRequest request, HttpServletResponse response) {
        ModelRuntimeContext rc = modelFormService.createRuntimeContext(modelCode);
        try {
            ResponseMapData resData = new ResponseMapData();
            resData.addResponseData("pk", modelFormService.createNewPk(rc));
            rc.close();
            JsonResultUtils.writeSuccessJson(response);
        } catch (SQLException e) {
            JsonResultUtils.writeErrorMessageJson(e.getMessage(), response);
            rc.rollbackAndClose();
            e.printStackTrace();
        }
    }

    /**
     * 如果表结构和工作流关联，这里应该会新建工作流相关的信息，
     * 和工作流相关的信息 需要在事件bean中实现，所有和工作流相关的工程需要有一个事件bean的实现
     *
     * @param modelCode 模块代码
     * @param jsonStr 表单数据
     * @param request 请求信息
     * @param response 返回信息
     */
    @ApiOperation(value = "工作流关联保存")
    @RequestMapping(value = "/{modelCode}/save", method = RequestMethod.POST)
    public void saveNew(@PathVariable String modelCode, @RequestBody String jsonStr,
                        HttpServletRequest request, HttpServletResponse response) {

        if (jsonStr == null || jsonStr.length() < 2) {
            JsonResultUtils.writeErrorMessageJson("数据格式不正确。", response);
            return;
        }
        if ('[' != jsonStr.charAt(0) && '{' != jsonStr.charAt(0)) {
            JsonResultUtils.writeErrorMessageJson("数据格式不正确。", response);
            return;
        }
//        List<ModelDataField> fields = modelDataFieldManager.listObjectsByProperty("modelCode", modelCode);
//        Map<String, String> defaultMap = new HashMap<>();
//        for(ModelDataField field : fields){
//            if(StringUtils.isNotBlank(field.getDefaultValue())){
//                defaultMap.put(field.getColumnName(), field.getDefaultValue());
//            }
//        }

        ModelRuntimeContext rc = modelFormService.createRuntimeContext(modelCode);
        try {
            if ('[' == jsonStr.charAt(0)) {
                JSONArray jo = JSON.parseArray(jsonStr);
                for (Object ja : jo) {
                    JSONObject jsonObj = (JSONObject)ja;
//                    for(String s : defaultMap.keySet()){
//                        if(StringUtils.isBlank(jsonObj.getString(s))){
//                            jsonObj.put(s, defaultMap.get(s));
//                        }
//                    }
                    modelFormService.mergeObject(rc, jsonObj, response);
                }

                JsonResultUtils.writeSuccessJson(response);
            } else {
                JSONObject jo = JSON.parseObject(jsonStr);
//                for(String s : defaultMap.keySet()){
//                    if(StringUtils.isBlank(jo.getString(s))){
//                        jo.put(s, defaultMap.get(s));
//                    }
//                }
                int n = modelFormService.saveNewObject(rc, jo, response);
                if (n <= 1)//>1 说明在 service 方法中已经在response中写入了返回信息
                    JsonResultUtils.writeSuccessJson(response);
            }
            rc.commitAndClose();
        } catch (Exception e) {
            JsonResultUtils.writeErrorMessageJson(e.getMessage(), response);
            rc.rollbackAndClose();
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "编辑")
    @RequestMapping(value = "/{modelCode}/edit", method = RequestMethod.GET)
    public void edit(@PathVariable String modelCode, boolean noMeta,
                     HttpServletRequest request, HttpServletResponse response) {
        ModelRuntimeContext rc = modelFormService.createRuntimeContext(modelCode);
        Map<String, Object> jo = rc.fetchPkFromRequest(request);
        ResponseMapData resData = new ResponseMapData();
        JSONObject obj = modelFormService.getObjectByProperties(rc, jo);
        if (obj != null) {
            try {
                Map<String, Object> refField = modelFormService.getModelReferenceFields(rc, obj);
                obj.putAll(refField);
            } catch (SQLException e) {
            }
        }

        MetaFormDefine metaData = modelFormService.createFormDefine(rc, "edit");
        resData.addResponseData("obj", metaData.transObjectRefranceData(obj));

        rc.close();
        if (!noMeta) {
            metaData.updateReadOnlyRefrenceField();
            resData.addResponseData("formModel", metaData);
        }

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @ApiOperation(value = "编辑")
    @RequestMapping(value = "/{modelCode}/update", method = RequestMethod.PUT)
    public void update(@PathVariable String modelCode, @RequestBody String jsonStr, HttpServletRequest request, HttpServletResponse response) {
        if (jsonStr == null || jsonStr.length() < 2) {
            JsonResultUtils.writeErrorMessageJson("数据格式不正确。", response);
            return;
        }
        if ('[' != jsonStr.charAt(0) && '{' != jsonStr.charAt(0)) {
            JsonResultUtils.writeErrorMessageJson("数据格式不正确。", response);
            return;
        }

        ModelRuntimeContext rc = modelFormService.createRuntimeContext(modelCode);
        try {
            if ('[' == jsonStr.charAt(0)) {
                JSONArray jo = JSON.parseArray(jsonStr);
                for (Object ja : jo)
                    modelFormService.mergeObject(rc, (JSONObject) ja, response);

                JsonResultUtils.writeSuccessJson(response);
            } else {
                JSONObject jo = JSON.parseObject(jsonStr);
                int n = modelFormService.updateObject(rc, jo, response);
                if (n <= 1)//>1 说明在 service 方法中已经在response中写入了返回信息
                    JsonResultUtils.writeSuccessJson(response);
            }
            rc.commitAndClose();
        } catch (Exception e) {
            JsonResultUtils.writeErrorMessageJson(e.getMessage(), response);
            rc.rollbackAndClose();
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "删除")
    @RequestMapping(value = "/{modelCode}/delete", method = RequestMethod.POST)
    public void delete(@PathVariable String modelCode, @RequestBody String jsonStr,
                       HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> joOri = convertSearchColumn(request);
        JSONObject jo = new JSONObject();
        jo.put(((String[]) joOri.get("primaryKey"))[0], ((String[]) (joOri.get("primaryValue")))[0]);
        ModelRuntimeContext rc = modelFormService.createRuntimeContext(modelCode);
        try {
            int n = modelFormService.deleteObjectById(rc, jo, response);
            rc.commitAndClose();
            if (n <= 1)//>1 说明在 service 方法中已经在response中写入了返回信息
                JsonResultUtils.writeSuccessJson(response);
        } catch (Exception e) {
            JsonResultUtils.writeErrorMessageJson(e.getMessage(), response);
            rc.rollbackAndClose();
            e.printStackTrace();
        }
    }

    /**
     * 判断模块是否和工作流程关联，返回失败
     * 如果有关联，先调用update
     *
     * @param modelCode
     * @param jsonStr
     * @param request
     * @param response
     */
    @ApiOperation(value = "表单提交")
    @RequestMapping(value = "/{modelCode}/submit", method = RequestMethod.POST)
    public void submit(@PathVariable String modelCode, @RequestBody String jsonStr, HttpServletRequest request, HttpServletResponse response) {
        if (jsonStr == null || jsonStr.length() < 2) {
            JsonResultUtils.writeErrorMessageJson("数据格式不正确。", response);
            return;
        }

        ModelRuntimeContext rc = modelFormService.createRuntimeContext(modelCode);
        try {
            JSONObject jo = JSON.parseObject(jsonStr);
            int n = modelFormService.submitObject(rc, jo, response);
            if (n <= 1)//>1 说明在 service 方法中已经在response中写入了返回信息
                JsonResultUtils.writeSuccessJson(response);
            rc.commitAndClose();
        } catch (Exception e) {
            JsonResultUtils.writeErrorMessageJson(e.getMessage(), response);
            rc.rollbackAndClose();
            e.printStackTrace();
        }
    }

    /**
     * 一个页面上多个表单同时提交，并且在一个事务中完成，这个暂未实现
     *
     * @param jsonStr
     * @param request
     * @param response
     */
    @ApiOperation(value = "多个表单提交")
    @RequestMapping(value = "/multimodelopt", method = RequestMethod.POST)
    public void multiModelOpt(@RequestBody String jsonStr,
                              HttpServletRequest request, HttpServletResponse response) {

    }

    @RequestMapping(value = "/{modelCode}/{propertyName}", method = RequestMethod.GET)
    public void asyncReferenceData(@PathVariable String modelCode,
                                   @PathVariable String propertyName, String startGroup,
                                   HttpServletRequest request, HttpServletResponse response) {

        ModelRuntimeContext rc = modelFormService.createRuntimeContext(modelCode);
        List<OptionItem> options = modelFormService.getAsyncReferenceData(
            rc, propertyName, startGroup);
        JsonResultUtils.writeSingleDataJson(options, response);
    }
}
