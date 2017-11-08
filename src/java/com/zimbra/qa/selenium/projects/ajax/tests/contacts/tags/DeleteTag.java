/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.tags;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.TagItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;

public class DeleteTag extends PrefGroupMailByMessageTest {

	public DeleteTag() {
		logger.info("New "+ DeleteTag.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	@Test (description = "Delete a tag - Right click, Delete",
			groups = { "smoke", "L1" })

	public void DeleteTag_01() throws HarnessException {

		// Create the tag to delete
		TagItem tag = TagItem.CreateUsingSoap(app.zGetActiveAccount());
		ZAssert.assertNotNull(tag, "Verify the tag was created");

		// Click on Get Mail to refresh the folder list
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Delete the tag using context menu
		DialogWarning dialog = (DialogWarning) app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, Button.B_DELETE, tag);
		ZAssert.assertNotNull(dialog, "Verify the warning dialog opened");

		// Click "Yes" to confirm
		dialog.zPressButton(Button.B_YES);

		// To check whether deleted tag is exist
		app.zGetActiveAccount().soapSend("<GetTagRequest xmlns='urn:zimbraMail'/>");

		String tagname = app.zGetActiveAccount().soapSelectValue("//mail:GetTagResponse//mail:tag[@name='" + tag.getName() + "']","name");
		ZAssert.assertNull(tagname, "Verify the tag is deleted");
	}
}