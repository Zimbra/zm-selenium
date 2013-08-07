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
import com.zimbra.qa.selenium.framework.util.SleepUtil;


public class ToastAlertMessage extends SelNGBase{
	public String zGetMsg() {
		return ClientSessionFactory.session().selenium().getText("id=z_toast");
	}
	
	public boolean zAlertMsgExists(String expectedMsg, String customMsg) {
	    String actMsg = "";
		for(int i =0; i < 15; i++) {
		    	actMsg = zGetMsg();
			if(actMsg.indexOf(expectedMsg) >=0)
				return true;
			SleepUtil.sleep(2000);
		}
		Assert.assertTrue(false, customMsg + "\nActual("+actMsg+") didnt contain Expected("+expectedMsg+")");
		return false;
	}	
}
