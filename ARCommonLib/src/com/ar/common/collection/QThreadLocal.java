package com.ar.common.collection;

/**
 * 2010.6.10
 * ThreadLocal의 구현클래스.
 * QMap을 내부에 담고있다.
 * @author Bak sang heon
 *
 */
public class QThreadLocal {
	ThreadLocal<QMap> localMap = new ThreadLocal<QMap>();
	
	/**
	 * 2010.6.10
	 * ThreadLocal에 담긴 QMap을 가져온다.
	 * @author Bak sang heon
	 * @return
	 */
	public QMap get(){
		QMap map = null;
		map = this.localMap.get();
		if ( map == null){
			map = new QMap();
			this.localMap.set(map);
		} // if		
		return map;
	} // get()	
	
	public void set(QMap map){
		this.localMap.set(map);
	} // set()
} // class QThreadLocal
