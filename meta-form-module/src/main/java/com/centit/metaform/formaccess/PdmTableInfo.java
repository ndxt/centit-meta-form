package com.centit.metaform.formaccess;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.centit.product.dbdesign.po.PendingMetaColumn;
import com.centit.product.dbdesign.po.PendingMetaTable;
import com.centit.support.database.metadata.PdmReader;
import com.centit.support.database.metadata.SimpleTableField;
import com.centit.support.database.metadata.SimpleTableInfo;

public class PdmTableInfo{

    public static List<Pair<String, String>> listTablesInPdm(String pdmFilePath) {
        PdmReader pdmReader = new PdmReader();
        if(!pdmReader.loadPdmFile(pdmFilePath))
            return null;

        return pdmReader.getAllTableCode();
    }

    public static PendingMetaTable importTableFromPdm(String pdmFilePath, String tableCode, String databaseCode) {
        PdmReader pdmReader = new PdmReader();
        if(!pdmReader.loadPdmFile(pdmFilePath))
            return null;
        SimpleTableInfo pdmTable = pdmReader.getTableMetadata(tableCode);
        PendingMetaTable metaTable = new PendingMetaTable();

        metaTable.setDatabaseCode(databaseCode);
        metaTable.setTableName(pdmTable.getTableName());
        metaTable.setTableLabelName(pdmTable.getTableLabelName());
        metaTable.setTableState("N");
        metaTable.setTableType("T");
        metaTable.setTableComment(pdmTable.getTableComment());

        for(SimpleTableField field : pdmTable.getColumns()){
            PendingMetaColumn mdColumn = new PendingMetaColumn();
            mdColumn.setColumnName(field.getColumnName());
            mdColumn.setColumnFieldType(field.getColumnType());
            mdColumn.setColumnComment(field.getColumnComment());
            mdColumn.setMaxLengthM(field.getMaxLength());
            mdColumn.setScaleM(field.getScale());
            if(StringUtils.isNotBlank(field.getDefaultValue())){
                mdColumn.setAutoCreateRule("C");
                mdColumn.setAutoCreateParam(field.getDefaultValue());
            }
            mdColumn.setAccessType("N");
            mdColumn.setMandatory(field.isMandatory()?"T":"F");
            mdColumn.setPrimarykey("F");
            mdColumn.setColumnState("N");

            metaTable.addMdColumn(mdColumn);
        }

        for(String pkcode : pdmTable.getPkColumns()){
            PendingMetaColumn col = metaTable.findFieldByColumn(pkcode);
            if(col!=null)
                col.setPrimarykey("T");
        }

        return metaTable;
    }

}
