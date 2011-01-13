package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Shortcut;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;


public class CreateMailText extends AjaxCommonTest {

	public CreateMailText() {
		logger.info("New "+ CreateMailText.class.getCanonicalName());
		
		// All tests start at the login page
		super.startingPage = app.zPageMail;
		super.startingAccount = new ZimbraAccount();
		super.startingAccount.provision();
		super.startingAccount.authenticate();
		super.startingAccount.modifyPreference("zimbraPrefComposeFormat", "text");
		
	}
	
	@Test(	description = "Send a mail using Text editor",
			groups = { "sanity" })
	public void CreateMailText_01() throws HarnessException {
		
		
		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
		mail.dSubject = "subject" + ZimbraSeleniumProperties.getUniqueString();
		mail.dBodyText = "body" + ZimbraSeleniumProperties.getUniqueString();
		
		
		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		
		// Fill out the form with the data
		mailform.zFill(mail);
		
		// Send the message
		mailform.zSubmit();
				
		
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ mail.dSubject +")");
		
		
		// TODO: add checks for TO, Subject, Body
		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(received.dSubject, mail.dSubject, "Verify the subject field is correct");
		ZAssert.assertStringContains(received.dBodyText, mail.dBodyText, "Verify the body field is correct");
		
	}

	
	@DataProvider(name = "DataProvideNewMessageShortcuts")
	public Object[][] DataProvideNewMessageShortcuts() {
	  return new Object[][] {
			  new Object[] { Shortcut.S_NEWITEM, Shortcut.S_NEWITEM.getKeys() },
			  new Object[] { Shortcut.S_NEWMESSAGE, Shortcut.S_NEWMESSAGE.getKeys() },
			  new Object[] { Shortcut.S_NEWMESSAGE2, Shortcut.S_NEWMESSAGE2.getKeys() }
	  };
	}
	
	@Test(	description = "Send a mail using Text editor using keyboard shortcuts",
			groups = { "smoke" },
			dataProvider = "DataProvideNewMessageShortcuts")
	public void CreateMailText_02(Shortcut shortcut, String keys) throws HarnessException {
		
		
		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
		mail.dSubject = "subject" + ZimbraSeleniumProperties.getUniqueString();
		mail.dBodyText = "body" + ZimbraSeleniumProperties.getUniqueString();
		
		
		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zKeyboardShortcut(shortcut);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		
		// Send the message
		mailform.zFill(mail);
		mailform.zSubmit();
				
		
		// From the receipient end, make sure the message is received
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ mail.dSubject +")");
		ZAssert.assertNotNull(received, "Verify the message is received");
		
	}

}
