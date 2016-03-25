package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.attachment;

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


import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowDisplayMail;


public class EditAsNewWithAnAttachment extends PrefGroupMailByMessageTest {


	public EditAsNewWithAnAttachment() {

		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
		logger.info("New "+ EditAsNewWithAnAttachment.class.getCanonicalName());


	}

	@Test(	description = "Edit as New message >> add Attchment from new window ",
			groups = { "windows","functional" })
	public void EditAsNewFromNewWindow_01() throws HarnessException {

		String subject = "subject"+ ZimbraSeleniumProperties.getUniqueString();		
		FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);


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


		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Create file item
		final String fileName = "testtextfile.txt";
		final String filePath = ZimbraSeleniumProperties.getBaseDirectory()
				+ "\\data\\public\\other\\" + fileName;

		SeparateWindowDisplayMail window = null;
		
		MailItem mail = new MailItem();
		mail.dBodyHtml = "body"+ ZimbraSeleniumProperties.getUniqueString();
		
		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);
			window.zSetWindowTitle(subject);
			window.zWaitForActive();	// Make sure the window is there
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");			

			window.zToolbarPressPulldown(Button.B_ACTIONS, Button.O_EDIT_AS_NEW);
			window.zSetWindowTitle("Zimbra: Compose");
			window.zWaitForActive();
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");
			window.sSelectWindow("Zimbra: Compose");

			window.zPressButton(Button.B_ATTACH);

			//Add an attachment
			zUpload(filePath);

			//Type in body
			String	locator = "css=div[id^='zv__COMPOSE'] iframe[id$='_body_ifr']";

			window.zWaitForElementPresent(locator, "5000");
			window.sSelectFrame(locator);		
			window.sClick(locator);	

			//Note: Explicitly we have used both command to type in body area.
			window.sTypeNewWindow(locator, mail.dBodyHtml);
			window.zTypeFormattedText(locator, mail.dBodyHtml);

			SleepUtil.sleepSmall();			
			window.zToolbarPressButton(Button.B_SEND);			
			window.zSetWindowTitle(subject);
			window.zWaitForActive();
			window.zToolbarPressButton(Button.B_CLOSE);
			SleepUtil.sleepSmall();

			// Window is closed automatically by the client
			window = null;

		} finally {

			// Make sure to close the window
			if ( window != null ) {
				window.zCloseWindow();
				window = null;
			}

		}

		for (int i = 0; i < 30; i++) {

			app.zGetActiveAccount().soapSend(
					"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
							+ "<query>subject:(" + subject + ")</query>"
							+ "</SearchRequest>");
			com.zimbra.common.soap.Element node = ZimbraAccount.AccountA()
					.soapSelectNode("//mail:m", 1);
			if (node != null) {
				// found the message
				break;
			}

			SleepUtil.sleep(1000);

		}

		app.zGetActiveAccount().soapSend(
				"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
						+ "<query>subject:(" + subject + ")</query>"
						+ "</SearchRequest>");
		String id = app.zGetActiveAccount().soapSelectValue("//mail:m", "id");

		app.zGetActiveAccount().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + id
				+ "' html='1'/>" + "</GetMsgRequest>");

		String html = app.zGetActiveAccount().soapSelectValue("//mail:mp[@ct='text/html']//mail:content", null);
		ZAssert.assertStringContains(html, mail.dBodyHtml, "Verify the html content");

		// Verify UI for attachment
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		ZAssert.assertTrue(app.zPageMail.zVerifyAttachmentExistsInMail(fileName),"Verify attachment exists in the email");

	}

}



