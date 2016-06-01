package com.centit.metaform.controller;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import com.centit.metaform.po.PendingMdTable;
import com.centit.metaform.service.PendingMdTableManager;


/**
 * PendingMdTable  Controller.
 * create by scaffold 2016-06-01 
 * @author codefan@sina.com
 * 未落实表元数据表null   
*/


@Controller
@RequestMapping("/metaform/pendingmdtable")
public class PendingMdTableController extends BaseController{
	private static final Log log = LogFactory.getLog(PendingMdTableController.class);
	
	@Resource
	private PendingMdTableManager pendingMdTableMag;	
	/*public void setPendingMdTableMag(PendingMdTableManager basemgr)
	{
		pendingMdTableMag = basemgr;
		//this.setBaseEntityManager(pendingMdTableMag);
	}*/

    /**
     * 查询所有   未落实表元数据表  列表
     *
     * @param field    json中只保存需要的属性名
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return {data:[]}
     */
    @RequestMapping(method = RequestMethod.GET)
    public void list(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);        
        
        JSONArray listObjects = pendingMdTableMag.listPendingMdTablesAsJson(field,searchColumn, pageDesc);

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
     * 查询单个  未落实表元数据表 
	
	 * @param tableId  Table_ID
     * @param catalogCode 主键
     * 
     * @param response    {@link HttpServletResponse}
     * @return {data:{}}
     */
    @RequestMapping(value = "/{tableId}", method = {RequestMethod.GET})
    public void getPendingMdTable(@PathVariable Long tableId, HttpServletResponse response) {
    	
    	PendingMdTable pendingMdTable =     			
    			pendingMdTableMag.getObjectById( tableId);
        
        JsonResultUtils.writeSingleDataJson(pendingMdTable, response);
    }
    
    /**
     * 新增 未落实表元数据表
     *
     * @param pendingMdTable  {@link PendingMdTable}
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST})
    public void createPendingMdTable(@Valid PendingMdTable pendingMdTable, HttpServletResponse response) {
    	Serializable pk = pendingMdTableMag.saveNewObject(pendingMdTable);
        JsonResultUtils.writeSingleDataJson(pk,response);
    }

    /**
     * 删除单个  未落实表元数据表 
	
	 * @param tableId  Table_ID
     */
    @RequestMapping(value = "/{tableId}", method = {RequestMethod.DELETE})
    public void deletePendingMdTable(@PathVariable Long tableId, HttpServletResponse response) {
    	
    	pendingMdTableMag.deleteObjectById( tableId);
        
        JsonResultUtils.writeBlankJson(response);
    } 
    
    /**
     * 新增或保存 未落实表元数据表 
    
	 * @param tableId  Table_ID
	 * @param pendingMdTable  {@link PendingMdTable}
     * @param response    {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{tableId}", method = {RequestMethod.PUT})
    public void updatePendingMdTable(@PathVariable Long tableId, 
    	@Valid PendingMdTable pendingMdTable, HttpServletResponse response) {
    	
    	
    	PendingMdTable dbPendingMdTable  =     			
    			pendingMdTableMag.getObjectById( tableId);
        
        

        if (null != pendingMdTable) {
        	dbPendingMdTable .copy(pendingMdTable);
        	pendingMdTableMag.mergeObject(dbPendingMdTable);
        } else {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }

        JsonResultUtils.writeBlankJson(response);
    }
}
