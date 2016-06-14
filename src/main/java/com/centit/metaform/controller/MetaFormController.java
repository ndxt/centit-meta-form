package com.centit.metaform.controller;

import java.sql.SQLException;
import java.util.HashMap;
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
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import com.centit.metaform.formaccess.ModelFormService;
import com.centit.metaform.formaccess.impl.JdbcModelRuntimeContext;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.metadata.TableField;

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
	
	
	
	@RequestMapping(value = "/{modelCode}/get",method = RequestMethod.GET)
	public void view(@PathVariable String modelCode, boolean addMeta, HttpServletRequest request, HttpServletResponse response) {
    	JdbcModelRuntimeContext rc = (JdbcModelRuntimeContext)formService.createRuntimeContext(modelCode);
    	Map<String,Object> jo = new HashMap<>();
    	for(String pk: rc.getTableInfo().getPkColumns()){
    		TableField pkp = rc.getTableInfo().findFieldByColumn(pk);
    		Object pv = request.getParameter(pkp.getPropertyName());
    		switch(pkp.getJavaType()){
			case "Date":
			case "Timestamp":
    			jo.put(pkp.getPropertyName(), DatetimeOpt.castObjectToDate(pv));
    			break;
			case "Long":
    			jo.put(pkp.getPropertyName(), NumberBaseOpt.castObjectToLong(pv));
    			break;
			case "Double":
    			jo.put(pkp.getPropertyName(), NumberBaseOpt.castObjectToDouble(pv));
    			break;
			default:
    			jo.put(pkp.getPropertyName(), StringBaseOpt.objectToString(pv));
    			break;
    		}
    	}
		ResponseData resData = new ResponseData();
		resData.addResponseData("fields", rc.getFormModle("view").getFilters());
		resData.addResponseData("obj", formService.getObjectByProperties(rc, jo));
		rc.close();
		JsonResultUtils.writeResponseDataAsJson(resData, response);
	}
	
	@RequestMapping(value = "/{modelCode}/meta/{metaType}",method = RequestMethod.GET)
	public void meta(@PathVariable String modelCode,  @PathVariable String metaType, 
			HttpServletRequest request, HttpServletResponse response) {
		JdbcModelRuntimeContext rc = (JdbcModelRuntimeContext)formService.createRuntimeContext(modelCode);
		JsonResultUtils.writeSingleDataJson(rc.getFormModle(metaType), response);
	    rc.close();
	}
	
	@RequestMapping(value = "/{modelCode}/create",method = RequestMethod.GET)
	public void create(@PathVariable String modelCode, @RequestBody String jsonStr,
			boolean createPk, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jo = JSON.parseObject(jsonStr);
    	JdbcModelRuntimeContext rc = (JdbcModelRuntimeContext)formService.createRuntimeContext(modelCode);
    	try {
			formService.saveNewObject(rc, jo);
			rc.commitAndClose();
			JsonResultUtils.writeSuccessJson(response);
		} catch (SQLException e) {
			JsonResultUtils.writeErrorMessageJson(e.getMessage(), response);
			rc.rollbackAndClose();
			e.printStackTrace();
		}		
	}
	
	@RequestMapping(value = "/{modelCode}/createpk",method = RequestMethod.GET)
	public void createPk(@PathVariable String modelCode, @RequestBody String jsonStr,  HttpServletRequest request, HttpServletResponse response) {
		JSONObject jo = JSON.parseObject(jsonStr);
    	JdbcModelRuntimeContext rc = (JdbcModelRuntimeContext)formService.createRuntimeContext(modelCode);
    	try {
			formService.saveNewObject(rc, jo);
			rc.commitAndClose();
			JsonResultUtils.writeSuccessJson(response);
		} catch (SQLException e) {
			JsonResultUtils.writeErrorMessageJson(e.getMessage(), response);
			rc.rollbackAndClose();
			e.printStackTrace();
		}		
	}
	
	@RequestMapping(value = "/{modelCode}/save",method = RequestMethod.POST)
	public void saveNew(@PathVariable String modelCode, @RequestBody String jsonStr,  HttpServletRequest request, HttpServletResponse response) {
		JSONObject jo = JSON.parseObject(jsonStr);
    	JdbcModelRuntimeContext rc = (JdbcModelRuntimeContext)formService.createRuntimeContext(modelCode);
    	try {
			formService.saveNewObject(rc, jo);
			rc.commitAndClose();
			JsonResultUtils.writeSuccessJson(response);
		} catch (SQLException e) {
			JsonResultUtils.writeErrorMessageJson(e.getMessage(), response);
			rc.rollbackAndClose();
			e.printStackTrace();
		}		
	}
	
	@RequestMapping(value = "/{modelCode}/edit",method = RequestMethod.GET)
	public void edit(@PathVariable String modelCode, @RequestBody String jsonStr,  HttpServletRequest request, HttpServletResponse response) {
        
		JSONObject jo = JSON.parseObject(jsonStr);
    	JdbcModelRuntimeContext rc = (JdbcModelRuntimeContext)formService.createRuntimeContext(modelCode);
    	try {
			formService.updateObject(rc, jo);
			rc.commitAndClose();
			JsonResultUtils.writeSuccessJson(response);
		} catch (SQLException e) {
			JsonResultUtils.writeErrorMessageJson(e.getMessage(), response);
			rc.rollbackAndClose();
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping(value = "/{modelCode}/update",method = RequestMethod.PUT)
	public void update(@PathVariable String modelCode, @RequestBody String jsonStr,  HttpServletRequest request, HttpServletResponse response) {
        
		JSONObject jo = JSON.parseObject(jsonStr);
    	JdbcModelRuntimeContext rc = (JdbcModelRuntimeContext)formService.createRuntimeContext(modelCode);
    	try {
			formService.updateObject(rc, jo);
			rc.commitAndClose();
			JsonResultUtils.writeSuccessJson(response);
		} catch (SQLException e) {
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
			formService.deleteObjectById(rc, jo);
			rc.commitAndClose();
			JsonResultUtils.writeSuccessJson(response);
		} catch (SQLException e) {
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
