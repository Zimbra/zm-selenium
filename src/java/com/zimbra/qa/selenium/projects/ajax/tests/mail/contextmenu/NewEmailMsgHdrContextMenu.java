package com.zimbra.qa.selenium.projects.ajax.tests.mail.contextmenu;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.PageMail.Locators;

public class NewEmailMsgHdrContextMenu extends PrefGroupMailByMessageTest {

	public NewEmailMsgHdrContextMenu() {
		logger.info("New "
				+ NewEmailMsgHdrContextMenu.class.getCanonicalName());

		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");

	}

	@Test(description = "Receive a  mail - Right Click From Msg Header and verify context menus>>New Emails", groups = { "smoke-temp-skipped" })
	public void NewEmailMessageHdrContextMenu() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<e t='c' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>"+ "body" + ZimbraSeleniumProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");

		// Get all the SOAP data for later verification
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);

		// Select the message so that it shows in the reading pane
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);		
		ZAssert.assertEquals(actual.zGetMailProperty(com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.Field.From), ZimbraAccount.AccountA().EmailAddress, "Verify the From matches");

		app.zPageMail.zRightClickAddressBubble(Field.From);
		app.zPageMail.NewEmailMsgHdrContextMenu();
		SleepUtil.sleepMedium();
		logger.info(ZimbraAccount.AccountA().EmailAddress );
		ZAssert.assertEquals(app.zPageMail.sGetText(Locators.zToAddressBubble), ZimbraAccount.AccountA().EmailAddress , "Compose window opens with email address should present in To field");	


	}

}