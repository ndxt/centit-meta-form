package com.centit.metaform.fromaccess;

import java.util.ArrayList;
import java.util.List;

public class ListViewModel extends  MateFormModel{
	
	public ListViewModel(){
		
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
