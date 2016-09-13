/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.ibm.staf.STAFException;
import com.ibm.staf.STAFHandle;
import com.ibm.staf.STAFMarshallingContext;
import com.ibm.staf.STAFResult;
import com.zimbra.qa.selenium.framework.util.*;

/**
 * A wrapper class to create STAF classes from
 * @author Matt Rhoades
 *
 */
public class StafAbstract {
	protected static Logger logger = LogManager.getLogger(StafAbstract.class);

	// STAF command settings
	protected String StafServer = null;
	protected String StafService = null;
	protected String StafParms = null;

	// STAF response
	protected STAFResult StafResult = null;
	protected String StafResponse = null;


	public StafAbstract() {
		logger.info("new "+ StafAbstract.class.getCanonicalName());

		StafServer = ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".server.host", ConfigProperties.getStringProperty("server.host"));
		StafService = "PING";
		StafParms = "PING";

	}

	public STAFResult getSTAFResult() {
		return (StafResult);
	}

	public String getSTAFResponse() {
		return (StafResponse);
	}

	/**
	 * Get the STAF command being used
	 * @return
	 */
	public String getStafCommand() {
		StringBuilder sb = new StringBuilder();
		sb.append("STAF ");
		sb.append(StafServer + " ");
		sb.append(StafService + " ");
		sb.append(StafParms + " ");
		return (sb.toString());
	}

	/**
	 * After using execute(), get the STAF response
	 * @return
	 */
	public String getStafResponse() {
		return (StafResponse);
	}

	/**
	 * Execute the STAF request
	 * @return
	 * @throws HarnessException
	 */
	public boolean execute() throws HarnessException {

		STAFHandle handle = null;

		try
		{

			handle = new STAFHandle(StafAbstract.class.getName());

	        try
	        {
	        	if (StafParms.indexOf("postqueue") >=0 ) {
	    			if (StafServer.indexOf(".lab.zimbra.com") >=0 ) {
	    				StafServer = StafServer.replace(StafServer.substring(StafServer.indexOf(".")-10, StafServer.indexOf(".com")+4), ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".mta.host", ConfigProperties.getStringProperty("mta.host")));
	    			} else if (StafServer.indexOf(".eng.zimbra.com") >=0 ) {
	    				StafServer = StafServer.replace(StafServer.substring(StafServer.indexOf(".")-7, StafServer.indexOf(".com")+4), ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".mta.host", ConfigProperties.getStringProperty("mta.host")));
	    			}
	        	} else if (StafParms.indexOf("zmmemcachedctl") >=0 ) {
	    			if (StafServer.indexOf(".lab.zimbra.com") >=0 ) {
	    				StafServer = StafServer.replace(StafServer.substring(StafServer.indexOf(".")-10, StafServer.indexOf(".com")+4), ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".server.host", ConfigProperties.getStringProperty("server.host")));
	    			} else if (StafServer.indexOf(".eng.zimbra.com") >=0 ) {
	    				StafServer = StafServer.replace(StafServer.substring(StafServer.indexOf(".")-7, StafServer.indexOf(".com")+4), ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".server.host", ConfigProperties.getStringProperty("server.host")));
	    			}
	    		} else if (StafParms.indexOf("zmmailbox") >=0 || StafParms.indexOf("zmtlsctl") >=0 || StafParms.indexOf("zm") >=0 || StafParms.indexOf("zmprov") >=0 || StafParms.indexOf("SYSTEM") >=0) {
	    			if (StafServer.indexOf(".lab.zimbra.com") >=0 ) {
	    				StafServer = StafServer.replace(StafServer.substring(StafServer.indexOf(".")-10, StafServer.indexOf(".com")+4), ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".store.host", ConfigProperties.getStringProperty("store.host")));
	    			} else if (StafParms.indexOf(".eng.zimbra.com") >=0 ) {
	    				StafServer = StafServer.replace(StafServer.substring(StafServer.indexOf(".")-7, StafServer.indexOf(".com")+4), ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".store.host", ConfigProperties.getStringProperty("store.host")));
	    			}
	    		}

	        	logger.info("STAF Command: " + getStafCommand());

	            StafResult = handle.submit2(StafServer, StafService, StafParms);

	            if (StafResult == null)
	            	throw new HarnessException("StafResult was null");

            	logger.info("STAF Response Code: "+ StafResult.rc);

            	if ( StafResult.rc == STAFResult.AccessDenied ) {
            		// Common error in WDC.  Log a helper message.
            		logger.error("On the server, use: staf local trust set machine *.eng.vmware.com level 5");
            	}

            	if ( StafResult.rc != STAFResult.Ok ) {
            		throw new HarnessException("Invalid STAF response code ("+ StafResult.rc +"): "+ StafResult.result);
            	}

	            if ( (StafResult.result != null) && (!StafResult.result.trim().equals("")) ) {

	            	logger.debug(StafResult.result);

	            	if ( STAFMarshallingContext.isMarshalledData(StafResult.result) )
	            	{
	            		STAFMarshallingContext mc = STAFMarshallingContext.unmarshall(StafResult.result);

	            		// Get the entire response
	            		StafResponse = STAFMarshallingContext.formatObject(mc);

	            	}
	            	else
	            	{
	            		StafResponse = StafResult.result;
	            	}

	            }

	            return (StafResult.rc == STAFResult.Ok);

			} finally {

				logger.info("STAF Response: " + StafResponse);

				if (handle != null )
					handle.unRegister();

			}

		}
		catch (STAFException e)
		{
        	throw new HarnessException("Error registering or unregistering with STAF, RC: " + e.rc, e);
		}



	}
}
