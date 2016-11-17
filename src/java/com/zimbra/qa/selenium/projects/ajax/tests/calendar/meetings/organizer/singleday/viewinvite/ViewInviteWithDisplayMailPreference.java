/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.singleday.viewinvite;

import java.io.File;
import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.tests.calendar.performance.ZmCalendarApp_ViewDay_Appointment1;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.Field;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.PagePreferences;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;
import java.util.Calendar;


public class ViewInviteWithDisplayMailPreference extends CalendarWorkWeekTest {

	public ViewInviteWithDisplayMailPreference() throws HarnessException {
		logger.info("New "+ ZmCalendarApp_ViewDay_Appointment1.class.getCanonicalName());
		
		super.startingPage = app.zPageMail;
		
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 3038458962443347843L;
			{
				put("zimbraPrefCalendarInitialView", "day");
				put("zimbraPrefMessageViewHtmlPreferred", "TRUE");
			}};

	}
	
	@Test( description = "View invite with display mail preference as HTML and verify body multiline HTML content ",
			groups = {"sanity" }
	)
	public void ViewHTMLInviteWithHTMLPreference_01() throws HarnessException {
		
		// Import appointment using text mime
		final String subject = "multiline HTML body";
		final String boldContent = "<div><strong>BoldString</strong></div>";
		final String italicContent = "<div><em>ItalicString</em></div>";
		final String underlineContent ="<div><span style=\"text-decoration: underline;\">Underline String</span></div>";
		final String colorFontContent ="<div><span style=\"color: rgb(255, 0, 0);\">Red Color text</span></div>";
		final String colorBackgroundContent ="<div><span style=\"background-color: rgb(0, 128, 0);\">Green Background</span></div>";
		final String fontFamilySizeContent ="<br /><div><span style=\"font-family: &quot;comic sans ms&quot;, &quot;comic sans&quot;, sans-serif; font-size: 14pt;\">Number list below :</span> </div><br />";
		final String numberedListContent ="<ol><li>point one</li><li>point two</li><li>point three</li></ol>";
		final String htmlContentForSOAP = XmlStringUtil.escapeXml("<html><body id='htmlmode'><h3>The following is a new meeting request:</h3><p><table border='0'><tr><th align=left>Subject:</th><td>multiline HTML body </td></tr><tr><th align=left>Organizer:</th><td>'foo' &lt;foo@example.com&gt; </td></tr></table><p><table border='0'><tr><th align=left>Location:</th><td>https://test.webex.com/testweb </td></tr><tr><th align=left>Time:</th><td>Monday, November 7, 2016, 1:30:00 PM - 2:00:00 PM GMT +05:30 Chennai, Kolkata, Mumbai, New Delhi </td></tr></table><p><table border='0'><tr><th align=left>Invitees:</th><td>bar@example.com </td></tr></table><div>*~*~*~*~*~*~*~*~*~*</div><br><div style='font-family: arial, helvetica, sans-serif; font-size: 12pt; color: #000000'><div><strong>BoldString</strong></div><div><em>ItalicString</em></div><div><span style='text-decoration: underline;' data-mce-style='text-decoration: underline;'>Underline String</span></div><div><span style='color: rgb(255, 0, 0);' data-mce-style='color: #ff0000;'>Red Color text</span></div><div><span style='background-color: rgb(0, 128, 0);' data-mce-style='background-color: #008000;'>Green Background</span></div><div><br data-mce-bogus='1'></div><div><span style='font-family: &quot;comic sans ms&quot;, &quot;comic sans&quot;, sans-serif; font-size: 14pt;' data-mce-style='font-family: 'comic sans ms', 'comic sans', sans-serif; font-size: 14pt;'>Number list below :</span>&nbsp;</div><div><br data-mce-bogus='1'></div><ol><li>point one</li><li>point two</li><li>point three</li></ol></div></body></html>");
		final String plainTextContentForSOAP = "BoldString\nItalicString\nUnderline String\nRed Color text\nGreen Background\n\nNumber list below :\n\n\n1. point one\n2. point two\n3. point three";
		
		// Absolute dates in UTC zone
		Calendar now = calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);
		String tz = ZTimeZone.TimeZoneEST.getID();
		
		// Send the message from AccountB to the current login user
				ZimbraAccount.AccountB().soapSend(
		                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
		                     "<m>"+
		                     	"<inv><comp method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' loc='' transp='O' allDay='0' name='"+ subject +"'>"+
		                     	"<or a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +	
		                     	"<at a='"+ app.zGetActiveAccount().EmailAddress + "' role='REQ'  ptst='NE'  rsvp='1'/>"+
		                     	"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() + "' tz='"+ tz +"'/>" +
		                     	"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
		                     "</comp></inv>" +
		                     "<e a='"+ app.zGetActiveAccount().EmailAddress +"' t='t'/>" +
		                     "<mp ct='multipart/alternative'>" +
		                     	"<mp ct='text/plain'>" +
		                     	"<content>"+ plainTextContentForSOAP +"</content>" +
		                     	"</mp>" +
		                     	"<mp ct='text/html'>" +
		                     	"<content>"+ htmlContentForSOAP +"</content>" +
		                     	"</mp>" +
		                     "</mp>" +
		                     "<su>"+ subject +"</su>" +
		                     "</m>" +
		               "</CreateAppointmentRequest>");
				
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify appointment displayed in current view");	

		// Select the message so that it shows in reading pane 
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Verify body content
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), boldContent, "Verify bold text content");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), italicContent, "Verify italic text content");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), underlineContent, "Verify underline text content");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), colorFontContent, "Verify colored body text content");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), colorBackgroundContent, "Verify color background text content");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), fontFamilySizeContent, "Verify font family and size of text content");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), numberedListContent, "Verify numbered list text content");
	}
	
	@Test( description = "View invite with display mail preference as Plain text and verify body multiline HTML content ",
			groups = {"sanity" } )
	
	public void ViewHTMLInviteWithPlainTextPreference_02() throws HarnessException {
		
		// Work around for getting correct body locator
		app.zPageMain.sRefresh();
		
		// Navigate to preference -> Mail and set display mail pref to Text and verify
		app.zPagePreferences.zNavigateTo();
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Mail);	
		
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(PagePreferences.Locators.zDisplayMailAsText), "Verify Display Mail as Text radio button is present");
				
		app.zPagePreferences.sClick(PagePreferences.Locators.zDisplayMailAsText);
		app.zPagePreferences.zWaitForBusyOverlay();
				
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);
				
		app.zGetActiveAccount().soapSend(
				"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
		+			"<pref name='zimbraPrefMessageViewHtmlPreferred'/>"
		+		"</GetPrefsRequest>");
				
		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefMessageViewHtmlPreferred']", null);
		ZAssert.assertEquals(value, "FALSE", "Verify zimbraPrefMessageViewHtmlPreferred preference changed to Text");
		
		String filename = ConfigProperties.getBaseDirectory() + "/data/public/mime/email20/CalendarHTMLBody.txt";
		String subject = "multiline HTML body";
		String multilineTextContent = "BoldString<br />ItalicString<br />Underline String<br />Red Color text<br />Green Background<br /><br />Number list below :<br /><br /><br />1. point one<br />2. point two<br />3. point three";
		
		if (!app.zPageMail.zVerifyMailExists(subject)) {
			LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(filename));
			ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");
		}
				
		// Select the message so that it shows in reading pane 
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
				
		// Verify body content
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), multilineTextContent, "Verify plain text content");
	}
	
	@Test( description = "View invite with display mail preference as HTML and verify body multiline plain text content ",
			groups = {"sanity" } )
	
	public void ViewPlainTextInviteWithHTMLPreference_03() throws HarnessException {
		
		app.zGetActiveAccount().soapSend(
				"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
		+			"<pref name='zimbraPrefMessageViewHtmlPreferred'/>"
		+		"</GetPrefsRequest>");
		
		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefMessageViewHtmlPreferred']", null);
		
		if (value.equalsIgnoreCase("FALSE")) {
			// Navigate to preference -> Mail and set display mail pref to HTML and verify
			app.zPagePreferences.zNavigateTo();
			app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Mail);	
			
			ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(PagePreferences.Locators.zDisplayMailAsHTML), "Verify Display Mail as HTML radio button is present");
			
			app.zPagePreferences.sClick(PagePreferences.Locators.zDisplayMailAsHTML);
			app.zPagePreferences.zWaitForBusyOverlay();
			
			app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);
			
			app.zGetActiveAccount().soapSend(
					"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
			+			"<pref name='zimbraPrefMessageViewHtmlPreferred'/>"
			+		"</GetPrefsRequest>");
			
			String value1 = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefMessageViewHtmlPreferred']", null);
			ZAssert.assertEquals(value1, "TRUE", "Verify zimbraPrefMessageViewHtmlPreferred preference changed to HTML");
		}
		
		String subject = "multiline plain text body";
		String multilineTextContentForSOAP = "line 1\nline two\n\n\nline 3";
		String verificationText = "line 1<br />line two<br /><br /><br />line 3";
		
		// Work around for getting correct body locator
		app.zPageMain.sRefresh();
		
		Calendar now = calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 13, 0, 0);
		String tz = ZTimeZone.TimeZoneEST.getID();
		
		// Send the message from AccountB to the current login user
		ZimbraAccount.AccountB().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     	"<inv><comp method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' loc='' transp='O' allDay='0' name='"+ subject +"'>"+
                     	"<or a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +	
                     	"<at a='"+ app.zGetActiveAccount().EmailAddress + "' role='REQ'  ptst='NE'  rsvp='1'/>"+
                     	"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() + "' tz='"+ tz +"'/>" +
                     	"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     "</comp></inv>" +
                     "<e a='"+ app.zGetActiveAccount().EmailAddress +"' t='t'/>" +
                     "<mp ct='multipart/alternative'>" + 
                     "<mp ct='text/plain'>" +
                     "<content>"+ multilineTextContentForSOAP +"</content>" +
                     "</mp>" +
                     "</mp>" +
                     "<su>"+ subject +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");
		
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify appointment displayed in current view");
		
		// Select the message so that it shows in reading pane 
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Verify body content
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), verificationText, "Verify plain text content");
		
	}
	
	@Test( description = "Verify invite with display mail as PlainText and verify body multiline plain text content ",
			groups = { "sanity" })
	public void ViewPlainTextInviteWithPlainTextPreference_04() throws HarnessException {
		
		// Work around for getting correct body locator
		app.zPageMain.sRefresh();
		
		app.zGetActiveAccount().soapSend(
				"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
		+			"<pref name='zimbraPrefMessageViewHtmlPreferred'/>"
		+		"</GetPrefsRequest>");
		
		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefMessageViewHtmlPreferred']", null);
		
		if (value.equalsIgnoreCase("TRUE")) {
			//Navigate to preference -> Mail and set display mail pref to Text and verify
			app.zPagePreferences.zNavigateTo();
			app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Mail);	
			
			String locator = "css=input[id$='_input'][value='false']";
			ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(locator), "Verify Display Mail as Text radio button is present");
			
			app.zPagePreferences.sClick(locator);
			app.zPagePreferences.zWaitForBusyOverlay();
			
			app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);
			
			app.zGetActiveAccount().soapSend(
					"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
			+			"<pref name='zimbraPrefMessageViewHtmlPreferred'/>"
			+		"</GetPrefsRequest>");
			
			String value1 = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefMessageViewHtmlPreferred']", null);
			ZAssert.assertEquals(value1, "FALSE", "Verify zimbraPrefMessageViewHtmlPreferred preference changed to Text");
		}
				
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email20/CalendarPlainTextBody.txt";
		final String subject = "multiline plain text body";
		final String multilineTextContent = "line 1<br />line two<br /><br /><br />line 3"; 
		
		if (!app.zPageMail.zVerifyMailExists(subject)) {
			LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mimeFile));
			ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");
		} 
		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Verify body content
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), multilineTextContent, "Verify plain text content");
	}
	
}