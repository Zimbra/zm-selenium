/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.core;

import java.util.HashMap;

public class FeatureBriefcaseTest extends AjaxCommonTest {

	public FeatureBriefcaseTest() {
		
	    super.startingPage = app.zPageMail;

	    super.startingAccountPreferences = new HashMap<String, String>() {
		
		private static final long serialVersionUID = -4746013883105449655L;
		
		{
			put("zimbraFeatureBriefcasesEnabled", "TRUE");
		}};
	}
}
