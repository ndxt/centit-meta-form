package com.centit.metaform.formaccess;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.OptionItem;
import com.centit.metaform.po.ModelOperation;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MetaFormDefine {
    private String modelName;
    private String extendOptBean;
    private String extendOptBeanParam;
    private String accessType;
    private String formType;

    private FormFieldGroup fields;
    private List<ModelOperation> operations;


    public MetaFormDefine() {
        fields = new FormFieldGroup();
    }

    public MetaFormDefine(String modelName, String formType) {
        this.modelName = modelName;
        this.formType = formType;
        fields = new FormFieldGroup();
    }

    public MetaFormDefine(List<FormField> fields) {
        this.fields.setFieldGroup(fields);
    }

    public MetaFormDefine(List<FormField> filters, List<ModelOperation> operations) {
        this.fields.setFieldGroup(filters);
        this.operations = operations;
    }

    /**
     * 将只读字段的下拉框改为输入框
     */
    public void updateReadOnlyRefrenceField() {
        if ("list".equals(formType))
            return;
        for (FormField ff : fields.getFieldGroup()) {
            if (ff.getTemplateOptions() != null &&
                (ff.getTemplateOptions().isDisabled()
                    || "view".equals(formType))) {
                List<OptionItem> ops = ff.getTemplateOptions().getOptions();
                //ff.getTemplateOptions().setDisabled(true);
                ff.setType("text");
                if (ops != null) {
                    ff.setKey(ff.getKey() + "Value");
                    //ff.setType("input");
                }
            }
        }
    }

    public JSONObject transObjectRefranceData(JSONObject obj) {
        if (fields == null)
            return obj;
        for (FormField ff : fields.getFieldGroup()) {
            Object v = obj.get(ff.getKey());
            if (v == null)
                continue;
            String readonlyKey = ff.getKey() + "Value";

            if ("multiCheckbox".equals(ff.getType())) {//inputType
                String[] sa = StringUtils.split(
                    StringBaseOpt.objectToString(v), ',');
                obj.put(ff.getKey(), sa);
                v = sa;
            }

            if (StringUtils.isNotBlank(ff.getTemplateOptions().getFormat())
                && ("view".equals(formType) || "text".equals(ff.getType()))) {
                if (v instanceof Date) {
                    obj.put(ff.getKey(), DatetimeOpt.convertDateToString(
                        (Date) v, ff.getTemplateOptions().getFormat()));
                }
            }

            if (ff.getTemplateOptions() != null &&
                (ff.getTemplateOptions().isDisabled()
                    || "view".equals(formType)
                    || "list".equals(formType))) {

                List<OptionItem> ops = ff.getTemplateOptions().getOptions();
                if (ops != null) {
                    if (v instanceof String[]) {
                        String[] sa = (String[]) v;
                        int n = sa.length;
                        if (n > 0) {
                            String[] sv = new String[n];
                            for (int i = 0; i < n; i++) {
                                int p = ops.indexOf(new OptionItem(sa[i]));
                                if (p >= 0)
                                    sv[i] = ops.get(p).getName();
                                else
                                    sv[i] = sa[i];
                            }
                            obj.put(readonlyKey, StringBaseOpt.objectToString(sv));
                        }
                    } else {
                        int p = ops.indexOf(new OptionItem(StringBaseOpt.objectToString(v)));
                        if (p >= 0)
                            obj.put(readonlyKey, ops.get(p).getName());
                        else
                            obj.put(readonlyKey, v);
                    }
                }
            }
        }
        return obj;
    }

    public JSONArray transObjectsRefranceData(JSONArray objs) {
        if (objs == null)
            return null;
        for (Object obj : objs) {
            transObjectRefranceData((JSONObject) obj);
        }
        return objs;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getExtendOptBean() {
        return extendOptBean;
    }

    public void setExtendOptBean(String extendOptBean) {
        this.extendOptBean = extendOptBean;
    }

    public String getExtendOptBeanParam() {
        return extendOptBeanParam;
    }

    public void setExtendOptBeanParam(String extendOptBeanParam) {
        this.extendOptBeanParam = extendOptBeanParam;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public List<ModelOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<ModelOperation> operations) {
        this.operations = operations;
    }

    public void addOperation(ModelOperation operation) {
        if (this.operations == null)
            this.operations = new ArrayList<>();
        this.operations.add(operation);
    }

    public List<FormField> getFields() {
        return fields.getFieldGroup();
    }

    public void setFields(List<FormField> fields) {
        this.fields.setFieldGroup(fields);
        ;
    }

    public void addField(FormField field) {
        this.fields.addField(field);
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

}
