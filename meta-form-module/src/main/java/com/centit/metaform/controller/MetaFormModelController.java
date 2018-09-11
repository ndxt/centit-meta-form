package com.centit.metaform.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.BaseController;
import com.centit.metaform.dao.ModelDataFieldDao;
import com.centit.metaform.dao.ModelOperationDao;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.po.ModelDataField;
import com.centit.metaform.po.ModelOperation;
import com.centit.metaform.service.MetaFormModelManager;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;


/**
 * MetaFormModel  Controller.
 * create by scaffold 2016-06-02 
 
 * 通用模块管理null   
*/


@Controller
@RequestMapping("/metaform/metaformmodel")
public class MetaFormModelController extends BaseController{
    //private static final Log log = LogFactory.getLog(MetaFormModelController.class);

    @Resource
    private MetaFormModelManager metaFormModelMag;
    /*public void setMetaFormModelMag(MetaFormModelManager basemgr)
    {
        metaFormModelMag = basemgr;
        //this.setBaseEntityManager(metaFormModelMag);
    }*/
    @Resource
    private ModelDataFieldDao modelDataFieldDao;

    @Resource
    private ModelOperationDao modelOperationDao;

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
        JSONArray listObjects = metaFormModelMag.listObjectsAsJson(searchColumn, pageDesc);
        JSONArray jsonObjects = metaFormModelMag.addTableNameToList(listObjects);
        SimplePropertyPreFilter simplePropertyPreFilter = null;
        
        if (null == pageDesc) {
            JsonResultUtils.writeSingleDataJson(jsonObjects, response);
            return;
        }
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, jsonObjects);
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
     * @param response    {@link HttpServletResponse}
     * @return {data:{}}
     */
    @RequestMapping(value = "/{modelCode}", method = {RequestMethod.GET})
    public void getMetaFormModel(@PathVariable String modelCode, HttpServletResponse response) {

        MetaFormModel metaFormModel = metaFormModelMag.getObjectById( modelCode);
        Set<ModelDataField> modelDataFields =
                new HashSet<>(metaFormModelMag.listModelDataFields(modelCode));
        Set<ModelOperation> modelOperations =
                new HashSet<>(modelOperationDao.listObjectsByProperty("modelCode", modelCode));

        metaFormModel.setModelDataFields(modelDataFields);
        metaFormModel.setModelOperations(modelOperations);

        JSONObject modelResult = JSONObject.parseObject(JSONObject.toJSONString(metaFormModel));
        modelResult.put("lastModifyDate",metaFormModel.getLastModifyDate().toString());

        JsonResultUtils.writeSingleDataJson(modelResult, response);
    }
    
    /**
     * 新增 通用模块管理
     *
     * @param metaFormModel  {@link MetaFormModel}
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST})
    public void createMetaFormModel(@RequestBody @Valid MetaFormModel metaFormModel,
             HttpServletRequest request, HttpServletResponse response) {
        MetaFormModel model=new MetaFormModel();
        String usercode = getLoginUserCode(request);
        model.copyNotNullProperty(metaFormModel);
        model.setRecorder(usercode);
        model.setLastModifyDate(new Date());
        metaFormModelMag.saveNewObject(model);

        Set<ModelDataField> modelDataFields = model.getModelDataFields();
        if (modelDataFields != null && modelDataFields.size()>0) {
            Iterator<ModelDataField> itr= modelDataFields.iterator();
            while(itr.hasNext()){
                ModelDataField tempDataField = itr.next();
                modelDataFieldDao.saveNewObject(tempDataField);
            }
        }

        Set<ModelOperation> modelOperations = model.getModelOperations();
        if (modelOperations != null && modelOperations.size()>0) {
            Iterator<ModelOperation> itr= modelOperations.iterator();
            while(itr.hasNext()){
                ModelOperation tempOperation = itr.next();
                modelOperationDao.saveNewObject(tempOperation);
            }
        }

        JsonResultUtils.writeSingleDataJson(model.getModelCode(),response);
    }

    /**
     * 删除单个  通用模块管理 

     * @param modelCode  Model_Code
     */
    @RequestMapping(value = "/{modelCode}", method = {RequestMethod.DELETE})
    public void deleteMetaFormModel(@PathVariable String modelCode, HttpServletResponse response) {

        metaFormModelMag.deleteObjectById( modelCode);
        
        JsonResultUtils.writeSuccessJson(response);
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
        MetaFormModel dbMetaFormModel = metaFormModelMag.getObjectById( modelCode);
        if (null != metaFormModel) {
            dbMetaFormModel.copyNotNullProperty(metaFormModel);
            dbMetaFormModel.setLastModifyDate(new Date());
            metaFormModelMag.updateMetaFormModel(dbMetaFormModel);

            Map<String, Object> tempFilter = new HashMap<>();
            tempFilter.put("modelCode", modelCode);
            modelDataFieldDao.deleteObjectsByProperties(tempFilter);
            modelOperationDao.deleteObjectsByProperties(tempFilter);

            Set<ModelDataField> modelDataFields = metaFormModel.getModelDataFields();
            if (modelDataFields != null && modelDataFields.size()>0) {
                Iterator<ModelDataField> itr= modelDataFields.iterator();
                while(itr.hasNext()){
                    ModelDataField tempDataField = itr.next();
                    modelDataFieldDao.saveNewObject(tempDataField);
                }
            }

            Set<ModelOperation> modelOperations = metaFormModel.getModelOperations();
            if (modelOperations != null && modelOperations.size()>0) {
                Iterator<ModelOperation> itr= modelOperations.iterator();
                while(itr.hasNext()){
                    ModelOperation tempOperation = itr.next();
                    modelOperationDao.saveNewObject(tempOperation);
                }
            }
        } else {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }

        JsonResultUtils.writeBlankJson(response);
    }
}
