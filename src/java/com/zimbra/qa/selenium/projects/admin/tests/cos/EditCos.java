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
package com.zimbra.qa.selenium.projects.admin.tests.cos;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.CosItem;
import com.zimbra.qa.selenium.projects.admin.ui.FormEditCos;
import com.zimbra.qa.selenium.projects.admin.ui.PageEditCOS;
import com.zimbra.qa.selenium.projects.admin.ui.PageEditCOS.Locators;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;
import com.zimbra.qa.selenium.projects.admin.ui.PageSearchResults;

public class EditCos extends AdminCommonTest {
	public EditCos() {
		logger.info("New "+ EditCos.class.getCanonicalName());

		// All tests start at the "Accounts" page
		super.startingPage = app.zPageManageCOS;

	}

	/**
	 * Testcase : Edit account name  - Manage Account View
	 * Steps :
	 * 1. Create an cos using SOAP.
	 * 2. Go to Manage Cos View
	 * 3. Select an Cos.
	 * 4. Edit an cos using edit button in Gear box menu.
	 * 5. Verify cos is edited using SOAP.
	 * @throws HarnessException
	 */
	@Test(	description = "Edit Cos name  - Manage Cos View",
			groups = { "functional" })
			public void EditCos_01() throws HarnessException {

		// Create a new cos in the Admin Console using SOAP
		CosItem cos = new CosItem();
		String cosName=cos.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + cosName + "</name>"
				+		"</CreateCosRequest>");

		// Refresh the account list
		app.zPageManageCOS.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be deleted.
		app.zPageManageCOS.zListItem(Action.A_LEFTCLICK, cosName);
		
		// Click on Edit button
		FormEditCos form = (FormEditCos) app.zPageManageCOS.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);
		
		//Edit the name.
		String editedName = "editedCos_" + ZimbraSeleniumProperties.getUniqueString();
		form.setName(editedName);
		
		//Submit the form.
		form.zSubmit();
		
		// Verify the cos exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
		"<GetCosRequest xmlns='urn:zimbraAdmin'>" +
		                     "<cos by='name'>"+editedName+"</cos>"+
		                   "</GetCosRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos", 1);
		ZAssert.assertNotNull(response, "Verify the cos is edited successfully");	
	}
	
	
	/**
	 * Testcase : Edit cos name -- right click 
	 * Steps :
	 * 1. Create an cos using SOAP.
	 * 2. Edit the cos name using UI Right Click.
	 * 3. Verify cos name is changed using SOAP.
	 * @throws HarnessException
	 */
	@Test(	description = "Edit cos name -- right click",
			groups = { "functional" })
			public void EditCos_02() throws HarnessException {
		
		// Create a new cos in the Admin Console using SOAP
		CosItem cos = new CosItem();
		String cosName=cos.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + cosName + "</name>"
				+		"</CreateCosRequest>");

		// Refresh the account list
		app.zPageManageCOS.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be deleted.
		app.zPageManageCOS.zListItem(Action.A_LEFTCLICK, cosName);
		
		// Click on Edit button
		FormEditCos form = (FormEditCos) app.zPageManageCOS.zToolbarPressButton(Button.B_TREE_EDIT);
				
		//Click on General Information tab.
		form.zClickTreeItem(FormEditCos.TreeItem.GENERAL_INFORMATION);

		//Edit the name.
		String editedName = "editedCos_" + ZimbraSeleniumProperties.getUniqueString();
		form.setName(editedName);
		
		//Submit the form.
		form.zSubmit();
		
		// Verify the cos exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
		"<GetCosRequest xmlns='urn:zimbraAdmin'>" +
		                     "<cos by='name'>"+editedName+"</cos>"+
		                   "</GetCosRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos", 1);
		ZAssert.assertNotNull(response, "https://bugzilla.zimbra.com/show_bug.cgi?id=79304");
	}

	/**
	 * Testcase : Edit account name  - Manage Account View
	 * Steps :k
	 * 1. Create an cos using SOAP.
	 * 2. Go to Manage Cos View
	 * 3. Select an Cos.
	 * 4. Edit an cos using edit button in Gear box menu.
	 * 5. Verify cos is edited using SOAP.
	 * @throws HarnessException
	 */
	@Test(	description = "Edit Cos name  - Search Cos View",
			groups = { "functional" })
			public void EditCos_03() throws HarnessException {
	
		// Create a new cos in the Admin Console using SOAP
		CosItem cos = new CosItem();
		String cosName=cos.getName();
	
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + cosName + "</name>"
				+		"</CreateCosRequest>");
	
		// Enter the search string to find the account
		app.zPageSearchResults.setType(PageSearchResults.TypeOfObject.COS);
		app.zPageSearchResults.zAddSearchQuery(cosName);
	
		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);
		
		// Click on cos
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, cos.getName());
	
	
		// Click on Edit button
		app.zPageSearchResults.setType(PageSearchResults.TypeOfObject.COS);
		FormEditCos form = (FormEditCos) app.zPageSearchResults.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);
		
		//Click on General Information tab.
		form.zClickTreeItem(FormEditCos.TreeItem.GENERAL_INFORMATION);
	
		//Edit the name.
		String editedName = "editedCos_" + ZimbraSeleniumProperties.getUniqueString();
		form.setName(editedName);
		
		//Submit the form.
		form.zSubmit();
		
		// Verify the cos exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
		"<GetCosRequest xmlns='urn:zimbraAdmin'>" +
		                     "<cos by='name'>"+editedName+"</cos>"+
		                   "</GetCosRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos", 1);
		ZAssert.assertNotNull(response, "Verify the cos is edited successfully");	
		app.zPageMain.logout();	
	}

	/**
	 * Testcase : Edit cos name -- right click 
	 * Steps :
	 * 1. Create an cos using SOAP.
	 * 2. Edit the cos name using UI Right Click.
	 * 3. Verify cos name is changed using SOAP.
	 * @throws HarnessException
	 */
	@Test(	description = "Edit cos name -- right click",
			groups = { "functional" })
			public void EditCos_04() throws HarnessException {
		
		// Create a new cos in the Admin Console using SOAP
		CosItem cos = new CosItem();
		String cosName=cos.getName();
	
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + cosName + "</name>"
				+		"</CreateCosRequest>");
	
		app.zPageSearchResults.setType(PageSearchResults.TypeOfObject.COS);

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(cosName);
	
	
		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on cos
		app.zPageSearchResults.zListItem(Action.A_RIGHTCLICK, cos.getName());
		app.zPageSearchResults.setType(PageSearchResults.TypeOfObject.COS);

		// Click on Edit button
		FormEditCos form = (FormEditCos) app.zPageSearchResults.zToolbarPressButton(Button.B_TREE_EDIT);
		
		
		//Edit the name.
		String editedName = "editedCos_" + ZimbraSeleniumProperties.getUniqueString();
		form.setName(editedName);
		
		//Submit the form.
		form.zSubmit();
		
		// Verify the cos exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
		"<GetCosRequest xmlns='urn:zimbraAdmin'>" +
		                     "<cos by='name'>"+editedName+"</cos>"+
		                   "</GetCosRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos", 1);
		ZAssert.assertNotNull(response, "Verify the cos is edited successfully");
	}
	
	/**
	 * Testcase : Edit cos - Two Factor Authentication
	 * Steps :
	 * 1. Create an cos using SOAP.
	 * 2. Edit the two factor authentication attributes using UI Right Click.
	 * 3. Verify two factor authentication attributes are changed using SOAP.
	 * @throws HarnessException
	 */
	@Test(	description = "Edit cos - Two Factor Authentication",
			groups = { "smoke" })
			public void EditCos_05() throws HarnessException {
				
		// Create a new cos in the Admin Console using SOAP
		CosItem cos = new CosItem();
		String cosName=cos.getName();
	
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + cosName + "</name>"
				+		"</CreateCosRequest>");
	
		app.zPageSearchResults.setType(PageSearchResults.TypeOfObject.COS);

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(cosName);
	
	
		// Click search
		app.zPageSearchResults.zToolbarPressPulldown(Button.B_SEARCH_TYPE, Button.O_CLASS_OF_SERVICE);
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on cos
		app.zPageSearchResults.zListItem(Action.A_RIGHTCLICK, cos.getName());
		app.zPageSearchResults.setType(PageSearchResults.TypeOfObject.COS);

		// Click on Edit -> Advanced button
		FormEditCos form = (FormEditCos) app.zPageSearchResults.zToolbarPressButton(Button.B_TREE_EDIT);
		form.zClickAt(PageEditCOS.Locators.ADVANCED,"");
		
		// Check "Enable two-factor authentication"
		app.zPageEditCOS.sClickAt(Locators.zEnableTwoFactorAuth,"");
		
		// Check "Require two-step authentication"
		app.zPageEditCOS.sClickAt(Locators.zRequiredTwoFactorAuth,"");

		// Check "Number of one-time codes to generate:"
		app.zPageEditCOS.sType(Locators.zTwoFactorAuthNumScratchCodes,"5");
		
		// Uncheck "Enable application passcodes"
		app.zPageEditCOS.sClickAt(Locators.zEnableApplicationPasscodes,"");
				
		// Submit the form
		form.zSubmit();
				
		// Verify the enable two-factor authentication is set to true
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
		"<GetCosRequest xmlns='urn:zimbraAdmin'>" +
		                     "<cos by='name'>"+cosName+"</cos>"+
		                   "</GetCosRequest>");
		Element response1 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos/admin:a[@n='zimbraFeatureTwoFactorAuthAvailable']", 1);
		ZAssert.assertNotNull(response1, "Verify the COS is edited successfully");
		ZAssert.assertStringContains(response1.toString(),"TRUE", "Verify the Enable two-factor authentication is set to true");
		
		// Verify the require two-step authentication is set to true
		Element response2 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos/admin:a[@n='zimbraFeatureTwoFactorAuthRequired']", 1);
		ZAssert.assertNotNull(response2, "Verify the COS is edited successfully");
		ZAssert.assertStringContains(response2.toString(),"TRUE", " Verify the Require two-step authentication is set to true");
		
		// Verify the number of one-time codes to generate is set to 5
		Element response3 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos/admin:a[@n='zimbraTwoFactorAuthNumScratchCodes']", 1); 
		ZAssert.assertNotNull(response3, "Verify the COS is edited successfully");
		ZAssert.assertStringContains(response3.toString(),"5", "Verify the Number of one-time codes to generate is set to 5");
		
		// Verify the enable application passcodes is set to false
		Element response4 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos/admin:a[@n='zimbraFeatureAppSpecificPasswordsEnabled']", 1);
		ZAssert.assertNotNull(response4, "Verify the COS is edited successfully");
		ZAssert.assertStringContains(response4.toString(),"FALSE", "Verify the Enable application passcodes is set to false");
	}
}
