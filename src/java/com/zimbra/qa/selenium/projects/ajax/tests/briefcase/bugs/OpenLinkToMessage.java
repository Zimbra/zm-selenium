package com.zimbra.qa.selenium.projects.ajax.tests.briefcase.bugs;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.PageMail;

public class OpenLinkToMessage extends AjaxCommonTest {

	String url;

	@SuppressWarnings("serial")
	public OpenLinkToMessage() {
		logger.info("New " + OpenLinkToMessage.class.getCanonicalName());

		// test starts in the Briefcase tab
		super.startingPage = app.zPageBriefcase;

		// use an account with message view
		super.startingAccountPreferences = new HashMap<String, String>() {
			{
				put("zimbraPrefGroupMailBy", "message");
				put("zimbraPrefMessageViewHtmlPreferred", "TRUE");
			}
		};
	}

	@Bugs(ids = "56802")
	@Test(description = "Open link to the message - Verify List View Rows are displayed after message closed", groups = { "functional" })
	public void OpenLink_01() throws HarnessException {
		// Create the message data to be sent
		String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();

		ZimbraAccount.AccountA()
				.soapSend(
						"<SendMsgRequest xmlns='urn:zimbraMail'>" + "<m>"
								+ "<e t='t' a='"
								+ app.zGetActiveAccount().EmailAddress + "'/>"
								+ "<su>" + subject + "</su>"
								+ "<mp ct='text/plain'>" + "<content>content"
								+ ZimbraSeleniumProperties.getUniqueString()
								+ "</content>" + "</mp>" + "</m>"
								+ "</SendMsgRequest>");

		// Temporary workaround
		// Click on Mail tab 
		app.zPageMain.zClickAt("id=zb__App__Mail_title", "0,0");
		// Click Get Mail button to view folder in list
		// app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);

		SleepUtil.sleepMedium();

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(),
				"subject:(" + subject + ")");

		// Store opened url
		// url = ZimbraSeleniumProperties.getBaseURL();

		url = app.zPageBriefcase.getLocation();

		// Open link through RestUtil
		// Map<String, String> map = CodeCoverage.getInstance().getQueryMap();

		Map<String, String> map = new HashMap<String, String>();

		if (url.contains("?") && !url.endsWith("?")) {
			String query = url.split("\\?")[1];

			for (String p : query.split("&")) {
				if (p.contains("=")) {
					map.put(p.split("=")[0], p.split("=")[1]);
				}
			}
		}

		map.put("id", mail.getId());

		app.zPageBriefcase.openUrl("zimbra", map);

		app.zPageBriefcase
				.zWaitForElementPresent(PageMail.Locators.zCloseIconBtn_messageWindow);

		app.zPageBriefcase.zClickAt(
				PageMail.Locators.zCloseIconBtn_messageWindow, "0,0");

		ZAssert
				.assertTrue(app.zPageBriefcase
						.sIsElementPresent(PageMail.Locators.zTVRows),
						"Verify List View Rows are displayed after message pane is closed");
	}

	@AfterMethod(groups = { "always" })
	public void afterMethod() throws HarnessException {
		logger.info("Switching to Briefcase page ...");

		// app.zPageBriefcase.openUrl("", null);
		app.zPageBriefcase.openUrl(url);

		app.zPageBriefcase.zNavigateTo();
	}
}
