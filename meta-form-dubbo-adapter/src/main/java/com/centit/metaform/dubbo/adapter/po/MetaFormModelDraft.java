package com.centit.metaform.dubbo.adapter.po;

import com.alibaba.fastjson.JSONObject;
import com.centit.framework.core.dao.DictionaryMap;
import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorTime;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义表单，未发布的表单
 */
@Data
@Entity
@Table(name = "M_META_FORM_MODEL_DRAFT")
public class MetaFormModelDraft implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模块代码", hidden = true)
    @Id
    @Column(name = "MODEL_ID")
    @ValueGenerator(strategy = GeneratorType.UUID22)
    private String modelId;

    @Deprecated
    @ApiModelProperty(value = "表单类型 C 卡片类型 R 日历类型 N 正常表单 S 子模块表单 L 列表表单 P 一对一子表表单 D 数据驱动表单（二阶表单）")
    @Column(name = "MODEL_TYPE")
    @Length(max = 1, message = "字段长度不能大于{max}")
    private String modelType;

    @ApiModelProperty(value = "模块名称，如果是子摸快，这个字段名为 relationName ", required = true)
    @Column(name = "MODEL_NAME")
    @Length(max = 200, message = "字段长度不能大于{max}")
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
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date lastModifyDate;

    @ApiModelProperty(value = "更改人员")
    @Column(name = "RECORDER")
    @Length(max = 8, message = "字段长度不能大于{max}")
    @DictionaryMap(fieldName = "recorderName", value = "userCode")
    private String recorder;

    @ApiModelProperty(value = "模块描述")
    @Column(name = "MODEL_COMMENT")
    @Length(max = 256, message = "字段长度不能大于{max}")
    private String modelComment;

    @ApiModelProperty(value = "应用id")
    @Column(name = "os_id")
    private String osId;

    @ApiModelProperty(value = "所属业务")
    @Column(name = "OPT_ID")
    private String optId;

    @ApiModelProperty(value = "发布时间")
    @Column(name = "PUBLISH_DATE")
    private Date publishDate;

    @ApiModelProperty(value = "是否禁用（是否逻辑删除），T：禁用，F：启用（默认）")
    @Column(name = "IS_VALID")
    private Boolean isValid;
}
