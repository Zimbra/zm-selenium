/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.webex;

import java.util.List;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.ZimletItem;
import com.zimbra.qa.selenium.framework.items.ZimletItem.CoreZimletItem;
import com.zimbra.qa.selenium.framework.items.ZimletItem.CoreZimletItem.CoreZimletName;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.TreeMail.FolderSection;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.TreeMail.FolderSectionAction;


public class FolderTree extends AjaxCommonTest {

	public FolderTree() {
		logger.info("New "+ FolderTree.class.getCanonicalName());
		
		// All tests start at the login page
		super.startingPage = app.zPageMail;
		
		
	}
	
	@Test( description = "Verify the WebEx zimlet appears in the folder tree",
			groups = { "smoke" })
	public void FolderTree_01() throws HarnessException {
		ZimletItem webex = CoreZimletItem.getCoreZimlet(CoreZimletName.com_zimbra_webex, app);
		
		// Expand the zimlets section
		app.zTreeMail.zSectionAction(FolderSectionAction.Expand, FolderSection.Zimlets);
		
		// Get the list of zimlets
		List<ZimletItem> zimlets = app.zTreeMail.zListGetZimlets();
		
		// Find out if WebEx is listed
		ZimletItem found = null;
		for (ZimletItem zimlet : zimlets) {
			if ( webex.equals(zimlet) ) {
				found = zimlet;
			}
		}
		
		ZAssert.assertNotNull(found, "Verify the Webex Zimlet was found");
				
	}

	// All these tests require the Folder tree to be fully loaded
	@BeforeMethod( groups = { "always" } )
	public void folderTreeBeforeMethod() throws HarnessException {
		logger.info("folderTreeBeforeMethod: start");
		
		for (int i = 0; i < 10; i++) {
			
			if ( app.zTreeMail.zIsActive() ) {
				return; // Done!
			}
			
			SleepUtil.sleep(1000);
			
		}
		logger.info("folderTreeBeforeMethod: finish");

	}

}
