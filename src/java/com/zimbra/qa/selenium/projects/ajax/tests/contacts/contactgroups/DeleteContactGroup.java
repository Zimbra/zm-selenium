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

import java.awt.event.KeyEvent;
import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.contacts.FormContactGroupNew;

public class DeleteContactGroup extends AjaxCommonTest  {
	
	public DeleteContactGroup() {
		logger.info("New "+ DeleteContactGroup.class.getCanonicalName());
		
		super.startingPage = app.zPageContacts;
		super.startingAccountPreferences = new HashMap<String , String>() {
		private static final long serialVersionUID = -263733102718446576L;	{
			put("zimbraPrefShowSelectionCheckbox", "TRUE");		         
		}};				
	}
	
	
	@Test( description = "Delete a contact group by click Delete button on toolbar", groups = { "smoke" })
	
	public void ClickDeleteOnToolbar_01() throws HarnessException {

		//--  Data
		
		// The trash folder
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		
		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		
		//-- GUI
		
		// Refresh
		app.zPageContacts.zRefresh();
		
		// Select the contact group
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, group.getName());
		
        //delete contact group by click Delete button on toolbar
        app.zPageContacts.zToolbarPressButton(Button.B_DELETE);

        
        //-- Verification
        
        ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere #nickname:"+ group.getName());
        ZAssert.assertNotNull(actual, "Verify the contact group exists");

        // Verify the contact group is in the trash
        ZAssert.assertEquals(actual.getFolderId(), trash.getId(), "Verify the contact group is in the trash");
        
   	}

	
	@Test( description = "Delete a contact group by click Delete on Context Menu",	groups = { "functional" })
	
	public void ClickDeleteOnContextMenu_02() throws HarnessException {

		//--  Data
		
		// The trash folder
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		
		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		
		//-- GUI
		
		// Refresh
		app.zPageContacts.zRefresh();

		//delete contact group by click Delete on Context menu
        app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_DELETE, group.getName());

        //-- Verification
        
        ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere #nickname:"+ group.getName());
        ZAssert.assertNotNull(actual, "Verify the contact group exists");
        
        // Verify the contact group is in the trash
        ZAssert.assertEquals(actual.getFolderId(), trash.getId(), "Verify the contact group is in the trash");
        
   	}
	

	@Test( description = "Delete a contact group selected by checkbox by click Delete button on toolbar", groups = { "functional" })
	
	public void DeleteContactGroupSelectedWithCheckbox_03() throws HarnessException {

		//--  Data
		
		// The trash folder
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		
		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		
		//-- GUI
		
		// Refresh
		app.zPageContacts.zRefresh();
		
		// Select the contact group
		//app.zPageContacts.zListItem(Action.A_CHECKBOX, group.getName());
		
        //delete contact group by click Delete button on toolbar
        app.zPageContacts.zToolbarPressButton(Button.B_DELETE);

        //-- Verification
        
        ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere #nickname:"+ group.getName());
        ZAssert.assertNotNull(actual, "Verify the contact group exists");
        
        // Verify the contact group is in the trash
        ZAssert.assertEquals(actual.getFolderId(), trash.getId(), "Verify the contact group is in the trash");
        
   	}
	

	@Test( description = "Delete a contact group use shortcut Del", groups = { "functional" })
	
	public void UseShortcutDel_04() throws HarnessException {

		//--  Data
		
		// The trash folder
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		
		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		
		//-- GUI
		
		// Refresh
		app.zPageContacts.zRefresh();
		
		// Select the contact group
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, group.getName());
		
        //delete contact group by click shortcut Del
		app.zPageContacts.zKeyboardKeyEvent(KeyEvent.VK_DELETE);

        //-- Verification
        
        ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere #nickname:"+ group.getName());
        ZAssert.assertNotNull(actual, "Verify the contact group exists");

        // Verify the contact group is in the trash
        ZAssert.assertEquals(actual.getFolderId(), trash.getId(), "Verify the contact group is in the trash");
        
   	}
	
	
	@Test( description = "Delete a contact group use shortcut backspace", groups = { "functional" })
	
	public void  UseShortcutBackspace_05() throws HarnessException {

		//--  Data
		
		// The trash folder
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		
		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		
		//-- GUI
		
		// Refresh
		app.zPageContacts.zRefresh();
		
		// Select the contact group
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, group.getName());
		
        //delete contact group by click shortcut Del
		app.zPageContacts.zKeyboardKeyEvent(KeyEvent.VK_BACK_SPACE);

        //-- Verification
        
        ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere #nickname:"+ group.getName());
        ZAssert.assertNotNull(actual, "Verify the contact group exists");

        // Verify the contact group is in the trash
        ZAssert.assertEquals(actual.getFolderId(), trash.getId(), "Verify the contact group is in the trash");

   	}

	
	@Test( description = "Delete multiple contact groups at once",	groups = { "functional" })
	
	public void DeleteMultipleContactGroups_06() throws HarnessException {

		//--  Data
		
		// The trash folder
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		
		// Create a contact group
		ContactGroupItem group1 = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		ContactGroupItem group2 = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		ContactGroupItem group3 = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		
		//-- GUI
		
		// Refresh
		app.zPageContacts.zRefresh();
		
		// Select the contact group
		app.zPageContacts.zListItem(Action.A_CHECKBOX, group3.getName());
		app.zPageContacts.zListItem(Action.A_CHECKBOX, group1.getName());
		app.zPageContacts.zListItem(Action.A_CHECKBOX, group2.getName());
		
        //delete contact group by click Delete button on toolbar
        app.zPageContacts.zToolbarPressButton(Button.B_DELETE);

        //-- Verification
        
        ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere #nickname:"+ group1.getName());
        ZAssert.assertNotNull(actual, "Verify the contact group exists");
        ZAssert.assertEquals(actual.getFolderId(), trash.getId(), "Verify the contact group is in the trash");

        actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere #nickname:"+ group2.getName());
        ZAssert.assertNotNull(actual, "Verify the contact group exists");
        ZAssert.assertEquals(actual.getFolderId(), trash.getId(), "Verify the contact group is in the trash");

        actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere #nickname:"+ group3.getName());
        ZAssert.assertNotNull(actual, "Verify the contact group exists");
        ZAssert.assertEquals(actual.getFolderId(), trash.getId(), "Verify the contact group is in the trash"); 

	}
	

	@Test( description = "Delete contact + contact group at once",	groups = { "functional" })
	
	public void DeleteMixOfContactAndGroup_07() throws HarnessException {

		//--  Data
		
		// The trash folder
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		
		// Create a contact group
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		//-- GUI
		
		// Refresh
		app.zPageContacts.zRefresh();
		
		// Select the contact group		
		app.zPageContacts.zListItem(Action.A_CHECKBOX, group.getName());
		app.zPageContacts.zListItem(Action.A_CHECKBOX, contact.getName());

        //delete contact group by click Delete button on toolbar
        app.zPageContacts.zToolbarPressButton(Button.B_DELETE);

        //-- Verification
        
        ContactGroupItem actual1 = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere #nickname:"+ group.getName());
        ZAssert.assertNotNull(actual1, "Verify the contact group exists");
        ZAssert.assertEquals(actual1.getFolderId(), trash.getId(), "Verify the contact group is in the trash");

        ContactItem actual2 = ContactItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere #firstname:"+ contact.firstName);
        ZAssert.assertNotNull(actual2, "Verify the contact exists");
        ZAssert.assertEquals(actual2.getFolderId(), trash.getId(), "Verify the contact is in the trash");

	}
	
	
	@Test( description = "Move a contact group to folder Trash by expand Move dropdown then select Trash", groups = { "functional" })
	
	public void MoveToTrashFromMoveDropdownOnToolbar_08() throws HarnessException {

		//--  Data
		
		// The trash folder
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		
		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		
		//-- GUI
		
		// Refresh
		app.zPageContacts.zRefresh();
		
		// Select the contact group
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, group.getName());
		
        //delete contact group by click Delete button on toolbar
        app.zPageContacts.zToolbarPressPulldown(Button.B_MOVE, trash);

        
        //-- Verification
        
        ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere #nickname:"+ group.getName());
        ZAssert.assertNotNull(actual, "Verify the contact group exists");
        ZAssert.assertEquals(actual.getFolderId(), trash.getId(), "Verify the contact group is in the trash");

   	}
	
	
	@Test( description = "Click Delete Toolbar button in Edit Contact Group form",
			groups = { "functional" })
	
	public void DeleteContactGroupFromEditContactGroupUI_09() throws HarnessException {
		
		//-- Data
		
		// The trash folder
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		
		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		
		//-- GUI
		
		// Refresh
		app.zPageContacts.zRefresh();
		
		// Edit the group
		FormContactGroupNew form = (FormContactGroupNew)app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_EDIT, group.getName());
		
		// In the form, click "Delete"
		form.zToolbarPressButton(Button.B_DELETE);

		//-- Verification
		
		// Verify the group is in the trash
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere "+ group.getName());
		ZAssert.assertNotNull(actual, "Verify the group stil exists");
		
		ZAssert.assertEquals(actual.getFolderId(), trash.getId(), "Verify the group is located in trash");

   	}
	

}
