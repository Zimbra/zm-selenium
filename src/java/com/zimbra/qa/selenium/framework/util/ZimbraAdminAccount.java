package com.zimbra.qa.selenium.framework.util;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.zimbra.qa.selenium.framework.core.DevEnvironment;


public class ZimbraAdminAccount extends ZimbraAccount {
	private static Logger logger = LogManager.getLogger(ZimbraAccount.class);

	public ZimbraAdminAccount(String email) {
		EmailAddress = email;
		Password = ZimbraSeleniumProperties.getStringProperty("adminPwd", "test123");
		ZimbraMailHost = EmailAddress.split("@")[1];

		// Start: Dev environment hack
		if ( DevEnvironment.isUsingDevEnvironment() ) {
			ZimbraMailHost = "localhost";
		}
		// End: Dev environment hack
	}

	/**
	 * Creates the account on the ZCS using CreateAccountRequest
	 * zimbraIsAdminAccount is set to TRUE
	 */
	public ZimbraAccount provision() {
		try {
			ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<CreateAccountRequest xmlns='urn:zimbraAdmin'>" +
					"<name>"+ EmailAddress +"</name>" +
					"<password>"+ Password +"</password>" +
					"<a n='zimbraIsAdminAccount'>TRUE</a>" +
			"</CreateAccountRequest>");
			ZimbraId = ZimbraAdminAccount.GlobalAdmin().soapSelectValue("//admin:account", "id");
			ZimbraMailHost = ZimbraAdminAccount.GlobalAdmin().soapSelectValue("//admin:account/admin:a[@n='zimbraMailHost']", null);

			// Start: Dev environment hack
			if ( DevEnvironment.isUsingDevEnvironment() ) {
				ZimbraMailHost = "localhost";
			}
			// End: Dev environment hack

		} catch (HarnessException e) {
			logger.error("Unable to provision account: "+ EmailAddress);
			ZimbraId = null;
			ZimbraMailHost = null;
		}
		return (this);
	}

	/**
	 * Authenticates the admin account (using SOAP admin AuthRequest)
	 * Sets the authToken
	 */
	public ZimbraAccount authenticate() {
		try {
			soapSend(
					"<AuthRequest xmlns='urn:zimbraAdmin'>" +
					"<name>"+ EmailAddress +"</name>" +
					"<password>"+ Password +"</password>" +
			"</AuthRequest>");
			String token = soapSelectValue("//admin:authToken", null);
			soapClient.setAuthToken(token);
		} catch (HarnessException e) {
			logger.error("Unable to authenticate "+ EmailAddress, e);
			soapClient.setAuthToken(null);
		}
		return (this);
	}

	/**
	 * Get the global admin account used for Admin Console testing
	 * This global admin has the zimbraPrefAdminConsoleWarnOnExit set to false
	 */
	public static synchronized ZimbraAdminAccount AdminConsoleAdmin() {
		if ( _AdminConsoleAdmin == null ) {
			try {
				String name = "globaladmin"+ ZimbraSeleniumProperties.getUniqueString();
				String domain = ZimbraSeleniumProperties.getStringProperty("server.host","qa60.lab.zimbra.com");
				_AdminConsoleAdmin = new ZimbraAdminAccount(name +"@"+ domain);
				_AdminConsoleAdmin.provision();
				_AdminConsoleAdmin.authenticate();
				_AdminConsoleAdmin.soapSend(
							"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
						+		"<id>"+ _AdminConsoleAdmin.ZimbraId +"</id>"
						+		"<a n='zimbraPrefAdminConsoleWarnOnExit'>FALSE</a>"
						+	"</ModifyAccountRequest>");
			} catch (HarnessException e) {
				logger.error("Unable to fully provision admin account", e);
			}
		}
		return (_AdminConsoleAdmin);
	}
	public static synchronized void ResetAccountAdminConsoleAdmin() {
		logger.warn("AdminConsoleAdmin is being reset");
		_AdminConsoleAdmin = null;
	}
	private static ZimbraAdminAccount _AdminConsoleAdmin = null;

	/**
	 * Reset all static accounts.  This method should be used before/after
	 * the harness has executed in the STAF service mode.  For example, if
	 * one STAF request executes on server1 and the subsequent STAF request
	 * executes on server2, then all accounts need to be reset, otherwise
	 * the second request will have references to server1.
	 */
	public static void reset() {
		ZimbraAdminAccount._AdminConsoleAdmin = null;
		ZimbraAdminAccount._GlobalAdmin = null;		
	}

	/**
	 * Get the global admin account
	 * This account is defined in config.properties as <adminName>@<server>
	 * @return The global admin account
	 */
	public static synchronized ZimbraAdminAccount GlobalAdmin() {
		if ( _GlobalAdmin == null ) {
			String name = ZimbraSeleniumProperties.getStringProperty("adminName", "admin@zqa-062.eng.vmware.com");
			_GlobalAdmin = new ZimbraAdminAccount(name);
			_GlobalAdmin.authenticate();
		}
		return (_GlobalAdmin);
	}
	private static ZimbraAdminAccount _GlobalAdmin = null;


	/**
	 * @param args
	 * @throws HarnessException 
	 */
	public static void main(String[] args) throws HarnessException {

		// Configure log4j using the basic configuration
		BasicConfigurator.configure();



		// Use the pre-provisioned global admin account to send a basic request
		ZimbraAdminAccount.GlobalAdmin().soapSend("<GetVersionInfoRequest xmlns='urn:zimbraAdmin'/>");
		if ( !ZimbraAdminAccount.GlobalAdmin().soapMatch("//admin:GetVersionInfoResponse", null, null) )
			throw new HarnessException("GetVersionInfoRequest did not return GetVersionInfoResponse");



		// Create a new global admin account
		String domain = ZimbraSeleniumProperties.getStringProperty("server.host","qa60.lab.zimbra.com");
		ZimbraAdminAccount admin = new ZimbraAdminAccount("admin"+ System.currentTimeMillis() +"@"+ domain);
		admin.provision();	// Create the account (CreateAccountRequest)
		admin.authenticate();		// Authenticate the account (AuthRequest)

		// Send a basic request as the new admin account
		admin.soapSend("<GetServiceStatusRequest xmlns='urn:zimbraAdmin'/>");
		if ( !admin.soapMatch("//admin:GetServiceStatusResponse", null, null) )
			throw new HarnessException("GetServiceStatusRequest did not return GetServiceStatusResponse");


		logger.info("Done!");
	}

}
