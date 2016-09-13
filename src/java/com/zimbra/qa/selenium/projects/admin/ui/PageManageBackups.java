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
/**
 *
 */
package com.zimbra.qa.selenium.projects.admin.ui;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;


/**
 * @author Matt Rhoades
 *
 */
public class PageManageBackups extends AbsTab {

	public static class Locators {
		public static final String GEAR_ICON="css=div.ImgConfigure";
		public static final String VIEW="css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgProperties']";
		public static final String BACKUP="css=div[id='zti__AppAdmin__magHV__BackUpHV_textCell']";
		public static final String RESTORE="css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgRestoreMailbox']";
		public static final String CONFIGURE="css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgAdvancedTools']";
		public static final String REFRESH="css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgRefresh']";

		public static final String DELETE_BUTTON="css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgDelete']";
		public static final String EDIT_BUTTON="css=div[id='zm__zb_currentApp__MENU_POP'] div[class='ImgEdit']";
		public static final String RIGHT_CLICK_MENU_DELETE_BUTTON="css=div[id^='zm__ACLV__MENU_POP'] div[class='ImgDelete']";
		public static final String RIGHT_CLICK_MENU_EDIT_BUTTON="css=div[id^='zm__ACLV__MENU_POP'] div[class='ImgEdit']";		
		
		public static final String TOOLS_AND_MIGRATION_ICON="css=div[id='zti__AppAdmin__Home__magHV_textCell']";
		public static final String HOME="Home";
		public static final String TOOLS_AND_MIGRATION="Tools and Migration";
		public static final String BACKUPS="Backups";
	}

	public PageManageBackups(AbsApplication application) {
		super(application);
	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsTab#isActive()
	 */
	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the Admin Console is loaded in the browser
		if ( !MyApplication.zIsLoaded() )
			throw new HarnessException("Admin Console application is not active!");


		boolean present = sIsElementPresent("css=td[id='zti__AppAdmin__Home__magHV_textTDCell']:contains('" + Locators.TOOLS_AND_MIGRATION + "')");
		if ( !present ) {
			return (false);
		}

		boolean visible = zIsVisiblePerPosition("css=td[id='zti__AppAdmin__Home__magHV_textTDCell']:contains('" + Locators.TOOLS_AND_MIGRATION + "')", 0, 0);
		if ( !visible ) {
			logger.debug("isActive() visible = "+ visible);
			return (false);
		}

		return (true);
	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsTab#myPageName()
	 */
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsTab#navigateTo()
	 */
	@Override
	public void zNavigateTo() throws HarnessException {


		if ( zIsActive() ) {
			// This page is already active.

			return;
		}

		// Click on Tools and Migration -> Downloads
		zClick(Locators.TOOLS_AND_MIGRATION_ICON);
		if(sIsElementPresent(Locators.BACKUP));
		sClickAt(Locators.BACKUP, "");

		zWaitForActive();
	}

	@Override
	public AbsPage zListItem(Action action, String item)
			throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item)
			throws HarnessException {
		return null;
	}
	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption ,String item)
			throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option)
			throws HarnessException {
		return null;
	}

	public boolean zVerifyHeader (String header) throws HarnessException {
		if(this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}

	public boolean zVerifyToolsAndMigrationHeader (String header) throws HarnessException {
		if(this.sIsElementPresent("css=div[id='zti__AppAdmin__Home__magHV_textCell']:contains('" + header + "')"))
			return true;
		return false;
	}
}
