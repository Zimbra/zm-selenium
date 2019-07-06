/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.mail.headers;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.PageMail.Locators;

public class CheckEmailAddressBubble extends SetGroupMailByMessagePreference {

	public CheckEmailAddressBubble() throws HarnessException {
		logger.info("New "+ CheckEmailAddressBubble.class.getCanonicalName());
		super.startingPage = app.zPageMail;
		super.startingAccountPreferences.put("zimbraPrefGroupMailBy", "message");		
	}


	@Bugs (ids = "57895" )
	@Test (description = "Verify that email address containing '-', '_' etc. are also displayed in address bubble",
			groups = { "sanity" })

	public void CheckEmailAddressBubble_01() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/unrecognizedEmailAddress.txt";
		final String subject = "Email address bubble check";
		
		// Some email addresses present in the mime
		final String email1 = "ay__@msn.com";
		final String email2 = "cilgin.kiz.35@hotmail.com";
		final String email3 = "deniz-sirtikkizil@hotmail.com";
		final String email4 = "cettia@cettia.com";
		final String email5 = "cenkkaya@residencehotel.com.tr";
		final String email6 = "cizgi1963@hotmail.com";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Verify that email address are are recognized and displayed in bubble in message view
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(Locators.zAddressBubbleInHeaderCss + ":contains('"+ email1 +"')"),
				"Verify that " + email1 + "is displayed in address bubble");
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(Locators.zAddressBubbleInHeaderCss + ":contains('"+ email2 +"')"),
				"Verify that " + email2 + "is displayed in address bubble");
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(Locators.zAddressBubbleInHeaderCss + ":contains('"+ email3 +"')"),
				"Verify that " + email3 + "is displayed in address bubble");
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(Locators.zAddressBubbleInHeaderCss + ":contains('"+ email4 +"')"),
				"Verify that " + email4 + "is displayed in address bubble");
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(Locators.zAddressBubbleInHeaderCss + ":contains('"+ email5 +"')"),
				"Verify that " + email5 + "is displayed in address bubble");
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(Locators.zAddressBubbleInHeaderCss + ":contains('"+ email6 +"')"),
				"Verify that " + email6 + "is displayed in address bubble");
		
		// Refresh the UI and Select Conversation view
		app.zPageMail.zRefreshUI();
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_BY_CONVERSATION);
		
		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Verify that email address are are recognized and displayed in bubble in conversation view
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(Locators.zAddressBubbleInHeaderCss + ":contains('"+ email1 +"')"),
				"Verify that " + email1 + "is displayed in address bubble");
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(Locators.zAddressBubbleInHeaderCss + ":contains('"+ email2 +"')"),
				"Verify that " + email2 + "is displayed in address bubble");
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(Locators.zAddressBubbleInHeaderCss + ":contains('"+ email3 +"')"),
				"Verify that " + email3 + "is displayed in address bubble");
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(Locators.zAddressBubbleInHeaderCss + ":contains('"+ email4 +"')"),
				"Verify that " + email4 + "is displayed in address bubble");
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(Locators.zAddressBubbleInHeaderCss + ":contains('"+ email5 +"')"),
				"Verify that " + email5 + "is displayed in address bubble");
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(Locators.zAddressBubbleInHeaderCss + ":contains('"+ email6 +"')"),
				"Verify that " + email6 + "is displayed in address bubble");
		
	}
}