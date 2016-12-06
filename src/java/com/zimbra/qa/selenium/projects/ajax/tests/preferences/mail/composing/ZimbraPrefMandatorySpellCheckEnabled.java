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

import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Locators;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.PagePreferences;


public class ZimbraPrefMandatorySpellCheckEnabled extends AjaxCommonTest {


	public ZimbraPrefMandatorySpellCheckEnabled() {

		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences.put("zimbraPrefMandatorySpellCheckEnabled", "FALSE");

	}

	@Test(
			description = "Verify mandatory spellcheck before sending a message",
			groups = { "functional", "L2" }
			)

	public void ZimbraPrefMandatorySpellCheckEnabled_01() throws HarnessException {

		// Navigate to preferences -> mail -> composing
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Mail);

		//Select the check box for Mandatory spell check 
		app.zPagePreferences.sClick(PagePreferences.Locators.zMandatorySpellCheck);

		// Click save
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		app.zGetActiveAccount().soapSend(
				"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
						+			"<pref name='zimbraPrefMandatorySpellCheckEnabled'/>"
						+		"</GetPrefsRequest>");

		//Verifying that the 'zimbraPrefMandatorySpellCheckEnabled' is set to TRUE
		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefMandatorySpellCheckEnabled']", null);
		ZAssert.assertEquals(value, "TRUE", "Verify that Mandatory spell check is enabled");		

		//Mail data
		String subject = "subject" + ConfigProperties.getUniqueString();
		String misspelledWord="addwordxy";

		// Open the new mail form
		FormMailNew mailform = null;		
		mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountB().EmailAddress);
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, misspelledWord);

		//Send the mail
		mailform.zToolbarPressButton(Button.B_SEND);		

		//Check that misspelling warning dialog is displayed
		ZAssert.assertTrue(mailform.sIsVisible(Locators.zSpellCheckWarningDialog), "Verify that misspelling warning dialog is displayed");

		//Verify the warning message
		ZAssert.assertStringContains(mailform.sGetText(Locators.zSpellCheckWarningDialogContent),"1 misspellings found. Do you want to correct them?","Verify that message on misspelling warning dialog");

		//Click on correct spelling button
		mailform.sClick(Locators.zCorrectSpellingBtn);

		//CLick on the misspelled word
		mailform.sClickAt(Locators.zMisspelledWordCss+":contains('"+misspelledWord+"')","0,0");

		//Click on Add  to add the word to spell check dictionary
		SleepUtil.sleepSmall();
		mailform.sClickAt(Locators.zAddMisspelledWord,"0,0");		

		//To go out of Spell check view
		mailform.zToolbarPressButton(Button.B_SPELL_CHECK);	

		//Send the mail
		mailform.zToolbarPressButton(Button.B_SEND);		

		//Check that misspelling warning dialog is not displayed again
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zSpellCheckWarningDialog), "Verify that misspelling warning dialog is displayed");
	}
}
