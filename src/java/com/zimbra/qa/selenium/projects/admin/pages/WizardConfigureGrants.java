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

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.AbsWizard;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.admin.items.AclItem;

/**
 * @author Pallavi Khairnar
 *
 */
public class WizardConfigureGrants extends AbsWizard {

	public static class Locators {

		public static final String ACL_RIGHT_TYPE_SYSTEM_DEFINED = "css=div[id$='right_type_choice_0']";
		public static final String ACL_RIGHT_TYPE_CUSTOM = "css=div[id$='right_type_choice_1']";
		public static final String ADD_AND_FINISH_BUTTON = "css=td[class='ZWidgetTitle']:contains('Add and Finish')";
		public static final String EDIT_AND_FINISH_BUTTON = "css=td[class='ZWidgetTitle']:contains('Edit and Finish')";
		public static final String TARGET_TYPE = "css=div[id$='_target_type']";
		public static final String ACCOUNT = "css=div[id$='_target_type_choice_0']";
		public static final String DL = "css=div[id$='target_type_choice_1']";
		public static final String DOMAIN = "css=div[id$='_target_type_choice_2']";
		public static final String GLOBAL = "css=div[id$='_target_type_choice_3']";
		public static final String TARGET_NAME = "css=input[id$='target_2_display']";
		public static final String ACL_RIGHT_NAME = "css=input[id$='right_display']";

	}

	public WizardConfigureGrants(AbsTab page) {
		super(page);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see projects.admin.pages.AbsWizard#completeWizard(projects.admin.clients.Item)
	 */

	public String tagetType = "";

	public String getTagetType() {
		return tagetType;
	}

	public void selectTargetType(String tagetType) throws HarnessException {

		this.sClickAt(Locators.TARGET_TYPE, "");

		if (tagetType.equals(Locators.ACCOUNT)) {
			tagetType = Locators.ACCOUNT;
		} else if (tagetType.equals(Locators.DL)) {
			tagetType = Locators.DL;
		} else if (tagetType.equals(Locators.DOMAIN)) {
			tagetType = Locators.DOMAIN;
		} else if (tagetType.equals(Locators.GLOBAL)) {
			tagetType = Locators.GLOBAL;
		}

		sClick(tagetType);
		this.tagetType = tagetType;
	}

	public IItem zCompleteWizard(IItem item) throws HarnessException {

		AclItem acl = (AclItem) item;

		String targetAccount = acl.getTargetAccountEmail();
		String rightName = acl.getRightName();

		sClick(Locators.TARGET_NAME);
		sType(Locators.TARGET_NAME, targetAccount);
		SleepUtil.sleepMedium();
		sType(Locators.ACL_RIGHT_NAME, rightName);

		return item;

	}

	public boolean zCloseWizard() throws HarnessException {
		this.sClickAt(Locators.ADD_AND_FINISH_BUTTON, "");
		return true;
	}

	public boolean zCloseEditACEWizard() throws HarnessException {
		this.sClickAt(Locators.EDIT_AND_FINISH_BUTTON, "");
		return true;
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
