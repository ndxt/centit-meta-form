package com.centit.metaform.formaccess.impl;

import java.sql.Connection;
import java.sql.SQLException;

import com.centit.support.database.jsonmaptable.*;
import com.centit.support.database.utils.DBType;
import com.centit.support.database.utils.DataSourceDescription;
import com.centit.support.database.utils.DbcpConnectPools;

public class JdbcModelRuntimeContext extends AbstractModelRuntimeContext{

    private Connection conn;
    private DataSourceDescription dataSource;

    public JdbcModelRuntimeContext(){

    }

    public JdbcModelRuntimeContext(String  modelCode){
        super(modelCode);
    }


    public Connection getConnection() throws SQLException {
        if(conn==null){
             conn = DbcpConnectPools.getDbcpConnect(dataSource);
        }
        return conn;
    }

    public void setDataSource(DataSourceDescription dataSource) {
        this.dataSource = dataSource;
    }

    private JsonObjectDao dao = null;

    public JsonObjectDao getJsonObjectDao() throws SQLException {
        if(dao==null){
            dao = GeneralJsonObjectDao.createJsonObjectDao(getConnection(),
                    getPersistenceTableInfo());
        }
        return dao;
    }

    public void close(){
        if(conn!=null){
            try {
                conn.close();

            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        conn = null;
    }

    public void commitAndClose(){
        if(conn!=null){
            try {
                conn.commit();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        conn = null;
    }

    public void rollbackAndClose(){
        if(conn!=null){
            try {
                conn.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        conn = null;
    }
}
