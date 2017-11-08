/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.contacts.toaster;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.Toaster;
import com.zimbra.qa.selenium.projects.universal.ui.contacts.FormContactNew;
import com.zimbra.qa.selenium.projects.universal.ui.contacts.FormContactNew.Field;

public class CreateContact extends UniversalCommonTest {

	public CreateContact() {
		logger.info("New " + CreateContact.class.getCanonicalName());
		// All tests start at the Address page
		super.startingPage = app.zPageContacts;
		// Make sure we are using an account with conversation view

	}

	@Test(description = "Create a basic contact item by click New in page Addressbook and verify toast msg ", 
			groups = {"functional", "L2"})

	public void CreateContactToastMsg_01() throws HarnessException {

		// -- DATA

		String contactFirst = "First" + ConfigProperties.getUniqueString();
		String contactLast = "Last" + ConfigProperties.getUniqueString();
		String contactEmail = "email" + ConfigProperties.getUniqueString() + "@domain.com";

		// -- GUI Action

		FormContactNew formContactNew = (FormContactNew) app.zPageContacts.zToolbarPressButton(Button.B_NEW);

		// Fill in the form
		formContactNew.zFillField(Field.FirstName, contactFirst);
		formContactNew.zFillField(Field.LastName, contactLast);
		formContactNew.zFillField(Field.Email, contactEmail);

		if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled",
				ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
			// this method won't wait for some sec after submitting data so
			// toast message disappears and testcase fails (JS COVERAGE)
			app.zPageContacts.sClickAt(
					"css=div#" + formContactNew.getToolbarID() + " div[id$='__SAVE'] td[id$='_title']", "0,0");
		} else {
			formContactNew.zSubmit();
		}

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "Contact Created", "Verify toast message: Contact Created");

	}
}
