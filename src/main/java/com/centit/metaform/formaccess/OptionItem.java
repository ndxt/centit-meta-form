package com.centit.metaform.formaccess;

public class OptionItem {
	private String name;
	private String value;
	private String group;
	
	public OptionItem(){
		
	}
	
	public OptionItem(String name,String value){
		this.name = name;
		this.value = value;
	}
	
	public OptionItem(String name,String value,String group){
		this.name = name;
		this.value = value;
		this.group = group;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getGroup() {
		return group;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}

}
