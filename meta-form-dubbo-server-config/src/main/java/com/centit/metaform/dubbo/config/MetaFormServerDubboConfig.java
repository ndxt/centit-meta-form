package com.centit.metaform.dubbo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath:dubbo-metaform-server.xml"})
public class MetaFormServerDubboConfig {

}
