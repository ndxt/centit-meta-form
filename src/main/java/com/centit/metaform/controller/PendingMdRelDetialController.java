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
import com.centit.metaform.po.PendingMdRelDetial;
import com.centit.metaform.service.PendingMdRelDetialManager;


/**
 * PendingMdRelDetial  Controller.
 * create by scaffold 2016-06-01 
 * @author codefan@sina.com
 * 未落实表关联细节表null   
*/


@Controller
@RequestMapping("/metaform/pendingmdreldetial")
public class PendingMdRelDetialController extends BaseController{
	private static final Log log = LogFactory.getLog(PendingMdRelDetialController.class);
	
	@Resource
	private PendingMdRelDetialManager pendingMdRelDetialMag;	
	/*public void setPendingMdRelDetialMag(PendingMdRelDetialManager basemgr)
	{
		pendingMdRelDetialMag = basemgr;
		//this.setBaseEntityManager(pendingMdRelDetialMag);
	}*/

    /**
     * 查询所有   未落实表关联细节表  列表
     *
     * @param field    json中只保存需要的属性名
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return {data:[]}
     */
    @RequestMapping(method = RequestMethod.GET)
    public void list(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);        
        
        JSONArray listObjects = pendingMdRelDetialMag.listPendingMdRelDetialsAsJson(field,searchColumn, pageDesc);

        if (null == pageDesc) {
            JsonResultUtils.writeSingleDataJson(listObjects, response);
            return;
        }
        
        ResponseData resData = new ResponseData();
        resData.addResponseData(OBJECT, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }
    
    /**
     * 查询单个  未落实表关联细节表 
	
	 * @param relationId  relation_ID
	 * @param parentColumnName  parent_column_Name
     * @param catalogCode 主键
     * 
     * @param response    {@link HttpServletResponse}
     * @return {data:{}}
     */
    @RequestMapping(value = "/{relationId}/{parentColumnName}", method = {RequestMethod.GET})
    public void getPendingMdRelDetial(@PathVariable String relationId,@PathVariable String parentColumnName, HttpServletResponse response) {
    	
    	PendingMdRelDetial pendingMdRelDetial =     			
    			pendingMdRelDetialMag.getObjectById(new com.centit.metaform.po.PendingMdRelDetialId(  relationId, parentColumnName) );
    	
        JsonResultUtils.writeSingleDataJson(pendingMdRelDetial, response);
    }
    
    /**
     * 新增 未落实表关联细节表
     *
     * @param pendingMdRelDetial  {@link PendingMdRelDetial}
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST})
    public void createPendingMdRelDetial(@Valid PendingMdRelDetial pendingMdRelDetial, HttpServletResponse response) {
    	Serializable pk = pendingMdRelDetialMag.saveNewObject(pendingMdRelDetial);
        JsonResultUtils.writeSingleDataJson(pk,response);
    }

    /**
     * 删除单个  未落实表关联细节表 
	
	 * @param relationId  relation_ID
	 * @param parentColumnName  parent_column_Name
     */
    @RequestMapping(value = "/{relationId}/{parentColumnName}", method = {RequestMethod.DELETE})
    public void deletePendingMdRelDetial(@PathVariable String relationId,@PathVariable String parentColumnName, HttpServletResponse response) {
    	
    	pendingMdRelDetialMag.deleteObjectById(new com.centit.metaform.po.PendingMdRelDetialId(  relationId, parentColumnName) );
    	
        JsonResultUtils.writeBlankJson(response);
    } 
    
    /**
     * 新增或保存 未落实表关联细节表 
    
	 * @param relationId  relation_ID
	 * @param parentColumnName  parent_column_Name
	 * @param pendingMdRelDetial  {@link PendingMdRelDetial}
     * @param response    {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{relationId}/{parentColumnName}", method = {RequestMethod.PUT})
    public void updatePendingMdRelDetial(@PathVariable String relationId,@PathVariable String parentColumnName, 
    	@Valid PendingMdRelDetial pendingMdRelDetial, HttpServletResponse response) {
    	
    	
    	PendingMdRelDetial dbPendingMdRelDetial =     			
    			pendingMdRelDetialMag.getObjectById(new com.centit.metaform.po.PendingMdRelDetialId(  relationId, parentColumnName) );
    	
        

        if (null != pendingMdRelDetial) {
        	dbPendingMdRelDetial .copy(pendingMdRelDetial);
        	pendingMdRelDetialMag.mergeObject(dbPendingMdRelDetial);
        } else {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }

        JsonResultUtils.writeBlankJson(response);
    }
}
