/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013 Zimbra Software, LLC.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.zcs.clients;

import org.testng.Assert;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;


public class MessageItem extends ListItem{
	public MessageItem() {
		super("listItemCore", "MessageItem");
	} 
	public void zExpand(String objNameOrId)  throws HarnessException  {
		ZObjectCore(objNameOrId, "expand");
	}		
	public void zVerifyIsUnRead(String objNameOrId)  throws HarnessException  {
		String actual = ZObjectCore(objNameOrId, "isUnread");
		Assert.assertEquals("true", actual);
	}
	public void zVerifyIsRead(String objNameOrId) throws HarnessException   {
		String actual = ZObjectCore(objNameOrId, "isRead");
		Assert.assertEquals("true", actual);
	}	
	public void zVerifyIsFlagged(String objNameOrId)  throws HarnessException  {
		String actual = ZObjectCore(objNameOrId, "isFlagged");
		Assert.assertEquals("true", actual);
	}
	public void zVerifyIsNotFlagged(String objNameOrId) throws HarnessException   {
		String actual = ZObjectCore(objNameOrId, "isNotFlagged");
		Assert.assertEquals("true", actual);
	}	

	public void zVerifyCurrentMsgHeaderText(String requiredTxt) throws HarnessException   {
		String actual = ClientSessionFactory.session().selenium().call("msgHeaderCore", "", "gettext", true, "", "");
		Assert.assertTrue(actual.indexOf(requiredTxt)>=0);
	}
	public String zGetCurrentMsgHeaderText()  throws HarnessException  {
		return ClientSessionFactory.session().selenium().call("msgHeaderCore", "", "gettext", true, "", "");
	}
	public void zVerifyCurrentMsgBodyText(String requiredTxt)  throws HarnessException  {
		String actual =  ClientSessionFactory.session().selenium().call("msgBodyCore", "", "gettext", true, "", "");
		Assert.assertTrue(actual.indexOf(requiredTxt)>=0);
	}
	public void zVerifyCurrentMsgBodyDoesNotHaveText(String requiredTxt)  throws HarnessException  {
		String actual =  ClientSessionFactory.session().selenium().call("msgBodyCore", "", "gettext", true, "", "");
		Assert.assertFalse(actual.indexOf(requiredTxt)>=0);
	}
	public String zGetCurrentMsgBodyText()  throws HarnessException  {
		return ClientSessionFactory.session().selenium().call("msgBodyCore", "", "gettext", true, "", "");
	}
	public void zVerifyCurrentMsgBodyHasImage()  throws HarnessException  {
		String actual =  ClientSessionFactory.session().selenium().call("msgBodyCore", "", "gethtml", true, "", "");		
		Assert.assertTrue(actual.indexOf("dfsrc=")>=0);
	}	
	public String zGetMsgBodyHTML()  throws HarnessException  {
		return ClientSessionFactory.session().selenium().call("msgBodyCore", "", "gethtml", true, "", "");		
	}
}

