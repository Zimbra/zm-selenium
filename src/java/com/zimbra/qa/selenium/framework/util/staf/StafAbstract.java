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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.ibm.staf.STAFException;
import com.ibm.staf.STAFHandle;
import com.ibm.staf.STAFMarshallingContext;
import com.ibm.staf.STAFResult;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

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

	protected static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hhmmss");
	protected static String currentDateTime = simpleDateFormat.format(new Date());

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
	 * Get the STAF command being used
	 * @return
	 */
	public String getStafCommand(String Server) {
		StringBuilder sb = new StringBuilder();
		sb.append("STAF ");
		sb.append(Server + " ");
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

		try {
			handle = new STAFHandle(StafAbstract.class.getName());

			String sStafLogFileName = "STAF_" + currentDateTime + ".log";
			String sStafLogFileFolderPath = "C:\\opt\\qa\\";
			String sStafLogFilePath = sStafLogFileFolderPath + "\\" + sStafLogFileName;
			Path pStafLogFilePath = Paths.get(sStafLogFileFolderPath, sStafLogFileName);
			File fStafLogFile = new File(sStafLogFilePath);
			List<String> lines = null;

			// Create STAF log folder and file
			File fStafLogFileFolder = new File(sStafLogFileFolderPath);
			if (!fStafLogFileFolder.exists()) {
				fStafLogFileFolder.mkdirs();
			}

			try {
				if (fStafLogFile.createNewFile()) {
					logger.info("Creating STAF log file: " + sStafLogFileName);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

	        try {

	        	if (AjaxCommonTest.totalZimbraServers < 1) {

	        		// Single Node Server

	        		if (StafParms.indexOf("postqueue") >=0 || StafParms.indexOf("zmmailbox") >=0 || StafParms.indexOf("zmtlsctl") >=0 || StafParms.indexOf("zmprov") >=0 || StafParms.indexOf(" zm") >=0) {

						StafServer = ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".server.host", ConfigProperties.getStringProperty("server.host"));

						lines = Arrays.asList("Executing STAF Command: " + StafParms + " on Single Node server (" + StafServer + ")\n");
						try {
							Files.write(pStafLogFilePath, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
						} catch (IOException e) {
							e.printStackTrace();
						}

						return runSTAFCommand(StafServer, handle);

					} else {

						// Localhost

						lines = Arrays.asList("Executing STAF Command: " + StafParms + " on localhost (" + StafServer + ")\n");
						try {
							Files.write(pStafLogFilePath, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return runSTAFCommand(StafServer, handle);
					}

	        	} else {
	        		
	        		// Multi Node Server

	        		if (StafParms.indexOf("postqueue") >=0 ) {

	        			// MTA hosts
	        			
						StafServer = ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".mta.host", ConfigProperties.getStringProperty("mta.host"));

						lines = Arrays.asList("Executing STAF Command: " + StafParms + " on MTA Host (" + StafServer + ")\n");
						try {
							Files.write(pStafLogFilePath, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return runSTAFCommand(StafServer, handle);

					} else if (StafParms.indexOf("zmmailbox") >=0 || StafParms.indexOf("zmtlsctl") >=0 || StafParms.indexOf("zmprov") >=0 || StafParms.indexOf(" zm") >=0) {

						// Store hosts

						Boolean storeCommandResponse = true;
						String storeServer = null;

						for (int i=0; i<AjaxCommonTest.storeServers.size(); i++) {

							storeServer = AjaxCommonTest.storeServers.get(i);

							lines = Arrays.asList("Executing STAF Command: " + StafParms + " on Store Host (" + storeServer + ")\n");
							try {
								Files.write(pStafLogFilePath, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
							} catch (IOException e) {
								e.printStackTrace();
							}

							storeCommandResponse = runSTAFCommand (storeServer, handle);
							if (!storeCommandResponse) {
								storeCommandResponse = false;
								break;
							}
						}
						return storeCommandResponse;

					} else {

						// Localhost

						lines = Arrays.asList("Executing STAF Command: " + StafParms + " on localhost (" + StafServer + ")\n");
						try {
							Files.write(pStafLogFilePath, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return runSTAFCommand(StafServer, handle);
					}
	        	}

			} finally {

				logger.info("STAF Response: " + StafResponse);

				lines = Arrays.asList("STAT Command: " +  StafParms + " Response: " + StafResponse + "\n\n");
				try {
					Files.write(pStafLogFilePath, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (handle != null )
					handle.unRegister();
			}

		} catch (STAFException e) {
        	throw new HarnessException("Error registering or unregistering with STAF, RC: " + e.rc, e);
		}
	}


	private boolean runSTAFCommand(String StafServer, STAFHandle handle) throws HarnessException {

		logger.info("STAF Command: " + getStafCommand(StafServer));

        StafResult = handle.submit2(StafServer, StafService, StafParms);

        if (StafResult == null)
        	throw new HarnessException("StafResult was null");

    	logger.info("STAF Response Code: "+ StafResult.rc);

    	if ( StafResult.rc == STAFResult.AccessDenied ) {
    		// Common error in WDC.  Log a helper message.
    		logger.error("On the server, use: staf local trust set machine *.eng.zimbra.com level 5");
    	}

    	if ( StafResult.rc != STAFResult.Ok ) {
    		throw new HarnessException("Invalid STAF response code ("+ StafResult.rc +"): "+ StafResult.result);
    	}

        if ( (StafResult.result != null) && (!StafResult.result.trim().equals("")) ) {

        	logger.debug(StafResult.result);

        	if ( STAFMarshallingContext.isMarshalledData(StafResult.result) ) {
        		STAFMarshallingContext mc = STAFMarshallingContext.unmarshall(StafResult.result);

        		// Get the entire response
        		StafResponse = STAFMarshallingContext.formatObject(mc);

        	} else {
        		StafResponse = StafResult.result;
        	}

        }

        return (StafResult.rc == STAFResult.Ok);

	}
}
