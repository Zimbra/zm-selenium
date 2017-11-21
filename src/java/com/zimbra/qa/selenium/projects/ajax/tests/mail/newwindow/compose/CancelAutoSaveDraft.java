/*
 * ***** BEGIN LICENSE BLOCK *****
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
 * ***** END LICENSE BLOCK *****
 */
 package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.compose;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.SeparateWindowFormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class CancelAutoSaveDraft extends SetGroupMailByMessagePreference {

	public CancelAutoSaveDraft() {
		logger.info("New "+ CancelAutoSaveDraft.class.getCanonicalName());
		super.startingPage = app.zPagePreferences;
	}


	@Bugs (ids = "99875")
	@Test (description = "Bug 99875 - 'Message gets auto saved while clicking cancel and 'Auto draft save'= No, from New window ",
			groups = { "functional", "L2" })

	public void CancelAutoSaveDraft_01() throws HarnessException {

		// Set 'Include original message as an attachment' from preferences
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Mail);
		app.zPagePreferences.sClick("css=td[id='Prefs_Select_FORWARD_INCLUDE_WHAT_dropdown']");

		// Select 'Include original message as attachment'
		app.zPagePreferences.sClickAt(("css=div[parentid='Prefs_Select_FORWARD_INCLUDE_WHAT_Menu_1'] td[id$='_title']:contains('Include original message as an attachment')"),"");
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		// Create message
		String subject = "subject"+ ConfigProperties.getUniqueString();
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// Forward email
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_FORWARD);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		SeparateWindowFormMailNew window = null;
		String windowTitle = "Zimbra: Forward";

		try {

			window = (SeparateWindowFormMailNew) app.zPageMail.zToolbarPressButton(Button.B_DETACH_COMPOSE);
			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");

			// Go to draft and check for draft message
			mailform.zToolbarPressButton(Button.B_CANCEL);

			// Do not save message
			app.zPageMail.sClick("css=div[id='YesNoCancel'] div[id$='_buttons'] td[id$='_button4_title']");

			MailItem draft = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
			ZAssert.assertNotNull(draft, "Verify the message is not present in drafts folder");

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}
	}
}