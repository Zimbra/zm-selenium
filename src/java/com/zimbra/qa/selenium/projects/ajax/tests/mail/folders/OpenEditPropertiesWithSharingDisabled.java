/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
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

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;

public class OpenEditPropertiesWithSharingDisabled extends AjaxCore {

	public OpenEditPropertiesWithSharingDisabled() {
		logger.info("New "+ OpenEditPropertiesWithSharingDisabled.class.getCanonicalName());

		super.startingPage = app.zPageMail;
		super.startingAccountPreferences.put("zimbraFeatureSharingEnabled", "FALSE");
	}


	@Test (description = "Bug 104040 - Open Edit properties with sharing disabled for a user ",
			groups = { "sanity" })

	public void OpenEditPropertiesWithSharingDisabled_01() throws HarnessException {

		// Create a new folder
		String name = "folder" + ConfigProperties.getUniqueString();
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		ZAssert.assertNotNull(inbox, "Verify the inbox is available");

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ name +"' l='"+ inbox.getId() +"'/>" +
                "</CreateFolderRequest>");

		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNotNull(subfolder, "Verify the subfolder is available");

		// Click the refresh button and select edit properties for the folder
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_EDIT, subfolder);

		// Verify that the 'folder properties' dialogue opens
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent("css=td[id='FolderProperties_title']"),"verify that 'edit properties' dialoge is present" );

		//Enable sharing again for the active account
		super.startingAccountPreferences.put("zimbraFeatureSharingEnabled", "TRUE");
		app.zGetActiveAccount().modifyAccountPreferences(startingAccountPreferences);
	}
}