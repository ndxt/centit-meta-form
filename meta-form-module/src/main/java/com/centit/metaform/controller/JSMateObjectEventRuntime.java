package com.centit.metaform.controller;

import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.metaform.po.MetaFormModel;
import com.centit.support.extend.JSRuntimeContext;
import com.centit.product.metadata.po.MetaTable;
import com.centit.product.metadata.service.DatabaseRunTime;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.workflow.commons.SubmitOptOptions;
import com.centit.workflow.service.FlowEngine;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class JSMateObjectEventRuntime {

    private static final Logger logger = LoggerFactory.getLogger(JSMateObjectEventRuntime.class);
    private MetaObjectService metaObjectService;
    private JSRuntimeContext jsRuntimeContext;
    private FlowEngine flowEngine;
    private Map<String, Object> bizModel;
    private DatabaseRunTime databaseRunTime;
    private HttpServletRequest request;
    private String javaScript;
    private NotificationCenter notificationCenter;
    private MetaFormModel metaModel;
    private MetaTable tableInfo;

    public JSMateObjectEventRuntime(MetaObjectService metaObjectService,
                                    DatabaseRunTime databaseRunTime,
                                    NotificationCenter notificationCenter,
                                    MetaFormModel model, MetaTable tableInfo,
                                    HttpServletRequest request){
        this.metaObjectService = metaObjectService;
        this.databaseRunTime = databaseRunTime;
        this.notificationCenter = notificationCenter;
        this.metaModel = model;
        this.tableInfo = tableInfo;
        this.javaScript = model.getExtendOptJs();
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
            flowEngine.saveFlowVariable(flowInstId, name,
                    StringBaseOpt.castObjectToString(value));
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    public void setFlowLocalVariable(String name, Object value){
        String nodeInstId = StringBaseOpt.castObjectToString(this.bizModel.get(MetaTable.WORKFLOW_NODE_INST_ID_PROP));
        try {
            flowEngine.saveFlowNodeVariable(nodeInstId, name,
                    StringBaseOpt.castObjectToString(value));
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    public void setFlowWorkRole(String roleCode, String userCode){
        String flowInstId = StringBaseOpt.castObjectToString(this.bizModel.get(MetaTable.WORKFLOW_INST_ID_PROP));
        try {
            flowEngine.assignFlowWorkTeam(flowInstId, roleCode,
                    CollectionsOpt.createList(userCode));
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    public void sendMessage(String sender,List<String> userCodes, String title, String msg){
        notificationCenter.sendMessage(sender, userCodes,
                NoticeMessage.create().operation(metaModel.getModelId())
                        .tag(this.tableInfo.fetchObjectPkAsId(bizModel))
                        .subject(title)
                        .content(msg));
    }

    public void sendUnitMessage(String sender,String unitCode, boolean includeSubUnit, String title, String msg){
        if(unitCode==null) return;
        notificationCenter.sendUnitMessage(sender, unitCode, includeSubUnit,
                NoticeMessage.create().operation(metaModel.getModelId())
                        .tag(this.tableInfo.fetchObjectPkAsId(bizModel))
                        .subject(title)
                        .content(msg));
    }

    public void submitOpt(){
        String nodeInstId = StringBaseOpt.castObjectToString(this.bizModel.get(MetaTable.WORKFLOW_NODE_INST_ID_PROP));
        try {
            flowEngine.submitOpt(SubmitOptOptions.create().nodeInst(nodeInstId)
                    .user(MetaFormController.fetchExtendParam("userCode", this.bizModel, request))
                    .unit(MetaFormController.fetchExtendParam("unitCode", this.bizModel, request)));
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

    public void setFlowEngine(FlowEngine flowEngine) {
        this.flowEngine = flowEngine;
    }

    public FlowEngine getFlowEngine() {
        return flowEngine;
    }
}
