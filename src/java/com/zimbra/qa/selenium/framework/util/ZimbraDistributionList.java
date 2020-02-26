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

	public ZimbraDistributionList(String email, String password) {

		EmailAddress = email;
		if ((email == null) || (email.trim().length() == 0)) {
			EmailAddress = "tcdl" + ConfigProperties.getUniqueString() + "@"
					+ ConfigProperties.getStringProperty("testdomain");
		}

		Password = password;
		if ((password == null) || (password.trim().length() == 0)) {
			password = ConfigProperties.getStringProperty("accountPassword");
		}
	}

	public ZimbraDistributionList provision() {
		Domain = new ZimbraDomain(EmailAddress.split("@")[1]);

		try {
			// Create the account
			ZimbraAdminAccount.GlobalAdmin()
					.soapSend("<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>" + "<name>" + this.EmailAddress
							+ "</name>" + "<a n='description'>Created by Selenium automation</a>"
							+ "</CreateDistributionListRequest>");

			ZimbraId = ZimbraAdminAccount.GlobalAdmin().soapSelectValue("//admin:dl", "id");

			ZimbraAdminAccount.GlobalAdmin().soapSend("<FlushCacheRequest  xmlns='urn:zimbraAdmin'>"
					+ "<cache type='galgroup'/>" + "</FlushCacheRequest>");

		} catch (HarnessException e) {
			logger.error("Unable to provision DL: " + EmailAddress, e);
			ZimbraId = null;
		}

		// Sync the GAL to put the account into the list
		try {
			Domain.syncGalAccount();
		} catch (HarnessException e) {
			logger.error("Unable to sync GAL", e);
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

		Domain.syncGalAccount();

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