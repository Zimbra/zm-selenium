/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.mail.readreceipt;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.FormMailNew;


public class PrefMailRequestReadReceiptsTrue extends PrefGroupMailByMessageTest {

	public PrefMailRequestReadReceiptsTrue() {
		logger.info("New "+ PrefMailRequestReadReceiptsTrue.class.getCanonicalName());
		
		super.startingAccountPreferences.put("zimbraPrefMailRequestReadReceipts", "TRUE");
		
	}
	
	@Test( description = "Send a text message requesting a read receipt",
			groups = { "functional", "L2" })
	public void CreateMailText_01() throws HarnessException {
		
		
		//-- DATA Setup
		
		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyHtml = "body" + ConfigProperties.getUniqueString();
		
		
		//-- GUI Actions
		//
		
		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		
		// Fill out the form with the data
		mailform.zFill(mail);
		
		// Send the message
		mailform.zSubmit();


		//-- VERIFICATION
		//
		
		// Verify the message is received with a read receipt request
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ mail.dSubject +")");

		ZimbraAccount.AccountA().soapSend(
					"<GetMsgRequest  xmlns='urn:zimbraMail'>"
				+		"<m id='"+ received.getId() +"'/>"
				+	"</GetMsgRequest>");
		String requestor = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='n']", "a");

		ZAssert.assertEquals(
				requestor, 
				app.zGetActiveAccount().EmailAddress, 
				"Verify the received message requests a read receipt from the test account");
		
	}

	


}
