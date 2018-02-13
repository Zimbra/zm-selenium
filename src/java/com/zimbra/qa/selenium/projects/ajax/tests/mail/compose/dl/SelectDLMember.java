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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.dl;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraDomain;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.AutocompleteEntry;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Locators;

public class SelectDLMember extends AjaxCore  {

	public SelectDLMember() {
		logger.info("New "+ SelectDLMember.class.getCanonicalName());
		super.startingPage = app.zPageMail;
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
		super.startingAccountPreferences.put("zimbraPrefGalAutoCompleteEnabled", "TRUE");
	}


	@Test (description = "Select a DL member from the expanded list while composing mail",
			groups = { "functional", "L2" })

	public void SelectDLMember_01() throws HarnessException {

		// DL data
		String dlMember1 = ZimbraAccount.Account1().EmailAddress;
		String dlMember2 = ZimbraAccount.Account2().EmailAddress;

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
						+			"<dlm>" + dlMember1 + "</dlm>"
						+			"<dlm>" + dlMember2 + "</dlm>"
						+		"</action>"
						+	"</DistributionListActionRequest>");

		// GAL Sync
		ZimbraDomain domain = new ZimbraDomain(ConfigProperties.getStringProperty("testdomain"));
		domain.provision();
		domain.syncGalAccount();

		// Message data
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

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

		// Expand the DL and select the member
		mailform.zSelectMemberFromDL(fullDLName, dlMember1);


		// Verify through UI that DL is removed and member is added in the To filed
		ZAssert.assertStringContains(mailform.sGetText(Locators.zBubbleToField), dlMember1,"Verify that member email adderess is present in To field");
		ZAssert.assertStringDoesNotContain(mailform.sGetText(Locators.zBubbleToField), dlName, "Verify that dl is removed from the To field");

		// Send the mail
		mailform.zSubmit();

		// Soap Verification
		ZimbraAccount.Account1().soapSend(
				"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
						+ "<query>subject:("+ subject +")</query>"
						+ "</SearchRequest>");
		String id = ZimbraAccount.Account1().soapSelectValue("//mail:m", "id");

		ZimbraAccount.Account1().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>"
						+ "<m id='"+ id +"' html='1'/>"
						+ "</GetMsgRequest>");

		String from = ZimbraAccount.Account1().soapSelectValue("//mail:e[@t='f']", "a");
		String to = ZimbraAccount.Account1().soapSelectValue("//mail:e[@t='t']", "a");
		String sub = ZimbraAccount.Account1().soapSelectValue("//mail:su", null);
		String bodyText = ZimbraAccount.Account1().soapSelectValue("//mail:mp[@ct='text/html']//mail:content", null);
		
		ZAssert.assertEquals(from, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(to, dlMember1, "Verify the To field is correct");
		ZAssert.assertEquals(sub, subject, "Verify the subject field is correct");
		ZAssert.assertStringContains(bodyText, body, "Verify the mail content");
	}
}