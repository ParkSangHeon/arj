package com.ar.web.mybatis.service;

import java.sql.Connection;

import org.apache.ibatis.session.SqlSession;

import com.ar.common.db.ConnectionManager;
import com.ar.common.db.DBManager;
import com.ar.common.logging.ARLog;
import com.ar.common.service.CommonService;

public class TxService implements CommonService {
	private static ARLog log = ARLog.getLogger(TxService.class);
	private static DBManager dbm = DBManager.getInstance();
	protected static ConnectionManager cm = dbm.getConnectionManager();
	protected SqlSession session;
	
	public TxService(){
	} // TxService()
	
	protected <T> T getDao(Class<T> clazz){
		T dao = null;
		session = cm.getSession();
		dao = session.getMapper(clazz);
		return dao;
	} // getDao();
	
	protected <T> T getBatchDao(Class<T> clazz) {
		session = cm.getBatchSession();
		T dao = session.getMapper(clazz);
		return dao;
	}
	
	public void setSession(SqlSession session){
		this.session = session;
	} // setSession()
	
	protected void open(boolean autoCommit){
		cm.open(autoCommit);
	} // open()
	
	protected void open(){
		cm.open();
	} // open();
	
	protected void openBatch(boolean autoCommit) {
		cm.openBatch(autoCommit);
	} // openBatch()
	
	protected void openBatch() {
		cm.openBatch();
	} // openBatch();
	
	protected void commit(){
		cm.commit();
	} // commit();
	
	protected void rollback(){
		cm.rollback();
	} // rollback();
	
	protected void close(){
		cm.close();
	} // close();

} // class TxService
