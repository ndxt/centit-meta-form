package com.centit.application.dao;

import com.centit.application.po.ApplicationInfo;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ApplicationInfoDao extends BaseDaoImpl<ApplicationInfo, String> {
    @Override
    public Map<String, String> getFilterField() {

        if( filterField == null) {
            filterField = new HashMap<>();

            filterField.put("isDelete", " isDelete is null or isDelete='F' ");
        }
        return filterField;
    }
}
