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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.contactgroups;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.contacts.*;
import com.zimbra.qa.selenium.projects.ajax.ui.contacts.FormContactGroupNew.Field;

public class CreateContactGroup extends AjaxCommonTest  {

	public CreateContactGroup() {
		logger.info("New "+ CreateContactGroup.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	@Test (description = "Create a basic contact group with 2 addresses using  New -> Contact Group",
			groups = { "sanity", "L0" })

	public void CreateContactGroup_01() throws HarnessException {

		String groupName = "group" + ConfigProperties.getUniqueString();
		String member1 = "m" + ConfigProperties.getUniqueString() + "@example.com";
		String member2 = "m" + ConfigProperties.getUniqueString() + "@example.com";

		// Refresh the addressbook
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Open contact group form
		FormContactGroupNew form = (FormContactGroupNew)app.zPageContacts.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_CONTACTGROUP);

		// Fill in group name and email addresses
		form.zFillField(Field.GroupName, groupName);
		form.zFillField(Field.FreeFormAddress, member1);
		form.zFillField(Field.FreeFormAddress, member2);
		form.zSubmit();

		app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='contact'>"
				+		"<query>#nickname:"+ groupName +"</query>"
				+	"</SearchRequest>");
		String contactId = app.zGetActiveAccount().soapSelectValue("//mail:cn", "id");

		ZAssert.assertNotNull(contactId, "Verify the contact is returned in the search");

		app.zGetActiveAccount().soapSend(
				"<GetContactsRequest xmlns='urn:zimbraMail'>"
			+		"<cn id='"+ contactId +"'/>"
			+	"</GetContactsRequest>");

		String nickname = app.zGetActiveAccount().soapSelectValue("//mail:cn//mail:a[@n='nickname']", null);
		String type = app.zGetActiveAccount().soapSelectValue("//mail:cn//mail:a[@n='type']", null);

		ZAssert.assertEquals(nickname, groupName, "Verify the group name is correct");
		ZAssert.assertEquals(type, "group", "Verify the type is set to 'group'");

		boolean found1 = false;
		boolean found2 = false;
		Element[] members = app.zGetActiveAccount().soapSelectNodes("//mail:cn//mail:m");
		for (Element e : members) {
			ZAssert.assertEquals(e.getAttribute("type", "notset"), "I", "Verify member type set to 'I'");
			String address = e.getAttribute("value", "notset");
			if ( address.equals(member1) ) {
				found1 = true;
			}
			if ( address.equals(member2) ) {
				found2 = true;
			}
		}

		ZAssert.assertTrue(found1, "Verify member 1 is in the group");
		ZAssert.assertTrue(found2, "Verify member 2 is in the group");
	}


	@Test (description = "Create a basic contact group with 2 GAL addresses",
			groups = { "smoke", "L0" })

	public void CreateContactGroup_02() throws HarnessException {

		String groupName = "group" + ConfigProperties.getUniqueString();

		// Refresh the addressbook
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Open contact group form
		FormContactGroupNew form = (FormContactGroupNew)app.zPageContacts.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_CONTACTGROUP);

		// Add the group name
		form.zFillField(Field.GroupName, groupName);

	    // Select GAL search
		form.zToolbarPressPulldown(Button.B_CONTACTGROUP_SEARCH_TYPE, Button.O_CONTACTGROUP_SEARCH_GAL);
		form.zFillField(Field.SearchField, ZimbraAccount.AccountA().EmailAddress);
		form.zToolbarPressButton(Button.B_SEARCH);
		form.zToolbarPressButton(Button.B_CONTACTGROUP_ADD_SEARCH_RESULT);

		form.zToolbarPressPulldown(Button.B_CONTACTGROUP_SEARCH_TYPE, Button.O_CONTACTGROUP_SEARCH_GAL);
		form.zFillField(Field.SearchField, ZimbraAccount.AccountB().EmailAddress);
		form.zToolbarPressButton(Button.B_SEARCH);
		form.zToolbarPressButton(Button.B_CONTACTGROUP_ADD_SEARCH_RESULT);

		// Save the group
		form.zSubmit();

		// Verification
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "#nickname:"+ groupName);
		ZAssert.assertNotNull(actual, "Verify the contact group exists in the mailbox");

		ZAssert.assertContains(actual.getMemberList(), new ContactGroupItem.MemberItemGAL(ZimbraAccount.AccountA()), "Verify member 1 is in the group");
		ZAssert.assertContains(actual.getMemberList(), new ContactGroupItem.MemberItemGAL(ZimbraAccount.AccountB()), "Verify member 1 is in the group");
	}


	@Test (description = "Create a contact group with existing contacts",
			groups = { "smoke", "L0" })

	public void CreateContactGroup_03() throws HarnessException {

		// The contact group name
		String groupName = "group" + ConfigProperties.getUniqueString();

		// Create two contacts
		ContactItem contact1 = ContactItem.createContactItem(app.zGetActiveAccount());
		ContactItem contact2 = ContactItem.createContactItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		//open contact group form
		FormContactGroupNew form = (FormContactGroupNew)app.zPageContacts.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_CONTACTGROUP);

		// Fill in group name
		form.zFillField(Field.GroupName, groupName);

	    // Select Contact search
		form.zToolbarPressPulldown(Button.B_CONTACTGROUP_SEARCH_TYPE, Button.O_CONTACTGROUP_SEARCH_CONTACTS);
		form.zFillField(Field.SearchField, contact1.email);
		form.zToolbarPressButton(Button.B_SEARCH);
		form.zToolbarPressButton(Button.B_CONTACTGROUP_ADD_SEARCH_RESULT);

		form.zToolbarPressPulldown(Button.B_CONTACTGROUP_SEARCH_TYPE, Button.O_CONTACTGROUP_SEARCH_CONTACTS);
		form.zFillField(Field.SearchField, contact2.email);
		form.zToolbarPressButton(Button.B_SEARCH);
		form.zToolbarPressButton(Button.B_CONTACTGROUP_ADD_SEARCH_RESULT);

		// Click Save
		form.zSubmit();

		// Verification
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "#nickname:"+ groupName);
		ZAssert.assertNotNull(actual, "Verify the contact group exists in the mailbox");

		ZAssert.assertContains(actual.getMemberList(), new ContactGroupItem.MemberItemContact(contact1), "Verify member 1 is in the group");
		ZAssert.assertContains(actual.getMemberList(), new ContactGroupItem.MemberItemContact(contact1), "Verify member 1 is in the group");
	}


	@Test (description = "Create a contact group with GAL + existing contacts + new emails",
			groups = { "functional", "L2" })

	public void CreateContactGroup_04() throws HarnessException {

		// The contact group name
		String groupName = "group" + ConfigProperties.getUniqueString();

		// Create a contact
		ContactItem contact1 = ContactItem.createContactItem(app.zGetActiveAccount());

		// A general email address
		String member1 = "m" + ConfigProperties.getUniqueString() + "@example.com";

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		//open contact group form
		FormContactGroupNew form = (FormContactGroupNew)app.zPageContacts.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_CONTACTGROUP);

		// Fill in group name
		form.zFillField(Field.GroupName, groupName);

	    // Select Contact search
		form.zToolbarPressPulldown(Button.B_CONTACTGROUP_SEARCH_TYPE, Button.O_CONTACTGROUP_SEARCH_CONTACTS);
		form.zFillField(Field.SearchField, contact1.email);
		form.zToolbarPressButton(Button.B_SEARCH);
		form.zToolbarPressButton(Button.B_CONTACTGROUP_ADD_SEARCH_RESULT);

	    // Select GAL search
		form.zToolbarPressPulldown(Button.B_CONTACTGROUP_SEARCH_TYPE, Button.O_CONTACTGROUP_SEARCH_GAL);
		form.zFillField(Field.SearchField, ZimbraAccount.AccountA().EmailAddress);
		form.zToolbarPressButton(Button.B_SEARCH);
		form.zToolbarPressButton(Button.B_CONTACTGROUP_ADD_SEARCH_RESULT);

		// Add the free-form email
		form.zFillField(Field.FreeFormAddress, member1);

		// Click Save
		form.zSubmit();

		// Verification
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "#nickname:"+ groupName);
		ZAssert.assertNotNull(actual, "Verify the contact group exists in the mailbox");

		ZAssert.assertContains(actual.getMemberList(), new ContactGroupItem.MemberItemContact(contact1), "Verify contact 1 is in the group");
		ZAssert.assertContains(actual.getMemberList(), new ContactGroupItem.MemberItemGAL(ZimbraAccount.AccountA()), "Verify GAL 1 is in the group");
		ZAssert.assertContains(actual.getMemberList(), new ContactGroupItem.MemberItemAddress(member1), "Verify GAL 1 is in the group");
	}


	@Bugs(ids = "77968,70905,66623")
	@Test (description="Create a new contact group from GAL search result",
		groups= { "smoke", "L0" } )

	public void CreateContactGroupFromGALSearchResult_05() throws HarnessException{

		String email=ZimbraAccount.AccountA().EmailAddress.substring(0,ZimbraAccount.AccountA().EmailAddress.indexOf('@'));

		// Work around
		app.zPageMain.zRefreshMainUI();
		app.zPageContacts.zNavigateTo();

		// Search for a GAL
		app.zPageSearch.zToolbarPressPulldown(Button.B_SEARCHTYPE, Button.O_SEARCHTYPE_GAL);
		app.zPageSearch.zAddSearchQuery(email);
		app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);

		// Right click and select New Contact Group
		// Create contact group
		ContactGroupItem newGroup = new ContactGroupItem("group_" + ConfigProperties.getUniqueString().substring(8));

		// Right click on the contact
		DialogNewContactGroup dialog = (DialogNewContactGroup) app.zPageContacts.zListItem(
				Action.A_RIGHTCLICK,
				Button.B_CONTACTGROUP,
				Button.O_NEW_CONTACTGROUP,
				email);

		// Fill in group name
		dialog.zEnterGroupName(newGroup.getName());
		// Save
		dialog.zPressButton(Button.B_OK);

		// Verify toast message 'group created'
		ZAssert.assertStringContains(app.zPageMain.zGetToaster().zGetToastMessage(), "Group Created" , "Verify toast message: '" + "Group Created" + "'");

		// Create a contact item
		ContactItem contactItem = new ContactItem(email);
		contactItem.email = ZimbraAccount.AccountA().EmailAddress;

		//Add the member to the group
		newGroup.addDListMember(contactItem);
		app.zPageContacts.zNavigateTo();

		// Click "Contacts" folder
		FolderItem folder= FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Contacts);
		app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, folder);

		// Verify group name is displayed
		List<ContactItem> contacts = app.zPageContacts.zListGetContacts();
		boolean isFileAsEqual=false;
		for (ContactItem ci : contacts) {
			if (ci.fileAs.equals(ci.fileAs)) {
				isFileAsEqual = true;
				break;
			}
		}
		ZAssert.assertTrue(isFileAsEqual, "Verify group name (" + newGroup.fileAs + ") displayed");
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "#nickname:"+ newGroup.getName());
		ZAssert.assertNotNull(actual, "Verify the group stil exists");
	}
}