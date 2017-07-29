package com.centit.metaform.formaccess.impl;

import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.JsonObjectWork;
import com.centit.support.database.jsonmaptable.JsonObjectDao;

public class HibernateModelRuntimeContext extends AbstractModelRuntimeContext {


	public void setBaseObjectDao(BaseDaoImpl<?, ?> baseObjectDao) {
		this.baseObjectDao = baseObjectDao;
	}

	private BaseDaoImpl<?, ?> baseObjectDao;
	
	public HibernateModelRuntimeContext(){
		
	}
	
	public HibernateModelRuntimeContext(String  modelCode){
		 super(modelCode);
	}
	

	
	private JsonObjectDao dao = null;
	
	public JsonObjectDao getJsonObjectDao(){
		if(dao==null){
			dao = new JsonObjectWork(baseObjectDao,getPersistenceTableInfo());
		}
		return dao;
	}
	
	public void close(){
		
	}
	
	public void commitAndClose(){
		
	}
	
	public void rollbackAndClose(){
		
	}	
	
}
