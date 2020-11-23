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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;

public class GALItem implements IItem {

	protected static Logger logger = LogManager.getLogger(IItem.class);

	protected GALMode newGALMod = null;
	protected GALMode currentGALMod = null;
	protected String dataSourceName = null;
	protected String pollingIntervalDays = null;
	protected ServerType serverType = ServerType.ActiveDirectory;
	protected String ldapUrl = "10.15.6.1";
	protected String ldapFilter = "dc=exchange2010,dc=lab";
	protected String ldapSearchBase = "dc=exchange2010,dc=lab";
	protected String bindPassword = null;

	public enum GALMode {
		Internal, External, Both,
	}

	public enum ServerType {
		LDAP, ActiveDirectory,
	}

	public GALItem() {
		super();
		currentGALMod = GALMode.Internal; // Internal GAL mode is the default GAl mode.
	}

	public GALMode getCurrentGALMode() {
		return (currentGALMod);
	}

	public void setCurrentGALMode(GALMode mode) {
		currentGALMod = mode;
	}

	public GALMode getNewGALMode() {
		return (newGALMod);
	}

	public void setNewGALMode(GALMode mode) {
		newGALMod = mode;
	}

	public String getDataSourceName() {
		return (dataSourceName);
	}

	public void setDataSourceName(String dataSrcName) {
		dataSourceName = dataSrcName;
	}

	public String getBindPassword() {
		return (bindPassword);
	}

	public void setBindPassword(String password) {
		bindPassword = password;
	}

	public String getPollingIntervalDays() {
		return (pollingIntervalDays);
	}

	public void setPollingIntervalDays(String days) {
		pollingIntervalDays = days;
	}

	public ServerType getServerType() {
		return (serverType);
	}

	public void setServerType(ServerType sType) {
		serverType = sType;
	}

	public String getLDAPUrl() {
		return ldapUrl;
	}

	public String getLDAPFilter() {
		return ldapFilter;
	}

	public String getLDAPSearchBase() {
		return ldapSearchBase;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void createUsingSOAP(ZimbraAccount account) throws HarnessException {
	}

	@Override
	public String prettyPrint() {
		return null;
	}
}