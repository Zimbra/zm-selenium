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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.gui.hover;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;

public class Toolbar extends PrefGroupMailByMessageTest {

	public Toolbar() {
		logger.info("New " + Toolbar.class.getCanonicalName());
	}


	@Test (description = "Hover over Delete button",
			groups = { "functional", "L2" })

	public void Toolbar_01() throws HarnessException {

		// No DOM or java script support to verify tooltip so checking through attributes
		String tooltip = app.zPageMail.sGetAttribute("css=div[id='zb__TV-main__DELETE']@title");
		ZAssert.assertStringContains(tooltip, "Move selected item(s) to Trash [Del]", "Verify tooltip exists as a title");
	}


	@Test (description = "Hover over Reply button",
			groups = { "functional","L3" })

	public void Toolbar_02() throws HarnessException {

		// No DOM or java script support to verify tooltip so checking through attributes
		String tooltip = app.zPageMail.sGetAttribute("css=div[id='zb__TV-main__REPLY']@title");
		ZAssert.assertStringContains(tooltip, "Reply to the sender of the selected message [r]", "Verify tooltip exists as a title");
	}
}