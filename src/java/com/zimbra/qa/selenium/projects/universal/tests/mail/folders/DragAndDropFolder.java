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
package com.zimbra.qa.selenium.projects.universal.tests.mail.folders;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;

public class DragAndDropFolder extends PrefGroupMailByMessageTest{

	public DragAndDropFolder() {
		logger.info("New "+ DragAndDropFolder.class.getCanonicalName());
	}

	@Test( description = "Drag one folder and Drop into other",
			groups = { "smoke", "L1" })
	
	public void DragDropFolder_01() throws HarnessException {

		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		ZAssert.assertNotNull(inbox, "Verify the inbox is available");

		String name1 = "folder" + ConfigProperties.getUniqueString();
		String name2 = "folder" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='"+ name1 +"' l='"+ inbox.getId() +"'/>"
				+	"</CreateFolderRequest>");


		FolderItem subfolder1 = FolderItem.importFromSOAP(app.zGetActiveAccount(), name1);
		ZAssert.assertNotNull(subfolder1, "Verify the first subfolder is available");

		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='"+ name2 +"' l='"+ inbox.getId() +"'/>"
				+	"</CreateFolderRequest>");

		FolderItem subfolder2 = FolderItem.importFromSOAP(app.zGetActiveAccount(), name2);
		ZAssert.assertNotNull(subfolder2, "Verify the second subfolder is available");

		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		SleepUtil.sleepSmall();
		app.zPageMail.zDragAndDrop(
				"css=td[id='zti__main_Mail__" + subfolder1.getId() + "_textCell']",
				"css=td[id='zti__main_Mail__" + subfolder2.getId() + "_textCell']");

		// Verify the folder is now in the other subfolder
		subfolder1 = FolderItem.importFromSOAP(app.zGetActiveAccount(), name1);
		ZAssert.assertNotNull(subfolder1, "Verify the subfolder is again available");
		ZAssert.assertEquals(subfolder2.getId(), subfolder1.getParentId(), "Verify the subfolder's parent is now the other subfolder");
	}
}
