package com.zimbra.qa.selenium.projects.ajax.pages.chat;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;

public class BuddyItem implements IItem {

	protected String EmailAddress;
	protected String NickName;

	public void setEmailAddress(String email) {
		EmailAddress = email;
	}

	public String getEmailAddress() {
		return (EmailAddress);
	}

	public void setNickName(String nickname) {
		NickName = nickname;
	}

	public String getNickName() {
		return (NickName);
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

}
