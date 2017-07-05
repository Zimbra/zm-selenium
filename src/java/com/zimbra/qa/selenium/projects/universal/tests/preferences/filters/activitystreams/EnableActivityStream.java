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
package com.zimbra.qa.selenium.projects.universal.tests.preferences.filters.activitystreams;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.DialogActivityStream;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.TreePreferences.TreeItem;


public class EnableActivityStream extends AjaxCommonTest {

	public EnableActivityStream() {
		
		super.startingPage = app.zPagePreferences;
		
		
	}


	@Test(
			description = "Enable the Activity Streams feature",
			groups = { "functional", "L2" }
			)
	public void EnableActivityStream_01() throws HarnessException {

		//-- DATA
		
		
		
		//-- GUI
		
		// Navigate to preferences -> mail -> composing
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailFilters);

	
		// Click "Activity Stream Settings"
		DialogActivityStream dialog = (DialogActivityStream)app.zPagePreferences.zToolbarPressButton(Button.B_ACTIVITY_STREAM_SETTINGS);
		
		// Check "Enable"
		dialog.zClickCheckbox(Button.B_ACTIVITY_STREAM_ENABLE, true);
		
		// Save
		dialog.zClickButton(Button.B_SAVE);
		
				
		
		//-- VERIFICATION

		app.zGetActiveAccount().soapSend(
						"<GetFilterRulesRequest xmlns='urn:zimbraMail'/>");
		
		// TODO: need to make this I18N compatible
		com.zimbra.common.soap.Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:filterRule[@name='Activity Stream']");
		
		ZAssert.assertGreaterThan(nodes.length, 0, "Verify the activity stream filter was created");
		
		String active = app.zGetActiveAccount().soapSelectValue("//mail:filterRule[@name='Activity Stream']", "active");
		
		ZAssert.assertEquals(active, "1", "Verify the activity stream filter is active");

	}
}
