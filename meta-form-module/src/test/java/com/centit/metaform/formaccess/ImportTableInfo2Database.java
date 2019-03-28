package com.centit.metaform.formaccess;

import com.centit.product.dbdesign.po.PendingMetaColumn;
import com.centit.product.dbdesign.po.PendingMetaTable;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.database.utils.DataSourceDescription;
import com.centit.support.database.utils.DatabaseAccess;
import com.centit.support.database.utils.DbcpConnectPools;
import org.apache.commons.lang3.tuple.Pair;
import java.sql.Connection;
import java.util.List;

public class ImportTableInfo2Database {

    public static void main(String[] args) {
        DataSourceDescription dbc = new DataSourceDescription();
        dbc.setConnUrl("jdbc:oracle:thin:@192.168.131.81:1521:orcl");
        dbc.setUsername("metaform");
        dbc.setPassword("metaform");
        String pdmFilePath =
              "D:/Projects/framework3/centit-meta-form/document/内部经营数据管理系统文档/公司经营数据管理（一期）物理模型设计.pdm";

        List<Pair<String, String>> tables = PdmTableInfo.listTablesInPdm(pdmFilePath);
        if(tables==null){
            System.out.println("读取文件出错!");
            return;
        }
        try {
            Connection conn= DbcpConnectPools.getDbcpConnect(dbc);
            for(Pair<String, String> t : tables){
                Long tableId =  NumberBaseOpt.castObjectToLong(DatabaseAccess.getScalarObjectQuery(
                         conn,
                         "SELECT SEQ_PENDINGTABLEID.nextval from dual"));
                PendingMetaTable metaTable = PdmTableInfo.importTableFromPdm(pdmFilePath, t.getLeft(), "1");
                DatabaseAccess.doExecuteSql(conn, "insert into F_PENDING_META_TABLE"
                        + "(Table_ID,Database_Code,Table_Name,Table_Label_Name,table_type,table_state,table_Comment) "
                        + "values(?,'1',?,?,'T','N',?)",
                    new Object[]{tableId,metaTable.getTableName(),metaTable.getTableLabelName(),metaTable.getTableComment()});
                for(PendingMetaColumn col: metaTable.getColumns()){
                    DatabaseAccess.doExecuteSql(conn, "insert into F_PENDING_META_COLUMN"
                            + "(Table_ID,column_Name,field_Label_Name,column_Type,access_type,"
                            + "max_Length,scale,column_state,column_Comment,auto_create_Rule,"
                            + "auto_create_Param,primarykey) "
                            + "values(?,?,?,?,'N', ?,?,'N',?,?,?,?)",
                        new Object[]{tableId,col.getColumnName(),col.getFieldLabelName(),col.getColumnFieldType(),
                                col.getMaxLength(),col.getScale(),col.getColumnComment(),col.getAutoCreateRule(),
                                col.getAutoCreateParam(),col.getPrimarykey()});
                }
                conn.commit();
                System.out.println("导入表:"+t.getRight()+"("+t.getLeft()+")!");
            }
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("done!");
    }

}
