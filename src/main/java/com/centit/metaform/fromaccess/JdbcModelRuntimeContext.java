package com.centit.metaform.fromaccess;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.centit.framework.core.controller.BaseController;
import com.centit.support.database.DBConnect;
import com.centit.support.database.DataSourceDescription;
import com.centit.support.database.DbcpConnectPools;
import com.centit.support.database.jsonmaptable.DB2JsonObjectDao;
import com.centit.support.database.jsonmaptable.JsonObjectDao;
import com.centit.support.database.jsonmaptable.MySqlJsonObjectDao;
import com.centit.support.database.jsonmaptable.OracleJsonObjectDao;
import com.centit.support.database.jsonmaptable.SqlSvrJsonObjectDao;
import com.centit.support.database.metadata.TableInfo;

public class JdbcModelRuntimeContext {
	private String  modelCode;
	private DBConnect conn;
	private TableInfo tableinfo;
	private DataSourceDescription dataSource;
	private List<FormField> formFields; 
	
	public JdbcModelRuntimeContext(){
		
	}
	
	public JdbcModelRuntimeContext(String  modelCode){
		this.modelCode = modelCode;
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

	public TableInfo getTableinfo() {
		return tableinfo;
	}

	public void setTableinfo(TableInfo tableinfo) {
		this.tableinfo = tableinfo;
	}
	
	private JsonObjectDao dao = null;
	
	public JsonObjectDao getJsonObjectDao(){
		if(dao==null){
			switch(getConnection().getDatabaseType()){
			case Oracle:
		  		return new OracleJsonObjectDao(getConnection() ,tableinfo);
		  	case DB2:
		  		return new DB2JsonObjectDao(getConnection() ,tableinfo);
		  	case SqlServer:
		  		return new SqlSvrJsonObjectDao(getConnection() ,tableinfo);
		  	case MySql:
		  		return new MySqlJsonObjectDao(getConnection() ,tableinfo);
		  	default:
		  		return new OracleJsonObjectDao(getConnection() ,tableinfo);
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
	
	public ListViewDescription getListViewDesc(){
		ListViewDescription lv = new ListViewDescription();
		FormField ff = new FormField();
		ff.setKey(BaseController.SEARCH_STRING_PREFIX + "userName");
		ff.setType("input");
		FieldTemplateOptions templateOptions = new FieldTemplateOptions();
		templateOptions.setLabel("姓名：");
		templateOptions.setPlaceholder("请输入完整的姓名。");
		ff.setTemplateOptions(templateOptions);
		lv.addFilter(ff);
		ListColumn id = new ListColumn("id","编号");
		id.setPrimaryKey(true);
		lv.addColumn(id);
		lv.addColumn(new ListColumn("userName","用户姓名"));
		lv.addColumn(new ListColumn("userPhone","电话"));
		
		lv.addOperation(new ModelOperation(modelCode,"view","get","查看"));
		lv.addOperation(new ModelOperation(modelCode,"edit","get","编辑"));
		return lv;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	
	public List<FormField> getFormFields(){
		
		return this.formFields;
	}

	public void addFormField(FormField ff){
		if(formFields==null)
			formFields = new ArrayList<>();
		formFields.add(ff);
	}

	public void setFormFields(List<FormField> formFields) {
		this.formFields = formFields;
	}
}
