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

import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogTag;

public class TagContactGroup extends AjaxCommonTest  {
	
	
	public TagContactGroup() {
		logger.info("New "+ TagContactGroup.class.getCanonicalName());
		
		// All tests start at the Address page
		super.startingPage = app.zPageContacts;

		super.startingAccountPreferences = new HashMap<String , String>() {
		private static final long serialVersionUID = 1L;

		{
		    put("zimbraPrefShowSelectionCheckbox", "TRUE");
		}};			
					
		
	}
	
	@Test( description = "Tag a contact group, click pulldown menu Tag->New Tag",
			groups = { "smoke" })
	public void  ClickPulldownMenuTagNewTag_01() throws HarnessException {
	
		//-- Data
		
		// Create a tag
		String tagName = "tag"+ ConfigProperties.getUniqueString();

		// Create a contact group via Soap then select
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());


		//-- GUI
		
		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);
		
		// Select the contact
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, group.getName());
		
		//click Tag Contact->New Tag	
        DialogTag dialogTag = (DialogTag) app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_TAG, Button.O_TAG_NEWTAG , group.getName());        
    	dialogTag.zSetTagName(tagName);
		dialogTag.zClickButton(Button.B_OK);		
    	
		
		
		//-- Verification
		
		app.zGetActiveAccount().soapSend(
				"<GetContactsRequest xmlns='urn:zimbraMail' >" +
						"<cn id='"+ group.getId() +"'/>" +
				"</GetContactsRequest>");
		
		String tn = app.zGetActiveAccount().soapSelectValue("//mail:cn", "tn");
		ZAssert.assertNotNull(tn, "Verify the contact has tags");
		ZAssert.assertStringContains(tn, tagName, "Verify the contact is tagged with the correct tag");
		


	           
   	}
	
		
	@Test( description = "Right click then click Tag Contact->New Tag",
			groups = { "functional" })
	public void ClickContextMenuTagGroupNewTag_02() throws HarnessException {
		
		//-- Data
		
		// Create a tag
		String tagName = "tag"+ ConfigProperties.getUniqueString();

		// Create a contact group via Soap then select
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());


		//-- GUI
		
		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);
		
		// Click Tag Group on context menu
        DialogTag dialogTag = (DialogTag) app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_TAG, Button.O_TAG_NEWTAG , group.getName());        
		dialogTag.zSetTagName(tagName);
		dialogTag.zClickButton(Button.B_OK);		
    	
		
		
		//-- Verification
		
		app.zGetActiveAccount().soapSend(
				"<GetContactsRequest xmlns='urn:zimbraMail' >" +
						"<cn id='"+ group.getId() +"'/>" +
				"</GetContactsRequest>");
		
		String tn = app.zGetActiveAccount().soapSelectValue("//mail:cn", "tn");
		ZAssert.assertNotNull(tn, "Verify the contact has tags");
		ZAssert.assertStringContains(tn, tagName, "Verify the contact is tagged with the correct tag");
		

   	}
	
	@Test( description = "Right click then click Tag Contact Group->a tag name",
			groups = { "functional" })	
	public void ClickContextMenuTagContactExistingTag_03() throws HarnessException {
		
		//-- Data
		
		// Create a tag
		TagItem tagItem = TagItem.CreateUsingSoap(app.zGetActiveAccount());		

		// Create a contact group via Soap then select
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());


		//-- GUI
		
		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);
		
		// Right Click -> Tag -> Existing Tag
		app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_TAG, tagItem, group.getName());        
    	
		
		
		//-- Verification
		
		app.zGetActiveAccount().soapSend(
				"<GetContactsRequest xmlns='urn:zimbraMail' >" +
						"<cn id='"+ group.getId() +"'/>" +
				"</GetContactsRequest>");
		
		String t = app.zGetActiveAccount().soapSelectValue("//mail:cn", "t");
		ZAssert.assertNotNull(t, "Verify the contact has tags");
		ZAssert.assertStringContains(t, tagItem.getId(), "Verify the contact is tagged with the correct tag");
		

	}

	@Test( description = "Click pulldown menu Tag->A tag name",
			groups = { "smoke" })	
	public void ClickPulldownMenuTagExistingTag_04() throws HarnessException {
		
		//-- Data
		
		// Create a tag
		TagItem tagItem = TagItem.CreateUsingSoap(app.zGetActiveAccount());		

		// Create a contact group via Soap then select
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());


		//-- GUI
		
		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);
		
		// Select the contact group
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, group.getName());

		// Tag -> Existing Tag
		app.zPageContacts.zToolbarPressPulldown(Button.B_TAG, tagItem);
    	
		
		
		//-- Verification
		
		app.zGetActiveAccount().soapSend(
				"<GetContactsRequest xmlns='urn:zimbraMail' >" +
						"<cn id='"+ group.getId() +"'/>" +
				"</GetContactsRequest>");
		
		String t = app.zGetActiveAccount().soapSelectValue("//mail:cn", "t");
		ZAssert.assertNotNull(t, "Verify the contact has tags");
		ZAssert.assertStringContains(t, tagItem.getId(), "Verify the contact is tagged with the correct tag");
		

	}
	
	@Test( description = "Double tag a group ",
			groups = { "functional" })	
	public void DoubleTag_05() throws HarnessException {
		
		//-- Data
		
		// Create a tag
		TagItem tag1 = TagItem.CreateUsingSoap(app.zGetActiveAccount());		
		TagItem tag2 = TagItem.CreateUsingSoap(app.zGetActiveAccount());		

		// Create a contact group via Soap then select
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());


		//-- GUI
		
		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);
		
		// Select the contact group
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, group.getName());

		// Tag -> Existing Tag
		app.zPageContacts.zToolbarPressPulldown(Button.B_TAG, tag1);
		app.zPageContacts.zToolbarPressPulldown(Button.B_TAG, tag2);
    	
		
		
		//-- Verification
		
		app.zGetActiveAccount().soapSend(
				"<GetContactsRequest xmlns='urn:zimbraMail' >" +
						"<cn id='"+ group.getId() +"'/>" +
				"</GetContactsRequest>");
		
		String t = app.zGetActiveAccount().soapSelectValue("//mail:cn", "t");
		ZAssert.assertNotNull(t, "Verify the contact has tags");
		ZAssert.assertStringContains(t, tag1.getId(), "Verify the contact is tagged with the correct tag");
		ZAssert.assertStringContains(t, tag2.getId(), "Verify the contact is tagged with the correct tag");
		
	}
	@Test( description = "Tag a contact group by dnd on an existing tag",
			groups = { "functional" })
	public void DnDOnExistingTag_06() throws HarnessException {
		
		//-- Data
		
		// Create a tag
		TagItem tagItem = TagItem.CreateUsingSoap(app.zGetActiveAccount());		

		// Create a contact group via Soap then select
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());


		//-- GUI
		
		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);
		
	    // Dnd on the new tag
		app.zPageContacts.zDragAndDrop(
				"css=[id=zlif__CNS-main__" + group.getId() + "__fileas]:contains("+ group.getName() + ")",
				"css=div[id=main_Contacts-parent-TAG] div[id=ztih__main_Contacts__TAG] td[id^=zti__main_Contacts__][id$=_textCell]:contains("+ tagItem.getName() + ")");
    	
		
		
		//-- Verification
		
		app.zGetActiveAccount().soapSend(
				"<GetContactsRequest xmlns='urn:zimbraMail' >" +
						"<cn id='"+ group.getId() +"'/>" +
				"</GetContactsRequest>");
		
		String t = app.zGetActiveAccount().soapSelectValue("//mail:cn", "t");
		ZAssert.assertNotNull(t, "Verify the contact has tags");
		ZAssert.assertStringContains(t, tagItem.getId(), "Verify the contact is tagged with the correct tag");
		


			  
   	}
}

