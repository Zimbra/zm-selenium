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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.tags;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.ContactsPrefShowSelectionCheckbox;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogTag;

public class TagContact extends  ContactsPrefShowSelectionCheckbox  {
	public TagContact() {
		logger.info("New "+ TagContact.class.getCanonicalName());
		
		// All tests start at the Address page
		super.startingPage = app.zPageContacts;

		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox", "FALSE");		
		
	}
	

	@BeforeClass( groups = { "always" } )
	public void TagContactBeforeClass() throws HarnessException {
		logger.info("TagContactBeforeClass: start");
		ZimbraAccount.ResetAccountZWC();
		logger.info("TagContactBeforeClass: finish");
	}
	
	@Test( description = "Tag a contact, click pulldown menu Tag->New Tag",
			groups = { "smoke" })
	public void ClickPulldownMenuTagNewTag() throws HarnessException {
		
		// Work around due to duplicate dialog ids
		app.zPageMain.sRefresh();
		app.zPageContacts.zNavigateTo();
				
		//-- Data
		
		// Tag Name
		String tagName = "tag"+ ConfigProperties.getUniqueString();

		// Create a contact via Soap then select
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());


		//-- GUI
		
		// Refresh
		app.zPageContacts.zRefresh();
		
		// Select the contact
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, contact.firstName);
		
		//click Tag Contact->New Tag	
		DialogTag dialogTag = (DialogTag) app.zPageContacts.zToolbarPressPulldown(Button.B_TAG, Button.O_TAG_NEWTAG);
    	dialogTag.zSetTagName(tagName);
		dialogTag.zClickButton(Button.B_OK);		
    	
		
		
		//-- Verification
		
		app.zGetActiveAccount().soapSend(
				"<GetContactsRequest xmlns='urn:zimbraMail' >" +
						"<cn id='"+ contact.getId() +"'/>" +
				"</GetContactsRequest>");
		
		String tn = app.zGetActiveAccount().soapSelectValue("//mail:cn", "tn");
		ZAssert.assertNotNull(tn, "Verify the contact has tags");
		ZAssert.assertStringContains(tn, tagName, "Verify the contact is tagged with the correct tag");
		
   	}
	
	@Test( description = "Right click then click Tag Contact->New Tag",
			groups = { "smoke" })	
	public void ClickContextMenuTagContactNewTag() throws HarnessException {
		
		
		//-- Data
		
		// Tag Name
		String tagName = "tag"+ ConfigProperties.getUniqueString();

		// Create a contact via Soap then select
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());


		//-- GUI
		
		// Refresh
		app.zPageContacts.zRefresh();		
		SleepUtil.sleepMedium();
		
		// Select the contact
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, contact.firstName);
		
		//click Tag Contact->New Tag	
        DialogTag dialogTag = (DialogTag) app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_TAG, Button.O_TAG_NEWTAG , contact.fileAs);        
    	dialogTag.zSetTagName(tagName);
		dialogTag.zClickButton(Button.B_OK);		
    	
		
		
		//-- Verification
		
		app.zGetActiveAccount().soapSend(
				"<GetContactsRequest xmlns='urn:zimbraMail' >" +
						"<cn id='"+ contact.getId() +"'/>" +
				"</GetContactsRequest>");
		
		String tn = app.zGetActiveAccount().soapSelectValue("//mail:cn", "tn");
		ZAssert.assertNotNull(tn, "Verify the contact has tags");
		ZAssert.assertStringContains(tn, tagName, "Verify the contact is tagged with the correct tag");
		

	}

	@Test( description = "Right click then click Tag Contact->a tag name",
			groups = { "functional" })	
	public void ClickContextMenuTagContactExistingTag() throws HarnessException {
		
		
		//-- Data
		
		// Create a tag
		TagItem tagItem = TagItem.CreateUsingSoap(app.zGetActiveAccount());		

		// Create a contact via Soap then select
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());


		//-- GUI
		
		// Refresh
		app.zPageContacts.zRefresh();
		
		// Select the contact
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, contact.firstName);
		
		//click Tag Contact->the tag name
		app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_TAG, tagItem, contact.fileAs);        
    	
		
		
		//-- Verification
		
		app.zGetActiveAccount().soapSend(
				"<GetContactsRequest xmlns='urn:zimbraMail' >" +
						"<cn id='"+ contact.getId() +"'/>" +
				"</GetContactsRequest>");
		
		String t = app.zGetActiveAccount().soapSelectValue("//mail:cn", "t");
		ZAssert.assertNotNull(t, "Verify the contact has tags");
		ZAssert.assertStringContains(t, tagItem.getId(), "Verify the contact is tagged with the correct tag");
		
	}

	@Test( description = "click pulldown menu Tag->A tag name",
			groups = { "smoke" })	
	public void ClickPulldownMenuTagExistingTag() throws HarnessException {
		
		
		//-- Data
		
		// Create a tag
		TagItem tagItem = TagItem.CreateUsingSoap(app.zGetActiveAccount());		

		// Create a contact via Soap then select
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());


		//-- GUI
		
		// Refresh
		app.zPageContacts.zRefresh();
		
		// Select the contact
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, contact.firstName);
		
		// select the tag
		app.zPageContacts.zToolbarPressPulldown(Button.B_TAG, tagItem);
    	
		
		
		//-- Verification
		
		app.zGetActiveAccount().soapSend(
				"<GetContactsRequest xmlns='urn:zimbraMail' >" +
						"<cn id='"+ contact.getId() +"'/>" +
				"</GetContactsRequest>");
		
		String t = app.zGetActiveAccount().soapSelectValue("//mail:cn", "t");
		ZAssert.assertNotNull(t, "Verify the contact has tags");
		ZAssert.assertStringContains(t, tagItem.getId(), "Verify the contact is tagged with the correct tag");
		
	}

	@Test( description = "Double tag a contact ",
			groups = { "functional" })	
	public void DoubleTag() throws HarnessException {
		
		
		//-- Data
		
		// Create a tag
		TagItem tag1 = TagItem.CreateUsingSoap(app.zGetActiveAccount());		
		TagItem tag2 = TagItem.CreateUsingSoap(app.zGetActiveAccount());		

		// Create a contact via Soap then select
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());


		//-- GUI
		
		// Refresh
		app.zPageContacts.zRefresh();
		
		// Select the contact
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, contact.firstName);
		
		// select the tag
		app.zPageContacts.zToolbarPressPulldown(Button.B_TAG, tag1);
		app.zPageContacts.zToolbarPressPulldown(Button.B_TAG, tag2);
    	
		
		
		//-- Verification
		
		app.zGetActiveAccount().soapSend(
				"<GetContactsRequest xmlns='urn:zimbraMail' >" +
						"<cn id='"+ contact.getId() +"'/>" +
				"</GetContactsRequest>");
		
		String t = app.zGetActiveAccount().soapSelectValue("//mail:cn", "t");
		ZAssert.assertNotNull(t, "Verify the contact has tags");
		ZAssert.assertStringContains(t, tag1.getId(), "Verify the contact is tagged with the correct tag");
		ZAssert.assertStringContains(t, tag2.getId(), "Verify the contact is tagged with the correct tag");
		
	}

	
	@Test( description = "Tag a contact by dnd on an existing tag",
			groups = { "functional" })
	public void DnDOnExistingTag() throws HarnessException {
		
		
		//-- Data
		
		// Create a tag
		TagItem tagItem = TagItem.CreateUsingSoap(app.zGetActiveAccount());		

		// Create a contact via Soap then select
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());


		//-- GUI
		
		// Refresh
		app.zPageContacts.zRefresh();
		
	    // Dnd on the new tag
		app.zPageContacts.zDragAndDrop(
				"css=[id=zlif__CNS-main__" + contact.getId() + "__fileas]:contains("+ contact.fileAs + ")",
				"css=div[id=main_Contacts-parent-TAG] div[id=ztih__main_Contacts__TAG] td[id^=zti__main_Contacts__][id$=_textCell]:contains("+ tagItem.getName() + ")");
    	
		
		
		//-- Verification
		
		app.zGetActiveAccount().soapSend(
				"<GetContactsRequest xmlns='urn:zimbraMail' >" +
						"<cn id='"+ contact.getId() +"'/>" +
				"</GetContactsRequest>");
		
		String t = app.zGetActiveAccount().soapSelectValue("//mail:cn", "t");
		ZAssert.assertNotNull(t, "Verify the contact has tags");
		ZAssert.assertStringContains(t, tagItem.getId(), "Verify the contact is tagged with the correct tag");
		
   	}



}

