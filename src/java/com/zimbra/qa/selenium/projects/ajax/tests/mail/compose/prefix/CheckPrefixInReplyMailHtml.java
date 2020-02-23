/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.prefix;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Checkbox;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.XmlStringUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class CheckPrefixInReplyMailHtml extends SetGroupMailByMessagePreference {

	public CheckPrefixInReplyMailHtml() {
		logger.info("New "+ CheckPrefixInReplyMailHtml.class.getCanonicalName());
		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
	}

	@Bugs( ids = "45446" )
	@Test (description = "Reply to an html mail and verify the display of blue color prefix before the original message",
			groups = { "sanity" })

	public void CheckPrefixInReplyMailHtml_01() throws HarnessException {
		
		String subject = "subject"+ ConfigProperties.getUniqueString();
		String bodyText = "text" + ConfigProperties.getUniqueString();
		String bodyHTML = "text <strong>bold"+ ConfigProperties.getUniqueString() +"</strong> text";
		String contentHTML = XmlStringUtil.escapeXml(
				"<html>" +
					"<head></head>" +
					"<body>"+ bodyHTML +"</body>" +
				"</html>");
		
		// Navigate to preferences -> Mail -> Composing Messages
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Mail);
		
		// Check the check box: Use Prefix for Email Reply
		app.zPagePreferences.zCheckboxSet(Checkbox.C_PREFIX_EMAIL_REPLY, true);
		
		// Click save
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='multipart/alternative'>" +
								"<mp ct='text/plain'>" +
									"<content>"+ bodyText +"</content>" +
								"</mp>" +
								"<mp ct='text/html'>" +
									"<content>"+ contentHTML +"</content>" +
								"</mp>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// Get the mail item for the new message
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
		SleepUtil.sleepMedium();

		// Click on Inbox folder in the tree
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inbox);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message present in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// Reply to the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		
		// Verify blue color prefix before original message in body
		ZAssert.assertTrue(mailform.zVerifyPrefixInHtmlMailBody(), 
				"Verify the blue color prefix is displayed befor header and quoted text");
		
		// Send the message
		mailform.zSubmit();

		// From the receiving end, verify the message details
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "in:inbox subject:("+ mail.dSubject +")");

		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
		ZAssert.assertStringContains(received.dSubject, mail.dSubject, "Verify the subject field is correct");
		ZAssert.assertStringContains(received.dSubject, "Re", "Verify the subject field contains the 'Re' prefix");
		
		// Verify prefix in the replied mail
		FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(),FolderItem.SystemFolder.Sent);

		// Get the page source of Body
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
		app.zPageMail.zListItem(Action.A_LEFTCLICK, "Re: " + mail.dSubject);
		ZAssert.assertTrue(app.zPageMail.zVerifyPrefixInDisplayedMail(), "Verify that the prefix is displayed in the replied mail");		
	}
}