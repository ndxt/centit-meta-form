package com.centit.metaform.formaccess;

import com.centit.support.metadata.po.MetaColumn;

public class ListColumn {
    private String name;
    private String label;
    private String url;
    private Boolean primaryKey;

    public ListColumn(){

    }
    public ListColumn(String name,String label){
        this.name = name;
        this.label = label;
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


    public ListColumn(MetaColumn metaColumn) {
        this.name = metaColumn.getColumnName();
        this.label = metaColumn.getFieldLabelName();
        this.primaryKey = metaColumn.isPrimaryKey();
    }
}
