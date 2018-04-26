package com.centit.metaform.formaccess;

import com.centit.metaform.po.ModelOperation;

import java.util.ArrayList;
import java.util.List;

public class ListViewDefine extends  MetaFormDefine{

    public ListViewDefine(){

    }

    public ListViewDefine(String modelName){
        super(modelName,"list");
    }

    public ListViewDefine(List<FormField> filters){
        super(filters);
    }

    public ListViewDefine(List<FormField> filters,List<ModelOperation> operations){
        super(filters,operations);
    }

    public ListViewDefine(List<FormField> filters,List<ModelOperation> operations,
                List<ListColumn> columns){
        super(filters,operations);
        this.columns = columns;
    }

    private List<String> primaryKey;
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

    public List<String> getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(List<String> primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void addPrimaryKey(String pk) {
        if(this.primaryKey==null)
            this.primaryKey = new ArrayList<>();
        this.primaryKey.add(pk);
    }
}
