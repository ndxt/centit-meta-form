package com.centit.metaform.formaccess;

public class ListColumn {
	private String name;
	private String label;
	private String url;
	private Boolean show;
	private Boolean primaryKey;
	
	public ListColumn(){
		show = true;
	}
	public ListColumn(String name,String label){
		this.name = name;
		this.label = label;
	}
	
	public Boolean isShow() {
		return show;
	}
	
	public void setShow(Boolean show) {
		this.show = show;
	}
	public ListColumn(String name,String label,String url){
		this.name = name;
		this.label = label;
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Boolean getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(Boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	
}
