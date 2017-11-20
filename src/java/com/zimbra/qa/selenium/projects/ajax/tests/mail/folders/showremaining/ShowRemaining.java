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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.folders.showremaining;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;

public class ShowRemaining extends SetGroupMailByMessagePreference {

	public ShowRemaining() {
		logger.info("New "+ ShowRemaining.class.getCanonicalName());
	}


	@Test (description = "Click on 'show remaining folders'",
			groups = { "functional", "L2" })

	public void ShowRemaining_01() throws HarnessException {

		String name = "";

		// Create 125 subfolders
		for ( int i = 0; i < 125; i++ ) {
			name = "folder" + ConfigProperties.getUniqueString();
			app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
	                	"<folder name='"+ name +"' l='1'/>" +
	                "</CreateFolderRequest>");
		}

		// Need to logout/login for changes to take effect
		ZimbraAccount active = app.zGetActiveAccount();
		app.zPageMain.zLogout();
		app.zPageLogin.zLogin(active);
		if ( !startingPage.zIsActive() ) {
			startingPage.zNavigateTo();
		}
		if ( !startingPage.zIsActive() ) {
			throw new HarnessException("Unable to navigate to "+ startingPage.myPageName());
		}

		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Click on the "Show Remaining"
		app.zTreeMail.zPressButton(Button.B_TREE_SHOW_REMAINING_FOLDERS);

		// Wait again
		SleepUtil.sleep(10000);

		// Verify the last folder now appears in the list
		List<FolderItem> folders = app.zTreeMail.zListGetFolders();

		FolderItem found = null;
		for (FolderItem f : folders) {
			if ( name.equals(f.getName()) ) {
				found = f;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the folder "+ name + " was in the tree");
	}
}