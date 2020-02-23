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
package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.url;

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


	@Test (description = "Receive a mail with a basic URL",
			groups = { "bhr" })

	public void GetMessage_01() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String url = "http://www.zimbra.com";
		String body = "text " + System.getProperty("line.separator") + url + System.getProperty("line.separator") + "text"+ ConfigProperties.getUniqueString() + System.getProperty("line.separator") ;

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
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message present in current view");

		// Get all the messages in the inbox
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Wait for a bit so the zimlet can take affect
		SleepUtil.sleep(5000);

		// Get the HTML of the body
		HtmlElement bodyElement = display.zGetMailPropertyAsHtml(Field.Body);

		// Verify that the phone zimlet has been applied
		// <a href="http://www.zimbra.com" target="_blank">http://www.zimbra.com</a>
		HtmlElement.evaluate(bodyElement, "//a[@href='"+ url +"']", null, (String)null, 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='"+ url +"']", "target", "_blank", 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='"+ url +"']", null, url, 1);
	}


	@Test (description = "Receive a mail with two URLs in body",
			groups = { "sanity" })

	public void GetMessage_02() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String url1 = "http://www.zimbra.com";
		String url2 = "http://www.google.com";
		String body = "url1: " + url1 + " url2: "+ url2;

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
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message present in current view");

		// Get all the messages in the inbox
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Wait for a bit so the zimlet can take affect
		SleepUtil.sleep(5000);

		// Get the HTML of the body
		HtmlElement bodyElement = display.zGetMailPropertyAsHtml(Field.Body);

		// Verify that the phone zimlet has been applied
		// <a href="callto:1-877-486-9273" onclick="window.top.Com_Zimbra_Phone.unsetOnbeforeunload()">1-877-486-9273</a>
		HtmlElement.evaluate(bodyElement, "//a[@href='"+ url1 +"']", null, (String)null, 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='"+ url1 +"']", "target", "_blank", 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='"+ url1 +"']", null, url1, 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='"+ url2 +"']", null, (String)null, 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='"+ url2 +"']", "target", "_blank", 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='"+ url2 +"']", null, url2, 1);
	}


	@Test (description = "Validate the url zimlet matches valid URLs",
			groups = { "sanity" })

	public void GetMessage_03() throws HarnessException {

		final String subject = "subject12955323015009";
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/url01/valid_url.txt";
		final String url1 = "http://www.zimbra.com";
		final String url2 = "https://www.zimbra.com";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message present in current view");

		// Get all the messages in the inbox
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Wait for a bit so the zimlet can take affect
		SleepUtil.sleep(5000);

		// Get the HTML of the body
		HtmlElement bodyElement = display.zGetMailPropertyAsHtml(Field.Body);

		// Verify that the phone zimlet has been applied
		// <a href="callto:1-877-486-9273" onclick="window.top.Com_Zimbra_Phone.unsetOnbeforeunload()">1-877-486-9273</a>
		HtmlElement.evaluate(bodyElement, "//a[@href='"+ url1 +"']", null, (String)null, 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='"+ url1 +"']", "target", "_blank", 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='"+ url1 +"']", null, url1, 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='"+ url2 +"']", null, (String)null, 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='"+ url2 +"']", "target", "_blank", 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='"+ url2 +"']", null, url2, 1);
	}


	@Test (description = "Validate the url zimlet does not match invalid URLs",
			groups = { "functional" })

	public void GetMessage_04() throws HarnessException {

		final String subject = "subject12976223025009";
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/url01/invalid_url.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message present in current view");

		// Get all the messages in the inbox
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Wait for a bit so the zimlet can take affect
		SleepUtil.sleep(5000);

		// Get the HTML of the body
		HtmlElement bodyElement = display.zGetMailPropertyAsHtml(Field.Body);

		// TODO:
		// Not sure which URL's to add to the mime
		// Need to mine bugs for URLs that give issues
		// Add URLS to the sample mime and add verification points here.
		logger.info(bodyElement.prettyPrint());
	}


	@Bugs (ids = "29018,67927")
	@Test (description = "Receive a mail with a URL in angled brackets",
			groups = { "sanity" })

	public void GetMessage_05() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String url = "http://www.zimbra.com";
		String body = "text &lt;" + url + "&gt; text "+ ConfigProperties.getUniqueString();

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
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message present in current view");

		// Get all the messages in the inbox
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Wait for a bit so the zimlet can take affect
		SleepUtil.sleep(5000);

		// Get the HTML of the body
		HtmlElement bodyElement = display.zGetMailPropertyAsHtml(Field.Body);

		// Verify that the phone zimlet has been applied
		// <a href="http://www.zimbra.com" target="_blank">http://www.zimbra.com</a>
		HtmlElement.evaluate(bodyElement, "//a[@href='"+ url +"']", null, (String)null, 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='"+ url +"']", "target", "_blank", 1);
		HtmlElement.evaluate(bodyElement, "//a[@href='"+ url +"']", null, url, 1);
	}
}