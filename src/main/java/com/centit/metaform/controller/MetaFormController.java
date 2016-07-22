package com.centit.metaform.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.OptionItem;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import com.centit.metaform.formaccess.MetaFormDefine;
import com.centit.metaform.formaccess.ModelFormService;
import com.centit.metaform.formaccess.ModelRuntimeContext;

@Controller
@RequestMapping("/metaform/formaccess")
public class MetaFormController  extends BaseController{
	//private static final Log log = LogFactory.getLog(MetaFormController.class);

	@Resource(name="modelFormService")
	private ModelFormService formService;	
	
	@RequestMapping(value = "/{modelCode}/list",method = RequestMethod.GET)
	public void list(@PathVariable String modelCode,boolean noMeta, PageDesc pageDesc ,HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = collectRequestParameters(request);//convertSearchColumn(request);
        
        ModelRuntimeContext rc = formService.createRuntimeContext(modelCode);
        rc.setCurrentUserDetails(this.getLoginUser(request));
        
        ResponseData resData = new ResponseData();
        //ListViewDefine metaData = formService.createListViewModel(rc);
        MetaFormDefine metaData = formService.createFormDefine(rc,"view");
        JSONArray objs = formService.listObjectsByFilter(rc, searchColumn, pageDesc);		
        resData.addResponseData(OBJLIST, metaData.transObjectsRefranceData(objs));
        resData.addResponseData(PAGE_DESC, pageDesc);
        rc.close();
        if(! noMeta){
        	resData.addResponseData("formModel", formService.createListViewModel(rc)); 
        }
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }	
	
	@RequestMapping(value = "/{modelCode}/view",method = RequestMethod.GET)
	public void view(@PathVariable String modelCode, boolean noMeta, HttpServletRequest request, HttpServletResponse response) {
		ModelRuntimeContext rc = formService.createRuntimeContext(modelCode);
    	Map<String,Object> jo = rc.fetchPkFromRequest(request);    	
		ResponseData resData = new ResponseData();
		
		JSONObject obj = formService.getObjectByProperties(rc, jo);
		try {
			if(obj!=null){
	    		Map<String,Object> refField = formService.getModelReferenceFields(rc,obj);
	    		obj.putAll(refField);
    		}
		} catch (SQLException e) {
		}
		
		MetaFormDefine metaData = formService.createFormDefine(rc,"view");
		resData.addResponseData("obj", metaData.transObjectRefranceData(obj));
		rc.close();		
		if(!noMeta){
			metaData.updateReadOnlyRefrenceField();
        	resData.addResponseData("formModel", metaData); 
        }
		
		JsonResultUtils.writeResponseDataAsJson(resData, response);
	}
	
	@RequestMapping(value = "/{modelCode}/viewlist",method = RequestMethod.GET)
	public void viewList(@PathVariable String modelCode, boolean noMeta, HttpServletRequest request, HttpServletResponse response) {
		ModelRuntimeContext rc = formService.createRuntimeContext(modelCode);
    	Map<String,Object> jo = rc.fetchPkFromRequest(request);    	
		ResponseData resData = new ResponseData();
		
		JSONObject obj = formService.getObjectByProperties(rc, jo);
		try {
			if(obj!=null){
	    		Map<String,Object> refField = formService.getModelReferenceFields(rc,obj);
	    		obj.putAll(refField);
    		}
		} catch (SQLException e) {
		}
		
		MetaFormDefine metaData = formService.createFormDefine(rc,"viewlist");
		resData.addResponseData("obj", metaData.transObjectRefranceData(obj));
		rc.close();		
		if(!noMeta){
			metaData.updateReadOnlyRefrenceField();
        	resData.addResponseData("formModel", metaData); 
        }
		
		JsonResultUtils.writeResponseDataAsJson(resData, response);
	}
	
	@RequestMapping(value = "/{modelCode}/meta/{metaType}",method = RequestMethod.GET)
	public void meta(@PathVariable String modelCode,  @PathVariable String metaType, 
			HttpServletRequest request, HttpServletResponse response) {
		ModelRuntimeContext rc = formService.createRuntimeContext(modelCode);
		if("list".equals(metaType)){
			JsonResultUtils.writeSingleDataJson(formService.createListViewModel(rc), response);
		}else{
			MetaFormDefine metaData = formService.createFormDefine(rc,metaType);
			metaData.updateReadOnlyRefrenceField();
			JsonResultUtils.writeSingleDataJson(metaData, response);
		}
	    rc.close();
	}
	
	@RequestMapping(value = "/{modelCode}/create",method = RequestMethod.GET)
	public void create(@PathVariable String modelCode,
			boolean noMeta, HttpServletRequest request, HttpServletResponse response) {
    	ModelRuntimeContext rc = formService.createRuntimeContext(modelCode);
    	try {
    		ResponseData resData = new ResponseData();
    		JSONObject obj = formService.createInitialObject(rc);
    		if(obj!=null){
	    		Map<String,Object> refField = formService.getModelReferenceFields(rc,obj);
	    		obj.putAll(refField);
    		}
    		
    		MetaFormDefine metaData = formService.createFormDefine(rc,"create");
    		resData.addResponseData("obj", metaData.transObjectRefranceData(obj));
    		
    		rc.close();
			if(! noMeta){
				metaData.updateReadOnlyRefrenceField();
	        	resData.addResponseData("formModel",metaData); 
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
    	ModelRuntimeContext rc = formService.createRuntimeContext(modelCode);
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
	
	@RequestMapping(value = "/{modelCode}/save",method = RequestMethod.POST)
	public void saveNew(@PathVariable String modelCode, @RequestBody String jsonStr, 
			HttpServletRequest request, HttpServletResponse response) {
		
		if(jsonStr==null || jsonStr.length()<2){
        	JsonResultUtils.writeErrorMessageJson("数据格式不正确。", response);
        	return;
        }
        if('['!=jsonStr.charAt(0) && '{'!=jsonStr.charAt(0)){
        	JsonResultUtils.writeErrorMessageJson("数据格式不正确。", response);
        	return;
        }
        
    	ModelRuntimeContext rc = formService.createRuntimeContext(modelCode);
    	try {
    		if('['==jsonStr.charAt(0)){
	    		JSONArray jo = JSON.parseArray(jsonStr);
	    		for(Object ja:jo)
	    			formService.mergeObject(rc, (JSONObject)ja,response);
	
				JsonResultUtils.writeSuccessJson(response);
    		}else{
	    		JSONObject jo = JSON.parseObject(jsonStr);
				int n = formService.saveNewObject(rc, jo,response);

				if(n<=1)
					JsonResultUtils.writeSuccessJson(response);
    		}   		
     		rc.commitAndClose();
		} catch (Exception e) {
			JsonResultUtils.writeErrorMessageJson(e.getMessage(), response);
			rc.rollbackAndClose();
			e.printStackTrace();
		}		
	}
	
	@RequestMapping(value = "/{modelCode}/edit",method = RequestMethod.GET)
	public void edit(@PathVariable String modelCode,boolean noMeta,
			HttpServletRequest request, HttpServletResponse response) {
        
		ModelRuntimeContext rc = formService.createRuntimeContext(modelCode);
    	Map<String,Object> jo = rc.fetchPkFromRequest(request);    	
		ResponseData resData = new ResponseData();
		JSONObject obj = formService.getObjectByProperties(rc, jo);
		if(obj!=null){
			try {				
	    		Map<String,Object> refField = formService.getModelReferenceFields(rc,obj);
	    		obj.putAll(refField);    		
			} catch (SQLException e) {
			}
		}
		
		MetaFormDefine metaData = formService.createFormDefine(rc,"edit");
		resData.addResponseData("obj", metaData.transObjectRefranceData(obj));
		
		rc.close();		
		if(!noMeta){
			metaData.updateReadOnlyRefrenceField();
        	resData.addResponseData("formModel", metaData); 
        }
		
		JsonResultUtils.writeResponseDataAsJson(resData, response);
	}
	
	
	@RequestMapping(value = "/{modelCode}/update",method = RequestMethod.PUT)
	public void update(@PathVariable String modelCode, @RequestBody String jsonStr,  HttpServletRequest request, HttpServletResponse response) {
        if(jsonStr==null || jsonStr.length()<2){
        	JsonResultUtils.writeErrorMessageJson("数据格式不正确。", response);
        	return;
        }
        if('['!=jsonStr.charAt(0) && '{'!=jsonStr.charAt(0)){
        	JsonResultUtils.writeErrorMessageJson("数据格式不正确。", response);
        	return;
        }
		
    	ModelRuntimeContext rc = formService.createRuntimeContext(modelCode);
    	try {
    		if('['==jsonStr.charAt(0)){
	    		JSONArray jo = JSON.parseArray(jsonStr);
	    		for(Object ja:jo)
	    			formService.mergeObject(rc, (JSONObject)ja,response);
	     		
				JsonResultUtils.writeSuccessJson(response);
    		}else{
	    		JSONObject jo = JSON.parseObject(jsonStr);
				int n = formService.updateObject(rc, jo,response);
				if(n<=1)
					JsonResultUtils.writeSuccessJson(response);
    		}
    		rc.commitAndClose();
		} catch (Exception e) {
			JsonResultUtils.writeErrorMessageJson(e.getMessage(), response);
			rc.rollbackAndClose();
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/{modelCode}/delete",method = RequestMethod.DELETE)
	public void delete(@PathVariable String modelCode,  @RequestBody String jsonStr,  
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject jo = JSON.parseObject(jsonStr);
    	ModelRuntimeContext rc = formService.createRuntimeContext(modelCode);
    	try {
			int n = formService.deleteObjectById(rc, jo,response);
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
		update(modelCode, jsonStr, request, response);
	}
	
	
	@RequestMapping(value = "/multimodelopt",method = RequestMethod.POST)
	public void multiModelOpt(@PathVariable String modelCode,  @RequestBody String jsonStr, 
			HttpServletRequest request, HttpServletResponse response) {
		
	}	
	
	@RequestMapping(value = "/{modelCode}/{propertyName}",method = RequestMethod.GET)
	public void asyncReferenceData(@PathVariable String modelCode,
			@PathVariable String propertyName,String startGroup,
			HttpServletRequest request, HttpServletResponse response) {
        
		ModelRuntimeContext rc = formService.createRuntimeContext(modelCode);
    	List<OptionItem> options = formService.getAsyncReferenceData(
    			rc, propertyName, startGroup);		
		JsonResultUtils.writeSingleDataJson(options, response);
	}
}
