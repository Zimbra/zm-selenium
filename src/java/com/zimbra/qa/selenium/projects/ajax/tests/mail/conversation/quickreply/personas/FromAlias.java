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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.conversation.quickreply.personas;

import java.util.List;

import org.testng.annotations.*;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.*;


public class FromAlias extends PrefGroupMailByConversationTest {

	public FromAlias() {
		logger.info("New "+ FromAlias.class.getCanonicalName());
		
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
		
	}
	
	@Bugs(ids = "73698")
	@Test( description = "Send a quick reply with alias as from",
			groups = { "functional", "L2" })
	public void FromAlias_01() throws HarnessException {
		
		
		//-- Data setup
		
		String aliasFromDisplay = "alias" + ConfigProperties.getUniqueString();
		String aliasEmailAddress = aliasFromDisplay + 
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
		
		// Send a message to the account to create the conversation
		ZimbraAccount account1 = new ZimbraAccount();
		account1.provision();
		account1.authenticate();
		
		
		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String content = "content" + ConfigProperties.getUniqueString();
		String reply = "quickreply" + ConfigProperties.getUniqueString();
		
		account1.soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ content +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		
		
		//-- GUI steps
		
		
		// Refresh UI
		app.zPageMain.sRefresh();

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");
		
		// Select the conversation
		DisplayConversation display = (DisplayConversation)app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Get the first mesage
		List<DisplayConversationMessage> messages = display.zListGetMessages();
				
		// Quick Reply
		messages.get(0).zPressButton(Button.B_QUICK_REPLY_REPLY);
		messages.get(0).zFillField(DisplayMail.Field.Body, reply);
		messages.get(0).zPressButton(Button.B_QUICK_REPLY_SEND);
	

		
		//-- Verification
		
		
		// Verify the message shows as from the alias
		account1.soapSend(
					"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
			+			"<query>subject:("+ subject +")</query>"
			+		"</SearchRequest>");
		String id = account1.soapSelectValue("//mail:m", "id");

		account1.soapSend(
					"<GetMsgRequest xmlns='urn:zimbraMail'>"
			+			"<m id='"+ id +"' html='1'/>"
			+		"</GetMsgRequest>");

		// Verify From: alias
		String address = account1.soapSelectValue("//mail:e[@t='f']", "a");
		ZAssert.assertEquals(address, aliasEmailAddress, "Verify the from is the alias email address");
		
		// Verify no headers contain active account
		Element[] nodes = account1.soapSelectNodes("//mail:e");
		for (Element e : nodes) {
			String attr = e.getAttribute("a", null);
			if ( attr != null ) {
				ZAssert.assertStringDoesNotContain(
						attr, 
						app.zGetActiveAccount().EmailAddress, 
						"Verify no headers contain the active account email address");
			}
		}

	}


}
