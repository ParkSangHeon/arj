package com.ar.common.logging;

import org.slf4j.LoggerFactory;

import com.ar.common.test.CommonTest;

import ch.qos.logback.classic.Logger;


/***************************
 * <pre>
 * Logback 테스트 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 ***************************/
public class LogbackTest implements CommonTest{
	private static ARLog logger = ARLog.getLogger(LogbackTest.class);
	
	@Override
	public String getName() {
		return "Logback Test";
	}

	@Override
	public void test() throws Exception {
		logger.debug("Logback Test!!!");
		logger.info("Logback Test!!!");
	} // test()
	
	
} // class LogbackTest
