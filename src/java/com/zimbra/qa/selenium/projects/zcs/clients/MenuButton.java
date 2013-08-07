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

public class MenuButton extends SelNGBase {
	public static void zClick(String menuBtnNameOrId) throws HarnessException   {
		MenuButtonCore(menuBtnNameOrId, "click");
	}	
	public static void zExists(String menuBtnNameOrId) throws HarnessException   {
		String actual = MenuButtonCore(menuBtnNameOrId, "exists");
		Assert.assertEquals(actual, "true", "Menu(" + menuBtnNameOrId
				+ ") Not Found.");
	}
	public static void zNotExists(String menuBtnNameOrId) throws HarnessException   {
		String actual = MenuButtonCore(menuBtnNameOrId, "notexists");
		Assert.assertEquals(actual, "true", "Menu(" + menuBtnNameOrId
				+ ") Found, which should not be present.");
	}
	public static void zIsEnabled(String menuBtnNameOrId) throws HarnessException   {
		String actual = MenuButtonCore(menuBtnNameOrId, "enabled");
		Assert.assertEquals(actual, "true", "Menu(" + menuBtnNameOrId
				+ ") is disabled");
	}
	public static void zIsDisabled(String menuBtnNameOrId) throws HarnessException   {
		String actual = MenuButtonCore(menuBtnNameOrId, "disabled");
		Assert.assertEquals(actual, "true", "Menu(" + menuBtnNameOrId
				+ ") is enabled(instead of disabled)");
	}	
	private static String MenuButtonCore(String menuBtnNameOrId, String action, String param1, String param2)  throws HarnessException  {
		String rc = "false";
		rc = ClientSessionFactory.session().selenium().call("buttonMenuCore",  menuBtnNameOrId, action, true, param1, param2);
		return rc;
	}
	private static String MenuButtonCore(String menuBtnNameOrId, String action)  throws HarnessException  {
		return MenuButtonCore(menuBtnNameOrId, action,"", "");
	}
}
