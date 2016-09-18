package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.drafts;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowFormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

public class NewWindowCancelAutoDraftSave extends PrefGroupMailByMessageTest {

	public NewWindowCancelAutoDraftSave() {
		logger.info("New "+ NewWindowCancelAutoDraftSave.class.getCanonicalName());
		
		super.startingPage = app.zPagePreferences;
		
	}
	
	@Test( description = "Bug 99875 - 'Message gets auto saved while clicking cancel and 'Auto draft save'= No, from New window ", groups = { "functional" })
	
	public void NewWindowCancelAutoDraftSave_01() throws HarnessException {
		
		// Set 'Include original message as an attachment' from preferences	
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Mail);
		app.zPagePreferences.sClick("css=td[id='Prefs_Select_FORWARD_INCLUDE_WHAT_dropdown']"); // Select drop down for email forwarding menu
		// Select 'Include original message as attachment'
		app.zPagePreferences.sClickAt(("css=div[parentid='Prefs_Select_FORWARD_INCLUDE_WHAT_Menu_1'] td[id$='_title']:contains('Include original message as an attachment')"),"");	
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		// Create message 
		String subject = "subject"+ ConfigProperties.getUniqueString();
		
		ZimbraAccount.AccountA().soapSend(
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
		app.zPageMail.zVerifyMailExists(subject);
				
		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);
		
		// Forward email
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_FORWARD);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		
		SeparateWindowFormMailNew window = null;
		String windowTitle = "Zimbra: Forward";

		try {

			window = (SeparateWindowFormMailNew) app.zPageMail.zToolbarPressButton(Button.B_DETACH_COMPOSE);
			SleepUtil.sleepLong();

			//Select the window
			window.sSelectWindow(windowTitle);				

			//Go to draft and check for draft message
			mailform.zToolbarPressButton(Button.B_CANCEL);	
			app.zPageMail.sClick("css=div[id='YesNoCancel'] div[id$='_buttons'] td[id$='_button4_title']");// Select no button
			MailItem draft = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
			ZAssert.assertNotNull(draft, "Verify the message is not present in drafts folder");

		} finally {

			if ( window != null ) {
				window.zCloseWindow();
				window = null;
   		 	}
			app.zPageMail.zSelectWindow(null);
		}
	
	}

}
