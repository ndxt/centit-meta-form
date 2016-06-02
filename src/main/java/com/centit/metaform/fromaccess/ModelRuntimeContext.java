package com.centit.metaform.fromaccess;

import java.sql.Connection;

public class ModelRuntimeContext {
	private Connection conn;

	public Connection getConnection() {
		return conn;
	}

	public void setConnection(Connection conn) {
		this.conn = conn;
	}
	
}
