/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.contactgroups;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.ContactGroupItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.ui.addressbook.*;
import com.zimbra.qa.selenium.projects.ajax.ui.addressbook.FormContactGroupNew.Field;


public class EditContactGroup extends AjaxCommonTest  {
	public EditContactGroup() {
		logger.info("New "+ EditContactGroup.class.getCanonicalName());

		// All tests start at the Address page
		super.startingPage =  app.zPageAddressbook;
		// Make sure we are using an account with conversation view
		super.startingAccountPreferences = null;		

	}
	@Bugs(ids="97157")
	@Test(description = "Edit a contact group by click Edit on Toolbar button", groups = { "smoke" })
	public void EditContactGroup_01() throws HarnessException {

		// A new group name
		String newname = "edit" + ZimbraSeleniumProperties.getUniqueString();
		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		
		//-Refresh
		app.zPageAddressbook.zRefresh();

		// Select the contact group
		app.zPageAddressbook.zListItem(Action.A_LEFTCLICK, group.getName());

		//Click Edit on Toolbar button	
		FormContactGroupNew formContactGroupNew = (FormContactGroupNew) app.zPageAddressbook.zToolbarPressButton(Button.B_EDIT);

		// CHange the group name
		formContactGroupNew.zFillField(Field.GroupName, newname);
		formContactGroupNew.zSubmit();
		
		//-- Verification
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "#nickname:"+ newname);
		ZAssert.assertNotNull(actual, "Verify the contact group with new name exists");
		}

	@Bugs(ids="97157")
	@Test(description = "Edit a contact group by click Edit Group on Context Menu ", groups = { "functional" })
	public void EditContactGroup_02() throws HarnessException {

		// A new group name
		String newname = "edit" + ZimbraSeleniumProperties.getUniqueString();
		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		
		// Refresh
		app.zPageAddressbook.zRefresh();

		// Right click -> Edit	
		FormContactGroupNew formContactGroupNew = (FormContactGroupNew) app.zPageAddressbook.zListItem(Action.A_RIGHTCLICK, Button.B_EDIT, group.getName());        

		// CHange the group name
		formContactGroupNew.zFillField(Field.GroupName, newname);
		formContactGroupNew.zSubmit();

		//-- Verification
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "#nickname:"+ newname);
		ZAssert.assertNotNull(actual, "Verify the contact group with new name exists");

	}

	@Bugs(ids="97157")
	@Test(description = "Edit a contact group by double click on the contact group  ", groups = { "functional" })
	public void EditContactGroup_03() throws HarnessException {

		// A new group name
		String newname = "edit" + ZimbraSeleniumProperties.getUniqueString();

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageAddressbook.zRefresh();

		// Double click	
		FormContactGroupNew formContactGroupNew = (FormContactGroupNew) app.zPageAddressbook.zListItem(Action.A_DOUBLECLICK, group.getName());        

		// CHange the group name
		formContactGroupNew.zFillField(Field.GroupName, newname);
		formContactGroupNew.zSubmit();

		//-- Verification
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "#nickname:"+ newname);
		ZAssert.assertNotNull(actual, "Verify the contact group with new name exists");

	}

	@Test(description = "Cancel Editing a contact group by click Close", groups = { "functional" })
	public void EditContactGroup_04() throws HarnessException {
		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		
		// Refresh
		app.zPageAddressbook.zRefresh();

		// Select the contact group
		app.zPageAddressbook.zListItem(Action.A_LEFTCLICK, group.getName());

		//Click Edit on Toolbar button	
		FormContactGroupNew formContactGroupNew = (FormContactGroupNew) app.zPageAddressbook.zToolbarPressButton(Button.B_EDIT);

		// CHange the group name
		formContactGroupNew.zToolbarPressButton(Button.B_CLOSE);
		//-- Verification

		// Verify the form closed
		ZAssert.assertFalse(formContactGroupNew.zIsActive(), "Verify the form closed after clicking CLOSE");

		// Verify the contact still exists
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "#nickname:"+ group.getName());
		ZAssert.assertNotNull(actual, "Verify the contact group with new name exists");

	}

	@Test(description = "Cancel an edited contact group by click Close, then click No", groups = { "functional" })
	public void EditContactGroup_05() throws HarnessException {

		//--  Data

		// A new group name
		String newname = "edit" + ZimbraSeleniumProperties.getUniqueString();

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageAddressbook.zRefresh();

		// Select the contact group
		app.zPageAddressbook.zListItem(Action.A_LEFTCLICK, group.getName());

		//Click Edit on Toolbar button	
		FormContactGroupNew formContactGroupNew = (FormContactGroupNew) app.zPageAddressbook.zToolbarPressButton(Button.B_EDIT);

		// CHange the group name
		formContactGroupNew.zFillField(Field.GroupName, newname);
		DialogWarning dialog = (DialogWarning) formContactGroupNew.zToolbarPressButton(Button.B_CLOSE);

		// Wait for the dialog to appear
		dialog.zWaitForActive();

		// Click No: Don't save changes
		dialog.zClickButton(Button.B_NO);

		//-- Verification

		// Verify the form closed
		ZAssert.assertFalse(formContactGroupNew.zIsActive(), "Verify the form closed after clicking CLOSE");

		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "#nickname:"+ group.getName());
		ZAssert.assertNotNull(actual, "Verify the contact group with new name exists");

		actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "#nickname:"+ newname);
		ZAssert.assertNull(actual, "Verify the contact group with new name exists");
		}
	@Bugs(ids="97157")
	@Test(description = "Cancel an edited contact by click Close, then click Cancel", groups = { "functional" })
	public void EditContactGroup_06() throws HarnessException {

		// A new group name
		String newname = "edit" + ZimbraSeleniumProperties.getUniqueString();

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		//-- GUI
		// Refresh
		app.zPageAddressbook.zRefresh();

		// Select the contact group
		app.zPageAddressbook.zListItem(Action.A_LEFTCLICK, group.getName());

		//Click Edit on Toolbar button	
		FormContactGroupNew formContactGroupNew = (FormContactGroupNew) app.zPageAddressbook.zToolbarPressButton(Button.B_EDIT);

		// CHange the group name
		formContactGroupNew.zFillField(Field.GroupName, newname);
		DialogWarning dialog = (DialogWarning) formContactGroupNew.zToolbarPressButton(Button.B_CLOSE);

		// Wait for the dialog to appear
		dialog.zWaitForActive();

		// Click Cancel: Save changes?
		dialog.zClickButton(Button.B_CANCEL);

		// Click Save
		formContactGroupNew.zToolbarPressButton(Button.B_SAVE);

		//-- Verification
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "#nickname:"+ newname);
		ZAssert.assertNotNull(actual, "Verify the contact group with new name exists");
		}
	@Bugs(ids="97157")
	@Test(description = "Cancel an edited contact by click Close, then click Yes", groups = { "functional" })
	public void EditContactGroup_07() throws HarnessException {

		//--  Data
		// A new group name
		String newname = "edit" + ZimbraSeleniumProperties.getUniqueString();

		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		//-- GUI

		// Refresh
		app.zPageAddressbook.zRefresh();

		// Select the contact group
		app.zPageAddressbook.zListItem(Action.A_LEFTCLICK, group.getName());

		//Click Edit on Toolbar button	
		FormContactGroupNew formContactGroupNew = (FormContactGroupNew) app.zPageAddressbook.zToolbarPressButton(Button.B_EDIT);

		// CHange the group name
		formContactGroupNew.zFillField(Field.GroupName, newname);
		DialogWarning dialog = (DialogWarning) formContactGroupNew.zToolbarPressButton(Button.B_CLOSE);

		// Wait for the dialog to appear
		dialog.zWaitForActive();

		// Click Yes: Don't save changes
		dialog.zClickButton(Button.B_YES);

		//-- Verification
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "#nickname:"+ newname);
		ZAssert.assertNotNull(actual, "Verify the contact group with new name exists");
		}
	} 

