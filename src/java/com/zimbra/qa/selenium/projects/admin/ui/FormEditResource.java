/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.admin.ui;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsForm;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.projects.admin.ui.FormEditAccount.Locators;


public class FormEditResource extends AbsForm {

	public static class TreeItem {
		public static final String PROPERTIES="Properties";
	}

	public static class Locators {
		public static final String NAME_TEXT_BOX="css=input#ztabv__RES_EDIT_";
		public static final String DA_NAME_TEXT_BOX="css=input[id='ztabv__RES_EDIT_displayName_2']";
		public static final String SAVE_BUTTON="css=td[id^='zb__ZaCurrentAppBar__SAVE_']:contains('Save')";
		public static final String CLOSE_BUTTON="css=td[id^='zb__ZaCurrentAppBar__CLOSE']";
	}

	public FormEditResource(AbsApplication application) {
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
	}

	public void zClickTreeItem(String treeItem) throws HarnessException {
		sClick("css=td:contains('" + treeItem + "')");
	}

	public void setName(String name) throws HarnessException {
		for(int i=10;i>=2;i--) {
			if(sIsElementPresent(Locators.NAME_TEXT_BOX+i+"_name_3")) {
				sType(Locators.NAME_TEXT_BOX+i+"_name_3", name);
				return;
			}
		}
		sType(Locators.NAME_TEXT_BOX+"name_3", name);
		}
	
	
	public void setNameAsDA(String name) throws HarnessException {
		sType(Locators.DA_NAME_TEXT_BOX, name);
		}
	}

