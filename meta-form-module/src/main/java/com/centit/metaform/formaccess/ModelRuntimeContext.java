package com.centit.metaform.formaccess;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.po.MetaTable;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.jsonmaptable.JsonObjectDao;
import com.centit.support.database.metadata.TableField;

public interface ModelRuntimeContext {

    JsonObjectDao getJsonObjectDao() throws SQLException;

    String getModelCode();

    MetaFormModel getMetaFormModel();

    MetaTable getTableInfo();

    List<String> getMetaFormField();

    Object castValueToFieldType(TableField field,Object fieldValue);

    Map<String,Object> castObjectToTableObject(Map<String,Object> object);

    JSONObject castTableObjectToObject(JSONObject object);

    JSONArray castTableObjectListToObjectList(JSONArray jsonArray);

    Map<String,Object> fetchPkFromRequest( HttpServletRequest request);

    Map<String,Object> fetchObjectFromRequest( HttpServletRequest request);

    void close();

    void commitAndClose();

    void rollbackAndClose();

    void setUserEvniData(String key,Object value);

    void setCurrentUserDetails(
            CentitUserDetails userDetails);

    QueryAndNamedParams getMetaFormFilter();

    QueryAndNamedParams translateSQL(String sql,Object obj);
}
