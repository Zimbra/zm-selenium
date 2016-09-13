/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.framework.util.staf;

import com.zimbra.qa.selenium.framework.util.HarnessException;

public class StafServicePROCESS extends StafAbstract {
	
	public static final int StafTimeoutMillisDefault = 100000;
	private int StafTimeoutMillis = StafTimeoutMillisDefault;

	public StafServicePROCESS() {
		logger.info("new "+ StafServicePROCESS.class.getCanonicalName());
		
		StafService = "PROCESS";
		StafParms = "START SHELL COMMAND \"ls\" RETURNSTDOUT RETURNSTDERR WAIT "+ StafTimeoutMillis;
		
	}
	
	public void setTimeout(int timeout) {
		StafTimeoutMillis = timeout;
	}
	
	public int getTimeout() {
		return (StafTimeoutMillis);
	}
	
	public int resetTimeout() {
		StafTimeoutMillis = StafTimeoutMillisDefault;
		return (StafTimeoutMillis);
	}
	
	/**
	 * Execute the STAF request
	 * @return 
	 * @throws HarnessException 
	 */
	public boolean execute(String command) throws HarnessException {
		if ( command.trim().startsWith("zm") ) {
			// For zm commands, run as zimbra user, and prepend the full path
			StafParms = String.format("START SHELL COMMAND \"su - zimbra -c '/opt/zimbra/bin/%s'\" RETURNSTDOUT RETURNSTDERR WAIT %d", command, StafTimeoutMillis);
		} else {
			StafParms = String.format("START SHELL COMMAND \"%s\" RETURNSTDOUT RETURNSTDERR WAIT %d", command, StafTimeoutMillis);
		}
		return (super.execute());

	}
}
