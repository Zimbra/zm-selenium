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
	groups = { "functional" })

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
			window.zWaitForActive();
			window.waitForComposeWindow();			
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");

			//Select the window
			window.sSelectWindow(windowTitle);					

			// Verify the data appearing in fields in New window
			ZAssert.assertEquals(mailform.sGetText(Locators.zBubbleToField),ZimbraAccount.AccountA().EmailAddress, "To field doesn't match");
			ZAssert.assertEquals(mailform.sGetValue(Locators.zSubjectField),subject, "Subject field doesn't match");
			ZAssert.assertStringContains(mailform.zGetHtmltBodyText(),body, "Body of mail doesn't match");

			//Enter some additional text in body of the newly opened compose window.
			mailform.zFillField(Field.Body, "New Text ");

			// Send the message
			mailform.zToolbarPressButton(Button.B_SEND);

			//Verification through SOAP
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

			ZAssert.assertEquals(from, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
			ZAssert.assertEquals(to, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
			ZAssert.assertEquals(subjectSoap, subject, "Verify the subject field is correct");
			ZAssert.assertStringContains(html, "New Text "+body, "Verify the html content");

		} finally {
			app.zPageMain.closeWindow(window, windowTitle, app);
		}
	}
}
