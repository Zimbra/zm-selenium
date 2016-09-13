package com.zimbra.qa.selenium.projects.ajax.tests.mail.readingpane;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByConversationTest;
import com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.drafts.OpenDraftMail;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.PageMail.Locators;


public class VerifyReadingPaneOptions extends PrefGroupMailByConversationTest {

	public VerifyReadingPaneOptions() {
		logger.info("New " + OpenDraftMail.class.getCanonicalName());
	}

	@Bugs(ids = "91533")
	@Test( description = "Verify reading pane options in message view (bottom, right, off)", groups = { "functional" })
	public void VerifyReadingPaneOptionsInMessageView_02() throws HarnessException {
					
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_BY_MESSAGE);
		SleepUtil.sleepVerySmall();
		app.zPageMail.zRefresh();
		
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_READING_PANE_RIGHT);
		ZAssert.assertTrue(app.zPageMail.sIsVisible(Locators.zRightReadingPaneSeparatorBar), "Verify horizontal seperator is visible");
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zBottomReadingPaneSeparatorBar), "Verify vertical seperator is not visible");
		SleepUtil.sleepVerySmall();
		
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_READING_PANE_BOTTOM);
		ZAssert.assertTrue(app.zPageMail.sIsVisible(Locators.zBottomReadingPaneSeparatorBar), "Verify vertical seperator is visible");
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zRightReadingPaneSeparatorBar), "Verify horizontal seperator is not visible");
		SleepUtil.sleepVerySmall();
		
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_READING_PANE_OFF);
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zBottomReadingPaneSeparatorBar), "Verify vertical seperator is not visible");
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zRightReadingPaneSeparatorBar), "Verify horizontal seperator is not visible");
		SleepUtil.sleepVerySmall();
	}
	
	
	@Bugs(ids = "91533")
	@Test( description = "Verify reading pane options in conversation view (bottom, right, off)", groups = { "functional" })
	public void VerifyReadingPaneOptionsInConversationView_01() throws HarnessException {
		
		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(),SystemFolder.Inbox);
		String subject = "subject"+ ConfigProperties.getUniqueString();
		
		// Send a message to the account(self)
		ZimbraAccount.AccountZWC().soapSend(
						"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>content"+ ConfigProperties.getUniqueString() + "</content>" +
						"</mp>" +
						"</m>" +
						"</SendMsgRequest>");

		// Get the mail item for the new message
		MailItem mail = MailItem.importFromSOAP(ZimbraAccount.AccountZWC(),"in:inbox subject:(" + subject + ")");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inboxFolder);


		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);
		SleepUtil.sleepSmall();
				
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_READING_PANE_RIGHT);
		ZAssert.assertTrue(app.zPageMail.sIsVisible(Locators.zRightReadingPaneSeparatorBar), "Verify horizontal seperator is visible");
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zBottomReadingPaneSeparatorBar), "Verify vertical appsash bar is not visible");
		SleepUtil.sleepVerySmall();
		
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_READING_PANE_BOTTOM);
		ZAssert.assertTrue(app.zPageMail.sIsVisible(Locators.zBottomReadingPaneSeparatorBar), "Verify horizontal appSash bar is visible");
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zRightReadingPaneSeparatorBar), "Issues in Bottom reading pane");
		SleepUtil.sleepVerySmall();
		
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_READING_PANE_OFF);
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zBottomReadingPaneSeparatorBar), "Issues in reading pane off option");
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zRightReadingPaneSeparatorBar), "Issues in reading pane off option");
		SleepUtil.sleepVerySmall();
	}
	
}
