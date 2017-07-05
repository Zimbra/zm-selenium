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

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.universal.ui.contacts.FormContactDistributionListNew;
import com.zimbra.qa.selenium.projects.universal.ui.contacts.FormContactDistributionListNew.Field;

public class ModifyDLByAddingListOwnersAndOwnerDeletesDL extends AjaxCommonTest  {

	public ModifyDLByAddingListOwnersAndOwnerDeletesDL() {
		logger.info("New "+ ModifyDLByAddingListOwnersAndOwnerDeletesDL.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
		super.startingAccountPreferences = null;
	}
	
	@Test( description = "Modify DL by adding list owners and owner deletes DL", 
			groups = { "functional", "L2"})

	public void ModifyDLByAddingListOwnersAndOwnerDeletesDL_01() throws HarnessException {

		String dlFolder = "Distribution Lists";
		String dlName = "dl" + ConfigProperties.getUniqueString();
		String fullDLName = dlName + "@" + ConfigProperties.getStringProperty("testdomain");
		
		ZimbraAccount secondContact = ZimbraAccount.Account2();
		String firstContactEmail = ZimbraAccount.Account1().EmailAddress;
		String secondContactEmail = secondContact.EmailAddress;

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
		
		// Delete DL
		app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, dlFolder);
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, fullDLName);

		app.zPageContacts.zToolbarPressButton(Button.B_DELETE);
		DialogWarning warning = new DialogWarning(DialogWarning.DialogWarningID.DeleteItem, app, app.zPageContacts);
		warning.zClickButton(Button.B_OK);

		app.zGetActiveAccount().soapSend(
				"<GetDistributionListRequest xmlns='urn:zimbraAccount' needOwners='1'>"
			+		"<dl by='name'>" + fullDLName + "</dl>"
			+	"</GetDistributionListRequest>");

		String dlId = app.zGetActiveAccount().soapSelectValue("//acct:dl", "id");
		ZAssert.assertNull(dlId, "Verify DL is not returned in the result");

		// Verify UI
		Boolean found = false;
		List<ContactItem> items = app.zPageContacts.zListGetContacts();

		for (ContactItem item : items ) {
			if ( item.fileAs.contains(fullDLName) ) {
				found = true;
				break;
			} else {
				logger.info("DL is not displayed in current view");
			}
		}

		ZAssert.assertFalse(found, "Verify deleted DL is not shown in UI");
	}
}
