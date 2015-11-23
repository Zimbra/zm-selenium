/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.search.savedsearch;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.SavedSearchFolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.*;


//TODO: add more in ContactItem.java

public class MoveSavedSearch extends AjaxCommonTest  {

	public MoveSavedSearch() {
		logger.info("New "+ MoveSavedSearch.class.getCanonicalName());
		
		// All tests start at the Address page
		super.startingPage = app.zPageMail;

		// Make sure we are using an account with conversation view
		super.startingAccountPreferences = null;		
		
	}
	
	
	@Test(	description = "Move a saved search",
			groups = { "smoke" })
	public void MoveSavedSearch_01() throws HarnessException {				
				
			
		// Create the message data to be moved
		String name1 = "search" + ZimbraSeleniumProperties.getUniqueString();
		String name2 = "search" + ZimbraSeleniumProperties.getUniqueString();
		String query1 = "subject:(" + ZimbraSeleniumProperties.getUniqueString() + ")";
		String query2 = "subject:(" + ZimbraSeleniumProperties.getUniqueString() + ")";
		

		app.zGetActiveAccount().soapSend(
				"<CreateSearchFolderRequest xmlns='urn:zimbraMail'>" +
					"<search name='"+ name1 +"' query='"+ query1 +"' l='1'/>" +
				"</CreateSearchFolderRequest>");
		SavedSearchFolderItem item1 = SavedSearchFolderItem.importFromSOAP(app.zGetActiveAccount(), name1);

		app.zGetActiveAccount().soapSend(
				"<CreateSearchFolderRequest xmlns='urn:zimbraMail'>" +
					"<search name='"+ name2 +"' query='"+ query2 +"' l='1'/>" +
				"</CreateSearchFolderRequest>");
		SavedSearchFolderItem item2 = SavedSearchFolderItem.importFromSOAP(app.zGetActiveAccount(), name2);


		// Refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		
		// Right click on the search, select delete
		// TODO: can the folder move dialog be reused?  Or, do we need DialogMoveSavedSearchFolder class?
		DialogMove dialog = (DialogMove) app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_MOVE, item1);
		
		// Rename the search
		dialog.zEnterFolderName(name2);
		dialog.zClickButton(Button.B_OK);


		// Verify the saved search exists under the other saved search
		item1 = SavedSearchFolderItem.importFromSOAP(app.zGetActiveAccount(), name1);
		ZAssert.assertEquals(item1.getParentId(), item2.getId(), "Verify the saved search's parent folder is the other saved search");
		

	}
}
