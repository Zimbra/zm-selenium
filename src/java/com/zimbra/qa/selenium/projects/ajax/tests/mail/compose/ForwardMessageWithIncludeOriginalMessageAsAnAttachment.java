package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

public class ForwardMessageWithIncludeOriginalMessageAsAnAttachment extends PrefGroupMailByMessageTest {

	public ForwardMessageWithIncludeOriginalMessageAsAnAttachment() {
		logger.info("New "+ ForwardMessageWithIncludeOriginalMessageAsAnAttachment.class.getCanonicalName());
		
		super.startingPage = app.zPagePreferences;
		
	}
	
	@Test( description = "Bug 102745 - 'Forward email with 'Include original message as attachment' option enabled ",
			groups = { "functional", "L2" })
	
	public void Forward_01() throws HarnessException {
		
		// Set 'Include original message as an attachment' from preferences	
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Mail);
		app.zPagePreferences.sClick("css=td[id='Prefs_Select_FORWARD_INCLUDE_WHAT_dropdown']"); // Select drop down for email forwarding menu
		// Select 'Include original message as attachment'
		app.zPagePreferences.sClickAt(("css=div[parentid='Prefs_Select_FORWARD_INCLUDE_WHAT_Menu_1'] td[id$='_title']:contains('Include original message as an attachment')"),"");	
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		// Create message 
		String subject = "subject"+ ConfigProperties.getUniqueString();
		
		ZimbraAccount.AccountB().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
		
		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");
				
		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);
		
		// Forward email
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_FORWARD);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		
		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.Account8().EmailAddress);
		
		// Send the message
		mailform.zSubmit();

		//-- Verification
		app.zPageLogin.zLogin(ZimbraAccount.Account8());
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// From the receiving end, verify the message details
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.Account8(), "subject:("+ subject +")");
		ZAssert.assertNotNull(received, "Verify the message is received correctly");
		
		// Verify the attachment exists in the forwarded mail		
		final String AttachmentName = subject;

		ZimbraAccount.Account8().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>"
				+		"<m id='"+ received.getId() +"'/>"
				+	"</GetMsgRequest>");

		String filename = ZimbraAccount.Account8().soapSelectValue("//mail:mp[@cd='attachment']", "filename");
		ZAssert.assertEquals(filename, AttachmentName, "Verify the attachment exists in the forwarded mail");
	}

}
