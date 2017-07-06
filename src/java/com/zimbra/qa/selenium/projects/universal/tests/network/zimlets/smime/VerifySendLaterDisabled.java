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
package com.zimbra.qa.selenium.projects.universal.tests.network.zimlets.smime;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.FormMailNew;

public class VerifySendLaterDisabled extends PrefGroupMailByMessageTest {

	public VerifySendLaterDisabled() {
		
		logger.info("New "+ VerifySendLaterDisabled.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraFeatureMailSendLaterEnabled", "TRUE");
		super.startingAccountPreferences.put("zimbraFeatureSMIMEEnabled", "TRUE");
		
	}
	
    @Test( description = "Verify Send later is disabled when smime security preference is set to 'Sign Only' and 'Sign & Encrypt'",
            groups = { "functional", "L2", "network" })

    public void VerifySendLaterDisabledForSigningOptions_01() throws HarnessException {
		
	        String[] signingOptions = { "sign", "both" };

	        for (int i=0; i<=1; i++) {

	          logger.info("Modify S/MIME security preference to ("+ signingOptions[i] +")");
	          app.zGetActiveAccount().soapSend(
	               "<ModifyPropertiesRequest xmlns='urn:zimbraAccount'>" +
	               "<prop xmlns='' zimlet='com_zimbra_securemail' name='MAIL_SECURITY_PREF'>" + signingOptions[i] + "</prop>" +
	          "</ModifyPropertiesRequest>");  
	          
	  		  //Refresh for changes
	          app.zPageMain.sRefresh();

	          // Open the new mail form
	          FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
	          ZAssert.assertNotNull(mailform, "Verify the new form opened");

	          //Verify that send later is disabled
	          ZAssert.assertTrue(mailform.zVerifyDisabledSendLater(), "Verify send later option is disabled for " + signingOptions[i] + " security option");
	        }		
	}

	@Test( description = "Verify Send later is enabled when smime security preference is set to 'Do not sign or encrypt'",
			groups = { "functional", "L2", "network" })
	public void VerifySendLaterEnabledWhenDontSign_02() throws HarnessException {
		
		//Set Smime pref to do not sign or encrypt only
		app.zGetActiveAccount().soapSend(
				"<ModifyPropertiesRequest xmlns='urn:zimbraAccount'>" +
				"<prop xmlns='' zimlet='com_zimbra_securemail' name='MAIL_SECURITY_PREF'>none</prop>" +
                "</ModifyPropertiesRequest>");

		//Refresh for changes
		app.zPageMain.sRefresh();
		
		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		
		//Verify that send later is enabled
		ZAssert.assertFalse(mailform.zVerifyDisabledSendLater(), "Verify that send later is enabled");
		
	}

	@Test( description = "Verify Send later is disabled when smime security preference is set to 'sign only'",
			groups = { "functional", "L2", "network" })
	public void VerifySendLaterEnabledWithSecureEmailOptions_03() throws HarnessException {
				
		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		
		//Choose sign only from the secure email drop-down
		mailform.zToolbarPressPulldown(Button.B_SECURE_EMAIL, Button.O_SIGN);
		
		//Verify that send later is disabled
		ZAssert.assertTrue(mailform.zVerifyDisabledSendLater(), "Verify that send later is disabled");

		//Choose sign and encrypt from the secure email drop-down
		mailform.zToolbarPressPulldown(Button.B_SECURE_EMAIL, Button.O_SIGN_AND_ENCRYPT);
		
		//Verify that send later is disabled
		ZAssert.assertTrue(mailform.zVerifyDisabledSendLater(), "Verify that send later is disabled");
		
		//Choose don't sign from the secure email dropd-own
		mailform.zToolbarPressPulldown(Button.B_SECURE_EMAIL, Button.O_DONT_SIGN);
		
		//Verify that send later is enabled
		ZAssert.assertFalse(mailform.zVerifyDisabledSendLater(), "Verify that send later is enabled");
		
	}
	
}
