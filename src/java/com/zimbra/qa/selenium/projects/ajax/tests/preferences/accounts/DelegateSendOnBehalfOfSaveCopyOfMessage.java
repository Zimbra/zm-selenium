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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.accounts;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;


public class DelegateSendOnBehalfOfSaveCopyOfMessage extends PrefGroupMailByMessageTest {

	public DelegateSendOnBehalfOfSaveCopyOfMessage() {
		logger.info("New "+ DelegateSendOnBehalfOfSaveCopyOfMessage.class.getCanonicalName());

		super.startingPage = app.zPageMail;

	}

	@Test( description = "Save a copy of sent messages to owner's Sent folder",
			groups = { "functional", "L3" })
	
	public void DelegateSendOnBehalfOfSaveCopyOfMessage_01() throws HarnessException {

		// Mail data
		String subject = "subject"+ ConfigProperties.getUniqueString();

		// The grantor - new account
		ZimbraAccount grantor = null;
		grantor = new ZimbraAccount();
		grantor.provision();
		grantor.authenticate();
		
		//Configure 'Delegate Send Settings' to - Save a copy of sent messages to my Sent folder
		grantor.soapSend(
				"<GrantRightsRequest xmlns='urn:zimbraAccount'>"
						+		"<ace gt='usr' d='"+ app.zGetActiveAccount().EmailAddress +"' right='sendOnBehalfOf'/>"
						+	"</GrantRightsRequest>");
		
		grantor.soapSend(
				"<ModifyPrefsRequest xmlns='urn:zimbraAccount'>"
			+		"<pref name='zimbraPrefDelegatedSendSaveTarget'>owner</pref>"
			+	"</ModifyPrefsRequest>");
		
		// Refresh UI
		app.zPageMain.sRefresh();
		
		//-- GUI Steps

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountB().EmailAddress);
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, "body" + ConfigProperties.getUniqueString());
		mailform.zFillField(Field.From, grantor.EmailAddress);	
		mailform.zSubmit();

		//-- Data Verification	
		//Verify sent message is not present in delegate's Sent folder
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ subject +")");
		ZAssert.assertNull(sent, "Verify sent message is not present in Delegate's Sent folder");
		
		//Verify sent message is present in owner's Sent folder
		MailItem actual = MailItem.importFromSOAP(grantor, "in:sent subject:("+ subject +")");
		ZAssert.assertNotNull(actual, "Verify the subject field is correct");
		
	}
	
	@Test( description = "Save a copy of sent messages to delegate's Sent folder",
			groups = { "functional", "L3" })
	
	public void DelegateSendOnBehalfOfSaveCopyOfMessage_02() throws HarnessException {

		// Mail data
		String subject = "subject"+ ConfigProperties.getUniqueString();

		// The grantor - new account
		ZimbraAccount grantor = null;
		grantor = new ZimbraAccount();
		grantor.provision();
		grantor.authenticate();

		//Configure 'Delegate Send Settings' to - Save a copy of sent messages to delegate's Sent folder
		grantor.soapSend(
				"<GrantRightsRequest xmlns='urn:zimbraAccount'>"
						+		"<ace gt='usr' d='"+ app.zGetActiveAccount().EmailAddress +"' right='sendOnBehalfOf'/>"
						+	"</GrantRightsRequest>");
		
		grantor.soapSend(
				"<ModifyPrefsRequest xmlns='urn:zimbraAccount'>"
			+		"<pref name='zimbraPrefDelegatedSendSaveTarget'>sender</pref>"
			+	"</ModifyPrefsRequest>");

		
		// Refresh UI
		app.zPageMain.sRefresh();
		
		//-- GUI Steps

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountB().EmailAddress);
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, "body" + ConfigProperties.getUniqueString());
		mailform.zFillField(Field.From, grantor.EmailAddress);	
		mailform.zSubmit();

		//-- Data Verification	
		//Verify sent message is present in delegate's sent folder
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ subject +")");
		ZAssert.assertNotNull(sent, "Verify sent message is present in delegate's Sent folder");
		
		//Verify sent message is present in grantor's sent folder
		MailItem actual = MailItem.importFromSOAP(grantor, "in:sent subject:("+ subject +")");
		ZAssert.assertNull(actual, "Verify the subject field is correct");
		
	}
	
	@Test( description = "Save a copy of sent messages to delegate's Sent folder and granter's Sent folder",
			groups = { "functional", "L3" })
	
	public void DelegateSendOnBehalfOfSaveCopyOfMessage_03() throws HarnessException {

		// Mail data
		String subject = "subject"+ ConfigProperties.getUniqueString();

		// The grantor - new account
		ZimbraAccount grantor = null;
		grantor = new ZimbraAccount();
		grantor.provision();
		grantor.authenticate();

		//Configure 'Delegate Send Settings' to - Save a copy of sent messages to delegate's Sent folder and my Sent folder
		grantor.soapSend(
				"<GrantRightsRequest xmlns='urn:zimbraAccount'>"
						+		"<ace gt='usr' d='"+ app.zGetActiveAccount().EmailAddress +"' right='sendOnBehalfOf'/>"
						+	"</GrantRightsRequest>");

		SleepUtil.sleepSmall();
		
		grantor.soapSend(
				"<ModifyPrefsRequest xmlns='urn:zimbraAccount'>"
			+		"<pref name='zimbraPrefDelegatedSendSaveTarget'>both</pref>"
			+	"</ModifyPrefsRequest>");

		
		// Refresh UI
		app.zPageMain.sRefresh();
		
		//-- GUI Steps

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountB().EmailAddress);
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, "body" + ConfigProperties.getUniqueString());
		mailform.zFillField(Field.From, grantor.EmailAddress);	
		mailform.zSubmit();

		//-- Data Verification	
		//Verify sent message is present in delegate's sent folder
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ subject +")");
		ZAssert.assertNotNull(sent, "Verify sent message is present in delegate's Sent folder");
		
		//Verify sent message is present in grantor's sent folder
		MailItem sent1 = MailItem.importFromSOAP(grantor, "in:sent subject:("+ subject +")");
		ZAssert.assertNotNull(sent1, "Verify sent message is present in grantor's Sent folder");
		
	}
	
	@Test( description = "Don't save a copy of sent messages",
			groups = { "functional", "L3" })
	
	public void DelegateSendOnBehalfOfSaveCopyOfMessage_04() throws HarnessException {

		// Mail data
		String subject = "subject"+ ConfigProperties.getUniqueString();

		// The grantor - new account
		ZimbraAccount grantor = null;
		grantor = new ZimbraAccount();
		grantor.provision();
		grantor.authenticate();

		//Configure 'Delegate Send Settings' to - Don't save a copy of sent messages
		grantor.soapSend(
				"<GrantRightsRequest xmlns='urn:zimbraAccount'>"
						+		"<ace gt='usr' d='"+ app.zGetActiveAccount().EmailAddress +"' right='sendOnBehalfOf'/>"
						+	"</GrantRightsRequest>");
		
		grantor.soapSend(
				"<ModifyPrefsRequest xmlns='urn:zimbraAccount'>"
			+		"<pref name='zimbraPrefDelegatedSendSaveTarget'>none</pref>"
			+	"</ModifyPrefsRequest>");

		
		// Refresh UI
		app.zPageMain.sRefresh();
		
		//-- GUI Steps

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountB().EmailAddress);
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, "body" + ConfigProperties.getUniqueString());
		mailform.zFillField(Field.From, grantor.EmailAddress);	
		mailform.zSubmit();

		//-- Data Verification	
		//Verify sent message is not present in delegate's sent folder
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ subject +")");
		ZAssert.assertNull(sent, "Verify the subject field is correct");
		
		//Verify sent message is not present in grantor's sent folder
		MailItem sent1 = MailItem.importFromSOAP(grantor, "in:sent subject:("+ subject +")");
		ZAssert.assertNull(sent1, "Verify the subject field is correct");
		
	}
}
