package com.centit.metaform.fromaccess.impl;

import java.sql.SQLException;

import com.centit.support.database.DBConnect;
import com.centit.support.database.DataSourceDescription;
import com.centit.support.database.DbcpConnectPools;
import com.centit.support.database.jsonmaptable.DB2JsonObjectDao;
import com.centit.support.database.jsonmaptable.JsonObjectDao;
import com.centit.support.database.jsonmaptable.MySqlJsonObjectDao;
import com.centit.support.database.jsonmaptable.OracleJsonObjectDao;
import com.centit.support.database.jsonmaptable.SqlSvrJsonObjectDao;

public class JdbcModelRuntimeContext extends AbstractModelRuntimeContext{

	private DBConnect conn;
	private DataSourceDescription dataSource;

	public JdbcModelRuntimeContext(){
		
	}
	
	public JdbcModelRuntimeContext(String  modelCode){
		super(modelCode);
	}
	

	public DBConnect getConnection() {
		if(conn==null){
			 conn = DbcpConnectPools.getDbcpConnect(dataSource);
		}
		return conn;
	}

	public void setDataSource(DataSourceDescription dataSource) {
		this.dataSource = dataSource;
	}

	
	
	private JsonObjectDao dao = null;
	
	public JsonObjectDao getJsonObjectDao(){
		if(dao==null){
			switch(getConnection().getDatabaseType()){
			case Oracle:
		  		return new OracleJsonObjectDao(getConnection() ,getTableInfo());
		  	case DB2:
		  		return new DB2JsonObjectDao(getConnection() ,getTableInfo());
		  	case SqlServer:
		  		return new SqlSvrJsonObjectDao(getConnection() ,getTableInfo());
		  	case MySql:
		  		return new MySqlJsonObjectDao(getConnection() ,getTableInfo());
		  	default:
		  		return new OracleJsonObjectDao(getConnection() ,getTableInfo());
			}
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
	}	
}
