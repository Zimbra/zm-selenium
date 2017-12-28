package com.zimbra.qa.selenium.projects.ajax.tests.zextras;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.chat.BuddyItem;
import com.zimbra.qa.selenium.projects.ajax.pages.chat.WizardAddBuddy;
import com.zimbra.qa.selenium.projects.ajax.pages.chat.PageChatPanel.Userstatus;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail.Field;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;


public class chat extends AjaxCore {

	public chat() {
		logger.info("New "+ chat.class.getCanonicalName());
		super.startingPage=app.zPageChatPanel;
	}

	@Test (description = "Add new buddy",
			groups = { "smoke", "L0" })

	public void ChatAddNewBuddy_01() throws HarnessException {

		BuddyItem item = new BuddyItem();
		item.setEmailAddress(ZimbraAccount.Account1().EmailAddress);
		app.zPageChatPanel.zNavigateTo();

		WizardAddBuddy wizard = (WizardAddBuddy)app.zPageChatPanel.zEllipsesOption(Button.B_ADD_NEW_BUDDY);

		wizard.zCompleteWizard(item);

		ZimbraAccount account = app.zGetActiveAccount();

		ZAssert.assertStringContains(app.zPageChatPanel.zUserStatus(ZimbraAccount.Account1().EmailAddress),Userstatus.invited.getStatus(),"Verify the user status");

		app.zPageLogin.zNavigateTo();
		app.zPageLogin.zLogin(ZimbraAccount.Account1());
		app.zPageChatPanel.zNavigateTo();

		ZAssert.assertStringContains(app.zPageChatPanel.zUserStatus(account.EmailAddress),Userstatus.need_response.getStatus(),"Verify the user status");ZAssert.assertStringContains(app.zPageChatPanel.zUserStatus(account.EmailAddress),Userstatus.need_response.getStatus(),"Verify the user status");

		app.zPageChatPanel.zSelectUser(account.EmailAddress, Userstatus.need_response);

	}

	@Test (description = "Chat with added new buddy when receiver is Online",
			groups = { "smoke", "L0" })

	public void ChatAddNewBuddy_02() throws HarnessException {

		BuddyItem item = new BuddyItem();
		item.setEmailAddress(ZimbraAccount.Account2().EmailAddress);
		app.zPageChatPanel.zNavigateTo();

		WizardAddBuddy wizard = (WizardAddBuddy)app.zPageChatPanel.zEllipsesOption(Button.B_ADD_NEW_BUDDY);

		wizard.zCompleteWizard(item);

		ZimbraAccount account = app.zGetActiveAccount();

		app.zPageLogin.zNavigateTo();
		app.zPageLogin.zLogin(ZimbraAccount.Account2());
		app.zPageChatPanel.zNavigateTo();

		app.zPageChatPanel.zSelectUser(account.EmailAddress, Userstatus.need_response);

		account.soapSend(
				"<ZxChatRequest xmlns='urn:zimbraAccount'>"
						+ "<type>1</type>"
						+ "<from>"+account.EmailAddress+"</from>"
						+ "<to>"+ZimbraAccount.Account2().EmailAddress+"</to>"
						+ "<message>Test</message>"
						+ "<message_type>chat</message_type>"
						+ "<action>send_message</action>"
						+ "</ZxChatRequest>");

		app.zPageChatPanel.zSelectUser(account.EmailAddress, Userstatus.offline);

		ZAssert.assertStringContains(app.zPageChatPanel.zGetMsg(), "Test", "verify the chat message");
	}


	@Test (description = "Chat with added new buddy when receiver is Offline",
			groups = { "smoke", "L0" })

	public void ChatAddNewBuddy_03() throws HarnessException {

		String message = "Chat"+ConfigProperties.getUniqueString();
		BuddyItem item = new BuddyItem();
		item.setEmailAddress(ZimbraAccount.Account3().EmailAddress);
		app.zPageChatPanel.zNavigateTo();

		WizardAddBuddy wizard = (WizardAddBuddy)app.zPageChatPanel.zEllipsesOption(Button.B_ADD_NEW_BUDDY);
		wizard.zCompleteWizard(item);

		ZimbraAccount account = app.zGetActiveAccount();

		app.zPageLogin.zNavigateTo();
		app.zPageLogin.zLogin(ZimbraAccount.Account3());
		app.zPageChatPanel.zNavigateTo();

		app.zPageChatPanel.zSelectUser(account.EmailAddress, Userstatus.need_response);

		app.zPageChatPanel.zSelectUser(account.EmailAddress, Userstatus.offline);
		app.zPageChatPanel.zSendMsg(message);

		app.zPageLogin.zNavigateTo();
		app.zPageLogin.zLogin(account);
		app.zPageChatPanel.zNavigateTo();

		ZAssert.assertTrue(app.zPageChatPanel.zVerifyChatFolder(),"Verify the chat folder");

		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, "OpenChat - "+ZimbraAccount.Account3().EmailAddress);

		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body),message,"Verify the Open Mail existence");
	}
}
