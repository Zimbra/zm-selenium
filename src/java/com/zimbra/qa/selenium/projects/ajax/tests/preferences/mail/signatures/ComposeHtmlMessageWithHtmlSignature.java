/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.signatures;

import java.util.HashMap;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem;
import com.zimbra.qa.selenium.framework.items.SignatureItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.XmlStringUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;

public class ComposeHtmlMessageWithHtmlSignature extends AjaxCore {
	String sigName = "signame" + ConfigProperties.getUniqueString();
	String sigBody = "signature<b>bold" + ConfigProperties.getUniqueString() + "</b>signature";
	String contentHTML = XmlStringUtil.escapeXml("<html>" + "<head></head>" + "<body>" + sigBody + "</body>" + "</html>");

	public ComposeHtmlMessageWithHtmlSignature() {
		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 1L; {
				put("zimbraPrefComposeFormat", "html");
			}
		};
	}

	@BeforeMethod(groups = { "always" })
	public void CreateSignature() throws HarnessException {
		System.out.println(this.sigName);
		ZimbraAccount.AccountZCS().authenticate();
		ZimbraAccount.AccountZCS()
				.soapSend("<CreateSignatureRequest xmlns='urn:zimbraAccount'>" + "<signature name='" + this.sigName
						+ "' >" + "<content type='text/html'>'" + this.contentHTML + "'</content>" + "</signature>"
						+ "</CreateSignatureRequest>");

		// Refresh UI
		app.zPageMain.zRefreshMainUI();

		logger.info("CreateSignature: finish");
	}

	/**
	 * Test case : Create html signature through soap Compose html message and
	 * add html signature Send mail to self and verify signature through soap.
	 */

	@Test (description = "Compose Html message with html signature and Verify signature through soap",
			groups = { "functional", "L2" })

	public void ComposeHtmlMessageWithHtmlSignature_01() throws HarnessException {

		// Signature is created
		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), this.sigName);
		ZAssert.assertEquals(signature.getName(), this.sigName, "Verify text signature is created");

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountZCS()));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyHtml = "bodybold" + ConfigProperties.getUniqueString() + "body";

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFill(mail);

		// Click Signature drop down and add signature
		app.zPageMail.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_ADD_SIGNATURE, this.sigName);

		// Send the message
		mailform.zSubmit();

		ZimbraAccount.AccountZCS().soapSend("<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+ "<query>in:inbox subject:(" + mail.dSubject + ")</query>" + "</SearchRequest>");

		String id = ZimbraAccount.AccountZCS().soapSelectValue("//mail:SearchResponse/mail:m", "id");

		ZimbraAccount.AccountZCS().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + id + "' html='1'/>" + "</GetMsgRequest>");
		Element getMsgResponse = ZimbraAccount.AccountZCS().soapSelectNode("//mail:GetMsgResponse", 1);
		MailItem received = MailItem.importFromSOAP(getMsgResponse);

		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress,
				"Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, ZimbraAccount.AccountZCS().EmailAddress,
				"Verify the to field is correct");
		ZAssert.assertEquals(received.dSubject, mail.dSubject, "Verify the subject field is correct");
		ZAssert.assertStringContains(received.dBodyHtml.toLowerCase(),
				mail.dBodyHtml.replace("<b>", "").replace("</b>", ""), "Verify the body content is correct");
	}
}