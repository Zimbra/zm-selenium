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
package com.zimbra.qa.selenium.projects.universal.tests.search.search;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;

public class SearchContact extends AjaxCommonTest {

	public SearchContact() {
		logger.info("New " + SearchContact.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}

	@Test(description = "Search for a contact", 
			groups = { "functional","L2" })
	public void SearchContact_01() throws HarnessException {

		// -- Data

		// Create a contact via soap
		ContactItem contactItem = ContactItem.createContactItem(app.zGetActiveAccount());

		// -- GUI

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Remember to close the search view
		try {

			// search for firstname
			app.zPageSearch.zToolbarPressPulldown(Button.B_SEARCHTYPE, Button.O_SEARCHTYPE_CONTACTS);
			app.zPageSearch.zAddSearchQuery(contactItem.firstName);
			app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);

			List<ContactItem> contacts = app.zPageSearch.zListGetContacts();
			ZAssert.assertNotNull(contacts, "Verify the message list exists");

			ZAssert.assertEquals(contacts.size(), 1, "Verify only the one message was returned");
			ZAssert.assertStringContains(contacts.get(0).getAttribute("fileAs", ""), contactItem.firstName,
					"Verify the contact is shown in the results");

		} finally {
			// Remember to close the search view
			app.zPageSearch.zClose();
		}

	}

	@Test(description = "Search for a non-existing contact", 
			groups = { "functional","L3" })
	public void SearchContact_02() throws HarnessException {

		// -- Data

		String doesnotexist = "contact" + ConfigProperties.getUniqueString();

		// -- GUI

		// Remember to close the search view
		try {

			// search for firstname
			app.zPageSearch.zToolbarPressPulldown(Button.B_SEARCHTYPE, Button.O_SEARCHTYPE_CONTACTS);
			app.zPageSearch.zAddSearchQuery(doesnotexist);
			app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);

			List<ContactItem> contacts = app.zPageSearch.zListGetContacts();
			ZAssert.assertNotNull(contacts, "Verify the message list exists");

			ZAssert.assertEquals(contacts.size(), 0, "Verify no results");

		} finally {
			// Remember to close the search view
			app.zPageSearch.zClose();
		}

	}

}
