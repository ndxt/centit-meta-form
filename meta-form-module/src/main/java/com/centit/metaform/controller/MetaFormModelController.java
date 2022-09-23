package com.centit.metaform.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.metaform.dubbo.adapter.MetaFormModelManager;
import com.centit.metaform.dubbo.adapter.po.MetaFormModel;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
    private PlatformEnvironment platformEnvironment;

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
    public MetaFormModel getMetaFormModel(@PathVariable String modelId) {
        return metaFormModelMag.getObjectById(modelId);
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
        if (StringUtils.isBlank(loginUser)) {
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN, "您未登录！");
        }
        if (!platformEnvironment.loginUserIsExistWorkGroup(metaFormModel.getOsId(), loginUser)) {
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您没有权限！");
        }
        MetaFormModel model = new MetaFormModel();
        String usercode = WebOptUtils.getCurrentUnitCode(request);
        model.copyNotNullProperty(metaFormModel);
        model.setRecorder(usercode);
        metaFormModelMag.saveNewMetaFormModel(model);
        return model.getModelId();
    }

    /**
     * 删除单个  通用模块管理
     *
     * @param modelId Model_Id
     */
    @ApiOperation(value = "删除单个通用模块")
    @RequestMapping(value = "/{modelId}", method = {RequestMethod.DELETE})
    @WrapUpResponseBody
    public void deleteMetaFormModel(@PathVariable String modelId) {
        String loginUser = WebOptUtils.getCurrentUserCode(RequestThreadLocal.getLocalThreadWrapperRequest());
        if (StringBaseOpt.isNvl(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(RequestThreadLocal.getLocalThreadWrapperRequest(), "userCode");
        }
        if (StringUtils.isBlank(loginUser)) {
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN, "您未登录！");
        }
        MetaFormModel metaFormModel = metaFormModelMag.getObjectById(modelId);
        if (null == metaFormModel) {
            throw new ObjectException("表单数据不存在!");
        }
        if (!platformEnvironment.loginUserIsExistWorkGroup(metaFormModel.getOsId(), loginUser)) {
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您没有权限！");
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
        metaFormModel.setModelId(modelId);
        String loginUser = WebOptUtils.getCurrentUserCode(RequestThreadLocal.getLocalThreadWrapperRequest());
        if (StringBaseOpt.isNvl(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(RequestThreadLocal.getLocalThreadWrapperRequest(), "userCode");
        }
        if (StringUtils.isBlank(loginUser)) {
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN, "您未登录！");
        }
        if (!platformEnvironment.loginUserIsExistWorkGroup(metaFormModel.getOsId(), loginUser)) {
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您没有权限！");
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
        if (StringUtils.isBlank(loginUser)) {
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN, "您未登录！");
        }
        if (!platformEnvironment.loginUserIsExistWorkGroup(metaFormModel.getOsId(), loginUser)) {
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您没有权限！");
        }
        if (StringUtils.equalsAnyIgnoreCase(type, "m", "mo", "mobile")) {
            metaFormModel.setMobileFormTemplate(formTemplate);
        } else {
            metaFormModel.setFormTemplate(formTemplate);
        }
        metaFormModelMag.updateMetaFormModel(metaFormModel);
    }

    @ApiOperation(value = "根据optId获取模板名和模块id")
    @RequestMapping(value = "/metaform/{optId}", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public ResponseData listModelByOptId(@PathVariable String optId) {
        return ResponseData.makeResponseData(metaFormModelMag.listModelByOptId(optId));
    }

    @ApiOperation(value = "验证已发布的自定义表单是否存在")
    @RequestMapping(value = "/metaform/isexist/{modelId}", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public ResponseData checkMetaFormModelIsExist(@PathVariable String modelId) {
        int counts = metaFormModelMag.countMetaFormModels(CollectionsOpt.createHashMap("modelId", modelId));
        return ResponseData.makeResponseData(counts > 0);
    }
}
