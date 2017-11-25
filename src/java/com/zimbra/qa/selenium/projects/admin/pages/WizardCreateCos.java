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
import com.zimbra.qa.selenium.projects.admin.items.CosItem;

public class WizardCreateCos extends AbsWizard {

	public static class Locators {
		public static final String zdlg_COS_NAME = "zdlgv__NEW_COS_cn";
	}

	public WizardCreateCos(AbsTab page) {
		super(page);
	}

	@Override
	public IItem zCompleteWizard(IItem item) throws HarnessException {

		if (!(item instanceof CosItem))
			throw new HarnessException("item must be an COSItem, was " + item.getClass().getCanonicalName());

		CosItem domain = (CosItem) item;

		String cosName = domain.getName();

		zType(Locators.zdlg_COS_NAME, "");
		this.zKeyboard.zTypeCharacters(cosName);
		zType(Locators.zdlg_COS_NAME, cosName);

		clickFinish(AbsWizard.Locators.COS_DIALOG);

		return (domain);
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