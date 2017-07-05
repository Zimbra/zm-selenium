/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.*;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DisplayMail.Field;

public class ReceiveReadReceipt extends PrefGroupMailByMessageTest {

	public ReceiveReadReceipt() {
		logger.info("New "+ ReceiveReadReceipt.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
	}
	
	@Test( description = "Receive/view a read receipt",
			groups = { "functional", "L2" })
	public void CreateMailText_01() throws HarnessException {
		
		// Data setup
		
		// Send a message requesting a read receipt
		String subject = "subject" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" 
			+			"<m>"
			+				"<e t='t' a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>"
			+				"<e t='f' a='"+ app.zGetActiveAccount().EmailAddress +"'/>"
			+				"<e t='n' a='"+ app.zGetActiveAccount().EmailAddress +"'/>"
			+				"<su>"+ subject +"</su>"
			+				"<mp ct='text/plain'>"
			+					"<content>content" + ConfigProperties.getUniqueString() +"</content>" 
			+				"</mp>"
			+			"</m>" 
			+		"</SendMsgRequest>");

		
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ subject +")");
		
		// Send the read receipt
		ZimbraAccount.AccountA().soapSend("<SendDeliveryReportRequest xmlns='urn:zimbraMail' mid='"+ received.getId() +"'/>");
		
		// GUI verification
		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		ZAssert.assertEquals(actual.zGetMailProperty(Field.To), app.zGetActiveAccount().EmailAddress, "Verify the message is to the test account");
		ZAssert.assertEquals(actual.zGetMailProperty(Field.From),	ZimbraAccount.AccountA().EmailAddress, "Verify the message is from the destination");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Subject), "Read-Receipt", "Verify the message subject contains the correct value");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), "The message sent on", "Verify the message subject contains the correct value");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), subject, "Verify the message subject contains the correct value");
	}
}