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

    @ApiModelProperty(value = "表ID")
    @Column(name = "TABLE_ID")
    private String tableId;

    @ApiModelProperty(value = "表单类型 C 卡片类型 R 日历类型 N 正常表单 S 子模块表单 L 列表表单 P 一对一子表表单 D 数据驱动表单（二阶表单）")
    @Column(name = "MODEL_TYPE")
    @Length(max = 1, message = "字段长度不能大于{max}")
    private String modelType;

    @ApiModelProperty(value = "子模块关联表ID")
    @Column(name = "RELATION_ID")
    private String relationId;

    @ApiModelProperty(value = "模块名称，如果是子摸快，这个字段名为 relationName ", required = true)
    @Column(name = "MODEL_NAME")
    @Length(max = 200, message = "字段长度不能大于{max}")
    private String modelName;

    @ApiModelProperty(value = "表单模板")
    @Column(name = "FORM_TEMPLATE")
    @Basic(fetch = FetchType.LAZY)
    private JSONObject formTemplate;

    @ApiModelProperty(value = "移动表单模板")
    @Column(name = "MOBILE_FORM_TEMPLATE")
    @Basic(fetch = FetchType.LAZY)
    private JSONObject mobileFormTemplate;

    @ApiModelProperty(value = "更改时间")
    @Column(name = "LAST_MODIFY_DATE")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date lastModifyDate;

    @ApiModelProperty(value = "更改人员")
    @Column(name = "RECORDER")
    @Length(max = 8, message = "字段长度不能大于{max}")
    @DictionaryMap(fieldName = "recorderName", value = "userCode")
    private String recorder;

    @ApiModelProperty(value = "业务特殊处理脚本")
    @Column(name = "EXTEND_OPT_JS")
    @Basic(fetch = FetchType.LAZY)
    private String extendOptJs;

    @ApiModelProperty(value = "数据查询参数驱动SQL，也可以是对应表的字段列表")
    @Column(name = "DATA_FILTER_SQL")
    @Length(max = 2000, message = "字段长度不能大于{max}")
    @Basic(fetch = FetchType.LAZY)
    private String dataFilterSql;

    @ApiModelProperty(value = "关联工作流代码")
    @Column(name = "REL_FLOW_CODE")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String relFlowCode;

    /**
     * 工作流业务标题模板 可以通过{变量名称} 来引用其他属性
     */
    @ApiModelProperty(value = "工作流业务标题模板")
    @Column(name = "FLOW_OPT_TITLE")
    @Length(max = 500, message = "字段长度不能大于{max}")
    private String flowOptTitle;

    @ApiModelProperty(value = "模块描述")
    @Column(name = "MODEL_COMMENT")
    @Length(max = 256, message = "字段长度不能大于{max}")
    private String modelComment;

    @ApiModelProperty(value = "表单后台处理url")
    @Column(name = "MODE_OPT_URL")
    @Length(max = 800, message = "字段长度不能大于{max}")
    private String modeOptUrl;

    /**
     * 所属数据库ID
     */
    @Column(name = "DATABASE_CODE")
    @ApiModelProperty(value = "数据库ID")
    @DictionaryMap(value = "databaseInfo", fieldName = "databaseName")
    private String databaseCode;

    @ApiModelProperty(value = "应用id")
    @Column(name = "os_id")
    private String osId;

    @ApiModelProperty(value = "所属业务")
    @Column(name = "OPT_ID")
    private String optId;

   /* @ApiModelProperty(value = "所属分组")
    @Column(name = "own_group")
    private String ownGroup;*/
    @ApiModelProperty(value = "发布时间")
    @Column(name = "PUBLISH_DATE")
    private Date publishDate;

    @OneToMany(targetEntity = MetaFormModelDraft.class, mappedBy = "metaFormModel",
            orphanRemoval = true,
            cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "MODEL_CODE", referencedColumnName = "MODEL_CODE")
    private Set<MetaFormModelDraft> childFormModels;

    @Transient
    private String relationName;

    public String getRelationName() {
        return relationName;
    }


    public MetaFormModelDraft() {
    }

    public MetaFormModelDraft(
            String modelId
            , String modelName) {
        this.modelId = modelId;
        this.modelName = modelName;
    }


    public Set<MetaFormModelDraft> getMetaFormModels() {
        if (this.childFormModels == null) {
            this.childFormModels = new HashSet<>();
        }
        return this.childFormModels;
    }

    public void setMetaFormModels(Set<MetaFormModelDraft> metaFormModels) {
        this.childFormModels = metaFormModels;
    }

    public void addMetaFormModel(MetaFormModelDraft metaFormModel) {
        if (this.childFormModels == null) {
            this.childFormModels = new HashSet<>();
        }
        this.childFormModels.add(metaFormModel);
    }

    public void removeMetaFormModel(MetaFormModelDraft metaFormModel) {
        if (this.childFormModels == null) {
            return;
        }
        this.childFormModels.remove(metaFormModel);
    }

}
