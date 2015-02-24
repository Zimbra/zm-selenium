package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.contextmenu;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.addressbook.FormContactNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.PageMail.Locators;

public class AddToContactsAddressContextMenu extends PrefGroupMailByMessageTest {

	public AddToContactsAddressContextMenu() {
		logger.info("New "
				+ AddToContactsAddressContextMenu.class.getCanonicalName());

		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");

	}

	@Test(description = "Right click to bubble address>>AddToContact", groups = { "smoke" })
	public void AddToContactAddressContextMenu() throws HarnessException {

		String contactFirst = "First"
				+ ZimbraSeleniumProperties.getUniqueString();
		;

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountB(),
				RecipientItem.RecipientType.To));

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail
				.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data

		mailform.zFill(mail);
		SleepUtil.sleepMedium();
		String OriginalEmailAddr = app.zPageMail
				.sGetText(Locators.zToAddressBubble);
		app.zPageMail.zRightClickAddressBubble(Field.To);
		app.zPageMail.AddToContactAddressContextMenu();
		SleepUtil.sleepMedium();

		app.zPageMail.sFocus("css=input[id$='_FIRST_input']");
		app.zPageMail.sType("css=input[id$='_FIRST_input']", contactFirst);
		app.zPageMail.sClickAt(FormContactNew.Toolbar.SAVE, "");
		SleepUtil.sleepMedium();

		// -- Data Verification

		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='contact'>"
						+ "<query>#firstname:" + contactFirst + "</query>"
						+ "</SearchRequest>");
		String contactId = app.zGetActiveAccount().soapSelectValue("//mail:cn",
				"id");

		ZAssert.assertNotNull(contactId,"Verify the contact is returned in the search");

		app.zGetActiveAccount().soapSend(
				"<GetContactsRequest xmlns='urn:zimbraMail'>" + "<cn id='"
						+ contactId + "'/>" + "</GetContactsRequest>");

		String firstname = app.zGetActiveAccount().soapSelectValue(
				"//mail:cn[@id='" + contactId + "']//mail:a[@n='firstName']",
				null);
		String email = app.zGetActiveAccount().soapSelectValue(
				"//mail:cn[@id='" + contactId + "']//mail:a[@n='email']", null);

		ZAssert.assertEquals(firstname, contactFirst,
				"Verify the first name was saved correctly");
		ZAssert.assertEquals(email, OriginalEmailAddr,
				"Verify the email was saved correctly");

	}

}