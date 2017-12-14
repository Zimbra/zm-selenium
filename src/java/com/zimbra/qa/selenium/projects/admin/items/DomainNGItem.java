package com.zimbra.qa.selenium.projects.admin.items;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;

public class DomainNGItem implements IItem {

	protected String GlobalAccountLimit;
	protected String DomainQuota;
	protected String CosName;
	protected String CosAccountLimit;
	public CosAction CosLimitAction;

	public enum CosAction{
		Add,Edit,Delete
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setGlobalAccountLimit(String accountlimit) {
		GlobalAccountLimit = accountlimit;
	}

	public String getGlobalAccountLimit() {
		return (GlobalAccountLimit);
	}

	public void setDomainQuota(String domainquota) {
		DomainQuota = domainquota;
	}

	public String getDomainQuota() {
		return (DomainQuota);
	}

	public void setCosName(String cosname) {
		CosName = cosname;
	}

	public String getCosName() {
		return (CosName);
	}

	public void setCosAccountLimit(String cosaccountlimit) {
		CosAccountLimit = cosaccountlimit;
	}

	public String getCosAccountLimit() {
		return (CosAccountLimit);
	}

	public void setCosAction(CosAction cosaction) {
		CosLimitAction = cosaction;
	}

	public CosAction getCosAction() {
		return (CosLimitAction);
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
}
