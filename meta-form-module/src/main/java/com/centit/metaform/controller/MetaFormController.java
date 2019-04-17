package com.centit.metaform.controller;

import com.centit.framework.core.controller.BaseController;
import com.centit.metaform.service.MetaFormModelManager;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/formaccess")
@Api(value = "自定义表单", tags = "自定义表单")
public class MetaFormController extends BaseController {
    protected static final Logger logger = LoggerFactory.getLogger(MetaFormController.class);

    @Autowired
    private MetaFormModelManager modelManager;



}
