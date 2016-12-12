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
package com.zimbra.qa.selenium.projects.admin.tests.globalsettings.MTA;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Checkbox;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageMTA.Locators;

public class DisableArchiving extends AdminCommonTest {

	public DisableArchiving() {
		logger.info("New "+ DisableArchiving.class.getCanonicalName());
		super.startingPage = app.zPageManageGlobalSettings;
	}

	/**
	 * Testcase : Verify administrator should be able to enable archiving at the global level
	 * Steps :
	 * 1. Login to admin console
	 * 2. Go to Global settings > MTA
	 * 3. Go to Archiving section > enable archiving
	 * 4. Verify archiving is enabled
	 * @throws HarnessException
	 */
	@Test( description = "Verify administrator should be able to enable archiving at the global level",
			groups = { "functional", "network" })

	public void DisableArchiving_01() throws HarnessException {

		// Navigating to MTA configuration page to make MTA configuration changes through Admin UI 
		app.zPageManageMTA.zNavigateTo();

		//Un-check enable archiving under under Archiving Configuration
		app.zPageManageMTA.zCheckboxSet(Checkbox.C_MTA_ENABLE_ARCHIVING, false);			
		
		//Save the changes done
		app.zPageManageMTA.zToolbarPressButton(Button.B_SAVE);
	
		//Verify through UI that the MTA configuration change made above is present  
		app.zPageManageMTA.sRefresh();
		app.zPageManageMTA.zWaitTillElementPresent(Locators.HOME);
		app.zPageManageGlobalSettings.zNavigateTo();
		app.zPageManageMTA.zNavigateTo();		

		Boolean checkValue= app.zPageManageMTA.sIsChecked(Locators.ENABLE_ARCHIVING);
		ZAssert.assertFalse(checkValue, "MTA archiving changes reflected in SOAP response");;
		
		//Verify through SOAP that the MTA configuration change made above is present
		String status = "FALSE";
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<GetConfigRequest xmlns='urn:zimbraAdmin'>"
						+		"<a n='zimbraArchiveEnabled'/>"
						+	"</GetConfigRequest>");

		Element[] zimbraArchiveEnabledValues = ZimbraAdminAccount.GlobalAdmin().soapSelectNodes("//admin:a[@n='zimbraArchiveEnabled']");

		//Verify that MTA configuration has changed
		boolean value = false;
		for(Element e : zimbraArchiveEnabledValues ) {
			if (e.getText().contains(status)) {
				value = true;
				break;
			}
		}
		ZAssert.assertTrue(value, "MTA archiving changes are reflected in SOAP response");
		
	}

}
