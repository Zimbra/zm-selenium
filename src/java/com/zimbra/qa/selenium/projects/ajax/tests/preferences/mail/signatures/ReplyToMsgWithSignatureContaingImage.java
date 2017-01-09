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

import com.zimbra.qa.selenium.framework.items.FileItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem;
import com.zimbra.qa.selenium.framework.items.SignatureItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.XmlStringUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;

public class ReplyToMsgWithSignatureContaingImage extends AjaxCommonTest {
	String sigName = "signame" + ConfigProperties.getUniqueString();
	String sigBody = "signature<b>bold" + ConfigProperties.getUniqueString() + "</b>signature";
	String contentHTMLSig = XmlStringUtil
			.escapeXml("<html>" + "<head></head>" + "<body>" + sigBody + "</body>" + "</html>");

	public ReplyToMsgWithSignatureContaingImage() {
		super.startingPage = app.zPageMail;
	}

	@BeforeMethod(groups = { "always" })
	public void CreateSignature() throws HarnessException {
		ZimbraAccount account = ZimbraAccount.AccountZWC();
		account.authenticate();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account,
				SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory()
				+ "/data/public/other/logo_sigclub.png";

		FileItem file = new FileItem(filePath);

		String fileName = file.getName();

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>"
				+ "<doc l='" + briefcaseFolder.getId() + "'>" + "<upload id='"
				+ attachmentId + "'/>" + "</doc>" + "</SaveDocumentRequest>");

		account.soapSend("<CreateSignatureRequest xmlns='urn:zimbraAccount'>" + "<signature name='" + this.sigName
				+ "' >" + "<content type='text/html'>" + this.contentHTMLSig + "<br />"
				+ "		<img src='/home/"+ account.EmailAddress + "/Briefcase/" + fileName 
				+ "' width='150' dfsrc='doc:Briefcase/"+ fileName 
				+ "' data-mce-src='/home/" + account.EmailAddress + "/Briefcase/" + fileName + "' />"
				+ "<br />" + this.contentHTMLSig +" </content>" + "</signature>"
				+ "</CreateSignatureRequest>");

		// Refresh UI so that signature get loaded
		app.zPageMain.sRefresh();

		logger.info("CreateSignature: finish");

	}

	/**
	 * Test case : Create a signature having image through SOAP. Send a mail using that signature.
	 * Open the received mail and click reply to it. 
	 * Verify that the body of reply compose form contains the signature with image displayed.
	 * 
	 * @throws HarnessException
	 */
	@Test(description = " Verify the display of signature image in reply compose window!", groups = { "functional", "L2" })
	public void ReplyMsgWithHtmlSignature_01() throws HarnessException {

		// Verify that the signature is created successfully
		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), this.sigName);
		ZAssert.assertEquals(signature.getName(), this.sigName, "verified Text Signature is created");

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyHtml = "body<b>bold" + ConfigProperties.getUniqueString() + "</b> text";

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);

		// Fill out the form with the data
		mailform.zFill(mail);

		// Select the signature from Options
		app.zPageMail.zToolbarPressPulldown(Button.B_OPTIONS,Button.O_ADD_SIGNATURE,sigName);

		// Send the message 
		mailform.zSubmit();

		//Login to the recipient account
		app.zPageLogin.zLogin(ZimbraAccount.AccountA());

		// Open the mail
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// Reply to the the mail
		mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);

		//Get Signature image src value
		String imgSrc = mailform.zGetSignatueImageSrc();

		//Verify that signature image src has value containing the the server URL 
		ZAssert.assertStringContains(imgSrc, "https://" + ConfigProperties.getStringProperty("server.host")+ "/", "Signature Image source is not correct in reply compose page!");

	}
}
