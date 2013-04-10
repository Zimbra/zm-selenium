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
package com.zimbra.qa.selenium.projects.ajax.tests.main.attributes;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

public class ZimbraFeatureChangePasswordEnabledFalse extends AjaxCommonTest {

	public ZimbraFeatureChangePasswordEnabledFalse() {
		logger.info("New "+ ZimbraFeatureChangePasswordEnabledFalse.class.getCanonicalName());

		super.startingPage = app.zPageMail;

		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -6950184638729472879L;
			{

				// Options/Preferences is disabled
				put("zimbraFeatureChangePasswordEnabled", "FALSE");

			}
		};

	}
	
	/**
	 * @throws HarnessException
	 */
	@Bugs(ids="81522")	
	@Test(
			description = "Verify the 'Change Password' option is not present in main->account->Change Password", 
			groups = { "functional" }
			)
	public void ZimbraFeatureChangePasswordEnabledFalse_01() throws HarnessException {

		// Click the account pulldown to see the menu
		String locator = "css=td[id='skin_dropMenu'] td[id$='_dropdown']";
		
		app.zPageMain.zClickAt(locator, "");

		// Look for the menu
		locator = "css=div[id^='POPUP'] div#documentation td[id$='_title']";
		boolean present = app.zPageMain.sIsElementPresent(locator);
		ZAssert.assertTrue(present, "Verify the pulldown is present");

		locator = "css=div[id^='POPUP'] div#changePassword td[id$='_title']";
		present = app.zPageMain.sIsElementPresent(locator);
		ZAssert.assertFalse(present, "Verify the change password option is not present");
	}
}