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
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.contacts.FormContactDistributionListNew;
import com.zimbra.qa.selenium.projects.universal.ui.contacts.FormContactDistributionListNew.Field;

public class ModifyDLByAddingRemovingMembers extends AjaxCommonTest  {

	public ModifyDLByAddingRemovingMembers() {
		logger.info("New "+ ModifyDLByAddingRemovingMembers.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
		super.startingAccountPreferences = null;
	}
	
	@Test( description = "Modify DL by adding and removing members", 
			groups = { "functional", "L2"})

	public void ModifyDLByAddingRemovingMembers_01 () throws HarnessException {

		String dlFolder = "Distribution Lists";
		String dlName = "dl" + ConfigProperties.getUniqueString();
		String fullDLName = dlName + "@" + ConfigProperties.getStringProperty("testdomain");

		String firstContactEmail = ZimbraAccount.Account1().EmailAddress;
		String secondContactEmail = ZimbraAccount.Account2().EmailAddress;
		String thirdContactEmail = ZimbraAccount.Account3().EmailAddress;

		// Create DL
		app.zGetActiveAccount().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAccount'>"
			+		"<name>" + fullDLName + "</name>"
			+	"</CreateDistributionListRequest>");

		// Add DL members
		app.zGetActiveAccount().soapSend(
				"<DistributionListActionRequest xmlns='urn:zimbraAccount'>"
			+		"<dl by='name'>" + fullDLName + "</dl>"
			+		"<action op='addMembers'>"
         	+			"<dlm>" + firstContactEmail + "</dlm>"
         	+			"<dlm>" + secondContactEmail + "</dlm>"
         	+		"</action>"
			+	"</DistributionListActionRequest>");

		// Remove member 2
		app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, dlFolder);
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, fullDLName);
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, Button.B_EDIT, fullDLName);
		app.zPageContacts.sClick("//div[@class='ZmContactView' and not(aria-hidden)]/descendant::td[@class='contactGroupTableContent']/div[contains(text(),'"+secondContactEmail+"')]/parent::td/following-sibling::td/div");
		
		SleepUtil.sleepMedium();

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
		ZAssert.assertFalse(found2, "Verify member 2 not in the DL");
		ZAssert.assertTrue(found3, "Verify member 3 is in the DL");
	}
}
