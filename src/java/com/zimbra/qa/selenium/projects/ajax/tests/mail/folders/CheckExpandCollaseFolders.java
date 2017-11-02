/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.folders;

import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.tests.mail.mail.GetMail;

public class CheckExpandCollaseFolders extends PrefGroupMailByMessageTest {

	@AfterClass( groups = { "always" } )
	public void bug57468AfterClass() throws HarnessException {
		logger.info("bug57468AfterClass: start");
		
		// Since we collapsed the folder tree, it may cause problems for other tests
		// Reset the ZCS user
		ZimbraAccount.ResetAccountZCS();
		
		logger.info("bug57468AfterClass: finish");
	}

	public CheckExpandCollaseFolders() {
		logger.info("New "+ GetMail.class.getCanonicalName());
	}

	@Bugs( ids = "57468")
	@Test( description = "Verify collapsed folders remain collapsed when getting mail",
			groups = { "functional", "L2" })
	
	public void CheckExpandCollaseFolders_01() throws HarnessException {

		// Create a subfolder in Inbox
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		ZAssert.assertNotNull(inbox, "Verify the inbox is available");

		String foldername = "folder" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ foldername +"' l='"+ inbox.getId() +"'/>" +
                "</CreateFolderRequest>");
		
		// Refresh current view to refresh the tree
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inbox);

		// Collapse the inbox
		app.zTreeMail.zTreeItem(Action.A_TREE_COLLAPSE, inbox);

		// Send a message to the test account
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ "subject" + ConfigProperties.getUniqueString() +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>content" + ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");
		
		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		
		// Verify the inbox remains collapsed
		List<FolderItem> folders = app.zTreeMail.zListGetFolders();
		FolderItem found = null;
		for (FolderItem f : folders) {
			if ( f.getId().equals(inbox.getId()) ) {
				found = f;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the inbox is in the folder tree");
		
		// Collapse the inbox if it is currently expanded
		ZAssert.assertFalse(found.gGetIsExpanded(), "Verify that the inbox is not expanded");
		
	}
}
