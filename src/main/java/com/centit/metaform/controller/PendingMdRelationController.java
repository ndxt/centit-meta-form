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
import com.centit.metaform.po.PendingMdRelation;
import com.centit.metaform.service.PendingMdRelationManager;


/**
 * PendingMdRelation  Controller.
 * create by scaffold 2016-06-01 
 * @author codefan@sina.com
 * 未落实表关联关系表null   
*/


@Controller
@RequestMapping("/metaform/pendingmdrelation")
public class PendingMdRelationController extends BaseController{
	private static final Log log = LogFactory.getLog(PendingMdRelationController.class);
	
	@Resource
	private PendingMdRelationManager pendingMdRelationMag;	
	/*public void setPendingMdRelationMag(PendingMdRelationManager basemgr)
	{
		pendingMdRelationMag = basemgr;
		//this.setBaseEntityManager(pendingMdRelationMag);
	}*/

    /**
     * 查询所有   未落实表关联关系表  列表
     *
     * @param field    json中只保存需要的属性名
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return {data:[]}
     */
    @RequestMapping(method = RequestMethod.GET)
    public void list(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);        
        
        JSONArray listObjects = pendingMdRelationMag.listPendingMdRelationsAsJson(field,searchColumn, pageDesc);

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
     * 查询单个  未落实表关联关系表 
	
	 * @param relationId  relation_ID
     * @param catalogCode 主键
     * 
     * @param response    {@link HttpServletResponse}
     * @return {data:{}}
     */
    @RequestMapping(value = "/{relationId}", method = {RequestMethod.GET})
    public void getPendingMdRelation(@PathVariable Long relationId, HttpServletResponse response) {
    	
    	PendingMdRelation pendingMdRelation =     			
    			pendingMdRelationMag.getObjectById( relationId);
        
        JsonResultUtils.writeSingleDataJson(pendingMdRelation, response);
    }
    
    /**
     * 新增 未落实表关联关系表
     *
     * @param pendingMdRelation  {@link PendingMdRelation}
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST})
    public void createPendingMdRelation(@Valid PendingMdRelation pendingMdRelation, HttpServletResponse response) {
    	Serializable pk = pendingMdRelationMag.saveNewObject(pendingMdRelation);
        JsonResultUtils.writeSingleDataJson(pk,response);
    }

    /**
     * 删除单个  未落实表关联关系表 
	
	 * @param relationId  relation_ID
     */
    @RequestMapping(value = "/{relationId}", method = {RequestMethod.DELETE})
    public void deletePendingMdRelation(@PathVariable Long relationId, HttpServletResponse response) {
    	
    	pendingMdRelationMag.deleteObjectById( relationId);
        
        JsonResultUtils.writeBlankJson(response);
    } 
    
    /**
     * 新增或保存 未落实表关联关系表 
    
	 * @param relationId  relation_ID
	 * @param pendingMdRelation  {@link PendingMdRelation}
     * @param response    {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{relationId}", method = {RequestMethod.PUT})
    public void updatePendingMdRelation(@PathVariable Long relationId, 
    	@Valid PendingMdRelation pendingMdRelation, HttpServletResponse response) {
    	
    	
    	PendingMdRelation dbPendingMdRelation  =     			
    			pendingMdRelationMag.getObjectById( relationId);
        
        

        if (null != pendingMdRelation) {
        	dbPendingMdRelation .copy(pendingMdRelation);
        	pendingMdRelationMag.mergeObject(dbPendingMdRelation);
        } else {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }

        JsonResultUtils.writeBlankJson(response);
    }
}
