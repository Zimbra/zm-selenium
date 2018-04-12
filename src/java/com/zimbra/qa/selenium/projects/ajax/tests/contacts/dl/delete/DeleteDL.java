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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.dl.delete;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning;

public class DeleteDL extends AjaxCore  {

	public DeleteDL() {
		logger.info("New "+ DeleteDL.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	@Test (description = "Delete DL using toolbar button",
			groups = { "smoke", "L1" })

	public void DeleteDLUsingToolbarButton_01() throws HarnessException {

		String firstContactEmail = ZimbraAccount.Account1().EmailAddress;
		String secondContactEmail = ZimbraAccount.Account2().EmailAddress;

		String dlFolder = "Distribution Lists";
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
		app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, dlFolder);
		app.zPageContacts.zListItem(Action.A_LEFTCLICK, fullDLName);

		app.zPageContacts.zToolbarPressButton(Button.B_DELETE);
		DialogWarning warning = new DialogWarning(DialogWarning.DialogWarningID.DeleteItem, app, app.zPageContacts);
		warning.zPressButton(Button.B_OK);

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


	@Test (description = "Delete DL using context menu",
			groups = { "smoke", "L1" })

	public void DeleteDLUsingContextMenu_02() throws HarnessException {

		String firstContactEmail = ZimbraAccount.Account1().EmailAddress;
		String secondContactEmail = ZimbraAccount.Account2().EmailAddress;

		String dlFolder = "Distribution Lists";
		String dlName = "dl" + ConfigProperties.getUniqueString();
		String fullDLName = dlName + "@" + ConfigProperties.getStringProperty("testdomain");

		// Create DL
		app.zGetActiveAccount().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAccount'>"
			+		"<name>" + fullDLName + "</name>"
			+	"</CreateDistributionListRequest>");

		// Add DL member
		app.zGetActiveAccount().soapSend(
				"<DistributionListActionRequest xmlns='urn:zimbraAccount'>"
			+		"<dl by='name'>" + fullDLName + "</dl>"
			+		"<action op='addMembers'>"
         	+			"<dlm>" + firstContactEmail + "</dlm>"
         	+			"<dlm>" + secondContactEmail + "</dlm>"
         	+		"</action>"
			+	"</DistributionListActionRequest>");

		// Delete DL
		app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, dlFolder);
		app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.O_DELETE_MENU, fullDLName);

		DialogWarning warning = new DialogWarning(DialogWarning.DialogWarningID.DeleteItem, app, app.zPageContacts);
		warning.zPressButton(Button.B_OK);

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