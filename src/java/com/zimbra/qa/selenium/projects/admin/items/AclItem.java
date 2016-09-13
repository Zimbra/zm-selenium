/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.admin.items;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;

public class AclItem implements IItem {

	protected String RightName;
	protected String aclGranteeEmail;
	protected String aclGranteeID;
	
	public AclItem() {
		super();

			}

	@Override
	public void createUsingSOAP(ZimbraAccount account) throws HarnessException {
	}

	@Override
	public String prettyPrint() {
		return null;
	}

	public void setGranteeAccountEmail(String emailAddress) throws HarnessException {
		if ( (aclGranteeEmail != null) && (aclGranteeEmail.equals(emailAddress)) )
			return; // Nothing to update

		if ( (emailAddress == null) || (emailAddress.trim().length() == 0) )
			throw new HarnessException("emailAddress cannot be null or blank");

		// Need to get the AccountID
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
							"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
				+                "<account by='name'>"+ emailAddress +"</account>"
                +            "</GetAccountRequest>");
		String id = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:account", "id");

		aclGranteeEmail = emailAddress;
		aclGranteeID = id;
	}
	
	public String getGranteeAccountEmail() {
		return (aclGranteeEmail);
	}

	public void setTargetAccountId(String id) throws HarnessException {
		if ( (aclGranteeID != null) && (aclGranteeID.equals(id)) )
			return; // Nothing to update

		if ( (id == null) || (id.trim().length() == 0) )
			throw new HarnessException("id cannot be null or blank");

		// Need to get the AccountID
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
							"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
				+                "<account by='id'>"+ id +"</account>"
                +            "</GetAccountRequest>");
		String emailAddress = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:account", "name");

		aclGranteeEmail = emailAddress;
		aclGranteeID = id;

	}

	public String getTargetAccountId() {
		return (aclGranteeID);
	}
	
	public void setRightName(String name) {
		RightName = name;
	}
	
	public String getRightName() {
		return (RightName);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
