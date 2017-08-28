package com.centit.metaform.formaccess;

import java.util.HashMap;
import java.util.Map;

import com.centit.support.database.utils.DBType;

public abstract class FieldType {
	public static final String STRING = "string";
	public static final String INTEGER= "integer";
	public static final String FLOAT= "float";
	public static final String BOOLEAN= "boolean";
	public static final String DATE= "date";
	public static final String DATETIME= "datetime";
	public static final String TEXT= "text";
	public static final String FILE = "file";
	
	/**
	 * 转换到Oracle的字段
	 * @param ft String
	 * @return String
	 */
	public static String mapToOracleColumnType(String ft){
		switch(ft){
		case STRING:
			return "varchar2";
	  	case INTEGER:
	  		return "number";
	  	case FLOAT:
	  		return "number";
	  	case BOOLEAN:
	  		return "varchar2(1)";
	  	case DATE:
	  	case DATETIME:
	  		return "Date";
	  	case TEXT:
	  		return "clob";//长文本
	  	case FILE:
	  		return "varchar2(64)";//默认记录文件的ID号
	  	default:
	  		return ft;
	  }
	}

	/**
	 * 转换到Oracle的字段
	 * @param ft String
	 * @return String
	 */
	public static String mapToSqlServerColumnType(String ft){
		switch(ft){
		case STRING:
			return "varchar";
	  	case INTEGER:
	  		return "decimal";
	  	case FLOAT:
	  		return "decimal";
	  	case BOOLEAN:
	  		return "varchar(1)";
	  	case DATE:
	  	case DATETIME:
	  		return "datetime";
	  	case TEXT:
	  		return "text";//长文本
	  	case FILE:
	  		return "varchar(64)";//默认记录文件的ID号
	  	default:
	  		return ft;
	  }
	}
	
	/**
	 * 转换到Oracle的字段
	 * @param ft String
	 * @return String
	 */
	public static String mapToDB2ColumnType(String ft){
		switch(ft){
		case STRING:
			return "varchar";
	  	case INTEGER:
	  		return "INTEGER";
	  	case FLOAT:
	  		return "DECIMAL";
	  	case BOOLEAN:
	  		return "varchar(1)";
	  	case DATE:
	  	case DATETIME:
	  		return "Date";
	  	case TEXT:
	  		return "clob(52428800)";//长文本
	  	case FILE:
	  		return "varchar(64)";//默认记录文件的ID号
	  	default:
	  		return ft;
	  }
	}
	
	/**
	 * 转换到Oracle的字段
	 * @param ft String
	 * @return String
	 */
	public static String mapToMySqlColumnType(String ft){
		switch(ft){
		case STRING:
			return "varchar";
	  	case INTEGER:
	  		return "INT";
	  	case FLOAT:
	  		return "DECIMAL";
	  	case BOOLEAN:
	  		return "varchar(1)";
	  	case DATE:
	  		return "Date";
	  	case DATETIME:
	  		return "DATETIME";
	  	case TEXT:
	  		return "clob";//长文本
	  	case FILE:
	  		return "varchar(64)";//默认记录文件的ID号
	  	default:
	  		return ft;
	  }
	}
	/**
	 * 
	 * @param dt 数据库类别
	 * @param ft 字段类别
	 * @return
	 */
	public static String mapToDBColumnType(DBType dt,String ft){
		if(dt==null)
			return ft;
		switch(dt){
		case SqlServer:
			return mapToSqlServerColumnType(ft);
	  	case Oracle:
	  		return mapToOracleColumnType(ft);
	  	case DB2:
	  		return mapToDB2ColumnType(ft);
	  	case MySql:
	  		return mapToMySqlColumnType(ft);
	  	default:
	  		return mapToOracleColumnType(ft);
		}
	}
	
	public static Map<String,String> getAllTypeMap(){
		Map<String,String> fts = new HashMap<>();
		fts.put("string","string");
		fts.put("integer","integer");
		fts.put("float","float");
		fts.put("boolean","boolean");
		fts.put("date","date");
		fts.put("datetime","datetime");
		fts.put("text","text");
		fts.put("file","file");
		return fts;
	}
}
