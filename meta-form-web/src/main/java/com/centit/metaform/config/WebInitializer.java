package com.centit.metaform.config;

import com.centit.framework.config.SystemSpringMvcConfig;
import com.centit.framework.config.WebConfig;
import com.centit.support.json.JSONOpt;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Created by zou_wy on 2017/3/29.
 */

public class WebInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        JSONOpt.fastjsonGlobalConfig();
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
        WebConfig.registerCharacterEncodingFilter(servletContext,servletUrlPatterns);
        WebConfig.registerHttpPutFormContentFilter(servletContext,servletUrlPatterns);
        WebConfig.registerHiddenHttpMethodFilter(servletContext,servletUrlPatterns);
        WebConfig.registerRequestThreadLocalFilter(servletContext);
        WebConfig.registerAssertUserLoginFilter(servletContext, new String[]{"/metadata/*", "/dbdesign/*"});
        WebConfig.registerSpringSecurityFilter(servletContext,servletUrlPatterns);

    }

}
