/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.singleday.create;

import java.awt.event.KeyEvent;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew.Field;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew.Locators;

public class CreateMeetingWithMultilineBody extends AjaxCore {

	public CreateMeetingWithMultilineBody() {
		logger.info("New "+ CreateMeetingWithMultilineBody.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Test (description = "Send a meeting invite by entering multiline body content with HTML compose preferences",
			groups = { "smoke" })

	public void CreateMeetingWithMultilineHtmlBody_01() throws HarnessException {

		// Appointment data
		String attendees = ZimbraAccount.AccountC().EmailAddress;
		String dSubject = "subject" + ConfigProperties.getUniqueString();
		String dBodyHtmlBold = "<strong>Bold</strong>";
		String dBodyHtmlItalic = "</div><div><em>Italic</em>";
		String dBodyHtmlRedColor = "255";
		String dBodyHtmlRedColorText = "Red</span><span id=\"_mce_caret\"></span>";
		String dBodyHtmlGreenBackgroundColor = "128";
		String dBodyHtmlGreenBackgroundText = "GreenBC</span><span id=\"_mce_caret\"></span>";
		// Open the new appointment form
		FormApptNew apptForm = (FormApptNew) app.zPageMail.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_APPOINTMENT);
		ZAssert.assertNotNull(apptForm, "Verify the new form opened");

		// Fill out the form with the data
		apptForm.zFillField(Field.Subject, dSubject);
		apptForm.zFillField(Field.Attendees, attendees);

		// Scroll down in view so that font and background color selection fully visible on screen
		apptForm.sGetEval("document.getElementsByClassName('mce-ico mce-i-bold')[0].scrollIntoView(true)") ;

		// Enter multiline body HTML text
		apptForm.sClick(Locators.zBoldButton);
		apptForm.zKeyboard.zTypeCharacters("Bold");
		apptForm.sClick(Locators.zBoldButton);
		apptForm.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		apptForm.sClick(Locators.zItalicButton);
		apptForm.zKeyboard.zTypeCharacters("Italic");
		apptForm.sClick(Locators.zItalicButton);
		apptForm.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		apptForm.sClick(Locators.zTextColorDropdown);
		apptForm.sClick(Locators.zTextColorRed);
		apptForm.zKeyboard.zTypeCharacters("Red");
		apptForm.sClick(Locators.zTextColorDropdown);
		apptForm.sClick(Locators.zTextColorTransparent);
		apptForm.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		apptForm.sClick(Locators.zTextBackgroundColorDropdown);
		apptForm.sClick(Locators.zTextBackgroundColorGreen);
		apptForm.zKeyboard.zTypeCharacters("GreenBC");
		apptForm.sClick(Locators.zTextBackgroundColorDropdown);
		apptForm.sClick(Locators.zTextBackgroundColorTransparent);
		apptForm.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);

		// Send invite
		apptForm.zSubmit();

		ZimbraAccount.AccountC().soapSend(
					"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
			+			"<query>subject:("+ dSubject +")</query>"
			+		"</SearchRequest>");
		String id = ZimbraAccount.AccountC().soapSelectValue("//mail:m", "id");

		ZimbraAccount.AccountC().soapSend(
					"<GetMsgRequest xmlns='urn:zimbraMail'>"
			+			"<m id='"+ id +"' html='1'/>"
			+		"</GetMsgRequest>");

		String from = ZimbraAccount.AccountC().soapSelectValue("//mail:e[@t='f']", "a");
		String to = ZimbraAccount.AccountC().soapSelectValue("//mail:e[@t='t']", "a");
		String subject = ZimbraAccount.AccountC().soapSelectValue("//mail:su", null);
		String html = ZimbraAccount.AccountC().soapSelectValue("//mail:mp[@ct='text/html']//mail:content", null).replace("\uFEFF", "");;

		// Verification
		ZAssert.assertEquals(from, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(to, ZimbraAccount.AccountC().EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(subject, dSubject, "Verify the subject field is correct");
		ZAssert.assertStringContains(html, dBodyHtmlBold, "Verify bold html content");
		ZAssert.assertStringContains(html, dBodyHtmlItalic, "Verify italic html content");
		ZAssert.assertStringContains(html, dBodyHtmlRedColor, "Verify red color rgb in html content");
		ZAssert.assertStringContains(html, dBodyHtmlRedColorText, "Verify red color text html content");
		ZAssert.assertStringContains(html, dBodyHtmlGreenBackgroundColor, "Verify green color rgb html content");
		ZAssert.assertStringContains(html, dBodyHtmlGreenBackgroundText, "Verify green color text html content");
	}


	@Test (description = "Send a meeting invite by entering multiline body content with Plain text compose preferences",
			groups = { "bhr" })

	public void CreateMeetingWithMultilinePlainTextBody_02() throws HarnessException {

		// Appointment data
		final String attendees = ZimbraAccount.AccountC().EmailAddress;
		final String dSubject = "subject" + ConfigProperties.getUniqueString();
		final String dPlainTextBody = "Plain text line 1" + '\n' + "Plain text line two" + '\n' + "Plain text line 3";

		// Open the new appointment form
		FormApptNew apptForm = (FormApptNew) app.zPageMail.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_APPOINTMENT);
		ZAssert.assertNotNull(apptForm, "Verify the new form opened");

		// Change compose preference
		DialogWarning dialog = (DialogWarning)apptForm.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_FORMAT_AS_PLAIN_TEXT);
		dialog.zPressButton(Button.B_OK);

		// Fill out the form with the data
		apptForm.zFillField(Field.Subject, dSubject);
		apptForm.zFillField(Field.Attendees, attendees);

		// Enter multiline body plain text
		apptForm.sClick(Locators.zPlainTextBodyField);
		apptForm.zKeyboard.zTypeCharacters("Plain text line 1");
		apptForm.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		apptForm.zKeyboard.zTypeCharacters("Plain text line two");
		apptForm.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		apptForm.zKeyboard.zTypeCharacters("Plain text line 3");

		// Send invite
		apptForm.zSubmit();

		ZimbraAccount.AccountC().soapSend(
					"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
			+			"<query>subject:("+ dSubject +")</query>"
			+		"</SearchRequest>");
		String id = ZimbraAccount.AccountC().soapSelectValue("//mail:m", "id");

		ZimbraAccount.AccountC().soapSend(
					"<GetMsgRequest xmlns='urn:zimbraMail'>"
			+			"<m id='"+ id +"' html='1'/>"
			+		"</GetMsgRequest>");

		String from = ZimbraAccount.AccountC().soapSelectValue("//mail:e[@t='f']", "a");
		String to = ZimbraAccount.AccountC().soapSelectValue("//mail:e[@t='t']", "a");
		String subject = ZimbraAccount.AccountC().soapSelectValue("//mail:su", null);
		String bodyText = ZimbraAccount.AccountC().soapSelectValue("//mail:mp[@ct='text/plain']//mail:content", null);

		// Verification
		ZAssert.assertEquals(from, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(to, ZimbraAccount.AccountC().EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(subject, dSubject, "Verify the subject field is correct");
		ZAssert.assertStringContains(bodyText, dPlainTextBody, "Verify the subject field is correct");
	}
}