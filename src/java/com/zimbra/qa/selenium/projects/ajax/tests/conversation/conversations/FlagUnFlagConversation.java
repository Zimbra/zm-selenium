/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.conversation.conversations;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;


public class FlagUnFlagConversation extends PrefGroupMailByConversationTest {

	public FlagUnFlagConversation() {
		logger.info("New "+ FlagUnFlagConversation.class.getCanonicalName());
		
		
		
	}
	
	@Test(	description = "Un-Flag a conversation by clicking flagged icon",
			groups = { "smoke" })
	public void FlagConversation_01() throws HarnessException {

		//-- DATA
		
		
		ConversationItem c = ConversationItem.createConversationItem(app.zGetActiveAccount());
		
		app.zGetActiveAccount().soapSend(
					"<ConvActionRequest xmlns='urn:zimbraMail'>"
				+		"<action op='flag' id='"+ c.getId() + "'/>"
				+	"</ConvActionRequest>"
				);

		
		//-- GUI
		
		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);
		
		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, c.getSubject());
		
		// Flag the item
		app.zPageMail.zListItem(Action.A_MAIL_UNFLAG, c.getSubject());
		


		//-- Verification
		

		// Each message in the conversation should now be flagged
		
		// Refresh the conversation
		c = ConversationItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ c.getSubject() +")");
		for (MailItem m : c.getMessageList()) {
			ZAssert.assertStringDoesNotContain(m.dFlags, "f", "Verify all messges in the conversation are not flagged");
		}

	}

	@Test(	description = "Un-Flag a conversation by using shortcut 'mf'",
			groups = { "functional" })
	public void FlagConversation_02() throws HarnessException {

		//-- DATA
		
		
		ConversationItem c = ConversationItem.createConversationItem(app.zGetActiveAccount());

		app.zGetActiveAccount().soapSend(
				"<ConvActionRequest xmlns='urn:zimbraMail'>"
			+		"<action op='flag' id='"+ c.getId() + "'/>"
			+	"</ConvActionRequest>"
			);

		//-- GUI
		
		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);
		
		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, c.getSubject());
		
		// Flag the item
		app.zPageMail.zKeyboardShortcut(Shortcut.S_MAIL_MARKFLAG);
		


		//-- Verification
		

		// Each message in the conversation should now be flagged
		
		// Refresh the conversation
		c = ConversationItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ c.getSubject() +")");
		for (MailItem m : c.getMessageList()) {
			ZAssert.assertStringDoesNotContain(m.dFlags, "f", "Verify all messges in the conversation are not flagged");
		}

	}



}
