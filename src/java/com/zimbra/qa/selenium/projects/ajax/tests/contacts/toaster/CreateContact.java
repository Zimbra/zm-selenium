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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.toaster;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.Toaster;
import com.zimbra.qa.selenium.projects.ajax.ui.contacts.FormContactNew;
import com.zimbra.qa.selenium.projects.ajax.ui.contacts.FormContactNew.Field;

public class CreateContact extends AjaxCommonTest {

	public CreateContact() {
		logger.info("New " + CreateContact.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	@Test(description = "Create a basic contact item by click New in page Addressbook and verify toast msg ",
			groups = {"functional", "L2"})

	public void CreateContactToastMsg_01() throws HarnessException {

		String contactFirst = "First" + ConfigProperties.getUniqueString();
		String contactLast = "Last" + ConfigProperties.getUniqueString();
		String contactEmail = "email" + ConfigProperties.getUniqueString() + "@domain.com";

		FormContactNew formContactNew = (FormContactNew) app.zPageContacts.zToolbarPressButton(Button.B_NEW);

		// Fill in the form
		formContactNew.zFillField(Field.FirstName, contactFirst);
		formContactNew.zFillField(Field.LastName, contactLast);
		formContactNew.zFillField(Field.Email, contactEmail);

		if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled",
				ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
			app.zPageContacts.zClickAt(
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
