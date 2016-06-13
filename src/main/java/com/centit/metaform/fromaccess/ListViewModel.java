package com.centit.metaform.fromaccess;

import java.util.ArrayList;
import java.util.List;

public class ListViewModel extends  MateFormModel{
	
	public ListViewModel(){
		
	}
	
	public ListViewModel(String modelName){
		super(modelName);
	}

	public ListViewModel(List<FormField> filters){
		super(filters);
	}
	
	public ListViewModel(List<FormField> filters,List<ModelOperation> operations){
		super(filters,operations);
	}
	
	public ListViewModel(List<FormField> filters,List<ModelOperation> operations,
				List<ListColumn> columns){
		super(filters,operations);
		this.columns = columns;
	}
	

	private List<ListColumn> columns;	
	
	public List<ListColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<ListColumn> columns) {
		this.columns = columns;
	}


	public void addColumn(ListColumn column) {
		if(this.columns == null)
			this.columns = new ArrayList<>();
		this.columns.add(column);
	}
	
}
