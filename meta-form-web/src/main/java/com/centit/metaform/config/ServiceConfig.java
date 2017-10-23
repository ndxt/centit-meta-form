package com.centit.metaform.config;

import com.centit.framework.core.config.DataSourceConfig;
import com.centit.framework.ip.app.config.IPAppSystemBeanConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"com.centit.*"},
    excludeFilters = @ComponentScan.Filter(value = org.springframework.stereotype.Controller.class))
@Import({IPAppSystemBeanConfig.class, DataSourceConfig.class})
public class ServiceConfig {

}
