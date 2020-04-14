package com.centit.metaform.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.DataPowerFilter;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.service.MetaFormModelManager;
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
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.compiler.Lexer;
import com.centit.support.compiler.Pretreatment;
import com.centit.support.database.transaction.JdbcTransaction;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.PersistenceException;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.file.FileType;
import com.centit.support.report.ExcelExportUtil;
import com.centit.workflow.client.service.FlowEngineClient;
import com.centit.workflow.commons.CreateFlowOptions;
import com.centit.workflow.commons.FlowOptParamOptions;
import com.centit.workflow.commons.SubmitOptOptions;
import com.centit.workflow.commons.WorkflowException;
import com.centit.workflow.po.FlowInstance;
import com.centit.workflow.po.NodeInstance;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;

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
    private DataScopePowerManager queryDataScopeFilter;

    @Autowired
    private NotificationCenter notificationCenter;

    @Autowired(required = false)
    private ESIndexer esObjectIndexer;

    @Autowired(required = false)
    private ESSearcher esObjectSearcher;

    private int runJSEvent(MetaFormModel model, Map<String, Object> object,
                           String event, HttpServletRequest request) {
        if (StringUtils.isBlank(model.getExtendOptJs())) {
            return 0;
        }
        MetaTable tableInfo = metaDataCache.getTableInfoAll(model.getTableId());
        JSMateObjectEventRuntime jsMateObjectEvent = new JSMateObjectEventRuntime(
                metaObjectService, databaseRunTime, notificationCenter, model, tableInfo, request);
        jsMateObjectEvent.setFlowEngineClient(flowEngineClient);
        int ret = jsMateObjectEvent.runEvent(event, object);
        if (ret < 0) {
            throw new ObjectException(ret, "外部事件" + event + "运行异常" + model.getExtendOptJs());
        }
        return ret;
    }
    @ApiOperation(value ="单位发送消息接口")
    @RequestMapping(value = "/sendUnitMessage", method = RequestMethod.POST)
    @ApiImplicitParam(
            name = "msgJson", value = "sender:发送人,unitCode:接收单位(必填),modelId:表单ID,pk:业务主键," +
            "title:标题,msg:内容",
            required = true, paramType = "body", dataType = "String"
    )
    @WrapUpResponseBody
    public ResponseData sendUnitMessage(@RequestBody JSONObject msgJson){
        if(null==msgJson.getString("unitCode")) return null;
        return notificationCenter.sendUnitMessage(msgJson.getString("sender"),
                msgJson.getString("unitCode"), false,
                NoticeMessage.create().operation(msgJson.getString("modelId"))
                        .tag(msgJson.getString("pk"))
                        .subject(msgJson.getString("title"))
                        .content(msgJson.getString("msg")));
    }
    @ApiOperation(value = "查询作为子表表单数据列表，不分页；传入的参数为父表的主键")
    @ApiImplicitParams({@ApiImplicitParam(
            name = "modelId", value = "表单模块id",
            required = true, paramType = "path", dataType = "String"
    ), @ApiImplicitParam(
            name = "relationName", value = "字表关联关系名称，注意区分大小写。 " +
            "如果是作为子表模块使用的可以传default，这样会使用RELATION_ID属性获得关联关系",
            required = true, paramType = "path", dataType = "String"
    )})
    @RequestMapping(value = "/{modelId}/tabulation/{relationName}", method = RequestMethod.GET)
    @WrapUpResponseBody
    @JdbcTransaction
    public JSONArray listObjectsAsTabulation(@PathVariable String modelId, @PathVariable String relationName,
                                             HttpServletRequest request) {
        MetaFormModel model = metaFormModelManager.getObjectById(StringUtils.trim(modelId));
        MetaRelation relation = null;
        if(StringUtils.isNotBlank(relationName) && !"default".equals(relationName)) {
            relation = metaDataService.getMetaRelationByName(model.getTableId(), relationName);
        }
        if(relation == null) {
            relation = metaDataService.getMetaRelationById(model.getRelationId());
        }
        if(relation == null) {
           throw new ObjectException(ObjectException.DATA_NOT_FOUND_EXCEPTION,
                   "找不到对应的关联字表信息");
        }
        Map<String, Object> parameters = collectRequestParameters(request);
        Map<String, Object> parentObject = metaObjectService
                .getObjectById(relation.getParentTableId(), parameters);

        JSONArray ja = metaObjectService.listObjectsByProperties(relation.getChildTableId(),
                relation.fetchChildFk(parentObject));

        MetaTable tableInfo = this.metaDataCache.getTableInfo(relation.getChildTableId());
        if ("C".equals(tableInfo.getTableType())) {
            return mapListPoToDto(ja);
        }
        return ja;
    }

    private JSONArray queryObjects(MetaFormModel model, PageDesc pageDesc,
                                               String[] fields, HttpServletRequest request) {
        Map<String, Object> params = collectRequestParameters(request);//convertSearchColumn(request);

        //String optId = FieldType.mapClassName(table.getTableName());
        List<String> filters = queryDataScopeFilter.listUserDataFiltersByOptIdAndMethod(
                WebOptUtils.getCurrentUserCode(request), model.getModelId(), "list");

        MetaTable tableInfo = this.metaDataCache.getTableInfo(model.getTableId());

        String sql = model.getDataFilterSql();
        if (StringUtils.isNotBlank(sql) && StringUtils.equalsIgnoreCase("select", new Lexer(sql).getAWord())) {
            if(WebOptUtils.getCurrentUserInfo(request)==null) {
                throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN,
                        "用户没有登录或者超时，请重新登录！");
            }
            DataPowerFilter dataPowerFilter = queryDataScopeFilter.createUserDataPowerFilter(
                    WebOptUtils.getCurrentUserInfo(request), WebOptUtils.getCurrentUnitCode(request));
            dataPowerFilter.addSourceData(params);
            QueryAndNamedParams qap = dataPowerFilter.translateQuery(sql, filters);
            JSONArray ja = metaObjectService.pageQueryObjects(
                    model.getTableId(), qap.getQuery(), qap.getParams(), pageDesc);
            if ("C".equals(tableInfo.getTableType())) {
                return mapListPoToDto(ja);
            }
            return ja;
        }

        if (StringUtils.isNotBlank(sql)) {
            fields = sql.split(",");
        }

        String extFilter = null;
        if (filters != null) {
            MetaTable table = metaDataCache.getTableInfo(model.getTableId());
            DataPowerFilter dataPowerFilter = queryDataScopeFilter.createUserDataPowerFilter(
                    WebOptUtils.getCurrentUserInfo(request), WebOptUtils.getCurrentUnitCode(request));
            dataPowerFilter.addSourceData(params);
            Map<String, String> tableAlias = new HashMap<>(3);
            tableAlias.put(table.getTableName(), "");
            QueryAndNamedParams qap = dataPowerFilter.translateQueryFilter(
                    tableAlias, filters);
            params.putAll(qap.getParams());
            extFilter = qap.getQuery();
        }
        JSONArray ja = metaObjectService.pageQueryObjects(
                model.getTableId(), extFilter, params, fields, pageDesc);

        if ("C".equals(tableInfo.getTableType())) {
            return mapListPoToDto(ja);
        }
        return ja;
    }

    private JSONArray mapListPoToDto(JSONArray ja ) {
        if(ja == null){
            return null;
        }

        JSONArray jsonArray = new JSONArray(ja.size());
        for (Object json : ja) {
            if(json instanceof Map) {
                jsonArray.add(mapPoToDto((Map<String, Object>) json));
            } else {
                jsonArray.add(json);
            }
        }
        return jsonArray;
    }

    @ApiOperation(value = "分页查询表单数据列表，传入自定义表单模块id")
    @RequestMapping(value = "/{modelId}/list", method = RequestMethod.GET)
    @WrapUpResponseBody
    @JdbcTransaction
    public PageQueryResult<Object> listObjects(@PathVariable String modelId, PageDesc pageDesc,
                                               String[] fields, HttpServletRequest request) {
        MetaFormModel model = metaFormModelManager.getObjectById(StringUtils.trim(modelId));
        //MetaFormModel model1=metaFormModelManager.getObjectByIdAndFile("D:\\D\\Projects\\RunData\\2019-12-24 153156",modelId);
        if(model!=null) {
            JSONArray ja = queryObjects(model, pageDesc, fields, request);
            return PageQueryResult.createJSONArrayResult(ja, pageDesc);
        } else {
            return PageQueryResult.createResult(CollectionsOpt.createList(modelId + "无此表单"), pageDesc);
        }
    }

    @ApiOperation(value = "导出表单数据列表可分页，传入自定义表单模块id")
    @RequestMapping(value = "/{modelId}/export", method = RequestMethod.GET)
    @JdbcTransaction
        public void exportObjects(@PathVariable String modelId, PageDesc pageDesc,
                               String jsonString,
                              HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        MetaFormModel model = metaFormModelManager.getObjectById(StringUtils.trim(modelId));
        Map<String,String> columnName=null;
        if(null!=jsonString){
            columnName=new LinkedHashMap<>();
           String[] a= StringUtils.split(jsonString,";");
           for(int i=0;i<a.length;i++){
               String[] a0=StringUtils.split(a[i],",");
               columnName.put(a0[0],a0[1]);
           }
        }

        JSONArray ja = queryObjects(model, pageDesc, columnName==null?null:columnName.keySet().toArray(new String[0]), request);
        if(ja == null || ja.isEmpty()){
            throw new ObjectException(ResponseData.ERROR_NOT_FOUND, "没有查询到任务数据！");
        }
        List<String> property = new ArrayList<>();
        List<String> header = new ArrayList<>();
        if(null!=columnName){
            property=new ArrayList<>(columnName.keySet());
            Collections.addAll(header,columnName.values().toArray(new String[0]));
        }else {
            MetaTable table = metaDataCache.getTableInfo(model.getTableId());
            Map<String, Object> firstRow = (Map<String, Object>) ja.get(0);
            property = new ArrayList<>(firstRow.keySet());
            for (String p : property) {
                MetaColumn col = table.findFieldByName(p);
                if (col == null && p.endsWith("Desc")) {
                    col = table.findFieldByName(p.substring(0, p.length() - 4));
                }
                header.add(col == null ? p : col.getFieldLabelName());
            }
        }
        InputStream excelStream = ExcelExportUtil.generateExcelStream(ja,
                CollectionsOpt.listToArray(header),  CollectionsOpt.listToArray(property));
        String fileName = URLEncoder.encode(model.getModelName(), "UTF-8") +
                pageDesc.getRowStart()+"-"+pageDesc.getRowEnd()+"-" +pageDesc.getTotalRows() +
                ".xlsx";
        response.setContentType(FileType.mapExtNameToMimeType("xlsx"));
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        IOUtils.copy(excelStream, response.getOutputStream());
    }

    @ApiOperation(value = "全文检索")
    @ApiImplicitParams({@ApiImplicitParam(
            name = "modelId", value = "表单模块id",
            required = true, paramType = "path", dataType = "String"
    ), @ApiImplicitParam(
            name = "query", value = "检索关键字",
            required = true, paramType = "query", dataType = "String"
    )})
    @RequestMapping(value = "/{modelId}/search", method = RequestMethod.GET)
    @WrapUpResponseBody
    public PageQueryResult<Map<String, Object>> searchObject(@PathVariable String modelId,
                                                             HttpServletRequest request, PageDesc pageDesc) {
        if(esObjectSearcher==null){
            throw new ObjectException(ObjectException.SYSTEM_CONFIG_ERROR, "没有正确配置Elastic Search");
        }
        Map<String, Object> queryParam = collectRequestParameters(request);
        Map<String, Object> searchQuery = new HashMap<>(10);
        String queryWord = StringBaseOpt.castObjectToString(queryParam.get("query"));
        MetaFormModel model = metaFormModelManager.getObjectById(StringUtils.trim(modelId));
        searchQuery.put("optId", model.getTableId());
        Object user = queryParam.get("userCode");
        if (user != null) {
            searchQuery.put("userCode", StringBaseOpt.castObjectToString(user));
        }
        Object units = queryParam.get("unitCode");
        if (units != null) {
            searchQuery.put("unitCode", StringBaseOpt.objectToStringArray(units));
        }

        Pair<Long, List<Map<String, Object>>> res =
                esObjectSearcher.search(searchQuery, queryWord, pageDesc.getPageNo(), pageDesc.getPageSize());
        if (res == null) throw new ObjectException("ELK异常");
        pageDesc.setTotalRows(NumberBaseOpt.castObjectToInteger(res.getLeft()));
        return PageQueryResult.createResult(res.getRight(), pageDesc);
    }

    private ObjectDocument mapObjectToDocument(Map<String, Object> object, MetaTable metaTable,
                                               String userCode, String unitCode) {
        ObjectDocument doc = new ObjectDocument();
        doc.setOsId(metaTable.getDatabaseCode());
        doc.setOptId(metaTable.getTableId());
        //Map<String, Object> pkMap = metaTable.fetchObjectPkAsId(object);
        doc.setOptTag(metaTable.fetchObjectPkAsId(object));
        doc.contentObject(object);//.setContent(JSON.toJSONString(object));
        doc.setTitle(Pretreatment.mapTemplateString(metaTable.getObjectTitle(), object));
        doc.setUserCode(userCode);
        doc.setUnitCode(unitCode);
        return doc;
    }

    private void saveFulltextIndex(Map<String, Object> obj, MetaTable metaTable, HttpServletRequest request) {
        //MetaTable metaTable = metaDataCache.getTableInfo(tableId);
        if (esObjectIndexer!=null && metaTable != null &&
                ("T".equals(metaTable.getFulltextSearch())
                        // 用json格式保存在大字段中的内容不能用sql检索，必须用全文检索
                        || "C".equals(metaTable.getTableType()))) {
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

    private void deleteFulltextIndex(Map<String, Object> obj, String tableId) {
        MetaTable metaTable = metaDataCache.getTableInfo(tableId);
        if (esObjectIndexer!=null && metaTable != null &&
                ("T".equals(metaTable.getFulltextSearch())
                        // 用json格式保存在大字段中的内容不能用sql检索，必须用全文检索
                        || "C".equals(metaTable.getTableType()))) {
            try {
                esObjectIndexer.deleteDocument(
                        mapObjectToDocument(obj, metaTable, "", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updataFulltextIndex(Map<String, Object> obj, MetaTable metaTable, HttpServletRequest request) {
        //MetaTable metaTable = metaDataCache.getTableInfo(tableId);
        if (esObjectIndexer!=null && metaTable != null &&
                ("T".equals(metaTable.getFulltextSearch())
                        // 用json格式保存在大字段中的内容不能用sql检索，必须用全文检索
                        || "C".equals(metaTable.getTableType()))) {
            try {
                Map<String, Object> dbObject =
                        metaObjectService.getObjectWithChildren(metaTable.getTableId(), obj, 1);
                esObjectIndexer.mergeDocument(
                        mapObjectToDocument(dbObject, metaTable,
                                WebOptUtils.getCurrentUserCode(request),
                                WebOptUtils.getCurrentUnitCode(request)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void checkUpdateTimeStamp(Map<String, Object> dbObject, Map<String, Object> object) {
        Object oldDate = dbObject.get(MetaTable.UPDATE_CHECK_TIMESTAMP_PROP);
        Object newDate = object.get(MetaTable.UPDATE_CHECK_TIMESTAMP_PROP);
        if (newDate==null || oldDate==null) return;
        if (!DatetimeOpt.equalOnSecond(DatetimeOpt.castObjectToDate(oldDate), DatetimeOpt.castObjectToDate(newDate))) {
            throw new ObjectException(CollectionsOpt.createHashMap(
                    "yourTimeStamp", newDate, "databaseTimeStamp", oldDate),
                    PersistenceException.DATABASE_OUT_SYNC_EXCEPTION, "更新数据对象时，数据版本不同步。");
        }

        object.put(MetaTable.UPDATE_CHECK_TIMESTAMP_PROP, DatetimeOpt.currentSqlDate());
    }

    @ApiOperation(value = "修改表单指定字段;需要修改的字段在url中用参数拼接")
    @RequestMapping(value = "/{modelId}/change", method = RequestMethod.PUT)
    @WrapUpResponseBody
    @JdbcTransaction
    @ApiImplicitParams({@ApiImplicitParam(
            name = "modelId", value = "表单模块id",
            required = true, paramType = "path", dataType = "String"
    ), @ApiImplicitParam(
            name = "jsonString", value = "需要修改的数据对象，一定要包括对象的主键或者工作流业务主键",
            required = true, paramType = "jsonString", dataType = "String"
    )})
    public void updateObjectPart(@PathVariable String modelId,
                                 @RequestBody String jsonString, HttpServletRequest request) {
        Map<String, Object> params = collectRequestParameters(request);//convertSearchColumn(request);
        MetaFormModel model = metaFormModelManager.getObjectById(StringUtils.trim(modelId));
        JSONObject object = JSON.parseObject(jsonString);
        object.putAll(params);
        metaObjectService.updateObjectFields(model.getTableId(), params.keySet(), object);
        // 更改索引
        updataFulltextIndex(object, metaDataCache.getTableInfo(model.getTableId()), request);

        MetaTable tableInfo = metaDataCache.getTableInfo(model.getTableId());
        if (tableInfo.isWriteOptLog()) {
            Map<String, Object> primaryKey = tableInfo.fetchObjectPk(object);
            OperationLogCenter.logUpdateObject(request,
                    modelId, JSON.toJSONString(primaryKey), "change", "修改数据指定字段", params, null);
        }
    }

    @ApiOperation(value = "批量修改数据库表数据；过滤条件在参数中拼接，规则同查询参数")
    @RequestMapping(value = "/{modelId}/batch", method = RequestMethod.PUT)
    @WrapUpResponseBody
    @JdbcTransaction
    public ResponseData batchUpdateObject(@PathVariable String modelId,
                                          @RequestBody String jsonString, HttpServletRequest request) {
        MetaFormModel model = metaFormModelManager.getObjectById(StringUtils.trim(modelId));
        Map<String, Object> params = collectRequestParameters(request);
        JSONObject object = JSON.parseObject(jsonString);
        int ireturn =metaObjectService.updateObjectsByProperties(model.getTableId(), object, params);
        if (ireturn==0){
           return ResponseData.makeErrorMessage("无对应sql生成");
        } else {
            return ResponseData.makeSuccessResponse();
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
        if (userDetails != null) {
            userDetails.put("currentUnitCode", WebOptUtils.getCurrentUnitCode(request));
        }
        parameters.put("currentUser", userDetails);
        MetaFormModel model = metaFormModelManager.getObjectById(StringUtils.trim(modelId));
        Map<String, Object> newObject =
                metaObjectService.makeNewObject(model.getTableId(), parameters);
        runJSEvent(model, newObject, "initNewObject", request);
        return newObject;
    }

    @ApiOperation(value = "获取一个数据带子表，主键作为参数以key-value形式提交," +
            "如果没有指定 fields、parents、children则默认返回所有字段并且返回父表和字表对象")
    @ApiImplicitParams({@ApiImplicitParam(
            name = "modelId", value = "表单模块id",
            required = true, paramType = "path", dataType = "String"
    ), @ApiImplicitParam(
            name = "fields", value = "字段列表，仅返回指定的字段类表，会自动添加主键字段。 String[]",
            required = true, paramType = "query", dataTypeClass = String[].class
    ), @ApiImplicitParam(
            name = "parents", value = "父表属性名列表，可以指定一个或者多个。 String[]",
            required = true, paramType = "query", dataTypeClass = String[].class
    ), @ApiImplicitParam(
            name = "children", value = "子表属性名列表，可以指定一个或者多个。 String[]",
            required = true, paramType = "query", dataTypeClass = String[].class
    )})
    @RequestMapping(value = "/{modelId}", method = RequestMethod.GET)
    @WrapUpResponseBody
    @JdbcTransaction
    public Map<String, Object> getObjectWithChildren(@PathVariable String modelId,
                                                     String [] fields,
                                                     String [] parents, String [] children,
                                                     HttpServletRequest request) {
        MetaFormModel model = metaFormModelManager.getObjectById(StringUtils.trim(modelId));
        if(model.getTableId()==null) return null;
        MetaTable tableInfo = metaDataCache.getTableInfoAll(model.getTableId());
        if (tableInfo==null) return null;
        Map<String, Object> parameters = collectRequestParameters(request);
        if ("C".equals(tableInfo.getTableType())) {
            if (tableInfo.countPkColumn() != 1) {
                String pkCol = tableInfo.getPkFields().get(0).getPropertyName();
                Object pkValue = parameters.get(pkCol);
                Object idValue = parameters.get("_id");
                if (pkValue == null && idValue != null) {
                    parameters.put(pkCol, idValue);
                }
            }
            String optTag = StringBaseOpt.castObjectToString(parameters.get("optTag"));
            if (StringUtils.isNotBlank(optTag)) {
                Map<String, Object> pk = tableInfo.parseObjectPkId(optTag);
                if (pk != null) {
                    parameters.putAll(pk);
                }
            }
        }

        Map<String, Object> objectMap = (fields != null && fields.length>0) ||
                (parents != null && parents.length>0) || (children != null && children.length>0) ?
                metaObjectService.getObjectWithChildren(
                        model.getTableId(), parameters, fields, parents, children)
                : metaObjectService.getObjectWithChildren(model.getTableId(), parameters, 1);

        if ("C".equals(tableInfo.getTableType())) {
            return mapPoToDto(objectMap);
        }
        return objectMap;
    }

    private Map<String, Object> mapPoToDto(Map<String, Object> po) {
        Object obj = po.get(MetaTable.OBJECT_AS_CLOB_PROP);
        if (/*obj!=null && */obj instanceof Map) {
            Map<String, Object> dto = (Map<String, Object>) obj;
            for (Map.Entry<String, Object> ent : po.entrySet()) {
                if (!MetaTable.OBJECT_AS_CLOB_PROP.equals(ent.getKey()) && ent.getValue() != null) {
                    dto.put(ent.getKey(), ent.getValue());
                }
            }
            return dto;
        }
        return po;
    }

    private Map<String, Object> mapDtoToPo(Map<String, Object> dto) {
        Map<String, Object> po = new HashMap<>(dto);
        //Map<String, Object> po = dto;
        po.remove(MetaTable.OBJECT_AS_CLOB_PROP);
        String jsonString = JSON.toJSONString(po);
        po.put(MetaTable.OBJECT_AS_CLOB_PROP, jsonString);
        return po;
    }

    private void innerUpdateObject(MetaFormModel model, MetaTable tableInfo, JSONObject object,
                                   Map<String, Object> dbObject, HttpServletRequest request) {
        Map<String, Object> po = object;
        if ("C".equals(tableInfo.getTableType())) {
            po = mapDtoToPo(object);
        }

        if (tableInfo.isUpdateCheckTimeStamp()) {
            checkUpdateTimeStamp(dbObject, po);
        }
        if (runJSEvent(model, po, "beforeUpdate", request) == 0) {
            metaObjectService.updateObjectWithChildren(model.getTableId(), po);
        }
        // 更改索引
        updataFulltextIndex(object, tableInfo, request);
    }

    @ApiOperation(value = "修改表单数据带子表")
    @RequestMapping(value = "/{modelId}", method = RequestMethod.PUT)
    @WrapUpResponseBody
    @JdbcTransaction
    public void updateObjectWithChildren(@PathVariable String modelId,
                                         @RequestBody String jsonString,
                                         HttpServletRequest request) {
        MetaFormModel model = metaFormModelManager.getObjectById(StringUtils.trim(modelId));
        JSONObject object = JSON.parseObject(jsonString);
        MetaTable tableInfo = metaDataCache.getTableInfo(model.getTableId());
        boolean writeLog = tableInfo.isWriteOptLog();
        Map<String, Object> dbObject = null;
        if (writeLog || tableInfo.isUpdateCheckTimeStamp()) {
            dbObject = metaObjectService.getObjectWithChildren(model.getTableId(), object, 1);
        }
        innerUpdateObject(model, tableInfo, object, dbObject, request);

        if (writeLog) {
            Map<String, Object> primaryKey = tableInfo.fetchObjectPk(object);
            OperationLogCenter.logUpdateObject(request,
                    modelId, JSON.toJSONString(primaryKey), "update", "修改数据对象（子对象）", object, dbObject);
        }
    }

    private void innerSaveObject(MetaFormModel model, MetaTable tableInfo, JSONObject object,
                                 HttpServletRequest request) {
        // 大字段 表格 只保存主键和 jsonObjectField 字段
        Map<String, Object> po = object;
        if ("C".equals(tableInfo.getTableType())) {
            po = mapDtoToPo(object);
        }

        if (tableInfo.isUpdateCheckTimeStamp()) {
            po.put(MetaTable.UPDATE_CHECK_TIMESTAMP_PROP, DatetimeOpt.currentSqlDate());
        }

        JSONObject userDetails = WebOptUtils.getCurrentUserInfo(request);
        Map<String, Object> parameters = collectRequestParameters(request);
        parameters.put("currentUser", userDetails);
        parameters.put("currentUnitCode", WebOptUtils.getCurrentUnitCode(request));

        if (runJSEvent(model, po, "beforeSave", request) == 0) {
            metaObjectService.saveObjectWithChildren(model.getTableId(), po, parameters);
        }

        // 添加索引
        saveFulltextIndex(object, tableInfo, request);

    }

    @ApiOperation(value = "新增表单数据带子表")
    @RequestMapping(value = "/{modelId}", method = RequestMethod.POST)
    @WrapUpResponseBody
    @JdbcTransaction
    public Map<String, Object> saveObjectWithChildren(@PathVariable String modelId,
                                                      @RequestBody String jsonString,
                                                      HttpServletRequest request) {
        MetaFormModel model = metaFormModelManager.getObjectById(StringUtils.trim(modelId));
        JSONObject object = JSON.parseObject(jsonString);
        MetaTable tableInfo = metaDataCache.getTableInfo(model.getTableId());
        innerSaveObject(model, tableInfo, object, request);

        Map<String, Object> primaryKey = tableInfo.fetchObjectPk(object);
        if (tableInfo.isWriteOptLog()) {
            OperationLogCenter.logNewObject(request,
                    modelId, JSON.toJSONString(primaryKey), "save", "保存新的数据对象（包括子对象）", object);
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
        MetaFormModel model = metaFormModelManager.getObjectById(StringUtils.trim(modelId));

        MetaTable tableInfo = metaDataCache.getTableInfo(model.getTableId());
        boolean writeLog = tableInfo.isWriteOptLog();
        Map<String, Object> dbObject = null;
        if (writeLog) {
            dbObject = metaObjectService.getObjectById(model.getTableId(), parameters);
        }

        if (runJSEvent(model, parameters, "beforeDelete", request) == 0) {
            metaObjectService.deleteObjectWithChildren(model.getTableId(), parameters);
        }
        // 删除索引
        deleteFulltextIndex(parameters, model.getTableId());

        if (writeLog) {
            Map<String, Object> primaryKey = tableInfo.fetchObjectPk(parameters);
            OperationLogCenter.logDeleteObject(request,
                    modelId, JSON.toJSONString(primaryKey), "delete", "删除数据对象（包括子对象）", dbObject);
        }
    }

    void fetchWorkflowVariables(FlowOptParamOptions options, MetaFormModel model, Map<String, Object> object) {
        Map<String, Object> variables = new HashMap<>(10);
        Map<String, Object> globalVariables = new HashMap<>(10);

        MetaTable tableInfo = metaDataCache.getTableInfo(model.getTableId());
        List<MetaColumn> columns = tableInfo.getColumns();
        for (MetaColumn col : columns) {
            Object value = object.get(col.getPropertyName());
            if (value != null) {
                if ("1".equals(col.getWorkFlowVariableType())) {
                    variables.put(col.getPropertyName(), value);
                } else if ("2".equals(col.getWorkFlowVariableType())) {
                    globalVariables.put(col.getPropertyName(), value);
                }
            }
        }
        Map<String, List<String>> flowRoleUsers = new HashMap<>(10);
        Map<String, List<String>> flowOrganizes = new HashMap<>(10);
        Map<String, String> nodeUnits = new HashMap<>(10);
        Map<String, Set<String>> nodeOptUsers = new HashMap<>(10);

        for (Map.Entry<String, Object> entry : object.entrySet()) {
            // 办件角色
            if (entry.getKey().startsWith("wfv_")) {
                variables.put(entry.getKey().substring(4), entry.getValue());
            } else if (entry.getKey().startsWith("wfgv_")) {
                globalVariables.put(entry.getKey().substring(5), entry.getValue());
            } else if (entry.getKey().startsWith("wfr_")) {
                flowRoleUsers.put(entry.getKey().substring(4),
                        StringBaseOpt.objectToStringList(entry.getValue()));
            } else if (entry.getKey().startsWith("wfo_")) {
                flowOrganizes.put(entry.getKey().substring(4),
                        StringBaseOpt.objectToStringList(entry.getValue()));
            } else if (entry.getKey().startsWith("wfnd_")) {
                nodeUnits.put(entry.getKey().substring(5),
                        StringBaseOpt.castObjectToString(entry.getValue()));
            } else if (entry.getKey().startsWith("wfnu_")) {
                nodeOptUsers.put(entry.getKey().substring(5),
                        StringBaseOpt.objectToStringSet(entry.getValue()));
            }
        }
        options.setVariables(variables);
        options.setGlobalVariables(globalVariables);
        options.setFlowRoleUsers(flowRoleUsers);
        options.setFlowOrganizes(flowOrganizes);
        options.setNodeUnits(nodeUnits);
        options.setNodeOptUsers(nodeOptUsers);
    }

    static String fetchExtendParam(String paramName, Map<String, Object> object, HttpServletRequest request) {
        String paramValue = request.getParameter(paramName);
        if (StringUtils.isNotBlank(paramValue)) {
            return paramValue;
        }
        paramValue = StringBaseOpt.castObjectToString(object.get(paramName));
        if (StringUtils.isNotBlank(paramValue)) {
            return paramValue;
        } else {
            if ("currentOperatorUserCode".equals(paramName)) {
                object.put("currentOperatorUserCode", WebOptUtils.getCurrentUserCode(request));
            } else if ("currentOperatorUnitCode".equals(paramName)) {
                object.put("currentOperatorUnitCode", WebOptUtils.getCurrentUnitCode(request));
            }
            return StringBaseOpt.castObjectToString(object.get(paramName));
        }
    }

    /**
     * 提交工作流 ; 分两种情况
     * 一： 新建业务，操作流程为， 保存表单，提交流程，这时表单对应的表中flowInstId字段必然为空，所以:
     * 1,创建流程,获取流程实例编号回填到这个表单中
     * 2,提交首节点，进入下一个节点(这个操作同时在流程引擎中执行)
     * 二： 提交节点，操作流程如下:
     * 1, 从工作流引擎中获得待办列表,通过这个待办列表进入节点实例
     * 2, 进入节点实例分两种情况,新建表单或者修改表单,无论是那种情况表单中都有两个字段flowInstId和nodeInstId
     * 3, 保存表单
     * 4, 提交表单
     *
     * @param modelId    模块id
     * @param jsonString 模块对象对应的主键
     */
    @ApiOperation(value = "提交工作流")
    @RequestMapping(value = "/{modelId}/submit", method = RequestMethod.POST)
    @WrapUpResponseBody
    @JdbcTransaction
    public Map<String, Object> submitFlow(@PathVariable String modelId,
                                          @RequestBody String jsonString,
                                          HttpServletRequest request) {
        MetaFormModel model = metaFormModelManager.getObjectById(StringUtils.trim(modelId));
        JSONObject object = JSON.parseObject(jsonString);
        MetaTable tableInfo = metaDataCache.getTableInfo(model.getTableId());
        Map<String, Object> dbObjectPk = tableInfo.fetchObjectPk(object);
        Map<String, Object> dbObject = dbObjectPk == null ? null :
                metaObjectService.getObjectById(model.getTableId(), dbObjectPk);
        // 如果是节点提交 应该有节点实例号
        String nodeInstId = fetchExtendParam("nodeInstId", object, request);
        if (StringUtils.isBlank(nodeInstId)) {
            nodeInstId = StringBaseOpt.castObjectToString(object.get(MetaTable.WORKFLOW_NODE_INST_ID_PROP));
        } else {
            //和流程过程对应的 表单 要写入 节点实例号
            object.put(MetaTable.WORKFLOW_NODE_INST_ID_PROP, nodeInstId);
        }
        String userCode = fetchExtendParam("currentOperatorUserCode", object, request);
        if(userCode.equals("")){
            userCode=StringBaseOpt.castObjectToString(object.get("userCode"));
        }
        String unitCode = fetchExtendParam("currentOperatorUnitCode", object, request);
        if(unitCode.equals("")){
            unitCode=StringBaseOpt.castObjectToString(object.get("unitCode"));
        }
        if (dbObject == null) {
            innerSaveObject(model, tableInfo, object, request);
        } else {
            innerUpdateObject(model, tableInfo, object, dbObject, request);
        }

        if (runJSEvent(model, object, "beforeSubmit", request) != 0) {
            throw new ObjectException("beforeSubmit 执行错误！" + jsonString);
        }

        Object flowInstId = object.get("flowInstId");
        if (flowInstId == null || StringUtils.isBlank(flowInstId.toString())) {
            // create flow instance
            try {
                String flowCode = fetchExtendParam("flowCode", object, request);
                if (StringUtils.isBlank(flowCode)) {
                    flowCode = model.getRelFlowCode();
                }
                if (StringUtils.isBlank(flowCode)) {
                    throw new ObjectException(model, "找不到对应的流程");
                }
                String flowOptTitle = fetchExtendParam("titleTemplate", object, request);
                if (StringUtils.isBlank(flowOptTitle)) {
                    flowOptTitle = model.getFlowOptTitle();
                }
                dbObjectPk = tableInfo.fetchObjectPk(object);

                CreateFlowOptions options = CreateFlowOptions.create().flow(flowCode)
                        .user(userCode)
                        .unit(unitCode)
                        .optName(Pretreatment.mapTemplateString(flowOptTitle, object))
                        .optTag(dbObjectPk.size() == 1 ? StringBaseOpt.castObjectToString(dbObjectPk.values().iterator().next())
                                : JSON.toJSONString(dbObjectPk));

                fetchWorkflowVariables(options, model, object);

                FlowInstance flowInstance = flowEngineClient.createInstance(options);

                object.put(MetaTable.WORKFLOW_INST_ID_PROP, flowInstance.getFlowInstId());
                NodeInstance nodeInstance = flowInstance.getFirstNodeInstance();
                if (nodeInstance != null) {
                    object.put(MetaTable.WORKFLOW_NODE_INST_ID_PROP, nodeInstance.getNodeInstId());
                }
                metaObjectService.updateObjectFields(model.getTableId(),
                        CollectionsOpt.createList(
                                MetaTable.WORKFLOW_INST_ID_PROP,
                                MetaTable.WORKFLOW_NODE_INST_ID_PROP), object);


                runJSEvent(model, object, "afterCreateFlow", request);
            } catch (Exception e) {
                throw new ObjectException(e);
            }
        } else {
            if (StringUtils.isBlank(nodeInstId)) {
                throw new ObjectException(WorkflowException.NodeInstNotFound, "找不到对应的节点实例号！" + jsonString);
            }
            SubmitOptOptions options = SubmitOptOptions.create().nodeInst(nodeInstId)
                    .user(userCode)
                    .unit(unitCode);
            fetchWorkflowVariables(options, model, object);
            // submit flow
            /*Map<String, Object> s = */flowEngineClient.submitOpt(options);
            runJSEvent(model, object, "afterSubmit", request);
        }


        Map<String, Object> primaryKey = tableInfo.fetchObjectPk(object);
        if (tableInfo.isWriteOptLog()) {
            OperationLogCenter.logNewObject(request,
                    modelId, JSON.toJSONString(primaryKey), "submit", "提交流程", object);
        }

        return CollectionsOpt.createHashMap(
                MetaTable.WORKFLOW_INST_ID_PROP, object.get(MetaTable.WORKFLOW_INST_ID_PROP),
                MetaTable.WORKFLOW_NODE_INST_ID_PROP, object.get(MetaTable.WORKFLOW_NODE_INST_ID_PROP));
    }

}
