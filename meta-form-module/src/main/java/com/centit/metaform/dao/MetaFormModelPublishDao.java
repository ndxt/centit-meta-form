package com.centit.metaform.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.po.MetaFormModelPublish;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * MetaFormModelDao  Repository.
 * create by scaffold 2016-06-02
 * <p>
 * 通用模块管理null
 */

@Repository
public class MetaFormModelPublishDao extends BaseDaoImpl<MetaFormModelPublish, String> {

    public static final Log log = LogFactory.getLog(MetaFormModelPublishDao.class);

    @Override
    public Map<String, String> getFilterField() {
        return null;
    }
}
