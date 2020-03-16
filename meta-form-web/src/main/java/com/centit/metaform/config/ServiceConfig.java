package com.centit.metaform.config;

import com.centit.framework.common.SysParametersUtils;
import com.centit.framework.components.impl.NotificationCenterImpl;
import com.centit.framework.config.SpringSecurityCasConfig;
import com.centit.framework.config.SpringSecurityDaoConfig;
import com.centit.framework.ip.app.config.IPOrStaticAppSystemBeanConfig;
import com.centit.framework.jdbc.config.JdbcConfig;
import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.framework.security.model.CentitPasswordEncoder;
import com.centit.framework.security.model.StandardPasswordEncoderImpl;
import com.centit.search.document.ObjectDocument;
import com.centit.search.service.ESServerConfig;
import com.centit.search.service.Impl.ESIndexer;
import com.centit.search.service.Impl.ESSearcher;
import com.centit.search.service.IndexerSearcherFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"com.centit"},
    excludeFilters = @ComponentScan.Filter(value = org.springframework.stereotype.Controller.class))
@Import({IPOrStaticAppSystemBeanConfig.class,
        JdbcConfig.class,
        SpringSecurityDaoConfig.class,
        SpringSecurityCasConfig.class})
public class ServiceConfig {

    @Bean(name = "passwordEncoder")
    public CentitPasswordEncoder centitPasswordEncoder(){
        return new StandardPasswordEncoderImpl();
    }

    @Bean
    public ESServerConfig esServerConfig(){
        return IndexerSearcherFactory.loadESServerConfigFormProperties(
                SysParametersUtils.loadProperties()
        );
    }

    @Bean(name = "esObjectIndexer")
    public ESIndexer esObjectIndexer(@Autowired ESServerConfig esServerConfig){
        return IndexerSearcherFactory.obtainIndexer(
                esServerConfig, ObjectDocument.class);
    }

    @Bean(name = "esObjectSearcher")
    public ESSearcher esObjectSearcher(@Autowired ESServerConfig esServerConfig){
        return IndexerSearcherFactory.obtainSearcher(
                esServerConfig, ObjectDocument.class);
    }
    @Bean
    public NotificationCenter notificationCenter() {
        NotificationCenterImpl notificationCenter = new NotificationCenterImpl();
        notificationCenter.initDummyMsgSenders();
        //notificationCenter.registerMessageSender("innerMsg",innerMessageManager);

        return notificationCenter;
    }

}
