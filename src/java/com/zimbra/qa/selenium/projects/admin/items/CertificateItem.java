/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.admin.items;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;

public class CertificateItem implements IItem {

	protected String CountryName;
	protected String state;
	protected String city;
	protected String OrganizationName;
	protected String OrganizationUnit;
	protected String CertificateValidationDays;
	protected String CommanName;

	public CertificateItem() {
		super();
	}

	@Override
	public void createUsingSOAP(ZimbraAccount account) throws HarnessException {
	}

	@Override
	public String prettyPrint() {
		return null;
	}

	public void setCountryName(String name) {
		CountryName = name;
	}

	public String getCountryName() {
		return (CountryName);
	}

	public void setCommonName(String name) {
		CommanName = name;
	}

	public String getCommonName() {
		return (CommanName);
	}

	public void setStateName(String name) {
		state = name;
	}

	public String getStateName() {
		return (state);
	}

	public void setCityName(String name) {
		city = name;
	}

	public String getCityName() {
		return (city);
	}

	public void setOrganizationName(String name) {
		OrganizationName = name;
	}

	public String getOrganizationName() {
		return (OrganizationName);
	}

	public void setOrganizationUnit(String name) {
		OrganizationUnit = name;
	}

	public String getOrganizationUnit() {
		return (OrganizationUnit);
	}

	public void setCertificateValidationDays(String name) {
		CertificateValidationDays = name;
	}

	public String getCertificateValidationDays() {
		return (CertificateValidationDays);
	}

	@Override
	public String getName() {
		return null;
	}
}