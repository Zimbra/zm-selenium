/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.pages;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.AbsWizard;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.admin.items.CertificateItem;

/**
 * @author Pallavi Khairnar
 *
 */
public class WizardInstallCertificate extends AbsWizard {

	public static class Locators {
		public static final String INSTALL_SELF_SIGNED_CERTIFICATE = "css=input[id='zdlgv__UNDEFINE_self']";
		public static final String GENERATE_CSR = "css=input[id='zdlgv__UNDEFINE_csr']";
		public static final String INSTALL_COMMERCIAL_CERTIFICATE = "css=input[id='zdlgv__UNDEFINE_comm']";
		public static final String COUNTRY_NAME = "css=input[id$='UNDEFINE_C']";
		public static final String STATE = "css=input[id$='UNDEFINE_ST']";
		public static final String CITY = "css=input[id$='UNDEFINE_L']";
		public static final String ORGANIZATION_NAME = "css=input[id$='UNDEFINE_O']";
		public static final String ORGANIZATION_UNIT = "css=input[id$='UNDEFINE_OU']";
		public static final String CERTIFICATE_VALIDATION_DAYS = "css=input[id='zdlgv__UNDEFINE_validation_days']";
		public static final String INSTALL_BUTTON = "css=td[id$='_button13_title']";
		public static final String REPLACE_THE_EXISTING_CSR = "css=input[id$='_force_new_csr']";
		public static final String NEXT_BUTTON = "css=td[id$='_button12_title']";
		public static final String COMMON_NAME = "css=input[id='zdlgv__UNDEFINE_CN']";
		public static final String DOWNLOAD_THE_CSR = "css=div[id='zdlgv__UNDEFINE_target_server_7'] a";

	}

	public WizardInstallCertificate(AbsTab page) {
		super(page);
	}

	public String certificateAction = "";

	public String getCertificateAction() {
		return certificateAction;
	}

	public void setCertificateAction(String certificateAction) throws HarnessException {

		if (certificateAction.equals(Locators.INSTALL_SELF_SIGNED_CERTIFICATE)) {
			certificateAction = Locators.INSTALL_SELF_SIGNED_CERTIFICATE;
		} else if (certificateAction.equals(Locators.GENERATE_CSR)) {
			certificateAction = Locators.GENERATE_CSR;
		} else if (certificateAction.equals(Locators.INSTALL_COMMERCIAL_CERTIFICATE)) {
			certificateAction = Locators.INSTALL_COMMERCIAL_CERTIFICATE;
			sClick(certificateAction);
			sClickAt(Locators.NEXT_BUTTON, "");
		}
		this.certificateAction = certificateAction;
	}

	@Override
	public IItem zCompleteWizard(IItem item) throws HarnessException {
		if (!(item instanceof CertificateItem))
			throw new HarnessException("item must be an CertificateItem, was " + item.getClass().getCanonicalName());

		CertificateItem cert = (CertificateItem) item;
		String countryName = cert.getCountryName();
		String state = cert.getStateName();
		String city = cert.getCityName();
		String organizationName = cert.getOrganizationName();
		String organizationUnit = cert.getOrganizationUnit();
		String commonName = cert.getCommonName();

		SleepUtil.sleepMedium();
		sClickAt(Locators.NEXT_BUTTON, "");

		if (certificateAction.equals(Locators.INSTALL_SELF_SIGNED_CERTIFICATE)) {
			SleepUtil.sleepMedium();
			sClickAt(Locators.NEXT_BUTTON, "");
			SleepUtil.sleepMedium();
			sCheck(Locators.REPLACE_THE_EXISTING_CSR);
			zType(Locators.COUNTRY_NAME, countryName);
			zType(Locators.STATE, state);
			zType(Locators.CITY, city);
			zType(Locators.ORGANIZATION_NAME, organizationName);
			zType(Locators.ORGANIZATION_UNIT, organizationUnit);
			sClickAt(Locators.NEXT_BUTTON, "");
			SleepUtil.sleepSmall();
			sClickAt(Locators.INSTALL_BUTTON, "");

		} else if (certificateAction.equals(Locators.GENERATE_CSR)) {
			sClickAt(Locators.GENERATE_CSR, "");
			SleepUtil.sleepMedium();
			sClickAt(Locators.NEXT_BUTTON, "");
			SleepUtil.sleepMedium();
			sCheck(Locators.REPLACE_THE_EXISTING_CSR);
			SleepUtil.sleepMedium();

			zType(Locators.COMMON_NAME, commonName);
			SleepUtil.sleepSmall();
			zType(Locators.COUNTRY_NAME, countryName);
			SleepUtil.sleepSmall();
			zType(Locators.STATE, state);
			SleepUtil.sleepSmall();
			zType(Locators.CITY, city);
			SleepUtil.sleepSmall();
			zType(Locators.ORGANIZATION_NAME, organizationName);
			SleepUtil.sleepSmall();
			zType(Locators.ORGANIZATION_UNIT, organizationUnit);
			SleepUtil.sleepSmall();
			sClickAt(Locators.NEXT_BUTTON, "");
			SleepUtil.sleepMedium();

			sClickAt(Locators.DOWNLOAD_THE_CSR, "");

		} else if (certificateAction.equals(Locators.INSTALL_COMMERCIAL_CERTIFICATE)) {

			sClickAt(Locators.NEXT_BUTTON, "");

			sClickAt(Locators.GENERATE_CSR, "");
			SleepUtil.sleepMedium();
			sClickAt(Locators.NEXT_BUTTON, "");
			SleepUtil.sleepMedium();
			sCheck(Locators.REPLACE_THE_EXISTING_CSR);
			SleepUtil.sleepMedium();
			zType(Locators.COUNTRY_NAME, countryName);
			SleepUtil.sleepSmall();
			zType(Locators.STATE, state);
			SleepUtil.sleepSmall();
			zType(Locators.CITY, city);
			SleepUtil.sleepSmall();
			zType(Locators.ORGANIZATION_NAME, organizationName);
			SleepUtil.sleepSmall();
			zType(Locators.ORGANIZATION_UNIT, organizationUnit);
			SleepUtil.sleepSmall();
			sClickAt(Locators.NEXT_BUTTON, "");
			SleepUtil.sleepMedium();
			sClickAt(Locators.INSTALL_BUTTON, "");

		} else {
			throw new HarnessException("implement me");
		}
		return (cert);
	}

	@Override
	public String myPageName() {
		return null;
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		return false;
	}

	public boolean zCloseWizard() throws HarnessException {
		this.sClickAt("css=td[id$='zdlg__UNDEFINE_button1_title']", "");
		return true;
	}
}