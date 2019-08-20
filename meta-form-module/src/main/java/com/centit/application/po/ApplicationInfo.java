package com.centit.application.po;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * create by codefan@sina.com
 * @author codefan
 * 应用信息
 */
@Data
@Entity
@Table(name = "M_APPLICATION_INFO")
public class ApplicationInfo implements java.io.Serializable {

    private static final long serialVersionUID =  1L;

    @ApiModelProperty(value = "模块代码", hidden = true)
    @Id
    @Column(name = "APPLICATION_ID")
    @ValueGenerator(strategy = GeneratorType.UUID22)
    private String applicationId;

    @ApiModelProperty(value = "应用模块名称，如果是子摸快，这个字段名为 relationName ", required = true)
    @Column(name = "APPLICATION_ID_NAME")
    @NotBlank(message = "字段不能为空")
    @Length(max = 200, message = "字段长度不能大于{max}")
    private String  applicationName;


    //CLOB 字段
    @ApiModelProperty(value = "应用流程")
    @Column(name = "PAGE_FLOW")
    @JSONField(serialize=false)
    private String  pageFlow;

    @JSONField(serialize=false)
    public String getPageFlow() {
        return pageFlow;
    }

    public void setPageFlow(String pageFlow) {
        this.pageFlow = pageFlow;
    }


    public JSONObject getPageFlowJson() {
        if(StringUtils.isBlank(pageFlow)) {
            return null;
        }
        try {
            return JSONObject.parseObject(pageFlow);
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }
}
