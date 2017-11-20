/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.main.session;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;

public class SessionTimeout extends SetGroupMailByMessagePreference {

	public SessionTimeout() {
		logger.info("New "+ SessionTimeout.class.getCanonicalName());

		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
		super.startingAccountPreferences.put("zimbraPrefAutoSaveDraftInterval", "90s");
		super.startingAccountPreferences.put("zimbraMailIdleSessionTimeout", "30s");
	}


	@Bugs (ids = "75133")
	@Test (description = "Verify content is saved when compose is interupted by zimbraMailIdleSessionTimeout",
			groups = { "smoke", "L1" })

	public void SessionTimeout_01() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Wait for the session timeout
		// User will automatically be logged out
		ZimbraAccount a = app.zGetActiveAccount();
		SleepUtil.sleep(60000);

		app.zPageLogin.zWaitForActive();
		app.zPageLogin.zLogin(a);

		// Confirm that the mailform is still visible
		ZAssert.assertFalse(mailform.zIsActive(), "Confirm that the mailform is no longer visible");

		MailItem draft = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
		ZAssert.assertNotNull(draft, "Verify the draft exists");

		ZAssert.assertEquals(draft.dSubject, subject, "Verify the subject field is correct");
		ZAssert.assertStringContains(draft.dBodyText, body, "Verify the body field is correct");
	}
}