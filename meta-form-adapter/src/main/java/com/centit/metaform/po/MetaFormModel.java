package com.centit.metaform.po;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.centit.framework.core.dao.DictionaryMap;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Date;

/**
 * create by scaffold 2016-06-02
 * <p>
 * <p>
 * 通用模块管理null
 */
@Data
@Entity
@Table(name = "M_META_FORM_MODEL")
public class MetaFormModel implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模块代码", hidden = true)
    @Id
    @Column(name = "MODEL_ID")
    //@GeneratedValue(generator = "paymentableGenerator")
    @ValueGenerator(strategy = GeneratorType.UUID22)
    //@GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
    private String modelId;

    @Deprecated
    @ApiModelProperty(value = "表单类型 C 卡片类型 R 日历类型 N 正常表单 S 子模块表单 L 列表表单 P 一对一子表表单 D 数据驱动表单（二阶表单）")
    @Column(name = "MODEL_TYPE")
    @Length(max = 1)
    private String modelType;

    @ApiModelProperty(value = "模块名称，如果是子摸快，这个字段名为 relationName ", required = true)
    @Column(name = "MODEL_NAME")
    @Length(max = 200)
    private String modelName;

    @ApiModelProperty(value = "模块标记")
    @Column(name = "MODEL_TAG")
    private String modelTag;

    @ApiModelProperty(value = "表单模板")
    @Column(name = "FORM_TEMPLATE")
    @Basic(fetch = FetchType.LAZY)
    private JSONObject formTemplate;

    @ApiModelProperty(value = "移动表单模板")
    @Column(name = "MOBILE_FORM_TEMPLATE")
    @Basic(fetch = FetchType.LAZY)
    private JSONObject mobileFormTemplate;

    @ApiModelProperty(value = "数据结构和函数")
    @Column(name = "STRUCTURE_FUNCTION")
    @Basic(fetch = FetchType.LAZY)
    private JSONObject structureFunction;

    @ApiModelProperty(value = "更改时间")
    @Column(name = "LAST_MODIFY_DATE")
    private Date lastModifyDate;

    @ApiModelProperty(value = "更改人员")
    @Column(name = "RECORDER")
    @Length(max = 8)
    @DictionaryMap(fieldName = "recorderName", value = "userCode")
    private String recorder;

    @ApiModelProperty(value = "模块描述")
    @Column(name = "MODEL_COMMENT")
    @Length(max = 256)
    private String modelComment;

    @ApiModelProperty(value = "应用id")
    @Column(name = "OS_ID")
    private String osId;

    @ApiModelProperty(value = "所属业务")
    @Column(name = "OPT_ID")
    private String optId;

    @ApiModelProperty(value = "发布时间")
    @Column(name = "PUBLISH_DATE")
    private Date publishDate;

    @Column(name = "SOURCE_ID")
    @ApiModelProperty(value = "模板来源")
    @JSONField(serialize = false)
    private String sourceId;

    @ApiModelProperty(value = "是否禁用（是否逻辑删除），T：禁用，F：启用（默认）")
    @Column(name = "IS_VALID")
    private Boolean isValid;


    public MetaFormModel copyNotNullProperty(MetaFormModel other) {
        if (other.getModelId() != null) {
            this.setModelId(other.getModelId());
        }
        if (other.getModelComment() != null) {
            this.modelComment = other.getModelComment();
        }
        if (other.getModelName() != null) {
            this.modelName = other.getModelName();
        }
        if (other.getModelTag() != null) {
            this.modelTag = other.getModelTag();
        }
        if (other.getModelType() != null) {
            this.modelType = other.getModelType();
        }
        if (other.getFormTemplate() != null) {
            this.formTemplate = other.getFormTemplate();
        }
        if (other.getMobileFormTemplate() != null) {
            this.mobileFormTemplate = other.getMobileFormTemplate();
        }
        if (other.getStructureFunction() != null) {
            this.structureFunction = other.getStructureFunction();
        }
        if (other.getLastModifyDate() != null) {
            this.lastModifyDate = other.getLastModifyDate();
        }
        if (other.getRecorder() != null) {
            this.recorder = other.getRecorder();
        }
        if (other.getOsId() != null) {
            this.osId = other.getOsId();
        }
        if (other.getOptId() != null) {
            this.optId = other.getOptId();
        }
        return this;
    }

}
