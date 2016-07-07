package com.centit.metaform.formaccess;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class OptionItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String value;
	private String group;
	
	public OptionItem(){
		
	}
	public OptionItem(String name){
		this.name = name;
		this.value = name;
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

	@Override  
    public int hashCode() {
		if(name==null)
			return 0;
        return name.hashCode();  
    }  
      
    @Override  
    public boolean equals(Object obj) {
    	if(obj instanceof String){
    		return StringUtils.equals(name, 
    				(String)obj);
    	}
    	if(obj instanceof OptionItem){
    		return StringUtils.equals(name, 
    				((OptionItem)obj).getName());
    	}
    	return false;
    }
}
