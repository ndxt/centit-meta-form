package com.centit.metaform.service;

import com.alibaba.fastjson.JSONObject;
import com.centit.framework.core.dao.DataPowerFilter;

import java.util.List;

/**
 * 将项目Service层需要用到的通用服务放在这个，供其他业务服务调用。
 * 使用方法：用下面的注解
 *             (name = "queryDataScopeFilter")
 *            protected QueryDataScopeFilter  queryDataScopeFilter;
 * @author codefan
 *
 */
public interface QueryDataScopeFilter {
    /**
     * 获得用户摸个功能方法的数据范围权限，返回null或者size==0表示拥有所有权限
     * @param sUserCode sUserCode
     * @param sOptid sOptid
     * @param sOptMethod sOptMethod
     * @return 用户摸个功能方法的数据范围权限
     */
     List<String> listUserDataFiltersByOptIdAndMethod
     (String sUserCode, String sOptid, String sOptMethod);
    /**
     * 创建用户数据范围过滤器，和上面的方法结合使用
     * @param userInfo CentitUserDetails
     * @param currentUnit 当前机构
     * @return DataPowerFilter
     */
     DataPowerFilter createUserDataPowerFilter(JSONObject userInfo, String currentUnit);
}
