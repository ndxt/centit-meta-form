package com.centit.metaform.formaccess;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.metaform.po.ModelOperation;
import com.centit.support.algorithm.StringBaseOpt;

public class MetaFormDefine {
	private String modelName;
	private String extendOptBean;
	private String extendOptBeanParam;
	private String accessType;
	private String formType;
	
	private List<FormField> filters;
	private List<ModelOperation> operations;
	
	
	public MetaFormDefine(){
		
	}
		
	public MetaFormDefine(String modelName,String formType){
		this.modelName = modelName;
		this.formType = formType;
	}

	public MetaFormDefine(List<FormField> filters){
		this.filters = filters;
	}
	
	public MetaFormDefine(List<FormField> filters,List<ModelOperation> operations){
		this.filters = filters;
		this.operations = operations;
	}
	
	public void updateReadOnlyRefrenceField(){
		if("list".equals(formType))
			return;
		for(FormField ff:filters){
			if(ff.getTemplateOptions()!=null &&
					(ff.getTemplateOptions().isDisabled() 
							|| "view".equals(formType))){
				ff.setKey(ff.getKey()+"Value");
				ff.setType("text");
			}
		}
	}	
	
	public JSONObject transObjectRefranceData(JSONObject obj){
		if(filters==null)
			return obj;
		for(FormField ff:filters){
			
			Object v = obj.get(ff.getKey());
			String readonlyKey = ff.getKey()+"Value";
			
			if("multiCheckbox".equals(ff.getType())){//inputType
				String[] sa = StringUtils.split(
						StringBaseOpt.objectToString(v),',');
				obj.put(ff.getKey(),sa);
				v = sa;
			}
			
			if(ff.getTemplateOptions()!=null &&
					(ff.getTemplateOptions().isDisabled() 
							|| "view".equals(formType)
							|| "list".equals(formType))){
				
				List<OptionItem> ops = ff.getTemplateOptions().getOptions();
				if(ops!=null){
					if(v instanceof String[] ){
						String[] sa  = (String[] ) v;
						int n = sa.length;
						if(n>0){
							String[] sv = new String[n];							
							for(int i=0;i<n;i++){
								int p = ops.indexOf(new OptionItem(sa[i]));
								if(p>=0)
									sv[i] = ops.get(p).getValue();
								else
									sv[i] = sa[i];
							}
							obj.put(readonlyKey,sv);
						}
					}else{
						int p = ops.indexOf(new OptionItem(StringBaseOpt.objectToString(v)));
						if(p>=0)
							obj.put(readonlyKey,ops.get(p).getValue());
						else
							obj.put(readonlyKey,v);
					}
				}
				// 文本等没有options的情况
				else {
					obj.put(readonlyKey, StringBaseOpt.objectToString(v));
				}
			}
		}
		return obj;
	}
	
	public JSONArray transObjectsRefranceData(JSONArray objs){
		if(objs==null)
			return null;
		for(Object obj:objs){
			transObjectRefranceData((JSONObject)obj);
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
		if(this.operations == null)
			this.operations = new ArrayList<>();
		this.operations.add(operation);
	}
	
	public List<FormField> getFilters() {
		return filters;
	}

	public void setFilters(List<FormField> filters) {
		this.filters = filters;
	}
	
	public void addFilter(FormField filter) {
		if(this.filters == null)
			this.filters = new ArrayList<>();
		this.filters.add(filter);
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}
	
}
