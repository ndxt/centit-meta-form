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
import com.centit.metaform.po.MdRelation;
import com.centit.metaform.service.MdRelationManager;


/**
 * MdRelation  Controller.
 * create by scaffold 2016-06-02 
 * @author codefan@sina.com
 * 表关联关系表null   
*/


@Controller
@RequestMapping("/metaform/mdrelation")
public class MdRelationController extends BaseController{
	private static final Log log = LogFactory.getLog(MdRelationController.class);
	
	@Resource
	private MdRelationManager mdRelationMag;	
	/*public void setMdRelationMag(MdRelationManager basemgr)
	{
		mdRelationMag = basemgr;
		//this.setBaseEntityManager(mdRelationMag);
	}*/

    /**
     * 查询所有   表关联关系表  列表
     *
     * @param field    json中只保存需要的属性名
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return {data:[]}
     */
    @RequestMapping(method = RequestMethod.GET)
    public void list(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);        
        
        JSONArray listObjects = mdRelationMag.listMdRelationsAsJson(field,searchColumn, pageDesc);

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
     * 查询单个  表关联关系表 
	
	 * @param relationId  relation_ID
     * @param catalogCode 主键
     * 
     * @param response    {@link HttpServletResponse}
     * @return {data:{}}
     */
    @RequestMapping(value = "/{relationId}", method = {RequestMethod.GET})
    public void getMdRelation(@PathVariable Long relationId, HttpServletResponse response) {
    	
    	MdRelation mdRelation =     			
    			mdRelationMag.getObjectById( relationId);
        
        JsonResultUtils.writeSingleDataJson(mdRelation, response);
    }
    
    /**
     * 新增 表关联关系表
     *
     * @param mdRelation  {@link MdRelation}
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST})
    public void createMdRelation(@Valid MdRelation mdRelation, HttpServletResponse response) {
    	Serializable pk = mdRelationMag.saveNewObject(mdRelation);
        JsonResultUtils.writeSingleDataJson(pk,response);
    }

    /**
     * 删除单个  表关联关系表 
	
	 * @param relationId  relation_ID
     */
    @RequestMapping(value = "/{relationId}", method = {RequestMethod.DELETE})
    public void deleteMdRelation(@PathVariable Long relationId, HttpServletResponse response) {
    	
    	mdRelationMag.deleteObjectById( relationId);
        
        JsonResultUtils.writeBlankJson(response);
    } 
    
    /**
     * 新增或保存 表关联关系表 
    
	 * @param relationId  relation_ID
	 * @param mdRelation  {@link MdRelation}
     * @param response    {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{relationId}", method = {RequestMethod.PUT})
    public void updateMdRelation(@PathVariable Long relationId, 
    	@Valid MdRelation mdRelation, HttpServletResponse response) {
    	
    	
    	MdRelation dbMdRelation  =     			
    			mdRelationMag.getObjectById( relationId);
        
        

        if (null != mdRelation) {
        	dbMdRelation .copy(mdRelation);
        	mdRelationMag.mergeObject(dbMdRelation);
        } else {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }

        JsonResultUtils.writeBlankJson(response);
    }
}
