/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.preferences.mail.displayingmessages;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.*;
import com.zimbra.qa.selenium.projects.universal.pages.preferences.TreePreferences.*;


public class ZimbraPrefMailPollingIntervalAsMailArrives extends UniversalCore {

	public static final long AsMailArrives = 500;			// The value set by the client when choosing AsMailArrives
	public static final long AsMailArrivesDelay = 7000;		// Give 7 seconds to show the mail



	public ZimbraPrefMailPollingIntervalAsMailArrives() {
		logger.info("New "+ ZimbraPrefMailPollingIntervalAsMailArrives.class.getCanonicalName());

		super.startingPage = app.zPagePreferences;
		//		super.startingAccountPreferences = new HashMap<String, String>() {
		//			private static final long serialVersionUID = 3090644573042724593L;
		//			{
		//				put("zimbraPrefMailPollingInterval", "300"); // 5 minutes default
		//			} };
		//

	}

	@Test (description = "Set 'Check new mail': As Mail Arrives",
			groups = { "functional", "L2" })
	public void ZimbraPrefMailPollingIntervalAsMailArrives_01() throws HarnessException {		

		// Navigate to preferences -> mail -> displaying messages
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Mail);

		// Click pulldown for "Check New Mail:"
		// See https://bugzilla.zimbra.com/show_bug.cgi?id=81992
		// See https://bugzilla.zimbra.com/attachment.cgi?id=48826
		//	app.zPagePreferences.sClickAt("css=div.ZmPreferencesPage td.ZOptionsField td[id$='_select_container'] td[id$='dropdown'] div.ImgSelectPullDownArrow", "");

		app.zPagePreferences.sClickAt("css=td[id='Prefs_Select_POLLING_INTERVAL_dropdown']>div", "");
		// Click "As Mail Arrives"
		// See https://bugzilla.zimbra.com/attachment.cgi?id=48828
		app.zPagePreferences.sClickAt("css=div[id^='Prefs_Select_POLLING_INTERVAL_Menu'] td[id$='_title']:contains('As new mail arrives')", "");

		// Click save
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);



		//-- VERIFICATION

		app.zGetActiveAccount().soapSend(
				"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
						+			"<pref name='zimbraPrefMailPollingInterval'/>"
						+		"</GetPrefsRequest>");

		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefMailPollingInterval']", null);
		ZAssert.assertEquals(value, ""+ AsMailArrives, "Verify the preference was changed to "+ AsMailArrives);


	}



}
