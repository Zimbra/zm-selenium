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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.mountpoints.manager;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.*;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;

public class ForwardMail extends SetGroupMailByMessagePreference {

	public ForwardMail() {
		logger.info("New "+ ForwardMail.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
	}


	@Test (description = "Forward (on behalf of) to a message in a shared folder (admin rights)",
			groups = { "functional", "L2" })

	public void ForwardMail_01() throws HarnessException {

		// Create the folder owner
		ZimbraAccount owner = (new ZimbraAccount()).provision().authenticate();

		// Allow sending rights
		owner.soapSend(
				"<GrantRightsRequest xmlns='urn:zimbraAccount'>"
			+		"<ace gt='usr' d='"+ app.zGetActiveAccount().EmailAddress +"' right='sendOnBehalfOf'/>"
			+	"</GrantRightsRequest>");

		String foldername = "folder" + ConfigProperties.getUniqueString();
		String subject = "subject" + ConfigProperties.getUniqueString();
		String mountpointname = "mountpoint" + ConfigProperties.getUniqueString();

		FolderItem inbox = FolderItem.importFromSOAP(owner, FolderItem.SystemFolder.Inbox);

		// Create a folder to share
		owner.soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + foldername + "' l='" + inbox.getId() + "'/>"
				+	"</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(owner, foldername);

		// Share it
		owner.soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidx'/>"
				+		"</action>"
				+	"</FolderActionRequest>");

		// Add a message to it
		owner.soapSend(
					"<AddMsgRequest xmlns='urn:zimbraMail'>"
        		+		"<m l='"+ folder.getId() +"' >"
            	+			"<content>From: "+ ZimbraAccount.AccountB().EmailAddress +"\n"
            	+				"To: "+ owner.EmailAddress +"\n"
            	+				"Subject: "+ subject +"\n"
            	+				"MIME-Version: 1.0 \n"
            	+				"Content-Type: text/plain; charset=utf-8 \n"
            	+				"Content-Transfer-Encoding: 7bit\n"
            	+				"\n"
            	+				"simple text string in the body\n"
            	+			"</content>"
            	+		"</m>"
				+	"</AddMsgRequest>");

		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountpointname +"'  rid='"+ folder.getId() +"' zid='"+ owner.ZimbraId +"'/>"
				+	"</CreateMountpointRequest>");

		FolderMountpointItem mountpoint = FolderMountpointItem.importFromSOAP(app.zGetActiveAccount(), mountpointname);

		// Refresh UI
		app.zPageMain.zRefreshMainUI();

		try {

			// Click on the mountpoint
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, mountpoint);

			// Select the item
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

			// Reply the item
			FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_FORWARD);
			mailform.zFillField(Field.From, owner.EmailAddress);
			mailform.zFillField(FormMailNew.Field.To, ZimbraAccount.AccountA().EmailAddress);
			mailform.zSubmit();

		} finally {

		}

		// From the receiving end, verify the message details
		MailItem sent = MailItem.importFromSOAP(owner, "in:sent subject:("+ subject +")");

		ZAssert.assertEquals(sent.dToRecipients.size(), 1, "Verify the message is sent to 1 'to' recipient");
		ZAssert.assertEquals(sent.dToRecipients.get(0).dEmailAddress, ZimbraAccount.AccountA().EmailAddress, "Verify the 'To' field is correct");
		ZAssert.assertEquals(sent.dFromRecipient.dEmailAddress, owner.EmailAddress, "Verify the 'From' field is correct");
		ZAssert.assertEquals(sent.dSenderRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress, "Verify the 'Sender' field is correct");
	}


	@Bugs (ids = "102475")
	@Test (description = "Forward (on behalf of) to a message in a shared folder (admin rights)  - no SOBO rights",
			groups = { "functional", "L2" })

	public void ForwardMail_02() throws HarnessException {

		// Create the folder owner
		ZimbraAccount owner = (new ZimbraAccount()).provision().authenticate();

		String foldername = "folder" + ConfigProperties.getUniqueString();
		String subject = "subject" + ConfigProperties.getUniqueString();
		String mountpointname = "mountpoint" + ConfigProperties.getUniqueString();

		FolderItem inbox = FolderItem.importFromSOAP(owner, FolderItem.SystemFolder.Inbox);

		// Create a folder to share
		owner.soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + foldername + "' l='" + inbox.getId() + "'/>"
				+	"</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(owner, foldername);

		// Share it
		owner.soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidx'/>"
				+		"</action>"
				+	"</FolderActionRequest>");

		// Add a message to it
		owner.soapSend(
					"<AddMsgRequest xmlns='urn:zimbraMail'>"
        		+		"<m l='"+ folder.getId() +"' >"
            	+			"<content>From: "+ ZimbraAccount.AccountB().EmailAddress +"\n"
            	+				"To: "+ owner.EmailAddress +"\n"
            	+				"Subject: "+ subject +"\n"
            	+				"MIME-Version: 1.0 \n"
            	+				"Content-Type: text/plain; charset=utf-8 \n"
            	+				"Content-Transfer-Encoding: 7bit\n"
            	+				"\n"
            	+				"simple text string in the body\n"
            	+			"</content>"
            	+		"</m>"
				+	"</AddMsgRequest>");

		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountpointname +"'  rid='"+ folder.getId() +"' zid='"+ owner.ZimbraId +"'/>"
				+	"</CreateMountpointRequest>");

		FolderMountpointItem mountpoint = FolderMountpointItem.importFromSOAP(app.zGetActiveAccount(), mountpointname);

		// Refresh UI
		app.zPageMain.zRefreshMainUI();

		try {

			// Click on the mountpoint
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, mountpoint);

			// Select the item
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

			// Reply the item
			FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_FORWARD);
			//mailform.zFillField(Field.From, owner.EmailAddress);
			mailform.zFillField(FormMailNew.Field.To, ZimbraAccount.AccountA().EmailAddress);
			mailform.zSubmit();

		} finally {

		}

		// From the receiving end, verify the message details
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ subject +")");

		ZAssert.assertEquals(sent.dToRecipients.size(), 1, "Verify the message is sent to 1 'to' recipient");
		ZAssert.assertEquals(sent.dToRecipients.get(0).dEmailAddress, ZimbraAccount.AccountA().EmailAddress, "Verify the 'To' field is correct");
		ZAssert.assertEquals(sent.dFromRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress, "Verify the 'From' field is correct");
		ZAssert.assertNull(sent.dSenderRecipient, "Verify the 'Sender' field is empty");
	}
}