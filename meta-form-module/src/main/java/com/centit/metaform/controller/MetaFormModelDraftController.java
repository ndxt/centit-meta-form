package com.centit.metaform.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.po.MetaFormModelDraft;
import com.centit.metaform.service.MetaFormModelDraftManager;
import com.centit.metaform.service.MetaFormModelManager;
import com.centit.metaform.vo.MetaFormModelDraftParam;
import com.centit.product.oa.team.utils.ResourceBaseController;
import com.centit.product.oa.team.utils.ResourceLock;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 编辑的表单管理（未发布表单）
 */
@Controller
@RequestMapping("/draft/metaformmodel")
@Api(value = "未发布的自定义表单管理", tags = "未发布的自定义表单管理")
public class MetaFormModelDraftController extends ResourceBaseController {

    @Autowired
    private MetaFormModelDraftManager metaFormModelDraftManager;


    @Autowired
    private MetaFormModelManager metaFormModelManager;

    @Autowired
    private PlatformEnvironment platformEnvironment;

    /**
     * 查询 可以和流程关联的 模块 列表
     *
     * @param field    json中只保存需要的属性名
     * @param optType  flow 查找和流程关联的业务， node查找和节点关联的业务， all查找所有相关业务
     * @param pageDesc 分页信息
     * @param request  {@link HttpServletRequest}
     * @return {data:[]}
     */
    @ApiOperation(value = "条件查询未发布所有通用模块(可查询工作流相关模块)")
    @RequestMapping(method = RequestMethod.GET)
    @WrapUpResponseBody
    public PageQueryResult draftFormModelList(String[] field, String optType, PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> searchColumn = collectRequestParameters(request);

        if (optType != null) {
            // 查询工作流相关模块
            if ("flow".equalsIgnoreCase(optType)) {
                searchColumn.put("flowOptType", "1");
            } else if ("node".equalsIgnoreCase(optType)) {
                searchColumn.put("flowOptType", "2");
            } else {
                searchColumn.put("allFlowOpt", "all");
            }
        }
        JSONArray listObjects = metaFormModelDraftManager.listFormModeAsJson(field, searchColumn, pageDesc);
        if (ArrayUtils.isNotEmpty(field)) {
            return PageQueryResult.createJSONArrayResult(listObjects, pageDesc, field, MetaFormModel.class);
        } else {
            return PageQueryResult.createJSONArrayResult(listObjects, pageDesc, MetaFormModel.class);
        }
    }

    @ApiOperation(value = "查询单个未发布的通用模块,其中keyProps为其主表对应的主键字段名称数组", response = MetaFormModel.class)
    @RequestMapping(value = "/{modelId}", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public MetaFormModelDraft getDraftMetaFormModel(@PathVariable String modelId) {
        return metaFormModelDraftManager.getMetaFormModelDraftById(modelId);
    }

    @ApiOperation(value = "新增通用模块")
    @RequestMapping(method = {RequestMethod.POST})
    @WrapUpResponseBody
    public String createMetaFormModel(@RequestBody MetaFormModelDraft metaFormModel, HttpServletRequest request) {

        String loginUser = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isBlank(loginUser)) {
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN, "您未登录！");
        }

        if (!platformEnvironment.loginUserIsExistWorkGroup(metaFormModel.getOsId(), loginUser)) {
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您没有权限！");
        }
        metaFormModel.setRecorder(loginUser);
        metaFormModelDraftManager.saveMetaFormModelDraft(metaFormModel);
        return metaFormModel.getModelId();
    }

    /**
     * 删除单个  通用模块管理
     *
     * @param modelId Model_Id
     */
    @ApiOperation(value = "删除单个通用模块", notes = "如果MetaFormModel也需要删除，传入参数deleteMetaFormModel=true")
    @RequestMapping(value = "/{modelId}", method = {RequestMethod.DELETE})
    @WrapUpResponseBody
    public void deleteMetaFormModel(@PathVariable String modelId, HttpServletRequest request) {
        String loginUser = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isBlank(loginUser)) {
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN, "您未登录！");
        }

        MetaFormModelDraft metaFormModelDraft = metaFormModelDraftManager.getMetaFormModelDraftById(modelId);
        if (null == metaFormModelDraft) {
            throw new ObjectException("表单数据不存在!");
        }
        if (!platformEnvironment.loginUserIsExistWorkGroup(metaFormModelDraft.getOsId(), loginUser)) {
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您没有权限！");
        }
        if (MapUtils.getBooleanValue(collectRequestParameters(request), "deleteMetaFormModel")) {
            metaFormModelDraftManager.deleteMetaFormModelDraftByIdWithMetaFormModel(modelId);
        } else {
            metaFormModelDraftManager.deleteMetaFormModelDraftById(modelId);
        }
    }

    /**
     * 新增或保存 通用模块管理
     *
     * @param modelId       Model_Id
     * @param metaFormModel {@link MetaFormModel}
     * @return
     */
    @ApiOperation(value = "编辑未发布的通用模块")
    @RequestMapping(value = "/{modelId}", method = {RequestMethod.PUT})
    @WrapUpResponseBody
    public Date updateMetaFormModel(@PathVariable String modelId, @RequestBody MetaFormModelDraft metaFormModel,
                                    HttpServletRequest request) {
        //检查资源
        ResourceLock.lockResource(modelId, WebOptUtils.getCurrentUserCode(request));

        metaFormModel.setModelId(modelId);
        String loginUser = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isBlank(loginUser)) {
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN, "您未登录！");
        }
        if (!platformEnvironment.loginUserIsExistWorkGroup(metaFormModel.getOsId(), loginUser)) {
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您没有权限！");
        }
        metaFormModel.setRecorder(loginUser);
        metaFormModelDraftManager.updateMetaFormModelDraft(metaFormModel);
        return metaFormModel.getLastModifyDate();
    }


    /**
     * 发布表单，暂时不做版本管理 表单有2种状态 A 草稿（未发布）  E 已发布
     *
     * @param modelId
     * @param request
     * @return
     */
    @ApiOperation(value = "发布表单")
    @RequestMapping(value = "/publish/{modelId}", method = {RequestMethod.POST})
    @WrapUpResponseBody
    public ResponseData publishMetaFormModel(@PathVariable String modelId, HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        if(StringUtils.isBlank(userCode)){
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN, "获取当前用户信息失败，原因可能是用户没登录，或者session已失效！");
        }

        MetaFormModelDraft metaFormModelDraft = metaFormModelDraftManager.getMetaFormModelDraftById(modelId);
        if (metaFormModelDraft == null) {
            return ResponseData.makeErrorMessage("未查询到表单！");
        }
        loginUserPermissionCheck(metaFormModelDraft.getOsId(), request);
        if (metaFormModelDraft.getLastModifyDate() != null && metaFormModelDraft.getPublishDate() != null &&
                !metaFormModelDraft.getLastModifyDate().after(metaFormModelDraft.getPublishDate())) {
            return ResponseData.makeErrorMessage("表单已发布，请勿重复发布！");
        }
        metaFormModelDraft.setPublishDate(new Date());
        metaFormModelDraft.setLastModifyDate(metaFormModelDraft.getLastModifyDate());

        metaFormModelDraft.setRecorder(userCode);
        // 发布表单
        metaFormModelDraftManager.publishMetaFormModel(metaFormModelDraft);
        // 更新编辑表单状态为已发布，更新时间和操作人
        metaFormModelDraftManager.updateMetaFormModelDraft(metaFormModelDraft);
        return ResponseData.makeSuccessResponse("发布成功！");
    }

    @ApiOperation(value = "修改表单所属业务模块")
    @PutMapping(value = "/batchUpdateOptId")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ResponseData batchUpdateOptId(@RequestBody MetaFormModelDraftParam metaFormModelDraftParam) {
        int[] metaFormModelDraftArr = metaFormModelDraftManager.batchUpdateOptId(metaFormModelDraftParam.getOptId(), Arrays.asList(metaFormModelDraftParam.getModelIds()));
        int[] metaFormModelArr = metaFormModelManager.batchUpdateOptId(metaFormModelDraftParam.getOptId(), Arrays.asList(metaFormModelDraftParam.getModelIds()));
        JSONObject result = new JSONObject();
        result.put("metaFormModelDraftArr", metaFormModelDraftArr);
        result.put("metaFormModelArr", metaFormModelArr);
        return ResponseData.makeSuccessResponse(result.toJSONString());
    }


    @ApiOperation(value = "修改表单数据可用状态(T:禁用，F:启用)")
    @PutMapping(value = "/{modelId}/{validType}")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ResponseData updateValidStatus(@PathVariable String modelId, @PathVariable String validType, HttpServletRequest request) {
        // 获取草稿状态的表单
        MetaFormModelDraft metaFormModelDraft = metaFormModelDraftManager.getMetaFormModelDraftById(modelId);
        if (metaFormModelDraft == null) {
            return ResponseData.makeErrorMessage("未查询到表单！");
        }
        loginUserPermissionCheck(metaFormModelDraft.getOsId(), request);
        //启用  disableType 必须等于T 或者 F
        if (!StringBaseOpt.isNvl(validType) && "F".equals(validType)) {
            metaFormModelDraftManager.updateValidStatus(modelId, validType);
            metaFormModelManager.updateValidStatus(modelId, validType);
        } else if (!StringBaseOpt.isNvl(validType) && "T".equals(validType)) {//禁用
            metaFormModelDraftManager.updateValidStatus(modelId, validType);
            metaFormModelManager.updateValidStatus(modelId, validType);
        } else {
            return ResponseData.makeErrorMessage(ResponseData.HTTP_PRECONDITION_FAILED, "非法传参，参数必须为T或F,传入的参数为：" + validType);
        }
        return ResponseData.makeSuccessResponse();
    }

    @ApiOperation(value = "批量删除和清空回收站")
    @PostMapping("batchDeleteByModelIds")
    @ApiImplicitParam(
            name = "jsonObject",
            value = "批量删除-参数：{modelIds:[\"modelId\"],osId:\"osId\"};清空回收站-参数：{osId:\"osId\"}"
    )
    @WrapUpResponseBody
    public void batchDeleteByModelIds(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        JSONArray modelIds = jsonObject.getJSONArray("modelIds");
        String osId = jsonObject.getString("osId");
        loginUserPermissionCheck(osId, request);
        if (modelIds != null && modelIds.size() > 0) {
            String[] ids = modelIds.toArray(new String[modelIds.size()]);
            metaFormModelDraftManager.batchDeleteByIds(ids);
            metaFormModelManager.batchDeleteByIds(ids);
        } else if (!StringBaseOpt.isNvl(osId)) {
            metaFormModelDraftManager.clearTrashStand(osId);
            metaFormModelManager.clearTrashStand(osId);
        }
    }

    private void loginUserPermissionCheck(String osId, HttpServletRequest request) {
        if (StringUtils.isBlank(osId)) {
            throw new ObjectException(ResponseData.ERROR_INTERNAL_SERVER_ERROR, "osId不能为空！");
        }
        String loginUser = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isBlank(loginUser)) {
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN, "您未登录！");
        }

        if (!platformEnvironment.loginUserIsExistWorkGroup(osId, loginUser)) {
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您没有权限！");
        }
    }


    @ApiOperation(value = "表单复制接口")
    @PostMapping("/metaFormCopy")
    @ApiImplicitParam(
            name = "jsonObject",
            value = "API复制接口-参数：{\"modelId\":\"\",\"modelName\":\"\",\"osId\":\"\",\"optId\":\"\"}"
    )
    @WrapUpResponseBody
    public ResponseData metaFormCopy(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String modelId = jsonObject.getString("modelId");
        String osId = jsonObject.getString("osId");
        String modelName = jsonObject.getString("modelName");
        String optId = jsonObject.getString("optId");
        if (StringUtils.isBlank(modelId) || StringUtils.isBlank(modelName) || StringUtils.isBlank(optId)) {
            return ResponseData.makeErrorMessage("缺少参数，请检查请求参数是否正确！");
        }
        MetaFormModelDraft metaFormModelDraft = metaFormModelDraftManager.getMetaFormModelDraftById(modelId);
        if (metaFormModelDraft == null) return ResponseData.makeErrorMessage("复制的表单数据不存在！");
        loginUserPermissionCheck(osId, request);
        metaFormModelDraft.setOsId(osId);
        metaFormModelDraft.setModelId(null);
        metaFormModelDraft.setModelName(modelName);
        metaFormModelDraft.setOptId(optId);
        metaFormModelDraftManager.saveMetaFormModelDraft(metaFormModelDraft);
        return ResponseData.successResponse;
    }
}
