package com.centit.metaform.controller;

import java.sql.SQLException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ContextLoaderListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import com.centit.metaform.formaccess.ModelFormService;
import com.centit.metaform.formaccess.OperationEvent;
import com.centit.metaform.formaccess.impl.JdbcModelRuntimeContext;

@Controller
@RequestMapping("/metaform/formaccess")
public class MetaFormController  extends BaseController{
	//private static final Log log = LogFactory.getLog(MetaFormController.class);

	@Resource(name="jdbcModelFormService")
	private ModelFormService formService;
	
	
	
	@RequestMapping(value = "/{modelCode}/list",method = RequestMethod.GET)
	public void list(@PathVariable String modelCode,boolean addMeta, PageDesc pageDesc ,HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
        JdbcModelRuntimeContext rc = (JdbcModelRuntimeContext) formService.createRuntimeContext(modelCode);
        
        ResponseData resData = new ResponseData();
        resData.addResponseData(OBJLIST, formService.listObjectsByFilter(rc, searchColumn, pageDesc));
        resData.addResponseData(PAGE_DESC, pageDesc);
        rc.close();
        if(addMeta){
        	resData.addResponseData("formModel", rc.getListViewModel()); 
        }
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }
	
	
	
	@RequestMapping(value = "/{modelCode}/view",method = RequestMethod.GET)
	public void view(@PathVariable String modelCode, boolean addMeta, HttpServletRequest request, HttpServletResponse response) {
    	JdbcModelRuntimeContext rc = (JdbcModelRuntimeContext)formService.createRuntimeContext(modelCode);
    	Map<String,Object> jo = rc.fetchPkFromRequest(request);    	
		ResponseData resData = new ResponseData();
		resData.addResponseData("obj", formService.getObjectByProperties(rc, jo));
		
		rc.close();		
		if(addMeta){
        	resData.addResponseData("formModel",  rc.getFormDefine("view")); 
        }
		
		JsonResultUtils.writeResponseDataAsJson(resData, response);
	}
	
	@RequestMapping(value = "/{modelCode}/meta/{metaType}",method = RequestMethod.GET)
	public void meta(@PathVariable String modelCode,  @PathVariable String metaType, 
			HttpServletRequest request, HttpServletResponse response) {
		JdbcModelRuntimeContext rc = (JdbcModelRuntimeContext)formService.createRuntimeContext(modelCode);
		JsonResultUtils.writeSingleDataJson(rc.getFormDefine(metaType), response);
	    rc.close();
	}
	
	@RequestMapping(value = "/{modelCode}/create",method = RequestMethod.GET)
	public void create(@PathVariable String modelCode,
			boolean addMeta, HttpServletRequest request, HttpServletResponse response) {
    	JdbcModelRuntimeContext rc = (JdbcModelRuntimeContext)formService.createRuntimeContext(modelCode);
    	try {
    		ResponseData resData = new ResponseData();
    		resData.addResponseData("obj", formService.createInitialObject(rc));    		
    		rc.close();
			if(addMeta){
	        	resData.addResponseData("formModel",  rc.getFormDefine("create")); 
	        }			
			JsonResultUtils.writeResponseDataAsJson(resData, response);
		} catch (SQLException e) {
			JsonResultUtils.writeErrorMessageJson(e.getMessage(), response);
			rc.rollbackAndClose();
			e.printStackTrace();
		}		
	}
	
	@RequestMapping(value = "/{modelCode}/createpk",method = RequestMethod.GET)
	public void createPk(@PathVariable String modelCode,
			HttpServletRequest request, HttpServletResponse response) {
    	JdbcModelRuntimeContext rc = (JdbcModelRuntimeContext)formService.createRuntimeContext(modelCode);
    	try {
    		ResponseData resData = new ResponseData();
    		resData.addResponseData("pk", formService.createNewPk(rc));    	
			rc.close();
			JsonResultUtils.writeSuccessJson(response);
		} catch (SQLException e) {
			JsonResultUtils.writeErrorMessageJson(e.getMessage(), response);
			rc.rollbackAndClose();
			e.printStackTrace();
		}		
	}
	/**
	 * 
	 * @param rc
	 * @param jo
	 * @param eventType
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	private int runOperationEvent(JdbcModelRuntimeContext rc, JSONObject jo , 
			String eventType , HttpServletResponse response ) throws Exception{
		String eventBeanName = rc.getMetaFormModel().getExtendOptBean();
		if(StringUtils.isBlank(eventBeanName))
				return 0;		
		OperationEvent optEvent = 
	                ContextLoaderListener.getCurrentWebApplicationContext().
	                getBean(eventBeanName,  OperationEvent.class);
		if(optEvent==null)
			return -1;
		switch(eventType){
		case "beforeSave":
			return optEvent.beforeSave(rc, jo, 
					rc.getMetaFormModel().getExtendOptBeanParam(), response);
		case "afterSave":
			return optEvent.afterSave(rc, jo, 
					rc.getMetaFormModel().getExtendOptBeanParam(), response);
		case "beforeUpdate":
			return optEvent.beforeUpdate(rc, jo, 
					rc.getMetaFormModel().getExtendOptBeanParam(), response);
		case "afterUpdate":
			return optEvent.afterUpdate(rc, jo, 
					rc.getMetaFormModel().getExtendOptBeanParam(), response);
		case "beforeDelete":
			return optEvent.beforeDelete(rc, jo, 
					rc.getMetaFormModel().getExtendOptBeanParam(), response);
		case "afterDelete":
			return optEvent.afterDelete(rc, jo, 
					rc.getMetaFormModel().getExtendOptBeanParam(), response);
		case "beforeSubmit":
			return optEvent.beforeSubmit(rc, jo, 
					rc.getMetaFormModel().getExtendOptBeanParam(), response);
		case "afterSubmit":
			return optEvent.afterSubmit(rc, jo, 
					rc.getMetaFormModel().getExtendOptBeanParam(), response);
		default:
			return 0;
		}
	}
	
	@RequestMapping(value = "/{modelCode}/save",method = RequestMethod.POST)
	public void saveNew(@PathVariable String modelCode, @RequestBody String jsonStr, 
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject jo = JSON.parseObject(jsonStr);
    	JdbcModelRuntimeContext rc = (JdbcModelRuntimeContext)formService.createRuntimeContext(modelCode);
    	try {
    		int n = runOperationEvent(rc, jo, "beforeSave", response);
    		if( n<=0 ){
    			formService.saveNewObject(rc, jo);
     			n = runOperationEvent(rc, jo, "afterSave", response);
    		}
    		rc.commitAndClose();			
			if(n<=1)
				JsonResultUtils.writeSuccessJson(response);
		} catch (Exception e) {
			JsonResultUtils.writeErrorMessageJson(e.getMessage(), response);
			rc.rollbackAndClose();
			e.printStackTrace();
		}		
	}
	
	@RequestMapping(value = "/{modelCode}/edit",method = RequestMethod.GET)
	public void edit(@PathVariable String modelCode,boolean addMeta,
			HttpServletRequest request, HttpServletResponse response) {
        
		JdbcModelRuntimeContext rc = (JdbcModelRuntimeContext)formService.createRuntimeContext(modelCode);
    	Map<String,Object> jo = rc.fetchPkFromRequest(request);    	
		ResponseData resData = new ResponseData();
		resData.addResponseData("obj", formService.getObjectByProperties(rc, jo));		
		rc.close();		
		if(addMeta){
        	resData.addResponseData("formModel",  rc.getFormDefine("edit")); 
        }
		
		JsonResultUtils.writeResponseDataAsJson(resData, response);
	}
	
	
	@RequestMapping(value = "/{modelCode}/update",method = RequestMethod.PUT)
	public void update(@PathVariable String modelCode, @RequestBody String jsonStr,  HttpServletRequest request, HttpServletResponse response) {
        
		JSONObject jo = JSON.parseObject(jsonStr);
    	JdbcModelRuntimeContext rc = (JdbcModelRuntimeContext)formService.createRuntimeContext(modelCode);
    	try {
			int n = runOperationEvent(rc, jo, "beforeUpdate", response);
    		if( n<=0 ){
    			formService.updateObject(rc, jo);
     			n = runOperationEvent(rc, jo, "afterUpdate", response);
    		}
    		rc.commitAndClose();			
			if(n<=1)
				JsonResultUtils.writeSuccessJson(response);
		} catch (Exception e) {
			JsonResultUtils.writeErrorMessageJson(e.getMessage(), response);
			rc.rollbackAndClose();
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/{modelCode}/delete",method = RequestMethod.DELETE)
	public void delete(@PathVariable String modelCode,  @RequestBody String jsonStr,  HttpServletRequest request, HttpServletResponse response) {
		JSONObject jo = JSON.parseObject(jsonStr);
    	JdbcModelRuntimeContext rc = (JdbcModelRuntimeContext)formService.createRuntimeContext(modelCode);
    	try {
			int n = runOperationEvent(rc, jo, "beforeDelete", response);
    		if( n<=0 ){
    			formService.deleteObjectById(rc, jo);
     			n = runOperationEvent(rc, jo, "afterDelete", response);
    		}
    		rc.commitAndClose();			
			if(n<=1)
				JsonResultUtils.writeSuccessJson(response);
		} catch (Exception e) {
			JsonResultUtils.writeErrorMessageJson(e.getMessage(), response);
			rc.rollbackAndClose();
			e.printStackTrace();
		}		
	}
	
	@RequestMapping(value = "/{modelCode}/submit",method = RequestMethod.POST)
	public void submit(@PathVariable String modelCode,  @RequestBody String jsonStr,  HttpServletRequest request, HttpServletResponse response) {
       
		
	}
	
	
	@RequestMapping(value = "/multimodelopt",method = RequestMethod.POST)
	public void multiModelOpt(@PathVariable String modelCode,  @RequestBody String jsonStr, 
			HttpServletRequest request, HttpServletResponse response) {
		
	}
	
}
