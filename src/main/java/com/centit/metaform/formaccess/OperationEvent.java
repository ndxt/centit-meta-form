package com.centit.metaform.formaccess;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
/**
 * 
 * @author codefan
 *
 */
public interface OperationEvent {
	/*** 
	 * @param mrc
	 * @param params
	 * @param optJsonStrParam
	 * @param response
	 * @return  0: 表示仅仅作为事件，不需要特别处理， 
	 * 			1： 表示替代操作无需后续处理 ，
	 * 			2 ：表示已经在response写入返回信息，主函数无需在写
	 * @throws Exception
	 */
	public int beforeSave(ModelRuntimeContext mrc, Map<String,Object> params,
			String optJsonStrParam, HttpServletResponse response) throws Exception;
	
	/*** 
	 * @param mrc
	 * @param params
	 * @param optJsonStrParam
	 * @param response
	 * @return  0: 表示仅仅作为事件，不需要特别处理， 
	 * 			1： 表示替代操作无需后续处理 ，
	 * 			2 ：表示已经在response写入返回信息，主函数无需在写
	 * @throws Exception
	 */
	public int afterSave(ModelRuntimeContext mrc, Map<String,Object> params,
			String optJsonStrParam, HttpServletResponse response) throws Exception;
	/*** 
	 * @param mrc
	 * @param params
	 * @param optJsonStrParam
	 * @param response
	 * @return  0: 表示仅仅作为事件，不需要特别处理， 
	 * 			1： 表示替代操作无需后续处理 ，
	 * 			2 ：表示已经在response写入返回信息，主函数无需在写
	 * @throws Exception
	 */
	public int beforeUpdate(ModelRuntimeContext mrc, Map<String,Object> params
			, String optJsonStrParam, HttpServletResponse response) throws Exception;
	/*** 
	 * @param mrc
	 * @param params
	 * @param optJsonStrParam
	 * @param response
	 * @return  0: 表示仅仅作为事件，不需要特别处理， 
	 * 			1： 表示替代操作无需后续处理 ，
	 * 			2 ：表示已经在response写入返回信息，主函数无需在写
	 * @throws Exception
	 */
	public int afterUpdate(ModelRuntimeContext mrc, Map<String,Object> params,
			String optJsonStrParam, HttpServletResponse response) throws Exception;
	/*** 
	 * @param mrc
	 * @param params
	 * @param optJsonStrParam
	 * @param response
	 * @return  0: 表示仅仅作为事件，不需要特别处理， 
	 * 			1： 表示替代操作无需后续处理 ，
	 * 			2 ：表示已经在response写入返回信息，主函数无需在写
	 * @throws Exception
	 */
	public int beforeDelete(ModelRuntimeContext mrc, Map<String,Object> params,
			String optJsonStrParam, HttpServletResponse response) throws Exception;
	/*** 
	 * @param mrc
	 * @param params
	 * @param optJsonStrParam
	 * @param response
	 * @return  0: 表示仅仅作为事件，不需要特别处理， 
	 * 			1： 表示替代操作无需后续处理 ，
	 * 			2 ：表示已经在response写入返回信息，主函数无需在写
	 * @throws Exception
	 */
	public int afterDelete(ModelRuntimeContext mrc, Map<String,Object> params,
			String optJsonStrParam, HttpServletResponse response) throws Exception;
	/*** 
	 * @param mrc
	 * @param params
	 * @param optJsonStrParam
	 * @param response
	 * @return  0: 表示仅仅作为事件，不需要特别处理， 
	 * 			1： 表示替代操作无需后续处理 ，
	 * 			2 ：表示已经在response写入返回信息，主函数无需在写
	 * @throws Exception
	 */
	public int beforeSubmit(ModelRuntimeContext mrc, Map<String,Object> params,
			String optJsonStrParam, HttpServletResponse response) throws Exception;
	/*** 
	 * @param mrc
	 * @param params
	 * @param optJsonStrParam
	 * @param response
	 * @return  0: 表示仅仅作为事件，不需要特别处理， 
	 * 			1： 表示替代操作无需后续处理 ，
	 * 			2 ：表示已经在response写入返回信息，主函数无需在写
	 * @throws Exception
	 */
	public int afterSubmit(ModelRuntimeContext mrc, Map<String,Object> params,
			String optJsonStrParam, HttpServletResponse response) throws Exception;

}
