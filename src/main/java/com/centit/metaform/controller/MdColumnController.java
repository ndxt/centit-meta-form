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
import com.centit.metaform.po.MdColumn;
import com.centit.metaform.service.MdColumnManager;


/**
 * MdColumn  Controller.
 * create by scaffold 2016-06-02 
 * @author codefan@sina.com
 * 字段元数据表null   
*/


@Controller
@RequestMapping("/metaform/mdcolumn")
public class MdColumnController extends BaseController{
	private static final Log log = LogFactory.getLog(MdColumnController.class);
	
	@Resource
	private MdColumnManager mdColumnMag;	
	/*public void setMdColumnMag(MdColumnManager basemgr)
	{
		mdColumnMag = basemgr;
		//this.setBaseEntityManager(mdColumnMag);
	}*/

    /**
     * 查询所有   字段元数据表  列表
     *
     * @param field    json中只保存需要的属性名
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return {data:[]}
     */
    @RequestMapping(method = RequestMethod.GET)
    public void list(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);        
        
        JSONArray listObjects = mdColumnMag.listMdColumnsAsJson(field,searchColumn, pageDesc);

        if (null == pageDesc) {
            JsonResultUtils.writeSingleDataJson(listObjects, response);
            return;
        }
        
        ResponseData resData = new ResponseData();
        resData.addResponseData("OBJLIST", listObjects);
        resData.addResponseData("PAGE_DESC", pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }
    
    /**
     * 查询单个  字段元数据表 
	
	 * @param tableId  Table_ID
	 * @param columnName  column_Name
     * @param catalogCode 主键
     * 
     * @param response    {@link HttpServletResponse}
     * @return {data:{}}
     */
    @RequestMapping(value = "/{tableId}/{columnName}", method = {RequestMethod.GET})
    public void getMdColumn(@PathVariable Long tableId,@PathVariable String columnName, HttpServletResponse response) {
    	
    	MdColumn mdColumn =     			
    			mdColumnMag.getObjectById(new com.centit.metaform.po.MdColumnId(  tableId, columnName) );
    	
        JsonResultUtils.writeSingleDataJson(mdColumn, response);
    }
    
    /**
     * 新增 字段元数据表
     *
     * @param mdColumn  {@link MdColumn}
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST})
    public void createMdColumn(@Valid MdColumn mdColumn, HttpServletResponse response) {
    	Serializable pk = mdColumnMag.saveNewObject(mdColumn);
        JsonResultUtils.writeSingleDataJson(pk,response);
    }

    /**
     * 删除单个  字段元数据表 
	
	 * @param tableId  Table_ID
	 * @param columnName  column_Name
     */
    @RequestMapping(value = "/{tableId}/{columnName}", method = {RequestMethod.DELETE})
    public void deleteMdColumn(@PathVariable Long tableId,@PathVariable String columnName, HttpServletResponse response) {
    	
    	mdColumnMag.deleteObjectById(new com.centit.metaform.po.MdColumnId(  tableId, columnName) );
    	
        JsonResultUtils.writeBlankJson(response);
    } 
    
    /**
     * 新增或保存 字段元数据表 
    
	 * @param tableId  Table_ID
	 * @param columnName  column_Name
	 * @param mdColumn  {@link MdColumn}
     * @param response    {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{tableId}/{columnName}", method = {RequestMethod.PUT})
    public void updateMdColumn(@PathVariable Long tableId,@PathVariable String columnName, 
    	@Valid MdColumn mdColumn, HttpServletResponse response) {
    	
    	
    	MdColumn dbMdColumn =     			
    			mdColumnMag.getObjectById(new com.centit.metaform.po.MdColumnId(  tableId, columnName) );
    	
        

        if (null != mdColumn) {
        	dbMdColumn .copy(mdColumn);
        	mdColumnMag.mergeObject(dbMdColumn);
        } else {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }

        JsonResultUtils.writeBlankJson(response);
    }
}
