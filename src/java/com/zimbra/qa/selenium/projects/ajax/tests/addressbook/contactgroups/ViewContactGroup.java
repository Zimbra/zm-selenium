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
package com.zimbra.qa.selenium.projects.ajax.tests.addressbook.contactgroups;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.ContactGroupItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.addressbook.DisplayContactGroup;


public class ViewContactGroup extends AjaxCommonTest  {
	
	public ViewContactGroup() {
		logger.info("New "+ ViewContactGroup.class.getCanonicalName());

		// All tests start at the Address page
		super.startingPage = app.zPageAddressbook;
		super.startingAccountPreferences = null;		

	}


	@Test(
			description = "View a contact group", 
			groups = { "smoke" }
			)
	public void DisplayContactGroupInfo() throws HarnessException {

		//-- Data
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		
		// Refresh
		app.zPageAddressbook.zRefresh();		
		
		// Select the contact group
		DisplayContactGroup groupView = (DisplayContactGroup) app.zPageAddressbook.zListItem(Action.A_LEFTCLICK, group.getName());

		// verify groupname
		//
		// The GUI view shows the group name surrounded in quotes, so do a 'contains'
		//
		ZAssert.assertStringContains(
				groupView.zGetContactProperty(DisplayContactGroup.Field.Company),
				group.getName(),
				"Verify contact group email (" + group.getName() + ") displayed");	

		// verify group members
		for (ContactGroupItem.MemberItem m : group.getMemberList()) {
			
			String email = m.getValue();
			String locator = "css=div.ZmContactSplitView div.contactGroupList div:contains('"+ email +"')";

			boolean present = app.zPageAddressbook.sIsElementPresent(locator);
			ZAssert.assertTrue(present, "Verify the member "+ email +" is present");

			boolean visible = app.zPageAddressbook.zIsVisiblePerPosition(locator, 0, 0);
			ZAssert.assertTrue(visible, "Verify the member "+ email +" is visible");
			
		}
		
	}
}

