package com.centit.metaform.formaccess.impl;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.centit.metaform.formaccess.ModelRuntimeContext;
import com.centit.metaform.formaccess.OperationEvent;

public abstract class AbstractOperationEvent implements OperationEvent {

	/**
	 * 新建 saveNew
	 */
	@Override
	public int beforeSave(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam,
			HttpServletResponse response) throws Exception {
		return 0;
	}

	@Override
	public int afterSave(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam,
			HttpServletResponse response) throws Exception {
		return 0;
	}

	@Override
	public int beforeUpdate(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam,
			HttpServletResponse response) throws Exception {
		return beforeSave(mrc, params,  optJsonStrParam,
				 response);
	}

	@Override
	public int afterUpdate(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam,
			HttpServletResponse response) throws Exception {
		return afterSave(mrc, params,  optJsonStrParam,
				 response);
	}

	@Override
	public int beforeMerge(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam,
			HttpServletResponse response) throws Exception {
		return beforeSave(mrc, params,  optJsonStrParam,
				 response);
	}

	@Override
	public int afterMerge(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam,
			HttpServletResponse response) throws Exception {
		return afterSave(mrc, params,  optJsonStrParam,
				 response);
	}
	
	@Override
	public int beforeDelete(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam,
			HttpServletResponse response) throws Exception {
		return 0;
	}

	@Override
	public int afterDelete(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam,
			HttpServletResponse response) throws Exception {
		return 0;
	}

	@Override
	public int beforeSubmit(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam,
			HttpServletResponse response) throws Exception {
		return beforeSave(mrc, params,  optJsonStrParam,
				 response);
	}

	@Override
	public int afterSubmit(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam,
			HttpServletResponse response) throws Exception {
		return afterSave(mrc, params,  optJsonStrParam,
				 response);
	}

}
