package com.centit.metaform.formaccess;

import com.centit.framework.ip.app.config.IPOrStaticAppSystemBeanConfig;
import com.centit.framework.jdbc.config.JdbcConfig;
import com.centit.framework.security.model.CentitPasswordEncoder;
import com.centit.framework.security.model.StandardPasswordEncoderImpl;
import com.centit.product.metadata.service.DatabaseRunTime;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.file.FileIOOpt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

@PropertySource(value = "classpath:system.properties")
@ComponentScan(basePackages = {"com.centit"},
        excludeFilters = @ComponentScan.Filter(value = org.springframework.stereotype.Controller.class))
@Import(value = {IPOrStaticAppSystemBeanConfig.class,JdbcConfig.class})
@ContextConfiguration(classes = TestJSEvent.class)
@RunWith(SpringRunner.class)
public class TestJSEvent {

    @Bean(name = "passwordEncoder")
    public CentitPasswordEncoder centitPasswordEncoder(){
        return new StandardPasswordEncoderImpl();
    }

    @Autowired
    private MetaObjectService metaObjectService;
    @Autowired
    private DatabaseRunTime databaseRunTime;

    @Test
    public void test(HttpServletRequest request){
        try(InputStream resource = TestJSEvent
                .class.getResourceAsStream("/eventjs/sample.js")){
            String js = FileIOOpt.readStringFromInputStream(resource);
            /*JSMateObjectEventRuntime jsMateObjectEvent =
                    new JSMateObjectEventRuntime(metaObjectService,
                            databaseRunTime, js, request);
            Map<String,Object> object = CollectionsOpt.createHashMap("hello","js");
            int n = jsMateObjectEvent.runEvent("beforeSave",object);
            System.out.println(JSON.toJSONString(object));
            System.out.println(n);*/
        }catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
