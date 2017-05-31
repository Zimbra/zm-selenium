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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.deeplink.compose;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.*;


public class CreateMail extends PrefGroupMailByMessageTest {

	public CreateMail() {
		logger.info("New "+ CreateMail.class.getCanonicalName());
		
	}
	
	@Bugs( ids = "21624")
	@Test( description = "Create a mail using the deep link URL",
			groups = { "smoke", "L1" })
	public void CreateMail_01() throws HarnessException {
		
		
		
		//-- DATA
		
		
		// Create the message data to be sent
		final String to = ZimbraAccount.AccountA().EmailAddress;
		final String subject = "subject" + ConfigProperties.getUniqueString();
		final String body = "body" + ConfigProperties.getUniqueString();
		
		
		
		
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

	@Test( description = "Create a mail with cc and bcc using the deep link URL",
			groups = { "functional", "L2" })
	public void CreateMail_02() throws HarnessException {
		
		
		
		//-- DATA
		
		
		// Create the message data to be sent
		final String subject = "subject" + ConfigProperties.getUniqueString();
		final String body = "body" + ConfigProperties.getUniqueString();
		
		
		
		
		//-- GUI
		
		// The account is already authenticated
		// However, we need to change the URL and open
		// the deep link form.
		
		ZimbraURI uri = new ZimbraURI(ZimbraURI.getCurrentURI());
		uri.addQuery("view", "compose");	// Setting view=compose will make PageMain return a FormMailNew object
		uri.addQuery("to", ZimbraAccount.AccountA().EmailAddress);
		uri.addQuery("cc", ZimbraAccount.AccountB().EmailAddress);
		uri.addQuery("bcc", ZimbraAccount.AccountC().EmailAddress);
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
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
		ZAssert.assertNotNull(sent, "Verify the message is received correctly");
		ZAssert.assertEquals(sent.dToRecipients.get(0).dEmailAddress, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(sent.dCcRecipients.get(0).dEmailAddress, ZimbraAccount.AccountB().EmailAddress, "Verify the cc field is correct");
		ZAssert.assertEquals(sent.dBccRecipients.get(0).dEmailAddress, ZimbraAccount.AccountC().EmailAddress, "Verify the bcc field is correct");

		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
		
		received = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
		
		received = MailItem.importFromSOAP(ZimbraAccount.AccountC(), "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
		
	}

	@Bugs( ids = "82734")
	@Test( description = "Create a mail with two 'to' adn two 'cc' addresses using the deep link URL",
			groups = { "functional", "L5" })
	public void CreateMail_03() throws HarnessException {
		
		
		
		//-- DATA
		
		
		// Create the message data to be sent
		final ZimbraAccount account1 = (new ZimbraAccount()).provision().authenticate();
		final ZimbraAccount account2 = (new ZimbraAccount()).provision().authenticate();
		final ZimbraAccount account3 = (new ZimbraAccount()).provision().authenticate();
		final ZimbraAccount account4 = (new ZimbraAccount()).provision().authenticate();
		final String subject = "subject" + ConfigProperties.getUniqueString();
		final String body = "body" + ConfigProperties.getUniqueString();
		
		
		
		
		//-- GUI
		
		// The account is already authenticated
		// However, we need to change the URL and open
		// the deep link form.
		
		ZimbraURI uri = new ZimbraURI(ZimbraURI.getCurrentURI());
		uri.addQuery("view", "compose");	// Setting view=compose will make PageMain return a FormMailNew object
		uri.addQuery("to", account1.EmailAddress);
		uri.addQuery("to", account2.EmailAddress);
		uri.addQuery("cc", account3.EmailAddress);
		uri.addQuery("cc", account4.EmailAddress);
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
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
		ZAssert.assertNotNull(sent, "Verify the message is received correctly");
		
		boolean found1 = false;
		boolean found2 = false;
		boolean found3 = false;
		boolean found4 = false;
		for ( RecipientItem r : sent.dToRecipients) {
			if ( r.dEmailAddress.equals(account1.EmailAddress) ) {
				found1 = true;
			}
			if ( r.dEmailAddress.equals(account2.EmailAddress) ) {
				found2 = true;
			}
		}
		for ( RecipientItem r : sent.dCcRecipients) {
			if ( r.dEmailAddress.equals(account3.EmailAddress) ) {
				found3 = true;
			}
			if ( r.dEmailAddress.equals(account4.EmailAddress) ) {
				found4 = true;
			}
		}
		ZAssert.assertTrue(found1, "Verify account1 (to) is correctly used");
		ZAssert.assertTrue(found2, "Verify account2 (to) is correctly used");
		ZAssert.assertTrue(found3, "Verify account3 (cc) is correctly used");
		ZAssert.assertTrue(found4, "Verify account4 (cc) is correctly used");
		
	}


	@Test( description = "Create a mail with two 'to' adn two 'cc' addresses (comma separated) using the deep link URL",
			groups = { "functional", "L3" })
	public void CreateMail_04() throws HarnessException {
		
		
		
		//-- DATA
		
		
		// Create the message data to be sent
		final ZimbraAccount account1 = (new ZimbraAccount()).provision().authenticate();
		final ZimbraAccount account2 = (new ZimbraAccount()).provision().authenticate();
		final ZimbraAccount account3 = (new ZimbraAccount()).provision().authenticate();
		final ZimbraAccount account4 = (new ZimbraAccount()).provision().authenticate();
		final String subject = "subject" + ConfigProperties.getUniqueString();
		final String body = "body" + ConfigProperties.getUniqueString();
		
		
		
		
		//-- GUI
		
		// The account is already authenticated
		// However, we need to change the URL and open
		// the deep link form.
		
		ZimbraURI uri = new ZimbraURI(ZimbraURI.getCurrentURI());
		uri.addQuery("view", "compose");	// Setting view=compose will make PageMain return a FormMailNew object
		uri.addQuery("to", account1.EmailAddress +","+ account2.EmailAddress);
		uri.addQuery("cc", account3.EmailAddress +","+ account4.EmailAddress);
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
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
		ZAssert.assertNotNull(sent, "Verify the message is received correctly");
		
		boolean found1 = false;
		boolean found2 = false;
		boolean found3 = false;
		boolean found4 = false;
		for ( RecipientItem r : sent.dToRecipients) {
			if ( r.dEmailAddress.equals(account1.EmailAddress) ) {
				found1 = true;
			}
			if ( r.dEmailAddress.equals(account2.EmailAddress) ) {
				found2 = true;
			}
		}
		for ( RecipientItem r : sent.dCcRecipients) {
			if ( r.dEmailAddress.equals(account3.EmailAddress) ) {
				found3 = true;
			}
			if ( r.dEmailAddress.equals(account4.EmailAddress) ) {
				found4 = true;
			}
		}
		ZAssert.assertTrue(found1, "Verify account1 (to) is correctly used");
		ZAssert.assertTrue(found2, "Verify account2 (to) is correctly used");
		ZAssert.assertTrue(found3, "Verify account3 (cc) is correctly used");
		ZAssert.assertTrue(found4, "Verify account4 (cc) is correctly used");
		
	}

	@DataProvider(name = "DataProviderMailtoBrackets")
	public Object[][] DataProviderDeleteKeys() {
	  return new Object[][] {
			    new Object[] { 
			    		"First Last <"+ ZimbraAccount.AccountA().EmailAddress +">", 
			    		"First Last <"+ ZimbraAccount.AccountA().EmailAddress +">" },
			    new Object[] { 
			    		"<"+ ZimbraAccount.AccountA().EmailAddress +">", 
			    		"<"+ ZimbraAccount.AccountA().EmailAddress +">" },
			    new Object[] { 
			    		"First <"+ ZimbraAccount.AccountA().EmailAddress +">", 
			    		"First <"+ ZimbraAccount.AccountA().EmailAddress +">" },
			    new Object[] { 
			    		"\"First Last\" <"+ ZimbraAccount.AccountA().EmailAddress +">", 
			    		"\"First Last\" <"+ ZimbraAccount.AccountA().EmailAddress +">" },
	  };
	}
	
	@Bugs( ids = "76182,80816")
	@Test( description = "Create a mail with to with angled brackets, i.e. to=First Last<email@domain.com>",
			dataProvider = "DataProviderMailtoBrackets",
			groups = { "functional", "L3" })
	public void CreateMail_05(String name, String value) throws HarnessException {
		
		
		
		//-- DATA
		
		
		// Create the message data to be sent
		final String subject = "subject" + ConfigProperties.getUniqueString();
		final String body = "body" + ConfigProperties.getUniqueString();
		
		
		
		
		//-- GUI
		
		// The account is already authenticated
		// However, we need to change the URL and open
		// the deep link form.
		
		ZimbraURI uri = new ZimbraURI(ZimbraURI.getCurrentURI());
		uri.addQuery("view", "compose");	// Setting view=compose will make PageMain return a FormMailNew object
		uri.addQuery("to", value);
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
		app.zGetActiveAccount().soapSend(
                "<SearchRequest xmlns='urn:zimbraMail' types='message'>" +
                   "<query>in:sent subject:("+ subject +")</query>" +
                "</SearchRequest>");
        String id = app.zGetActiveAccount().soapSelectValue("//mail:SearchResponse/mail:m", "id");

        app.zGetActiveAccount().soapSend(
                "<GetMsgRequest xmlns='urn:zimbraMail'>" +
                      "<m id='"+ id +"' />" +
                    "</GetMsgRequest>");
        
        String t = app.zGetActiveAccount().soapSelectValue("//mail:m//mail:e[@t='t']", "a");
        ZAssert.assertEquals(t, ZimbraAccount.AccountA().EmailAddress, "Verify the message is addressed correctly");
        
				
	}



}
