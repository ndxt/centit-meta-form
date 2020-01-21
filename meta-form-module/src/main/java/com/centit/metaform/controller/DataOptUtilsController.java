package com.centit.metaform.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.fileserver.utils.SystemTempFileUtils;
import com.centit.fileserver.utils.UploadDownloadUtils;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.product.dbdesign.pdmutils.PdmTableInfoUtils;
import com.centit.support.common.ObjectException;
import com.centit.support.database.metadata.SimpleTableInfo;
import com.centit.support.file.FileSystemOpt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/optUtils")
@Api(value = "业务数据处理工具类", tags = "业务数据处理工具类")
public class DataOptUtilsController {

    private static final Log logger = LogFactory.getLog(DataOptUtilsController.class);

    private List<SimpleTableInfo> fetchPdmTables(String tempFilePath) {
        if ("".equals(tempFilePath)) {
            throw new ObjectException("pdm文件不能为空");
        }
        return PdmTableInfoUtils.importTableFromPdm(tempFilePath);
    }

    @ApiOperation(value = "range")
    @CrossOrigin(origins = "*", allowCredentials = "true", maxAge = 86400, methods = RequestMethod.GET)
    @RequestMapping(value = "/range", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public JSONObject checkFileRange(String token, long size) {
        //FileRangeInfo fr = new FileRangeInfo(token,size);
        //Pair<String, InputStream> fileInfo = UploadDownloadUtils.fetchInputStreamFromMultipartResolver(request);
        //检查临时目录中的文件大小，返回文件的其实点
        String tempFilePath = SystemTempFileUtils.getTempFilePath(token, size);
        long tempFileSize = SystemTempFileUtils.checkTempFileSize(tempFilePath);
        Map<String, Object> data = new HashMap<>(4);
        data.put("tempFilePath", token +"_"+size);
        JSONObject jsonObject = UploadDownloadUtils.makeRangeUploadJson(tempFileSize, token, token +"_"+size);
        if (tempFileSize == size) {
            data.put("tables", fetchPdmTables(tempFilePath));
            jsonObject.put("tables", data);
        }
        return jsonObject;
    }

    @ApiOperation(value = "导入pdm返回表数据")
    @CrossOrigin(origins = "*", allowCredentials = "true", maxAge = 86400, methods = RequestMethod.POST)
    @RequestMapping(value = "/excelData", method = {RequestMethod.POST})
    @WrapUpResponseBody
    public JSONArray fetchDataFromExcel(String token, long size,
                                        HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Pair<String, InputStream> fileInfo = UploadDownloadUtils.fetchInputStreamFromMultipartResolver(request);
        FileSystemOpt.createDirect(SystemTempFileUtils.getTempDirectory());
        String tempFilePath = SystemTempFileUtils.getTempFilePath(token, size);
        try {
            long uploadSize = UploadDownloadUtils.uploadRange(tempFilePath, fileInfo.getRight(), token, size, request);
            if(uploadSize>0){
                //上传到临时区成功
                JSONObject jsonObject = new JSONObject();
                Map<String, Object> data = new HashMap<>(4);
                data.put("tempFilePath", token +"_"+size);
                data.put("tables", PdmTableInfoUtils.getTableNameFromPdm(tempFilePath));
                jsonObject.put("tables", data);
                JsonResultUtils.writeSingleDataJson(jsonObject,response);
                //FileSystemOpt.deleteFile(tempFilePath);
            }else {
                JsonResultUtils.writeOriginalJson(UploadDownloadUtils.
                        makeRangeUploadJson(uploadSize, token, token +"_"+size).toJSONString(), response);
            }

        }catch (ObjectException e){
            logger.error(e.getMessage(), e);
            JsonResultUtils.writeHttpErrorMessage(e.getExceptionCode(),
                    e.getMessage(), response);
        }
        return null;
    }

}
