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
package com.zimbra.qa.selenium.projects.admin.pages;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.items.GALItem;
import com.zimbra.qa.selenium.projects.admin.items.GALItem.GALMode;

/**
 * @author Matt Rhoades
 *
 */
public class PageManageCofigureGAL extends AbsTab {

	public static class Locators {
		public static final String GAL_HEADER = "css=span:contains('GAL')";
		public static final String DOMAIN_GAL = "css=div[id^='zti__AppAdmin__CONFIGURATION__DOMAINS__'] div[id$='_textCell']:contains('GAL')";
		public static final String GAL_MODE = "css=div[id^='ztabv__DOAMIN_EDIT'][id$='_zimbraGalMode']";
		public static final String EXT_DATA_SRC_NAME = "css=div[id$='ldap_ds.name']";
		public static final String INT_DATA_SRC_NAME = "css=div[id$='zimbra_ds.name']";
		public static final String SERVER_TYPE = "css=div[id$='_galservertype']";
	}

	public PageManageCofigureGAL(AbsApplication application) {
		super(application);
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		return sIsElementPresent(Locators.GAL_HEADER);
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zNavigateTo() throws HarnessException {

		if (zIsActive()) {
			return;
		}

		sClick(Locators.DOMAIN_GAL);
		zWaitForActive();
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

		if (button == Button.B_CONFIGURE_GAL) {
			locator = PageMain.Locators.HomeConfigureGal;
			page = new WizardConfigureGAL(this);

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		this.sClickAt(locator, "");

		if (page != null) {
			SleepUtil.sleepMedium();
		}

		return (page);
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		return null;
	}

	public boolean zVerifyHeader(String header) throws HarnessException {
		if (this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}

	// Verify the displayed GAL configuration data
	public boolean zVerifyGALConfigData(GALItem gItem) throws HarnessException {
		boolean status = false;
		ZAssert.assertEquals(this.getElement(Locators.GAL_MODE).getText(), gItem.getNewGALMode().toString(),
				"GAL mode is not correct!");

		if (gItem.getCurrentGALMode().equals(GALMode.Internal)) {

			ZAssert.assertEquals(this.getElement(Locators.EXT_DATA_SRC_NAME).getText(), gItem.getDataSourceName(),
					"External Data source is not displayed correctly!");
			ZAssert.assertStringContains(this.getElement(Locators.SERVER_TYPE).getText().replaceAll(" ", ""),
					gItem.getServerType().toString(), "GAL Server type is not correct!");

		} else if (gItem.getCurrentGALMode().equals(GALMode.External)) {

			ZAssert.assertEquals(this.getElement(Locators.INT_DATA_SRC_NAME).getText(), gItem.getDataSourceName(),
					"Internal Data source is not displayed correctly!");

		} else {
			throw new HarnessException("Implement me for changing GAL mode from " + gItem.getCurrentGALMode() + " to "
					+ gItem.getNewGALMode() + "!");
		}

		status = true;
		return status;
	}
}