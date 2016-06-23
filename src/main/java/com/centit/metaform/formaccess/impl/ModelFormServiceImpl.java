package com.centit.metaform.formaccess.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import com.alibaba.fastjson.JSON;
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
import com.centit.metaform.formaccess.ModelRuntimeContext;
import com.centit.metaform.formaccess.NameValuePair;
import com.centit.metaform.formaccess.OperationEvent;
import com.centit.metaform.po.MetaColumn;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.po.MetaTable;
import com.centit.metaform.po.ModelDataField;
import com.centit.metaform.po.ModelOperation;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.database.DataSourceDescription;
import com.centit.support.database.jsonmaptable.JsonObjectDao;
import com.centit.support.database.metadata.SimpleTableField;

@Service(value="modelFormService")
public class ModelFormServiceImpl implements ModelFormService {

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
	public ModelRuntimeContext createRuntimeContext(String modelCode) {
		
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
	public JdbcModelRuntimeContext createJdbcRuntimeContext(String modelCode) {
		
		JdbcModelRuntimeContext rc = new JdbcModelRuntimeContext(modelCode);		
		
		MetaFormModel mfm = formModelDao.getObjectById(modelCode);
		MetaTable mtab = mfm.getMdTable();

		rc.setTableInfo(mtab);
		rc.setMetaFormModel(mfm);
		
		DatabaseInfo mdb = databaseInfoDao.getObjectById( mtab.getDatabaseCode());		
		DataSourceDescription dbc = new DataSourceDescription();
		dbc.setDatabaseCode(mdb.getDatabaseCode());
		dbc.setConnUrl(mdb.getDatabaseUrl());
		dbc.setUsername(mdb.getUsername());
		dbc.setPassword(mdb.getPassword());		
		rc.setDataSource(dbc);

		return rc;
	}

	@Transactional(readOnly=true)
	public HibernateModelRuntimeContext createHibernateRuntimeContext(String modelCode) {
		
		HibernateModelRuntimeContext rc = new HibernateModelRuntimeContext(modelCode);		
		
		MetaFormModel mfm = formModelDao.getObjectById(modelCode);
		MetaTable mtab = mfm.getMdTable();

		rc.setTableInfo(mtab);
		rc.setMetaFormModel(mfm);		
		rc.setBaseObjectDao(tableDao);
		
		return rc;
	}

	@Override
	@Transactional
	public Map<String, Object> createNewPk(ModelRuntimeContext rc) throws SQLException {
		JSONObject pk = new JSONObject();
		for(String pkCol:rc.getTableInfo().getPkColumns()){
			MetaColumn field = rc.getTableInfo().findFieldByColumn(pkCol);
			String autoCreateRule = field.getAutoCreateRule();
			switch(autoCreateRule){
			case "C":
				pk.put(field.getPropertyName(), field.getAutoCreateParam());
				break;
			case "U":
				pk.put(field.getPropertyName(), UuidOpt.getUuidAsString());
				break;
			case "S":
				try {
					pk.put(field.getPropertyName(),
							rc.getJsonObjectDao().getSequenceNextValue( field.getAutoCreateParam()));
				} catch (IOException e) {
				}
				break;
			default:
				break;
			}
		}
		return pk;
	}

	@Override
	@Transactional
	public JSONObject createInitialObject(ModelRuntimeContext rc) throws SQLException {
		JSONObject object = new JSONObject();
		for(MetaColumn field :rc.getTableInfo().getMdColumns()){			
			String autoCreateRule = field.getAutoCreateRule();
			switch(autoCreateRule){
			case "C":
				object.put(field.getPropertyName(), field.getAutoCreateParam());
				break;
			case "U":
				object.put(field.getPropertyName(), UuidOpt.getUuidAsString());
				break;
			case "S":
				try {
					object.put(field.getPropertyName(),
							rc.getJsonObjectDao().getSequenceNextValue( field.getAutoCreateParam()));
				} catch (IOException e) {
				}
				break;
			default:
				break;
			}
		}
		return object;
	}

	/**
	 * @param operation 取值范围：view,create,edit,list(ListViewDefine)
	 */
	@Override
	@Transactional(readOnly=true)
	public MateFormDefine getFormDefine(ModelRuntimeContext rc,String operation) {
		
		//MetaFormModel mfm = rc.getMetaFormModel();
		//mfm.getMetaFormModels()
		
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

	/**
	 * @param operation 取值范围：view,create,edit,list(ListViewDefine)
	 */
	@Transactional(readOnly=true)
	public MateFormDefine getFormDefineDB(ModelRuntimeContext rc,String operation) {
		MetaFormModel mfm = rc.getMetaFormModel();
		MetaTable tableInfo = rc.getTableInfo();
		MateFormDefine mff = new MateFormDefine(mfm.getModelName());
		mff.setAccessType(mfm.getAccessType()); 
		mff.setExtendOptBean(mfm.getExtendOptBean());
		mff.setExtendOptBeanParam(mfm.getExtendOptBeanParam());
		
		for(ModelDataField field:mfm.getModelDataFields()){
			FormField ff = new FormField();
			MetaColumn mc = tableInfo.findFieldByColumn(field.getColumnName());
			ff.setKey(SimpleTableField.mapPropName(field.getColumnName()));
			ff.setType("input");
			FieldTemplateOptions templateOptions = new FieldTemplateOptions();
			templateOptions.setLabel(mc.getFieldLabelName());
			templateOptions.setPlaceholder(field.getInputHint());
			ff.setValidatorHint(field.getValidateHint());
			if(field.isFocus())
				templateOptions.setFocus(true);
			if(field.isMandatory())
				templateOptions.setRequired(true);
			
			switch(field.getReferenceType()){
			case "1":{//数据字典（列表）
				String sql = "select datacode,datavalue from f_datadictionary where catalogcode=?";
				List<Object[]> datas;
				try {
					datas = rc.getJsonObjectDao().findObjectsBySql(
							sql,
							new Object[]{field.getReferenceData()});
				
					for (Object[] data : datas) {
						templateOptions.addOption(new NameValuePair(
								StringBaseOpt.objectToString(data[1]),
								StringBaseOpt.objectToString(data[0])));
					}
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case "2":{//数据字典（树）
				String sql = "select datacode,datavalue,extracode from f_datadictionary where catalogcode=?";
				List<Object[]> datas;
				try {
					datas = rc.getJsonObjectDao().findObjectsBySql(
							sql,
							new Object[]{field.getReferenceData()});
				
					for (Object[] data : datas) {
						templateOptions.addOption(new NameValuePair(
								StringBaseOpt.objectToString(data[1]),
								StringBaseOpt.objectToString(data[0]),
								StringBaseOpt.objectToString(data[2])));
					}
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case "3"://JSON
				templateOptions.setOptions(
						JSON.parseArray(field.getReferenceData(),NameValuePair.class));
				break;
			case "4":{//SQL语句（列表）
				String sql = field.getReferenceData();
				List<Object[]> datas;
				try {
					datas = rc.getJsonObjectDao().findObjectsBySql(
							sql,null);				
					for (Object[] data : datas) {
						templateOptions.addOption(new NameValuePair(
								StringBaseOpt.objectToString(data[1]),
								StringBaseOpt.objectToString(data[0])));
					}
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case "5":{//SQL语句（树）
				String sql = field.getReferenceData();
				List<Object[]> datas;
				try {
					datas = rc.getJsonObjectDao().findObjectsBySql(
							sql,null);				
					for (Object[] data : datas) {
						templateOptions.addOption(new NameValuePair(
								StringBaseOpt.objectToString(data[1]),
								StringBaseOpt.objectToString(data[0]),
								StringBaseOpt.objectToString(data[2])));
					}
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case "Y":{//年
				int currYear = DatetimeOpt.getYear(new Date());
				for (int i = 5; i > -45; i--) {
					templateOptions.addOption(new NameValuePair(
							String.valueOf(currYear + i) + "年",
							String.valueOf(currYear + i)));
				}			
				break;
			}
			case "M"://月
				for (int i = 1; i < 13; i++) {
					templateOptions.addOption(new NameValuePair(
							String.valueOf(i) + "月",
							String.valueOf(i)));
				}
				break;
			case "F"://文件
				//文件类型特殊属相
				break;
			default:
				break;
			}

			ff.setTemplateOptions(templateOptions);
			mff.addFilter(ff);
		}		
		return mff;
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
	
	@Transactional(readOnly=true)
	public ListViewDefine getListViewModelDB(ModelRuntimeContext rc){
		
		MetaFormModel mfm = rc.getMetaFormModel();
		MetaTable tableInfo = rc.getTableInfo();
		ListViewDefine mff = new ListViewDefine(mfm.getModelName());
		mff.setAccessType(mfm.getAccessType()); 
		mff.setExtendOptBean(mfm.getExtendOptBean());
		mff.setExtendOptBeanParam(mfm.getExtendOptBeanParam());
		
		for(ModelDataField field:mfm.getModelDataFields()){
			if("NO".equals(field.getFilterType()))
				continue;
			FormField ff = new FormField();
			MetaColumn mc = tableInfo.findFieldByColumn(field.getColumnName());
			ff.setKey(SimpleTableField.mapPropName(field.getColumnName()));
			ff.setType("input");
			FieldTemplateOptions templateOptions = new FieldTemplateOptions();
			templateOptions.setLabel(mc.getFieldLabelName());
			templateOptions.setPlaceholder(field.getInputHint());
			ff.setValidatorHint(field.getValidateHint());
			if(field.isFocus())
				templateOptions.setFocus(true);
			if(field.isMandatory())
				templateOptions.setRequired(true);
			
			switch(field.getReferenceType()){
			case "1":{//数据字典（列表）
				String sql = "select datacode,datavalue from f_datadictionary where catalogcode=?";
				List<Object[]> datas;
				try {
					datas = rc.getJsonObjectDao().findObjectsBySql(
							sql,
							new Object[]{field.getReferenceData()});
				
					for (Object[] data : datas) {
						templateOptions.addOption(new NameValuePair(
								StringBaseOpt.objectToString(data[1]),
								StringBaseOpt.objectToString(data[0])));
					}
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case "2":{//数据字典（树）
				String sql = "select datacode,datavalue,extracode from f_datadictionary where catalogcode=?";
				List<Object[]> datas;
				try {
					datas = rc.getJsonObjectDao().findObjectsBySql(
							sql,
							new Object[]{field.getReferenceData()});
				
					for (Object[] data : datas) {
						templateOptions.addOption(new NameValuePair(
								StringBaseOpt.objectToString(data[1]),
								StringBaseOpt.objectToString(data[0]),
								StringBaseOpt.objectToString(data[2])));
					}
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case "3"://JSON
				templateOptions.setOptions(
						JSON.parseArray(field.getReferenceData(),NameValuePair.class));
				break;
			case "4":{//SQL语句（列表）
				String sql = field.getReferenceData();
				List<Object[]> datas;
				try {
					datas = rc.getJsonObjectDao().findObjectsBySql(
							sql,null);				
					for (Object[] data : datas) {
						templateOptions.addOption(new NameValuePair(
								StringBaseOpt.objectToString(data[1]),
								StringBaseOpt.objectToString(data[0])));
					}
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case "5":{//SQL语句（树）
				String sql = field.getReferenceData();
				List<Object[]> datas;
				try {
					datas = rc.getJsonObjectDao().findObjectsBySql(
							sql,null);				
					for (Object[] data : datas) {
						templateOptions.addOption(new NameValuePair(
								StringBaseOpt.objectToString(data[1]),
								StringBaseOpt.objectToString(data[0]),
								StringBaseOpt.objectToString(data[2])));
					}
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case "Y":{//年
				int currYear = DatetimeOpt.getYear(new Date());
				for (int i = 5; i > -45; i--) {
					templateOptions.addOption(new NameValuePair(
							String.valueOf(currYear + i) + "年",
							String.valueOf(currYear + i)));
				}			
				break;
			}
			case "M"://月
				for (int i = 1; i < 13; i++) {
					templateOptions.addOption(new NameValuePair(
							String.valueOf(i) + "月",
							String.valueOf(i)));
				}
				break;
			case "F"://文件
				//文件类型特殊属相
				break;
			default:
				break;
			}

			ff.setTemplateOptions(templateOptions);
			mff.addFilter(ff);
		}
		
		return mff;
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
