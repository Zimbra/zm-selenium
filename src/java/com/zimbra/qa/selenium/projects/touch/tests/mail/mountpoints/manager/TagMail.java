/* ***** BEGIN LICENSE BLOCK *****
/* Zimbra Collaboration Suite Server
/* Copyright (C) 2015, 2016 Synacor, Inc.
/*
/* This program is free software: you can redistribute it and/or modify it under
/* the terms of the GNU General Public License as published by the Free Software Foundation,
/* version 2 of the License.
/*
/* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
/* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
/* See the GNU General Public License for more details.
/* You should have received a copy of the GNU General Public License along with this program.
/* If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.touch.tests.mail.mountpoints.manager;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.touch.pages.PageCreateFolder;

public class TagMail extends SetGroupMailByMessagePreference{


	public TagMail() {
		logger.info("New " + TagMail.class.getCanonicalName());
	}

	@Test (description = "Verify user can mark tag a email on mount point with manager rights",
			groups = { "functional" })

	public void TagMail_01() throws HarnessException  {

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
		// Create a tag
		TagItem tagItem = TagItem.CreateUsingSoap(app.zGetActiveAccount());
		String tagName = tagItem.getName();
		app.zPageMain.zRefreshMainUI();

		// Flag the email in the mount folder
		PageCreateFolder createFolderPage = new PageCreateFolder(app, startingPage);
		createFolderPage.zSelectMountFolder(mountpointname);
		app.zPageMail.zListItem(Action.A_LEFTCLICK, Button.B_TAG_CONVERSATION, subject);
		app.zTreeMail.zSelectTag(tagName);

		// Verification
		MailItem actual = MailItem.importFromSOAP(ZimbraAccount.AccountA(),"tag:"+tagItem.getName()+" AND subject:(" + subject + ")");
        ZAssert.assertNotNull(actual, "Verify the mail is with the tag");
		

	}}
