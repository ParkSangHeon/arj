package com.ar.web.mybatis.db;

import java.io.IOException;

import java.io.Reader;
import java.sql.Connection;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.ar.common.config.ARConfig;
import com.ar.common.constant.ARConst;
import com.ar.common.db.ConnectionManager;
import com.ar.common.logging.ARLog;

/**
 * 2010.7.21
 * MyBatis3을 기준으로한 ConnectionManager의 구현체
 * SqlSession을 이용해 Connection 처리
 * Session은 Thread별로 할당하여, ThreadLocal로 관리된다.
 * @author Bak SangHeon
 *
 */
public class MyBatisConnectionManager implements ConnectionManager{
	private ARLog log = ARLog.getLogger(MyBatisConnectionManager.class);
	private ARConfig config = ARConfig.getInstance();
	
	private SqlSessionFactory sessionFactory;
	private ThreadLocal<SqlSession> localSession = new ThreadLocal<SqlSession>();
	public MyBatisConnectionManager(){
		try {
			Reader reader = Resources.getResourceAsReader(config.getValue(ARConst.MYBATIS_CONFIGURATION_FILE));
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (IOException e) {
			log.error(e);
		} // try
	} // MyBatisConnectionManager()
	
	public void getSecondarySession() {
		log.info("### Get Secondary Session");
		try {
			Reader reader = Resources.getResourceAsReader(config.getValue(ARConst.MYBATIS_CONFIGURATION_FILE_2));
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			localSession = new ThreadLocal<SqlSession>();
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public SqlSession getSession(){
		SqlSession session = null;
		Object obj = localSession.get();
		if(obj != null){
			session = (SqlSession)obj;
			log.debug("Get Session : " + session.hashCode());
			
			try {
				Connection conn = session.getConnection();
				if(conn.isClosed()) throw new Exception("Connection is closed");
			} catch (Exception e) {
				log.error("Connection 생성 실패", e);
				log.warn("현재 세션 종료");
				
				getSecondarySession();
				this.open();
				obj = localSession.get();
				if(obj != null){
					session = (SqlSession)obj;
					log.debug("Get Session : " + session.hashCode());
				}
			} // try
		} // if
		return session;
	} // getSession()
	
	@SuppressWarnings("unchecked")
	public SqlSession getBatchSession() {
		this.openBatch();
		SqlSession session = null;
		Object obj = localSession.get();
		if(obj != null){
			session = (SqlSession)obj;
			log.debug("Get Session : " + session.hashCode());
		} // if
		return session;
	} // getBatchSession();
	
	public void close() {
		SqlSession session = localSession.get();
		session.close();
		log.debug("Session Close : " + session.hashCode());
		localSession.remove();
	} // close()

	public void commit() {
		try {
			SqlSession session = localSession.get();
			session.commit();
			log.debug("Session Commit : " + session.hashCode());
		} catch(NullPointerException e){
			log.error(e);
		} // try
	} // commit()

	public void open() {
		open(false);
	} // connect()
	
	public void open(boolean autoCommit) {
		SqlSession session = localSession.get();
		if(session == null){
			session = sessionFactory.openSession(autoCommit);
			localSession.set(session);
		}
		log.debug("Session Open : " + session.hashCode());
	} // connect
	
	public void openBatch() {
		this.openBatch(false);
	} // openBatch()
	
	public void openBatch(boolean autoCommit) {
		SqlSession session = sessionFactory.openSession(ExecutorType.BATCH, autoCommit);
		localSession.set(session);
	} // openBatch
	
	public void rollback() {
		SqlSession session = localSession.get();
		session.rollback();
		log.debug("Session Rollback : " + session.hashCode());
	} // rollback();

} // class MyBatisConnectionManager
