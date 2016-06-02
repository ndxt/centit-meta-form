package com.centit.metaform.controller;

import java.io.Serializable;
import java.util.List;
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
import com.centit.metaform.po.MdTable;
import com.centit.metaform.service.MdTableManager;


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
public class MdTableController extends BaseController{
	private static final Log log = LogFactory.getLog(MdTableController.class);
	
	@Resource
	private MdTableManager mdTableMag;	
	/*public void setMdTableMag(MdTableManager basemgr)
	{
		mdTableMag = basemgr;
		//this.setBaseEntityManager(mdTableMag);
	}*/

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
        
        List<MdTable> listObjects = mdTableMag.listObjects(searchColumn, pageDesc);

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
     * 查询单个  表元数据表 
	
	 * @param tableId  Table_ID
     * @param catalogCode 主键
     * 
     * @param response    {@link HttpServletResponse}
     * @return {data:{}}
     */
    @RequestMapping(value = "/{tableId}", method = {RequestMethod.GET})
    public void getMdTable(@PathVariable Long tableId, HttpServletResponse response) {
    	
    	MdTable mdTable =     			
    			mdTableMag.getObjectById( tableId);
        
        JsonResultUtils.writeSingleDataJson(mdTable, response);
    }
    
    /**
     * 新增 表元数据表
     *
     * @param mdTable  {@link MdTable}
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST})
    public void createMdTable(@Valid MdTable mdTable, HttpServletResponse response) {
    	Serializable pk = mdTableMag.saveNewObject(mdTable);
        JsonResultUtils.writeSingleDataJson(pk,response);
    }

    /**
     * 删除单个  表元数据表 
	
	 * @param tableId  Table_ID
     */
    @RequestMapping(value = "/{tableId}", method = {RequestMethod.DELETE})
    public void deleteMdTable(@PathVariable Long tableId, HttpServletResponse response) {
    	
    	mdTableMag.deleteObjectById( tableId);
        
        JsonResultUtils.writeBlankJson(response);
    } 
    
    /**
     * 新增或保存 表元数据表 
    
	 * @param tableId  Table_ID
	 * @param mdTable  {@link MdTable}
     * @param response    {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{tableId}", method = {RequestMethod.PUT})
    public void updateMdTable(@PathVariable Long tableId, 
    	@Valid MdTable mdTable, HttpServletResponse response) {
    	
    	
    	MdTable dbMdTable  =     			
    			mdTableMag.getObjectById( tableId);
        
        

        if (null != mdTable) {
        	dbMdTable .copy(mdTable);
        	mdTableMag.mergeObject(dbMdTable);
        } else {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }

        JsonResultUtils.writeBlankJson(response);
    }
}
