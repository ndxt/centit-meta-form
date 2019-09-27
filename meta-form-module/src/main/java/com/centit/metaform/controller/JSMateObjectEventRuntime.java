package com.centit.metaform.controller;

import com.centit.framework.common.ObjectException;
import com.centit.product.dataopt.utils.JSRuntimeContext;
import com.centit.product.metadata.po.MetaTable;
import com.centit.product.metadata.service.DatabaseRunTime;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.workflow.client.service.FlowEngineClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class JSMateObjectEventRuntime {

    private static final Logger logger = LoggerFactory.getLogger(JSMateObjectEventRuntime.class);
    private MetaObjectService metaObjectService;
    private JSRuntimeContext jsRuntimeContext;
    private FlowEngineClient flowEngineClient;
    private Map<String, Object> bizModel;
    private DatabaseRunTime databaseRunTime;
    private HttpServletRequest request;
    private String javaScript;

    public JSMateObjectEventRuntime(MetaObjectService metaObjectService,
                                    DatabaseRunTime databaseRunTime,
                                    String js,
                                    HttpServletRequest request){
        this.metaObjectService = metaObjectService;
        this.databaseRunTime = databaseRunTime;
        this.javaScript = js;
        this.request = request;
    }

    /**
     * 运行js事件
     * @param eventFunc 时间方法名称
     * @param bizModel 业务数据对象
     * @return 0 表示正常， &lg;0 表示报错， &gt;0 表示事件已经处理好所有任务，无需在做额外的数据库操作
     */
    public int runEvent(String eventFunc, Map<String, Object> bizModel)  {
        if(jsRuntimeContext == null){
            jsRuntimeContext = new JSRuntimeContext();
        }

        if(StringUtils.isNotBlank(javaScript)){
            jsRuntimeContext.compileScript(javaScript);
        }
        this.bizModel = bizModel;
        try {
            Object retObj = jsRuntimeContext.callJsFunc(eventFunc, this, bizModel);
            return NumberBaseOpt.castObjectToInteger(retObj, 0);
        } catch (ScriptException e) {
            throw new ObjectException(ObjectException.UNKNOWN_EXCEPTION,
                    e.getMessage());
        } catch (NoSuchMethodException e) {
            logger.info(e.getMessage());
            return 0;
        }
    }

    public void setJsRuntimeContext(JSRuntimeContext jsRuntimeContext) {
        this.jsRuntimeContext = jsRuntimeContext;
    }

    public MetaObjectService getMetaObjectService() {
        return this.metaObjectService;
    }

    public DatabaseRunTime getDatabaseRunTime() {
        return this.databaseRunTime;
    }

    public void setFlowVariable(String name, Object value){
        String flowInstId = StringBaseOpt.castObjectToString(this.bizModel.get(MetaTable.WORKFLOW_INST_ID_PROP));
        try {
            flowEngineClient.saveFlowVariable(flowInstId, name,
                    StringBaseOpt.castObjectToString(value));
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    public void setFlowLocalVariable(String name, Object value){
        String nodeInstId = StringBaseOpt.castObjectToString(this.bizModel.get(MetaTable.WORKFLOW_NODE_INST_ID_PROP));
        try {
            flowEngineClient.saveFlowNodeVariable(nodeInstId, name,
                    StringBaseOpt.castObjectToString(value));
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    public void setFlowWorkRole(String roleCode, String userCode){
        String flowInstId = StringBaseOpt.castObjectToString(this.bizModel.get(MetaTable.WORKFLOW_INST_ID_PROP));
        try {
            flowEngineClient.assignFlowWorkTeam(flowInstId, roleCode,
                    CollectionsOpt.createList(userCode));
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    public void submitOpt(){
        String nodeInstId = StringBaseOpt.castObjectToString(this.bizModel.get(MetaTable.WORKFLOW_NODE_INST_ID_PROP));
        try {
            flowEngineClient.submitOpt(nodeInstId ,
                    MetaFormController.fetchExtendParam("userCode", this.bizModel, request),
                    MetaFormController.fetchExtendParam("unitCode", this.bizModel, request),
                    null);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    public Object getRequestAttribute(String name){
        Object pvalue = this.request.getAttribute(name);
        if(pvalue == null || StringUtils.isBlank(StringBaseOpt.castObjectToString(pvalue))){
            pvalue = request.getParameter(name);
        }
        return pvalue;
    }

    public Object getSessionAttribute(String name){
        return this.request.getSession().getAttribute(name);
    }

    public void setSessionAttribute(String name, Object value){
        this.request.getSession().setAttribute(name, value);
    }

    public void setFlowEngineClient(FlowEngineClient flowEngineClient) {
        this.flowEngineClient = flowEngineClient;
    }
}
