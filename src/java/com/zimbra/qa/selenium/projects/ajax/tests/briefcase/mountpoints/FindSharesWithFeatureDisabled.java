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
package com.zimbra.qa.selenium.projects.ajax.tests.briefcase.mountpoints;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.LinkItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.EnableBriefcaseFeature;
import com.zimbra.qa.selenium.projects.ajax.pages.briefcase.DialogFindShares;

public class FindSharesWithFeatureDisabled extends EnableBriefcaseFeature {

	public FindSharesWithFeatureDisabled() {
		logger.info("New " + FindSharesWithFeatureDisabled.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
		super.startingAccountPreferences.put("zimbraFeatureCalendarEnabled", "FALSE");
	}


	@Bugs (ids = "60854")
	@Test (description = "Click on Find Shares link when some of the Features are disabled - Verify Find Shares dialog is displayed",
			groups = { "functional", "L3" })

	public void FindSharesWithFeatureDisabled_01() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		new LinkItem();

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Click on Find shares link
		DialogFindShares dialog = (DialogFindShares) app.zTreeBriefcase.zPressPulldown(Button.B_TREE_FOLDERS_OPTIONS,
				Button.B_TREE_FIND_SHARES);

		// Verify Find Shares dialog is opened
		ZAssert.assertTrue(dialog.zIsActive(), "Verify Find Shares dialog is opened");

		// Dismiss the dialog
		dialog.zPressButton(Button.B_CANCEL);
	}
}