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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.contacts;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.*;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;

public class ForwardContact extends AjaxCore  {
	public ForwardContact() {
		logger.info("New "+ ForwardContact.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	@Test (description = "Forward a contact by click Forward on the context menu",
			groups = { "bhr", "non-msedge" })

	public void ClickForwardOnContextmenu_01() throws HarnessException {

		// Create a contact
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());

		// Mail subject
		String subject = "subject"+ ConfigProperties.getUniqueString();

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

        // Right Click -> Forward
        FormMailNew formMail = (FormMailNew) app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_FORWARD, contact.fileAs);

        // Wait for attachment link present
        for (int i=0; (i<20) && !app.zPageContacts.sIsElementPresent("css=div[id$=_attachments_div] div[class='ImgAttachment']") ; i++ , SleepUtil.sleepVerySmall());

        // since the contact.fileAs length probably large, it is usually trim in the middle and replace with ...
        ZAssert.assertTrue( formMail.zHasAttachment("vcf"), "Verify the VCF attachment is there");

        formMail.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);
        formMail.zFillField(Field.Subject, subject);
        formMail.zSubmit();

        // Verification
        ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ subject +")</query>"
			+	"</SearchRequest>");
        String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");

        ZimbraAccount.AccountA().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>"
			+		"<m id='"+ id +"'/>"
			+	"</GetMsgRequest>");

        // Make sure we have a "mixed" content
        ZimbraAccount.AccountA().soapSelectNode("//mail:mp[@ct='multipart/mixed']", 1);

        // Make sure ct = text/directory
        String ct = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@cd='attachment']", "ct");
        ZAssert.assertEquals(ct, "text/vcard", "Make sure ct = text/vcard");

        // Make sure filename contains .vcf
        String filename = ZimbraAccount.AccountA().soapSelectValue("//mail:mp[@cd='attachment']", "filename");
        ZAssert.assertStringContains(filename, ".vcf", "Make sure filename contains .vcf");
	}
}