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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.attributes;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.*;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormAddressPicker.Field;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormAddressPicker.Locators;

public class ContactsDetailedSearchEnabled extends SetGroupMailByMessagePreference {

	public ContactsDetailedSearchEnabled() {
		logger.info("New "+ ContactsDetailedSearchEnabled.class.getCanonicalName());
		super.startingAccountPreferences.put("ZimbraFeatureContactsDetailedSearchEnabled", "TRUE");
	}


	@Bugs (ids = "70708")
	@Test (description = "Filter addresses using department name after selecting To: while composing mail",
			groups = { "sanity" })

	public void ContactsDetailedSearchEnabled_01() throws HarnessException {
		String department = "dept" + ConfigProperties.getUniqueString();

		// Add departments to three accounts
		ZimbraAdminAccount.GlobalAdmin()
				.soapSend("<ModifyAccountRequest xmlns='urn:zimbraAdmin'>" + "<id>"
						+ ZimbraAccount.Account5().ZimbraId + "</id>" + "<a n='ou'>" + department + "</a>"
						+ "</ModifyAccountRequest>");

		ZimbraAdminAccount.GlobalAdmin()
				.soapSend("<ModifyAccountRequest xmlns='urn:zimbraAdmin'>" + "<id>"
						+ ZimbraAccount.Account6().ZimbraId + "</id>" + "<a n='ou'>" + department + "</a>"
						+ "</ModifyAccountRequest>");

		ZimbraAdminAccount.GlobalAdmin().soapSend("<ModifyAccountRequest xmlns='urn:zimbraAdmin'>" + "<id>"
				+ ZimbraAccount.Account7().ZimbraId + "</id>" + "<a n='ou'>HR</a>" + "</ModifyAccountRequest>");

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Click on To button
		FormAddressPicker selectAddress = (FormAddressPicker) mailform.zToolbarPressButton(Button.B_TO);

		// Enter department to search
		selectAddress.zFillField(Field.Department, department);

		for (int i=0; i<=5; i++) {
			selectAddress.zToolbarPressButton(Button.B_SEARCH);

			if (selectAddress.sIsElementPresent(
							Locators.SearchResultArea + ":contains('" + ZimbraAccount.Account5().EmailAddress + "')")) {

				// Check that correct addresses are filtered out
				ZAssert.assertTrue(
						selectAddress.sIsElementPresent(
								Locators.SearchResultArea + ":contains('" + ZimbraAccount.Account5().EmailAddress + "')"),
						"Verify that correct addresses are filtered out");
				ZAssert.assertTrue(
						selectAddress.sIsElementPresent(
								Locators.SearchResultArea + ":contains('" + ZimbraAccount.Account6().EmailAddress + "')"),
						"Verify that correct addresses are filtered out");

				// Check that department names are also displayed
				ZAssert.assertTrue(
						selectAddress.sIsElementPresent(Locators.SearchResultArea + ":contains('" + department + "')"),
						"Verify that search result containing the department name is displayed ");

				// Select first address
				selectAddress.sClick(Locators.ContactPickerFirstContact);
				selectAddress.zToolbarPressButton(Button.B_TO);
				ZAssert.assertTrue(
						selectAddress.sIsElementPresent(
								Locators.SearchResultArea + ":contains('" + department + "'):contains('To:')"),
						"Verify department is displayed in recipient textarea");

				break;
			}
		}
	}
}