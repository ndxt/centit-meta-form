package com.centit.application.controller;

import com.centit.application.po.ApplicationInfo;
import com.centit.application.service.ApplicationInfoManager;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "applicationInfo")
@Api(value = "应用管理", tags = "应用管理")
public class ApplicationInfoController extends BaseController{
    @Autowired
    private ApplicationInfoManager applicationInfoManager;
    @ApiOperation(value = "新增应用")
    @PostMapping
    @WrapUpResponseBody
    public void createApplicationInfo(ApplicationInfo applicationInfo, HttpServletResponse response){
        applicationInfo.setPageFlow(StringEscapeUtils.unescapeHtml4(applicationInfo.getPageFlow()));
        applicationInfoManager.createApplicationInfo(applicationInfo);
        JsonResultUtils.writeSingleDataJson(applicationInfo.getApplicationId(),response);
    }
    @ApiOperation(value = "修改应用")
    @ApiImplicitParam(name = "applicationId", value = "应用ID")
    @PutMapping(value = "/{applicationId}")
    @WrapUpResponseBody
    public void updateApplicationInfo(@PathVariable String applicationId, @RequestBody ApplicationInfo applicationInfo){
        applicationInfo.setApplicationId(applicationId);
        applicationInfo.setPageFlow(StringEscapeUtils.unescapeHtml4(applicationInfo.getPageFlow()));
        applicationInfoManager.updateApplicationInfo(applicationInfo);
    }
    @ApiOperation(value = "删除应用模块")
    @ApiImplicitParam(name = "applicationId", value = "图表ID")
    @DeleteMapping(value = "/{applicationId}")
    @WrapUpResponseBody
    public void deleteApplicationInfo(@PathVariable String applicationId){
        applicationInfoManager.deleteApplicationInfo(applicationId);
    }
    @ApiOperation(value = "查询应用模块")
    @GetMapping
    @WrapUpResponseBody
    public PageQueryResult<ApplicationInfo> listApplicationInfo(HttpServletRequest request,PageDesc pageDesc){
        Map<String, Object> searchColumn = collectRequestParameters(request);
        List<ApplicationInfo> list = applicationInfoManager.listApplicationInfo(searchColumn, pageDesc);
        return PageQueryResult.createResult(list, pageDesc);
    }

    @ApiOperation(value = "查询单个应用模块")
    @GetMapping(value = "/{applicationId}")
    @WrapUpResponseBody
    public ApplicationInfo getApplicationInfo(@PathVariable String applicationId){
        ApplicationInfo applicationInfo = applicationInfoManager.getApplicationInfo(applicationId);
        return applicationInfo;
    }
}
