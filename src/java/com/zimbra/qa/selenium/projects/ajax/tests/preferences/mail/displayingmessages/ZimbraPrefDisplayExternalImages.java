/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.displayingmessages;

import java.io.File;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.LmtpInject;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.PageMail.Locators;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.PagePreferences;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;


public class ZimbraPrefDisplayExternalImages extends AjaxCommonTest {


	public ZimbraPrefDisplayExternalImages() {

		super.startingPage = app.zPageMail;
		super.startingAccountPreferences.put("zimbraPrefDisplayExternalImages", "FALSE");
	}
	
	@Test(
			description = "Verify the Automatic display of external image when 'zimbraPrefDisplayExternalImages' is Set TRUE",
			groups = { "functional", "L2" }
			)

	public void ZimbraPrefDisplayExternalImages_01() throws HarnessException {


		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email18/AutomaticExternalImageDisplayMime.txt";
		final String subject = "Display external image preference check";		

		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mimeFile));

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Verify Warning info bar with other links
		ZAssert.assertTrue(app.zPageMail.zHasWDDLinks(), "Verify display images link");

		//Navigate to Preferences -> General
		startingPage=app.zPagePreferences;
		startingPage.zNavigateTo();

		// Navigate to preferences -> mail
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Mail);

		//Select the check box for Automatic display of External images 
		app.zPagePreferences.sClick(PagePreferences.Locators.zDisplayExternalImage);

		// Click save
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		//Verification through SOAP
		app.zGetActiveAccount().soapSend(
				"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
						+			"<pref name='zimbraPrefDisplayExternalImages'/>"
						+		"</GetPrefsRequest>");

		//Verifying that the 'zimbraPrefDisplayExternalImages' is set to TRUE
		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefDisplayExternalImages']", null);
		ZAssert.assertEquals(value, "TRUE", "Verify that external image display is enabled");

		//Verification of External image display when preference is set TRUE

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Verify Warning info bar with other links is not present
		ZAssert.assertFalse(app.zPageMail.zHasWDDLinks(), "Verify display images link");
		
		//Verify the external image is displayed
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(Locators.zMsgExternalImage), "Verify the external is displayed");
		
	}
}
