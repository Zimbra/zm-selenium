/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.mail;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail.Field;

public class OpenMail extends SetGroupMailByMessagePreference {

	public OpenMail() {
		logger.info("New "+ OpenMail.class.getCanonicalName());
	}


	@Test (description = "Double click a mail",
			groups = { "bhr" })

	public void OpenMail_01() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();

		// Send the message from AccountA to the ZCS user
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<e t='c' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ "body" + ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message present in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// Double click the message so that it opens
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_DOUBLECLICK, mail.dSubject);

		// Verify the To, From, Subject, Body
		ZAssert.assertEquals(actual.zGetMailPropertyDisplayedInSeparateTab(Field.Subject), mail.dSubject, "Verify the subject matches");
		ZAssert.assertNotNull(actual.zGetMailPropertyDisplayedInSeparateTab(Field.ReceivedDate), "Verify the date is displayed");
		ZAssert.assertNotNull(actual.zGetMailPropertyDisplayedInSeparateTab(Field.ReceivedTime), "Verify the time is displayed");
		ZAssert.assertEquals(actual.zGetMailPropertyDisplayedInSeparateTab(Field.From), ZimbraAccount.AccountA().EmailAddress, "Verify the From matches");
		ZAssert.assertEquals(actual.zGetMailPropertyDisplayedInSeparateTab(Field.Cc), ZimbraAccount.AccountB().EmailAddress, "Verify the Cc matches");
		ZAssert.assertEquals(actual.zGetMailPropertyDisplayedInSeparateTab(Field.To), app.zGetActiveAccount().EmailAddress, "Verify the To matches");
		ZAssert.assertStringContains(actual.zGetMailPropertyDisplayedInSeparateTab(Field.Body), mail.dBodyText, "Verify the body matches");
	}
}