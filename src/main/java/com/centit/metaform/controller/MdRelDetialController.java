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
import com.centit.metaform.po.MdRelDetial;
import com.centit.metaform.service.MdRelDetialManager;


/**
 * MdRelDetial  Controller.
 * create by scaffold 2016-06-02 
 * @author codefan@sina.com
 * 表关联细节表null   
*/


@Controller
@RequestMapping("/metaform/mdreldetial")
public class MdRelDetialController extends BaseController{
	private static final Log log = LogFactory.getLog(MdRelDetialController.class);
	
	@Resource
	private MdRelDetialManager mdRelDetialMag;	
	/*public void setMdRelDetialMag(MdRelDetialManager basemgr)
	{
		mdRelDetialMag = basemgr;
		//this.setBaseEntityManager(mdRelDetialMag);
	}*/

    /**
     * 查询所有   表关联细节表  列表
     *
     * @param field    json中只保存需要的属性名
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return {data:[]}
     */
    @RequestMapping(method = RequestMethod.GET)
    public void list(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);        
        
        JSONArray listObjects = mdRelDetialMag.listMdRelDetialsAsJson(field,searchColumn, pageDesc);

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
     * 查询单个  表关联细节表 
	
	 * @param relationId  relation_ID
	 * @param parentColumnName  parent_column_Name
     * @param catalogCode 主键
     * 
     * @param response    {@link HttpServletResponse}
     * @return {data:{}}
     */
    @RequestMapping(value = "/{relationId}/{parentColumnName}", method = {RequestMethod.GET})
    public void getMdRelDetial(@PathVariable String relationId,@PathVariable String parentColumnName, HttpServletResponse response) {
    	
    	MdRelDetial mdRelDetial =     			
    			mdRelDetialMag.getObjectById(new com.centit.metaform.po.MdRelDetialId(  relationId, parentColumnName) );
    	
        JsonResultUtils.writeSingleDataJson(mdRelDetial, response);
    }
    
    /**
     * 新增 表关联细节表
     *
     * @param mdRelDetial  {@link MdRelDetial}
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST})
    public void createMdRelDetial(@Valid MdRelDetial mdRelDetial, HttpServletResponse response) {
    	Serializable pk = mdRelDetialMag.saveNewObject(mdRelDetial);
        JsonResultUtils.writeSingleDataJson(pk,response);
    }

    /**
     * 删除单个  表关联细节表 
	
	 * @param relationId  relation_ID
	 * @param parentColumnName  parent_column_Name
     */
    @RequestMapping(value = "/{relationId}/{parentColumnName}", method = {RequestMethod.DELETE})
    public void deleteMdRelDetial(@PathVariable String relationId,@PathVariable String parentColumnName, HttpServletResponse response) {
    	
    	mdRelDetialMag.deleteObjectById(new com.centit.metaform.po.MdRelDetialId(  relationId, parentColumnName) );
    	
        JsonResultUtils.writeBlankJson(response);
    } 
    
    /**
     * 新增或保存 表关联细节表 
    
	 * @param relationId  relation_ID
	 * @param parentColumnName  parent_column_Name
	 * @param mdRelDetial  {@link MdRelDetial}
     * @param response    {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{relationId}/{parentColumnName}", method = {RequestMethod.PUT})
    public void updateMdRelDetial(@PathVariable String relationId,@PathVariable String parentColumnName, 
    	@Valid MdRelDetial mdRelDetial, HttpServletResponse response) {
    	
    	
    	MdRelDetial dbMdRelDetial =     			
    			mdRelDetialMag.getObjectById(new com.centit.metaform.po.MdRelDetialId(  relationId, parentColumnName) );
    	
        

        if (null != mdRelDetial) {
        	dbMdRelDetial .copy(mdRelDetial);
        	mdRelDetialMag.mergeObject(dbMdRelDetial);
        } else {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }

        JsonResultUtils.writeBlankJson(response);
    }
}
