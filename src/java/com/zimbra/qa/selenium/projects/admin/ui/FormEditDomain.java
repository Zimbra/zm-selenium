/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.ui;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsForm;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;


public class FormEditDomain extends AbsForm {

	public static class TreeItem {
		public static final String GENERAL_INFORMATION="General Information";
	}

	public static class Locators {
		public static final String DESCRIPTION_TEXT_BOX="css=input#ztabv__DOAMIN_EDIT_";
		public static final String SAVE_BUTTON="css=td[id^='zb__ZaCurrentAppBar__SAVE']";
		public static final String CLOSE_BUTTON="css=td[id^='zb__ZaCurrentAppBar__CLOSE']";
		public static final String DOMAIN_EDIT_THEMES="css=div[id^='zti__AppAdmin__CONFIGURATION__DOMAINS'] div[class='ZTreeItemTextCell']:contains('Themes')";
		public static final String DOMAIN_LOGO_URL="css=input[id^='ztabv__DOAMIN_EDIT_zimbraSkinLogoURL']";
		
	}

	public FormEditDomain(AbsApplication application) {
		super(application);

		logger.info("new " + myPageName());

	}

	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the Admin Console is loaded in the browser
		if ( !MyApplication.zIsLoaded() )
			throw new HarnessException("Admin Console application is not active!");


		boolean present = sIsElementPresent("");
		if ( !present ) {
			return (false);
		}

		String attrs = sGetAttribute("");
		if ( !attrs.contains("ZSelected") ) {
			return (false);
		}

		return (true);

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zFill(IItem item) throws HarnessException {
	}

	@Override
	public void zSubmit() throws HarnessException {
		sClickAt(Locators.SAVE_BUTTON,"");
		SleepUtil.sleepSmall();
		zClickAt(Locators.CLOSE_BUTTON,"0,0");
	}

	public void zClickTreeItem(String treeItem) throws HarnessException {
		sClickAt("css=td:contains('" + treeItem + "')", "");
	}

	public void setName(String name) throws HarnessException {
		for(int i=12;i>=0;i--) {
			if(sIsElementPresent(Locators.DESCRIPTION_TEXT_BOX+i+"_description\\[0\\]_2")) {
				sType(Locators.DESCRIPTION_TEXT_BOX+i+"_description\\[0\\]_2", name);
				return;
			}
		}
		sType(Locators.DESCRIPTION_TEXT_BOX+"description\\[0\\]_2", name);
		}
	
	public void setLogoURL(String URL) throws HarnessException {
		for(int i=12;i>=0;i--) {
			if(sIsElementPresent(Locators.DOMAIN_EDIT_THEMES)) {
				zClickAt(Locators.DOMAIN_EDIT_THEMES,"0,0");
				sType(Locators.DOMAIN_LOGO_URL, URL);
				return;
			}
		}
	
	}
}

