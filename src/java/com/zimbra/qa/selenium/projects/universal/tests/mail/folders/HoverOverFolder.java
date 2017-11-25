/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.mail.folders;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.universal.pages.TooltipFolder;


public class HoverOverFolder extends SetGroupMailByMessagePreference {

	public HoverOverFolder() {
		logger.info("New "+ HoverOverFolder.class.getCanonicalName());
		
	}
	
	@Test (description = "Hover over a folder to show the tooltip",
			groups = { "functional", "L2" })
	public void TooltipFolder_01() throws HarnessException {
		
		//-- Data
		
		// Create a subfolder with a message in it		
		String foldername = "folder" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ foldername +"' l='"+ FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox).getId() +"'/>" +
                "</CreateFolderRequest>");
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Add a message
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
    		+		"<m l='"+ subfolder.getId() +"' f='u'>"
        	+			"<content>From: foo@foo.com\n"
        	+				"To: foo@foo.com \n"
        	+				"Subject: subject"+ ConfigProperties.getUniqueString() +"\n"
        	+				"MIME-Version: 1.0 \n"
        	+				"Content-Type: text/plain; charset=utf-8 \n"
        	+				"Content-Transfer-Encoding: 7bit\n"
        	+				"\n"
        	+				"simple text string in the body\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");
		
		
		//-- GUI
		
		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Hover over the folder
		TooltipFolder tooltip = (TooltipFolder)app.zTreeMail.zTreeItem(Action.A_HOVEROVER, subfolder);
		
		
		// Verification
		
		ZAssert.assertTrue(tooltip.zIsActive(), "Verify the tooltip shows");

	}	

	@Test (description = "Hover over a folder - Verify contents",
			groups = { "functional", "L2" })
	public void TooltipFolder_02() throws HarnessException {
		
		//-- Data
		
		// Create a subfolder with a message in it		
		String foldername = "folder" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ foldername +"' l='"+ FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox).getId() +"'/>" +
                "</CreateFolderRequest>");
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Add a message
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
    		+		"<m l='"+ subfolder.getId() +"' f='u'>"
        	+			"<content>From: foo@foo.com\n"
        	+				"To: foo@foo.com \n"
        	+				"Subject: subject"+ ConfigProperties.getUniqueString() +"\n"
        	+				"MIME-Version: 1.0 \n"
        	+				"Content-Type: text/plain; charset=utf-8 \n"
        	+				"Content-Transfer-Encoding: 7bit\n"
        	+				"\n"
        	+				"simple text string in the body\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");
		
		
		//-- GUI
		
		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Hover over the folder
		TooltipFolder tooltip = (TooltipFolder)app.zTreeMail.zTreeItem(Action.A_HOVEROVER, subfolder);
		
		
		// Verification
		
		// Verify the tooltip appears
		ZAssert.assertTrue(tooltip.zIsActive(), "Verify the tooltip shows");

		String actual;
		
		// Verify the folder name appears
		actual = tooltip.zGetField(TooltipFolder.Field.Foldername);
		ZAssert.assertEquals(actual, foldername, "Verify the correct foldername is shown");

		// Verify 1 total message
		actual = tooltip.zGetField(TooltipFolder.Field.TotalMessages);
		ZAssert.assertEquals(actual, "1", "Verify the correct foldername is shown");

		// Verify 1 unread message
		actual = tooltip.zGetField(TooltipFolder.Field.UnreadMessages);
		ZAssert.assertEquals(actual, "1", "Verify the correct foldername is shown");

		/**
		 * I've tried:
		 * 198 B
		 * 201 B
		 * 200 B
		 * The computed size depends on the server.  So, just use not "0 B" (and hope
		 * that some implementation won't use 0.2 MB)
		 * 
		 */
		// Verify "201 B" message size
		actual = tooltip.zGetField(TooltipFolder.Field.Size);
		ZAssert.assertNotMatches("^0", actual, "Verify size contains non-0 message size");

	}	

	@Test (description = "Hover over an empty folder",
			groups = { "functional", "L3" })
	public void TooltipFolder_03() throws HarnessException {
		
		//-- Data
		
		// Create a subfolder with a message in it		
		String foldername = "folder" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ foldername +"' l='"+ FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox).getId() +"'/>" +
                "</CreateFolderRequest>");
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

	
		
		//-- GUI
		
		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Hover over the folder
		TooltipFolder tooltip = (TooltipFolder)app.zTreeMail.zTreeItem(Action.A_HOVEROVER, subfolder);
		
		
		// Verification
		
		ZAssert.assertTrue(tooltip.zIsActive(), "Verify the tooltip shows");

	}	

	@DataProvider(name = "DataProviderSystemFolders")
	public Object[][] DataProviderSystemFolders() {
	  return new Object[][] {
			    new Object[] { "Inbox", SystemFolder.Inbox },
			    new Object[] { "Drafts", SystemFolder.Drafts },
			    new Object[] { "Sent", SystemFolder.Sent },
			    new Object[] { "Trash", SystemFolder.Trash },
			    new Object[] { "Junk", SystemFolder.Junk },
	  };
	}
	
	@Test (description = "Hover over the system folders",
			groups = { "functional", "L2" },
			dataProvider = "DataProviderSystemFolders")
	public void TooltipFolder_10(String foldername, SystemFolder foldertype) throws HarnessException {
		
		//-- Data
		
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldertype);

		if ( foldertype == SystemFolder.Drafts ) {
			
			app.zGetActiveAccount().soapSend(
					"<SaveDraftRequest xmlns='urn:zimbraMail'>"
	    		+		"<m >"
	        	+			"<e t='t' a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>"
	        	+			"<su>subject"+ ConfigProperties.getUniqueString() +"</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>content</content>"
				+			"</mp>"
	        	+		"</m>"
				+	"</SaveDraftRequest>");

		} else {
			
			// Add a message
			app.zGetActiveAccount().soapSend(
					"<AddMsgRequest xmlns='urn:zimbraMail'>"
	    		+		"<m l='"+ subfolder.getId() +"' f='u'>"
	        	+			"<content>From: foo@foo.com\n"
	        	+				"To: foo@foo.com \n"
	        	+				"Subject: subject"+ ConfigProperties.getUniqueString() +"\n"
	        	+				"MIME-Version: 1.0 \n"
	        	+				"Content-Type: text/plain; charset=utf-8 \n"
	        	+				"Content-Transfer-Encoding: 7bit\n"
	        	+				"\n"
	        	+				"simple text string in the body\n"
	        	+			"</content>"
	        	+		"</m>"
				+	"</AddMsgRequest>");
		
		}
		
		
		//-- GUI
		
		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Hover over the folder
		TooltipFolder tooltip = (TooltipFolder)app.zTreeMail.zTreeItem(Action.A_HOVEROVER, subfolder);
		
		
		// Verification
		
		ZAssert.assertTrue(tooltip.zIsActive(), "Verify the tooltip shows");

	}	


}
