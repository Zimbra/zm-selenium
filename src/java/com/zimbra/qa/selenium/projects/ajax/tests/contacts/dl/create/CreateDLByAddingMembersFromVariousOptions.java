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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.dl.create;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.contacts.FormContactDistributionListNew;
import com.zimbra.qa.selenium.projects.ajax.ui.contacts.FormContactDistributionListNew.Field;

public class CreateDLByAddingMembersFromVariousOptions extends AjaxCommonTest  {

	public CreateDLByAddingMembersFromVariousOptions() {
		logger.info("New "+ CreateDLByAddingMembersFromVariousOptions.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	@Test( description = "Create DL by adding DL members from various options",
			groups = { "sanity", "L0"})

	public void CreateDLByAddingMembersFromVariousOptions_01 () throws HarnessException {

		String dlName = "dl" + ConfigProperties.getUniqueString();
		String fullDLName = dlName + "@" + ConfigProperties.getStringProperty("testdomain");

		String firstContactFirstName = "First1" + ConfigProperties.getUniqueString();
		String firstContactLastName = "Last1" + ConfigProperties.getUniqueString();
		String firstContactEmail = ZimbraAccount.Account1().EmailAddress;
		String secondContactEmail = ZimbraAccount.Account2().EmailAddress;
		String thirdContactEmail = ZimbraAccount.Account3().EmailAddress;

		app.zGetActiveAccount().soapSend(
            "<CreateContactRequest xmlns='urn:zimbraMail'>" +
            		"<cn >" +
            			"<a n='firstName'>" + firstContactFirstName +"</a>" +
            			"<a n='lastName'>" + firstContactLastName +"</a>" +
            			"<a n='email'>" + firstContactEmail + "</a>" +
        			"</cn>" +
            "</CreateContactRequest>");

		FormContactDistributionListNew FormContactDistributionListNew = (FormContactDistributionListNew) zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_DISTRIBUTION_LIST);
		FormContactDistributionListNew.zFillField(Field.DistributionListName, dlName);

		// Personal Contacts
		FormContactDistributionListNew.zToolbarPressPulldown (Button.B_DISTRIBUTIONLIST_SEARCH_TYPE, Button.O_DISTRIBUTIONLIST_SEARCH_CONTACTS);
		FormContactDistributionListNew.zFillField(Field.SearchField, firstContactEmail);
		FormContactDistributionListNew.zToolbarPressButton(Button.B_SEARCH);
		FormContactDistributionListNew.zToolbarPressButton(Button.B_ADD);

		// GAL
		FormContactDistributionListNew.zToolbarPressPulldown (Button.B_DISTRIBUTIONLIST_SEARCH_TYPE, Button.O_DISTRIBUTIONLIST_SEARCH_GAL);
		FormContactDistributionListNew.zFillField(Field.SearchField, secondContactEmail);
		FormContactDistributionListNew.zToolbarPressButton(Button.B_SEARCH);
		FormContactDistributionListNew.zToolbarPressButton(Button.B_ADD);

		// Comma separated emails
		FormContactDistributionListNew.zFillField(Field.CommaSeparatedEmailsField, thirdContactEmail);
		FormContactDistributionListNew.zToolbarPressButton(Button.B_ADD_NEW);

		FormContactDistributionListNew.zSubmit();

		// Verify DL
		app.zGetActiveAccount().soapSend(
				"<GetDistributionListRequest xmlns='urn:zimbraAccount' needOwners='1'>"
			+		"<dl by='name'>" + fullDLName + "</dl>"
			+	"</GetDistributionListRequest>");

		String dlId = app.zGetActiveAccount().soapSelectValue("//acct:dl", "id");
		ZAssert.assertNotNull(dlId, "Verify the DL is returned in result");

		String ownerName = app.zGetActiveAccount().soapSelectValue("//acct:dl//acct:owners//acct:owner", "name");
		String dlMailStatus = app.zGetActiveAccount().soapSelectValue("//acct:dl//acct:a[@n='zimbraMailStatus']", null);
		String dlSubscriptionPolicy = app.zGetActiveAccount().soapSelectValue("//acct:dl//acct:a[@n='zimbraDistributionListSubscriptionPolicy']", null);
		String dlUnsubscriptionPolicy = app.zGetActiveAccount().soapSelectValue("//acct:dl//acct:a[@n='zimbraDistributionListUnsubscriptionPolicy']", null);

		ZAssert.assertEquals(ownerName, app.zGetActiveAccount().EmailAddress, "Verify the DL name is correct");
		ZAssert.assertEquals(dlMailStatus, "enabled", "Verify the DL mail status");
		ZAssert.assertEquals(dlSubscriptionPolicy, "ACCEPT", "Verify the DL subscription policy");
		ZAssert.assertEquals(dlUnsubscriptionPolicy, "ACCEPT", "Verify the DL unsubscription policy");

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
