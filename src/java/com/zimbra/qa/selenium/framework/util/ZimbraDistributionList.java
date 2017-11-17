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

import org.apache.log4j.*;

public class ZimbraDistributionList {
	private static Logger logger = LogManager.getLogger(ZimbraDistributionList.class);

	public String ZimbraId = null;
	public String DisplayName = null;
	public String EmailAddress = null;
	public String Password = null;
	public ZimbraDomain Domain = null;

	public ZimbraDistributionList() {
		this(null, null);
	}

	/*
	 * Create an account with the email address <name>@<domain> The password is set
	 * to config property "adminPwd"
	 */
	public ZimbraDistributionList(String email, String password) {

		EmailAddress = email;
		if ((email == null) || (email.trim().length() == 0)) {
			EmailAddress = "dl" + ConfigProperties.getUniqueString() + "@"
					+ ConfigProperties.getStringProperty("testdomain", "testdomain.com");
		}

		Password = password;
		if ((password == null) || (password.trim().length() == 0)) {
			// password = ConfigProperties.getStringProperty("adminPwd", "test123");
			password = "test123";
		}
	}

	/**
	 * Creates the account on the ZCS using CreateAccountRequest
	 */
	public ZimbraDistributionList provision() {

		try {

			// Make sure domain exists
			Domain = new ZimbraDomain(EmailAddress.split("@")[1]);
			Domain.provision();

			// Create the account
			ZimbraAdminAccount.GlobalAdmin()
					.soapSend("<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>" + "<name>" + this.EmailAddress
							+ "</name>" + "<a n='description'>description" + ConfigProperties.getUniqueString() + "</a>"
							+ "</CreateDistributionListRequest>");

			ZimbraId = ZimbraAdminAccount.GlobalAdmin().soapSelectValue("//admin:dl", "id");

			// You can't add a logger to a DL
			// if ( ConfigProperties.getStringProperty("soap.trace.enabled",
			// "false").toLowerCase().equals("true") ) {
			//
			// ZimbraAdminAccount.GlobalAdmin().soapSend(
			// "<AddAccountLoggerRequest xmlns='urn:zimbraAdmin'>"
			// + "<account by='name'>"+ this.EmailAddress + "</account>"
			// + "<logger category='zimbra.soap' level='trace'/>"
			// + "</AddAccountLoggerRequest>");
			//
			// }

			// Need to sync the GSA
			Domain.syncGalAccount();

			// Need to flush galgroup cache after creating a new DL
			// (https://bugzilla.zimbra.com/show_bug.cgi?id=78970#c7)
			ZimbraAdminAccount.GlobalAdmin().soapSend("<FlushCacheRequest  xmlns='urn:zimbraAdmin'>"
					+ "<cache type='galgroup'/>" + "</FlushCacheRequest>");

		} catch (HarnessException e) {

			logger.error("Unable to provision DL: " + EmailAddress, e);
			ZimbraId = null;

		}

		return (this);
	}

	public ZimbraDistributionList addMember(ZimbraAccount account) throws HarnessException {
		return (addMember(account.EmailAddress));
	}

	public ZimbraDistributionList addMember(ZimbraDistributionList list) throws HarnessException {
		return (addMember(list.EmailAddress));
	}

	protected ZimbraDistributionList addMember(String email) throws HarnessException {

		ZimbraAdminAccount.GlobalAdmin().soapSend("<AddDistributionListMemberRequest xmlns='urn:zimbraAdmin'>" + "<id>"
				+ this.ZimbraId + "</id>" + "<dlm>" + email + "</dlm>" + "</AddDistributionListMemberRequest>");

		// Sync the GSA
		Domain.syncGalAccount();

		// Need to flush galgroup cache after creating a new DL
		// (https://bugzilla.zimbra.com/show_bug.cgi?id=78970#c7)
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<FlushCacheRequest  xmlns='urn:zimbraAdmin'>" + "<cache type='galgroup'/>" + "</FlushCacheRequest>");

		return (this);
	}

	public ZimbraDistributionList grantRight(ZimbraAccount grantee, String right) throws HarnessException {

		ZimbraAdminAccount.GlobalAdmin()
				.soapSend("<GrantRightRequest xmlns='urn:zimbraAdmin'>" + "<target by='name' type='dl'>"
						+ this.EmailAddress + "</target>" + "<grantee by='name' type='usr'>" + grantee.EmailAddress
						+ "</grantee>" + "<right>" + right + "</right>" + "</GrantRightRequest>");

		return (this);
	}
}