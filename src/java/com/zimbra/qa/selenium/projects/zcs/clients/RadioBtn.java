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
package com.zimbra.qa.selenium.projects.zcs.clients;

import org.testng.Assert;

import com.zimbra.qa.selenium.framework.util.HarnessException;


public class RadioBtn extends ZObject{
	public RadioBtn() {
		super("radioBtnCore", "RadioBtn");
	} 
	public void zClick(String objNameOrId)  throws HarnessException  {
		ZObjectCore(objNameOrId, "click");
	}
	public void zClickInDlg(String objNameOrId)  throws HarnessException  {
		ZObjectCore(objNameOrId, "click", true,
				 "",  "dialog",  "",  "");
	}	
	public void zClickInDlgByName(String objNameOrId, String dialogName)  throws HarnessException  {
		ZObjectCore(objNameOrId, "click", true,
				 "",  "__dialogByName__"+dialogName,  "",  "");
	}	


	public void zVerifyIsChecked(String objNameOrId, String data)  throws HarnessException  {
		String actual = ZObjectCore(objNameOrId, "checked", true,
				 data,  "",  "",  "");
		Assert.assertEquals(actual, "true", objTypeName+"(" + objNameOrId
				+ ") doesn't exist in dialog or no dialog was found");
	}
	public void zExists(String objNameOrId) throws HarnessException   {
		String actual =ZObjectCore(objNameOrId, "exists", true,
				 "",  "",  "",  "");
		Assert.assertEquals("true", actual, objTypeName+"(" + objNameOrId
				+ ") Not Found.");
	}	
	public void zExistsInDlg(String objNameOrId) throws HarnessException   {
		String actual =ZObjectCore(objNameOrId, "exists", true,
				 "",  "dialog",  "",  "");
		Assert.assertEquals(actual, "true", objTypeName+"(" + objNameOrId
				+ ") doesn't exist in dialog or no dialog was found");
	}
	public void zExistsInDlgByName(String objNameOrId, String dialogName)  throws HarnessException  {
		String actual =ZObjectCore(objNameOrId, "exists", true,
				 "",  "__dialogByName__"+dialogName,  "",  "");
		Assert.assertEquals(actual, "true", objTypeName+"(" + objNameOrId
				+ ") doesn't exist in dialog("+dialogName+")");
	}	
	
}
