/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.items.FolderItem;
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
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;

public class ReplyWithHtmlSignatureAboveMessage extends AjaxCore {
	String sigName = "signame" + ConfigProperties.getUniqueString();
	String sigBody = "signature<b>bold" + ConfigProperties.getUniqueString() + "</b>signature";
	String contentHTMLSig = XmlStringUtil.escapeXml("<html>" + "<head></head>" + "<body>" + sigBody + "</body>" + "</html>");

	@SuppressWarnings("serial")
	public ReplyWithHtmlSignatureAboveMessage() {
		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>() {{
			put("zimbraPrefGroupMailBy", "message");
		}};
	}

	@BeforeMethod(groups = { "always" })
	public void CreateSignature() throws HarnessException {
		ZimbraAccount.AccountZCS()
				.soapSend("<CreateSignatureRequest xmlns='urn:zimbraAccount'>" + "<signature name='" + this.sigName
						+ "' >" + "<content type='text/html'>'" + this.contentHTMLSig + "'</content>" + "</signature>"
						+ "</CreateSignatureRequest>");

		// Refresh UI
		app.zPageMain.zRefreshMainUI();

		logger.info("CreateSignature: finish");
	}


	/**
	 * Test case : Verify html signature above included message While Reply'ing Create
	 * html signature through soap Send message through soap Select Same message and
	 * click Reply from toolbar Click Options dropdown and select Signature
	 * Verify signature should place above included message when replying message
	 */

	@Test (description = "Verify html signature placed above included message when replying",
			groups = { "sanity" })

	public void ReplyWithHtmlSignatureAboveMessage_01() throws HarnessException {

		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), this.sigName);
		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		ZAssert.assertEquals(signature.getName(), this.sigName, "Verify text signature is created");

		String subject = "subject" + ConfigProperties.getUniqueString();
		String bodyText = "text" + ConfigProperties.getUniqueString();
		String bodyHTML = "text <b>bold" + ConfigProperties.getUniqueString() + "</b> text";
		String contentHTML = XmlStringUtil.escapeXml("<html>" + "<head></head>" + "<body>" + bodyHTML + "<br></br>" + "</body>" + "</html>");

		// Send a message to the account
		ZimbraAccount.AccountZCS()
				.soapSend("<SendMsgRequest xmlns='urn:zimbraMail'>" + "<m>" + "<e t='t' a='"
						+ app.zGetActiveAccount().EmailAddress + "'/>" + "<su>" + subject + "</su>"
						+ "<mp ct='multipart/alternative'>" + "<mp ct='text/plain'>" + "<content>" + bodyText
						+ "</content>" + "</mp>" + "<mp ct='text/html'>" + "<content>" + contentHTML + "\n</content>"
						+ "</mp>" + "</mp>" + "</m>" + "</SendMsgRequest>");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inboxFolder);

		// Select message
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Forward the item
		actual.zPressButton(Button.B_REPLY);
		ZAssert.assertTrue(actual.zGetMailPropertyAsText(DisplayMail.Field.Subject).contains("Re"), "Verify Reply Window");

		// Click Options Drop Down and select Signature
		app.zPageMail.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_ADD_Reply_SIGNATURE, this.sigName);

		// Verify Signature is place above included message.
		actual.zVerifySignaturePlaceInHTML("AboveIncludedMessage", this.sigBody, "Reply");
	}
}