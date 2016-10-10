/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2016 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.filters.activitystreams;

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
