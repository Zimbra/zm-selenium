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
package com.zimbra.qa.selenium.projects.ajax.tests.tasks;

import java.awt.event.KeyEvent;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.TaskItem;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.tasks.PageTasks.Locators;

public class QuickAddTask extends AjaxCore {

	public QuickAddTask() {
		logger.info("New " + QuickAddTask.class.getCanonicalName());
		super.startingPage = app.zPageTasks;
	}


	@Test (description = "Create a new task via quick add",
			groups = { "sanity", "L0" })

	public void QuickAddTask_01() throws HarnessException {

		String subject = "task" + ConfigProperties.getUniqueString();
		app.zPageTasks.sClick(Locators.zNewTaskBanner);
		app.zPageTasks.zKeyboard.zTypeCharacters(subject);
		app.zPageTasks.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertEquals(task.getName(), subject, "Verify task subject");
	}
}