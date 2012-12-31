package com.zimbra.qa.selenium.projects.admin.tests.cos;

import java.util.List;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.items.CosItem;
import com.zimbra.qa.selenium.projects.admin.ui.DialogForDeleteOperation;
import com.zimbra.qa.selenium.projects.admin.ui.DialogForDeleteOperationCos;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;

public class DeleteCos extends AdminCommonTest {
	public DeleteCos() {
		logger.info("New " + DeleteCos.class.getCanonicalName());

		//All tests starts at "Cos" page
		super.startingPage=app.zPageManageCOS;
	}

	/**
	 * Testcase : Verify delete cos operation -- Manage Cos view.
	 * Steps :
	 * 1. Create a cos using SOAP.
	 * 2. Search cos created in Step-1.
	 * 3. Select delete from gear box menu
	 * 4. Verify cos is deleted using SOAP
	 * @throws HarnessException
	 */
	@Test(	description = "Verify delete cos operation -- Manage cos view",
			groups = { "smoke" })
			public void DeleteCos_01() throws HarnessException {

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
		

		// Click on Delete button
		DialogForDeleteOperationCos dialog = (DialogForDeleteOperationCos) app.zPageManageCOS.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zClickButton(Button.B_YES);

		// Verify the cos exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
		"<GetCosRequest xmlns='urn:zimbraAdmin'>" +
		                     "<cos by='name'>"+cosName+"</cos>"+
		                   "</GetCosRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos", 1);
		ZAssert.assertNull(response, "Verify the cos is edited successfully");	
	}
	
	
	/**
	 * Testcase : Verify delete cos operation -- Manage COS list view/Right click menu.
	 * Steps :
	 * 1. Create a cos using SOAP.
	 * 2. Search cos created in Step-1.
	 * 3. Select delete from right click menu
	 * 4. Verify cos is deleted using SOAP
	 * @throws HarnessException
	 */
	@Test(	description = "Verify delete cos operation -- Manage COS list view/Right click menu",
			groups = { "functional" })
			public void DeleteCos_02() throws HarnessException {

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
		app.zPageManageCOS.zListItem(Action.A_RIGHTCLICK, cosName);
		

		// Click on Delete button
		DialogForDeleteOperationCos dialog = (DialogForDeleteOperationCos) app.zPageManageCOS.zToolbarPressButton(Button.B_TREE_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zClickButton(Button.B_YES);

		// Verify the cos exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
		"<GetCosRequest xmlns='urn:zimbraAdmin'>" +
		                     "<cos by='name'>"+cosName+"</cos>"+
		                   "</GetCosRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos", 1);
		ZAssert.assertNull(response, "Verify the cos is edited successfully");	
	}
	
	/**
	 * Testcase : Verify delete cos operation -- Search list view.
	 * Steps :
	 * 1. Create a cos using SOAP.
	 * 2. Search cos created in Step-1.
	 * 3. Select delete from gear box menu
	 * 4. Verify cos is deleted using SOAP
	 * @throws HarnessException
	 */
	@Test(	description = "Verify delete cos operation -- Search list view",
			groups = { "functional" })
			public void DeleteCos_03() throws HarnessException {

		// Create a new cos in the Admin Console using SOAP
		CosItem cos = new CosItem();
		String cosName=cos.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + cosName + "</name>"
				+		"</CreateCosRequest>");

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(cosName);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on cos to be deleted.
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, cos.getName());

		// Click on Delete button
		DialogForDeleteOperation dialog = (DialogForDeleteOperation) app.zPageSearchResults.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zClickButton(Button.B_YES);

		// Click Ok on "Delete Items" dialog
		dialog.zClickButton(Button.B_OK);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the cos list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for cos "+ cosName + " found: "+ a.getGEmailAddress());
			if ( cosName.equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNull(found, "Verify the cos is deleted successfully");
	}
	
	/**
	 * Testcase : Verify delete cos operation -- Search list view/Right click menu.
	 * Steps :
	 * 1. Create a cos using SOAP.
	 * 2. Search cos created in Step-1.
	 * 3. Select delete from right click menu
	 * 4. Verify cos is deleted using SOAP
	 * @throws HarnessException
	 */
	@Test(	description = "Verify delete cos operation -- Search list view/Right click menu",
			groups = { "functional" })
			public void DeleteCos_04() throws HarnessException {

		// Create a new cos in the Admin Console using SOAP
		CosItem cos = new CosItem();
		String cosName=cos.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + cosName + "</name>"
				+		"</CreateCosRequest>");

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(cosName);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Right Click on cos to be deleted.
		app.zPageSearchResults.zListItem(Action.A_RIGHTCLICK, cos.getName());

		// Click on Delete button
		DialogForDeleteOperation dialog = (DialogForDeleteOperation) app.zPageSearchResults.zToolbarPressButton(Button.B_TREE_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zClickButton(Button.B_YES);

		// Click Ok on "Delete Items" dialog
		dialog.zClickButton(Button.B_OK);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the cos list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for cos "+ cosName + " found: "+ a.getGEmailAddress());
			if ( cosName.equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNull(found, "Verify the cos is deleted successfully");
	}
}
