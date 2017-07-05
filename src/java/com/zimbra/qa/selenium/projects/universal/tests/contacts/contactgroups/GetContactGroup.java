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
package com.zimbra.qa.selenium.projects.universal.tests.contacts.contactgroups;

import java.util.*;
import java.util.Map.Entry;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.contacts.PageContacts;

/**
 * These test cases verify the contact lists display the correct contact gropus
 * 
 * @author Matt Rhoades
 */

public class GetContactGroup extends AjaxCommonTest {

	public GetContactGroup() {
		logger.info("New " + GetContactGroup.class.getCanonicalName());
		super.startingPage = app.zPageContacts;

	}

	@Test(description = "Click Alphabetbar button All: Verify contact groups started with digit and A-Z listed", 
			groups = {"smoke", "L0" })

	public void GetContactGroup_All_Button_01() throws HarnessException {

		String groupname;

		// -- Data
		String member = "email" + ConfigProperties.getUniqueString() + "@example.com";

		// Create three contact groups

		groupname = "Bp" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount()
				.soapSend("<CreateContactRequest xmlns='urn:zimbraMail'>" + "<cn >" + "<a n='type'>group</a>"
						+ "<a n='nickname'>" + groupname + "</a>" + "<a n='fileAs'>8:" + groupname + "</a>"
						+ "<m type='I' value='" + member + "' />" + "</cn>" + "</CreateContactRequest>");
		ContactGroupItem group1 = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), groupname);

		groupname = "5" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount()
				.soapSend("<CreateContactRequest xmlns='urn:zimbraMail'>" + "<cn >" + "<a n='type'>group</a>"
						+ "<a n='nickname'>" + groupname + "</a>" + "<a n='fileAs'>8:" + groupname + "</a>"
						+ "<m type='I' value='" + member + "' />" + "</cn>" + "</CreateContactRequest>");
		ContactGroupItem group2 = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), groupname);

		groupname = "b" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount()
				.soapSend("<CreateContactRequest xmlns='urn:zimbraMail'>" + "<cn >" + "<a n='type'>group</a>"
						+ "<a n='nickname'>" + groupname + "</a>" + "<a n='fileAs'>8:" + groupname + "</a>"
						+ "<m type='I' value='" + member + "' />" + "</cn>" + "</CreateContactRequest>");
		ContactGroupItem group3 = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), groupname);

		// -- GUI

		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// click All
		app.zPageContacts.zToolbarPressButton(Button.B_AB_ALL);

		// -- Verification

		// Verify group name and members displayed
		List<ContactItem> items = app.zPageContacts.zListGetContacts();

		boolean found1 = false;
		boolean found2 = false;
		boolean found3 = false;

		for (ContactItem item : items) {

			if (item.getName().equals(group1.getName())) {
				found1 = true;
			}
			if (item.getName().equals(group2.getName())) {
				found2 = true;
			}
			if (item.getName().equals(group3.getName())) {
				found3 = true;
			}

		}

		ZAssert.assertTrue(found1, "Verify contact group starting with B is listed");
		ZAssert.assertTrue(found2, "Verify contact group starting with 5 is listed");
		ZAssert.assertTrue(found3, "Verify contact group starting with b is listed");

	}

	@Test(description = "Click Alphabetbar button All: Verify contact groups started with digit and A-Z listed", 
			groups = {"smoke", "L0"})
	public void GetContactGroup_123_Button_02() throws HarnessException {

		String groupname;

		// -- Data
		String member = "email" + ConfigProperties.getUniqueString() + "@example.com";

		// Create three contact groups

		groupname = "Bp" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount()
				.soapSend("<CreateContactRequest xmlns='urn:zimbraMail'>" + "<cn >" + "<a n='type'>group</a>"
						+ "<a n='nickname'>" + groupname + "</a>" + "<a n='fileAs'>8:" + groupname + "</a>"
						+ "<m type='I' value='" + member + "' />" + "</cn>" + "</CreateContactRequest>");
		ContactGroupItem group1 = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), groupname);

		groupname = "5" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount()
				.soapSend("<CreateContactRequest xmlns='urn:zimbraMail'>" + "<cn >" + "<a n='type'>group</a>"
						+ "<a n='nickname'>" + groupname + "</a>" + "<a n='fileAs'>8:" + groupname + "</a>"
						+ "<m type='I' value='" + member + "' />" + "</cn>" + "</CreateContactRequest>");
		ContactGroupItem group2 = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), groupname);

		groupname = "b" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount()
				.soapSend("<CreateContactRequest xmlns='urn:zimbraMail'>" + "<cn >" + "<a n='type'>group</a>"
						+ "<a n='nickname'>" + groupname + "</a>" + "<a n='fileAs'>8:" + groupname + "</a>"
						+ "<m type='I' value='" + member + "' />" + "</cn>" + "</CreateContactRequest>");
		ContactGroupItem group3 = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), groupname);

		// -- GUI

		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// click All
		app.zPageContacts.zToolbarPressButton(Button.B_AB_123);

		// -- Verification

		// Verify group name and members displayed
		List<ContactItem> items = app.zPageContacts.zListGetContacts();

		boolean found1 = false;
		boolean found2 = false;
		boolean found3 = false;

		for (ContactItem item : items) {

			if (item.getName().equals(group1.getName())) {
				found1 = true;
			}
			if (item.getName().equals(group2.getName())) {
				found2 = true;
			}
			if (item.getName().equals(group3.getName())) {
				found3 = true;
			}

		}

		ZAssert.assertFalse(found1, "Verify contact group starting with B is not listed");
		ZAssert.assertTrue(found2, "Verify contact group starting with 5 is listed");
		ZAssert.assertFalse(found3, "Verify contact group starting with b is not listed");

	}

	@Bugs(ids = "100227")
	@Test(description = "Click Alphabetbar button Z: Verify only contact groups started with Z|z is listed", 
			groups = {"functional", "L2"})
	public void GetContactGroup_B_Button_03() throws HarnessException {

		String groupname;

		// -- Data
		String member = "email" + ConfigProperties.getUniqueString() + "@example.com";

		// Create three contact groups

		groupname = "Bp" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount()
				.soapSend("<CreateContactRequest xmlns='urn:zimbraMail'>" + "<cn >" + "<a n='type'>group</a>"
						+ "<a n='nickname'>" + groupname + "</a>" + "<a n='fileAs'>8:" + groupname + "</a>"
						+ "<m type='I' value='" + member + "' />" + "</cn>" + "</CreateContactRequest>");
		ContactGroupItem group1 = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), groupname);

		groupname = "5" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount()
				.soapSend("<CreateContactRequest xmlns='urn:zimbraMail'>" + "<cn >" + "<a n='type'>group</a>"
						+ "<a n='nickname'>" + groupname + "</a>" + "<a n='fileAs'>8:" + groupname + "</a>"
						+ "<m type='I' value='" + member + "' />" + "</cn>" + "</CreateContactRequest>");
		ContactGroupItem group2 = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), groupname);

		groupname = "b" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount()
				.soapSend("<CreateContactRequest xmlns='urn:zimbraMail'>" + "<cn >" + "<a n='type'>group</a>"
						+ "<a n='nickname'>" + groupname + "</a>" + "<a n='fileAs'>8:" + groupname + "</a>"
						+ "<m type='I' value='" + member + "' />" + "</cn>" + "</CreateContactRequest>");
		ContactGroupItem group3 = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), groupname);

		// -- GUI

		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// click All
		app.zPageContacts.zToolbarPressButton(Button.B_AB_B);

		// -- Verification

		// Verify group name and members displayed
		List<ContactItem> items = app.zPageContacts.zListGetContacts();

		boolean found1 = false;
		boolean found2 = false;
		boolean found3 = false;

		for (ContactItem item : items) {

			if (item.getName().equals(group1.getName())) {
				found1 = true;
			}
			if (item.getName().equals(group2.getName())) {
				found2 = true;
			}
			if (item.getName().equals(group3.getName())) {
				found3 = true;
			}

		}

		ZAssert.assertTrue(found1, "Verify contact group starting with B is listed");
		ZAssert.assertFalse(found2, "Verify contact group starting with 5 is not listed");
		ZAssert.assertTrue(found3, "Verify contact group starting with b is listed");

	}

	@Test(description = "Click all Alphabetbar buttons: Verify only contact group started with the alphabet is listed", 
			groups = {"functional", "L2"})
	public void GetContactGroup_Iterate_Buttons_04() throws HarnessException {

		// -- Data

		// A map of buttons to ContactGroupItem
		HashMap<Button, ContactGroupItem> groups = new HashMap<Button, ContactGroupItem>();

		// Create contact groups with each letter

		for (Entry<Character, Button> entry : PageContacts.buttons.entrySet()) {

			Character c = entry.getKey();
			Button b = entry.getValue();

			String groupname = c + ConfigProperties.getUniqueString();
			app.zGetActiveAccount()
					.soapSend("<CreateContactRequest xmlns='urn:zimbraMail'>" + "<cn >" + "<a n='type'>group</a>"
							+ "<a n='nickname'>" + groupname + "</a>" + "<a n='fileAs'>8:" + groupname + "</a>"
							+ "<m type='I' value='email" + ConfigProperties.getUniqueString() + "@example.com' />"
							+ "</cn>" + "</CreateContactRequest>");
			ContactGroupItem group = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), groupname);

			groups.put(b, group);

		}

		// -- GUI

		// refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// -- Verification

		for (Entry<Button, ContactGroupItem> entry : groups.entrySet()) {

			Button b = entry.getKey();
			ContactGroupItem g = entry.getValue();

			// Click each button
			app.zPageContacts.zToolbarPressButton(b);

			// Verify the group is listed
			boolean found = false;
			for (ContactItem i : app.zPageContacts.zListGetContacts()) {
				if (i.getName().equals(g.getName())) {
					found = true;
				}
			}

			ZAssert.assertTrue(found, "Verify contact group " + g.getName() + " is listed");

		}

	}
}
