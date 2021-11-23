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
import com.centit.metaform.dao.MetaFormModelDraftParam;
import com.centit.metaform.dubbo.adapter.MetaFormModelDraftManager;
import com.centit.metaform.dubbo.adapter.MetaFormModelManager;
import com.centit.metaform.dubbo.adapter.po.MetaFormModel;
import com.centit.metaform.dubbo.adapter.po.MetaFormModelDraft;
import com.centit.product.adapter.api.WorkGroupManager;
import com.centit.product.metadata.po.MetaTable;
import com.centit.product.metadata.service.MetaDataCache;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.database.metadata.TableField;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 编辑的表单管理（未发布表单）
 */
@Controller
@RequestMapping("/draft/metaformmodel")
@Api(value = "未发布的自定义表单管理", tags = "未发布的自定义表单管理")
public class MetaFormModelDraftController extends BaseController {

    @Autowired
    private MetaFormModelDraftManager metaFormModelDraftManager;

    @Autowired
    private MetaDataCache metaDataCache;

    @Autowired
    private MetaFormModelManager metaFormModelManager;

    @Autowired
    private WorkGroupManager workGroupManager;

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
    public ObjectAppendProperties<MetaFormModelDraft> getDraftMetaFormModel(@PathVariable String modelId) {
        MetaFormModelDraft metaFormModel = metaFormModelDraftManager.getMetaFormModelDraftById(modelId);
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

    @ApiOperation(value = "新增通用模块")
    @RequestMapping(method = {RequestMethod.POST})
    @WrapUpResponseBody
    public String createMetaFormModel(@RequestBody MetaFormModelDraft metaFormModel, HttpServletRequest request) {
        String usercode = WebOptUtils.getCurrentUnitCode(request);
        metaFormModel.setRecorder(usercode);
        metaFormModel.setExtendOptJs(StringEscapeUtils.unescapeHtml4(metaFormModel.getExtendOptJs()));
        String loginUser = WebOptUtils.getCurrentUserCode(RequestThreadLocal.getLocalThreadWrapperRequest());
        if (StringBaseOpt.isNvl(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(RequestThreadLocal.getLocalThreadWrapperRequest(), "userCode");
        }
        if (StringUtils.isBlank(loginUser)){
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN, "您未登录！");
        }
        if (!workGroupManager.loginUserIsExistWorkGroup(metaFormModel.getOsId(),loginUser)){
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您没有权限！");
        }
        metaFormModelDraftManager.saveMetaFormModelDraft(metaFormModel);
        return metaFormModel.getModelId();
    }

    /**
     * 删除单个  通用模块管理
     *
     * @param modelId Model_Id
     */
    @ApiOperation(value = "删除单个通用模块")
    @RequestMapping(value = "/{osId}/{modelId}", method = {RequestMethod.DELETE})
    @WrapUpResponseBody
    public void deleteMetaFormModel(@PathVariable String osId,@PathVariable String modelId,HttpServletRequest request) {
        String loginUser = WebOptUtils.getCurrentUserCode(RequestThreadLocal.getLocalThreadWrapperRequest());
        if (StringBaseOpt.isNvl(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(RequestThreadLocal.getLocalThreadWrapperRequest(), "userCode");
        }
        if (StringUtils.isBlank(loginUser)){
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN, "您未登录！");
        }
        if (!workGroupManager.loginUserIsExistWorkGroup(osId,loginUser)){
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您没有权限！");
        }
        metaFormModelDraftManager.deleteMetaFormModelDraftById(modelId);
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
    public Date updateMetaFormModel(@PathVariable String modelId, @RequestBody MetaFormModelDraft metaFormModel) {
//        MetaFormModelDraft oldMetaForm = metaFormModelDraftManager.getObjectById(modelId);
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
        if (StringUtils.isBlank(loginUser)){
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN, "您未登录！");
        }
        if (!workGroupManager.loginUserIsExistWorkGroup(metaFormModel.getOsId(),loginUser)){
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您没有权限！");
        }
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
        String loginUser = WebOptUtils.getCurrentUserCode(RequestThreadLocal.getLocalThreadWrapperRequest());
        if (StringBaseOpt.isNvl(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(RequestThreadLocal.getLocalThreadWrapperRequest(), "userCode");
        }
        if (StringUtils.isBlank(loginUser)){
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN, "您未登录！");
        }
        // 获取草稿状态的表单
        MetaFormModelDraft metaFormModelDraft = metaFormModelDraftManager.getMetaFormModelDraftById(modelId);
        if (!workGroupManager.loginUserIsExistWorkGroup(metaFormModelDraft.getOsId(),loginUser)){
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您没有权限！");
        }
        if (metaFormModelDraft == null) {
            return ResponseData.makeErrorMessage("未查询到表单！");
        }
        if (metaFormModelDraft.getLastModifyDate() != null && metaFormModelDraft.getPublishDate() != null &&
                !metaFormModelDraft.getLastModifyDate().after(metaFormModelDraft.getPublishDate())) {
            return ResponseData.makeErrorMessage("表单已发布，请勿重复发布！");
        }
        metaFormModelDraft.setPublishDate(new Date());
        metaFormModelDraft.setLastModifyDate(metaFormModelDraft.getLastModifyDate());
        String usercode = WebOptUtils.getCurrentUnitCode(request);
        if (usercode != null) {
            metaFormModelDraft.setRecorder(usercode);
        }
        // 发布表单
        metaFormModelDraftManager.publishMetaFormModel(metaFormModelDraft);
        // 更新编辑表单状态为已发布，更新时间和操作人
        metaFormModelDraftManager.updateMetaFormModelDraft(metaFormModelDraft);
        return ResponseData.makeSuccessResponse("发布成功！");
    }

    @ApiOperation(value = "修改表单所属业务模块")
    @PutMapping(value = "/batchUpdateOptId")
    @Transactional
    public JSONObject batchUpdateOptId( @RequestBody MetaFormModelDraftParam metaFormModelDraftParam) {
        int[] metaFormModelDraftArr = metaFormModelDraftManager.batchUpdateOptId(metaFormModelDraftParam.getOptId(), Arrays.asList(metaFormModelDraftParam.getModelIds()));
        int[] metaFormModelArr = metaFormModelManager.batchUpdateOptId(metaFormModelDraftParam.getOptId(), Arrays.asList(metaFormModelDraftParam.getModelIds()));
        JSONObject result = new JSONObject();
        result.put("metaFormModelDraftArr",metaFormModelDraftArr);
        result.put("metaFormModelArr",metaFormModelArr);
        return result;
    }


}
