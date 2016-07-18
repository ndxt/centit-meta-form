package com.centit.metaform.formaccess;

import java.util.ArrayList;
import java.util.List;

import com.centit.framework.common.OptionItem;

public class FieldTemplateOptions {
	
	private String label;
	private Boolean required;
	private List<OptionItem> options;
	private String placeholder;
	private String description;
	private Boolean focus;
	private Boolean readonly;
	private Boolean disabled;
	private String format;
	/**
	 * 正则表达式
	 */	
	private String pattern;
	/**
	 * 文件上传
	 */
	private String url;
	
	public FieldTemplateOptions(){
		disabled = false;
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

	public List<OptionItem> getOptions() {
		return options;
	}

	public void setOptions(List<OptionItem> options) {
		this.options = options;
	}

	public void addOption(OptionItem option) {
		if(this.options == null)
			this.options = new ArrayList<>();
		this.options.add(option);
	}
	
	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Boolean getReadonly() {
		return readonly;
	}

	public void setReadonly(Boolean readonly) {
		this.readonly = readonly;
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

	public Boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}	
}
