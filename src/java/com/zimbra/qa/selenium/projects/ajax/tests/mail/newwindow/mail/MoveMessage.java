package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.mail;

/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
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


import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.*;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DialogCreateFolder;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowDisplayMail;


public class MoveMessage extends PrefGroupMailByMessageTest {

	@AfterMethod( groups = { "always" } )
	public void afterMethod() throws HarnessException {
		logger.info("Checking for the Move Dialog ...");

		// Check if the "Move Dialog is still open
		DialogMove dialog = new DialogMove(app, ((AppAjaxClient) app).zPageMail);
		if (dialog.zIsActive()) {
			logger.warn(dialog.myPageName()
					+ " was still active.  Cancelling ...");
			dialog.zClickButton(Button.B_CANCEL);
		}

	}

	public MoveMessage() {
		logger.info("New " + MoveMessage.class.getCanonicalName());

	}



	@Test(	description = "Move a mail from New Window by using Move -> New folder",
			groups = { "functional1" })
	public void MoveMail_01() throws HarnessException {

		String subject = "subject"+ ZimbraSeleniumProperties.getUniqueString();
		String foldername = "folder"+ ZimbraSeleniumProperties.getUniqueString();

		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);


		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>content"+ ZimbraSeleniumProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");

		// Get the mail item for the new message
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");


		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);

		// Click the inbox
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inbox);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		SeparateWindowDisplayMail window = null;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(subject);
			window.zWaitForActive();		// Make sure the window is there

			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");

			// Click move
			DialogCreateFolder dialog = (DialogCreateFolder) app.zPageMail.zToolbarPressPulldown(Button.B_MOVE, Button.O_NEW_FOLDER);
			dialog.zEnterFolderName(foldername);
			dialog.zClickButton(Button.B_OK);

			// Get the new subfolder
			// i.e. Inbox/subfolder
			//



			window.zCloseWindow();
			window = null;

		} finally {

			// Make sure to close the window
			if ( window != null ) {
				window.zCloseWindow();
				window = null;
			}

		}


		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);
		ZAssert.assertNotNull(subfolder, "Verify the subfolder was created");


		// Get the message, make sure it is in the correct folder
		app.zGetActiveAccount().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" +
						"<m id='" + mail.getId() +"'/>" +
				"</GetMsgRequest>");
		String folderId = app.zGetActiveAccount().soapSelectValue("//mail:m", "l");

		ZAssert.assertEquals(folderId, subfolder.getId(), "Verify the subfolder ID that the message was moved into");

	}



	@Test(	description = "Move a mail from new window by selecting message, then clicking toolbar 'Move' button",
			groups = { "functional1" })
	public void MoveMail_02() throws HarnessException {

		String subject = "subject"+ ZimbraSeleniumProperties.getUniqueString();
		String foldername = "folder"+ ZimbraSeleniumProperties.getUniqueString();

		// Create a subfolder to move the message into
		// i.e. Inbox/subfolder
		//
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='" + foldername +"' l='"+ inbox.getId() +"'/>" +
				"</CreateFolderRequest>");
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>content"+ ZimbraSeleniumProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");

		// Get the mail item for the new message
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");


		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);
		SeparateWindowDisplayMail window = null;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(subject);
			window.zWaitForActive();		// Make sure the window is there

			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");

			// Click move -> subfolder
			app.zPageMail.zToolbarPressPulldown(Button.B_MOVE, subfolder);

			window.zCloseWindow();
			window = null;

		} finally {

			// Make sure to close the window
			if ( window != null ) {
				window.zCloseWindow();
				window = null;
			}

		}




		// Get the message, make sure it is in the correct folder
		app.zGetActiveAccount().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" +
						"<m id='" + mail.getId() +"'/>" +
				"</GetMsgRequest>");
		String folderId = app.zGetActiveAccount().soapSelectValue("//mail:m", "l");

		ZAssert.assertEquals(folderId, subfolder.getId(), "Verify the subfolder ID that the message was moved into");

	}

	@Test(	description = "Move a mail from new window by using 'move to trash' ",
			groups = { "functional1" })
	public void MoveMail_03() throws HarnessException {

		String subject = "subject"+ ZimbraSeleniumProperties.getUniqueString();
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);

		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>content"+ ZimbraSeleniumProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");

		// Get the mail item for the new message
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");


		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		SeparateWindowDisplayMail window = null;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(subject);
			window.zWaitForActive();		// Make sure the window is there

			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");

			// Click move -> subfolder
			app.zPageMail.zToolbarPressPulldown(Button.B_MOVE, trash);

			window.zCloseWindow();
			window = null;

		} finally {

			// Make sure to close the window
			if ( window != null ) {
				window.zCloseWindow();
				window = null;
			}

		}
		// Get the message, make sure it is in the correct folder
		app.zGetActiveAccount().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" +
						"<m id='" + mail.getId() +"'/>" +
				"</GetMsgRequest>");
		String folderId = app.zGetActiveAccount().soapSelectValue("//mail:m", "l");


		ZAssert.assertEquals(folderId, trash.getId(), "Verify the message was moved to the trash folder");

	}



}
