/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.globalsettings.MTA;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Checkbox;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.pages.PageManageMTA.Locators;

public class CheckMTASettings extends AdminCore {

	public CheckMTASettings() {
		logger.info("New "+ CheckMTASettings.class.getCanonicalName());
	}


	/**
	 * Test case : Verify that MTA configuration changes are reflected on Admin UI as well as on CLI
	 * Steps :
	 * 1. Go to configure >> global settings >> MTA
	 * 2. Get the existing configuration of MTA
	 * 3. Make some changes in the MTA configuration through Admin UI and SOAP request
	 * 4. Get the MTA configuration using soap and check that all the changes are reflected.
	 * 5. Check that the changes are also present in ADMIn UI
	 * @throws HarnessException
	 */

	@Bugs (ids = "104512,106769")
	@Test (description = "Verify MTA restriction values after changing some MTA configuration through Command line and Admin console",
			groups = { "functional", "L2" })

	public void CheckMTASettings_01() throws HarnessException {

		// MTA restriction value to be changed though SOAP
		String MTARestriction1 = "check_client_access lmdb:/opt/zimbra/conf/postfix_rbl_override";
		String MTARestriction2 = "reject_unknown_client_hostname";

		try {

			// Modify the MTA restriction values
			ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<ModifyConfigRequest xmlns='urn:zimbraAdmin'>"
							+		"<a n='+zimbraMtaRestriction'>"+ MTARestriction1 +"</a>"
							+	"</ModifyConfigRequest>");

			ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<GetConfigRequest xmlns='urn:zimbraAdmin'>"
							+		"<a n='zimbraMtaRestriction'/>"
							+	"</GetConfigRequest>");

			Element[] restrictionValues = ZimbraAdminAccount.GlobalAdmin().soapSelectNodes("//admin:a[@n='zimbraMtaRestriction']");

			// Verify that MTA configuration has changed
			boolean present = false;
			for(Element e : restrictionValues ) {
				if (e.getText().contains(MTARestriction1)) {
					present = true;
					break;
				}
			}
			ZAssert.assertTrue(present, "MTA restriction changes are not reflected in SOAP response");

			// Navigating to MTA configuration page to make MTA configuration changes through Admin UI
			app.zPageManageGlobalSettings.zNavigateTo();
			app.zPageManageMTA.zNavigateTo();
			SleepUtil.sleepMedium();

			// Select client's IP address under under DNS check (an MTA restriction)
			app.zPageManageMTA.zCheckboxSet(Checkbox.C_MTA_CLIENTS_IP_ADDRESS, true);

			// Save the changes done
			app.zPageManageMTA.zToolbarPressButton(Button.B_SAVE);

			// Verify through UI that the MTA configuration change made above is present
			app.zPageManageMTA.zRefreshMainUI();
			app.zPageMain.zWaitForActive();
			app.zPageManageGlobalSettings.zNavigateTo();
			app.zPageManageMTA.zNavigateTo();

			ZAssert.assertTrue(app.zPageManageMTA.sIsElementPresent(Locators.CLIENTS_IP_ADDRESS + "[checked='']"),"MTA restriction changes are not reflected in Admin UI after saving and refresh.");

			// Verify through SOAP that the MTA configuration change made above is present
			ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<GetConfigRequest xmlns='urn:zimbraAdmin'>"
							+		"<a n='zimbraMtaRestriction'/>"
							+	"</GetConfigRequest>");
			restrictionValues = ZimbraAdminAccount.GlobalAdmin().soapSelectNodes("//admin:a[@n='zimbraMtaRestriction']");

			// Verify that MTA restriction contains the newly added as well as the earlier added restriction

			boolean value1 = false;
			boolean value2 = false;
			for(Element e : restrictionValues ) {
				if (e.getText().contains(MTARestriction1)) {
					value1 = true;
					break;
				}
			}
			for(Element e : restrictionValues ) {
				if (e.getText().contains(MTARestriction2)) {
					value2 = true;
					break;
				}
			}

			ZAssert.assertTrue((value1 && value2), "MTA restriction changes are not reflected in SOAP response");

			// Change other two MTA setting(Enable milter server and disable TLS authentication) through Admin UI
			app.zPageManageMTA.zCheckboxSet(Checkbox.C_MTA_ENABLE_MILTER_SERVER, true);
			app.zPageManageMTA.zCheckboxSet(Checkbox.C_MTA_TLS_AUTHENTICATION_ONLY, false);

			// Save the changes done
			app.zPageManageMTA.zToolbarPressButton(Button.B_SAVE);

			// Verify through SOAP that these MTA configuration changes are reflected
			ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<GetConfigRequest xmlns='urn:zimbraAdmin'>"
							+		"<a n='zimbraMtaRestriction'/>"
							+	"</GetConfigRequest>");

			restrictionValues = ZimbraAdminAccount.GlobalAdmin().soapSelectNodes("//admin:a[@n='zimbraMtaRestriction']");

			value1 = false;
			value2 = false;
			for(Element e : restrictionValues ) {
				if (e.getText().contains(MTARestriction1)) {
					value1 = true;
					break;
				}
			}
			for(Element e : restrictionValues ) {
				if (e.getText().contains(MTARestriction2)) {
					value2 = true;
					break;
				}
			}

			ZAssert.assertTrue((value1 && value2), "MTA restrictions are not present in SOAP response after the MTA configuration is changed from Admin UI");

			// Verify that milter setting is reflected in soap response
			ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<GetConfigRequest xmlns='urn:zimbraAdmin'>"
							+		"<a n='zimbraMilterServerEnabled'/>"
							+	"</GetConfigRequest>");
			String milterSetting = ZimbraAdminAccount.GlobalAdmin().soapSelectValue("//admin:a[@n='zimbraMilterServerEnabled']", null);
			ZAssert.assertStringContains(milterSetting, "TRUE", "Milter setting is not reflected in soap response");

			// Verify that TLS Authentication only setting is reflected in soap response
			ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<GetConfigRequest xmlns='urn:zimbraAdmin'>"
							+		"<a n='zimbraMtaTlsAuthOnly'/>"
							+	"</GetConfigRequest>");
			String TLSAuthentication = ZimbraAdminAccount.GlobalAdmin().soapSelectValue("//admin:a[@n='zimbraMtaTlsAuthOnly']", null);
			ZAssert.assertStringContains(TLSAuthentication, "FALSE", "TLS authentication only setting is not reflected in soap response");

		} finally {

			// Restore the MTA restriction values to values there were before the test
			ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<ModifyConfigRequest xmlns='urn:zimbraAdmin'>"
							+		"<a n='-zimbraMtaRestriction'>"+ MTARestriction1 +"</a>"
							+		"<a n='-zimbraMtaRestriction'>"+ MTARestriction2 +"</a>"
							+		"<a n='zimbraMilterServerEnabled'>FALSE</a>"
							+		"<a n='zimbraMtaTlsAuthOnly'>TRUE</a>"
							+	"</ModifyConfigRequest>");
		}
	}
}