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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.dl.modify;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.AutocompleteEntry;
import com.zimbra.qa.selenium.projects.ajax.pages.contacts.FormContactDistributionListNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;

public class ModifyDLDisplayNameAndDescription extends AjaxCore  {

	public ModifyDLDisplayNameAndDescription() {
		logger.info("New "+ ModifyDLDisplayNameAndDescription.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	@Test (description = "Modify DL display name and description",
			groups = { "sanity" })

	public void ModifyDLDisplayNameAndDescription_01 () throws HarnessException {
		String dlFolder = "Distribution Lists";
		String dlName = "tcdl" + ConfigProperties.getUniqueString();
		String fullDLName = dlName + "@" + ConfigProperties.getStringProperty("testdomain");
		String displayName = ConfigProperties.getUniqueString();
		String description = ConfigProperties.getUniqueString();

		String firstContactEmail = ZimbraAccount.Account1().EmailAddress;
		String secondContactEmail = ZimbraAccount.Account2().EmailAddress;

		// Create DL
		app.zGetActiveAccount().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAccount'>"
			+		"<name>" + fullDLName + "</name>"
			+		"<a n='description'>Created by Selenium automation</a>"
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

		app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, dlFolder);

		FormContactDistributionListNew FormContactDistributionListNew = (FormContactDistributionListNew) app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.O_EDIT_DISTRIBUTION_LIST, fullDLName);
		SleepUtil.sleepMedium();
		app.zPageContacts.zToolbarPressButton(Button.B_DISTRIBUTIONLIST_PROPERTIES);
		SleepUtil.sleepMedium();
		app.zPageContacts.sType("css=input[id$='_dlDisplayName']", displayName);
		app.zPageContacts.sType("css=textarea[id$='_dlDesc']", description);
		SleepUtil.sleepSmall();
		FormContactDistributionListNew.zSubmit();

		// Verify DL
		app.zGetActiveAccount().soapSend(
				"<GetDistributionListRequest xmlns='urn:zimbraAccount' needOwners='1'>"
			+		"<dl by='name'>" + fullDLName + "</dl>"
			+	"</GetDistributionListRequest>");

		String dlId = app.zGetActiveAccount().soapSelectValue("//acct:dl", "id");
		ZAssert.assertNotNull(dlId, "Verify the DL is returned in result");

		String actualDLDisplayName = app.zGetActiveAccount().soapSelectValue("//acct:dl//acct:a[@n='displayName']", null);
		String actualDLDescription = app.zGetActiveAccount().soapSelectValue("//acct:dl//acct:a[@n='description']", null);
		ZAssert.assertEquals(actualDLDisplayName, displayName, "Verify DL display name");
		ZAssert.assertEquals(actualDLDescription, description, "Verify DL description");

		// GAL Sync
		ZimbraDomain domain = new ZimbraDomain(ZimbraAccount.Account1().EmailAddress.split("@")[1]);
		domain.provision();
		domain.syncGalAccount();

		// Try to auto complete DL using display name
		app.zPageMail.zNavigateTo();
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(FormMailNew.Field.To, displayName);
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if (entry.getAddress().contains(fullDLName)) {
				found = entry;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");
	}
}
