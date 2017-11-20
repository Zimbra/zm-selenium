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
package com.zimbra.qa.selenium.projects.universal.tests.contacts.dl.modify;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCore;
import com.zimbra.qa.selenium.projects.universal.pages.contacts.FormContactDistributionListNew;
import com.zimbra.qa.selenium.projects.universal.pages.contacts.FormContactDistributionListNew.Field;

public class ModifyDLByAddingListOwnersAndOwnerAddsMember extends UniversalCore  {

	public ModifyDLByAddingListOwnersAndOwnerAddsMember() {
		logger.info("New "+ ModifyDLByAddingListOwnersAndOwnerAddsMember.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}
	
	@Test (description = "Modify DL by adding list owners and owner adds one more member", 
			groups = { "functional", "L2"})

	public void ModifyDLByAddingListOwnersAndOwnerAddsMember_01() throws HarnessException {

		String dlFolder = "Distribution Lists";
		String dlName = "dl" + ConfigProperties.getUniqueString();
		String fullDLName = dlName + "@" + ConfigProperties.getStringProperty("testdomain");
		
		ZimbraAccount secondContact = ZimbraAccount.Account2();
		String firstContactEmail = ZimbraAccount.Account1().EmailAddress;
		String secondContactEmail = secondContact.EmailAddress;
		String thirdContactEmail = ZimbraAccount.Account3().EmailAddress;

		FormContactDistributionListNew FormContactDistributionListNew = (FormContactDistributionListNew) zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_DISTRIBUTION_LIST);

		FormContactDistributionListNew.zFillField(Field.DistributionListName, dlName);
		FormContactDistributionListNew.zToolbarPressPulldown (Button.B_DISTRIBUTIONLIST_SEARCH_TYPE, Button.O_DISTRIBUTIONLIST_SEARCH_GAL);
		FormContactDistributionListNew.zFillField(Field.SearchField, firstContactEmail);
		FormContactDistributionListNew.zToolbarPressButton(Button.B_SEARCH);
		FormContactDistributionListNew.zToolbarPressButton(Button.B_DISTRIBUTIONLIST_ADD_SEARCH_RESULT);

		FormContactDistributionListNew.zFillField(Field.SearchField, secondContactEmail);
		FormContactDistributionListNew.zToolbarPressButton(Button.B_SEARCH);
		FormContactDistributionListNew.zToolbarPressButton(Button.B_DISTRIBUTIONLIST_ADD_SEARCH_RESULT);

		// Modify DL by adding more owners
		SleepUtil.sleepSmall();
		app.zPageContacts.zToolbarPressButton(Button.B_DISTRIBUTIONLIST_PROPERTIES);
		SleepUtil.sleepMedium();
		app.zPageContacts.sType("css=input[id$='_dlListOwners']", app.zGetActiveAccount().EmailAddress + ";" + secondContactEmail);
		SleepUtil.sleepSmall();
		FormContactDistributionListNew.zSubmit();
		
		// Logout to current account and login as DL owner
		app.zPageMain.zLogout();
		app.zPageLogin.zLogin(secondContact);
		app.zPageContacts.zNavigateTo();
		app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, dlFolder);
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, fullDLName);
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, Button.B_EDIT, fullDLName);
		
		// Add member 3
		FormContactDistributionListNew formContactDistributionListNew = new FormContactDistributionListNew(app);
		formContactDistributionListNew.zFillField(Field.CommaSeparatedEmailsField, thirdContactEmail);
		formContactDistributionListNew.zToolbarPressButton(Button.B_ADD_NEW);
		formContactDistributionListNew.zSubmit();

		// Verify DL members
		app.zGetActiveAccount().soapSend(
				"<GetDistributionListMembersRequest xmlns='urn:zimbraAccount'>"
			+		"<dl>" + fullDLName + "</dl>"
			+	"</GetDistributionListMembersRequest>");

		boolean found1 = false;
		boolean found2 = false;
		boolean found3 = false;
		Element[] members = app.zGetActiveAccount().soapSelectNodes("//acct:dlm");
		for (Element e : members) {
			if ( e.getText().equals(firstContactEmail) ) {
				found1 = true;
			}
			if ( e.getText().equals(secondContactEmail) ) {
				found2 = true;
			}
			if ( e.getText().equals(thirdContactEmail) ) {
				found3 = true;
			}
		}

		ZAssert.assertTrue(found1, "Verify member 1 is in the DL");
		ZAssert.assertTrue(found2, "Verify member 2 is in the DL");
		ZAssert.assertTrue(found3, "Verify member 3 is in the DL");
	}
}
