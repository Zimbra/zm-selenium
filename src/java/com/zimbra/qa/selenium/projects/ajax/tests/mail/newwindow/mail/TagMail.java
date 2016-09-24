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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.mail;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowDisplayMail;

public class TagMail extends PrefGroupMailByMessageTest {

	public TagMail() {
		logger.info("New " + TagMail.class.getCanonicalName());


	}

	@Bugs(ids = "99519")
	@Test( description = "Tag a message using Toolbar -> Tag -> Existing Tag - in a separate window", 
			groups = { "functional" })
	public void TagMail_01() throws HarnessException {

		// Create the tag to delete
		String tagname = "tag" + ConfigProperties.getUniqueString();
		
		app.zGetActiveAccount().soapSend(
					"<CreateTagRequest xmlns='urn:zimbraMail'>"
				+	  	"<tag name='"+ tagname +"' color='1' />"
				+	"</CreateTagRequest>");

		TagItem tag = TagItem.importFromSOAP(app.zGetActiveAccount(), tagname);
		ZAssert.assertNotNull(tag, "Verify the tag was created");

		
		String subject = "subject" + ConfigProperties.getUniqueString();

		// Add a message to the mailbox
		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		app.zGetActiveAccount().soapSend(
					"<AddMsgRequest xmlns='urn:zimbraMail'>"
				+		"<m l='" + inboxFolder.getId() + "'>"
				+			"<content>"
				+				"From: foo@foo.com\n"
				+				"To: foo@foo.com \n"
				+				"Subject: " + subject + "\n"
				+				"MIME-Version: 1.0 \n"
				+				"Content-Type: text/plain; charset=utf-8 \n"
				+				"Content-Transfer-Encoding: 7bit\n"
				+				"\n"
				+				"simple text string in the body\n" 
				+			"</content>"
				+		"</m>"
				+	"</AddMsgRequest>");

		// Get the message data from SOAP
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:(" + subject + ")");

		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: Compose";
		
		try {
			
			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);
			
			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();
			
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");
			
			window.zToolbarPressPulldown(Button.B_TAG, tagname);
			
		} finally {
			app.zPageMain.closeWindow(window, windowTitle, app);
		}

		// Make sure the tag was applied to the message
		app.zGetActiveAccount().soapSend(
						"<GetMsgRequest xmlns='urn:zimbraMail'>"
				+			"<m id='" + mail.getId() + "'/>"
				+		"</GetMsgRequest>");
		String mailTags = app.zGetActiveAccount().soapSelectValue("//mail:m", "t");

		ZAssert.assertEquals(mailTags, tag.getId(), "Verify the tag appears on the message");

	}

}
