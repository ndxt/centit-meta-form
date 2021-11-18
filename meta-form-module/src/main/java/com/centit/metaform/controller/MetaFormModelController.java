package com.centit.metaform.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.ObjectAppendProperties;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.metaform.dubbo.adapter.MetaFormModelManager;
import com.centit.metaform.dubbo.adapter.po.MetaFormModel;
import com.centit.product.adapter.api.WorkGroupManager;
import com.centit.product.metadata.po.MetaTable;
import com.centit.product.metadata.service.MetaDataCache;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.database.metadata.TableField;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * MetaFormModel  Controller.
 * create by scaffold 2016-06-02
 * <p>
 * 通用模块管理null
 */

@Controller
@RequestMapping("/metaformmodel")
@Api(value = "已发布的自定义表单管理", tags = "已发布的自定义表单管理")
public class MetaFormModelController extends BaseController {
    //private static final Log logger = LogFactory.getLog(MetaFormModelController.class);

    @Autowired
    private MetaFormModelManager metaFormModelMag;

    @Autowired
    private MetaDataCache metaDataCache;

    @Autowired
    private WorkGroupManager workGroupManager;

    /**
     * 查询所有   通用模块管理  列表
     *
     * @param field    json中只保存需要的属性名
     * @param pageDesc 分页信息
     * @param request  {@link HttpServletRequest}
     * @return {data:[]}
     */
    @ApiOperation(value = "查询所有已发布的通用模块")
    @RequestMapping(method = RequestMethod.GET)
    @WrapUpResponseBody
    public PageQueryResult list(String[] field, PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> searchColumn = collectRequestParameters(request);
        JSONArray listObjects = metaFormModelMag.listFormModeAsJson(field, searchColumn, pageDesc);
        if (ArrayUtils.isNotEmpty(field)) {
            return PageQueryResult.createJSONArrayResult(listObjects, pageDesc, field, MetaFormModel.class);
        } else {
            return PageQueryResult.createJSONArrayResult(listObjects, pageDesc, MetaFormModel.class);
        }
    }

    /**
     * 查询 可以和流程关联的 模块 列表
     *
     * @param field    json中只保存需要的属性名
     * @param optType  flow 查找和流程关联的业务， node查找和节点关联的业务， all查找所有相关业务
     * @param pageDesc 分页信息
     * @param request  {@link HttpServletRequest}
     * @return {data:[]}
     */
    @ApiOperation(value = "查询已发布的工作流相关模块")
    @RequestMapping(value = "/workflow", method = RequestMethod.GET)
    @WrapUpResponseBody
    public PageQueryResult listFlowModel(String[] field, String optType, PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> searchColumn = collectRequestParameters(request);
        if ("flow".equalsIgnoreCase(optType)) {
            searchColumn.put("flowOptType", "1");
        } else if ("node".equalsIgnoreCase(optType)) {
            searchColumn.put("flowOptType", "2");
        } else {
            searchColumn.put("allFlowOpt", "all");
        }

        JSONArray listObjects = metaFormModelMag.listFormModeAsJson(field, searchColumn, pageDesc);
        if (ArrayUtils.isNotEmpty(field)) {
            return PageQueryResult.createJSONArrayResult(listObjects, pageDesc, field, MetaFormModel.class);
        } else {
            return PageQueryResult.createJSONArrayResult(listObjects, pageDesc, MetaFormModel.class);
        }
    }

    /**
     * 查询单个  通用模块管理
     *
     * @param modelId Model_Id
     * @return {data:{}}
     */
    @ApiOperation(value = "查询单个已发布的通用模块,其中keyProps为其主表对应的主键字段名称数组", response = MetaFormModel.class)
    @RequestMapping(value = "/{modelId}", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public ObjectAppendProperties<MetaFormModel> getMetaFormModel(@PathVariable String modelId) {
        /*return*/
        MetaFormModel metaFormModel = metaFormModelMag.getObjectById(modelId);
        List<String> pkCols = new ArrayList<>(6);
        if (metaFormModel.getTableId() != null && !"".equals(metaFormModel.getTableId())) {
            MetaTable tableInfo = this.metaDataCache.getTableInfo(metaFormModel.getTableId());
        /*if(tableInfo!=null && tableInfo.getPkFields()!=null && tableInfo.getPkFields().size()>0) {
            String[] pks = tableInfo.getPkFields().stream()
                    .map(TableField::getPropertyName)
                    .toArray(String[]::new);*/
            if (tableInfo != null) {
                //if(tableInfo.getPkFields()!=null) {
                for (TableField field : tableInfo.getPkFields()) {
                    pkCols.add(field.getPropertyName());
                }
                if (!"0".equals(tableInfo.getWorkFlowOptType())) {
                    pkCols.add(MetaTable.WORKFLOW_INST_ID_PROP);
                    pkCols.add(MetaTable.WORKFLOW_NODE_INST_ID_PROP);
                }
                if ("C".equals(tableInfo.getTableType())) {
                    pkCols.add("_id");
                }
                if ("C".equals(tableInfo.getTableType()) || tableInfo.isFulltextSearch()) {
                    pkCols.add("optTag");
                }
            }
        }
        return ObjectAppendProperties.create(metaFormModel,
                CollectionsOpt.createHashMap("keyProps", pkCols));
    }

    /**
     * 新增 通用模块管理
     *
     * @param metaFormModel {@link MetaFormModel}
     * @return
     */
    @ApiOperation(value = "新增通用模块")
    @RequestMapping(method = {RequestMethod.POST})
    @WrapUpResponseBody
    public String createMetaFormModel(@RequestBody MetaFormModel metaFormModel,
                                      HttpServletRequest request) {
        String loginUser = WebOptUtils.getCurrentUserCode(RequestThreadLocal.getLocalThreadWrapperRequest());
        if (StringBaseOpt.isNvl(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(RequestThreadLocal.getLocalThreadWrapperRequest(), "userCode");
        }
        if (!workGroupManager.loginUserIsExistWorkGroup(metaFormModel.getOsId(),loginUser)){
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您未登录或没有权限！");
        }
        MetaFormModel model = new MetaFormModel();
        String usercode = WebOptUtils.getCurrentUnitCode(request);
        model.copyNotNullProperty(metaFormModel);
        model.setRecorder(usercode);
        /*model.setFormTemplate(StringEscapeUtils.unescapeHtml4(model.getFormTemplate()));*/
        model.setExtendOptJs(StringEscapeUtils.unescapeHtml4(model.getExtendOptJs()));
        metaFormModelMag.saveNewMetaFormModel(model);
        return model.getModelId();
    }

    /**
     * 删除单个  通用模块管理
     *
     * @param modelId Model_Id
     */
    @ApiOperation(value = "删除单个通用模块")
    @RequestMapping(value = "/{osId}/{modelId}", method = {RequestMethod.DELETE})
    @WrapUpResponseBody
    public void deleteMetaFormModel(@PathVariable String osId,@PathVariable String modelId) {
        String loginUser = WebOptUtils.getCurrentUserCode(RequestThreadLocal.getLocalThreadWrapperRequest());
        if (StringBaseOpt.isNvl(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(RequestThreadLocal.getLocalThreadWrapperRequest(), "userCode");
        }
        if (!workGroupManager.loginUserIsExistWorkGroup(osId,loginUser)){
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您未登录或没有权限！");
        }
        metaFormModelMag.deleteObjectById(modelId);
    }

    /**
     * 新增或保存 通用模块管理
     *
     * @param modelId       Model_Id
     * @param metaFormModel {@link MetaFormModel}
     * @return
     */
    @ApiOperation(value = "编辑通用模块")
    @RequestMapping(value = "/{modelId}", method = {RequestMethod.PUT})
    @WrapUpResponseBody
    public Date updateMetaFormModel(@PathVariable String modelId, @RequestBody MetaFormModel metaFormModel) {
        MetaFormModel oldMetaForm = metaFormModelMag.getObjectById(modelId);
//        if(!DatetimeOpt.equalOnSecond(oldMetaForm.getLastModifyDate(),metaFormModel.getLastModifyDate())){
//            throw new ObjectException(CollectionsOpt.createHashMap(
//                    "yourTimeStamp", metaFormModel.getLastModifyDate(), "databaseTimeStamp", oldMetaForm.getLastModifyDate()),
//                    PersistenceException.DATABASE_OUT_SYNC_EXCEPTION, "已有其他人更新过该表单，请重新打开后提交。");
//        }
        metaFormModel.setModelId(modelId);
        /*metaFormModel.setFormTemplate(JSON
                JSONStringEscapeUtils.unescapeHtml4(metaFormModel.getFormTemplate()));*/
        metaFormModel.setExtendOptJs(StringEscapeUtils.unescapeHtml4(metaFormModel.getExtendOptJs()));
        String loginUser = WebOptUtils.getCurrentUserCode(RequestThreadLocal.getLocalThreadWrapperRequest());
        if (StringBaseOpt.isNvl(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(RequestThreadLocal.getLocalThreadWrapperRequest(), "userCode");
        }
        if (!workGroupManager.loginUserIsExistWorkGroup(metaFormModel.getOsId(),loginUser)){
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您未登录或没有权限！");
        }
        metaFormModelMag.updateMetaFormModel(metaFormModel);
        return metaFormModel.getLastModifyDate();
    }

    @ApiOperation(value = "获取模板内容")
    @RequestMapping(value = "/{modelId}/template", method = {RequestMethod.GET})
    @ApiImplicitParams({@ApiImplicitParam(
            name = "modelId", value = "表单模块id",
            required = true, paramType = "path", dataType = "String"
    ), @ApiImplicitParam(
            name = "type", value = "表单类型， m、mo、mobile为移动表单，其他的默认返回pc表单 ",
            paramType = "query", dataType = "String"
    )})
    @WrapUpResponseBody
    public JSONObject getFormTemplate(@PathVariable String modelId, String type,
                                      HttpServletRequest request) {
        MetaFormModel metaFormModel = metaFormModelMag.getObjectById(modelId);
        boolean isFromMobile = StringUtils.isBlank(type) ? WebOptUtils.isFromMobile(request)
                : StringUtils.equalsAnyIgnoreCase(type, "m", "mo", "mobile");
        if (isFromMobile) {
            //获取移动页面定义，如果没有单独设置就用pc页面
            JSONObject model = metaFormModel.getMobileFormTemplate();
            if (model != null) {
                return model;
            }
        }
        return metaFormModel.getFormTemplate();
    }

    @ApiOperation(value = "修改模板内容")
    @RequestMapping(value = "/{modelId}/template", method = {RequestMethod.PUT})
    @ApiImplicitParams({@ApiImplicitParam(
            name = "modelId", value = "表单模块id",
            required = true, paramType = "path", dataType = "String"
    ), @ApiImplicitParam(
            name = "formTemplate", value = "表单类型， m、mo、mobile为更改移动表单，其他的默认更改pc表单",
            required = true, paramType = "body", dataType = "String"
    ), @ApiImplicitParam(
            name = "type", value = "表单类型， m、mo、mobile为更改移动表单，其他的默认更改pc表单",
            paramType = "query", dataType = "String"
    )})
    @WrapUpResponseBody
    public void updateFormTemplate(@PathVariable String modelId,
                                   @RequestBody JSONObject formTemplate,
                                   String type) {
        String loginUser = WebOptUtils.getCurrentUserCode(RequestThreadLocal.getLocalThreadWrapperRequest());
        if (StringBaseOpt.isNvl(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(RequestThreadLocal.getLocalThreadWrapperRequest(), "userCode");
        }
        MetaFormModel metaFormModel = metaFormModelMag.getObjectById(modelId);
        if (!workGroupManager.loginUserIsExistWorkGroup(metaFormModel.getOsId(),loginUser)){
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您未登录或没有权限！");
        }
        if (StringUtils.equalsAnyIgnoreCase(type, "m", "mo", "mobile")) {
            metaFormModel.setMobileFormTemplate(formTemplate);
        } else {
            metaFormModel.setFormTemplate(formTemplate);
        }
        metaFormModelMag.updateMetaFormModel(metaFormModel);
    }

    @ApiOperation(value = "修改模板事件操作")
    @RequestMapping(value = "/{modelId}/optjs", method = {RequestMethod.PUT})
    @WrapUpResponseBody
    public void updateFormOptJs(@PathVariable String modelId,
                                @RequestBody String formOptjs) {
        String loginUser = WebOptUtils.getCurrentUserCode(RequestThreadLocal.getLocalThreadWrapperRequest());
        if (StringBaseOpt.isNvl(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(RequestThreadLocal.getLocalThreadWrapperRequest(), "userCode");
        }
        MetaFormModel metaFormModel = metaFormModelMag.getObjectById(modelId);
        if (!workGroupManager.loginUserIsExistWorkGroup(metaFormModel.getOsId(),loginUser)){
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您未登录或没有权限！");
        }
        metaFormModel.setExtendOptJs(StringEscapeUtils.unescapeHtml4(formOptjs));
        metaFormModelMag.updateMetaFormModel(metaFormModel);
    }

    @ApiOperation(value = "删除模板事件操作")
    @RequestMapping(value = "/{modelId}/optjs", method = {RequestMethod.DELETE})
    @WrapUpResponseBody
    public void deleteFormOptJs(@PathVariable String modelId) {
        metaFormModelMag.deleteFormOptJs(modelId);
    }

    @ApiOperation(value = "修改模板与流程关联")
    @RequestMapping(value = "/{modelId}/flow", method = {RequestMethod.PUT})
    @WrapUpResponseBody
    public void updateFormFlow(@PathVariable String modelId,
                               @RequestBody String relFlowCode) {
        String loginUser = WebOptUtils.getCurrentUserCode(RequestThreadLocal.getLocalThreadWrapperRequest());
        if (StringBaseOpt.isNvl(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(RequestThreadLocal.getLocalThreadWrapperRequest(), "userCode");
        }
        MetaFormModel metaFormModel = metaFormModelMag.getObjectById(modelId);
        if (!workGroupManager.loginUserIsExistWorkGroup(metaFormModel.getOsId(),loginUser)){
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您未登录或没有权限！");
        }
        metaFormModel.setRelFlowCode(
                StringUtils.substring(
                        StringEscapeUtils.unescapeHtml4(relFlowCode), 0, 64));
        metaFormModelMag.updateMetaFormModel(metaFormModel);
    }
    @ApiOperation(value = "根据optId获取模板名和模块id")
    @RequestMapping(value = "/metaform/{optId}", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public ResponseData listModelByOptId(@PathVariable String optId) {
        return ResponseData.makeResponseData(metaFormModelMag.listModelByOptId(optId));
    }
}
