package com.centit.metaform.formaccess.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.metaform.formaccess.FieldType;
import com.centit.metaform.formaccess.ModelRuntimeContext;
import com.centit.metaform.po.MetaColumn;
import com.centit.metaform.po.MetaFormModel;
import com.centit.metaform.po.MetaTable;
import com.centit.metaform.po.ModelDataField;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.StringRegularOpt;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryUtils;
import com.centit.support.database.utils.QueryUtils.SimpleFilterTranslater;
import com.centit.support.database.metadata.SimpleTableField;
import com.centit.support.database.metadata.SimpleTableInfo;
import com.centit.support.database.metadata.TableField;
import com.centit.support.database.metadata.TableInfo;
import com.centit.support.xml.XMLObject;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public abstract class AbstractModelRuntimeContext implements ModelRuntimeContext{
    private String  modelCode;
    private MetaTable tableInfo;
    private MetaFormModel metaFormModel;
    private Map<String,Object> userEvniData;

    public AbstractModelRuntimeContext(){

    }

    public AbstractModelRuntimeContext(String  modelCode){
        this.modelCode = modelCode;
    }
    @Override
    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    @Override
    public MetaTable getTableInfo() {
        return tableInfo;
    }

    public void setTableInfo(MetaTable tableinfo) {
        this.tableInfo = tableinfo;
    }

    @Override
    public MetaFormModel getMetaFormModel() {
        return metaFormModel;
    }

    public void setMetaFormModel(MetaFormModel metaFormModel) {
        this.metaFormModel = metaFormModel;
    }

    public TableInfo getPersistenceTableInfo() {
        if("C".equals(tableInfo.getTableType())) {
            SimpleTableInfo sti = new SimpleTableInfo();

            sti.setSchema( tableInfo.getSchema());
            sti.setTableName( tableInfo.getTableName());
            sti.setTableLabelName(tableInfo.getTableLabelName());
            sti.setTableComment(tableInfo.getTableComment());
            sti.setPkName(tableInfo.getPkName());
            List<SimpleTableField> columns = new ArrayList<>();
            for(MetaColumn column : tableInfo.getColumns()){
                if(column.isPrimaryKey()){
                    SimpleTableField pc = new SimpleTableField();
                    pc.setPropertyName( column.getPropertyName());
                    pc.setFieldLabelName(column.getFieldLabelName());
                    pc.setJavaType(column.getJavaType());
                    pc.setColumnType(column.getColumnType());
                    pc.setColumnName(column.getColumnName());
                    pc.setColumnComment(column.getColumnComment());
                    pc.setDefaultValue(column.getDefaultValue());
                    pc.setMandatory(column.isMandatory());
                    pc.setMaxLength(column.getMaxLength());
                    pc.setPrecision(column.getPrecision());
                    pc.setScale(column.getScale());
                    columns.add(pc);
                }
            }
            SimpleTableField pc = new SimpleTableField();
            pc.setPropertyName( SimpleTableField.mapPropName(
                                tableInfo.getExtColumnName()));
            pc.setFieldLabelName("object");
            pc.setJavaType(FieldType.TEXT);
            pc.setColumnType("CLOB");
            pc.setColumnName( tableInfo.getExtColumnName());
            pc.setColumnComment("存储对象的大字段");

            columns.add(pc);

            sti.setColumns(columns);

            sti.setPkColumns(tableInfo.getPkColumns());
            sti.setReferences(null);
        }
        return tableInfo;
    }

    @Override
    public List<String> getMetaFormField(){
        Set<ModelDataField> fields = metaFormModel.getModelDataFields();

        if(fields==null){
            List<String>  propertyName = new ArrayList<>(fields.size()+1);
            for(ModelDataField field : fields){
                propertyName.add(
                        tableInfo.findFieldByColumn(
                                field.getColumnName()).getPropertyName());
            }
            return propertyName;
        }
        return null;
    }

    @Override
    public Map<String,Object> fetchPkFromRequest(HttpServletRequest request){
        Map<String,Object> jo = new HashMap<>();
        for(String pk: getTableInfo().getPkColumns()){
            TableField pkp = getTableInfo().findFieldByColumn(pk);
            Object pv = request.getParameter(pkp.getPropertyName());
            if(pv==null)
                continue;
            pv = castValueToFieldType(pkp,pv);
            jo.put(pkp.getPropertyName(),pv);
        }
        return jo;
    }

    @Override
    public Map<String,Object> fetchObjectFromRequest( HttpServletRequest request){
        Map<String,Object> jo = new HashMap<>();
        for(TableField field: getTableInfo().getColumns()){
            Object pv = request.getParameter(field.getPropertyName());
            if(pv==null)
                continue;
            pv = castValueToFieldType(field,pv);
            jo.put(field.getPropertyName(),pv);
        }
        return jo;
    }

    @Override
    public  Object castValueToFieldType(TableField field,Object fieldValue){
        switch(field.getJavaType()){
        case FieldType.DATE:
        case FieldType.DATETIME:
            return DatetimeOpt.castObjectToDate(fieldValue);
        case FieldType.INTEGER:
            return NumberBaseOpt.castObjectToLong(fieldValue);
        case FieldType.FLOAT:
            return NumberBaseOpt.castObjectToDouble(fieldValue);
        case FieldType.BOOLEAN:
            return StringRegularOpt.isTrue(
                            StringBaseOpt.objectToString(fieldValue))?"T":"F";
        default:
            return StringBaseOpt.objectToString(fieldValue);
        }
    }

    /**
     * 需要添加 属性到 lob字段的转换
     * @param object
     * @return
     */
    @Override
    public Map<String,Object> castObjectToTableObject(Map<String,Object> object){
        if(object==null)
            return null;
        Map<String,Object> jo = new HashMap<>(object.size()*2);
        Map<String,Object> jpo = new HashMap<>(object.size()*2);

        for(MetaColumn field: getTableInfo().getColumns()){
            Object pv = object.get(field.getPropertyName());
            if(pv==null)
                continue;
            pv = castValueToFieldType(field,pv);

            if("C".equals(tableInfo.getTableType())
                    && field.isPrimaryKey()) {
                jpo.put(field.getPropertyName(), pv);
            } else {
                jo.put(field.getPropertyName(), pv);
            }

        }
        if("C".equals(tableInfo.getTableType())) {
            if("XML".equalsIgnoreCase(getTableInfo().getExtColumnFormat())){
                jpo.put(SimpleTableField.mapPropName(
                        tableInfo.getExtColumnName()),
                        XMLObject.jsonObjectToXMLString(jo)
                );
            }else {
                jpo.put(SimpleTableField.mapPropName(
                        tableInfo.getExtColumnName()),
                        JSON.toJSONString(jo)
                );
            }
            return jpo;
        }

        return jo;
    }

    /**
     * 需要添加 lob字段 到 对象的转换
     * @param object
     * @return
     */
    @Override
    public JSONObject castTableObjectToObject(JSONObject object){
        if(object==null)
            return object;
        String lobFieldColumn = SimpleTableField.mapPropName(
                tableInfo.getExtColumnName());
        if("C".equals(tableInfo.getTableType())) {
            String objStr = String.valueOf(object.get(lobFieldColumn));
            object.remove(lobFieldColumn);
            if("XML".equalsIgnoreCase(getTableInfo().getExtColumnFormat())){
                Map<String, Object> jo = XMLObject.xmlStringToJSONObject(objStr);
                object.putAll(jo);
            }else {
                JSONObject jo = JSON.parseObject(objStr);
                object.putAll(jo);
            }
        }
        return object;
    }

    @Override
    public JSONArray castTableObjectListToObjectList(JSONArray jsonArray){
        if(jsonArray==null)
            return jsonArray;
        if("C".equals(tableInfo.getTableType())) {
            JSONArray nja = new JSONArray(jsonArray.size());
            for( Object jo : jsonArray) {
                nja.add( castTableObjectToObject ((JSONObject)jo));
            }
            return nja;
        }
        return jsonArray;
    }

    @Override
    public void setCurrentUserDetails(
            CentitUserDetails userDetails) {
        if(userDetails==null)
            return;
        if(userEvniData==null)
            userEvniData = new HashMap<>();

        userEvniData.put("currentUser", userDetails);
        //当前用户主机构信息
        userEvniData.put("primaryUnit", CodeRepositoryUtil
                .getUnitInfoByCode(userDetails.getUserInfo().getPrimaryUnit()));
        //当前用户所有机构关联关系信息
        userEvniData.put("userUnits",
                CodeRepositoryUtil.getUserUnits(userDetails.getUserCode()));
        //当前用户的角色信息
        userEvniData.put("userRoles", userDetails.getUserRoles());
    }

    @Override
    public void setUserEvniData(String key,Object value) {
        if(userEvniData==null)
            userEvniData = new HashMap<>();

        userEvniData.put(key, value);
    }

    @Override
    public QueryAndNamedParams getMetaFormFilter(){
        if(metaFormModel==null)
            return null;
        if(StringUtils.isBlank(metaFormModel.getDataFilterSql()))
            return null;

        return QueryUtils.translateQueryFilter(
                metaFormModel.getDataFilterSql(),
                new SimpleFilterTranslater(userEvniData));
    }

    @Override
    public QueryAndNamedParams translateSQL(String sql,Object obj){
        setUserEvniData("obj",obj);
        return QueryUtils.translateQueryFilter(
                sql,
                new SimpleFilterTranslater(userEvniData));
    }
}
