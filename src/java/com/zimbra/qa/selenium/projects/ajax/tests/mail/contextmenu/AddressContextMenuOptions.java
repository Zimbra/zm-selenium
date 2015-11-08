package com.zimbra.qa.selenium.projects.ajax.tests.mail.contextmenu;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;

public class AddressContextMenuOptions extends PrefGroupMailByMessageTest {

	public AddressContextMenuOptions() {
		logger.info("New " + AddressContextMenuOptions.class.getCanonicalName());

		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");

	}

	@Test(description = "Right click To bubble address>>Verify Delete/Copy/Edit/Expand/AddtoContact/Moveto-cc-bcc menus", groups = { "sanity-temp-skipped" })
	public void VerifyAddressContextMenuOptions() throws HarnessException {

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountB(),
				RecipientItem.RecipientType.To));

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail
				.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		
		SleepUtil.sleepMedium();
		mailform.zFill(mail);
		SleepUtil.sleepMedium();
		app.zPageMail.zRightClickAddressBubble(Field.To);
		ZAssert.assertTrue(app.zPageMail.zVerifyAllAddressContextMenu(""),
				"Delete/Copy/Edit/Expand/AddtoContact/Moveto-cc-bcc menu should be exist");

	}

}
