package com.centit.metaform.po;

import com.alibaba.fastjson2.JSONObject;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

/**
 * 页面模板表
 *
 * @Date 2021/1/25
 */
@Data
@Entity
@Table(name = "M_META_FORM_TEMPLATE")
public class MetaFormTemplate implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id", hidden = true)
    @Id
    @Column(name = "ID")
    @ValueGenerator(strategy = GeneratorType.UUID22)
    private String id;

    @ApiModelProperty(value = "表单模板")
    @Column(name = "FORM_TEMPLATE")
    @Basic(fetch = FetchType.LAZY)
    private JSONObject formTemplate;

    @ApiModelProperty(value = "缩略图")
    @Column(name = "IMAGE")
    private String image;

    @ApiModelProperty(value = "模板名称")
    @Column(name = "TEMPLATE_NAME")
    private String templateName;

    @ApiModelProperty(value = "模板描述")
    @Column(name = "TEMPLATE_DESC")
    private String templateDesc;


    @ApiModelProperty(value = "模板类型：报表、门户、框架、表单")
    @Column(name = "TEMPLATE_TYPE")
    @Length(max = 1, message = "字段长度不能大于{max}")
    private String templateType;

    @ApiModelProperty(value = "模板类别：公用，私有")
    @Column(name = "TEMPLATE_CATEGORY")
    @Length(max = 1, message = "字段长度不能大于{max}")
    private String templateCategory;

    @ApiModelProperty(value = "私有应用")
    @Column(name = "APPLICATION_ID")
    private String applicationId;


}
