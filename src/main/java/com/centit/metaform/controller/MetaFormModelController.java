package com.centit.metaform.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.service.MetaFormModelManager;


/**
 * MetaFormModel  Controller.
 * create by scaffold 2016-06-02 
 
 * 通用模块管理null   
*/


@Controller
@RequestMapping("/metaform/metaformmodel")
public class MetaFormModelController extends BaseController{
	private static final Log log = LogFactory.getLog(MetaFormModelController.class);
	
	@Resource
	private MetaFormModelManager metaFormModelMag;	
	/*public void setMetaFormModelMag(MetaFormModelManager basemgr)
	{
		metaFormModelMag = basemgr;
		//this.setBaseEntityManager(metaFormModelMag);
	}*/

    /**
     * 查询所有   通用模块管理  列表
     *
     * @param field    json中只保存需要的属性名
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return {data:[]}
     */
    @RequestMapping(method = RequestMethod.GET)
    public void list(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
    	Map<String, Object> searchColumn = convertSearchColumn(request);        
        List<MetaFormModel> listObjects = metaFormModelMag.listObjects(searchColumn, pageDesc);
        SimplePropertyPreFilter simplePropertyPreFilter = null;
        
        if (null == pageDesc) {
            JsonResultUtils.writeSingleDataJson(listObjects, response);
            return;
        }
        ResponseData resData = new ResponseData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);
        if (ArrayUtils.isNotEmpty(field)) {
            simplePropertyPreFilter = new SimplePropertyPreFilter(MetaFormModel.class, field);
            JsonResultUtils.writeResponseDataAsJson(resData, response,simplePropertyPreFilter);
        }
        else{
        	JsonResultUtils.writeResponseDataAsJson(resData, response);
        }
    }
    /**
     * 查询单个  通用模块管理 
	
	 * @param modelCode  Model_Code
     * @param catalogCode 主键
     * 
     * @param response    {@link HttpServletResponse}
     * @return {data:{}}
     */
    @RequestMapping(value = "/{modelCode}", method = {RequestMethod.GET})
    public void getMetaFormModel(@PathVariable String modelCode, HttpServletResponse response) {
    	
    	MetaFormModel metaFormModel =     			
    			metaFormModelMag.getObjectById( modelCode);
        
        JsonResultUtils.writeSingleDataJson(metaFormModel, response);
    }
    
    /**
     * 新增 通用模块管理
     *
     * @param metaFormModel  {@link MetaFormModel}
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST})
    public void createMetaFormModel(@RequestBody @Valid MetaFormModel metaFormModel, HttpServletResponse response) {
    	MetaFormModel model=new MetaFormModel();
    	model.copyNotNullProperty(metaFormModel);
    	Serializable pk = metaFormModelMag.saveNewObject(model);
        JsonResultUtils.writeSingleDataJson(pk,response);
    }

    /**
     * 删除单个  通用模块管理 
	
	 * @param modelCode  Model_Code
     */
    @RequestMapping(value = "/{modelCode}", method = {RequestMethod.DELETE})
    public void deleteMetaFormModel(@PathVariable String modelCode, HttpServletResponse response) {
    	
    	metaFormModelMag.deleteObjectById( modelCode);
        
        JsonResultUtils.writeBlankJson(response);
    } 
    
    /**
     * 新增或保存 通用模块管理 
    
	 * @param modelCode  Model_Code
	 * @param metaFormModel  {@link MetaFormModel}
     * @param response    {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{modelCode}", method = {RequestMethod.PUT})
    public void updateMetaFormModel(@PathVariable String modelCode, 
    		@RequestBody @Valid MetaFormModel metaFormModel, HttpServletResponse response) {
    	
    	
    	MetaFormModel dbMetaFormModel  =     			
    			metaFormModelMag.getObjectById( modelCode);
        
        

        if (null != metaFormModel) {
        	dbMetaFormModel .copyNotNullProperty(metaFormModel);
        	metaFormModelMag.mergeObject(dbMetaFormModel);
        } else {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }

        JsonResultUtils.writeBlankJson(response);
    }
}
