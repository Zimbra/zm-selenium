package com.zimbra.qa.selenium.projects.universal.tests.contacts.features;

/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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

import java.util.*;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;

public class NavigateToAppContactsURL extends AjaxCommonTest {
	public NavigateToAppContactsURL() {
		logger.info("New " + NavigateToAppContactsURL.class.getCanonicalName());

		// All tests start at the Address page
		super.startingPage = app.zPageContacts;

		// Enable user preference checkboxes

	}

	@Test(description = "?app=contacts in url", 
			groups = { "smoke", "L1"})

	public void NavigateToAppContactsURL_01() throws HarnessException {

		// Go to Mail tab
		app.zPageMail.zNavigateTo();
		SleepUtil.sleepMedium();

		String lastname;

		// Create contact

		lastname = "B" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount()
				.soapSend("<CreateContactRequest xmlns='urn:zimbraMail'>" + "<cn >" + "<a n='firstName'>first"
						+ ConfigProperties.getUniqueString() + "</a>" + "<a n='lastName'>" + lastname + "</a>"
						+ "<a n='email'>email@domain.com</a>" + "</cn>" + "</CreateContactRequest>");
		ContactItem contact1 = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#lastname:" + lastname);

		// -- GUI

		// Reload the application, with app=contacts query parameter

		ZimbraURI uri = new ZimbraURI(ZimbraURI.getBaseURI());
		uri.addQuery("app", "contacts");
		app.zPageContacts.sOpen(uri.toString());

		SleepUtil.sleepMedium();

		// click All
		app.zPageContacts.zToolbarPressButton(Button.B_AB_ALL);

		// -- Verification of added contacts

		List<ContactItem> items = app.zPageContacts.zListGetContacts();

		boolean found1 = false;

		for (ContactItem item : items) {

			if (item.getName().equals(contact1.getName())) {
				found1 = true;
			}

		}

		ZAssert.assertTrue(found1, "Verify contact  is listed");

	}

}
