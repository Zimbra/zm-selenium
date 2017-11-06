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

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.ContactGroupItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.contacts.DisplayContactGroup;

public class ViewContactGroup extends AjaxCommonTest  {

	public ViewContactGroup() {
		logger.info("New "+ ViewContactGroup.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	@Test( description = "View a contact group",
			groups = { "smoke", "L0" } )

	public void DisplayContactGroupInfo_01() throws HarnessException {

		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Select the contact group
		DisplayContactGroup groupView = (DisplayContactGroup) app.zPageContacts.zListItem(Action.A_LEFTCLICK, group.getName());

		ZAssert.assertStringContains(groupView.zGetContactProperty(DisplayContactGroup.Field.Company), group.getName(),
				"Verify contact group email (" + group.getName() + ") displayed");

		// verify group members
		for (ContactGroupItem.MemberItem m : group.getMemberList()) {
			String email = m.getValue();
			String locator = "css=div.ZmContactSplitView div.contactGroupList div:contains('"+ email +"')";

			boolean present = app.zPageContacts.sIsElementPresent(locator);
			ZAssert.assertTrue(present, "Verify the member "+ email +" is present");

			boolean visible = app.zPageContacts.zIsVisiblePerPosition(locator, 0, 0);
			ZAssert.assertTrue(visible, "Verify the member "+ email +" is visible");
		}
	}
}