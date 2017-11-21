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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.drafts;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Locators;

public class ModifySavedDraftFormatAndContent extends SetGroupMailByMessagePreference {

	public ModifySavedDraftFormatAndContent() {
		logger.info("New " + OpenDraftMail.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
	}


	@Bugs (ids = "53373")
	@Test (description = "Modify compose html draft mail and verify its format",
			groups = { "functional", "L2" })

	public void ReopenAndCheckFormat_01() throws HarnessException {

		// Create the message data to be entered while composing mail
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Switch to html mode and verify it
		mailform.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_FORMAT_AS_HTML);
		SleepUtil.sleepSmall();
		ZAssert.assertTrue(mailform.sIsVisible(Locators.zBoldButton),"to verify html format check that any(bold) button is present");

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Save and close the message
		mailform.zToolbarPressButton(Button.B_SAVE_DRAFT);
		SleepUtil.sleepMedium();
		mailform.zToolbarPressButton(Button.B_CLOSE);

		// Go to draft
		FolderItem drafts = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Drafts);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, drafts);

		// Select the mail,Click Edit
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		mailform.zToolbarPressButton(Button.B_EDIT);
		SleepUtil.sleepMedium();

		// verify format
		ZAssert.assertTrue(mailform.sIsVisible(Locators.zBoldButton),"to verify mailform open in html format check that any(bold) button is present");
		// verify body content
		ZAssert.assertEquals(mailform.zGetHtmltBodyText(), body, "Verify mail content is equal");
	}
}