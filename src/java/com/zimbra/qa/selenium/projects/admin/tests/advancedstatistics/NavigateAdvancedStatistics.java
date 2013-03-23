/*
 * ***** BEGIN LICENSE BLOCK *****
 * 
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012 VMware, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.advancedstatistics;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageAdvancedStatistics;

public class NavigateAdvancedStatistics extends AdminCommonTest {
	
	public NavigateAdvancedStatistics() {
		logger.info("New "+ NavigateAdvancedStatistics.class.getCanonicalName());

		// All tests start at the "Monitor" page
		super.startingPage = app.zPageManageAdvancedStatistics;
	}
	
	/**
	 * Testcase : Navigate to Advanced Statistics page
	 * Steps :
	 * 1. Verify navigation path -- "Home --> Monitor --> Advanced Statistics"
	 * @throws HarnessException
	 */
	@Test(	description = "Navigate to Advanced Statistics",
			groups = { "sanity" })
			public void NavigateAdvancedStatistics_01() throws HarnessException {
		
		/*
		 * Verify navigation path -- "Home --> Monitor --> Advanced Statistics"
		 */
		ZAssert.assertTrue(app.zPageManageAdvancedStatistics.zVerifyHeader(PageManageAdvancedStatistics.Locators.HOME), "Verfiy the \"Home\" text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageAdvancedStatistics.zVerifyHeader(PageManageAdvancedStatistics.Locators.MONITOR), "Verfiy the \"Monitor\" text exists in navigation path");
		ZAssert.assertTrue(app.zPageManageAdvancedStatistics.zVerifyHeader(PageManageAdvancedStatistics.Locators.ADVANCED_STATISTICS), "Verfiy the \"Advanced Statistics\" text exists in navigation path");
		
	}

}
