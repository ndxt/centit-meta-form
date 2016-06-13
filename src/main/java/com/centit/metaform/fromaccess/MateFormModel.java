package com.centit.metaform.fromaccess;

import java.util.ArrayList;
import java.util.List;

public class MateFormModel {
	public MateFormModel(){
		
	}
	private List<FormField> filters;

	
	private List<ModelOperation> operations;

	
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
