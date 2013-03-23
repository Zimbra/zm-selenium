/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011 VMware, Inc.
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
package com.zimbra.qa.selenium.projects.html2.clients;

import org.testng.Assert;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;


public class TaskItem extends ListItem{
	public TaskItem() {
		super("listItemCore", "TaskItem");
	} 
			
	
	public void zVerifyHasHighPriority(String messageOrId)  throws HarnessException  {
		String actual = ZObjectCore(messageOrId, "hasHighPriority");
		Assert.assertEquals("true", actual);
	}	
	public void zVerifyHasLowPriority(String messageOrId)  throws HarnessException  {
		String actual = ZObjectCore(messageOrId, "hasLowPriority");
		Assert.assertEquals("true", actual);
	}	
	public void zVerifyIsSelected(String messageOrId)  throws HarnessException  {
		String actual = ZObjectCore(messageOrId, "isSelected");
		Assert.assertEquals("true", actual);
	}
	
	public void zVerifyCurrentTaskBodyText(String requiredTxt)  throws HarnessException  {
		String actual =  ClientSessionFactory.session().selenium().call("msgBodyCore", "", "gettext", true, "", "");
		Assert.assertTrue(actual.indexOf(requiredTxt)>=0);
	}
	public void zVerifyCurrentTaskBodyHasImage()  throws HarnessException  {
		String actual =  ClientSessionFactory.session().selenium().call("msgBodyCore", "", "gethtml", true, "", "");		
		Assert.assertTrue(actual.indexOf("dfsrc=")>=0);
	}	
	public String zGetTaskBodyHTML()  throws HarnessException  {
		return ClientSessionFactory.session().selenium().call("msgBodyCore", "", "gethtml", true, "", "");		
	}	


		
}

