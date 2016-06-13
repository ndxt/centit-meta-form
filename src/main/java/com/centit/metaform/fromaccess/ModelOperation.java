package com.centit.metaform.fromaccess;

public class ModelOperation {
	private String modelCode;
	private String operation;
	private String method;
	private String label;
	private String url;
	private Long displayOrder;
	private String openType;
	private String returnOperation;
	private String optHintType;
	private String optHintInfo;
	private String extendOptions;
	
	public ModelOperation(){
		
	}

	public ModelOperation(String modelCode, String operation, String method,String label){
		this.modelCode = modelCode;
		this.operation = operation;
		this.method = method;
		this.label = label;
	}
	
	public Long getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}

	public String getReturnOperation() {
		return returnOperation;
	}

	public void setReturnOperation(String returnOperation) {
		this.returnOperation = returnOperation;
	}

	public String getOptHintType() {
		return optHintType;
	}

	public void setOptHintType(String optHintType) {
		this.optHintType = optHintType;
	}

	public String getOptHintInfo() {
		return optHintInfo;
	}

	public void setOptHintInfo(String optHintInfo) {
		this.optHintInfo = optHintInfo;
	}

	public String getExtendOptions() {
		return extendOptions;
	}

	public void setExtendOptions(String extendOptions) {
		this.extendOptions = extendOptions;
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
