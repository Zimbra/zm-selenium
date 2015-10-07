package com.zimbra.qa.selenium.projects.ajax.tests.mail.contextmenu;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;


public class CopyAddressContextMenu extends PrefGroupMailByMessageTest {

	public CopyAddressContextMenu() {
		logger.info("New "
				+ CopyAddressContextMenu.class.getCanonicalName());

		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");

	}

	@Test(description = "Right click BCc bubble address>>Copy", groups = { "Incomplete" })
	public void CopyToAddressContextMenu() throws HarnessException {

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

		app.zPageMail.CopyAddressContextMenu();
		app.zPageMail.sFocus(FormMailNew.Locators.zCcField);
		app.zPageMail.zClick(FormMailNew.Locators.zCcField);	

		/* Incomplete test case.. need to find clikcable event while doing copy*/
		//FormMailNew.Locators.CcField
		app.zPageMail.zKeyboardShortcut(Shortcut.S_PASTE);
		logger.info("hello");
		

	}

}