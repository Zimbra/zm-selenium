package com.zimbra.qa.selenium.projects.universal.tests.mail.compose;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.SeparateWindowFormMailNew;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.TreePreferences.TreeItem;

public class AddContactToEmailedContactNewWindow extends PrefGroupMailByMessageTest {

	public AddContactToEmailedContactNewWindow() {
		logger.info("New "+ AddContactToEmailedContactNewWindow.class.getCanonicalName());

		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences.put("zimbraPrefAutoAddAddressEnabled", "TRUE");

	}

	@Test( description = "Bug 62727 - Sending email to from new window should register contact under email contact list ",
			groups = { "functional", "L3" })

	public void AddContactToEmailedContactNewWindow_01() throws HarnessException {

		// Set 'Always compose in new window'from preferences	
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Mail);	
		String composelocator = "css=input[id$='_MAIL_NEW_WINDOW_COMPOSE']"; //Locator for selecting 'Always compose new window'
		app.zPagePreferences.zCheckboxSet(composelocator, true);		
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		FolderItem emailedContacts = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.EmailedContacts);

		ZimbraAccount receiver = new ZimbraAccount();
		receiver.provision();
		receiver.authenticate();

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(receiver));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();

		// Open the new mail form
		SeparateWindowFormMailNew window = null;
		window = (SeparateWindowFormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW_IN_NEW_WINDOW);
		window.zSetWindowTitle("Compose");
		window.zWaitForActive();
		window.waitForComposeWindow();
		ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");

		// Fill out the form with the data
		window.zFill(mail);
		mail.dBodyHtml = "body" + ConfigProperties.getUniqueString();
		window.sSelectWindow("Zimbra: Compose");
		String locator = "css=iframe[id*=ifr]";
		window.zWaitForElementPresent(locator, "5000");
		window.sClickAt(locator,"");
		window.zTypeFormattedText(locator, mail.dBodyHtml);

		// Send the message
		window.zToolbarPressButton(Button.B_SEND);

		// Soap verification
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='contact'>"
						+		"<query>"+ receiver.EmailAddress +"</query>"
						+	"</SearchRequest>");

		String email = app.zGetActiveAccount().soapSelectValue("//mail:cn//mail:a[@n='email']", null);
		ZAssert.assertEquals(email, receiver.EmailAddress, "Verify the recipient is in the contacts folder");

		String folder = app.zGetActiveAccount().soapSelectValue("//mail:cn", "l");
		ZAssert.assertEquals(folder, emailedContacts.getId(), "Verify the recipient is in the Emailed Contacts folder");

		// UI verification 
		// Go to emailed contacts folder
		app.zPageContacts.zNavigateTo();
		app.zTreeContacts.zClickAt("css=td[id='zti__main_Contacts__13_textCell']:contains('Emailed Contacts')", "");

		ZAssert.assertFalse(app.zTreeContacts.sIsElementPresent("css=div[id^='zlif__CNS-main__']:contains('"+ receiver.DisplayName + "')"), "Verify that receiver contact is present in emailed contact list");

	}
}
