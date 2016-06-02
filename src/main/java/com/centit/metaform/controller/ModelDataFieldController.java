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
import com.centit.metaform.po.ModelDataField;
import com.centit.metaform.service.ModelDataFieldManager;


/**
 * ModelDataField  Controller.
 * create by scaffold 2016-06-02 
 
 * 数据模板字段null   
*/


@Controller
@RequestMapping("/metaform/modeldatafield")
public class ModelDataFieldController extends BaseController{
	private static final Log log = LogFactory.getLog(ModelDataFieldController.class);
	
	@Resource
	private ModelDataFieldManager modelDataFieldMag;	
	/*public void setModelDataFieldMag(ModelDataFieldManager basemgr)
	{
		modelDataFieldMag = basemgr;
		//this.setBaseEntityManager(modelDataFieldMag);
	}*/

    /**
     * 查询所有   数据模板字段  列表
     *
     * @param field    json中只保存需要的属性名
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return {data:[]}
     */
    @RequestMapping(method = RequestMethod.GET)
    public void list(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);        
        
        JSONArray listObjects = modelDataFieldMag.listModelDataFieldsAsJson(field,searchColumn, pageDesc);

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
     * 查询单个  数据模板字段 
	
	 * @param modelCode  Model_Code
	 * @param columnName  column_Name
     * @param catalogCode 主键
     * 
     * @param response    {@link HttpServletResponse}
     * @return {data:{}}
     */
    @RequestMapping(value = "/{modelCode}/{columnName}", method = {RequestMethod.GET})
    public void getModelDataField(@PathVariable String modelCode,@PathVariable String columnName, HttpServletResponse response) {
    	
    	ModelDataField modelDataField =     			
    			modelDataFieldMag.getObjectById(new com.centit.metaform.po.ModelDataFieldId(  modelCode, columnName) );
    	
        JsonResultUtils.writeSingleDataJson(modelDataField, response);
    }
    
    /**
     * 新增 数据模板字段
     *
     * @param modelDataField  {@link ModelDataField}
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST})
    public void createModelDataField(@Valid ModelDataField modelDataField, HttpServletResponse response) {
    	Serializable pk = modelDataFieldMag.saveNewObject(modelDataField);
        JsonResultUtils.writeSingleDataJson(pk,response);
    }

    /**
     * 删除单个  数据模板字段 
	
	 * @param modelCode  Model_Code
	 * @param columnName  column_Name
     */
    @RequestMapping(value = "/{modelCode}/{columnName}", method = {RequestMethod.DELETE})
    public void deleteModelDataField(@PathVariable String modelCode,@PathVariable String columnName, HttpServletResponse response) {
    	
    	modelDataFieldMag.deleteObjectById(new com.centit.metaform.po.ModelDataFieldId(  modelCode, columnName) );
    	
        JsonResultUtils.writeBlankJson(response);
    } 
    
    /**
     * 新增或保存 数据模板字段 
    
	 * @param modelCode  Model_Code
	 * @param columnName  column_Name
	 * @param modelDataField  {@link ModelDataField}
     * @param response    {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{modelCode}/{columnName}", method = {RequestMethod.PUT})
    public void updateModelDataField(@PathVariable String modelCode,@PathVariable String columnName, 
    	@Valid ModelDataField modelDataField, HttpServletResponse response) {
    	
    	
    	ModelDataField dbModelDataField =     			
    			modelDataFieldMag.getObjectById(new com.centit.metaform.po.ModelDataFieldId(  modelCode, columnName) );
    	
        

        if (null != modelDataField) {
        	dbModelDataField .copy(modelDataField);
        	modelDataFieldMag.mergeObject(dbModelDataField);
        } else {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }

        JsonResultUtils.writeBlankJson(response);
    }
}
