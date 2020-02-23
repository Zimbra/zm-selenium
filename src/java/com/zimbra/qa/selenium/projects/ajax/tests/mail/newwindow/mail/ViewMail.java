/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.mail;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.SeparateWindowDisplayMail;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail.Field;

public class ViewMail extends SetGroupMailByMessagePreference {

	public ViewMail() throws HarnessException {
		logger.info("New "+ ViewMail.class.getCanonicalName());
	}


	@Test (description = "Receive a mail with Sender: specified",
			groups = { "sanity" })

	public void ViewMail_01() throws HarnessException {

		final String subject = "subject12996131112962";
		final String from = ConfigProperties.getStringProperty("adminUser") + "@" + ConfigProperties.getStringProperty("testdomain");
		final String sender = "sender12996131112962@" + ConfigProperties.getStringProperty("testdomain");;
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email00/mime_wSender.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(mail, "Verify message is received");
		ZAssert.assertEquals(from, mail.dFromRecipient.dEmailAddress, "Verify the from matches");
		ZAssert.assertEquals(sender, mail.dSenderRecipient.dEmailAddress, "Verify the sender matches");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message present in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + subject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");

			// Verify the To, From, Subject, Body
			String actualOBO = window.zGetMailProperty(Field.OnBehalfOf);
			ZAssert.assertEquals(actualOBO, from, "Verify the On-Behalf-Of matches the 'From:' header");

			String actualSender = window.zGetMailProperty(Field.From);
			ZAssert.assertEquals(actualSender, sender, "Verify the From matches the 'Sender:' header");

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}
	}


	@Bugs (ids = "86168")
	@Test (description = "Receive a mail with Reply-To: specified",
			groups = { "sanity" })

	public void ViewMail_02() throws HarnessException {

		final String subject = "subject13016959916873";
		final String from = ConfigProperties.getStringProperty("adminUser") + "@" + ConfigProperties.getStringProperty("testdomain");
		final String replyto = "replyto13016959916873@" + ConfigProperties.getStringProperty("testdomain");;
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email00/mime_wReplyTo.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(mail, "Verify message is received");
		ZAssert.assertEquals(from, mail.dFromRecipient.dEmailAddress, "Verify the from matches");
		ZAssert.assertEquals(replyto, mail.dReplyToRecipient.dEmailAddress, "Verify the Reply-To matches");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message present in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + subject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");

			// Verify the To, From, Subject, Body
			String actualReplyto = window.zGetMailProperty(Field.ReplyTo);
			ZAssert.assertEquals(actualReplyto, replyto, "Verify the Reply-To matches the 'Reply-To:' header");

			String actualFrom = window.zGetMailProperty(Field.From);
			ZAssert.assertEquals(actualFrom, from, "Verify the From matches the 'From:' header");

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}
	}


	@Bugs (ids = "61575")
	@Test (description = "Receive a mail with Resent-From: specified",
			groups = { "functional" })

	public void ViewMail_03() throws HarnessException {

		final String subject = "subject13147509564213";
		final String from = ConfigProperties.getStringProperty("adminUser") + "@" + ConfigProperties.getStringProperty("testdomain");
		final String resentfrom = "resentfrom13016943216873@" + ConfigProperties.getStringProperty("testdomain");;
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email00/mime_wResentFrom.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(mail, "Verify message is received");
		ZAssert.assertEquals(resentfrom, mail.dRedirectedFromRecipient.dEmailAddress, "Verify the Resent-From matches");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message present in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + subject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");

			// Verify the To, From, Subject, Body
			String actualResentFrom = window.zGetMailProperty(Field.ResentFrom);
			ZAssert.assertEquals(actualResentFrom, resentfrom, "Verify the Resent-From matches the 'Resent-From:' header");

			String actualFrom = window.zGetMailProperty(Field.From);
			ZAssert.assertEquals(actualFrom, from, "Verify the From matches the 'From:' header");

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}
	}


	@Test (description = "Verify multipart/alternative with text and html parts",
			groups = { "sanity" })

	public void ViewMail_04() throws HarnessException {

		final String subject = "bug72233";
		final String htmlcontent = "html1328844621404";
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug72233/mime.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message present in current view");

		// Select the message
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + subject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");

			String body = window.zGetMailProperty(Field.Body);

			// Verify the Content shows correctly
			ZAssert.assertStringContains(body, htmlcontent, "Verify the html content");

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}
	}
}