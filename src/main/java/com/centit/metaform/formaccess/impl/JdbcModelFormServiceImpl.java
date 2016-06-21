package com.centit.metaform.formaccess.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoaderListener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.dao.DatabaseInfoDao;
import com.centit.dde.po.DatabaseInfo;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import com.centit.metaform.dao.MetaColumnDao;
import com.centit.metaform.dao.MetaFormModelDao;
import com.centit.metaform.dao.MetaTableDao;
import com.centit.metaform.dao.ModelDataFieldDao;
import com.centit.metaform.formaccess.FieldTemplateOptions;
import com.centit.metaform.formaccess.FormField;
import com.centit.metaform.formaccess.ListColumn;
import com.centit.metaform.formaccess.ListViewDefine;
import com.centit.metaform.formaccess.MateFormDefine;
import com.centit.metaform.formaccess.ModelFormService;
import com.centit.metaform.formaccess.ModelOperation;
import com.centit.metaform.formaccess.ModelRuntimeContext;
import com.centit.metaform.formaccess.OperationEvent;
import com.centit.metaform.po.MetaColumn;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.po.MetaTable;
import com.centit.support.database.DataSourceDescription;
import com.centit.support.database.jsonmaptable.JsonObjectDao;

@Service(value="jdbcModelFormService")
public class JdbcModelFormServiceImpl implements ModelFormService {

    @Resource(name = "databaseInfoDao") 
    private DatabaseInfoDao databaseInfoDao;

    @Resource
    private MetaTableDao tableDao;

    @Resource
    private MetaColumnDao columnDao;
    
    @Resource
    private MetaFormModelDao formModelDao;
    
    @Resource
    private ModelDataFieldDao formFieldDao;
    
	@Override
	public JdbcModelRuntimeContext createRuntimeContext(String modelCode) {
		
		JdbcModelRuntimeContext rc = new JdbcModelRuntimeContext(modelCode);		
		DataSourceDescription dbc = new DataSourceDescription();	  
		dbc.setConnUrl("jdbc:oracle:thin:@192.168.131.81:1521:orcl");
		dbc.setUsername("metaform");
		dbc.setPassword("metaform");
		rc.setDataSource(dbc);
		
		Set<MetaColumn> cols = new HashSet<MetaColumn>();
		 
		MetaTable tableInfo = new MetaTable();
		tableInfo.setTableName("TEST_TABLE");
		tableInfo.setTableLabelName("通讯录");
		MetaColumn field = new MetaColumn();
		field.setColumnName("ID");
		field.setColumnType("Number(10)");
		field.setMaxLength(10);
		field.setScale(0);
		field.setMandatory("T");
		field.setPrimarykey("Y");
		cols.add(field);
		
		field = new MetaColumn();
		field.setColumnName("USER_NAME");
		field.setColumnType("varchar2");
		field.setMaxLength(50);
		cols.add(field);
		
		field = new MetaColumn();
		field.setColumnName("USER_PHONE");
		field.setColumnType("varchar2");
		field.setMaxLength(20);
		field.setAutoCreateRule("C");
		field.setAutoCreateParam("'110'");
		cols.add(field);			
		
		tableInfo.setMdColumns(cols);
		
		rc.setTableInfo(tableInfo);
		
		rc.setMetaFormModel(new MetaFormModel());
		
		return rc;
	}
	
	@Transactional(readOnly=true)
	public JdbcModelRuntimeContext createRuntimeContextFromDB(String modelCode) {
		
		JdbcModelRuntimeContext rc = new JdbcModelRuntimeContext(modelCode);		
		
		MetaFormModel mfm = formModelDao.getObjectById(modelCode);
		MetaTable mtab = mfm.getMdTable();
		DatabaseInfo mdb = databaseInfoDao.getObjectById( mtab.getDatabaseCode());		
		rc.setTableInfo(mtab);		
		DataSourceDescription dbc = new DataSourceDescription();
		dbc.setDatabaseCode(mdb.getDatabaseCode());
		dbc.setConnUrl(mdb.getDatabaseUrl());
		dbc.setUsername(mdb.getUsername());
		dbc.setPassword(mdb.getPassword());
		
		rc.setDataSource(dbc);
		
		return rc;
	}

	@Override
	@Transactional
	public Map<String, Object> createNewPk(ModelRuntimeContext rc) throws SQLException {
		//rc.getTableinfo().getPkColumns()
		//TableField findFieldByColumn(String name)
		return null;
	}

	@Override
	@Transactional
	public JSONObject createInitialObject(ModelRuntimeContext rc) throws SQLException {
		// 查找所有 有自动生成策略的字段，并生成对应的值
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public MateFormDefine getFormDefine(ModelRuntimeContext rc,String operation) {
		List<FormField> fields = new ArrayList<>();
		FormField ff = new FormField();
		ff.setKey("id");
		ff.setType("input");
		FieldTemplateOptions templateOptions = new FieldTemplateOptions();
		templateOptions.setLabel("编号：");
		templateOptions.setPlaceholder("请输入数字。");
		ff.setTemplateOptions(templateOptions);
		fields.add(ff);
		
		ff = new FormField();
		ff.setKey("userName");
		ff.setType("input");
		templateOptions = new FieldTemplateOptions();
		templateOptions.setLabel("姓名：");
		templateOptions.setPlaceholder("请输入完整的姓名。");
		ff.setTemplateOptions(templateOptions);
		fields.add(ff);
		

		ff = new FormField();
		ff.setKey("userPhone");
		ff.setType("input");
		templateOptions = new FieldTemplateOptions();
		templateOptions.setLabel("电话：");
		templateOptions.setPlaceholder("请输入电话号码。");
		ff.setTemplateOptions(templateOptions);
		fields.add(ff);
		
		return new MateFormDefine(fields);
	}

	@Override
	@Transactional(readOnly=true)
	public ListViewDefine getListViewModel(ModelRuntimeContext rc){
		ListViewDefine lv = new ListViewDefine();
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
		
		lv.addOperation(new ModelOperation(rc.getModelCode(),"view","get","查看"));
		lv.addOperation(new ModelOperation(rc.getModelCode(),"edit","get","编辑"));
		return lv;
	}
/*--------------------------------------------------------------------------------------------
*/	
	@Override
	@Transactional
	public JSONArray listObjectsByFilter(ModelRuntimeContext rc, Map<String, Object> filters) {
		JsonObjectDao dao = rc.getJsonObjectDao();
		try {
			return dao.listObjectsByProperties(filters);
		} catch (SQLException | IOException e) {
			return null;
		}
	}
	
	@Override
	@Transactional
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
	@Transactional
	public JSONObject getObjectByProperties(ModelRuntimeContext rc,Map<String, Object> properties){
		JsonObjectDao dao = rc.getJsonObjectDao();
		try {
			return dao.getObjectByProperties(properties);
		} catch (SQLException | IOException e) {
			return null;
		}
	}
	
	
	/**
	 * 
	 * @param rc
	 * @param jo
	 * @param eventType
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	private int runOperationEvent(ModelRuntimeContext rc, Map<String, Object> jo, 
			String eventType , HttpServletResponse response ) throws Exception{
		String eventBeanName = rc.getMetaFormModel().getExtendOptBean();
		if(StringUtils.isBlank(eventBeanName))
				return 0;		
		OperationEvent optEvent = 
	                ContextLoaderListener.getCurrentWebApplicationContext().
	                getBean(eventBeanName,  OperationEvent.class);
		if(optEvent==null)
			return -1;
		switch(eventType){
		case "beforeSave":
			return optEvent.beforeSave(rc, jo, 
					rc.getMetaFormModel().getExtendOptBeanParam(), response);
		case "afterSave":
			return optEvent.afterSave(rc, jo, 
					rc.getMetaFormModel().getExtendOptBeanParam(), response);
		case "beforeUpdate":
			return optEvent.beforeUpdate(rc, jo, 
					rc.getMetaFormModel().getExtendOptBeanParam(), response);
		case "afterUpdate":
			return optEvent.afterUpdate(rc, jo, 
					rc.getMetaFormModel().getExtendOptBeanParam(), response);
		case "beforeDelete":
			return optEvent.beforeDelete(rc, jo, 
					rc.getMetaFormModel().getExtendOptBeanParam(), response);
		case "afterDelete":
			return optEvent.afterDelete(rc, jo, 
					rc.getMetaFormModel().getExtendOptBeanParam(), response);
		case "beforeSubmit":
			return optEvent.beforeSubmit(rc, jo, 
					rc.getMetaFormModel().getExtendOptBeanParam(), response);
		case "afterSubmit":
			return optEvent.afterSubmit(rc, jo, 
					rc.getMetaFormModel().getExtendOptBeanParam(), response);
		default:
			return 0;
		}
	}

	@Override
	@Transactional
	public int saveNewObject(ModelRuntimeContext rc, 
			Map<String, Object> object , HttpServletResponse response) throws Exception {
		int n = runOperationEvent(rc, object, "beforeSave", response);
		if( n<=0 ){
			JsonObjectDao dao = rc.getJsonObjectDao();
			dao.saveNewObject(object);
 			n = runOperationEvent(rc, object, "afterSave", response);
		}
		//rc.commitAndClose();
		return n;
	}

	@Override
	@Transactional
	public int updateObject(ModelRuntimeContext rc, 
			Map<String, Object> object, HttpServletResponse response) throws Exception {
		int n = runOperationEvent(rc, object, "beforeUpdate", response);
		if( n<=0 ){
			JsonObjectDao dao = rc.getJsonObjectDao();		
			dao.updateObject(object);
			n = runOperationEvent(rc, object, "afterUpdate", response);
		}
		//rc.commitAndClose();
		return n;
	}

	@Override
	@Transactional
	public int deleteObjectById(ModelRuntimeContext rc, 
			Map<String, Object> keyValue, HttpServletResponse response) throws Exception {
		int n = runOperationEvent(rc, keyValue, "beforeDelete", response);
		if( n<=0 ){
			JsonObjectDao dao = rc.getJsonObjectDao();
			dao.deleteObjectById(keyValue);
			n = runOperationEvent(rc, keyValue, "afterDelete", response);
		}
		//rc.commitAndClose();
		return n;
	}

}
