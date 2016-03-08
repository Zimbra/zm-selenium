/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.mail.fullviewpane;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;

public class DeleteMail extends PrefGroupMailByMessageTest {

	public DeleteMail() throws HarnessException {
		logger.info("New "+ DeleteMail.class.getCanonicalName());
	}
	
	@Test( description = "Bug 77538 - Double click a mail and delete it", groups = { "functional" } )
	
	public void DeleteMail_01() throws HarnessException {

		// Data Setup	
		String subject = "subject"+ ZimbraSeleniumProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
			"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
					"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
					"<su>"+ subject +"</su>" +
					"<mp ct='text/plain'>" +
					"<content>content"+ ZimbraSeleniumProperties.getUniqueString() +"</content>" +
					"</mp>" +
					"</m>" +
			"</SendMsgRequest>");

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);

		// Double click the message to open in full reading pane
		app.zPageMail.zListItem(Action.A_DOUBLECLICK, subject);

		app.zPageMail.zToolbarPressButtonFullViewPane (Button.B_DELETE_FULL_VIEW_PANE);
		
		// Verify the message no longer exist in the list
		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		MailItem found = null;
		for (MailItem m : messages) {
			logger.info("Subject: looking for "+ mail.dSubject +" found: "+ m.gSubject);
			if ( mail.dSubject.equals(m.gSubject) ) {
				found = m;
				break;
			}
		}
		
		ZAssert.assertNull(found, "Verify the message is no longer in the inbox");

		ZAssert.assertFalse(app.zPageMail.zIsVisiblePerPosition("css=div[id^='ztb__MSG'] div[id$='DELETE'] tr td[id$='DELETE_title']", 10, 10), "Verify delete button is not present");
	}
}
