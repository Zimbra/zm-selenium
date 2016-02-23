package com.zimbra.qa.selenium.projects.ajax.tests.search.search;

/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
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
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.search.PageSearch;
import com.zimbra.qa.selenium.projects.ajax.ui.search.PageSearch.Locators;

public class TypingInSearchOpensSearchMenu extends PrefGroupMailByMessageTest {

	public TypingInSearchOpensSearchMenu() {
		logger.info("New " + TypingInSearchOpensSearchMenu.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox",	"TRUE");
	}
	
	@Bugs(	ids = "102600" )
	@Test(description = "Typing in: in search input and pressing down arrow >> it opens Search menu list as well", groups = { "functional" })
	
	public void TypingInSearchOpensSearchMenu_01() throws HarnessException {

		// Type In: in search input
		app.zPageSearch.zAddSearchQuery("in:");
		SleepUtil.sleepMedium();
		
		//Press Down arrow
		app.zPageSearch.sKeyDown(Locators.zSearchInput, "40");

		//Verify only Search input menu should open and Search drop down should not open
		ZAssert.assertTrue(app.zPageSearch.sIsElementPresent(PageSearch.Locators.zSearchInputMenu),"In:menu should open");
		ZAssert.assertFalse(app.zPageSearch.sIsElementPresent(PageSearch.Locators.zSearchDropDownMenu),"Search menu should not open");

	}

}