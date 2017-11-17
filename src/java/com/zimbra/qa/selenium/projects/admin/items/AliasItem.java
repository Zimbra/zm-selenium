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
package com.zimbra.qa.selenium.projects.admin.items;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;

public class AliasItem implements IItem {

	protected String aliasLocalName;
	protected String aliasDomainName;
	protected String aliasId;
	protected String aliasTargetEmail;
	protected String aliasTargetId;

	public AliasItem() {
		super();

		aliasLocalName = "alias" + ConfigProperties.getUniqueString();
		aliasDomainName = ConfigProperties.getStringProperty("testdomain");
		aliasId = null;
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
		return (getEmailAddress());
	}

	public String getID() {
		return (aliasId);
	}

	public String getEmailAddress() {
		return (aliasLocalName + "@" + aliasDomainName);
	}

	public void setLocalName(String name) {
		aliasLocalName = name;
	}

	public String getLocalName() {
		return (aliasLocalName);
	}

	public void setDomainName(String domain) {
		aliasDomainName = domain;
	}

	public String getDomainName() {
		return (aliasDomainName);
	}

	@SuppressWarnings("static-access")
	public void setTarget(AccountItem account) throws HarnessException {
		if ((account.getEmailAddress() == null) || (account.getEmailAddress().trim().length() == 0))
			throw new HarnessException("AccountItem email address is not set");
		if ((account.Id == null) || (account.Id.trim().length() == 0))
			throw new HarnessException("AccountItem ID is not set");

		aliasTargetEmail = account.getEmailAddress();
		aliasTargetId = account.Id;

	}

	public void setTargetAccountEmail(String emailAddress) throws HarnessException {
		if ((aliasTargetEmail != null) && (aliasTargetEmail.equals(emailAddress)))
			return; // Nothing to update

		if ((emailAddress == null) || (emailAddress.trim().length() == 0))
			throw new HarnessException("emailAddress cannot be null or blank");

		// Need to get the AccountID
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend("<GetAccountRequest xmlns='urn:zimbraAdmin'>"
				+ "<account by='name'>" + emailAddress + "</account>" + "</GetAccountRequest>");
		String id = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:account", "id");

		aliasTargetEmail = emailAddress;
		aliasTargetId = id;
	}

	public String getTargetAccountEmail() {
		return (aliasTargetEmail);
	}

	public void setTargetAccountId(String id) throws HarnessException {
		if ((aliasTargetId != null) && (aliasTargetId.equals(id)))
			return; // Nothing to update

		if ((id == null) || (id.trim().length() == 0))
			throw new HarnessException("id cannot be null or blank");

		// Need to get the AccountID
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend("<GetAccountRequest xmlns='urn:zimbraAdmin'>"
				+ "<account by='id'>" + id + "</account>" + "</GetAccountRequest>");
		String emailAddress = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:account", "name");

		aliasTargetEmail = emailAddress;
		aliasTargetId = id;

	}

	public String getTargetAccountId() {
		return (aliasTargetId);
	}
}