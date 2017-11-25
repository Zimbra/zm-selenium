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
public class PageManageACL extends AbsTab {

	public static class Locators {
		public static final String GEAR_ICON = "css=div[class=ImgConfigure]";
		public static final String ACL_ADD = "css=td[id^='ztabv__ACCT_EDIT_dwt_button_'] td[id$='title']:contains('Add')";
		public static final String GRANTED_ACL = "css=div[id='zl'] table tr:nth-child(2) td div div table";
		public static final String YES_BUTTON = "css=div[id^='zdlg__MSG__DWT'] td[id$='_button5_title']:contains('Yes')";
		public static final String NO_BUTTON = "zdlg__MSG__GLOBAL__confirm2btn_button4_title";
		public static final String OK_BUTTON = "css=td#zdlg__UNDEFINE";
		public static final String DOMAIN_EDIT_ACL_ADD = "css=td[id^='ztabv__DOAMIN_EDIT_dwt_button'] td[id$='title']:contains('Add')";
		public static final String EDIT_ACL = "css=td[id^='ztabv__ACCT_EDIT_dwt_button'] td:contains('Edit')";
		public static final String DELETE_ACL = "css=td[id^='ztabv__ACCT_EDIT_dwt_button'] div:contains('Delete')";
		public static final String ADD_GLOBAL_ACL = "css=td[id='zmi__zb_currentApp__NEW_title']";
		public static final String DELETE_GLOBAL_ACL = "css=div[id='zmi__zb_currentApp__DELETE'] td[id='zmi__zb_currentApp__DELETE_title']";
	}

	public PageManageACL(AbsApplication application) {
		super(application);
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the Admin Console is loaded in the browser
		if (!MyApplication.zIsLoaded())
			throw new HarnessException("Admin Console application is not active!");

		boolean present = sIsElementPresent(Locators.GEAR_ICON);
		if (!present) {
			return (false);
		}

		boolean visible = zIsVisiblePerPosition(Locators.GEAR_ICON, 0, 0);
		if (!visible) {
			logger.debug("isActive() visible = " + visible);
			return (false);
		}

		return (true);

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
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

		if (button == Button.B_ADD) {
			locator = Locators.ACL_ADD;
			page = new WizardAddACL(this);

		} else if (button == Button.B_ADD_ACL_AT_DOMAIN) {
			locator = Locators.DOMAIN_EDIT_ACL_ADD;
			page = new WizardAddACL(this);

		} else if (button == Button.B_EDIT) {
			locator = Locators.EDIT_ACL;
			page = new WizardEditACL(this);

		} else if (button == Button.B_DELETE) {
			locator = Locators.DELETE_ACL;
			page = new DialogForDeleteOperationACL(this.MyApplication, null);

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		this.sClickAt(locator, "");
		SleepUtil.sleepMedium();

		if (page != null) {
			SleepUtil.sleepLong();
		}

		sMouseOut(locator);
		return (page);
	}

	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		tracer.trace("Click dialog button " + button);
		if (button == null)
			throw new HarnessException("button cannot be null");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_YES) {
			locator = Locators.YES_BUTTON;

		} else if (button == Button.B_NO) {
			locator = Locators.NO_BUTTON;

		} else if (button == Button.B_OK) {
			for (int i = 0; i <= 15; i++) {
				if (sIsElementPresent(Locators.OK_BUTTON + i + "_button2_title")) {
					locator = Locators.OK_BUTTON + i + "_button2_title";
					break;
				}
			}
		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (locator == null) {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not present!");
		}

		this.sClickAt(locator, "");
		SleepUtil.sleepLong();

		return (page);
	}

	public boolean zVerifyHeader(String header) throws HarnessException {
		if (this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}

	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown(" + pulldown + ", " + option + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + option);

		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (option == null)
			throw new HarnessException("Option cannot be null!");

		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = null;

		if (pulldown == Button.B_GEAR_BOX) {
			pulldownLocator = Locators.GEAR_ICON;

			if (option == Button.B_ADD_GLOBAL_ACL) {
				optionLocator = Locators.ADD_GLOBAL_ACL;
				page = new WizardGlobalACL(this);

			} else if (option == Button.B_DELETE) {
				optionLocator = Locators.DELETE_GLOBAL_ACL;
				page = new DialogForDeleteOperationACL(this.MyApplication, null);

			} else {
				throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
			}

		} else {
			throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
		}

		if (pulldownLocator != null) {

			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option " + option + " pulldownLocator "
						+ pulldownLocator + " not present!");
			}

			this.sClickAt(pulldownLocator, "");
			SleepUtil.sleepMedium();

			if (optionLocator != null) {

				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator "
							+ optionLocator + " not present!");
				}

				this.sClickAt(optionLocator, "");
			}
		}
		return (page);
	}

	@Override
	public void zNavigateTo() throws HarnessException {
		// TODO Auto-generated method stub

	}
}