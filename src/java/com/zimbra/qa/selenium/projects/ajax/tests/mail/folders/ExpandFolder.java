/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.folders;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;


public class ExpandFolder extends PrefGroupMailByMessageTest {

	public ExpandFolder() {
		logger.info("New "+ ExpandFolder.class.getCanonicalName());
		

		
	}
	
	@Test(	description = "Expand a subfolder",
			groups = { "smoke" })
	public void ExpandFolder_01() throws HarnessException {
		
		
		//-- DATA
		
		
		final FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		final String foldername1 = "folder" + ZimbraSeleniumProperties.getUniqueString();
		final String foldername2 = "folder" + ZimbraSeleniumProperties.getUniqueString();

		// Create this folder structure
		// +- Inbox
		//  +-- folder1
		//   +-- folder2
		// +- Sent
		//
		
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ foldername1 +"' l='"+ inbox.getId() +"'/>" +
                "</CreateFolderRequest>");
		FolderItem folder1 = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername1);

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ foldername2 +"' l='"+ folder1.getId() +"'/>" +
                "</CreateFolderRequest>");


		// Collapse all folders
		app.zGetActiveAccount().soapSend(
				"<SetMailboxMetadataRequest xmlns='urn:zimbraMail'>"
	    	+		"<meta section='zwc:implicit'>"
	    	+			"<a n='zimbraPrefFoldersExpanded'>"+ inbox.getId() +":true</a>"
    		+		"</meta>"
			+	"</SetMailboxMetadataRequest>");

		// Login/Logout to pick up the metadata
		app.zPageLogin.zNavigateTo();
		this.startingPage.zNavigateTo();
		
		//-- GUI
		
		
		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);

		
		// Expand folder1
		app.zTreeMail.zTreeItem(Action.A_TREE_EXPAND, folder1);
		
		
		//-- VERIFICATION
		
		
		// Check that the mailbox metadata shows the folder as expanded
		
		app.zGetActiveAccount().soapSend(
				"<GetMailboxMetadataRequest xmlns='urn:zimbraMail'>"
	    	+		"<meta section='zwc:implicit'>"
	    	+			"<a n='zimbraPrefFoldersExpanded'/>"
    		+		"</meta>"
			+	"</GetMailboxMetadataRequest>");

		String value = app.zGetActiveAccount().soapSelectValue("//mail:a[@n='zimbraPrefFoldersExpanded']", null);
		ZAssert.assertStringContains(value, folder1.getId() + ":true", "Verify the mailbox metadata saves the folder as expanded");
	}	

	@Test(	description = "Expand 2 subfolders",
			groups = { "functional" })
	public void ExpandFolder_02() throws HarnessException {
		
		
		//-- DATA
		
		
		final FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		final String foldername1 = "folder" + ZimbraSeleniumProperties.getUniqueString();
		final String foldername2 = "folder" + ZimbraSeleniumProperties.getUniqueString();
		final String foldername3 = "folder" + ZimbraSeleniumProperties.getUniqueString();
		final String foldername4 = "folder" + ZimbraSeleniumProperties.getUniqueString();

		// Create this folder structure
		// +- Inbox
		//  +-- folder1
		//   +-- folder2
		//  +-- folder3
		//   +-- folder4
		// +- Sent
		//
		
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ foldername1 +"' l='"+ inbox.getId() +"'/>" +
                "</CreateFolderRequest>");
		FolderItem folder1 = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername1);

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ foldername2 +"' l='"+ folder1.getId() +"'/>" +
                "</CreateFolderRequest>");

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ foldername3 +"' l='"+ inbox.getId() +"'/>" +
                "</CreateFolderRequest>");
		FolderItem folder3 = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername3);

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ foldername4 +"' l='"+ folder3.getId() +"'/>" +
                "</CreateFolderRequest>");

		// Collapse all folders
		app.zGetActiveAccount().soapSend(
				"<SetMailboxMetadataRequest xmlns='urn:zimbraMail'>"
	    	+		"<meta section='zwc:implicit'>"
	    	+			"<a n='zimbraPrefFoldersExpanded'>"+ inbox.getId() +":true</a>"
    		+		"</meta>"
			+	"</SetMailboxMetadataRequest>");

		// Login/Logout to pick up the metadata
		app.zPageLogin.zNavigateTo();
		this.startingPage.zNavigateTo();

		//-- GUI
		
		
		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);

		
		// Expand folder1
		app.zTreeMail.zTreeItem(Action.A_TREE_EXPAND, folder1);
		
		// Expand folder3
		app.zTreeMail.zTreeItem(Action.A_TREE_EXPAND, folder3);
		
		
		//-- VERIFICATION
		
		
		// Check that the mailbox metadata shows the folder as expanded
		
		app.zGetActiveAccount().soapSend(
				"<GetMailboxMetadataRequest xmlns='urn:zimbraMail'>"
	    	+		"<meta section='zwc:implicit'>"
	    	+			"<a n='zimbraPrefFoldersExpanded'/>"
    		+		"</meta>"
			+	"</GetMailboxMetadataRequest>");

		String value = app.zGetActiveAccount().soapSelectValue("//mail:a[@n='zimbraPrefFoldersExpanded']", null);
		ZAssert.assertStringContains(value, folder1.getId() + ":true", "Verify the mailbox metadata saves the folder as expanded");
		ZAssert.assertStringContains(value, folder3.getId() + ":true", "Verify the mailbox metadata saves the folder as expanded");

	}	

	@Test(	description = "Expand-All subfolders",
			groups = { "functional" })
	public void ExpandAllFolders_01() throws HarnessException {
		
		
		//-- DATA
		
		
		final FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		final String foldername1 = "folder" + ZimbraSeleniumProperties.getUniqueString();
		final String foldername2 = "folder" + ZimbraSeleniumProperties.getUniqueString();
		final String foldername3 = "folder" + ZimbraSeleniumProperties.getUniqueString();
		final String foldername4 = "folder" + ZimbraSeleniumProperties.getUniqueString();

		// Create this folder structure
		// +- Inbox
		//  +-- folder1
		//   +-- folder2
		//  +-- folder3
		//   +-- folder4
		// +- Sent
		//
		
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ foldername1 +"' l='"+ inbox.getId() +"'/>" +
                "</CreateFolderRequest>");
		FolderItem folder1 = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername1);

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ foldername2 +"' l='"+ folder1.getId() +"'/>" +
                "</CreateFolderRequest>");

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ foldername3 +"' l='"+ inbox.getId() +"'/>" +
                "</CreateFolderRequest>");
		FolderItem folder3 = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername3);

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ foldername4 +"' l='"+ folder3.getId() +"'/>" +
                "</CreateFolderRequest>");

		// Collapse all folders
		app.zGetActiveAccount().soapSend(
				"<SetMailboxMetadataRequest xmlns='urn:zimbraMail'>"
	    	+		"<meta section='zwc:implicit'>"
	    	+			"<a n='zimbraPrefFoldersExpanded'>"+ inbox.getId() +":true</a>"
    		+		"</meta>"
			+	"</SetMailboxMetadataRequest>");

		// Login/Logout to pick up the metadata
		app.zPageLogin.zNavigateTo();
		this.startingPage.zNavigateTo();

		//-- GUI
		
		
		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);

		
		// Expand-All on Inbox
		app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_FOLDER_EXPANDALL, inbox);
		
		
		
		//-- VERIFICATION
		
		
		// Check that the mailbox metadata shows the folder as expanded
		
		app.zGetActiveAccount().soapSend(
				"<GetMailboxMetadataRequest xmlns='urn:zimbraMail'>"
	    	+		"<meta section='zwc:implicit'>"
	    	+			"<a n='zimbraPrefFoldersExpanded'/>"
    		+		"</meta>"
			+	"</GetMailboxMetadataRequest>");

		String value = app.zGetActiveAccount().soapSelectValue("//mail:a[@n='zimbraPrefFoldersExpanded']", null);
		ZAssert.assertStringContains(value, inbox.getId() + ":true", "Verify the mailbox metadata saves the folder as expanded");
		ZAssert.assertStringContains(value, folder1.getId() + ":true", "Verify the mailbox metadata saves the folder as expanded");
		ZAssert.assertStringContains(value, folder3.getId() + ":true", "Verify the mailbox metadata saves the folder as expanded");

	}	

}
