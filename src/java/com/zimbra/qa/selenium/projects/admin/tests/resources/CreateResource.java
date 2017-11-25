/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.resources;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.ResourceItem;
import com.zimbra.qa.selenium.projects.admin.pages.WizardCreateResource;

public class CreateResource extends AdminCore {

	public CreateResource() {
		logger.info("New " + CreateResource.class.getCanonicalName());
		super.startingPage=app.zPageManageResources;
	}


	/**
	 * Testcase : Create a basic resource.
	 * Steps :
	 * 1. Create a resource from GUI.
	 * 2. Verify resource is created using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = "Create a basic resource",
			groups = { "functional-skip", "L3-skip" })

	public void CreateResource_01() throws HarnessException {

		// Create a new resource in the Admin Console
		ResourceItem resource = new ResourceItem();

		// Click "New"
		WizardCreateResource wizard = (WizardCreateResource) app.zPageManageResources.zToolbarPressButton(Button.B_NEW);

		// Fill out the wizard and click Finish
		wizard.zCompleteWizard(resource);

		// Verify the resource exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
				+ 		"<calresource by='name'>" +  resource.getEmailAddress() + "</calresource>"
				+		"</GetCalendarResourceRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCalendarResourceResponse/admin:calresource", 1);
		ZAssert.assertNotNull(response, "Verify the RESOURCE is created successfully");
	}


	/**
	 * Testcase : Create a basic resource.
	 * Steps :
	 * 1. Create a resource from GUI i.e. Manage Accounts --> Resources --> Gear box --> New
	 * 2. Verify resource is created using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = "Create a basic resource using Manage Accounts --> Resources --> Gear box --> New",
			groups = { "functional-skip", "L3-skip" })

	public void CreateResource_02() throws HarnessException {

		// Create a new resource in the Admin Console
		ResourceItem resource = new ResourceItem();

		// Click "New --> Resources"
		WizardCreateResource wizard = (WizardCreateResource) app.zPageManageResources
				.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_NEW);

		// Fill out the wizard and click Finish
		wizard.zCompleteWizard(resource);

		// Verify the resource exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
				+ 		"<calresource by='name'>" +  resource.getEmailAddress() + "</calresource>"
				+		"</GetCalendarResourceRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCalendarResourceResponse/admin:calresource", 1);
		ZAssert.assertNotNull(response, "Verify the RESOURCE is created successfully");
	}


	/**
	 * Testcase : Create a basic location resource.
	 * Steps :
	 * 1. Create a Location resource from GUI.
	 * 2. Verify resource is created using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = "Create a basic Location resource",
			groups = { "sanity", "L0" })

	public void CreateResource_03() throws HarnessException {

		// Create a new resource in the Admin Console
		ResourceItem resource = new ResourceItem();

		// Click "New"
		WizardCreateResource wizard = (WizardCreateResource) app.zPageManageResources
				.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_NEW);

		// Fill out the wizard and click Finish
		wizard.zSetResourceType(WizardCreateResource.Locators.LOCATION);
		wizard.zCompleteWizard(resource);

		// Verify the resource exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
				+ 		"<calresource by='name'>" +  resource.getEmailAddress() + "</calresource>"
				+		"</GetCalendarResourceRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCalendarResourceResponse/admin:calresource", 1);
		ZAssert.assertNotNull(response, "Verify the RESOURCE is created successfully");
	}


	/**
	 * Testcase : Create a basic equipment resource.
	 * Steps :
	 * 1. Create a Equipment resource from GUI.
	 * 2. Verify resource is created using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = "Create a basic Equipment resource",
			groups = { "sanity", "L0" })

	public void CreateResource_04() throws HarnessException {

		// Create a new resource in the Admin Console
		ResourceItem resource = new ResourceItem();

		// Click "New"
		WizardCreateResource wizard = (WizardCreateResource) app.zPageManageResources
				.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_NEW);

		// Fill out the wizard and click Finish
		wizard.zSetResourceType(WizardCreateResource.Locators.EQUIPMENT);
		wizard.zCompleteWizard(resource);

		// Verify the resource exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
				+ 		"<calresource by='name'>" +  resource.getEmailAddress() + "</calresource>"
				+		"</GetCalendarResourceRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCalendarResourceResponse/admin:calresource", 1);
		ZAssert.assertNotNull(response, "Verify the RESOURCE is created successfully");
	}
}
