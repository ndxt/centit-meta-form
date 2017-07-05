package com.centit.metaform.config;

import com.centit.framework.hibernate.config.DataSourceConfig;
import com.centit.framework.staticsystem.config.SpringConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(basePackages = {"com.centit.*"},
    excludeFilters = @ComponentScan.Filter(value = org.springframework.stereotype.Controller.class))
@Import({SpringConfig.class, DataSourceConfig.class})
public class BeanConfig  {

}
