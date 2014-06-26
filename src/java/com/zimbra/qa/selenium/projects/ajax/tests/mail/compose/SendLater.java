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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose;

import java.util.GregorianCalendar;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning.DialogWarningID;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DialogSendLater;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;


public class SendLater extends PrefGroupMailByMessageTest {

	public SendLater() {
		logger.info("New "+ SendLater.class.getCanonicalName());
		
		
		
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
		super.startingAccountPreferences.put("zimbraFeatureMailSendLaterEnabled", "TRUE");
		
	}
	
	@Test(	description = "Send a mail later using Send -> Send Later",
			groups = { "smoke" })
	public void SendLater_01() throws HarnessException {
		
		
		// Create the message data to be sent
		String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();
		
		
		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		
		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, "body" + ZimbraSeleniumProperties.getUniqueString());
		mailform.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);

		DialogSendLater dialog = (DialogSendLater)mailform.zToolbarPressPulldown(Button.B_SEND, Button.O_SEND_SEND_LATER);

		// Don't change the default information.  Just click OK.
		dialog.zClickButton(Button.B_OK);
		
		DialogWarning warning = (DialogWarning) app.zPageMain.zGetWarningDialog(DialogWarningID.SelectedTimeIsInPast);
		if ( warning.zIsActive() ) {
			warning.zClickButton(Button.B_OK);
		}
		
		// Difficult to determine how to verify.  If we searched drafts, the message already
		// may have been sent.
		//
		// For this test case, just verify that the dialog works.  No further verification
		// here.  (See next test case)
		// 
	}

	
	@Test(	description = "Send a mail later using Send -> Send Later - specify time in future",
			groups = { "smoke" })
	public void SendLater_02() throws HarnessException {
		
		// Create the message data to be sent
		GregorianCalendar calendar = new GregorianCalendar(2015, 11, 25, 12, 0, 0); // Dec 25, 2015 at noon local time
		String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();


		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		
		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, "body" + ZimbraSeleniumProperties.getUniqueString());
		mailform.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);

		DialogSendLater dialog = (DialogSendLater)mailform.zToolbarPressPulldown(Button.B_SEND, Button.O_SEND_SEND_LATER);

		// Enter the dialog information
		dialog.zFill(calendar);
		
		dialog.zClickButton(Button.B_OK);
		
		
		// Verify the message is queue'ed to send later
		MailItem draft = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
		ZAssert.assertNotNull(draft, "Verify the message is saved in drafts to be sent later");
		ZAssert.assertNotNull(draft.getAutoSendTime(), "Verify AutoSendTime is set");

	}

	@Test(	description = "Send a mail now using Send -> Send",
			groups = { "smoke" })
	public void SendLater_03() throws HarnessException {
		
		
		// Create the message data to be sent
		String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();
		
		
		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		
		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, "body" + ZimbraSeleniumProperties.getUniqueString());
		mailform.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);

		mailform.zToolbarPressPulldown(Button.B_SEND, Button.O_SEND_SEND);
		
		// Verify the message is received immediately
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify message is recieved immediately at destination account");
				
	}



}
