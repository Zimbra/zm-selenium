/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

/**
 * @author Matt Rhoades
 *
 */
public class PageEditDistributionList extends AbsTab {

	public static class Locators {
		public static final String DL_EDIT_ACL = "css=div[id^='zti__AppAdmin__Home__dlLstHV'] div[class='ZTreeItemTextCell']:contains('ACL')";
		public static final String DL_EDIT_ACL_ADD = "css=td[id^='ztabv__UNDEFINE_dwt_button_'] td[id$='title']:contains('Add')";
		public static final String DL_EDIT_ACL_GRANTEE_NAME = "css=div[class='DwtDialog WindowOuterContainer'] table[class='dynselect_table'] input[id^='zdlgv__EDIT_ACL'][id$='_grantee_email_display']";
		public static final String PROPERTIES = "css=div[id^='zti__AppAdmin__Home__dlLstHV'] div[class='ZTreeItemTextCell']:contains('Properties')";
		public static final String PREFERENCES = "css=div[id^='zti__AppAdmin__Home__dlLstHV'] div[class='ZTreeItemTextCell']:contains('Preferences')";
		public static final String ALIASES = "css=div[id^='zti__AppAdmin__Home__dlLstHV'] div[class='ZTreeItemTextCell']:contains('Aliases')";
		public static final String OWNER = "css=div[id^='zti__AppAdmin__Home__dlLstHV'] div[class='ZTreeItemTextCell']:contains('Owners')";
		public static final String ADD_BUTTON = "css=td#ztabv__UNDEFINE_dwt_button_";
	}

	public PageEditDistributionList(AbsApplication application) {
		super(application);
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		throw new HarnessException("implement me");
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zNavigateTo() throws HarnessException {
		throw new HarnessException("implement me");
	}

	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption, String item) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {

		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_PROPERTIES) {
			locator = Locators.PROPERTIES;
			page = new FormEditDistributionList(this.MyApplication);

		} else if (button == Button.B_PREFERENCES) {
			locator = Locators.PREFERENCES;
			page = new FormEditDistributionList(this.MyApplication);

		} else if (button == Button.B_ALIASES) {
			locator = Locators.ALIASES;

		} else if (button == Button.B_OWNER) {
			locator = Locators.OWNER;

		} else if (button == Button.B_ADD) {
			for (int i = 0; i <= 15; i++) {
				if (sIsElementPresent("css=td[id^='ztabv__UNDEFINE_dwt_button_" + i
						+ "___container'] div table tbody tr td:nth-child(2):contains('Add')")) {
					locator = "css=td[id^='ztabv__UNDEFINE_dwt_button_" + i
							+ "___container'] div table tbody tr td:nth-child(2):contains('Add')";
					break;
				}
			}
			page = new FormEditDistributionList(this.MyApplication);

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		this.sClickAt(locator, "");
		SleepUtil.sleepMedium();

		if (page != null) {
			SleepUtil.sleepMedium();
		}

		return (page);
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		return null;
	}
}