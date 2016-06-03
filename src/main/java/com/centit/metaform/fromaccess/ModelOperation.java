package com.centit.metaform.fromaccess;

public class ModelOperation {
	private String modelCode;
	private String operation;
	private String method;
	private String label;
	private String url;
	
	public ModelOperation(){
		
	}

	public ModelOperation(String modelCode, String operation, String method,String label){
		this.modelCode = modelCode;
		this.operation = operation;
		this.method = method;
		this.label = label;
	}
	
	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
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

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
}
