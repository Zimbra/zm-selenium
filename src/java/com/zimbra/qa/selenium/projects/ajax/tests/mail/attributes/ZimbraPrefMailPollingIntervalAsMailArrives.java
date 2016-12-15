/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.attributes;

import java.util.*;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;


public class ZimbraPrefMailPollingIntervalAsMailArrives extends PrefGroupMailByMessageTest {

	
	
	
	public ZimbraPrefMailPollingIntervalAsMailArrives() {
		logger.info("New "+ ZimbraPrefMailPollingIntervalAsMailArrives.class.getCanonicalName());
		
		super.startingAccountPreferences.put("zimbraPrefMailPollingInterval", ""+ com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.displayingmessages.ZimbraPrefMailPollingIntervalAsMailArrives.AsMailArrives);



	}
	
	@Test( description = "Receive a mail with - As Mail Arrives set",
			groups = { "functional", "L2" })
	public void ZimbraPrefMailPollingIntervalAsMailArrives_01() throws HarnessException {
		
		
		//-- DATA
		
		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		

		

		//-- GUI
		
		// Refresh the client to sync up
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Send a new message
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ "body" + ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// Wait for at least a little time (i.e. harness may be faster than the client and network)
		SleepUtil.sleep(com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.displayingmessages.ZimbraPrefMailPollingIntervalAsMailArrives.AsMailArrivesDelay);
		
		
		
		//-- VERIFICATION
		
		
		// Get all the messages in the inbox
		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		// Make sure the message appears in the list
		MailItem found = null;
		for (MailItem m : messages) {
			logger.info("Subject: looking for "+ subject +" found: "+ m.gSubject);
			if ( subject.equals(m.gSubject) ) {
				found = m;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the message is in the inbox");

		
	}



}
