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
	int beforeSave(ModelRuntimeContext mrc, Map<String,Object> params,
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
	int afterSave(ModelRuntimeContext mrc, Map<String,Object> params,
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
	int beforeUpdate(ModelRuntimeContext mrc, Map<String,Object> params
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
	int afterUpdate(ModelRuntimeContext mrc, Map<String,Object> params,
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
	int beforeMerge(ModelRuntimeContext mrc, Map<String,Object> params
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
	int afterMerge(ModelRuntimeContext mrc, Map<String,Object> params,
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
	int beforeDelete(ModelRuntimeContext mrc, Map<String,Object> params,
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
	int afterDelete(ModelRuntimeContext mrc, Map<String,Object> params,
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
	int beforeSubmit(ModelRuntimeContext mrc, Map<String,Object> params,
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
	int afterSubmit(ModelRuntimeContext mrc, Map<String,Object> params,
			String optJsonStrParam, HttpServletResponse response) throws Exception;

}
