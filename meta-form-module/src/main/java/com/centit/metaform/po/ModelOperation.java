package com.centit.metaform.po;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * create by scaffold 2016-06-21


  模块操作定义null
*/
@Data
@Entity
@Table(name = "M_MODEL_OPERATION")
public class ModelOperation implements java.io.Serializable {
    private static final long serialVersionUID =  1L;

//    @EmbeddedId
//    private com.centit.metaform.po.ModelOperationId cid;

    /**
     *
     */
    @Id
    @Column(name = "MODEL_CODE")
    @Length(max = 16, message = "字段长度不能大于{max}")
    private String  modelCode;

    /**
     *
     */
    @Id
    @Column(name = "OPERATION")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String  operation;


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
     * 返回操作 0：不操作 1： 刷新页面  2：删除当前行 3：更新当前行
     */
    @Column(name = "RETURN_OPERATION")
    @Length(max = 1, message = "字段长度不能大于{max}")
    private String  returnOperation;

    /**
     * 操作前提示类别
     */
    @Column(name = "OPT_HINT_TYPE")
    private String  optHintType;

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
    public ModelOperation(String modelCode, String operation) {
//        this.cid = id;
        this.modelCode = modelCode;
        this.operation = operation;
    }

    public ModelOperation(String modelCode, String operation, String method,String label){

//        this.cid =  new ModelOperationId( modelCode,operation);
        this.modelCode = modelCode;
        this.operation = operation;
        this.method = method;
        this.label = label;
    }

/** full constructor */
    public ModelOperation(String modelCode, String operation
        ,String  optModelCode,String  method,String  label,String  dataRelationType,
        Long  displayOrder,String  openType,String  returnOperation,String  optMessage,String  optHintInfo,String  extendOptions) {
//        this.cid = id;
        this.modelCode = modelCode;
        this.operation = operation;
        this.optModelCode= optModelCode;
        this.method= method;
        this.label= label;
        this.displayOrder= displayOrder;
        this.openType= openType;
        this.dataRelationType= dataRelationType;
        this.optMessage= optMessage;
        this.optHintInfo= optHintInfo;
        this.extendOptions= extendOptions;
        this.returnOperation = returnOperation;
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
        this.returnOperation = other.getReturnOperation();
        this.optHintType = other.getOptHintType();
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
        if( other.getReturnOperation() != null)
            this.returnOperation = other.getReturnOperation();
        if( other.getOptHintType() != null)
            this.optHintType = other.getOptHintType();
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
        this.optHintType = null;
        this.returnOperation= null;
        return this;
    }
}
