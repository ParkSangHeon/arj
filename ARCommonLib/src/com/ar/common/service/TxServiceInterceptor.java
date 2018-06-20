package com.ar.common.service;

import com.ar.common.db.ConnectionManager;
import com.ar.common.db.DBManager;
import com.ar.common.logging.ARLog;
import com.ar.common.proxy.ARInterceptor;

/*******************************************
 * <pre>
 * 트랜잭션 전후 처리를 진행하는 인터셉터 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 *******************************************/
public class TxServiceInterceptor extends ARInterceptor{
	private ARLog log = ARLog.getLogger(TxServiceInterceptor.class);
	private ConnectionManager cm = DBManager.getInstance().getConnectionManager();
	public TxServiceInterceptor(String[] methods) {
		super(methods);
	} // TxServiceIntercepter

	@Override
	protected void before() {
		// 커넥션을 생성하고 트랜잭션을 시작한다.
		cm.open(false);
		log.debug("Transaction Start");
	} // before()
	
	@Override
	protected void after() {
		// 트랜잭션 커밋을 수행하고 커넥션을 닫는다.
		cm.commit();
		log.debug("Transaction Commited");
		cm.close();
		log.debug("Transaction Closed");
	} // after()	

} // class TxServiceInterceptor
