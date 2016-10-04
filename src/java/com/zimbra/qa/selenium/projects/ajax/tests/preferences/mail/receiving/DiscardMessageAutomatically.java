/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.receiving;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

public class DiscardMessageAutomatically extends PrefGroupMailByMessageTest {

	public DiscardMessageAutomatically() {
		logger.info("New "+ DiscardMessageAutomatically.class.getCanonicalName());
				
		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");

	}
	
	@Test( description = "Set discard message automatically and send a message to self", groups = { "functional" })
	
	public void DiscardMessageAutomatically_01() throws HarnessException {
		
		//Set discard message automatically to self
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Mail);	
		app.zPagePreferences.sClickAt(("css=label[id$='_text_right']:contains('Discard message automatically')") , "");	 //check 'discard message automatically'
 		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		// Create message 	
		String subject = "subject"+ ConfigProperties.getUniqueString();
		String bodyText = "text" + ConfigProperties.getUniqueString();
		String bodyHTML = "text <strong>bold"+ ConfigProperties.getUniqueString() +"</strong> text";
		String contentHTML = XmlStringUtil.escapeXml(
				"<html>" +
					"<head></head>" +
					"<body>"+ bodyHTML +"</body>" +
				"</html>");

		// Send a message to the self
		app.zGetActiveAccount().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='multipart/alternative'>" +
								"<mp ct='text/plain'>" +
									"<content>"+ bodyText +"</content>" +
								"</mp>" +
								"<mp ct='text/html'>" +
									"<content>"+ contentHTML +"</content>" +
								"</mp>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

			// Refresh current view
			app.zPageMain.zToolbarPressButton(Button.B_REFRESH);
			
			// Verify that the message is not present in Inbox
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
		
			// UI verification
			//check message is in Send folder
			app.zTreeMail.sClickAt(("css=td[id='zti__main_Mail__5_textCell']:contains('Sent')"), ""); // Go to sent folder
			ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

	}

}
