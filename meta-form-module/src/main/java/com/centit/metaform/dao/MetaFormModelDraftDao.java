package com.centit.metaform.dao;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.metaform.po.MetaFormModelDraft;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
public class MetaFormModelDraftDao extends BaseDaoImpl<MetaFormModelDraft, String> {

    public static final Log log = LogFactory.getLog(MetaFormModelDraftDao.class);

    @Override
    public Map<String, String> getFilterField() {
        return null;
    }
}
