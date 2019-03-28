package com.centit.metaform.po;

import com.centit.support.algorithm.DatetimeOpt;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.Date;


/**
 * create by scaffold 2016-06-01


  元数据更改记录null
*/
@Data
@Entity
@Table(name = "F_META_CHANG_LOG")
public class MetaChangLog implements java.io.Serializable {
    private static final long serialVersionUID =  1L;

    /**
     * 编号 null
     */
    @Id
    @Column(name = "CHANGE_ID")
    @GeneratedValue(generator = "assignedGenerator")
    //@GenericGenerator(name = "assignedGenerator", strategy = "assigned")
    private Long  changeId;
    /**
     * 版本号 null
     */
    @Column(name = "TABLE_ID")
    private String tableID;
    /**
     * 提交日期 null
     */
    @Column(name = "CHANGE_DATE")
    @OrderBy(value = "DESC")
    private Date  changeDate;
    /**
     * 提交人 null
     */
    @Column(name = "CHANGER")
    @NotBlank(message = "字段不能为空")
    //@Length(max = 6, message = "字段长度不能大于{max}")
    private String  changer;

    /**
     * 更改脚本 null
     */
    @Column(name = "CHANGE_SCRIPT")
    private String  changeScript;
    /**
     * 更改说明 null
     */
    @Column(name = "CHANGE_COMMENT")
    @Length(max = 2048, message = "字段长度不能大于{max}")
    private String  changeComment;


    // Constructors
    /** default constructor */
    public MetaChangLog() {
        this.changeDate= DatetimeOpt.currentUtilDate();
    }
    /** minimal constructor */
    public MetaChangLog(
        String tableID
        ,Date  changeDate,String  changer) {

        this.tableID = tableID;

        this.changeDate= changeDate;
        this.changer= changer;
    }

/** full constructor */
    public MetaChangLog(
     String tableID
    ,Long  changeId,Date  changeDate,String  changer,String  changeScript,String  changeComment) {


        this.tableID = tableID;

        this.changeId= changeId;
        this.changeDate= changeDate;
        this.changer= changer;
        this.changeScript= changeScript;
        this.changeComment= changeComment;
    }

    public MetaChangLog copy(MetaChangLog other){

        this.setTableID(other.getTableID());

        this.changeId= other.getChangeId();
        this.changeDate= other.getChangeDate();
        this.changer= other.getChanger();
        this.changeScript= other.getChangeScript();
        this.changeComment= other.getChangeComment();
        return this;
    }

    public MetaChangLog copyNotNullProperty(MetaChangLog other){

    if( other.getTableID() != null)
        this.setTableID(other.getTableID());

        if( other.getChangeId() != null)
            this.changeId= other.getChangeId();
        if( other.getChangeDate() != null)
            this.changeDate= other.getChangeDate();
        if( other.getChanger() != null)
            this.changer= other.getChanger();

        if( other.getChangeScript() != null)
            this.changeScript= other.getChangeScript();
        if( other.getChangeComment() != null)
            this.changeComment= other.getChangeComment();

        return this;
    }

    public MetaChangLog clearProperties(){

        this.changeId= null;
        this.changeDate= null;
        this.changer= null;
        this.changeScript= null;
        this.changeComment= null;
        return this;
    }
}
