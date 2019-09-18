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
        initializeSpringConfig(servletContext);
        initializeSystemSpringMvcConfig(servletContext);
        initializeNormalSpringMvcConfig(servletContext);
        initializeDBDesignSpringMvcConfig(servletContext);
        initializeMetaSpringMvcConfig(servletContext);

        String [] servletUrlPatterns = {"/system/*","/metadata/*","/metaform/*","/dbdesign/*"};
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

    /**
     * 加载Spring 配置
     * @param servletContext ServletContext
     */
    private void initializeSpringConfig(ServletContext servletContext){
        AnnotationConfigWebApplicationContext springContext = new AnnotationConfigWebApplicationContext();
        springContext.register(ServiceConfig.class);
        servletContext.addListener(new ContextLoaderListener(springContext));
    }

    /**
     * 加载Servlet 配置
     * @param servletContext ServletContext
     */
    private void initializeSystemSpringMvcConfig(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(SystemSpringMvcConfig.class, SwaggerConfig.class);
        ServletRegistration.Dynamic system  = servletContext.addServlet("system", new DispatcherServlet(context));
        system.addMapping("/system/*");
        system.setLoadOnStartup(1);
        system.setAsyncSupported(true);
    }

    /**
     * 加载Servlet 项目配置
     * @param servletContext ServletContext
     */
    private void initializeNormalSpringMvcConfig(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(NormalSpringMvcConfig.class, SwaggerConfig.class);
        ServletRegistration.Dynamic metaform  = servletContext.addServlet("metaform", new DispatcherServlet(context));
        metaform.addMapping("/metaform/*");
        metaform.setLoadOnStartup(1);
        metaform.setAsyncSupported(true);
    }

    private void initializeDBDesignSpringMvcConfig(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(DBDesignSpringMvcConfig.class, SwaggerConfig.class);
        ServletRegistration.Dynamic dbdesign  = servletContext.addServlet("dbdesign", new DispatcherServlet(context));
        dbdesign.addMapping("/dbdesign/*");
        dbdesign.setLoadOnStartup(1);
        dbdesign.setAsyncSupported(true);
    }

    private void initializeMetaSpringMvcConfig(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(MetaDataSpringMvcConfig.class, SwaggerConfig.class);
        ServletRegistration.Dynamic metadata  = servletContext.addServlet("metadata", new DispatcherServlet(context));
        metadata.addMapping("/metadata/*");
        metadata.setLoadOnStartup(1);
        metadata.setAsyncSupported(true);
    }

    /*public void registerOpenSessionInViewFilter(ServletContext servletContext) {
        javax.servlet.FilterRegistration.Dynamic openSessionInViewFilter
                = servletContext.addFilter("openSessionInViewFilter", OpenSessionInViewFilter.class);
        openSessionInViewFilter.addMappingForUrlPatterns(null, false, "/service/*", "/system/*");
        openSessionInViewFilter.setAsyncSupported(true);
    }*/
}
