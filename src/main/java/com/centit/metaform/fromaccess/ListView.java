package com.centit.metaform.fromaccess;

import java.util.List;

public class ListView {
	public ListView(){
		
	}
	private List<FormField> filters;
	
	private List<ListColumn> columns;
	
	private List<ModelOperation> operations;

	public List<ListColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<ListColumn> columns) {
		this.columns = columns;
	}

	public List<ModelOperation> getOperations() {
		return operations;
	}

	public void setOperations(List<ModelOperation> operations) {
		this.operations = operations;
	}

	public List<FormField> getFilters() {
		return filters;
	}

	public void setFilters(List<FormField> filters) {
		this.filters = filters;
	}
	
}
