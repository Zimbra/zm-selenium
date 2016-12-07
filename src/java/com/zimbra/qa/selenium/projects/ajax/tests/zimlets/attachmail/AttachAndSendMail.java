/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.attachmail;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.AppAjaxClient;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogAttach;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.*;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.PageMail.Locators;

public class AttachAndSendMail extends PrefGroupMailByMessageTest {

	public AttachAndSendMail() {
		logger.info("New "+ AttachAndSendMail.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
	}

	@Test( description = "Attach an email to a mail",
			groups = { "functional", "L2" })
	
	public void AttachAndSendMail_01() throws HarnessException {

		//-- DATA

		// Create an email to attach
		final FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		final String subject = "attached"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
			"<AddMsgRequest xmlns='urn:zimbraMail'>"
					+		"<m l='"+ inbox.getId() +"' >"
					+			"<content>From: foo@foo.com\n"
					+				"To: foo@foo.com \n"
					+				"Subject: "+ subject +"\n"
					+				"MIME-Version: 1.0 \n"
					+				"Content-Type: text/plain; charset=utf-8 \n"
					+				"Content-Transfer-Encoding: 7bit\n"
					+				"\n"
					+				"simple text string in the body\n"
					+			"</content>"
					+		"</m>"
					+	"</AddMsgRequest>");

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyText = "body" + ConfigProperties.getUniqueString();

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		mailform.zFill(mail);
		app.zPageMail.zToolbarPressPulldown(Button.B_ATTACH, Button.O_MAILATTACH);

		DialogAttach dialog = new DialogAttach(app, ((AppAjaxClient) app).zPageMail);
		ZAssert.assertTrue(dialog.zIsActive(), "Attach Mail dialog gets open and active");

		// Click on Inbox folder
		dialog.zClick(Locators.zAttachInboxFolder);
		SleepUtil.sleepMedium();
		dialog.sClick(
				"css=div[id='zv__BCI'] tr[id^='zlif__BCI__'] div[class='AttachMailRowDiv'] span[class='AttachMailSubject']");
		dialog.zClickButton(Button.B_ATTACH);
		SleepUtil.sleepMedium();
		mailform.zSubmit();

		// From the receiving end, verify the message details
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:(" + mail.dSubject + ")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
		ZimbraAccount.AccountA().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + received.getId() + "'/>" + "</GetMsgRequest>");

		String filename = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@cd='attachment']", "filename");

		ZAssert.assertEquals(filename, subject, "Verify the attached mail exist");

	}

}
