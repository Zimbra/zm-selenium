/*
 * ***** BEGIN LICENSE BLOCK *****
 * 
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011 VMware, Inc.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * 
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.sandbox.projects.sand.tests.multithreaded;

import org.testng.annotations.Test;

public class MultithreadedClass02 extends AbsMultithreadedTest {

	public MultithreadedClass02() {
		logger.info("new MultithreadedClass02");
	}
	
	@Test()
	public void testMethod01() {
		logger.info("MultithreadedClass02.testMethod01 ...");
		sleep(1500);
		logger.info("MultithreadedClass02.testMethod01 ... done");
	}

	@Test()
	public void testMethod02() {
		logger.info("MultithreadedClass02.testMethod02 ...");
		sleep(2500);
		logger.info("MultithreadedClass02.testMethod02 ... done");
	}

	@Test()
	public void testMethod03() {
		logger.info("MultithreadedClass02.testMethod02 ...");
		sleep(3500);
		logger.info("MultithreadedClass02.testMethod02 ... done");
	}

	@Test()
	public void testMethod04() {
		logger.info("MultithreadedClass02.testMethod02 ...");
		sleep(4500);
		logger.info("MultithreadedClass02.testMethod02 ... done");
	}

	@Test()
	public void testMethod05() {
		logger.info("MultithreadedClass02.testMethod02 ...");
		sleep(5500);
		logger.info("MultithreadedClass02.testMethod02 ... done");
	}


}
