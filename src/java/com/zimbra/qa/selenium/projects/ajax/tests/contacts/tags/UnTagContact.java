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

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

public class UnTagContact extends AjaxCommonTest  {
	public UnTagContact() {
		logger.info("New "+ UnTagContact.class.getCanonicalName());
		
		// All tests start at the Address page
		super.startingPage = app.zPageContacts;
		
	}
	
	@Test( description = "Untag a contact by click Toolbar Tag, then select Remove Tag",
			groups = { "smoke", "L1"})
	public void ClickToolbarTagRemoveTag_01() throws HarnessException {
		
		// Create a tag
		TagItem tagItem = TagItem.CreateUsingSoap(app.zGetActiveAccount());		

		// Create a contact via Soap then select
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());

		// Tag the contact
		app.zGetActiveAccount().soapSend(
				"<ContactActionRequest xmlns='urn:zimbraMail'>" +
					"<action id='"+ contact.getId() +"' op='tag' tag='"+ tagItem.getId() +"'/>" +
				"</ContactActionRequest>");
		
		//-- GUI
		
		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);
		SleepUtil.sleepMedium();
		
		// Select the contact
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, contact.firstName);

    	// Untag it
		app.zPageContacts.zToolbarPressPulldown(Button.B_TAG, Button.O_TAG_REMOVETAG);
		
		//-- Verification
		
		app.zGetActiveAccount().soapSend(
				"<GetContactsRequest xmlns='urn:zimbraMail' >" +
						"<cn id='"+ contact.getId() +"'/>" +
				"</GetContactsRequest>");
		
		String t = app.zGetActiveAccount().soapSelectValue("//mail:cn", "t");
		ZAssert.assertNull(t, "Verify the contact has no tags");

   	}
	   
	@Test( description = "Untag a contact by click Tag->Remove Tag on context menu",
				groups = { "smoke", "L1"})
	public void ClickContextMenuTagRemoveTag_02() throws HarnessException {
		
		// Create a tag
		TagItem tagItem = TagItem.CreateUsingSoap(app.zGetActiveAccount());		

		// Create a contact via Soap then select
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());

		// Tag the contact
		app.zGetActiveAccount().soapSend(
				"<ContactActionRequest xmlns='urn:zimbraMail'>" +
					"<action id='"+ contact.getId() +"' op='tag' tag='"+ tagItem.getId() +"'/>" +
				"</ContactActionRequest>");
		
		//-- GUI
		
		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);
		
		SleepUtil.sleepMedium();
		
    	// Untag it
        app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_TAG, Button.O_TAG_REMOVETAG , contact.fileAs);
		
		//-- Verification
		
		app.zGetActiveAccount().soapSend(
				"<GetContactsRequest xmlns='urn:zimbraMail' >" +
						"<cn id='"+ contact.getId() +"'/>" +
				"</GetContactsRequest>");
		
		String t = app.zGetActiveAccount().soapSelectValue("//mail:cn", "t");
		ZAssert.assertNull(t, "Verify the contact has no tags");

	}
	
}

