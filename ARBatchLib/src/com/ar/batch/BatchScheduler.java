package com.ar.batch;

/***************************
 * <pre>
 * 스캐쥴 정보를 담은 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 ***************************/
public class BatchScheduler {
	
	private String _name;
	private String _schedule;
	private String _task;
	
	public void setName(String name) { this._name = name; }
	public void setSchedule(String schedule) { this._schedule = schedule; }
	public void setTask(String task) { this._task = task; }
	
	public String getName() { return this._name; }
	public String getSchedule() { return this._schedule; }
	public String getTask() { return this._task; }
	
	
} // class Scheduler
