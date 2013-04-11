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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.filters.activitystreams;

import java.lang.reflect.Method;

import org.testng.ITestContext;
import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.DialogActivityStream;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;


public class DisableActivityStream extends AjaxCommonTest {

	public DisableActivityStream() {
		
		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = null;
		
	}

	/**
	 * It is difficult to enable Activity Streams using SOAP
	 * Use the client to do so before each test method
	 */
	@BeforeMethod( groups = { "always" } )
	public void DisableActivityStream_BeforeMethod(Method method, ITestContext testContext) throws HarnessException {

		app.zPagePreferences.zNavigateTo();
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailFilters);
		DialogActivityStream dialog = (DialogActivityStream)app.zPagePreferences.zToolbarPressButton(Button.B_ACTIVITY_STREAM_SETTINGS);
		dialog.zClickCheckbox(Button.B_ACTIVITY_STREAM_ENABLE, true);
		dialog.zClickButton(Button.B_SAVE);

	}
	
	@Test(
			description = "Disable the Activity Streams feature",
			groups = { "functional" }
			)
	public void DisableActivityStream_01() throws HarnessException {

		//-- DATA
		
		
		
		//-- GUI
		
		// Navigate to preferences -> mail -> composing
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailFilters);

	
		// Click "Activity Stream Settings"
		DialogActivityStream dialog = (DialogActivityStream)app.zPagePreferences.zToolbarPressButton(Button.B_ACTIVITY_STREAM_SETTINGS);
		
		// Check "Enable"
		dialog.zClickCheckbox(Button.B_ACTIVITY_STREAM_ENABLE, false);
		
		// Save
		dialog.zClickButton(Button.B_SAVE);
		
				
		
		//-- VERIFICATION

		app.zGetActiveAccount().soapSend(
						"<GetFilterRulesRequest xmlns='urn:zimbraMail'/>");
		
		// TODO: need to make this I18N compatible
		com.zimbra.common.soap.Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:filterRule[@name='Activity Stream']");
		
		ZAssert.assertGreaterThan(nodes.length, 0, "Verify the activity stream filter was created");
		
		String active = app.zGetActiveAccount().soapSelectValue("//mail:filterRule[@name='Activity Stream']", "active");
		
		ZAssert.assertEquals(active, "0", "Verify the activity stream filter is disabled");

	}
}
