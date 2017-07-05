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
package com.zimbra.qa.selenium.projects.universal.tests.contacts.dl.duplicate;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.DialogError;
import com.zimbra.qa.selenium.projects.universal.ui.DialogError.DialogErrorID;
import com.zimbra.qa.selenium.projects.universal.ui.contacts.FormContactDistributionListNew;
import com.zimbra.qa.selenium.projects.universal.ui.contacts.FormContactDistributionListNew.Field;

public class CreateDuplicateDL extends UniversalCommonTest  {

	public CreateDuplicateDL() {
		logger.info("New "+ CreateDuplicateDL.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}

	@Test( description = "Try to create duplicate DL", 
			groups = { "functional", "L2"})

	public void TryToCreateDuplicateDL_01 () throws HarnessException {

		String firstContactEmail = ZimbraAccount.Account1().EmailAddress;
		String secondContactEmail = ZimbraAccount.Account2().EmailAddress;

		String dlName = "dl" + ConfigProperties.getUniqueString();
		String fullDLName = dlName + "@" + ConfigProperties.getStringProperty("testdomain");

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

		// Delete DL
		FormContactDistributionListNew FormContactDistributionListNew = (FormContactDistributionListNew) zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_DISTRIBUTION_LIST);
		FormContactDistributionListNew.zFillField(Field.DistributionListName, dlName);
		FormContactDistributionListNew.zToolbarPressPulldown (Button.B_DISTRIBUTIONLIST_SEARCH_TYPE, Button.O_DISTRIBUTIONLIST_SEARCH_GAL);
		FormContactDistributionListNew.zFillField(Field.SearchField, firstContactEmail);
		FormContactDistributionListNew.zToolbarPressButton(Button.B_SEARCH);
		FormContactDistributionListNew.zToolbarPressButton(Button.B_DISTRIBUTIONLIST_ADD_SEARCH_RESULT);
		FormContactDistributionListNew.zSubmit();

		// Verification
		DialogError error = new DialogError(DialogErrorID.Zimbra, app, app.zPageContacts);
		ZAssert.assertEquals(error.zGetWarningContent(), "Create distribution list failed", "Verify error message when try to create duplicate distribution list");
		error.zClickButton(Button.B_OK);
	}
}
