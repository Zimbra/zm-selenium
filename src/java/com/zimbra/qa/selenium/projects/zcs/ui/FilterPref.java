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
package com.zimbra.qa.selenium.projects.zcs.ui;


/**
 *This Class contains all the UI-level objects and methods for Preferences>Mail
 * Filters
 * 
 * @author Raja
 * 
 */
@SuppressWarnings("static-access")
public class FilterPref extends AppPage {

	/**
	 * Returns localised value of "all" or "any" menuItems in MailFilter
	 * dialog's "If [any|all]  of the following conditions are met:" menu.
	 * 
	 * @param allOrany
	 *            pass "all" or "any"
	 * @return
	 */
	public static String zGetLocalizedAllOrAny(String allOrany) {
		String str = localize(locator.filterCondition);
		String[] temp = str.split("#");
		String any = temp[1].split("\\|")[0];
		String all = temp[2].split("}")[0];
		if (allOrany.equals("any")) {
			return any;
		} else
			return all;
	}

}
