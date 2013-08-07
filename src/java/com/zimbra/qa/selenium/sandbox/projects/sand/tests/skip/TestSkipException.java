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
package com.zimbra.qa.selenium.sandbox.projects.sand.tests.skip;

import java.lang.reflect.Method;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.HarnessException;


public class TestSkipException {
	private static Logger logger = LogManager.getLogger(TestSkipException.class);
	
	public TestSkipException() {
	}
	
	@Test(
		description = "A passing test case"
	)
	public void TestPass() throws HarnessException {
		logger.info("PASS");
		Assert.assertEquals("equal", "equal", "Verify the strings are equal");
	}

	@Test(
			description = "A failed test case"
		)
	public void TestFail() throws HarnessException {
		logger.info("FAIL");
		Assert.assertEquals("not equal", "equal", "Verify the strings are equal");
	}

	@Test(
			description = "A skipped test case"
		)
	public void TestSkipped01() throws HarnessException {
		logger.info("SKIP");
		Assert.assertEquals("not equal", "equal", "Verify the strings are equal");
	}
	
	@Test(
			description = "Another skipped test case"
		)
	public void TestSkipped02() throws HarnessException {
		logger.info("SKIP");
		Assert.assertEquals("equal", "equal", "Verify the strings are equal");
		throw new SkipException("Skip this method too!");
	}
	
	@BeforeMethod()
	public void beforeMethod(Method m) {
		logger.info("Method: " + m.getName());
		
		if ( m.getName().equals("TestSkipped01"))
			throw new SkipException("Skip this method");
	}
}
