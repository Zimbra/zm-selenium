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
package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.phone;

import java.util.*;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail.Field;

public class GetMessage extends AjaxCore {

	@SuppressWarnings("serial")
	public GetMessage() {
		logger.info("New "+ GetMessage.class.getCanonicalName());

		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>() {{
		    put("zimbraPrefGroupMailBy", "message");
		}};
	}


	@Test (description = "Receive a mail with a basic US Phone number",
			groups = { "smoke", "L0" })

	public void GetMessage_01() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String phonenumber = "1-877-486-9273";
		String body = "text " + System.getProperty("line.separator") + phonenumber + System.getProperty("line.separator") + "text"+ ConfigProperties.getUniqueString() + System.getProperty("line.separator") ;

		// Send the message from AccountA to the ZCS user
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
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Get all the messages in the inbox
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Wait for a bit so the zimlet can take affect
		SleepUtil.sleep(5000);

		// Get the HTML of the body
		HtmlElement bodyElement = display.zGetMailPropertyAsHtml(Field.Body);

		// Verify that the phone zimlet has been applied
		// <a href="callto:1-877-486-9273" onclick="window.top.Com_Zimbra_Phone.unsetOnbeforeunload()">1-877-486-9273</a>
		HtmlElement.evaluate(bodyElement, "//a[@href='callto:1-877-486-9273']", null, (String)null, 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='callto:1-877-486-9273']", "onclick", "window.top.Com_Zimbra_Phone.unsetOnbeforeunload()", 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='callto:1-877-486-9273']", null, "1-877-486-9273", 1);
	}


	@Test (description = "Receive a mail with two phone numbers in body",
			groups = { "functional", "L2" })

	public void GetMessage_02() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String phonenumber1 = "1-877-486-9273";
		String phonenumber2 = "1-877-555-9273";
		String body = "phone1: " + phonenumber1 + " phone2: "+ phonenumber2;

		// Send the message from AccountA to the ZCS user
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
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Get all the messages in the inbox
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Wait for a bit so the zimlet can take affect
		SleepUtil.sleep(5000);

		// Get the HTML of the body
		HtmlElement bodyElement = display.zGetMailPropertyAsHtml(Field.Body);

		// Verify that the phone zimlet has been applied
		// <a href="callto:1-877-486-9273" onclick="window.top.Com_Zimbra_Phone.unsetOnbeforeunload()">1-877-486-9273</a>
		HtmlElement.evaluate(bodyElement, "//a[@href='callto:"+ phonenumber1 +"']", null, (String)null, 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='callto:"+ phonenumber1 +"']", "onclick", "window.top.Com_Zimbra_Phone.unsetOnbeforeunload()", 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='callto:"+ phonenumber1 +"']", null, phonenumber1, 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='callto:"+ phonenumber2 +"']", null, (String)null, 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='callto:"+ phonenumber2+"']", "onclick", "window.top.Com_Zimbra_Phone.unsetOnbeforeunload()", 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='callto:"+ phonenumber2 +"']", null, phonenumber2, 1);
	}


	@Test (description = "Validate the phone zimlet matches NANP phone numbers",
			groups = { "functional", "L3" })

	public void GetMessage_03() throws HarnessException {

		final String subject = "subject12977323015009";
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email01/en_us_valid_phone.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Get all the messages in the inbox
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Wait for a bit so the zimlet can take affect
		SleepUtil.sleep(5000);

		// Get the HTML of the body
		HtmlElement bodyElement = display.zGetMailPropertyAsHtml(Field.Body);

		// A map of xpath to phone number
		Map<String, String> phonenumbers = new HashMap<String, String>() {
			private static final long serialVersionUID = -3501450894693465698L;
		{
			put("//a[@href='callto:1-877-486-9273']", 	"1-877-486-9273");
			put("//a[@href='callto:877-486-9273']", 		"877-486-9273");
			//put("//a[@href='callto:%28877%29%20456-7890']", "(877) 456-7890");		// http://bugzilla.zimbra.com/show_bug.cgi?id=67424
			put("//a[@href='callto:(877) 456-7890']", "(877) 456-7890");
			put("//a[@href='callto:6503501010']", 		"6503501010");
			put("//a[@href='callto:+1 650 350 1010']", "+1 650 350 1010");		// http://bugzilla.zimbra.com/show_bug.cgi?id=52337#c15
			put("//a[@href='callto:650.350.1010']", 		"650.350.1010");
			put("//a[@href='callto:(650) 350 1010']", "(650) 350 1010");		// http://bugzilla.zimbra.com/show_bug.cgi?id=67424
			put("//a[@href='callto:+1 (650) 350 1010']",	"+1 (650) 350 1010");	// http://bugzilla.zimbra.com/show_bug.cgi?id=52337#c15
			put("//a[@href='callto:1-650-350-1010']", 	"1-650-350-1010");
		}};

		for (Map.Entry<String, String> entry : phonenumbers.entrySet()) {
			String xpath = entry.getKey();
			String value = entry.getValue();

			// Verify that the phone zimlet has been applied
			// <a href="callto:1-877-486-9273" onclick="window.top.Com_Zimbra_Phone.unsetOnbeforeunload()">1-877-486-9273</a>

			HtmlElement.evaluate(bodyElement, xpath, null, (String)null, 1);
			HtmlElement.evaluate(bodyElement, xpath, "onclick", "window.top.Com_Zimbra_Phone.unsetOnbeforeunload()", 1);
			HtmlElement.evaluate(bodyElement, xpath, null, value, 1);
		}
	}


	@Test (description = "Validate the phone zimlet matches NANP phone numbers",
			groups = { "functional", "L2" })

	public void GetMessage_04() throws HarnessException {

		final String subject = "subject12977323025009";
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email01/en_us_invalid_phone.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Get all the messages in the inbox
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Wait for a bit so the zimlet can take affect
		SleepUtil.sleep(5000);

		// Get the HTML of the body
		HtmlElement bodyElement = display.zGetMailPropertyAsHtml(Field.Body);

		// A map of xpath to phone number
		// See the mime
		Map<String, String> phonenumbers = new HashMap<String, String>() {
			private static final long serialVersionUID = -3501450894693465698L;
		{
			put("//a[@href='callto:486-9273']", "486-9273");
			put("//a[@href='callto:00005818']", "00005818");
			put("//a[@href='callto:0005818']", "0005818");
			put("//a[@href='callto:123-1234']", "123-12340");
			put("//a[@href='callto:1234-1234']",	"1234-1234");
			put("//a[@href='callto:1-555-1212']",	"1-555-1212");
		}};

		// Verify that the phone zimlet is NOT applied
		// <a href="callto:1-877-486-9273" onclick="window.top.Com_Zimbra_Phone.unsetOnbeforeunload()">1-877-486-9273</a>
		for (Map.Entry<String, String> entry : phonenumbers.entrySet()) {
			String xpath = entry.getKey();

			// Verify that the phone zimlet has been applied
			// <a href="callto:1-877-486-9273" onclick="window.top.Com_Zimbra_Phone.unsetOnbeforeunload()">1-877-486-9273</a>
			HtmlElement.evaluate(bodyElement, xpath, null, (String)null, 0);
		}
	}


	@Test (description = "Receive a mail with a phone numbers in subject( reference bug 86667)",
			groups = { "deprecated" })

	public void GetMessage_05() throws HarnessException {

		// Create the message data to be sent
		String phonenumber = "1-877-486-9273";
		String subject = "subject " + phonenumber;

		// Send the message from AccountA to the ZCS user
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
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Get all the messages in the inbox
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Wait for a bit so the zimlet can take affect
		SleepUtil.sleep(5000);

		// Find the subject and the phone span
		String locator = "css=span[id$='_com_zimbra_phone']";

		ZAssert.assertTrue(display.sIsElementPresent(locator), "Verify the phone zimlet applies to the subject");
		ZAssert.assertEquals(display.sGetText(locator), phonenumber, "Verify the phone zimlet highlights the phone number");
	}


	@Bugs (ids = "73264")
	@Test (description = "Receive a mail with an unformated number string - should not match",
			groups = { "functional", "L3" })

	public void GetMessage_06() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String phonenumber = "16504755345";
		String body = "text " + System.getProperty("line.separator") + phonenumber + System.getProperty("line.separator") + "text"+ ConfigProperties.getUniqueString() + System.getProperty("line.separator") ;

		// Send the message from AccountA to the ZCS user
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
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Get all the messages in the inbox
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Wait for a bit so the zimlet can take affect
		SleepUtil.sleep(5000);

		// Get the HTML of the body
		HtmlElement bodyElement = display.zGetMailPropertyAsHtml(Field.Body);

		// Verify that the phone zimlet has been applied
		// <a href="callto:1-877-486-9273" onclick="window.top.Com_Zimbra_Phone.unsetOnbeforeunload()">1-877-486-9273</a>
		HtmlElement.evaluate(bodyElement, "//a[@href='callto:16504755345']", null, (String)null, 0);
	}
}