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
package com.zimbra.qa.selenium.projects.touch.tests.mail.mountpoints.manager;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.RecipientItem.RecipientType;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.touch.ui.PageCreateFolder;
import com.zimbra.qa.selenium.projects.touch.ui.mail.FormMailNew;

public class ForwardMail extends PrefGroupMailByMessageTest{


	public ForwardMail() {
		logger.info("New " + FlagMail.class.getCanonicalName());
	}

	@Test( description = "Forward a email on mount point with manager rights",
			groups = { "functional" })

	public void ForwardMail_01() throws HarnessException  {

		String foldername = "folder" + ConfigProperties.getUniqueString();
		String subject = "subject" + ConfigProperties.getUniqueString();
		String mountpointname = "mountpoint" + ConfigProperties.getUniqueString();

		FolderItem inbox = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Inbox);

		// Create a folder to share
		ZimbraAccount.AccountA().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>"
						+		"<folder name='" + foldername + "' l='" + inbox.getId() + "'/>"
						+	"</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), foldername);

		// Share it
		ZimbraAccount.AccountA().soapSend(
				"<FolderActionRequest xmlns='urn:zimbraMail'>"
						+		"<action id='"+ folder.getId() +"' op='grant'>"
						+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidx'/>"
						+		"</action>"
						+	"</FolderActionRequest>");

		// Add a message to it
		ZimbraAccount.AccountA().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
						+		"<m l='"+ folder.getId() +"' >"
						+			"<content>From: foo@foo.com\n"
						+				"To: foo@foo.com \n"
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
						+		"<link l='1' name='"+ mountpointname +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.AccountA().ZimbraId +"'/>"
						+	"</CreateMountpointRequest>");

		app.zPageMain.sRefresh();	

		//Select the mount folder
		PageCreateFolder createFolderPage = new PageCreateFolder(app, startingPage);
		createFolderPage.zSelectMountFolder(mountpointname);
		
		// Forward the email in the mount folder
		MailItem mail = new MailItem();
		mail.dBodyText = "body" + ConfigProperties.getUniqueString();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA(), RecipientType.To));	

		app.zPageMail.zConversationListItem(Button.B_CONVERSATION_ACTION_DROPDOWN, subject);
		FormMailNew mailform = (FormMailNew) app.zPageMail.zListItem(Button.B_FORWARD_MAIL);
		mailform.zFill(mail);
		app.zFormMailNew.zToolbarPressButton(Button.B_SEND);

		// From the receiving end, verify the message details
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ subject +")");
		ZAssert.assertEquals(sent.dToRecipients.size(), 1, "Verify the message is sent to 1 'to' recipient");
	}
}
