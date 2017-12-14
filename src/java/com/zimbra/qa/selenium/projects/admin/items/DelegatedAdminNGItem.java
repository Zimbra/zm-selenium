package com.zimbra.qa.selenium.projects.admin.items;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;

public class DelegatedAdminNGItem implements IItem {

	protected String Account;
	protected String Domain;
	protected Boolean DelegatedAuth;
	protected Boolean EditFeatures;
	protected grantlimt Limit;
	protected String GrantCustomValue;

	public enum grantlimt{
		None,Custom,Unlimited
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createUsingSOAP(ZimbraAccount account) throws HarnessException {
		// TODO Auto-generated method stub

	}

	@Override
	public String prettyPrint() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setAccountAddress(String email) {
		Account = email;
	}

	public String getAccountAddress() {
		return (Account);
	}

	public void setDomain(String domain) {
		Domain = domain;
	}

	public String getDomain() {
		return (Domain);
	}

	public void setDelegatedAuth(Boolean IsDelegatedAuth) {
		DelegatedAuth = IsDelegatedAuth;
	}

	public Boolean getDelegatedAuth() {
		return (DelegatedAuth);
	}

	public void setEditFeatures(Boolean IsEditFeatures) {
		EditFeatures = IsEditFeatures;
	}

	public Boolean getEditFeatures() {
		return (EditFeatures);
	}

	public void setGrantLimit(grantlimt limitval) {
		Limit = limitval;
	}

	public grantlimt getGrantLimit() {
		return (Limit);
	}

	public void setGrantCustomValue(String CustomValue) {
		GrantCustomValue = CustomValue;
	}

	public String getGrantCustomValue() {
		return (GrantCustomValue);
	}
}
