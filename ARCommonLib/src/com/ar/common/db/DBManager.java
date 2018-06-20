package com.ar.common.db;

import com.ar.common.config.ARConfig;
import com.ar.common.constant.ARConst;
import com.ar.common.logging.ARLog;

/**
 * <pre>
 * DB연결을 위한 ConnectionManager을 생성한다. 
 * </pre>
 * @author Bak Sang Heon
 *
 */
public class DBManager {
	private ARLog log = ARLog.getLogger(DBManager.class);
	private ARConfig config = ARConfig.getInstance();
	private static ConnectionManager cm;
	
	private static DBManager instance;
	public static synchronized DBManager getInstance(){
		if(instance == null) instance = new DBManager();
		return instance;
	} // getInstance()
		
	private DBManager(){
	} // DBManager()
	
	public synchronized ConnectionManager getConnectionManager(){
		if(cm == null){
			String cmName = config.getValue(ARConst.CONNECTION_MANAGER);
			log.debug("Create ConnectionManager : " + cmName);
			if (cmName == null) return null;
			
			try {
				Class<?> clazz = Class.forName(cmName);
				cm = (ConnectionManager)clazz.newInstance();
			} catch (ClassNotFoundException e) {
				log.error(e);
			} catch (InstantiationException e) {
				log.error(e);
			} catch (IllegalAccessException e) {
				log.error(e);
			} // try
		} // if
		
		return cm;
	} // getConnectionManager()	
	
} // class DBManager
