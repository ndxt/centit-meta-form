package com.centit.metaform.fromaccess;

import java.util.Map;

public interface OperationEvent {
	public void beforeSave(ModelRuntimeContext mrc, Map<String,Object> params) throws Exception;
	
	public void afterSave(ModelRuntimeContext mrc, Map<String,Object> params) throws Exception;
	
	public void beforeUpdate(ModelRuntimeContext mrc, Map<String,Object> params) throws Exception;
	
	public void afterUpdate(ModelRuntimeContext mrc, Map<String,Object> params) throws Exception;
	
	public void beforeDelete(ModelRuntimeContext mrc, Map<String,Object> params) throws Exception;
	
	public void afterDelete(ModelRuntimeContext mrc, Map<String,Object> params) throws Exception;
}
