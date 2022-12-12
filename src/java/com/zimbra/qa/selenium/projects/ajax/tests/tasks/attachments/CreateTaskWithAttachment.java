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
package com.zimbra.qa.selenium.projects.ajax.tests.tasks.attachments;

import java.util.HashMap;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.tasks.FormTaskNew;
import com.zimbra.qa.selenium.projects.ajax.pages.tasks.FormTaskNew.Field;

public class CreateTaskWithAttachment extends SetGroupMailByMessagePreference {

	@SuppressWarnings("serial")
	public CreateTaskWithAttachment() {
		logger.info("New "+ CreateTaskWithAttachment.class.getCanonicalName());
		super.startingPage = app.zPageTasks;
		super.startingAccountPreferences = new HashMap<String , String>() {{
			put("zimbraPrefShowSelectionCheckbox", "TRUE");
			put("zimbraPrefComposeFormat", "text");
			put("zimbraPrefGroupMailBy", "message");
			put("zimbraPrefTasksReadingPaneLocation", "bottom");
		}};
	}


	@Bugs (ids = "104231")
	@Test (description = "Create task with attachment",
			groups = { "smoke", "upload", "testcafe" })

	public void CreateTaskWithAttachment_01() throws HarnessException {

		try {

			String subject = "task" + ConfigProperties.getUniqueString();
			String body = "taskbody"+ ConfigProperties.getUniqueString();

			// Create file item
			final String fileName = "testtextfile.txt";
			final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;

			// Click NEW button
			FormTaskNew taskNew = (FormTaskNew) app.zPageTasks.zToolbarPressButton(Button.B_NEW);

			// Fill out the resulting form
			taskNew.zFillField(Field.Subject, subject);
			taskNew.zFillField(Field.Body, body);

			// Upload the file
			app.zPageTasks.zToolbarPressButton(Button.B_Attachment);
			app.zPageTasks.zToolbarPressButton(Button.B_ATTACH);
			zUpload(filePath);

			// Submit task
			taskNew.zSubmit();

			TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
			ZAssert.assertEquals(task.getName(), subject, "Verify task subject");
			ZAssert.assertEquals(task.gettaskBody().trim(), body.trim(), "Verify task body");
			ZAssert.assertEquals(app.zGetActiveAccount().soapSelectValue("//mail:GetMsgResponse//mail:mp[@cd='attachment']", "filename"), fileName, "Verify task attachment");

			// Verify UI for attachment
			ZAssert.assertTrue(app.zPageMail.zVerifyAttachmentExistsInMail(fileName), "Verify attachment exists in the task");

		} finally {
			app.zPageMain.zKeyboardKeyEvent(Keys.ESCAPE);
		}
	}
}