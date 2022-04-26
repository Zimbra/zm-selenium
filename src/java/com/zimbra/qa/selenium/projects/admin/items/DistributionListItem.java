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
package com.zimbra.qa.selenium.projects.admin.items;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;

public class DistributionListItem implements IItem {

	protected static Logger logger = LogManager.getLogger(IItem.class);

	protected static String Id = null;

	protected String distributionListLocalName; // Email Address is LocalName@DomainName
	protected String distributionListDomainName;
	protected boolean isDynamicDL;
	protected boolean isRightManagement;
	protected String memberURL;

	public DistributionListItem() {
		super();

		distributionListLocalName = "tcdl" + ConfigProperties.getUniqueString();
		distributionListDomainName = ConfigProperties.getStringProperty("testdomain");
		isDynamicDL = false;
		isRightManagement = true;
		memberURL = "ldap:///??sub?";
	}

	public void createUsingSOAP(ZimbraAccount account) throws HarnessException {
	}

	public String getName() {
		return (getEmailAddress());
	}

	public String getID() {
		return (Id);
	}

	public String getEmailAddress() {
		return (distributionListLocalName + "@" + distributionListDomainName);
	}

	public String prettyPrint() {
		return null;
	}

	public void setLocalName(String name) {
		distributionListLocalName = name;
	}

	public String getLocalName() {
		return (distributionListLocalName);
	}

	public void setDomainName(String domain) {
		distributionListDomainName = domain;
	}

	public void setDynamicDL(boolean isDynamic) {
		isDynamicDL = isDynamic;
	}

	public boolean getRightManagement() {
		return (isRightManagement);
	}

	public void setRightManagement(boolean isRightMgmt) {
		isRightManagement = isRightMgmt;
	}

	public boolean getDynamicDL() {
		return (isDynamicDL);
	}

	public String getDomainName() {
		return (distributionListDomainName);
	}

	public String getMemberURL() {
		return (memberURL);
	}

	public void setMemberURL(String memURL) {
		memberURL = memURL;
	}
}