package com.centit.metaform.config;

import com.centit.framework.config.BaseSpringMvcConfig;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by zou_wy on 2017/3/29.
 */
@ComponentScan(basePackages = {"com.centit.product.metadata.controller"},
        includeFilters = {@ComponentScan.Filter(value= org.springframework.stereotype.Controller.class)},
        useDefaultFilters = false)
public class MetaDataSpringMvcConfig extends BaseSpringMvcConfig {

}
