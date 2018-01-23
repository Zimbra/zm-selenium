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

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.AbsWizard;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.admin.items.BackupItem;

public class WizardExportBackup extends AbsWizard {
	
	public static class Locators {
		public static final String EXPORT_PATH = "css=div[style*='z-index: 7'] input[id^='zdlgv__UNDEFINE_'][id$='exportPath']";
		public static final String SELECT_ALL_DOMAIN = "css=div[style*='z-index: 7'] td.ZWidgetTitle:contains('Select All >>')";
		public static final String NEXT_BUTTON = "css=div[style*='z-index: 7'] td[id$='_button12_title']";
		public static final String FINISH_BUTTON = "css=div[style*='z-index: 7'] td[id$='_button13_title']";
	}
	
	public WizardExportBackup(AbsTab page) {
		super(page);
		logger.info("New " + WizardRestore.class.getName());
	}

	@Override
	public IItem zCompleteWizard(IItem item) throws HarnessException {
		if (!(item instanceof BackupItem))
			throw new HarnessException("item must be an BackupItem, was " + item.getClass().getCanonicalName());

		BackupItem backup = (BackupItem) item;
		sClickAt(Locators.NEXT_BUTTON, "");
		sType(Locators.EXPORT_PATH, backup.getExportDestinationPath());
		SleepUtil.sleepSmall();
		sClickAt(Locators.NEXT_BUTTON, "");
		SleepUtil.sleepLong();
		sClickAt(Locators.SELECT_ALL_DOMAIN, "");
		sClickAt(Locators.NEXT_BUTTON, "");
		SleepUtil.sleepSmall();
		sClickAt(Locators.FINISH_BUTTON, "");
		SleepUtil.sleepMedium();

		return (backup);
	}

	@Override
	public String myPageName() {
		return null;
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		return false;
	}
}
