/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.pages;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsForm;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

public class FormEditServer extends AbsForm {

	public static class TreeItem {
		public static final String GENERAL_INFORMATION = "General Information";
	}

	public static class Locators {
		public static final String NAME_TEXT_BOX = "css=input#ztabv__ACCT_EDIT_";
		public static final String DA_NAME_TEXT_BOX = "css=input[id='ztabv__ACCT_EDIT_name_2']";
		public static final String SAVE_BUTTON = "css=td[id^='zb__ZaCurrentAppBar__SAVE']";
		public static final String CLOSE_BUTTON = "css=td[id^='zb__ZaCurrentAppBar__CLOSE']";
		public static final String Pull_DOWN = "css=div[aria-controls='zm__zb_currentApp__MENU_POP'] div[class='ImgSelectPullDownArrow']";
		public static final String PULL_DOWN_CLOSE = "css=td[id='zmi__zb_currentApp__CLOSE_title']";
		public static final String PASSWORD = "css=input[id$='ztabv__ACCT_EDIT_password']";
		public static final String CONFIRM_PASSWORD = "css=input[id$='ztabv__ACCT_EDIT_confirmPassword']";
		public static final String zdlg_OK = "css=td[id$='_button2_title']:contains('OK')";
		public static final String DESCRIPTION = "css=input[id^='ztabv__SERVER_EDIT_description']";
		public static final String NOTES = "css=textarea[id='ztabv__SERVER_EDIT_zimbraNotes']";
	}

	public FormEditServer(AbsApplication application) {
		super(application);

		logger.info("new " + myPageName());

	}

	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the Admin Console is loaded in the browser
		if (!MyApplication.zIsLoaded())
			throw new HarnessException("Admin Console application is not active!");

		boolean present = sIsElementPresent("");
		if (!present) {
			return (false);
		}

		String attrs = sGetAttribute("");
		if (!attrs.contains("ZSelected")) {
			return (false);
		}

		return (true);

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zFill(IItem item) throws HarnessException {
	}

	@Override
	public void zSubmit() throws HarnessException {
		sClickAt(Locators.SAVE_BUTTON, "");
		SleepUtil.sleepSmall();
		sClickAt(Locators.CLOSE_BUTTON, "0,0");
	}

	public void zSelectTreeItem(String treeItem) throws HarnessException {
		sClickAt("css=td:contains('" + treeItem + "')", "");
	}

	public void setDescription(String desc) throws HarnessException {
		sType(Locators.DESCRIPTION, desc);
		SleepUtil.sleepSmall();
	}

	public void setNotes(String notes) throws HarnessException {
		sType(Locators.NOTES, notes);
		SleepUtil.sleepSmall();
	}

}
