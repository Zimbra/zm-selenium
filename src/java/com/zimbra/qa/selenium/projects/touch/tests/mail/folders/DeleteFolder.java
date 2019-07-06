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
package com.zimbra.qa.selenium.projects.touch.tests.mail.folders;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.touch.pages.PageCreateFolder;

public class DeleteFolder extends SetGroupMailByMessagePreference {

	public DeleteFolder() {
		logger.info("New "+ CreateFolder.class.getCanonicalName());
	}

	@Test (description = "Delete mail folder",
			groups = { "bhr" })
	
	public void DeleteFolder_01() throws HarnessException  {
	
		String subject = "subject" + ConfigProperties.getUniqueString();
		
		// Send the message from AccountA to the ZCS user
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ "body" + ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");
		
		
		// Create a folder
		FolderItem userRoot = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		String folderName = "folder" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ folderName +"' l='"+ userRoot.getId() +"'/>" +
                "</CreateFolderRequest>");

		app.zPageMail.zRefresh();
				
		// Select the folder from the list and Delete
		PageCreateFolder createFolderPage = new PageCreateFolder(app, startingPage);
		createFolderPage.zPressButton(Button.B_EDIT);
		createFolderPage.zSelectFolder(folderName);
		createFolderPage.zPressButton(Button.B_DELETE);

        // Verification
        
        // SOAP
     	FolderItem actual = FolderItem.importFromSOAP(app.zGetActiveAccount(), folderName);
     	ZAssert.assertEquals(actual.getParentId(), trash.getId(), "Verify folder is moved to trash");
     		
        // UI (rest of UI verification will be covered with permanent delete folder test case)
		ZAssert.assertTrue(createFolderPage.zVerifyFolderExists(folderName), "Verify deleted folder moved to trash");
	}
}