package com.centit.metaform.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.centit.framework.common.SysParametersUtils;
import com.centit.framework.config.SystemSpringMvcConfig;
import com.centit.framework.config.WebConfig;
import com.centit.support.algorithm.CollectionsOpt;
import lombok.SneakyThrows;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by zou_wy on 2017/3/29.
 */

public class WebInitializer implements WebApplicationInitializer {
    @SneakyThrows
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        findProperties(CollectionsOpt.createList("login.dao.enable","login.cas.enable"));

        String [] servletUrlPatterns = {"/system/*","/metadata/*","/metaform/*","/dbdesign/*"};
        WebConfig.registerSpringConfig(servletContext, ServiceConfig.class);

        WebConfig.registerServletConfig(servletContext, "system",
                "/system/*",
                SystemSpringMvcConfig.class,SwaggerConfig.class);
        WebConfig.registerServletConfig(servletContext, "metaform",
                "/metaform/*",
                MetaformSpringMvcConfig.class,SwaggerConfig.class);
        WebConfig.registerServletConfig(servletContext, "metadata",
                "/metadata/*",
                MetaDataSpringMvcConfig.class,SwaggerConfig.class);
        WebConfig.registerServletConfig(servletContext, "dbdesign",
                "/dbdesign/*",
                DBDesignSpringMvcConfig.class,SwaggerConfig.class);


        WebConfig.registerRequestContextListener(servletContext);
        WebConfig.registerSingleSignOutHttpSessionListener(servletContext);
//        WebConfig.registerResponseCorsFilter(servletContext);
        WebConfig.registerCharacterEncodingFilter(servletContext,servletUrlPatterns);
        WebConfig.registerHttpPutFormContentFilter(servletContext,servletUrlPatterns);
        WebConfig.registerHiddenHttpMethodFilter(servletContext,servletUrlPatterns);
        WebConfig.registerRequestThreadLocalFilter(servletContext);
        WebConfig.registerSpringSecurityFilter(servletContext,servletUrlPatterns);
        //registerOpenSessionInViewFilter(servletContext);

    }


    /**
     * 查找配置，优先从system.properties中获取，如果没有从nacos中获取
     * @param keys 需要被提前查找的key
     * @throws IOException
     * @throws NacosException
     */
    private void findProperties(List<String> keys) throws IOException, NacosException {
        HashMap<String, Object> map = new HashMap<>();
        for (String key : keys) {
            String value = SysParametersUtils.getStringValue(key);
            if (StringUtils.isNotBlank(value)){
                map.put(key,value);
            }
        }
        if (map.size()!=keys.size()){
            String serverAddr = SysParametersUtils.getStringValue("nacos.server-addr");
            String dataId = SysParametersUtils.getStringValue("nacos.system-dataid");
            String group = "CENTIT";
            Properties nacosProperties = new Properties();
            nacosProperties.put("serverAddr", serverAddr);
            ConfigService configService = NacosFactory.createConfigService(nacosProperties);
            String content = configService.getConfig(dataId, group, 60000);
            if (StringUtils.isNotBlank(content)){
                ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
                Properties properties = new Properties();
                properties.load(inputStream);
                //优先使用配置文件中的属性
                for (String key : keys) {
                    String value = properties.getProperty(key);
                    if (StringUtils.isBlank(MapUtils.getString(map,key)) && StringUtils.isNotBlank(value)){
                        map.put(key,value);
                    }
                }
            }
        }
        System.getProperties().putAll(map);
    }

}
