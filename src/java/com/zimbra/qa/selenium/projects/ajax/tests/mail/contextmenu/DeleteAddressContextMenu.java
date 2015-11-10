package com.zimbra.qa.selenium.projects.ajax.tests.mail.contextmenu;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;

public class DeleteAddressContextMenu extends PrefGroupMailByMessageTest {

	public DeleteAddressContextMenu() {
		logger.info("New " + DeleteAddressContextMenu.class.getCanonicalName());

		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");

	}

	@Test(description = "Right click To bubble address>>Delete", groups = { "smoke" })
	public void DeleteToAddressContextMenu() throws HarnessException {

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA(),
				RecipientItem.RecipientType.To));

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail
				.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data

		mailform.zFill(mail);
		app.zPageMail.zRightClickAddressBubble(Field.To);
		app.zPageMail.DeleteAddressContextMenu();
		ZAssert.assertTrue(app.zPageMail.zHasTOCcBccEmpty(),
				"To/Cc/Bcc should be empty");

	}

	@Test(description = "Right click Cc bubble address>>Delete", groups = { "smoke" })
	public void DeleteCcAddressContextMenu() throws HarnessException {

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dCcRecipients.add(new RecipientItem(ZimbraAccount.AccountB(),
				RecipientItem.RecipientType.Cc));

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail
				.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data

		mailform.zFill(mail);
		app.zPageMail.zRightClickAddressBubble(Field.Cc);
		app.zPageMail.DeleteAddressContextMenu();
		ZAssert.assertTrue(app.zPageMail.zHasTOCcBccEmpty(),
				"To/Cc/Bcc should be empty");

	}

	@Test(description = "Right click BCc bubble address>>Delete", groups = { "smoke" })
	public void DeleteBccAddressContextMenu() throws HarnessException {

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dBccRecipients.add(new RecipientItem(ZimbraAccount.AccountB(),
				RecipientItem.RecipientType.Bcc));

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail
				.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data

		mailform.zFill(mail);
		app.zPageMail.zRightClickAddressBubble(Field.Bcc);
		app.zPageMail.DeleteAddressContextMenu();
		ZAssert.assertTrue(app.zPageMail.zHasTOCcBccEmpty(),
				"To/Cc/Bcc should be empty");

	}
	
}