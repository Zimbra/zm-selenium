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
package com.zimbra.qa.selenium.projects.universal.tests.contacts.assistant;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.DialogAssistant;

public class OpenAssistant extends AjaxCommonTest {
	
	public OpenAssistant() {
		logger.info("New "+ OpenAssistant.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}
	
	
	@Test( description = "Open the assistant",
			groups = { "deprecated", "L4" })
	
	public void OpenAssistant_01() throws HarnessException {
		
		DialogAssistant assistant = (DialogAssistant)app.zPageContacts.zKeyboardShortcut(Shortcut.S_ASSISTANT);
		assistant.zClickButton(Button.B_CANCEL);
		
	}
}