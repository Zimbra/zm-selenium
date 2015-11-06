package com.zimbra.qa.selenium.projects.ajax.tests.contacts.bugs;

/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014 Zimbra, Inc.
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

import java.util.*;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

public class Bug101623_app_contacts extends AjaxCommonTest {
	public Bug101623_app_contacts() {
		logger.info("New " + Bug101623_app_contacts.class.getCanonicalName());

		// All tests start at the Address page
		super.startingPage = app.zPageAddressbook;

		// Enable user preference checkboxes
		super.startingAccountPreferences = null;

	}

	@Test(description = "?app=contacts in url", groups = { "smoke" })
	public void Bug101623AppContacts() throws HarnessException {
		
		
		//Go to Mail tab
				app.zPageMail.zNavigateTo();				
				SleepUtil.sleepMedium();

		String lastname;		

		// Create  contact

		lastname = "B" + ZimbraSeleniumProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" + "<cn >"
						+ "<a n='firstName'>first"
						+ ZimbraSeleniumProperties.getUniqueString() + "</a>"
						+ "<a n='lastName'>" + lastname + "</a>"
						+ "<a n='email'>email@domain.com</a>" + "</cn>"
						+ "</CreateContactRequest>");
		ContactItem contact1 = ContactItem.importFromSOAP(
				app.zGetActiveAccount(), "#lastname:" + lastname);

		// -- GUI

	//	app.zPageAddressbook.zRefresh();	
		
	//	SleepUtil.sleepMedium();

		// Reload the application, with app=contacts query parameter

		ZimbraURI uri = new ZimbraURI(ZimbraURI.getBaseURI());
		uri.addQuery("app", "contacts");
		app.zPageAddressbook.sOpen(uri.toString());
		
		SleepUtil.sleepMedium();

		// click All
		app.zPageAddressbook.zToolbarPressButton(Button.B_AB_ALL);

		// -- Verification of added contacts

		List<ContactItem> items = app.zPageAddressbook.zListGetContacts();

		boolean found1 = false;

		for (ContactItem item : items) {

			if (item.getName().equals(contact1.getName())) {
				found1 = true;
			}

		}

		ZAssert.assertTrue(found1, "Verify contact  is listed");

	}

}
