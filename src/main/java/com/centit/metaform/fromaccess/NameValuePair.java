package com.centit.metaform.fromaccess;

public class NameValuePair {
	private String name;
	private String value;
	private String group;
	
	public NameValuePair(){
		
	}
	public NameValuePair(String name,String value){
		this.name = name;
		this.value = value;
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
