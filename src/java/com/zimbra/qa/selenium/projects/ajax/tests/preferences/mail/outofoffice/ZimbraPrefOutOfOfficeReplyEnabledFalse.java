/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.outofoffice;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

public class ZimbraPrefOutOfOfficeReplyEnabledFalse extends AjaxCommonTest {

	public static final String Message = "message" + ZimbraSeleniumProperties.getUniqueString();
	

	public ZimbraPrefOutOfOfficeReplyEnabledFalse() throws HarnessException {
		
		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -3101848474022410670L;
			{
				put("zimbraPrefOutOfOfficeReplyEnabled", "TRUE");
				put("zimbraPrefOutOfOfficeReply", Message);
				put("zimbraPrefOutOfOfficeStatusAlertOnLogin", "TRUE");
			}
		};
		
	}

	@Test(
			description = "Disable out of office",
			groups = { "smoke" }
			)
	public void ZimbraPrefOutOfOfficeReplyEnabledTrue_01() throws HarnessException {

		/* test properties */


	
		/* GUI steps */


		// Navigate to preferences -> mail -> Out of Office
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailOutOfOffice);
		
		// Disable the preferences
		app.zPagePreferences.sClick("css=input[id$='VACATION_MSG_DISABLED_input']");
		
		// Click "Save"
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);
		
		// Wait for the ModifyPrefsRequest to complete
		app.zPagePreferences.zWaitForBusyOverlay();
		
		
		/* Test verification */
		
		app.zGetActiveAccount().soapSend(
					"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
				+		"<pref name='zimbraPrefOutOfOfficeReplyEnabled'/>"
				+		"<pref name='zimbraPrefOutOfOfficeReply'/>"
				+		"<pref name='zimbraPrefOutOfOfficeStatusAlertOnLogin'/>"
				+	"</GetPrefsRequest>");

		String zimbraPrefOutOfOfficeReplyEnabled = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefOutOfOfficeReplyEnabled']", null);
		String zimbraPrefOutOfOfficeReply = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefOutOfOfficeReply']", null);
		String zimbraPrefOutOfOfficeStatusAlertOnLogin = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefOutOfOfficeStatusAlertOnLogin']", null);
		
		// zimbraPrefOutOfOfficeReplyEnabled should be FALSE, but
		// all other properties should not be cleared (in case 
		// re-enabled in the future)
		//
		ZAssert.assertEquals(zimbraPrefOutOfOfficeReplyEnabled, "FALSE", "Verify zimbraPrefOutOfOfficeReplyEnabled is FALSE");
		ZAssert.assertEquals(zimbraPrefOutOfOfficeReply, Message, "Verify zimbraPrefOutOfOfficeReply contains the message");
		ZAssert.assertEquals(zimbraPrefOutOfOfficeStatusAlertOnLogin, "TRUE", "Verify zimbraPrefOutOfOfficeStatusAlertOnLogin is TRUE");
		
		
	}

}
