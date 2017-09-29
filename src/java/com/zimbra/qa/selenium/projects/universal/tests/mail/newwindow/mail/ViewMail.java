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
package com.zimbra.qa.selenium.projects.universal.tests.mail.newwindow.mail;

import java.io.File;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.LmtpInject;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.SeparateWindowDisplayMail;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DisplayMail.Field;

public class ViewMail extends PrefGroupMailByMessageTest {

	final String mimeFolder = ConfigProperties.getBaseDirectory() + "/data/public/mime/email00";
	
	public ViewMail() throws HarnessException {
		logger.info("New "+ ViewMail.class.getCanonicalName());
	}
	
	@Test( description = "Receive a mail with Sender: specified",
			groups = { "functional", "L2" })

	public void ViewMail_01() throws HarnessException {
		
		final String subject = "subject12996131112962";
		final String from = "from12996131112962@example.com";
		final String sender = "sender12996131112962@example.com";

		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mimeFolder + "/mime_wSender.txt"));

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(mail, "Verify message is received");
		ZAssert.assertEquals(from, mail.dFromRecipient.dEmailAddress, "Verify the from matches");
		ZAssert.assertEquals(sender, mail.dSenderRecipient.dEmailAddress, "Verify the sender matches");
		
		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + subject;
		
		try {
			
			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);
			
			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();
			
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");
			
			// Verify the To, From, Subject, Body
			String actualOBO = window.zGetMailProperty(Field.OnBehalfOf);
			ZAssert.assertEquals(actualOBO, from, "Verify the On-Behalf-Of matches the 'From:' header");
			
			String actualSender = window.zGetMailProperty(Field.From);
			ZAssert.assertEquals(actualSender, sender, "Verify the From matches the 'Sender:' header");

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);			
		}
	}

	
	@Bugs( ids = "86168")
	@Test( description = "Receive a mail with Reply-To: specified",
			groups = { "functional", "L2" })

	public void ViewMail_02() throws HarnessException {
		
		final String subject = "subject13016959916873";
		final String from = "from13016959916873@example.com";
		final String replyto = "replyto13016959916873@example.com";

		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mimeFolder + "/mime_wReplyTo.txt"));
		
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(mail, "Verify message is received");
		ZAssert.assertEquals(from, mail.dFromRecipient.dEmailAddress, "Verify the from matches");
		ZAssert.assertEquals(replyto, mail.dReplyToRecipient.dEmailAddress, "Verify the Reply-To matches");
		
		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");
		
		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + subject;
		
		try {
			
			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);
			
			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();
			
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");
			
			// Verify the To, From, Subject, Body
			String actualReplyto = window.zGetMailProperty(Field.ReplyTo);
			ZAssert.assertEquals(actualReplyto, replyto, "Verify the Reply-To matches the 'Reply-To:' header");

			String actualFrom = window.zGetMailProperty(Field.From);
			ZAssert.assertEquals(actualFrom, from, "Verify the From matches the 'From:' header");

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}
	}


	@Bugs( ids = "61575")
	@Test( description = "Receive a mail with Resent-From: specified",
			groups = { "functional", "L3" })

	public void ViewMail_03() throws HarnessException {
		
		final String subject = "subject13147509564213";
		final String from = "from13011239916873@example.com";
		final String resentfrom = "resentfrom13016943216873@example.com";

		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mimeFolder + "/mime_wResentFrom.txt"));

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(mail, "Verify message is received");
		ZAssert.assertEquals(resentfrom, mail.dRedirectedFromRecipient.dEmailAddress, "Verify the Resent-From matches");
		
		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + subject;
		
		try {
			
			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);
			
			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();
			
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");
			
			// Verify the To, From, Subject, Body
			String actualResentFrom = window.zGetMailProperty(Field.ResentFrom);
			ZAssert.assertEquals(actualResentFrom, resentfrom, "Verify the Resent-From matches the 'Resent-From:' header");

			String actualFrom = window.zGetMailProperty(Field.From);
			ZAssert.assertEquals(actualFrom, from, "Verify the From matches the 'From:' header");

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);			
		}

	}


	@Test( description = "Verify multipart/alternative with text and html parts",
			groups = { "functional", "L2" })
	public void ViewMail_04() throws HarnessException {
		
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug72233";
		final String subject = "bug72233";
		final String htmlcontent = "html1328844621404";
		
		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mimeFile));
		
		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message 
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + subject;
		
		try {
			
			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);
			
			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();
			
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");
			
			String body = window.zGetMailProperty(Field.Body);

			// Verify the Content shows correctly
			ZAssert.assertStringContains(body, htmlcontent, "Verify the html content");

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}

	}

}