package com.centit.metaform.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import com.centit.metaform.po.MetaTable;
import com.centit.metaform.po.PendingMetaTable;
import com.centit.metaform.service.MetaChangLogManager;
import com.centit.metaform.service.MetaTableManager;


/**
 * MdTable  Controller.
 * create by scaffold 2016-06-02 
 
 * 表元数据表状态分为 系统/查询/更新
系统，不可以做任何操作
查询，仅用于通用查询模块，不可以更新
更新，可以更新
   
*/


@Controller
@RequestMapping("/metaform/mdtable")
public class MetaTableController extends BaseController{
	private static final Log log = LogFactory.getLog(MetaTableController.class);
	
	@Resource
	private MetaTableManager mdTableMag;	
	/*public void setMdTableMag(MdTableManager basemgr)
	{
		mdTableMag = basemgr;
		//this.setBaseEntityManager(mdTableMag);
	}*/

	@Resource
	private MetaChangLogManager mdChangLogMag;	
	/*public void setMdChangLogMag(MdChangLogManager basemgr)
	{
		mdChangLogMag = basemgr;
		//this.setBaseEntityManager(mdChangLogMag);
	}*/

    /**
     * 查询所有   元数据更改记录  列表
     *
     * @param field    json中只保存需要的属性名
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return {data:[]}
     */
    @RequestMapping(value="/log",method = RequestMethod.GET)
    public void loglist(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);        
        
        JSONArray listObjects = mdChangLogMag.listMdChangLogsAsJson(field,searchColumn, pageDesc);

        if (null == pageDesc) {
            JsonResultUtils.writeSingleDataJson(listObjects, response);
            return;
        }
        
        ResponseData resData = new ResponseData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }
	
	
    /**
     * 查询所有   表元数据表  列表
     *
     * @param field    json中只保存需要的属性名
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return {data:[]}
     */
    @RequestMapping(method = RequestMethod.GET)
    public void list(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);        
        
        List<MetaTable> listObjects = mdTableMag.listObjects(searchColumn, pageDesc);

        if (null == pageDesc) {
            JsonResultUtils.writeSingleDataJson(listObjects, response);
            return;
        }
        
        ResponseData resData = new ResponseData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }
    
    @RequestMapping(value="/draft",method = RequestMethod.GET)
    public void listdraft(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);        
        List<PendingMetaTable> listObjects = mdTableMag.listDrafts(searchColumn, pageDesc);
        SimplePropertyPreFilter simplePropertyPreFilter = null;
        
        
        if (null == pageDesc) {
            JsonResultUtils.writeSingleDataJson(listObjects, response);
            return;
        }
        ResponseData resData = new ResponseData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);
        if (ArrayUtils.isNotEmpty(field)) {
            simplePropertyPreFilter = new SimplePropertyPreFilter(PendingMetaTable.class, field);
            JsonResultUtils.writeResponseDataAsJson(resData, response,simplePropertyPreFilter);
        }
        else{
        	JsonResultUtils.writeResponseDataAsJson(resData, response);
        }
        
    }
    
    /**
     * 查询单个  表元数据表 
	
	 * @param tableId  Table_ID
     * @param catalogCode 主键
     * 
     * @param response    {@link HttpServletResponse}
     * @return {data:{}}
     */
    @RequestMapping(value = "/{tableId}", method = {RequestMethod.GET})
    public void getMdTable(@PathVariable Long tableId, HttpServletResponse response) {
    	
    	MetaTable mdTable =     			
    			mdTableMag.getObjectById( tableId);
        JsonResultUtils.writeSingleDataJson(mdTable, response);
    }
    
    /**
     * 查询单个  表元数据表  草稿
	
	 * @param tableId  Table_ID
     * @param catalogCode 主键
     * 
     * @param response    {@link HttpServletResponse}
     * @return {data:{}}
     */
    @RequestMapping(value = "/draft/{tableId}", method = {RequestMethod.GET})
    public void getMdTableDraft(@PathVariable Long tableId, HttpServletResponse response) {
    	
    	PendingMetaTable mdTable =     			
    			mdTableMag.getPendingMetaTable(tableId);
        JsonResultUtils.writeSingleDataJson(mdTable, response);
    }
    
    /**
     * 新增 表元数据表 草稿
     *
     * @param mdTable  {@link MetaTable}
     * @return
     */
    @RequestMapping(value="/draft",method = {RequestMethod.POST})
    public void createMdTable(@RequestBody @Valid PendingMetaTable mdTable, HttpServletResponse response) {
    	PendingMetaTable table=new PendingMetaTable();
    	table.copyNotNullProperty(mdTable);
    	Serializable pk = mdTableMag.saveNewPendingMetaTable(table);
        JsonResultUtils.writeSingleDataJson(pk,response);
    }
    
    
    /**
     * 发布 表元数据表
     *
     * @param mdTable  {@link MetaTable}
     * @return
     */
    @RequestMapping(value="/publish/{ptableId}",method = {RequestMethod.POST})
    public void publishMdTable(@PathVariable Long ptableId,
    		HttpServletRequest request,HttpServletResponse response) {
    	String userCode = super.getLoginUserCode(request);
    	if(StringUtils.isBlank(userCode)){
    		JsonResultUtils.writeErrorMessageJson(401,"当前用户没有登录，请先登录。", response);
    		return;
    	}
    	String msg=mdTableMag.publishMetaTable(ptableId, userCode);
        JsonResultUtils.writeSingleDataJson(msg,response);
    }
    
    /**
     * 删除单个  表元数据表 
	
	 * @param tableId  Table_ID
     */
    @RequestMapping(value = "/draft/{tableId}", method = {RequestMethod.DELETE})
    public void deleteMdTable(@PathVariable Long tableId, HttpServletResponse response) {
    	
    	mdTableMag.deletePendingMetaTable(tableId);
        
        JsonResultUtils.writeBlankJson(response);
    } 
    
    /**
     * 新增或保存 表元数据表 
    
	 * @param tableId  Table_ID
	 * @param mdTable  {@link MetaTable}
     * @param response    {@link HttpServletResponse}
     */
    @RequestMapping(value = "/draft/{tableId}", method = {RequestMethod.PUT})
    public void updateMdTable(@PathVariable Long tableId, 
    	@RequestBody @Valid PendingMetaTable mdTable, HttpServletResponse response) {
    	
    	
    	PendingMetaTable dbMdTable  =     			
    			mdTableMag.getPendingMetaTable(tableId);
        
        if (null != mdTable) {
        	dbMdTable .copyNotNullProperty(mdTable);
        	mdTableMag.savePendingMetaTable(dbMdTable);
        } else {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }

        JsonResultUtils.writeBlankJson(response);
    }
}
