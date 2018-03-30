/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.mail;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail.Field;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.PageMail.Locators;

@SuppressWarnings("unused")
public class ViewMail extends SetGroupMailByMessagePreference {

	public ViewMail() throws HarnessException {
		logger.info("New "+ ViewMail.class.getCanonicalName());
	}


	@Bugs (ids = "57047" )
	@Test (description = "Receive a mail with Sender: specified",
			groups = { "functional", "L2" })

	public void ViewMail_01() throws HarnessException {

		final String subject = "subject12996131112962";
		final String from = ConfigProperties.getStringProperty("adminUser") + "@" + ConfigProperties.getStringProperty("testdomain");
		final String sender = "sender12996131112962@example.com";
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email00/mime_wSender.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(mail, "Verify message is received");
		ZAssert.assertEquals(from, mail.dFromRecipient.dEmailAddress, "Verify the from matches");
		ZAssert.assertEquals(sender, mail.dSenderRecipient.dEmailAddress, "Verify the sender matches");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Verify the To, From, Subject, Body
		String fromLocator = "css=div[id='zv__TV-main__MSG'] td[id$='_from'] span:nth-child(3)>span[class='addrBubble']";
		ZAssert.assertEquals(app.zPageMail.sGetText(fromLocator).trim(), from, "Verify the On-Behalf-Of matches the 'From:' header");

		String senderLocator = "css=div[id='zv__TV-main__MSG'] td[id$='_from'] span[class='addrBubble']";
		ZAssert.assertEquals(app.zPageMail.sGetText(senderLocator).trim(), sender, "Verify the From matches the 'Sender:' header");
	}


	@Test (description = "Receive a mail with Reply-To: specified",
			groups = { "functional", "L2" })

	public void ViewMail_02() throws HarnessException {

		final String subject = "subject13016959916873";
		final String from = ConfigProperties.getStringProperty("adminUser") + "@" + ConfigProperties.getStringProperty("testdomain");
		final String replyto = "replyto13016959916873@example.com";
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email00/mime_wReplyTo.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(mail, "Verify message is received");
		ZAssert.assertEquals(from, mail.dFromRecipient.dEmailAddress, "Verify the from matches");
		ZAssert.assertEquals(replyto, mail.dReplyToRecipient.dEmailAddress, "Verify the Reply-To matches");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Verify the To, From, Subject, Body
		String replytoLocator = "css=div[id='zv__TV-main__MSG'] tr[id$='_reply to'] td.LabelColValue span[class='addrBubble'] ";
		ZAssert.assertEquals(app.zPageMail.sGetText(replytoLocator).trim(), replyto, "Verify the Reply-To matches the 'Reply-To:' header");

		String fromLocator = "css=div[id='zv__TV-main__MSG'] td[id$='_from'] span[class='addrBubble']";
		ZAssert.assertEquals(app.zPageMail.sGetText(fromLocator).trim(), from, "Verify the From matches the 'From:' header");
	}


	@Bugs (ids = "61575")
	@Test (description = "Receive a mail with Resent-From: specified",
			groups = { "functional", "L3" })

	public void ViewMail_03() throws HarnessException {

		final String subject = "subject13147509564213";
		final String from = ConfigProperties.getStringProperty("adminUser") + "@" + ConfigProperties.getStringProperty("testdomain");
		final String resentfrom = "resentfrom13016943216873@example.com";
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email00/mime_wResentFrom.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(mail, "Verify message is received");
		ZAssert.assertEquals(resentfrom, mail.dRedirectedFromRecipient.dEmailAddress, "Verify the Resent-From matches");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Verify the To, From, Subject, Body
		String resentfromLocator = "css=div[id='zv__TV-main__MSG'] td[id$='_from'] span:nth-child(3)";
		ZAssert.assertEquals(app.zPageMail.sGetText(resentfromLocator).trim(), resentfrom, "Verify the From matches the 'From:' header");

		String fromLocator = "css=div[id='zv__TV-main__MSG'] td[id$='_from'] span[class='addrBubble']";
		ZAssert.assertEquals(app.zPageMail.sGetText(fromLocator).trim(), from, "Verify the From matches the 'From:' header");
	}


	@Bugs (ids = "102049")	
	@Test (description = "Receive a mail with only audio/wav content",
			groups = { "functional", "L2" })

	public void ViewMail_04() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug64444/bug64444.txt";
		final String subject = "subject13150123168433";
		final String from = ConfigProperties.getStringProperty("adminUser") + "@" + ConfigProperties.getStringProperty("testdomain");
		final String to = ConfigProperties.getStringProperty("adminUser") + "@" + ConfigProperties.getStringProperty("testdomain");

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:(" + subject +")");
		ZAssert.assertNotNull(mail, "Verify message is received");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Verify the To, From, Subject, Body
		ZAssert.assertEquals(actual.zGetMailProperty(Field.From), from, "Verify the From matches the 'From:' header");
		ZAssert.assertEquals(actual.zGetMailProperty(Field.To), to, "Verify the From matches the 'From:' header");
	}


	@Bugs (ids = "66565")
	@Test (description = "Receive a mail formatting in the subject",
			groups = { "functional", "L2" })

	public void ViewMail_05() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug66565/mime01.txt";
		final String subject = "subject13197565510464";
		final String subjectText = "<u><i> subject13197565510464 </i></u>";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:(" + subject +")");
		ZAssert.assertNotNull(mail, "Verify message is received");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Verify the Subject
		ZAssert.assertEquals(actual.zGetMailProperty(Field.Subject), subjectText, "Verify the Subject matches");

		String locator = "css=div[id='zv__TV-main__MSG'] tr[id$='_hdrTableTopRow'] td[class~='SubjectCol']";
		ZAssert.assertFalse( actual.sIsElementPresent(locator + " u"), "Verify the <u> element is not in the DOM");
		ZAssert.assertFalse( actual.sIsElementPresent(locator + " i"), "Verify the <i> element is not in the DOM");
	}


	@Bugs (ids = "65933,65623")
	@Test (description = "Verify message with only HTML part",
			groups = { "functional", "L2" })

	public void ViewMail_06() throws HarnessException {

		String subject = "subject13188948451403";
		String content = "Welcome to the NetWorker Listserv list";
		String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug65933/Bug65933.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh the inbox
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Verify the To, From, Subject, Body
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), content, "Verify the body displays correctly");
	}


	@Bugs (ids = "65933,65623")
	@Test (description = "Verify message with only HTML part and charset",
			groups = { "functional", "L2" })

	public void ViewMail_07() throws HarnessException {

		String subject = "subject13189485723753";
		String content = "Enrico Medici";
		String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug65623/Bug65623.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh the inbox
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Verify the To, From, Subject, Body
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), content, "Verify the body displays correctly");
	}


	@Bugs (ids = "65079")
	@Test (description = "Verify message with only HTML part and charset",
			groups = { "functional", "L2" })

	public void ViewMail_08() throws HarnessException {

		String subject = "subject13189993282183";
		String content = "Incident Title";
		String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug65079/Bug65079.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh the inbox
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Verify the To, From, Subject, Body
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), content, "Verify the body displays correctly");
	}


	@Test (description = "zimbraPrefMessageViewHtmlPreferred=TRUE: Receive message with text only parts - should be rendered as text",
			groups = { "functional", "L2" })

	public void ViewMail_09() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email04/mimeTextOnly.txt";
		final String subject = "subject13214016725788";
		final String content = "The Ming Dynasty";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Verify the To, From, Subject, Body
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), content, "Verify the text content");
	}


	@Test (description = "zimbraPrefMessageViewHtmlPreferred=TRUE: Receive message with html only parts - should be rendered as html",
			groups = { "functional", "L2" })

	public void ViewMail_10() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email04/mimeHtmlOnly.txt";
		final String subject = "subject13214016672655";
		final String content = "Bold";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Verify the To, From, Subject, Body
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), content, "Verify the text content");
	}


	@Test (description = "zimbraPrefMessageViewHtmlPreferred=TRUE: Receive message with text and html  parts - should be rendered as html",
			groups = { "functional", "L2" })

	public void ViewMail_11() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email04/mimeTextAndHtml.txt";
		final String subject = "subject13214016621403";
		final String content = "Bold";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Verify the To, From, Subject, Body
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), content, "Verify the text content");
	}


	@Bugs (ids = "67854")
	@Test (description = "Verify empty message shows 'no content'",
			groups = { "functional", "L2" })

	public void ViewMail_12() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug67854/mime01.txt";
		final String subject = "subject13218526621403";
		final String content = "The message has no text content.";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Get the content
		String body = actual.zGetMailProperty(Field.Body);

		// Verify the Content shows "no content"
		ZAssert.assertStringContains(body, content, "Verify the text content");

		// Bug 67854: Verify the Content does not show HTML
		ZAssert.assertStringDoesNotContain(body, "&lt;table", "Verify the content does not contain HTML");
	}


	@Bugs (ids = "72248")
	@Test (description = "Verify multipart/alternative with only 1 part",
			groups = { "functional", "L2" })

	public void ViewMail_13() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug72248/mime.txt";
		final String subject = "subject13217218621403";
		final String content = "content1328844621403";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Get the content
		String body = actual.zGetMailProperty(Field.Body);

		// Verify the Content shows "no content"
		ZAssert.assertStringContains(body, content, "Verify the text content");
	}


	@Test (description = "Verify multipart/alternative with text and html parts",
			groups = { "functional", "L2" })

	public void ViewMail_14() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug72233/mime.txt";
		final String subject = "bug72233";
		final String htmlcontent = "html1328844621404";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Get the content
		String body = actual.zGetMailProperty(Field.Body);

		// Verify the Content shows correctly
		ZAssert.assertStringContains(body, htmlcontent, "Verify the html content");
	}


	@Test (description = "View a message with external images",
			groups = { "functional", "L2" })

	public void ViewMail_15() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/externalImage01/externalimage01.txt";
		final String subject = "externalimage01";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Verify Warning info bar with other links
		ZAssert.assertTrue(app.zPageMail.zHasWDDLinks(), "Verify display images link");
	}


	@Bugs (ids = "96820")
	@Test (description = "View very large message using 'View entire message' link",
			groups = { "functional", "L3" } )

	public void ViewMail_16() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug96820/Bug96820_VeryLargeMessage.txt";
		final String subject = "Very large message";
		final String testString = "Entire message is displayed";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(Locators.zViewEntireMessageLink), "Verify Display entire message link is displayed");
		app.zPageMail.sClick(Locators.zViewEntireMessageLink,(WebElement[]) null);
		SleepUtil.sleepVeryLong();

		// Verify that  View entire message link is not present now
		ZAssert.assertFalse(app.zPageMail.sIsElementPresent(Locators.zViewEntireMessageLink), "Verify Display entire message link is still present");

		// Verify that Entire content is displayed
		SleepUtil.sleepVeryLong();
		ZAssert.assertTrue(actual.zGetMailProperty(Field.Body).contains(testString), "Verify that entire message is displayed");
	}
}