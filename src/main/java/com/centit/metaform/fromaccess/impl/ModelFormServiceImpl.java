package com.centit.metaform.fromaccess.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.core.dao.PageDesc;
import com.centit.metaform.fromaccess.ModelFormService;
import com.centit.metaform.fromaccess.ModelRuntimeContext;
import com.centit.support.database.DataSourceDescription;
import com.centit.support.database.DbcpConnectPools;
import com.centit.support.database.jsonmaptable.JsonObjectDao;
import com.centit.support.database.metadata.SimpleTableField;
import com.centit.support.database.metadata.SimpleTableInfo;

@Service
public class ModelFormServiceImpl implements ModelFormService {

	@Override
	public ModelRuntimeContext createRuntimeContext(String modelCode) {
		ModelRuntimeContext rc = new ModelRuntimeContext();
		
		DataSourceDescription dbc = new DataSourceDescription();	  
		dbc.setConnUrl("jdbc:oracle:thin:@192.168.131.81:1521:orcl");
		dbc.setUsername("metaform");
		dbc.setPassword("metaform");
		rc.setConnection( DbcpConnectPools.getDbcpConnect(dbc));
		
		SimpleTableInfo tableInfo = new SimpleTableInfo("TEST_TABLE");
		SimpleTableField field = new SimpleTableField();
		field.setColumnName("ID");
		field.setColumnType("Number(10)");
		field.setPrecision(10);
		field.setScale(0);
		field.setMandatory(true);
		field.setPropertyName("id");
		tableInfo.getColumns().add(field);
		
		field = new SimpleTableField();
		field.setColumnName("USER_NAME");
		field.setColumnType("varchar2");
		field.setPropertyName("userName");
		field.setMaxLength(50);
		tableInfo.getColumns().add(field);
		
		field = new SimpleTableField();
		field.setColumnName("USER_PHONE");
		field.setColumnType("varchar2");
		field.setMaxLength(20);
		field.setPropertyName("userPhone");
		field.setDefaultValue("'110'");
		tableInfo.getColumns().add(field);			
		
		tableInfo.getPkColumns().add("ID");
		rc.setTableinfo(tableInfo);
		return rc;
	}

	@Override
	public JSONArray listObjectsByFilter(ModelRuntimeContext rc, Map<String, Object> filters) {
		JsonObjectDao dao = rc.getJsonObjectDao();
		try {
			return dao.listObjectsByProperties(filters);
		} catch (SQLException | IOException e) {
			return null;
		}
	}
	
	@Override
	public JSONArray listObjectsByFilter(ModelRuntimeContext rc, Map<String, Object> filters, PageDesc pageDesc) {
		JsonObjectDao dao = rc.getJsonObjectDao();
		try {
			JSONArray ja = dao.listObjectsByProperties(filters,
						(pageDesc.getPageNo()-1)>0? (pageDesc.getPageNo()-1)*pageDesc.getPageSize():0,
						pageDesc.getPageSize());
			Long ts = dao.fetchObjectsCount(filters);
			if(ts!=null)
				pageDesc.setTotalRows(ts.intValue());
			return ja;
		} catch (SQLException | IOException e) {
			return null;
		}
	}
	
	
	@Override
	public JSONObject getObjectByProperties(ModelRuntimeContext rc,Map<String, Object> properties){
		JsonObjectDao dao = rc.getJsonObjectDao();
		try {
			return dao.getObjectByProperties(properties);
		} catch (SQLException | IOException e) {
			return null;
		}
	}

	@Override
	public void saveNewObject(ModelRuntimeContext rc, Map<String, Object> object) throws SQLException {
		JsonObjectDao dao = rc.getJsonObjectDao();
		dao.saveNewObject(object);
	}

	@Override
	public void updateObject(ModelRuntimeContext rc, Map<String, Object> object) throws SQLException {
		JsonObjectDao dao = rc.getJsonObjectDao();
		dao.updateObject(object);
	}

	@Override
	public void deleteObjectById(ModelRuntimeContext rc, Map<String, Object> keyValue) throws SQLException {
		JsonObjectDao dao = rc.getJsonObjectDao();
		dao.deleteObjectById(keyValue);
	}

}
