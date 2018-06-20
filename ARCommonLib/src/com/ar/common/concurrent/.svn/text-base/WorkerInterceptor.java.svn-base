package com.ar.common.concurrent;

import com.ar.common.logging.ARLog;
import com.ar.common.proxy.ARInterceptor;

/********************************
 * <pre>
 * Worker에 이벤트를 가로챌 인터셉터
 * </pre>
 * @author Bak Sang Heon
 *
 ********************************/
public class WorkerInterceptor extends ARInterceptor {
	private static ARLog log = ARLog.getLogger(WorkerInterceptor.class);
	
	public WorkerInterceptor(String[] methods) {
		super(methods);
	} // Constructor

	@Override
	protected void before() {
	} // before()
	
	@Override
	protected void before(Object obj) {
		if (obj instanceof Worker) {
			Worker worker = (Worker)obj;
			worker.beforeRun();
		} // if
	} // before(Object)

	@Override
	protected void after() {
	} // after()
	
	@Override
	protected void after(Object obj) {
		if (obj instanceof Worker) {
			Worker worker = (Worker)obj;
			worker.afterRun();
			worker.stopWorker();
		} // if
	} // after(Object);

} // class WorkerInterceptor
