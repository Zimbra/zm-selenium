/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013 Zimbra Software, LLC.
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
package com.zimbra.qa.selenium.results;

import java.io.*;
import java.util.*;


public class BugStatus extends BugDataFile {

	public enum BugState {
		UNCONFIRMED,
		NEW,
		ASSIGNED,
		REOPENED,
		RESOLVED,
		VERIFIED,
		CLOSED
	}
	
	/**
	 * Return the current list of "Bug ID" to "Bug Status"
	 * @return
	 * @throws IOException
	 */
	public static Map<String, BugState> getStatusData() throws IOException {
		BugStatus engine = new BugStatus();
		return (engine.getData());
	}
	

	
	
	protected static final String DataFilename = "bugStatus.txt";

	
	protected BugStatus() {
		logger.info("new " + BugStatus.class.getCanonicalName());
	}


	protected Map<String, BugState> getData() throws IOException {
		
		// New datafile was found.  Clear the map
		Map<String, BugState> bugStatusMap = new HashMap<String, BugState>();
		
		// Read the file and build the map
		BufferedReader reader = null;
		String line;
		
		try {
			
			reader = new BufferedReader(new FileReader(getDatafile(DataFilename)));
			while ( (line=reader.readLine()) != null ) {

				// Example: 50208	RESOLVED
				String[] values = line.split("\\s");
				if ( values.length != 2 ) {
					logger.warn("bugStatus: invalid line: "+ line);
					continue;
				}
				
				String bugid = values[0];
				BugState bugState = BugState.valueOf(values[1]);
							
				bugStatusMap.put(bugid, bugState);
				logger.debug("bugStatus: put "+ line);
				
			}
			
		} finally {
			if ( reader != null )
				reader.close();
			reader = null;
		}


		return (bugStatusMap);
	}
	


}
