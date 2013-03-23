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


public class BriefcaseItem extends ListItem{
	public BriefcaseItem() {
		super("listItemCore", "BriefcaseItem");
	}		
	public void zVerifyBFItemIsSelected(String briefcaseItemOrId) throws HarnessException  {
		String actual = ZObjectCore(briefcaseItemOrId, "isSelected");
		Assert.assertEquals("true", actual);
	}
}