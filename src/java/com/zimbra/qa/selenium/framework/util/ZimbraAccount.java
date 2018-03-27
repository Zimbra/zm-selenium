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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpRecoverableException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dom4j.InvalidXPathException;
import com.zimbra.common.auth.ZAuthToken;
import com.zimbra.common.net.SocketFactories;
import com.zimbra.common.service.ServiceException;
import com.zimbra.common.soap.Element;
import com.zimbra.common.soap.SoapFaultException;
import com.zimbra.common.soap.SoapParseException;
import com.zimbra.common.soap.SoapProtocol;
import com.zimbra.common.soap.SoapUtil;
import com.zimbra.common.soap.Element.ContainerException;
import com.zimbra.common.soap.XmlParseException;
import com.zimbra.common.util.ByteUtil;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.ui.I18N;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;

@SuppressWarnings("deprecation")
public class ZimbraAccount {
	private static Logger logger = LogManager.getLogger(ZimbraAccount.class);

	protected SoapClient soapClient = new SoapClient();
	public String ZimbraSoapClientHost = null;
	public String ZimbraSoapAdminHost = null;
	public String ZimbraMailHost = null;
	public String ZimbraMailClientHost = null;
	public String ZimbraId = null;
	public String EmailAddress = null;
	public String Password = null;
	public String DisplayName = null;
	public boolean accountIsDirty = false;
	protected String ZimbraPrefLocale = Locale.getDefault().toString();
	protected String MyAuthToken = null;
	protected String MyClientAuthToken = null;
	public final static String clientAccountName = "local@host.local";

	// Account Attributes
	// These attributes are set per each test class
	protected Map<String, String> startingAccountPreferences = new HashMap<String, String>();
	// These attributes are set for the zimlets per each test case
	protected Map<String, String> startingUserZimletPreferences = new HashMap<String, String>();

	/*
	 * Create an account with the email address account<num>@<testdomain> The
	 * password is set to config property "adminPwd"
	 */
	public ZimbraAccount() {
		this(null, null);
	}

	/*
	 * Create an account with the email address <name>@<domain> The password is set
	 * to config property "adminPassword"
	 */
	public ZimbraAccount(String email, String password) {

		if (email == null) {
			DisplayName = ConfigProperties.getStringProperty("locale").toLowerCase().replace("_", "")
					+ ConfigProperties.getUniqueString();
			email = DisplayName + "@" + ConfigProperties.getStringProperty("testdomain");
		} else {
			DisplayName = email.split("@")[0];
		}
		EmailAddress = email;

		if (password == null) {
			password = ConfigProperties.getStringProperty("adminPassword");
		}
		Password = password;
	}

	public String zGetAccountStoreHost() {
		String ZimbraMailHost;

		try {
			ZimbraAdminAccount.GlobalAdmin().soapSend("<GetAccountRequest xmlns='urn:zimbraAdmin'>"
					+ "<account by='name'>" + this.EmailAddress + "</account>" + "</GetAccountRequest>");
		} catch (HarnessException e) {
			e.printStackTrace();
		}

		ZimbraAdminAccount.GlobalAdmin().soapSelectNodes("//admin:GetAccountResponse");
		ZimbraMailHost = ZimbraAdminAccount.GlobalAdmin()
				.soapSelectValue("//admin:account/admin:a[@n='zimbraMailHost']", null);
		logger.info("Zimbra mail host for account " + this.EmailAddress + ": " + ZimbraMailHost);

		return (ZimbraMailHost);
	}

	/**
	 * This is to reset the Client's authentication. Note: To be used when
	 * terminating client app.
	 */
	public void resetClientAuthentication() {
		logger.debug("Reset client authentication...");
		this.MyClientAuthToken = null;
	}

	// ZCS account
	public static synchronized ZimbraAccount AccountZCS() {
		if (_AccountZCS == null) {
			_AccountZCS = new ZimbraAccount();
			_AccountZCS.provision();
			_AccountZCS.authenticate();
		}
		return (_AccountZCS);
	}

	public static synchronized void ResetAccountZCS() {
		logger.warn("AccountZCS is being reset");
		_AccountZCS = null;
	}

	private static ZimbraAccount _AccountZCS = null;

	// Test accounts
	public static synchronized ZimbraAccount AccountA() {
		if (_AccountA == null) {
			_AccountA = new ZimbraAccount();
			_AccountA.provision();
			_AccountA.authenticate();
		}
		return (_AccountA);
	}

	private static ZimbraAccount _AccountA = null;

	public static synchronized ZimbraAccount AccountB() {
		if (_AccountB == null) {
			_AccountB = new ZimbraAccount();
			_AccountB.provision();
			_AccountB.authenticate();
		}
		return (_AccountB);
	}

	private static ZimbraAccount _AccountB = null;

	public static synchronized ZimbraAccount AccountC() {
		if (_AccountC == null) {
			_AccountC = new ZimbraAccount();
			_AccountC.provision();
			_AccountC.authenticate();
		}
		return (_AccountC);
	}

	private static ZimbraAccount _AccountC = null;

	public static synchronized ZimbraAccount Account1() {
		if (_Account1 == null) {
			_Account1 = new ZimbraAccount();
			_Account1.provision();
			_Account1.authenticate();
		}
		return (_Account1);
	}

	private static ZimbraAccount _Account1 = null;

	public static synchronized ZimbraAccount Account2() {
		if (_Account2 == null) {
			_Account2 = new ZimbraAccount();
			_Account2.provision();
			_Account2.authenticate();
		}
		return (_Account2);
	}

	private static ZimbraAccount _Account2 = null;

	public static synchronized ZimbraAccount Account3() {
		if (_Account3 == null) {
			_Account3 = new ZimbraAccount();
			_Account3.provision();
			_Account3.authenticate();
		}
		return (_Account3);
	}

	private static ZimbraAccount _Account3 = null;

	public static synchronized ZimbraAccount Account4() {
		if (_Account4 == null) {
			_Account4 = new ZimbraAccount();
			_Account4.provision();
			_Account4.authenticate();
		}
		return (_Account4);
	}

	private static ZimbraAccount _Account4 = null;

	public static synchronized ZimbraAccount Account5() {
		if (_Account5 == null) {
			_Account5 = new ZimbraAccount();
			_Account5.provision();
			_Account5.authenticate();
		}
		return (_Account5);
	}

	private static ZimbraAccount _Account5 = null;

	public static synchronized ZimbraAccount Account6() {
		if (_Account6 == null) {
			_Account6 = new ZimbraAccount();
			_Account6.provision();
			_Account6.authenticate();
		}
		return (_Account6);
	}

	private static ZimbraAccount _Account6 = null;

	public static synchronized ZimbraAccount Account7() {
		if (_Account7 == null) {
			_Account7 = new ZimbraAccount();
			_Account7.provision();
			_Account7.authenticate();
		}
		return (_Account7);
	}

	private static ZimbraAccount _Account7 = null;

	public static synchronized ZimbraAccount Account8() {
		if (_Account8 == null) {
			_Account8 = new ZimbraAccount();
			_Account8.provision();
			_Account8.authenticate();
		}
		return (_Account8);
	}

	private static ZimbraAccount _Account8 = null;

	public static synchronized ZimbraAccount Account9() {
		if (_Account9 == null) {
			_Account9 = new ZimbraAccount();
			_Account9.provision();
			_Account9.authenticate();
		}
		return (_Account9);
	}

	private static ZimbraAccount _Account9 = null;

	public static synchronized ZimbraAccount Account10() {
		if (_Account10 == null) {
			_Account10 = new ZimbraAccount();
			_Account10.provision();
			_Account10.authenticate();
		}
		return (_Account10);
	}

	private static ZimbraAccount _Account10 = null;

	/**
	 * Reset all static accounts. This method should be used before/after the
	 * harness has executed in the STAF service mode. For example, if one STAF
	 * request executes on server1 and the subsequent STAF request executes on
	 * server2, then all accounts need to be reset, otherwise the second request
	 * will have references to server1.
	 */
	public static void reset() {
		ZimbraAccount._AccountZCS = null;
		ZimbraAccount._AccountA = null;
		ZimbraAccount._AccountB = null;
		ZimbraAccount._AccountC = null;
		ZimbraAccount._Account1 = null;
		ZimbraAccount._Account2 = null;
		ZimbraAccount._Account3 = null;
		ZimbraAccount._Account4 = null;
		ZimbraAccount._Account5 = null;
		ZimbraAccount._Account6 = null;
		ZimbraAccount._Account7 = null;
		ZimbraAccount._Account8 = null;
		ZimbraAccount._Account9 = null;
		ZimbraAccount._Account10 = null;
	}

	// Set the default account settings
	@SuppressWarnings("serial")
	private static final Map<String, String> accountAttrs = new HashMap<String, String>() {
		{
			if (ConfigProperties.getStringProperty("server.host").startsWith("pnq-")) {
				put("zimbraPrefTimeZoneId", "Asia/Kolkata");

			} else {
				put("zimbraPrefTimeZoneId", "America/Chicago");
			}

			if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
					|| Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				put("zimbraPrefCalendarInitialView", "week");
			}

			/*
			 * The following settings are specific to the test harness and deviate from the
			 * default settings to work around Test harness issues/limitations
			 */
			put("zimbraPrefLocale", ConfigProperties.getStringProperty("locale"));
			put("zimbraPrefClientType", "advanced");
			put("zimbraFeatureTouchClientEnabled", "TRUE");
			put("zimbraPrefAutoAddAddressEnabled", "FALSE");
			put("zimbraPrefCalendarApptReminderWarningTime", "0");
			put("zimbraPrefCalendarShowPastDueReminders", "FALSE");
			put("zimbraPrefOutOfOfficeStatusAlertOnLogin", "FALSE");
			put("zimbraPrefOutOfOfficeReplyEnabled", "FALSE");
			put("zimbraPrefWarnOnExit", "FALSE");

		}
	};

	/**
	 * Determines if the account already exists If yes, then the account settings
	 * are reset based on the GetAccountResponse
	 *
	 * @throws HarnessException
	 */
	public boolean exists() throws HarnessException {

		// Check if the account exists
		ZimbraAdminAccount.GlobalAdmin().soapSend("<GetAccountRequest xmlns='urn:zimbraAdmin'>" + "<account by='name'>"
				+ EmailAddress + "</account>" + "</GetAccountRequest>");

		Element[] getAccountResponse = ZimbraAdminAccount.GlobalAdmin().soapSelectNodes("//admin:GetAccountResponse");

		if ((getAccountResponse == null) || (getAccountResponse.length == 0)) {
			logger.debug("Account does not exist");
			return (false);
		}

		// Reset the account settings based on the response
		ZimbraId = ZimbraAdminAccount.GlobalAdmin().soapSelectValue("//admin:account", "id");
		ZimbraMailHost = ZimbraAdminAccount.GlobalAdmin()
				.soapSelectValue("//admin:account/admin:a[@n='zimbraMailHost']", null);
		ZimbraPrefLocale = ZimbraAdminAccount.GlobalAdmin()
				.soapSelectValue("//admin:account/admin:a[@n='zimbraPrefLocale']", null);

		// If pref is not set, then use default
		if ((ZimbraPrefLocale == null) || ZimbraPrefLocale.trim().equals("")) {
			ZimbraPrefLocale = Locale.getDefault().toString();
		}

		return (true);
	}

	/**
	 * Creates the account on the ZCS using CreateAccountRequest
	 */
	public ZimbraAccount provision() {

		try {

			if (exists()) {
				logger.info(EmailAddress + " already exists. Not provisioning again.");
				return (this);
			}

			// Make sure domain exists
			ZimbraDomain domain = new ZimbraDomain(EmailAddress.split("@")[1]);
			domain.provision();

			// Build the list of default preferences
			Map<String, String> attributes = new HashMap<String, String>();

			attributes.putAll(accountAttrs);
			attributes.put("displayName", DisplayName);
			attributes.putAll(startingAccountPreferences);

			// Add the display name
			StringBuilder prefs = new StringBuilder();
			for (Map.Entry<String, String> entry : attributes.entrySet()) {
				prefs.append(String.format("<a n='%s'>%s</a>", entry.getKey(), entry.getValue()));
			}

			// Create the account
			ZimbraAdminAccount.GlobalAdmin()
					.soapSend("<CreateAccountRequest xmlns='urn:zimbraAdmin'>" + "<name>" + EmailAddress + "</name>"
							+ "<password>" + Password + "</password>" + prefs.toString() + "</CreateAccountRequest>");

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
			ZimbraPrefLocale = ZimbraAdminAccount.GlobalAdmin()
					.soapSelectValue("//admin:account/admin:a[@n='zimbraPrefLocale']", null);

			// If pref is not set, then use default
			if ((ZimbraPrefLocale == null) || ZimbraPrefLocale.trim().equals("")) {
				ZimbraPrefLocale = Locale.getDefault().toString();
			}

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

			logger.error("Unable to provision account: " + EmailAddress, e);
			ZimbraId = null;
			ZimbraMailHost = null;

		}

		return (this);
	}

	/**
	 * Authenticates the account (using SOAP client AuthRequest) Sets the authToken
	 */
	public ZimbraAccount authenticate() {
		try {
			soapSend("<AuthRequest xmlns='urn:zimbraAccount'>" + "<account by='name'>" + EmailAddress + "</account>"
					+ "<password>" + Password + "</password>" + "</AuthRequest>");
			MyAuthToken = soapSelectValue("//acct:authToken", null);
			soapClient.setAuthToken(MyAuthToken);
		} catch (HarnessException e) {
			logger.error("Unable to authenticate " + EmailAddress, e);
			soapClient.setAuthToken(null);
		}
		return (this);
	}

	/**
	 * Set this accounts Zimlet preferences table.
	 *
	 * These preferences are set per test class and specific to the test case
	 * features. These preferences are the *account* preferences set by the
	 * administrator, as compared to the *user* preferences set by the end-user.
	 *
	 *
	 * @param preferences
	 * @throws HarnessException
	 */
	public void setUserZimletPreferences(Map<String, String> preferences) throws HarnessException {
		if (preferences == null || preferences.isEmpty()) {
			return;
		}
		this.startingUserZimletPreferences = new HashMap<String, String>(preferences);
	}

	/**
	 * Compare this accounts preferences to another table.
	 *
	 * This is useful in determining if the account settings are correct for a
	 * particular test case
	 *
	 *
	 * @param preferences
	 * @return
	 */
	public boolean compareUserZimletPreferences(Map<String, String> preferences) {
		return (this.startingUserZimletPreferences.equals(preferences));
	}

	/**
	 * Use ModifyAccountRequest to modify this account per specified preferences
	 *
	 * @param preferences
	 * @return
	 * @throws HarnessException
	 */
	public ZimbraAccount modifyUserZimletPreferences(Map<String, String> preferences) throws HarnessException {

		if (preferences == null || preferences.isEmpty()) {
			// Nothing to modify
			logger.warn("modifyAccountPreferences called with null or empty preferences");
			return (this);
		}

		// Remember the specified preferences (useful for comparison)
		this.setUserZimletPreferences(preferences);

		for (Map.Entry<String, String> entry : preferences.entrySet()) {
			ExecuteHarnessMain.tracer
					.trace(EmailAddress + " zimletPreferences: " + entry.getKey() + "=" + entry.getValue());
		}

		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : preferences.entrySet()) {
			sb.append(String.format("<zimlet xmlns='' name='%s' presence='%s'/>", entry.getKey(), entry.getValue()));
		}

		this.soapSend(
				"<ModifyZimletPrefsRequest xmlns='urn:zimbraAccount'>" + sb.toString() + "</ModifyZimletPrefsRequest>");

		return (this);

	}

	/**
	 * Set this accounts preferences table.
	 *
	 * These preferences are set per test class and specific to the test case
	 * features. These preferences are the *account* preferences set by the
	 * administrator, as compared to the *user* preferences set by the end-user.
	 *
	 *
	 * @param preferences
	 * @throws HarnessException
	 */
	public void setAccountPreferences(Map<String, String> preferences) throws HarnessException {
		if (preferences == null || preferences.isEmpty()) {
			return;
		}
		this.startingAccountPreferences = new HashMap<String, String>(preferences);
	}

	/**
	 * Compare this accounts preferences to another table.
	 *
	 * This is useful in determining if the account settings are correct for a
	 * particular test case
	 *
	 *
	 * @param preferences
	 * @return
	 */
	public boolean compareAccountPreferences(Map<String, String> preferences) {
		return (this.startingAccountPreferences.equals(preferences));
	}

	/**
	 * Use ModifyAccountRequest to modify this account per specified preferences
	 *
	 * These preferences are the *account* preferences set by the administrator, as
	 * compared to the *user* preferences set by the end-user.
	 *
	 *
	 * @param preferences
	 * @return
	 * @throws HarnessException
	 */
	public ZimbraAccount modifyAccountPreferences(Map<String, String> preferences) throws HarnessException {

		if (preferences == null || preferences.isEmpty()) {
			// Nothing to modify
			logger.warn("modifyAccountPreferences called with null or empty preferences");
			return (this);
		}

		// Remember the specified preferences (useful for comparison)
		this.setAccountPreferences(preferences);

		// Build the SOAP <a/> elements from the map
		StringBuilder attributes = new StringBuilder();
		for (Map.Entry<String, String> entry : preferences.entrySet()) {
			attributes.append(String.format("<a n='%s'>%s</a>", entry.getKey(), entry.getValue()));
		}

		// Use the global admin to modify the account
		ZimbraAdminAccount.GlobalAdmin().soapSend("<ModifyAccountRequest xmlns='urn:zimbraAdmin'>" + "<id>"
				+ this.ZimbraId + "</id>" + attributes.toString() + "</ModifyAccountRequest>");

		// Set the flag so the account is reset for the next test
		this.accountIsDirty = true;

		return (this);

	}

	/**
	 * Modify user preferences using ModifyPrefsRequest with the default SERVER host
	 * destination type
	 *
	 * @param preferences
	 *            Preferences to be modified through SOAP
	 * @throws HarnessException
	 */
	public ZimbraAccount modifyUserPreferences(Map<String, String> preferences) {

		// Test Case Trace logging
		for (Map.Entry<String, String> entry : preferences.entrySet()) {
			ExecuteHarnessMain.tracer.trace(EmailAddress + " preferences: " + entry.getKey() + "=" + entry.getValue());
		}

		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : preferences.entrySet()) {
			sb.append(String.format("<pref name='%s'>%s</pref>", entry.getKey(), entry.getValue()));

			// If the locale preference is being changed, then remember the
			// value
			if (entry.getKey().equals("zimbraPrefLocale")) {
				setLocalePreference(entry.getValue());
			}

		}

		if (sb.length() <= 0)
			return (this); // Nothing to modify

		try {

			soapSend("<ModifyPrefsRequest xmlns='urn:zimbraAccount'>" + sb.toString() + "</ModifyPrefsRequest>");

			Element[] response = soapSelectNodes("//acct:ModifyPrefsResponse");
			if (response == null || response.length != 1)
				throw new HarnessException("Unable to modify preference " + soapLastResponse());

		} catch (HarnessException e) {
			logger.error("Unable to modify preference", e);
		}

		accountIsDirty = true;

		return (this);

	}

	/**
	 * Get all the available zimlets through SOAP from either client or server
	 *
	 * @param info
	 *            Information to look for
	 * @param destinationType
	 *            Type of SOAP destination, client or server
	 * @return String[] All available zimlets
	 * @throws HarnessException
	 */
	public String[] getAvailableZimlets() throws HarnessException {

		String[] output = null;

		try {
			this.soapSend("<GetInfoRequest xmlns='urn:zimbraAccount'>" + "</GetInfoRequest>");
			Element[] response = soapSelectNodes("//acct:GetInfoResponse/acct:attrs/acct:attr");

			StringBuilder temp = new StringBuilder();
			for (Element element : response) {
				if (element.getAttribute("name").equals("zimbraZimletAvailableZimlets")) {
					temp.append(element.getText().trim().replace("+", "")).append(";");
				}
			}

			temp.deleteCharAt(temp.length() - 1);
			output = temp.toString().split(";");

		} catch (ServiceException se) {
			throw new HarnessException("Getting service exception while getting available zimlets", se);
		}

		accountIsDirty = true;

		return output;

	}

	/**
	 * Get a user preference value
	 */
	public String getPreference(String pref) throws HarnessException {

		soapSend("<GetPrefsRequest xmlns='urn:zimbraAccount'>" + "<pref name='" + pref + "'/>" + "</GetPrefsRequest>");

		String value = soapSelectValue("//acct:pref[@name='" + pref + "']", null);
		return (value);
	}

	/**
	 * Get this Account's Locale Preference (zimbraPrefLocale)
	 *
	 * @return
	 * @throws HarnessException
	 */
	public Locale getLocalePreference() {
		return (I18N.getLocaleFromString(ZimbraPrefLocale));
	}

	protected void setLocalePreference(String locale) {
		ZimbraPrefLocale = locale;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((EmailAddress == null) ? 0 : EmailAddress.hashCode());
		return result;
	}

	/*
	 * If the account email addresses are equal, then the objects are equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ZimbraAccount other = (ZimbraAccount) obj;
		if (EmailAddress == null) {
			if (other.EmailAddress != null)
				return false;
		} else if (!EmailAddress.equals(other.EmailAddress))
			return false;
		return true;
	}

	/**
	 * Upload a file to the upload servlet
	 *
	 * @param filename
	 *            The full path to the upload file
	 * @return the attachment id, to be used with SaveDocumentRequest, for example
	 * @throws HarnessException
	 */
	public String uploadFile(String filename) throws HarnessException {
		RestUtil util = new RestUtil();
		util.setAuthentication(this);
		util.setPath("/service/upload");
		util.setQueryParameter("fmt", "raw");
		util.setUploadFile(new File(filename));
		if (util.doPost() != HttpStatus.SC_OK)
			throw new HarnessException("Unable to upload " + filename + " to " + util.getLastURI());

		String response = util.getLastResponseBody();

		// paw through the returned HTML and get the attachment id
		// example: loaded(<code>,'null','<id>')
		//
		int firstQuote = response.indexOf("','") + 3;
		int lastQuote = response.lastIndexOf("'");
		if (lastQuote == -1 || firstQuote == -1)
			throw new HarnessException("Attachment post failed, unexpected response: " + response);

		String id = response.substring(firstQuote, lastQuote);
		logger.info("Attachment ID: " + id);

		return (id);
	}

	/**
	 * Send a SOAP request from this account with the default mail server
	 * destination
	 *
	 * @param request
	 *            the SOAP request body (see ZimbraServer/docs/soap.txt)
	 * @return the response envelope
	 * @throws HarnessException
	 *             on failure
	 */
	public Element soapSend(String request) throws HarnessException {

		try {
			if (!(this instanceof ZimbraAdminAccount)) {
				ExecuteHarnessMain.tracer.trace(EmailAddress + " sends " + Element.parseXML(request).getName());
			}
		} catch (XmlParseException e) {
			ExecuteHarnessMain.tracer.warn("Unable to parse " + request);
		}

		return (soapClient.sendSOAP(request));
	}

	/**
	 * Match an xpath or regex from the last SOAP response if xpath == null, then
	 * use the root element value is element text. if attr != null, value is attr
	 * value if regex == null, return true if xpath matches. If regex != null, a
	 * regex to match against the value
	 *
	 * @param xpath
	 * @param attr
	 * @param regex
	 * @return
	 */
	public boolean soapMatch(String xpath, String attr, String regex) {

		// Find all nodes that match the expath
		Element[] elements = soapClient.selectNodes(xpath);

		// If regex == null, return true if xpath matched elements
		if (regex == null) {
			return (elements.length > 0);
		}

		// Loop through all xpath matches, looking for values that may match
		for (Element e : elements) {

			String value;
			if (attr == null) {
				value = e.getText();
			} else {
				value = e.getAttribute(attr, null);
			}

			if (value == null)
				continue; // No match in this element

			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(value);
			if (m.matches())
				return (true); // Otherwise, continue on next element
		}

		return (false); // Never found a match
	}

	/**
	 * Get a value from the last SOAP response
	 *
	 * @param xpath
	 * @param attr
	 * @return if attr == null, the element text. if attr != null, the attr text.
	 */
	public String soapSelectValue(String xpath, String attr) {
		return (soapClient.selectValue(xpath, attr, 1));
	}

	/**
	 * Get a list of elements from the last response that match an xpath
	 *
	 * @param xpath
	 * @return
	 */
	public Element[] soapSelectNodes(String xpath) {

		return (soapClient.selectNodes(xpath));
	}

	/**
	 * Get an element that matches the last response
	 *
	 * @param xpath
	 * @param index
	 *            1-based index if xpath matches multiple elements
	 * @return
	 */
	public Element soapSelectNode(String xpath, int index) {
		return (soapClient.selectNode(xpath, index));
	}

	/**
	 * Return the last SOAP response
	 *
	 * @return the last SOAP envelope in prettyPrint() string format
	 */
	public String soapLastResponse() {
		return (soapClient.responseEnvelope == null ? "null" : soapClient.responseEnvelope.prettyPrint());
	}

	public static class SoapClient {
		private Logger logger = LogManager.getLogger(SoapClient.class);

		protected String AuthToken = null;
		protected String ClientAuthToken = null;
		protected String SessionId = null;
		protected String clientSessionId = null;
		protected int SequenceNum = -1;

		protected Element requestEnvelope;
		protected Element responseEnvelope;
		protected Element requestContext;
		protected Element requestBody;

		private static final Element[] EMPTY_ELEMENT_ARRAY = new Element[0];

		protected URI mURI = null;

		protected SoapProtocol mSoapProto = null;
		protected ProxySoapHttpTransport mTransport = null;

		private static boolean isEasySslInitiated = false;

		/**
		 * Create a SOAP 1.2 client
		 */
		public SoapClient() {

			if (mSoapProto == null) {
				mSoapProto = SoapProtocol.Soap12;
			}

			if (!isEasySslInitiated) {
				SocketFactories.registerProtocols(true);
				isEasySslInitiated = true;
			}
		}

		/**
		 * Set the Zimbra AuthToken
		 *
		 * @param token
		 *            - use null to clear the context
		 * @return
		 */
		public String setAuthToken(String token) {

			if (token == null) {

				// If the authToken is null, then we need to
				// also clear the mTransport, which has a cookie
				// associated with the authToken.
				//
				// Clearing the current URI will have the same
				// effect.
				//
				mURI = null;

			}

			return (AuthToken = token);
		}

		/**
		 * Set the Zimbra ClientAuthToken
		 *
		 * @param token
		 *            - use null to clear the context
		 * @return
		 */
		public String setClientAuthToken(String token) {
			return (ClientAuthToken = token);
		}

		/**
		 * Set the Zimbra SessionId
		 *
		 * @param id
		 * @return
		 */
		public String setSessionId(String id) {
			return (SessionId = id);
		}

		/**
		 * Set the Zimbra SessionId
		 *
		 * @param id
		 * @return
		 */
		public String setClientSessionId(String id) {
			return (clientSessionId = id);
		}

		/**
		 * Set the Zimbra SequenceNum Use with the SessionId
		 *
		 * @param num
		 *            - default -1
		 * @return
		 */
		public int setSequenceNum(int num) {
			return (SequenceNum = num);
		}

		protected Element setContext(String token, String sessionId, int sequenceId) {
			if (token == null) {
				requestContext = null;
			} else {
				ZAuthToken zat = new ZAuthToken(null, token, null);
				if (sessionId == null) {
					requestContext = SoapUtil.toCtxt(mSoapProto, zat, null);
				} else {
					requestContext = SoapUtil.toCtxt(mSoapProto, zat, sessionId, sequenceId);
					if (sequenceId != -1) {
						Element e = requestContext.addElement("notify");
						e.addAttribute("seq", String.valueOf(sequenceId));
					}
				}

			}
			return (requestContext);
		}

		/**
		 * Send the specified Zimbra SOAP request to the specified host with the default
		 * SOAP context
		 *
		 * @param request
		 *            Request to be sent over SOAP
		 * @return
		 * @throws HarnessException
		 */
		public Element sendSOAP(String request) throws HarnessException {
			try {
				setContext(AuthToken, SessionId, SequenceNum);
				return (sendSOAP(requestContext, Element.parseXML(request)));
			} catch (XmlParseException e) {
				throw new HarnessException("Unable to parse request " + request, e);
			} catch (ContainerException e) {
				throw new HarnessException("Unable to parse request " + request, e);
			}
		}

		/**
		 * Send a Zimbra SOAP context/request to the host with the default SERVER type
		 * destination host
		 *
		 * @param context
		 * @param request
		 *            Request to be sent over SOAP
		 * @return
		 * @throws HarnessException
		 */
		public Element sendSOAP(Element context, Element request) throws HarnessException {

			setTransport(request);

			// Remember the context, request, envelope and response for logging purposes
			requestBody = request;
			requestEnvelope = mSoapProto.soapEnvelope(requestBody, context);

			try {
				responseEnvelope = mTransport.invokeRaw(requestEnvelope);
			} catch (IOException e) {
				throw new HarnessException("Unable to send SOAP to " + this.mURI.toString(), e);
			} catch (ServiceException e) {
				throw new HarnessException("Unable to send SOAP to " + this.mURI.toString(), e);
			}

			// Log the request/response
			logger.info("\n" + new Date() + " " + mURI.toString() + "\n---\n" + requestEnvelope.prettyPrint()
					+ "\n---\n" + responseEnvelope.prettyPrint() + "\n---\n");

			// Check the queue, if required
			doPostfixDelay();

			return (responseEnvelope);
		}

		/**
		 * For certain SOAP requests, such as SendMsgRequest, a message may wind up in
		 * the postfix queue. Check that the queue is empty before proceeding
		 *
		 * @throws HarnessException
		 */
		public void doPostfixDelay() throws HarnessException {

			// If disabled, don't do anything
			boolean enabled = ConfigProperties.getStringProperty("postfix.check", "true").equals("true");
			if (!enabled) {
				logger.debug("postfix.check was not true ... skipping queue check");
				return;
			}

			// Create an array of the requests that require a queue check
			final List<String> requests = new ArrayList<String>();
			requests.add("mail:SendMsgRequest");
			requests.add("mail:SendDeliveryReportRequest");
			requests.add("mail:CreateTaskRequest");
			requests.add("mail:ModifyTaskRequest");
			requests.add("mail:SetTaskRequest");
			requests.add("mail:SetAppointmentRequest");
			requests.add("mail:CreateAppointmentRequest");
			requests.add("mail:ModifyAppointmentRequest");
			requests.add("mail:CancelAppointmentRequest");
			requests.add("mail:ForwardAppointmentRequest");
			requests.add("mail:ForwardAppointmentInviteRequest");
			requests.add("mail:SendInviteReplyRequest");
			requests.add("mail:CreateAppointmentExceptionRequest");

			// If the current SOAP request matches any of the "queue" requests,
			// set matched=true
			for (String request : requests) {
				Element[] nodes = selectNodes(requestEnvelope, "//" + request);
				if (nodes.length > 0) {
					Stafpostqueue sp = new Stafpostqueue();
					sp.waitForPostqueue();
					break;
				}
			}

		}

		/**
		 * Return an array of elements from the last received SOAP response that match
		 * the xpath
		 *
		 * @param xpath
		 * @return
		 */
		public Element[] selectNodes(String xpath) {
			return (selectNodes(responseEnvelope, xpath));
		}

		/**
		 * Return the first matching element from the context that match the xpath
		 *
		 * @param context
		 * @param xpath
		 * @return
		 * @throws HarnessException
		 */
		public static Element selectNode(Element context, String xpath) {
			Element[] nodes = selectNodes(context, xpath);
			if (nodes.length == 0)
				return (null);
			return (nodes[0]);
		}

		/**
		 * Return an array of elements from the context that match the xpath
		 *
		 * @param context
		 * @param xpath
		 * @return An Array of elements that match the xpath (empty array if none)
		 * @throws HarnessException
		 * @throws HarnessException
		 */
		public static Element[] selectNodes(Element context, String xpath) {
			if (context == null)
				return (SoapClient.EMPTY_ELEMENT_ARRAY);

			try {
				org.dom4j.Element d4context = context.toXML();
				org.dom4j.XPath Xpath = d4context.createXPath(xpath);
				Xpath.setNamespaceURIs(getURIs());

				List<Element> zimbraElements = new ArrayList<Element>();

				for (Object o : Xpath.selectNodes(d4context)) {
					if (o instanceof org.dom4j.Element) {
						zimbraElements.add(Element.convertDOM((org.dom4j.Element) o));
					}
				}

				return (zimbraElements.toArray(new Element[zimbraElements.size()]));

			} catch (InvalidXPathException e) {
				LogManager.getRootLogger().error("Unable to select nodes", e);
				throw e;
			}
		}

		/**
		 * Return the element from the last received SOAP response that matches the
		 * xpath
		 *
		 * @param xpath
		 * @param index
		 *            - 1 based index
		 * @return
		 */
		public Element selectNode(String xpath, int index) {
			return (selectNode(responseEnvelope, xpath, index));
		}

		/**
		 * Return the element from context that matches the xpath
		 *
		 * @param context
		 * @param xpath
		 * @param index
		 * @return
		 */
		public Element selectNode(Element context, String xpath, int index) {
			Element[] nodes = selectNodes(context, xpath);
			if (nodes.length < index)
				return (null);
			return (nodes[index - 1]);
		}

		/**
		 * Return the
		 *
		 * @param xpath
		 * @param attr
		 * @param index
		 * @return
		 */
		public String selectValue(String xpath, String attr, int index) {
			return (selectValue(responseEnvelope, xpath, attr, index));
		}

		public String selectValue(Element context, String xpath, String attr, int index) {

			Element[] elements = null;
			if (xpath == null) {

				// no xpath - use the entire context
				elements = new Element[1];
				elements[0] = context;

			} else {

				// xpath specfied - only use the matching nodes
				elements = selectNodes(context, xpath);

			}

			// Make sure we have at least the specified index
			if (elements.length < index)
				return (null);

			// Only use the element corresponding to the specified index
			Element element = elements[index - 1];
			String value = null;

			if (attr == null) {
				value = element.getText();
			} else {
				value = element.getAttribute(attr, null);
			}

			return (value);
		}

		protected void setTransport(Element request) throws HarnessException {

			// Only set the transport if the URI changes
			if (setURI(request)) {

				synchronized (mSoapProto) {

					if (mTransport != null) {

						logger.debug("mTransport shutting down");

						mTransport.shutdown();
						mTransport = null;

					}

					mTransport = new ProxySoapHttpTransport(mURI.toString());

					logger.debug("mTransport pointing at " + mURI);

				}

			}
		}

		protected boolean setURI(Element request) throws HarnessException {

			String scheme = ConfigProperties.getStringProperty("server.scheme");
			String userInfo = null;
			String host = ConfigProperties.getStringProperty("server.host");
			String path = "/";
			String query = null;
			String fragment = null;
			int port = ExecuteHarnessMain.serverPort;

			String namespace = getNamespace(request);
			logger.debug("namespace: " + namespace);

			if (namespace.equals("urn:zimbraAdmin")) {
				// https://server.com:7071/service/admin/soap/
				scheme = "https";
				path = "/service/admin/soap/";
				port = ExecuteHarnessMain.adminPort;

			} else if (namespace.equals("urn:zimbraAccount")) {
				// http://server.com:80/service/soap/
				path = "/service/soap/";

			} else if (namespace.equals("urn:zimbraMail")) {
				// http://server.com:80/service/soap/
				path = "/service/soap/";

			} else if (namespace.equals("urn:zimbra")) {
				// http://server.com:80/service/soap/
				path = "/service/soap/";

			} else {
				throw new HarnessException("Unsupported qname: " + namespace + ". Need to implement setURI for it.");
			}

			try {
				URI uri = new URI(scheme, userInfo, host, port, path, query, fragment);
				logger.debug("scheme: " + scheme);
				logger.debug("userInfo: " + userInfo);
				logger.debug("Host: " + host);
				logger.debug("Port: " + ExecuteHarnessMain.adminPort);
				logger.debug("Path: " + path);
				logger.debug("Query: " + query);
				logger.debug("Fragment: " + fragment);

				if (uri.equals(mURI)) {
					return (false); // URI didn't change
				} else {
					mURI = uri;
					return (true);
				}
			} catch (URISyntaxException e) {
				throw new HarnessException("Unable to create SOAP URI", e);
			}
		}

		protected static final Pattern mNamespacePattern = Pattern.compile("(xmlns=\\\"([^\"]+)\\\")");

		protected String getNamespace(Element e) {
			Matcher matcher = mNamespacePattern.matcher(e.toString());
			while (matcher.find()) {
				return (matcher.group(2));
			}
			return (null);
		}
		/*
		 * protected Element[] getElementsFromPath(Element context, String path) {
		 * org.dom4j.Element d4context = context.toXML(); org.dom4j.XPath xpath =
		 * d4context.createXPath(path); xpath.setNamespaceURIs(getURIs());
		 * org.dom4j.Node node; List dom4jElements = xpath.selectNodes(d4context);
		 *
		 * List<Element> zimbraElements = new ArrayList<Element>(); Iterator iter =
		 * dom4jElements.iterator(); while (iter.hasNext()) { node =
		 * (org.dom4j.Node)iter.next(); if (node instanceof org.dom4j.Element) { Element
		 * zimbraElement = Element.convertDOM((org.dom4j.Element) node);
		 * zimbraElements.add(zimbraElement); } }
		 *
		 * Element[] retVal = new Element[zimbraElements.size()];
		 * zimbraElements.toArray(retVal); return retVal; }
		 *
		 */

		private static Map<String, String> mURIs = null;
		static {
			mURIs = new HashMap<String, String>();
			mURIs.put("zimbra", "urn:zimbra");
			mURIs.put("acct", "urn:zimbraAccount");
			mURIs.put("mail", "urn:zimbraMail");
			mURIs.put("offline", "urn:zimbraOffline");
			mURIs.put("admin", "urn:zimbraAdmin");
			mURIs.put("voice", "urn:zimbraVoice");
			mURIs.put("im", "urn:zimbraIM");
			mURIs.put("mapi", "urn:zimbraMapi");
			mURIs.put("sync", "urn:zimbraSync");
			mURIs.put("cs", "urn:zimbraCS");
			mURIs.put("test", "urn:zimbraTestHarness");
			mURIs.put("soap", "http://www.w3.org/2003/05/soap-envelope");
			mURIs.put("soap12", "http://www.w3.org/2003/05/soap-envelope");
			mURIs.put("soap11", "http://schemas.xmlsoap.org/soap/envelope/");
		}

		@SuppressWarnings({ "rawtypes" })
		private static Map getURIs() {
			return mURIs;
		}

	}

	public static class ProxySoapHttpTransport extends com.zimbra.common.soap.SoapTransport {

		private static final String X_ORIGINATING_IP = "X-Originating-IP";

		private int mRetryCount;
		private int mTimeout;
		private String mUri;
		private HttpClient mClient;
		public String mAuthToken = null;

		public String toString() {
			return "ProxySoapHttpTransport(uri=" + mUri + ")";
		}

		private static final HttpClientParams sDefaultParams = new HttpClientParams();
		static {
			// we're doing the retry logic at the SoapHttpTransport level, so
			// don't do it at the HttpClient level as well
			sDefaultParams.setParameter(HttpMethodParams.RETRY_HANDLER, new HttpMethodRetryHandler() {
				public boolean retryMethod(HttpMethod method, IOException exception, int executionCount) {
					return false;
				}
			});
		}

		/**
		 * Create a new SoapHttpTransport object for the specified URI. Supported
		 * schemes are http and https. The connection is not made until invoke or
		 * connect is called.
		 *
		 * Multiple threads using this transport must do their own synchronization.
		 */
		public ProxySoapHttpTransport(String uri) {
			this(uri, null, 0);
		}

		/**
		 * Create a new SoapHttpTransport object for the specified URI, with specific
		 * proxy information.
		 *
		 * @param uri
		 *            the origin server URL
		 * @param proxyHost
		 *            hostname of proxy
		 * @param proxyPort
		 *            port of proxy
		 */
		public ProxySoapHttpTransport(String uri, String proxyHost, int proxyPort) {
			this(uri, proxyHost, proxyPort, null, null);
		}

		/**
		 * Create a new SoapHttpTransport object for the specified URI, with specific
		 * proxy information including proxy auth credentials.
		 *
		 * @param uri
		 *            the origin server URL
		 * @param proxyHost
		 *            hostname of proxy
		 * @param proxyPort
		 *            port of proxy
		 * @param proxyUser
		 *            username for proxy auth
		 * @param proxyPass
		 *            password for proxy auth
		 */
		public ProxySoapHttpTransport(String uri, String proxyHost, int proxyPort, String proxyUser, String proxyPass) {
			super();
			mClient = new HttpClient(sDefaultParams);
			commonInit(uri);

			if (proxyHost != null && proxyHost.length() > 0 && proxyPort > 0) {
				mClient.getHostConfiguration().setProxy(proxyHost, proxyPort);
				if (proxyUser != null && proxyUser.length() > 0 && proxyPass != null && proxyPass.length() > 0) {
					mClient.getState().setProxyCredentials(new AuthScope(proxyHost, proxyPort),
							new UsernamePasswordCredentials(proxyUser, proxyPass));
				}
			}
		}

		/**
		 * Creates a new SoapHttpTransport that supports multiple connections to the
		 * specified URI. Multiple threads can call the invoke() method safely without
		 * synchronization.
		 *
		 * @param uri
		 * @param maxConnections
		 *            Note RFC2616 recommends the default of 2.
		 */
		public ProxySoapHttpTransport(String uri, int maxConnections, boolean connectionStaleCheckEnabled) {
			super();
			MultiThreadedHttpConnectionManager connMgr = new MultiThreadedHttpConnectionManager();
			connMgr.setMaxConnectionsPerHost(maxConnections);
			connMgr.setConnectionStaleCheckingEnabled(connectionStaleCheckEnabled);
			mClient = new HttpClient(sDefaultParams, connMgr);
			commonInit(uri);
		}

		/**
		 * Frees any resources such as connection pool held by this transport.
		 */
		public void shutdown() {
			HttpConnectionManager connMgr = mClient.getHttpConnectionManager();
			if (connMgr instanceof MultiThreadedHttpConnectionManager) {
				MultiThreadedHttpConnectionManager multiConnMgr = (MultiThreadedHttpConnectionManager) connMgr;
				multiConnMgr.shutdown();
			}
			mClient = null;
		}

		private void commonInit(String uri) {
			mUri = uri;
			mRetryCount = 3;
			setTimeout(0);
		}

		/**
		 * Gets the URI
		 */
		public String getURI() {
			return mUri;
		}

		/**
		 * The number of times the invoke method retries when it catches a
		 * RetryableIOException.
		 *
		 * <p>
		 * Default value is <code>3</code>.
		 */
		public void setRetryCount(int retryCount) {
			this.mRetryCount = retryCount;
		}

		/**
		 * Get the mRetryCount value.
		 */
		public int getRetryCount() {
			return mRetryCount;
		}

		/**
		 * The number of miliseconds to wait when connecting or reading during a invoke
		 * call.
		 * <p>
		 * Default value is <code>0</code>, which means no mTimeout.
		 */
		public void setTimeout(int timeout) {
			mTimeout = timeout;
			mClient.setConnectionTimeout(mTimeout);
			mClient.setTimeout(mTimeout);
		}

		/**
		 * Get the mTimeout value.
		 */
		public int getTimeout() {
			return mTimeout;
		}

		public Element invoke(Element document, boolean raw, boolean noSession, String requestedAccountId,
				String changeToken, String tokenType) throws SoapFaultException, IOException, HttpException {
			int statusCode = -1;

			PostMethod method = null;
			try {
				// the content-type charset will determine encoding used
				// when we set the request body
				method = new PostMethod(mUri);
				method.setRequestHeader("Content-Type", getRequestProtocol().getContentType());
				if (getClientIp() != null)
					method.setRequestHeader(X_ORIGINATING_IP, getClientIp());

				Element soapReq = generateSoapMessage(document, raw, noSession, requestedAccountId, changeToken,
						tokenType);
				String soapMessage = SoapProtocol.toString(soapReq, getPrettyPrint());
				method.setRequestBody(soapMessage);
				method.setRequestContentLength(EntityEnclosingMethod.CONTENT_LENGTH_AUTO);

				if (getRequestProtocol().hasSOAPActionHeader())
					method.setRequestHeader("SOAPAction", mUri);

				if (mAuthToken != null) {
					HttpState initialState = new HttpState();
					String mUriHost = "";
					try {
						mUriHost = (new URI(mUri)).getHost();
						Cookie authCookie = new Cookie(mUriHost, "ZM_AUTH_TOKEN", mAuthToken, "/", null, false);
						initialState.addCookie(authCookie);
						mClient.setState(initialState);
					} catch (URISyntaxException e) {
						System.err.println("A exception occurred " + e.getMessage());
					}

				}

				for (int attempt = 0; statusCode == -1 && attempt < mRetryCount; attempt++) {
					try {
						// execute the method.
						statusCode = mClient.executeMethod(method);
					} catch (HttpRecoverableException e) {
						if (attempt == mRetryCount - 1)
							throw e;
						System.err.println("A recoverable exception occurred, retrying." + e.getMessage());
					}
				}

				// Read the response body. Use the stream API instead of the
				// byte[] one
				// to avoid HTTPClient whining about a large response.
				byte[] responseBody = ByteUtil.getContent(method.getResponseBodyAsStream(),
						(int) method.getResponseContentLength());

				// Deal with the response.
				// Use caution: ensure correct character encoding and is not
				// binary data
				String responseStr = SoapProtocol.toString(responseBody);

				try {
					return parseSoapResponse(responseStr, raw);
				} catch (SoapFaultException x) {
					// attach request/response to the exception and rethrow for
					// downstream consumption
					x.setFaultRequest(soapMessage);
					x.setFaultResponse(responseStr);
					throw x;
				}
			} finally {
				// Release the connection.
				if (method != null)
					method.releaseConnection();
			}
		}

		@Override
		protected Element parseSoapResponse(String envelopeStr, boolean raw)
				throws SoapParseException, SoapFaultException {
			Element env;
			try {
				if (envelopeStr.trim().startsWith("<")) {
					logger.debug("envelopeStr: " + envelopeStr);
					env = Element.parseXML(envelopeStr);
				} else {
					env = Element.parseJSON(envelopeStr);
				}
			} catch (XmlParseException e) {
				throw new SoapParseException("unable to parse response", envelopeStr);
			}

			// if (mDebugListener != null)
			// mDebugListener.receiveSoapMessage(env);

			return raw ? env : extractBodyElement(env);
		}

		@Override
		public Element invoke(Element arg0, boolean arg1, boolean arg2, String arg3, String arg4, String arg5,
				NotificationFormat arg6, String arg7) throws IOException, HttpException, ServiceException {
			return null;
		}

		@Override
		public Future<HttpResponse> invokeAsync(Element arg0, boolean arg1, boolean arg2, String arg3, String arg4,
				String arg5, NotificationFormat arg6, String arg7, FutureCallback<HttpResponse> arg8)
				throws IOException {
			return null;
		}

	}

	public static void main(String[] args) throws HarnessException {

		// ConfigProperties.getStringProperty("server.host","zqa-062.eng.zimbra.com");
		String domain = ConfigProperties.getStringProperty("server.host");

		// Configure log4j using the basic configuration
		BasicConfigurator.configure();

		// Create a new account object
		ZimbraAccount account = new ZimbraAccount("foo" + System.currentTimeMillis(), domain);

		// Provision it on the server
		account.provision();

		// Get the SOAP authToken
		account.authenticate();

		// Send a basic SOAP request. Check the response.
		account.soapSend("<NoOpRequest xmlns='urn:zimbraMail'/>");
		if (!account.soapMatch("//mail:NoOpResponse", null, null))
			throw new HarnessException("NoOpRequest did not return NoOpResponse");

		// Add a message to the mailbox. Check the response
		account.soapSend("<SendMsgRequest xmlns='urn:zimbraMail'>" + "<m>" + "<e t='t' a='" + account.EmailAddress
				+ "'/>" + "<su>subject123</su>" + "<mp ct='text/plain'>" + "<content>content123</content>" + "</mp>"
				+ "</m>" + "</SendMsgRequest>");
		if (!account.soapMatch("//mail:SendMsgResponse", null, null))
			throw new HarnessException("SendMsgRequest did not return SendMsgResponse");

		logger.info("Done!");
	}
}