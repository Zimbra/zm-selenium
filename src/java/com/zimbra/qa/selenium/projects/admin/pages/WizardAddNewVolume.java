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

public class WizardAddNewVolume extends AbsWizard {
	
	public static class Locators {
		public static final String VOLUME_TYPE_LOCAL = "css=input[id='zdlgv__DLG_NEW_PWRSTR_VOLUME_BObject_63']";
		public static final String VOLUME_TYPE_S3_BUCKET = "css=[style*='z-index: 7'] input[id='zdlgv__DLG_NEW_PWRSTR_VOLUME_BObject_50']";
		public static final String NEXT_BUTTON = "css=div[style*='z-index: 7'] td[id$='_button12_title']";
		public static final String FINISH_BUTTON = "css=div[style*='z-index: 7'] td[id$='_button13_title']";
		public static final String VOLUME_NAME_TEXT_BOX = "css=div#zdlg__DLG_NEW_PWRSTR_VOLUME_content td.step_body_container div.XFormCase[style*='visibility: visible'] table tr table tbody tr:nth-child(1) input";
		public static final String VOLUME_PATH_TEXT_BOX = "css=div#zdlg__DLG_NEW_PWRSTR_VOLUME_content td.step_body_container div.XFormCase[style*='visibility: visible'] table tr table tbody tr:nth-child(2) input";
		public static final String SET_CURRENT_CHECK_BOX = "css=div#zdlg__DLG_NEW_PWRSTR_VOLUME_content td.step_body_container div.XFormCase[style*='visibility: visible'] table tr table tbody tr:nth-child(5) input";
		public static final String successfulDialog = "css=div.DwtDialog[style*='display: block;'] table td:contains('Zimbra Administration')";
		public static final String VOLUME_ADDED_OK_BUTTON = "css=div#zdlg__MSG.DwtDialog[style*='z-index: 7'] td[class='ZWidgetTitle']:contains('OK')";
	}

	public WizardAddNewVolume(AbsTab page) {
		super(page);
		logger.info("New " + WizardAddNewVolume.class.getName());
	}

	@Override
	public IItem zCompleteWizard(IItem item) throws HarnessException {
		if (!(item instanceof HSMItem)) throw new HarnessException("Item must be a HSMItem, was " + item.getClass().getCanonicalName());

		HSMItem hsmVolume = (HSMItem) item;
		sClickAt(Locators.VOLUME_TYPE_LOCAL, "");
		sClickAt(Locators.NEXT_BUTTON, "");
		sType(Locators.VOLUME_NAME_TEXT_BOX, hsmVolume.getVolumeName());
		sType(Locators.VOLUME_PATH_TEXT_BOX, hsmVolume.getVolumePath());
		SleepUtil.sleepVerySmall();
		sClickAt(Locators.NEXT_BUTTON, "");
		if(hsmVolume.getIsCurrent()== true){
			sCheck(Locators.SET_CURRENT_CHECK_BOX);
		}
		sClickAt(Locators.FINISH_BUTTON, "");
		if (zWaitForElementPresent(Locators.successfulDialog)) {
			sClick(Locators.VOLUME_ADDED_OK_BUTTON);
		} else {
			throw new HarnessException("Policy added successfully dialog is not appeared");
		}
		SleepUtil.sleepSmall();
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
