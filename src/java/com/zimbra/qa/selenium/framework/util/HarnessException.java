/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.framework.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties.AppType;

public class HarnessException extends Exception {
	Logger logger = LogManager.getLogger(HarnessException.class);

	private static final long serialVersionUID = 4657095353247341818L;

	protected void resetAccounts() {
		logger.error("Reset AccountZWC due to exception");
		ZimbraAccount.ResetAccountZWC();
		ZimbraAccount.ResetAccountHTML();
		ZimbraAccount.ResetAccountZMC();
		ZimbraAdminAccount.ResetAccountAdminConsoleAdmin();
		if (ZimbraSeleniumProperties.getAppType() == AppType.ADMIN) {
			//ClientSessionFactory.session().selenium().refresh();
			//SleepUtil.sleep(10000);
		}
	}
	
	public HarnessException(String message) {
		super(message);
		logger.error(message, this);
		resetAccounts();
	}

	public HarnessException(Throwable cause) {
		super(cause);
		logger.error(cause.getMessage(), cause);
		resetAccounts();
	}

	public HarnessException(String message, Throwable cause) {
		super(message, cause);
		logger.error(message, cause);
		resetAccounts();
	}

}
