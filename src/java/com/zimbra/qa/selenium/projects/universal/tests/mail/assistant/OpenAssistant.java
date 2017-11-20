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
package com.zimbra.qa.selenium.projects.universal.tests.mail.assistant;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Shortcut;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.projects.universal.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.universal.pages.DialogAssistant;


public class OpenAssistant extends SetGroupMailByMessagePreference {
	
	public OpenAssistant() {
		logger.info("New "+ OpenAssistant.class.getCanonicalName());
		
		
		

	}
	
	@Test (description = "Open the assistant",
			groups = { "deprecated", "L4" })
	public void OpenAssistant_01() throws HarnessException {
		
		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		DialogAssistant assistant = (DialogAssistant)app.zPageMail.zKeyboardShortcut(Shortcut.S_ASSISTANT);
		assistant.zPressButton(Button.B_CANCEL);
		
	}


}
