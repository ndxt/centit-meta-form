package com.centit.metaform.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import com.centit.metaform.dao.ModelOperationDao;
import com.centit.metaform.po.ModelOperation;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * ModelOperation  Service.
 * create by scaffold 2016-06-21

 * 模块操作定义null
*/
@Service
public class ModelOperationManagerImpl
        extends BaseEntityManagerImpl<ModelOperation, HashMap<String,Object>, ModelOperationDao>
    {

    public static final Log log = LogFactory.getLog(ModelOperationManagerImpl.class);


    private ModelOperationDao modelOperationDao ;

    @Resource(name = "modelOperationDao")
    @NotNull
    public void setModelOperationDao(ModelOperationDao baseDao)
    {
        this.modelOperationDao = baseDao;
        setBaseDao(this.modelOperationDao);
    }

/*     @PostConstruct
    public void init() {

    }
 */
    @Transactional(propagation=Propagation.REQUIRED)
    public JSONArray listModelOperationsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc){

        return baseDao.listObjectsAsJson(filterMap, pageDesc);
    }

}

