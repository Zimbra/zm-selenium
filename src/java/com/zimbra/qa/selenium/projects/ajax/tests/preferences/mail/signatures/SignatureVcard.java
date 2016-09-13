package com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.signatures;

/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
import org.testng.Assert;
import org.testng.annotations.Test;













import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem;
import com.zimbra.qa.selenium.framework.items.SignatureItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.DialogSelectContact;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.DialogSelectContact.Locators;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.signature.FormSignatureNew;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.signature.FormSignatureNew.Field;


public class SignatureVcard extends AjaxCommonTest {
	public SignatureVcard() {
		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = null;
	}

	@Test( description = "Verify Signature Vcard thoough GUI", groups = { "functional" })
	public void SignatureVcard_01() throws HarnessException {

		// Create a contact
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());
		String sigName = "signame" + ConfigProperties.getUniqueString();
		String sigBody = "sigbody" + ConfigProperties.getUniqueString();

		// click on signature from left pane
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK,TreeItem.MailSignatures);

		//Click on New signature button
		FormSignatureNew signew =(FormSignatureNew) app.zPageSignature.zToolbarPressButton(Button.B_NEW);

		// Fill Signature Name and body
		signew.zFillField(Field.SignatureName, sigName);
		signew.zFillField(Field.SignatureBody, sigBody);

		//click Browse button to select contact from Select Contact Dialog

		DialogSelectContact selectContactDialog =(DialogSelectContact) app.zPageSignature.zToolbarPressButton(Button.B_BROWSE);

		//Verify Select Contact is active
		selectContactDialog.zIsActive();

		//Enter contact name 
		selectContactDialog.zEnterContacts(contact.getAttribute("email"));
		SleepUtil.sleepMedium();	

		//Click Search button		
		selectContactDialog.zClickButton(Button.B_SEARCH);
		SleepUtil.sleepMedium();

		//Verify contact shows in search list
		selectContactDialog.sIsElementPresent(Locators.zListIcon);	
		SleepUtil.sleepMedium();

		//Click OK
		selectContactDialog.zClickButton(Button.B_OK);

		//Click Save
		signew.zSubmit();

		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), sigName);
		//Verify signature name and body content	
		ZAssert.assertEquals(signature.getName(),sigName,"Verify signature Name");
		ZAssert.assertEquals(signature.dBodyText,sigBody,"Verify Text signature body");

		//Go to Mail Tab(Explicitly)
		app.zPageMail.zNavigateTo();				
		SleepUtil.sleepMedium();

		MailItem mail = new MailItem();

		FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountZWC()));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyText = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFill(mail);

		//click Signature drop down and add signature
		app.zPageMail.zToolbarPressPulldown(Button.B_OPTIONS,Button.O_ADD_SIGNATURE,sigName);

		//Vrify Attachment present in compose window
		Assert.assertTrue(app.zPageMail.sIsElementPresent("css=a[class='AttLink']"),"vcf attachment link present");

		//Verify Signature present in body
		Assert.assertEquals(app.zPageMail.sGetText("css=body[id='tinymce'] div[data-marker='__SIG_PRE__']"),sigBody);

		// Send the message
		mailform.zSubmit();

		//Verify signature body and attachment through UI

		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject.toString());	
		SleepUtil.sleepSmall();
		
		
		//Verify Attachment present in Reading pane
		Assert.assertTrue(app.zPageMail.sIsElementPresent("css=a[class='AttLink']"),"vcf attachment link present");

		//Verify Signature present in body
		Assert.assertEquals(app.zPageMail.sGetText("css=body[id='tinymce'] div[data-marker='__SIG_PRE__']"),sigBody);



	}


}

