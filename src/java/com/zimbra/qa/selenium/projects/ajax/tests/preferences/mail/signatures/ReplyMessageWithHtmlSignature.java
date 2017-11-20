/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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

public class ReplyMessageWithHtmlSignature extends AjaxCore {
	String sigName = "signame" + ConfigProperties.getUniqueString();
	String sigBody = "signature<b>bold" + ConfigProperties.getUniqueString() + "</b>signature";
	String contentHTMLSig = XmlStringUtil.escapeXml("<html>" + "<head></head>" + "<body>" + sigBody + "</body>" + "</html>");

	public ReplyMessageWithHtmlSignature() {
		super.startingPage = app.zPageMail;
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
	 * Test case : Reply message with html signature and Verify signature through
	 * soap Create signature through soap Send message with html signature
	 * through soap Reply same message. Verify html signature in Replied message
	 * through soap
	 */

	@Test (description = "Reply message with html signature and Verify signature through soap",
			groups = { "functional", "L2" })

	public void ReplyMessageWithHtmlSignature_01() throws HarnessException {

		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);

		// Verify Signature
		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), this.sigName);
		ZAssert.assertEquals(signature.getName(), this.sigName, "Verify text signature is created");

		String subject = "subject" + ConfigProperties.getUniqueString();
		String bodyText = "text" + ConfigProperties.getUniqueString();
		String bodyHTML = "text <b>bold" + ConfigProperties.getUniqueString() + "</b> text";
		String contentHTML = XmlStringUtil.escapeXml("<html>" + "<head></head>" + "<body>" + bodyHTML + "<br></br>" + "</body>" + "</html>");
		String signatureContent = XmlStringUtil.escapeXml("<html>" + "<head></head>" + "<body>" + signature.dBodyHtmlText + "</body>" + "</html>");

		// Send a message to the account
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
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Send the message
		mailform.zSubmit();

		ZimbraAccount.AccountZCS().soapSend("<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+ "<query>in:inbox subject:(" + mail.dSubject + ")</query>" + "</SearchRequest>");

		String id = ZimbraAccount.AccountZCS().soapSelectValue("//mail:SearchResponse/mail:m", "id");

		ZimbraAccount.AccountZCS().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + id + "' html='1'/>" + "</GetMsgRequest>");
		Element getMsgResponse = ZimbraAccount.AccountZCS().soapSelectNode("//mail:GetMsgResponse", 1);
		MailItem received = MailItem.importFromSOAP(getMsgResponse);

		// Verify TO, Reply'ed Subject, HtmlBody,HtmlSignature
		ZAssert.assertStringContains(received.dSubject, "Re", "Verify the subject field contains the 'Re' prefix");
		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress,
				"Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, ZimbraAccount.AccountZCS().EmailAddress,
				"Verify the to field is correct");
		ZAssert.assertStringContains(received.dBodyHtml.toLowerCase(), bodyHTML, "Verify the body content is correct");
		ZAssert.assertStringContains(received.dBodyHtml.toLowerCase(), this.sigBody, "Verify the signature is correct");
	}
}
