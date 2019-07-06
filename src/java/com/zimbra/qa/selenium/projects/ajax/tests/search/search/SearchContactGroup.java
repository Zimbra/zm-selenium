/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.search.search;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;

public class SearchContactGroup extends AjaxCore {

	public SearchContactGroup() {
		logger.info("New " + SearchContactGroup.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	@Test (description = "Search for an existing contact group",
			groups = { "sanity" })

	public void SearchContactGroup_01() throws HarnessException {

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		try {

			// Search for group name
			app.zPageSearch.zToolbarPressPulldown(Button.B_SEARCHTYPE, Button.O_SEARCHTYPE_CONTACTS);
			app.zPageSearch.zAddSearchQuery(group.groupName);
			app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);

			List<ContactItem> contacts = app.zPageSearch.zListGetContacts();
			ZAssert.assertTrue(contacts.size() == 1, "Verify only 1 contact group displayed");
			ZAssert.assertEquals(contacts.get(0).fileAs, group.groupName,
					"Verify contact group (" + group.groupName + ") is displayed");

		} finally {
			app.zPageSearch.zClose();
		}
	}
}