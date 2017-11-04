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
package com.zimbra.qa.selenium.projects.ajax.tests.tasks.assistant;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Shortcut;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogAssistant;

public class OpenAssistant extends AjaxCommonTest {

	@SuppressWarnings("serial")
	public OpenAssistant() {
		logger.info("New " + OpenAssistant.class.getCanonicalName());
		
		super.startingPage = app.zPageTasks;
		super.startingAccountPreferences = new HashMap<String , String>() {{
			put("zimbraPrefTasksReadingPaneLocation", "bottom");
		}};

	}
/**
 * Test case: Open the assistant
 * 1.Go to Tasks
 * 2.Press '`' or backquote
 * Result:- Zimbra Assistant dialog should get open
 * @throws HarnessException
 */
	@Test( description = "Open the assistant", 
			groups = { "deprecated"})
	public void OpenAssistant_01() throws HarnessException {

		// Click Get Mail button
		app.zPageTasks.zToolbarPressButton(Button.B_REFRESH);

		DialogAssistant assistant = (DialogAssistant) app.zPageTasks.zKeyboardShortcut(Shortcut.S_ASSISTANT);
		assistant.zClickButton(Button.B_CANCEL);
	}

}
