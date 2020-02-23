/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Smail listte Server
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;

public class CheckMailDetailsInMailList extends SetGroupMailByMessagePreference {

	public CheckMailDetailsInMailList() {
		logger.info("New "+ CheckMailDetailsInMailList.class.getCanonicalName());
	}


	@Test (description = "Verify mail details displayed in inbox mail list",
			groups = { "bhr" })

	public void CheckMailDetailsInMailList_01() throws HarnessException {

		// Create the message data to be sent
		String subject = "This is the subject of the mail " + ConfigProperties.getUniqueString();
		String body = "This is the body of the mail " + ConfigProperties.getUniqueString();
		
		// Send a high priority message from AccountA to the ZCS user
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<f>!</f>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<e t='c' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>" + body + "</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");
		
		// Time of mail delivery
		SimpleDateFormat formatDate = new SimpleDateFormat("h:mm a");
		String time = formatDate.format(Calendar.getInstance().getTime());

		// Refresh current view and check for the mail
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message present in current view");
		
		// Change the reading pane to bottom
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_READING_PANE_BOTTOM);
		
		// Verify the mail details displayed in mail list
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "flag"), "false", "Verify the message is shown as unflagged in the mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "highPriority"), "true", "Verify the message is shown as high priority message in the mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "tag"), "false", "Verify the message is shown as untagged in the mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "readStatus"), "false", "Verify the message is shown as unread in the mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "msgReadStatus"), "false", "Verify the message is shown as unread in the mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "from"), ZimbraAccount.AccountA().DisplayName, "Verify that from address in message is shown as correctly in the mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "attachment"), "false", "Verify that attachment synbol is not shown in mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "subject"), subject, "Verify that subject is shown correctly in the mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "folder"), "Inbox", "Verify that folder name is displayed correctly in the mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "size"), "2 KB", "Verify that size of the message is shown correctly in the mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "time"), time, "Verify that message delivery time is displayed correctly in the mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "mailBody"), body, "Verify that the message body is shown as correctly in the mail list");
	}
	
	@Test (description = "Verify mail details displayed in sent mail list",
			groups = { "bhr" })

	public void CheckMailDetailsInMailList_02() throws HarnessException {

		// Create the message data to be sent
		String subject = "Subject of mail in sent mail list " + ConfigProperties.getUniqueString();
		String body = "Body of mail in sent mail list " + ConfigProperties.getUniqueString();
		
		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.Account10().EmailAddress);
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);
		mailform.zToolbarPressPulldown(Button.B_PRIORITY, Button.O_PRIORITY_HIGH);
		mailform.zSubmit();
		
		// Verify that mail is sent correctly
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.Account10(), "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received");
		
		// Go to sent folder
		FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
		
		// Time of mail delivery
		SimpleDateFormat formatDate = new SimpleDateFormat("h:mm a");
		String time = formatDate.format(Calendar.getInstance().getTime());
		
		// Change the reading pane to bottom
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_READING_PANE_BOTTOM);
		
		// Verify the mail details displayed in mail list
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "flag"), "false", "Verify the message is shown as unflagged in the mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "highPriority"), "true", "Verify the message is shown as high priority message in the mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "tag"), "false", "Verify the message is shown as untagged in the mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "readStatus"), "true", "Verify the message is shown as unread in the mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "msgSentStatus"), "true", "Verify the message is shown as unread in the mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "to"), ZimbraAccount.Account10().DisplayName, "Verify that from address in message is shown as correctly in the mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "attachment"), "false", "Verify that attachment synbol is not shown in mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "subject"), subject, "Verify that subject is shown correctly in the mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "folder"), "Sent", "Verify that folder name is displayed correctly in the mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "size"), "1 KB", "Verify that size of the message is shown correctly in the mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "time"), time, "Verify that message delivery time is displayed correctly in the mail list");
		ZAssert.assertStringContains(app.zPageMail.zGetMessageProperty(subject, "mailBody"), body, "Verify that the message body is shown as correctly in the mail list");
	}
}