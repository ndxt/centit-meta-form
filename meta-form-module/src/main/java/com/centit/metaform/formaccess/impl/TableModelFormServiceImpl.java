package com.centit.metaform.formaccess.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.OptionItem;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.metaform.dao.*;
import com.centit.metaform.formaccess.*;
import com.centit.metaform.po.*;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.database.jsonmaptable.GeneralJsonObjectDao;
import com.centit.support.database.jsonmaptable.JsonObjectDao;
import com.centit.support.database.metadata.SimpleTableField;
import com.centit.support.database.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoaderListener;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@Service(value = "modelFormService")
public class TableModelFormServiceImpl implements ModelFormService {


    @Resource
    private MetaTableDao tableDao;

    @Resource
    private MetaFormModelDao formModelDao;

    @Resource
    private MetaColumnDao metaColumnDao;

    @Resource
    private MetaRelationDao metaRelationDao;

    @Resource
    private MetaRelDetialDao metaRelDetialDao;

    @Resource
    private ModelDataFieldDao modelDataFieldDao;

    @Resource
    private ModelOperationDao modelOperationDao;

    @Resource
    protected IntegrationEnvironment integrationEnvironment;

    @Value("${metaform.dataaccess.embedded:false}")
    private boolean useLocalDatabase;

    private Map<String, List<OptionItem>> propertyOptionCache;

    @Override
    @Transactional(readOnly = true)
    public ModelRuntimeContext createRuntimeContext(String modelCode) {

        ModelRuntimeContext runtimeContext =
            ModelRuntimeContextPool.getRuntimeContextPool(modelCode);
        if (runtimeContext != null)
            return runtimeContext;

        if (useLocalDatabase)
            runtimeContext = createHostModelRuntimeContext(modelCode);
        else
            runtimeContext = createJdbcRuntimeContext(modelCode);

        ModelRuntimeContextPool.registerRuntimeContextPool(runtimeContext);
        return runtimeContext;
    }

    @Transactional(readOnly = true)
    public JdbcModelRuntimeContext createJdbcRuntimeContext(String modelCode) {

        JdbcModelRuntimeContext rc = new JdbcModelRuntimeContext(modelCode);

        MetaFormModel mfm = formModelDao.getObjectById(modelCode);
//        MetaTable mtab = mfm.getMdTable();
        Long tableId = mfm.getTableId();
        MetaTable mtab = tableDao.getObjectById(tableId);

        Map<String, Object> tempFilter = new HashMap<>();
        tempFilter.put("tableId", tableId);
        Set<MetaColumn> tempColumn = new HashSet<>(metaColumnDao.listObjectsByProperties(tempFilter));
        mtab.setMdColumns(tempColumn);

        Set<ModelDataField> modelDataFields =
            new HashSet<>(modelDataFieldDao.listObjectsByProperty("modelCode", modelCode));
        Set<ModelOperation> modelOperations =
            new HashSet<>(modelOperationDao.listObjectsByProperty("modelCode", modelCode));
        mfm.setModelDataFields(modelDataFields);
        mfm.setModelOperations(modelOperations);

        rc.setTableInfo(mtab);
        rc.setMetaFormModel(mfm);

        DatabaseInfo mdb = integrationEnvironment.getDatabaseInfo(mtab.getDatabaseCode());
        //databaseInfoDao.getObjectById( mtab.getDatabaseCode());

        DataSourceDescription dbc = new DataSourceDescription();
        dbc.setDatabaseCode(mdb.getDatabaseCode());
        dbc.setConnUrl(mdb.getDatabaseUrl());
        dbc.setUsername(mdb.getUsername());
        dbc.setPassword(mdb.getClearPassword());
        rc.setDataSource(dbc);

        return rc;
    }

    @Transactional(readOnly = true)
    public HostModelRuntimeContext createHostModelRuntimeContext(String modelCode) {

        HostModelRuntimeContext rc = new HostModelRuntimeContext(modelCode);

        MetaFormModel mfm = formModelDao.getObjectById(modelCode);
//        MetaTable mtab = mfm.getMdTable();
        Long tableId = mfm.getTableId();
        MetaTable mtab = tableDao.getObjectById(tableId);

        rc.setTableInfo(mtab);
        rc.setMetaFormModel(mfm);
        rc.setBaseObjectDao(tableDao);

        return rc;
    }

    @Override
    @Transactional
    public Map<String, Object> createNewPk(ModelRuntimeContext rc) throws SQLException {
        JSONObject pk = new JSONObject();
        for (String pkCol : rc.getTableInfo().getPkColumns()) {
            MetaColumn field = rc.getTableInfo().findFieldByColumn(pkCol);
            String autoCreateRule = field.getAutoCreateRule();
            if (null == autoCreateRule) continue;

            switch (autoCreateRule) {
                case "C":
                    pk.put(field.getPropertyName(), field.getAutoCreateParam());
                    break;
                case "U":
                    pk.put(field.getPropertyName(), UuidOpt.getUuidAsString());
                    break;
                case "S":
                    try {
                        pk.put(field.getPropertyName(),
                            rc.getJsonObjectDao().getSequenceNextValue(field.getAutoCreateParam()));
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
    public Map<String, Object> getModelReferenceFields(ModelRuntimeContext rc, JSONObject object) throws SQLException {
        JSONObject refData = new JSONObject();
        for (ModelDataField field : rc.getMetaFormModel().getModelDataFields()) {
            if ("R".equals(field.getColumnType())) {
                if ("3".equals(field.getReferenceType())) {
                    refData.put(SimpleTableField.mapPropName(field.getColumnName()),
                        field.getReferenceData());
                } else {
                    String sql = field.getReferenceData();
                    QueryAndNamedParams qap = rc.translateSQL(sql, object);
                    Object value = null;
                    try {
                        value = DatabaseAccess.fetchScalarObject(
                            rc.getJsonObjectDao().findObjectsByNamedSql(
                                qap.getQuery(),
                                qap.getParams()));
                    } catch (SQLException | IOException e) {
                        e.printStackTrace();
                    }

                    refData.put(SimpleTableField.mapPropName(field.getColumnName()),
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
        for (MetaColumn field : rc.getTableInfo().getMdColumns()) {
            String autoCreateRule = field.getAutoCreateRule();
            if (null == autoCreateRule) continue;

            switch (autoCreateRule) {
                case "C":
                    object.put(field.getPropertyName(), field.getAutoCreateParam());
                    break;
                case "U":
                    object.put(field.getPropertyName(), UuidOpt.getUuidAsString());
                    break;
                case "S":
                    try {
                        object.put(field.getPropertyName(),
                            rc.getJsonObjectDao().getSequenceNextValue(field.getAutoCreateParam()));
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
                                                        ModelDataField field, String propertyName) {
        if (field.getReferenceType() == null || "0".equals(field.getReferenceType()))
            return null;

        if (propertyOptionCache == null) {
            propertyOptionCache = new HashMap<>();
        } else {
            List<OptionItem> options = propertyOptionCache.get(propertyName);
            if (options != null) {
                return options;
            }
        }

        List<OptionItem> options = null;

        switch (field.getReferenceType()) {
            case "1": {//数据字典（列表）
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
            case "2": {//数据字典（树）
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
                    JSON.parseArray(field.getReferenceData(), OptionItem.class);
                break;
            case "4": {//SQL语句（列表）
                String sql = field.getReferenceData();
                List<Object[]> datas;
                try {
                    datas = rc.getJsonObjectDao().findObjectsBySql(
                        sql, null);
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
            case "5": {//SQL语句（树）
                String sql = field.getReferenceData();
                List<Object[]> datas;
                try {
                    datas = rc.getJsonObjectDao().findObjectsBySql(
                        sql, null);
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

            case "Y": {//年
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
        if (options != null)
            propertyOptionCache.put(propertyName, options);
        return options;
    }

    protected void referenceDataToOption(ModelRuntimeContext rc,
                                         ModelDataField field, String propertyName, FieldTemplateOptions templateOptions) {
        if (field.getReferenceType() == null)
            return;
        List<OptionItem> options = getReferenceDataToOption(rc, field, propertyName);
        if (options != null)
            templateOptions.setOptions(options);
    }

    /**
     * @param operation 取值范围：view,create,edit,list(ListViewDefine)
     */
    @Override
    @Transactional(readOnly = true)
    public MetaFormDefine createFormDefine(ModelRuntimeContext rc, String operation) {
        MetaFormModel mfm = rc.getMetaFormModel();
        MetaTable tableInfo = rc.getTableInfo();
        MetaFormDefine mff = new MetaFormDefine(mfm.getModelName(), operation);
        mff.setAccessType(mfm.getAccessType());
        mff.setExtendOptBean(mfm.getExtendOptBean());
        mff.setExtendOptBeanParam(mfm.getExtendOptBeanParam());

        List<ModelDataField> fieldList = new ArrayList<>(mfm.getModelDataFields());
        sortFieldListByDisplayOrder(fieldList);

        for (ModelDataField field : fieldList) {
            //字段类型是 隐藏
            if ("H".equals(field.getAccessType()))
                continue;
            //查看视图；查询时隐藏
            if ("viewlist".equals(operation) && "HI".equals(field.getFilterType()))
                continue;
            FormField ff = new FormField();
            MetaColumn mc = tableInfo.findFieldByColumn(field.getColumnName());
            ff.setKey(SimpleTableField.mapPropName(field.getColumnName()));

            ff.setType(StringUtils.isBlank(field.getInputType()) ? "input" : field.getInputType());
            FieldTemplateOptions templateOptions = new FieldTemplateOptions();
            templateOptions.setLabel(mc.getFieldLabelName());
            templateOptions.setPlaceholder(field.getInputHint());
            if (StringUtils.isNoneBlank(field.getValidateHint()))
                ff.setValidatorHint(field.getValidateHint());
            if ("T".equals(field.getFocus()) || "Y".equals(field.getFocus()) || "1".equals(field.getFocus()))
                templateOptions.setFocus(true);
            if ("T".equals(field.getMandatory()) || "Y".equals(field.getMandatory()) || "1".equals(field.getMandatory()))
                templateOptions.setRequired(true);
            if (StringUtils.isNotBlank(field.getViewFormat()))
                templateOptions.setFormat(field.getViewFormat());
            if (field.getFieldWidth() != null)
                ff.setFieldWidth(field.getFieldWidth().intValue());

            if ("view".equals(operation) || "viewlist".equals(operation) ||
                ("R".equals(field.getColumnType()) ||
                    "R".equals(field.getAccessType()) ||
                    ("C".equals(field.getAccessType()) && !"create".equals(operation)))) {
                //READONLY
                templateOptions.setDisabled(true);
                //ff.setNoFormControl(true);
                //ff.setTemplate("<p>Some text here</p>");
            }

            referenceDataToOption(rc,
                field, mc.getPropertyName(), templateOptions);

            ff.setTemplateOptions(templateOptions);
            mff.addField(ff);
        }
        for (ModelOperation mo : mfm.getModelOperations())
            mff.addOperation(mo);
        return mff;
    }

    /**
     * 将模块字段根据[显示顺序]排序
     * @param fieldList
     */
    public void sortFieldListByDisplayOrder(List<ModelDataField> fieldList) {
        Collections.sort(fieldList, (o1, o2) -> {
                Long order1 = (o1.getDisplayOrder() == null) ? 0 : o1.getDisplayOrder();
                Long order2 = (o2.getDisplayOrder() == null) ? 0 : o2.getDisplayOrder();
                return order1 > order2 ? 1 : order1 == order2 ? 0 : -1;
        });
    }

    /**
     * 将模块操作根据[显示顺序]排序
     * @param modelOperations
     */
    public void sortModelOperationByDisplayOrder(List<ModelOperation> modelOperations) {
        Collections.sort(modelOperations, (o1, o2) -> {
            Long order1 = (o1.getDisplayOrder() == null) ? 0 : o1.getDisplayOrder();
            Long order2 = (o2.getDisplayOrder() == null) ? 0 : o2.getDisplayOrder();
            return order1 > order2 ? 1 : order1 == order2 ? 0 : -1;
        });
    }


    @Override
    @Transactional(readOnly = true)
    public List<OptionItem> getAsyncReferenceData(ModelRuntimeContext rc,
                                                  String propertyName, String startGroup) {

        ModelDataField mdf = rc.getMetaFormModel().findFieldByName(propertyName);
        if (mdf == null || !"A".equals(mdf.getReferenceType()))
            return null;
        List<OptionItem> options = new ArrayList<OptionItem>();
        try {
            JsonObjectDao dao = rc.getJsonObjectDao();

            QueryAndNamedParams qap = QueryUtils.translateQuery(mdf.getReferenceData(),
                QueryUtils.createSqlParamsMap("sg", startGroup));

            List<Object[]> objss = dao.findObjectsByNamedSql(qap.getQuery(), qap.getParams());
            for (Object[] objs : objss) {
                options.add(new OptionItem(
                    StringBaseOpt.objectToString(objs[1]),
                    StringBaseOpt.objectToString(objs[0]),
                    StringBaseOpt.objectToString(objs[2])));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rc.close();
        }
        return options;
    }

    @Override
    @Transactional(readOnly = true)
    public ListViewDefine createListViewModel(ModelRuntimeContext rc) {

        MetaFormModel mfm = rc.getMetaFormModel();
        MetaTable tableInfo = rc.getTableInfo();
        ListViewDefine mff = new ListViewDefine(mfm.getModelName());
        mff.setAccessType(mfm.getAccessType());
        mff.setExtendOptBean(mfm.getExtendOptBean());
        mff.setExtendOptBeanParam(mfm.getExtendOptBeanParam());

        List<ModelDataField> fieldList = new ArrayList<>(mfm.getModelDataFields());
        sortFieldListByDisplayOrder(fieldList);

        for (ModelDataField field : fieldList) {
            MetaColumn mc = tableInfo.findFieldByColumn(field.getColumnName());
            if (!"H".equals(field.getAccessType()) && !"HI".equals(field.getFilterType())) {

                char rt = StringUtils.isNotBlank(mc.getReferenceType()) ?
                    mc.getReferenceType().charAt(0) : '0';
                String fieldName;
                if (rt > '0' && rt <= '9') {
                    fieldName = SimpleTableField.mapPropName(field.getColumnName()) + "Value";
                } else {
                    fieldName = SimpleTableField.mapPropName(field.getColumnName());
                }

                ListColumn col = new ListColumn(
                    fieldName,
                    mc.getFieldLabelName());
                if (mc.isPrimaryKey()) {
                    col.setPrimaryKey(true);
                    mff.addPrimaryKey(fieldName);
                }
                //if("H".equals(field.getAccessType()))
                //col.setShow(false);
                mff.addColumn(col);

                if (StringUtils.isBlank(field.getFilterType()) || "NO".equals(field.getFilterType()))
                    continue;
                FormField ff = new FormField();

                ff.setKey(SimpleTableField.mapPropName(field.getColumnName()));
                ff.setType(StringUtils.isBlank(field.getInputType()) ? "input" : field.getInputType());
                FieldTemplateOptions templateOptions = new FieldTemplateOptions();
                templateOptions.setLabel(mc.getFieldLabelName());
                templateOptions.setPlaceholder(field.getInputHint());

                if (StringUtils.isNotBlank(field.getViewFormat()))
                    templateOptions.setFormat(field.getViewFormat());

                referenceDataToOption(rc,
                    field, mc.getPropertyName(), templateOptions);
                ff.setTemplateOptions(templateOptions);
                if (field.getFieldWidth() != null)
                    ff.setFieldWidth(field.getFieldWidth().intValue());

                if ("BT".equals(field.getFilterType())) {
                    //ff.setKey(SimpleTableField.mapPropName("l_"+field.getColumnName()));
                    ff.getTemplateOptions().setLabel(templateOptions.getLabel() + " 从");
                    mff.addField(ff);
                    FormField ffu = new FormField();
                    BeanUtils.copyProperties(ff, ffu);
                    ffu.getTemplateOptions().setLabel("到");
                    ffu.setKey(SimpleTableField.mapPropName("top_" + field.getColumnName()));
                    mff.addField(ffu);
                } else
                    mff.addField(ff);
            }
        }

        List<ModelOperation> modelOperations = new ArrayList<>(mfm.getModelOperations());
        sortModelOperationByDisplayOrder(modelOperations);

        for (ModelOperation mo : modelOperations)
            mff.addOperation(mo);

        return mff;
    }
    /*--------------------------------------------------------------------------------------------
     */

    public static String buildFilterSql(ModelRuntimeContext rc, String alias,
                                        Map<String, Object> filters) {
        MetaTable ti = rc.getTableInfo();
        StringBuilder sBuilder = new StringBuilder();
        int i = 0;
        MetaFormModel mfm = rc.getMetaFormModel();

        for (ModelDataField field : mfm.getModelDataFields()) {
            MetaColumn col = ti.findFieldByColumn(field.getColumnName());
            Object paramValue = filters.get(col.getPropertyName());
            if (paramValue != null) {
                if (i > 0)
                    sBuilder.append(" and ");

                if (field.getFilterType() == null) {
                    field.setFilterType("");
                }
                switch (field.getFilterType()) {
                    case "MC":
                        if (StringUtils.isNotBlank(alias))
                            sBuilder.append(alias).append('.');
                        sBuilder.append(col.getColumnName()).append(" like :").append(col.getPropertyName());
                        filters.put(col.getPropertyName(), QueryUtils.getMatchString(
                            StringBaseOpt.objectToString(paramValue)));
                        break;
                    case "LT":
                        if (StringUtils.isNotBlank(alias))
                            sBuilder.append(alias).append('.');
                        sBuilder.append(col.getColumnName()).append(" < :").append(col.getPropertyName());

                        filters.put(col.getPropertyName(), rc.castValueToFieldType(col,
                            paramValue));
                        break;
                    case "GT":
                        if (StringUtils.isNotBlank(alias))
                            sBuilder.append(alias).append('.');
                        sBuilder.append(col.getColumnName()).append(" > :").append(col.getPropertyName());
                        filters.put(col.getPropertyName(), rc.castValueToFieldType(col,
                            paramValue));
                        break;
                /*case "BT":
                    break;*/
                    case "LE":
                        if (StringUtils.isNotBlank(alias))
                            sBuilder.append(alias).append('.');
                        sBuilder.append(col.getColumnName()).append(" <= :").append(col.getPropertyName());
                        filters.put(col.getPropertyName(), rc.castValueToFieldType(col,
                            paramValue));
                        break;
                    case "GE":
                    case "BT":
                        if (StringUtils.isNotBlank(alias))
                            sBuilder.append(alias).append('.');
                        sBuilder.append(col.getColumnName()).append(" >= :").append(col.getPropertyName());
                        filters.put(col.getPropertyName(), rc.castValueToFieldType(col,
                            paramValue));
                        break;

                    default:
                        if (StringUtils.isNotBlank(alias))
                            sBuilder.append(alias).append('.');
                        sBuilder.append(col.getColumnName()).append(" = :").append(col.getPropertyName());
                        filters.put(col.getPropertyName(), rc.castValueToFieldType(col,
                            paramValue));
                        break;
                }
                i++;
            }
            if ("BT".equals(field.getFilterType())) {
                String skey = SimpleTableField.mapPropName("top_" + field.getColumnName());
                if (filters.get(skey) != null) {
                    if (i > 0)
                        sBuilder.append(" and ");
                    if (StringUtils.isNotBlank(alias))
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

    /**
     * 根据传递的Map构造查询条件
     *
     * @param filters
     * @return
     */
    public static String buildSimpleFilterSql(String alias, Map<String, Object> filters) {
        StringBuilder sBuilder = new StringBuilder();
        int i = 0;

        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            if (entry.getValue() == null || StringUtils.isBlank(entry.getKey())) {
                continue;
            }

            if (i > 0)
                sBuilder.append(" and ");

            if (StringUtils.isNotBlank(alias))
                sBuilder.append(alias).append('.');

            sBuilder.append(entry.getKey()).append(" = '").append(entry.getValue()).append("'");
            i++;
        }
        return sBuilder.toString();
    }

    @Override
    @Transactional
    public JSONArray listObjectsByFilter(ModelRuntimeContext rc, Map<String, Object> requestFilters) {
        Map<String, Object> filters = makeTabulationFilter(rc, requestFilters);

        try {
            JsonObjectDao dao = rc.getJsonObjectDao();
            Pair<String, String[]> q = GeneralJsonObjectDao.buildFieldSqlWithFieldName(rc.getTableInfo(), null);
            String sql = "select " + q.getLeft() + " from " + rc.getTableInfo().getTableName();

            QueryAndNamedParams qap = rc.getMetaFormFilter();
            String filter = buildFilterSql(rc, null, filters);
            if (qap != null) {
                sql = sql + " where (" + qap.getQuery() + ")";
                if (StringUtils.isNotBlank(filter))
                    sql = sql + " and " + filter;
                filters.putAll(qap.getParams());
            } else if (StringUtils.isNotBlank(filter))
                sql = sql + " where " + filter;

            if (StringUtils.isNotBlank(filter))
                sql = sql + " where " + filter;
            return rc.castTableObjectListToObjectList(
                dao.findObjectsByNamedSqlAsJSON(
                    sql,
                    filters,
                    q.getRight())
            );
        } catch (SQLException | IOException e) {
            return null;
        }
    }

    //    @Transactional
    private Map<String, Object> makeTabulationFilter(ModelRuntimeContext rc, Map<String, Object> filters) {
        if (StringUtils.isBlank(rc.getMetaFormModel().getRelationType()) || "0".equals(rc.getMetaFormModel().getRelationType()))
            return filters;
//        MetaFormModel parentModel = rc.getMetaFormModel().getParentModel();
        MetaFormModel parentModel = formModelDao.getObjectById(rc.getMetaFormModel().getParentModelCode());
        if (parentModel == null)
            return filters;
        List<Pair<String, String>> pMap = formModelDao.getSubModelPropertiesMap(parentModel.getTableId(),
            rc.getMetaFormModel().getTableId());
        if (pMap == null)
            return filters;
        Map<String, Object> newFilters = new HashMap<>(filters);
        for (Pair<String, String> p : pMap) {
            Object v = filters.get(p.getLeft());
            if (v != null)
                newFilters.put(p.getRight(), v);
        }
        return newFilters;
    }

    @Override
    public List<MetaFormModel> listSubModel(String modelCode) {
        return formModelDao.listObjectsByProperty("parentModelCode", modelCode);
    }

    /**
     * 根据父模块code获取子模块code
     */
    @Override
    public JSONArray listSubModelCode(String modelCode) {
        List<MetaFormModel> subModels = formModelDao.listObjectsByProperty("parentModelCode", modelCode);

        JSONArray result = new JSONArray();
        if (subModels == null || subModels.size() == 0) {
            return result;
        }

        for (MetaFormModel subModel : subModels) {
            result.add(subModel.getModelCode());
        }
        return result;
    }

    @Override
    @Transactional
    public JSONArray listSubModelObjectsByFilter(ModelRuntimeContext rc, Map<String, Object> requestFilters, PageDesc pageDesc) {

        Map<String, Object> filters = makeTabulationFilter(rc, requestFilters);

        try {
            JsonObjectDao dao = rc.getJsonObjectDao();
            Pair<String, String[]> q = GeneralJsonObjectDao.buildFieldSqlWithFieldName(rc.getTableInfo(), null);
            String sql = "select " + q.getLeft() + " from " + rc.getTableInfo().getTableName();
            QueryAndNamedParams qap = rc.getMetaFormFilter();
            String filter = buildFilterSql(rc, null, filters);
            String whereSql = "";
            if (qap != null) {
                whereSql = " where (" + qap.getQuery() + ")";
                if (StringUtils.isNotBlank(filter))
                    whereSql = whereSql + " and " + filter;
                filters.putAll(qap.getParams());
            } else if (StringUtils.isNotBlank(filter))
                whereSql = " where " + filter;

            JSONArray ja = dao.findObjectsByNamedSqlAsJSON(sql + whereSql, filters, q.getRight(),
                (pageDesc.getPageNo() - 1) > 0 ? (pageDesc.getPageNo() - 1) * pageDesc.getPageSize() : 0,
                pageDesc.getPageSize());

            sql = "select count(1) as rs from " +
                rc.getTableInfo().getTableName() + whereSql;

            List<Object[]> objList = dao.findObjectsByNamedSql(sql, filters);
            Long ts = NumberBaseOpt.castObjectToLong(
                DatabaseAccess.fetchScalarObject(objList));
            if (ts != null)
                pageDesc.setTotalRows(ts.intValue());
            else
                pageDesc.setTotalRows(ja.size());
            return rc.castTableObjectListToObjectList(ja);
        } catch (SQLException | IOException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public JSONArray listSubModelObjectsByFilter(ModelRuntimeContext rc, Map<String, Object> requestFilters) {
        //前台传递的父模块字段
        Map<String, Object> pModelParam = makeTabulationFilter(rc, requestFilters);
        //构造的过滤器
        Map<String, Object> filters = new HashMap<>();

        Long tTableId = rc.getTableInfo().getTableId();
        MetaFormModel parentModel = formModelDao.getObjectById(rc.getMetaFormModel().getParentModelCode());

        Map<String, Object> relSearchColumn = new HashMap<>();
        relSearchColumn.put("childTableId", tTableId);
        relSearchColumn.put("parentTableId", parentModel.getTableId());
        MetaRelation tRelations = metaRelationDao.getObjectByProperties(relSearchColumn);
        if (tRelations == null || tRelations.getRelationId() == null) {
            return null;
        }
        List<MetaRelDetail> tRelationDetails = metaRelDetialDao.listObjectsByProperty("relationId", tRelations.getRelationId());
        if (tRelationDetails == null || tRelationDetails.size() == 0
            || pModelParam == null || pModelParam.size() == 0) {
            return null;
        }

        for (MetaRelDetail tRelationDetail : tRelationDetails) {
            String parentColumnName = tRelationDetail.getParentColumnName();

            String parentColumnNameKey = "";
            parentColumnName.split("_");
            if (parentColumnName.contains("_")) {
                String[] tempNames = parentColumnName.split("_");
                for (int tns = 0; tns < tempNames.length; tns++) {
                    String tempName = tempNames[tns];
                    if (tns == 0) {
                        parentColumnNameKey += tempName.toLowerCase();
                    } else {
                        parentColumnNameKey += tempName.toUpperCase().charAt(0);
                        parentColumnNameKey += tempName.toLowerCase().substring(1);
                    }
                }
            } else {
                parentColumnNameKey += parentColumnName;
            }

            if (pModelParam.containsKey(parentColumnNameKey)) {
                filters.put(tRelationDetail.getChildColumnName(), ((String[]) pModelParam.get(parentColumnNameKey))[0]);
            }
        }

        if (filters == null || filters.size() == 0) {
            return null;
        }

        try {
            JsonObjectDao dao = rc.getJsonObjectDao();
            Pair<String, String[]> q = GeneralJsonObjectDao.buildFieldSqlWithFieldName(rc.getTableInfo(), null);
            String sql = "select " + q.getLeft() + " from " + rc.getTableInfo().getTableName();

            String filter = buildSimpleFilterSql(null, filters);

            if (StringUtils.isNotBlank(filter))
                sql = sql + " where " + filter;
            return rc.castTableObjectListToObjectList(
                dao.findObjectsAsJSON(
                    sql, null, null)
            );
        } catch (SQLException | IOException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public JSONArray listObjectsByFilter(ModelRuntimeContext rc, Map<String, Object> requestFilters, PageDesc pageDesc) {

        Map<String, Object> filters = makeTabulationFilter(rc, requestFilters);

        try {
            JsonObjectDao dao = rc.getJsonObjectDao();
            Pair<String, String[]> q = GeneralJsonObjectDao.buildFieldSqlWithFieldName(rc.getTableInfo(), null);
            String sql = "select " + q.getLeft() + " from " + rc.getTableInfo().getTableName();
            QueryAndNamedParams qap = rc.getMetaFormFilter();
            String filter = buildFilterSql(rc, null, filters);
            String whereSql = "";
            if (qap != null) {
                whereSql = " where (" + qap.getQuery() + ")";
                if (StringUtils.isNotBlank(filter))
                    whereSql = whereSql + " and " + filter;
                filters.putAll(qap.getParams());
            } else if (StringUtils.isNotBlank(filter))
                whereSql = " where " + filter;

            JSONArray ja = dao.findObjectsByNamedSqlAsJSON(sql + whereSql, filters, q.getRight(),
                (pageDesc.getPageNo() - 1) > 0 ? (pageDesc.getPageNo() - 1) * pageDesc.getPageSize() : 0,
                pageDesc.getPageSize());

            sql = "select count(1) as rs from " +
                rc.getTableInfo().getTableName() + whereSql;

            List<Object[]> objList = dao.findObjectsByNamedSql(sql, filters);
            Long ts = NumberBaseOpt.castObjectToLong(
                DatabaseAccess.fetchScalarObject(objList));
            if (ts != null)
                pageDesc.setTotalRows(ts.intValue());
            else
                pageDesc.setTotalRows(ja.size());
            return rc.castTableObjectListToObjectList(ja);
        } catch (SQLException | IOException e) {
            return null;
        }
    }


    @Override
    @Transactional
    public JSONObject getObjectByProperties(ModelRuntimeContext rc, Map<String, Object> properties) {
        try {
            JsonObjectDao dao = rc.getJsonObjectDao();
            return rc.castTableObjectToObject(
                dao.getObjectByProperties(properties));
        } catch (SQLException | IOException e) {
            return null;
        }
    }


    /**
     * @param rc
     * @param jo
     * @param eventType
     * @param response
     * @return
     * @throws Exception
     */
    private int runOperationEvent(ModelRuntimeContext rc, Map<String, Object> jo,
                                  String eventType, HttpServletResponse response) throws Exception {

        String eventBeanName = rc.getMetaFormModel().getExtendOptBean();
        //没有设置时间bean直接返回
        if (StringUtils.isBlank(eventBeanName))
            return 0;
        //获取时间bean
        OperationEvent optEvent =
            ContextLoaderListener.getCurrentWebApplicationContext().
                getBean(eventBeanName, OperationEvent.class);
        if (optEvent == null)
            return -1;
        switch (eventType) {
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
                             Map<String, Object> object, HttpServletResponse response) throws Exception {
        int n = runOperationEvent(rc, object, "beforeSave", response);
        if (n <= 0) {
            JsonObjectDao dao = rc.getJsonObjectDao();
            dao.saveNewObject(rc.castObjectToTableObject(object));
            n = runOperationEvent(rc, object, "afterSave", response);
        }
        //rc.commitAndClose();
        return n;
    }

    private static Map<String, Object> mergeTwoObject(
        Collection<String> fields, Map<String, Object> oldObject, Map<String, Object> newObject) {

        if (newObject == null || fields == null)
            return oldObject;

        if (oldObject == null) {
            oldObject = new HashMap<>(fields.size());
        }

        for (String f : fields) {
            Object obj = newObject.get(f);
            if (obj == null) {
                oldObject.remove(f);
            } else {
                oldObject.put(f, obj);
            }
        }
        return oldObject;
    }

    private static Map<String, Object> mergeTwoObject(
        Map<String, Object> oldObject, Map<String, Object> newObject) {
        if (newObject == null)
            return oldObject;

        if (oldObject == null) {
            return newObject;
        }
        oldObject.putAll(newObject);
        return oldObject;
    }

    private int updateModelObject(ModelRuntimeContext rc, Map<String, Object> object) throws SQLException, IOException {
        JsonObjectDao dao = rc.getJsonObjectDao();
        //这个地方要判断 ，数据的存储方式，如果 大字段则要自己 merge
        List<String> fields = rc.getMetaFormField();
        if ("C".equalsIgnoreCase(rc.getTableInfo().getTableType())) {
            Map<String, Object> oldObject = dao.getObjectById(object);
            if (fields != null)
                oldObject = mergeTwoObject(oldObject, object);
            else
                oldObject = mergeTwoObject(fields, oldObject, object);

            return dao.updateObject(rc.castObjectToTableObject(oldObject));
        } else {
            if (fields != null)
                return dao.updateObject(fields, rc.castObjectToTableObject(object));
            else
                return dao.updateObject(rc.castObjectToTableObject(object));
        }
    }

    @Override
    @Transactional
    public int updateObject(ModelRuntimeContext rc,
                            Map<String, Object> object, HttpServletResponse response) throws Exception {
        int n = runOperationEvent(rc, object, "beforeUpdate", response);
        if (n <= 0) {
            updateModelObject(rc, object);
            n = runOperationEvent(rc, object, "afterUpdate", response);
        }
        //rc.commitAndClose();
        return n;
    }

    private int mergeModelObject(ModelRuntimeContext rc, Map<String, Object> object) throws SQLException, IOException {
        JsonObjectDao dao = rc.getJsonObjectDao();
        //这个地方要判断 ，数据的存储方式，如果 大字段则要自己 merge
        Map<String, Object> oldObject = dao.getObjectById(object);
        if (oldObject == null)
            return dao.saveNewObject(rc.castObjectToTableObject(object));

        return updateModelObject(rc, object);
    }

    @Override
    @Transactional
    public int mergeObject(ModelRuntimeContext rc,
                           Map<String, Object> object, HttpServletResponse response) throws Exception {
        int n = runOperationEvent(rc, object, "beforeMerge", response);
        if (n <= 0) {
            mergeModelObject(rc, object);
            n = runOperationEvent(rc, object, "afterMerge", response);
        }
        //rc.commitAndClose();
        return n;
    }

    @Override
    @Transactional
    public int submitObject(ModelRuntimeContext rc,
                            Map<String, Object> object, HttpServletResponse response) throws Exception {
        int n = runOperationEvent(rc, object, "beforeSubmit", response);
        if (n <= 0) {
            updateModelObject(rc, object);
            n = runOperationEvent(rc, object, "afterSubmit", response);
        }
        //rc.commitAndClose();
        return n;
    }

    @Override
    @Transactional
    public int deleteObjectById(ModelRuntimeContext rc,
                                Map<String, Object> keyValue, HttpServletResponse response) throws Exception {
        int n = runOperationEvent(rc, keyValue, "beforeDelete", response);
        if (n <= 0) {
            JsonObjectDao dao = rc.getJsonObjectDao();
            dao.deleteObjectById(rc.castObjectToTableObject(keyValue));
            n = runOperationEvent(rc, keyValue, "afterDelete", response);
        }
        //rc.commitAndClose();
        return n;
    }

    @Override
    @Transactional
    public JSONObject getObjectById(ModelRuntimeContext rc, Map<String, Object> keyValue) {
        try {
            JsonObjectDao dao = rc.getJsonObjectDao();
            return rc.castTableObjectToObject(
                dao.getObjectById(keyValue));
        } catch (SQLException | IOException e) {
            return null;
        }
    }
}
