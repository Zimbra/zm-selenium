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
 * @author Matt Rhoades
 *
 */
public class WizardAddACLAtCos extends AbsWizard {

	public static class Locators {
		public static final String ACL_RIGHT_NAME = "css=input[id$='right_display']";
		public static final String ACL_GRANTEE_NAME = "css=div[class='DwtDialog WindowOuterContainer'] table[class='dynselect_table'] input";
		public static final String ACL_RIGHT_TYPE_SYSTEM_DEFINED = "css=div[id$='_right_type_choice_0']";
		public static final String ACL_RIGHT_TYPE_CUSTOM = "css=div[id$='right_type_choice_1']";
		public static final String Pull_DOWN = "css=div[id^='zdlgv__EDIT_ACL'][id*='_right_type'] div[class='ImgSelectPullDownArrow']";
		public static final String ADD_AND_FINISH_BUTTON = "css=td[class='ZWidgetTitle']:contains('Add and Finish')";
		public static final String GRANTEE_TYPE = "css=td[id$='_grantee_type___container'] input";
		public static final String SYSTEM_DEFINED_RIGHTS = "css=div[id='zdlgv__EDIT_ACLundefined_right_type_choice_0']";
	}

	public WizardAddACLAtCos(AbsTab page) {
		super(page);
	}

	public IItem zCompleteWizard(IItem item) throws HarnessException {

		AclItem acl = (AclItem) item;
		String granteeAccount = acl.getGranteeAccountEmail();
		String rightName = acl.getRightName();

		sClick(Locators.ACL_GRANTEE_NAME);
		sType(Locators.ACL_GRANTEE_NAME, granteeAccount);
		SleepUtil.sleepSmall();

		sClick(Locators.Pull_DOWN);
		SleepUtil.sleepSmall();
		sClick(Locators.ACL_RIGHT_TYPE_SYSTEM_DEFINED);

		sType(Locators.ACL_RIGHT_NAME, rightName);
		SleepUtil.sleepSmall();

		sClickAt(Locators.ADD_AND_FINISH_BUTTON, "");
		return item;
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