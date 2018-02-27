package com.centit.metaform.po;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;


/**
 * create by scaffold 2016-06-21 
 
 
  模块操作定义null   
*/
@Entity
@Table(name = "M_MODEL_OPERATION")
public class ModelOperation implements java.io.Serializable {
    private static final long serialVersionUID =  1L;

    @EmbeddedId
    private com.centit.metaform.po.ModelOperationId cid;


    /**
     * 操作模块代码 一个模块中的操作可能是针对其他模块的
     */
    @Column(name = "OPT_MODEL_CODE")
    @Length(max = 16, message = "字段长度不能大于{max}")
    private String  optModelCode;

    /**
     * F：自定义表单 S:统计报表  P:页面设计器页面  U:开发编写的页面
     */
    @Column(name = "OPT_MODEL_TYPE")
    @Length(max = 1, message = "字段长度不能大于{max}")
    private String  optModelType;
    /**
     * 操作方法 null
     */
    @Column(name = "METHOD")
    @Length(max = 16, message = "字段长度不能大于{max}")
    private String  method;
    /**
     * 操作名称 null
     */
    @Column(name = "LABEL")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String  label;

    /**
     * L: list 列表   N ：不关联数据   S：单选择  M多选
     */
    @Column(name = "DATA_RELATION_TYPE")
    private String  dataRelationType;
    /**
     * 显示顺序 null
     */
    @Column(name = "DISPLAY_ORDER")
    private Long  displayOrder;
    /**
     * 打开方式 0：没有：1： 提示信息  2：只读表单  3：读写表单
     */
    @Column(name = "OPEN_TYPE")
    @Length(max = 1, message = "字段长度不能大于{max}")
    private String  openType;


    /**
     * 操作后提示信息
     */
    @Column(name = "OPT_MESSAGE")
    @Length(max = 500, message = "字段长度不能大于{max}")
    private String  optMessage;
    /**
     * 操作提示信息 操作前提示信息 标题
     */
    @Column(name = "OPT_HINT_TITLE")
    @Length(max = 100, message = "字段长度不能大于{max}")
    private String  optHintTitle;

    /**
     * 操作提示信息 操作前提示信息
     */
    @Column(name = "OPT_HINT_INFO")
    @Length(max = 500, message = "字段长度不能大于{max}")
    private String  optHintInfo;

    /**
     * 其他扩展属性 null
     */
    @Column(name = "EXTEND_OPTIONS")
    @Length(max = 1000, message = "字段长度不能大于{max}")
    private String  extendOptions;

    // Constructors
    /** default constructor */
    public ModelOperation() {
    }
    /** minimal constructor */
    public ModelOperation(ModelOperationId id

        ) {
        this.cid = id;
    }

    public ModelOperation(String modelCode, String operation, String method,String label){

        this.cid =  new ModelOperationId( modelCode,operation);
        this.method = method;
        this.label = label;
    }

/** full constructor */
    public ModelOperation(com.centit.metaform.po.ModelOperationId id
        ,String  optModelCode,String  method,String  label,String  dataRelationType,
        Long  displayOrder,String  openType,String  returnOperation,String  optMessage,String  optHintInfo,String  extendOptions) {
        this.cid = id;
        this.optModelCode= optModelCode;
        this.method= method;
        this.label= label;
        this.displayOrder= displayOrder;
        this.openType= openType;
        this.dataRelationType= dataRelationType;
        this.optMessage= optMessage;
        this.optHintInfo= optHintInfo;
        this.extendOptions= extendOptions;
    }

    public com.centit.metaform.po.ModelOperationId getCid() {
        return this.cid;
    }

    public void setCid(com.centit.metaform.po.ModelOperationId id) {
        this.cid = id;
    }
  
    public String getModelCode() {
        if(this.cid==null)
            this.cid = new com.centit.metaform.po.ModelOperationId();
        return this.cid.getModelCode();
    }

    public void setModelCode(String modelCode) {
        if(this.cid==null)
            this.cid = new com.centit.metaform.po.ModelOperationId();
        this.cid.setModelCode(modelCode);
    }
  
    public String getOperation() {
        if(this.cid==null)
            this.cid = new com.centit.metaform.po.ModelOperationId();
        return this.cid.getOperation();
    }

    public void setOperation(String operation) {
        if(this.cid==null)
            this.cid = new com.centit.metaform.po.ModelOperationId();
        this.cid.setOperation(operation);
    }



    // Property accessors
  
    public String getOptModelCode() {
        return this.optModelCode;
    }

    public void setOptModelCode(String optModelCode) {
        this.optModelCode = optModelCode;
    }
  
    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
  
    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
  
    public Long getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(Long displayOrder) {
        this.displayOrder = displayOrder;
    }
  
    public String getOpenType() {
        return this.openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }
  
    public String getDataRelationType() {
        return this.dataRelationType;
    }

    public void setDataRelationType(String dataRelationType) {
        this.dataRelationType = dataRelationType;
    }
  
    public String getOptHintInfo() {
        return this.optHintInfo;
    }

    public void setOptHintInfo(String optHintInfo) {
        this.optHintInfo = optHintInfo;
    }
  
    public String getExtendOptions() {
        return this.extendOptions;
    }

    public void setExtendOptions(String extendOptions) {
        this.extendOptions = extendOptions;
    }



    public String getOptModelType() {
        return optModelType;
    }
    public void setOptModelType(String optModelType) {
        this.optModelType = optModelType;
    }
    public String getOptMessage() {
        return optMessage;
    }
    public void setOptMessage(String optMessage) {
        this.optMessage = optMessage;
    }
    public String getOptHintTitle() {
        return optHintTitle;
    }
    public void setOptHintTitle(String optHintTitle) {
        this.optHintTitle = optHintTitle;
    }

    public ModelOperation copy(ModelOperation other){
  
        this.setModelCode(other.getModelCode());
        this.setOperation(other.getOperation());
        this.optModelCode= other.getOptModelCode();
        this.method= other.getMethod();
        this.label= other.getLabel();
        this.displayOrder= other.getDisplayOrder();
        this.openType= other.getOpenType();
        this.dataRelationType= other.getDataRelationType();
        this.optModelType= other.getOptModelType();
        this.optMessage = other.getOptMessage();
        this.optHintInfo= other.getOptHintInfo();
        this.extendOptions= other.getExtendOptions();
        this.optHintTitle = other.getOptHintTitle();
        return this;
    }

    public ModelOperation copyNotNullProperty(ModelOperation other){
  
    if( other.getModelCode() != null)
        this.setModelCode(other.getModelCode());
    if( other.getOperation() != null)
        this.setOperation(other.getOperation());
  
        if( other.getOptModelCode() != null)
            this.optModelCode= other.getOptModelCode();
        if( other.getMethod() != null)
            this.method= other.getMethod();
        if( other.getLabel() != null)
            this.label= other.getLabel();
        if( other.getDisplayOrder() != null)
            this.displayOrder= other.getDisplayOrder();
        if( other.getOpenType() != null)
            this.openType= other.getOpenType();
        if( other.getOptMessage() != null)
            this.optMessage = other.getOptMessage();
        if( other.getDataRelationType() != null)
            this.dataRelationType= other.getDataRelationType();
        if( other.getOptModelType() != null)
            this.optModelType= other.getOptModelType();
        if( other.getOptHintInfo() != null)
            this.optHintInfo= other.getOptHintInfo();
        if( other.getExtendOptions() != null)
            this.extendOptions= other.getExtendOptions();
        if( other.getOptHintTitle() != null)
            this.optHintTitle = other.getOptHintTitle();
        return this;
    }


    public ModelOperation clearProperties(){
  
        this.optModelCode= null;
        this.method= null;
        this.label= null;
        this.displayOrder= null;
        this.openType= null;
        this.dataRelationType = null;
        this.optModelType= null;
        this.optMessage = null;
        this.optHintInfo= null;
        this.extendOptions= null;
        this.optHintTitle = null;
        return this;
    }
}
