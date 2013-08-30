/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013 Zimbra Software, LLC.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.4 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.sandbox.projects.sand.tests.multithreaded;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public abstract class AbsMultithreadedTest {
	public static Logger logger = LogManager.getLogger(AbsMultithreadedTest.class);
	
	public AbsMultithreadedTest() {
		logger.info("new AbsMultithreadedTest");
	}
	
	public void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			logger.warn("Caught InterruptedException during sleep.  Ignoring.");
		}
	}
}
