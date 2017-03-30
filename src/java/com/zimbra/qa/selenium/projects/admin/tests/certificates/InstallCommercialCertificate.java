/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.certificates;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.FileItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.WizardInstallCertificate;
import com.zimbra.qa.selenium.projects.admin.ui.WizardInstallCertificate.Locators;

public class InstallCommercialCertificate extends AdminCommonTest {

	public InstallCommercialCertificate() {
		logger.info("New " + InstallCommercialCertificate.class.getCanonicalName());

		// All tests start at the "Certificate" page
		super.startingPage = app.zPageManageCertificates;
	}

	/**
	 * Testcase : Install Commercial Certificate
	 * 1. Install Commercial Certificate from GUI
	 * 2. Verify certificate using SOAP.
	 * @throws HarnessException
	 */
	@Test(	description = "Install Commercial Certificate",
			groups = { "smoke", "L1" })

	public void InstallCommercialCertificate_01() throws HarnessException {
		
		String hostname = ConfigProperties.getStringProperty("server.host");

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<GetServerRequest xmlns='urn:zimbraAdmin'>"
								+	"<server by='name'>" + hostname + "</server>"
								+		"</GetServerRequest>");
		String id = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:server", "id");

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<GenCSRRequest xmlns='urn:zimbraAdmin' type='comm' digest='sha256' keysize='2048' new='1' server= '"+id+"'>"
								+		"<SubjectAltName>" + hostname + "</SubjectAltName>"
								+		"<CN>*.zimbra.com</CN>"
								+		"<C>US</C>"
								+		"<ST>Texas</ST>"
								+		"<L>Frisco</L>"
								+		"<O>Zimbra</O>"
								+		"<OU>Zimbra</OU>"
								+	"</GenCSRRequest>");

		// Create file item
		final String fileName = "commercial.crt";
		final String rootCertfileName = "commercial_ca.crt";
		final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\private\\certificates\\" + fileName;
		final String rootCertfilePath = ConfigProperties.getBaseDirectory() + "\\data\\private\\certificates\\" + rootCertfileName;
		String issuer = "DigiCert";
		FileItem fileItem = new FileItem(filePath);

		// Click on server
		app.zPageManageCertificates.zListItem(Action.A_LEFTCLICK, hostname);

		// Click on install certificate
		WizardInstallCertificate wizard= (WizardInstallCertificate)app.zPageManageCertificates.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.B_INSTALL_CERTIFICATE);
		
		// Click on next
		wizard.sClickAt(Locators.NEXT_BUTTON,"");
		SleepUtil.sleepMedium();

		// Select install certificate option
		wizard.sClickAt(Locators.INSTALL_COMMERCIAL_CERTIFICATE,"");
		SleepUtil.sleepMedium();

		// Click on next
		wizard.sClickAt(Locators.NEXT_BUTTON,"");
		SleepUtil.sleepMedium();

		// Click on next
		wizard.sClickAt(Locators.NEXT_BUTTON,"");
		SleepUtil.sleepMedium();

		// Click on Upload File button in the toolbar
		app.zPageManageCertificates.zToolbarPressButton(Button.B_UPLOAD_CERTIFICATE, fileItem);

		// Upload certificate
		zUpload(filePath);

		// Click on Upload file button in the toolbar
		app.zPageManageCertificates.zToolbarPressButton(Button.B_UPLOAD_ROOT_CERTIFICATE, fileItem);

		// Upload root certificate
		zUpload(rootCertfilePath);

		// Click on next
		wizard.sClickAt(Locators.NEXT_BUTTON,"");
		SleepUtil.sleepMedium();

		// Click on install
		wizard.sClickAt(Locators.INSTALL_BUTTON,"");
		SleepUtil.sleepVeryVeryLong();

		// Verify certificate installed correctly
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCertRequest server='"+id+"' xmlns='urn:zimbraAdmin' type='all'>"
						+		"</GetCertRequest>");

		Element cert_issuer =  ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCertResponse/admin:cert/admin:issuer", 1); 
		ZAssert.assertStringContains(cert_issuer.toString(), issuer, "Verify issuer is correct!");

		String cert_server = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:cert", "server");
		ZAssert.assertStringContains(cert_server, hostname, "Verify server is correct!");
	}

}
