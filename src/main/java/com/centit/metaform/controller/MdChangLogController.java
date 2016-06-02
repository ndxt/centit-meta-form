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
import com.centit.metaform.po.MdChangLog;
import com.centit.metaform.service.MdChangLogManager;


/**
 * MdChangLog  Controller.
 * create by scaffold 2016-06-01 
 
 * 元数据更改记录null   
*/


@Controller
@RequestMapping("/metaform/mdchanglog")
public class MdChangLogController extends BaseController{
	private static final Log log = LogFactory.getLog(MdChangLogController.class);
	
	@Resource
	private MdChangLogManager mdChangLogMag;	
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
    @RequestMapping(method = RequestMethod.GET)
    public void list(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
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
     * 查询单个  元数据更改记录 
	
	 * @param version  version
     * @param catalogCode 主键
     * 
     * @param response    {@link HttpServletResponse}
     * @return {data:{}}
     */
    @RequestMapping(value = "/{version}", method = {RequestMethod.GET})
    public void getMdChangLog(@PathVariable Long version, HttpServletResponse response) {
    	
    	MdChangLog mdChangLog =     			
    			mdChangLogMag.getObjectById( version);
        
        JsonResultUtils.writeSingleDataJson(mdChangLog, response);
    }
    
    /**
     * 新增 元数据更改记录
     *
     * @param mdChangLog  {@link MdChangLog}
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST})
    public void createMdChangLog(@Valid MdChangLog mdChangLog, HttpServletResponse response) {
    	Serializable pk = mdChangLogMag.saveNewObject(mdChangLog);
        JsonResultUtils.writeSingleDataJson(pk,response);
    }

    /**
     * 删除单个  元数据更改记录 
	
	 * @param version  version
     */
    @RequestMapping(value = "/{version}", method = {RequestMethod.DELETE})
    public void deleteMdChangLog(@PathVariable Long version, HttpServletResponse response) {
    	
    	mdChangLogMag.deleteObjectById( version);
        
        JsonResultUtils.writeBlankJson(response);
    } 
    
    /**
     * 新增或保存 元数据更改记录 
    
	 * @param version  version
	 * @param mdChangLog  {@link MdChangLog}
     * @param response    {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{version}", method = {RequestMethod.PUT})
    public void updateMdChangLog(@PathVariable Long version, 
    	@Valid MdChangLog mdChangLog, HttpServletResponse response) {
    	
    	
    	MdChangLog dbMdChangLog  =     			
    			mdChangLogMag.getObjectById( version);
        
        

        if (null != mdChangLog) {
        	dbMdChangLog .copy(mdChangLog);
        	mdChangLogMag.mergeObject(dbMdChangLog);
        } else {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }

        JsonResultUtils.writeBlankJson(response);
    }
}
