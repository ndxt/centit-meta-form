package com.centit.metaform.config;

import com.centit.framework.hibernate.config.HibernateConfig;
import com.centit.framework.ip.app.config.IPAppSystemBeanConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"com.centit"},
    excludeFilters = @ComponentScan.Filter(value = org.springframework.stereotype.Controller.class))
@Import({IPAppSystemBeanConfig.class, HibernateConfig.class})
public class ServiceConfig {

}
