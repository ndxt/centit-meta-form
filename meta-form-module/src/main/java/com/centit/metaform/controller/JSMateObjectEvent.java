package com.centit.metaform.controller;

import com.centit.product.dataopt.utils.JSRuntimeContext;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.algorithm.NumberBaseOpt;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;
import java.util.Map;

public class JSMateObjectEvent {

    protected static final Logger logger = LoggerFactory.getLogger(JSMateObjectEvent.class);
    private MetaObjectService metaObjectService;
    protected JSRuntimeContext jsRuntimeContext;
    protected String javaScript;

    public JSMateObjectEvent(MetaObjectService metaObjectService, String js){
        this.metaObjectService = metaObjectService;
        this.javaScript = js;
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
            Object retObj = jsRuntimeContext.callJsFunc(eventFunc, metaObjectService, bizModel);
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

}
