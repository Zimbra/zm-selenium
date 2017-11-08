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
package com.zimbra.qa.selenium.projects.ajax.tests.search.savedsearch;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.SavedSearchFolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogRenameFolder;

public class RenameSavedSearch extends AjaxCommonTest  {

	public RenameSavedSearch() {
		logger.info("New "+ RenameSavedSearch.class.getCanonicalName());
		super.startingPage = app.zPageMail;
	}


	@Test (description = "Rename a saved search",
			groups = { "smoke","L1" })

	public void RenameSavedSearch_01() throws HarnessException {

		// Create the message data to be sent
		String name1 = "search" + ConfigProperties.getUniqueString();
		String name2 = "search" + ConfigProperties.getUniqueString();
		String query = "subject:(" + ConfigProperties.getUniqueString() + ")";

		app.zGetActiveAccount().soapSend(
				"<CreateSearchFolderRequest xmlns='urn:zimbraMail'>" +
					"<search name='"+ name1 +"' query='"+ query +"' l='1'/>" +
				"</CreateSearchFolderRequest>");

		// Get the item
		SavedSearchFolderItem item = SavedSearchFolderItem.importFromSOAP(app.zGetActiveAccount(), name1);

		// Refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Right click on the search, select delete
		DialogRenameFolder dialog = (DialogRenameFolder) app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_RENAME, item);

		// Rename the search
		dialog.zSetNewName(name2);
		dialog.zPressButton(Button.B_OK);

		// Verify the saved search exists in the folder tree
		List<SavedSearchFolderItem> searches = app.zTreeMail.zListGetSavedSearches();
		ZAssert.assertNotNull(searches, "Verify the saved search list exists");

		// Make sure the old search no longer appears
		// Make sure the new search does appear
		SavedSearchFolderItem found1 = null;
		SavedSearchFolderItem found2 = null;
		for (SavedSearchFolderItem s : searches) {
			logger.info("Subject: looking for "+ name1 +" found: "+ s.getName());
			if ( name1.equals(s.getName()) ) {
				found1 = s;
				break;
			}
			logger.info("Subject: looking for "+ name2 +" found: "+ s.getName());
			if ( name2.equals(s.getName()) ) {
				found2 = s;
				break;
			}
		}
		ZAssert.assertNull(found1, "Verify the old saved search is not in the folder tree");
		ZAssert.assertNotNull(found2, "Verify the new saved search is in the folder tree");
	}
}
