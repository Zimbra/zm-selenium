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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.dl.rename;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.contacts.FormContactDistributionListNew;
import com.zimbra.qa.selenium.projects.ajax.ui.contacts.FormContactDistributionListNew.Field;

public class RenameDL extends AjaxCommonTest  {

	public RenameDL() {
		logger.info("New "+ RenameDL.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	@Test( description = "Rename DL", 
			groups = { "functional", "L2"})

	public void RenameDL_01 () throws HarnessException {

		String firstContactEmail = ZimbraAccount.Account1().EmailAddress;
		String secondContactEmail = ZimbraAccount.Account2().EmailAddress;

		String dlFolder = "Distribution Lists";
		String dlName = "dl" + ConfigProperties.getUniqueString();
		String renamedDlName = "dl" + ConfigProperties.getUniqueString();
		String fullDLName = dlName + "@" + ConfigProperties.getStringProperty("testdomain");
		String renamedFullDLName = renamedDlName + "@" + ConfigProperties.getStringProperty("testdomain");

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

		// Rename DL
		app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, dlFolder);
		FormContactDistributionListNew FormContactDistributionListNew = (FormContactDistributionListNew) app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.O_EDIT_DISTRIBUTION_LIST, fullDLName);
		FormContactDistributionListNew.zFillField(Field.DistributionListName, renamedDlName);
		FormContactDistributionListNew.zSubmit();

		// Verify renamed DL
		app.zGetActiveAccount().soapSend(
				"<GetDistributionListRequest xmlns='urn:zimbraAccount' needOwners='1'>"
			+		"<dl by='name'>" + renamedFullDLName + "</dl>"
			+	"</GetDistributionListRequest>");

		String dlId = app.zGetActiveAccount().soapSelectValue("//acct:dl", "id");
		ZAssert.assertNotNull(dlId, "Verify the DL is returned in result");

		String ownerName = app.zGetActiveAccount().soapSelectValue("//acct:dl//acct:owners//acct:owner", "name");
		String dlMailStatus = app.zGetActiveAccount().soapSelectValue("//acct:dl//acct:a[@n='zimbraMailStatus']", null);
		String dlSubscriptionPolicy = app.zGetActiveAccount().soapSelectValue("//acct:dl//acct:a[@n='zimbraDistributionListSubscriptionPolicy']", null);
		String dlUnsubscriptionPolicy = app.zGetActiveAccount().soapSelectValue("//acct:dl//acct:a[@n='zimbraDistributionListUnsubscriptionPolicy']", null);

		ZAssert.assertEquals(ownerName, app.zGetActiveAccount().EmailAddress, "Verify the DL name is correct");
		ZAssert.assertEquals(dlMailStatus, "enabled", "Verify the DL mail status");
		ZAssert.assertEquals(dlSubscriptionPolicy, "REJECT", "Verify the DL subscription policy");
		ZAssert.assertEquals(dlUnsubscriptionPolicy, "REJECT", "Verify the DL unsubscription policy");

		// Verify renamed DL members
		app.zGetActiveAccount().soapSend(
				"<GetDistributionListMembersRequest xmlns='urn:zimbraAccount'>"
			+		"<dl>" + renamedFullDLName + "</dl>"
			+	"</GetDistributionListMembersRequest>");

		boolean found1 = false;
		boolean found2 = false;
		Element[] members = app.zGetActiveAccount().soapSelectNodes("//acct:dlm");
		for (Element e : members) {
			if ( e.getText().equals(firstContactEmail) ) {
				found1 = true;
			}
			if ( e.getText().equals(secondContactEmail) ) {
				found2 = true;
			}
		}

		ZAssert.assertTrue(found1, "Verify member 1 is in the DL");
		ZAssert.assertTrue(found2, "Verify member 2 is in the DL");
	}
}
