package com.ar.db.mongo;

import com.ar.common.config.ARConfig;
import com.ar.common.logging.ARLog;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

/***********************************
 * <pre>
 * MongoDB 관련 연결을 관리하는 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 ***********************************/
public class ARMongoManager {
	private static ARLog log = ARLog.getLogger(ARMongoManager.class);
	
	private static Datastore _ds;
	
	/**************************
	 * <pre>
	 * Datastore 객체를 반환한다.
	 * </pre>
	 * @return
	 **************************/
	public static Datastore getDatastore() {
		if (_ds == null) _ds = connect();
		return _ds;
	} // getDatastore()
	
	/********************
	 * <pre>
	 * MongoDB에 연결한다.
	 * </pre>
	 ********************/
	private static Datastore connect() {
		ARConfig config = ARConfig.getInstance();
		String db = config.getValue(ARMongoConst.DB);
		String host = config.getValue(ARMongoConst.HOST);
		int port = config.getInteger(ARMongoConst.PORT);
		String user = config.getValue(ARMongoConst.USER);
		String password = config.getValue(ARMongoConst.PASSWORD);
		
		Datastore ds = null;
		try {
			Morphia morphia = new Morphia();
			Mongo mongo = new Mongo(host, port);
			ds = morphia.createDatastore(mongo, db, user, password.toCharArray());
			ds.ensureIndexes();
			ds.ensureCaps();
		} catch (Exception e) {
			log.error(e);
		} // try
		return ds;
	} // connect()

} // class ARMongoManager


