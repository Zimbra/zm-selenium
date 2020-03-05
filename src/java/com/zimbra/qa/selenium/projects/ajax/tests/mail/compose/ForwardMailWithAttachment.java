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
 package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class ForwardMailWithAttachment extends SetGroupMailByMessagePreference {

	public ForwardMailWithAttachment() {
		logger.info("New "+ ForwardMailWithAttachment.class.getCanonicalName());
		super.startingPage = app.zPagePreferences;
	}


	@Test (description = "Bug 102745 - 'Forward email with 'Include original message as attachment' option enabled ",
			groups = { "functional" })

	public void ForwardMailWithAttachment_01() throws HarnessException {

		// Set 'Include original message as an attachment' from preferences
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Mail);
		app.zPagePreferences.sClick("css=td[id='Prefs_Select_FORWARD_INCLUDE_WHAT_dropdown']"); // Select drop down for email forwarding menu
		// Select 'Include original message as attachment'
		app.zPagePreferences.sClickAt(("css=div[parentid='Prefs_Select_FORWARD_INCLUDE_WHAT_Menu_1'] td[id$='_title']:contains('Include original message as an attachment')"),"");
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		// Create message
		String subject = "subject"+ ConfigProperties.getUniqueString();

		ZimbraAccount.AccountB().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message present in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// Forward email
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_FORWARD);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.Account8().EmailAddress);

		// Send the message
		mailform.zSubmit();

		// Verification
		app.zPageLogin.zLogin(ZimbraAccount.Account8());
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// From the receiving end, verify the message details
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.Account8(), "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");

		// Verify the attachment exists in the forwarded mail
		final String AttachmentName = subject;

		ZimbraAccount.Account8().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>"
				+		"<m id='"+ received.getId() +"'/>"
				+	"</GetMsgRequest>");

		String filename = ZimbraAccount.Account8().soapSelectValue("//mail:mp[@cd='attachment']", "filename");
		ZAssert.assertEquals(filename, AttachmentName, "Verify the attachment exists in the forwarded mail");
	}
}