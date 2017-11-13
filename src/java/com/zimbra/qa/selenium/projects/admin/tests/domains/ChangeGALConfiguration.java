/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.domains;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.DomainItem;
import com.zimbra.qa.selenium.projects.admin.items.GALItem;
import com.zimbra.qa.selenium.projects.admin.items.GALItem.GALMode;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain.Locators;
import com.zimbra.qa.selenium.projects.admin.ui.WizardConfigureGAL;

public class ChangeGALConfiguration extends AdminCommonTest {

	public ChangeGALConfiguration() {
		logger.info("New " + ChangeGALConfiguration.class.getName());
		super.startingPage=app.zPageManageDomains;
	}

	/**
	 * Testcase : Change the GAL mode of a domain from Internal to Both
	 * Steps :
	 * 1. Create a domain using SOAP with GAL mode as Internal
	 * 2. Select the domain and Configure its GAL mode to Both
	 * 3. Verify that an external data source is created after changing GAL mode. 
	 * @throws HarnessException
	 */
	@Bugs (ids = "96777")
	@Test (description = "Verify GAL Configuration after chnaging GAL mode of a domain from internal to both",
	groups = { "functional", "L2" })
	public void ChangeGALConfiguration_01() throws HarnessException {

		//External data source name
		String extDataSrc = "extDataSource";

		// Create a new domain using SOAP
		DomainItem domain = new DomainItem();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + domain.getName() + "</name>"
						+		"</CreateDomainRequest>");


		// Refresh the domain list
		app.zPageManageDomains.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Select the domain for which GAL mode needs to changed.
		app.zPageManageDomains.zListItem(Action.A_LEFTCLICK, domain.getName());

		//Configure GAL for the domain
		//Open GAL configuration wizard
		WizardConfigureGAL wizard = 
				(WizardConfigureGAL)app.zPageManageDomains.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_CONFIGURE_GAL);

		//Create a GAL item for changing GAL mode
		GALItem gItem = new GALItem();

		//Set the different values of the gal item
		gItem.setCurrentGALMode(GALMode.Internal);
		gItem.setNewGALMode(GALMode.Both);
		gItem.setDataSourceName(extDataSrc);
		gItem.setPollingIntervalDays("1");

		//Fill the required fields
		wizard.zCompleteWizard(gItem);

		//Verification through IU

		//Refresh the admin console
		app.zPageManageDomains.zRefreshMainUI();
		app.zPageMain.zWaitTillElementPresent(Locators.zLogoffDropDownArrow);
		app.zPageManageDomains.zNavigateTo();

		// Double click domain to open it
		app.zPageManageDomains.zListItem(Action.A_DOUBLECLICK, domain.getName());

		//Open GAL page
		app.zPageManageCofigureGAL.zNavigateTo();

		//Verify GAL configuration through UI
		ZAssert.assertTrue(app.zPageManageCofigureGAL.zVerifyGALConfigData(gItem), "Gal is not configured successfully, Displayed data is not correct!");

		//Verify GAL configuration and data sources through SOAP
		//Get galsync account id
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>galsync@"+ domain.getName()  +"</account>"
						+		"</GetAccountRequest>");
		String accountId = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:account", "id");

		//Get data sources associated with the galsync account
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDataSourcesRequest xmlns='urn:zimbraAdmin'>"
						+		"<id>" + accountId + "</id>"
						+	 "</GetDataSourcesRequest>");
		//Data sources name and type
		String internalDataSrcName = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:dataSource[1]/admin:a[@n='zimbraDataSourceName']", null);
		String internalGalType = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:dataSource[1]/admin:a[@n='zimbraGalType']", null);
		String externalDataSrcName = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:dataSource[2]/admin:a[@n='zimbraDataSourceName']", null);
		String externalGalType = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:dataSource[2]/admin:a[@n='zimbraGalType']", null);

		//Verification
		ZAssert.assertEquals(internalDataSrcName, "zimbra", "Internal GAL data source name is not correct!");
		ZAssert.assertEquals(internalGalType, "zimbra", "Internal GAL type is not correct!");
		ZAssert.assertEquals(externalDataSrcName, extDataSrc, "External GAL data source name is not correct!");
		ZAssert.assertEquals(externalGalType, "ldap", "External GAL type is not correct!");
	}

	@Bugs (ids = "96777")
	@Test (description = "Verify GAL Configuration after chnaging GAL mode of a domain from external to both",
	groups = { "functional", "L2" })
	public void ChangeGALConfiguration_02() throws HarnessException {

		//data source name
		String extDataSrc = "extDataSource";
		String intDataSrc = "intDataSource";

		// Create a new domain with external GAL mode using SOAP
		DomainItem domain = new DomainItem();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
						+		"<name>" + domain.getName() + "</name>"
						+		"<a n='zimbraGalMode'>ldap</a>"
						+		"<a n='zimbraGalLdapURL'>ldap://10.15.6.1:3268</a>"
						+		"<a n='zimbraGalLdapSearchBase'>dc=exchange2010,dc=lab</a>"
						+		"<a n='zimbraGalLdapBindDn'>administrator</a>"
						+		"<a n='zimbraGalLdapBindPassword'>z1mbr4@123</a>"
						+		"<a n='zimbraGalLdapFilter'>dc=exchange2010,dc=lab</a>"
						+		"<a n='zimbraGalAutoCompleteLdapFilter'>adAutoComplete</a>"
						+		"<a n='zimbraAuthMech'>zimbra</a>"
						+		"<a n='zimbraDomainStatus'>active</a>"			
						+	"</CreateDomainRequest>");

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateGalSyncAccountRequest xmlns='urn:zimbraAdmin'"
								+	" name='" + extDataSrc + "' folder='_" + extDataSrc + "' type='"+ "ldap" + "' domain='" + domain.getName() + "' "
									+ "server='" + ConfigProperties.getStringProperty("server.host") + "'>"
						+		"<account by='name'>galsync@"+ domain.getName()  +"</account>"
						+		"<a n='zimbraDataSourcePollingInterval'>1m</a>"		
						+	"</CreateGalSyncAccountRequest>");


		// Refresh the domain list
		app.zPageManageDomains.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Select the domain for which GAL mode needs to changed.
		app.zPageManageDomains.zListItem(Action.A_LEFTCLICK, domain.getName());

		//Configure GAL for the domain
		//Open GAL configuration wizard
		WizardConfigureGAL wizard = 
				(WizardConfigureGAL)app.zPageManageDomains.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_CONFIGURE_GAL);

		//Create a GAL item for changing GAL mode
		GALItem gItem = new GALItem();

		//Set the different values of the gal item
		gItem.setCurrentGALMode(GALMode.External);
		gItem.setNewGALMode(GALMode.Both);
		gItem.setDataSourceName(intDataSrc);
		gItem.setPollingIntervalDays("1");

		//Fill the required fields
		wizard.zCompleteWizard(gItem);

		//Verification through IU

		//Refresh the admin console
		app.zPageManageDomains.zRefreshMainUI();
		app.zPageMain.zWaitTillElementPresent(Locators.zLogoffDropDownArrow);
		app.zPageManageDomains.zNavigateTo();

		// Double click domain to open it
		app.zPageManageDomains.zListItem(Action.A_DOUBLECLICK, domain.getName());

		//Open GAL page
		app.zPageManageCofigureGAL.zNavigateTo();

		//Verify GAL configuration through UI
		ZAssert.assertTrue(app.zPageManageCofigureGAL.zVerifyGALConfigData(gItem), "Gal is not configured successfully, Displayed data is not correct!");

		//Verify GAL configuration and data sources through SOAP
		//Get galsync account id
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>galsync@"+ domain.getName()  +"</account>"
						+		"</GetAccountRequest>");
		String accountId = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:account", "id");

		//Get data sources associated with the galsync account
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDataSourcesRequest xmlns='urn:zimbraAdmin'>"
						+		"<id>" + accountId + "</id>"
						+	 "</GetDataSourcesRequest>");
		//Data sources name and type
		String internalDataSrcName = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:dataSource[2]/admin:a[@n='zimbraDataSourceName']", null);
		String internalGalType = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:dataSource[2]/admin:a[@n='zimbraGalType']", null);
		String externalDataSrcName = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:dataSource[1]/admin:a[@n='zimbraDataSourceName']", null);
		String externalGalType = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:dataSource[1]/admin:a[@n='zimbraGalType']", null);

		//Verification
		ZAssert.assertEquals(internalDataSrcName, intDataSrc, "Internal GAL data source name is not correct!");
		ZAssert.assertEquals(internalGalType, "zimbra", "Internal GAL type is not correct!");
		ZAssert.assertEquals(externalDataSrcName, extDataSrc, "External GAL data source name is not correct!");
		ZAssert.assertEquals(externalGalType, "ldap", "External GAL type is not correct!");
	}
}
