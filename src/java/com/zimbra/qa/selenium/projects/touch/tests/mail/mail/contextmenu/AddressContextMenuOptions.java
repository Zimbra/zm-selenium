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
package com.zimbra.qa.selenium.projects.touch.tests.mail.mail.contextmenu;


import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.*;
import com.zimbra.qa.selenium.projects.touch.ui.mail.DisplayMail.*;

public class AddressContextMenuOptions extends PrefGroupMailByMessageTest {

	public AddressContextMenuOptions() {
		logger.info("New "+ AddressContextMenuOptions.class.getCanonicalName());
		super.startingPage = app.zPageMail;
	}
	
	@Test(	description = "Click from bubble address>>Verify AddtoContact/Newmessage/Search menus", 
			groups = { "test" })
	
	public void AcceptMeeting_01() throws HarnessException {

		// ------------------------ Test data ------------------------------------
		String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();
		String body = "text <strong>bold"+ ZimbraSeleniumProperties.getUniqueString() +"</strong> text";
		String htmlBody = XmlStringUtil.escapeXml(
				"<html>" +
					"<head></head>" +
					"<body>"+ body +"</body>" +
				"</html>");

		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<e t='c' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='multipart/alternative'>" +
						"<mp ct='text/plain'>" +
							"<content>"+ body +"</content>" +
						"</mp>" +
						"<mp ct='text/html'>" +
							"<content>"+ htmlBody +"</content>" +
						"</mp>" +
					"</mp>" +
					"</m>" +
				"</SendMsgRequest>");
		app.zPageMail.zRefresh();
		
		//----------------------------- Verification ------------------------------

		// Select the mail
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Click on from address bubble
		SleepUtil.sleepMedium();
		app.zPageMail.zClickAddressBubble(Field.From);
		
		//Verify AddtoContact/New message/Search menus
		ZAssert.assertTrue(app.zPageMail.zVerifyAllAddressContextMenu(""),
				"AddtoContact/Newmessage/Search menu should be exist");
		
		
	}

}
