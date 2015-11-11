package com.zimbra.qa.selenium.projects.ajax.tests.mail.contextmenu;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.PageMail.Locators;

public class EditAddressContextMenu extends PrefGroupMailByMessageTest {

	public EditAddressContextMenu() {
		logger.info("New "
				+ EditAddressContextMenu.class.getCanonicalName());

		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");

	}

	@Test(description = "Right click to bubble address>>Edit", groups = { "smoke-skip" })
	public void EditToAddressContextMenu() throws HarnessException {

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

		app.zPageMail.zRightClickAddressBubble(Field.To);		
		app.zPageMail.EditAddressContextMenu();
		app.zPageMail.sFocus(FormMailNew.Locators.zToField);
		app.zPageMail.zClick(FormMailNew.Locators.zToField);	
		app.zPageMail.zType(FormMailNew.Locators.zToField, "test@test.com");
		app.zPageMail.sKeyDown(FormMailNew.Locators.zToField, "13");
		//String ss1= app.zPageMail.sGetText(Locators.zToAddressBubble);
		SleepUtil.sleepMedium();
		ZAssert.assertEquals(app.zPageMail.sGetText(Locators.zToAddressBubble), "test@test.com", "Edited address should present");	

	}

}