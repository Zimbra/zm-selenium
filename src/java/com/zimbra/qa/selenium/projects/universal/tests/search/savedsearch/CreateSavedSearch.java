/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.search.savedsearch;

import java.util.List;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.SavedSearchFolderItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.search.DialogSaveSearch;

//TODO: add more in ContactItem.java

public class CreateSavedSearch extends UniversalCommonTest  {

	public CreateSavedSearch() {
		logger.info("New "+ CreateSavedSearch.class.getCanonicalName());
		
		// All tests start at the Address page
		super.startingPage = app.zPageMail;

		// Make sure we are using an account with conversation view
		
	}
	
	@Test (description = "Create a basic saved search",
			groups = { "sanity","L0" })
	
	public void CreateSavedSearch_01() throws HarnessException {				
				
			
		// Create the message data to be sent
		String name = "search" + ConfigProperties.getUniqueString();
		String query = "subject:(" + ConfigProperties.getUniqueString() + ")";

		// Remember to close the search window after saving
		try {

			// Search for the message
			app.zPageSearch.zAddSearchQuery(query);
			app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);
			DialogSaveSearch dialog = (DialogSaveSearch)app.zPageSearch.zToolbarPressButton(Button.B_SAVE);
			
			// Save the search
			dialog.zEnterFolderName(name);
			dialog.zPressButton(Button.B_OK);
		
		} finally {
			
			// Remember to close the search window after saving
			app.zPageSearch.zClose();
			
		}

		
		//Verify the saved search exists in the server
		SavedSearchFolderItem item = SavedSearchFolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNotNull(item, "Verify the saved search was created correctly");
		
		// Verify the saved search exists in the folder tree
		List<SavedSearchFolderItem> searches = app.zTreeMail.zListGetSavedSearches();
		ZAssert.assertNotNull(searches, "Verify the saved search list exists");

		// Make sure the message appears in the list
		SavedSearchFolderItem found = null;
		for (SavedSearchFolderItem s : searches) {
			logger.info("Subject: looking for "+ name +" found: "+ s.getName());
			if ( name.equals(s.getName()) ) {
				found = s;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the saved search is in the folder tree");

	}
}
