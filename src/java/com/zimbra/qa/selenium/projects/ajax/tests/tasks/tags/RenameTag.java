/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.tasks.tags;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.TagItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.*;

public class RenameTag extends AjaxCommonTest {

	public RenameTag() {
		logger.info("Tasks " + RenameTag.class.getCanonicalName());
		super.startingPage = app.zPageTasks;
	}


	@Test( description = "Rename a tag - Right click, Rename",
			groups = { "smoke", "L1"})

	public void RenameTag_01() throws HarnessException {

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		// Create the tag to rename
		String name1 = "tag" + ConfigProperties.getUniqueString();
		String name2 = "tag" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<CreateTagRequest xmlns='urn:zimbraMail'>" + "<tag name='"
						+ name1 + "' color='1' />" + "</CreateTagRequest>");

		TagItem tag = TagItem.importFromSOAP(app.zGetActiveAccount(), name1);
		ZAssert.assertNotNull(tag, "Verify the tag was created");

		// Click to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Click on Task explicitly to refresh the tag list
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Rename the tag using the context menu
		DialogRenameTag dialog = (DialogRenameTag) app.zTreeTasks.zTreeItem(Action.A_RIGHTCLICK, Button.B_RENAME, tag);
		ZAssert.assertNotNull(dialog, "Verify the warning dialog opened");

		// Set the new name, click OK
		dialog.zSetNewName(name2);
		dialog.zClickButton(Button.B_OK);

		// Verify the tag is no longer found
		app.zGetActiveAccount().soapSend("<GetTagRequest xmlns='urn:zimbraMail'/>");

		Element[] eTag1 = app.zGetActiveAccount().soapSelectNodes("//mail:tag[@name='" + name1 + "']");
		ZAssert.assertEquals(eTag1.length, 0,"Verify the old tag name no longer exists");

		Element[] eTag2 = app.zGetActiveAccount().soapSelectNodes("//mail:tag[@name='" + name2 + "']");
		ZAssert.assertEquals(eTag2.length, 1, "Verify the new tag name exists");
	}
}