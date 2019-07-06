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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.composing;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Locators;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.PagePreferences;

public class ShowComposeDirection extends AjaxCore {

	public ShowComposeDirection() {
		super.startingPage = app.zPageMail;
		super.startingAccountPreferences.put("zimbraPrefShowComposeDirection", "FALSE");
	}


	@Bugs (ids = "ZCS-3528")
	@Test (description = "Verify the presence and working of direction buttons in compose mail screen",
			groups = { "functional-application-bug" })

	public void ShowComposeDirection_01() throws HarnessException {

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);

		// Check that direction buttons are not present
		ZAssert.assertFalse(mailform.sIsElementPresent(Locators.zDirectionLeftButton),"Direction buttons are present even if they are not enabled in preference");
		ZAssert.assertFalse(mailform.sIsElementPresent(Locators.zDirectionRightButton),"Direction buttons are present even if they are not enabled in preference");

		// Cancel the compose
		mailform.zToolbarPressButton(Button.B_CANCEL);

		// Navigate to Preferences -> General
		startingPage = app.zPagePreferences;
		startingPage.zNavigateTo();

		// Select the check box for Mandatory spell check
		app.zPagePreferences.sClick(PagePreferences.Locators.zShowComposeDirection);

		// Click save
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		// Verification through SOAP
		app.zGetActiveAccount().soapSend(
				"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
						+			"<pref name='zimbraPrefShowComposeDirection'/>"
						+		"</GetPrefsRequest>");

		// Verifying that the 'zimbraPrefShowComposeDirection' is set to TRUE
		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefShowComposeDirection']", null);
		ZAssert.assertEquals(value, "TRUE", "Verify that Mandatory spell check is enabled");

		// Open the new mail form and enter the data
		mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);

		mailform.zFillField(Field.To,app.zGetActiveAccount().EmailAddress);
		mailform.zFillField(Field.Subject,"Check Direction buttons");
		mailform.zFillField(Field.Body, "Left to Right");

		// Check that direction buttons are present now
		ZAssert.assertTrue(mailform.sIsElementPresent(Locators.zDirectionLeftButton),"Direction buttons are not present even if they are enabled in preference");
		ZAssert.assertTrue(mailform.sIsElementPresent(Locators.zDirectionRightButton),"Direction buttons are not present even if they are enabled in preference");

		// Click on left direction button
		mailform.sClick(Locators.zDirectionLeftButton);

		// Check the present of body content under 'dir=ltr' i.e. left to right tag
		mailform.sSelectFrame(Locators.zBodyFrameCss);
		ZAssert.assertStringContains(mailform.sGetText("css=body[id='tinymce'] div[dir='ltr']"), "Left", "Verify text is present in body when direction is left to right" );
		ZAssert.assertFalse(mailform.sIsElementPresent("css=body[id='tinymce'] div[dir='rtl']"),"Verify no text is present in body when direction is left to right" );
		mailform.sSelectFrame("relative=top");

		// Click on right direction button
		mailform.sClick(Locators.zDirectionRightButton);

		// Check the present of body content under 'dir=rtl' i.e. right to left tag
		mailform.sSelectFrame(Locators.zBodyFrameCss);
		ZAssert.assertStringContains(mailform.sGetText("css=body[id='tinymce'] div[dir='rtl']"), "Left", "Verify text is present in body when direction is right to left" );
		ZAssert.assertFalse(mailform.sIsElementPresent("css=body[id='tinymce'] div[dir='ltr']"),"Verify no text is present in body when direction is right to left" );
		mailform.sSelectFrame("relative=top");

		// Cancel the compose
		mailform.zToolbarPressButton(Button.B_CANCEL);
	}
}