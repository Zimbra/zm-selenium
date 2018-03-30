/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.mail;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.PageMail.Locators;

public class DisplayExternalImages extends SetGroupMailByMessagePreference {

	public DisplayExternalImages() throws HarnessException {
		logger.info("New "+ DisplayExternalImages.class.getCanonicalName());
	}


	@Bugs (ids = "75010" )
	@Test (description = "View the display of external images when user clicks on 'Always display images sent from' email link",
			groups = { "functional", "L2" })

	public void DisplayExternalImages_01() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/externalImage01/externalimage01.txt";
		final String subject = "externalimage01";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Verify warning info bar with other links
		ZAssert.assertTrue(app.zPageMail.zHasWDDLinks(), "Verify display images link");

		// Click on 'Always display images sent from' email link to always display the external image
		app.zPageMail.sClick(Locators.zMsgViewEmailLink);
		SleepUtil.sleepLong();

		// Verify warning info bar with other links
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zMsgViewInfoBar), "Verify that info bar with links is not appearing now");

		try {
			// Select the body frame and verify the external image is displayed
			app.zPageMail.sSelectFrame("iframe[name$='__body__iframe']");
			ZAssert.assertTrue(app.zPageMail.sIsElementPresent(Locators.zMsgExternalImage), "Verify the external image is displayed");
		} finally {
			app.zPageMail.sSelectFrame("relative=top");
		}

		// Refresh the inbox folder and open the message again to check if the external image warning info bar is displayed
		SleepUtil.sleepMedium();
		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inboxFolder);
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Verify Warning info bar with other links
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zMsgViewInfoBar), "Verify that info bar with links is not appearing now");
		ZAssert.assertFalse(app.zPageMail.zHasWDDLinks(), "Verify display images link");

		// Select the body frame and verify the external image is displayed
		app.zPageMail.sSelectFrame("iframe[name$='__body__iframe']");
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(Locators.zMsgExternalImage), "Verify the external is displayed");
	}


	@Bugs (ids = "70600" )
	@Test (description = "View the display of external images when user clicks on 'Always display images sent from' domain link",
			groups = { "functional", "L3" })

	public void DisplayExternalImages_02() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/externalImage01/externalimage02.txt";
		final String subject = "external image test";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Verify warning info bar with other links
		ZAssert.assertTrue(app.zPageMail.zHasWDDLinks(), "Verify display images link");

		// Click on 'Always display images sent from' email link to always display the external image
		app.zPageMail.sClick(Locators.zMsgViewDomainLink);
		SleepUtil.sleepLong();

		// Verify warning info bar with other links
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zMsgViewInfoBar), "Verify that info bar with links is not appearing now");

		try {
			// Select the body frame and verify the external image is displayed
			app.zPageMail.sSelectFrame("iframe[name$='__body__iframe']");
			ZAssert.assertTrue(app.zPageMail.sIsElementPresent(Locators.zMsgExternalImage), "Verify the external image is displayed");
		} finally {
			app.zPageMail.sSelectFrame("relative=top");
		}

		// Refresh the inbox folder and open the message again to check if the external image warning info bar is displayed
		SleepUtil.sleepMedium();
		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inboxFolder);
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Verify Warning info bar with other links
		ZAssert.assertFalse(app.zPageMail.sIsVisible(Locators.zMsgViewInfoBar), "Verify that info bar with links is not appearing now");
		ZAssert.assertFalse(app.zPageMail.zHasWDDLinks(), "Verify display images link");

		// Select the body frame and verify the external image is displayed
		app.zPageMail.sSelectFrame("iframe[name$='__body__iframe']");
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(Locators.zMsgExternalImage), "Verify the external is displayed");
	}
}