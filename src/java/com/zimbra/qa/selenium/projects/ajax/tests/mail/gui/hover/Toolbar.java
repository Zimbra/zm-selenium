/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.mail.gui.hover;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;


public class Toolbar extends PrefGroupMailByMessageTest {

	
	public Toolbar() {
		logger.info("New "+ Toolbar.class.getCanonicalName());
		
	}
	
	@Test(	description = "Hover over Delete button",
			groups = { "deprecated" })		// Toolbar tooltips are now handled in the browser, not the DOM
	public void Toolbar_01() throws HarnessException {
		
		AbsTooltip tooltip = app.zPageMail.zHoverOver(Button.B_DELETE);
		
		String contents = tooltip.zGetContents();
		
		ZAssert.assertStringContains(contents, "Trash", "Verify the tool tip text"); // TODO: I18N
		
	}

	@Test(	description = "Hover over Reply button",
			groups = { "deprecated" })		// Toolbar tooltips are now handled in the browser, not the DOM
	public void Toolbar_02() throws HarnessException {
		
		AbsTooltip tooltip = app.zPageMail.zHoverOver(Button.B_REPLY);
		
		String contents = tooltip.zGetContents();
		
		ZAssert.assertStringContains(contents, "Reply", "Verify the tool tip text"); // TODO: I18N

		
	}


}
