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
/**
 *
 */
package com.zimbra.qa.selenium.projects.admin.ui;

import java.awt.event.KeyEvent;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.AbsWizard;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.admin.items.ResourceItem;


/**
 * @author Matt Rhoades
 *
 */
public class WizardCreateResource extends AbsWizard {
	public static class Locators {
		public static final String zdlg_RESOURCE_NAME = "zdlgv__NEW_RES_displayName";
		public static final String zdlg_RESOURCE_NAME_DA= "css=input[id='zdlgv__NEW_RES_displayName']";
		public static final String zdlg_RESOURCE_LOCAL_NAME = "zdlgv__NEW_RES_name";
		public static final String zdlg_RESOURCE_DOMAIN_NAME="zdlgv__NEW_RES_name_2_display";
		public static final String zdlg_OK="zdlg__NEW_ALIAS_button2_title";
		public static final String zdlg_RESOURCE_TYPE="zdlgv__NEW_RES_zimbraCalResType_display";
		public static final String zdlg_RESOURCE_TYPE_LOCATION="zdlgv__NEW_RES_zimbraCalResType_choice_0";
		public static final String zdlg_RESOURCE_TYPE_EQUIPMENT="zdlgv__NEW_RES_zimbraCalResType_choice_1";
		public static final String LOCATION="Location";
		public static final String EQUIPMENT="Equipment";
	}
	public String resourceType="";


	public WizardCreateResource(AbsTab page) {
		super(page);
	}

	@Override
	public IItem zCompleteWizard(IItem item) throws HarnessException {

		if ( !(item instanceof ResourceItem) )
			throw new HarnessException("item must be an ResourceItem, was "+ item.getClass().getCanonicalName());

		ResourceItem resource = (ResourceItem)item;


		String CN = resource.getLocalName();
		String domain = resource.getDomainName();


		sType(Locators.zdlg_RESOURCE_NAME, CN);
		sType(Locators.zdlg_RESOURCE_LOCAL_NAME, CN);

		/**
		 * If you use normal type method domain is taken as default domain name.
		 * Below line of code is not grid friendly but this is only solution working currently.
		 */
		if(ZimbraSeleniumProperties.isWebDriver()) {
			SleepUtil.sleepSmall();
			this.clearField(Locators.zdlg_RESOURCE_DOMAIN_NAME);
		}
		zType(Locators.zdlg_RESOURCE_DOMAIN_NAME,"");
		zType(Locators.zdlg_RESOURCE_DOMAIN_NAME,domain);

		this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_TAB);
		sType(Locators.zdlg_RESOURCE_LOCAL_NAME, CN);


		if(resourceType!="") {
			sClick(Locators.zdlg_RESOURCE_TYPE);
			if(resourceType.equals(Locators.LOCATION)) {
				sClick(Locators.zdlg_RESOURCE_TYPE_LOCATION);
			} else if(resourceType.equals(Locators.EQUIPMENT)) {
				sClick(Locators.zdlg_RESOURCE_TYPE_EQUIPMENT);
			}
		}else{
			zClickAt(Locators.zdlg_RESOURCE_TYPE,"");

		}

		clickFinish(AbsWizard.Locators.RESOURCE_DIALOG);

		return resource;
	}



	@Override
	public String myPageName() {
		return null;
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		return false;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}


}
