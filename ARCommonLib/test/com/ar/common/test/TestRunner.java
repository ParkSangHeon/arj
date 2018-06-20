package com.ar.common.test;

import java.util.ArrayList;
import java.util.List;

/*******************************
 * <pre>
 * 테스트 코드를 구동시키는 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 *******************************/
public class TestRunner {
	public static void main(String[] args) {
		System.out.println("BEGIN TEST ===============================");
		
		TestRunner.addTest("com.ar.common.logging.LogbackTest");
		
		TestRunner.runTest();
		
		System.out.println("END TEST ===============================");
	} // main()
	
	
	private static List<CommonTest> tests;
	/************************
	 * <pre>
	 * 테스트 클래스를 추가한다.
	 * </pre>
	 * @param className
	 ************************/
	public static void addTest(String className) {
		if (tests == null) {
			tests = new ArrayList<CommonTest>();
		} // if
		
		try {
			System.out.println("Add Test : " + className);
			Class<?> clazz = Class.forName(className);
			if (clazz == null) throw new Exception();
			Object obj = clazz.newInstance();
			if (obj instanceof CommonTest) {
				CommonTest test = (CommonTest)obj;
				tests.add(test);
			} else {
				throw new Exception();
			} // if
		} catch (Exception e) {
			System.err.println("Failed to Test");
			System.err.println("Cause : Can't not add CommonTest : " + className);
			e.printStackTrace();
			System.exit(1);
		} // try
	} // addTest()
	
	public static void addTest(Class<?> clazz) {
		if (tests == null) {
			tests = new ArrayList<CommonTest>();
		} // if
		
		try {
			System.out.println("Add Test : " + clazz.getName());
			if (clazz == null) throw new Exception();
			Object obj = clazz.newInstance();
			if (obj instanceof CommonTest) {
				CommonTest test = (CommonTest)obj;
				tests.add(test);
			} else {
				throw new Exception();
			} // if
		} catch (Exception e) {
			System.err.println("Failed to Test");
			System.err.println("Cause : Can't not add CommonTest : " + clazz.getName());
			e.printStackTrace();
			System.exit(1);
		} // try
		
	} // addTest()
	
	/*********************
	 * <pre>
	 * 테스트를 실행한다.
	 * </pre>
	 *********************/
	public static void runTest() {
		System.out.println("Run Test");
		for(CommonTest test : tests) {
			try {
				String test_name = test.getName();
				if (test_name == null) {
					throw new Exception("Can not found 'Test Name'");
				} // if
				try {
					test.test();
				} catch (Exception e) {
					System.err.println("Test Failed : " + test_name);
					continue;
				} // try
			} catch (Exception e) {
				System.err.println("Test Failed");
				System.err.println("Cause : " + e.getMessage());
				e.printStackTrace();
			} // try
		} // for
	} // runTest()
	
} // class TestRunner
