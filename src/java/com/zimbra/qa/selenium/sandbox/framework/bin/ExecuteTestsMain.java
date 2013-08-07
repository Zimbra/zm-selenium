/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013 Zimbra Software, LLC.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.sandbox.framework.bin;

import org.apache.log4j.BasicConfigurator;
import org.testng.TestNG;

import com.zimbra.qa.selenium.sandbox.projects.sand.tests.multithreaded.MultithreadedClass01;
import com.zimbra.qa.selenium.sandbox.projects.sand.tests.multithreaded.MultithreadedClass02;
import com.zimbra.qa.selenium.sandbox.projects.sand.tests.skip.TestSkipException;


public class ExecuteTestsMain {

	// TestNG configurations
	public static final String TEST_NG_SERIAL			= "classes";
	public static final String TEST_NG_PARALLEL_CLASSES	= "classes";
	public static final String TEST_NG_PARALLEL_METHODS	= "methods";
	
	public String		TestNgOutputFoldername	= "sandbox-output";
	public int			TestNgVerbosity			= 10;
	public String		TestNgParallel			= TEST_NG_SERIAL;
	public int			TestNgThreadCount		= 5;
	public Class<?>[]	TestNgTestClasses		=
							{
								TestSkipException.class,
								MultithreadedClass01.class,
								MultithreadedClass02.class
							};
	

	
	
	public ExecuteTestsMain() {
		
	}
	
	public void execute() {
		TestNG ng = new TestNG();
	
		ng.setTestClasses(TestNgTestClasses);

		ng.setOutputDirectory(TestNgOutputFoldername);
		
		ng.setVerbose(TestNgVerbosity);
		
		if ( TestNgParallel != TEST_NG_SERIAL ) { 
			ng.setParallel(TestNgParallel);
			ng.setThreadCount(TestNgThreadCount);
		}
		
		ng.run();
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();

		ExecuteTestsMain harness = new ExecuteTestsMain();
		harness.execute();
		
	}

}
