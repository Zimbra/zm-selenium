/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.mail.mail;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.*;
import com.zimbra.qa.selenium.projects.universal.ui.mail.FormMailNew.*;

public class SendMailFromAccountWithDisabledAddressBook extends PrefGroupMailByMessageTest {

	public SendMailFromAccountWithDisabledAddressBook() {
		logger.info("New " + SendMailFromAccountWithDisabledAddressBook.class.getCanonicalName());

		super.startingAccountPreferences.put("zimbraFeatureContactsEnabled", "FALSE");
	}

	@Bugs( ids = "48923")
	@Test( description = "Send a mail from an account having Contacts disabled", groups = { "functional", "L2" } )

	public void SendMailFromAccountWithNoAddressBook_01() throws HarnessException {

		//Message data to be entered		
		String subject = "Subject "+ConfigProperties.getUniqueString();
		String body = "body " + ConfigProperties.getUniqueString();

		//-- GUI

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Send the message
		mailform.zSubmit();

		//-- VERIFIFICATION		

		//Verification through SOAP
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
						+			"<query>subject:("+ subject +")</query>"
						+		"</SearchRequest>");
		String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");

		ZimbraAccount.AccountA().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>"
						+			"<m id='"+ id +"' html='1'/>"
						+		"</GetMsgRequest>");

		String from = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='f']", "a");
		String to = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='t']", "a");
		String subjectSoap = ZimbraAccount.AccountA().soapSelectValue("//mail:su", null);
		String html = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@ct='text/html']//mail:content", null);

		ZAssert.assertEquals(from, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(to, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(subjectSoap, subject, "Verify the subject field is correct");
		ZAssert.assertStringContains(html,body, "Verify the body of mail is correct");

		//Verification through UI

		// Go to Sent		
		FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);

		//Verify that mail is present in Sent folder
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject),"Verify that mail is present in sent folder");

	}

}
