package com.centit.metaform.fromaccess;

import java.util.Map;

public class FormField {

	private String key;
	private String type;
	private String defaultValue;
	private FieldTemplateOptions templateOptions;
	private String hideExpression;
	private Map<String,String> expressionProperties;
	private Map<String,Object> modelOptions;
	private Boolean noFormControl;
	private String template;
	private String templateUrl;
	private Long rows;
	
	private Map<String,FieldValidator> validators;
	
	public FormField(){
		
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public FieldTemplateOptions getTemplateOptions() {
		return templateOptions;
	}

	public void setTemplateOptions(FieldTemplateOptions templateOptions) {
		this.templateOptions = templateOptions;
	}

	public String getHideExpression() {
		return hideExpression;
	}

	public void setHideExpression(String hideExpression) {
		this.hideExpression = hideExpression;
	}

	public Map<String, String> getExpressionProperties() {
		return expressionProperties;
	}

	public void setExpressionProperties(Map<String, String> expressionProperties) {
		this.expressionProperties = expressionProperties;
	}

	public Map<String, Object> getModelOptions() {
		return modelOptions;
	}

	public void setModelOptions(Map<String, Object> modelOptions) {
		this.modelOptions = modelOptions;
	}

	public Boolean isNoFormControl() {
		return noFormControl;
	}

	public void setNoFormControl(boolean noFormControl) {
		this.noFormControl = noFormControl;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplateUrl() {
		return templateUrl;
	}

	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}

	public Map<String, FieldValidator> getValidators() {
		return validators;
	}

	public void setValidators(Map<String, FieldValidator> validators) {
		this.validators = validators;
	}

	public Long getRows() {
		return rows;
	}

	public void setRows(Long rows) {
		this.rows = rows;
	}
	
}
