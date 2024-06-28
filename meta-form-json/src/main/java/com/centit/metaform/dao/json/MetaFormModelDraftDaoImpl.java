package com.centit.metaform.dao.json;

import com.alibaba.fastjson2.JSONArray;
import com.centit.metaform.dao.MetaFormModelDraftDao;
import com.centit.metaform.po.MetaFormModelDraft;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.PageDesc;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository("metaFormModelDraftDao")
public class MetaFormModelDraftDaoImpl implements MetaFormModelDraftDao {

    //public static final Logger logger = LoggerFactory.getLogger(MetaFormModelDraftDaoImpl.class);

    @Override
    public MetaFormModelDraft getObjectById(Object tableId) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int mergeObject(MetaFormModelDraft metaFormModel) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int saveObjectReferences(MetaFormModelDraft metaFormModel) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int deleteObjectById(Object tableId) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public JSONArray listFormModeAsJson(String[] fields, Map<String, Object> filterMap, PageDesc pageDesc) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public JSONArray searchFormModeAsJson(String keyWords, String applicationId, String formType, PageDesc pageDesc) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }
    @Override
    public int[] batchUpdateOptId(String optId, List<String> modleIds) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public void updateValidStatus(String modelId, String validType) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public void batchDeleteByIds(String[] modleIds) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int clearTrashStand(String osId) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public void updatePublishDate(MetaFormModelDraft metaFormModelDraft) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public List<MetaFormModelDraft> listNeedPublishDataPacket(String osId) {
        return null;
    }
}
