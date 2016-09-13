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
package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.date;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.Field;

public class GetMessage extends AjaxCommonTest {
	
	@SuppressWarnings("serial")
	public GetMessage() {
		logger.info("New "+ GetMessage.class.getCanonicalName());
		
		// All tests start at the login page
		super.startingPage = app.zPageMail;

		// Basic settings
		super.startingAccountPreferences = new HashMap<String, String>() {{
		    put("zimbraPrefGroupMailBy", "message");
		}};

	}
	
	@Test( description = "Receive a mail with a basic date",
			groups = { "smoke" })
	public void GetMessage_01() throws HarnessException {
		
		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String date = "12/25/2014";
		String body = "text " + date + " text";
		
		// Send the message from AccountA to the ZWC user
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ body +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);

		// Get all the messages in the inbox
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Wait for a bit so the zimlet can take affect
		SleepUtil.sleep(5000);
		
		// Get the HTML of the body
		HtmlElement bodyElement = display.zGetMailPropertyAsHtml(Field.Body);
		HtmlElement.evaluate(bodyElement, "//span//span", "id", Pattern.compile(".*_com_zimbra_date"), 1);
		HtmlElement.evaluate(bodyElement, "//span//span", null, date, 1);

	}


	@Test( description = "Receive a mail with two dates in body",
			groups = { "functional" })
	public void GetMessage_02() throws HarnessException {
		
		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String date1 = "12/25/2014";
		String date2 = "1/1/2015";
		String body = "date1: " + date1 + " date2: "+ date2;
		
		// Send the message from AccountA to the ZWC user
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ body +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);

		// Get all the messages in the inbox
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Wait for a bit so the zimlet can take affect
		SleepUtil.sleep(5000);
		
		// Get the HTML of the body
		HtmlElement bodyElement = display.zGetMailPropertyAsHtml(Field.Body);
		
		// Verify that the date zimlet has been applied
		HtmlElement.evaluate(bodyElement, "//span//span", null, date1, 1);
		HtmlElement.evaluate(bodyElement, "//span//span", null, date2, 1);

	}

	

	@Test( description = "Validate the date zimlet matches valid dates",
			groups = { "functional" })
	public void GetMessage_03() throws HarnessException {

		final String subject = "subject12912323015009";
		final String mime = ConfigProperties.getBaseDirectory() + "/data/public/mime/date01/en_us_valid_dates.txt";
		
		// Inject the example message
		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mime));

		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);

		// Get all the messages in the inbox
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Wait for a bit so the zimlet can take affect
		SleepUtil.sleep(5000);
		
		// Get the HTML of the body
		HtmlElement bodyElement = display.zGetMailPropertyAsHtml(Field.Body);
		
		// See the mime file
		List<String> dates = new ArrayList<String>();
		dates.add("1/1/2014");
		dates.add("1/31/2014");
		dates.add("10/1/2014");
		dates.add("10/31/2014");

		for (String date : dates) {
			// Verify that the phone zimlet has been applied
			HtmlElement.evaluate(bodyElement, "//span//span", null, date, 1);
		}


	}


	@Test( description = "Validate the date zimlet does not match invalid dates",
			groups = { "functional" })
	public void GetMessage_04() throws HarnessException {

		final String subject = "subject1293323025009";
		final String mime = ConfigProperties.getBaseDirectory() + "/data/public/mime/date01/en_us_invalid_dates.txt";
		
		// Inject the example message
		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mime));

		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);

		// Get all the messages in the inbox
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Wait for a bit so the zimlet can take affect
		SleepUtil.sleep(5000);
		
		// Get the HTML of the body
		HtmlElement bodyElement = display.zGetMailPropertyAsHtml(Field.Body);
		

		// No spans should be present in the mime
		HtmlElement.evaluate(bodyElement, "//span//span", null, (String)null, 0);
		
	}

	@Bugs(ids="86667")
	@Test( description = "Receive a mail with a date in subject(as Per bug this is invalid test case",
			groups = { "deprecated" })
	public void GetMessage_05() throws HarnessException {
		
		// Create the message data to be sent
		String date = "12/25/2016";
		String subject = "subject " + date;
		
		// Send the message from AccountA to the ZWC user
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

		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);

		// Get all the messages in the inbox
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Wait for a bit so the zimlet can take affect
		SleepUtil.sleep(5000);
		
		// Find the subject and the phone span
		String locator = "css=span[id$='_com_zimbra_date']";
		
		ZAssert.assertTrue(display.sIsElementPresent(locator), "Verify the date zimlet applies to the subject");
		ZAssert.assertEquals(display.sGetText(locator), date, "Verify the date zimlet highlights the date");

	}


	@Test( description = "Receive a mail with a date string, such as today, tomorrow, last night, etc.",
			groups = { "functional" })
	public void GetMessage_11() throws HarnessException {
		String newline = String.format("%n");
		

		/**
		 * 
		 
		  From Zimlet/src/zimlet/com_zimbra_date/com_zimbra_date.properties:
		 
format1.pattern = (today|tonight|this morning)
format1.rule = now

format2.pattern = (tomorrow night|tomorrow morning|tomorrow)
format2.rule = now +1 day

format3.pattern = (last night|yesterday morning|yesterday)
format3.rule = now -1 day

# e.g. next Thursday
format4.pattern = (this|next) {dayname}
format4.rule = now +1 {dayname}

# e.g. last Thursday
format5.pattern = last {dayname}
format5.rule = now -1 {dayname}

# e.g. first Wed in April
format6.pattern = {weekord} {dayname} (of|in) {monthname}
format6.rule = now date=1 +1 {monthname} week={weekord},{dayname}

# e.g. third Monday
format7.pattern = {weekord} {dayname}
format7.rule = now week={weekord},{dayname}

		 */
		List<String> values = Arrays.asList("today,tonight,this morning,tomorrow night,tomorrow morning,tomorrow,last night,yesterday morning,yesterday,this Monday,next Monday,Last Monday,first Monday in April,third Monday".split(","));
		
		// Create the message content, with one term on each line
		StringBuffer content = new StringBuffer(ConfigProperties.getUniqueString()).append(newline);
		for (String s : values) {
			content.append(s).append(newline);
		}
		String subject = "subject " + ConfigProperties.getUniqueString();

		// Send the message from AccountA to the ZWC user
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ content.toString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);

		// Get all the messages in the inbox
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Wait for a bit so the zimlet can take affect
		SleepUtil.sleep(5000);
		
		// Get the HTML of the body
		HtmlElement bodyElement = display.zGetMailPropertyAsHtml(Field.Body);
		
		for (String value : values) {

			HtmlElement.evaluate(bodyElement, "//span//span", null, value, 1);
		
		}
		

	}


}
