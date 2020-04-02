package com.centit.application.service.impl;

import com.centit.application.dao.ApplicationInfoDao;
import com.centit.application.po.ApplicationInfo;
import com.centit.application.service.ApplicationInfoManager;
import com.centit.support.database.utils.PageDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
@Service
@Transactional
public class ApplicationInfoManagerImpl implements ApplicationInfoManager {
    @Autowired
    private ApplicationInfoDao applicationInfoDao;
    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void createApplicationInfo(ApplicationInfo applicationInfo){
        applicationInfoDao.saveNewObject(applicationInfo);
        applicationInfoDao.saveObjectReferences(applicationInfo);

    }
    @Override
    public List<ApplicationInfo> listApplicationInfo(Map<String, Object> param, PageDesc pageDesc){
        return applicationInfoDao.listObjectsByProperties(param,pageDesc);
    }
    @Override
    public ApplicationInfo getApplicationInfo(String applicationId){
       return applicationInfoDao.getObjectWithReferences(applicationId);
    }
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public void deleteApplicationInfo(String applicationId){
        ApplicationInfo applicationInfo=applicationInfoDao.getObjectById(applicationId);
        applicationInfo.setIsDelete(true);
        updateApplicationInfo(applicationInfo);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public void updateApplicationInfo(ApplicationInfo applicationInfo){
        applicationInfoDao.updateObject(applicationInfo);
        applicationInfoDao.saveObjectReferences(applicationInfo);
    }
}
