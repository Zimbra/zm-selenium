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
package com.zimbra.qa.selenium.framework.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.zimbra.common.soap.Element;

public class ZimbraDomain {
	private static Logger logger = LogManager.getLogger(ZimbraDomain.class);

	// Domain information
	protected String DomainName = null;

	// GAL Sync account information
	protected String DomainGalSyncAccountID = null;
	protected String DomainGalSyncDatasourceID = null;

	/**
	 *
	 * @param name
	 */
	public ZimbraDomain(String name) {
		DomainName = name;
	}

	/**
	 * Does the domain exist already?
	 *
	 * @return
	 * @throws HarnessException
	 */
	public boolean exists() throws HarnessException {
		// Check if the domain exists
		ZimbraAdminAccount.GlobalAdmin().soapSend("<GetDomainRequest xmlns='urn:zimbraAdmin'>" + "<domain by='name'>"
				+ DomainName + "</domain>" + "</GetDomainRequest>");

		Element response = ZimbraAdminAccount.GlobalAdmin().soapSelectNode("//admin:GetDomainResponse", 1);

		// If the domain exists, there will be an id
		if (response == null) {
			return (false);
		}

		// If there was a response, make sure we have the up to date information
		DomainGalSyncAccountID = ZimbraAdminAccount.GlobalAdmin()
				.soapSelectValue("//admin:GetDomainResponse//admin:a[@n='zimbraGalAccountId']", null);
		logger.info("DomainGalSyncAccountID=" + DomainGalSyncAccountID);

		ZimbraAdminAccount.GlobalAdmin().soapSend("<GetDataSourcesRequest xmlns='urn:zimbraAdmin'>" + "<id>"
				+ DomainGalSyncAccountID + "</id>" + "</GetDataSourcesRequest>");

		DomainGalSyncDatasourceID = ZimbraAdminAccount.GlobalAdmin()
				.soapSelectValue("//admin:GetDataSourcesResponse//admin:dataSource", "id");
		logger.info("DomainGalSyncDatasourceID=" + DomainGalSyncDatasourceID);

		return (true);
	}

	/**
	 * Create the domain
	 *
	 * @throws HarnessException
	 */
	public void provision() throws HarnessException {
		if (exists()) {
			logger.info(DomainName + " already exists.  Not provisioning again.");
			return;
		}

		// If the domain does not exist, create it
		ZimbraAdminAccount.GlobalAdmin()
				.soapSend("<CreateDomainRequest xmlns='urn:zimbraAdmin'>" + "<name>" + DomainName + "</name>"
						+ "<a n='zimbraGalMode'>zimbra</a>" + "<a n='zimbraGalMaxResults'>15</a>"
						+ "</CreateDomainRequest>");

		this.createGalSyncAccount();
		this.syncGalAccount();
	}

	/**
	 * Create the GAL sync account for this domain
	 *
	 * @throws HarnessException
	 */
	public void createGalSyncAccount() throws HarnessException {
		// Create the Sync GAL Account
		String galaccount = "galaccount" + ConfigProperties.getUniqueString() + "@" + DomainName;
		String datasourcename = "datasource" + ConfigProperties.getUniqueString();

		ZimbraAdminAccount.GlobalAdmin()
				.soapSend("<CreateGalSyncAccountRequest xmlns='urn:zimbraAdmin' name='" + datasourcename
						+ "' type='zimbra' domain='" + DomainName + "' >" + "<account by='name'>" + galaccount
						+ "</account>" + "<password>" + ConfigProperties.getStringProperty("accountPassword")
						+ "</password>" + "</CreateGalSyncAccountRequest>");

		DomainGalSyncAccountID = ZimbraAdminAccount.GlobalAdmin()
				.soapSelectValue("//admin:CreateGalSyncAccountResponse/admin:account", "id");
		logger.info("DomainGalSyncAccountID=" + DomainGalSyncAccountID);

		ZimbraAdminAccount.GlobalAdmin().soapSend("<GetDataSourcesRequest xmlns='urn:zimbraAdmin'>" + "<id>"
				+ DomainGalSyncAccountID + "</id>" + "</GetDataSourcesRequest>");

		DomainGalSyncDatasourceID = ZimbraAdminAccount.GlobalAdmin()
				.soapSelectValue("//admin:GetDataSourcesResponse//admin:dataSource", "id");
		logger.info("DomainGalSyncDatasourceID=" + DomainGalSyncDatasourceID);
	}

	public void syncGalAccount() throws HarnessException {
		ZimbraAdminAccount.GlobalAdmin()
				.soapSend("<SyncGalAccountRequest xmlns='urn:zimbraAdmin'>" + "<account id='" + DomainGalSyncAccountID
						+ "'>" + "<datasource by='id' fullSync='true' reset='true'>" + DomainGalSyncDatasourceID
						+ "</datasource>" + "</account>" + "</SyncGalAccountRequest>");
		String syncGalAccountResponse = ZimbraAdminAccount.GlobalAdmin()
				.soapSelectValue("//admin:SyncGalAccountResponse", null);

		if (syncGalAccountResponse == null) {
			throw new HarnessException("Unable to sync GAL account.  Response was: "
					+ ZimbraAdminAccount.GlobalAdmin().soapLastResponse());
		}
	}
}