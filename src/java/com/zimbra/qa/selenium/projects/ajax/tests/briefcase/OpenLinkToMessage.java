/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.briefcase;

import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.FeatureBriefcaseTest;
import com.zimbra.qa.selenium.projects.ajax.ui.PageMain;
import com.zimbra.qa.selenium.projects.ajax.ui.briefcase.PageBriefcase;

public class OpenLinkToMessage extends FeatureBriefcaseTest {
	String url;

	public OpenLinkToMessage() {
		logger.info("New " + OpenLinkToMessage.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
		super.startingAccountPreferences.put("zimbraPrefGroupMailBy", "message");
		super.startingAccountPreferences.put("zimbraPrefMessageViewHtmlPreferred", "TRUE");
	}


	@Bugs (ids = "56802,64833,65939,67059")
	@Test (description = "Open link to the message - Verify List View Rows are displayed after message closed",
			groups = { "functional", "L2" })

	public void OpenLinkToMessage_01() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
						"<SendMsgRequest xmlns='urn:zimbraMail'>" + "<m>"
								+ "<e t='t' a='"
								+ app.zGetActiveAccount().EmailAddress + "'/>"
								+ "<su>" + subject + "</su>"
								+ "<mp ct='text/plain'>" + "<content>content"
								+ ConfigProperties.getUniqueString()
								+ "</content>" + "</mp>" + "</m>"
								+ "</SendMsgRequest>");

		app.zPageMail.zNavigateTo();

		// Click Get Mail button to view message in the list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:(" + subject + ")");

		url = app.zPageBriefcase.getLocation();

		Map<String, String> map = new HashMap<String, String>();

		if (url.contains("?") && !url.endsWith("?")) {
			String query = url.split("\\?")[1];

			for (String p : query.split("&")) {
				if (p.contains("=")) {
					map.put(p.split("=")[0], p.split("=")[1].substring(0, 1));
				}
			}
		}

		map.put("id", mail.getId());

		app.zPageBriefcase.openUrl("", map);

		String locator = PageBriefcase.Locators.zCloseIconBtn.locator;

		app.zPageBriefcase.zWaitForElementPresent(locator);

		app.zPageBriefcase.sClickAt(locator, "");
		SleepUtil.sleepSmall();

		ZAssert.assertTrue(app.zPageBriefcase.zWaitForElementPresent("css=[id=zl__TV-main__rows]", "5000"),
				"Verify List View Rows are displayed after message pane is closed");
	}

	@AfterMethod(groups = { "always" })
	public void afterMethod() throws HarnessException {
		logger.info("Switching to Briefcase page ...");

		// app.zPageBriefcase.openUrl("", null);
		app.zPageBriefcase.openUrl(url);
		app.zPageBriefcase.zWaitForElementPresent(PageMain.Locators.zLogoffOption,"2000");
		app.zPageBriefcase.zNavigateTo();
	}
}
