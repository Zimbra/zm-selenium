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
package com.zimbra.qa.selenium.framework.util;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.*;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.ExecuteHarnessMain;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;

public class ZimbraAdminAccount extends ZimbraAccount {
	private static Logger logger = LogManager.getLogger(ZimbraAccount.class);

	public ZimbraAdminAccount(String email) {
		EmailAddress = email;
		Password = ConfigProperties.getStringProperty("adminPassword");
	}

	/**
	 * Creates the Delegated Administrator account on the ZCS using
	 * CreateAccountRequest zimbraIsAdminAccount is set to TRUE
	 */
	public ZimbraAdminAccount provisionDA(String email) {

		try {

			// Make sure domain exists
			ZimbraDomain domain = new ZimbraDomain(EmailAddress.split("@")[1]);
			domain.provision();

			// Build the list of default preferences
			Map<String, String> attributes = new HashMap<String, String>();
			attributes.put("zimbraPrefAdminConsoleWarnOnExit", "FALSE");

			// Add the display name
			StringBuilder prefs = new StringBuilder();
			for (Map.Entry<String, String> entry : attributes.entrySet()) {
				prefs.append(String.format("<a n='%s'>%s</a>", entry.getKey(), entry.getValue()));
			}

			// Create a new account in the Admin Console using SOAP
			AccountItem account = new AccountItem(email, ConfigProperties.getStringProperty("testdomain"));
			ZimbraAdminAccount.AdminConsoleAdmin()
					.soapSend("<CreateAccountRequest xmlns='urn:zimbraAdmin'>" + "<name>" + account.getEmailAddress()
							+ "</name>" + "<password>" + ConfigProperties.getStringProperty("adminPassword")
							+ "</password>" + "<a xmlns='' n='zimbraIsDelegatedAdminAccount'>TRUE</a>"
							+ "<a xmlns='' n='zimbraPrefAdminConsoleWarnOnExit'>FALSE</a>" + "</CreateAccountRequest>");

			Element[] createAccountResponse = ZimbraAdminAccount.AdminConsoleAdmin()
					.soapSelectNodes("//admin:CreateAccountResponse");

			if ((createAccountResponse == null) || (createAccountResponse.length == 0)) {

				Element[] soapFault = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNodes("//soap:Fault");
				if (soapFault != null && soapFault.length > 0) {

					String error = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//zimbra:Code", null);
					throw new HarnessException("Unable to create account: " + error);

				}

				throw new HarnessException("Unknown error when provisioning account");

			}

			// Set the account settings based on the response
			ZimbraId = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:account", "id");
			ZimbraMailHost = ZimbraAdminAccount.AdminConsoleAdmin()
					.soapSelectValue("//admin:account/admin:a[@n='zimbraMailHost']", null);

			// Assign standard basic rights to DA
			String[] rights = { "adminConsoleAccountRights", "adminConsoleDLRights", "adminConsoleAliasRights",
					"adminConsoleResourceRights", "adminConsoleSavedSearchRights", "adminConsoleDomainRights" };
			for (String right : rights) {
				this.grantRightRequest(account, right.trim());
			}

			ZimbraAdminAccount.GlobalAdmin().soapSend("<FlushCacheRequest  xmlns='urn:zimbraAdmin'>"
					+ "<cache type='galgroup'/>" + "</FlushCacheRequest>");

			ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<GetEffectiveRightsRequest xmlns='urn:zimbraAdmin'>" + "<target  by='name' type='account'>"
							+ account.getEmailAddress() + "</target>" + "<grantee  by='name' >"
							+ GlobalAdmin().EmailAddress + "</grantee>" + "</GetEffectiveRightsRequest>");

			// If SOAP trace logging is specified, turn it on
			if (ConfigProperties.getStringProperty("soap.trace.enabled", "false").toLowerCase().equals("true")) {

				ZimbraAdminAccount.GlobalAdmin()
						.soapSend("<AddAccountLoggerRequest xmlns='urn:zimbraAdmin'>" + "<account by='name'>"
								+ EmailAddress + "</account>" + "<logger category='zimbra.soap' level='trace'/>"
								+ "</AddAccountLoggerRequest>");

			}

			// Sync the GAL to put the account into the list
			domain.syncGalAccount();

		} catch (HarnessException e) {
			logger.error("Unable to provision account: " + EmailAddress);
			ZimbraId = null;
			ZimbraMailHost = null;
		}

		return (this);
	}

	/**
	 * Creates the account on the ZCS using CreateAccountRequest
	 * zimbraIsAdminAccount is set to TRUE
	 */
	public ZimbraAccount provision() {

		try {

			if (exists()) {
				logger.info(EmailAddress + " already exists.  Not provisioning again.");
				return (this);
			}

			// Make sure domain exists
			ZimbraDomain domain = new ZimbraDomain(EmailAddress.split("@")[1]);
			domain.provision();

			// Build the list of default preferences
			Map<String, String> attributes = new HashMap<String, String>();
			attributes.put("zimbraPrefAdminConsoleWarnOnExit", "FALSE");

			// Add the display name
			StringBuilder prefs = new StringBuilder();
			for (Map.Entry<String, String> entry : attributes.entrySet()) {
				prefs.append(String.format("<a n='%s'>%s</a>", entry.getKey(), entry.getValue()));
			}

			// Account does not exist. Create it now.
			ZimbraAdminAccount.GlobalAdmin()
					.soapSend("<CreateAccountRequest xmlns='urn:zimbraAdmin'>" + "<name>" + EmailAddress + "</name>"
							+ "<password>" + Password + "</password>" + prefs.toString()
							+ "<a n='zimbraIsAdminAccount'>TRUE</a>" + "</CreateAccountRequest>");

			Element[] createAccountResponse = ZimbraAdminAccount.GlobalAdmin()
					.soapSelectNodes("//admin:CreateAccountResponse");

			if ((createAccountResponse == null) || (createAccountResponse.length == 0)) {

				Element[] soapFault = ZimbraAdminAccount.GlobalAdmin().soapSelectNodes("//soap:Fault");
				if (soapFault != null && soapFault.length > 0) {
					String error = ZimbraAdminAccount.GlobalAdmin().soapSelectValue("//zimbra:Code", null);
					throw new HarnessException("Unable to create account: " + error);
				}
				throw new HarnessException("Unknown error when provisioning account");

			}

			// Set the account settings based on the response
			ZimbraId = ZimbraAdminAccount.GlobalAdmin().soapSelectValue("//admin:account", "id");
			ZimbraMailHost = ZimbraAdminAccount.GlobalAdmin()
					.soapSelectValue("//admin:account/admin:a[@n='zimbraMailHost']", null);

			// If SOAP trace logging is specified, turn it on
			if (ConfigProperties.getStringProperty("soap.trace.enabled", "false").toLowerCase().equals("true")) {

				ZimbraAdminAccount.GlobalAdmin()
						.soapSend("<AddAccountLoggerRequest xmlns='urn:zimbraAdmin'>" + "<account by='name'>"
								+ EmailAddress + "</account>" + "<logger category='zimbra.soap' level='trace'/>"
								+ "</AddAccountLoggerRequest>");

			}

			// Sync the GAL to put the account into the list
			domain.syncGalAccount();

		} catch (HarnessException e) {
			logger.error("Unable to provision account: " + EmailAddress);
			ZimbraId = null;
			ZimbraMailHost = null;
		}

		return (this);
	}

	public Element soapSend(String request) throws HarnessException {

		// Tests seem to be running a lot longer, or there are now
		// so many tests that the authToken timeout for the global
		// admin expires.
		//
		// Either way, check for <Code>service.AUTH_EXPIRED</Code>
		// and re-auth, if necessary
		//

		// Send the request
		Element response = super.soapSend(request);

		// Check if re-auth is required
		String code = this.soapSelectValue("//zimbra:Code", null);
		if ("service.AUTH_EXPIRED".equalsIgnoreCase(code)) {

			logger.warn("Admin Auth token expired.  Re-authenticating.");
			logger.warn(request);
			logger.warn(this.soapLastResponse());

			// Re-auth
			soapClient.setAuthToken(null);
			this.authenticate();

			// Re-send SOAP request (this time, ignore errors and bubble back up)
			response = super.soapSend(request);

		}

		return (response);
	}

	/**
	 * Authenticates the admin account (using SOAP admin AuthRequest) Sets the
	 * authToken
	 */
	public ZimbraAccount authenticate() {
		try {
			super.soapSend("<AuthRequest xmlns='urn:zimbraAdmin'>" + "<name>" + EmailAddress + "</name>" + "<password>"
					+ Password + "</password>" + "</AuthRequest>");
			String token = soapSelectValue("//admin:authToken", null);
			soapClient.setAuthToken(token);
		} catch (HarnessException e) {
			logger.error("Unable to authenticate " + EmailAddress, e);
			soapClient.setAuthToken(null);
		}
		return (this);
	}

	/**
	 * Get the global admin account used for Admin Console testing This global admin
	 * has the zimbraPrefAdminConsoleWarnOnExit set to false
	 */
	public static synchronized ZimbraAdminAccount AdminConsoleAdmin() {
		if (_AdminConsoleAdmin == null) {
			try {
				String name = ConfigProperties.getStringProperty("adminUser") + ConfigProperties.getUniqueString();
				String domain = ExecuteHarnessMain.proxyServers.get(0);
				_AdminConsoleAdmin = new ZimbraAdminAccount(name + "@" + domain);
				_AdminConsoleAdmin.provision();
				_AdminConsoleAdmin.authenticate();
				_AdminConsoleAdmin.soapSend("<ModifyAccountRequest xmlns='urn:zimbraAdmin'>" + "<id>"
						+ _AdminConsoleAdmin.ZimbraId + "</id>" + "<a n='zimbraPrefAdminConsoleWarnOnExit'>FALSE</a>"
						+ "</ModifyAccountRequest>");
			} catch (HarnessException e) {
				logger.error("Unable to fully provision admin account", e);
			}
		}
		return (_AdminConsoleAdmin);
	}

	private static ZimbraAdminAccount _AdminConsoleAdmin = null;

	/**
	 * Reset all static accounts. This method should be used before/after the
	 * harness has executed in the STAF service mode. For example, if one STAF
	 * request executes on server1 and the subsequent STAF request executes on
	 * server2, then all accounts need to be reset, otherwise the second request
	 * will have references to server1.
	 */
	public static synchronized void ResetAdminAccount() {
		logger.warn("AdminConsoleAdmin is being reset");
		_AdminConsoleAdmin = null;
		_GlobalAdmin = null;
		AdminConsoleAdmin();
	}

	/**
	 * Get the global admin account This account is defined in config.properties as
	 * <adminUser>@<server>
	 *
	 * @return The global admin account
	 */
	public static synchronized ZimbraAdminAccount GlobalAdmin() {
		if (_GlobalAdmin == null) {
			String name = ConfigProperties.getStringProperty("adminUser") + "@"
					+ ExecuteHarnessMain.proxyServers.get(0);
			_GlobalAdmin = new ZimbraAdminAccount(name);
			_GlobalAdmin.authenticate();
		}
		return (_GlobalAdmin);
	}

	private static ZimbraAdminAccount _GlobalAdmin = null;

	public static void main(String[] args) throws HarnessException {

		// Configure log4j using the basic configuration
		BasicConfigurator.configure();

		// Use the pre-provisioned global admin account to send a basic request
		ZimbraAdminAccount.GlobalAdmin().soapSend("<GetVersionInfoRequest xmlns='urn:zimbraAdmin'/>");
		if (!ZimbraAdminAccount.GlobalAdmin().soapMatch("//admin:GetVersionInfoResponse", null, null))
			throw new HarnessException("GetVersionInfoRequest did not return GetVersionInfoResponse");

		// Create a new global admin account
		String domain = ConfigProperties.getStringProperty("server.host");
		ZimbraAdminAccount admin = new ZimbraAdminAccount("admin" + System.currentTimeMillis() + "@" + domain);
		admin.provision(); // Create the account (CreateAccountRequest)
		admin.authenticate(); // Authenticate the account (AuthRequest)

		// Send a basic request as the new admin account
		admin.soapSend("<GetServiceStatusRequest xmlns='urn:zimbraAdmin'/>");
		if (!admin.soapMatch("//admin:GetServiceStatusResponse", null, null))
			throw new HarnessException("GetServiceStatusRequest did not return GetServiceStatusResponse");

		logger.info("Done!");
	}

	public void grantRightRequest(AccountItem account, String right) throws HarnessException {

		ZimbraAdminAccount.GlobalAdmin()
				.soapSend("<GrantRightRequest xmlns='urn:zimbraAdmin'>" + "<target  by='name' type='domain'>"
						+ account.getDomainName() + "</target>" + "<grantee  by='name' type='usr'>"
						+ account.getEmailAddress() + "</grantee>" + "<right>" + right + "</right>"
						+ "</GrantRightRequest>");
	}
}