/*
 * ***** BEGIN LICENSE BLOCK *****
 * 
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
 * 
 * ***** END LICENSE BLOCK *****
 */
//helper class for logging
package com.zimbra.qa.selenium.framework.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ZimbraSeleniumLogger {

	public static Logger mLog = LogManager.getLogger(new GetCurClass().getCurrentClass().getSimpleName());

	public static <T> void setmLog (Class <T> clazz) {
		if(clazz!=null)
		mLog = LogManager.getLogger(clazz);
	}

	private static class GetCurClass extends SecurityManager {
		private Class<?> getCurrentClass() {
			return getClassContext()[2];
		}
	}
}