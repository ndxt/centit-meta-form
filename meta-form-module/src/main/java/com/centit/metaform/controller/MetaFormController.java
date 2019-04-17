package com.centit.metaform.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.centit.framework.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.service.MetaFormModelManager;
import com.centit.product.dataopt.service.MetaObjectService;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/formaccess")
@Api(value = "自定义表单", tags = "自定义表单")
public class MetaFormController extends BaseController {
    protected static final Logger logger = LoggerFactory.getLogger(MetaFormController.class);

    @Autowired
    private MetaFormModelManager metaFormModelManager;

    @Autowired
    private MetaObjectService metaObjectService;

    @RequestMapping(value = "/{modelCode}/list", method = RequestMethod.GET)
    @WrapUpResponseBody
    public PageQueryResult<Object> listObjects(@PathVariable String modelCode, PageDesc pageDesc,
                          HttpServletRequest request) {
        Map<String, Object> params = collectRequestParameters(request);//convertSearchColumn(request);
        MetaFormModel model = metaFormModelManager.getObjectById(modelCode);
        String sql = model.getDataFilterSql();
        JSONArray ja;
        if(StringUtils.isBlank(sql)) {
            ja = metaObjectService.pageQueryObjects(
                    model.getTableId(), params, pageDesc);
        }else{
            ja = metaObjectService.pageQueryObjects(
                    model.getTableId(), sql, params, pageDesc);
        }
        return PageQueryResult.createJSONArrayResult(ja, pageDesc);
    }

    @RequestMapping(value = "/{modelCode}/get", method = RequestMethod.GET)
    @WrapUpResponseBody
    public Map<String, Object> getObject(@PathVariable String modelCode,
                                               HttpServletRequest request) {
        Map<String, Object> parameters = collectRequestParameters(request);
        MetaFormModel model = metaFormModelManager.getObjectById(modelCode);
        return metaObjectService.getObjectById(model.getTableId(), parameters);
    }

    @RequestMapping(value = "/{modelCode}", method = RequestMethod.PUT)
    @WrapUpResponseBody
    public ResponseData updateObject(@PathVariable String modelCode,
                                            @RequestBody String jsonString) {
        MetaFormModel model = metaFormModelManager.getObjectById(modelCode);
        metaObjectService.updateObject(model.getTableId(), JSON.parseObject(jsonString));
        return ResponseData.makeSuccessResponse();
    }

    @RequestMapping(value = "/{modelCode}", method = RequestMethod.POST)
    @WrapUpResponseBody
    public ResponseData saveObject(@PathVariable String modelCode,
                                          @RequestBody String jsonString) {
        MetaFormModel model = metaFormModelManager.getObjectById(modelCode);
        metaObjectService.saveObject(model.getTableId(), JSON.parseObject(jsonString));
        return ResponseData.makeSuccessResponse();
    }

}
