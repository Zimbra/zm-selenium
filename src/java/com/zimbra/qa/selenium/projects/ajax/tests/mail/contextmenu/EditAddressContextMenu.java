/*
 * ***** BEGIN LICENSE BLOCK *****
 *
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
 *
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.mail.contextmenu;

import java.awt.event.KeyEvent;
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
		logger.info("New " + EditAddressContextMenu.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
	}


	@Test (description = "Right click to bubble address >> Edit",
			groups = { "smoke", "L1" })

	public void EditToAddressContextMenu_01() throws HarnessException {

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountB(), RecipientItem.RecipientType.To));

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFill(mail);

		app.zPageMail.zRightClickAddressBubble(Field.To);
		app.zPageMail.zEditAddressContextMenu();
		app.zPageMail.sFocus(FormMailNew.Locators.zToField);
		app.zPageMail.sClick(FormMailNew.Locators.zToField);
		app.zPageMail.zType(FormMailNew.Locators.zToField, "test@test.com");
		app.zPageMail.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);

		SleepUtil.sleepMedium();
		ZAssert.assertEquals(app.zPageMail.sGetText(Locators.zToAddressBubble), "test@test.com", "Edited address should present");
	}
}