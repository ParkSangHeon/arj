package com.ar.common.process;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.ar.common.logging.ARLog;
import com.ar.common.process.ProcessOutputListener.Type;

/*************************************************
 * <pre>
 * 명령 프로세스에서 출력한 메세지를 수신하는 스레드 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 *************************************************/
public class ProcessOutputMonitor implements Runnable {
	private static ARLog log = ARLog.getLogger(ProcessOutputMonitor.class);
	
	private boolean _runningFlag;
	private Type _type;
	private BufferedReader _reader;
	private ProcessOutputListener _listener;
	
	public ProcessOutputMonitor(InputStream in, ProcessOutputListener listener, Type type) {
		super();
		this._reader = new BufferedReader(new InputStreamReader(in));
		this._listener = listener;
		this._type = type;
	} // Constructor
	
	/***************************************************
	 * <pre>
	 * 루프를 돌면서 받은 메세지를 지속적으로 Listener로 전달한다.
	 * </pre>
	 ***************************************************/
	@Override
	public void run() { 
		try {
			this._runningFlag = true;
			char[] buffer = new char[256];
			while(this._runningFlag) {
				int read = this._reader.read(buffer);
				if (read <= 0) break;
				this._listener.messageReceived(this._type, buffer, 0, read);
			} // while
		} catch (Exception e) {
			log.error(e);
		} finally {
			try {
				if ( this._reader != null) this._reader.close();
				this._reader = null;
			} catch (Exception e) {
				log.error(e);
			} // try
		} // try
	} // run()
	
	/**************
	 * <pre>
	 * 연결 종료.
	 * </pre>
	 **************/
	public void close() {
		this._runningFlag = false;
		if (this._reader != null)
			try {
				this._reader.close();
				this._reader = null;
			} catch (Exception e) { 
				log.error(e); 
			} // try
	} // close()
} // class ProcessOutputMonitor
