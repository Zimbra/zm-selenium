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

import java.util.*;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;

public class SearchGAL extends AjaxCore {

	public SearchGAL() {
		logger.info("New " + SearchGAL.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	@Test (description = "Search for a GAL contact",
			groups = { "sanity" })

	public void SearchGAL_01() throws HarnessException {

		// Create a GAL Account
		final String first = "first" + ConfigProperties.getUniqueString();
		final String last = "last" + ConfigProperties.getUniqueString();
		ZimbraAccount accountGAL = new ZimbraAccount();
		Map<String, String> attrs = new HashMap<String, String>() {
			private static final long serialVersionUID = -939087302049217526L;
			{
				put("givenName", first);
				put("sn", last);
				put("displayName", first + " " + last);
			}
		};
		accountGAL.setAccountPreferences(attrs);
		accountGAL.provision();
		accountGAL.authenticate();

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		try {

			// Search for firstname
			app.zPageSearch.zToolbarPressPulldown(Button.B_SEARCHTYPE, Button.O_SEARCHTYPE_GAL);
			app.zPageSearch.zAddSearchQuery(first);
			app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);

			List<ContactItem> contacts = app.zPageSearch.zListGetContacts();
			ZAssert.assertNotNull(contacts, "Verify the message list exists");

			ZAssert.assertEquals(contacts.size(), 1, "Verify only the one message was returned");
			ZAssert.assertStringContains(contacts.get(0).getAttribute("fileAs", ""), first,
					"Verify the contact is shown in the results");

		} finally {
			app.zPageSearch.zClose();
		}
	}


	@Test (description = "Search for a non-existing GAL contact",
			groups = { "sanity" })

	public void SearchGAL_02() throws HarnessException {

		String nonExistContact = "contact" + ConfigProperties.getUniqueString();

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		try {

			// Search for firstname
			app.zPageSearch.zToolbarPressPulldown(Button.B_SEARCHTYPE, Button.O_SEARCHTYPE_GAL);
			app.zPageSearch.zAddSearchQuery(nonExistContact);
			app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);

			List<ContactItem> contacts = app.zPageSearch.zListGetContacts();
			ZAssert.assertNotNull(contacts, "Verify the message list exists");

			ZAssert.assertEquals(contacts.size(), 0, "Verify no results");

		} finally {
			app.zPageSearch.zClose();
		}
	}
}