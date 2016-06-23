package com.centit.metaform.formaccess;

import java.util.ArrayList;
import java.util.List;

public class FieldTemplateOptions {
	
	private String label;
	private Boolean required;
	private List<NameValuePair> options;
	private String placeholder;
	private String description;
	private Boolean focus;
	/**
	 * 正则表达式
	 */	
	private String pattern;
	/**
	 * 文件上传
	 */
	private String url;
	
	public FieldTemplateOptions(){
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public List<NameValuePair> getOptions() {
		return options;
	}

	public void setOptions(List<NameValuePair> options) {
		this.options = options;
	}

	public void addOption(NameValuePair option) {
		if(this.options == null)
			this.options = new ArrayList<>();
		this.options.add(option);
	}
	
	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean isFocus() {
		return focus;
	}

	public void setFocus(boolean focus) {
		this.focus = focus;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}	
}
