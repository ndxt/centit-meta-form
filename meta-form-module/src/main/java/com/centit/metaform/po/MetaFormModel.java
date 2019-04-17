package com.centit.metaform.po;

import com.alibaba.fastjson.JSONObject;
import com.centit.framework.core.dao.DictionaryMap;
import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorTime;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.*;

/**
 * create by scaffold 2016-06-02


  通用模块管理null
*/
@Data
@Entity
@Table(name = "M_META_FORM_MODEL")
public class MetaFormModel implements java.io.Serializable {
    private static final long serialVersionUID =  1L;

    @ApiModelProperty(value = "模块代码", hidden = true)
    @Id
    @Column(name = "MODE_ID")
    @GeneratedValue(generator = "paymentableGenerator")
    //@GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
    private String modeId;

    @ApiModelProperty(value = "表ID")
    @Column(name = "TABLE_ID")
    private String tableId;

    @ApiModelProperty(value = "模块名称", required = true)
    @Column(name = "MODEL_NAME")
    @NotBlank(message = "字段不能为空")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String  modelName;

    @ApiModelProperty(value = "存储类别 R 只读（视图、查询）;A  新增（只能新增一条）;W 修改 ;L 编辑列表（增删改）")
    @Column(name = "ACCESS_TYPE")
    @Length(max = 1, message = "字段长度不能大于{max}")
    private String  accessType;

    @ApiModelProperty(value = "表单模板")
    @Column(name = "FORM_TEMPLATE")
    private String  formTemplate;

    @ApiModelProperty(value = "更改时间")
    @Column(name = "LAST_MODIFY_DATE")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date  lastModifyDate;

    @ApiModelProperty(value = "更改人员")
    @Column(name = "RECORDER")
    @Length(max = 8, message = "字段长度不能大于{max}")
    @DictionaryMap(fieldName = "recorderName", value = "userCode")
    private String  recorder;

    @ApiModelProperty(value = "业务特殊处理脚本")
    @Column(name = "EXTEND_OPT_JS")
    private String  extendOptJs;

    @ApiModelProperty(value = "数据范围权限过滤")
    @Column(name = "DATA_FILTER_SQL")
    @Length(max = 2000, message = "字段长度不能大于{max}")
    private String  dataFilterSql;

    @ApiModelProperty(value = "关联工作流代码")
    @Column(name = "REL_WFCODE")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String  relFlowCode;

    @ApiModelProperty(value = "模块描述")
    @Column(name = "MODEL_COMMENT")
    @Length(max = 256, message = "字段长度不能大于{max}")
    private String  modelComment;

    @ApiModelProperty(value = "表单后台处理url")
    @Column(name = "MODE_OPT_URL")
    @Length(max = 800, message = "字段长度不能大于{max}")
    private String  modeOptUrl;

    @OneToMany(mappedBy="metaFormModel",orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "MODEL_CODE", referencedColumnName = "MODEL_CODE")
//    @OneToMany(mappedBy="parentModelCode",fetch = FetchType.EAGER)
//    @JoinColumn(name = "MODEL_CODE", referencedColumnName = "PARENT_MODEL_CODE")
//    @OrderBy(value="displayOrder asc")
    private Set<MetaFormModel> childFormModels;

    public JSONObject getFormTemplateJson() {
        if(StringUtils.isBlank(formTemplate)) {
            return null;
        }
        return JSONObject.parseObject(formTemplate);
    }

    public JSONObject getExtendOptJson() {
        if(StringUtils.isBlank(extendOptJs)) {
            return null;
        }
        return JSONObject.parseObject(extendOptJs);
    }
    // Constructors
    /** default constructor */
    public MetaFormModel() {
    }
    /** minimal constructor */
    public MetaFormModel(
        String modeId
        ,String  modelName) {


        this.modeId = modeId;

        this.modelName= modelName;
    }

    /** full constructor */
    public MetaFormModel(String modeId, String tableId,
            String modelComment, String modelName, String accessType,
            String formTemplate, Date lastModifyDate,String recorder,
            String extendOptJs, String dataFilterSql,
            String relFlowCode, String modeOptUrl) {
        super();
        this.modeId = modeId;
        this.tableId = tableId;
        this.modelComment = modelComment;
        this.modelName = modelName;
        this.accessType = accessType;
        this.formTemplate = formTemplate;

        this.lastModifyDate = lastModifyDate;
        this.recorder = recorder;
        this.extendOptJs = extendOptJs;
        this.dataFilterSql = dataFilterSql;
        this.relFlowCode = relFlowCode;
        this.modeOptUrl = modeOptUrl;
        this.childFormModels = new HashSet<MetaFormModel>();
    }



    public Set<MetaFormModel> getMetaFormModels(){
        if(this.childFormModels==null)
            this.childFormModels = new HashSet<MetaFormModel>();
        return this.childFormModels;
    }

    public void setMetaFormModels(Set<MetaFormModel> metaFormModels) {
        this.childFormModels = metaFormModels;
    }

    public void addMetaFormModel(MetaFormModel metaFormModel ){
        if (this.childFormModels==null)
            this.childFormModels = new HashSet<MetaFormModel>();
        this.childFormModels.add(metaFormModel);
    }

    public void removeMetaFormModel(MetaFormModel metaFormModel ){
        if (this.childFormModels==null)
            return;
        this.childFormModels.remove(metaFormModel);
    }

    public MetaFormModel newMetaFormModel(){
        MetaFormModel res = new MetaFormModel();

        return res;
    }
    /**
     * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
     *
     */
    public void replaceMetaFormModels(Set<MetaFormModel> set) {
        List<MetaFormModel> newObjs = new ArrayList<MetaFormModel>();
        for(MetaFormModel p :set){
            if(p==null)
                continue;
            MetaFormModel newdt = newMetaFormModel();
            newdt.copyNotNullProperty(p);
            newObjs.add(newdt);
        }
        //delete
        boolean found = false;
        Set<MetaFormModel> oldObjs = new HashSet<MetaFormModel>();
        oldObjs.addAll(getMetaFormModels());

        for(Iterator<MetaFormModel> it=oldObjs.iterator(); it.hasNext();){
            MetaFormModel odt = it.next();
            found = false;
            for(MetaFormModel newdt :newObjs){
                if(odt.getModeId().equals( newdt.getModeId())){
                    found = true;
                    break;
                }
            }
            if(! found)
                removeMetaFormModel(odt);
        }
        oldObjs.clear();
        //insert or update
        for(MetaFormModel newdt :newObjs){
            found = false;
            for(Iterator<MetaFormModel> it=getMetaFormModels().iterator();
             it.hasNext();){
                MetaFormModel odt = it.next();
                if(odt.getModeId().equals( newdt.getModeId())){
                    odt.copy(newdt);
                    found = true;
                    break;
                }
            }
            if(! found)
                addMetaFormModel(newdt);
        }
    }

      public MetaFormModel copy(MetaFormModel other){

        this.setModeId(other.getModeId());
        this.tableId=other.getTableId();
        this.modelComment= other.getModelComment();
        this.modelName= other.getModelName();
        this.accessType= other.getAccessType();
        this.formTemplate= other.getFormTemplate();
        this.lastModifyDate= other.getLastModifyDate();
        this.recorder= other.getRecorder();
        this.extendOptJs= other.getExtendOptJs();
        this.childFormModels = other.getMetaFormModels();
        this.dataFilterSql = other.getDataFilterSql();
        this.relFlowCode = other.getRelFlowCode();
        this.modeOptUrl = other.getModeOptUrl();
        return this;
    }


    public MetaFormModel copyNotNullProperty(MetaFormModel other){

    if( other.getModeId() != null)
        this.setModeId(other.getModeId());
        if( other.getTableId() != null)
            this.tableId= other.getTableId();
        if( other.getModelComment() != null)
            this.modelComment= other.getModelComment();
        if( other.getModelName() != null)
            this.modelName= other.getModelName();
        if( other.getAccessType() != null)
            this.accessType= other.getAccessType();
        if( other.getFormTemplate() != null)
            this.formTemplate=other.getFormTemplate();
        if( other.getLastModifyDate() != null)
            this.lastModifyDate= other.getLastModifyDate();
        if( other.getRecorder() != null)
            this.recorder= other.getRecorder();
        if( other.getMetaFormModels() != null)
        this.childFormModels = other.getMetaFormModels();
        if( other.getExtendOptJs() != null)
            this.extendOptJs = other.getExtendOptJs();
        if( other.getRelFlowCode() != null)
            this.relFlowCode = other.getRelFlowCode();
        if(null!=other.getDataFilterSql())
            this.dataFilterSql = other.getDataFilterSql();
        if( other.getModeOptUrl() != null)
            this.modeOptUrl = other.getModeOptUrl();
        return this;
    }

    public MetaFormModel clearProperties(){

//        this.setMdTable(null);
        this.modelComment= null;
        this.modelName= null;
        this.accessType= null;
        this.formTemplate= null;
        this.lastModifyDate= null;
        this.recorder= null;
        this.extendOptJs= null;
        this.dataFilterSql= null;
        this.relFlowCode = null;
        this.modeOptUrl = null;
        this.childFormModels = new HashSet<>();
        return this;
    }
}
