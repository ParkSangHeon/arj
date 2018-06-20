package com.ar.common.concurrent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ar.common.logging.ARLog;

/********************************
 * <pre>
 * 전체 Worker를 관리하는 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 ********************************/
public class Workers {
	private static ARLog log = ARLog.getLogger(Workers.class);
	
	private static Map<String, ExecutorService> workerServiceMap = new HashMap<String, ExecutorService>();
	private static Map<String, List<Worker>> workerMap = new HashMap<String, List<Worker>>();
	private static Map<String, Integer> workerCountMap = new HashMap<String, Integer>();
	
	/********************************************
	 * <pre>
	 * Worker 클래스를 Pool에 넣어 구동시킨다.
	 * 해당 Worker에 대한 Pool이 없다면 새로 생성한다.
	 * </pre>
	 * @param workerName
	 * @param poolSize
	 * @param worker
	 ********************************************/
	public static void work(int poolSize, Worker worker) {
		String workerName = worker.getName();
		log.debug("WorkerName : {}", workerName);
		
		// Pool을 가져온다.
		log.debug("Get ExecutorService");
		ExecutorService service = workerServiceMap.get(workerName);
		if (service == null) {
			// Pool이 없다면 생성한다.
			log.debug("ExecutorService is null");
			service = Executors.newFixedThreadPool(poolSize);
			log.debug("new executor service is created");
			workerServiceMap.put(workerName, service);
		} // if
		
		log.debug("run executor");
		service.execute(worker);
		
		log.debug("Get Worker Count");
		Integer workerCount = workerCountMap.get(workerName);
		if (workerCount == null) { 
			workerCount = 0;
		} // if
		
		workerCount++;
		log.debug("Put Worker Count");
		workerCountMap.put(workerName, workerCount);
		
		log.debug("Add Worker List");
		List<Worker> workerList = workerMap.get(workerName);
		if (workerList == null) {
			workerList = new ArrayList<Worker>();
			workerMap.put("workerName", workerList);
		}
		workerList.add(worker);
	} // work()
	
	/****************************
	 * <pre>
	 * Worker의 작업 종료시 호출된다.
	 * </pre>
	 * @param worker
	 ****************************/
	public static void stopWorking(Worker worker) {
		String workerName = worker.getName();
		Integer workerCount = workerCountMap.get(workerName);
		if (workerCount != null) {
			workerCount--;
			workerCountMap.put(workerName, workerCount);
		} // if
		
		List<Worker> workerList = workerMap.get(workerName);
		workerList.remove(worker);
		
		log.debug("Worker {} is stoped", workerName);
		log.debug("WORKER COUNT : {}", workerCount);
	} // stopWorking()
	
	/***********************************
	 * <pre>
	 * 해당 명칭의 Worker 구동 갯수를 확인한다.
	 * </pre>
	 * @param workerName
	 * @return
	 ***********************************/
	public static int getRunningWorkerCount(String workerName) {
		Integer workerCount = workerCountMap.get(workerName);
		if (workerCount == null)
			return 0;
		else 					
			return workerCount;
	} // getRunningWorkerCount()
	
	/******************************
	 * <pre>
	 * 해당 Worker의 목록을 가져온다.
	 * </pre>
	 * @param workerName
	 ******************************/
	public static List<Worker> getWorkerList(String workerName) {
		List<Worker> list = workerMap.get(workerName);
		if (list == null) {
			list = new ArrayList<Worker>();
			workerMap.put(workerName, list);
		} // if
		
		return list;
	} // getWorkerList()
	
} // class Workers




