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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.compose;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowFormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Locators;

public class OpenComposedMessageInNewWindow extends PrefGroupMailByMessageTest {

	public OpenComposedMessageInNewWindow() {
		logger.info("New "+ CreateMailHtml.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
	}

	@Bugs( ids = "89778")
	@Test( description = "Send a mail using HTML editor - from a separate window using DETACH COMPOSE button",
			groups = { "functional", "L2" })

	public void OpenComposedMessageInNewWindow_01() throws HarnessException {

		// Create the message data to be entered while composing mail
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);	

		SeparateWindowFormMailNew window = null;
		String windowTitle = "Zimbra: Compose";

		try {

			window = (SeparateWindowFormMailNew) app.zPageMail.zToolbarPressButton(Button.B_DETACH_COMPOSE);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");
			
			window.zWaitForElementPresent(Locators.zBubbleToField);
			// Verify the data appearing in fields in New window
			ZAssert.assertStringContains(
					mailform.sGetText(Locators.zBubbleToField) + "@" + ConfigProperties.getStringProperty("testdomain"),
					ZimbraAccount.AccountA().EmailAddress, "Verify To field value");
			ZAssert.assertEquals(mailform.sGetValue(Locators.zSubjectField),subject, "Verify Subject field value");
			ZAssert.assertStringContains(mailform.zGetHtmltBodyText(),body, "Verify Body field value");

			// Enter some additional text in body of the newly opened compose window.
			mailform.sTypeKeys(Locators.zBodyFrameCss," appended text");
			// Send the message
			mailform.zSubmit();

			// Verification through SOAP
			ZimbraAccount.AccountA().soapSend(
					"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
							+			"<query>subject:("+ subject +")</query>"
							+		"</SearchRequest>");
			String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");

			ZimbraAccount.AccountA().soapSend(
					"<GetMsgRequest xmlns='urn:zimbraMail'>"
							+			"<m id='"+ id +"' html='1'/>"
							+		"</GetMsgRequest>");

			String from = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='f']", "a");
			String to = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='t']", "a");
			String subjectSoap = ZimbraAccount.AccountA().soapSelectValue("//mail:su", null);
			String html = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@ct='text/html']//mail:content", null);

			ZAssert.assertStringContains(from, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
			ZAssert.assertStringContains(to, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
			ZAssert.assertEquals(subjectSoap, subject, "Verify the subject field is correct");
			ZAssert.assertStringContains(html, body + " appended text", "Verify the html content");

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}
	}
}
