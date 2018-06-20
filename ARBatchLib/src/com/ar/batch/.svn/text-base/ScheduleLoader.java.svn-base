package com.ar.batch;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.ar.common.config.ARConfig;
import com.ar.common.logging.ARLog;
import com.ar.common.spec.SpecFactory;

/*************************************
 * <pre>
 * 설정파일을 읽어 배치 스케쥴을 생성한다.
 * </pre>
 * @author Bak Sang Heon
 *
 *************************************/
public class ScheduleLoader {
	private static ARLog log = ARLog.getLogger(ScheduleLoader.class);
	
	/***********************
	 * <pre>
	 * 배치 설정파일을 파싱한다.
	 * </pre>
	 * @return
	 * @throws Exception
	 ***********************/
	public static List<BatchScheduler> load() throws Exception {
		String path = ARConfig.getInstance().getValue(BatchConst.BATCH_CONFIG);
		InputStream in = SpecFactory.class.getClassLoader().getResourceAsStream(path);
		
		List<BatchScheduler> scheduleList = new ArrayList<BatchScheduler>();
		
		Document document = null;
		if (in == null) {	
			SAXBuilder builder = new SAXBuilder();
			document = builder.build(new File(path));
			log.debug("InputStream is null");
		} else {  			
			SAXBuilder builder = new SAXBuilder();
			document = builder.build(in);
			log.debug("InputStream is not null");
		} // if
		
		Element root = document.getRootElement();
		List<Element> schedulers = root.getChildren(BatchConst.TAG_SCHEDULER);
		for (Element schedule : schedulers) {
			BatchScheduler scheduler = getScheduler(schedule);
			scheduleList.add(scheduler);
		} // for
		
		try {
			if (in != null) in.close();
		} catch (Exception e) {
			log.error(e);
		} // try
		
		return scheduleList;
	} // load()
	
	/***********************************
	 * <pre>
	 * 태그 정보로 스케쥴을 생성하여 반환한다.
	 * </pre>
	 * @param tag
	 * @return
	 ***********************************/
	private static BatchScheduler getScheduler(Element tag) {
		BatchScheduler scheduler = new BatchScheduler();
		
		String name = tag.getAttributeValue(BatchConst.ATTR_NAME);
		
		Element scheduleTag = tag.getChild(BatchConst.TAG_SCHEDULE);
		String schedule = scheduleTag.getValue().trim();
		
		Element taskTag = tag.getChild(BatchConst.TAG_TASK);
		String task = taskTag.getValue().trim();
		
		scheduler.setName(name);
		scheduler.setSchedule(schedule);
		scheduler.setTask(task);
		
		return scheduler;
	} // getScheduler()
	
} // class ScheduleLoader
