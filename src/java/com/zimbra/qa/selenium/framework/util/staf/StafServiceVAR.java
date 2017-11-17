/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2016 Synacor, Inc.
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

/**
 * Use the STAF FS service to execute a command
 *
 * @author Matt Rhoades
 *
 */
public class StafServiceVAR extends StafAbstract {

	public StafServiceVAR() {
		logger.info("new " + StafServiceVAR.class.getCanonicalName());

		// Command:
		// STAF local VAR GET SYSTEM VAR STAF/Config/Machine

		this.StafServer = "local";
		this.StafService = "VAR";
		this.StafParms = "GET SYSTEM VAR STAF/Config/Machine";

	}

	/**
	 * Execute the STAF request
	 * <p>
	 * For example, if "command = 'QUERY ENTRY /tmp'", then execute 'STAF server FS
	 * QUERY ENTRY /tmp'
	 * 
	 * @return
	 * @throws HarnessException
	 */
	public boolean execute(String command) throws HarnessException {
		StafParms = command;
		return (super.execute());
	}
}