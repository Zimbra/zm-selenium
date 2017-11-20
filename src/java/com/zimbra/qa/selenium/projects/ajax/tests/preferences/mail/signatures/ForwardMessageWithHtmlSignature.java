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
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.SignatureItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.XmlStringUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;

public class ForwardMessageWithHtmlSignature extends AjaxCore {
	String sigName = "signame" + ConfigProperties.getUniqueString();
	String sigBody = "signature<b>bold" + ConfigProperties.getUniqueString() + "</b>signature";
	String contentHTMLSig = XmlStringUtil.escapeXml("<html>" + "<head></head>" + "<body>" + sigBody + "</body>" + "</html>");

	@SuppressWarnings("serial")
	public ForwardMessageWithHtmlSignature() {
		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>() {{
			put("zimbraPrefGroupMailBy", "message");
		}};
	}

	@BeforeMethod(groups = { "always" })
	public void CreateSignature() throws HarnessException {
		ZimbraAccount.AccountZCS().authenticate();
		ZimbraAccount.AccountZCS()
				.soapSend("<CreateSignatureRequest xmlns='urn:zimbraAccount'>" + "<signature name='" + this.sigName
						+ "' >" + "<content type='text/html'>'" + this.contentHTMLSig + "'</content>" + "</signature>"
						+ "</CreateSignatureRequest>");

		// Refresh UI
		app.zPageMain.zRefreshMainUI();

		logger.info("CreateSignature: finish");
	}


	/**
	 * Test case : Forward message with html signature and Verify signature through
	 * soap Create signature through soap Send message with html signature
	 * through soap Fwd same message to another account say (accountB()) Verify
	 * html signature in forwarded message through soap
	 */

	@Test (description = "Forward message with html signature and verify html signature through soap",
			groups = { "functional", "L2" })

	public void ForwardMessageWithHtmlSignature_01() throws HarnessException {

		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), this.sigName);
		logger.info(signature.dBodyHtmlText);
		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		ZAssert.assertEquals(signature.getName(), this.sigName, "Verify text signature is created");

		String subject = "subject" + ConfigProperties.getUniqueString();
		String bodyText = "text" + ConfigProperties.getUniqueString();
		String bodyHTML = "text <b>bold" + ConfigProperties.getUniqueString() + "</b> text";
		String contentHTML = XmlStringUtil.escapeXml("<html>" + "<head></head>" + "<body>" + bodyHTML + "<br></br>" + "</body>" + "</html>");
		String signatureContent = XmlStringUtil.escapeXml("<html>" + "<head></head>" + "<body>" + signature.dBodyHtmlText + "</body>" + "</html>");

		// Send a message to the account with html signature
		ZimbraAccount.AccountZCS()
				.soapSend("<SendMsgRequest xmlns='urn:zimbraMail'>" + "<m>" + "<e t='t' a='"
						+ app.zGetActiveAccount().EmailAddress + "'/>" + "<su>" + subject + "</su>"
						+ "<mp ct='multipart/alternative'>" + "<mp ct='text/plain'>" + "<content>" + bodyText
						+ "</content>" + "</mp>" + "<mp ct='text/html'>" + "<content>" + contentHTML + signatureContent
						+ "\n</content>" + "</mp>" + "</mp>" + "</m>" + "</SendMsgRequest>");

		// Get the mail item for the new message
		MailItem mail = MailItem.importFromSOAP(ZimbraAccount.AccountZCS(), "in:inbox subject:(" + subject + ")");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inboxFolder);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// Forward the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_FORWARD);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountB().EmailAddress);

		// Send the message
		mailform.zSubmit();

		ZimbraAccount.AccountB().soapSend("<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+ "<query>in:inbox subject:(" + mail.dSubject + ")</query>" + "</SearchRequest>");

		String id = ZimbraAccount.AccountB().soapSelectValue("//mail:SearchResponse/mail:m", "id");

		ZimbraAccount.AccountB().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + id + "' html='1'/>" + "</GetMsgRequest>");
		Element getMsgResponse = ZimbraAccount.AccountB().soapSelectNode("//mail:GetMsgResponse", 1);

		MailItem received = MailItem.importFromSOAP(getMsgResponse);

		// Verify TO, Fwd'ed Subject, HtmlBody,HtmlSignature
		ZAssert.assertStringContains(received.dSubject, "Fwd", "Verify the subject field contains the 'Fwd' prefix");
		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress,
				"Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, ZimbraAccount.AccountB().EmailAddress,
				"Verify the to field is correct");
		ZAssert.assertStringContains(received.dBodyHtml.toLowerCase(), bodyHTML, "Verify html body content is correct");
		ZAssert.assertStringContains(received.dBodyHtml.toLowerCase(), this.sigBody,
				"Verify html signature is correct");
	}
}