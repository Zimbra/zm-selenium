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
package com.zimbra.qa.selenium.projects.universal.tests.contacts.dl.autocomplete;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCore;
import com.zimbra.qa.selenium.projects.universal.pages.AutocompleteEntry;
import com.zimbra.qa.selenium.projects.universal.pages.mail.FormMailNew;

public class AutoCompleteDL extends UniversalCore  {

	public AutoCompleteDL() {
		logger.info("New "+ AutoCompleteDL.class.getCanonicalName());
		super.startingPage = app.zPageMail;
		
	}

	@Test (description = "Auto complete DL and send mail to DL", 
			groups = { "smoke", "L0" })

	public void AutoCompleteDL_01() throws HarnessException {

		ZimbraAccount firstContact = ZimbraAccount.Account1();
		ZimbraAccount secondContact = ZimbraAccount.Account2();
		ZimbraAccount thirdContact = ZimbraAccount.Account3();

		String dlName = "dl" + ConfigProperties.getUniqueString();
		String fullDLName = dlName + "@" + ConfigProperties.getStringProperty("testdomain");

		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

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
         	+			"<dlm>" + firstContact.EmailAddress + "</dlm>"
         	+			"<dlm>" + secondContact.EmailAddress + "</dlm>"
         	+			"<dlm>" + thirdContact.EmailAddress + "</dlm>"
         	+		"</action>"
			+	"</DistributionListActionRequest>");

		// GAL Sync
		ZimbraDomain domain = new ZimbraDomain(firstContact.EmailAddress.split("@")[1]);
		domain.provision();
		domain.syncGalAccount();

		// Compose mail
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		mailform.zFillField(FormMailNew.Field.Subject, subject);
		mailform.zFillField(FormMailNew.Field.Body, body);

		// Auto complete DL
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(FormMailNew.Field.To, dlName);
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(fullDLName) ) {
				found = entry;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");
		mailform.zAutocompleteSelectItem(found);

		// Send the message
		mailform.zSubmit();

		// Verify message received
		ZimbraAccount[] accounts = { firstContact, secondContact, thirdContact };
		for (int i=0; i<accounts.length; i++) {
			MailItem received = MailItem.importFromSOAP(accounts[i], "subject:("+ subject +")");
			ZAssert.assertNotNull(received, "Verify the message is received correctly to " + accounts[i].EmailAddress);
		}
	}
}
