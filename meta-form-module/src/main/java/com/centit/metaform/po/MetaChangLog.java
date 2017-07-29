package com.centit.metaform.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.centit.support.algorithm.DatetimeOpt;


/**
 * create by scaffold 2016-06-01 
 
 
  元数据更改记录null   
*/
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
	@GenericGenerator(name = "assignedGenerator", strategy = "assigned")
	private Long  changeId;
	/**
	 * 版本号 null 
	 */
	@Column(name = "TABLE_ID")
	private Long tableID;
	/**
	 * 提交日期 null 
	 */
	@Column(name = "CHANGE_DATE")
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
		Long tableID		
		,Date  changeDate,String  changer) {	
	
		this.tableID = tableID;		
	
		this.changeDate= changeDate; 
		this.changer= changer; 
	}

/** full constructor */
	public MetaChangLog(
	 Long tableID		
	,Long  changeId,Date  changeDate,String  changer,String  changeScript,String  changeComment) {
	
	
		this.tableID = tableID;		
	
		this.changeId= changeId;
		this.changeDate= changeDate;
		this.changer= changer;
		this.changeScript= changeScript;
		this.changeComment= changeComment;
	}
	

  
	public Long getTableID() {
		return this.tableID;
	}

	public void setTableID(Long tableID) {
		this.tableID = tableID;
	}
	// Property accessors
  
	public Long getChangeId() {
		return this.changeId;
	}
	
	public void setChangeId(Long changeId) {
		this.changeId = changeId;
	}
  
	public Date getChangeDate() {
		return this.changeDate;
	}
	
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
  
	public String getChanger() {
		return this.changer;
	}
	
	public void setChanger(String changer) {
		this.changer = changer;
	}
  
	  
	public String getChangeScript() {
		return this.changeScript;
	}
	
	public void setChangeScript(String changeScript) {
		this.changeScript = changeScript;
	}
  
	public String getChangeComment() {
		return this.changeComment;
	}
	
	public void setChangeComment(String changeComment) {
		this.changeComment = changeComment;
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
