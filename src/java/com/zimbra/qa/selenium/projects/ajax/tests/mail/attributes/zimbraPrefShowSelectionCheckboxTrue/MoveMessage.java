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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.attributes.zimbraPrefShowSelectionCheckboxTrue;

import org.testng.annotations.*;
import com.zimbra.common.soap.*;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.*;

public class MoveMessage extends PrefGroupMailByMessageTest {

	@AfterMethod( groups = { "always" } )
	public void afterMethod() throws HarnessException {
		logger.info("Checking for the Move Dialog ...");

		// Check if the "Move Dialog is still open
		DialogMove dialog = new DialogMove(app, ((AppAjaxClient)app).zPageMail);
		if ( dialog.zIsActive() ) {
			logger.warn(dialog.myPageName() +" was still active.  Cancelling ...");
			dialog.zClickButton(Button.B_CANCEL);
		}
	}
	
	public MoveMessage() {
		logger.info("New "+ MoveMessage.class.getCanonicalName());		
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox", "TRUE");
		super.startingAccountPreferences.put("zimbraPrefItemsPerVirtualPage", "10");
	}
	

	@Test( description = "Move all mails by selecting 'select all', then clicking toolbar 'Move' button",
			groups = { "functional", "L2" })
	
	public void MoveMessage_01() throws HarnessException {

		//-- DATA
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		String subject = "subject"+ ConfigProperties.getUniqueString();
		
		// Create 5 emails in the inbox
		for (int i = 0; i < 5; i++) {
			
			// Send a message to the account
			app.zGetActiveAccount().soapSend(
					"<AddMsgRequest xmlns='urn:zimbraMail'>"
	        		+		"<m l='"+ inbox.getId() +"' >"
	            	+			"<content>From: foo@foo.com\n"
	            	+				"To: foo@foo.com \n"
	            	+				"Subject: "+ subject +" index"+ i +"\n"
	            	+				"MIME-Version: 1.0 \n"
	            	+				"Content-Type: text/plain; charset=utf-8 \n"
	            	+				"Content-Transfer-Encoding: 7bit\n"
	            	+				"\n"
	            	+				"simple text string in the body\n"
	            	+			"</content>"
	            	+		"</m>"
					+	"</AddMsgRequest>");
		}
		
		// Create a subfolder to move the message into
		// i.e. Inbox/subfolder

		String foldername = "folder"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='" + foldername +"' l='"+ inbox.getId() +"'/>" +
					"</CreateFolderRequest>");
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		SleepUtil.sleepMedium(); // Wait for sometime to deliver 5 mails

		//-- GUI
		
		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK,inbox);
		
		// Select all
		app.zPageMail.zToolbarPressButton(Button.B_SELECT_ALL);
				
		// Click move -> subfolder
		app.zPageMail.zToolbarPressPulldown(Button.B_MOVE, subfolder);

		//-- VERIFICATION
		
		// Verify no messages remain in the inbox
		app.zGetActiveAccount().soapSend(
                "<SearchRequest xmlns='urn:zimbraMail' types='message'>" +
                   "<query>in:inbox subject:("+ subject +")</query>" +
                "</SearchRequest>");
		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:m");
		ZAssert.assertEquals(nodes.length, 0, "Verify 0 messages remain in the inbox");
	}

	
	@Bugs(ids = "106905")
	@Test( description = "Move all mails by selecting 'shift-select all', then clicking toolbar 'Move' button",
			groups = { "functional", "L3" })
	
	public void MoveMessage_02() throws HarnessException {
		
		//-- DATA
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		String subject = "subject"+ ConfigProperties.getUniqueString();
		
		// Create 15 emails in the inbox
		for (int i = 0; i < 25; i++) {
			
			// Send a message to the account
			app.zGetActiveAccount().soapSend(
					"<AddMsgRequest xmlns='urn:zimbraMail'>"
	        		+		"<m l='"+ inbox.getId() +"' >"
	            	+			"<content>From: foo@foo.com\n"
	            	+				"To: foo@foo.com \n"
	            	+				"Subject: "+ subject +" index"+ i +"\n"
	            	+				"MIME-Version: 1.0 \n"
	            	+				"Content-Type: text/plain; charset=utf-8 \n"
	            	+				"Content-Transfer-Encoding: 7bit\n"
	            	+				"\n"
	            	+				"simple text string in the body\n"
	            	+			"</content>"
	            	+		"</m>"
					+	"</AddMsgRequest>");
			
		}
		
		// Create a subfolder to move the message into
		// i.e. Inbox/subfolder

		String foldername = "folder"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='" + foldername +"' l='"+ inbox.getId() +"'/>" +
					"</CreateFolderRequest>");
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		SleepUtil.sleepLong(); // Wait for sometime to deliver 25 mails

		//-- GUI

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK,inbox);
		
		// Select all
		app.zPageMail.zToolbarPressButton(Button.B_SHIFT_SELECT_ALL);
				
		// Click move -> subfolder
		app.zPageMail.zToolbarPressPulldown(Button.B_MOVE, subfolder);
		
		//-- VERIFICATION
		
		// Verify no messages remain in the inbox
		app.zGetActiveAccount().soapSend(
                "<SearchRequest xmlns='urn:zimbraMail' types='message'>" +
                   "<query>in:inbox subject:("+ subject +")</query>" +
                "</SearchRequest>");
		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:m");
		ZAssert.assertEquals(nodes.length, 0, "Verify 0 messages remain in the inbox");
	}
}