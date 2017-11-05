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
package com.zimbra.qa.selenium.projects.universal.tests.mail.compose;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.FormMailNew;

public class ReplyFromSentFolderWithReplyToHeader extends PrefGroupMailByMessageTest {

	ZimbraAccount account1 = null;
	ZimbraAccount account2 = null;
	
	public ReplyFromSentFolderWithReplyToHeader() {
		logger.info("New "+ ReplyFromSentFolderWithReplyToHeader.class.getCanonicalName());
	}
	
	@Test( description = "Reply to all from the sent folder (alias in Reply-to header)",
			groups = { "functional", "L2" })
	
	public void ReplyFromSentFolderWithReplyToHeader_01() throws HarnessException {

		//-- DATA
		
		if ( account1 == null ) {
			account1 = (new ZimbraAccount()).provision().authenticate();
			account2 = (new ZimbraAccount()).provision().authenticate();
		}
		
		// Set an alias on the account
		
		//-- Data setup
		
		String aliasFromDisplay = "alias" + ConfigProperties.getUniqueString();
		String aliasEmailAddress = 
					aliasFromDisplay + 
					"@" +
					ConfigProperties.getStringProperty("testdomain", "testdomain.com");
		
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<AddAccountAliasRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ app.zGetActiveAccount().ZimbraId +"</id>"
			+		"<alias>"+ aliasEmailAddress +"</alias>"
			+	"</AddAccountAliasRequest>");
		
		// Modify the from address in the primary identity
		app.zGetActiveAccount().soapSend("<GetIdentitiesRequest xmlns='urn:zimbraAccount' />");
		String identity = app.zGetActiveAccount().soapSelectValue("//acct:identity", "id");
		
		app.zGetActiveAccount().soapSend(
				" <ModifyIdentityRequest  xmlns='urn:zimbraAccount'>"
			+		"<identity id='"+ identity +"'>"
			+			"<a name='zimbraPrefFromDisplay'>"+ aliasFromDisplay +"</a>"
			+			"<a name='zimbraPrefFromAddress'>"+ aliasEmailAddress +"</a>"
			+		"</identity>"
			+	"</ModifyIdentityRequest >");
		
		// Send a message from the account
		
		String subject = "subject"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ account1.EmailAddress +"'/>" +
						"<e t='c' a='"+ account2.EmailAddress +"'/>" +
						"<e t='r' a='"+ aliasEmailAddress +"' p='"+ aliasFromDisplay +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>content" + ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		//-- GUI
		
		// Refresh UI
		app.zPageMain.zRefreshMainUI();

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Click in sent
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Sent));
		
		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Reply the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLYALL);

		// Send the message
		mailform.zSubmit();

		//-- Verification
		
		// All sent messages should not have TO: include the test account
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>in:sent subject:("+ subject +")</query>"
			+	"</SearchRequest>");

		Element[] messages = app.zGetActiveAccount().soapSelectNodes("//mail:m");
		
		// Make sure there are m nodes
		ZAssert.assertEquals(messages.length, 2, "Verify 2 messages are found in the sent folder");
		
		// Iterate over the sent messages, make sure the test account is not in the To or CC list
		for (Element message : messages) {
			
			String id = message.getAttribute("id", null);
			
			ZAssert.assertNotNull(id, "Verify the sent message ID is not null");
			
			app.zGetActiveAccount().soapSend(
					"<GetMsgRequest xmlns='urn:zimbraMail' >"
				+		"<m id='"+ id +"'/>"
				+	"</GetMsgRequest>");

			Element[] elements = app.zGetActiveAccount().soapSelectNodes("//mail:e");
			
			for ( Element e : elements ) {

				String type = e.getAttribute("t", null);
				String address = e.getAttribute("a", null);
				
				// Check To (t='t') and Cc (t='c') that they don't contain the sender
				if ( "t".equals(type) || "c".equals(type) ) {
					
					ZAssert.assertNotEqual(address, app.zGetActiveAccount().EmailAddress, "Verify the sender is not included in To or Cc");
					ZAssert.assertNotEqual(address, aliasEmailAddress, "Verify the alias is not included in To or Cc");
					
				}

			}
			

		}
	}
	
	@Test( description = "Reply to all from the sent folder (primary address in Reply-to header)",
			groups = { "functional", "L2" })
	public void ReplyFromSentFolderWithReplyToHeader_02() throws HarnessException {

		//-- DATA
		
		if ( account1 == null ) {
			account1 = (new ZimbraAccount()).provision().authenticate();
			account2 = (new ZimbraAccount()).provision().authenticate();
		}

		// Set the primary address on the account
		
		//-- Data setup
		
		String replyToDisplay = "alias" + ConfigProperties.getUniqueString();
		
		
		// Modify the from address in the primary identity
		app.zGetActiveAccount().soapSend("<GetIdentitiesRequest xmlns='urn:zimbraAccount' />");
		String identity = app.zGetActiveAccount().soapSelectValue("//acct:identity", "id");
		
		app.zGetActiveAccount().soapSend(
				" <ModifyIdentityRequest  xmlns='urn:zimbraAccount'>"
			+		"<identity id='"+ identity +"'>"
			+			"<a name='zimbraPrefFromAddressType'>sendAs</a>"
			+			"<a name='zimbraPrefReplyToEnabled'>TRUE</a>"
			+			"<a name='zimbraPrefFromDisplay'>"+ replyToDisplay +"</a>"
			+			"<a name='zimbraPrefFromAddress'>"+ app.zGetActiveAccount().EmailAddress +"</a>"
			+		"</identity>"
			+	"</ModifyIdentityRequest >");
		
		

		// Send a message from the account
		
		String subject = "subject"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ account1.EmailAddress +"'/>" +
						"<e t='c' a='"+ account2.EmailAddress +"'/>" +
						"<e t='r' a='"+ app.zGetActiveAccount().EmailAddress +"' p='"+ replyToDisplay +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>content" + ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		//-- GUI
		
		// Refresh UI
		app.zPageMain.zRefreshMainUI();

		// Click in sent
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Sent));
		
		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Reply the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLYALL);

		// Send the message
		mailform.zSubmit();

		//-- Verification
		
		// All sent messages should not have TO: include the test account
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>in:sent subject:("+ subject +")</query>"
			+	"</SearchRequest>");

		Element[] messages = app.zGetActiveAccount().soapSelectNodes("//mail:m");
		
		// Make sure there are m nodes
		ZAssert.assertEquals(messages.length, 2, "Verify 2 messages are found in the sent folder");
		
		// Iterate over the sent messages, make sure the test account is not in the To or CC list
		for (Element message : messages) {
			
			String id = message.getAttribute("id", null);
			
			ZAssert.assertNotNull(id, "Verify the sent message ID is not null");
			
			app.zGetActiveAccount().soapSend(
					"<GetMsgRequest xmlns='urn:zimbraMail' >"
				+		"<m id='"+ id +"'/>"
				+	"</GetMsgRequest>");

			Element[] elements = app.zGetActiveAccount().soapSelectNodes("//mail:e");

			for ( Element e : elements ) {

				String type = e.getAttribute("t", null);
				String address = e.getAttribute("a", null);
				
				// Check To (t='t') and Cc (t='c') that they don't contain the sender
				if ( "t".equals(type) || "c".equals(type) ) {
					
					ZAssert.assertNotEqual(address, app.zGetActiveAccount().EmailAddress, "Verify the sender is not included in To or Cc");
					
				}

			}

		}

	}
	
}
