/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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

import com.ibm.staf.STAFException;
import com.ibm.staf.STAFHandle;
import com.ibm.staf.STAFMarshallingContext;
import com.ibm.staf.STAFResult;
import com.zimbra.qa.selenium.framework.util.HarnessException;

public class StafExecute extends StafAbstract {

	public StafExecute(String StafService, String StafParms) {
		logger.info("new " + StafExecute.class.getCanonicalName());

		this.StafServer = "local";
		this.StafService = StafService;
		this.StafParms = StafParms;
	}

	public boolean execute() throws HarnessException {

		STAFHandle handle = null;

		try {

			handle = new STAFHandle(StafAbstract.class.getName());

			try {

				logger.info("STAF Command: " + getStafCommand());

				StafResult = handle.submit2(StafServer, StafService, StafParms);

				if (StafResult == null)
					throw new HarnessException("StafResult was null");

				logger.info("STAF Response Code: " + StafResult.rc);

				if (StafResult.rc == STAFResult.AccessDenied) {
					logger.error("On the server, use: staf local trust set machine *.eng.vmware.com level 5");
				}

				if ((StafResult.result != null) && (!StafResult.result.trim().equals(""))) {

					logger.debug(StafResult.result);

					if (STAFMarshallingContext.isMarshalledData(StafResult.result)) {
						STAFMarshallingContext mc = STAFMarshallingContext.unmarshall(StafResult.result);

						// Get the entire response
						StafResponse = STAFMarshallingContext.formatObject(mc);

					} else {
						StafResponse = StafResult.result;
					}

				}

				return (StafResult.rc == STAFResult.Ok);

			} finally {
				logger.info("STAF execution reached finally block");
			}

		} catch (STAFException e) {
			throw new HarnessException("Error registering or unregistering with STAF, RC: " + e.rc, e);
		}
	}
}