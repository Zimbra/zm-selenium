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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;

public class SendMailUsingKeyboardShortcuts extends SetGroupMailByMessagePreference {

	public SendMailUsingKeyboardShortcuts() {
		logger.info("New "+ SendMailUsingKeyboardShortcuts.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
		super.startingAccountPreferences.put("zimbraPrefUseKeyboardShortcuts", "FALSE");
	}

	@Bugs (ids = "76547")
	@Test (description = "Send a mail using Text editor - zimbraPrefUseKeyboardShortcuts = FALSE",
			groups = { "sanity" })

	public void SendMailUsingKeyboardShortcuts_01() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();


		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(FormMailNew.Field.Subject, subject);
		mailform.zFillField(FormMailNew.Field.Body, "body" + ConfigProperties.getUniqueString());

		mailform.sFocus(FormMailNew.Locators.zToField);
		mailform.sClick(FormMailNew.Locators.zToField);
		mailform.zWaitForBusyOverlay();
		mailform.zKeyboard.zTypeCharacters(ZimbraAccount.AccountA().EmailAddress);

		// Send the message
		mailform.zSubmit();

		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received by Account A");
	}
}