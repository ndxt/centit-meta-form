package com.centit.metaform.config;

import com.centit.framework.config.SystemSpringMvcConfig;
import com.centit.framework.config.WebConfig;
import com.centit.support.file.PropertiesReader;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Properties;

/**
 * Created by zou_wy on 2017/3/29.
 */

public class WebInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        String [] servletUrlPatterns = {"/system/*","/metadata/*","/metaform/*","/dbdesign/*"};
        WebConfig.registerSpringConfig(servletContext, ServiceConfig.class);

        WebConfig.registerServletConfig(servletContext, "system",
                "/system/*",
                SystemSpringMvcConfig.class,SwaggerConfig.class);

        WebConfig.registerServletConfig(servletContext, "metaform",
                "/metaform/*",
                NormalSpringMvcConfig.class,SwaggerConfig.class);
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

        Properties properties = PropertiesReader.getClassPathProperties("/system.properties");
        String jdbcUrl = properties.getProperty("jdbc.url");

        if(jdbcUrl.startsWith("jdbc:h2")){
            WebConfig.initializeH2Console(servletContext);
        }
    }

}
