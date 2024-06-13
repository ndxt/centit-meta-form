package com.centit.metaform.dao.json;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.framework.components.CodeRepositoryCache;
import com.centit.metaform.dao.MetaFormModelDao;
import com.centit.metaform.po.MetaFormModel;
import com.centit.support.common.CachedMap;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * MetaFormModelDao  Repository.
 * create by scaffold 2016-06-02
 * <p>
 * 通用模块管理null
 */

@Repository("metaFormModelDao")
public class MetaFormModelDaoImpl implements MetaFormModelDao {

    @Value("${app.home:./}")
    private String appHome;

    private CachedMap<String, MetaFormModel> metaTableCache =
            new CachedMap<>(
                    (modelId)->  this.loadMetaTable(modelId),
                    CodeRepositoryCache.CACHE_EXPIRE_EVERY_DAY );

    private MetaFormModel loadMetaTable(String modelId){
        String pageFile = appHome + File.separator + "config" +
                File.separator +  "pages" + File.separator + modelId +".json";
        try {
            JSONObject moduleJson = JSON.parseObject(new FileInputStream(pageFile));
            MetaFormModel metaForm = moduleJson.toJavaObject(MetaFormModel.class);
            return metaForm;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public MetaFormModel getObjectById(Object modelId) {
        return metaTableCache.getCachedValue((String) modelId);
    }

    @Override
    public void saveNewObject(MetaFormModel metaFormModel) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int mergeObject(MetaFormModel metaFormModel) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int updateObject(MetaFormModel metaFormModel) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int deleteObjectById(Object id) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int saveObjectReferences(MetaFormModel metaFormModel) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public List<Pair<String, String>> getSubModelPropertiesMap(Long parentTableId, Long childTableId) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public JSONArray listFormModeAsJson(String[] fields, Map<String, Object> filterMap, PageDesc pageDesc) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int[] batchUpdateOptId(String optId, List<String> modleIds) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public List<MetaFormModel> listObjectsByProperties(Map<String, Object> filterMap) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int countObjectByProperties(Map<String, Object> filterMap) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int clearTrashStand(String osId) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public void batchDeleteByIds(String[] modleIds) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public void updateValidStatus(String modelId, String validType) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }
}
