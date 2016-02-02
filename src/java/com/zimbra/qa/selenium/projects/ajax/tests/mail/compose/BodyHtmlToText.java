package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose;

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

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;

public class BodyHtmlToText extends PrefGroupMailByMessageTest {

	public BodyHtmlToText() {
		logger.info("New " + BodyHtmlToText.class.getCanonicalName());

		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");

	}

	@Test(description = "Compose a meeting with html body, change to Plain Text and verify if text is not lostr", groups = { "smoke" })
	public void BodyHtmltoText_01() throws HarnessException {

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
		mail.dSubject = "subject" + ZimbraSeleniumProperties.getUniqueString();
		mail.dBodyHtml = "body" + ZimbraSeleniumProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFill(mail);

		// change html to Plain format.

		DialogWarning dialog = (DialogWarning) mailform.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_FORMAT_AS_PLAIN_TEXT);
		dialog.zClickButton(Button.B_OK);
		SleepUtil.sleepMedium();

		// Verify Body contents remain same or does not lost.

		ZAssert.assertStringContains(mailform.zGetPlainBodyText(),mail.dBodyHtml, "Verify content is not lost");

	}

}
