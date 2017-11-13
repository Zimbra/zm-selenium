/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.touch.tests.mail.compose;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.items.RecipientItem.RecipientType;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.TouchCommonTest;
import com.zimbra.qa.selenium.projects.touch.ui.mail.FormMailNew;

public class CancelCompose extends TouchCommonTest {

	public CancelCompose() {
		logger.info("New "+ CancelCompose.class.getCanonicalName());
		
	}
	
	@Test (description = "Compose message, specify subject, body and cancel it without saving as draft",
			groups = { "functional" })
			
	public void CancelCompose_DontSaveDraft_01() throws HarnessException {
		
		// Create the message data
		MailItem mail = new MailItem();
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyText = "body" + ConfigProperties.getUniqueString();
		
		// Open new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		mailform.zFill(mail);
		mailform.zToolbarPressButton(Button.B_CANCEL);
		mailform.zPressButton(Button.B_NO);
		
		// Get the message from server and verify draft is not present
		app.zGetActiveAccount().soapSend(
				"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
						+ "<query>subject:(" + mail.dSubject + ")</query>"
						+ "</SearchRequest>");
		String id = app.zGetActiveAccount().soapSelectValue("//mail:m", "id");
		ZAssert.assertNull(id, "Verify the draft folder id is null");
		
	}
	
	@Test (description = "Compose message, specify subject, body and cancel it by saving as draft",
			groups = { "functional" })
			
	public void CancelCompose_SaveDraft_02() throws HarnessException {
		
		// Create the message data
		MailItem mail = new MailItem();
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyText = "body" + ConfigProperties.getUniqueString();
		
		// Open new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		mailform.zFill(mail);
		mailform.zToolbarPressButton(Button.B_CANCEL);
		mailform.zPressButton(Button.B_YES);
		
		// Get the message from server and verify draft data matches
		app.zGetActiveAccount().soapSend(
				"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
						+ "<query>subject:(" + mail.dSubject + ")</query>"
						+ "</SearchRequest>");
		String id = app.zGetActiveAccount().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify the draft folder id is not null");
		
		// Get the message from server and verify draft data matches
		MailItem draft = MailItem.importFromSOAP(app.zGetActiveAccount(),
				"subject:("+ mail.dSubject +")");
		FolderItem draftsFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Drafts);
		
		ZAssert.assertEquals(draft.dSubject, mail.dSubject, "Verify the subject field is correct");
		ZAssert.assertEquals(draft.dBodyText, mail.dBodyText, "Verify the body field is correct");
		ZAssert.assertEquals(draft.dFolderId, draftsFolder.getId(), "Verify the draft is saved in the drafts folder");
		
	}
	
	@Bugs (ids = "83978")
	@Test (description = "Compose message, specify subject, body and send a mail after saving as draft",
			groups = { "obsolete" }) // obsolete testcase because UI has been changed (after saving draft, dialog is closed)
		
	public void CancelCompose_SendMail_03() throws HarnessException {
		
		// Create the message data
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA(), RecipientType.To));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyText = "body" + ConfigProperties.getUniqueString();
		
		// Try to cancel the compose and again send it
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		mailform.zFill(mail);
		mailform.zToolbarPressButton(Button.B_CANCEL);
		mailform.zPressButton(Button.B_YES);
		mailform.zSubmit();
		
		// Get the message from server and verify draft data matches
		app.zGetActiveAccount().soapSend(
				"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
						+ "<query>" + "in:drafts " + "subject:(" + mail.dSubject + ")</query>"
						+ "</SearchRequest>");
		String id = app.zGetActiveAccount().soapSelectValue("//mail:m", "id");
		ZAssert.assertNull(id, "Verify the draft folder id is null");
			
		// Verify received mail to To: user
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
						+ "<query>subject:(" + mail.dSubject + ")</query>"
						+ "</SearchRequest>");
		String toid = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		
		ZimbraAccount.AccountA().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + toid
						+ "' html='1'/>" + "</GetMsgRequest>");

		String tofrom = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='f']", "a");
		String toto = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='t']", "a");
		String tosubject = ZimbraAccount.AccountA().soapSelectValue("//mail:su", null);
		String tobody = ZimbraAccount.AccountA().soapSelectValue("//mail:content", null);
		
		ZAssert.assertEquals(tofrom, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(toto, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(tosubject, mail.dSubject, "Verify the subject field is correct");
		ZAssert.assertStringContains(tobody, mail.dBodyText, "Verify the body content");
		
	}
	
	@Test (description = "Compose message, specify subject, body and send a mail without saving as draft",
			groups = { "functional" })
			
	public void CancelCompose_SendMail_04() throws HarnessException {
		
		// Create the message data
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA(), RecipientType.To));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyText = "body" + ConfigProperties.getUniqueString();
		
		// Try to cancel the compose and again send it
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		mailform.zFill(mail);
		mailform.zToolbarPressButton(Button.B_CANCEL);
		mailform.zPressButton(Button.B_CANCEL);
		mailform.zSubmit();
		
		// Get the message from server and verify draft data matches
		app.zGetActiveAccount().soapSend(
				"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
						+ "<query>" + "in:drafts " + "subject:(" + mail.dSubject + ")</query>"
						+ "</SearchRequest>");
		String id = app.zGetActiveAccount().soapSelectValue("//mail:m", "id");
		ZAssert.assertNull(id, "Verify the draft folder id is null");

		// Verify received mail to To: user
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
						+ "<query>subject:(" + mail.dSubject + ")</query>"
						+ "</SearchRequest>");
		String toid = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		
		ZimbraAccount.AccountA().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + toid
						+ "' html='1'/>" + "</GetMsgRequest>");

		String tofrom = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='f']", "a");
		String toto = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='t']", "a");
		String tosubject = ZimbraAccount.AccountA().soapSelectValue("//mail:su", null);
		String tobody = ZimbraAccount.AccountA().soapSelectValue("//mail:content", null);
		
		ZAssert.assertEquals(tofrom, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(toto, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(tosubject, mail.dSubject, "Verify the subject field is correct");
		ZAssert.assertStringContains(tobody, mail.dBodyText, "Verify the body content");
		
	}
	
}
