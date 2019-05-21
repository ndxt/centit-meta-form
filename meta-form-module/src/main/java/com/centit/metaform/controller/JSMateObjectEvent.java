package com.centit.metaform.controller;

import com.centit.product.dataopt.utils.JSRuntimeContext;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.algorithm.NumberBaseOpt;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class JSMateObjectEvent {

    private static final Logger logger = LoggerFactory.getLogger(JSMateObjectEvent.class);
    private MetaObjectService metaObjectService;
    private JSRuntimeContext jsRuntimeContext;
    private HttpServletRequest request;
    private String javaScript;

    public JSMateObjectEvent(MetaObjectService metaObjectService, String js, HttpServletRequest request){
        this.metaObjectService = metaObjectService;
        this.javaScript = js;
        this.request = request;
    }

    /**
     * 运行js事件
     * @param eventFunc 时间方法名称
     * @param bizModel 业务数据对象
     * @return 0 表示正常， &lg;0 表示报错， &gt;0 表示事件已经处理好所有任务，无需在做额外的数据库操作
     */
    public int runEvent(String eventFunc, Map<String, Object> bizModel) {
        if(jsRuntimeContext == null){
            jsRuntimeContext = new JSRuntimeContext();
        }

        if(StringUtils.isNotBlank(javaScript)){
            jsRuntimeContext.compileScript(javaScript);
        }
        try {
            Object retObj = jsRuntimeContext.callJsFunc(eventFunc, this, bizModel);
            return NumberBaseOpt.castObjectToInteger(retObj, 0);
        } catch (ScriptException e) {
            logger.error(e.getMessage());
            return -1;
        } catch (NoSuchMethodException e) {
            logger.info(e.getMessage());
            return 0;
        }
    }

    public void setJsRuntimeContext(JSRuntimeContext jsRuntimeContext) {
        this.jsRuntimeContext = jsRuntimeContext;
    }

    public MetaObjectService getMetaObjectService() {
        return metaObjectService;
    }

    public Object getRequestAttribute(String name){
        return this.request.getAttribute(name);
    }

    public Object getSessionAttribute(String name){
        return this.request.getSession().getAttribute(name);
    }

    public void setSessionAttribute(String name, Object value){
        this.request.getSession().setAttribute(name, value);
    }
}
