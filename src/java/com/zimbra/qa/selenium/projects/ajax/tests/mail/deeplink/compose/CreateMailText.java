/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.deeplink.compose;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.*;


public class CreateMailText extends PrefGroupMailByMessageTest {

	public CreateMailText() {
		logger.info("New "+ CreateMailText.class.getCanonicalName());
		
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
		
	}
	
	@Test(	description = "Create a text mail using the deep link URL",
			groups = { "functional" })
	public void CreateMailText_01() throws HarnessException {
		
		
		
		//-- DATA
		
		
		// Create the message data to be sent
		final String to = ZimbraAccount.AccountA().EmailAddress;
		final String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();
		final String body = "body" + ZimbraSeleniumProperties.getUniqueString();
		
		
		
		
		//-- GUI
		
		// The account is already authenticated
		// However, we need to change the URL and open
		// the deep link form.
		
		ZimbraURI uri = new ZimbraURI(ZimbraURI.getCurrentURI());
		uri.addQuery("view", "compose");	// Setting view=compose will make PageMain return a FormMailNew object
		uri.addQuery("to", to);
		uri.addQuery("subject", subject);
		uri.addQuery("body", body);

		// Open the Deep Link URL: http://server.com/?view=compose&to=addy&subject=text&body=value
		//
		FormMailNew mailform = (FormMailNew)app.zPageMain.zOpenDeeplink(uri);
		ZAssert.assertNotNull(mailform, "Verify the deeplink page opens");
		ZAssert.assertTrue(mailform.zIsActive(), "Verify the deeplink page opens");
		
		// The form should be filled out, so just submit
		mailform.zSubmit();
		
		
		//-- VERIFICATION
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
		
	}





}
