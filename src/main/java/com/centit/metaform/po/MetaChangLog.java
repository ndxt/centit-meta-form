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
	@Column(name = "VERSION")
	private Long version;
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
	@Length(min = 0, max = 6, message = "字段长度不能小于{min}大于{max}")
	private String  changer;
	/**
	 * 更改表总数 null 
	 */
	@Column(name = "CHANGE_TABLE_SUM")
	private Long  changeTableSum;
	/**
	 * 更改关系总数 null 
	 */
	@Column(name = "CHANGE_RELATION_SUM")
	private Long  changeRelationSum;
	/**
	 * 更改脚本 null 
	 */
	@Column(name = "CHANGE_SCRIPT")
	@Length(min = 0,message = "字段长度不能小于{min}大于{max}")
	private String  changeScript;
	/**
	 * 更改说明 null 
	 */
	@Column(name = "CHANGE_COMMENT")
	@Length(min = 0, max = 2048, message = "字段长度不能小于{min}大于{max}")
	private String  changeComment;
	/**
	 * 审核人 null 
	 */
	@Column(name = "AUDITOR")
	@Length(min = 0, max = 6, message = "字段长度不能小于{min}大于{max}")
	private String  auditor;
	/**
	 * 审核时间 null 
	 */
	@Column(name = "AUDIT_DATE")
	private Date  auditDate;

	// Constructors
	/** default constructor */
	public MetaChangLog() {
	}
	/** minimal constructor */
	public MetaChangLog(
		Long version		
		,Date  changeDate,String  changer,Long  changeTableSum,Long  changeRelationSum) {
	
	
		this.version = version;		
	
		this.changeDate= changeDate; 
		this.changer= changer; 
		this.changeTableSum= changeTableSum; 
		this.changeRelationSum= changeRelationSum; 		
	}

/** full constructor */
	public MetaChangLog(
	 Long version		
	,Long  changeId,Date  changeDate,String  changer,Long  changeTableSum,Long  changeRelationSum,String  changeScript,String  changeComment,String  auditor,Date  auditDate) {
	
	
		this.version = version;		
	
		this.changeId= changeId;
		this.changeDate= changeDate;
		this.changer= changer;
		this.changeTableSum= changeTableSum;
		this.changeRelationSum= changeRelationSum;
		this.changeScript= changeScript;
		this.changeComment= changeComment;
		this.auditor= auditor;
		this.auditDate= auditDate;		
	}
	

  
	public Long getVersion() {
		return this.version;
	}

	public void setVersion(Long version) {
		this.version = version;
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
  
	public Long getChangeTableSum() {
		return this.changeTableSum;
	}
	
	public void setChangeTableSum(Long changeTableSum) {
		this.changeTableSum = changeTableSum;
	}
  
	public Long getChangeRelationSum() {
		return this.changeRelationSum;
	}
	
	public void setChangeRelationSum(Long changeRelationSum) {
		this.changeRelationSum = changeRelationSum;
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
  
	public String getAuditor() {
		return this.auditor;
	}
	
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
  
	public Date getAuditDate() {
		return this.auditDate;
	}
	
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}



	public MetaChangLog copy(MetaChangLog other){
  
		this.setVersion(other.getVersion());
  
		this.changeId= other.getChangeId();  
		this.changeDate= other.getChangeDate();  
		this.changer= other.getChanger();  
		this.changeTableSum= other.getChangeTableSum();  
		this.changeRelationSum= other.getChangeRelationSum();  
		this.changeScript= other.getChangeScript();  
		this.changeComment= other.getChangeComment();  
		this.auditor= other.getAuditor();  
		this.auditDate= other.getAuditDate();

		return this;
	}
	
	public MetaChangLog copyNotNullProperty(MetaChangLog other){
  
	if( other.getVersion() != null)
		this.setVersion(other.getVersion());
  
		if( other.getChangeId() != null)
			this.changeId= other.getChangeId();  
		if( other.getChangeDate() != null)
			this.changeDate= other.getChangeDate();  
		if( other.getChanger() != null)
			this.changer= other.getChanger();  
		if( other.getChangeTableSum() != null)
			this.changeTableSum= other.getChangeTableSum();  
		if( other.getChangeRelationSum() != null)
			this.changeRelationSum= other.getChangeRelationSum();  
		if( other.getChangeScript() != null)
			this.changeScript= other.getChangeScript();  
		if( other.getChangeComment() != null)
			this.changeComment= other.getChangeComment();  
		if( other.getAuditor() != null)
			this.auditor= other.getAuditor();  
		if( other.getAuditDate() != null)
			this.auditDate= other.getAuditDate();		

		return this;
	}

	public MetaChangLog clearProperties(){
  
		this.changeId= null;  
		this.changeDate= null;  
		this.changer= null;  
		this.changeTableSum= null;  
		this.changeRelationSum= null;  
		this.changeScript= null;  
		this.changeComment= null;  
		this.auditor= null;  
		this.auditDate= null;

		return this;
	}
}
