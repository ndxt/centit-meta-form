package com.centit.application.service.impl;

import com.centit.application.dao.ApplicationInfoDao;
import com.centit.application.po.ApplicationInfo;
import com.centit.application.service.ApplicationInfoManager;
import com.centit.support.database.utils.PageDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
@Service
@Transactional
public class ApplicationInfoManagerImpl implements ApplicationInfoManager {
    @Autowired
    private ApplicationInfoDao applicationInfoDao;
    public void createApplicationInfo(ApplicationInfo applicationInfo){
        applicationInfoDao.saveNewObject(applicationInfo);
        applicationInfoDao.saveObjectReferences(applicationInfo);

    }

    public List<ApplicationInfo> listApplicationInfo(Map<String, Object> param, PageDesc pageDesc){
        return applicationInfoDao.listObjectsByProperties(param,pageDesc);
    }

    public ApplicationInfo getApplicationInfo(String applicationId){
       return applicationInfoDao.getObjectWithReferences(applicationId);
    }

    public void deleteApplicationInfo(String applicationId){
        applicationInfoDao.deleteObjectById(applicationId);
    }

    public void updateApplicationInfo(ApplicationInfo applicationInfo){
        applicationInfoDao.updateObject(applicationInfo);
        applicationInfoDao.saveObjectReferences(applicationInfo);
    }
}
