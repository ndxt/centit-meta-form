package com.centit.metaform.formaccess.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoaderListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.dao.DatabaseInfoDao;
import com.centit.dde.po.DatabaseInfo;
import com.centit.framework.common.OptionItem;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.dao.PageDesc;
import com.centit.metaform.dao.MetaFormModelDao;
import com.centit.metaform.dao.MetaTableDao;
import com.centit.metaform.formaccess.FieldTemplateOptions;
import com.centit.metaform.formaccess.FormField;
import com.centit.metaform.formaccess.ListColumn;
import com.centit.metaform.formaccess.ListViewDefine;
import com.centit.metaform.formaccess.MetaFormDefine;
import com.centit.metaform.formaccess.ModelFormService;
import com.centit.metaform.formaccess.ModelRuntimeContext;
import com.centit.metaform.formaccess.OperationEvent;
import com.centit.metaform.po.MetaColumn;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.po.MetaTable;
import com.centit.metaform.po.ModelDataField;
import com.centit.metaform.po.ModelOperation;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.database.DataSourceDescription;
import com.centit.support.database.DatabaseAccess;
import com.centit.support.database.QueryAndNamedParams;
import com.centit.support.database.QueryUtils;
import com.centit.support.database.jsonmaptable.GeneralJsonObjectDao;
import com.centit.support.database.jsonmaptable.JsonObjectDao;
import com.centit.support.database.metadata.SimpleTableField;

@Service(value="modelFormService")
public class ModelFormServiceImpl implements ModelFormService {

    @Resource
    private DatabaseInfoDao databaseInfoDao;

    @Resource
    private MetaTableDao tableDao;

    @Resource
    private MetaFormModelDao formModelDao;
     
    @Value("${metaform.dataaccess.embedded}")
    private boolean useLocalDatabase;
   
    private Map<String,List<OptionItem>> propertyOptionCache;
    
	@Override
	public ModelRuntimeContext createRuntimeContext(String modelCode) {
		if(useLocalDatabase)
			return createHibernateRuntimeContext(modelCode);
		
		return createJdbcRuntimeContext(modelCode);
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
		dbc.setPassword(mdb.getClearPassword());		
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
			if (null == autoCreateRule) continue;
			
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
	public Map<String,Object> getModelReferenceFields(ModelRuntimeContext rc, JSONObject object) throws SQLException{
		JSONObject refData = new JSONObject();
		for(ModelDataField field :rc.getMetaFormModel().getModelDataFields()){
			if("R".equals(field.getColumnType())){
				if("3".equals(field.getReferenceType())){
					refData.put( SimpleTableField.mapPropName(field.getColumnName()),
							field.getReferenceData());
				}else{
					String sql = field.getReferenceData();
					QueryAndNamedParams qap = rc.translateSQL(sql, object);
					Object value=null;
					try {
						value = DatabaseAccess.fetchScalarObject(
								rc.getJsonObjectDao().findObjectsByNamedSql(
										qap.getQuery(),
										qap.getParams()));
					} catch (SQLException | IOException e) {
						e.printStackTrace();
					}
				
				refData.put( SimpleTableField.mapPropName(field.getColumnName()),
						value);
				}
			}
		}
		return refData;
	}

	@Override
	@Transactional
	public JSONObject createInitialObject(ModelRuntimeContext rc) throws SQLException {
		JSONObject object = new JSONObject();
		for(MetaColumn field :rc.getTableInfo().getMdColumns()){			
			String autoCreateRule = field.getAutoCreateRule();
			if (null == autoCreateRule) continue;
			
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
	
	protected List<OptionItem> getReferenceDataToOption(ModelRuntimeContext rc,
			ModelDataField field,String propertyName){
		if(field.getReferenceType()==null || "0".equals(field.getReferenceType()))
			return null;
		
		if(propertyOptionCache==null){
			propertyOptionCache = new HashMap<>();
		}else{
			List<OptionItem> options = propertyOptionCache.get(propertyName);
			if(options!=null){				
				return options;
			}
		}
		
		List<OptionItem> options = null;
				
		switch(field.getReferenceType()){
		case "1":{//数据字典（列表）
			String sql = "select datacode,datavalue from f_datadictionary where catalogcode=?";
			List<Object[]> datas;
			
			try {
				datas = rc.getJsonObjectDao().findObjectsBySql(
						sql,
						new Object[]{field.getReferenceData()});
				options = new ArrayList<>();
				for (Object[] data : datas) {
					options.add(new OptionItem(
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
				options = new ArrayList<>();
				for (Object[] data : datas) {
					options.add(new OptionItem(
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
			options = 
					JSON.parseArray(field.getReferenceData(),OptionItem.class);
			break;
		case "4":{//SQL语句（列表）
			String sql = field.getReferenceData();
			List<Object[]> datas;
			try {
				datas = rc.getJsonObjectDao().findObjectsBySql(
						sql,null);
				options = new ArrayList<>();
				for (Object[] data : datas) {
					options.add(new OptionItem(
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
				options = new ArrayList<>();
				for (Object[] data : datas) {
					options.add(new OptionItem(
							StringBaseOpt.objectToString(data[1]),
							StringBaseOpt.objectToString(data[0]),
							StringBaseOpt.objectToString(data[2])));
				}
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
			break;
		}
		case "9"://C :框架内置字典（用户、机构、角色等等）
			options = CodeRepositoryUtil.getOptionForSelect(field.getReferenceData());
			break;
			
		case "Y":{//年
			int currYear = DatetimeOpt.getYear(new Date());
			options = new ArrayList<>();
			for (int i = 5; i > -45; i--) {
				options.add(new OptionItem(
						String.valueOf(currYear + i) + "年",
						String.valueOf(currYear + i)));
			}			
			break;
		}
		case "M"://月
			options = new ArrayList<>();
			for (int i = 1; i < 13; i++) {
				options.add(new OptionItem(
						String.valueOf(i) + "月",
						String.valueOf(i)));
			}
			break;
		
		//case "F"://文件
			//文件类型特殊属相
			//break;
		//default:
			//break;
		}
		if(options!=null)
			propertyOptionCache.put(propertyName,options);
		return options;
	}
	
	protected void referenceDataToOption(ModelRuntimeContext rc,
			ModelDataField field,String propertyName,FieldTemplateOptions templateOptions){
		if(field.getReferenceType()==null)
			return;		
		List<OptionItem> options = getReferenceDataToOption( rc, field, propertyName);
		if(options!=null)
			templateOptions.setOptions(options);
	}

	/**
	 * @param operation 取值范围：view,create,edit,list(ListViewDefine)
	 */
	@Override
	@Transactional(readOnly=true)
	public MetaFormDefine createFormDefine(ModelRuntimeContext rc,String operation) {
		MetaFormModel mfm = rc.getMetaFormModel();
		MetaTable tableInfo = rc.getTableInfo();
		MetaFormDefine mff = new MetaFormDefine(mfm.getModelName(),operation);
		mff.setAccessType(mfm.getAccessType()); 
		mff.setExtendOptBean(mfm.getExtendOptBean());
		mff.setExtendOptBeanParam(mfm.getExtendOptBeanParam());
		
		for(ModelDataField field:mfm.getModelDataFields()){
			if("H".equals(field.getAccessType()))
				continue;
			if("viewlist".equals(operation) && "HI".equals(field.getFilterType()) )
				continue;
			FormField ff = new FormField();
			MetaColumn mc = tableInfo.findFieldByColumn(field.getColumnName());
			ff.setKey(SimpleTableField.mapPropName(field.getColumnName()));
			
			ff.setType(StringUtils.isBlank(field.getInputType())?"input":field.getInputType());
			FieldTemplateOptions templateOptions = new FieldTemplateOptions();
			templateOptions.setLabel(mc.getFieldLabelName());
			templateOptions.setPlaceholder(field.getInputHint());
			if(StringUtils.isNoneBlank(field.getValidateHint()))
				ff.setValidatorHint(field.getValidateHint());
			if(field.isFocus())
				templateOptions.setFocus(true);
			if(field.isMandatory())
				templateOptions.setRequired(true);
			if(StringUtils.isNotBlank(field.getViewFormat()))
				templateOptions.setFormat(field.getViewFormat());
			if(field.getFieldWidth()!=null)
				ff.setFieldWidth(field.getFieldWidth().intValue());
			
			if("view".equals(operation) || "viewlist".equals(operation) ||
					("R".equals(field.getColumnType()) ||
					"R".equals(field.getAccessType())||
					("C".equals(field.getAccessType()) && !"create".equals(operation) ) ) ){
				//READONLY
				templateOptions.setDisabled(true);
				//ff.setNoFormControl(true);
				//ff.setTemplate("<p>Some text here</p>");
			}
			
			referenceDataToOption( rc,
					 field, mc.getPropertyName(),templateOptions);
			
			ff.setTemplateOptions(templateOptions);
			mff.addField(ff);
		}
		for(ModelOperation mo :mfm.getModelOperations())	
			mff.addOperation(mo);
		return mff;
	}

	
	@Override
	@Transactional(readOnly=true)
	public List<OptionItem> getAsyncReferenceData(ModelRuntimeContext rc,
			String propertyName,String startGroup){
		
		ModelDataField mdf = rc.getMetaFormModel().findFieldByName(propertyName);
		if(mdf==null || !"A".equals(mdf.getReferenceType()))
			return null;
		JsonObjectDao dao = rc.getJsonObjectDao();
		
		QueryAndNamedParams qap = QueryUtils.translateQuery(mdf.getReferenceData(),
				QueryUtils.createSqlParamsMap("sg",startGroup));
		List<OptionItem> options = new ArrayList<OptionItem>();
		try {
			List<Object[]> objss = dao.findObjectsByNamedSql(qap.getQuery(), qap.getParams());
			for(Object[] objs:objss){
				options.add(new OptionItem(
						StringBaseOpt.objectToString(objs[1]),
						StringBaseOpt.objectToString(objs[0]),
						StringBaseOpt.objectToString(objs[2])));
			}
		} catch (Exception e) {			
			e.printStackTrace();
		} finally{
			rc.close();
		}		
		return options;
	}	
	@Override
	@Transactional(readOnly=true)
	public ListViewDefine createListViewModel(ModelRuntimeContext rc){
		
		MetaFormModel mfm = rc.getMetaFormModel();
		MetaTable tableInfo = rc.getTableInfo();
		ListViewDefine mff = new ListViewDefine(mfm.getModelName());
		mff.setAccessType(mfm.getAccessType()); 
		mff.setExtendOptBean(mfm.getExtendOptBean());
		mff.setExtendOptBeanParam(mfm.getExtendOptBeanParam());
		for(MetaColumn c: tableInfo.getColumns()){
			if(c.isPrimaryKey()){
				mff.addPrimaryKey(c.getPropertyName());
			}
		}
	
		for(ModelDataField field:mfm.getModelDataFields()){
			MetaColumn mc = tableInfo.findFieldByColumn(field.getColumnName());
			if(!"H".equals(field.getAccessType()) && !"HI".equals(field.getFilterType())){
				
				char rt =StringUtils.isNotBlank(mc.getReferenceType())?
						mc.getReferenceType().charAt(0):'0';
				String fieldName;
				if(rt>'0' && rt<='9'){
					fieldName = SimpleTableField.mapPropName(field.getColumnName())+"Value";
				}else{			
					fieldName = SimpleTableField.mapPropName(field.getColumnName());
				}
				
				ListColumn col = new ListColumn(
						fieldName,
						mc.getFieldLabelName());
				if(mc.isPrimaryKey())
					col.setPrimaryKey(true);
				//if("H".equals(field.getAccessType()))
					//col.setShow(false);
				mff.addColumn(col);			
			
				if(StringUtils.isBlank(field.getFilterType()) || "NO".equals(field.getFilterType()))
					continue;
				FormField ff = new FormField();
							
				ff.setKey(SimpleTableField.mapPropName(field.getColumnName()));
				ff.setType(StringUtils.isBlank(field.getInputType())?"input":field.getInputType());
				FieldTemplateOptions templateOptions = new FieldTemplateOptions();
				templateOptions.setLabel(mc.getFieldLabelName());
				templateOptions.setPlaceholder(field.getInputHint());
				
				if(StringUtils.isNotBlank(field.getViewFormat()))
					templateOptions.setFormat(field.getViewFormat());
				
				referenceDataToOption( rc,
						 field, mc.getPropertyName(),templateOptions);
				ff.setTemplateOptions(templateOptions);
				if(field.getFieldWidth()!=null)
					ff.setFieldWidth(field.getFieldWidth().intValue());
				
				if("BT".equals(field.getFilterType())){
					//ff.setKey(SimpleTableField.mapPropName("l_"+field.getColumnName()));			
					ff.getTemplateOptions().setLabel(templateOptions.getLabel()+" 从" );				
					mff.addField(ff);
					FormField ffu = new FormField();
					BeanUtils.copyProperties(ff, ffu);
					ffu.getTemplateOptions().setLabel("到" );
					ffu.setKey(SimpleTableField.mapPropName("top_"+field.getColumnName()));
					mff.addField(ffu);
				}else	
					mff.addField(ff);
			}
		}
		for(ModelOperation mo :mfm.getModelOperations())	
			mff.addOperation(mo);

		return mff;
	}
/*--------------------------------------------------------------------------------------------
*/
	
	public static String buildFilterSql(ModelRuntimeContext rc,String alias,
			Map<String, Object> filters){
		MetaTable ti = rc.getTableInfo();		
		StringBuilder sBuilder= new StringBuilder();
		int i=0;		
		MetaFormModel mfm = rc.getMetaFormModel();	
		
		for(ModelDataField field:mfm.getModelDataFields()){
			MetaColumn col = ti.findFieldByColumn(field.getColumnName());
			Object paramValue = filters.get( col.getPropertyName());
			if(paramValue !=null){
				if(i>0)
					sBuilder.append(" and ");
	
				switch(field.getFilterType()){
				case "MC":
					if(StringUtils.isNotBlank(alias))
						sBuilder.append(alias).append('.');
					sBuilder.append(col.getColumnName()).append(" like :").append(col.getPropertyName());
					filters.put(col.getPropertyName(), QueryUtils.getMatchString(
							StringBaseOpt.objectToString(paramValue)));
					break;
				case "LT":
					if(StringUtils.isNotBlank(alias))
						sBuilder.append(alias).append('.');
					sBuilder.append(col.getColumnName()).append(" < :").append(col.getPropertyName());
					
					filters.put(col.getPropertyName(), rc.castValueToFieldType(col,
							paramValue));					 
					break;
				case "GT":
					if(StringUtils.isNotBlank(alias))
						sBuilder.append(alias).append('.');
					sBuilder.append(col.getColumnName()).append(" > :").append(col.getPropertyName());
					filters.put(col.getPropertyName(), rc.castValueToFieldType(col,
							paramValue));
					break;
				/*case "BT":
					break;*/
				case "LE":
					if(StringUtils.isNotBlank(alias))
						sBuilder.append(alias).append('.');
					sBuilder.append(col.getColumnName()).append(" <= :").append(col.getPropertyName());
					filters.put(col.getPropertyName(), rc.castValueToFieldType(col,
							paramValue));
					break;
				case "GE":
				case "BT":
					if(StringUtils.isNotBlank(alias))
						sBuilder.append(alias).append('.');
					sBuilder.append(col.getColumnName()).append(" >= :").append(col.getPropertyName());
					filters.put(col.getPropertyName(), rc.castValueToFieldType(col,
							paramValue));
					break;
				
				default:
					if(StringUtils.isNotBlank(alias))
						sBuilder.append(alias).append('.');
					sBuilder.append(col.getColumnName()).append(" = :").append(col.getPropertyName());
					filters.put(col.getPropertyName(), rc.castValueToFieldType(col,
							paramValue));
					break;
				}
				i++;
			}
			if("BT".equals(field.getFilterType())){
				String skey = SimpleTableField.mapPropName("top_"+field.getColumnName());				
				if(filters.get(skey) !=null){
					if(i>0)
						sBuilder.append(" and ");
					if(StringUtils.isNotBlank(alias))
						sBuilder.append(alias).append('.');
					sBuilder.append(col.getColumnName()).append(" < :").append(skey);
					filters.put(skey, rc.castValueToFieldType(col,
							paramValue));
					i++;
				}
			}
				
		}
		return sBuilder.toString();
	}
	
	@Override
	@Transactional
	public JSONArray listObjectsByFilter(ModelRuntimeContext rc, Map<String, Object> requestFilters) {
		Map<String, Object> filters = makeTabulationFilter(rc, requestFilters);
		
		JsonObjectDao dao = rc.getJsonObjectDao();
		try {			
			Pair<String,String[]> q = GeneralJsonObjectDao.buildFieldSql(rc.getTableInfo(),null);
			String sql = "select " + q.getLeft() +" from " +rc.getTableInfo().getTableName();
		
			QueryAndNamedParams qap = rc.getMetaFormFilter();
			String filter = buildFilterSql(rc,null,filters);
			if(qap!=null){
				sql = sql + " where (" + qap.getQuery()+")";
				if(StringUtils.isNotBlank(filter))
					sql = sql + " and " + filter;
				filters.putAll(qap.getParams());
			}else if(StringUtils.isNotBlank(filter))
				sql = sql + " where " + filter;
			
			if(StringUtils.isNotBlank(filter))
				sql = sql + " where " + filter;	
			return dao.findObjectsByNamedSqlAsJSON(
					 sql,
					 filters,
					 q.getRight());
		} catch (SQLException | IOException e) {
			return null;
		}
	}
	
	@Transactional
	private Map<String, Object> makeTabulationFilter(ModelRuntimeContext rc, Map<String, Object> filters){
		if(StringUtils.isBlank(rc.getMetaFormModel().getRelationType()) || "0".equals(rc.getMetaFormModel().getRelationType()))
				return filters;
		MetaFormModel parentModel = rc.getMetaFormModel().getParentModel();
		if(parentModel==null)
			return filters;
		List<Pair<String,String>> pMap = formModelDao.getSubModelPropertiesMap(parentModel.getTableId(),
				rc.getMetaFormModel().getTableId());
		if(pMap==null)
			return filters;
		Map<String, Object> newFilters = new HashMap<>(filters);
		for(Pair<String,String> p : pMap){
			Object v = filters.get(p.getLeft());
			if(v !=null)
				newFilters.put(p.getRight(), v);
		}
		return newFilters;
	}
	
	@Override
	@Transactional
	public JSONArray listObjectsByFilter(ModelRuntimeContext rc, Map<String, Object> requestFilters, PageDesc pageDesc) {
		
		Map<String, Object> filters = makeTabulationFilter(rc, requestFilters);
		
		JsonObjectDao dao = rc.getJsonObjectDao();
		try {
			Pair<String,String[]> q = GeneralJsonObjectDao.buildFieldSql(rc.getTableInfo(),null);
			String sql = "select " + q.getLeft() +" from " +rc.getTableInfo().getTableName();
			QueryAndNamedParams qap = rc.getMetaFormFilter();
			String filter = buildFilterSql(rc,null,filters);
			String whereSql="";
			if(qap!=null){
				whereSql = " where (" + qap.getQuery()+")";
				if(StringUtils.isNotBlank(filter))
					whereSql = whereSql + " and " + filter;
				filters.putAll(qap.getParams());
			}else if(StringUtils.isNotBlank(filter))
				whereSql = " where " + filter;

			JSONArray ja = dao.findObjectsByNamedSqlAsJSON(sql + whereSql,filters,q.getRight(),
						(pageDesc.getPageNo()-1)>0? (pageDesc.getPageNo()-1)*pageDesc.getPageSize():0,
						pageDesc.getPageSize());
			
			sql = "select count(1) as rs from " +
					rc.getTableInfo().getTableName() + whereSql;
			
			List<Object[]> objList = dao.findObjectsByNamedSql(sql,filters);
			Long ts = NumberBaseOpt.castObjectToLong(
					DatabaseAccess.fetchScalarObject(objList));			
			if(ts!=null)
				pageDesc.setTotalRows(ts.intValue());
			else
				pageDesc.setTotalRows(ja.size());
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
		case "beforeMerge":
			return optEvent.beforeMerge(rc, jo, 
					rc.getMetaFormModel().getExtendOptBeanParam(), response);
		case "afterMerge":
			return optEvent.afterMerge(rc, jo, 
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
			dao.saveNewObject(rc.caseObjectTableObject(object));
 			n = runOperationEvent(rc, object, "afterSave", response);
		}
		//rc.commitAndClose();
		return n;
	}

	@Override
	@Transactional
	public int mergeObject(ModelRuntimeContext rc, 
			Map<String, Object> object, HttpServletResponse response) throws Exception {
		int n = runOperationEvent(rc, object, "beforeMerge", response);
		if( n<=0 ){
			JsonObjectDao dao = rc.getJsonObjectDao();		
			dao.mergeObject(rc.caseObjectTableObject(object));
			n = runOperationEvent(rc, object, "afterMerge", response);
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
			dao.updateObject(rc.caseObjectTableObject(object));
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
			dao.deleteObjectById(rc.caseObjectTableObject(keyValue));
			n = runOperationEvent(rc, keyValue, "afterDelete", response);
		}
		//rc.commitAndClose();
		return n;
	}

}
