package com.centit.metaform.plugins;

import com.alibaba.fastjson2.JSONArray;

import java.util.Collection;
import java.util.Map;

public interface FormDataService {

    Map<String, Object> getObjectById(String tableId, Map<String, Object> pk);

    Map<String, Object> makeNewObject(String tableId, Map<String, Object> extParams);

    int saveObject(String tableId, Map<String, Object> object);

    int updateObject(String tableId, Map<String, Object> object);

    int updateObjectFields(String tableId, final Collection<String> fields, final Map<String, Object> object);

    int updateObjectsByProperties(String tableId, final Collection<String> fields,
                                  final Map<String, Object> fieldValues,final Map<String, Object> filterProperties);

    void deleteObject(String tableId, Map<String, Object> pk);

    JSONArray listObjectsByProperties(String tableId, Map<String, Object> filter);

    //JSONArray pageQueryObjects(String tableId, Map<String, Object> params, PageDesc pageDesc);
}
