package com.ar.common.constant;

/**********************************
 * <pre>
 * 기본적으로 사용되는 상수값을 정의한다.
 * </pre>
 * @author Bak Sang Heon
 *
 **********************************/
public class ARConst {
	
	// Property 파일 위치.
//	public static final String PROPERTY_FILE = "/Users/kaori573/Documents/workspace/???/bin/ar.properties";
	public static final String PROPERTY_FILE = "ar.properties";
	
	// MyBatis 설정.
	public static final String MYBATIS_CONFIGURATION_FILE 		= "ar.db.mybatis.configuration";
	public static final String MYBATIS_CONFIGURATION_FILE_2 	= "ar.db.mybatis.configuration_secondary";
	
	// Connection Manager
	public static final String CONNECTION_MANAGER				= "ar.database.cm";
	
	// 2011.8.1 Bak Sang Heon
	// SPEC 설정.
	public static final String SPEC_PATH 						= "ar.common.spec.path";
	
	// 2011.8.4 Bak Sang Heon
	// Online
	public static final String SERVER_MONITOR_PORT				= "ar.net.server.monitor.port";
	public static final String SERVER_CONFIG_PATH				= "ar.net.server";
	public static final String CLIENT_CONFIG_PATH				= "ar.net.client";
	
	// Runtime
	public static final String RUNTIME							= "ar.runtime";
	public static final String RUN_LOCAL_TEST					= "localtest";
	public static final String RUN_WAS							= "was";		
	
	// MysqlDataSource Property
	public static final String MYSQL_CONNECTION_URL 			= "ar.database.mysql.url";
	public static final String MYSQL_CONNECTION_USER 			= "ar.database.mysql.user";
	public static final String MYSQL_CONNECTION_PASSWORD 		= "ar.database.mysql.password";
	public static final String MYSQL_CONNECTION_AUTO_RECONNECT 	= "ar.database.mysql.auto_reconnect";
	
	// Batch
	public static final String BATCH_CONFIG_PATH				= "ar.batch.configuration";
} // class ARConst
