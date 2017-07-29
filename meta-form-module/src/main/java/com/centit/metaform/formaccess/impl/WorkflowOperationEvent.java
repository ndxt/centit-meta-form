package com.centit.metaform.formaccess.impl;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.centit.metaform.formaccess.ModelRuntimeContext;

public class WorkflowOperationEvent extends AbstractOperationEvent {

	/**
	 * 新建 saveNew 
	 * 创建于工作流关联的 流程实例
	 */
	@Override
	public int beforeSave(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam,
			HttpServletResponse response) throws Exception {
		return 0;
	}
	
	/**
	 * 检查流程 状态，判断是否可以提交
	 */
	@Override
	public int beforeSubmit(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam,
			HttpServletResponse response) throws Exception {
		return beforeSave(mrc, params,  optJsonStrParam,
				 response);
	}

	/**
	 * 更改业务状态，并创建后续业务节点
	 */
	@Override
	public int afterSubmit(ModelRuntimeContext mrc, Map<String, Object> params, String optJsonStrParam,
			HttpServletResponse response) throws Exception {
		return afterSave(mrc, params,  optJsonStrParam,
				 response);
	}


}
