package com.centit.product.dbdesign.controller;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.product.dbdesign.dao.PendingMetaTableDao;
import com.centit.product.dbdesign.po.PendingMetaTable;
import com.centit.product.dbdesign.service.MetaChangLogManager;
import com.centit.product.dbdesign.service.MetaTableManager;
import com.centit.support.database.utils.PageDesc;
import com.centit.product.metadata.po.MetaColumn;
import com.centit.product.metadata.po.MetaTable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * MdTable  Controller.
 * create by scaffold 2016-06-02
 * 表元数据表状态分为 系统/查询/更新
 * 系统，不可以做任何操作
 * 查询，仅用于通用查询模块，不可以更新
 * 更新，可以更新
 */
@Controller
@RequestMapping("/mdtable")
@Api(value = "表元数据", tags = "表元数据")
public class MetaTableController extends BaseController {
    //private static final Log log = LogFactory.getLog(MetaTableController.class);

    @Resource
    private MetaTableManager mdTableMag;

    @Resource
    private MetaChangLogManager mdChangLogMag;

    @Resource
    private PendingMetaTableDao pendingMetaTableDao;

    /**
     * 查询所有   元数据更改记录  列表
     *
     * @param field    json中只保存需要的属性名
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return {data:[]}
     */
    @ApiOperation(value = "查询所有元数据")
    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public void loglist(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);

        JSONArray listObjects = mdChangLogMag.listMdChangLogsAsJson(field, searchColumn, pageDesc);

        if (null == pageDesc) {
            JsonResultUtils.writeSingleDataJson(listObjects, response);
            return;
        }

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }


    /**
     * 查询表元数据（不带数据库名）
     * @param field    json中只保存需要的属性名
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return {data:[]}
     */
    @ApiOperation(value = "查询表元数据")
    @RequestMapping(method = RequestMethod.GET)
    public void list(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);

        JSONArray listObjects = mdTableMag.listObjectsAsJson(searchColumn, pageDesc);

        if (null == pageDesc) {
            JsonResultUtils.writeSingleDataJson(listObjects, response);
            return;
        }

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 获取表元数据（带数据库名）
     * @param field
     * @param pageDesc
     * @param request
     * @param response
     */
    @ApiOperation(value = "获取表元数据")
    @RequestMapping(value="/listdraft",method = RequestMethod.GET)
    public void listdraft(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
        JSONArray listObjects = mdTableMag.listDrafts(field, searchColumn, pageDesc);
        if (null == pageDesc) {
            JsonResultUtils.writeSingleDataJson(listObjects, response);
            return;
        }
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);
        if (ArrayUtils.isNotEmpty(field)) {
            JsonResultUtils.writeResponseDataAsJson(resData, response);
        } else {
            JsonResultUtils.writeResponseDataAsJson(resData, response);
        }
    }

    /**
     * 查询单个  表元数据表
     *
     * @param tableId  Table_ID
     * @param response {@link HttpServletResponse}
     * @return {data:{}}
     */
    @ApiOperation(value = "查询单个表元数据表")
    @RequestMapping(value = "/{tableId}", method = {RequestMethod.GET})
    public void getMdTable(@PathVariable Long tableId, HttpServletResponse response) {

        MetaTable mdTable = mdTableMag.getObjectByProperty("tableId", tableId);
//        MetaTable mdTable =
//                mdTableMag.getObjectById( tableId);
        JsonResultUtils.writeSingleDataJson(mdTable, response);
    }

    /**
     * 查询单个  表元数据表  草稿
     *
     * @param tableId  Table_ID
     * @param response {@link HttpServletResponse}
     * @return {data:{}}
     */
    @ApiOperation(value = "查询单个表元数据表")
    @RequestMapping(value = "/draft/{tableId}", method = {RequestMethod.GET})
    public void getMdTableDraft(@PathVariable Long tableId, HttpServletResponse response) {

        PendingMetaTable mdTable = mdTableMag.getPendingMetaTable(tableId);
        JsonResultUtils.writeSingleDataJson(mdTable, response);
    }

    /*
     * 新增 表元数据表 草稿
     */
    @ApiOperation(value = "新增表元数据表")
    @RequestMapping(method = {RequestMethod.POST})
    public void createMdTable(@RequestBody @Valid PendingMetaTable mdTable, HttpServletResponse response) {
        PendingMetaTable table = new PendingMetaTable();
        table.copyNotNullProperty(mdTable);
        if (null == table.getTableId()) {
            table.setTableId(String.valueOf(pendingMetaTableDao.getNextKey()));
        }
        table.setLastModifyDate(new Date());
//      mdTable.setTableType("T");// T 是数据表，后期会添加 V（视图）的选择
//      mdTable.setTableState("N");
        mdTableMag.saveNewPendingMetaTable(table);
        JsonResultUtils.writeSingleDataJson(table.getTableId(), response);
    }


    /*
     * 发布 表元数据表
     */
    @ApiOperation(value = "发布表元数据表")
    @RequestMapping(value = "/beforePublish/{ptableId}", method = {RequestMethod.POST})
    public void alertSqlBeforePublish(@PathVariable Long ptableId,
                                      HttpServletRequest request, HttpServletResponse response) {
        List<String> sqls = mdTableMag.makeAlterTableSqls(ptableId);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, sqls);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /*
     * 发布 表元数据表
     */
    @ApiOperation(value = "发布表元数据表")
    @RequestMapping(value = "/publish/{ptableId}", method = {RequestMethod.POST})
    public void publishMdTable(@PathVariable Long ptableId,
                               HttpServletRequest request, HttpServletResponse response) {
        String userCode = super.getLoginUserCode(request);
        if (StringUtils.isBlank(userCode)) {
            JsonResultUtils.writeErrorMessageJson(ResponseData.ERROR_UNAUTHORIZED,
                "当前用户没有登录，请先登录。", response);
            return;
        }
        Pair<Integer, String> ret = mdTableMag.publishMetaTable(ptableId, userCode);
        JsonResultUtils.writeErrorMessageJson(ret.getLeft(), ret.getRight(), response);
    }

    /**
     * 删除单个  表元数据表
     *
     * @param tableId Table_ID
     */
    @ApiOperation(value = "删除单个表元数据表")
    @RequestMapping(value = "/draft/{tableId}", method = {RequestMethod.DELETE})
    public void deleteMdTable(@PathVariable Long tableId, HttpServletResponse response) {

        mdTableMag.deletePendingMetaTable(tableId);

        JsonResultUtils.writeBlankJson(response);
    }

    /**
     * 新增或保存 表元数据表
     *
     * @param tableId  Table_ID
     * @param mdTable  {@link MetaTable}
     * @param response {@link HttpServletResponse}
     */
    @ApiOperation(value = "新增或保存表元数据表")
    @RequestMapping(value = "/draft/{tableId}", method = {RequestMethod.PUT})
    public void updateMdTable(@PathVariable Long tableId,
                              @RequestBody @Valid PendingMetaTable mdTable, HttpServletResponse response) {


        PendingMetaTable dbMdTable =
            mdTableMag.getPendingMetaTable(tableId);

        if (null != mdTable) {
            dbMdTable.copyNotNullProperty(mdTable);
            dbMdTable.setLastModifyDate(new Date());
            mdTableMag.savePendingMetaTable(dbMdTable);
        } else {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }

        JsonResultUtils.writeBlankJson(response);
    }

    /**
     * 列出未加入表单的field
     *
     * @param tableId
     * @param response
     * @param pageDesc
     */
    @ApiOperation(value = "列出未加入表单的field")
    @RequestMapping(value = "/{tableId}/getField", method = RequestMethod.GET)
    public void listfield(@PathVariable Long tableId, HttpServletResponse response, PageDesc pageDesc) {

        List<MetaColumn> meTadColumns =
            mdTableMag.listFields(tableId);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, meTadColumns);
        resData.addResponseData(PAGE_DESC, pageDesc);
        JsonResultUtils.writeSingleDataJson(meTadColumns, response);

    }

    /**
     * 获取草稿序列中的tableId
     *
     * @param response
     * @param pageDesc
     */
    @ApiOperation(value = "获取草稿序列中的tableId")
    @RequestMapping(value = "/draft/getNextKey", method = RequestMethod.GET)
    public void getPdNextKey(HttpServletResponse response, PageDesc pageDesc) {

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData("tableId", pendingMetaTableDao.getNextKey());
        JsonResultUtils.writeSingleDataJson(resData, response);

    }
}
