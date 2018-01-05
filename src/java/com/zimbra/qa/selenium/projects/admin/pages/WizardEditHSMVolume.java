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
import com.zimbra.qa.selenium.projects.admin.items.HSMItem;

public class WizardEditHSMVolume extends AbsWizard {

	public static class Locators {
		public static final String VOLUME_NAME = "css=div[class='DwtDialog'][style*='z-index: 7'] input.xform_field[id$=_name]";
		public static final String VOLUME_PATH = "css=div[class='DwtDialog'][style*='z-index: 7'] input.xform_field[id$=_path]";
		public static final String SET_CURRENT_CHECKBOX = "css=div[class='DwtDialog'][style*='z-index: 7'] input[id$=_current]";
		public static final String ENABLE_COMPRESSION_CHECKBOX = "css=div[class='DwtDialog'][style*='z-index: 7'] input[id$=_compressed]";
		public static final String OK_BUTTON = "css=div[style*='z-index: 7'] td[id$='_button2_title']";
		public static final String CANCEL_BUTTON = "css=div[style*='z-index: 7'] td[id$='_button1_title']";
		public static final String successfulDialog = "css=div.DwtDialog[style*='display: block;'] table td:contains('Zimbra Administration')";
		public static final String VOLUME_ADDED_OK_BUTTON = "css=div#zdlg__MSG.DwtDialog[style*='z-index: 7'] td[class='ZWidgetTitle']:contains('OK')";
	}

	public WizardEditHSMVolume(AbsTab page) {
		super(page);
		logger.info("New " + WizardAddNewVolume.class.getName());
	}

	@Override
	public IItem zCompleteWizard(IItem item) throws HarnessException {
		if (!(item instanceof HSMItem))
			throw new HarnessException("Item must be a HSMItem, was " + item.getClass().getCanonicalName());

		SleepUtil.sleepSmall();
		HSMItem hsmVolume = (HSMItem) item;
		if (!sGetValue(Locators.VOLUME_NAME).equals(hsmVolume.getVolumeName())) {
			sType(Locators.VOLUME_NAME, hsmVolume.getVolumeName());
		}
		if (!sGetValue(Locators.VOLUME_PATH).equals(hsmVolume.getVolumePath())) {
			sType(Locators.VOLUME_PATH, hsmVolume.getVolumePath());
		}
		if (hsmVolume.getIsCurrent() != null) {
			if (hsmVolume.getIsCurrent() == true) {
				sCheck(Locators.SET_CURRENT_CHECKBOX);
			} else if (hsmVolume.getIsCurrent() == false) {
				sUncheck(Locators.SET_CURRENT_CHECKBOX);
			}
		}
		sClickAt(Locators.OK_BUTTON, "");
		if (zWaitForElementPresent(Locators.successfulDialog)) {
			sClick(Locators.VOLUME_ADDED_OK_BUTTON);
		} else {
			throw new HarnessException("Policy edited successfully dialog is not appeared");
		}
		return (hsmVolume);
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
