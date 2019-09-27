package com.centit.metaform.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.ObjectException;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.DataPowerFilter;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.service.MetaFormModelManager;
import com.centit.metaform.service.QueryDataScopeFilter;
import com.centit.product.metadata.po.MetaColumn;
import com.centit.product.metadata.po.MetaRelation;
import com.centit.product.metadata.po.MetaTable;
import com.centit.product.metadata.service.DatabaseRunTime;
import com.centit.product.metadata.service.MetaDataCache;
import com.centit.product.metadata.service.MetaDataService;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.search.document.ObjectDocument;
import com.centit.search.service.Impl.ESIndexer;
import com.centit.search.service.Impl.ESSearcher;
import com.centit.support.algorithm.*;
import com.centit.support.compiler.Lexer;
import com.centit.support.compiler.Pretreatment;
import com.centit.support.database.transaction.JdbcTransaction;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.workflow.client.service.FlowEngineClient;
import com.centit.workflow.commons.WorkflowException;
import com.centit.workflow.po.FlowInstance;
import com.centit.workflow.po.NodeInstance;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/formaccess")
@Api(value = "自定义表单数据处理", tags = "自定义表单数据处理")
public class MetaFormController extends BaseController {
    protected static final Logger logger = LoggerFactory.getLogger(MetaFormController.class);

    @Autowired
    private MetaFormModelManager metaFormModelManager;

    @Autowired
    private MetaObjectService metaObjectService;

    @Autowired
    private DatabaseRunTime databaseRunTime;

    @Autowired
    private MetaDataService metaDataService;

    @Autowired
    private MetaDataCache metaDataCache;

    @Autowired
    private FlowEngineClient flowEngineClient;

    @Autowired
    private QueryDataScopeFilter queryDataScopeFilter;

    @Autowired(required=false)
    private ESIndexer esObjectIndexer;

    @Autowired(required=false)
    private ESSearcher esObjectSearcher;

    private int runJSEvent(String js, Map<String, Object> object, String event, HttpServletRequest request){
        if(StringUtils.isBlank(js)){
            return 0;
        }

        JSMateObjectEventRuntime jsMateObjectEvent = new JSMateObjectEventRuntime(
                metaObjectService, databaseRunTime, js, request);
        return jsMateObjectEvent.runEvent(event,object);
    }

    @ApiOperation(value = "查询作为字表表单数据列表，不分页；传入的参数为父表的主键")
    @RequestMapping(value = "/{modelId}/tabulation", method = RequestMethod.GET)
    @WrapUpResponseBody
    @JdbcTransaction
    public JSONArray listObjectsAsTabulation(@PathVariable String modelId, HttpServletRequest request) {
        MetaFormModel model = metaFormModelManager.getObjectById(modelId);
        Map<String, Object> parameters = collectRequestParameters(request);
        MetaRelation relation = metaDataService.getMetaRelationById(model.getRelationId());
        Map<String, Object> parentObject = metaObjectService
                .getObjectById(relation.getParentTableId(), parameters);

        return metaObjectService.listObjectsByProperties(relation.getChildTableId(),
                relation.fetchObjectFk(parentObject));
    }

    @ApiOperation(value = "分页查询表单数据列表")
    @RequestMapping(value = "/{modelId}/list", method = RequestMethod.GET)
    @WrapUpResponseBody
    @JdbcTransaction
    public PageQueryResult<Object> listObjects(@PathVariable String modelId, PageDesc pageDesc,
                                               String [] fields, HttpServletRequest request) {
        Map<String, Object> params = collectRequestParameters(request);//convertSearchColumn(request);
        MetaFormModel model = metaFormModelManager.getObjectById(modelId);
        //String optId = FieldType.mapClassName(table.getTableName());
        List<String> filters = queryDataScopeFilter.listUserDataFiltersByOptIdAndMethod(
                WebOptUtils.getCurrentUserCode(request), modelId, "list");

        String sql = model.getDataFilterSql();
        if(StringUtils.isNotBlank(sql) && StringUtils.equalsIgnoreCase("select",new Lexer(sql).getAWord())) {
            DataPowerFilter dataPowerFilter = queryDataScopeFilter.createUserDataPowerFilter(
                    WebOptUtils.getCurrentUserInfo(request), WebOptUtils.getCurrentUnitCode(request));
            dataPowerFilter.addSourceDatas(params);
            QueryAndNamedParams qap = dataPowerFilter.translateQuery(sql, filters);
            JSONArray ja = metaObjectService.pageQueryObjects(
                    model.getTableId(), qap.getQuery(), qap.getParams(), pageDesc);
            return PageQueryResult.createJSONArrayResult(ja, pageDesc);
        }

        if(StringUtils.isNotBlank(sql)) {
            fields = sql.split(",");
        }

        String extFilter = null;
        if(filters !=null) {
            MetaTable table = metaDataCache.getTableInfo(model.getTableId());
            DataPowerFilter dataPowerFilter = queryDataScopeFilter.createUserDataPowerFilter(
                    WebOptUtils.getCurrentUserInfo(request), WebOptUtils.getCurrentUnitCode(request));
            dataPowerFilter.addSourceDatas(params);
            Map<String, String> tableAlias = new HashMap<>(3);
            tableAlias.put(table.getTableName(),"");
            QueryAndNamedParams qap = dataPowerFilter.translateQueryFilter(
                    tableAlias, filters) ;
            params.putAll(qap.getParams());
            extFilter = qap.getQuery();
        }
        JSONArray ja = metaObjectService.pageQueryObjects(
                model.getTableId(), extFilter, params, fields, pageDesc);
        return PageQueryResult.createJSONArrayResult(ja, pageDesc);

    }

    @ApiOperation(value = "全文检索")
    @ApiImplicitParams({@ApiImplicitParam(
            name = "modelId", value="表单id",
            required=true, paramType = "path", dataType ="String"
    ),@ApiImplicitParam(
            name = "query", value="检索关键字",
            required=true, paramType = "query", dataType ="String"
    )})
    @RequestMapping(value = "/{modelId}/search", method = RequestMethod.GET)
    @WrapUpResponseBody
    public PageQueryResult<Map<String, Object>> searchObject(@PathVariable String modelId,
                             @RequestParam("query") String query, PageDesc pageDesc) {
        Pair<Long, List<Map<String, Object>>> res =
                esObjectSearcher.searchOpt(modelId, query, pageDesc.getPageNo(), pageDesc.getPageSize());
        pageDesc.setTotalRows(NumberBaseOpt.castObjectToInteger(res.getLeft()));
        return PageQueryResult.createResult(res.getRight(), pageDesc);
    }

    private ObjectDocument mapObjectToDocument(Map<String, Object> object, MetaTable metaTable,
                                               String userCode, String unitCode){
        ObjectDocument doc = new ObjectDocument();
        doc.setOptId(metaTable.getTableId());
        doc.setOptTag(JSON.toJSONString(metaTable.fetchObjectPk(object)));
        doc.setContent(JSON.toJSONString(object));
        doc.setOsId(metaTable.getDatabaseCode());
        doc.setUserCode(userCode);
        doc.setUserCode(unitCode);
        return doc;
    }

    private void saveFulltextIndex(Map<String, Object> obj, String tableId, HttpServletRequest request){
        MetaTable metaTable = metaDataCache.getTableInfo(tableId);
        if(metaTable != null && "T".equals(metaTable.getFulltextSearch())) {
            try {
                esObjectIndexer.saveNewDocument(
                    mapObjectToDocument(obj, metaTable,
                            WebOptUtils.getCurrentUserCode(request),
                            WebOptUtils.getCurrentUnitCode(request)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteFulltextIndex(Map<String, Object> obj, String tableId){
        MetaTable metaTable = metaDataCache.getTableInfo(tableId);
        if(metaTable != null && "T".equals(metaTable.getFulltextSearch())) {
            try {
                esObjectIndexer.deleteDocument(
                        mapObjectToDocument(obj, metaTable, "", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updataFulltextIndex(Map<String, Object> obj, String tableId, HttpServletRequest request){
        MetaTable metaTable = metaDataCache.getTableInfo(tableId);
        if(metaTable != null && "T".equals(metaTable.getFulltextSearch())) {
            try {
                Map<String, Object> dbObject =
                        metaObjectService.getObjectWithChildren(tableId, obj, 1);
                esObjectIndexer.mergeDocument(
                        mapObjectToDocument(dbObject, metaTable,
                                WebOptUtils.getCurrentUserCode(request),
                                WebOptUtils.getCurrentUnitCode(request)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void checkUpdateTimeStamp(Map<String, Object> dbObject, Map<String, Object> object){
        Object oldDate = dbObject.get(MetaTable.UPDATE_CHECK_TIMESTAMP_PROP);
        Object newDate = object.get(MetaTable.UPDATE_CHECK_TIMESTAMP_PROP);
        if(!GeneralAlgorithm.equals(oldDate, newDate)){
            throw new ObjectException(CollectionsOpt.createHashMap(
                    "yourTimeStamp",newDate,"databaseTimeStamp",oldDate),
                    ObjectException.DATABASE_OUT_SYNC_EXCEPTION,"更新数据对象时，数据版本不同步。");
        }

        object.put(MetaTable.UPDATE_CHECK_TIMESTAMP_PROP, DatetimeOpt.currentSqlDate());
    }


    @ApiOperation(value = "修改表单指定字段")
    @RequestMapping(value = "/{modelId}/change", method = RequestMethod.PUT)
    @WrapUpResponseBody
    @JdbcTransaction
    public void updateObjectPart(@PathVariable String modelId,
                                 @RequestBody String jsonString, HttpServletRequest request) {
        Map<String, Object> params = collectRequestParameters(request);//convertSearchColumn(request);
        MetaFormModel model = metaFormModelManager.getObjectById(modelId);
        JSONObject object = JSON.parseObject(jsonString);
        object.putAll(params);
        metaObjectService.updateObjectFields(model.getTableId(), params.keySet(), object);
        // 更改索引
        updataFulltextIndex(object, model.getTableId(), request);

        MetaTable tableInfo = metaDataCache.getTableInfo(model.getTableId());
        if(StringRegularOpt.isTrue(tableInfo.getWriteOptLog())){
            Map<String, Object> primaryKey = tableInfo.fetchObjectPk(object);
            OperationLogCenter.logUpdateObject(WebOptUtils.getCurrentUserCode(request),
                    modelId,JSON.toJSONString(primaryKey),"change","修改数据指定字段",params, null);
        }
    }

    @ApiOperation(value = "获取一个数据带子表，主键作为参数以key-value形式提交")
    @RequestMapping(value = "/{modelId}/new", method = RequestMethod.GET)
    @WrapUpResponseBody
    @JdbcTransaction
    public Map<String, Object> makeNewObject(@PathVariable String modelId,
                                                     HttpServletRequest request) {
        Map<String, Object> parameters = collectRequestParameters(request);
        JSONObject userDetails = WebOptUtils.getCurrentUserInfo(request);
        if(userDetails != null){
            userDetails.put("currentUnitCode", WebOptUtils.getCurrentUnitCode(request));
        }
        parameters.put("currentUser", userDetails);
        MetaFormModel model = metaFormModelManager.getObjectById(modelId);
        return metaObjectService.makeNewObject(model.getTableId(), parameters);
    }

    @ApiOperation(value = "获取一个数据带子表，主键作为参数以key-value形式提交")
    @RequestMapping(value = "/{modelId}", method = RequestMethod.GET)
    @WrapUpResponseBody
    @JdbcTransaction
    public Map<String, Object> getObjectWithChildren(@PathVariable String modelId,
                                         HttpServletRequest request) {
        Map<String, Object> parameters = collectRequestParameters(request);
        MetaFormModel model = metaFormModelManager.getObjectById(modelId);
        return metaObjectService.getObjectWithChildren(model.getTableId(), parameters, 1);
    }

    private void innerUpdateObject(MetaFormModel model, MetaTable tableInfo, JSONObject object,
                                   Map<String, Object> dbObject, HttpServletRequest request ){
        if(StringRegularOpt.isTrue(tableInfo.getUpdateCheckTimeStamp())){
            checkUpdateTimeStamp(dbObject, object);
        }

        if(runJSEvent(model.getExtendOptJs(), object, "beforeUpdate", request)==0) {
            metaObjectService.updateObjectWithChildren(model.getTableId(), object);
        }
        // 更改索引
        updataFulltextIndex(object, model.getTableId(), request);
    }

    @ApiOperation(value = "修改表单数据带子表")
    @RequestMapping(value = "/{modelId}", method = RequestMethod.PUT)
    @WrapUpResponseBody
    @JdbcTransaction
    public void updateObjectWithChildren(@PathVariable String modelId,
                                     @RequestBody String jsonString,
                                         HttpServletRequest request) {
        MetaFormModel model = metaFormModelManager.getObjectById(modelId);
        JSONObject object = JSON.parseObject(jsonString);
        MetaTable tableInfo = metaDataCache.getTableInfo(model.getTableId());
        boolean writeLog =  StringRegularOpt.isTrue(tableInfo.getWriteOptLog());
        Map<String, Object> dbObject = null;
        if(writeLog || StringRegularOpt.isTrue(tableInfo.getUpdateCheckTimeStamp())) {
            dbObject = metaObjectService.getObjectWithChildren(model.getTableId(), object, 1);
        }
        innerUpdateObject(model,tableInfo,object, dbObject, request);

        if(writeLog){
            Map<String, Object> primaryKey = tableInfo.fetchObjectPk(object);
            OperationLogCenter.logUpdateObject(WebOptUtils.getCurrentUserCode(request),
                    modelId,JSON.toJSONString(primaryKey),"update","修改数据对象（子对象）",object, dbObject);
        }
    }

    private void innerSaveObject(MetaFormModel model, MetaTable tableInfo, JSONObject object,
                                 HttpServletRequest request ){

        if(StringRegularOpt.isTrue(tableInfo.getUpdateCheckTimeStamp())){
            object.put(MetaTable.UPDATE_CHECK_TIMESTAMP_PROP, DatetimeOpt.currentSqlDate());
        }

        JSONObject userDetails = WebOptUtils.getCurrentUserInfo(request);
        if(userDetails != null){
            userDetails.put("currentUnitCode", WebOptUtils.getCurrentUnitCode(request));
        }
        Map<String, Object> parameters = collectRequestParameters(request);
        parameters.put("currentUser", userDetails);
        if(runJSEvent(model.getExtendOptJs(), object, "beforeSave", request)==0) {
            metaObjectService.saveObjectWithChildren(model.getTableId(), object, parameters);
        }
        // 添加索引
        saveFulltextIndex(object,model.getTableId(),request);
    }

    @ApiOperation(value = "新增表单数据带子表")
    @RequestMapping(value = "/{modelId}", method = RequestMethod.POST)
    @WrapUpResponseBody
    @JdbcTransaction
    public Map<String, Object> saveObjectWithChildren(@PathVariable String modelId,
                                   @RequestBody String jsonString,
                                       HttpServletRequest request) {
        MetaFormModel model = metaFormModelManager.getObjectById(modelId);
        JSONObject object = JSON.parseObject(jsonString);
        MetaTable tableInfo = metaDataCache.getTableInfo(model.getTableId());
        innerSaveObject(model,tableInfo,object,request);

        Map<String, Object> primaryKey = tableInfo.fetchObjectPk(object);
        if(StringRegularOpt.isTrue(tableInfo.getWriteOptLog())){
            OperationLogCenter.logNewObject(WebOptUtils.getCurrentUserCode(request),
                    modelId,JSON.toJSONString(primaryKey),"save","保存新的数据对象（包括子对象）", object);
        }
        return primaryKey;
    }

    @ApiOperation(value = "删除表单数据带子表")
    @RequestMapping(value = "/{modelId}", method = RequestMethod.DELETE)
    @WrapUpResponseBody
    @JdbcTransaction
    public void deleteObjectWithChildren(@PathVariable String modelId,
                                     HttpServletRequest request) {
        Map<String, Object> parameters = collectRequestParameters(request);
        MetaFormModel model = metaFormModelManager.getObjectById(modelId);

        MetaTable tableInfo = metaDataCache.getTableInfo(model.getTableId());
        boolean writeLog =  StringRegularOpt.isTrue(tableInfo.getWriteOptLog());
        Map<String, Object> dbObject = null;

        if(writeLog) {
            dbObject = metaObjectService.getObjectById(model.getTableId(), parameters);
        }

        if(runJSEvent(model.getExtendOptJs(), parameters, "beforeDelete", request)==0) {
            metaObjectService.deleteObjectWithChildren(model.getTableId(), parameters);
        }
        // 删除索引
        deleteFulltextIndex(parameters,model.getTableId());

        if(writeLog){
            Map<String, Object> primaryKey = tableInfo.fetchObjectPk(parameters);
            OperationLogCenter.logDeleteObject(WebOptUtils.getCurrentUserCode(request),
                    modelId,JSON.toJSONString(primaryKey),"delete","删除数据对象（包括子对象）", dbObject);
        }
    }

    // 这个 设置流程变量的时机 需要考虑，应该不仅仅实在 流程提交，在表单保存时也应该修改
    private void setWorkflowVariables(MetaFormModel model, Map<String, Object> object) throws Exception {
        //MetaTable tableInfo = metaObjectService.getTableInfo(model.getTableId());
        String flowInstId = StringBaseOpt.castObjectToString(object.get(MetaTable.WORKFLOW_INST_ID_PROP));
        if(flowInstId == null){
            throw new ObjectException(object,
                    ObjectException.NULL_EXCEPTION,"工作流实例号为空。");
        }
        String nodeInstId = StringBaseOpt.castObjectToString(object.get(MetaTable.WORKFLOW_NODE_INST_ID_PROP));
        //List<MetaColumn> columns = metaDataCache.listMetaColumns(model.getTableId());
        MetaTable tableInfo = metaDataCache.getTableInfo(model.getTableId());
        List<MetaColumn> columns = tableInfo.getColumns();
        for(MetaColumn col : columns) {
            Object value = object.get(col.getColumnName());
            if(value != null) {
                if ("1".equals(col.getWorkFlowVariableType())) {
                    flowEngineClient.saveFlowVariable(flowInstId, col.getColumnName(),
                            StringBaseOpt.castObjectToString(value));
                } else if ("2".equals(col.getWorkFlowVariableType()) && nodeInstId != null) {
                    flowEngineClient.saveFlowNodeVariable(nodeInstId, col.getColumnName(),
                            StringBaseOpt.castObjectToString(value));
                }
            }
        }
        for(String skey : object.keySet()){
            // 办件角色
            if(skey.startsWith("flowRole")){
                flowEngineClient.assignFlowWorkTeam(flowInstId, skey.substring(9),
                        StringBaseOpt.objectToStringList(object.get(skey)));
            }
        }
    }

    private static String fetchExtendParam(String paramName, JSONObject object, HttpServletRequest request){
        String paramValue = request.getParameter(paramName);
        if(StringUtils.isNotBlank(paramValue)){
            return paramValue;
        }
        paramValue = object.getString(paramName);
        if(StringUtils.isNotBlank(paramValue)){
            return paramValue;
        }

        if("userCode".equals(paramName)){
            return WebOptUtils.getCurrentUserCode(request);
        } else if("unitCode".equals(paramName)){
            return WebOptUtils.getCurrentUnitCode(request);
        }

        return null;
    }
    /**
     * 提交工作流 ; 分两种情况
     * 一： 新建业务，操作流程为， 保存表单，提交流程，这时表单对应的表中flowInstId字段必然为空，所以:
     *  1,创建流程,获取流程实例编号回填到这个表单中
     *  2,提交首节点，进入下一个节点(这个操作同时在流程引擎中执行)
     * 二： 提交节点，操作流程如下:
     *  1, 从工作流引擎中获得待办列表,通过这个待办列表进入节点实例
     *  2, 进入节点实例分两种情况,新建表单或者修改表单,无论是那种情况表单中都有两个字段flowInstId和nodeInstId
     *  3, 保存表单
     *  4, 提交表单
     * @param modelId 模块id
     * @param jsonString 模块对象对应的主键
     */
    @ApiOperation(value = "提交工作流")
    @RequestMapping(value = "/{modelId}/submit", method = RequestMethod.POST)
    @WrapUpResponseBody
    @JdbcTransaction
    public Map<String, Object> submitFlow(@PathVariable String modelId,
                           @RequestBody String jsonString,
                           HttpServletRequest request) {
        MetaFormModel model = metaFormModelManager.getObjectById(modelId);
        JSONObject object = JSON.parseObject(jsonString);
        MetaTable tableInfo = metaDataCache.getTableInfo(model.getTableId());
        Map<String, Object> dbObjectPk = tableInfo.fetchObjectPk(object);
        Map<String, Object> dbObject = dbObjectPk==null? null :
                metaObjectService.getObjectById(model.getTableId(), dbObjectPk);

        if(dbObject == null){
            innerSaveObject(model,tableInfo,object,request);
        } else {
            innerUpdateObject(model,tableInfo,object,dbObject,request);
        }

        if(runJSEvent(model.getExtendOptJs(), object, "beforeSubmit", request)!=0){
            throw new ObjectException("beforeSubmit 执行错误！" + jsonString);
        }

        Object flowInstId = object.get("flowInstId");
        if(flowInstId==null || StringUtils.isBlank(flowInstId.toString())){
            // create flow instance
            try {
                // 这个接口需要修改，需要和 flowEngine 一致
                // TODO 从工作流中 找到模块对应的业务流程代码
                /*FlowInstance flowInstance = flowEngineClient.createMetaFormFlowAndSubmit(model.getModelId(),
                        Pretreatment.mapTemplateString(model.getFlowOptTitle(),object),// 这边需要添加一个title表达式
                        object.get(tableInfo.getPkColumns().get(0).toLowerCase()).toString(),
                        "U0000019",
                        "D00005"
                        //WebOptUtils.getCurrentUserCode(request),
                        //WebOptUtils.getCurrentUnitCode(request)
                );*/
                dbObjectPk = tableInfo.fetchObjectPk(object);

                FlowInstance flowInstance = flowEngineClient.createInstance(model.getRelFlowCode(),
                        Pretreatment.mapTemplateString(model.getFlowOptTitle(), object),// 这边需要添加一个title表达式
                        dbObjectPk.size()==1? StringBaseOpt.castObjectToString(dbObjectPk.values().iterator().next())
                                :JSON.toJSONString(dbObjectPk),
                        fetchExtendParam("userCode", object, request),
                        fetchExtendParam("unitCode", object, request));

                object.put(MetaTable.WORKFLOW_INST_ID_PROP, flowInstance.getFlowInstId());
                NodeInstance nodeInstance = flowInstance.getFirstNodeInstance();
                if(nodeInstance != null) {
                    object.put(MetaTable.WORKFLOW_NODE_INST_ID_PROP, nodeInstance.getNodeInstId());
                }
                metaObjectService.updateObjectFields(model.getTableId(),
                        CollectionsOpt.createList(
                                MetaTable.WORKFLOW_INST_ID_PROP,
                                MetaTable.WORKFLOW_NODE_INST_ID_PROP), object);
                //  设置流程变量
                setWorkflowVariables(model, object);
            } catch (Exception e) {
                throw new ObjectException(e);
            }
        } else {
            String nodeInstId = StringBaseOpt.castObjectToString(object.get(MetaTable.WORKFLOW_NODE_INST_ID_PROP));
            if(nodeInstId == null){
                throw new ObjectException(WorkflowException.NodeInstNotFound,"找不到对应的节点实例号！" + jsonString);
            }
            try {
                setWorkflowVariables(model, object);
                // submit flow
                flowEngineClient.submitOpt(nodeInstId , WebOptUtils.getCurrentUserCode(request),
                        WebOptUtils.getCurrentUnitCode(request), null, null);
            } catch (Exception e) {
                throw new ObjectException(e);
            }
        }


        Map<String, Object> primaryKey = tableInfo.fetchObjectPk(object);
        if(StringRegularOpt.isTrue(tableInfo.getWriteOptLog())){
            OperationLogCenter.logNewObject(WebOptUtils.getCurrentUserCode(request),
                    modelId,JSON.toJSONString(primaryKey),"submit","提交流程", object);
        }

        return CollectionsOpt.createHashMap(
                MetaTable.WORKFLOW_INST_ID_PROP,object.get(MetaTable.WORKFLOW_INST_ID_PROP),
                MetaTable.WORKFLOW_NODE_INST_ID_PROP,object.get(MetaTable.WORKFLOW_NODE_INST_ID_PROP));
    }

}
