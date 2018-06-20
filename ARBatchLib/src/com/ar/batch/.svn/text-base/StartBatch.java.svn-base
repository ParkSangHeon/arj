package com.ar.batch;

import java.util.List;

import com.ar.common.logging.ARLog;

/****************************
 * <pre>
 * 배치 프로세스를 구동한다.
 * </pre>
 * @author Bak Sang Heon
 *
 ****************************/
public class StartBatch {
	private static ARLog log = ARLog.getLogger(StartBatch.class);
	
	/*****************
	 * <pre>
	 * 배치를 구동한다.
	 * </pre>
	 *****************/
	public void start() {
		try {
			ScheduleManager manager = ScheduleManager.getScheduleManager();
			List<BatchScheduler> schedulerList = ScheduleLoader.load();
			for (BatchScheduler scheduler : schedulerList) {
				manager.addScheduler(scheduler);
			} // for
			
			manager.start();
			
		} catch (Exception e) {
			log.error("Can't create schedule");
			log.error(e);
		} // try
		
	} // start()
} // class StartBatch
