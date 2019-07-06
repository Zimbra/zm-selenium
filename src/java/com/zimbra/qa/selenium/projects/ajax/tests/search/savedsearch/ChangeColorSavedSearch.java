/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.SavedSearchFolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DialogEditFolder;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DialogEditFolder.FolderColor;

public class ChangeColorSavedSearch extends SetGroupMailByMessagePreference {

	public ChangeColorSavedSearch() {
		logger.info("New " + ChangeColorSavedSearch.class.getCanonicalName());
	}


	@Test (description = "Edit a folder, change the color (Context menu -> Edit)",
			groups = { "functional" } )

	public void ChangeColorSavedSearch_01() throws HarnessException {

		// Create the message data to be sent
		String name = "search" + ConfigProperties.getUniqueString();
		String query = "subject:(" + ConfigProperties.getUniqueString() + ")";

		app.zGetActiveAccount().soapSend(
				"<CreateSearchFolderRequest xmlns='urn:zimbraMail'>" +
					"<search name='"+ name +"' query='"+ query +"' l='1'/>" +
				"</CreateSearchFolderRequest>");

		// Get the item
		SavedSearchFolderItem item = SavedSearchFolderItem.importFromSOAP(app.zGetActiveAccount(), name);

		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Right click on the search, select edit
		DialogEditFolder dialog = (DialogEditFolder) app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_EDIT, item);

		// Change the color, click OK
		dialog.zSetNewColor(FolderColor.Gray);
		dialog.zPressButton(Button.B_OK);

		// Check the color
		app.zGetActiveAccount().soapSend(
				"<GetFolderRequest xmlns='urn:zimbraMail'>"
			+		"<folder id='" + item.getId() + "'/>"
			+	"</GetFolderRequest>");

		String color = app.zGetActiveAccount().soapSelectValue("//mail:search[@name='" + item.getName() + "']", "color");
		ZAssert.assertEquals(color, "8", "Verify the color of the folder is set to gray (8)");
	}
}