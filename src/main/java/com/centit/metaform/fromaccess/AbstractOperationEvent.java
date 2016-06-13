package com.centit.metaform.fromaccess;

import java.util.Map;

public abstract class AbstractOperationEvent implements OperationEvent {

	@Override
	public void beforeSave(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam)
			throws Exception {
	}

	@Override
	public void afterSave(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam)
			throws Exception {
	}

	@Override
	public void beforeUpdate(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam)
			throws Exception {
	}

	@Override
	public void afterUpdate(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam)
			throws Exception {
	}

	@Override
	public void beforeDelete(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam)
			throws Exception {
	}

	@Override
	public void afterDelete(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam)
			throws Exception {
	}

	@Override
	public void beforeSubmit(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam)
			throws Exception {
	}

	@Override
	public void afterSubmit(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam)
			throws Exception {
	}

}
