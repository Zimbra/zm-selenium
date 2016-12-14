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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.drafts;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.items.RecipientItem.RecipientType;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Shortcut;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.PageMail.Locators;

public class SaveDraftMail extends PrefGroupMailByMessageTest {

	public SaveDraftMail() {
		logger.info("New "+ SaveDraftMail.class.getCanonicalName());
	}

	@Test( description = "Save a basic draft (subject only)", groups = { "smoke", "L1" })
	
	public void SaveDraftMail_01() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();


		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);

		// Save the message
		mailform.zToolbarPressButton(Button.B_SAVE_DRAFT);
		SleepUtil.sleepMedium();
		mailform.zToolbarPressButton(Button.B_CANCEL);

		// Get the message from the server
		MailItem draft = MailItem.importFromSOAP(app.zGetActiveAccount(),
				"subject:("+ subject +")");

		FolderItem draftsFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Drafts);

		// Verify the draft data matches
		ZAssert.assertEquals(draft.dSubject, subject, "Verify the subject field is correct");
		ZAssert.assertEquals(draft.dFolderId, draftsFolder.getId(), "Verify the draft is saved in the drafts folder");
	}
	
	
	/**
	 * Test Case: Save draft using keyboard shortcut 'Escape''
	 * 1.Compose Text mail
	 * 2.Press 'Esc' key of keyboard
	 * 3.Verify 'SaveCurrentMessageAsDraft'Warning Dialog
	 * 4.Press Yes
	 * 5.Verify Message is present in Draft
	 * @throws HarnessException
	 */

	@Test( description = "Save draft using keyboard shortcut 'Escape'", groups = { "functional", "L2" })
	
	public void SaveDraftMail_02() throws HarnessException {

		Shortcut shortcut = Shortcut.S_ESCAPE;
		// Create the message data to be sent
		String body = "body" + ConfigProperties.getUniqueString();
		String subject = "subject" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertTrue(mailform.zIsActive(), "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		DialogWarning warning = (DialogWarning) app.zPageMail.zKeyboardShortcut(shortcut);
		ZAssert.assertNotNull(warning, "Verify the dialog is opened");
		
		

		warning.zClickButton(Button.B_YES);
		warning.zWaitForClose(); // Make sure the dialog is dismissed

		// Get the message from the server
		MailItem draft = MailItem.importFromSOAP(app.zGetActiveAccount(),"subject:(" + subject + ")");

		FolderItem draftsFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Drafts);

		// Verify the draft data matches
		ZAssert.assertEquals(draft.dSubject, subject,"Verify the subject field is correct");
		ZAssert.assertEquals(draft.dFolderId, draftsFolder.getId(),"Verify the draft is saved in the drafts folder");
	}
	

	@Test( description = "Save draft with To", groups = { "functional", "L2" })
	
	public void SaveDraftMail_03() throws HarnessException {

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyHtml = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertTrue(mailform.zIsActive(), "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFill(mail);

		// Save the message
		mailform.zToolbarPressButton(Button.B_SAVE_DRAFT);
		SleepUtil.sleepMedium();
		mailform.zToolbarPressButton(Button.B_CANCEL);
		
		if (app.zPageMail.sIsElementPresent(Locators.zSaveWarningDialog)) {
			app.zPageMail.zClickAt("css=div[id='YesNoCancel_button5']", "");
		}


		// Get the message from the server
		MailItem draft = MailItem.importFromSOAP(app.zGetActiveAccount(),"subject:(" + mail.dSubject + ")");

		// Verify the draft contains the To
		boolean found = false;
		for (RecipientItem r : draft.dToRecipients) {
			if ( r.dEmailAddress.equals(ZimbraAccount.AccountA().EmailAddress) ) {
				found = true;
			}
		}
		ZAssert.assertTrue(found, "Verify the To field contains the correct address(es)");
	}

	
	@Test( description = "Save draft with two receipients in To field", groups = { "functional", "L2" })
	
	public void SaveDraftMail_04() throws HarnessException {

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountB()));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyHtml = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertTrue(mailform.zIsActive(), "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFill(mail);

		// Save the message
		mailform.zToolbarPressButton(Button.B_SAVE_DRAFT);
		SleepUtil.sleepMedium();
		mailform.zToolbarPressButton(Button.B_CANCEL);
		
		if (app.zPageMail.sIsElementPresent(Locators.zSaveWarningDialog)) {
			app.zPageMail.zClickAt("css=div[id='YesNoCancel_button5']", "");
		}

		// Get the message from the server
		MailItem draft = MailItem.importFromSOAP(app.zGetActiveAccount(),"subject:(" + mail.dSubject + ")");

		// Verify the draft contains the To
		boolean foundA = false;
		boolean foundB = false;
		for (RecipientItem r : draft.dToRecipients) {
			if ( r.dEmailAddress.equals(ZimbraAccount.AccountA().EmailAddress) ) {
				foundA = true;
			}
			if ( r.dEmailAddress.equals(ZimbraAccount.AccountA().EmailAddress) ) {
				foundB = true;
			}
		}
		ZAssert.assertTrue(foundA, "Verify the To field contains the first correct address(es)");
		ZAssert.assertTrue(foundB, "Verify the To field contains the second correct address(es)");
	}

	
	@Test( description = "Save draft with Cc", groups = { "functional", "L2" })

	public void SaveDraftMail_05() throws HarnessException {

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA(), RecipientType.To));
		mail.dCcRecipients.add(new RecipientItem(ZimbraAccount.AccountB(), RecipientType.Cc));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyHtml = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertTrue(mailform.zIsActive(), "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFill(mail);

		// Save the message
		mailform.zToolbarPressButton(Button.B_SAVE_DRAFT);
		SleepUtil.sleepMedium();
		mailform.zToolbarPressButton(Button.B_CANCEL);
		
		if (app.zPageMail.sIsElementPresent(Locators.zSaveWarningDialog)) {
			app.zPageMail.zClickAt("css=div[id='YesNoCancel_button5']", "");
		}

		// Get the message from the server
		MailItem draft = MailItem.importFromSOAP(app.zGetActiveAccount(),"subject:(" + mail.dSubject + ")");

		// Verify the draft contains the To
		boolean found = false;
		for (RecipientItem r : draft.dCcRecipients) {
			if ( r.dEmailAddress.equals(ZimbraAccount.AccountB().EmailAddress) ) {
				found = true;
			}
		}
		ZAssert.assertTrue(found, "Verify the Cc field contains the correct address(es)");
	}


	@Test( description = "Save draft with 2 Cc", groups = { "functional", "L2" })
	
	public void SaveDraftMail_06() throws HarnessException {

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem((new ZimbraAccount()).provision().authenticate()));
		mail.dCcRecipients.add(new RecipientItem(ZimbraAccount.AccountA(), RecipientType.Cc));
		mail.dCcRecipients.add(new RecipientItem(ZimbraAccount.AccountB(), RecipientType.Cc));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyHtml = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertTrue(mailform.zIsActive(), "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFill(mail);

		// Save the message
		mailform.zToolbarPressButton(Button.B_SAVE_DRAFT);
		SleepUtil.sleepMedium();
		mailform.zToolbarPressButton(Button.B_CANCEL);
		SleepUtil.sleepMedium();
		
		if (app.zPageMail.sIsElementPresent(Locators.zSaveWarningDialog)) {
			app.zPageMail.zClickAt("css=div[id='YesNoCancel_button5']", "");
		}
		
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inbox);


		// Get the message from the server
		MailItem draft = MailItem.importFromSOAP(app.zGetActiveAccount(),"subject:(" + mail.dSubject + ")");

		// Verify the draft contains the To
		boolean foundA = false;
		boolean foundB = false;
		for (RecipientItem r : draft.dCcRecipients) {
			if ( r.dEmailAddress.equals(ZimbraAccount.AccountA().EmailAddress) ) {
				foundA = true;
			}
			if ( r.dEmailAddress.equals(ZimbraAccount.AccountB().EmailAddress) ) {
				foundB = true;
			}
		}
		ZAssert.assertTrue(foundA, "Verify the Cc field contains the first correct address(es)");
		ZAssert.assertTrue(foundB, "Verify the Cc field contains the second correct address(es)");
	}


	@Test( description = "Save draft with Bcc", groups = { "functional", "L2" })
	
	public void SaveDraftMail_07() throws HarnessException {

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
		mail.dBccRecipients.add(new RecipientItem(ZimbraAccount.AccountB(), RecipientType.Bcc));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyHtml = "body" + ConfigProperties.getUniqueString();

		//-- GUI
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inbox);

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		SleepUtil.sleepMedium();
		ZAssert.assertTrue(mailform.zIsActive(), "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFill(mail);

		SleepUtil.sleepLong();
		// Save the message
		mailform.zToolbarPressButton(Button.B_SAVE_DRAFT);
		SleepUtil.sleepLong();
		mailform.zToolbarPressButton(Button.B_CANCEL);
		
		if (app.zPageMail.sIsElementPresent(Locators.zSaveWarningDialog)) {
			app.zPageMail.zClickAt("css=div[id='YesNoCancel_button5']", "");
		}

		// Get the message from the server
		MailItem draft = MailItem.importFromSOAP(app.zGetActiveAccount(),"subject:(" + mail.dSubject + ")");

		// Verify the draft contains the To
		boolean found = false;
		for (RecipientItem r : draft.dBccRecipients) {
			if ( r.dEmailAddress.equals(ZimbraAccount.AccountB().EmailAddress) ) {
				found = true;
			}
		}
		ZAssert.assertTrue(found, "Verify the Bcc field contains the correct address(es)");
	}

	
	@Test( description = "Save draft with 2 Bcc", groups = { "functional", "L2" })
	
	public void SaveDraftMail_08() throws HarnessException {

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem((new ZimbraAccount()).provision().authenticate()));
		mail.dBccRecipients.add(new RecipientItem(ZimbraAccount.AccountA(), RecipientType.Bcc));
		mail.dBccRecipients.add(new RecipientItem(ZimbraAccount.AccountB(), RecipientType.Bcc));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyHtml = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertTrue(mailform.zIsActive(), "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFill(mail);
		SleepUtil.sleepLong();
		// Save the message
		mailform.zToolbarPressButton(Button.B_SAVE_DRAFT);
		SleepUtil.sleepLong();
		mailform.zToolbarPressButton(Button.B_CANCEL);

		if (app.zPageMail.sIsElementPresent(Locators.zSaveWarningDialog)) {
			app.zPageMail.zClickAt("css=div[id='YesNoCancel_button5']", "");
		}

		// Get the message from the server
		MailItem draft = MailItem.importFromSOAP(app.zGetActiveAccount(),"subject:(" + mail.dSubject + ")");

		// Verify the draft contains the To
		boolean foundA = false;
		boolean foundB = false;
		for (RecipientItem r : draft.dBccRecipients) {
			if ( r.dEmailAddress.equals(ZimbraAccount.AccountA().EmailAddress) ) {
				foundA = true;
			}
			if ( r.dEmailAddress.equals(ZimbraAccount.AccountB().EmailAddress) ) {
				foundB = true;
			}
		}
		ZAssert.assertTrue(foundA, "Verify the Bcc field contains the first correct address(es)");
		ZAssert.assertTrue(foundB, "Verify the Bcc field contains the second correct address(es)");

	}
}
