/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;

public class ResourceItem implements IItem {
	protected String resourceLocalName;
	protected String resourceDomainName;
	protected String resourceId;

	public ResourceItem() {
		super();

		resourceLocalName = "a_resource" + ConfigProperties.getUniqueString();
		resourceDomainName = ConfigProperties.getStringProperty("testdomain");
		resourceId = null;
	}

	@Override
	public void createUsingSOAP(ZimbraAccount account) throws HarnessException {
	}

	@Override
	public String prettyPrint() {
		return null;
	}

	@Override
	public String getName() {
		return (getLocalName());
	}

	public String getID() {
		return (resourceId);
	}

	public String getEmailAddress() {
		return (resourceLocalName + "@" + resourceDomainName);
	}

	public void setLocalName(String name) {
		resourceLocalName = name;
	}

	public String getLocalName() {
		return (resourceLocalName);
	}

	public void setDomainName(String domain) {
		resourceDomainName = domain;
	}

	public String getDomainName() {
		return (resourceDomainName);
	}
}