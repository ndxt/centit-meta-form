package com.centit.metaform.formaccess;

import java.util.ArrayList;
import java.util.List;

public class FormFieldGroup {

	private String className;
	private List<FormField> fieldGroup;
	
	public FormFieldGroup(){
		className = "row";
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<FormField> getFieldGroup() {
		return fieldGroup;
	}
	public void setFieldGroup(List<FormField> fieldGroup) {
		this.fieldGroup = fieldGroup;
	}
	
	public void addField(FormField field) {
		if(this.fieldGroup == null)
			this.fieldGroup = new ArrayList<>();
		this.fieldGroup.add(field);
	}

}
