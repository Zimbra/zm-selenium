/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.zimlets.attachbriefcase;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.*;
import com.zimbra.qa.selenium.projects.universal.pages.UniversalPages;
import com.zimbra.qa.selenium.projects.universal.pages.DialogAttach;
import com.zimbra.qa.selenium.projects.universal.pages.mail.*;
import com.zimbra.qa.selenium.projects.universal.pages.mail.PageMail.Locators;

public class AttachBriefcaseFileAndSendMail extends SetGroupMailByMessagePreference {

	public AttachBriefcaseFileAndSendMail() {
		logger.info("New " + AttachBriefcaseFileAndSendMail.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
	}

	@Test (description = "Attach an briefcase file to a mail", 
			groups = { "functional", "L2" })

	public void AttachBriefcaseFileAndSendMail_01() throws HarnessException {

		// -- DATA

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create document item
		DocumentItem docItem = new DocumentItem();

		String docName = docItem.getName();
		String docText = docItem.getDocText();

		// Create document using SOAP
		String contentHTML = XmlStringUtil.escapeXml("<html>" + "<body>" + docText + "</body>" + "</html>");

		account.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>" + "<doc name='" + docName
				+ "' l='" + briefcaseFolder.getId() + "' ct='application/x-zimbra-doc'>" + "<content>" + contentHTML
				+ "</content>" + "</doc>" + "</SaveDocumentRequest>");

		// Create the message data to be sent
		MailItem mail = new MailItem();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA()));
		mail.dSubject = "subject" + ConfigProperties.getUniqueString();
		mail.dBodyText = "body" + ConfigProperties.getUniqueString();

		// -- GUI

		// Click Get Mail button to get the new contact
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		mailform.zFill(mail);

		// Click Attach drop down and click Briefcase
		app.zPageMail.zToolbarPressPulldown(Button.B_ATTACH, Button.O_BRIEFCASEATTACH);

		DialogAttach dialog = new DialogAttach(app, ((UniversalPages) app).zPageMail);
		ZAssert.assertTrue(dialog.zIsActive(), "Attach File dialog gets open and active");

		// Click on Briefcase folder
		SleepUtil.sleepMedium();
		dialog.sClick(Locators.zAttachBriefcaseFolder);
		SleepUtil.sleepMedium();
		dialog.sClick("css=div[id='zv__BCI'] div[id^='zli__BCI__'] tr td:contains('" + docName + "')");
		SleepUtil.sleepSmall();
		dialog.zPressButton(Button.B_ATTACH);
		SleepUtil.sleepLong();
		mailform.zSubmit();

		// -- Verification

		// From the receiving end, verify the message details
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:(" + mail.dSubject + ")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
		ZimbraAccount.AccountA().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + received.getId() + "'/>" + "</GetMsgRequest>");

		String filename = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@cd='attachment']", "filename");

		ZAssert.assertEquals(filename, docName, "Verify the attached file exist");

	}

}
