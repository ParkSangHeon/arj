package com.ar.batch;

import com.ar.common.logging.ARLog;

import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.Task;

/*************************************
 * <pre>
 * 배치 스케쥴을 등록, 구동, 관리하는 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 *************************************/
public class ScheduleManager {
	private static ARLog log = ARLog.getLogger(ScheduleManager.class);
	
	private static ScheduleManager _instance;
	public static ScheduleManager getScheduleManager() {
		if (_instance == null) _instance = new ScheduleManager();
		return _instance;
	} // getScheduleManager()
	
	private ScheduleManager() {
		this._scheduler = new Scheduler();
	} // Constructor
	
	private Scheduler _scheduler;
	
	/************************
	 * <pre>
	 * 배치 스케쥴을 추가한다.
	 * </pre>
	 * @param scheduler
	 ************************/
	public void addScheduler(BatchScheduler scheduler) {
		String name = scheduler.getName();
		String schedule = scheduler.getSchedule();
		String taskName = scheduler.getTask();
		try {
			Class<?> clazz = Class.forName(taskName);
			Object obj = clazz.newInstance();
			if (obj instanceof Task) {
				Task task = (Task)obj;
				this._scheduler.schedule(schedule, task);
				log.info("Batch [{}] is added", name);
			} // if
		} catch (Exception e) {
			log.error(e);
		} // try
	} // addScheduler()
	
	/*********************
	 * <pre>
	 * 배치를 구동한다.
	 * </pre>
	 *********************/
	public void start() {
		this._scheduler.start();
		log.info("Batch schedule is started");
	} // start()
	
	/*****************
	 * <pre>
	 * 배치를 종료한다.
	 * </pre>
	 *****************/
	public void stop() {
		this._scheduler.stop();
	} // stop()
	
	/**************************
	 * <pre>
	 * 배치 구동 여부를 반환한다.
	 * </pre>
	 * @return
	 **************************/
	public boolean isStarted() { 
		return this._scheduler.isStarted();
	} // isStarted()
	
	
} // class ScheduleManager
