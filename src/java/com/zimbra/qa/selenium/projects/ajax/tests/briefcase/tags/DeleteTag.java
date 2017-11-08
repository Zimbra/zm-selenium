/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.briefcase.tags;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.TagItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.FeatureBriefcaseTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;

public class DeleteTag extends FeatureBriefcaseTest {

	public DeleteTag() {
		logger.info("New " + DeleteTag.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
	}


	@Test (description = "Delete a tag - Right click, Delete",
			groups = { "functional", "L3" })

	public void DeleteTag_01() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create the tag to delete
		String name = "tag" + ConfigProperties.getUniqueString();

		account.soapSend("<CreateTagRequest xmlns='urn:zimbraMail'>" + "<tag name='" + name + "' color='1' />"
				+ "</CreateTagRequest>");

		// Get the tag
		TagItem tag = TagItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNotNull(tag, "Verify the tag was created");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Delete the tag using context menu
		DialogWarning dialog = (DialogWarning) app.zTreeBriefcase.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_DELETE,
				tag);
		ZAssert.assertNotNull(dialog, "Verify the warning dialog opened");

		// Click "Yes" to confirm
		dialog.zPressButton(Button.B_YES);

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Verify the tag is deleted
		account.soapSend("<GetTagRequest xmlns='urn:zimbraMail'/>");

		String tagname = account.soapSelectValue("//mail:GetTagResponse//mail:tag[@name='" + name + "']", "name");
		ZAssert.assertNull(tagname, "Verify the tag is deleted");
	}
}