package com.centit.metaform.formaccess;

import java.util.ArrayList;
import java.util.List;

public class MateFormModel {
	private String modelName;
	private String extendOptBean;
	private String extendOptBeanParam;
	private String accessType;
	
	private List<FormField> filters;
	private List<ModelOperation> operations;
	
	
	public MateFormModel(){
		
	}
		
	public MateFormModel(String modelName){
		this.modelName = modelName;
	}

	public MateFormModel(List<FormField> filters){
		this.filters = filters;
	}
	
	public MateFormModel(List<FormField> filters,List<ModelOperation> operations){
		this.filters = filters;
		this.operations = operations;
	}
	
	
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getExtendOptBean() {
		return extendOptBean;
	}

	public void setExtendOptBean(String extendOptBean) {
		this.extendOptBean = extendOptBean;
	}

	public String getExtendOptBeanParam() {
		return extendOptBeanParam;
	}

	public void setExtendOptBeanParam(String extendOptBeanParam) {
		this.extendOptBeanParam = extendOptBeanParam;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public List<ModelOperation> getOperations() {
		return operations;
	}

	public void setOperations(List<ModelOperation> operations) {
		this.operations = operations;
	}

	public void addOperation(ModelOperation operation) {
		if(this.operations == null)
			this.operations = new ArrayList<>();
		this.operations.add(operation);
	}
	
	public List<FormField> getFilters() {
		return filters;
	}

	public void setFilters(List<FormField> filters) {
		this.filters = filters;
	}
	
	public void addFilter(FormField filter) {
		if(this.filters == null)
			this.filters = new ArrayList<>();
		this.filters.add(filter);
	}
	
}
