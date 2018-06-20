package com.ar.common.concurrent;

import com.ar.common.logging.ARLog;

/*********************************
 * <pre>
 * Worker를 구현하기 위한 상위클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 *********************************/
public abstract class Worker implements Runnable {
	private static ARLog log = ARLog.getLogger(Worker.class);
	
	protected String _workerName;
	protected boolean _isRunning;
	
	
	/*****************************
	 * <pre>
	 * Worker의 생성자.
	 * 파라미터로 Worker명을 받는다.
	 * </pre>
	 * @param workerName
	 *****************************/
	public Worker(String workerName) {
		this._workerName = workerName;
	} // Constructor
	
	public String getName() {
		return this._workerName;
	} // getName()
	
	public boolean isRunning() {
		return this._isRunning;
	} // isRunning()
	
	public void beforeRun(){}
	public void afterRun(){}
	
	/*****************************************************
	 * <pre>
	 * Worker의 구동 종료시 WorkerInterceptor를 통해 호출된다.
	 * </pre>
	 *****************************************************/
	public void stopWorker() {
		Workers.stopWorking(this);
		log.debug("Worker is stoped");
	} // stopWorker()
	
} // Worker
