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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.contextmenu;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.PageMail.Locators;

public class NewEmail extends SetGroupMailByMessagePreference {

	public NewEmail() {
		logger.info("New " + NewEmail.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
	}


	@Test (description = "Right click from header and verify New Email option",
			groups = { "bhr" })

	public void NewEmail_01() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<e t='c' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>"+ "body" + ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");

		// Get all the SOAP data for later verification
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message present in current view");

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);
		ZAssert.assertEquals(actual.zGetMailProperty(com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail.Field.From), ZimbraAccount.AccountA().EmailAddress, "Verify the From matches");

		app.zPageMail.zRightClickAddressBubble(Field.From);
		app.zPageMail.NewEmailMsgHdrContextMenu();
		SleepUtil.sleepMedium();
		logger.info(ZimbraAccount.AccountA().EmailAddress );
		ZAssert.assertEquals(app.zPageMail.sGetText(Locators.zToAddressBubble), ZimbraAccount.AccountA().EmailAddress , "Compose window opens with email address should present in To field");
	}
}